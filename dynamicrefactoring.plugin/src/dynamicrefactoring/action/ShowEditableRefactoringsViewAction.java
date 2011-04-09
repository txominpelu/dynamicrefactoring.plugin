package dynamicrefactoring.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import dynamicrefactoring.interfaz.view.AvailableRefactoringView;


/**
 * 
 * Implementa una acción capaz mostrar en la vista asociada las refactorizaciones
 * editables, es decir, las propias del usuario, en caso de haberlas.
 * 
 */
public class ShowEditableRefactoringsViewAction implements IViewActionDelegate{

	/**
	 * Vista para la que se implementa la acción de mostrar las 
	 * refactorizaciones editables.
	 */
	private IViewPart view;
	
	/**
	 * Inicializa la acción con la vista a la que queda asociada.
	 * 
	 * @param view la vista a la que se asocia la acción de mostrar las
	 * refactorizaciones editables.
	 */
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * Ejecuta la acción de mostrar en la vista las refactorizaciones editables.
	 * 
	 * <p>Acción que muestra en la vista las refactorizaciones editables,
	 *  es decir, las propias del usuario, en caso de haberlas.</p>
	 * 
	 * @param action el <i>proxy</i> que representa esta acción hasta su
	 * activación.
	 */
	@Override
	public void run(IAction action) {
		if (view instanceof AvailableRefactoringView)
			((AvailableRefactoringView)view).showEditableRefactorings();
	}

	/**
	 * Sin implementación.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
