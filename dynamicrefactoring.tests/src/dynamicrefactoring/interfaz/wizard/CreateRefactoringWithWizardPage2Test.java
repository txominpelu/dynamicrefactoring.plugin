package dynamicrefactoring.interfaz.wizard;

import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;

public final class CreateRefactoringWithWizardPage2Test extends AbstractRefactoringWizardTest{
	
	private RefactoringWizardPage2Object refactoringWizardPage2Object;

	/**
	 * Crea la primera pagina del wizard de crear/editar una refactorizacion.
	 */
	@Before
	public void setUp(){
		SWTWorkbenchBot bot = super.setUpBot();
		refactoringWizardPage2Object = goToSecondPage(bot);
		//bot.viewByTitle("Welcome").close();
	}

	/**
	 * Pasa a la siguiente pagina del wizard para obtener
	 * su objeto y poder empezar a hacer pruebas con el.
	 * 
	 * @param bot bot que permite llegar a la siguiente pagina
	 * @return objeto que permite manipular la segunda pagina y hacer tests sobre ella
	 */
	public RefactoringWizardPage2Object goToSecondPage(SWTWorkbenchBot bot) {
		RefactoringWizardPage1Object refactoringWizardPageObject = new RefactoringWizardPage1Object(bot);
		refactoringWizardPageObject.setMotivation(CreateRefactoringTest.MI_MOTIVACION);
		refactoringWizardPageObject.setDescription(CreateRefactoringTest.MI_DESCRIPCION);
		refactoringWizardPageObject.checkCategory(PluginClassificationsCatalog.SCOPE_CLASSIFICATION, Scope.METHOD.toString());
		refactoringWizardPageObject.setName(CreateRefactoringTest.MI_NOMBRE);
		return  (RefactoringWizardPage2Object) refactoringWizardPageObject.goToNextPage();
	}
	
	/**
	 * Prueba que poniendo el elemento moon.core.Model como 
	 * elemento principal se puede pasar a la siguiente pagina
	 * del wizard.
	 */
	@Test
	public void canGoToNextPageTest(){
		refactoringWizardPage2Object.markInputAsMain("moon.core.Model (1)");
		assertTrue(refactoringWizardPage2Object.canGoToNextPage());
	}

	@Override
	public AbstractRefactoringWizardPage getPage() {
		return refactoringWizardPage2Object;
	}

}
