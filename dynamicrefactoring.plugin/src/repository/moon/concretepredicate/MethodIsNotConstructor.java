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

import moon.core.classdef.*;

import refactoring.engine.Predicate;

/**
 * Permite verificar que un cierto método no es constructor de la clase que lo 
 * alberga.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodIsNotConstructor extends Predicate {
	
	/**
	 * Método cuyo carácter de constructor se quiere verificar.
	 */
	private MethDec method;
	
	/**
	 * Clase que contiene al método.
	 */
	private ClassDef classDef;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodIsNotConstructor.
	 *
	 * @param method el método cuyo carácter de constructor se quiere verificar.
	 */
	public MethodIsNotConstructor(MethDec method) {
		
		super("MethodIsNotConstructor:\n\t" + //$NON-NLS-1$
			  "Makes sure the given method " + '"' + method.getName() + '"' + //$NON-NLS-1$
			  " is not a constructor" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.method = method;
		this.classDef = method.getClassDef();
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el método no actúa como constructor de 
	 * la clase especificada, <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		if(method.getName().equals(classDef.getName())){
			return false;
		}
		return true;		
	}
}