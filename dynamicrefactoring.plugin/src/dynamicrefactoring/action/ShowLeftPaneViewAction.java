package dynamicrefactoring.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import dynamicrefactoring.interfaz.view.RefactoringCatalogBrowserView;

/**
 * Implementa una acción capaz de mostrar la totalidad del contenedor 
 * izquierdo en la vista, ocultando el contenedor derecho y el spliter
 * que los separa.
 */
public class ShowLeftPaneViewAction implements IViewActionDelegate{

	/**
	 * Vista para la que se implementa la acción de mostrar contenedor izquierdo.
	 */
	private IViewPart view;
	
	/**
	 * Identificador de la accion.
	 */
	public static final String ID = "dynamicrefactoring.viewmenus.showLeftPaneRefactoringCatalogBrowserAction"; //$NON-NLS-1$
	
	/**
	 * Inicializa la acción con la vista a la que queda asociada.
	 * 
	 * @param view la vista a la que se asocia la acción de mostrar contenedor izquierdo.
	 */
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * Ejecuta la acción de mostrar contenedor izquierdo.
	 * 
	 * <p>Acción que se encarga de mostrar la totalidad del contenedor 
	 * izquierdo en la vista unicamente.</p>
	 * 
	 * @param action el <i>proxy</i> que representa esta acción hasta su
	 * activación.
	 */
	@Override
	public void run(IAction action) {
		if (view instanceof RefactoringCatalogBrowserView){
			((RefactoringCatalogBrowserView)view).showLeftPane();
			action.setEnabled(false);
		}
	}

	/**
	 * Sin implementación.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}


}
