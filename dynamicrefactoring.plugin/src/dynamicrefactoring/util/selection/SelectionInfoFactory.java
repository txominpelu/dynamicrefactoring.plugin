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

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Proporciona funciones de creación de cualquiera de los objetos que 
 * permiten manejar la información relativa a la selecci�n actual sobre
 * Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectionInfoFactory {
	
	/**
	 * Instancia única de la f�brica (patr�n Singleton).
	 */
	private static SelectionInfoFactory myInstance;
	
	/**
	 * Constructor.
	 * 
	 * Privado seg�n la estructura del patr�n de dise�o Singleton.
	 */
	private SelectionInfoFactory(){}

	/**
	 * Obtiene la instancia única de la f�brica (patr�n Singleton).
	 *
	 * @return la instancia única de la f�brica.
	 */
	public static SelectionInfoFactory getInstance(){
		if (myInstance == null)
			myInstance = new SelectionInfoFactory();
		return myInstance;
	}

	/**
	 * Obtiene un proveedor de información concreto para una selecci�n dada.
	 * 
	 * Si la selecci�n forma parte de un �rbol de selecci�n, devuelve una
	 * instancia de <code>TreeSelectionInfo</code>.
	 * Si la selecci�n forma parte de una representación textual, devuelve una
	 * instancia de <code>TextSelectionInfo</code>.
	 * 
	 * Método f�brica (patr�n de dise�o Método f�brica).
	 * 
	 * @param selection la selecci�n para la que se quiere obtener un proveedor
	 * de información concreto.
	 * @param window ventana que se deber� pasar al proveedor de información
	 * creado para su interacción con la interfaz gr�fica.
	 * 
	 * @return un proveedor de información concreto para una selecci�n dada.
	 * 
	 * @throws Exception si se produce un error al intentar crear cualquiera de 
	 * los proveedores de información concretos.
	 * 
	 * @see TextSelectionInfo#TextSelectionInfo(TextSelection, IWorkbenchWindow)
	 */
	public SelectionInfo createSelectionInfo(
		ISelection selection, IWorkbenchWindow window) throws Exception{
		
		if(selection instanceof TreeSelection)
			return new TreeSelectionInfo(
				(TreeSelection) selection, window);
		if(selection instanceof TextSelection)
			return new TextSelectionInfo(
				(TextSelection) selection, window);
		
		return null;
	}
}