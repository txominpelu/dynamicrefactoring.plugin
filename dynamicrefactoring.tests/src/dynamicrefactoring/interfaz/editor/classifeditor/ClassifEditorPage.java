package dynamicrefactoring.interfaz.editor.classifeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;

import dynamicrefactoring.interfaz.editor.classifeditor.ClassifListSection;
import dynamicrefactoring.interfaz.editor.classifeditor.Messages;


public final class ClassifEditorPage {
	
	private final SWTFormsBot swtEditorFormsBot;
	private SWTWorkbenchBot swtEclipseBot;

	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public ClassifEditorPage(SWTWorkbenchBot bot) {
		SWTBotView view = bot.viewByTitle("Refactoring Catalog Browser");
		view.setFocus();
		view.toolbarButton("Classification XML Editor").click();
		bot.menu("Window").menu("Navigation").menu("Maximize Active View or Editor").click();
		this.swtEclipseBot = bot;
		swtEditorFormsBot = new SWTFormsBot();
	}
	
	/**
	 * Agrega una clasificacion desde el editor.
	 * 
	 * @param nombre de la clasificacion a crear
	 */
	public void addClassification(String newClassificationName){
		swtEditorFormsBot.buttonWithTooltip(ClassifListSection.ADD_CLASSIFICATION_BUTTON_TOOLTIP).click();
		swtEditorFormsBot.text().setText(newClassificationName);
		swtEditorFormsBot.button("OK").click();
		
	}
	
	/**
	 * Devuelve la lista de clasificaciones.
	 * 
	 * @return lista de clasificaciones mostradas en el editor
	 */
	public List<String> getClassifications(){
		List<String> lista = new ArrayList<String>();
		for (int row=0 ; row < swtEditorFormsBot.tableWithLabel("Classifications").rowCount(); row++){
			lista.add(swtEditorFormsBot.tableWithLabel("Classifications").getTableItem(row).getText());
		}
		return lista;
	}

	/**
	 * Elimina una clasificacion desde el editor.
	 * 
	 * @param classification nombre de la clasificacion a eliminar
	 */
	public void removeClassification(String classification) {
		swtEditorFormsBot.tableWithLabel(Messages.ClassifListSection_Classifications).select(classification);
		swtEditorFormsBot.buttonWithTooltip(Messages.ClassifListSection_DeleteClassificationToolTip).click();
		swtEditorFormsBot.button(Messages.ClassifListSection_Proceed).click();
	}
	
	/**
	 * Elimina una clasificacion desde el editor.
	 * 
	 * @param nombre de la clasificacion a eliminar
	 */
	public void closeEditor() {
		swtEclipseBot.activeEditor().close();
	}

	/**
	 * Renombra una clasificacion.
	 * 
	 * @param classification nombre actual de la clasificacion
	 * @param newName nuevo nombre que la clasificacion tomara
	 */
	public void renameClassification(String classification, String newName) {
		swtEditorFormsBot.tableWithLabel(Messages.ClassifListSection_Classifications).select(classification);
		swtEditorFormsBot.buttonWithTooltip(Messages.ClassifListSection_RenameClassificationToolTip).click();
		swtEditorFormsBot.text().setText(newName);
		swtEditorFormsBot.button("OK").click();
	}

}
