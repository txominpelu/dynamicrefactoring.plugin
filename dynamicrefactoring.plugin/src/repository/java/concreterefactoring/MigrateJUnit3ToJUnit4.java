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


import moon.core.Model;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import repository.java.concreteaction.AddJUnit4Annotation;
import repository.java.concreteaction.AddJUnit4Imports;
import repository.java.concreteaction.RemoveJUnit3Imports;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.RemoveInheritanceClause;
import repository.moon.concretepredicate.IsSubtype;

/**
 * 
 * Migrate a class test from JUnit 3 to JUnit 4.
 * 
 * @author Raúl Marticorena
 * 
 */
public class MigrateJUnit3ToJUnit4 extends MOONRefactoring {

	/**
	 * Name.
	 */
	private static final String NAME = "MigrateJUnit3ToJUnit4";
	
	/**
	 * Migrates from test class in JUnit 3 to JUnit4.
	 *  
	 * @param classDef class
	 * @param classType type i.e. junit.framework.TestCase
	 * @param model model
	 */
	public MigrateJUnit3ToJUnit4(ClassDef classDef, ClassType classType, Model model) {
		super(NAME,model);
		// Preconditions
		this.addPrecondition(new IsSubtype(classDef.getClassType(), classType));
		// Actions
		this.addAction(new RemoveJUnit3Imports(classDef));
		this.addAction(new AddJUnit4Imports(classDef));
		this.addAction(new RemoveInheritanceClause(classDef, classType)); 								
		this.addAction(new AddJUnit4Annotation(classDef));
		// Postconditions
		// not available now... 
		// this.addPrecondition(new IsNotSubtype(classDef.getClassType(), classType));
	}
}
