package dynamicrefactoring.interfaz.wizard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.RefactoringPlugin;

public class RefactoringWizardPage1Object {
	
	private static final String DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT = "Dynamic Refactoring Wizard";
	private static final String DYNAMIC_REFACTORING_MENU_TEXT = Platform.getResourceString(RefactoringPlugin.getDefault().getBundle(), "%dynamicrefactoring.menu.main");
	private static final String NEW_REFACTORING_MENU_TEXT = "New Refactoring...";
	
	private static final SWTWorkbenchBot BOT = new SWTWorkbenchBot();
	

	/**
	 * Crea un PageObject que permite a los tests de interfaz
	 * acceder a la primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public RefactoringWizardPage1Object(){
		UIThreadRunnable.syncExec(new VoidResult() {
            public void run() {
            	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                        .forceActive();
            }
        });

		BOT.menu(DYNAMIC_REFACTORING_MENU_TEXT).menu(NEW_REFACTORING_MENU_TEXT).click();
	}
	
	/**
	 * Cambia el estado checked en una categoria del arbol
	 * de categorias a las que la refactorizacion pertenece.
	 * 
	 * @param classification clasificacion a la que la categoria pertenece.
	 * @param category categoria
	 */
	public final void checkCategory(String classification, String category){
		if (BOT.tree().expandNode(classification).getNode(category).isChecked()){
			BOT.tree().expandNode(classification).getNode(category).uncheck();
		}else {
			BOT.tree().expandNode(classification).getNode(category).check();
		}
	}
	
	
	/**
	 * Cambia el estado checked en una clasificacion del arbol
	 * de categorias a las que la refactorizacion pertenece.
	 * 
	 * @param classification clasificacion a la que la categoria pertenece.
	 * @param category categoria
	 */
	public final void checkClassification(String classification){
		if(BOT.tree().getTreeItem(classification).isChecked()){
			BOT.tree().getTreeItem(classification).uncheck();
		}else{
			BOT.tree().getTreeItem(classification).check();
		}
		
	}
	
	/**
	 * Comprueba si una categoria esta marcada en el arbol
	 * de categorias de la refactorizacion.
	 * 
	 * @param classification clasificacion a la que la categoria pertenece.
	 * @param category categoria
	 */
	public final boolean isCategoryChecked(String classification, String category){
		return BOT.tree().expandNode(classification).getNode(category).isChecked();
	}
	
	/**
	 * Cierra la ventana y finaliza el wizard de refactorizacion.
	 */
	public final void cancelWizard(){
		BOT.activeShell().close();
	}

}
