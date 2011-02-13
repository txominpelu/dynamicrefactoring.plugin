package dynamicrefactoring.interfaz.wizard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

import dynamicrefactoring.RefactoringPlugin;

public class RefactoringWizardPage1Object {
	
	private static final String DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT = "Dynamic Refactoring Wizard";
	private static final String DYNAMIC_REFACTORING_MENU_TEXT = Platform.getResourceString(RefactoringPlugin.getDefault().getBundle(), "%dynamicrefactoring.menu.main");
	private static final String NEW_REFACTORING_MENU_TEXT = "New Refactoring...";
	
	private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();
	

	/**
	 * Crea un PageObject que permite a los tests de interfaz
	 * acceder a la primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public RefactoringWizardPage1Object(){
		bot.menu(DYNAMIC_REFACTORING_MENU_TEXT).menu(NEW_REFACTORING_MENU_TEXT).click();
		bot.shell(DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT).activate();
	}
	
	/**
	 * Hace check en una categoria del arbol
	 * de categorias a las que la refactorizacion pertenece.
	 * 
	 * @param classification clasificacion a la que la categoria pertenece.
	 * @param category categoria
	 */
	public void checkCategory(String classification, String category){
		bot.tree().expandNode(classification).getNode(category).check();
	}
	
	/**
	 * Comprueba si una categoria esta marcada en el arbol
	 * de categorias de la refactorizacion.
	 * 
	 * @param classification clasificacion a la que la categoria pertenece.
	 * @param category categoria
	 */
	public boolean isCategoryChecked(String classification, String category){
		return bot.tree().expandNode(classification).getNode(category).isChecked();
	}
	
	/**
	 * Cierra la ventana y finaliza el wizard de refactorizacion.
	 */
	public void finish(){
		bot.activeShell().close();
	}

}
