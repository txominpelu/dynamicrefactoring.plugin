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

package repository.java.concreteaction;

import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.core.entity.JavaAnnotationReference;
import javamoon.core.entity.JavaRoutineDec;

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la acción que añade las anotaciones
 * de JUnit4 a una clase basándose en la convención de nombres utilizada desde
 * JUnit3.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestAddJUnit4Annotation extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la acción añade correctamente la anotación <code>@Before
	 * </code> al método <code>setUp()</code>.
	 * 
	 * @throws Exception si se produjo algún error durante la ejecución de la prueba.
	 */
	@Test
	public void testSetUp() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestAddJUnit4Annotation")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$
		
		AddJUnit4Annotation action = new AddJUnit4Annotation(classdef);	
		action.run();

		// Comienzan las comprobaciones
		MethDec setup = classdef.getMethDecByName(factory.createName("setUp")).get(0); //$NON-NLS-1$
		List<JavaAnnotationReference> annotations = ((JavaRoutineDec)setup).getAnnotations();
		
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación al método setUp()", 1, annotations.size()); //$NON-NLS-1$
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación @Before al método setUp()", "@Before", annotations.get(0).toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/** 
	 * Comprueba que la acción añade correctamente la anotación <code>@After
	 * </code> al método <code>tearDown()</code>.
	 * 
	 * @throws Exception si se produjo algún error durante la ejecución de la prueba.
	 */
	@Test
	public void testTearDown() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestAddJUnit4Annotation")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$
		
		AddJUnit4Annotation action = new AddJUnit4Annotation(classdef);	
		action.run();

		// Comienzan las comprobaciones
		MethDec tearDown = classdef.getMethDecByName(factory.createName("tearDown")).get(0); //$NON-NLS-1$
		List<JavaAnnotationReference> annotations = ((JavaRoutineDec)tearDown).getAnnotations();
		
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación al método tearDown()", 1, annotations.size()); //$NON-NLS-1$
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación @After al método tearDown()", "@After", annotations.get(0).toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/** 
	 * Comprueba que la acción añade correctamente las anotaciones <code>@Test
	 * </code> a los métodos de test.
	 * 
	 * @throws Exception si se produjo algún error durante la ejecución de la prueba.
	 */
	@Test
	public void testTestMethods() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestAddJUnit4Annotation")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$
		
		AddJUnit4Annotation action = new AddJUnit4Annotation(classdef);	
		action.run();

		// Comienzan las comprobaciones
		MethDec test = classdef.getMethDecByName(factory.createName("testMetodo")).get(0); //$NON-NLS-1$
		List<JavaAnnotationReference> annotations = ((JavaRoutineDec)test).getAnnotations();
				
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación al método testMetodo()", 1, annotations.size()); //$NON-NLS-1$
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación @Test al método testMetodo()", "@Test", annotations.get(0).toString()); //$NON-NLS-1$ //$NON-NLS-2$
	
		test = classdef.getMethDecByName(factory.createName("testMetodo2")).get(0); //$NON-NLS-1$
		annotations = ((JavaRoutineDec)test).getAnnotations();
				
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación al método testMetodo2()", 1, annotations.size()); //$NON-NLS-1$
		assertEquals("Test añadir anotaciones JUnit4: no se ha añadido" + //$NON-NLS-1$
			" la anotación @Test al método testMetodo2()", "@Test", annotations.get(0).toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/** 
	 * Comprueba que la acción deshace correctamente las anotaciones <code>@Test
	 * </code>, <code>@Before</code> y <code>@After</code>.
	 * 
	 * @throws Exception si se produjo algún error durante la ejecución de la prueba.
	 */
	@Test
	public void testUndo() throws Exception {

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestAddJUnit4Annotation")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$
		
		AddJUnit4Annotation action = new AddJUnit4Annotation(classdef);	 
		action.run();
		action.undo();

		// Comienzan las comprobaciones
		MethDec test = classdef.getMethDecByName(factory.createName("testMetodo")).get(0); //$NON-NLS-1$
		MethDec setUp = classdef.getMethDecByName(factory.createName("setUp")).get(0); //$NON-NLS-1$
		MethDec tearDown = classdef.getMethDecByName(factory.createName("tearDown")).get(0); //$NON-NLS-1$
		List<JavaAnnotationReference> annTest = ((JavaRoutineDec)test).getAnnotations();
		List<JavaAnnotationReference> annSetup = ((JavaRoutineDec)setUp).getAnnotations();
		List<JavaAnnotationReference> annTeardown = ((JavaRoutineDec)tearDown).getAnnotations();
				
		assertEquals("Test deshacer anotaciones JUnit4: no se ha eliminado" + //$NON-NLS-1$
			" la anotación al método testMetodo()", 0, annTest.size()); //$NON-NLS-1$
		assertEquals("Test deshacer anotaciones JUnit4: no se ha eliminado" + //$NON-NLS-1$
			" la anotación al método setUp()", 0, annSetup.size()); //$NON-NLS-1$
		assertEquals("Test deshacer anotaciones JUnit4: no se ha eliminado" + //$NON-NLS-1$
			" la anotación al método tearDown()", 0, annTeardown.size()); //$NON-NLS-1$
	}
}