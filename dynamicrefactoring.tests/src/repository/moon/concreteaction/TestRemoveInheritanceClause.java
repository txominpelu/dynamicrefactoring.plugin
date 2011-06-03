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

import static org.junit.Assert.assertEquals;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.classdef.ClassDef;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;

/** 
 * Comprueba que funciona correctamente la acción que elimina una cl�usula de
 * herencia de una clase.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestRemoveInheritanceClause extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la acción funciona correctamente al eliminar una
	 * cl�usula de herencia de una clase que hereda de una superclase en su
	 * mismo paquete.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testRemove() throws Exception {

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreteaction/TestRemoveInheritanceClause/testRemove")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();

		ClassDef classdef = jm.getClassDef(jm.getMoonFactory().createName("<anonymous>.Clase")); //$NON-NLS-1$
		ClassDef superclass = jm.getClassDef(jm.getMoonFactory().createName("<anonymous>.Superclase")); //$NON-NLS-1$

		RemoveInheritanceClause action = 
			new RemoveInheritanceClause(classdef, superclass.getClassType());			
		action.run();
		
		// Comienzan las comprobaciones
		assertEquals(0, classdef.getInheritanceClause().size());
	}
	
	/** 
	 * Comprueba que la funciona correctamente la funci�n que deshace la 
	 * eliminación de una cl�usula de herencia en una clase que hereda de una 
	 * superclase en su mismo paquete.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndo() throws Exception {

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreteaction/TestRemoveInheritanceClause/testUndo")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();

		ClassDef classdef = jm.getClassDef(jm.getMoonFactory().createName("<anonymous>.Clase")); //$NON-NLS-1$
		ClassDef superclass = jm.getClassDef(jm.getMoonFactory().createName("<anonymous>.Superclase")); //$NON-NLS-1$

		RemoveInheritanceClause action = 
			new RemoveInheritanceClause(classdef, superclass.getClassType());			
		action.run();
		action.undo();
		
		// Comienzan las comprobaciones
		assertEquals(1, classdef.getInheritanceClause().size());
		assertEquals(superclass.getClassType().getUniqueName(),
			classdef.getInheritanceClause().get(0).getType().getUniqueName());
	}
}