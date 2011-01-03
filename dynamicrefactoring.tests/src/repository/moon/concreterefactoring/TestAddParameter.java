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
import javamoon.regenerate.Regenerate;
import javamoon.utils.EclipsePrettyPrinter;

import moon.core.MoonFactory;
import moon.core.classdef.*;
import moon.core.expression.CallExpr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import moon.core.Name;

import static org.junit.Assert.*;
import org.junit.Test;

import refactoring.engine.PreconditionException;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorizaci�n que a�ade un
 * argumento formal a la lista de par�metros de un m�todo.<p>
 * 
 * Indirectamente, se comprueba tambi�n la correcci�n de las funciones,
 * acciones y predicados utilizados por la refactorizaci�n.
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 * @see AddParameter
 */
public class TestAddParameter extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente en un caso
	 * sencillo y correcto con un argumento de tipo entero.<p>
	 * 
	 * En un modelo con una �nica clase, a�ade un argumento formal de tipo
	 * entero al m�todo �nico de la clase, que no tiene m�s argumentos.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List<MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("ParameterA"); //$NON-NLS-1$

		Type tipo = jm.getType(factory.createName("int"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$

		assertEquals("Test a�adir par�metro simple: no se encuentra el " + //$NON-NLS-1$
			"m�todo al que se ha a�adido el par�metro.", 1, lMetodo.size()); //$NON-NLS-1$

		MethDec metodo2 = lMetodo2.get(0);
		metodo.getFormalArgument();
		assertEquals("Test a�adir par�metro simple: " + //$NON-NLS-1$
			"el m�todo no tiene un �nico argumento.", 1,  //$NON-NLS-1$
			metodo2.getFormalArgument().size());
		assertEquals("Test a�adir par�emtro simple: " + //$NON-NLS-1$
			"no se encuentra el nuevo param�tro.", "ParameterA", //$NON-NLS-1$ //$NON-NLS-2$
			metodo.getFormalArgument().get(0).toString());
		assertEquals("Test a�adir par�metro simple: " + //$NON-NLS-1$
			"el tipo del par�metro no coincide con el nuevo tipo", //$NON-NLS-1$
			metodo.getFormalArgument().get(0).getType(), tipo);
	} 

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente en un caso
	 * sencillo y correcto con un argumento de tipo <code>String</code>.<p>
	 * 
	 * En un modelo con una �nica clase, a�ade un argumento formal de tipo
	 * cadena de caracteres al m�todo �nico de la clase, que no tiene m�s 
	 * argumentos.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testAddString() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testAddString")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("ParameterB"); //$NON-NLS-1$

		Type tipo = jm.getType(factory.createName("java.lang.String"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();			

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$

		assertEquals("Test a�adir argumento String: no se encuentra el " + //$NON-NLS-1$
			"m�todo al que se ha a�adido el par�metro.", 1, lMetodo.size()); //$NON-NLS-1$

		MethDec metodo2 = lMetodo2.get(0);

		assertEquals("Test a�adir argumento String: " + //$NON-NLS-1$
			"el m�todo no tiene un �nico par�metro.", //$NON-NLS-1$
			1, metodo2.getFormalArgument().size());
		assertEquals("Test a�adir argumento String: " + //$NON-NLS-1$
			"no se encuentra el nuevo param�tro.", //$NON-NLS-1$
			"ParameterB", metodo.getFormalArgument().get(0).toString()); //$NON-NLS-1$
		assertEquals("Test a�adir argumento String: " + //$NON-NLS-1$
			"el tipo del argumento no coincide con el nuevo tipo.", //$NON-NLS-1$
			tipo, metodo.getFormalArgument().get(0).getType());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente en un caso
	 * sencillo y correcto con un argumento de tipo booleano.<p>
	 * 
	 * En un modelo con una �nica clase, a�ade un argumento formal de tipo
	 * booleano al m�todo �nico de la clase, que no tiene m�s argumentos.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testAddBoolean() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testAddBoolean")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		
		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("ParameterC"); //$NON-NLS-1$

		Type tipo = jm.getType(factory.createName("boolean"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();			

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$

		assertEquals("Test a�adir argumento boolean: no se encuentra el " + //$NON-NLS-1$
			"m�todo al que se ha a�adido el par�metro.", 1, lMetodo.size()); //$NON-NLS-1$

		MethDec metodo2 = lMetodo2.get(0);

		assertEquals("Test a�adir argumento boolean: " + //$NON-NLS-1$
			"el m�todo no tiene un �nico par�metro.", 1,  //$NON-NLS-1$
			metodo2.getFormalArgument().size());
		assertEquals("Test a�adir argumento boolean: "+ //$NON-NLS-1$
			"no se encuentra el nuevo param�tro.", "ParameterC", //$NON-NLS-1$ //$NON-NLS-2$
			metodo.getFormalArgument().get(0).toString());
		assertEquals("Test a�adir argumento boolean: " + //$NON-NLS-1$
			"el tipo del par�metro no coincide con el nuevo tipo", //$NON-NLS-1$
			tipo, metodo.getFormalArgument().get(0).getType());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando el m�todo
	 * al que se le a�ade el argumento formal tiene previamente otros 
	 * argumentos (dos, en este caso).
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testMethodWithParameters() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testMethodWithParameters")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("ParameterD"); //$NON-NLS-1$

		Type tipo = jm.getType(factory.createName("int")); //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();		

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$

		assertEquals("Test a�adir argumento a m�todo con par�metro: " + //$NON-NLS-1$
			"no se encuentra el m�todo al que se ha a�adido el par�metro.", 1, //$NON-NLS-1$
			lMetodo.size());

		MethDec metodo2 = lMetodo2.get(0);

		assertEquals("Test a�adir argumento a m�todo con par�metro: " + //$NON-NLS-1$
			"el m�todo no tiene tres par�metros.", 3,  //$NON-NLS-1$
			metodo2.getFormalArgument().size());
		assertEquals("Test a�adir argumento a m�todo con par�metro: " + //$NON-NLS-1$
			"el nombre del primer par�metro se ha modificado.", "a", //$NON-NLS-1$ //$NON-NLS-2$
			metodo.getFormalArgument().get(0).toString());
		assertEquals("Test a�adir argumento a m�todo con par�metro: " + //$NON-NLS-1$
			"no se encuentra el par�metro a�adido.", "ParameterD", //$NON-NLS-1$ //$NON-NLS-2$
			metodo.getFormalArgument().get(2).toString());
		assertEquals("Test a�adir argumento a m�todo con par�metro: " + //$NON-NLS-1$
			"el tipo del nuevo par�metro no coincide.", tipo, //$NON-NLS-1$
			metodo.getFormalArgument().get(2).getType());
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando se a�ade
	 * un argumento formal a un m�todo al que se referencia en otras clases del
	 * modelo.<p>
	 *
	 * En un modelo con dos clases, se toma un m�todo de la primera clase y se 
	 * le a�ade un argumento de tipo <code>float</code>. En la segunda clase se 
	 * utiliza dicho m�todo en las sentencias del cuerpo de un m�todo. Se 
	 * comprueba que los cambios se extiendan a las instrucciones de este 
	 * segundo m�todo.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testMethodIsCalled() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testMethodIsCalled")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("parametro"); //$NON-NLS-1$

		Type tipo = jm.getType(factory.createName("float")); //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();	

		// Comienzan las comprobaciones
		ClassDef classDef2 = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		List <MethDec> lMetodo2 = classDef2.getMethDecByName(factory.createName("metodoB")); //$NON-NLS-1$
		MethDec metodo2 = lMetodo2.get(0);

		CompoundInstr instr1 = (CompoundInstr)metodo2.getInstructions().get(0);
		List <Instr> instrIt = instr1.getInstructions();
		
		//CallInstrLength1 instr2 = (CallInstrLength1)instrIt.get(3);
		AssignmentInstr instr2 = (AssignmentInstr) instrIt.get(3);
		

		assertEquals("Test a�adir argumento a m�todo usado: las llamadas al m�todo " + //$NON-NLS-1$
			"no contienen un valor real para el nuevo argumento.", "0", //$NON-NLS-1$ //$NON-NLS-2$
			((CallExpr)instr2.getRighSide()).getRealArgument(0).toString());
	}
	
	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando se a�ade
	 * un argumento formal a un m�todo del que existen todo tipo de llamadas
	 * con expresiones e instrucciones de tipos muy distintos.<p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */  
	@Test
	public void testMethodDifferentInstructions() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testMethodDifferentInstructions")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(
			factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("parametro"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("char")); //$NON-NLS-1$

		MOONRefactoring addition = new AddParameter(metodo, tipo, name, jm);			
		addition.run();	

		// Comienzan las comprobaciones
		MethDec metodoB = classDef.getMethDecByName(factory.createName("metodoB")).get(0); //$NON-NLS-1$
		MethDec metodoC = classDef.getMethDecByName(factory.createName("metodoC")).get(0); //$NON-NLS-1$
		
		List<Instr> instrB = metodoB.getInstructions();
		List<Instr> instrC = metodoC.getInstructions();

		for (Instr i : instrB)
			for (Instr subi : ((CompoundInstr)i).getInstructions())
				if (subi.toString().contains("metodoA")) //$NON-NLS-1$
					assertEquals("A�adir par�metro con llamadas complejas: " + //$NON-NLS-1$
						"no se ha a�adido el par�metro real para el nuevo argumento.", //$NON-NLS-1$
						"metodoA('0') * 2 / metodoA('0')", subi.toString()); //$NON-NLS-1$
		
		for (Instr i : instrC)
			for (Instr subi : ((CompoundInstr)i).getInstructions())
				if (subi.toString().contains("metodoA")) //$NON-NLS-1$
					assertEquals("A�adir par�metro con llamadas complejas: " + //$NON-NLS-1$
						"no se ha a�adido el par�metro real en el segundo m�todo.", //$NON-NLS-1$
						"a=metodoA('0').intValue() - metodoB()", subi.toString()); //$NON-NLS-1$
		
		addition.undoActions();
		
		instrB = metodoB.getInstructions();
		instrC = metodoC.getInstructions();
		
		for (Instr i : instrB)
			for (Instr subi : ((CompoundInstr)i).getInstructions())
				if (subi.toString().contains("metodoA")) //$NON-NLS-1$
					assertEquals("Deshacer a�adir par�metro con llamadas complejas: " + //$NON-NLS-1$
						"no se ha eliminado el par�metro real para el nuevo argumento.", //$NON-NLS-1$
						"metodoA() * 2 / metodoA()", subi.toString()); //$NON-NLS-1$
		
		for (Instr i : instrC)
			for (Instr subi : ((CompoundInstr)i).getInstructions())
				if (subi.toString().contains("metodoA")) //$NON-NLS-1$
					assertEquals("Deshacer a�adir par�metro con llamadas complejas: " + //$NON-NLS-1$
						"no se ha eliminado el par�metro real en el segundo m�todo.", //$NON-NLS-1$
						"a=metodoA().intValue() - metodoB()", subi.toString()); //$NON-NLS-1$
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente cuando se a�ade
	 * un argumento formal a la signatura de un m�todo cuando se encuentra en
	 * una clase intermedia de una jerarqu�a de herencia.
	 * 
	 * <p>En una jerarqu�a de tres niveles, con una clase en cada nivel, en la que
	 * las tres clases dan una definici�n para el mismo m�todo, se a�ade un 
	 * nuevo argumento formal desde la clase central. Se comprueba que los
	 * cambios se extiendan adecuadamente a la superclase y a la subclase de la
	 * clase afectada directamente.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testWithHierarchy() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testWithHierarchy")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.MediumClass")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("newParanuevoParametro"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("int")); //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();	
		
		// Comprobaciones en la clase afectada directamente
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo2 = lMetodo2.get(0);		

		assertFalse("Test a�adir argumento con herencia: sigue existiendo el " + //$NON-NLS-1$
			"m�todo con la antigua signatura en la clase.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA".equals(metodo2.getUniqueName().toString())); //$NON-NLS-1$

		assertEquals("Testa a�adir argumento con herencia: no se encuentra el " + //$NON-NLS-1$
			"m�todo al que se ha a�adido el nuevo argumento.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA%int", metodo2.getUniqueName().toString()); //$NON-NLS-1$

		List<FormalArgument> lArg = metodo2.getFormalArgument();

		assertEquals("Test a�adir argumento con herencia: no se encuentra el " + //$NON-NLS-1$
			"nuevo argumento.", 1, lArg.size()); //$NON-NLS-1$

		FormalArgument argumento = lArg.get(0);

		assertEquals("Test a�adir argumento con herencia: no se ha generado " + //$NON-NLS-1$
			"correctamente el par�metro.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA%int#newParanuevoParametro", //$NON-NLS-1$
			argumento.getUniqueName().toString());

		// Comprobaciones en la superclase
		ClassDef classDefS = jm.getClassDef(factory.createName("paqueteA.SuperType")); //$NON-NLS-1$
		List <MethDec> lMetodoS = classDefS.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodoS = lMetodoS.get(0);

		assertFalse("Test a�adir argumento con herencia: sigue existiendo el " + //$NON-NLS-1$
			"m�todo con la antigua signatura en la superclase.", //$NON-NLS-1$
			"paqueteA.SuperType~metodoA".equals(metodoS.getUniqueName().toString())); //$NON-NLS-1$

		assertEquals("Test a�adir argumento con herencia: no se encuentra en la " + //$NON-NLS-1$
			"superclase el m�todo al que se ha a�adido el nuevo argumento.", //$NON-NLS-1$
			"paqueteA.SuperType~metodoA%int", metodoS.getUniqueName().toString()); //$NON-NLS-1$

		List<FormalArgument> lArgS = metodoS.getFormalArgument();

		assertEquals("Test a�adir argumento con herencia: no se encuentra en el " + //$NON-NLS-1$
			"m�todo de la superclase el nuevo argumento.", 1, lArgS.size()); //$NON-NLS-1$

		FormalArgument argumentoS = lArgS.get(0);

		assertEquals("Test a�adir argumento con herencia: no se ha generado " + //$NON-NLS-1$
			"correctamente el par�metro en la superclase.", //$NON-NLS-1$
			"paqueteA.SuperType~metodoA%int#newParanuevoParametro", //$NON-NLS-1$
			argumentoS.getUniqueName().toString());
						
		// Comprobaciones en la subclase
		ClassDef classDefs = jm.getClassDef(factory.createName("paqueteA.SubType")); //$NON-NLS-1$
		List <MethDec> lMetodos = classDefs.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodos = lMetodos.get(0);

		assertFalse("Test a�adir argumento con herencia: sigue existiendo el " + //$NON-NLS-1$
			"m�todo con la antigua signatura en la subclase.", //$NON-NLS-1$
			"paqueteA.SubType~metodoA".equals(metodos.getUniqueName().toString())); //$NON-NLS-1$

		assertEquals("Test a�adir argumento con herencia: no se encuentra en la " + //$NON-NLS-1$
			"subclase el m�todo al que se ha a�adido el nuevo argumento.", //$NON-NLS-1$
			"paqueteA.SubType~metodoA%int", metodos.getUniqueName().toString()); //$NON-NLS-1$

		List<FormalArgument> lArgs = metodos.getFormalArgument();

		assertEquals("Test a�adir argumento con herencia: no se encuentra en el " + //$NON-NLS-1$
			"m�todo de la subclase el nuevo argumento.", 1, lArgs.size()); //$NON-NLS-1$

		FormalArgument argumentos = lArgs.get(0);

		assertEquals("Test a�adir argumento con herencia: no se ha generado " + //$NON-NLS-1$
			"correctamente el par�metro en la subclase.", //$NON-NLS-1$
			"paqueteA.SubType~metodoA%int#newParanuevoParametro", //$NON-NLS-1$
			argumentos.getUniqueName().toString());		
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta a�adir un 
	 * argumento formal con un nombre que ya est� asignado a otro par�metro 
	 * del m�todo.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class) 
	public void testCheckNotExistsParameterWithSameName() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testCheckNotExistsParameterWithSameName")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("a"); //$NON-NLS-1$

		Type tipo = jm.getType(factory.createName("int"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta a�adir un 
	 * argumento con un nombre que ya est� asignado a una variable local del 
	 * m�todo.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class) 
	public void testCheckNotExistsLocalDecWithSameName() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testCheckNotExistsLocalDecWithSameName")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("a"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("int"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta a�adir un 
	 * argumento que da lugar a un nombre �nico de m�todo que ya existe en una 
	 * superclase.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotInSuperclass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testCheckMethodIsNotInSuperclass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("a"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("int"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();
	}

	/**
     * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
     *
     * <p>Comprueba que se lanza una excepci�n cuando se intenta a�adir un 
     * argumento que da lugar a un nombre �nico de m�todo que ya existe en 
     * una subclase.</p>
     * 
     * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
     */
    @Test(expected=PreconditionException.class)
    public void testCheckMethodIsNotInSubclass() throws Exception{

    	SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testCheckMethodIsNotInSubclass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

        ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
        List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoB")); //$NON-NLS-1$
        MethDec metodo = lMetodo.get(0);

        Name name = factory.createName("b"); //$NON-NLS-1$
        Type tipo = jm.getType(factory.createName("int")); //$NON-NLS-1$
        
        MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);            
        adition.run();
    }

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta a�adir un 
	 * argumento que da lugar a un nombre �nico de m�todo que ya existe en la 
	 * propia clase.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckMethodIsNotAlreadyInClass() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testCheckMethodIsNotAlreadyInClass")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		
		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("a"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("int"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();
	}

	/** 
	 * Comprueba que funciona correctamente la operaci�n que deshace la
	 * adici�n de un argumento formal, en un caso sencillo y correcto.
	 * 
	 * <p>En un modelo con una �nica clase, a�ade un argumento formal al m�todo
	 * �nico de la clase, que no tiene m�s argumentos, y despu�s trata de 
	 * deshacer los cambios.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test 
	public void testUndoSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testUndoSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("ParameterA"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("int"));	 //$NON-NLS-1$

		MOONRefactoring adition = new AddParameter(metodo, tipo, name, jm);			
		adition.run();
		adition.undoActions();

		// Comienzan las comprobaciones
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$

		assertEquals("Test deshacer a�adir argumento simple: " + //$NON-NLS-1$
			"no se encuentra el m�todo.", 1, lMetodo.size()); //$NON-NLS-1$

		MethDec metodo2 = lMetodo2.get(0);
				
		assertTrue("Test deshacer a�adir argumento simple: " + //$NON-NLS-1$
			"se ha encontrado un argumento formal a�adido.", //$NON-NLS-1$
			metodo2.getFormalArgument().isEmpty());
	}

	/** 
	 * Comprueba que se deshace la refactorizaci�n correctamente cuando 
	 * se a�ade un argumento formal a la signatura de un m�todo que se 
	 * encuentra en una clase intermedia de una jerarqu�a de herencia.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndoWithHierarchy() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestAddParameter/testUndoWithHierarchy")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.MediumClass")); //$NON-NLS-1$
		List <MethDec> lMetodo = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo = lMetodo.get(0);

		Name name = factory.createName("newParanuevoParametro"); //$NON-NLS-1$
		Type tipo = jm.getType(factory.createName("int")); //$NON-NLS-1$

		MOONRefactoring addition = new AddParameter(metodo, tipo, name, jm);			
		addition.run();
		addition.undoActions();

		// Comprobaciones en la clase afectada directamente
		List <MethDec> lMetodo2 = classDef.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodo2 = lMetodo2.get(0);	

		assertEquals("Test deshacer a�adir argumento con herencia:" + //$NON-NLS-1$
			" no se ha encontrado el m�todo en la clase.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA", metodo2.getUniqueName().toString()); //$NON-NLS-1$

		assertFalse("Test deshacer a�adir argumento con herencia:" +  //$NON-NLS-1$
			" se ha encontrado el m�todo con la nueva signatura.", //$NON-NLS-1$
			"paqueteA.MediumClass~metodoA%int".equals(metodo2.getUniqueName().toString())); //$NON-NLS-1$

		List<FormalArgument> lArg = metodo2.getFormalArgument();
		assertEquals("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"el m�todo no tiene una lista vac�a de argumentos", 0, lArg.size()); //$NON-NLS-1$

		// Comprobaciones en la superclase
		ClassDef classDefS = jm.getClassDef(factory.createName("paqueteA.SuperType")); //$NON-NLS-1$
		List <MethDec> lMetodoS = classDefS.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodoS = lMetodoS.get(0);

		assertEquals("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"no se ha encontrado el m�todo en la superclase.", //$NON-NLS-1$
			"paqueteA.SuperType~metodoA", metodoS.getUniqueName().toString()); //$NON-NLS-1$
		assertTrue("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"se ha encontrado el m�todo con la nueva signatura.", //$NON-NLS-1$
			metodoS.getUniqueName().toString().compareTo(
				"paqueteA.SuperType~metodoA%int")!=0); //$NON-NLS-1$

		List<FormalArgument> lArgS = metodoS.getFormalArgument();
		assertEquals("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"el m�todo no tiene una lista vac�a de argumentos.", 0, lArgS.size()); //$NON-NLS-1$

		// Comprobaciones en la subclase
		ClassDef classDefs = jm.getClassDef(factory.createName("paqueteA.SubType")); //$NON-NLS-1$
		List <MethDec> lMetodos = classDefs.getMethDecByName(factory.createName("metodoA")); //$NON-NLS-1$
		MethDec metodos = lMetodos.get(0);

		assertEquals("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"no se ha encontrado el m�todo en la subclase.", //$NON-NLS-1$
			"paqueteA.SubType~metodoA", metodos.getUniqueName().toString()); //$NON-NLS-1$
		assertTrue("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"se ha encontrado el m�todo con la nueva signatura en la subclase.", //$NON-NLS-1$
			metodos.getUniqueName().toString().compareTo(
				"paqueteA.SubType~metodoA%int")!=0); //$NON-NLS-1$

		List<FormalArgument> lArgs = metodos.getFormalArgument();
		assertEquals("Test deshacer a�adir argumento con herencia: " + //$NON-NLS-1$
			"el m�todo no tiene una lista vac�a de argumentos.", 0, lArgs.size()); //$NON-NLS-1$
	}
}