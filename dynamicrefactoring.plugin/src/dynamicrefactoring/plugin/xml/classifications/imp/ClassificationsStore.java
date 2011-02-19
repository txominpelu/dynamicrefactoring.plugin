package dynamicrefactoring.plugin.xml.classifications.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.ValidationException;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Almacen con todas las clasificaciones leidas del fichero de configuracion
 * xml.
 * 
 * @author imediava
 * 
 */
public class ClassificationsStore {

	private static ClassificationsStore instance;

	private Set<Classification> classifications;

	public static final String SCOPE_CLASSIFICATION = "scope";

	/**
	 * Crea el almacen y lee las clasificaciones del fichero de clasificaciones
	 * xml.
	 */
	private ClassificationsStore() {
		try {
			classifications = ClassificationsReaderFactory
					.getReader(
							ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER)
					.readClassifications(
							RefactoringConstants.CLASSIFICATION_TYPES_FILE);
			Collections.sort(new ArrayList<Classification>(classifications));
		} catch (ValidationException e) {
			Throwables.propagate(e);
		}
	}

	/**
	 * Obtiene la instancia del almacen con las clasificaciones disponibles.
	 * 
	 * @return instancia del almacen
	 */
	public static ClassificationsStore getInstance() {
		if (instance == null) {
			instance = new ClassificationsStore();
		}
		return instance;
	}

	/**
	 * Obtiene la clasificación disponible que concuerda con el nombre
	 * pasado por parámetro. 
	 * 
	 * @param name nombre de la clasificación a obtener
	 * @return la clasificación. En caso de no existir, devuelve nulo
	 */
	public Classification getClassification(String name){
		for(Classification c : classifications){
			if(c.getName().equalsIgnoreCase(name))
				return c;
		}
		return null;
	}
	
	/**
	 * Obtiene el conjunto de todos las clasificaciones disponibles.
	 * 
	 * @return todas las clasificaciones leidas del fichero xml.
	 */
	public Set<Classification> getAllClassifications() {
		return classifications;
	}
	
	/**
	 * Determina si existe una clasificación con el nombre
	 * pasado por parámetro. 
	 * 
	 * @param name nombre de la clasificación
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	public boolean containsClassification(String name){
		if(getClassification(name)==null)
			return false;
		return true;
	}
	
	/**
	 * Determina si existe una clasificación que contenga 
	 * a la categoria indicada por parámetro.
	 * @param cat categoria
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	public boolean containsCategoryClassification(Category cat){
		if(containsClassification(cat.getParent()) && 
		   getClassification(cat.getParent()).containsCategory(cat))
		   return true;
		return false;
	}

}
