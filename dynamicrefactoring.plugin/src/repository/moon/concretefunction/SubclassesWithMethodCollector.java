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
import moon.core.classdef.MethDec;
import refactoring.engine.Function;
import repository.moon.MOONRefactoring;

/**
 * Permite obtener las subclases a una clase dada que redefinan un cierto método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class SubclassesWithMethodCollector extends Function {
	
	/**
	 * La clase cuyas subclases se van a explorar.
	 */
	private ClassDef classDef;
	
	/**
	 * El nombre único del método cuyo equivalente en una subclase se busca.
	 */
	private String methodUniqueName;
				
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de SubclassesWithMethodCollector.
	 *
	 * @param classDef la clase cuyas subclases se van a explorar.
	 * @param uniqueName el nombre único del método cuyo equivalente se busca.
	 */
	public SubclassesWithMethodCollector(ClassDef classDef, String uniqueName) {
		
		this.classDef = classDef;
		this.methodUniqueName = uniqueName;
	}

	/**
	 * Obtiene las clases que se encuentra al recorrer hacia abajo la
	 * jerarquía de herencia a partir de la clase <code>classDef</code>, y que
	 * contengan un cierto método.
	 *
	 * @return Las clases encontradas, o null si no se encontrá ninguna.
	 */ 
	public Collection<ClassDef> getCollection() {
		
		Collection<ClassDef> foundClasses = new Vector<ClassDef>();
		
		Iterator<ClassDef> allClassesIt = 
			MOONRefactoring.getModel().getClassDef().iterator();
		
		while(allClassesIt.hasNext()){
			
			ClassDef nextClass = allClassesIt.next();
			
			SuperclassCollector superclassesCollector = 
				new SuperclassCollector(nextClass);
				
			Collection<ClassDef> foundSuperclasses = 
				superclassesCollector.getCollection();
			
			if(foundSuperclasses.contains(classDef)){
				
				int indexOfMethodName = methodUniqueName.lastIndexOf('~');
				String methNameWithoutPath = 
					methodUniqueName.substring(indexOfMethodName);
				String newMethUniqueName = 
					nextClass.getUniqueName().toString() + methNameWithoutPath;
				
				List<MethDec> classMethods = nextClass.getMethDec();
				for(MethDec nextMethod : classMethods)
					if (nextMethod.getUniqueName().toString().equals(newMethUniqueName)){
						foundClasses.add(nextClass);
						break;
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