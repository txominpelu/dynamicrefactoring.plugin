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
 * Comprueba que una clase no es una clase interna.
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsNotInnerClass extends Predicate {
	
	/**
	 * Clase cuyo carácter de clase no interna se debe comprobar.
	 */
	private ClassDef classDef;
	
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsNotInnerClass</code>.
	 *
	 * @param classDef clase cuyo carácter de clase no interna se debe comprobar.
	 */
	public IsNotInnerClass(ClassDef classDef) {

		super("IsNotInnerClass:\n\t" + //$NON-NLS-1$
			  "Makes sure the class " + '"' +  //$NON-NLS-1$
			  classDef.getName().toString() + '"' + 
			  " is not an inner class" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.classDef = classDef; 
	}	

	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si la clase no es una clase interna;
	 * <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {		
		return ! classDef.getUniqueName().toString().contains("$"); //$NON-NLS-1$
	}
}