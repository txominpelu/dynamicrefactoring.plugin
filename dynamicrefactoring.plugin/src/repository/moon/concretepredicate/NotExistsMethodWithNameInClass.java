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
import moon.core.classdef.ClassDef;
import refactoring.engine.Predicate;

/**
 * Permite verificar que no existe ning�n m�todo con un cierto nombre �nico (es,
 * decir, con una cierta signatura) en una clase determinada.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class NotExistsMethodWithNameInClass extends Predicate {
	
	/**
	 * Condici�n opuesta a la que se comprueba.
	 */
	private Predicate oppositePredicate;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de NotExistsMethodWithNameInClass.
	 * @param classDef la clase en la que se busca el m�todo.
	 * @param methodUniqueName el nombre �nico del m�todo que se desea buscar.
	 */
	public NotExistsMethodWithNameInClass(ClassDef classDef, Name methodUniqueName) {
		
		super("NotExistsMethodWithNameInClass: \n\t" + //$NON-NLS-1$
			  "Makes sure the given method " + '"' + methodUniqueName + '"' +  //$NON-NLS-1$
			  " does not already belong to " + '"' + classDef.getName() +  //$NON-NLS-1$
			  '"' + ".\n\n"); //$NON-NLS-1$
		
		this.oppositePredicate = 
			new ExistsMethodWithNameInClass(classDef, methodUniqueName);
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el m�todo en la clase especificada, 
	 * <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		return !oppositePredicate.isValid();
	}
}