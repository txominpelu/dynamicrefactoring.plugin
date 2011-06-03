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

package repository.java.concreteaction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.core.classdef.JavaClassDef;
import javamoon.core.classdef.JavaImport;
import moon.core.MoonFactory;
import moon.core.classdef.ClassDef;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la acción que elimina de una clase las
 * importaciones que corresponden a JUnit3.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestRemoveJUnit3Imports extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la acción elimina correctamente las importaciones 
	 * correspondientes a JUnit3.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testRemove() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestRemoveJUnit3Imports")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$
		
		RemoveJUnit3Imports action = new RemoveJUnit3Imports(classdef);	
		action.run();
		
		List<JavaImport> imports = ((JavaClassDef)classdef).getImport();
		assertEquals("Test eliminar importaciones JUnit3: " + //$NON-NLS-1$
			"no se eliminaron las importaciones", 0, imports.size()); //$NON-NLS-1$
	}
	
	/** 
	 * Comprueba que la acción deshace correctamente la eliminación de las 
	 * importaciones correspondientes a JUnit3.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testUndo() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestRemoveJUnit3Imports")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$
		
		RemoveJUnit3Imports action = new RemoveJUnit3Imports(classdef);	
		action.run();
		action.undo();
		
		List<JavaImport> imports = ((JavaClassDef)classdef).getImport();
		assertEquals("Test deshacer eliminar importaciones JUnit3: " + //$NON-NLS-1$
			"no se deshizo la eliminación las importaciones", 1, imports.size()); //$NON-NLS-1$
		assertEquals("Test deshacer eliminar importaciones JUnit3: " + //$NON-NLS-1$
			"no se mantuvo el tipo esperado.",  //$NON-NLS-1$
			"junit.framework.TestCase", imports.get(0).getUniqueName().toString()); //$NON-NLS-1$
	}
}