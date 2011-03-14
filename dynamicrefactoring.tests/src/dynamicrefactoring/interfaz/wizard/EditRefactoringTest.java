package dynamicrefactoring.interfaz.wizard;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.plugin.xml.classifications.imp.PluginCatalog;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EditRefactoringTest {
	
	private static final String BAD_SMELLS_CLASSIF = "BadSmells";

	private static final String MAKING_METHOD_CALLS_SIMPLER_CATEGORY = "MakingMethodCallsSimpler";

	private static final String COMPOSING_METHODS_TREE_ITEM_TEXT = "ComposingMethods";

	private static final String FOWLER_CLASSIF = "Fowler";

	private RefactoringWizardPage1Object refactoringWizardPageObject;

	private SWTWorkbenchBot bot;

	/**
	 * Crea la primera pagina del wizard de crear/editar una refactorizacion.
	 */
	@Before
	public final void setUp(){
		bot = new SWTWorkbenchBot();
		 UIThreadRunnable.syncExec(new VoidResult() {
	            public void run() {
	                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
	                        .forceActive();
	            }
	        });

			bot .waitUntil(new DefaultCondition() {

				public boolean test() throws Exception {
					return bot.menu(RefactoringWizardPage1Object.DYNAMIC_REFACTORING_MENU_TEXT).isEnabled();
				}

				public String getFailureMessage() {
					return "Menu bar not available";
				}
			});
		refactoringWizardPageObject = new RefactoringWizardPage1Object(bot);
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
	 * Comprueba que se pueden marcar todas las categorias
	 * de una multicategory.
	 */
	@Test
	public final void markAllCategoriesInMultiCategory(){
		
		refactoringWizardPageObject.checkClassification(BAD_SMELLS_CLASSIF);
		
		for (Category c :PluginCatalog.getInstance().getClassification(BAD_SMELLS_CLASSIF).getCategories()) {
			assertTrue(refactoringWizardPageObject.isCategoryChecked(BAD_SMELLS_CLASSIF, c.getName()));
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
	 * Comprueba que si se marcan una a una todas las categorias de una 
	 * clasificacion multicategoria y se pulsa en la categoria se desmarcan
	 * automaticamente todas.
	 */
	@Test
	public final void unmarkCategories(){
		
		for (Category c :PluginCatalog.getInstance().getClassification(BAD_SMELLS_CLASSIF).getCategories()) {
			refactoringWizardPageObject.checkCategory(BAD_SMELLS_CLASSIF, c.getName());
		}
		
		refactoringWizardPageObject.checkClassification(BAD_SMELLS_CLASSIF);
		
		for (Category c :PluginCatalog.getInstance().getClassification(BAD_SMELLS_CLASSIF).getCategories()) {
			assertFalse(refactoringWizardPageObject.isCategoryChecked(BAD_SMELLS_CLASSIF, c.getName()));
		}
		
	}

}
