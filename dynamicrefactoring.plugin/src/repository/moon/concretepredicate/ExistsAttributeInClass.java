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
import moon.core.classdef.ClassDef;
import refactoring.engine.Predicate;

/**
 * Permite verificar que existe un determinado atributo en una cierta clase.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class ExistsAttributeInClass extends Predicate {
	
	/**
	 * Atributo cuya presencia en una clase se quiere comprobar.
	 */
	private AttDec attribute;
	
	/**
	 * Clase en la que se busca un determinado atributo.
	 */
	private ClassDef classDef;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ExistsAttributeInClass.
	 *
	 * @param attribute el atributo cuya presencia en una clase se desea 
	 * comprobar.
	 * @param classDef la clase en la que se busca el atributo.
	 */
	public ExistsAttributeInClass(AttDec attribute, ClassDef classDef) {
		
		super("ExistsAttributeInClass: \n" + //$NON-NLS-1$
			  "\t" + "Checks whether the attribute " + '"' +  //$NON-NLS-1$ //$NON-NLS-2$
			  attribute.getName().toString() +
			  '"' + " belongs to the class " + '"' +  //$NON-NLS-1$
			  classDef.getName().toString() + '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.attribute = attribute;
		this.classDef = classDef;
	}	
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si existe el atributo en la clase especificada;
	 * <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		List<AttDec> attributes = classDef.getAttributes();
		
		for(AttDec nextAttribute : attributes)
			if(nextAttribute.getUniqueName().equals(attribute.getUniqueName()))
				return true;

		return false;		
	}
}