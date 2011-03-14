package dynamicrefactoring.plugin.xml.classifications.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsReaderFactory.ClassificationsReaderTypes;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.util.io.FileManager;

public class PluginCatalogTest {

	private static final String REFACT_REPO_PATH = FilenameUtils
			.separatorsToSystem("testdata/"
					+ "dynamicrefactoring/plugin/xml/classifications/imp/"
					+ "ClassificationsStoreTest/");
	private static final String MI_REFACT1_NAME = "MiRefact1-MiCategoria1";
	private static final String NEW_NAME = "NewName";
	private static final String MI_CATEGORIA1 = "MiCategoria1";
	private static final String MI_CLASIFICACION1 = "MiClasificacion1";
	private Catalog catalog;

	@Before
	public void setUp() throws Exception {
		FileUtils.deleteDirectory(new File(RefactoringPlugin
				.getDynamicRefactoringsDir()));

		FileManager.copyBundleDirToFileSystem(REFACT_REPO_PATH,
				RefactoringPlugin.getDefault().getStateLocation().toOSString()
						+ File.separator + "test" + File.separator);

		FileUtils.copyDirectory(new File(RefactoringPlugin.getDefault()
				.getStateLocation().toOSString()
				+ File.separator + "test" + File.separator + REFACT_REPO_PATH),
				new File(RefactoringPlugin.getDefault().getStateLocation()
						.toOSString()));

		catalog = new PluginCatalog(
				AbstractCatalog.getClassificationsFromFile(RefactoringPlugin
						.getDefault().getStateLocation().toOSString()
						+ File.separator
						+ "test"
						+ File.separator
						+ REFACT_REPO_PATH + "classifications.xml"),
				AbstractCatalog.getRefactoringsFromDir(RefactoringPlugin
						.getDynamicRefactoringsDir()));
	}

	/**
	 * Restaura el estado del repositorio de refactorizaciones.
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@After
	public void tearDown() throws IllegalStateException, IOException {
		FileUtils.deleteDirectory(new File(RefactoringPlugin.getDefault()
				.getStateLocation().toOSString()
				+ "/test/"));
		FileUtils.deleteDirectory(new File(RefactoringPlugin
				.getDynamicRefactoringsDir()));
		FileManager.copyBundleDirToFileSystem(
				RefactoringPlugin.DYNAMIC_REFACTORINGS_FOLDER_NAME,
				RefactoringPlugin.getDefault().getStateLocation().toOSString());
		FileManager.copyResourceToDir("/Classification/classifications.xml",
				RefactoringPlugin.getDefault().getStateLocation().toOSString());
	}

	/**
	 * Comprueba que al renombrar una categoria en una clasificacion las
	 * refactorizaciones que pertenecen a esa categoria son actualizadas (en el
	 * catalogo y en el fichero xml).
	 * 
	 * Ademas el fichero de clasificaciones tambien debe ser actualizado.
	 * 
	 * @throws ValidationException
	 */
	@Test
	public final void testRenameCategory() throws ValidationException {
		Set<Category> expectedClassificationCategories = new HashSet<Category>();
		expectedClassificationCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedClassificationCategories.add(new Category(MI_CLASIFICACION1,
				NEW_NAME));

		Set<Category> expectedRefactoringCategories = catalog.getRefactoring(
				MI_REFACT1_NAME).getCategories();
		expectedRefactoringCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedRefactoringCategories.add(new Category(MI_CLASIFICACION1,
				NEW_NAME));

		catalog.renameCategory(MI_CLASIFICACION1, MI_CATEGORIA1, NEW_NAME);
		assertEquals(expectedClassificationCategories, catalog
				.getClassification(MI_CLASIFICACION1).getCategories());
		assertEquals(expectedRefactoringCategories,
				catalog.getRefactoring(MI_REFACT1_NAME).getCategories());

		assertEquals(
				expectedRefactoringCategories,
				new JDOMXMLRefactoringReaderImp()
						.getDynamicRefactoringDefinition(
								new File(
										PluginCatalog
												.getXmlRefactoringDefinitionFilePath(MI_REFACT1_NAME)))
						.getCategories());
		assertEquals(
				catalog.getAllClassifications(),
				ClassificationsReaderFactory.getReader(
						ClassificationsReaderTypes.JAXB_READER)
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));
	}

	@Test
	public final void testAddCategory() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRemoveCategory() {
		fail("Not yet implemented"); // TODO
	}

}
