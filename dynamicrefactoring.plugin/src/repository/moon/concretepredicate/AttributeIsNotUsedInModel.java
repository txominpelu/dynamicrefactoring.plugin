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

import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;

/**
 * Permite comprobar que un determinado atributo de clase no es utilizado en el 
 * cuerpo de ninguna clase del modelo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class AttributeIsNotUsedInModel extends Predicate {
	
	/**
	 * El atributo cuyo uso en las clases de un modelo MOON se quiere estudiar.
	 */
	private AttDec att;
			
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado AttributeIsNotUsedInModel.
	 *
	 * @param att el atributo cuyo uso se desea estudiar.
	 */
	public AttributeIsNotUsedInModel(AttDec att) {
		
		super("AttributeIsNotUsedInModel: \n\t" + //$NON-NLS-1$
			  "Makes sure the attribute " + '"' + att.getName().toString() +  //$NON-NLS-1$
			  '"' + " is not used within any class." + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.att = att;
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el atributo no se emplea en ninguna clase 
	 * del modelo; <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {	
		AttributeIsNotUsedInClass classCheck;
				
		Iterator<ClassDef> classIterator = 
			MOONRefactoring.getModel().getClassDef().iterator();
		
		while(classIterator.hasNext()){			
			classCheck = new AttributeIsNotUsedInClass(att,classIterator.next());
			
			if(!(classCheck.isValid()))
				return false;
		}
		return true;
	}
}