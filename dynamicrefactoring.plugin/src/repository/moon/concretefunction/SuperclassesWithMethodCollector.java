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

package repository.moon.concretefunction;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.inheritance.InheritanceClause;
import refactoring.engine.Function;

/**
 * Permite obtener las superclases de una clase dada que declaren un cierto 
 * método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class SuperclassesWithMethodCollector extends Function {
	
	/**
	 * La clase cuyas superclases se van a explorar.
	 */
	private ClassDef classDef;
	
	/**
	 * El nombre único del método cuyo equivalente en una superclase se busca.
	 */
	private String methodUniqueName;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de SuperclassesWithMethodCollector.
	 *
	 * @param classDef la clase cuyas superclases se van a explorar.
	 * @param methodUniqueName el nombre único del método que se busca en una superclase.
	 */
	public SuperclassesWithMethodCollector(ClassDef classDef, String methodUniqueName){
		
		this.classDef = classDef;
		this.methodUniqueName = methodUniqueName;
	}

	/**
	 * Obtiene las clases que se encuentra al recorrer hacia arriba la
	 * jerarquía de herencia a partir de la clase <code>classDef</code>, y que
	 * contengan un cierto método.<p>
	 *
	 * Si por una rama de herencia encuentra una clase que contiene el método,
	 * no sigue buscando hacia las superclases de la misma. En cambio, si 
	 * encuentra superclases que no contienen el método, contin�a buscando a su
	 * vez en las clases superiores de las mismas.
	 *
	 * @return Las clases encontradas o null, si no se encontr� ninguna.
	 */ 
	public Collection<ClassDef> getCollection() {
		
		Collection<ClassDef> foundClasses = new Vector<ClassDef>();
		
		Collection<InheritanceClause> inheritanceClauses = 
			classDef.getInheritanceClause();
		
		for (InheritanceClause clause : inheritanceClauses){
			Type parentClassType = clause.getType();
			
			if(parentClassType instanceof ClassType){
				ClassDef parentClass = parentClassType.getClassDef();
					
				int indexOfMethodName = methodUniqueName.lastIndexOf('~');
				String methNameWithoutPath = methodUniqueName.substring(indexOfMethodName);
				
				String newMethUniqueName = 
					parentClass.getUniqueName().toString() + methNameWithoutPath;
				List<MethDec> classMethods = parentClass.getMethDec();
				for(MethDec nextMethod : classMethods){
					if (nextMethod.getUniqueName().toString().equals(newMethUniqueName)){
						foundClasses.add(parentClass);
						break;
					}
				}	
								
				SuperclassesWithMethodCollector recursiveSearch = 
					new SuperclassesWithMethodCollector(parentClass, methodUniqueName);
				Collection<ClassDef> recursivelyFoundClasses = 
					recursiveSearch.getCollection();
					
				if(! recursivelyFoundClasses.isEmpty()){
					Iterator<ClassDef> foundClassesIt = 
						recursivelyFoundClasses.iterator();
					while(foundClassesIt.hasNext()){
						ClassDef nextClass = foundClassesIt.next();
						if(!foundClasses.contains(nextClass)){
							foundClasses.add(nextClass);
						}
					}
				}
			}
		}		
		
		return foundClasses;
	}
	
	/**
	 * Sin implementación.
	 *
	 * @return null.
	 */
	public Object getValue() {
		return null;		
	}
}