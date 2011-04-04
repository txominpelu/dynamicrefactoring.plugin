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

import moon.core.ObjectMoon;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Construye una nueva ventana de refactorizaci�n din�mica y la abre.
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
	 *            refactorizaci�n.
	 * @param refactoringName
	 *            nombre de la refactorizaci�n din�mica seleccionada.
	 */
	public DynamicRefactoringWindowLauncher(ObjectMoon currentObject,
			DynamicRefactoringDefinition refactoring) {

		// Si el fichero asociado est� disponible.
		DynamicRefactoringWindow drw = new DynamicRefactoringWindow(
				currentObject, refactoring);
		drw.setBlockOnOpen(true);
		drw.open();

	}

	/**
	 * Obtiene la shell que se debe pasar a las ventanas creadas.
	 * 
	 * @return la shell que se debe pasar a las ventanas creadas.
	 */
	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}