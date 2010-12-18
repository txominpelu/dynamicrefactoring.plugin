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

import dynamicrefactoring.interfaz.wizard.ImportWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.*;

import org.eclipse.ui.*;

/**
 * Acción que inicia el asistente de importación de refactorizaciones dinámicas.
 */
public class OpenImportDialogAction implements IWorkbenchWindowActionDelegate  {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		
		ImportWizard wizard = new ImportWizard(
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			
		wizard.setBlockOnOpen(true);
		wizard.open();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection){}
	
	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	@Override
	public void dispose() {}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window) {}
}