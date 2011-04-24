package dynamicrefactoring.interfaz.wizard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;

import dynamicrefactoring.RefactoringPlugin;

public final class RefactoringWizardPage1Object extends AbstractRefactoringWizardPage{

	public static final String DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT = "Dynamic Refactoring Wizard";
	public static final String DYNAMIC_REFACTORING_MENU_TEXT = Platform
			.getResourceString(RefactoringPlugin.getDefault().getBundle(),
					"%dynamicrefactoring.menu.main");
	private static final String NEW_REFACTORING_MENU_TEXT = "New Refactoring...";


	/**
	 * Crea un PageObject que permite a los tests de interfaz acceder a la
	 * primera pagina del interfaz de crear/editar una refactorizacion.
	 */
	public RefactoringWizardPage1Object(SWTWorkbenchBot bot) {
		super(bot);
		getBot().menu(DYNAMIC_REFACTORING_MENU_TEXT)
				.menu(NEW_REFACTORING_MENU_TEXT).click();
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
						.forceActive();
			}
		});
		bot.waitUntil(Conditions.shellIsActive(DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT));
		getBot().shell(DYNAMIC_REFACTORING_WIZARD_SHELL_TEXT).activate();
	}

	/**
	 * Cambia el estado checked en una categoria del arbol de categorias a las
	 * que la refactorizacion pertenece.
	 * 
	 * @param classification
	 *            clasificacion a la que la categoria pertenece.
	 * @param category
	 *            categoria
	 */
	public void checkCategory(String classification, String category) {
		if (getBot().tree().expandNode(classification).getNode(category)
				.isChecked()) {
			getBot().tree().expandNode(classification).getNode(category)
					.uncheck();
		} else {
			getBot().tree().expandNode(classification).getNode(category).check();
		}
	}

	/**
	 * Cambia el estado checked en una clasificacion del arbol de categorias a
	 * las que la refactorizacion pertenece.
	 * 
	 * @param classification
	 *            clasificacion a la que la categoria pertenece.
	 * @param category
	 *            categoria
	 */
	public void checkClassification(String classification) {
		if (getBot().tree().getTreeItem(classification).isChecked()) {
			getBot().tree().getTreeItem(classification).uncheck();
		} else {
			getBot().tree().getTreeItem(classification).check();
		}

	}

	/**
	 * Asigna un nombre en el cuadro de texto del nombre de la refactorizacion.
	 * 
	 * @param name
	 *            nombre a asignar a la refactorizacion.
	 */
	public void setName(String name) {
		getBot().textWithLabel("Name").setText(name);
	}

	/**
	 * Obtiene el texto que tiene el campo de texto de nombre de la
	 * refactorizacion.
	 * 
	 * @return texto que contiene el campo de texto de nombre de la
	 *         refactorizacion
	 */
	public String getName() {
		return getBot().textWithLabel("Name").getText();
	}

	/**
	 * Asigna un descripcion en el cuadro de texto del descripcion de la
	 * refactorizacion.
	 * 
	 * @param description
	 *            descripcion a asignar a la refactorizacion.
	 */
	public void setDescription(String description) {
		getBot().textWithLabel("Description").setText(description);
	}

	/**
	 * Obtiene el texto que tiene el campo de texto de descripcion de la
	 * refactorizacion.
	 * 
	 * @return texto que contiene el campo de texto de descripcion de la
	 *         refactorizacion
	 */
	public String getDescription() {
		return getBot().textWithLabel("Description").getText();
	}
	
	/**
	 * Asigna un motivacion en el cuadro de texto del motivacion de la
	 * refactorizacion.
	 * 
	 * @param motivation
	 *            motivacion a asignar a la refactorizacion.
	 */
	public void setMotivation(String motivation) {
		getBot().textWithLabel("Motivation").setText(motivation);
	}

	/**
	 * Obtiene el texto que tiene el campo de texto de motivacion de la
	 * refactorizacion.
	 * 
	 * @return texto que contiene el campo de texto de motivacion de la
	 *         refactorizacion
	 */
	public String getMotivation() {
		return getBot().textWithLabel("Motivation").getText();
	}

	/**
	 * Comprueba si una categoria esta marcada en el arbol de categorias de la
	 * refactorizacion.
	 * 
	 * @param classification
	 *            clasificacion a la que la categoria pertenece.
	 * @param category
	 *            categoria
	 */
	public boolean isCategoryChecked(String classification, String category) {
		return getBot().tree().expandNode(classification).getNode(category)
				.isChecked();
	}
	
	/**
	 * Pasa a la siguie.
	 * 
	 * @return verdader si se puede pasar, falso en caso contrario
	 */
	public RefactoringWizardPage goToNextPage() {
		Preconditions.checkArgument(canGoToNextPage());
		getBot().button("Next").click();
		return new RefactoringWizardPage2Object(getBot());
	}

}
