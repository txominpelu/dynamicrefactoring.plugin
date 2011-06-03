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

package repository.moon.concretepredicate;

import moon.core.classdef.ClassDef;
import refactoring.engine.Predicate;

/**
 * Permite comprobar que dos clases no son en realidad una misma.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class NotEqualClasses extends Predicate {
	
	/**
	 * La primera clase de la comprobación. 
	 */
	private ClassDef firstClass;
	
	/**
	 * La segunda clase de la comprobación. 
	 */
	private ClassDef secondClass;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado NotEqualClasses.
	 *
	 * @param firstClass la primera clase de la comprobación.
	 * @param secondClass la segunda clase de la comprobacion. 
	 */
	public NotEqualClasses(ClassDef firstClass, ClassDef secondClass) {
		
		super("NotEqualClasses:\n\t" + //$NON-NLS-1$
			  "Makes sure the given class " + '"' + firstClass.getName().toString() + //$NON-NLS-1$
			  '"' + " does not refer to the same one as " + '"' +  //$NON-NLS-1$
			  secondClass.getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$
			
		this.firstClass = firstClass;
		this.secondClass = secondClass;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no son la misma clase; <code>false</code> en
	 * caso contrario.
	 */	 
	public boolean isValid() {
		if(firstClass.getUniqueName().equals(secondClass.getUniqueName()))
			return false;
		return true;		
	}
}