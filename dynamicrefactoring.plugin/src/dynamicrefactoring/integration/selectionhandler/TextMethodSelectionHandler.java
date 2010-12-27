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

import dynamicrefactoring.util.processor.JavaMethodProcessor;
import dynamicrefactoring.util.selection.TextSelectionInfo;

import java.io.IOException;

import moon.core.classdef.ClassDef;

import org.eclipse.jdt.core.IMethod;

/**
 * Proporciona las funciones necesarias para obtener el método MOON con el que
 * se corresponde un método seleccionado en Eclipse sobre un editor de texto.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TextMethodSelectionHandler extends MethodSelectionHandler {
	
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
	 * selección de un método sobre una representación textual.
	 */
	public TextMethodSelectionHandler (TextSelectionInfo selectionInfo) 
		throws Exception{
		
		if (! selectionInfo.isMethodSelection())
			throw new Exception(
				Messages.TextMethodSelectionHandler_InvalidSelection
				+ Messages.TextMethodSelectionHandler_MethodSelection);
		
		infoProvider = selectionInfo;
	}

	/**
	 * @see MethodSelectionHandler#getMethodClass()
	 */
	@Override
	public ClassDef getMethodClass() throws ClassNotFoundException, IOException {
		if (methodClass == null)
			methodClass = SelectionClassFinder.getTextSelectionClass(methodClass,
				infoProvider);
		return methodClass;
	}

	/**
	 * Implementación de la operación primitiva.
	 * (Patrón de diseño Método Plantilla).
	 * 
	 * @see MethodSelectionHandler#getMethodProcessor()
	 */
	@Override
	public JavaMethodProcessor getMethodProcessor() {
		return new JavaMethodProcessor(
			(IMethod)infoProvider.getSelectedJavaElement());
	}
}