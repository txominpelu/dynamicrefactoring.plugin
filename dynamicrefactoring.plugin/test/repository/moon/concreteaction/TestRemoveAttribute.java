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

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;

import org.junit.Test; 

import repository.RefactoringTemplateAbstractTest;

/** 
 * Comprueba que funciona correctamente la acción que eliminar un atributo 
 * de una clase. <p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestRemoveAttribute extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la acción funciona correctamente al eliminar un atributo de 
	 * una clase y deshacer después la operación.<p>
	 * 
	 * En una clase simple con un único atributo, elimina el atributo y después
	 * deshace la eliminación.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testUndo() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreteaction/TestRemoveAttribute/testUndo")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("<anonymous>.Source")); //$NON-NLS-1$
		AttDec att = classdef.getAttributes().get(0);
		
		RemoveAttribute action = new RemoveAttribute(att);			
		action.run();
		action.undo();

		// Comienzan las comprobaciones
		assertEquals("Deshacer eliminación de atributo: la clase no ha recuperado " + //$NON-NLS-1$
			"el atributo único al deshacer.", 1, classdef.getAttributes().size()); //$NON-NLS-1$
		
		AttDec recovered = classdef.getAttributes().get(0);
		assertNotNull("Deshacer eliminación de atributo: el atributo no se " + //$NON-NLS-1$
			"ha recuperado.", recovered); //$NON-NLS-1$
		assertEquals("Deshacer eliminación de atributo: el tipo del atributo " + //$NON-NLS-1$
			"recuperado no es el correcto.", classdef.getClassType().getUniqueName(),  //$NON-NLS-1$
			recovered.getType().getUniqueName());
		assertEquals("Deshacer eliminación de atributo: el nombre del atributo " + //$NON-NLS-1$
			"recuperado no es el original.", "attribute", recovered.getName().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}
}