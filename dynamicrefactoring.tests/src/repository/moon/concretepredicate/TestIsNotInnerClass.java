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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.classdef.ClassDef;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;

/** 
 * Comprueba que funciona correctamente el predicado que comprueba que una
 * clase no es clase interna de otra.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestIsNotInnerClass extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que el predicado funciona correctamente cuando la clase
	 * no es, efectivamente, clase interna.<p>
	 * 
	 * Ejecuta la comprobación sobre una clase que contiene una clase interna.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testNotInner() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestIsNotInnerClass/testNotInner")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		
		IsNotInnerClass predicate = new IsNotInnerClass(classdef);
				
		// Comienzan las comprobaciones
		assertTrue("Comprobar carácter de clase interna: se ha detectado " + //$NON-NLS-1$
			"como clase interna una que no lo es.", predicate.isValid());		 //$NON-NLS-1$
	}

	/**
	 * Comprueba que el predicado funciona correctamente cuando la clase
	 * s� es clase interna de otra.<p>
	 * 
	 * Ejecuta la comprobación sobre una clase interna contenida en otra.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testInner() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestIsNotInnerClass/testInner")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase$Interna")); //$NON-NLS-1$
		
		IsNotInnerClass predicate = new IsNotInnerClass(classdef);
				
		// Comienzan las comprobaciones
		assertFalse("Comprobar carácter de clase interna: no se ha detectado " + //$NON-NLS-1$
			"como clase interna una que s� lo es.", predicate.isValid());		 //$NON-NLS-1$
	}
}