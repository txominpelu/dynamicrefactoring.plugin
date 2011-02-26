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
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;

import org.junit.Test;

import repository.RefactoringTemplateAbstractTest;

/** 
 * Comprueba que funciona correctamente la funci�n que permite recuperar
 * un m�todo de una clase en base a su nombre �nico.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestMethodRetriever extends RefactoringTemplateAbstractTest {

	/**
	 * Comprueba que la funci�n obtiene correctamente un m�todo sin argumentos.<p>
	 * 
	 * En una clase con dos m�todos, intenta obtener el m�todo sin argumentos ni
	 * tipo de retorno.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
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
		assertNotNull("Recuperar m�todo por nombre �nico: no se ha obtenido el " + //$NON-NLS-1$
			"m�todo simple sin argumentos.", recovered); //$NON-NLS-1$
		assertEquals("Recuperar m�todo por nombre �nico: el m�todo obtenido no " + //$NON-NLS-1$
			"es el m�todo esperado.", method, recovered); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la funci�n obtiene correctamente un m�todo con argumentos
	 * y tipo de retorno.<p>
	 * 
	 * En una clase con dos m�todos, intenta obtener el m�todo con argumentos y
	 * tipo de retorno.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
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
		assertNotNull("Recuperar m�todo por nombre �nico: no se ha obtenido el " + //$NON-NLS-1$
			"m�todo con argumentos.", recovered); //$NON-NLS-1$
		assertEquals("Recuperar m�todo por nombre �nico: el m�todo obtenido no " + //$NON-NLS-1$
			"es el m�todo esperado.", method, recovered); //$NON-NLS-1$
	}
}