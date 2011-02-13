package dynamicrefactoring.interfaz.wizard;


import static org.junit.Assert.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.interfaz.wizard.Messages;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EditRefactoringTests {
	
	private static final String DYNAMIC_REFACTORING_MENU = Platform.getResourceString(RefactoringPlugin.getDefault().getBundle(), "%dynamicrefactoring.menu.main");
	

	private SWTWorkbenchBot bot;

	private SWTBotMenu dinamicRefactoringMenu;

	@Before
	public void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		dinamicRefactoringMenu = bot.menu(EditRefactoringTests.DYNAMIC_REFACTORING_MENU);
	}
	
	@Test
	public void cantMarkTwoCategoriesInUniCategoryClassifications() throws Exception {
		bot.sleep(2000);
		dinamicRefactoringMenu.menu("New Refactoring...").click();
 
		
		bot.tree().expandNode("scope").getNode("CodeFragment").check();
		bot.tree().expandNode("scope").getNode("Method").check();
		
		assertFalse(bot.tree().expandNode("scope").getNode("CodeFragment").isChecked());
		assertTrue(bot.tree().expandNode("scope").getNode("Method").isChecked());
		
		// FIXME: assert that the project is actually created, for later
	}

}
