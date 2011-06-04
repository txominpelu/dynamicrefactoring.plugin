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

package repository;

import static org.junit.Assert.assertTrue;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import repository.moon.MOONRefactoring;
import repository.moon.concreterefactoring.MoveField;
import repository.moon.concreterefactoring.TestAddParameter;

/** 
 * Comprueba que funciona correctamente el mecanismo de distribuci�n de
 * mensajes enviados por los elementos del repositorio durante la ejecución
 * de una refactorización.<p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TestMessageListener extends RefactoringTemplateAbstractTest 
	implements IRefactoringMessageListener {

	/**
	 * Indica que se ha recibido al menos un mensaje.
	 */
	private boolean received = false; 
	
	/**
	 * Este método se invoca después de cada test.
	 */
	@After @Override
	public void tearDown(){
		super.tearDown();
		RelayListenerRegistry.getInstance().remove(this);
	}
	
	/**
	 * Este método se invoca antes de cada test.
	 * 
	 * @throws Exception si se produce un error durante la inicialización.
	 */
	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		RelayListenerRegistry.getInstance().add(this);
	}
	
	/**
	 * @see IRefactoringMessageListener#messageSent(String)
	 */
	@Override
	public void messageSent(String message){
		if (message != null && message.length() > 0)
			received = true;
	}
	
	/** 
	 * Comprueba que se reciben mensajes durante la ejecución de una
	 * refactorización. <p>
	 * 
	 * Ejecuta una refactorización "Mover Atributo" y comprueba que se han
	 * recibido mensajes de refactorización en el proceso.
	 * 
	 * @throws Exception si se produjo algún error durante la ejecución de la prueba.
	 */
	@Test
	public void testMessageReceived() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/TestMessageListener")); //$NON-NLS-1$
		
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef source = jm.getClassDef(factory.createName(TestAddParameter.PAQUETE_A_CLASE_A));			 //$NON-NLS-1$
		ClassDef dest = jm.getClassDef(factory.createName("paqueteA.ClaseB")); //$NON-NLS-1$
		AttDec atribute = source.getAttributes().get(0);

		MOONRefactoring refactoring = new MoveField(atribute, dest, jm);			
		refactoring.run();
		
		assertTrue("Test distribuci�n de mensajes: " + //$NON-NLS-1$
			"no se ha recibido ningún mensaje durante la refactorización.", received); //$NON-NLS-1$
	}
}