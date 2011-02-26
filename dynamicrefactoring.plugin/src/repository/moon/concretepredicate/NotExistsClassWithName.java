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
import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;

/**
 * Permite verificar que no existe ninguna clase cuyo nombre coincida con el 
 * especificado, dentro del modelo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class NotExistsClassWithName extends Predicate {
	
	/**
	 * Nombre de la clase cuya presencia se quiere comprobar.
	 */
	private Name className;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado NotExistsClassWithName.
	 *
	 * @param className el nombre con el que se busca una clase.
	 */
	public NotExistsClassWithName(Name className) {
		
		super("NotExistsClassWithName:\n\t" + //$NON-NLS-1$
			  "Checks whether a class with the given name " + '"' +  //$NON-NLS-1$
			  className.toString() + '"' + " exists within the model or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.className = className;
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe en el model una clase con el 
	 * nombre especificado, <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		Iterator<ClassDef> classes = MOONRefactoring.getModel().getClassDef().iterator();
		
		while(classes.hasNext()){
			ClassDef cd = classes.next();
			if(cd.getName().equals(className))
				return false;
		}
		return true;
	}
}