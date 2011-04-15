package dynamicrefactoring.interfaz.view.classifeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;


public final class ClassifEditorPage {
	
	private final SWTFormsBot swtBot;

	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public ClassifEditorPage(SWTWorkbenchBot bot) {
		if(bot.activeView().getTitle().equals("Welcome")){
			bot.activeView().close();
		}
		
		SWTBotView view = bot.viewByTitle("Refactoring Catalog Browser");
		view.setFocus();
		view.toolbarButton("Classification XML Editor").click();
		bot.menu("Window").menu("Navigation").menu("Maximize Active View or Editor").click();
		swtBot = new SWTFormsBot();
	}
	
	/**
	 * Agrega una clasificacion desde el editor.
	 * 
	 * @param nombre de la clasificacion a crear
	 */
	public void addClassification(String newClassificationName){
		swtBot.buttonWithTooltip(ClassifListSection.ADD_CLASSIFICATION_BUTTON_TOOLTIP).click();
		swtBot.text().setText(newClassificationName);
		swtBot.button("OK").click();
		
	}
	
	/**
	 * Devuelve la lista de clasificaciones.
	 * 
	 * @return lista de clasificaciones mostradas en el editor
	 */
	public List<String> getClassifications(){
		List<String> lista = new ArrayList<String>();
		for (int row=0 ; row < swtBot.tableWithLabel("Classifications").rowCount(); row++){
			lista.add(swtBot.tableWithLabel("Classifications").getTableItem(row).getText());
		}
		return lista;
	}

}
