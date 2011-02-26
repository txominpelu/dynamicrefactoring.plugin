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
import moon.core.classdef.MethDec;

import org.junit.Test;

import refactoring.engine.PostconditionException;
import refactoring.engine.PreconditionException;
import refactoring.engine.Refactoring;
import repository.RefactoringTemplateAbstractTest;

/**
 * Tests the refactoring that adds an override annotation.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @since JavaMoon-2.2.2
 */
public class OverrideAnnotationTest extends RefactoringTemplateAbstractTest{


	/**
	 * Prueba la refactorizaci�n con un m�todo carente de par�metros.
	 * 
	 * @throws Exception if something goes wrong
	 */
	@Test
	public void testSimpleTest() throws Exception{
		check("simpleTest");				
	}
	
	/**
	 * Prueba la refactorizaci�n con un m�todo con par�metros.
	 * 
	 * @throws Exception if something goes wrong
	 */
	@Test
	public void testWithParameters() throws Exception{
		check("withParameters");				
	}
	
	
	
	
	 
	/**
	 * Checks.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @throws ParseException ParseException
	 * @throws BinaryClassFileException BinaryClassFileException
	 * @throws PreconditionException PreconditionException
	 * @throws PostconditionException PostconditionException
	 * @throws IOException IOException
	 */
	private void check(String dir) throws BinaryClassFileException, 
										PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");

		String source = Regenerate.regenerate("A.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName("<anonymous>.A"));
		
		MethDec m = jc.getMethDecByName(new JavaName("met1")).get(0);
		Refactoring refactoring = new OverrideAnnotation(m, JavaModel
				.getInstance());
		try{
		refactoring.run();
		}catch(Exception e){}

		source = Regenerate.regenerate("A.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
		System.out.println(target);
		

		//Compare the two "files", must be equals...
		this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "after" + File.separator + "A.java");
	}


	/**
	 * Prueba a deshacer un caso correcto sobre un m�todo sin par�metros.
	 * 
	 * @throws Exception if something goes wrong
	 */
	@Test
	public void testSimpleTestUndo() throws Exception{
		undo("simpleTest");		
	}
	
	/**
	 * Prueba a deshacer un caso correcto sobre un m�todo sin par�metros.
	 * 
	 * @throws Exception if something goes wrong
	 */
	@Test
	public void testWithParametersUndo() throws Exception{
		undo("withParameters");		
	}
	
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @throws ParseException ParseException
	 * @throws BinaryClassFileException BinaryClassFileException
	 * @throws PreconditionException PreconditionException
	 * @throws PostconditionException PostconditionException
	 * @throws IOException IOException
	 */
	private void undo(String dir) throws BinaryClassFileException, 
						PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");

		
		String source = Regenerate.regenerate("A.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName("<anonymous>.A"));
		
		MethDec m = jc.getMethDecByName(new JavaName("met1")).get(0);
		Refactoring refactoring = new OverrideAnnotation(m, JavaModel
				.getInstance());
		refactoring.run();

		// undo actions...
		refactoring.undoActions();

		source = Regenerate.regenerate("A.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);

		// the file must be recovered as it was initially
		this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "before" + File.separator + "A.java");
	}
	
	
	
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una cuando la superclase de la clase en donde se 
	 *    encuentra el m�todo sobre el que operamos no tiene un m�todo con la misma
	 *    signatura que este.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test(expected=PreconditionException.class)
	public void testNotMethodSignatureIsInSuperclass() throws Exception{
		exceptions("notMethodSignatureIsInSuperclass");
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una cuando el m�todo sobre el que estamos operando
	 *    ya tiene una anotaci�n override.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test(expected=PreconditionException.class)
	public void testHasOverrideAnnotation() throws Exception{
		exceptions("hasOverrideAnnotation");
	}
	
	/**
	 * Comprueba el correcto lanzamiento de excepciones.
	 * 
	 * @param dir directorio.
	 * @throws Exception excepcion en caso de error.
	 */
	private void exceptions(String dir) throws Exception{ 

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + "exceptions" 
				+ File.separator + dir); //$NON-NLS-1$

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName("<anonymous>.A"));
		
		MethDec m = jc.getMethDecByName(new JavaName("met1")).get(0);
		Refactoring refactoring = new OverrideAnnotation(m, JavaModel
				.getInstance());
		refactoring.run();

	}

}
