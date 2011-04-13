package dynamicrefactoring.interfaz;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Filtro para que únicamente las refactorizaciones del
 * usuario sean visibles en la lista de refactorizaciones.
 * @author XPMUser
 *
 */
public class RefactoringListFilter extends ViewerFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if (element instanceof DynamicRefactoringDefinition){
			return ((DynamicRefactoringDefinition) element).isEditable();
		}
		return true;
	}

}
