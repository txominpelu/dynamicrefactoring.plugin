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
import moon.core.classdef.MethDec;
import repository.java.concreteaction.AddJUnit4AnnotationTestExceptionValue;
import repository.java.concreteaction.RemoveJUnit3FailInstructions;
import repository.java.concreteaction.RemoveTryCatchFinally;
import repository.java.concretepredicate.IsJUnit4TestMethod;
import repository.moon.MOONRefactoring;


/**
 * 
 * Migrate a class test method containing exception catch
 * from JUnit 3 to JUnit 4.
 * 
 * @author Raúl Marticorena
 * 
 */
public class MigrateJUnit3ToJUnit4TestException extends MOONRefactoring {

	/**
	 * Nombre de la refactorización.
	 */
	private static final String NAME = "MigrateJUnit3ToJUnit4ExceptionTest";
	
	/**
	 * Migrates an exception test method in JUnit 3 to JUnit4.
	 *  
	 * @param methDec method
	 * @param model model
	 */
	public MigrateJUnit3ToJUnit4TestException(MethDec methDec, Model model) {
		super(NAME,model);
		// Preconditions		
		this.addPrecondition(new IsJUnit4TestMethod(methDec));
		
		// Actions
		this.addAction(new AddJUnit4AnnotationTestExceptionValue(methDec));
		this.addAction(new RemoveTryCatchFinally(methDec));
		this.addAction(new RemoveJUnit3FailInstructions(methDec));
		
		// Postconditions		 
		this.addPostcondition(new IsJUnit4TestMethod(methDec));
	}

}
