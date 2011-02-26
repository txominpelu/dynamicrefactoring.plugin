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

package repository.moon.concreterefactoring;

import java.io.File;
import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.regenerate.Regenerate;
import javamoon.utils.EclipsePrettyPrinter;
import moon.core.MoonFactory;
import moon.core.Name;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;

import org.junit.Test;

import refactoring.engine.PreconditionException;
import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/**
 * 
 * Comprueba el correcto funcionamiento de la refactorizaci�n RenameField.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class TestRenameField extends RefactoringTemplateAbstractTest{
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre
	 * una clase sencilla con un constructor, un atributo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testSimpleTest() throws Exception{
		check("simpleTest","<anonymous>");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase que
	 * contiene una asignaci�n de valor al atributo en la propia clase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheLeftInTheClass() throws Exception{
		check("assignmentOnTheLeftInTheClass","<anonymous>");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase que
	 * utiliza el valor del atributo para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheRightInTheClass() throws Exception{
		check("assignmentOnTheRightInTheClass","<anonymous>");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que hace una 
	 * llamada a un m�todo utilizando como par�metro al atributo a renombrar.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testMethodParameterInTheClass() throws Exception{
		check("methodParameterInTheClass","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que devuelve
	 * el atributo a refactorizar como valor de retorno de un m�todo.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testReturnTypeInClass() throws Exception{
		check("returnTypeInClass","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase que
	 * contiene una asignaci�n de valor de un atributo de otra clase (que va a ser 
	 * el atributo a renombrar).
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheLeftInOtherClass() throws Exception{
		check("assignmentOnTheLeftInOtherClass","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase que
	 * utiliza el valor de un atributo (el que va a ser renombrado) 
	 * de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheRightInOtherClass() throws Exception{
		check("assignmentOnTheRightInOtherClass","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase que
	 * que tiene un m�todo que devuelve el valor del atributo (atributo a ser renombrado)
	 * de otra clase.  
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testReturnTypeInOtherClass() throws Exception{
		check("returnTypeInOtherClass","paqueteA");				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una subclase que
	 * utiliza el atributo de su superclase (atributo que va a ser renonbrado).
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testSubclassUsingAttribute() throws Exception{
		check("subclassUsingAttribute","paqueteA");				
	}
	/**
	 * Comprueba la correcta ejecuci�n de la refactorizaci�n RenameField sobre una clase que
	 * utiliza el valor de un atributo de otra clase (el que va a ser renombrado) como 
	 * par�metro de un m�todo determinado.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testMethodParameterInOtherClass() throws Exception{
		check("methodParameterInOtherClass","paqueteA");				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una cuando ya existe un atributo con dixho nombre
	 * en la clase a la que pertenece el atributo a ser renombrado.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)
	public void testAttributeWithTheSameName() throws Exception{
		check("attributeWithTheSameName","<anonymous>");				
	}
	
	

	/**
	 * Ejecuta la refactorizaci�n sobre un caso determinado.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete java de la clase sobre la que se esta probando 
	 * 		  la refactorizaci�n.
	 * @throws Exception Excepci�n en caso de haber alg�n problema durante
	 * la ejecuci�n de la refactorizaci�n.
	 */
	private void check(String dir, String paquete) throws Exception{
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName(paquete + ".B")); //$NON-NLS-1$
		List <AttDec> lAtt = classDef.getAttributes(); //$NON-NLS-1$
		AttDec att=null;
		for(AttDec attribute: lAtt){
			if(attribute.getName().equals(factory.createName("a"))){
				att=attribute;
			}		
		}
		Name name = factory.createName("nuevoAtt"); //$NON-NLS-1$



		MOONRefactoring renombrado = new RenameField(att,classDef, name, jm);			
		renombrado.run();


		if(new File("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "before"
				+File.separator + paquete + File.separator + "C.java").exists()){

			String source = Regenerate.regenerate("C.java");
			String target = EclipsePrettyPrinter.formatCompilationUnit(source);

			if (paquete.equals("<anonymous>")){
				this.compare(target, "testdata" + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + "after" +File.separator + "C.java");

			}else{
				this.compare(target, "testdata" + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + "after" 
						+ File.separator + paquete+ File.separator + "C.java");
			}

		}
		String source = Regenerate.regenerate("B.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		//Compare the two "files", must be equals...
		if (paquete.equals("<anonymous>")){
			this.compare(target, "testdata" + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" +File.separator + "B.java");

		}else{
			this.compare(target, "testdata" + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" 
					+ File.separator + paquete+ File.separator + "B.java");
		}
	}
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete de la clase sobre la que se esta probando 
	 * 		  la refactorizaci�n.
	 * @throws Exception Excepci�n en caso de haber alg�n problema mientras se
	 * deshace la refactorizaci�n.
	 */
	private void undo(String dir, String paquete) throws Exception {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName(paquete + ".B")); //$NON-NLS-1$
		List <AttDec> lAtt = classDef.getAttributes(); //$NON-NLS-1$
		AttDec att=null;
		for(AttDec attribute: lAtt){
			if(attribute.getName().equals(factory.createName("a"))){
				att=attribute;
			}		
		}
		Name name = factory.createName("nuevoAtt"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameField(att,classDef, name, jm);			
		renombrado.run();
		
		// undo actions...
		renombrado.undoActions();

		if(new File("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + "before"
				+File.separator + paquete + File.separator + "C.java").exists()){
			
			String source = Regenerate.regenerate("C.java");
			String target = EclipsePrettyPrinter.formatCompilationUnit(source);
			
			if (paquete.equals("<anonymous>")){
				this.compare(target, "testdata" + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + "before" +File.separator + "C.java");

			}else{
				this.compare(target, "testdata" + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + "before" 
						+ File.separator + paquete+ File.separator + "C.java");
			}
			
		}
		String source = Regenerate.regenerate("B.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		//Compare the two "files", must be equals...
		if (paquete.equals("<anonymous>")){
			this.compare(target, "testdata" + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "before" +File.separator + "B.java");

		}else{
			this.compare(target, "testdata" + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "before" 
					+ File.separator + paquete+ File.separator + "B.java");
		}
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre
	 * una clase sencilla con un constructor, un atributo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoSimpleTest() throws Exception{
		undo("simpleTest","<anonymous>");				
	}
	
	/**
	 * CComprueba que la refactorizaci�n se deshace de forma correcta sobre una clase que
	 * contiene una asignaci�n de valor al atributo en la propia clase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheLeftInTheClass() throws Exception{
		undo("assignmentOnTheLeftInTheClass","<anonymous>");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una clase que
	 * utiliza el valor del atributo para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheRightInTheClass() throws Exception{
		undo("assignmentOnTheRightInTheClass","<anonymous>");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que hace una 
	 * llamada a un m�todo utilizando como par�metro a ese atributo.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoMethodParameterInTheClass() throws Exception{
		undo("methodParameterInTheClass","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta. sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que devuelve
	 * el atributo a refactorizar en un m�todo.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoReturnTypeInClass() throws Exception{
		undo("returnTypeInClass","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una clase que
	 * contiene una asignaci�n de valor de un atributo de otra clase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheLeftInOtherClass() throws Exception{
		undo("assignmentOnTheLeftInOtherClass","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una clase que
	 * utiliza el valor de un atributo de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheRightInOtherClass() throws Exception{
		undo("assignmentOnTheRightInOtherClass","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una clase que
	 * utiliza el valor de un atributo de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoReturnTypeInOtherClass() throws Exception{
		undo("returnTypeInOtherClass","paqueteA");				
	}
	
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una subclase que
	 * utiliza el atributo de su superclase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoSubclassUsingAttribute() throws Exception{
		undo("subclassUsingAttribute","paqueteA");				
	}
	/**
	 * Comprueba que la refactorizaci�n se deshace de forma correcta sobre una clase que
	 * utiliza el valor de un atributo de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoMethodParameterInOtherClass() throws Exception{
		undo("methodParameterInOtherClass","paqueteA");				
	}

}
