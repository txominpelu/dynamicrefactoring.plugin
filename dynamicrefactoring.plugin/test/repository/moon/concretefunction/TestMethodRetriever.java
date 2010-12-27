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

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;

import moon.core.MoonFactory;
import moon.core.classdef.*;

import static org.junit.Assert.*;

import org.junit.Test; 

import repository.RefactoringTemplateAbstractTest;

/** 
 * Comprueba que funciona correctamente la función que permite recuperar
 * un método de una clase en base a su nombre único.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestMethodRetriever extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que la función obtiene correctamente un método sin argumentos.<p>
	 * 
	 * En una clase con dos métodos, intenta obtener el método sin argumentos ni
	 * tipo de retorno.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testGetSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretefunction/TestMethodRetriever/testGetSimple")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoA")).get(0);  //$NON-NLS-1$
		String name = method.getUniqueName().toString(); 		
		
		MethodRetriever function = new MethodRetriever(classdef, name);
		MethDec recovered = (MethDec)function.getValue();
		
		// Comienzan las comprobaciones
		assertNotNull("Recuperar método por nombre único: no se ha obtenido el " + //$NON-NLS-1$
			"método simple sin argumentos.", recovered); //$NON-NLS-1$
		assertEquals("Recuperar método por nombre único: el método obtenido no " + //$NON-NLS-1$
			"es el método esperado.", method, recovered); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la función obtiene correctamente un método con argumentos
	 * y tipo de retorno.<p>
	 * 
	 * En una clase con dos métodos, intenta obtener el método con argumentos y
	 * tipo de retorno.
	 * 
	 * @throws Exception si se produce un error durante la ejecución de la prueba.
	 */
	@Test
	public void testGetComplex() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concretefunction/TestMethodRetriever/testGetComplex")); //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classdef = jm.getClassDef(factory.createName("paquete.Clase")); //$NON-NLS-1$
		MethDec method = classdef.getMethDecByName(factory.createName("metodoB")).get(0);  //$NON-NLS-1$
		String name = method.getUniqueName().toString(); 		
		
		MethodRetriever function = new MethodRetriever(classdef, name);
		MethDec recovered = (MethDec)function.getValue();
		
		// Comienzan las comprobaciones
		assertNotNull("Recuperar método por nombre único: no se ha obtenido el " + //$NON-NLS-1$
			"método con argumentos.", recovered); //$NON-NLS-1$
		assertEquals("Recuperar método por nombre único: el método obtenido no " + //$NON-NLS-1$
			"es el método esperado.", method, recovered); //$NON-NLS-1$
	}
}