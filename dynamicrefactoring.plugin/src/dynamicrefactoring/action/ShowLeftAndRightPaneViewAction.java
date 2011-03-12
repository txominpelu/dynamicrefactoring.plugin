package dynamicrefactoring.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import dynamicrefactoring.interfaz.view.RefactoringCatalogBrowserView;

/**
 * Implementa una acción capaz de mostrar tanto el contenedor 
 * izquierdo como el derecho siendo separados por un spliter.
 */
public class ShowLeftAndRightPaneViewAction implements IViewActionDelegate{

	/**
	 * Vista para la que se implementa la acción de mostrar tanto el contenedor
	 * izquierdo como el derecho.
	 */
	private IViewPart view;
	
	/**
	 * Inicializa la acción con la vista a la que queda asociada.
	 * 
	 * @param view la vista a la que se asocia la acción de mostrar los contenedores.
	 */
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * Ejecuta la acción de mostrar los dos contenedores.
	 * 
	 * <p>Acción que se encarga de mostrar los dos contenedores 
	 * en la vista.</p>
	 * 
	 * @param action el <i>proxy</i> que representa esta acción hasta su
	 * activación.
	 */
	@Override
	public void run(IAction action) {
		if (view instanceof RefactoringCatalogBrowserView)
			((RefactoringCatalogBrowserView)view).showLeftAndRightPane();
	}

	/**
	 * Sin implementación.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
