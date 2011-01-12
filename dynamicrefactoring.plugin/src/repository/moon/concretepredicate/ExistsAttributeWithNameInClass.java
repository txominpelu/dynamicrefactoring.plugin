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
import moon.core.classdef.AttDec;
import moon.core.Name;

import refactoring.engine.Predicate;

/**
 * Permite verificar que existe un atributo con un cierto nombre único (es decir,
 * con una cierta signatura) en un clase determinada.
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */ 
public class ExistsAttributeWithNameInClass extends Predicate {
		
	/**
	 * Nombre único del atributo cuya presencia en una clase se quiere comprobar.
	 */
	private Name attName;
	
	/**
	 * Clase en la que se busca un método con cierto nombre.
	 */
	private ClassDef classDef;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ExistsAttributeWithNameInClass.
	 * @param classDef la clase en la que se busca el atributo.
	 * @param attName el nombre único del atributo que se desea buscar.
	 */
	public ExistsAttributeWithNameInClass(ClassDef classDef, Name attName) {
		
		super("ExistsAttributeWithNameInClass: \n\t" + //$NON-NLS-1$
			  "Checks whether the given attribute " + '"' + attName + '"' + //$NON-NLS-1$
			  " belongs to the class " + '"' + classDef.getName() +  //$NON-NLS-1$
			  '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.attName = attName;
		this.classDef = classDef;
	}	
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si existe el método en la clase especificada, 
	 * <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		List<AttDec> attributes = classDef.getAttributes();
		
		for(AttDec nextAttribute : attributes)
			if (nextAttribute.getName().equals(attName)){
				return true;
			}
		
		return false;		
	}
}