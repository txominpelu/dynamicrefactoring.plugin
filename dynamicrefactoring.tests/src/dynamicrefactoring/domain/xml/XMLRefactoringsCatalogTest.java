package dynamicrefactoring.domain.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.util.Utils;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.util.io.FileManager;

public final class XMLRefactoringsCatalogTest {

	private static final String TESTDATA_EXAMPLES_DIR = "/testdata/examples/";
	private static final String IMAGEN_FILENAME = "imagen.jpeg";
	private static final String IMAGEN_ORIGINAL_PATH = "/testdata/image/"
			+ IMAGEN_FILENAME;
	private static final String MI_REFACT1_NAME = "MiRefact1-MiCategoria1";
	private static final String NEW_NAME = "NewName";
	private static final String MI_CATEGORIA1 = "MiCategoria1";
	private static final String MI_CLASIFICACION1 = "MiClasificacion1";
	private RefactoringsCatalog refactCatalog;

	/**
	 * Set up de las refactorizaciones disponibles y el catalogo.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		Utils.setTestRefactoringInRefactoringsDir();
		refactCatalog = new XMLRefactoringsCatalog(
				XMLRefactoringsCatalog.getRefactoringsFromDir(RefactoringPlugin
						.getDynamicRefactoringsDir()));
	}

	/**
	 * Restaura el directorio de refactorizaciones.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		Utils.restoreDefaultRefactorings();
	}

	/**
	 * Prueba a actualizar una refactorizacion y comprobar que el xml tambien se
	 * actualiza.
	 */
	@Test
	public final void testUpdateRefactoringClassifications() {
		final DynamicRefactoringDefinition refactoringToUpdate = refactCatalog
				.getRefactoring(MI_REFACT1_NAME);
		Set<Category> expectedRefactoringCategories = refactoringToUpdate
				.getCategories();
		expectedRefactoringCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedRefactoringCategories.add(new Category(MI_CLASIFICACION1,
				NEW_NAME));

		final DynamicRefactoringDefinition expectedRefact = refactoringToUpdate
				.getBuilder().categories(expectedRefactoringCategories)
				.build();
		refactCatalog
				.updateRefactoring(MI_REFACT1_NAME, expectedRefact);

		assertEquals(expectedRefactoringCategories,
				new RefactoringCatalogStub().getRefactoring(MI_REFACT1_NAME)
						.getCategories());
		//assertTrue(expectedRefact.exactlyEquals(new RefactoringCatalogStub().getRefactoring(MI_REFACT1_NAME)));
	}

	/**
	 * Prueba a agregar una nueva refactorizacion y comprobar que se agrega
	 * correctamente al catalogo y se guarda correctamente su fichero xml.
	 */
	@Test
	public final void testAddRefactoring() {
		final Set<DynamicRefactoringDefinition> expectedRefactorings = refactCatalog
				.getAllRefactorings();
		final DynamicRefactoringDefinition refactToAdd = refactCatalog
				.getRefactoring(MI_REFACT1_NAME).getBuilder().name(NEW_NAME)
				.build();
		expectedRefactorings.add(refactToAdd);
		refactCatalog.addRefactoring(refactToAdd);
		assertEquals(expectedRefactorings, refactCatalog.getAllRefactorings());
		//assertTrue(refactToAdd.exactlyEquals(new RefactoringCatalogStub().getRefactoring(NEW_NAME)));
	}

