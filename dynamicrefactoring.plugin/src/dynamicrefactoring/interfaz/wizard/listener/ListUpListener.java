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

package dynamicrefactoring.interfaz.wizard.listener;

import java.util.Arrays;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;

/**
 * Actualiza el orden en que aparecen las entradas de la lista, desplazando
 * un puesto hacia arriba aquélla que se encuentre seleccionada.
 * 
 * <p>Si se tienen varias entradas seleccionadas, se desplazan todas ellas
 * un puesto hacia arriba. Si la primera entrada se encuentra seleccionada
 * la acción sobre el botón no tiene efecto alguno.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ListUpListener implements SelectionListener {

	/**
	 * Lista cuyas entradas se pueden desplazar hacia arriba.
	 */
	private List list;
	
	/**
	 * Constructor.
	 * 
	 * @param list lista cuyas entradas se pueden desplazar hacia arriba.
	 */
	public ListUpListener(List list) {
		super();
		this.list = list;
	}
	
	/**
	 * Recibe una notificación de que se ha pulsado el botón que permite 
	 * desplazar una entrada hacia arriba en la lista de entradas 
	 * seleccionadas.
	 * 
	 * <p>Inicia las acciones que sean necesarias para actualizar el orden en 
	 * que se muestran las entradas seleccionadas.</p>
	 * 
	 * @param e el evento de selección disparado en la ventana.
	 * 
	 * @see SelectionListener#widgetSelected(SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		// Si hay elementos seleccionados entre los elegidos
		if (list.getSelectionCount() > 0 &&
			list.getItem(list.getSelectionIndex()) != null){
			
			// Se obtienen las entradas seleccionadas y sus posiciones.
			String[] selected = list.getSelection();
			int[] indices = list.getSelectionIndices();
			Arrays.sort(indices);
			// Se asegura de que no se ha seleccionado el primero de la lista.
			if (Arrays.binarySearch(indices, 0) < 0){
				// Se eliminan de la lista las entradas seleccionadas.
				list.remove(indices);
				// Se añaden de una en una, una posición más abajo.
				for (int i = 0; i < selected.length && i < indices.length; i++)
					list.add(selected[i], indices[i] - 1);
			}
			
			list.setSelection(selected);
		}
	}
	
	/**
	 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
}