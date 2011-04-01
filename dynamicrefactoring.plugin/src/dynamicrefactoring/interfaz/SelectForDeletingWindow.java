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

package dynamicrefactoring.interfaz;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.XMLRefactoringsCatalog;
import dynamicrefactoring.util.io.FileManager;

/**
 * Permite seleccionar una de las refactorizaciones din�micas disponibles para
 * ser eliminada.
 * 
 * <p>
 * Muestra en todo momento un resumen con las caracter�sticas principales de la
 * refactorizaci�n seleccionada, hasta que se pulsa el bot�n que inicia el
 * borrado de la refactorizaci�n.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectForDeletingWindow extends SelectDynamicRefactoringWindow {

	/**
	 * Crea la ventana de di�logo.
	 * 
	 * @param parentShell
	 *            la <i>shell</i> padre de esta ventana de di�logo.
	 */
	public SelectForDeletingWindow(Shell parentShell, RefactoringsCatalog refactCatalog) {
		super(parentShell, refactCatalog);

		logger = Logger.getLogger(SelectForDeletingWindow.class);
	}

	/**
	 * Crea el bot�n que permite lanzar la ejecuci�n del borrado de la
	 * refactorizaci�n seleccionada.
	 * 
	 * @see SelectDynamicRefactoringWindow#createOKButton(Composite)
	 */
	@Override
	protected void createOKButton(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				Messages.SelectForDeletingWindow_DeleteCaps, true);
	}

	/**
	 * Obtiene el verbo asociado a la acci�n que permite iniciar la ventana de
	 * di�logo sobre la refactorizaci�n seleccionada.
	 */
	@Override
	protected String getOperation() {
		return Messages.SelectForDeletingWindow_DeleteLower;
	}

	/**
	 * Notifica que el bot�n de este di�logo con el identificador especificado
	 * ha sido pulsado.
	 * 
	 * @param buttonId
	 *            el identificador del bot�n que ha sido pulsado (v�anse las
	 *            constantes <code>IDialogConstants.*ID</code>).
	 * 
	 * @see Dialog#buttonPressed
	 * @see IDialogConstants
	 */
	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == IDialogConstants.OK_ID) {

			if (l_Available.getSelectionCount() == 1) {

				DynamicRefactoringDefinition refactoring = refactCatalog
						.getRefactoring(l_Available.getSelection()[0]);

				if (isConfirmed(refactoring.getName())) {

					this.close();

					FileManager.emptyDirectories(XMLRefactoringsCatalog
							.getXmlRefactoringDefinitionFilePath(refactoring
									.getName()));
					boolean deleted = FileManager
							.deleteDirectories(
									XMLRefactoringsCatalog
											.getXmlRefactoringDefinitionFilePath(refactoring
													.getName()), true);

					Object[] messageArgs = { "\"" + refactoring.getName() + "\"" }; //$NON-NLS-1$ //$NON-NLS-2$
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					if (deleted) {
						formatter
								.applyPattern(Messages.SelectForDeletingWindow_Deleted);

						MessageDialog
								.openInformation(
										PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
										Messages.SelectForDeletingWindow_RefactoringDeleted,
										formatter.format(messageArgs) + "."); //$NON-NLS-1$
					} else {
						formatter
								.applyPattern(Messages.SelectForDeletingWindow_NotDeleted);

						MessageDialog
								.openError(
										PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
										Messages.SelectForDeletingWindow_RefactoringNotDeleted,
										formatter.format(messageArgs) + "."); //$NON-NLS-1$
					}
				}
			}
		}

		super.buttonPressed(buttonId);
	}

	/**
	 * Pide confirmaci�n al usuario para proceder a eliminar una refactorizaci�n
	 * din�mica.
	 * 
	 * @param name
	 *            nombre de la refactorizaci�n seleccionada para ser eliminada.
	 * 
	 * @return <code>true</code> si el usuario da su confirmaci�n para eliminar
	 *         la refactorizaci�n seleccionada; <code>false</code> en caso
	 *         contrario.
	 */
	private boolean isConfirmed(String name) {
		Object[] messageArgs = { "\"" + name + "\"" }; //$NON-NLS-1$ //$NON-NLS-2$
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.SelectForDeletingWindow_AreYouSure);

		return (MessageDialog.openConfirm(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),
				Messages.SelectForDeletingWindow_Confirmation,
				formatter.format(messageArgs)));
	}
}