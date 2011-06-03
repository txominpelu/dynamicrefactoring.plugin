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

import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Permite verificar que no existe ningun parámetro formal cuyo nombre coincida
 * con el del argumento formal indicado dentro de la signatura de un método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class HasNotFormalArg extends Predicate {
	
	/**
	 * Predicado auxiliar.
	 */
	private HasNotFormalArgWithName auxiliar;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado HasNotFormalArgWithName.
	 * @param formalArg el argumento formal con cuyo nombre se busca un parámetro 
	 * en el método.
	 * @param methDec el método en que se busca el parámetro.
	 */
	public HasNotFormalArg(FormalArgument formalArg, MethDec methDec) {
		
		super("HasNotFormalArgWithName:\n\t" + //$NON-NLS-1$
			"Makes sure a formal argument named " + '"' +  //$NON-NLS-1$
			formalArg.getName().toString() + '"' +
			"does not exist within the signature of the method " + //$NON-NLS-1$
			'"' + methDec.getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$
		
		this.auxiliar = new HasNotFormalArgWithName(methDec, formalArg.getName());
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el parámetro en el método 
	 * especificado; <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		return auxiliar.isValid();
	}
}