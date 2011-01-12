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

import moon.core.Name;
import moon.core.classdef.MethDec;

import refactoring.engine.Predicate;

/**
 * Permite verificar que existe un parámetro cuyo nombre coincide con el 
 * especificado, dentro de la signatura de un método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class HasFormalArgWithName extends Predicate {
	
	/**
	 * Condición opuesta a la que se comprueba.
	 */
	private Predicate oppositePredicate;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de HasFormalArgWithName.
	 *
	 * @param methDec el método en cuya signatura se busca el parámetro.
	 * @param name el nombre del parámetro que se busca.
	 */
	public HasFormalArgWithName(MethDec methDec, Name name) {
		
		super("HasFormalArgWithName: \n\t" + //$NON-NLS-1$
			  "Checks whether a formal argument named " + '"' +  //$NON-NLS-1$
			  name.toString() + '"' + " exists within the signature of the " + //$NON-NLS-1$
			  "method " + '"' + methDec.getName().toString() + '"' + "or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		this.oppositePredicate = new HasNotFormalArgWithName(methDec, name);
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el parámetro en el método 
	 * especificado, <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		return !oppositePredicate.isValid();
	}
}