	/**
	 * Prueba a agregar una nueva refactorizacion con imagenes y comprobar que
	 * se agrega correctamente al catalogo y se guarda correctamente su fichero
	 * xml.
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testAddRefactoringWithImages() throws IOException {
		final Set<DynamicRefactoringDefinition> expectedRefactorings = refactCatalog
				.getAllRefactorings();
		final DynamicRefactoringDefinition refactToAdd = refactCatalog
				.getRefactoring(MI_REFACT1_NAME)
				.getBuilder()
				.name(NEW_NAME)
				.image(FileManager.getBundleFileAsSystemFile(IMAGEN_ORIGINAL_PATH)
						.getAbsolutePath()).build();
		expectedRefactorings.add(refactToAdd);
		refactCatalog.addRefactoring(refactToAdd);
		assertEquals(expectedRefactorings, refactCatalog.getAllRefactorings());
		assertEquals(refactToAdd,
				new RefactoringCatalogStub().getRefactoring(NEW_NAME));
		// Comprobamos que el directorio existe
		assertTrue(refactToAdd.getDirectoryToSaveRefactoringFile().exists());
		checkImage(refactToAdd);
	}

	private void checkImage(DynamicRefactoringDefinition refactoring) throws IOException {
		assertTrue(getRefactoringDirectoryFile(refactoring, IMAGEN_FILENAME)
				.exists());
		assertTrue(FileUtils.contentEquals(
				FileManager.getBundleFileAsSystemFile(IMAGEN_ORIGINAL_PATH),
				getRefactoringDirectoryFile(refactoring, IMAGEN_FILENAME)));
		assertEquals(getRefactoringDirectoryFile(refactoring, IMAGEN_FILENAME)
				.getName(),
				new RefactoringCatalogStub().getRefactoring(refactoring.getName())
						.getImage());
	}

	

	/**
	 * Prueba a agregar una nueva refactorizacion con imagenes y comprobar que
	 * se agrega correctamente al catalogo y se guarda correctamente su fichero
	 * xml.
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testAddRefactoringWithExamples() throws IOException {
		final Set<DynamicRefactoringDefinition> expectedRefactorings = refactCatalog
				.getAllRefactorings();
		final DynamicRefactoringDefinition refactToAdd = refactCatalog
				.getRefactoring(MI_REFACT1_NAME).getBuilder().name(NEW_NAME)
				.examples(getSourceExamples()).build();
		expectedRefactorings.add(refactToAdd);
		refactCatalog.addRefactoring(refactToAdd);
		assertEquals(expectedRefactorings, refactCatalog.getAllRefactorings());
		final RefactoringCatalogStub refactoringCatalogStub = new RefactoringCatalogStub();
		assertEquals(refactToAdd,
				refactoringCatalogStub.getRefactoring(NEW_NAME));
		// Comprobamos que el directorio existe
		assertTrue(refactToAdd.getDirectoryToSaveRefactoringFile().exists());
		checkExamples(refactoringCatalogStub);
	}
	
	/**
	 * Prueba a agregar una nueva refactorizacion con imagenes y comprobar que
	 * se agrega correctamente al catalogo y se guarda correctamente su fichero
	 * xml.
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testUpdateRefactoringWithExamples() throws IOException {
		final Set<DynamicRefactoringDefinition> expectedRefactorings = refactCatalog.getAllRefactorings();
		final DynamicRefactoringDefinition refactToRemove = refactCatalog
				.getRefactoring(MI_REFACT1_NAME);
		expectedRefactorings.remove( refactToRemove);
		final DynamicRefactoringDefinition refactToUpdate = refactToRemove.getBuilder().name(NEW_NAME)
				.examples(getSourceExamples()).build();
		expectedRefactorings.add(refactToUpdate);
		refactCatalog.updateRefactoring(MI_REFACT1_NAME, refactToUpdate);
		assertEquals(expectedRefactorings, refactCatalog.getAllRefactorings());
		final RefactoringCatalogStub refactoringCatalogStub = new RefactoringCatalogStub();
		assertEquals(refactToUpdate,
				refactoringCatalogStub.getRefactoring(NEW_NAME));
		// Comprobamos que el directorio existe
		assertTrue(refactToUpdate.getDirectoryToSaveRefactoringFile(
				).exists());
		checkExamples(refactoringCatalogStub);
		assertFalse(refactToRemove.getDirectoryToSaveRefactoringFile().exists());
	}
	
	/**
	 * Prueba a agregar una nueva refactorizacion con imagenes y comprobar que
	 * se agrega correctamente al catalogo y se guarda correctamente su fichero
	 * xml.
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testUpdateRefactoringWithImages() throws IOException {
		final Set<DynamicRefactoringDefinition> expectedRefactorings = refactCatalog.getAllRefactorings();
		expectedRefactorings.remove( refactCatalog
				.getRefactoring(MI_REFACT1_NAME));
		final DynamicRefactoringDefinition refactToUpdate = refactCatalog
				.getRefactoring(MI_REFACT1_NAME).getBuilder().name(NEW_NAME)
				.image(FileManager.getBundleFileAsSystemFile(IMAGEN_ORIGINAL_PATH)
						.getAbsolutePath()).build();
		expectedRefactorings.add(refactToUpdate);
		refactCatalog.updateRefactoring(MI_REFACT1_NAME, refactToUpdate);
		assertEquals(expectedRefactorings, refactCatalog.getAllRefactorings());
		final RefactoringCatalogStub refactoringCatalogStub = new RefactoringCatalogStub();
		assertEquals(refactToUpdate,
				refactoringCatalogStub.getRefactoring(NEW_NAME));
		// Comprobamos que el directorio existe
		// Comprobamos que el directorio existe
		assertTrue(refactToUpdate.getDirectoryToSaveRefactoringFile(
				).exists());
		checkImage(refactToUpdate);
	}

	/**
	 * Comprueba que los ejemplos se han agregado correctamente.
	 * 
	 * @param refactoringCatalogStub catalogo
	 * @throws IOException
	 */
	private void checkExamples(final RefactoringsCatalog refactoringCatalogStub)
			throws IOException {
		assertEquals(getFinalExamples(),
				refactoringCatalogStub.getRefactoring(NEW_NAME).getExamples());
		for (RefactoringExample ejemplo : getFinalExamples()) {
			assertTrue(getRefactoringDirectoryFile(refactoringCatalogStub.getRefactoring(NEW_NAME),
					ejemplo.getBefore()).exists());
			assertTrue(FileUtils.contentEquals(
					FileManager.getBundleFileAsSystemFile(TESTDATA_EXAMPLES_DIR
							+ ejemplo.getBefore()),
					getRefactoringDirectoryFile(refactoringCatalogStub.getRefactoring(NEW_NAME), ejemplo.getBefore())));
			assertTrue(getRefactoringDirectoryFile(refactoringCatalogStub.getRefactoring(NEW_NAME), ejemplo.getAfter())
					.exists());
			assertTrue(getRefactoringDirectoryFile(refactoringCatalogStub.getRefactoring(NEW_NAME), ejemplo.getAfter())
					.exists());
			assertTrue(FileUtils.contentEquals(
					FileManager.getBundleFileAsSystemFile(TESTDATA_EXAMPLES_DIR
							+ ejemplo.getAfter()),
					getRefactoringDirectoryFile(refactoringCatalogStub.getRefactoring(NEW_NAME), ejemplo.getAfter())));
		}
	}

	private List<RefactoringExample> getFinalExamples() throws IOException {
		List<RefactoringExample> listaEjemplos = new ArrayList<RefactoringExample>();
		listaEjemplos.add(new RefactoringExample("ejemplo1_antes.txt",
				"ejemplo1_despues.txt"));
		return listaEjemplos;
	}

	private List<RefactoringExample> getSourceExamples() throws IOException {
		final RefactoringExample ejemplo = new RefactoringExample(
				FileManager.getBundleFileAsSystemFile(
						TESTDATA_EXAMPLES_DIR + "ejemplo1_antes.txt")
						.getAbsolutePath(), FileManager.getBundleFileAsSystemFile(
						TESTDATA_EXAMPLES_DIR + "ejemplo1_despues.txt")
						.getAbsolutePath());
		List<RefactoringExample> listaEjemplos = new ArrayList<RefactoringExample>();
		listaEjemplos.add(ejemplo);
		return listaEjemplos;
	}
	
	private File getRefactoringDirectoryFile (DynamicRefactoringDefinition refact, String fileName) {
		return new File(refact
				.getDirectoryToSaveRefactoringFile()
				.getAbsolutePath()
				+ File.separator + fileName);
	}
}
