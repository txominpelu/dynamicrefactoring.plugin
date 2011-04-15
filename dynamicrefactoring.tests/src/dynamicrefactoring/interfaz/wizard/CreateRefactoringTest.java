package dynamicrefactoring.interfaz.wizard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;

public final class CreateRefactoringTest extends AbstractRefactoringWizardTest{
	
	static final String MI_MOTIVACION = "MiMotivacion";
	static final String MI_DESCRIPCION = "MiDescripcion";
	static final String MI_NOMBRE = "MiNombre";
	private RefactoringWizardPage1Object refactoringWizardPageObject;

	/**
	 * Crea la primera pagina del wizard de crear/editar una refactorizacion.
	 */
	@Before
	public void setUp(){
		SWTWorkbenchBot bot = super.setUpBot();
		refactoringWizardPageObject = new RefactoringWizardPage1Object(bot);
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

	@Override
	public AbstractRefactoringWizardPage getPage() {
		return refactoringWizardPageObject;
	}
	

}
