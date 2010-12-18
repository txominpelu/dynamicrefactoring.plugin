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

import moon.core.classdef.MethDec;
import moon.core.entity.SignatureEntity;

import refactoring.engine.Predicate;

/**
 * Permite comprobar que una determinada <code>SignatureEntity</code> (atributo
 * de clase o argumento formal) no es utilizada dentro del cuerpo de un cierto 
 * método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class SignatureEntityIsNotUsedInMethod extends Predicate {
	
	/**
	 * Condición opuesta a la que se comprueba.
	 */
	private Predicate oppositePredicate;
		
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado 
	 * SignatureEntityIsNotUsedInMethod.
	 *
	 * @param ent la entidad de signatura cuyo uso se desea estudiar.
	 * @param methDec el método en cuyo cuerpo se estudia el uso de la entidad.
	 */
	public SignatureEntityIsNotUsedInMethod(SignatureEntity ent, 
		MethDec methDec) {
		
		super("SignatureEntityIsNotUsedInMethod:\n\t" + //$NON-NLS-1$
			  "Makes sure the given entity " + '"' + ent.getName().toString() +  //$NON-NLS-1$
			  '"' + " is not being used within the body of the given method " +  //$NON-NLS-1$
			  '"' + methDec.getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$
		
		this.oppositePredicate = new SignatureEntityIsUsedInMethod(
			ent, methDec);
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el valor de la entidad de signatura no se 
	 * emplea en ningún punto del cuerpo del método; <code>false</code> en caso 
	 * contrario.
	 */	 
	public boolean isValid() {
		return !oppositePredicate.isValid();
	}
}