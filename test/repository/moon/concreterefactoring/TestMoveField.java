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

import java.util.*;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;
import org.junit.Test; 

import refactoring.engine.PreconditionException;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorización que mueve un atributo
 * de una clase del modelo a otra.
 * 
 * <p>Indirectamente, se comprueba también la corrección de las funciones,
 * acciones y predicados utilizados por la refactorización.</p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 * @see MoveField
 */
public class TestMoveField extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la refactorización funciona correctamente al mover un
	 * atributo de una clase a otra en un caso correcto.
	 * 
	 * <p>En un modelo con dos clases, se mueve un atributo que cumple todas las
	 * precondiciones de una clase a la otra.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveField/testSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <AttDec> lAtribute = classSource.getAttributes();
		AttDec atribute = lAtribute.get(0);

		MOONRefactoring movido = new MoveField(atribute, classDest, jm);			
		movido.run();

		// Comienzan las comprobaciones
		List <AttDec> lAtribute2 = classSource.getAttributes();
		assertTrue("Test mover atributo simple: no se ha eliminado el " + //$NON-NLS-1$
			"atributo de la clase origen.", lAtribute2.isEmpty()); //$NON-NLS-1$

		List <AttDec> lAtribute3= classDest.getAttributes();
		assertEquals("Test mover atributo simple: no se ha añadido el " + //$NON-NLS-1$
			"atributo a la clase destino.", 1, lAtribute3.size()); //$NON-NLS-1$
		
		AttDec atribute3 = lAtribute3.get(0);
		assertEquals("Test mover atributo simple: se ha cambiado el nombre del " + //$NON-NLS-1$
			"atributo en clase destino.", "atributo",  //$NON-NLS-1$ //$NON-NLS-2$
			atribute3.getName().toString());
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una excepción cuando se intenta mover un atributo 
	 * a la misma clase que originalmente lo contiene.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */    
	@Test(expected=PreconditionException.class) 
	public void testCheckNotEqualClasses() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveField/testCheckNotEqualClasses")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$

		List <AttDec> lAtribute = classSource.getAttributes();
		AttDec atribute = lAtribute.get(0);

		MOONRefactoring movido = new MoveField(atribute, classSource, jm);			
		movido.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una excepción cuando se intenta mover un atributo 
	 * que está siendo utilizado en su clase de origen.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */  
	@Test(expected=PreconditionException.class) 
	public void testCheckAttributeIsNotUsedInClass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveField/testCheckAttributeIsNotUsedInClass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.Class_Source"));			 //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.Class_Dest")); //$NON-NLS-1$

		List <AttDec> lAtribute = classSource.getAttributes();
		AttDec atribute = lAtribute.get(0);	

		MOONRefactoring movido = new MoveField(atribute, classDest, jm);			
		movido.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una excepción cuando se intenta mover un atributo
	 * que está siendo utilizado en algún punto del modelo.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testAttributeIsNotUsedInModel() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveField/testAttributeIsNotUsedInModel")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <AttDec> lAtribute = classSource.getAttributes();
		AttDec atribute = lAtribute.get(0);	

		MOONRefactoring movido = new MoveField(atribute, classDest, jm);			
		movido.run();
	}

	/** 
	 * Comprueba que funciona correctamente la operación que deshace el
	 * movimiento de un atributo de una clase a otra en un caso correcto
	 * simple.
	 * 
	 * <p>En un modelo con dos clases, se mueve un atributo que cumple todas las
	 * precondiciones de una clase a la otra y después se deshace el movimiento.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */      
	@Test
	public void testUndoSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestMoveField/testUndoSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		ClassDef classDest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <AttDec> lAtribute = classSource.getAttributes();
		AttDec atribute = lAtribute.get(0);

		MOONRefactoring movido = new MoveField(atribute, classDest, jm);			
		movido.run();
		movido.undoActions();

		// Comienzan las comprobaciones
		List <AttDec> lAtribute2 = classSource.getAttributes();
		assertEquals("Test deshacer mover atributo simple: no se ha restaurado el " + //$NON-NLS-1$
			"atributo a la clase origen.", 1, lAtribute2.size()); //$NON-NLS-1$

		AttDec atribute2 = lAtribute2.get(0);
		assertEquals("Test deshacer mover atributo simple: se ha cambiado el " + //$NON-NLS-1$
			"nombre del atributo en clase origen ", "atributo", //$NON-NLS-1$ //$NON-NLS-2$
			atribute2.getName().toString());

		List <AttDec> lAtribute3= classDest.getAttributes();
		assertTrue("Test deshacer mover atributo simple: no se ha eliminado el " + //$NON-NLS-1$
			"atributo de la clase destino.", lAtribute3.isEmpty()); //$NON-NLS-1$
	}
}