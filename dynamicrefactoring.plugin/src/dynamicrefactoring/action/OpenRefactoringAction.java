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

package dynamicrefactoring.action;

import java.io.IOException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.integration.selectionhandler.ISelectionHandler;
import dynamicrefactoring.integration.selectionhandler.SelectionHandlerFactory;
import dynamicrefactoring.util.selection.SelectionInfo;
import dynamicrefactoring.util.selection.SelectionInfoFactory;

/**
 * Acción que inicia el asistente de ejecución de refactorizaciones.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */

public class OpenRefactoringAction implements IWorkbenchWindowActionDelegate,
		IObjectActionDelegate, IEditorActionDelegate {

	/**
	 * La ventana activa de trabajo.
	 */
	private IWorkbenchWindow window;

	/**
	 * Constructor for OpenRefactoringAction.
	 */
	public OpenRefactoringAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IEditorActionDelegate#setActiveEditor(IAction, IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart editorPart) {
	}

	/**
	 * Recibe notificaciones cada vez que se modifica la selección de la
	 * ventana.
	 * 
	 * @param action
	 *            no se utiliza.
	 * @param selection
	 *            no se utiliza.
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * Sin implementación.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * Se guarda en caché el objeto ventana, con objeto de poder obtener la
	 * <code>shell</code> sobre la que abrir los mensajes de diálogo.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Implementa las operaciones que deben ejecutarse cuando se invoque la
	 * acción.
	 * 
	 * @param action
	 *            la acción que ha sido invocada.
	 */
	public void run(IAction action) {
		// Si la acción se ha disparado desde un menú desplegable,
		// hay que recuperar la ventana de trabajo de forma manual.
		if (window == null)
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		IWorkbenchPage current = (window.getPages().length > 0) ? window
				.getPages()[0] : null;
		ISelection selection = (current != null) ? current.getSelection()
				: null;

		if (selection != null && !selection.isEmpty()) {

			try {
				SelectionInfo selectionInfo = SelectionInfoFactory
						.getInstance().createSelectionInfo(selection, window);

				// Si el elemento seleccionado es válido como entrada para la
				// refactorización.
				if (selectionInfo.isValidSelectionType()
						&& saveUnsavedChanges()
						&& generateMOONModel(selectionInfo, true)
						&& canRunRefactoringForSelection(selectionInfo)) {

					RefactoringPlugin.getDefault().runRefactoring(
							SelectionHandlerFactory.getInstance()
									.createHandler(selectionInfo)
									.getMainObject(), selectionInfo, true);
				} else {
					openNoneAvailableMessage();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(window.getShell(),
						Messages.OpenRefactoringAction_Error,
						Messages.OpenRefactoringAction_ErrorOccurred + ".\n" //$NON-NLS-1$
								+ e.getMessage());
			}
		} else {
			MessageDialog.openInformation(window.getShell(),
					Messages.OpenRefactoringAction_Warning,
					Messages.OpenRefactoringAction_NoSelection + "."); //$NON-NLS-1$
		}
	}

	private boolean canRunRefactoringForSelection(SelectionInfo selectionInfo)
			throws Exception, ClassNotFoundException, IOException {
		RefactoringPlugin mediator = RefactoringPlugin.getDefault();

		SelectionHandlerFactory factory = SelectionHandlerFactory.getInstance();
		ISelectionHandler handler = factory.createHandler(selectionInfo);
		boolean canRunRefactoring = (handler != null && handler.getMainObject() != null);
		return canRunRefactoring;
	}

	/**
	 * Intenta generar el modelo MOON para el proyecto al que pertenece la
	 * selección indicada.
	 * 
	 * @param selectionInfo
	 *            la selección para la que se desea crear el modelo.
	 * 
	 * @param createwindow
	 *            indica si se quiere mostrar al usuario una ventana indicando
	 *            el proceso de generación del modelo o no.
	 * 
	 * @return <code>true</code> si se pudo crear el modelo correctamente;
	 *         <code>
	 * false</code> en caso contrario.
	 */
	private boolean generateMOONModel(SelectionInfo selectionInfo,
			boolean createwindow) {

		if (ModelGenerator.getInstance().generateModel(selectionInfo,
				createwindow, false))
			return true;
		else {
			MessageDialog.openError(window.getShell(),
					Messages.OpenRefactoringAction_Error,
					Messages.OpenRefactoringAction_4 + "."); //$NON-NLS-1$
			return false;
		}
	}

	/**
	 * Muestra un mensaje de aviso indicando que no existen refactorizaciones
	 * disponibles para la selección actual.
	 */
	private void openNoneAvailableMessage() {
		MessageDialog.openInformation(window.getShell(),
				Messages.OpenRefactoringAction_Warning,
				Messages.OpenRefactoringAction_NoRefactoringAvailable + "."); //$NON-NLS-1$
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
	private boolean saveUnsavedChanges() {
		// Se recogen los editores abiertos con cambios sin guardar.
		IEditorPart[] dirtyEditors = window.getActivePage().getDirtyEditors();

		if (dirtyEditors.length > 0) {

			if (MessageDialog.openConfirm(window.getShell(),
					Messages.OpenRefactoringAction_Warning,
					Messages.OpenRefactoringAction_SaveChanges
							+ Messages.OpenRefactoringAction_CannontContinue)) {
				// Se guardan los cambios de cada editor.
				for (int i = 0; i < dirtyEditors.length; i++)
					dirtyEditors[i].doSave(new NullProgressMonitor());
				return true;
			}

			return false;
		} else
			return true;
	}
}