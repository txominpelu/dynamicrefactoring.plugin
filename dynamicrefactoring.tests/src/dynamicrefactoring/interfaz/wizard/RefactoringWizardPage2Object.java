package dynamicrefactoring.interfaz.wizard;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;


/**
 * PageObject que facilita los tests de la interfaz gráfica y se corresponde con
 * la última página del wizard de creacion/edicion de una refactorizacion.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 * 
 */
public final class RefactoringWizardPage2Object extends AbstractRefactoringWizardPage implements RefactoringWizardPage {
	
	private static final String INPUTS_LIST_ID = "inputs";

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
		getBot().listWithId(INPUTS_LIST_ID).select(inputName);
		getBot().checkBoxInGroup("Parameters").click();
	}

	@Override
	public RefactoringWizardPage goToNextPage() {
		return null;
	}
	
	/**
	 * Asigna el nombre pasado a la entrada con el tipo pasado.
	 * 
	 * @param name nombre a asignar
	 * @param input texto de la entrada
	 */
	public void setNameForInput(String name, String input){
		getBot().listWithId(INPUTS_LIST_ID).select(input);
		getBot().textWithLabel("Name").setFocus();
		getBot().textWithLabel("Name").setText(name);
		getBot().listWithId(INPUTS_LIST_ID).setFocus();
		getBot().listWithId(INPUTS_LIST_ID).select(0);
	}

	/**
	 * Agrega la entrada pasada a la lista de entradas
	 * de la refactorizacion.
	 * 
	 * @param inputType tipo del parametro de entrada a agregar
	 */
	public void addInput(String inputType) {
		getBot().listWithId("types").select(inputType);
		getBot().buttonWithId("addInput").click();
		
	}

	/**
	 * Devuelve la lista de resultados que se muestran
	 * en el panel de busqueda de elementos.
	 * 
	 * @return lista de resultados del panel de elementos
	 */
	public List<String> getSearchResults() {
		return Arrays.asList(getBot().list().getItems());
	}

	/**
	 * Realiza una busqueda sobre los elementos
	 * en el panel de busquedas de la pagina.
	 * 
	 * @param query busqueda a realizar
	 */
	public void searchElement(String query) {
		getBot().text().setText(query);
		getBot().button().click();
	}
	

}
