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

package dynamicrefactoring.interfaz;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import dynamicrefactoring.RefactoringImages;

/**
 * Proporciona métodos de edición de elementos gráficos de tipo árbol
 * desplegable.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TreeEditor {

	/**
	 * Puebla una rama de un árbol a partir de los elementos de una lista.
	 * 
	 * @param elements
	 *            elementos cuyos nombres aparecerán como hojas del árbol
	 * @param parent
	 *            rama del árbol en la que se insertarán los elementos.
	 * @param icon
	 *            ruta relativa al <i>plugin</i> del icono que se asociará a
	 *            cada uno de los elementos que se inserten.
	 */
	public static void fillInTreeBranch(List<String> elements, TreeItem parent,
		String icon){
		
		for(int i = 0; i < elements.size(); i++){
			TreeItem leaf = new TreeItem(parent, SWT.NONE, i);
			leaf.setText(elements.get(i));
			leaf.setImage(RefactoringImages.getImageForPath(icon));
		}
	}

	/**
	 * Crea una rama en un árbol de elementos.
	 * 
	 * @param parent
	 *            árbol al que se le añadirá una rama.
	 * @param position
	 *            posición de la rama en el árbol (empezando en 0).
	 * @param text
	 *            texto asociado a la nueva rama.
	 * @param icon
	 *            ruta relativa al <i>plugin</i> del icono asociado a la nueva
	 *            rama.
	 * 
	 * @return el elemento que actúa como raíz de la rama.
	 */
	public static TreeItem createBranch(Tree parent, int position, String text, String icon){
		TreeItem child = new TreeItem(parent, SWT.NONE, position);
		return addItemProperties(text, icon, child);
	}

	/**
	 * Crea una subrama de un arbol dada la rama padre.
	 * 
	 * @param parent
	 *            rama padre a la que se añadirá una rama.
	 * @param position
	 *            posición de la rama en el árbol (empezando en 0).
	 * @param text
	 *            texto asociado a la nueva rama.
	 * @param icon
	 *            ruta relativa al <i>plugin</i> del icono asociado a la nueva
	 *            rama.
	 * 
	 * @return el elemento que actúa como raíz de la rama.
	 */
	public static TreeItem createBranch(TreeItem parent, int position,
			String text, String icon) {
		TreeItem child = new TreeItem(parent, SWT.NONE, position);
		return addItemProperties(text, icon, child);
	}

	/**
	 * Agrega las propiedades pasadas al item.
	 * 
	 * @param text
	 *            texto del item
	 * @param icon
	 *            icono del item
	 * @param child
	 *            item a modificar
	 * @return devuelve el item modificado
	 */
	private static TreeItem addItemProperties(String text, String icon,
			TreeItem child) {
		child.setText(text);
		child.setImage(RefactoringImages.getImageForPath(icon));
		return child;
	}
}