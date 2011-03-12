package dynamicrefactoring.plugin.xml.classifications.imp;

import java.util.ArrayList;
import java.util.Collections;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

/**
 * Almacen con todas las clasificaciones leidas del fichero de configuracion
 * xml.
 * 
 * @author imediava
 * 
 */
public class ClassificationsStore extends ClassificationsManager implements ClassificationsCatalog{

	private static ClassificationsStore instance;


	public static final String SCOPE_CLASSIFICATION = "Scope";

	/**
	 * Crea el almacen y lee las clasificaciones del fichero de clasificaciones
	 * xml.
	 */
	private ClassificationsStore() {
			super(RefactoringConstants.CLASSIFICATION_TYPES_FILE);
			Collections.sort(new ArrayList<Classification>(super.getAllClassifications()));
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
	 * Determina si existe una clasificación que contenga 
	 * a la categoria indicada por parámetro.
	 * @param cat categoria
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	public boolean containsCategoryClassification(Category cat){
		return containsClassification(cat.getParent()) && 
		   getClassification(cat.getParent()).containsCategory(cat);
	}

}
