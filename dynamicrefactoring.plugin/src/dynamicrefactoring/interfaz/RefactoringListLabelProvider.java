package dynamicrefactoring.interfaz;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Porveedor de etiquetas para la lista de refactorizaciones.
 * @author XPMUser
 *
 */
public class RefactoringListLabelProvider extends LabelProvider implements ITableLabelProvider{

	/**
	 * Dado el elemento refactorización devuelve la imagen asociada 
	 * que se debe mostar en la lista de refactorizaciones para dicho
	 * elemento.
	 * 
	 * @param element elemento refactorización
	 * @param columnIndex indice de la columna
	 * @return imagen a mostrar en la lista de refactorizaciones para el elemento
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof DynamicRefactoringDefinition) {
			if(((DynamicRefactoringDefinition) element).isEditable())
				return RefactoringImages.getRefIcon();
			else
				return RefactoringImages.getPluginRefIcon();
		}
		return null;
	}

	/**
	 * Dado el elemento refactorización devuelve el texto asociado 
	 * que se debe mostar en la lista de refactorizaciones para dicho
	 * elemento. Corresponde con el nombre de la refactorización.
	 * 
	 * @param element elemento refactorización
	 * @param columnIndex indice de la columna
	 * @return texto a mostrar en la lista de refactorizaciones para el elemento
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof DynamicRefactoringDefinition) {
			return ((DynamicRefactoringDefinition) element).getName();
		}
		return element.toString();
	}
}