package dynamicrefactoring.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import dynamicrefactoring.interfaz.view.AvailableRefactoringView;

/**
 * 
 * Implementa una acción capaz mostrar en la vista asociada las refactorizaciones
 * no editables, es decir, las que se suministran con el plugin.
 * 
 */
public class ShowNonEditableRefactoringsViewAction implements IViewActionDelegate{

	/**
	 * Vista para la que se implementa la acción de mostrar las 
	 * refactorizaciones no editables.
	 */
	private IViewPart view;
	
	/**
	 * Inicializa la acción con la vista a la que queda asociada.
	 * 
	 * @param view la vista a la que se asocia la acción de mostrar las
	 * refactorizaciones no editables.
	 */
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * Ejecuta la acción de mostrar en la vista las refactorizaciones no editables.
	 * 
	 * <p>Acción que muestra en la vista las refactorizaciones no editables,
	 *  es decir, las que se suministran con el plugin.</p>
	 * 
	 * @param action el <i>proxy</i> que representa esta acción hasta su
	 * activación.
	 */
	@Override
	public void run(IAction action) {
		if (view instanceof AvailableRefactoringView)
			((AvailableRefactoringView)view).showNonEditableRefactorings();
	}

	/**
	 * Sin implementación.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}