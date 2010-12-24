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
import java.io.PrintWriter;
import java.util.Scanner;

import repository.moon.MOONRefactoring;

import javamoon.construct.binary.BinaryLoader;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.utils.Comparator;
import javamoon.utils.EclipsePrettyPrinter;
import junit.framework.TestCase;

/**
 * Refactoring test template.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.1.0
 */
public abstract class RefactoringTemplateAbstractTest extends TestCase{

	/**
	 * Source loader.
	 */
	protected static SourceLoader sourceLoader;
	
	/**
	 * Binary jar file with basic classes.
	 * 
	 */
	public static final String JARFILE = "mini-java-1.6.0_02.jar";
	
	/**
	 * Binary jar file with JUnit basic classes.
	 * 
	 */
	private static final String JUNITJARFILE = "junit-4.4.jar";
	
	/**
	 * Constructor.
	 * 
	 * @param name nombre.
	 */
	public RefactoringTemplateAbstractTest(String name) {
        super(name);        
    }
    
	/**
	 * Set up tests.
	 * 
	 * @throws Exception if something goes wrong at beginning
	 */
    public void setUp() throws Exception{     	
    	
    		BinaryLoader bl = new BinaryLoader();		
    		bl.addClassesFromPackageInJar("java.lang", "." + File.separator + "jarFiles" + File.separator + JARFILE);
    		bl.addClassesFromPackageInJar("java.lang.annotation", "." + File.separator + "jarFiles" + File.separator + JARFILE);
    		bl.addClassesFromPackageInJar("java.util", "." + File.separator + "jarFiles" + File.separator + JARFILE);
    		bl.addClassesFromPackageInJar("java.io", "." + File.separator + "jarFiles" + File.separator + JARFILE);		
    		bl.addClassesFromPackageInJar("org.junit", "." + File.separator + "jarFiles" + File.separator + JUNITJARFILE);
    		bl.addClassesFromPackageInJar("junit.framework", "." + File.separator + "jarFiles" + File.separator + JUNITJARFILE);    		
    		bl.addClassesFromPackageInJar("junit.textui", "." + File.separator + "jarfiles" + File.separator + JUNITJARFILE);
    		bl.load();  	
    		
   	
    }    
    
    
    /**
     * Tear down.
     */
    public void tearDown() {
		JavaModel.getInstance().reset();
		MOONRefactoring.resetModel();
		System.gc();
	}

    
    /**
     * Compares a string with file content, line to line.
     * 
     * @param source initial string
     * @param file file to read
     * @throws IOException when errors with file accesss
     */
    protected void compare(String source, String file) throws IOException{
    	final boolean WRITE = true;

		String initial = new Scanner(new File(file)).useDelimiter("" + File.separatorChar + "Z").next();
		String initialFormatted = null;
		if (WRITE) {
			initialFormatted = EclipsePrettyPrinter
					.formatCompilationUnit(initial);
		} else {
			initialFormatted = initial;
		}

		source = EclipsePrettyPrinter.formatCompilationUnit(source);

		// Uncomment if you want to see the strings...
		System.out.println(initialFormatted);
		System.out.println(source);
		
		boolean bool = Comparator.compareText(initialFormatted, source, false);
		if(!bool)
			System.out.println("Failure comparing " + file + ".java with");
		
		assertTrue("Failure comparing " + file + ".java with", bool);
		
		if (WRITE && !bool) {
			PrintWriter printWriter = new PrintWriter(new File(file));
			printWriter.write(initialFormatted);
			printWriter.close();
		}
	}
}
