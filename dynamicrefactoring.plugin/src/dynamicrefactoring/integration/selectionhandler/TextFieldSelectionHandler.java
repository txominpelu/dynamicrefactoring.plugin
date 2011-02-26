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

import moon.core.classdef.ClassDef;

import org.eclipse.jdt.core.IField;

import dynamicrefactoring.util.processor.JavaFieldProcessor;
import dynamicrefactoring.util.selection.TextSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener el atributo MOON de una
 * clase con el que se corresponde un atributo seleccionado en Eclipse sobre
 * un editor de texto.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TextFieldSelectionHandler extends FieldSelectionHandler {
	
	/**
	 * El proveedor de informaci�n concreto para la selecci�n de texto.
	 */
	private TextSelectionInfo infoProvider;
	
	/**
	 * Constructor.
	 * 
	 * @param selectionInfo contenedor de la selecci�n que se desea manejar.
	 * 
	 * @throws Exception si la selecci�n contenida en #selectionInfo no es una
	 * selecci�n de un atributo sobre una representaci�n textual.
	 */
	public TextFieldSelectionHandler (TextSelectionInfo selectionInfo) 
		throws Exception{
		
		if (! selectionInfo.isFieldSelection())
			throw new Exception(
				Messages.TextFieldSelectionHandler_InvalidSelection
				+ Messages.TextFieldSelectionHandler_FieldExpected);
		
		infoProvider = selectionInfo;
	}

	/**
	 * @see FieldSelectionHandler#getFieldClass()
	 */
	@Override
	public ClassDef getFieldClass() throws ClassNotFoundException, IOException {
		
		if (fieldClass == null)
			fieldClass = SelectionClassFinder.getTextSelectionClass(fieldClass,
					infoProvider);
		
		return fieldClass;
	}

	/**
	 * Implementaci�n de la operaci�n primitiva 
	 * (patr�n de dise�o M�todo Plantilla).
	 * 
	 * @see FieldSelectionHandler#getFieldProcessor()
	 */
	@Override
	protected JavaFieldProcessor getFieldProcessor(){ 
		return	new JavaFieldProcessor(
			(IField)infoProvider.getSelectedJavaElement());
	}
}