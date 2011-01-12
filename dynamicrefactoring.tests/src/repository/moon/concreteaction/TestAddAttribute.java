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

package repository.moon.concreteaction;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.classdef.*;

import static org.junit.Assert.*;

import org.junit.Test; 

import repository.RefactoringTemplateAbstractTest;

/** 
 * Comprueba que funciona correctamente la acción que añade un atributo a una clase. <p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestAddAttribute extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la acción funciona correctamente al añadir un atributo a una 
	 * clase y deshacer después la operación. <p>
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testUndo() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreteaction/TestAddAttribute/testUndo")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();

		ClassDef classdef = jm.getClassDef(jm.getMoonFactory().createName("<anonymous>.Clase")); //$NON-NLS-1$
		ClassDef source = jm.getClassDef(jm.getMoonFactory().createName("<anonymous>.Source")); //$NON-NLS-1$

		AttDec att = source.getAttributes().get(0);
		
		AddAttribute action = new AddAttribute(att, classdef);			
		action.run();
		action.undo();

		// Comienzan las comprobaciones
		assertEquals(0, classdef.getAttributes().size());
		assertEquals(1, source.getAttributes().size());
	}
}