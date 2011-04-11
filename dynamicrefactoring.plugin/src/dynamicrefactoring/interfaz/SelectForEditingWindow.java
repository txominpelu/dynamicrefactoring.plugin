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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.interfaz.wizard.RefactoringWizard;

/**
 * Permite seleccionar una de las refactorizaciones din�micas disponibles para ser
 * editada.
 * 
 * <p>Muestra en todo momento un resumen con las caracter�sticas principales de la
 * refactorizaci�n seleccionada, hasta que se pulsa el bot�n que inicia el 
 * asistente para la edici�n de refactorizaciones.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectForEditingWindow extends SelectDynamicRefactoringWindow {
	
	private RefactoringsCatalog refactCatalog;
	
	/**
	 * Crea la ventana de di�logo.
	 * 
	 * @param parentShell la <i>shell</i> padre de esta ventana de di�logo.
	 */
	public SelectForEditingWindow(Shell parentShell, RefactoringsCatalog refactCatalog) {
		super(parentShell, refactCatalog);
		this.refactCatalog = refactCatalog;
		logger = Logger.getLogger(SelectForEditingWindow.class);
	}
	
	/**
	 * Crea el bot�n que permite lanzar el asistente para la edici�n de la 
	 * refactorizaci�n seleccionada.
	 * 
	 * @param parent el componente padre del bot�n.
	 * 
	 * @see SelectDynamicRefactoringWindow#createOKButton(Composite)
	 */
	@Override
	protected void createOKButton(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.SelectForEditingWindow_EditCaps, true);		
	}
	
	/**
	 * Obtiene el verbo asociado a la acci�n que permite iniciar la ventana
	 * de di�logo sobre la refactorizaci�n seleccionada.
	 * 
	 * @return el verbo asociado a la acci�n que permite iniciar la ventana
	 * de di�logo sobre la refactorizaci�n seleccionada.
	 */
	@Override
	protected String getOperation(){
		return Messages.SelectForEditingWindow_EditLower;
	}

	/**
	 * Notifica que el bot�n de este di�logo con el identificador especificado
	 * ha sido pulsado.
	 * 
	 * @param buttonId el identificador del bot�n que ha sido pulsado (v�anse
	 * las constantes <code>IDialogConstants.*ID</code>).
	 * 
	 * @see Dialog#buttonPressed
	 * @see IDialogConstants
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		
		if(buttonId == IDialogConstants.OK_ID) {
			
			Table availableList=availableRefListViewer.getTable();
			if (availableList.getSelectionCount() == 1){
				
				DynamicRefactoringDefinition refactoring = 
					refactCatalog.getRefactoring(availableList.getSelection()[0].getData().toString());
			
				this.close();

				RefactoringWizard wizard =  new RefactoringWizard(refactoring, refactCatalog);
				wizard.init(PlatformUI.getWorkbench(), null);
				
				WizardDialog dialog = new WizardDialog(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),wizard);
				dialog.create();
				dialog.setPageSize(200, 200);
				dialog.updateSize();
				dialog.open();
			}
		}
			
		super.buttonPressed(buttonId);
	}	
}