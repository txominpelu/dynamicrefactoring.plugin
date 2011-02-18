package dynamicrefactoring.interfaz.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.PlatformUI;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MyFirstTest {
 
	private static SWTWorkbenchBot	bot;
 
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
	}
	
	@Before
	public final void waitMenu() {
        // The following seems to be needed to run SWTBot tests in Xvfb (the 'fake' X-server).
        // see http://dev.eclipse.org/mhonarc/newsLists/news.eclipse.swtbot/msg01134.html
        UIThreadRunnable.syncExec(new VoidResult() {
            public void run() {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                        .forceActive();
            }
        });

		bot.waitUntil(new DefaultCondition() {

			public boolean test() throws Exception {
				return bot.menu("File").isEnabled();
			}

			public String getFailureMessage() {
				return "Menu bar not available";
			}
		});
	}


	
 
 
	@Test
	public void canCreateANewJavaProject() throws Exception {
		bot.menu("File").menu("New").menu("Project...").click();
 
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("Java").select("Java Project");
		bot.button("Next >").click();
 
		bot.textWithLabel("Project name:").setText("MyFirstProject");
 
		bot.button("Finish").click();
		// FIXME: assert that the project is actually created, for later
	}
 
 
	@AfterClass
	public static void sleep() {
		bot.sleep(2000);
	}
 
}
