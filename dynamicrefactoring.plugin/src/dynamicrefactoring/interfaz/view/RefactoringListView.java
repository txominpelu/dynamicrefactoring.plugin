package dynamicrefactoring.interfaz.view;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * Proporcia una vista de Eclipse la cual muestra la lista de todas las
 * refactorizaciones asi como la información asociada a las mismas.
 * Sobre estas se pueden realizar clasificaciones y filtros.
 */
public class RefactoringListView extends ViewPart {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(RefactoringListView.class);

	/**
	 * Identificador de la vista.
	 */
	public static final String ID = "dynamicrefactoring.views.refactoringListView"; //$NON-NLS-1$


	/**
	 * Crea los controles SWT para este componente del espacio de trabajo.
	 * 
	 * @param parent
	 *            el control padre.
	 */
	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.RefactoringListView_title);

	}
	
	/**
	 * Oculta la vista.
	 */
	@Override
	public void dispose() {
	}
	
	/**
	 * Solicita a la vista que tome el foco en el espacio de trabajo.
	 */
	@Override
	public void setFocus() {
	}

}
