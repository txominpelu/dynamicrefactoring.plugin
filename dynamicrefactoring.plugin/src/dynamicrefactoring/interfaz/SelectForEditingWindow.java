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

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.interfaz.wizard.RefactoringWizard;

/**
 * Permite seleccionar una de las refactorizaciones dinámicas disponibles para
 * ser editada.
 * 
 * <p>
 * Muestra en todo momento un resumen con las características principales de la
 * refactorización seleccionada, hasta que se pulsa el botón que inicia el
 * asistente para la edición de refactorizaciones.
 * </p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectForEditingWindow extends SelectDynamicRefactoringWindow {

	/**
	 * Crea la ventana de diálogo.
	 * 
	 * @param parentShell
	 *            la <i>shell</i> padre de esta ventana de diálogo.
	 */
	public SelectForEditingWindow(Shell parentShell,
			RefactoringsCatalog refactCatalog) {
		super(parentShell, refactCatalog);
		logger = Logger.getLogger(SelectForEditingWindow.class);
	}

	/**
	 * Crea el resto de botones necesarios en el dialogo que permiten lanzar las
	 * distintas funcionalidades del dialogo. Entre ellos se encuentra el botón
	 * OK.
	 * 
	 * @param parent
	 *            el componente padre del botón.
	 */
	protected void createOtherButtons(Composite parent) {
		createButton(parent, IDialogConstants.CLIENT_ID,
				Messages.SelectForEditingWindow_CreateFrom, true);
		getButton(IDialogConstants.CLIENT_ID).setEnabled(false);
		super.createOtherButtons(parent);
	}

	/**
	 * Crea el botón que permite lanzar el asistente para la edición de la
	 * refactorización seleccionada.
	 * 
	 * @param parent
	 *            el componente padre del botón.
	 * 
	 * @see SelectDynamicRefactoringWindow#createOKButton(Composite)
	 */
	@Override
	protected void createOKButton(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				Messages.SelectForEditingWindow_EditCaps, false);
	}

	/**
	 * Obtiene el verbo asociado a la acción que permite iniciar la ventana de
	 * diálogo sobre la refactorización seleccionada.
	 * 
	 * @return el verbo asociado a la acción que permite iniciar la ventana de
	 *         diálogo sobre la refactorización seleccionada.
	 */
	@Override
	protected String getOperation() {
		return Messages.SelectForEditingWindow_EditLower;
	}

	/**
	 * Notifica que el botón de este diálogo con el identificador especificado
	 * ha sido pulsado.
	 * 
	 * @param buttonId
	 *            el identificador del botón que ha sido pulsado (véanse las
	 *            constantes <code>IDialogConstants.*ID</code>).
	 * 
	 * @see Dialog#buttonPressed
	 * @see IDialogConstants
	 */
	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId != IDialogConstants.CANCEL_ID) {
			Table availableList = availableRefListViewer.getTable();
			if (availableList.getSelectionCount() == 1) {
				Object obj = availableList.getSelection()[0].getData();
				if (obj instanceof DynamicRefactoringDefinition) {

					this.close();

					DynamicRefactoringDefinition refactoring = (buttonId == IDialogConstants.CLIENT_ID) ? getRefactoringCopy(obj)
							: (DynamicRefactoringDefinition) obj;
					RefactoringWizard wizard = new RefactoringWizard(
							refactoring, refactCatalog);
					wizard.init(PlatformUI.getWorkbench(), null);

					WizardDialog dialog = new WizardDialog(PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getShell(), wizard);
					dialog.create();
					dialog.setPageSize(200, 200);
					dialog.updateSize();
					dialog.open();
				}
			}
		}

		super.buttonPressed(buttonId);
	}

	/**
	 * Devuelve una copia de la refactorizacion con ciertos parametros
	 * modificados para hacer la viable. Es necesario llamar a este metodo
	 * cuando se crean copias de las refactorizaciones del plugin
	 * 
	 * @param obj
	 *            refactorizacion
	 * @return copia de la refactorizacion modificada
	 */
	private DynamicRefactoringDefinition getRefactoringCopy(Object obj) {
		DynamicRefactoringDefinition refactoring = ((DynamicRefactoringDefinition) obj);

		String name = refactoring.getName()
				+ Messages.SelectForEditingWindow_SuffixCopy;
		int i = 1;
		String version = "";//$NON-NLS-1$
		while (refactCatalog.hasRefactoring(name + version)) {
			i++;
			version = String.valueOf(i);
		}
		Builder builder = refactoring.getBuilder();
		builder.image(refactoring.getImageAbsolutePath());
		builder.examples(refactoring.getExamplesAbsolutePath());
		builder.name(name + version);
		return builder.build();

	}
}