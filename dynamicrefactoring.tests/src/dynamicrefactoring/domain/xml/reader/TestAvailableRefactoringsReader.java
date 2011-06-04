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

import java.util.Map;

import org.junit.Test;

import dynamicrefactoring.domain.Scope;

/**
 * Comprueba que funciona correctamente el proceso de lectura del conjunto de
 * refactorizaciones disponibles.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public final class TestAvailableRefactoringsReader {

	/**
	 * Comprueba que la lectura no se realiza cuando la definición no contiene
	 * toda la información m�nima necesaria (no cumple las reglas del DTD).
	 * 
	 * Para ello se realiza una lectura del documento para un Ámbito
	 * determinado.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test(expected = XMLRefactoringReaderException.class)
	public void testReadingFileWithIncompleteInformation() throws Exception {
		JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.ATTRIBUTE,
						"./testdata/XML/Reader/availableRefactorings/incompleteInformation.xml");
	}

	/**
	 * Comprueba que la lectura no se realiza cuando la definición utiliza otra
	 * estructura que la que se define en el DTD.
	 * 
	 * Para ello se realiza una lectura del documento para un Ámbito
	 * determinado.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test(expected = XMLRefactoringReaderException.class)
	public void testReadingIncorrectStructure() throws Exception {
		JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.ATTRIBUTE,
						"./testdata/XML/Reader/availableRefactorings/diferentStructure.xml");
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando el fichero xml
	 * contiene la información m�nima necesaria.Es decir que el fichero no tiene
	 * ninguna refactorización de ningún Ámbito.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithMinimumInformation() throws Exception {
		// Comprueba que no hay ninguna refactorización en ningún Ámbito

		Map<String, String> refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.CLASS,
						"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");

		assertEquals(0, refactorings.size());

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.METHOD,
						"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");

		assertEquals(0, refactorings.size());

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.ATTRIBUTE,
						"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");

		assertEquals(0, refactorings.size());

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.FORMAL_PAR,
						"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");

		assertEquals(0, refactorings.size());

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.FORMAL_ARG,
						"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");

		assertEquals(0, refactorings.size());
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando el plan contiene
	 * la información completa. Hay refactorizaciones para cada uno de los
	 * Ámbitos.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithCompleteInformation() throws Exception {
		// Comprueba que no hay ninguna refactorización en ningún Ámbito

		Map<String, String> refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.CLASS,
						"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");

		assertEquals(3, refactorings.size());
		assertEquals(refactorings.get("EnumeratedTypes"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "EnumeratedTypes" + "\\"
				+ "EnumeratedTypes.xml");
		assertEquals(refactorings.get("MigrateJUnit3ToJUnit4"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "MigrateJUnit3ToJUnit4" + "\\"
				+ "MigrateJUnit3ToJUnit4.xml");
		assertEquals(refactorings.get("Rename Class"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Rename Class" + "\\"
				+ "Rename Class.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.METHOD,
						"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");

		assertEquals(4, refactorings.size());
		assertEquals(refactorings.get("MigrateJUnit3ToJUni4TestException"), "."
				+ "\\" + "DynamicRefactorings" + "\\"
				+ "MigrateJUnit3ToJUni4TestException" + "\\"
				+ "MigrateJUnit3ToJUni4TestException.xml");
		assertEquals(refactorings.get("Rename Method"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Rename Method" + "\\"
				+ "Rename Method.xml");
		assertEquals(refactorings.get("Add Parameter"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Add Parameter" + "\\"
				+ "Add Parameter.xml");
		assertEquals(refactorings.get("AddOverrideAnnotation"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "AddOverrideAnnotation" + "\\"
				+ "AddOverrideAnnotation.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.ATTRIBUTE,
						"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("Move Field"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Move Field" + "\\"
				+ "Move Field.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.FORMAL_ARG,
						"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");

		assertEquals(2, refactorings.size());
		assertEquals(refactorings.get("Rename Parameter"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Rename Parameter" + "\\"
				+ "Rename Parameter.xml");
		assertEquals(refactorings.get("Remove Parameter"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Remove Parameter" + "\\"
				+ "Remove Parameter.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.FORMAL_PAR,
						"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");

		assertEquals(3, refactorings.size());
		assertEquals(refactorings.get("Specialize Bound S"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "Specialize Bound S" + "\\"
				+ "Specialize Bound S.xml");
		assertEquals(refactorings.get("Replace Formal Parameter With Type"),
				"." + "\\" + "DynamicRefactorings" + "\\"
						+ "Replace Formal Parameter With Type" + "" + "\\"
						+ "Replace Formal Parameter With Type.xml");
		assertEquals(
				refactorings.get("Replace Formal Parameter With Bounding Type"),
				"." + "\\" + "DynamicRefactorings" + "\\"
						+ "Replace Formal Parameter With Bounding Type" + ""
						+ "\\"
						+ "Replace Formal Parameter With Bounding Type.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.CODE_FRAGMENT,
						"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("ExtractMethod"), "." + "\\"
				+ "DynamicRefactorings" + "\\" + "ExtractMethod" + "\\"
				+ "ExtractMethod.xml");

	}
}
