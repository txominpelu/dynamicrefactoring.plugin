package dynamicrefactoring.interfaz.wizard;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EditRefactoringTests {
	
	private static final String MAKING_METHOD_CALLS_SIMPLER_CATEGORY = "MakingMethodCallsSimpler";

	private static final String COMPOSING_METHODS_TREE_ITEM_TEXT = "ComposingMethods";

	private static final String FOWLER_CLASSIF = "Fowler";

	private static final String SCOPE_CLASSIF = "scope";

	private static final String METHOD_CATEGORY = "Method";

	private static final String CODE_FRAGMENT_CATEGORY = "CodeFragment";

	private RefactoringWizardPage1Object refactoringWizardPageObject;

	/**
	 * Crea la primera pagina del wizard de crear/editar una refactorizacion.
	 */
	@Before
	public final void setUp(){
		refactoringWizardPageObject = new RefactoringWizardPage1Object();
		//bot.viewByTitle("Welcome").close();
	}
	
	/**
	 * Cierra el wizard de crear/editar una refactorizacion.
	 */
	@After
	public final void tearDown(){
		refactoringWizardPageObject.finish();
	}
	
	/**
	 * Comprueba que no se pueden asignar dos categorias a una clasificacion
	 * uni-categoria.
	 */
	@Test
	public final void cantMarkTwoCategoriesInUniCategoryClassifications() {
 
		refactoringWizardPageObject.checkCategory(SCOPE_CLASSIF,CODE_FRAGMENT_CATEGORY);
		refactoringWizardPageObject.checkCategory(SCOPE_CLASSIF,METHOD_CATEGORY);
		
		assertFalse(refactoringWizardPageObject.isCategoryChecked(SCOPE_CLASSIF,CODE_FRAGMENT_CATEGORY));
		assertTrue(refactoringWizardPageObject.isCategoryChecked(SCOPE_CLASSIF,METHOD_CATEGORY));
		
	}
	
	/**
	 * Comprueba que se pueden asignar dos categorias a una clasificacion
	 * multi-categoria.
	 */
	@Test
	public final void markTwoCategoriesInMultiCategoryClassificationsTest(){
 
		refactoringWizardPageObject.checkCategory(FOWLER_CLASSIF,COMPOSING_METHODS_TREE_ITEM_TEXT);
		refactoringWizardPageObject.checkCategory(FOWLER_CLASSIF,MAKING_METHOD_CALLS_SIMPLER_CATEGORY);
		
		assertTrue(refactoringWizardPageObject.isCategoryChecked(FOWLER_CLASSIF,COMPOSING_METHODS_TREE_ITEM_TEXT));
		assertTrue(refactoringWizardPageObject.isCategoryChecked(FOWLER_CLASSIF,MAKING_METHOD_CALLS_SIMPLER_CATEGORY));
		
	}

}
