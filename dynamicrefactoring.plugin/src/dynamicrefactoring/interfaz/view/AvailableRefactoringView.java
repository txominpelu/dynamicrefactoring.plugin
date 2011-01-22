/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente De La Fuente

This file is part of Foobar

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.view;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.SelectionListenerRegistry;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.integration.selectionhandler.ISelectionHandler;
import dynamicrefactoring.integration.selectionhandler.SelectionHandlerFactory;
import dynamicrefactoring.interfaz.dynamic.DynamicRefactoringWindowLauncher;
import dynamicrefactoring.listener.IMainSelectionListener;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.RefactoringTreeManager;
import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.util.selection.SelectionInfo;

/**
 * Proporciona una vista de Eclipse en la que quedan reflejadas todas las
 * refactorizaciones disponibles para el objeto seleccionado.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class AvailableRefactoringView extends ViewPart {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(AvailableRefactoringView.class);

	/**
	 * Identificador de la vista.
	 */
	public static final String ID = "dynamicrefactoring.views.availableRefactoringView"; //$NON-NLS-1$


	/**
	 * Refactorizaciones cargadas en la vista actualmente.
	 */
	private HashMap<String, String> refactorings;

	/**
	 * Última seleción válida como entrada para una refactorizacion en el
	 * espacio de trabajo.
	 */
	private SelectionInfo select;

	/**
	 * Árbol sobre el que se mostrarán de forma estructurada las diferentes refactorizaciones disponibles
	 * para el elemento selecionado en el espacio de trabajo.
	 */
	private Tree tr_Refactorings;

	/**
	 * Crea los controles SWT para este componente del espacio de trabajo.
	 * 
	 * @param parent
	 *            el control padre.
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.setPartName(Messages.AvailableRefactoringView_title);

		// Registramos el listener de la vista
		SelectionListenerRegistry.getInstance().addListener(
				new MainSelectionListener());

		tr_Refactorings = new Tree(parent, SWT.NULL);
		tr_Refactorings.addMouseListener(new TreeMouseListener());

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

	/**
	 * Permite responder a los eventos generados por la selección de un elemento
	 * válido como entrada para una refactorización
	 * {@link AvailableRefactoringView}.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 */
	private class MainSelectionListener implements IMainSelectionListener {

		/**
		 * Notifica al <i>listener</i> que un objeto válido como entrada para la
		 * refactorización ha sido seleccionado.
		 * 
		 * @param selection
		 *            selección del espacio de trabajo.
		 */
		public void elementSelected(SelectionInfo selection) {
			if (selection.isValidSelectionType()) {
				select = selection;
				try {
					if (selection.existsScopeForSelection()) {
						refactorings = JDOMXMLRefactoringReaderImp
								.readAvailableRefactorings(
										selection.getSelectionScope(),
										RefactoringConstants.REFACTORING_TYPES_FILE);
						try {
							RefactoringTreeManager.fillTree(refactorings, tr_Refactorings);
						} catch (RefactoringException e) {
							logger.error(Messages.AvailableRefactoringView_NotRepresented
									+ ".\n" + e.getMessage()); //$NON-NLS-1$
						}
					}
				} catch (XMLRefactoringReaderException e) {
					logger.error(Messages.AvailableRefactoringView_ReaderFail
							+ ".\n" + e.getMessage()); //$NON-NLS-1$
				}
			} else {
				RefactoringTreeManager.cleanTree(tr_Refactorings);
			}
		}
	}

	/**
	 * Recibe notificaciones cuando uno de los elementos de la lista de tipos es
	 * seleccionado.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 */
	private class TreeMouseListener implements MouseListener {

		/**
		 * Recibe una notificación de que un elemento del árbol que forma la
		 * vista ha sido presionado doblemente con el ratón.
		 * 
		 * @param e
		 *            el evento de selección disparado en la ventana.
		 * 
		 * @see MouseListener#mouseDoubleClick(MouseEvent)
		 */
		public void mouseDoubleClick(MouseEvent e) {
			TreeItem[] selection = tr_Refactorings.getSelection();
			// Si se ha señalado el nombre de la refactorización
			String selectedName = selection[0].getText();
			if (selection[0].getParentItem() == null) {
				try {
					if (saveUnsavedChanges()) {
						// generamos el modelo
						ModelGenerator.getInstance().generateModel(select,
								true, false);

						RefactoringPlugin mediator = RefactoringPlugin
								.getDefault();

						SelectionHandlerFactory factory = SelectionHandlerFactory
								.getInstance();
						ISelectionHandler handler = factory
								.createHandler(select);

						if (handler != null)
							mediator.runRefactoring(handler.getMainObject(),
									select, false);

						String name = FileManager.getFileName(refactorings
								.get(selectedName));
						new DynamicRefactoringWindowLauncher(
								handler.getMainObject(),
								FileManager.getFilePathWithoutExtension(name));
					}
				} catch (Exception excp) {
					excp.printStackTrace();
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					MessageDialog.openError(window.getShell(),
							Messages.AvailableRefactoringView_Error,
							Messages.AvailableRefactoringView_ErrorOccurred
									+ ".\n" //$NON-NLS-1$
									+ excp.getMessage());
				}
			}
		}

		/**
		 * @see MouseListener#mouseDown(MouseEvent)
		 */
		public void mouseDown(MouseEvent e) {
		}

		/**
		 * @see MouseListener#mouseUp(MouseEvent)
		 */
		public void mouseUp(MouseEvent e) {
		}

	}

	/**
	 * Pide confirmación al usuario para guardar los cambios pendientes antes de
	 * continuar. Si obtiene la confirmación, guarda los cambios de todos los
	 * editores abiertos con cambios pendientes.
	 * 
	 * @return <code>true</code> si se guardaron las modificaciones pendientes o
	 *         si no había ninguna pendiente; <code>false</code> en caso
	 *         contrario.
	 */
	public boolean saveUnsavedChanges() {
		// FIXME: Esto no deberia estar aqui es util para muchos
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		// Se recogen los editores abiertos con cambios sin guardar.
		IEditorPart[] dirtyEditors = window.getActivePage().getDirtyEditors();

		if (dirtyEditors.length > 0) {

			if (MessageDialog.openConfirm(window.getShell(), "WARNING", //$NON-NLS-1$
					Messages.AvailableRefactoringView_SaveChanges)) {
				// Se guardan los cambios de cada editor.
				for (int i = 0; i < dirtyEditors.length; i++)
					dirtyEditors[i].doSave(new NullProgressMonitor());
				return true;
			}

			return false;
		} else
			return true;
	}

	/**
	 * Limpia la vista.
	 */
	public void cleanView() {
		RefactoringTreeManager.cleanTree(tr_Refactorings);
	}

}