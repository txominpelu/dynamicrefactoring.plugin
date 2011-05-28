package dynamicrefactoring.interfaz.editor.classifeditor;


import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.CatalogStub;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.editor.classifeditor.CategoriesSection;

public class CategoriesSectionTest {

	private static final String CLASIF_INICIAL = "ClasifPrueba";
	private static final String CATEGORY_TO_RENAME = "clasifToRename";
	private static final String CATEGORY_NEW_NAME = "clasifNewName";
	
	private ClassificationsCatalog catalog;
	private CategoriesSection editor;

	@Before
	public void setUp() throws Exception {
		catalog = new CatalogStub();
		editor = new CategoriesSection(CLASIF_INICIAL, catalog);
	}
	
	/**
	 * Comprueba que si se pide al editor renombrar una categoria
	 * lo hace correctamente.
	 */
	@Test
	public void renameCategoryTest(){
		Set<Category> expectedCategories = catalog.getClassification(editor.getClassification().getName()).getCategories();
		expectedCategories.remove(new Category(editor.getClassification().getName(), CATEGORY_TO_RENAME));
		expectedCategories.add(new Category(editor.getClassification().getName(), CATEGORY_NEW_NAME));
		editor.renameCategory(CATEGORY_TO_RENAME, CATEGORY_NEW_NAME);
		assertEquals(expectedCategories, catalog.getClassification(editor.getClassification().getName()).getCategories());
	}
	
	/**
	 * Comprueba que si se pide al editor agregar una categoria
	 * lo hace correctamente.
	 */
	@Test
	public void addCategoryTest(){
		Set<Category> expectedCategories = catalog.getClassification(editor.getClassification().getName()).getCategories();
		expectedCategories.add(new Category(editor.getClassification().getName(), CATEGORY_NEW_NAME));
		editor.addCategory(CATEGORY_NEW_NAME);
		assertEquals(expectedCategories, catalog.getClassification(editor.getClassification().getName()).getCategories());
	}
	
	/**
	 * Comprueba que si se pide al editor eliminar una categoria
	 * lo hace correctamente.
	 */
	@Test
	public void removeCategoryTest(){
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(editor.getClassification()
				.getName(), CATEGORY_NEW_NAME));

		editor.addCategory(CATEGORY_NEW_NAME);
		editor.removeCategory(CATEGORY_TO_RENAME);
		assertEquals(expectedCategories,
				catalog.getClassification(CLASIF_INICIAL).getCategories());
	}
	

}
