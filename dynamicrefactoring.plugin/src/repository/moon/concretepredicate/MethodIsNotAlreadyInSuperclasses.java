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


import java.util.Iterator;

import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.SuperclassCollector;

/**
 * Permite verificar que no existe ningún método con una cierta signatura en 
 * ninguna de las superclases de una clase determinada.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodIsNotAlreadyInSuperclasses extends Predicate {
	
	/**
	 * La clase en cuyas superclases se busca el método.
	 */
	private ClassDef classDef;
	
	/**
	 * El método cuyo equivalente en una superclase se busca.
	 */
	private MethDec method;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodIsNotAlreadyInSuperclasses.
	 *
	 * @param method el método cuyo equivalente en una superclase se desea buscar.
	 * @param classDef la clase en cuyas superclases se busca el método.
	 */
	public MethodIsNotAlreadyInSuperclasses(MethDec method, ClassDef classDef) {
		
		super("MethodIsNotAlreadyInSuperclasses:\n\t" + //$NON-NLS-1$
			  "Makes sure the given method " + '"' + method.getName().toString() +  //$NON-NLS-1$
			  '"' + " is not to be found within any of the superclasses of the " + //$NON-NLS-1$
			  "class " + '"' + classDef.getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
			  
		this.classDef = classDef;
		this.method = method;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el método en ninguna superclase 
	 * de la clase especificada; <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		NotExistsMethodWithNameInClass subPredicate;
		ClassDef nextSuperclass;
		Name uniqueName;
		
		// Se obtiene la lista de parámetros del método buscado.
		int parameterTypesFirstIndex =
			method.getUniqueName().toString().indexOf('%');
		
		String parameterTypesPart = ""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = method.getUniqueName().toString().substring(
					parameterTypesFirstIndex);
		
		// Se obtienen todas las superclases de la clase seleccionada.
		Iterator<ClassDef> superClassIt = 
			new SuperclassCollector(classDef).getCollection().iterator();
			
		while(superClassIt.hasNext()){			
			nextSuperclass = superClassIt.next();
			
			uniqueName = nextSuperclass.getUniqueName().concat(
				'~' + method.getName().toString() + parameterTypesPart);
			
			subPredicate = new NotExistsMethodWithNameInClass(nextSuperclass,
				uniqueName);
				
			if(! (subPredicate.isValid()))
				return false;
		}
		
		return true;
	}
}