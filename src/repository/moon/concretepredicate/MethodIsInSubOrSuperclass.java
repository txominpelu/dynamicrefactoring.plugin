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


import java.util.*;

import moon.core.classdef.ClassDef;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.*;

/**
 * Permite verificar que existe un método con un cierto nombre en alguna 
 * superclase o subclase de una clase dada, o a su vez, recursivamente, en 
 * alguna superclase o subclase de éstas.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodIsInSubOrSuperclass extends Predicate {
	
	/**
	 * Nombre único del método cuya presencia en las clases se quiere comprobar.
	 */
	private String methodUniqueName;
	
	/**
	 * Clase a partir de la cual se busca un método con un cierto nombre.
	 */
	private ClassDef classDef;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodIsInSubOrSuperclass.
	 * @param classDef la clase a partir de la cual se busca el método.
	 * @param methodUniqueName el nombre único del método que se desea buscar.
	 */
	public MethodIsInSubOrSuperclass(ClassDef classDef, String methodUniqueName){
			
		super("MethodIsInSubOrSuperclass: \n\t" + //$NON-NLS-1$
			  "Checks whether the given method " + '"' + methodUniqueName + '"' + //$NON-NLS-1$
			  " is to be found in any other class within the hierarchy or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.methodUniqueName = methodUniqueName;
		this.classDef = classDef;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si se encuentra el método en alguna clase de la
	 * jerarquía de herencia de la clase original (o recursivamente en las 
	 * jerarquías de herencia de las clases que se vayan encontrando); <code>
	 * false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		Collection<ClassDef> foundClasses = new Vector<ClassDef>(10,1);
		
		ClassesAffectedByMethRenameCollector getAffectedClasses =
			new ClassesAffectedByMethRenameCollector(
				classDef, methodUniqueName, foundClasses,true);
			
		if(getAffectedClasses.getCollection().isEmpty())
			return false;
		
		return true;
	}
}