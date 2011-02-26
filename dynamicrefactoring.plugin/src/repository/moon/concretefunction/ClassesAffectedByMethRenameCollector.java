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

import moon.core.classdef.ClassDef;
import refactoring.engine.Function;

/**
 * Permite obtener las clases de una jerarqu�a de herencia que se podr�an ver 
 * afectadas por un cambio en la signatura de un m�todo en una cierta clase de 
 * la jerarqu�a.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class ClassesAffectedByMethRenameCollector extends Function {
	
	/**
	 * La clase a la que pertenece el m�todo que se ver�a afectado por un cambio
	 * en su signatura.
	 */
	private ClassDef classDef;
	
	/**
	 * El nombre �nico del m�todo que sufrir�a un cambio en su signatura.
	 */
	private String methodUniqueName;
	
	/**
	 * Clases que ya se han inclu�do como afectadas.
	 */
	private Collection<ClassDef> alreadyFound;
	
	/**
	 * Booleano que indica si se quiere revisar tambi�n las subclases o solo se
	 * quiere revisar las superclases.
	 */
	private Boolean subclases;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de ClassesAffectedByMethRenameCollector.
	 *
	 * @param classDef la clase que sufrir�a el cambio en la signatura de un m�todo.
	 * @param methodUniqueName el nombre �nico del m�todo cuya signatura sufrir�a un cambio.
	 * @param alreadyFound la lista de clases ya incluidas como afectadas.
	 * @param subclases indica si se quiere revisar tambi�n las subclases o solo se
	 * quiere revisar las superclases.
	 */
	public ClassesAffectedByMethRenameCollector(ClassDef classDef, 
		String methodUniqueName, Collection<ClassDef> alreadyFound, Boolean subclases) {
		this.subclases=subclases;	
		this.classDef = classDef;
		this.methodUniqueName = methodUniqueName;
		this.alreadyFound = alreadyFound;
	}

	/**
	 * Obtiene las clases que se encuentra al recorrer recursivamente la
	 * jerarqu�a de herencia de una clase y que, por tanto, podr�an 
	 * hipot�ticamente verse afectadas por un cambio en la signatura de un 
	 * m�todo de la clase original.
	 *
	 * @return Las clases encontradas o una colecci�n vac�a, si no se encontr� ninguna.
	 */ 
	public Collection<ClassDef> getCollection() {
		
		boolean newClassesFound = false;
		Collection<ClassDef> affectedClasses = alreadyFound;
		
		
		
		SuperclassesWithMethodCollector searchSuperclasses =
			new SuperclassesWithMethodCollector(classDef, methodUniqueName);
		Collection<ClassDef> foundClasses = searchSuperclasses.getCollection();
		
		
		if(subclases==true){
		
		SubclassesWithMethodCollector searchSubclasses =
			new SubclassesWithMethodCollector(classDef, methodUniqueName);	
		Collection<ClassDef> subclasses  = searchSubclasses.getCollection();
		
		subclasses.removeAll(foundClasses);
		foundClasses.addAll(subclasses);
		}
		
		
		Iterator<ClassDef> foundClassesIt = foundClasses.iterator();
		while(foundClassesIt.hasNext()){
			ClassDef nextClass = foundClassesIt.next();
			
			if(! alreadyFound.contains(nextClass)){
				affectedClasses.add(nextClass);
				newClassesFound = true;
			}
		}
		
		alreadyFound = affectedClasses;
		
		if(newClassesFound){
			
			foundClassesIt = foundClasses.iterator();
			while(foundClassesIt.hasNext()){
				ClassDef nextClass = foundClassesIt.next();
				ClassesAffectedByMethRenameCollector getNewClasses =
					new ClassesAffectedByMethRenameCollector(
						nextClass, methodUniqueName, affectedClasses,subclases);
				Collection<ClassDef> moreClasses = getNewClasses.getCollection();
				
				Iterator<ClassDef> moreClassesIt = moreClasses.iterator();				
				while(moreClassesIt.hasNext()){
					ClassDef nextNewClass = moreClassesIt.next();
					
					if(! alreadyFound.contains(nextNewClass)){
						affectedClasses.add(nextNewClass);
					}
				}
			}
		}
		
		return affectedClasses;		
		
	}
	
	/**
	 * Sin implementaci�n.
	 *
	 * @return null.
	 */
	public Object getValue() {
		return null;		
	}
}