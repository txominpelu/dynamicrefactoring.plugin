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

package repository.moon;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;

import org.junit.Test; 
import static org.junit.Assert.*;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la clase que representa una 
 * refactorización sobre MOON.<p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestMOONRefactoring extends RefactoringTemplateAbstractTest {
	
	/**
	 * Comprueba que se lanza una excepción <code>RuntimeException</code>
	 * cuando se intentan instanciar dos refactorizaciones sucesivas sobre MOON
	 * con diferentes metamodelos.
	 * 
	 * @throws Exception si se produce un fallo al cargar el modelo MOON.
	 */
	@Test (expected=RuntimeException.class)
	public void testInconsistency() throws Exception {
		
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/TestMOONRefactoring/modelA"));		 //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$
		
		model.reset();
		sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/TestMOONRefactoring/modelB")); //$NON-NLS-1$
		model = JavaModel.getInstance();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$
	}
	
	/**
	 * Comprueba que no se lanza una excepción <code>RuntimeException</code>
	 * cuando se intentan instanciar dos refactorizaciones sucesivas sobre MOON
	 * con diferentes metamodelos si entre ambas se resetea el modelo.
	 * 
	 * <p>Comprueba también que en cada momento se obtiene el modelo esperado
	 * al consultar a <code>MOONRefactoring</code>.
	 * 
	 * @throws Exception si se produce un fallo al cargar el modelo MOON.
	 */
	@Test
	public void testConsistency() throws Exception {
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/TestMOONRefactoring/modelA"));		 //$NON-NLS-1$
		JavaModel modelA = JavaModel.getInstance();
		
		new MOONRefactoring("Test", modelA); //$NON-NLS-1$
		assertEquals(modelA, MOONRefactoring.getModel());
		
		MOONRefactoring.resetModel();
		
		JavaModel.getInstance().reset();
		sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/TestMOONRefactoring/modelA"));		 //$NON-NLS-1$
		JavaModel modelB = JavaModel.getInstance();
		
		new MOONRefactoring("Test", modelB); //$NON-NLS-1$
		
		assertEquals(modelB, MOONRefactoring.getModel());
	}
}