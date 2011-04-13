package dynamicrefactoring.interfaz;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Proporciona el orden en el que se desea visualizar la lista
 * de refactorizaciones. Este orden atiende al nombre de las mismas.
 * @author XPMUser
 *
 */
public class RefactoringListSorter extends ViewerSorter{

	@Override
	public int compare(Viewer viewer, Object e1, Object e2){
		if (e1 instanceof DynamicRefactoringDefinition && 
			e2 instanceof DynamicRefactoringDefinition) {
			return ((DynamicRefactoringDefinition)e1).getName()
						.compareToIgnoreCase(((DynamicRefactoringDefinition)e2).getName());
		}
		return e1.toString().compareToIgnoreCase(e2.toString());
	}

	
}
