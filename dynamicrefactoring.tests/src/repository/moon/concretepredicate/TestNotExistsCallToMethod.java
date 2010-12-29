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

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;

import org.junit.Test; 

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente el predicado que comprueba que no existan
 * llamadas a un determinado método.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestNotExistsCallToMethod extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que el predicado funciona correctamente cuando existen
	 * llamadas al método dentro de distintos tipos de instrucciones y 
	 * expresiones.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testCallExistToMethodA() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestNotExistsCallToThisMethod/testCallExistToMethodA")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoA")).get(0); //$NON-NLS-1$
		
		NotExistsCallToThisMethod predicate = new NotExistsCallToThisMethod(method);
				
		// Comienzan las comprobaciones
		assertFalse("Comprobar llamadas a un método: no se han detectado " + //$NON-NLS-1$
			"las llamadas existentes a un método.", predicate.isValid()); //$NON-NLS-1$
	}

	/**
	 * Comprueba que el predicado funciona correctamente cuando existen
	 * llamadas al método dentro de distintos tipos de instrucciones y 
	 * expresiones.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testCallExistToMethodC() throws Exception {
		
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestNotExistsCallToThisMethod/testCallExistToMethodC")); //$NON-NLS-1$
		
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$
		
		ClassDef classdef = jm.getClassDef(factory.createName("<anonymous>.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoA")).get(0); //$NON-NLS-1$
		
		NotExistsCallToThisMethod predicate = new NotExistsCallToThisMethod(method);
				
		// Comienzan las comprobaciones
		assertFalse("Comprobar llamadas a un método: no se han detectado " + //$NON-NLS-1$
			"las llamadas existentes a un método.", predicate.isValid()); //$NON-NLS-1$
	}

	/**
	 * Comprueba que el predicado funciona correctamente cuando existen
	 * llamadas al método dentro de distintos tipos de instrucciones y 
	 * expresiones.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testCallExistToMethodB() throws Exception {
		
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestNotExistsCallToThisMethod/testCallExistToMethodB")); //$NON-NLS-1$
		
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoA")).get(0); //$NON-NLS-1$

		NotExistsCallToThisMethod predicate = new NotExistsCallToThisMethod(method);

		// Comienzan las comprobaciones
		assertFalse("Comprobar llamadas a un método: no se han detectado " + //$NON-NLS-1$
				"las llamadas existentes a un método.", predicate.isValid()); //$NON-NLS-1$
	}
}