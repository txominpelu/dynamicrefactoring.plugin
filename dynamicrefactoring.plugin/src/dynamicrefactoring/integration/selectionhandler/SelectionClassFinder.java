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

import javamoon.core.JavaName;
import moon.core.Model;
import moon.core.classdef.ClassDef;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.util.processor.JavaClassProcessor;
import dynamicrefactoring.util.processor.JavaElementProcessor;
import dynamicrefactoring.util.selection.TextSelectionInfo;
import dynamicrefactoring.util.selection.TreeSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener la clase MOON a la que 
 * pertenece un objeto seleccionado en Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectionClassFinder {
	
	/**
	 * Obtiene la clase del modelo MOON a la que pertenece el elemento seleccionado
	 * en una representación textual del interfaz gráfico.
	 * 
	 * @param selectionClass apuntador sobre el que se almacena la referencia 
	 * a la clase MOON a que pertenece la selección actual.
	 * @param selectionInfo proveedor de información sobre el elemento 
	 * seleccionado actualmente en Eclipse sobre un editor de texto.
	 * 
	 * @return la clase del modelo MOON a la que pertenece el elemento seleccionado
	 * en una representación textual del interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase en
	 * el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 */
	public static ClassDef getTextSelectionClass(ClassDef selectionClass, 
		TextSelectionInfo selectionInfo)
		throws ClassNotFoundException, IOException {
		
		if (selectionClass == null){
		
			Model MOONModel = ModelGenerator.getInstance().getModel();
			
			JavaElementProcessor elementProcessor = 
				new JavaClassProcessor(selectionInfo.getPrimaryType()); 
		
			selectionClass = MOONModel.getClassDef(
				new JavaName(elementProcessor.getUniqueName()));
		}
		return selectionClass;
	}
	
	/**
	 * Obtiene la clase del modelo MOON a la que pertenece el elemento seleccionado
	 * en una representación en árbol del interfaz gráfico.
	 * 
	 * @param selectionClass apuntador sobre el que se almacena la referencia 
	 * a la clase MOON a que pertenece la selección actual.
	 * @param selectionInfo proveedor de información sobre el elemento 
	 * seleccionado actualmente en Eclipse sobre un árbol desplegable.
	 * 
	 * @return la clase del modelo MOON a la que pertenece el elemento seleccionado
	 * en una representación en árbol del interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase en
	 * el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 */
	public static ClassDef getTreeSelectionClass(ClassDef selectionClass, 
		TreeSelectionInfo selectionInfo)
		throws ClassNotFoundException, IOException {
		
		if (selectionClass == null){
		
			Model MOONModel = ModelGenerator.getInstance().getModel();
			
			TreePath path = ((TreeSelection)selectionInfo.getSelection()).
				getPaths()[0].getParentPath();
			
			if (path.getLastSegment() instanceof IJavaElement){
				IJavaElement element = (IJavaElement)path.getLastSegment();
				if(element.getElementType() == IJavaElement.TYPE){
					JavaElementProcessor elementProcessor = 
						new JavaClassProcessor((IType)element); 
				
					selectionClass = MOONModel.getClassDef(
						new JavaName(elementProcessor.getUniqueName()));
				}
			}
		}
		return selectionClass;
	}
}