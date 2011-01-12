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
import moon.core.entity.FunctionDec;
import moon.core.genericity.FormalPar;

import static org.junit.Assert.*;
import org.junit.Test;

import refactoring.engine.PreconditionException;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorización que reemplaza un 
 * parámetro formal por un tipo.
 * 
 * <p>Indirectamente, se comprueba también la corrección de las funciones,
 * acciones y predicados utilizados por la refactorización.</p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 * @see ReplaceFormalParameterWithType
 */
public class TestReplaceFormalParameterWithType 
	extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la refactorización funciona correctamente en un caso simple.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */  
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestReplaceFormalParameterWithType/testSimple"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("java.io.Bits")); //$NON-NLS-1$

		MOONRefactoring ref = new ReplaceFormalParameterWithType(formalPar, newType, jm);			
		ref.run();	

		// Comienzan las comprobaciones.
		List<FormalPar> lFormalPar2 = classDef.getFormalPars();
		assertTrue("Test reemplazar parámetro formal por tipo simple: " + //$NON-NLS-1$
			"no se ha remplazado correctamente el parámetro formal.",  //$NON-NLS-1$
			lFormalPar2.isEmpty());
	}
	
	/** 
	 * Comprueba que la refactorización funciona correctamente en un caso 
	 * en que el tipo paramétrico se utiliza en la declaración de atributos
	 * y argumentos formales de métodos y como tipo de retorno de métodos.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */  
	@Test
	public void testReplaceContents() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestReplaceFormalParameterWithType/testReplaceContents"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		
		ClassDef classDef = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("java.lang.Integer")); //$NON-NLS-1$

		MOONRefactoring ref = new ReplaceFormalParameterWithType(formalPar, newType, jm);			
		ref.run();	

		// Comienzan las comprobaciones.
		List<FormalPar> pars = classDef.getFormalPars();
		assertTrue("Test sustituir parámetro formal por tipo: " + //$NON-NLS-1$
			"no se ha eliminado el parámetro formal de la clase.", pars.isEmpty()); //$NON-NLS-1$
		
		assertEquals("Test sustituir parámetro formal por tipo: " + //$NON-NLS-1$
			"no se ha sustituido el tipo del atributo.", //$NON-NLS-1$
			jm.getType(factory.createName("java.lang.Integer")),  //$NON-NLS-1$
			classDef.getAttributes().get(0).getType());
		
		MethDec method = classDef.getMethDecByName(factory.createName("metodo")).get(0); //$NON-NLS-1$
		assertEquals(jm.getType(factory.createName("java.lang.Integer")),  //$NON-NLS-1$
			((FunctionDec)method).getReturnType());
		assertEquals(jm.getType(factory.createName("java.lang.Integer")),  //$NON-NLS-1$
			method.getFormalArgument().get(0).getType());
	}

	/** 
	 * Comprueba que la refactorización funciona correctamente en un caso con
	 * más de un parámetro formal.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */  
	@Test
	public void testWithMorePar() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestReplaceFormalParameterWithType/testWithMorePar"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("java.io.Bits")); //$NON-NLS-1$

		MOONRefactoring ref = new ReplaceFormalParameterWithType(formalPar, newType, jm);			
		ref.run();	

		// Comienzan las comprobaciones.
		List<FormalPar> lFormalPar2 = classDef.getFormalPars(); 
		assertEquals("Test reemplazar parámetro formal por tipo: " + //$NON-NLS-1$
			"no se ha remplazado correctamente el parámetro formal.", 1,  //$NON-NLS-1$
			lFormalPar2.size());

		FormalPar fp =lFormalPar2.get(0);
		assertEquals("Test reemplazar parámetro formal por tipo: " + //$NON-NLS-1$
			"se ha modificado el segundo parámetro formal.", //$NON-NLS-1$
			"paqueteA.ClaseA@PB", fp.getUniqueName().toString()); //$NON-NLS-1$
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorización.
	 *
	 * <p>Comprueba que se lanza una excepción cuando se intenta  reemplazar 
	 * un parámetro formal y existe una sustitución a este diferente al tipo 
	 * propuesto.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test(expected=PreconditionException.class)
	public void testCheckIsSingleGenericInstance() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestReplaceFormalParameterWithType/testCheckIsSingleGenericInstance"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.ClaseA")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$

		MOONRefactoring ref = new ReplaceFormalParameterWithType(formalPar, newType, jm);			
		ref.run();	
	}
}