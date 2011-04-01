/**
 * 
 */
package dynamicrefactoring.interfaz.view;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.interfaz.wizard.RefactoringWizardPage1Object;



/**
 * @author imediava
 *
 */
public class RefactoringCatalogBrowserViewTest {

	private RefactoringCatalogBrowserViewPage page;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
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
		page = new RefactoringCatalogBrowserViewPage(bot);
	}
	
	/**
	 * Selecciona en el editor un campo y comprueba
	 * que aparecen las refactorizaciones adecuadas.
	 */
	@Test
	public void classifyForScope(){
		page.changeSelectedClassification("Scope");
		System.out.println("Tal");
	}

}
