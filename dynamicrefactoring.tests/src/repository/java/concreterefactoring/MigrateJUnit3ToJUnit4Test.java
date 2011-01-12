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


import java.io.File;
import java.io.IOException;

import javamoon.construct.binary.BinaryClassFileException;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.core.JavaName;
import javamoon.core.classdef.JavaClassDef;
import javamoon.regenerate.Regenerate;
import javamoon.utils.EclipsePrettyPrinter;
import junit.framework.TestSuite;
import moon.core.classdef.ClassType;


import refactoring.engine.PostconditionException;
import refactoring.engine.PreconditionException;
import refactoring.engine.Refactoring;
import repository.java.concreterefactoring.MigrateJUnit3ToJUnit4;

/**
 * Tests the refactoring that migrates fromo JUnit 3 test
 * to new versions.
 * 
 * @author Ra�l Marticorena
 * @since JavaMoon-2.1.0
 */
public class MigrateJUnit3ToJUnit4Test extends RefactoringTemplateAbstractTest{

	/**
	 * Constructor.
	 * 
	 * @param name nombre.
	 */
	public MigrateJUnit3ToJUnit4Test(String name) {
		super(name);		
	}
	

	/**
	 * Tests a correct running without problems. Final result
	 * must be the new JUnit4 test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualRun() throws Exception{
		check("usualRun");				
	}
	
	
	/**
	 * Tests a correct running without problems. Final result
	 * must be the new JUnit4 test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	public void testSimpleTest() throws Exception{
		check("simpleTest");				
	}
	
	/**
	 * Checks.
	 * 
	 * @param dir directorio de la refactorizaci�n.
	 * @throws BinaryClassFileException BinaryClassFileException.
	 * @throws PreconditionException PreconditionException.
	 * @throws PostconditionException PostconditionException.
	 * @throws IOException IOException.
	 */
	private void check(String dir) throws BinaryClassFileException, 
										PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");

		String source = Regenerate.regenerate("OldTest.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName("<anonymous>.OldTest"));
		ClassType ct = (ClassType) JavaModel.getInstance().getType(
				new JavaName("junit.framework.TestCase"));
		Refactoring refactoring = new MigrateJUnit3ToJUnit4(jc, ct, JavaModel
				.getInstance());
		refactoring.run();

		source = Regenerate.regenerate("OldTest.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);

		// Compare the two "files", must be equals...
		this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "after" + File.separator + "OldTest.java");
	}
	
	/**
	 * Tests a correct running without problems. Final result
	 * must be the new JUnit4 test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualUndo() throws Exception{
		undo("usualRun");		
	}
	

	/**
	 * Tests a correct running without problems. Final result
	 * must be the new JUnit4 test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testSimpleTestUndo() throws Exception{
		undo("simpleTest");		
	}
	
	
	/**
	 * Permite deshacer la refactorizaci�n.
	 * 
	 * @param dir directorio de la refactorizaci�n.
	 * @throws BinaryClassFileException BinaryClassFileException.
	 * @throws PreconditionException PreconditionException.
	 * @throws PostconditionException PostconditionException.
	 * @throws IOException IOException.
	 */
	private void undo(String dir) throws BinaryClassFileException, 
						PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");

		String source = Regenerate.regenerate("OldTest.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName("<anonymous>.OldTest"));
		ClassType ct = (ClassType) JavaModel.getInstance().getType(
				new JavaName("junit.framework.TestCase"));

		Refactoring refactoring = new MigrateJUnit3ToJUnit4(jc, ct, JavaModel
				.getInstance());
		refactoring.run();

		// undo actions...
		refactoring.undoActions();

		source = Regenerate.regenerate("OldTest.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);

		// the file must be recovered as it was initially
		this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "before" + File.separator + "OldTest.java");
	}
	
	/**
	 * Suite.
	 * 
	 * 	@return new suite
	 */
    public static TestSuite suite() {
        return new TestSuite(MigrateJUnit3ToJUnit4Test.class);
    }
 


}
