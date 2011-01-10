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

package dynamicrefactoring.interfaz.dynamic;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;

import moon.core.ObjectMoon;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.util.DynamicRefactoringLister;

/**
 * Construye una nueva ventana de refactorización dinámica y la abre.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringWindowLauncher {

	/**
	 * Constructor.
	 * 
	 * @param currentObject
	 *            objeto que constituye la entrada principal a la
	 *            refactorización.
	 * @param refactoringName
	 *            nombre de la refactorización dinámica seleccionada.
	 */
	public DynamicRefactoringWindowLauncher(
			ObjectMoon currentObject, String refactoringName) {

		try {
			
			// Se comprueba que la refactorización dinámica solicitada está
			// disponible.
			DynamicRefactoringLister drlister = 
				DynamicRefactoringLister.getInstance();
			HashMap<String, String> list = drlister.getDynamicRefactoringNameList(
				RefactoringPlugin.getDynamicRefactoringsDir(), true, null);
			String refactoringFilePath = 
				list.get(refactoringName+ " (" + refactoringName + ".xml)"); //$NON-NLS-1$ //$NON-NLS-2$

			// Si el fichero asociado está disponible.
			if(refactoringFilePath != null) {
				DynamicRefactoringWindow drw = 
					new DynamicRefactoringWindow(currentObject, 
						refactoringName, refactoringFilePath);
				drw.setBlockOnOpen(true);
				drw.open();
			}
			else {
				Object[] messageArgs = {"\"" + refactoringName + "\""}; //$NON-NLS-1$ //$NON-NLS-2$
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(
					Messages.DynamicRefactoringWindowLauncher_RefactoringDoesNotExist);
				
				MessageDialog.openError(getShell(), Messages.DynamicRefactoringWindowLauncher_Error,
					formatter.format(messageArgs) + ".");		 //$NON-NLS-1$
			}
		}
		catch (RefactoringException exception) {
			Object[] messageArgs = {"\"" + refactoringName + "\""}; //$NON-NLS-1$ //$NON-NLS-2$
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.DynamicRefactoringWindowLauncher_Refactoring);
			
			MessageDialog.openError(getShell(), Messages.DynamicRefactoringWindowLauncher_Error,
				formatter.format(messageArgs) + ": " +  //$NON-NLS-1$
				Messages.DynamicRefactoringWindowLauncher_ErrorInitializing +
				".\n" + exception.getMessage());	 //$NON-NLS-1$
		}
		catch (IOException ioe) {
			Object[] messageArgs = {"\"" + refactoringName + "\""}; //$NON-NLS-1$ //$NON-NLS-2$
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.DynamicRefactoringWindowLauncher_Refactoring);
			
			MessageDialog.openError(getShell(), Messages.DynamicRefactoringWindowLauncher_Error,
				formatter.format(messageArgs) + ": " + //$NON-NLS-1$
				Messages.DynamicRefactoringWindowLauncher_ErrorInitializing +
				".\n" + ioe.getMessage());	 //$NON-NLS-1$
		}
	}

	/**
	 * Obtiene la shell que se debe pasar a las ventanas creadas.
	 * 
	 * @return la shell que se debe pasar a las ventanas creadas.
	 */
	private static Shell getShell(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}