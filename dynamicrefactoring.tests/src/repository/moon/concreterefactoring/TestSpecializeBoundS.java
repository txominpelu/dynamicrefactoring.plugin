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

package repository.moon.concreterefactoring;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;
import moon.core.MoonFactory;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.Type;
import moon.core.genericity.BoundS;
import moon.core.genericity.FormalPar;

import org.junit.Test;

import refactoring.engine.PreconditionException;
import repository.RefactoringTemplateAbstractTest;
import repository.moon.MOONRefactoring;

/** 
 * Comprueba que funciona correctamente la refactorizaci�n que especializa la 
 * acotaci�n de un par�metro formal.
 * 
 * <p>Indirectamente, se comprueba tambi�n la correcci�n de las funciones,
 * acciones y predicados utilizados por la refactorizaci�n.</p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 * @see SpecializeBoundS
 */
public class TestSpecializeBoundS extends RefactoringTemplateAbstractTest {

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente en un caso simple.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test
	public void testSimple() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestSpecializeBoundS/testSimple"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.B")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("paqueteA.Entero")); //$NON-NLS-1$

		ClassType oldType = null;
		if (formalPar instanceof BoundS){
			List<Type> bounds = ((BoundS)formalPar).getBounds();
			for(int i = 0; i < bounds.size(); i++){
				if (bounds.get(i).getUniqueName().equals(
						factory.createName(("paqueteA.H")))){ //$NON-NLS-1$
					oldType = (ClassType)bounds.get(i);
					break;
				}
			}
		}

		MOONRefactoring ref = new SpecializeBoundS(formalPar, oldType,newType, jm);			
		ref.run();	

		// Comienzan las comprobaciones.
		ClassType bTypeDest = (ClassType)(((BoundS)classDef.getFormalPars().get(0)).getBounds()).get(0);
		assertEquals(bTypeDest, newType);
	}

	/** 
	 * Comprueba que la refactorizaci�n funciona correctamente en un caso 
	 * con varios par�metros formales.
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test
	public void testWithMorePar() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestSpecializeBoundS/testWithMorePar"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.B")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("paqueteA.Entero")); //$NON-NLS-1$

		ClassType oldType = null;
		if (formalPar instanceof BoundS){
			List<Type> bounds = ((BoundS)formalPar).getBounds();
			for(int i = 0; i < bounds.size(); i++){
				if (bounds.get(i).getUniqueName().equals(
						factory.createName(("paqueteA.H")))){ //$NON-NLS-1$
					oldType = (ClassType)bounds.get(i);
					break;
				}
			}
		}

		MOONRefactoring ref = new SpecializeBoundS(formalPar, oldType,newType, jm);			
		ref.run();	

		// Comienzan las comprobaciones
		ClassType bTypeDest = (ClassType)(((BoundS)classDef.getFormalPars().get(0)).getBounds()).get(0);
		assertEquals(bTypeDest, newType);
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta hacer la 
	 * refactorizaci�n y el tipo elegido no es descendiente de la acotaci�n 
	 * inicial.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */ 
	@Test(expected=PreconditionException.class)   
	public void testCheckIsSubTypeBoundType() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestSpecializeBoundS/testCheckIsSubTypeBoundType"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.B")); //$NON-NLS-1$
		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("paqueteA.Entero")); //$NON-NLS-1$

		ClassType oldType = null;
		if (formalPar instanceof BoundS){
			List<Type> bounds = ((BoundS)formalPar).getBounds();
			for(int i = 0; i < bounds.size(); i++){
				if (bounds.get(i).getUniqueName().equals(
						factory.createName(("paqueteA.H")))){ //$NON-NLS-1$
					oldType = (ClassType)bounds.get(i);
					break;
				}
			}
		}

		MOONRefactoring ref = new SpecializeBoundS(formalPar, oldType,newType, jm);			
		ref.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta hacer la 
	 * refactorizaci�n y existe una sustituci�n del par�metro formal que no 
	 * es descendiente del seleccionado.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class) 
	public void testCheckIsSubTypeSubstFormalPar() throws Exception{
		
		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestSpecializeBoundS/testCheckIsSubTypeSubstFormalPar"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.B")); //$NON-NLS-1$

		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(factory.createName("paqueteA.Entero")); //$NON-NLS-1$

		ClassType oldType = null;
		if (formalPar instanceof BoundS){
			List<Type> bounds = ((BoundS)formalPar).getBounds();
			for(int i = 0; i < bounds.size(); i++){
				if (bounds.get(i).getUniqueName().equals(
						factory.createName(("paqueteA.H")))){ //$NON-NLS-1$
					oldType = (ClassType)bounds.get(i);
					break;
				}
			}
		}

		MOONRefactoring ref = new SpecializeBoundS(formalPar, oldType,newType, jm);			
		ref.run();
	}

	/**
	 * Verifica el funcionamiento de las precondiciones de la refactorizaci�n.
	 *
	 * <p>Comprueba que se lanza una excepci�n cuando se intenta hacer la 
	 * refactorizaci�n y hay una sustituci�n inv�lida del par�metro formal 
	 * en un descendiente.</p>
	 * 
	 * @throws Exception si se produce un error durante la ejecuci�n de la prueba.
	 */
	@Test(expected=PreconditionException.class) 
	public void testCheckIsSubtypeBoundDesc() throws Exception{

		SourceLoader sourceLoader = new SourceLoader();
		sourceLoader.loadFromDirectory(formatString(
			"./testdata/repository/moon/concreterefactoring/TestSpecializeBoundS/testCheckIsSubtypeBoundDesc"));		 //$NON-NLS-1$
		JavaModel jm = JavaModel.getInstance();
		MoonFactory factory = jm.getMoonFactory();

		ClassDef classDef = jm.getClassDef(factory.createName("paqueteA.B")); //$NON-NLS-1$

		List<FormalPar> lFormalPar = classDef.getFormalPars();
		FormalPar formalPar = lFormalPar.get(0); 

		ClassType newType = (ClassType)jm.getType(
				factory.createName("paqueteA.Entero")); //$NON-NLS-1$

		ClassType oldType = null;
		if (formalPar instanceof BoundS){
			List<Type> bounds = ((BoundS)formalPar).getBounds();
			for(int i = 0; i < bounds.size(); i++){
				if (bounds.get(i).getUniqueName().equals(
						factory.createName(("paqueteA.H")))){ //$NON-NLS-1$
					oldType = (ClassType)bounds.get(i);
					break;
				}
			}
		}

		MOONRefactoring ref = new SpecializeBoundS(formalPar, oldType,newType, jm);			
		ref.run();
	}
}