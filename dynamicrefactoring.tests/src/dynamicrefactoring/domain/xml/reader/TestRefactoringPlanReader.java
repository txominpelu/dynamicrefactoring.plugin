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

package dynamicrefactoring.domain.xml.reader;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import dynamicrefactoring.domain.xml.reader.RefactoringPlanReader;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderException;

/**
 * Comprueba que funciona correctamente el proceso de lectura de un plan de 
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 *
 */
public class TestRefactoringPlanReader {

	/**
	 * Comprueba que la lectura no se realiza cuando la definición no contiene
	 * toda la información m�nima necesaria (no cumple las reglas del DTD).
	 * 
	 * Para ello se realiza una lectura del documento para obtener todas las
	 * refactorizaciones del mismo.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test(expected=XMLRefactoringReaderException.class)
	public void testReadingPlanWithIncompleteInformation() throws Exception{

		RefactoringPlanReader.readAllRefactoringsFromThePlan(
			"./testdata/XML/Reader/refactoringPlan/refactoringPlanIncomplete.xml"); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura no se realiza cuando la definición utiliza otra
	 * estructura que la que se define en el DTD.
	 * 
	 * Para ello se realiza una lectura del documento para obtener todas las
	 * refactorizaciones del mismo.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test(expected=XMLRefactoringReaderException.class)
	public void testReadingNotARefactoringPlan() throws Exception{

		RefactoringPlanReader.readAllRefactoringsFromThePlan(
			"./testdata/XML/Reader/refactoringPlan/notARefactoringPlan.xml"); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando el plan
	 * contiene la información m�nima necesaria.Es decir que el plan solo tiene
	 * la etiqueta principal refactoring plan y ninguna refactorización.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingPlanWithMinimunInformation() throws Exception{
		ArrayList<String> refactorings =RefactoringPlanReader
			.readAllRefactoringsFromThePlan("./testdata/XML/Reader/refactoringPlan" +
					"/minimunRefactoringPlan.xml");
		//Comprueba que el plan no tiene ninguna refactorización.
		assertEquals(0,refactorings.size());
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando el plan
	 * contiene toda la información posible. 
	 * 
	 * Esta información es: un conjunto de refactorizaciones con su nombre, fecha
	 * de ejecuci�n y parámetros.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingPlanWithFullInformation() throws Exception {
		ArrayList<String> refactorings =RefactoringPlanReader
		.readAllRefactoringsFromThePlan("./testdata/XML/Reader/refactoringPlan" +
				"/completeRefactoringPlan.xml");
		//Comprueba que el plan tiene 2 refactorizaciones.
		assertEquals(3,refactorings.size());
		
		//Comprueba que las cadenas devueltas se corresponden con los nombres de 
		//las refactorizaciones.
		assertEquals("Add Parameter",refactorings.get(0));
		assertEquals("Move Field",refactorings.get(1));
		assertEquals("ExtractMethod",refactorings.get(2));
		
		//Comprobamos que lee correctamente los parámetros de las refactorizaciones.
		assertEquals(RefactoringPlanReader.getInputValue("Add Parameter","Name","./testdata/XML" +
				"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "p4");
		assertEquals(RefactoringPlanReader.getInputValue("Add Parameter","Method","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "paqueteA.C~met1%int%Double%short");
		assertEquals(RefactoringPlanReader.getInputValue("Add Parameter","Type","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "int");
		assertEquals(RefactoringPlanReader.getInputValue("Add Parameter","Class","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "paqueteA.C");
		assertEquals(RefactoringPlanReader.getInputValue("Move Field","Source_class","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "paqueteA.H");
		assertEquals(RefactoringPlanReader.getInputValue("Move Field","Target_class","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "paqueteA.B");
		assertEquals(RefactoringPlanReader.getInputValue("Move Field","Attribute","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "paqueteA.H#atributo1");
		assertEquals(RefactoringPlanReader.getInputValue("ExtractMethod","Name","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), "nuevo_metodo");
		assertEquals(RefactoringPlanReader.getInputValue("ExtractMethod","Fragment","./testdata/XML" +
		"/Reader/refactoringPlan/completeRefactoringPlan.xml"), 
		"11,8,11,29,paqueteA.E,System.out.println(a);");
		
	}
}
