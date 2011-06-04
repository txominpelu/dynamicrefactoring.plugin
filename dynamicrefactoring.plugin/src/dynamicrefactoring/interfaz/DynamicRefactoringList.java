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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;

import dynamicrefactoring.domain.RefactoringsCatalog;

/**
 * Proporciona el comportamiento abstracto por defecto común a las ventanas de
 * diálogo que deben mostrar una lista seleccionable con el conjunto de
 * refactorizaciones dinámicas disponibles.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class DynamicRefactoringList extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	protected static Logger logger = Logger.getLogger(DynamicRefactoringList.class);
	
	protected TableViewer availableRefListViewer;

	protected final RefactoringsCatalog refactCatalog;

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            <i>shell</i> padre del diálogo.
	 * @param refactCatalog 
	 */
	public DynamicRefactoringList(Shell parentShell, RefactoringsCatalog refactCatalog) {
		super(parentShell);
		this.refactCatalog = refactCatalog;
	}

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            <i>shell</i> padre del dialogo.
	 */
	public DynamicRefactoringList(IShellProvider parentShell,  RefactoringsCatalog refactCatalog) {
		super(parentShell);
		this.refactCatalog = refactCatalog;
	}

	/**
	 * Puebla la lista de refactorizaciones disponibles con los nombres
	 * ordenados alfabeticamente de las refactorizaciones.
	 */
	protected void fillInRefactoringList(){
		if(refactCatalog.getAllRefactorings().size() == 0)
			availableRefListViewer.add(Messages.DynamicRefactoringList_NoneFound);
		else
			availableRefListViewer.setInput(refactCatalog.getAllRefactorings());
	}
	
}