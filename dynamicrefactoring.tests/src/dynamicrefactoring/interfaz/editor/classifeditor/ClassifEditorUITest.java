package dynamicrefactoring.interfaz.editor.classifeditor;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dynamicrefactoring.interfaz.wizard.RefactoringWizardPage1Object;

/**
 * Pruebas de interfaz del editor de clasificaciones.
 * 
 * @author imediava
 *
 */
public final class ClassifEditorUITest {

	private static final String RENAMED_CLASSIFICATION = "RenamedName";
	private static final String NEW_CLASSIFICATION = "NewClassification";
	private ClassifEditorPage page;

	/**
	 * Elimina la ventana de Welcome si esta abierta.
	 */
	@BeforeClass
	public static void setUpClass(){
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		if(bot.activeView().getTitle().equals("Welcome")){
			bot.activeView().close();
		}
	}
	
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
		page = new ClassifEditorPage(bot);
	}
	
	/**
	 * Agrega una clasificacion y la elimina y comprueba
	 * que hace ambas cosas correctamente.
	 */
	@Test
	public void addRemoveClassificationTest(){
		Set<String> expectedClassifications = new TreeSet<String>(page.getClassifications());
		expectedClassifications.add(NEW_CLASSIFICATION);
		page.addClassification(NEW_CLASSIFICATION);
		assertEquals(new ArrayList<String>(expectedClassifications), page.getClassifications());
		
		expectedClassifications.remove(NEW_CLASSIFICATION);
		page.removeClassification(NEW_CLASSIFICATION);
		assertEquals(new ArrayList<String>(expectedClassifications), page.getClassifications());
	}
	
	/**
	 * Agrega una clasificacion y la renombra y comprueba
	 * que hace ambas cosas correctamente.
	 */
	@Test
	public void renameRemoveClassificationTest(){
		Set<String> expectedClassifications = new TreeSet<String>(page.getClassifications());
		expectedClassifications.add(NEW_CLASSIFICATION);
		page.addClassification(NEW_CLASSIFICATION);
		assertEquals(new ArrayList<String>(expectedClassifications), page.getClassifications());
		
		page.renameClassification(NEW_CLASSIFICATION, RENAMED_CLASSIFICATION);
		expectedClassifications.remove(NEW_CLASSIFICATION);
		expectedClassifications.add(RENAMED_CLASSIFICATION);
		assertEquals(new ArrayList<String>(expectedClassifications), page.getClassifications());
		
		expectedClassifications.remove(RENAMED_CLASSIFICATION);
		page.removeClassification(RENAMED_CLASSIFICATION);
		assertEquals(new ArrayList<String>(expectedClassifications), page.getClassifications());
	}

}
