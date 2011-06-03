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

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jface.viewers.TreeSelection;

import dynamicrefactoring.util.processor.JavaMethodProcessor;
import dynamicrefactoring.util.selection.TreeSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener el método MOON con el que
 * se corresponde un método seleccionado en Eclipse sobre un �rbol desplegable.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TreeMethodSelectionHandler extends MethodSelectionHandler {
	
	/**
	 * La selecci�n de un método sobre un �rbol de selecci�n.
	 */
	private TreeSelectionInfo infoProvider;
	
	/**
	 * Constructor.
	 * 
	 * @param selectionInfo contenedor de la selecci�n que se desea manejar.
	 * 
	 * @throws Exception si la selecci�n contenida en #selectionInfo no es una
	 * selecci�n de un método sobre un �rbol de selecci�n.
	 */
	public TreeMethodSelectionHandler (TreeSelectionInfo selectionInfo) 
		throws Exception{
		
		if (! selectionInfo.isMethodSelection())
			throw new Exception(
				Messages.TreeMethodSelectionHandler_InvalidSelection
				+ Messages.TreeMethodSelectionHandler_MethodExpected);
		
		infoProvider = selectionInfo;
	}

	/**
	 * @see MethodSelectionHandler#getMethodClass()
	 */
	@Override
	public ClassDef getMethodClass() throws ClassNotFoundException, IOException {
		if (methodClass == null)
			methodClass = SelectionClassFinder.getTreeSelectionClass(
				methodClass, infoProvider); 
		return methodClass;
	}

	/**
	 * Implementación de la operación primitiva.
	 * (Patr�n de dise�o Método Plantilla).
	 * 
	 * @see MethodSelectionHandler#getMethodProcessor()
	 */
	@Override
	public JavaMethodProcessor getMethodProcessor() {
		TreeSelection methodSelection = (TreeSelection)infoProvider.getSelection();
		return new JavaMethodProcessor(
			(IMethod)methodSelection.getFirstElement());
	}
}