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

package repository.java.concretepredicate;

import java.util.List;

import javamoon.core.ModifierSet;
import javamoon.core.classdef.JavaClassDef;
import javamoon.core.entity.JavaAttDec;
import moon.core.classdef.AttDec;
import refactoring.engine.Predicate;


/**
 * Permite comprobar que una clase tiene atributos p�blicos finales y 
 * est�ticos cuyo tipo corresponde al tipo de la clase en la que son definidos.
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */ 
public class HasPublicFinalStaticAttributes extends Predicate {
	
	/**
	 * Clase sobre la que se quiere comprobar al existencia de un constructor.
	 */
	private JavaClassDef classDef;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado HasPublicFinalStaticAttributes.
	 *
	 * @param classDef la clase que se desea comprobar los atributos.
	 */
	public HasPublicFinalStaticAttributes(JavaClassDef classDef) {
		
		super("HasOnlyFinalStaticAttributes: \n\t" + //$NON-NLS-1$
			  "Checks whether the class " + '"' +  //$NON-NLS-1$
			  classDef.getName().toString() + '"' +
			  "has only final and static attributes" +
			  "which type is the same as the type of the class." + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.classDef = classDef;
	}	
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si la clase posee un constructor con un entero 
	 * como par�metro, <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {	
		List<AttDec> attributes = classDef.getAttributes();
		
		for(int i=0; i<attributes.size(); i++){
			JavaAttDec attribute = (JavaAttDec)attributes.get(i);
			if(ModifierSet.isFinal(attribute.getModifiers()))
				if(ModifierSet.isStatic(attribute.getModifiers()))
					if(ModifierSet.isPublic(attribute.getModifiers()))
						if(attribute.getType().toString().equals(classDef.getName().toString()))
							return true;
		}
		
		return false;	
	}
}

