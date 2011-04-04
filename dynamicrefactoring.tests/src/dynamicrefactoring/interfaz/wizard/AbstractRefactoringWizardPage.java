package dynamicrefactoring.interfaz.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

public abstract class AbstractRefactoringWizardPage implements RefactoringWizardPage{
	
	private SWTWorkbenchBot swtBot;

	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public AbstractRefactoringWizardPage(SWTWorkbenchBot bot) {
		swtBot = bot;
	}
	
	/**
	 * Cierra la ventana y finaliza el wizard de refactorizacion.
	 */
	public final void cancelWizard() {
		swtBot.activeShell().close();
	}

	/**
	 * Devuelve si se puede pasar a la siguiente página del
	 * wizard.
	 * 
	 * @return verdader si se puede pasar, falso en caso contrario
	 */
	public final boolean canGoToNextPage() {
		return swtBot.button("Next").isEnabled();
	}
	
	
	
	/**
	 * Obtiene el bot de la pagina.
	 * 
	 * @return bot de la pagina
	 */
	public final SWTWorkbenchBot getBot(){
		return swtBot;
	}

}
