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
 * Comprueba el correcto funcionamiento de la refactorización RenameField.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class TestRenameField extends RefactoringTemplateAbstractTest{
	
	private static final String BEFORE = "before";
	private static final String TESTDATA = "testdata";
	private static final String AFTER = "after";
	private static final String ANONYMOUS = "<anonymous>";
	private static final String PAQUETE_A = "paqueteA";
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre
	 * una clase sencilla con un constructor, un atributo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testSimpleTest() throws Exception{
		check("simpleTest",ANONYMOUS);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase que
	 * contiene una asignación de valor al atributo en la propia clase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheLeftInTheClass() throws Exception{
		check("assignmentOnTheLeftInTheClass",ANONYMOUS);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase que
	 * utiliza el valor del atributo para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheRightInTheClass() throws Exception{
		check("assignmentOnTheRightInTheClass",ANONYMOUS);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que hace una 
	 * llamada a un método utilizando como parámetro al atributo a renombrar.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testMethodParameterInTheClass() throws Exception{
		check("methodParameterInTheClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que devuelve
	 * el atributo a refactorizar como valor de retorno de un método.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testReturnTypeInClass() throws Exception{
		check("returnTypeInClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase que
	 * contiene una asignación de valor de un atributo de otra clase (que va a ser 
	 * el atributo a renombrar).
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheLeftInOtherClass() throws Exception{
		check("assignmentOnTheLeftInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase que
	 * utiliza el valor de un atributo (el que va a ser renombrado) 
	 * de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testAssignmentOnTheRightInOtherClass() throws Exception{
		check("assignmentOnTheRightInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase que
	 * que tiene un método que devuelve el valor del atributo (atributo a ser renombrado)
	 * de otra clase.  
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testReturnTypeInOtherClass() throws Exception{
		check("returnTypeInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una subclase que
	 * utiliza el atributo de su superclase (atributo que va a ser renonbrado).
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testSubclassUsingAttribute() throws Exception{
		check("subclassUsingAttribute",PAQUETE_A);				
	}
	/**
	 * Comprueba la correcta ejecuci�n de la refactorización RenameField sobre una clase que
	 * utiliza el valor de un atributo de otra clase (el que va a ser renombrado) como 
	 * parámetro de un método determinado.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testMethodParameterInOtherClass() throws Exception{
		check("methodParameterInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una cuando ya existe un atributo con dixho nombre
	 * en la clase a la que pertenece el atributo a ser renombrado.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)
	public void testAttributeWithTheSameName() throws Exception{
		check("attributeWithTheSameName",ANONYMOUS);				
	}
	
	

	/**
	 * Ejecuta la refactorización sobre un caso determinado.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete java de la clase sobre la que se esta probando 
	 * 		  la refactorización.
	 * @throws Exception Excepci�n en caso de haber algún problema durante
	 * la ejecuci�n de la refactorización.
	 */
	private void check(String dir, String paquete) throws Exception{
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(TESTDATA + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + BEFORE);
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


		if(new File(TESTDATA + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + BEFORE
				+File.separator + paquete + File.separator + "C.java").exists()){

			String source = Regenerate.regenerate("C.java");
			String target = EclipsePrettyPrinter.formatCompilationUnit(source);

			if (paquete.equals(ANONYMOUS)){
				this.compare(target, TESTDATA + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + AFTER +File.separator + "C.java");

			}else{
				this.compare(target, TESTDATA + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + AFTER 
						+ File.separator + paquete+ File.separator + "C.java");
			}

		}
		String source = Regenerate.regenerate("B.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		//Compare the two "files", must be equals...
		if (paquete.equals(ANONYMOUS)){
			this.compare(target, TESTDATA + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + AFTER +File.separator + "B.java");

		}else{
			this.compare(target, TESTDATA + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + AFTER 
					+ File.separator + paquete+ File.separator + "B.java");
		}
	}
	/**
	 * Comprueba que la refactorización se deshace de forma correcta.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete de la clase sobre la que se esta probando 
	 * 		  la refactorización.
	 * @throws Exception Excepci�n en caso de haber algún problema mientras se
	 * deshace la refactorización.
	 */
	private void undo(String dir, String paquete) throws Exception {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(TESTDATA + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + BEFORE);
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

		if(new File(TESTDATA + File.separator
				+ this.getClass().getName().replace(".", File.separator) 
				+ File.separator + dir + File.separator + BEFORE
				+File.separator + paquete + File.separator + "C.java").exists()){
			
			String source = Regenerate.regenerate("C.java");
			String target = EclipsePrettyPrinter.formatCompilationUnit(source);
			
			if (paquete.equals(ANONYMOUS)){
				this.compare(target, TESTDATA + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + BEFORE +File.separator + "C.java");

			}else{
				this.compare(target, TESTDATA + File.separator
						+ this.getClass().getName().replace(".", File.separator) 
						+ File.separator + dir + File.separator + BEFORE 
						+ File.separator + paquete+ File.separator + "C.java");
			}
			
		}
		String source = Regenerate.regenerate("B.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		//Compare the two "files", must be equals...
		if (paquete.equals(ANONYMOUS)){
			this.compare(target, TESTDATA + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + BEFORE +File.separator + "B.java");

		}else{
			this.compare(target, TESTDATA + File.separator
					+ this.getClass().getName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + BEFORE 
					+ File.separator + paquete+ File.separator + "B.java");
		}
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre
	 * una clase sencilla con un constructor, un atributo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoSimpleTest() throws Exception{
		undo("simpleTest",ANONYMOUS);				
	}
	
	/**
	 * CComprueba que la refactorización se deshace de forma correcta sobre una clase que
	 * contiene una asignación de valor al atributo en la propia clase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheLeftInTheClass() throws Exception{
		undo("assignmentOnTheLeftInTheClass",ANONYMOUS);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una clase que
	 * utiliza el valor del atributo para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheRightInTheClass() throws Exception{
		undo("assignmentOnTheRightInTheClass",ANONYMOUS);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que hace una 
	 * llamada a un método utilizando como parámetro a ese atributo.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoMethodParameterInTheClass() throws Exception{
		undo("methodParameterInTheClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta. sobre una clase
	 * que esta dentro de un paquete con un atributo y un constructor y que devuelve
	 * el atributo a refactorizar en un método.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoReturnTypeInClass() throws Exception{
		undo("returnTypeInClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una clase que
	 * contiene una asignación de valor de un atributo de otra clase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheLeftInOtherClass() throws Exception{
		undo("assignmentOnTheLeftInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una clase que
	 * utiliza el valor de un atributo de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoAssignmentOnTheRightInOtherClass() throws Exception{
		undo("assignmentOnTheRightInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una clase que
	 * utiliza el valor de un atributo de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoReturnTypeInOtherClass() throws Exception{
		undo("returnTypeInOtherClass",PAQUETE_A);				
	}
	
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una subclase que
	 * utiliza el atributo de su superclase.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoSubclassUsingAttribute() throws Exception{
		undo("subclassUsingAttribute",PAQUETE_A);				
	}
	/**
	 * Comprueba que la refactorización se deshace de forma correcta sobre una clase que
	 * utiliza el valor de un atributo de otra clase para asignar a una variable dicho valor.
	 *    
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoMethodParameterInOtherClass() throws Exception{
		undo("methodParameterInOtherClass",PAQUETE_A);				
	}

}
