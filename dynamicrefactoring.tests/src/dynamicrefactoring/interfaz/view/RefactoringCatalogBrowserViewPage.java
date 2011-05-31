package dynamicrefactoring.interfaz.view;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;

public final class RefactoringCatalogBrowserViewPage {


	
	private final SWTWorkbenchBot swtBot;

	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public RefactoringCatalogBrowserViewPage(SWTWorkbenchBot bot) {
		swtBot = bot;
		SWTBotView view = swtBot.viewByTitle("Refactoring Catalog Browser");
		view.setFocus();
		bot.menu("Window").menu("Navigation").menu("Maximize Active View or Editor").click();
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

	/**
	 * Obtiene una lista de todas las categorias que se muestran
	 * en el arbol del catalogo de refactorizaciones.
	 * 
	 * @return lista de categorias que se muestran en el arbol de refactorizaciones
	 */
	public List<String> getExistingCategories(){
		return swtBot.tree().getAllItems()[0].getNodes();
	}
	
	/**
	 * Selecciona una refactorizacion para mostrarla
	 * en el panel de resumen.
	 * 
	 * @param category categoria a la que pertenece la refactorizacion en la clasificacion actualmente seleccionada
	 * @param refactoringName nombre de la refactorizacion
	 */
	public void selectRefactoring(String category, String refactoringName){
		swtBot.tree().expandNode(category).getNode(refactoringName).doubleClick();
	}
	
	/**
	 * Titulos de las pestañas del panel de resumen
	 * de los datos de la refactorizacion
	 * 
	 * @return titulos de las pestañas del panel de resumen
	 */
	public List<String> getSummaryPanelTabsTitles(){
		
		List<String> titles = new ArrayList<String>();
		for(TabItem item : swtBot.getFinder().findControls(widgetOfType(TabItem.class))){
			SWTBotTabItem tabItem = new SWTBotTabItem(item);
			titles.add(tabItem.getText());
		}
		return titles;
	}
	


}
