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

import org.junit.Test;

import refactoring.engine.PostconditionException;
import refactoring.engine.PreconditionException;
import refactoring.engine.Refactoring;
import repository.RefactoringTemplateAbstractTest;


/**
 * Tests the refactoring that migrates from class to enum.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @since JavaMoon-2.2.2
 */
public class EnumeratedTypeTest extends RefactoringTemplateAbstractTest{


	/**
	 * Comprueba la correcta ejecución de la refactorización EnumeratedType sobre
	 * una clase sencilla con un constructor, un atributo y una constante.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSimpleTest() throws Exception{
		check("simpleTest","<anonymous>");				
	}
	
	/**
	 * Comprueba la correcta ejecución de la refactorización EnumeratedType sobre
	 * una clase sencilla dentro de un paquete con un constructor, un atributo
	 * y varias constantes.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSimpleClass() throws Exception{
		check("simpleTest2","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecución de la refactorización EnumeratedType sobre
	 * una clase sencilla que solo tiene constantes.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testOnlyWithConstants() throws Exception{
		check("onlyWithConstants","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecución de la refactorización EnumeratedType sobre
	 * una clase sencilla cuyo constructor cuenta con dos enteros.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testConstructorWithTwoExpresion() throws Exception{
		check("constructorWithTwoExpresion","paqueteA");				
	}
	
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando la clase no dispone de constantes.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */   
	@Test(expected=PreconditionException.class)
	public void testWithoutFinalStaticAttributes() throws Exception{
		exception("withoutFinalStaticAttributes","<anonymous>");				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando los atributos de la clase se encuentran
	 *    antes que las constantes de la misma.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */   
	@Test(expected=PreconditionException.class)
	public void testAttributesBeforeConstants() throws Exception{
		exception("attributesBeforeConstants","paqueteA");				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando los atributos de la clase se encuentran
	 *    antes que alguna de las constantes de la misma.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)
	public void testAttributesBeforeSomeConstants() throws Exception{
		exception("attributesBeforeSomeConstants","paqueteA");				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando los metodos de la clase se encuentran
	 *    antes que las constantes de la misma.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)
	public void testMethodBeforeConstants() throws Exception{
		exception("methodBeforeConstants","paqueteA");				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando los atributos y métodos de la clase se encuentran
	 *    por medio de las constantes de la misma.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)
	public void testMethodAttributeBetweenConstants() throws Exception{
		exception("methodAttibuteBetweenConstants","paqueteA");				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando hay un constructor que no es privado.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)
	public void testConstructorPublic() throws Exception{
		exception("constructorPublic","paqueteA");				
	}
	
	 
	/**
	 * Ejecuta la refactorización sobre un caso determinado.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete java de la clase sobre la que se esta probando 
	 * 		  la refactorización.
	 * @throws ParseException ParseException
	 * @throws BinaryClassFileException BinaryClassFileException
	 * @throws PreconditionException PreconditionException
	 * @throws PostconditionException PostconditionException
	 * @throws IOException IOException
	 */
	private void check(String dir, String paquete) throws BinaryClassFileException, 
										PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");

		String source = Regenerate.regenerate("A.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName(paquete+".A"));
		Refactoring refactoring = new EnumeratedType(jc, JavaModel
				.getInstance());
		refactoring.run();

		source = Regenerate.regenerate("A.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
		System.out.println("\ntarget:\n" + target);

		//Compare the two "files", must be equals...
		if (paquete.equals("<anonymous>")){
			this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "after" +File.separator + "A.java");
	
		}else{
			this.compare(target, "testdata" + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" 
					+ File.separator + paquete+ File.separator + "A.java");
		}
	}
	
	/**
	 * Ejecuta la refactorización sobre un caso determinado en el que va a aparecer
	 * una excepción.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete de la clase sobre la que se esta probando 
	 * 		  la refactorización.
	 * @throws ParseException ParseException
	 * @throws BinaryClassFileException BinaryClassFileException
	 * @throws PreconditionException PreconditionException
	 * @throws PostconditionException PostconditionException
	 * @throws IOException IOException
	 */
	private void exception(String dir, String paquete) throws BinaryClassFileException, 
	PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator 
				+ "exceptions"+ File.separator + dir  + File.separator + "before");


		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName(paquete+".A"));
		Refactoring refactoring = new EnumeratedType(jc, JavaModel
				.getInstance());
		refactoring.run();
	}

	/**
	 * Comprueba que la refactorización se deshace de forma correcta.
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testConstructorWithTwoExpresionUndo() throws Exception{
		undo("constructorWithTwoExpresion","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta.
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testOnlyWithConstantsUndo() throws Exception{
		undo("onlyWithConstants","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta.
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSimpleTestUndo() throws Exception{
		undo("simpleTest","<anonymous>");		
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta.
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSimpleClassUndo() throws Exception{
		undo("simpleTest2","paqueteA");		
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete de la clase sobre la que se esta probando 
	 * 		  la refactorización.
	 * @throws ParseException ParseException
	 * @throws BinaryClassFileException BinaryClassFileException
	 * @throws PreconditionException PreconditionException
	 * @throws PostconditionException PostconditionException
	 * @throws IOException IOException
	 */
	private void undo(String dir, String paquete) throws BinaryClassFileException, 
						PreconditionException, PostconditionException, IOException {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");

		String source = Regenerate.regenerate("A.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		JavaClassDef jc = (JavaClassDef) JavaModel.getInstance().getClassDef(
				new JavaName(paquete+".A"));
		Refactoring refactoring = new EnumeratedType(jc, JavaModel
				.getInstance());
		refactoring.run();
		
		// undo actions...
		refactoring.undoActions();

		source = Regenerate.regenerate("A.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);

		// the file must be recovered as it was initially
		if (paquete.equals("<anonymous>")){
			this.compare(target, "testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "after" +File.separator + "A.java");
	
		}else{
			this.compare(target, "testdata" + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" 
					+ File.separator + paquete+ File.separator + "A.java");
		}
	}
 


}
