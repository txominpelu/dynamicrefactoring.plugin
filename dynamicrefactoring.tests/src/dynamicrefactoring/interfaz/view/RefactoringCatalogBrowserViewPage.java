package dynamicrefactoring.interfaz.view;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;

public class RefactoringCatalogBrowserViewPage {


	
	private final SWTWorkbenchBot swtBot;
	private SWTBotView view;

	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public RefactoringCatalogBrowserViewPage(SWTWorkbenchBot bot) {
		swtBot = bot;
		if(swtBot.activeView().getTitle().equals("Welcome")){
			swtBot.activeView().close();
		}
		view = swtBot.viewByTitle("Refactoring Catalog Browser");
		view.setFocus();
		
	}
	
	/**
	 * Hace que en la vista las refactorizaciones se clasifiquen en base
	 * a la nueva clasificacion pasada.
	 * 
	 * @param classification nombre de la clasificacion a asignar
	 */
	public void changeSelectedClassification(String classification){
		swtBot.comboBoxWithLabel("Classification:").setSelection(classification);
	}
	

	public void getCategoryForRefactoring(String refactoringName){
		
	}
	
	

}
