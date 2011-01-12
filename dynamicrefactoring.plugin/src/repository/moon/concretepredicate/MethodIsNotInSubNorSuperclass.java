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
 * Permite verificar que no existe ningún método con un cierto nombre en ninguna
 * superclase ni subclase de una clase dada, y a su vez, recursivamente, en 
 * ninguna superclase ni subclase de éstas.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodIsNotInSubNorSuperclass extends Predicate {
	
	/**
	 * Condición opuesta a la que se comprueba.
	 */
	private Predicate oppositePredicate;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodIsNotInSubNorSuperclass.
	 * @param classDef la clase a partir de la cual se busca el método.
	 * @param methodUniqueName el nombre único del método que se desea buscar.
	 */
	public MethodIsNotInSubNorSuperclass(ClassDef classDef, String methodUniqueName){
			
		super("MethodIsNotInSubNorSuperclass:\n\t" + //$NON-NLS-1$
			  "Makes sure the given method " + '"' + methodUniqueName + '"' + //$NON-NLS-1$
			  " is not to be found in any other class within the hierarchy" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.oppositePredicate = new MethodIsInSubOrSuperclass(
			classDef, methodUniqueName);
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el método con ese nombre en 
	 * ninguna clase de la jerarquía de herencia que se fuera a ver afectada por 
	 * un cambio de signatura en el método de la clase modificada; <code>false
	 * </code> en caso contrario.
	 */	 
	public boolean isValid() {
		return !oppositePredicate.isValid();
	}
}