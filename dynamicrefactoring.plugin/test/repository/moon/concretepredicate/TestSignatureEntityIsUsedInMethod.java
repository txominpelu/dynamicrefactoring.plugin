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

package repository.moon.concretepredicate;

import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente el predicado que verifica que una entidad
 * de signatura (un argumento formal o una variable local) no se utilice en un
 * método.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestSignatureEntityIsUsedInMethod extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que el predicado funciona correctamente cuando se utiliza un
	 * argumento formal en la sentencia de retorno del método, que devuelve el
	 * resultado de invocar un método sobre el argumento formal.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSignatureUsedA() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestSignatureEntityIsUsedInMethod/testSignatureUsedA")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoA")).get(0); //$NON-NLS-1$
		FormalArgument argument = method.getFormalArgument().get(0);
		
		SignatureEntityIsUsedInMethod predicate = new SignatureEntityIsUsedInMethod(
			argument, method);
				
		// Comienzan las comprobaciones
		assertTrue("Comprobar uso de entidad de signatura: no se han detectado " + //$NON-NLS-1$
			"el uso del argumento en la sentencia de retorno.", predicate.isValid()); //$NON-NLS-1$
	}
	
	/**
	 * Comprueba que el predicado funciona correctamente cuando se utiliza un
	 * argumento formal en el lado derecho de una sentencia de asignación.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSignatureUsedB() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestSignatureEntityIsUsedInMethod/testSignatureUsedB")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoB")).get(0); //$NON-NLS-1$
		FormalArgument argument = method.getFormalArgument().get(0);
				
		SignatureEntityIsUsedInMethod predicate = new SignatureEntityIsUsedInMethod(
			argument, method);
				
		// Comienzan las comprobaciones
		assertTrue("Comprobar uso de entidad de signatura: no se han detectado " + //$NON-NLS-1$
			"el uso del argumento en la sentencia de asignación.", predicate.isValid()); //$NON-NLS-1$
	}
	
	/**
	 * Comprueba que el predicado funciona correctamente cuando se utiliza un
	 * argumento formal como parte de una operación matemática en una sentencia
	 * de retorno.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSignatureUsedC() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestSignatureEntityIsUsedInMethod/testSignatureUsedC")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoC")).get(0); //$NON-NLS-1$
		FormalArgument argument = method.getFormalArgument().get(0);
		
		SignatureEntityIsUsedInMethod predicate = new SignatureEntityIsUsedInMethod(
			argument, method);
				
		// Comienzan las comprobaciones
		assertTrue("Comprobar uso de entidad de signatura: no se han detectado " + //$NON-NLS-1$
			"el uso del argumento en la operación de retorno.", predicate.isValid()); //$NON-NLS-1$
	}
	
	/**
	 * Comprueba que el predicado funciona correctamente cuando se utiliza un
	 * argumento formal como argumento de una instrucción de creación.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testSignatureUsedD() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestSignatureEntityIsUsedInMethod/testSignatureUsedD")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoD")).get(0); //$NON-NLS-1$
		FormalArgument argument = method.getFormalArgument().get(0);
		
		SignatureEntityIsUsedInMethod predicate = new SignatureEntityIsUsedInMethod(
			argument, method);
				
		// Comienzan las comprobaciones
		assertTrue("Comprobar uso de entidad de signatura: no se han detectado " + //$NON-NLS-1$
			"el uso del argumento en la operación de creación.", predicate.isValid()); //$NON-NLS-1$
	}
}