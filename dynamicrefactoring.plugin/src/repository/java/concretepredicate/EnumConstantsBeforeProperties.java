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
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Compueba que los atributos que pasar�n a ser constantes en el tipo enumerado 
 * est�n antes que el resto de los métodos y atributos.
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">LauraFuente de la Fuente</A>
 */ 
public class EnumConstantsBeforeProperties extends Predicate {
	
	/**
	 * Nombre de la clase sobre la que se quiere comprobar que los atributos que pasar�n a 
	 * ser constantes en el tipo enumerado est�n antes que el resto de los métodos y atributos.
	 */
	private JavaClassDef clase;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado EnumConstantsBeforeOthers.
	 *
	 * @param clase Clase sobre la que se quiere comprobar que los atributos que pasar�n a 
	 * ser constantes en el tipo enumerado est�n antes que el resto de los métodos y atributos.
	 */
	public EnumConstantsBeforeProperties(JavaClassDef clase) {
		
		super("EnumConstantsBeforeOthers:\n\t" + //$NON-NLS-1$
			"Makes sure that the attributes that will be constants in " + 
			"the target enum type are before other attributes and methods "
			+ "in the class " +  clase.getName() +"."); //$NON-NLS-1$
		
		this.clase = clase;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si las constantes est�n ordenadas
	 *  <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {
		
		long maxLineToEnum = 999999999;//valor m�ximo para long
		
		List<AttDec> attributes = clase.getAttributes();
		for(int i=0; i<attributes.size(); i++){
			JavaAttDec attribute = (JavaAttDec)attributes.get(i);
			if(!(ModifierSet.isFinal(attribute.getModifiers()) && 
					ModifierSet.isStatic(attribute.getModifiers()) &&
					ModifierSet.isPublic(attribute.getModifiers()) &&
					attribute.getType().toString().equals(clase.getName().toString()))){
				if(attribute.getLine()<maxLineToEnum){
					maxLineToEnum=attribute.getLine();
				}	
			}
		}
		List<MethDec> methods = clase.getMethDec();
		for(int i=0; i<methods.size(); i++){
			MethDec meth =methods.get(i);
			if(meth.getLine()<maxLineToEnum)
				maxLineToEnum=meth.getLine();
		}
		for(int i=0; i<attributes.size(); i++){
			JavaAttDec attribute = (JavaAttDec)attributes.get(i);
			if(ModifierSet.isFinal(attribute.getModifiers()) && 
					ModifierSet.isStatic(attribute.getModifiers()) &&
					ModifierSet.isPublic(attribute.getModifiers()) &&
					attribute.getType().toString().equals(clase.getName().toString())){
				if(attribute.getLine()>maxLineToEnum){
					return false;
				}	
			}
		}
		
		return true;
			
	}
		
}
