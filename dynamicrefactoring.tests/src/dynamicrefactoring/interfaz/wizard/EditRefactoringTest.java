package dynamicrefactoring.interfaz.wizard;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dynamicrefactoring.domain.Scope;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EditRefactoringTest {
	
	private static final String MAKING_METHOD_CALLS_SIMPLER_CATEGORY = "MakingMethodCallsSimpler";

	private static final String COMPOSING_METHODS_TREE_ITEM_TEXT = "ComposingMethods";

	private static final String FOWLER_CLASSIF = "Fowler";

	private static final String SCOPE_CLASSIF = "scope";

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
		refactoringWizardPageObject.cancelWizard();
	}
	
	/**
	 * Comprueba que no se pueden asignar dos categorias a una clasificacion
	 * uni-categoria.
	 */
	@Test
	public final void cantMarkTwoCategoriesInUniCategoryClassifications() {
 
		refactoringWizardPageObject.checkCategory(FOWLER_CLASSIF,COMPOSING_METHODS_TREE_ITEM_TEXT);
		refactoringWizardPageObject.checkCategory(FOWLER_CLASSIF,MAKING_METHOD_CALLS_SIMPLER_CATEGORY);
		
		assertFalse(refactoringWizardPageObject.isCategoryChecked(FOWLER_CLASSIF,COMPOSING_METHODS_TREE_ITEM_TEXT));
		assertTrue(refactoringWizardPageObject.isCategoryChecked(FOWLER_CLASSIF,MAKING_METHOD_CALLS_SIMPLER_CATEGORY));
		
	}
	
	/**
	 * Comprueba que se pueden asignar dos categorias a una clasificacion
	 * multi-categoria.
	 */
	@Test
	public final void markTwoCategoriesInMultiCategoryClassificationsTest(){
		
		refactoringWizardPageObject.checkCategory(SCOPE_CLASSIF,Scope.CODE_FRAGMENT.toString());
		refactoringWizardPageObject.checkCategory(SCOPE_CLASSIF, Scope.METHOD.toString());
		
		assertTrue(refactoringWizardPageObject.isCategoryChecked(SCOPE_CLASSIF, Scope.CODE_FRAGMENT.toString()));
		assertTrue(refactoringWizardPageObject.isCategoryChecked(SCOPE_CLASSIF, Scope.METHOD.toString()));
 
		
		
	}
	
	/**
	 * Comprueba que se pueden marcar todas las categorias
	 * de una multicategory.
	 */
	@Test
	public final void markAllCategoriesInMultiCategory(){
		
		refactoringWizardPageObject.checkClassification(SCOPE_CLASSIF);
		
		for (Scope scope : Scope.values()) {
			if (! scope.equals(Scope.BOUNDED_PAR)) {
				assertTrue(refactoringWizardPageObject.isCategoryChecked(SCOPE_CLASSIF, scope.toString()));
			}
		}
 
		
		
	}
	
	/**
	 * Comprueba que no se pueden marcar todas las categorias
	 * de una unicategory.
	 */
	@Test
	public final void markAllCategoriesInUniCategory(){
		
		refactoringWizardPageObject.checkClassification(FOWLER_CLASSIF);
		
		assertFalse(refactoringWizardPageObject.isCategoryChecked(FOWLER_CLASSIF, COMPOSING_METHODS_TREE_ITEM_TEXT));
		assertFalse(refactoringWizardPageObject.isCategoryChecked(FOWLER_CLASSIF, MAKING_METHOD_CALLS_SIMPLER_CATEGORY));
		
	}
	
	/**
	 * Comprueba que no se pueden marcar todas las categorias
	 * de una unicategory.
	 */
	@Test
	public final void unmarkCategories(){
		
		for (Scope scope : Scope.values()) {
			if (! scope.equals(Scope.BOUNDED_PAR)) {
				refactoringWizardPageObject.checkCategory(SCOPE_CLASSIF, scope.toString());
			}
		}
		
		refactoringWizardPageObject.checkClassification(SCOPE_CLASSIF);
		
		for (Scope scope : Scope.values()) {
			if (! scope.equals(Scope.BOUNDED_PAR)) {
				assertFalse(refactoringWizardPageObject.isCategoryChecked(SCOPE_CLASSIF, scope.toString()));
			}
		}
		
	}

}
