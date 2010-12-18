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

package repository.java.concreterefactoring;

import javamoon.core.classdef.JavaClassDef;
import moon.core.Model;
import moon.core.classdef.ClassDef;
import repository.java.concreteaction.AddEnumTypeWithClassData;
import repository.java.concretepredicate.HasPublicFinalStaticAttributes;
import repository.java.concretepredicate.EnumConstantsBeforeProperties;
import repository.moon.MOONRefactoring;
import repository.java.concretepredicate.ExistsEnumType;
import repository.java.concretepredicate.AreConstructorsPrivate;

/**
 * 
 * Transforma una clase con unas caracteristicas determinadas a un tipo enumerado.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class EnumeratedType extends MOONRefactoring {

	/**
	 * Name.
	 */
	private static final String NAME = "EnumeratedType";
	
	/**
	 * Migrates from class to enum.
	 *  
	 * @param classDef class
	 * @param model model
	 */
	public EnumeratedType(ClassDef classDef, Model model) {
		super(NAME,model);
		// Preconditions
		this.addPrecondition(new HasPublicFinalStaticAttributes((JavaClassDef)classDef));
		this.addPrecondition(new EnumConstantsBeforeProperties((JavaClassDef)classDef));
		this.addPrecondition(new AreConstructorsPrivate((JavaClassDef)classDef));		
				
		// Actions
		this.addAction(new AddEnumTypeWithClassData(classDef));
		
		// Postconditions
		this.addPostcondition(new ExistsEnumType(classDef.getName()));
		
	}
}
