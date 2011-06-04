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

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Combo;

/**
 * Permite actualizar la selección de un campo desplegable cuando se
 * introduce su contenido manualmente.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ComboEditor implements FocusListener {
	
	/**
	 * @see FocusListener#focusGained(FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e){}
	
	/**
	 * @see FocusListener#focusLost(FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e){
		if (e.getSource() instanceof Combo){
			Combo combo = (Combo)e.getSource();
			
			// Si el combo no tiene ninguna selección marcada.
			if (combo.getSelectionIndex() < 0){
				String content = combo.getText();
				
				// Si se ha escrito algo a mano en el texto del desplegable.
				if (content != null && content.length() > 0){
					String[] items = combo.getItems();
					
					for (int i = 0; i < items.length; i++)
						// Si lo que se ha escrito es un elemento.
						if (items[i].equals(content)){
							// Se selecciona el elemento.
							combo.select(i);
							break;
						}
				}
			}
		}
	}
}