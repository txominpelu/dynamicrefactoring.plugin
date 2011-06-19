package repository.java.concreterefactoring;


import java.io.File;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.core.JavaName;
import javamoon.core.instruction.JavaCodeFragment;
import javamoon.regenerate.Regenerate;
import javamoon.utils.EclipsePrettyPrinter;
import junit.framework.TestSuite;
import moon.core.classdef.ClassDef;
import moon.core.instruction.CodeFragment;

import org.junit.Test;

import refactoring.engine.PreconditionException;
import repository.moon.MOONRefactoring;
import repository.moon.concreterefactoring.ExtractMethod;

public class ExtractMethodTest extends RefactoringTemplateAbstractTest{


	public static final String PATH_TESTDATA = "testdata" + File.separator + "repository" + File.separator + "java" + File.separator + "concreterefactoring" + File.separator;

	public ExtractMethodTest(String name) {
		super(name);
	}
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		// nothing to do
		// we don't want the binary load from the superclass...
	}

	class Fragmento{
		int inicio;
		int fin;
		String text;
		
		Fragmento(int inicio, int fin, String text){
			this.inicio = inicio;
			this.fin = fin;
			this.text = text;
		}
		
		Fragmento(int inicio, int fin){
			this.inicio = inicio;
			this.fin = fin;			
		}
	}

	/**
	 * Extract method with return instruction.
	 *    
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testExtractMethodWithReturn() throws Exception{
		check("simpleTest","paqueteA","A", new Fragmento(8,9,
				"a = 5;\n" + 
				"System.out.println(b);"));				
	}
	
	/**
	 * Extract method without return instruction.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodNoReturn() throws Exception{
		check("noReturnTest","paqueteA","A", new Fragmento(8,9,
				"a = 5;\n" + 
				"System.out.println(b);"));				
	}

	
	/**
	 * Extract method without variables in Fowler's example.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodFowlerExampleWithoutVariables() throws PreconditionException, Exception{
		check("fowlerExampleWithoutVariables","fowler","FowlerExample", new Fragmento(24,26,
				"System.out.println(\"**********************\");\n" +
				"System.out.println(\"**** Custom Owes *****\");\n" +
				"System.out.println(\"**********************\");"));				
	}
	
	
	
	/**
	 * Extract method impossible with double return.
	 * @throws Exception 
	 *    
	 * @throws exception
	 */
	public void testExtractMethodFowlerExampleDoubleReturn() throws Exception {
		try{
			check("fowlerExampleDoubleReturn","fowler","FowlerExample", new Fragmento(84,88,
				"while (e.hasMoreElements()) {\n" +
				"	Order each = (Order) e.nextElement();\n" + 
				"	outstanding += each.getAmount();\n" +
				"}\n" +
				"otherOutstanding += outstanding;"));
				fail("Expected PreconditionException");
		}
		catch(PreconditionException ex){			
		}
	}
	
	/**
	 * Extract method with variables in Fowler's example.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodFowlerExampleWithVariables() throws PreconditionException, Exception{
		check("fowlerExampleWithVariables","fowler","FowlerExample", new Fragmento(40,41,
				"System.out.println(\"name:\" + _name);\n" +
				"System.out.println(\"amount:\" + outstanding);" ));				
	}
	
	/**
	 * Extract method with variables and one return in Fowler's example.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodFowlerExampleOneReturn() throws PreconditionException, Exception{
		check("fowlerExampleOneReturn","fowler","FowlerExample", new Fragmento(33,36,
				"while (e.hasMoreElements()) {\n" + 
				"	Order each = (Order) e.nextElement();\n" + 
				"	outstanding += each.getAmount();\n" + 
				"}" ));				
	}
	
	/**
	 * Extract method with variables, one return and the type
	 * declaration must be repeated outside (in Fowler's example).
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodFowlerExampleOneReturnWithDeclaration() throws PreconditionException, Exception{
		check("fowlerExampleOneReturnWithDeclaration","fowler","FowlerExample", new Fragmento(31,35,
				"double outstanding = 0.0;\n" +
				"while (e.hasMoreElements()) {\n" + 
				"	Order each = (Order) e.nextElement();\n" + 
				"	outstanding += each.getAmount();\n" + 
				"}" ));				
	}
	
	/**
	 * Extract method with loop reentrance.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodWithLoopReentrance() throws PreconditionException, Exception{
		check("loopReentrance","test","LoopReentrance", new Fragmento(10,12,
				"attribute++;\n" + 
				"i = i + 1;\n" +
				"System.out.println(\"i = \" + i + \", \" + \"\" + \"attribute = \" + attribute);"
			));				
	}
	
	/**
	 * Extract method with loop reentrance.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodWithLoopReentrance2() throws PreconditionException, Exception{
		check("loopReentrance2","test","LoopReentrance", new Fragmento(10,12,
				"attribute++;\n" + 
				"i++;\n" +
				"System.out.println(\"i = \" + i + \", \" + \"\" + \"attribute = \" + attribute);"
			));				
	}
	
	
	/**
	 * Extract method with loop reentrance with double return.
	 *    
	 * @throws exception
	 */
	public void testExtractMethodWithLoopReentranceDoubleReturn() throws PreconditionException, Exception{
		try{
		check("loopReentranceDoubleReturn","test","LoopReentrance", new Fragmento(23,24,
				"acc += j;\n" +
				"j++;"
			));
			fail("Expected precondition exception.");
		}
		catch(PreconditionException ex){
			
		}
	}
	

	/**
	 * Extract method impossible with double return.
	 *    
	 * @throws exception
	 */	
	public void testExtractMethodDoubleReturn() throws PreconditionException, Exception{
		try{
		check("doubleReturnTest","paqueteA","A",new Fragmento(8,9,
				"a = 5;\n" +
				"b = 5;"));
			fail("Expected precondition exception.");
		}
		catch(PreconditionException ex){
			
		}
	}
	
	/**
	 * Extract method with loop reentrance with i++ in System.out.println.
	 *    
	 */
	@Test
	public void testExtractMethodWithLoopReentrance3() throws PreconditionException, Exception{
		check("loopReentrance3","test","LoopReentrance", new Fragmento(10,11,
				"attribute++;\n" + 
				"System.out.println(\"i = \" + i++ + \", \" + \"\" + \"attribute = \" + attribute);"
			));				
	}
	
	/**
	 * Extract method with exception handling.
	 *    
	 * @throws exception
	 */
	@Test
	public void testExtractMethodExceptionHandling1() throws PreconditionException, Exception{
		check("exceptionHandling1","paqueteA","A",new Fragmento(12,21,
				"// begin\n" + 
				"FileInputStream fileIn = new FileInputStream(new File(\"./in\"));\n" + 
				"FileOutputStream fileOut = new FileOutputStream(new File(\"./out\"));\n" + 
				"while(fileIn.available()>0){\n" + 
				"	int b = fileIn.read();\n" +
				"	fileOut.write(b);\n" +				
				"}\n" + 
				"fileIn.close();\n" +
				"fileOut.close();\n" +
				"// end"));								
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
	private void check(String dir, String paquete, String nombreClase, Fragmento fragmento) throws PreconditionException,Exception{
		SourceLoader sourceLoader = new SourceLoader();

		sourceLoader.loadFromDirectoryWithBinaryLoad(PATH_TESTDATA + this.getClass().getSimpleName().replace(".", File.separator) + File.separator + dir + File.separator
				+ "before");
		
		JavaModel jm = JavaModel.getInstance();
		
		
		ClassDef cd = jm.getClassDef(new JavaName(paquete + "." + nombreClase));
		
		CodeFragment codeFragment = new JavaCodeFragment(fragmento.inicio,0,fragmento.fin,0,cd,fragmento.text);
			
		
		MOONRefactoring extract = new ExtractMethod(new JavaName("n"),codeFragment, jm);			
		extract.run();		
		
		String source = Regenerate.regenerate(nombreClase + ".java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		//Compare the two "files", must be equals...
		
		if (paquete.equals("<anonymous>")){
			this.compare(target, PATH_TESTDATA 
					+ this.getClass().getSimpleName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" +File.separator + nombreClase + ".java");

		}else{
			this.compare(target, PATH_TESTDATA
					+ this.getClass().getSimpleName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" 
					+ File.separator + paquete+ File.separator + nombreClase + ".java");
		}
		
		System.out.println(target);
	}
	
	/**
	 * Suite.
	 * 
	 * 	@return new suite
	 */
    public static TestSuite suite() {
        return new TestSuite(ExtractMethodTest.class);
    }
}
	
