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
import moon.core.classdef.AttDec;
import moon.core.classdef.MethDec;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.MethodCollector;

/**
 * Permite comprobar que un determinado atributo de clase no es utilizado en 
 * ningún punto de la misma.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class AttributeIsNotUsedInClass extends Predicate {
	
	/**
	 * El atributo cuyo uso en una clase se quiere estudiar.
	 */
	private AttDec att;
	
	/**
	 * La clase en la que se quiere comprobar que el atributo no se emplea.
	 */
	private ClassDef classDef;
		
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado AttributeIsNotUsedInClass.
	 *
	 * @param att el atributo cuyo uso se desea estudiar.
	 * @param classDef la clase en la que se estudia el uso del atributo.
	 */
	public AttributeIsNotUsedInClass(AttDec att, ClassDef classDef) {
		
		super("AttributeIsNotUsedInClass: \n\t" + //$NON-NLS-1$
			  "Checks whether the attribute " + '"' + att.getName().toString() +  //$NON-NLS-1$
			  '"' + " is used within the class " + '"' +  //$NON-NLS-1$
			  classDef.getName().toString() + '"' + "\n\n"); //$NON-NLS-1$
		
		this.att = att;
		this.classDef = classDef;
	}

	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el atributo no se emplea en ningún punto de 
	 * la clase; <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {		
		SignatureEntityIsUsedInMethod methodCheck;
				
		for (MethDec next : new MethodCollector(classDef).getCollection()){
			methodCheck = new SignatureEntityIsUsedInMethod(att, next);
			if(methodCheck.isValid())
				return false;
		}
		return true;
	}
}