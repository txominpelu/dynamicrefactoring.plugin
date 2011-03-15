package dynamicrefactoring.plugin.xml.classifications.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsReaderFactory.ClassificationsReaderTypes;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.util.io.FileManager;

public class PluginCatalogTest {

	private static final String MI_NUEVA_CLASSIFICACION = "MiNuevaClassificacion";
	private static final String REFACT_REPO_PATH = FilenameUtils
			.separatorsToSystem("testdata/"
					+ "dynamicrefactoring/plugin/xml/classifications/imp/"
					+ "ClassificationsStoreTest/");
	private static final String MI_REFACT1_NAME = "MiRefact1-MiCategoria1";
	private static final String NEW_CATEGORY = "NewName";
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
				NEW_CATEGORY));

		Set<Category> expectedRefactoringCategories = catalog.getRefactoring(
				MI_REFACT1_NAME).getCategories();
		expectedRefactoringCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedRefactoringCategories.add(new Category(MI_CLASIFICACION1,
				NEW_CATEGORY));

		catalog.renameCategory(MI_CLASIFICACION1, MI_CATEGORIA1, NEW_CATEGORY);
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
	public final void testAddCategory() throws ValidationException {
		Set<Category> expectedClassificationCategories = new HashSet<Category>();
		expectedClassificationCategories.add(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedClassificationCategories.add(new Category(MI_CLASIFICACION1,
				NEW_CATEGORY));
		catalog.addCategoryToClassification(MI_CLASIFICACION1, NEW_CATEGORY);
		assertEquals(expectedClassificationCategories, catalog
				.getClassification(MI_CLASIFICACION1).getCategories());
		assertEquals(
				catalog.getAllClassifications(),
				ClassificationsReaderFactory.getReader(
						ClassificationsReaderTypes.JAXB_READER)
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));
	}

	@Test
	public final void testRemoveCategory() throws ValidationException {
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(MI_CLASIFICACION1, NEW_CATEGORY));
		catalog.addCategoryToClassification(MI_CLASIFICACION1, NEW_CATEGORY);
		catalog.addCategoryToRefactoring(MI_REFACT1_NAME, MI_CLASIFICACION1,
				NEW_CATEGORY);
		catalog.removeCategory(MI_CLASIFICACION1, MI_CATEGORIA1);
		assertEquals(expectedCategories,
				catalog.getClassification(MI_CLASIFICACION1).getCategories());
		assertEquals(
				catalog.getAllClassifications(),
				ClassificationsReaderFactory.getReader(
						ClassificationsReaderTypes.JAXB_READER)
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));

		assertEquals(expectedCategories, catalog
				.getRefactoring(MI_REFACT1_NAME).getCategories());

		assertEquals(
				expectedCategories,
				new JDOMXMLRefactoringReaderImp()
						.getDynamicRefactoringDefinition(
								new File(
										PluginCatalog
												.getXmlRefactoringDefinitionFilePath(MI_REFACT1_NAME)))
						.getCategories());
	}

	@Test
	public final void testRenameClassification() throws ValidationException {
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(MI_NUEVA_CLASSIFICACION,
				MI_CATEGORIA1));

		catalog.renameClassification(MI_CLASIFICACION1, MI_NUEVA_CLASSIFICACION);

		assertFalse(catalog.containsClassification(MI_CLASIFICACION1));
		assertTrue(catalog.containsClassification(MI_NUEVA_CLASSIFICACION));
		assertEquals(expectedCategories,
				catalog.getClassification(MI_NUEVA_CLASSIFICACION)
						.getCategories());

		assertEquals(
				catalog.getAllClassifications(),
				ClassificationsReaderFactory.getReader(
						ClassificationsReaderTypes.JAXB_READER)
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));

		assertEquals(expectedCategories, catalog
				.getRefactoring(MI_REFACT1_NAME).getCategories());

		assertEquals(
				expectedCategories,
				new JDOMXMLRefactoringReaderImp()
						.getDynamicRefactoringDefinition(
								new File(
										PluginCatalog
												.getXmlRefactoringDefinitionFilePath(MI_REFACT1_NAME)))
						.getCategories());
	}

	@Test
	public final void testAddClassification() throws ValidationException {
		Set<Classification> expectedClassifications = catalog
				.getAllClassifications();
		final SimpleUniLevelClassification newClassification = new SimpleUniLevelClassification(
				MI_NUEVA_CLASSIFICACION, "", ImmutableSet.of(new Category(
						MI_NUEVA_CLASSIFICACION, NEW_CATEGORY)));
		expectedClassifications.add(newClassification);

		catalog.addClassification(newClassification);

		assertTrue(catalog.containsClassification(MI_NUEVA_CLASSIFICACION));
		assertEquals(expectedClassifications, catalog.getAllClassifications());
		assertEquals(
				expectedClassifications,
				ClassificationsReaderFactory.getReader(
						ClassificationsReaderTypes.JAXB_READER)
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));
		assertEquals(newClassification,
				catalog.getClassification(MI_NUEVA_CLASSIFICACION));

	}

	@Test
	public final void testRemoveClassification() throws ValidationException {
		Set<Classification> expectedClassifications = new HashSet<Classification>();
		final SimpleUniLevelClassification newClassification = new SimpleUniLevelClassification(
				MI_NUEVA_CLASSIFICACION, "", ImmutableSet.of(new Category(
						MI_NUEVA_CLASSIFICACION, NEW_CATEGORY)));
		expectedClassifications.add(newClassification);

		catalog.addClassification(newClassification);
		catalog.addCategoryToRefactoring(MI_REFACT1_NAME,
				MI_NUEVA_CLASSIFICACION, NEW_CATEGORY);

		catalog.removeClassification(MI_CLASIFICACION1);

		assertFalse(catalog.containsClassification(MI_CLASIFICACION1));
		assertEquals(expectedClassifications, catalog.getAllClassifications());
		assertEquals(
				expectedClassifications,
				ClassificationsReaderFactory.getReader(
						ClassificationsReaderTypes.JAXB_READER)
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));

		Set<Category> expectedRefactoringCategories = new HashSet<Category>();
		expectedRefactoringCategories.add(new Category(MI_NUEVA_CLASSIFICACION,
				NEW_CATEGORY));
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

	}

}
