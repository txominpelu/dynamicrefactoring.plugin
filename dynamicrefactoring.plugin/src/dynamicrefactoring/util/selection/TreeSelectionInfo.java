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
package dynamicrefactoring.util.selection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Proporciona funciones capaces de determinar el tipo de elemento que se 
 * encuentra seleccionado en una ventana sobre un árbol desplegable, así como
 * otra información adicional relativa al mismo.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TreeSelectionInfo extends SelectionInfo {
		
	/**
	 * Selección sobre un árbol de selección cuya información se desea consultar.
	 */
	private TreeSelection treeSelection;
	
	/**
	 * Constructor.
	 * 
	 * @param selection selección actual sobre un árbol de selección.
	 * @param window ventana de la interfaz con la que interaccionar. 
	 * 
	 * @throws Exception si #selection no es una selección sobre un árbol.
	 */
	public TreeSelectionInfo (TreeSelection selection, 
		IWorkbenchWindow window) throws Exception {
		
		super(selection, window);
		
		if (! (selection instanceof TreeSelection))
			throw new Exception(
				Messages.TreeSelectionInfo_InvalidSelection
				+ Messages.TreeSelectionInfo_TreeExpected + ".\n"); //$NON-NLS-1$
		
		treeSelection = selection;
	}
	
	/**
	 * Determina el tipo de elemento seleccionado en una ventana.
	 * Solo tiene en consideración los tipos indicados por #typeName, #methodName 
	 * y #fieldName.
	 * 
	 * @return el nombre completamente cualificado del tipo de elemento 
	 * seleccionado, o <code>null</code> si no es ninguno de los admitidos.
	 * 
	 * @see SelectionInfo
	 */
	@Override
	public String getSelectionType(){
		
		if (selectionName == null)
					
			selectionName = 
				treeSelection.getFirstElement().getClass().getCanonicalName();
	
		return selectionName;
	}
		
	/**
	 * Obtiene el proyecto al que pertenece el elemento seleccionado 
	 * en una ventana.
	 * 
	 * @return el proyecto al que pertenece el elemento seleccioando.
	 */
	@Override
	public IProject getProjectForSelection(){
		
		IJavaElement element = getJavaProjectForSelection();
		
		if (element != null){
			IProject myProject = getJavaProjectForName(element.getElementName());
			return myProject;
		}
		
		return null;
	}
	
	/**
	 * @see SelectionInfo#getJavaProjectForSelection()
	 */
	@Override
	public IJavaProject getJavaProjectForSelection(){
		TreePath path = treeSelection.getPaths()[0].getParentPath();
		
		if (path.getFirstSegment() instanceof IJavaElement){
			IJavaElement element = (IJavaElement)path.getFirstSegment();
			if(element.getElementType() == IJavaElement.JAVA_PROJECT)
				return (IJavaProject)element;
		}
		return null;
	}
}