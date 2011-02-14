package dynamicrefactoring.plugin.xml.classifications.imp;

import java.util.Set;

import javax.xml.bind.ValidationException;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
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
	 * Obtiene el conjunto de todos las clasificaciones disponibles.
	 * 
	 * @return todas las clasificaciones leidas del fichero xml.
	 */
	public Set<Classification> getAllClassifications() {
		return classifications;
	}

}
