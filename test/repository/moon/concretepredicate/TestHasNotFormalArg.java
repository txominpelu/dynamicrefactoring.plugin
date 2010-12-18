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

/** 
 * Comprueba que funciona correctamente el predicado que comprueba que no 
 * exista en la signatura de un método ningún argumento formal con el mismo 
 * nombre que el argumento indicado.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestHasNotFormalArg extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que el predicado funciona correctamente cuando sí existe ya
	 * un argumento formal del método con el mismo nombre que el argumento
	 * indicado.<p>
	 * 
	 * En un método con dos argumentos, ejecuta la comprobación sobre uno de los
	 * propios argumentos del método.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testArgumentExists() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestHasNotFormalArg/testArgumentExists")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoB")).get(0);  //$NON-NLS-1$
		FormalArgument existing = method.getFormalArgument().get(0);
		
		HasNotFormalArg predicate = new HasNotFormalArg(existing, method);
				
		// Comienzan las comprobaciones
		assertFalse("Comprobar existencia de argumento formal: no se ha detectado " + //$NON-NLS-1$
			"que existe un argumento con el mismo nombre.", predicate.isValid());		 //$NON-NLS-1$
	}

	/**
	 * Comprueba que el predicado funciona correctamente cuando no existe aún
	 * un argumento formal del método con el mismo nombre que el argumento
	 * indicado.<p>
	 * 
	 * En un método con dos argumentos, ejecuta la comprobación sobre un argumento
	 * formal externo con distinto nombre a ambos.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testArgumentNotExists() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretepredicate/TestHasNotFormalArg/testArgumentNotExists")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoB")).get(0);  //$NON-NLS-1$
		
		FormalArgument different = factory.createFormalArgument(
			factory.createName("diferente"), classdef.getClassType()); //$NON-NLS-1$
		
		HasNotFormalArg predicate = new HasNotFormalArg(different, method);
				
		// Comienzan las comprobaciones
		assertTrue("Comprobar existencia de argumento formal: se ha detectado " + //$NON-NLS-1$
			"que existe un argumento con el mismo nombre y no es cierto.", //$NON-NLS-1$
			predicate.isValid());		
	}	
}