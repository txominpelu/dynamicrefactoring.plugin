package dynamicrefactoring.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import dynamicrefactoring.interfaz.view.RefactoringCatalogBrowserView;

/**
 * Implementa una acción que muestra un editor xml de clasificaciones, desde el cual editar las 
 * clasificaciones definidas y las categorías de cada una de ellas.
 */
public class ClassificationEditorViewAction implements IViewActionDelegate{

	/**
	 * Vista para la que se implementa la acción de editor de clasificaciones.
	 */
	private IViewPart view;
	
	/**
	 * Inicializa la acción con la vista a la que queda asociada.
	 * 
	 * @param view la vista a la que se asocia la acción de editor de clasificaciones.
	 */
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
	
	/**
	 * Ejecuta la acción de editor de clasificaciones de la vista.
	 * 
	 * <p>Acción que muestra el editor de clasificaciones, desde el cual 
	 * poder editar las clasificaciones definidas y las categorías de cada 
	 * una de ellas.</p>
	 * 
	 * @param action el <i>proxy</i> que representa esta acción hasta su
	 * activación.
	 */
	@Override
	public void run(IAction action) {
		if (view instanceof RefactoringCatalogBrowserView)
			((RefactoringCatalogBrowserView)view).editClassification();
	}

	/**
	 * Sin implementación.
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}


}
