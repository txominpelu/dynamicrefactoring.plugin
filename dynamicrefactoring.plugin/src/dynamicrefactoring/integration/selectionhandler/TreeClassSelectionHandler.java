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
import moon.core.ObjectMoon;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.util.processor.JavaClassProcessor;
import dynamicrefactoring.util.processor.JavaElementProcessor;
import dynamicrefactoring.util.selection.TreeSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener la clase MOON con la que
 * se corresponde una clase seleccionada en Eclipse sobre un �rbol desplegable.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TreeClassSelectionHandler extends ClassSelectionHandler {
	
	/**
	 * La selecci�n de una clase sobre un �rbol de selecci�n.
	 */
	private TreeSelection classSelection;
	
	/**
	 * Constructor.
	 * 
	 * @param selectionInfo contenedor de la selecci�n que se desea manejar.
	 * 
	 * @throws Exception si la selecci�n contenida en #selectionInfo no es una
	 * selecci�n de una clase sobre un �rbol de selecci�n.
	 */
	public TreeClassSelectionHandler (TreeSelectionInfo selectionInfo)
		throws Exception{
		
		if (! selectionInfo.isClassSelection())
			throw new Exception(
				Messages.TreeClassSelectionHandler_InvalidSelection
				+ Messages.TreeClassSelectionHandler_ClassExpected);
		
		classSelection = (TreeSelection)selectionInfo.getSelection();
	}

	/**
	 * @see ClassSelectionHandler#getMainObject()
	 */
	@Override
	public ObjectMoon getMainObject() throws ClassNotFoundException, IOException {
		
		if (classDef == null){
			
			TreePath path = classSelection.getPaths()[0].getParentPath();
			
			if (path.getLastSegment() instanceof IJavaElement){
				IJavaElement element = (IJavaElement)path.getLastSegment();
				if(element.getElementType() == IJavaElement.COMPILATION_UNIT){
					ICompilationUnit unit = (ICompilationUnit) element;
					
					JavaElementProcessor elementProcessor = 
						new JavaClassProcessor((IType)unit.findPrimaryType()); 
				
					classDef = ModelGenerator.getInstance().getModel().
						getClassDef(new JavaName(
							elementProcessor.getUniqueName()));
				}
			}
		}
		
		return classDef;
	}
}