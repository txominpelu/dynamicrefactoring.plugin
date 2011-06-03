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
import refactoring.engine.Function;
import refactoring.engine.Predicate;
import repository.moon.concretefunction.ClassRetriever;

/**
 * Permite verificar que existe una clase dentro de un modelo dado.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class ExistsClass extends Predicate {
	
	/**
	 * Clase cuya presencia en un modelo se quiere comprobar.
	 */
	private ClassDef classDef;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ExistsClass.
	 *
	 * @param classDef la clase que se desea buscar.
	 */
	public ExistsClass(ClassDef classDef) {
		
		super("ExistsClass: \n\t" + //$NON-NLS-1$
			  "Checks whether the class " + '"' +  //$NON-NLS-1$
			  classDef.getName().toString() + '"' +
			  " exists within the model or not." + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.classDef = classDef;
	}	
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si existe la clase en el modelo especificado, 
	 * <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {	
		Function search = new ClassRetriever(classDef);
		
		if (search.getValue()!= null )
			return true;
		return false;		
	}
}