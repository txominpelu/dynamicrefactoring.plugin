package dynamicrefactoring.interfaz.wizard;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EditRefactoringCategoriesTest extends AbstractRefactoringWizardTest{
	
	private static final String BAD_SMELLS_CLASSIF = "BadSmells";

	private static final String MAKING_METHOD_CALLS_SIMPLER_CATEGORY = "MakingMethodCallsSimpler";

	private static final String COMPOSING_METHODS_TREE_ITEM_TEXT = "ComposingMethods";

	private static final String FOWLER_CLASSIF = "Fowler";

	private RefactoringWizardPage1Object refactoringWizardPageObject;

	/**
	 * Crea la primera pagina del wizard de crear/editar una refactorizacion.
	 */
	@Before
	public final void setUp(){
		SWTWorkbenchBot bot = super.setUpBot();
		SWTBotPreferences.TIMEOUT = 10000;
		refactoringWizardPageObject = new RefactoringWizardPage1Object(bot);
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
		
		for (Category c :PluginClassificationsCatalog.getInstance().getClassification(BAD_SMELLS_CLASSIF).getCategories()) {
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
		
		for (Category c :PluginClassificationsCatalog.getInstance().getClassification(BAD_SMELLS_CLASSIF).getCategories()) {
			refactoringWizardPageObject.checkCategory(BAD_SMELLS_CLASSIF, c.getName());
		}
		
		refactoringWizardPageObject.checkClassification(BAD_SMELLS_CLASSIF);
		
		for (Category c :PluginClassificationsCatalog.getInstance().getClassification(BAD_SMELLS_CLASSIF).getCategories()) {
			assertFalse(refactoringWizardPageObject.isCategoryChecked(BAD_SMELLS_CLASSIF, c.getName()));
		}
		
	}



	@Override
	public AbstractRefactoringWizardPage getPage() {
		return refactoringWizardPageObject;
	}

}
