package dynamicrefactoring.interfaz.wizard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;

public final class CreateRefactoringTest {
	
	static final String MI_MOTIVACION = "MiMotivacion";
	static final String MI_DESCRIPCION = "MiDescripcion";
	static final String MI_NOMBRE = "MiNombre";
	private RefactoringWizardPage1Object refactoringWizardPageObject;

	/**
	 * Crea la primera pagina del wizard de crear/editar una refactorizacion.
	 */
	@Before
	public void setUp(){
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
		refactoringWizardPageObject = new RefactoringWizardPage1Object(bot);
		//bot.viewByTitle("Welcome").close();
	}
	
	/**
	 * Cierra el wizard de crear/editar una refactorizacion.
	 */
	@After
	public void tearDown(){
		refactoringWizardPageObject.cancelWizard();
	}
	
	/**
	 * Asigna el nombre en el wizard.
	 */
	@Test
	public void setNameTest(){
		refactoringWizardPageObject.setName(MI_NOMBRE);
		assertEquals(MI_NOMBRE, refactoringWizardPageObject.getName());
		assertFalse(refactoringWizardPageObject.canGoToNextPage());
	}
	
	/**
	 * Asigna la descripcion en el wizard.
	 */
	@Test
	public void setDescriptionTest(){
		refactoringWizardPageObject.setDescription(MI_DESCRIPCION);
		assertEquals(MI_DESCRIPCION, refactoringWizardPageObject.getDescription());
		assertFalse(refactoringWizardPageObject.canGoToNextPage());
	}
	
	/**
	 * Asigna la motivacion en el wizard.
	 */
	@Test
	public void setMotivationTest(){
		refactoringWizardPageObject.setMotivation(MI_MOTIVACION);
		assertEquals(MI_MOTIVACION, refactoringWizardPageObject.getMotivation());
		assertFalse(refactoringWizardPageObject.canGoToNextPage());
	}
	
	/**
	 * Asigna nombre, motivacion y descripcion y comprueba
	 * que todavia no se puede pasar porque no se ha asignado
	 * un scope.
	 */
	@Test
	public void notEnabledGoToNextPageIfNoScopeTest(){
		refactoringWizardPageObject.setName(MI_NOMBRE);
		refactoringWizardPageObject.setMotivation(MI_MOTIVACION);
		refactoringWizardPageObject.setDescription(MI_DESCRIPCION);
		assertFalse(refactoringWizardPageObject.canGoToNextPage());
	}
	
	/**
	 * Asigna todos los parametros necesarios y comprueba que 
	 * se puede pasar a la siguiente pagina.
	 */
	@Test
	public void enableGoToNextPageTest(){
		refactoringWizardPageObject.setMotivation(MI_MOTIVACION);
		refactoringWizardPageObject.setDescription(MI_DESCRIPCION);
		refactoringWizardPageObject.checkCategory(PluginClassificationsCatalog.SCOPE_CLASSIFICATION, Scope.METHOD.toString());
		refactoringWizardPageObject.setName(MI_NOMBRE);
		assertTrue(refactoringWizardPageObject.canGoToNextPage());
	}
	

}
