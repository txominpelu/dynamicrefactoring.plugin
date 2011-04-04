package dynamicrefactoring.interfaz.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;


/**
 * PageObject que facilita los tests de la interfaz gráfica y se corresponde con
 * la última página del wizard de creacion/edicion de una refactorizacion.
 * 
 * @author imediava
 * 
 */
public class RefactoringWizardPage2Object extends AbstractRefactoringWizardPage implements RefactoringWizardPage {
	
	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public RefactoringWizardPage2Object(SWTWorkbenchBot bot) {
		super(bot);
		getBot().shell(RefactoringWizardPage1Object.DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT).activate();
	}
	
	/**
	 * Marca la refactorizacion con el nombre dado como
	 * entrada principal de la refactorizacion.
	 * 
	 * @param inputName nombre de la entrada en la lista. Sera algo como: "moon.core.Model (1)" .
	 */
	public void markInputAsMain(String inputName){
		getBot().listWithId("inputs").select(inputName);
		getBot().checkBoxInGroup("Parameters").click();
	}

	@Override
	public RefactoringWizardPage goToNextPage() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
