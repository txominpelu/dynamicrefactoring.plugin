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

import moon.core.Name;
import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;
import moon.core.classdef.ClassDef;

/**
 * Comprueba la existencia de un tipo enumerado dentro del modelo.
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">LauraFuente de la Fuente</A>
 */ 
public class ExistsEnumType extends Predicate {
	
	/**
	 * Nombre del tipo enumerado del que queremos conocer su existencia.
	 */
	private Name enumName;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ExistsEnumType.
	 *
	 * @param enumName nombre del tipo enumerado del que queremos conocer su existencia.
	 */
	public ExistsEnumType(Name enumName) {
		
		super("ExistsEnumType:\n\t" + //$NON-NLS-1$
			"Makes sure that the enumerated type named " + '"' +  //$NON-NLS-1$
			enumName + '"' +
			" exists in the model."); //$NON-NLS-1$
		
		this.enumName = enumName;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el parámetro en el método 
	 * especificado; <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {
		try{
			Class<?> enumtype = Class.forName("javamoon.core.classdef.JavaEnum");
			
			for(ClassDef c: MOONRefactoring.getModel().getClassDef()){
				if(c.getName().toString().equals(enumName.toString()) && 
						c.getClass().equals(enumtype))
					return true;
			}
			
		}catch(ClassNotFoundException exception){
			System.out.println("The class javamoon.core.classdef.JavaEnum is not found" );
		}
		
		return false;
			
	}
		
}
