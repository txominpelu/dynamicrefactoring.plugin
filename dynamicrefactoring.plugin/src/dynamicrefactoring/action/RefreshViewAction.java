package dynamicrefactoring.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import dynamicrefactoring.interfaz.view.RefactoringCatalogBrowserView;

/**
 * Implementa una acción capaz de refrescar la vista asociada,
 * en caso de que hayan sufrido modificaciones las refactorizaciones 
 * o clasificaciones.
 */
public class RefreshViewAction implements IViewActionDelegate{

	/**
	 * Vista para la que se implementa la acción de refresco.
	 */
	private IViewPart view;
	
	/**
	 * Inicializa la acción con la vista a la que queda asociada.
	 * 
	 * @param view la vista a la que se asocia la acción de refresco.
	 */
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * Ejecuta la acción de refresco sobre la vista.
	 * 
	 * <p>Acción que refresca la vista en caso que hayan sufrido modificaciones las
	 * refactorizaciones o clasificaciones.</p>
	 * 
	 * @param action el <i>proxy</i> que representa esta acción hasta su
	 * activación.
	 */
	@Override
	public void run(IAction action) {
		if (view instanceof RefactoringCatalogBrowserView)
			((RefactoringCatalogBrowserView)view).refreshView();
	}

	/**
	 * Sin implementación.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
