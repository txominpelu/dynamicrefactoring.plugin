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
import static org.junit.Assert.assertTrue;

import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;

import org.junit.Test;

import refactoring.engine.PreconditionException;
import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorizaci�n que mueve un m�todo
 * de una clase del modelo a otra.
 * 
 * <p>Indirectamente, se comprueba tambi�n la correcci�n de las funciones,
 * acciones y predicados utilizados por la refactorizaci�n.</p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 *
 * @see MoveMethod
 */
public class TestMoveMethod extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que la refactorizaci�n funciona correctamente al mover un
	 * m�todo sencillo de una clase a otra.
	 * 
	 * <p>En un modelo con dos clases, se mueve un m�todo que cumple todas las
	 * precondiciones de una clase a la otra.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A));			 //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);			
		movido.run();

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classSource.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$

		assertTrue("Test mover m�todo simple: no se ha eliminado el " + //$NON-NLS-1$
			"m�todo en la clase origen.", lMetodo2.isEmpty()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDest.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$

		assertEquals("Test mover m�todo simple: no se ha a�adido el " + //$NON-NLS-1$
			"m�todo en la clase destino.", 1, lMetodo3.size()); //$NON-NLS-1$
	}
	
	/**
	 * Comprueba que la refactorizaci�n funciona correctamente al mover un
	 * m�todo con argumentos formales de una clase a otra.
	 * 
	 * <p>En un modelo con dos clases, se mueve un m�todo que cumple todas las
	 * precondiciones y tiene un argumento formal, de una clase a la otra.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testMoveWithArguments() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testMoveWithArguments")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef source = jm.getClassDef(factory.createName("paquete.Clase"));			 //$NON-NLS-1$
		ClassDef destination = jm.getClassDef(factory.createName("paquete.Destino")); //$NON-NLS-1$

		MethDec method = source.getMethDecByName(factory.createName(TestRemoveParameter.METODO_A)).get(0); //$NON-NLS-1$
		
		MOONRefactoring moving = new MoveMethod(destination, method, jm);			
		moving.run();

		// Comienzan las comprobaciones
		List<MethDec> old = source.getMethDecByName(factory.createName(TestRemoveParameter.METODO_A)); //$NON-NLS-1$
		assertTrue("Test mover m�todo con argumentos: no se ha eliminado el " + //$NON-NLS-1$
			"m�todo en la clase origen.", old.isEmpty()); //$NON-NLS-1$

		List <MethDec> now = destination.getMethDecByName(factory.createName(TestRemoveParameter.METODO_A)); //$NON-NLS-1$
		assertEquals("Test mover m�todo con argumentos: no se ha a�adido el " + //$NON-NLS-1$
			"m�todo en la clase destino.", 1, now.size()); //$NON-NLS-1$
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta mover un m�todo
	 * a la misma clase en la que originalmente se encuentra.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class) 
	public void testCheckNotEqualClasses() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testCheckNotEqualClasses")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$

		List <MethDec> lMetodo = classDef .getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDef , metodo, jm);			
		movido.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta mover un m�todo a
	 * una clase destino, cuya superclase contiene un m�todo con la misma 
	 * signatura que el que se desea mover.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test(expected=PreconditionException.class) 
	public void testCheckMethodIsNotAlreadyInSuperclasses() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testCheckMethodIsNotAlreadyInSuperclasses")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A));			 //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseC")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);			
		movido.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta mover un m�todo
	 * constructor.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test(expected=PreconditionException.class) 
	public void testCheckMethodIsNotConstructor() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testCheckMethodIsNotConstructor")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		
		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("ClaseA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);			
		movido.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta mover un m�todo
	 * al que existen llamadas en alguna clase del modelo.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class) 
	public void testCheckNotExistsCallToThisMethod() throws Exception{
			try{
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testCheckNotExistsCallToThisMethod")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName(TestRemoveParameter.METODO_A)); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);			
		movido.run();
			}catch(Exception e){
			throw e;}
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta mover un m�todo 
	 * cuyo cuerpo utiliza un atributo de la clase que lo contiene.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test(expected=PreconditionException.class) 
	public void testCheckMethodNotUsesClassAttribute() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testCheckMethodNotUsesClassAttribute")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName(TestRemoveParameter.METODO_A)); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);
		movido.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta mover un m�todo 
	 * abstracto.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */    
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotDeferred() throws Exception{ 

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testCheckMethodIsNotDeferred")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName(TestRemoveParameter.METODO_A)); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);			
		movido.run();
	}

	/** 
	 * Comprueba que funciona correctamente la operaci�n que deshace el 
	 * movimiento de un m�todo de una clase a otra.
	 * 
	 * <p>En un modelo con dos clases, se mueve un m�todo que cumple todas las
	 * precondiciones de una clase a la otra y despu�s se deshace el movimiento.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testUndoSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveMethod/testUndoSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A)); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		MOONRefactoring movido = new MoveMethod(classDest, metodo, jm);			
		movido.run();
		movido.undoActions();

		// Comienzan las comprobaciones

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classSource.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertEquals("Test undo mover m�todo simple: no se ha restaurado el " + //$NON-NLS-1$
			"m�todo a la clase de origen", 1, lMetodo2.size()); //$NON-NLS-1$

		List <MethDec> lMetodo3 = classDest.getMethDecByName(factory.createName("metodo1")); //$NON-NLS-1$
		assertTrue("Test undo mover m�todo simple: no se ha eliminado el " + //$NON-NLS-1$
			"m�todo de la clase destino", lMetodo3.isEmpty()); //$NON-NLS-1$
	}
}