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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.regenerate.Regenerate;
import javamoon.utils.EclipsePrettyPrinter;
import moon.core.MoonFactory;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.LocalDec;
import moon.core.classdef.MethDec;

import org.junit.Test;

import refactoring.engine.PreconditionException;
import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/**
 * Comprueba que funciona correctamente la refactorización que renombra un
 * método de una clase.
 * 
 * <p>
 * Indirectamente, se comprueba también la corrección de las funciones, acciones
 * y predicados utilizados por la refactorización.
 * </p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 * @see RenameMethod
 */
public class TestRenameMethod extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que la refactorización funciona correctamente al hacer un
	 * renombrado sencillo de un método.
	 * 
	 * <p>
	 * En un modelo con una única clase, toma un método sin argumentos formales,
	 * valor de retorno, ni variables locales, y le asigna un nuevo nombre
	 * correcto, distinto al de los demás métodos de la clase.
	 * </p>
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */    
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(jm.getMoonFactory().createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("nuevoMetodo"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName =factory.createName(classDef.getUniqueName().toString() +
			"~" + "nuevoMetodo" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();				

		// Comienzan las comprobaciones.
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertTrue("Test renombrar método simple: todavía existe el " + //$NON-NLS-1$
				"método con el nombre anterior.", lMetodo2.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDef.getMethDecByName(factory.createName("nuevoMetodo")); //$NON-NLS-1$
		assertFalse("Test renombrar método simpel: no existe el método" + //$NON-NLS-1$
			" con el nuevo nombre.", lMetodo3.isEmpty()); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la refactorización funciona correctamente al hacer un
	 * renombrado de un método en una superclase.
	 * 
	 * <p>
	 * En un modelo con dos clases (una extiende a la otra), renombra un método
	 * en la superclase que está siendo redefinido en la subclase, comprobando
	 * que los cambios se extiendan correctamente por la jerarquía de herencia.
	 * </p>
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */  
	@Test
	public void testWithInheritance() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testWithInheritance")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("nuevoMetodo"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName =factory.createName(classDef.getUniqueName().toString() + 
			"~" + "nuevoMetodo" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();	

		// Comienzan las comprobaciones.
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertTrue("Test renombrar método con herencia: todavía existe el " + //$NON-NLS-1$
				"método con el nombre anterior.", lMetodo2.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDef.getMethDecByName(factory.createName("nuevoMetodo")); //$NON-NLS-1$
		assertFalse("Test renombrar método con herencia: No existe el método" + //$NON-NLS-1$
			" con el nuevo nombre.", lMetodo3.isEmpty()); //$NON-NLS-1$

		ClassDef classDef2 = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		List <MethDec> lMetodo4 = classDef2.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertTrue("Test renombrar método con herencia: Todavía existe el " + //$NON-NLS-1$
				"método con el nombre anterior.", lMetodo4.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo5 = classDef2.getMethDecByName(factory.createName("nuevoMetodo")); //$NON-NLS-1$
		assertFalse("Test renombrar método con herencia: no existe el método" + //$NON-NLS-1$
			" con el nuevo nombre", lMetodo5.isEmpty()); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la refactorización funciona correctamente al hacer un
	 * renombrado un método con argumentos formales, variables locales y valor
	 * de retorno.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */  
	@Test
	public void testWithContents() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testWithContents")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("nuevoMetodo"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName =factory.createName(classDef.getUniqueName().toString() + 
			"~" + "nuevoMetodo" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();	

		// Comienzan las comprobaciones.
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertTrue("Test renombrar método con contenidos: todavía existe el " + //$NON-NLS-1$
				"método con el nombre anterior.", lMetodo2.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDef.getMethDecByName(factory.createName("nuevoMetodo")); //$NON-NLS-1$
		assertFalse(
				"Test renombrar método con contenidos: no existe el método" + //$NON-NLS-1$
			" con el nuevo nombre.", lMetodo3.isEmpty()); //$NON-NLS-1$

		MethDec metodo3 = lMetodo3.get(0);
		List <FormalArgument> lArgumentos = metodo3.getFormalArgument();
		FormalArgument argumento = lArgumentos.get(0);

		assertEquals(
				"Test renombrar método con contenidos: no se ha " + //$NON-NLS-1$
						"modificado correctamente el nombre del argumento formal en el método", //$NON-NLS-1$
				factory.createName("paqueteA.ClaseA~nuevoMetodo%String#java.lang.String:b(0)"), //$NON-NLS-1$
			argumento.getUniqueName());

		List <LocalDec> ld = metodo3.getLocalDecs();
		LocalDec local = ld.get(0);

		assertEquals(
				"Test renombrar método con contenidos: no se " + //$NON-NLS-1$
						"encuentra la variable local del método.", //$NON-NLS-1$
				factory.createName("paqueteA.ClaseA~nuevoMetodo%String@java.lang.String:a(10)"), //$NON-NLS-1$
			local.getUniqueName());
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 * 
	 * <p>
	 * Comprueba que se lanza una excepción cuando se intenta renombrar un
	 * método y su signatura vaya a coincidir con la de algún otro método de las
	 * clases de la jerarquía de herencia.
	 * </p>
	 * 
	 * <p>
	 * En un modelo con dos clases (la una hereda de la otra), toma un método de
	 * la subclase y lo renombra de manera que su signatura vaya a coincidir con
	 * la de un método de la superclase.
	 * </p>
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */
	@Test(expected=PreconditionException.class)	
	public void testCheckMethodIsNotInSubNorSuperclass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testCheckMethodIsNotInSubNorSuperclass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoB")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("metodoA"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName =factory.createName(classDef.getUniqueName().toString() + 
			"~" + "metodoA" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();	
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 * 
	 * <p>
	 * Comprueba que se lanza una excepción cuando se intenta renombrar un
	 * método constructor de su clase.
	 * </p>
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotConstructor() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testCheckMethodIsNotConstructor")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("ClaseA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("nuevoMetodo"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName = factory.createName(classDef.getUniqueName().toString() +
			"~" + "nuevoMetodo" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();
	}

	/**
	 * Comprueba que funciona correctamente la operación que deshace el
	 * renombrado de un método.
	 * 
	 * <p>
	 * En un modelo con una única clase, toma un método sin argumentos formales,
	 * valor de retorno, ni variables locales, y le asigna un nuevo nombre
	 * correcto, distinto al de los demás métodos de la clase. Después, deshace
	 * la operación de renombrado.
	 * </p>
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */    
	@Test
	public void testUndoSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testUndoSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();		

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("nuevoMetodo"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName =factory.createName(classDef.getUniqueName().toString() + 
			"~" + "nuevoMetodo" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();	
		renombrado.undoActions();

		// Comienzan las comprobaciones.
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertFalse("Test deshacer renombrado de método simple: " + //$NON-NLS-1$
				"no se ha recuperado el método antiguo", lMetodo2.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDef.getMethDecByName(factory.createName("nuevoMetodo")); //$NON-NLS-1$
		assertTrue("Test deshacer renombrado de método simple: t" + //$NON-NLS-1$
				"todavía existe el método con el nuevo nombre",
				lMetodo3.isEmpty()); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la refactorización se deshace correctamente al deshacer un
	 * renombrado de un método en una superclase.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la ejecución de la prueba.
	 */  
	@Test
	public void testUndoWithInheritance() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testUndoWithInheritance")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		MethDec metodo = classDef.getMethDecByName(factory.createName("metodo1")).get(0); //$NON-NLS-1$
		Name name = factory.createName("renombrado"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		Name uniqueName = factory.createName(classDef.getUniqueName().toString() 
			+ "~" + "renombrado" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);
		renombrado.run();
		renombrado.undoActions();

		// Comienzan las comprobaciones.
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertFalse("Deshacer renombrado de método con herencia: " + //$NON-NLS-1$
				"no existe el método con el nombre original.",
				lMetodo2.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDef.getMethDecByName(factory.createName("renombrado")); //$NON-NLS-1$
		assertTrue("Deshacer renombrado de método con herencia: " + //$NON-NLS-1$
				"sigue existiendo el método renombrado.", lMetodo3.isEmpty()); //$NON-NLS-1$

		ClassDef classDef2 = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo4 = classDef2.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertFalse("Deshacer renombrado de método con herencia: " + //$NON-NLS-1$
				"no existe el método con el nombre original.",
				lMetodo4.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo5 = classDef2.getMethDecByName(factory.createName("renombrado")); //$NON-NLS-1$
		assertTrue("Deshacer renombrado de método con herencia: " + //$NON-NLS-1$
				"sigue existiendo el método renombrado por herencia.",
				lMetodo5.isEmpty()); //$NON-NLS-1$
	}

	/**
	 * Comprueba el renombrado de un método.
	 * 
	 * @throws Exception
	 *             excepción.
	 */
	@Test
	public void testProbando() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameMethod/testPrueba")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();	

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.B")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("m1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		Name name = factory.createName("nuevoMetodo"); //$NON-NLS-1$

		int parameterTypesFirstIndex = metodo.getUniqueName().toString().indexOf('%');

		String parameterTypesPart=""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = metodo.getUniqueName().toString().substring(
				parameterTypesFirstIndex);
		
		String source = Regenerate.regenerate("B.java");
		String target = EclipsePrettyPrinter.formatCompilationUnit(source);

		Name uniqueName =factory.createName(classDef.getUniqueName().toString() + 
			"~" + "nuevoMetodo" + parameterTypesPart); //$NON-NLS-1$ //$NON-NLS-2$

		MOONRefactoring renombrado = new RenameMethod(metodo,classDef, name, uniqueName, jm);			
		renombrado.run();	
		
		source = Regenerate.regenerate("B.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
		System.out.println("\ntarget:\n" + target);
		
		source = Regenerate.regenerate("H.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
		System.out.println("\ntarget:\n" + target);
		
		source = Regenerate.regenerate("Nota.java");
		target = EclipsePrettyPrinter.formatCompilationUnit(source);
		
		System.out.println("\ntarget:\n" + target);

		
	}
}