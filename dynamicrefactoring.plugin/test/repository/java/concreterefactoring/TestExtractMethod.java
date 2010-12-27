package repository.java.concreterefactoring;

import java.io.File;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.core.JavaName;
import javamoon.core.instruction.JavaCodeFragment;
import javamoon.regenerate.Regenerate;
import javamoon.utils.EclipsePrettyPrinter;
import moon.core.MoonFactory;
import moon.core.classdef.ClassDef;
import moon.core.instruction.CodeFragment;

import org.junit.Test;


import refactoring.engine.PreconditionException;
import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;
import repository.moon.concreterefactoring.ExtractMethod;

/**
 * tests de extractMethod.
 * 
 * @author Raúl Marticorena
 *
 */
public class TestExtractMethod extends RefactoringTemplateAbstractTest{

	/**
	 * Extract method with return instruction.
	 *    
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testExtractMethodWithReturn() throws Exception{
		check("simpleTest","paqueteA");				
	}
	
	/**
	 * Extract method without return instruction.
	 *    
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testExtractMethodNoReturn() throws Exception{
		check("noReturnTest","paqueteA");				
	}
	
	/**
	 * Extract method impossible with double return.
	 *    
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 * @throws PreconditionException precondición que debe ser lanzada.
	 */
	@Test(expected=PreconditionException.class)
	public void testExtractMethodDoubleReturn() throws PreconditionException, Exception{
		check("doubleReturnTest","paqueteA");				
	}
	
	
	/**
	 * Ejecuta la refactorización sobre un caso determinado.
	 * 
	 * @param dir directorio sobre el que se encuentra el caso a probar.
	 * @param paquete paquete java de la clase sobre la que se esta probando 
	 * 		  la refactorización.
	 * @throws Exception Excepción en caso de haber algún problema durante
	 * la ejecución de la refactorización.
	 */
	private void check(String dir, String paquete) throws Exception{
		SourceLoader sourceLoader = new SourceLoader();

		sourceLoader.loadFromDirectoryWithBinaryLoad("testdata" + File.separator
				+ this.getClass().getName().replace(".", File.separator) + File.separator + dir + File.separator + "before");
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	
		
		ClassDef cd = jm.getClassDef(new JavaName("paqueteA.A"));
		
		CodeFragment codeFragment = new JavaCodeFragment(8,0,9,0,cd,"");
		
		
		MOONRefactoring extract = new ExtractMethod(new JavaName("n"),codeFragment, jm);			
		extract.run();		
		
		String source = Regenerate.regenerate("A.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

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
}
	
