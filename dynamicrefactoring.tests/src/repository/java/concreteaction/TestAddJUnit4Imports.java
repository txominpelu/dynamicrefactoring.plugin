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

import java.util.ArrayList;
import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import javamoon.core.classdef.JavaClassDef;
import javamoon.core.classdef.JavaImport;

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;
import org.junit.Test; 

import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la acción que añade las importaciones
 * correspondientes a JUnit4 a una clase.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestAddJUnit4Imports extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la acción añade correctamente todas las cláusulas de 
	 * importación esperadas.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testAddImports() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestAddJUnit4Imports")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$

		AddJUnit4Imports action = new AddJUnit4Imports(classdef);	
		action.run();

		List<JavaImport> imports = ((JavaClassDef)classdef).getImport();
		List<String> types = new ArrayList<String>();
		types.add("org.junit.Test"); //$NON-NLS-1$
		types.add("org.junit.Before"); //$NON-NLS-1$
		types.add("org.junit.After"); //$NON-NLS-1$
		types.add("org.junit.Assert.assertEquals"); //$NON-NLS-1$
		types.add("org.junit.Assert.assertFalse"); //$NON-NLS-1$
		types.add("org.junit.Assert.assertTrue"); //$NON-NLS-1$
		types.add("org.junit.Assert.fail"); //$NON-NLS-1$
		types.add("junit.framework.TestCase"); //$NON-NLS-1$
		
		assertEquals("Test añadir importaciones JUnit4: " + //$NON-NLS-1$
			"no se añadió el número de importaciones esperado", 8, imports.size()); //$NON-NLS-1$
		for (JavaImport i : imports){
			if (i.getType() != null)
				if (i.hasPropertyName()){
					assertTrue("Test añadir importaciones JUnit4: " + //$NON-NLS-1$
							"se añadió un tipo no esperado.",  //$NON-NLS-1$
							types.contains(i.getType().getUniqueName().toString() + "." + i.getPropertyName().toString()));
				}else{
					assertTrue("Test añadir importaciones JUnit4: " + //$NON-NLS-1$
							"se añadió un tipo no esperado.",  //$NON-NLS-1$
							types.contains(i.getType().getUniqueName().toString()));

				}
			else
				assertTrue("Test añadir importacines JUnit4: " + //$NON-NLS-1$
					"se añadió un tipo no esperado.", //$NON-NLS-1$
					types.contains(i.getUniqueName().toString()));
		}		
	}
	
	/** 
	 * Comprueba que la acción deshace correctamente la adición de todas las 
	 * cláusulas de importación esperadas.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testUndo() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/java/concreteaction/TestAddJUnit4Imports")); //$NON-NLS-1$
		JavaModel model = JavaModel.getInstance();
		MoonFactory factory = model.getMoonFactory();
		
		new MOONRefactoring("Test", model); //$NON-NLS-1$

		ClassDef classdef = model.getClassDef(factory.createName("paquete.JUnit3")); //$NON-NLS-1$

		AddJUnit4Imports action = new AddJUnit4Imports(classdef);	
		action.run();
		action.undo();

		List<JavaImport> imports = ((JavaClassDef)classdef).getImport();
		List<String> types = new ArrayList<String>();
		types.add("junit.framework.TestCase"); //$NON-NLS-1$
		
		assertEquals("Test deshacer añadir importaciones JUnit4: " + //$NON-NLS-1$
			"no se eliminaron las importaciones.", 1, imports.size()); //$NON-NLS-1$
		for (JavaImport i : imports){
			if (i.getType() != null)
				assertTrue("Test deshacer añadir importaciones JUnit4: " + //$NON-NLS-1$
					"se mantuvo un tipo no esperado.",  //$NON-NLS-1$
					types.contains(i.getType().getUniqueName().toString()));
			else
				assertTrue("Test deshacer añadir importacines JUnit4: " + //$NON-NLS-1$
					"se mantuvo un tipo no esperado.", //$NON-NLS-1$
					types.contains(i.getUniqueName().toString()));
		}		
	}
}