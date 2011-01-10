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

public class ExtractMethodWithGenericsTest extends RefactoringTemplateAbstractTest{


	public ExtractMethodWithGenericsTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setUp() throws Exception {
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
	 * Extract method using unknown type.
	 * 
	 * @throws Exception.
	 */
	@Test
	public void testExtractMethodUsingUnknownType() throws Exception {
		check("usingUnknownType", "test", "Lists", new Fragmento(12, 15,
				"Object[] a = c.toArray();\n" + "int numNew = a.length;\n"
						+ "ensureCapacity(size + numNew);\n"
						+ "System.arraycopy(a, 0, elementData, size, numNew);"));
	}

	/**
	 * Extract method with type inference from declarations.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testExtractMethodWithTypeInferenceFromDeclarations()
			throws Exception {
		check("typeInferenceFromDeclarations", "test", "Lists", new Fragmento(
				10, 12, "for (T elt : arr) {\n" + "list.add(elt);\n" + "}"));
	}

	/**
	 * Extract method with simple bound in method formal parameter.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testExtractMethodWithSimpleBoundInMethodFormalParameter()
			throws Exception {
		check("simpleBoundInMethodFormalParameter", "test", "Lists",
				new Fragmento(12, 17, "while (i > 0) {\n" + "buf.flip();\n"
						+ "trg.append(buf);\n" + "buf.clear();\n"
						+ "i = src.read(buf);\n" + "}"));
	}


	

	
	


	/**
	 * Extract method with multiple bound in method formal parameter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExtractMethodWithMultipleBoundInMethodFormalParameter()
			throws Exception {
		check("multipleBoundInMethodFormalParameter", "test", "Lists",
				new Fragmento(13, 18, "while (i > 0) {\n" + "buf.flip();\n"
						+ "trg.append(buf);\n" + "buf.clear();\n"
						+ "i = src.read(buf);\n" + "}"));
	}
	
	/**
	 * Extract method with class formal parameter.
	 * 
	 * @throws Exception.
	 */
	@Test
	public void testExtractMethodWithClassFormalParameter() throws Exception {
		check(
				"classFormalParameter",
				"test",
				"Lists",
				new Fragmento(
						14,
						18,
						"E oldValue = (E) elementData[index];\n"
								+ "int numMoved = size - index - 1;\n"
								+ "if (numMoved > 0)\n"
								+ "System.arraycopy(elementData, index + 1, elementData, index,\n"
								+ "numMoved);"));
	}
	
	/**
	 * Extract method with formal parameter inferred from generic array type.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExtractMethodMethodFormalParameterInferredFromGenericArrayType()
			throws Exception {
		check("methodFormalParameterInferredFromGenericArrayType", "test", "Lists",
				new Fragmento(14, 16, "System.arraycopy(elementData, 0, a, 0, size);\n"
						+ "if (a.length > size)\n"
						+ "a[size] = null;"));
	}

	/**
	 * Ejecuta la refactorización sobre un caso determinado.
	 * 
	 * @param dir
	 *            directorio sobre el que se encuentra el caso a probar.
	 * @param paquete
	 *            paquete java de la clase sobre la que se esta probando la
	 *            refactorización.
	 * @throws Exception
	 *             Excepción en caso de haber algún problema durante la
	 *             ejecución de la refactorización.
	 */
	private void check(String dir, String paquete, String nombreClase, Fragmento fragmento) throws PreconditionException,Exception{
		SourceLoader sourceLoader = new SourceLoader();

		sourceLoader
				.loadFromDirectoryWithBinaryLoad(ExtractMethodTest.PATH_TESTDATA
				+ this.getClass().getSimpleName().replace(".", File.separator) + File.separator + dir + File.separator + "before");
		
		JavaModel jm = JavaModel.getInstance();
		
		
		ClassDef cd = jm.getClassDef(new JavaName(paquete + "." + nombreClase));
		
		CodeFragment codeFragment = new JavaCodeFragment(fragmento.inicio,0,fragmento.fin,0,cd,fragmento.text);
			
		
		MOONRefactoring extract = new ExtractMethod(new JavaName("n"),codeFragment, jm);			
		extract.run();		
		
		String source = Regenerate.regenerate(nombreClase + ".java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		//Compare the two "files", must be equals...
		
		if (paquete.equals("<anonymous>")){
			this.compare(target, ExtractMethodTest.PATH_TESTDATA
					+ this.getClass().getSimpleName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" +File.separator + nombreClase + ".java");

		}else{
			this.compare(target, ExtractMethodTest.PATH_TESTDATA
					+ this.getClass().getSimpleName().replace(".", File.separator) 
					+ File.separator + dir + File.separator + "after" 
					+ File.separator + paquete+ File.separator + nombreClase + ".java");
		}
		
		System.out.println(target);
	}

	/**
	 * Extract method with bounded unknown type with formal parameter.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testExtractMethodWithBoundedUnknownTypeWithFormalParameter() throws Exception{
		check("boundedUnknownTypeWithFormalParameter","test","Lists", 
				new Fragmento(8,10,				
				"for (int i = 0; i < src.size(); i++) {\n" +
				"dst.set(i, src.get(i));\n" +
				"}"));				
	}
	
	/**
	 * Suite.
	 * 
	 * 	@return new suite
	 */
    public static TestSuite suite() {
        return new TestSuite(ExtractMethodWithGenericsTest.class);
    }
}
	
