package dynamicrefactoring.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.util.Utils;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderImp;

public class SimpleRefactoringsCatalogTest {

	private static final String MI_REFACT1_NAME = "MiRefact1-MiCategoria1";
	private static final String NEW_NAME = "NewName";
	private static final String MI_CATEGORIA1 = "MiCategoria1";
	private static final String MI_CLASIFICACION1 = "MiClasificacion1";
	private static RefactoringsCatalog refactCatalog;

	/**
	 * Set up de las refactorizaciones disponibles y el catalogo.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		Utils.setTestRefactoringInRefactoringsDir();
		refactCatalog = new XMLRefactoringsCatalog(
				XMLRefactoringsCatalog
						.getRefactoringsFromDir(RefactoringPlugin
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
	 * Prueba a actualizar una refactorizacion y comprobar
	 * que el xml tambien se actualiza.
	 */
	@Test
	public final void testUpdateRefactoring() {
		final DynamicRefactoringDefinition refactoringToUpdate = refactCatalog
				.getRefactoring(MI_REFACT1_NAME);
		Set<Category> expectedRefactoringCategories = refactoringToUpdate
				.getCategories();
		expectedRefactoringCategories.remove(new Category(MI_CLASIFICACION1,
				MI_CATEGORIA1));
		expectedRefactoringCategories.add(new Category(MI_CLASIFICACION1,
				NEW_NAME));

		refactCatalog.updateRefactoring(refactoringToUpdate.getBuilder()
				.categories(expectedRefactoringCategories).build());

		assertEquals(
				expectedRefactoringCategories,
				new JDOMXMLRefactoringReaderImp()
						.getDynamicRefactoringDefinition(
								new File(
										XMLRefactoringsCatalog
												.getXmlRefactoringDefinitionFilePath(MI_REFACT1_NAME)))
						.getCategories());
	}

	/**
	 * Prueba a agregar una nueva refactorizacion y comprobar
	 * que se agrega correctamente al catalogo y se guarda
	 * correctamente su fichero xml.
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
		assertEquals(
				refactToAdd,
				new JDOMXMLRefactoringReaderImp().getDynamicRefactoringDefinition(new File(
						XMLRefactoringsCatalog
								.getXmlRefactoringDefinitionFilePath(NEW_NAME))));
	}

}
