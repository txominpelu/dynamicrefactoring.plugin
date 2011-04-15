package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.util.Utils;

import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.xml.RefactoringCatalogStub;
import dynamicrefactoring.util.io.FileManager;

/**
 * Pruebas del catalogo de clasificaciones del plugin.
 * 
 * @author imediava
 *
 */
public final class PluginClassificationsCatalogTest {

	private static final String DESCRIPCION = "Descripcion";
	private static final String MI_CLASIFICACION2 = "MiClasificacion2";
	private static final String CLASSIFICATIONS_XML_FILENAME = "classifications.xml";
	public static final String TEST_REPO_PATH = "/testdata/dynamicrefactoring/plugin/xml/classifications/imp/ClassificationsStoreTest/" ;
	private static final String MI_NUEVA_CLASSIFICACION = "MiNuevaClassificacion";
	private static final String MI_REFACT1_NAME = "MiRefact1-MiCategoria1";
	private static final String NEW_CATEGORY = "NewName";
	private static final String MI_CATEGORIA1 = "MiCategoria1";
	private static final String MI_CLASIFICACION1 = "MiClasificacion1";
	private ClassificationsCatalog catalog;
	private RefactoringsCatalog refactCatalog;

	@Before
	public void setUp() throws Exception {
		Utils.setTestRefactoringInRefactoringsDir();
		FileManager.copyResourceToExactDir(TEST_REPO_PATH + CLASSIFICATIONS_XML_FILENAME, RefactoringPlugin.getCommonPluginFilesDir() + File.separator + "Classification" + File.separator);
		refactCatalog = new RefactoringCatalogStub();
		catalog = new PluginClassificationsCatalog(AbstractCatalog.getClassificationsFromFile(RefactoringConstants.CLASSIFICATION_TYPES_FILE), refactCatalog);
	}

	/**
	 * Restaura el estado del repositorio de refactorizaciones.
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@After
	public void tearDown() throws IOException {
		Utils.restoreDefaultRefactorings();
		FileManager.copyResourceToDir("/Classification/classifications.xml",
				RefactoringPlugin.getCommonPluginFilesDir());
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
	public void testRenameCategory() throws ValidationException {
		Set<Category> expectedClassificationCategories = new HashSet<Category>();
		expectedClassificationCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedClassificationCategories.add(new Category(MI_CLASIFICACION1,
				NEW_CATEGORY));

		Set<Category> expectedRefactoringCategories = refactCatalog.getRefactoring(
				MI_REFACT1_NAME).getCategories();
		expectedRefactoringCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedRefactoringCategories.add(new Category(MI_CLASIFICACION1,
				NEW_CATEGORY));

		catalog.renameCategory(MI_CLASIFICACION1, MI_CATEGORIA1, NEW_CATEGORY);
		assertEquals(expectedClassificationCategories, catalog
				.getClassification(MI_CLASIFICACION1).getCategories());
		assertEquals(expectedRefactoringCategories,
				refactCatalog.getRefactoring(MI_REFACT1_NAME).getCategories());

		
		assertEquals(
				catalog.getAllClassifications(),
				ClassificationsReaderFactory.getReader()
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));
	}

	/**
	 * Prueba que se agrega correctamente una clasificacion.
	 * 
	 * @throws ValidationException si hay problemas de lectura del fichero de clasificaciones
	 */
	@Test
	public void testAddCategory() throws ValidationException {
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
				ClassificationsReaderFactory.getReader()
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));
	}

	/**
	 * Prueba que se elimina correctamente una categoria de una clasificacion.
	 * 
	 * @throws ValidationException si hay problemas de lectura del fichero de clasificaciones
	 */
	@Test
	public void testRemoveCategory() throws ValidationException {
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
				ClassificationsReaderFactory.getReader()
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));

		assertEquals(expectedCategories, refactCatalog
				.getRefactoring(MI_REFACT1_NAME).getCategories());
	}

	/**
	 * Prueba que se renombra correctamente una clasificacion.
	 * 
	 * @throws ValidationException si hay problemas de lectura del fichero de clasificaciones
	 */
	@Test
	public void testRenameClassification() throws ValidationException {
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
				ClassificationsReaderFactory.getReader()
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));

		assertEquals(expectedCategories, refactCatalog
				.getRefactoring(MI_REFACT1_NAME).getCategories());
	}
	
	
	/**
	 * Prueba que se agrega correctamente una clasificacion.
	 * 
	 * @throws ValidationException si hay problemas de lectura del fichero de clasificaciones
	 */
	@Test
	public void testAddClassification() throws ValidationException {
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
				ClassificationsReaderFactory.getReader()
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));
		assertEquals(newClassification,
				catalog.getClassification(MI_NUEVA_CLASSIFICACION));

	}

	/**
	 * Prueba que se elimina correctamente una clasificacion.
	 * 
	 * @throws ValidationException si hay problemas de lectura del fichero de clasificaciones
	 */
	@Test
	public void testRemoveClassification() throws ValidationException {
		Set<Classification> expectedClassifications = new HashSet<Classification>(catalog.getAllClassifications());
		expectedClassifications.remove(catalog.getClassification(MI_CLASIFICACION1));


		catalog.removeClassification(MI_CLASIFICACION1);

		assertFalse(catalog.containsClassification(MI_CLASIFICACION1));
		assertEquals(expectedClassifications, catalog.getAllClassifications());
		assertEquals(
				expectedClassifications,
				ClassificationsReaderFactory.getReader()
						.readClassifications(
								RefactoringConstants.CLASSIFICATION_TYPES_FILE));

	}
	
	/**
	 * Comprueba que si se intenta hacer unicategory
	 * una clasificacion con refactorizaciones en mas de una
	 * categoria salta la excepcion.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testTryToMakeUniCategoryWhenIsImpossible(){
		catalog.setMultiCategory(MI_CLASIFICACION2, false);
	}
	
	/**
	 * Comprueba que se hace unicategory
	 * una clasificacion.
	 */
	@Test
	public void testMakeUniCategoryWhenIsPossible(){
		catalog.setMultiCategory(MI_CLASIFICACION1, false);
		assertFalse(catalog.getClassification(MI_CLASIFICACION1).isMultiCategory());
	}
	
	/**
	 * Comprueba que se hace multicategory
	 * una clasificacion unicategory.
	 */
	@Test
	public void testMakeMultiCategory(){
		catalog.setMultiCategory(MI_CLASIFICACION2, true);
		assertTrue(catalog.getClassification(MI_CLASIFICACION2).isMultiCategory());
	}
	
	/**
	 * Comprueba que se asigna bien una descripcion a una clasificacion.
	 */
	@Test
	public void testSetDescription(){
		catalog.setDescription(MI_CLASIFICACION2, DESCRIPCION);
		assertEquals(DESCRIPCION, catalog.getClassification(MI_CLASIFICACION2).getDescription());
	}

}
