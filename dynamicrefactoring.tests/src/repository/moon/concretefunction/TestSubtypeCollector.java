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

package repository.moon.concretefunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.classdef.ClassType;
import moon.core.classdef.Type;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la función que permite obtener
 * el conjunto de subtipos de un tipo determinado.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestSubtypeCollector extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que la función obtiene correctamente los subtipos de un tipo
	 * con varios subtipos.<p>
	 * 
	 * En un modelo con las bibliotecas b�sicas de Java cargadas, intenta obtener
	 * los subtipos de <code>java.lang.Number</code>.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testGetSubtypes() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretefunction/TestSubtypeCollector/testGetSubtypes")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();
		
		new MOONRefactoring("Test", jm); //$NON-NLS-1$

		Type type = jm.getType(factory.createName("java.lang.Number")); //$NON-NLS-1$
		
		SubtypeCollector function = new SubtypeCollector((ClassType)type);
		Collection<ClassType> types= function.getCollection();
		
		Collection<Type> expected = new ArrayList<Type>();
		expected.add(jm.getType(factory.createName("java.lang.Integer"))); //$NON-NLS-1$
		expected.add(jm.getType(factory.createName("java.lang.Double"))); //$NON-NLS-1$
		expected.add(jm.getType(factory.createName("java.lang.Float"))); //$NON-NLS-1$
		expected.add(jm.getType(factory.createName("java.lang.Number"))); //$NON-NLS-1$
		expected.add(jm.getType(factory.createName("java.lang.Short"))); //$NON-NLS-1$
		expected.add(jm.getType(factory.createName("java.lang.Byte"))); //$NON-NLS-1$
		expected.add(jm.getType(factory.createName("java.lang.Long"))); //$NON-NLS-1$
		
		// Comienzan las comprobaciones
		assertNotNull("Obtener subtipos de un tipo: no se ha obtenido la " + //$NON-NLS-1$
			"lista de subtipos.", types); //$NON-NLS-1$
		assertTrue("Obtener subtipos de un tipo: no se ha obtenido la " + //$NON-NLS-1$
			"lista de tipos esperada.", types.containsAll(expected)); //$NON-NLS-1$
		assertEquals("Obtener subtipos de un tipo: no se ha obtenido la " + //$NON-NLS-1$
			"lista de tipos esperada.", expected.size(), types.size()); //$NON-NLS-1$
	}
}