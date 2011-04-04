package dynamicrefactoring.interfaz.wizard;


public interface RefactoringWizardPage {
	
	/**
	 * Cierra la ventana y finaliza el wizard de refactorizacion.
	 */
	public void cancelWizard();
	
	/**
	 * Devuelve si se puede pasar a la siguiente página del
	 * wizard.
	 * 
	 * @return verdader si se puede pasar, falso en caso contrario
	 */
	public boolean canGoToNextPage();
	
	/**
	 * Pasa a la siguiente pagina.
	 * 
	 * @return la siguiente pagina a la que el wizard a pasado
	 */
	public RefactoringWizardPage goToNextPage();

}
