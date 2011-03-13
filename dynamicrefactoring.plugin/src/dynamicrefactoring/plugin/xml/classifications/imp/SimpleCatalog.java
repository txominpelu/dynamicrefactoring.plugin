package dynamicrefactoring.plugin.xml.classifications.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Almacen con todas las clasificaciones leidas del fichero de configuracion
 * xml.
 * 
 * @author imediava
 * 
 */
public class SimpleCatalog extends AbstractCatalog implements
		Catalog {

	private static SimpleCatalog instance;

	public static final String SCOPE_CLASSIFICATION = "Scope";

	/**
	 * Crea el almacen y lee las clasificaciones del fichero de clasificaciones
	 * xml.
	 */
	private SimpleCatalog() {
		super(
				AbstractCatalog
						.getClassificationsFromFile(RefactoringConstants.CLASSIFICATION_TYPES_FILE),
				AbstractCatalog.getRefactoringsFromDir(RefactoringPlugin
						.getDynamicRefactoringsDir()));
		Collections.sort(new ArrayList<Classification>(super
				.getAllClassifications()));
	}
	
	protected SimpleCatalog(Set<Classification> classifSet,
			Set<DynamicRefactoringDefinition> refactSet){
		super(classifSet, refactSet);
	}

	/**
	 * Obtiene la instancia del almacen con las clasificaciones disponibles.
	 * 
	 * @return instancia del almacen
	 */
	public static SimpleCatalog getInstance() {
		if (instance == null) {
			instance = new SimpleCatalog();
		}
		return instance;
	}

	/**
	 * Determina si existe una clasificación que contenga a la categoria
	 * indicada por parámetro.
	 * 
	 * @param cat
	 *            categoria
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	public boolean containsCategoryClassification(Category cat) {
		return containsClassification(cat.getParent())
				&& getClassification(cat.getParent()).containsCategory(cat);
	}

}
