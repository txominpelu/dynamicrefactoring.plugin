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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.Name;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.inheritance.InheritanceClause;

import org.junit.Test;

import refactoring.engine.PreconditionException;
import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorizaci�n que renombra una
 * clase.
 * 
 * <p>Indirectamente, se comprueba tambi�n la correcci�n de las funciones,
 * acciones y predicados utilizados por la refactorizaci�n.</p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 *
 * @see RenameClass
 */
public class TestRenameClass extends RefactoringTemplateAbstractTest { 

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al hacer un
	 * renombrado sencillo de una clase.
	 * 
	 * <p>Se renombra una clase en un modelo en que es la �nica clase contenida. Se
	 * le asigna un nombre correcto.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		Name name = factory.createName("nuevoNombre"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();
		
		// Comienzan las comprobaciones
		ClassDef classDef2 = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		assertNull("Test renombrar clase simple: todav�a existe la clase " + //$NON-NLS-1$
			"con el nombre anterior.", classDef2); //$NON-NLS-1$

		ClassDef classDef3 = jm.getClassDef(factory.createName("paqueteA.nuevoNombre")); //$NON-NLS-1$
		assertNotNull("Test renombrar clase simple: no existe la clase " + //$NON-NLS-1$
			"con el nuevo nombre.", classDef3); //$NON-NLS-1$
	}
	
	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al hacer un
	 * renombrado de una clase gen�rica.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test
	public void testRenameGeneric() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testRenameGeneric")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		Name name = factory.createName("Renombrada"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();

		// Comienzan las comprobaciones
		ClassDef oldclass = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		assertNull("Test renombrar clase gen�rica: todav�a existe la clase " + //$NON-NLS-1$
			"con el nombre anterior al renombrado.", oldclass); //$NON-NLS-1$

		ClassDef newclass = jm.getClassDef(factory.createName("<anonymous>.Renombrada")); //$NON-NLS-1$
		assertNotNull("Test renombrar clase gen�rica: no existe la clase " + //$NON-NLS-1$
			"con el nuevo nombre.", newclass); //$NON-NLS-1$
		
		ArrayList<String> types = new ArrayList<String>();
		for (Type type : jm.getTypes())
			types.add(type.getUniqueName().toString());
		
		assertTrue(types.contains("<anonymous>.Renombrada<java.lang.Integer>")); //$NON-NLS-1$
		assertTrue(types.contains("<anonymous>.Renombrada")); //$NON-NLS-1$
		assertTrue(types.contains("<anonymous>.Renombrada@T")); //$NON-NLS-1$
		assertFalse(types.contains("<anonymous>.Clase<java.lang.Integer>")); //$NON-NLS-1$
		assertFalse(types.contains("<anonymous>.Clase")); //$NON-NLS-1$
		assertFalse(types.contains("<anonymous>.Clase@T")); //$NON-NLS-1$
		
		renombrado.undoActions();
		
		// Comienzan las comprobaciones
		oldclass = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		assertNotNull("Test deshacer renombrar clase gen�rica: " + //$NON-NLS-1$
			"no se encuentra la clase con el nombre original.", oldclass); //$NON-NLS-1$

		newclass = jm.getClassDef(factory.createName("<anonymous>.Renombrada")); //$NON-NLS-1$
		assertNull("Test deshacer renombrar clase gen�rica: " + //$NON-NLS-1$
			"sigue existiendo la clase renombrada.", newclass); //$NON-NLS-1$
		
		types = new ArrayList<String>();
		for (Type type : jm.getTypes())
			types.add(type.getUniqueName().toString());
		
		assertFalse(types.contains("<anonymous>.Renombrada<java.lang.Integer>")); //$NON-NLS-1$
		assertFalse(types.contains("<anonymous>.Renombrada")); //$NON-NLS-1$
		assertFalse(types.contains("<anonymous>.Renombrada@T")); //$NON-NLS-1$
		assertTrue(types.contains("<anonymous>.Clase<java.lang.Integer>")); //$NON-NLS-1$
		assertTrue(types.contains("<anonymous>.Clase")); //$NON-NLS-1$
		assertTrue(types.contains("<anonymous>.Clase@T")); //$NON-NLS-1$
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al renombrar una
	 * clase que forma parte de una jerarqu�a de herencia.
	 * 
	 * <p>En un modelo con dos clases, en el que una hereda de la otra, se renombra
	 * la superclase y se verifica que la relaci�n de herencia se mantenga 
	 * correctamente.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testWithInheritance() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testWithInheritance")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		Name name = factory.createName("nuevoNombre"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();

		// Comienzan las comprobaciones sobre la superClase
		ClassDef classDef2 = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		assertNull("Test renombrar clase con herencia: todav�a existe la clase " + //$NON-NLS-1$
			"con el nombre anterior.", classDef2); //$NON-NLS-1$

