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

package dynamicrefactoring.listener;

import dynamicrefactoring.util.selection.SelectionInfo;


/**
 * Define la interfaz común a todos los objetos que deseen actuar como 
 * observadores de una selección dentro de los objetos del espacio de trabajo
 * que son válidos como entrada para una refactorización.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public interface IMainSelectionListener {
	
	/**
	 * Notifica al <i>listener</i> que un objeto válido como entrada para la refactorización
	 * ha sido seleccionado.
	 * 
	 * @param selection selección del espacio de trabajo.
	 */
	public abstract void elementSelected(SelectionInfo selection);
}
