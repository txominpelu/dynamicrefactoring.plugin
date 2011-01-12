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

import moon.core.classdef.MethDec;


import refactoring.engine.PostconditionException;
import refactoring.engine.PreconditionException;
import refactoring.engine.Refactoring;

import repository.java.concreterefactoring.MigrateJUnit3ToJUnit4TestException;

/**
 * Tests the refactoring that migrates fromo JUnit 3 exception test
 * to new versions.
 * 
 * @author Ra�l Marticorena
 * @since JavaMoon-2.1.0
 */
public class MigrateJUnit3ToJUnit4ExceptionTest extends RefactoringTemplateAbstractTest{

	/**
	 * Constructor.
	 * 
	 * @param name nombre.
	 */
	public MigrateJUnit3ToJUnit4ExceptionTest(String name) {
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
	 * must be the initial JUnit4 method test.
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
	
	public void testUsualRun2() throws Exception{
		check("usualRun2");		
	}
	
	
	/**
	 * Tests a correct running without problems. Final result
	 * must be the initial JUnit4 method test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualUndo2() throws Exception{
		undo("usualRun2");		
	}
	
	

	
	
	/**
	 * Tests a correct running without problems. Final result
	 * must be the initial JUnit4 method test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualUndo3() throws Exception{
		undo("usualRun3");		
	}

	/**
	 * Tests a correct running without problems. Final result
	 * must be the new JUnit4 test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualRun4() throws Exception{
		check("usualRun4");		
	}
	
	
	/**
	 * Tests a correct running without problems. Final result
	 * must be the initial JUnit4 method test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualUndo4() throws Exception{
		undo("usualRun4");		
	}
	
	
	/**
	 * Suite.
	 * 
	 * 	@return new suite
	 */
    public static TestSuite suite() {
        return new TestSuite(MigrateJUnit3ToJUnit4ExceptionTest.class);
    }
 
 
    /**
     * Checks the test.
     * 
     * @param dir dir
     * @throws ParseException
     * @throws BinaryClassFileException
     * @throws PreconditionException
     * @throws PostconditionException
     * @throws IOException
     */
    private void check(String dir) throws BinaryClassFileException, PreconditionException, PostconditionException, IOException{
    	SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");
		
		String source = Regenerate.regenerate("OldOtherTest.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
			
		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(new JavaName("<anonymous>.OldOtherTest"));		
		MethDec md = jc.getMethDecByName(new JavaName("testException")).get(0);
		
		Refactoring refactoring = new MigrateJUnit3ToJUnit4TestException(md,JavaModel.getInstance());
		refactoring.run();
		
		source = Regenerate.regenerate("OldOtherTest.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
		// Compare the two "files", must be equals...
		this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "after" + File.separator + "OldOtherTest.java");
    }
    
    /**
     * Checks undo actions.
     * 
     * @param dir dir
     * @throws ParseException
     * @throws BinaryClassFileException
     * @throws PreconditionException
     * @throws PostconditionException
     * @throws IOException
     */
    private void undo(String dir) throws BinaryClassFileException, PreconditionException, PostconditionException, IOException{
    	SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");
		
		String source = Regenerate.regenerate("OldOtherTest.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(new JavaName("<anonymous>.OldOtherTest"));		
		MethDec md = jc.getMethDecByName(new JavaName("testException")).get(0);
		
		Refactoring refactoring = new MigrateJUnit3ToJUnit4TestException(md,JavaModel.getInstance());
		refactoring.run();
		
		// undo actions...
		refactoring.undoActions();
		
		source = Regenerate.regenerate("OldOtherTest.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
				
		// the file must be recovered as it was initially
		this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "before" + File.separator + "OldOtherTest.java");
    }

	/**
	 * Tests a correct running without problems. Final result
	 * must be the new JUnit4 test.
	 * 
	 * @throws Exception if something goes wrong
	 */
	
	public void testUsualRun3() throws Exception{
		check("usualRun3");		
	}

}
