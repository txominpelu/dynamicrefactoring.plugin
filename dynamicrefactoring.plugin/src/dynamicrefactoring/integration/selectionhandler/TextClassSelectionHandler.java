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

package dynamicrefactoring.integration.selectionhandler;

import java.io.IOException;

import moon.core.ObjectMoon;
import dynamicrefactoring.util.selection.TextSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener la clase MOON con la que
 * se corresponde una clase seleccionada en Eclipse sobre un editor de texto.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TextClassSelectionHandler extends ClassSelectionHandler {
	
	/**
	 * El proveedor de información concreto para la selección de texto.
	 */
	private TextSelectionInfo infoProvider;
	
	/**
	 * Constructor.
	 * 
	 * @param selectionInfo contenedor de la selección que se desea manejar.
	 * 
	 * @throws Exception si la selección contenida en #selectionInfo no es una
	 * selección de una clase sobre una representación textual.
	 */
	public TextClassSelectionHandler (TextSelectionInfo selectionInfo)
		throws Exception{
		
		if (! selectionInfo.isClassSelection())
			throw new Exception(
				Messages.TextClassSelectionHandler_InvalidSelection
				+ Messages.TextClassSelectionHandler_ClassExpected);
		
		infoProvider = selectionInfo;
	}

	/**
	 * @see ClassSelectionHandler#getMainObject()
	 */
	@Override
	public ObjectMoon getMainObject() throws ClassNotFoundException, IOException {
		if (classDef == null)
			classDef = SelectionClassFinder.getTextSelectionClass(
				classDef, infoProvider);
		return classDef;
	}
}
