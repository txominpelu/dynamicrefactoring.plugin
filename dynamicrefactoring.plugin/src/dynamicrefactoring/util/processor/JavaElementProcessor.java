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

package dynamicrefactoring.util.processor;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Proporciona funciones que permiten manejar un elemento Java en general 
 * tal y como lo define Eclipse en su representación interna.
 * 
 * <p>Los elementos Java pueden representar cualquier tipo de componente que
 * forme parte de un proyecto Java en Eclipse, como un proyecto, un paquete,
 * una clase, un método, etc.</p>.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JavaElementProcessor {
	
	/**
	 * Elemento Java que se procesa.
	 */
	protected IJavaElement element;
	
	/**
	 * Constructor.
	 * 
	 * @param element el elemento Java que se procesa.
	 */
	public JavaElementProcessor (IJavaElement element){
		this.element = element;
	}
	
	/**
	 * Obtiene el nombre del elemento Java.
	 * 
	 * @return el nombre del elemento Java.
	 */
	public String getUniqueName(){
		return element.getElementName();
	}
}