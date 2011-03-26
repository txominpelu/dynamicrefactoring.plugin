package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.XMLRefactoringsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

/**
 * Almacen con todas las clasificaciones leidas del fichero de configuracion xml
 * y las refactorizaciones. Proporciona metodos para acceder a las
 * clasificaciones y refactorizaciones del plugin y para modificar los y guardar
 * los cambios en sus ficheros xml correspondientes.
 * 
 * @author imediava
 * 
 */
public final class PluginClassificationsCatalog extends AbstractCatalog implements
		ClassificationsCatalog {

	private static PluginClassificationsCatalog instance;

	public static final String SCOPE_CLASSIFICATION = "Scope";

	/**
	 * Crea el almacen y lee las clasificaciones del fichero de clasificaciones
	 * xml.
	 */
	private PluginClassificationsCatalog() {
		super(
				AbstractCatalog
						.getClassificationsFromFile(RefactoringConstants.CLASSIFICATION_TYPES_FILE),
				XMLRefactoringsCatalog.getInstance());
		Collections.sort(new ArrayList<Classification>(super
				.getAllClassifications()));
	}

	protected PluginClassificationsCatalog(Set<Classification> classifSet,
			RefactoringsCatalog refactCatalog) {
		super(classifSet, refactCatalog);
	}

	@Override
	public void removeClassification(String classification) {
		// No se puede eliminar la clasificacion Scope
		Preconditions.checkArgument(!classification
				.equals(SCOPE_CLASSIFICATION));
		super.removeClassification(classification);
		updateClassificationsFile();
	}

	/**
	 * Tambien se ocupa de actualizar el fichero de clasificaciones a parte de
	 * la funcionalidad de gestion de clasificaciones en memoria.
	 */
	@Override
	public void addCategoryToClassification(String classifName, String name) {
		super.addCategoryToClassification(classifName, name);
		updateClassificationsFile();
	}

	/**
	 * Tambien se ocupa de actualizar el fichero de clasificaciones y de los
	 * ficheros xml de refactorizaciones a parte de la funcionalidad de gestion
	 * en memoria.
	 */
	@Override
	public void removeCategory(String classifName, String name) {
		super.removeCategory(classifName, name);
		updateClassificationsFile();
	}

	/**
	 * Se encarga de renombrar la categoria y actualizar las definiciones en
	 * ficheros xml de las refactorizaciones que pertenecen a la categoria.
	 * 
	 */
	@Override
	public void renameCategory(String classifName, String oldName,
			String newName) {
		super.renameCategory(classifName, oldName, newName);
		updateClassificationsFile();
	}

	/**
	 * Actualiza el contenido del fichero de configuracion de clasificaciones de
	 * acuerdo con el contenido de las clasificaciones del catalogo.
	 */
	private void updateClassificationsFile() {
		try {
			JAXBClassificationsWriter.getInstance().saveClassificationsToXml(
					getAllClassifications(),
					RefactoringConstants.CLASSIFICATION_TYPES_FILE);
		} catch (FileNotFoundException e) {
			// El fichero debe existir sino seria error de programacion
			throw Throwables.propagate(e);
		}
	}




	/**
	 * Obtiene la instancia del almacen con las clasificaciones disponibles.
	 * 
	 * @return instancia del almacen
	 */
	public static PluginClassificationsCatalog getInstance() {
		if (instance == null) {
			instance = new PluginClassificationsCatalog();
		}
		return instance;
	}

	/**
	 * Determina si existe una clasificacion que contenga a la categoria
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

	@Override
	public void renameClassification(String clasifName, String clasifNewName) {
		Preconditions.checkArgument(!clasifName.equals(SCOPE_CLASSIFICATION));
		super.renameClassification(clasifName, clasifNewName);
		updateClassificationsFile();
	}

	@Override
	public void addClassification(Classification classification) {
		super.addClassification(classification);
		updateClassificationsFile();
	}
	
	@Override
	public void setMultiCategory(String classification, boolean isMultiCategory) {
		super.setMultiCategory(classification, isMultiCategory);
		updateClassificationsFile();
	}
	
	@Override
	public void setDescription(String classification, String description) {
		super.setDescription(classification, description);
		updateClassificationsFile();
	}

}
