package dynamicrefactoring.interfaz.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.ui.PlatformUI;
import org.junit.After;

public abstract class AbstractRefactoringWizardTest {

	public SWTWorkbenchBot setUpBot(){
		// increase timeout to 10 seconds
		SWTBotPreferences.TIMEOUT = 5000;
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
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
			return bot;
	}
	
	/**
	 * Cierra el wizard de crear/editar una refactorizacion.
	 */
	@After
	public final void tearDown(){
		getPage().cancelWizard();
		// Devuelve el timeout al tiempo por defecto
		SWTBotPreferences.TIMEOUT = 500;
	}
	
	
	/**
	 * Obtiene la pagina.
	 * 
	 * @return pagina del test
	 */
	public abstract AbstractRefactoringWizardPage getPage();

}
