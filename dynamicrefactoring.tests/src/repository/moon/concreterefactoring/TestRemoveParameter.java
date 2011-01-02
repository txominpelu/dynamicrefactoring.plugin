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
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;

import static org.junit.Assert.*;
import org.junit.Test; 

import refactoring.engine.PreconditionException;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorizaci�n que elimina un
 * argumento formal de la lista de par�metros de un m�todo.
 * 
 * <p>Indirectamente, se comprueba tambi�n la correcci�n de las funciones,
 * acciones y predicados utilizados por la refactorizaci�n.</p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 *
 * @see RemoveParameter
 */
public class TestRemoveParameter extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al hacer una
	 * eliminaci�n de un argumento formal en un m�todo con m�s de un par�metro.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testRemoveWithManyParameters() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testRemoveWithManyParameters")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();					

		//comienzan las comprobaciones
		List <MethDec> lMetodo2 = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		assertTrue("Test eliminar argumento en m�todo con varios par�metros: " + //$NON-NLS-1$
			"no se encuentra el m�todo del que se ha eliminado el par�metro.", //$NON-NLS-1$
			!lMetodo2.isEmpty());

		MethDec metodo2 = lMetodo2.get(0);
		List <FormalArgument> lArgument2 = metodo2.getFormalArgument();
		assertEquals("Test eliminar argumento en m�todo con varios par�metros: " + //$NON-NLS-1$
			"hay m�s par�metros de los que debiera", 1, lArgument2.size()); //$NON-NLS-1$

		FormalArgument argumento2 = lArgument2.get(0);		
		assertEquals("Test eliminar argumento en m�todo con varios par�metros: " + //$NON-NLS-1$
			"no se ha eliminado correctamente el par�metro.", "b",  //$NON-NLS-1$ //$NON-NLS-2$
			argumento2.getName().toString());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al eliminar
	 * el segundo argumento formal de un m�todo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testRemoveSecondArgument() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testRemoveSecondArgument")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(1);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();					

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec refactorizado = lMetodo2.get(0);

		List <FormalArgument> argumentos = refactorizado.getFormalArgument();
		assertEquals("Test eliminar segundo argumento formal: " + //$NON-NLS-1$
			"no se ha eliminado un argumento formal.", 1, argumentos.size()); //$NON-NLS-1$

		FormalArgument otro = argumentos.get(0);
		assertEquals("Test eliminar segundo argumento formal: " + //$NON-NLS-1$
			"no se ha conservado el primer argumento formal.",  //$NON-NLS-1$
			"a", otro.getName().toString()); //$NON-NLS-1$
	}
	
	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al hacer una
	 * eliminaci�n de un argumento formal en un m�todo con un solo par�metro.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testRemoveWithOneParameter() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testRemoveWithOneParameter")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();

		//comienzan las comprobaciones
		List <MethDec> lMetodo2 = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		assertTrue("Test eliminar argumento en m�todo con un par�metro: " + //$NON-NLS-1$
			"No se encuentra el m�todo del que se ha eliminado el par�metro.", //$NON-NLS-1$
			!lMetodo2.isEmpty());

		MethDec metodo2 = lMetodo2.get(0);
		List <FormalArgument> lArgument2 = metodo2.getFormalArgument();
		assertTrue("Test eliminar argumento en m�todo con un par�metro: " + //$NON-NLS-1$
			"no se ha eliminado correctamente el par�metro.",  //$NON-NLS-1$
			lArgument2.isEmpty());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando se elimina
	 * un argumento formal en un m�todo de una clase intermedia de una jerarqu�a
	 * de herencia.
	 *
	 * <p>En un modelo con 3 clases repartidas en 3 niveles de la misma jerarqu�a
	 * de herencia, en la que las 3 clases dan su propia definici�n para un 
	 * m�todo, se elimina el argumento �nico del m�todo en la clase del nivel
	 * intermedio. Se comprueba que los cambios se extiendan a las dem�s clases
	 * de la jerarqu�a.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testRemoveWithHierarchy() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testRemoveWithHierarchy")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.MediumClass")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();	

		// Comprobaciones sobre la clase afectada directamente
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo2 = lMetodo2.get(0);		

		assertFalse("Test eliminar argumento con herencia: sigue existiendo el " + //$NON-NLS-1$
			"m�todo con la antigua signatura en la clase.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA%int".equals(metodo2.getUniqueName().toString())); //$NON-NLS-1$
		assertEquals("Test eliminar argumento con herencia: no se encuentra el " + //$NON-NLS-1$
			"m�todo del que se ha eliminado el argumento.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA", metodo2.getUniqueName().toString()); //$NON-NLS-1$

		List<FormalArgument> lArg = metodo2.getFormalArgument();
		assertEquals("Test eliminar argumento con herencia: " + //$NON-NLS-1$
			"no se ha eliminado correctamente el argumento en la clase.", //$NON-NLS-1$
			0, lArg.size());

		// Comprobaciones sobre la superclase
		ClassDef classDefS = jm.getClassDef(factory.createName("paqueteA.SuperType")); //$NON-NLS-1$
		List <MethDec> lMetodoS = classDefS.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodoS = lMetodoS.get(0);		

		assertFalse("Test eliminar argumento con herencia: sigue existiendo el " + //$NON-NLS-1$
			"m�todo con la antigua signatura en la superclase.", //$NON-NLS-1$
			"paqueteA.SuperType~metodoA%int".equals(metodoS.getUniqueName().toString())); //$NON-NLS-1$
		assertEquals("Test eliminar argumento con herencia: no se encuentra el " + //$NON-NLS-1$
			"m�todo del que se ha eliminado el argumento en la superclase.", //$NON-NLS-1$
			"paqueteA.SuperType~metodoA", metodoS.getUniqueName().toString()); //$NON-NLS-1$

		List<FormalArgument> lArgS = metodoS.getFormalArgument();
		assertEquals("Test eliminar argumento con herencia: " + //$NON-NLS-1$
			"no se ha eliminado correctamente el argumento en la superclase.", //$NON-NLS-1$
			0, lArgS.size());

		// Comprobaciones sobre la subclase
		ClassDef classDefs = jm.getClassDef(factory.createName("paqueteA.SubType")); //$NON-NLS-1$
		List <MethDec> lMetodos = classDefs.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodos = lMetodos.get(0);		

		assertFalse("Test eliminar argumento con herencia: sigue existiendo el " + //$NON-NLS-1$
			"m�todo con la antigua signatura en la subclase.", //$NON-NLS-1$
			"paqueteA.SubType~metodoA%int".equals(metodos.getUniqueName().toString())); //$NON-NLS-1$
		assertEquals("Test eliminar argumento con herencia: no se encuentra el " + //$NON-NLS-1$
			"m�todo del que se ha eliminado el argumento en la subclase.", //$NON-NLS-1$
			"paqueteA.SubType~metodoA", metodos.getUniqueName().toString()); //$NON-NLS-1$

		List<FormalArgument> lArgs = metodos.getFormalArgument();
		assertEquals("Test eliminar argumento con herencia: " + //$NON-NLS-1$
			"no se ha eliminado correctamente el argumento en la subclase.", //$NON-NLS-1$
			0, lArgs.size());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando se elimina
	 * un argumento formal de un m�todo al que se referencia en otras clases del
	 * modelo.
	 *
	 * <p>En un modelo con dos clases, se toma un m�todo de la primera clase y se 
	 * elimina el primero de sus dos argumentos, de tipo <code>String</code>.
	 * En la segunda clase se utiliza dicho m�todo en las sentencias del cuerpo 
	 * de un m�todo. Se comprueba que los cambios se extiendan a las instrucciones 
	 * de este segundo m�todo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testMethodIsCalled() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testMethodIsCalled")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();	

		// Comienzan las comprobaciones
		ClassDef classDef2 = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		List <MethDec> lMetodo2 = classDef2.getMethDecByName(factory.createName("metodoB")); //$NON-NLS-1$
		MethDec metodo2 = lMetodo2.get(0);

		CompoundInstr instr1 = (CompoundInstr)metodo2.getInstructions().get(0);

		List <Instr> instrIt = instr1.getInstructions();
		//CallInstrLength1 instr2 = (CallInstrLength1)instrIt.get(3);
		
		AssignmentInstr instr2 = (AssignmentInstr) instrIt.get(3);
		
		List <Expr> list = ((CallExpr)instr2.getRighSide()).getRealArguments();
		//List <ExprAtom> list =instr2.getRealArguments();

		assertTrue("Test eliminar argumento m�todo usado: las llamadas al m�todo " + //$NON-NLS-1$
			"no han eliminado el argumento.", list.isEmpty()); //$NON-NLS-1$
	}
	
	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando se elimina
	 * un argumento formal de un m�todo del que existen llamadas que representan
	 * diferentes tipos de instrucciones y expresiones del modelo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testMethodDiffInstructions() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testMethodDiffInstructions")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$

		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);
		List <MethDec> lMetodoB = classDef.getMethDecByName(factory.createName("metodoB")); //$NON-NLS-1$
		MethDec metodoB = lMetodoB.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();	

		// Comienzan las comprobaciones
		for (Instr i : metodoB.getInstructions())
			for (Instr subi : ((CompoundInstr)i).getInstructions())
				if (subi instanceof AssignmentInstr)
					assertEquals("Test eliminar argumento m�todo con muchas llamadas.", //$NON-NLS-1$
						0, ((CallExpr)((AssignmentInstr)subi).getRighSide()).getRealArguments().size());
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n <code>PreconditionException</code>
	 * cuando se intenta eliminar un argumento formal de forma que se genera un 
	 * nombre �nico para el m�todo que ya existe en un m�todo de la misma clase.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotAlreadyInClass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testCheckMethodIsNotAlreadyInClass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();	
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n <code>PreconditionException</code>
	 * cuando se intenta eliminar un argumento formal de manera que se genera un 
	 * nombre �nico para el m�todo que ya existe en otro m�todo de una 
	 * superclase.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotInSuperclass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testCheckMethodIsNotInSuperclass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n <code>PreconditionException</code>
	 * cuando se intenta eliminar un argumento formal de manera que se genera un 
	 * nombre �nico para el m�todo que ya existe en un m�todo de una subclase.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotInSubclass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testCheckMethodIsNotInSubclass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		
		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n <code>PreconditionException</code>
	 * cuando se intenta eliminar un argumento formal que est� siendo utilizado
	 * en el cuerpo del m�todo.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckSignatureEntityIsNotUsedInMethod() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testCheckSignatureEntityIsNotUsedInMethod")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente al deshacer una
	 * eliminaci�n de un argumento formal en un m�todo con un solo par�metro.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testUndo() throws Exception{
		
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestRemoveParameter/testUndo")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classSource = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		List <FormalArgument> lArgument = metodo.getFormalArgument();
		FormalArgument argumento = lArgument.get(0);

		MOONRefactoring eliminacion = new RemoveParameter(argumento, jm);			
		eliminacion.run();
		eliminacion.undoActions();

		//comienzan las comprobaciones
		List <MethDec> lMetodo2 = classSource.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		assertTrue("Test deshacer eliminar par�metro en m�todo con argumento: " + //$NON-NLS-1$
			"no se encuentra el m�todo.", !lMetodo2.isEmpty()); //$NON-NLS-1$

		MethDec metodo2 = lMetodo2.get(0);
		List <FormalArgument> lArgument2 = metodo2.getFormalArgument();
		assertEquals("Test deshacer eliminar par�metro en m�todo con un argumento: " + //$NON-NLS-1$
			"no se ha restaurado el argumento.", 1, lArgument2.size()); //$NON-NLS-1$
		assertEquals("Test deshacer eliminar par�metro en m�todo con un argumento: " + //$NON-NLS-1$
			"no se ha restaurado correctamente el argumento.", //$NON-NLS-1$
			"paqueteA.ClaseA~metodoA%int#a", lArgument2.get(0).getUniqueName().toString()); //$NON-NLS-1$
	}
}