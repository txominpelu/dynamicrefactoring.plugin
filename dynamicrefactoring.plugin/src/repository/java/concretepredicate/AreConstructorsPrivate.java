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
import javamoon.core.entity.JavaFunctionDec;
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Compueba que los constructores de una clase son privados.
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">LauraFuente de la Fuente</A>
 */ 
public class AreConstructorsPrivate extends Predicate {
	
	/**
	 * Clase sobre la que se quiere comprobar que los constructores son privados.
	 */
	private JavaClassDef clase;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ConstructorsArePrivate.
	 *
	 * @param clase Clase sobre la que se quiere comprobar que losconstructores son privados.
	 */
	public AreConstructorsPrivate(JavaClassDef clase) {
		
		super("ConstructorsArePrivate:\n\t" + //$NON-NLS-1$
			"Makes sure that the constructors of the class " + clase.getName() +
			" are private."); //$NON-NLS-1$
		
		this.clase = clase;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si los constructores spon privados
	 *  <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {
		
		List<MethDec> constructores = clase.getMethDecByName(clase.getName());
		
			for(int i=0; i<constructores.size(); i++){
				if(ModifierSet.isPublic(((JavaFunctionDec)constructores.get(i)).getModifiers())){
					return false;
				}
			}
		
		return true;
			
	}
		
}