		ClassDef classDef3 = jm.getClassDef(factory.createName("paqueteA.nuevoNombre")); //$NON-NLS-1$
		assertNotNull("Test renombrar clase con herencia: no existe la clase " + //$NON-NLS-1$
			"con el nuevo nombre", classDef3); //$NON-NLS-1$

		// Comprobaciones en la Clase heredera
		ClassDef classDef4 = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		List<InheritanceClause> antecesors = classDef4.getInheritanceClause();

		for(int i=0; i<antecesors.size(); i++){
			InheritanceClause ic = antecesors.get(i);			
			assertEquals("Test renombrar clase con herencia: no se ha " + //$NON-NLS-1$
				"renombrado en la clase que hereda", "nuevoNombre",  //$NON-NLS-1$ //$NON-NLS-2$
				ic.getType().toString());
		}
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al renombrar una
	 * clase de la que se usan instancias en otras clases.
	 * 
	 * <p>En un modelo con dos clases, renombra una clase de cuyo tipo existe un
	 * atributo en la otra clase.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */   
	@Test
	public void testUsedAsField() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testUsedAsField")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		Name name = factory.createName("nuevoNombre"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();

		// Comprobaciones sobre la clase renombrada.
		ClassDef classDef2 = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		assertNull("Test renombrar clase en atributo: todav�a existe la clase " + //$NON-NLS-1$
			"con el nombre anterior", classDef2); //$NON-NLS-1$

		ClassDef classDef3 = jm.getClassDef(factory.createName("paqueteA.nuevoNombre")); //$NON-NLS-1$
		assertNotNull("Test renombrar clase en atributo: no existe la clase " + //$NON-NLS-1$
			"con el nuevo nombre", classDef3); //$NON-NLS-1$

		// Comprobaciones sobre la Clase que usaba la primera.
		ClassDef classDef4 = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		AttDec atributo = classDef4.getAttributes().get(0);

		assertEquals("Test renombrar clase en atributo: no se ha " + //$NON-NLS-1$
			"modificado el tipo del atributo en la segunda clase", "nuevoNombre",  //$NON-NLS-1$ //$NON-NLS-2$
			atributo.getType().toString());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al hacer un
	 * renombrado de una clase con dos constructores.
	 * 
	 * <p>Se renombra una clase con dos constructores, y despu�s se deshace la
	 * operaci�n.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test
	public void testConstructors() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testConstructors")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		Name name = factory.createName("Renombrada"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();

		// Comienzan las comprobaciones
		List<MethDec> old = classDef.getMethDecByName(factory.createName("Clase")); //$NON-NLS-1$
		List<MethDec> now = classDef.getMethDecByName(factory.createName("Renombrada")); //$NON-NLS-1$

		assertEquals("Test renombrar clase con constructores: " + //$NON-NLS-1$
			"se mantienen algunos de los constructores con el nombre original.", //$NON-NLS-1$
			0, old.size());
		assertEquals("Test renombrar clase con constructores: " + //$NON-NLS-1$
			"no han sido renombrados los dos constructores .", 2, now.size()); //$NON-NLS-1$
		
		renombrado.undoActions();
		
		old = classDef.getMethDecByName(factory.createName("Clase")); //$NON-NLS-1$
		now = classDef.getMethDecByName(factory.createName("Renombrada")); //$NON-NLS-1$

		assertEquals("Test deshacer renombrar clase con constructores: " + //$NON-NLS-1$
			"no se ha deshecho el renombrado de constructores.", 2, old.size()); //$NON-NLS-1$
		assertEquals("Test deshacer renombrar clase con constructores: " + //$NON-NLS-1$
			"no se ha deshecho el renombrado de constructores.", 0, now.size()); //$NON-NLS-1$
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta renombrar una 
	 * clase para darle un nombre que ya se corresponda con otra clase del 
	 * modelo.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckNotExistsClassWithSameName() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testCheckNotExistsClassWithSameName")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		Name name = factory.createName("ClaseB"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();
	}
	
	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al deshacer un
	 * renombrado sencillo de una clase.
	 * 
	 * <p>Se renombra una clase en un modelo en que es la �nica clase contenida. Se
	 * le asigna un nombre correcto.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test
	public void testUndoSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRenameClass/testUndoSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		Name name = factory.createName("nuevoNombre"); //$NON-NLS-1$

		MOONRefactoring renombrado = new RenameClassTestVersion(name, classDef, jm);			
		renombrado.run();
		renombrado.undoActions();

		// Comienzan las comprobaciones
		ClassDef classDef2 = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		assertNotNull("Test deshacer renombrado de clase simple: " + //$NON-NLS-1$
			"no se ha restaurado el nombre anterior.", classDef2); //$NON-NLS-1$

		ClassDef classDef3 = jm.getClassDef(factory.createName("paqueteA.nuevoNombre")); //$NON-NLS-1$
		assertNull("Test deshacer renombrado de clase simple: " + //$NON-NLS-1$
			"todav�a existe la clase con el nuevo nobmre.", classDef3); //$NON-NLS-1$
	}
}