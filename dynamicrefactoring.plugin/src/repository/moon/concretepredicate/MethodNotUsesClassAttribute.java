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

import java.util.List;

import moon.core.classdef.AttDec;
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Permite comprobar que un determinado método no utiliza ninguno de los 
 * atributos de la clase a la que pertenece.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class MethodNotUsesClassAttribute extends Predicate {
	
	/**
	 * El método en el que se estudia el uso de atributos de la clase.
	 */
	private MethDec method;
		
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado MethodNotUsesClassAttribute.
	 *
	 * @param method el método en el que se estudia el uso de atributos de clase.
	 */
	public MethodNotUsesClassAttribute(MethDec method) {
		
		super("MethodNotUsesClassAttribute:\n\t" + //$NON-NLS-1$
			  "Makes sure the given method " + '"' + method.getName().toString() + //$NON-NLS-1$
			  '"' + " does not use any of the attributes belonging to the class " + //$NON-NLS-1$
			  '"' + method.getClassDef().getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$
		
		this.method = method;
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el método no emplea ningún atributo de la 
	 * clase; <code>false</code>, en caso contrario.
	 */
	@Override
	public boolean isValid() {
		
		SignatureEntityIsUsedInMethod attCheck;
		
		List<AttDec> attributes = method.getClassDef().getAttributes();
				
		for(AttDec nextAttribute : attributes){
								
			attCheck = new SignatureEntityIsUsedInMethod(nextAttribute ,method);
					
			if(attCheck.isValid())
				return false;
		}		
		return true;
	}
}