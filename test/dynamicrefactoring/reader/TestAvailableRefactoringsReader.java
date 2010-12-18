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

package dynamicrefactoring.reader;

import dynamicrefactoring.interfaz.SelectRefactoringWindow;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.XMLRefactoringReaderException;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test; 

/**
 * Comprueba que funciona correctamente el proceso de lectura del conjunto de
 * refactorizaciones disponibles.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 *
 */
public class TestAvailableRefactoringsReader {

	/**
	 * Comprueba que la lectura no se realiza cuando la definición no contiene
	 * toda la información mínima necesaria (no cumple las reglas del DTD).
	 * 
	 * Para ello se realiza una lectura del documento para un ámbito determinado.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test(expected=XMLRefactoringReaderException.class)
	public void testReadingFileWithIncompleteInformation() throws Exception{
		new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
				"./testdata/XML/Reader/availableRefactorings/incompleteInformation.xml");
	}

	/**
	 * Comprueba que la lectura no se realiza cuando la definición utiliza otra
	 * estructura que la que se define en el DTD.
	 * 
	 * Para ello se realiza una lectura del documento para un ámbito determinado.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test(expected=XMLRefactoringReaderException.class)
	public void testReadingIncorrectStructure() throws Exception{
		new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
				"./testdata/XML/Reader/availableRefactorings/diferentStructure.xml");
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando el fichero xml
	 * contiene la información mínima necesaria.Es decir que el fichero no
	 * tiene ninguna refactorización de ningún ámbito.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithMinimumInformation() throws Exception{
		//Comprueba que no hay ninguna refactorización en ningún ámbito
		
		HashMap<String,String> refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_CLASS,
		"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");
	
		assertEquals(0,refactorings.size());
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_METHOD,
		"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");
	
		assertEquals(0,refactorings.size());
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
		"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");
	
		assertEquals(0,refactorings.size());
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_PAR,
		"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");
	
		assertEquals(0,refactorings.size());
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_ARG,
		"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml");
	
		assertEquals(0,refactorings.size());
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando el plan
	 * contiene la información completa. Hay refactorizaciones para cada uno de
	 * los ámbitos.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithCompleteInformation() throws Exception{
		//Comprueba que no hay ninguna refactorización en ningún ámbito
		
		HashMap<String,String> refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_CLASS,
				"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");
	
		assertEquals(3,refactorings.size());
		assertEquals(refactorings.get("EnumeratedTypes"),
				".\\DynamicRefactorings\\EnumeratedTypes\\EnumeratedTypes.xml");
		assertEquals(refactorings.get("MigrateJUnit3ToJUnit4"),
				".\\DynamicRefactorings\\MigrateJUnit3ToJUnit4\\MigrateJUnit3ToJUnit4.xml");
		assertEquals(refactorings.get("Rename Class"),
				".\\DynamicRefactorings\\Rename Class\\Rename Class.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_METHOD,
				"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");
	
		assertEquals(4,refactorings.size());
		assertEquals(refactorings.get("MigrateJUnit3ToJUni4TestException"),
			".\\DynamicRefactorings\\MigrateJUnit3ToJUni4TestException\\MigrateJUnit3ToJUni4TestException.xml");
		assertEquals(refactorings.get("Rename Method"),
			".\\DynamicRefactorings\\Rename Method\\Rename Method.xml");
		assertEquals(refactorings.get("Add Parameter"),
			".\\DynamicRefactorings\\Add Parameter\\Add Parameter.xml");
		assertEquals(refactorings.get("AddOverrideAnnotation"),
			".\\DynamicRefactorings\\AddOverrideAnnotation\\AddOverrideAnnotation.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
				"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Move Field"),
			".\\DynamicRefactorings\\Move Field\\Move Field.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_ARG,
			"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");
	
		assertEquals(2,refactorings.size());
		assertEquals(refactorings.get("Rename Parameter"),
			".\\DynamicRefactorings\\Rename Parameter\\Rename Parameter.xml");
		assertEquals(refactorings.get("Remove Parameter"),
			".\\DynamicRefactorings\\Remove Parameter\\Remove Parameter.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_FORMAL_PAR,
				"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");
	
		assertEquals(3,refactorings.size());
		assertEquals(refactorings.get("Specialize Bound S"),
			".\\DynamicRefactorings\\Specialize Bound S\\Specialize Bound S.xml");
		assertEquals(refactorings.get("Replace Formal Parameter With Type"),
			".\\DynamicRefactorings\\Replace Formal Parameter With Type" +
			"\\Replace Formal Parameter With Type.xml");
		assertEquals(refactorings.get("Replace Formal Parameter With Bounding Type"),
			".\\DynamicRefactorings\\Replace Formal Parameter With Bounding Type" +
			"\\Replace Formal Parameter With Bounding Type.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT,
				"./testdata/XML/Reader/availableRefactorings/completedInformation.xml");
		
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("ExtractMethod"),
			".\\DynamicRefactorings\\ExtractMethod\\ExtractMethod.xml");
		
		
	}
}
