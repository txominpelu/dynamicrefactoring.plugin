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

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReader;
import dynamicrefactoring.reader.XMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReaderImp;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test; 

/**
 * Comprueba que funciona correctamente el proceso de lectura de la definición
 * de una refactorización dinámica.
 * 
 * Indirectamente, se comprueba también el funcionamiento de las clases que
 * implementan los patrones Bridge y Factory Method.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 *
 */
public class TestCaseRefactoringReader {

	/**
	 * Comprueba que la lectura no se realiza cuando la definición no contiene
	 * toda la información mínima necesaria (no cumple las reglas del DTD).
	 * 
	 * Para ello se realiza la carga de una definición incompleta desde un
	 * fichero XML y luego se comprueba que se lanza una excepción de tipo
	 * DynamicRefactoringException.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test(expected=RefactoringException.class)
	public void testReadingWithIncompleteInformation() throws Exception{

		DynamicRefactoringDefinition.getRefactoringDefinition(
			"./testdata/XML/Reader/IncompleteInformation.xml"); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura no se realiza cuando la definición utiliza otra
	 * estructura que la que se define en el DTD.
	 * 
	 * Para ello se realiza la carga de un fichero XML con una estructura
	 * distinta a la del DTD y luego se comprueba que se lanza una excepción de
	 * tipo DynamicRefactoringException.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test(expected=RefactoringException.class)
	public void testReadingNotARefactoring() throws Exception{

		DynamicRefactoringDefinition.getRefactoringDefinition(
			"./testdata/XML/Reader/NotARefactoring.xml"); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando la definición
	 * contiene la información mínima necesaria. Para ello se realiza la carga
	 * de la definición de una refactorización desde un fichero XML y luego se
	 * comprueba el valor de todos los campos recuperados.
	 * 
	 * Esta información es: el nombre, la descripción, la motivación, una
	 * entrada, una precondición, una acción y una postcondición; no tiene ni
	 * imagen, ni parámetros ambiguos ni ejemplos.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithMinimunInformation() throws Exception{


		DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
		.getRefactoringDefinition("./testdata/XML/Reader/MinimumInformation.xml"); //$NON-NLS-1$

		assertEquals(definition.getName(), "MinimumInformation"); //$NON-NLS-1$

		assertMinimumInformationEqual(definition);

	}
	
	@Test
	public void testReadingWithMinimunInformationWithCategories() throws Exception{


		DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
		.getRefactoringDefinition("./testdata/XML/Reader/RefactoringWithClassification.xml"); //$NON-NLS-1$

		assertEquals(definition.getName(), "RefactoringWithClassification"); //$NON-NLS-1$

		assertMinimumInformationEqual(definition);
		
		//Comprobar categorias
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category("MiClassification", "MiCategoria1"));
		expectedCategories.add(new Category("MiClassification", "MiCategoria2"));
		
		assertEquals (expectedCategories,definition.getCategories());

	}

	private void assertMinimumInformationEqual(
			DynamicRefactoringDefinition definition) {
		assertEquals(definition.getDescription(), "Descripcion."); //$NON-NLS-1$

		assertEquals(definition.getImage(), ""); //$NON-NLS-1$

		assertEquals(definition.getMotivation(), "Motivacion."); //$NON-NLS-1$

		Iterator<String[]> entradas = definition.getInputs().iterator();
		String[] entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Model"); //$NON-NLS-1$
		assertEquals(entrada[1], "Model"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], "false"); //$NON-NLS-1$
		String[] entrada2 = entradas.next();
		assertEquals(entrada2[0], "moon.core.classdef.MethDec"); //$NON-NLS-1$
		assertEquals(entrada2[1], "Method"); //$NON-NLS-1$
		assertNull(entrada2[2]);
		assertNull(entrada2[3]);
		assertEquals(entrada2[4], "true"); //$NON-NLS-1$
		assertFalse(entradas.hasNext());

		Iterator<String> preconditions = definition.getPreconditions().iterator();
		String pre = preconditions.next();
		assertEquals(pre, "ExistsClass"); //$NON-NLS-1$
		assertFalse(preconditions.hasNext());

		Iterator<String> actions = definition.getActions().iterator();
		String a = actions.next();
		System.out.println(a);
		assertEquals(a, "RenameClass"); //$NON-NLS-1$
		assertFalse(actions.hasNext());

		Iterator<String> postconditions = definition.getPostconditions()
		.iterator();
		String post = postconditions.next();
		assertEquals(post, "ExistsClass"); //$NON-NLS-1$
		assertFalse(postconditions.hasNext());

		Iterator<String[]> examples = definition.getExamples().iterator();
		assertFalse(examples.hasNext());
	}
	
	

	/**
	 * Comprueba que la lectura se realiza correctamente cuando la definición
	 * contiene toda la información posible. Para ello se realiza la carga de la
	 * definición de una refactorización desde un fichero XML y luego se
	 * comprueba el valor de todos los campos recuperados.
	 * 
	 * Esta información es: el nombre, la descripción, la imagen, la motivación,
	 * varias entradas, precondiciones, acciones, postcondiciones, parámetros
	 * ambiguos y ejemplos.
	 * 
	 * @throws Exception si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithFullInformation() throws Exception {

		XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
		XMLRefactoringReaderImp implementor = f
		.makeXMLRefactoringReaderImp(new File("./testdata/XML/Reader/FullInformation.xml")); //$NON-NLS-1$
		XMLRefactoringReader temporaryReader = new XMLRefactoringReader(
				implementor);
		DynamicRefactoringDefinition definition = temporaryReader
		.getDynamicRefactoringDefinition();

		assertEquals(definition.getName(), "FullInformation"); //$NON-NLS-1$

		assertEquals(definition.getDescription(), "Renames the class."); //$NON-NLS-1$

		assertEquals(definition.getImage(), "renameclass.JPG"); //$NON-NLS-1$

		assertEquals(definition.getMotivation(),
			"The name of class does not reveal its intention."); //$NON-NLS-1$

		Iterator<String[]> entradas = definition.getInputs().iterator();
		String[] entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Name"); //$NON-NLS-1$
		assertEquals(entrada[1], "Old_name"); //$NON-NLS-1$
		assertEquals(entrada[2], "Class"); //$NON-NLS-1$
		assertEquals(entrada[3], "getName"); //$NON-NLS-1$
		assertEquals(entrada[4], "false"); //$NON-NLS-1$

		entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Model"); //$NON-NLS-1$
		assertEquals(entrada[1], "Model"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], "false"); //$NON-NLS-1$

		entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.classdef.ClassDef"); //$NON-NLS-1$
		assertEquals(entrada[1], "Class"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], "true"); //$NON-NLS-1$

		entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Name"); //$NON-NLS-1$
		assertEquals(entrada[1], "New_name"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], "false"); //$NON-NLS-1$

		assertFalse(entradas.hasNext());

		Iterator<String> preconditions = definition.getPreconditions()
		.iterator();
		String pre = preconditions.next();
		assertEquals(pre, "NotExistsClassWithName"); //$NON-NLS-1$

		ArrayList<String[]> para1= definition.getAmbiguousParameters(pre, RefactoringConstants.PRECONDITION);
		assertEquals(para1.get(0)[0], "New_name"); //$NON-NLS-1$

		assertFalse(preconditions.hasNext());

		Iterator<String> actions = definition.getActions().iterator();
		ArrayList<String[]> para2;
		String a = actions.next();
		assertEquals(a, "RenameClass"); //$NON-NLS-1$

		para2= definition.getAmbiguousParameters(a, RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameReferenceFile"); //$NON-NLS-1$

		para2= definition.getAmbiguousParameters(a, RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameClassType"); //$NON-NLS-1$

		para2= definition.getAmbiguousParameters(a, RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameGenericClassType"); //$NON-NLS-1$

		para2= definition.getAmbiguousParameters(a, RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameConstructors"); //$NON-NLS-1$

		para2= definition.getAmbiguousParameters(a, RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameJavaFile"); //$NON-NLS-1$

		para2= definition.getAmbiguousParameters(a, RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		assertFalse(actions.hasNext());

		Iterator<String> postconditions = definition.getPostconditions()
		.iterator();
		String post = postconditions.next();
		assertEquals(post, "NotExistsClassWithName"); //$NON-NLS-1$

		ArrayList<String[]> para3= definition.getAmbiguousParameters(post, RefactoringConstants.POSTCONDITION);
		assertEquals(para3.get(0)[0], "Old_name"); //$NON-NLS-1$

		assertFalse(postconditions.hasNext());

		Iterator<String[]> examples = definition.getExamples().iterator();
		String[] example = examples.next();
		assertEquals(example[0], "ejemplo1_antes.txt"); //$NON-NLS-1$
		assertEquals(example[1], "ejemplo1_despues.txt"); //$NON-NLS-1$

		assertFalse(examples.hasNext());
	}
}