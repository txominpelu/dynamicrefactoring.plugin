package dynamicrefactoring.plugin.xml.classifications.imp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.writer.JDOMXMLRefactoringWriterImp;
import dynamicrefactoring.writer.XMLRefactoringWriterException;

/**
 * Almacen con todas las clasificaciones leidas del fichero de configuracion xml
 * y las refactorizaciones. Proporciona metodos para acceder a las
 * clasificaciones y refactorizaciones del plugin y para modificar los y guardar
 * los cambios en sus ficheros xml correspondientes.
 * 
 * @author imediava
 * 
 */
public class PluginCatalog extends AbstractCatalog implements ClassificationsCatalog {

	private static PluginCatalog instance;

	public static final String SCOPE_CLASSIFICATION = "Scope";

	/**
	 * Crea el almacen y lee las clasificaciones del fichero de clasificaciones
	 * xml.
	 */
	private PluginCatalog() {
		super(
				AbstractCatalog
						.getClassificationsFromFile(RefactoringConstants.CLASSIFICATION_TYPES_FILE),
				AbstractCatalog.getRefactoringsFromDir(RefactoringPlugin
						.getDynamicRefactoringsDir()));
		Collections.sort(new ArrayList<Classification>(super
				.getAllClassifications()));
	}

	protected PluginCatalog(Set<Classification> classifSet,
			Set<DynamicRefactoringDefinition> refactSet) {
		super(classifSet, refactSet);
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
		final Collection<DynamicRefactoringDefinition> refactsToUpdate = getRefactoringBelongingTo(
				classifName, name);
		super.removeCategory(classifName, name);
		for (DynamicRefactoringDefinition refact : refactsToUpdate) {
			saveRefactoringToFile(getRefactoring(refact.getName()));
		}
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
		for (DynamicRefactoringDefinition refact : getRefactoringBelongingTo(
				classifName, newName)) {
			saveRefactoringToFile(refact);
		}
	}

	/**
	 * Se encarga de la actualizacion del fichero xml de la refact.
	 */
	@Override
	public void addCategoryToRefactoring(String refactName,
			String classificationName, String categoryName) {
		super.addCategoryToRefactoring(refactName, classificationName,
				categoryName);
		saveRefactoringToFile(getRefactoring(refactName));
	}

	/**
	 * Actualiza el contenido del fichero de configuracion de clasificaciones de
	 * acuerdo con el contenido de las clasificaciones del catalogo.
	 */
	private final void updateClassificationsFile() {
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
	 * Guarda el fichero xml de definicion de la refactorizacion en la ruta que
	 * le corresponde.
	 * 
	 * @param refact
	 *            definicion de la refactorizacion a guardar
	 */
	private void saveRefactoringToFile(DynamicRefactoringDefinition refact) {
		try {
			new JDOMXMLRefactoringWriterImp(refact)
					.writeRefactoring(getDirectoryToSaveRefactoringFile(refact
							.getName()));
		} catch (XMLRefactoringWriterException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Obtiene ruta donde se guarda el fichero de definicion de la
	 * refactorizacion pasada.
	 * 
	 * @param refactName
	 *            nombre de la refactorizacion
	 * @return ruta donde se guarda la definicion de la refactorizacion
	 */
	public static String getXmlRefactoringDefinitionFilePath(String refactName) {
		return getDirectoryToSaveRefactoringFile(refactName).getPath()
				+ File.separator + refactName
				+ RefactoringConstants.FILE_EXTENSION;

	}

	/**
	 * Obtiene un fichero cuya ruta sera la del directorio donde se guardara el
	 * fichero de definicion de la refactorizacion.
	 * 
	 * @param refact
	 *            nombre de la refactorizacion
	 * @return fichero con la ruta donde se guardara la definicion de la
	 *         refactorizacion
	 */
	private static File getDirectoryToSaveRefactoringFile(String refactName) {
		return new File(RefactoringPlugin.getDynamicRefactoringsDir()
				+ File.separator + refactName + File.separator);
	}

	/**
	 * Obtiene la instancia del almacen con las clasificaciones disponibles.
	 * 
	 * @return instancia del almacen
	 */
	public static PluginCatalog getInstance() {
		if (instance == null) {
			instance = new PluginCatalog();
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
		List<DynamicRefactoringDefinition> refactoringsToUpdate = new ArrayList<DynamicRefactoringDefinition>();
		for (Category category : getClassification(clasifName).getCategories()) {
			for (DynamicRefactoringDefinition refact : getRefactoringBelongingTo(
					clasifName, category.getName())) {
				refactoringsToUpdate.add(refact);
			}
		}
		super.renameClassification(clasifName, clasifNewName);
		for (DynamicRefactoringDefinition refact : refactoringsToUpdate) {
			saveRefactoringToFile(getRefactoring(refact.getName()));
		}
		updateClassificationsFile();
	}

	@Override
	public void addClassification(Classification classification) {
		super.addClassification(classification);
		updateClassificationsFile();
	}

}
