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


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.util.selection.SelectionInfo;
import dynamicrefactoring.util.selection.SelectionInfoFactory;
import dynamicrefactoring.SelectionListenerRegistry;

/**
 * Acción que de notificar a todos los observadores registrados dentro de 
 * SelectionListenerRegistry que el elemento seleccionado ha cambiado..
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class SelectionAction implements IWorkbenchWindowActionDelegate {

	/**
	 * La ventana activa de trabajo.
	 */
	private IWorkbenchWindow window;

	/**
	 * Constructor.
	 */
	public SelectionAction() {
		super(); 
	}
	
	/**
	 * Envía notificaciones cada vez que el usuario selecciona un elemento de entrada válido 
	 * para al refactorización.
	 * 
	 * @param action no se utiliza.
	 * @param selection elemento seleccionado por el usuario del espacio de trabajo.
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (window == null)
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		if(selection != null && ! selection.isEmpty()){
			
			try{
				
				SelectionInfo selectionInfo = 
					SelectionInfoFactory.getInstance().
						createSelectionInfo(selection, window);
				
				System.out.println("The chosen operation");
					SelectionListenerRegistry.getInstance().notifySelection(selectionInfo);
				
			}catch(Exception e){
				MessageDialog.openError(
						window.getShell(), 
						Messages.OpenRefactoringAction_Error, 
						Messages.OpenRefactoringAction_ErrorOccurred + ".\n"  //$NON-NLS-1$
						+ e.getMessage());
				
			
			}
		}//if	
	}
	
	
	
	/**
	 * Sin implementación.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {}
	
	/**
	 * Se guarda en caché el objeto ventana, con objeto de poder obtener
	 * la <code>shell</code> sobre la que abrir los mensajes de diálogo.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	
	/**
	 * Sin implementación.
	 * 
	 * @param action la acción que ha sido invocada.
	 */
	public void run(IAction action) {
	}
}
