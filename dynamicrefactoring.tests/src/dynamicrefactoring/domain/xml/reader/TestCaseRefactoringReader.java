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

import static dynamicrefactoring.domain.RefactoringMechanismType.POSTCONDITION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.RefactoringMechanismType;
import dynamicrefactoring.domain.metadata.interfaces.Category;

/**
 * Comprueba que funciona correctamente el proceso de lectura de la definici�n
 * de una refactorizaci�n din�mica.
 * 
 * Indirectamente, se comprueba tambi�n el funcionamiento de las clases que
 * implementan los patrones Bridge y Factory Method.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 */
public class TestCaseRefactoringReader {

	private static final String METHOD = "Method";
	/**
	 * Categoria 2
	 */
	public static final String MI_CATEGORIA2 = "MiCategoria2";
	/**
	 * Categoria 1
	 */
	public static final String MI_CATEGORIA1 = "MiCategoria1";
	/**
	 * Clasificacion
	 */
	public static final String MI_CLASSIFICATION = "MiClassification";
	/**
	 * Clasificacion 2
	 */
	public static final String MI_CLASSIFICATION2 = "MiClassification2";
	/**
	 * Palabra clave 2
	 */
	public static final String KEY_WORD2 = "PalabraClave2";
	/**
	 * Palabra clave 1
	 */
	public static final String KEY_WORD1 = "PalabraClave1";
	/**
	 * Extension de fichero xml
	 */
	public static final String XML_EXTENSION = ".xml";
	/**
	 * Nombre de la refactorizacion con informacion minima con palabras clave.
	 */
	public static final String MINIMUM_INFORMATION_WITH_KEYWORDS = "MinimumInformationWithKeywords";
	/**
	 * Nombre de la refactorizacion con clasificaciones
	 */
	public static final String REFACTORING_WITH_CLASSIFICATION = "RefactoringWithClassification";
	/**
	 * Nombre de la refactorizacion con dos clasificaciones.
	 */
	public static final String REFACTORING_WITH_TWO_CLASSIFICATIONS = "RefactoringWithTwoClassifications";
	/**
	 * Nombre de la refactorizacion minima.
	 */
	public static final String MINIMUM_INFORMATION_REFACTORING = "MinimumInformation";

	/**
	 * Directorio XML con los datos de entrada de los tests.
	 */
	public static final String TESTDATA_XML_READER_DIR = "./testdata/XML/Reader/";

	/**
	 * Comprueba que la lectura no se realiza cuando la definici�n no contiene
	 * toda la informaci�n m�nima necesaria (no cumple las reglas del DTD).
	 * 
	 * Para ello se realiza la carga de una definici�n incompleta desde un
	 * fichero XML y luego se comprueba que se lanza una excepci�n de tipo
	 * DynamicRefactoringException.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test(expected = RefactoringException.class)
	public void testReadingWithIncompleteInformation() throws Exception {

		DynamicRefactoringDefinition
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ "IncompleteInformation" + XML_EXTENSION); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura no se realiza cuando la definici�n utiliza otra
	 * estructura que la que se define en el DTD.
	 * 
	 * Para ello se realiza la carga de un fichero XML con una estructura
	 * distinta a la del DTD y luego se comprueba que se lanza una excepci�n de
	 * tipo DynamicRefactoringException.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test(expected = RefactoringException.class)
	public void testReadingNotARefactoring() throws Exception {

		DynamicRefactoringDefinition
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ "NotARefactoring" + XML_EXTENSION); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando la definici�n
	 * contiene la informaci�n m�nima necesaria. Para ello se realiza la carga
	 * de la definici�n de una refactorizaci�n desde un fichero XML y luego se
	 * comprueba el valor de todos los campos recuperados.
	 * 
	 * Esta informaci�n es: el nombre, la descripci�n, la motivaci�n, una
	 * entrada, una precondici�n, una acci�n y una postcondici�n; no tiene ni
	 * imagen, ni par�metros ambiguos ni ejemplos.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithMinimunInformation() throws Exception {

		DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ MINIMUM_INFORMATION_REFACTORING + XML_EXTENSION); //$NON-NLS-1$

		assertEquals(definition.getName(), MINIMUM_INFORMATION_REFACTORING); //$NON-NLS-1$

		assertMinimumInformationEqual(definition);

	}

	/**
	 * Comprueba que se lee correctamente una refactorizacion con informacion
	 * minima y categorias.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadingWithMinimunInformationWithCategories()
			throws Exception {

		DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ REFACTORING_WITH_CLASSIFICATION + XML_EXTENSION); //$NON-NLS-1$

		assertEquals(definition.getName(), REFACTORING_WITH_CLASSIFICATION); //$NON-NLS-1$

		assertMinimumInformationEqual(definition);

		// Comprobar categorias
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA1));
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA2));

		assertEquals(expectedCategories, definition.getCategories());

	}

	/**
	 * Comprueba que se lee correctamente una refactorizacion con la informacion
	 * minima y con dos categorias.
	 * 
	 * @throws Exception
	 *             si hay algun fallo
	 */
	@Test
	public void testReadingWithMinimunInformationWithTwoCategories()
			throws Exception {

		DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ REFACTORING_WITH_TWO_CLASSIFICATIONS + XML_EXTENSION); //$NON-NLS-1$

		assertEquals(definition.getName(), REFACTORING_WITH_TWO_CLASSIFICATIONS); //$NON-NLS-1$

		assertMinimumInformationEqual(definition);

		// Comprobar categorias
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA1));
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA2));
		expectedCategories.add(new Category(MI_CLASSIFICATION2, MI_CATEGORIA1));
		expectedCategories.add(new Category(MI_CLASSIFICATION2, MI_CATEGORIA2));

		assertEquals(expectedCategories, definition.getCategories());

	}

	/**
	 * Comprueba que se lee correctamente una refactorizacion con la informacion
	 * minima y palabras clave.
	 * 
	 * @throws Exception
	 *             si hay fallos
	 */
	@Test
	public void testReadingWithMinimunInformationWithKeyWords()
			throws Exception {

		DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ MINIMUM_INFORMATION_WITH_KEYWORDS + XML_EXTENSION); //$NON-NLS-1$

		assertEquals(definition.getName(), MINIMUM_INFORMATION_WITH_KEYWORDS); //$NON-NLS-1$

		assertMinimumInformationEqual(definition);

		// Comprobar categorias
		Set<String> expectedKeywords = new HashSet<String>();
		expectedKeywords.add(KEY_WORD1);
		expectedKeywords.add(KEY_WORD2);

		assertEquals(expectedKeywords, definition.getKeywords());

	}

	private void assertMinimumInformationEqual(
			DynamicRefactoringDefinition definition) {
		assertEquals(definition.getDescription(), "Descripcion."); //$NON-NLS-1$

		assertEquals(definition.getImage(), ""); //$NON-NLS-1$

		assertEquals(definition.getMotivation(), "Motivacion."); //$NON-NLS-1$

		Iterator<InputParameter> entradas = definition.getInputs().iterator();
		InputParameter entrada = entradas.next();
		assertEquals(entrada.getType(), "moon.core.Model"); //$NON-NLS-1$
		assertEquals(entrada.getName(), "Model"); //$NON-NLS-1$
		assertTrue(entrada.getFrom().isEmpty());
		assertTrue(entrada.getMethod().isEmpty());
		assertEquals(entrada.isMain(), false); //$NON-NLS-1$
		
		InputParameter entrada2 = entradas.next();
		assertEquals(entrada2.getType(), "moon.core.classdef.MethDec"); //$NON-NLS-1$
		assertEquals(entrada2.getName(), METHOD); //$NON-NLS-1$
		assertTrue(entrada2.getFrom().isEmpty());
		assertTrue(entrada2.getMethod().isEmpty());
		assertEquals(entrada2.isMain(), true); //$NON-NLS-1$
		assertFalse(entradas.hasNext());

		Iterator<RefactoringMechanismInstance> preconditions = definition.getPreconditions()
				.iterator();
		RefactoringMechanismInstance pre = preconditions.next();
		assertEquals(pre, new RefactoringMechanismInstance("ExistsClass", RefactoringMechanismType.PRECONDITION)); //$NON-NLS-1$
		assertFalse(preconditions.hasNext());

		Iterator<String> actions = RefactoringMechanismInstance.getMechanismListClassNames(definition.getActions()).iterator();
		String a = actions.next();

		assertEquals(a, "RenameClass"); //$NON-NLS-1$
		assertFalse(actions.hasNext());

		Iterator<RefactoringMechanismInstance> postconditions = definition.getPostconditions()
				.iterator();
		RefactoringMechanismInstance post = postconditions.next();
		assertEquals(post, new RefactoringMechanismInstance("ExistsClass", POSTCONDITION)); //$NON-NLS-1$
		assertFalse(postconditions.hasNext());

		Iterator<RefactoringExample> examples = definition.getExamples().iterator();
		assertFalse(examples.hasNext());
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando la definici�n
	 * contiene toda la informaci�n posible. Para ello se realiza la carga de la
	 * definici�n de una refactorizaci�n desde un fichero XML y luego se
	 * comprueba el valor de todos los campos recuperados.
	 * 
	 * Esta informaci�n es: el nombre, la descripci�n, la imagen, la motivaci�n,
	 * varias entradas, precondiciones, acciones, postcondiciones, par�metros
	 * ambiguos y ejemplos.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithFullInformation() throws Exception {

		XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
		XMLRefactoringReaderImp implementor = f.makeXMLRefactoringReaderImp(); //$NON-NLS-1$
		XMLRefactoringReader temporaryReader = new XMLRefactoringReader(
				implementor);
		DynamicRefactoringDefinition definition = temporaryReader
				.getDynamicRefactoringDefinition(new File(
						TESTDATA_XML_READER_DIR + "FullInformation"
								+ XML_EXTENSION));

		assertEquals(definition.getName(), "FullInformation"); //$NON-NLS-1$

		assertEquals(definition.getDescription(), "Renames the class."); //$NON-NLS-1$

		assertEquals(definition.getImage(), "renameclass.JPG"); //$NON-NLS-1$

		assertEquals(definition.getMotivation(),
				"The name of class does not reveal its intention."); //$NON-NLS-1$

		List<InputParameter> expected = new ArrayList<InputParameter>();
		expected.add(new InputParameter.Builder( "moon.core.Name").name("Old_name").from("Class").method("getName").main(false).build());
		expected.add(new InputParameter.Builder( "moon.core.Model").name("Model").from("").method("").main(false).build());
		expected.add(new InputParameter.Builder( "moon.core.classdef.ClassDef").name( "Class").from("").method("").main(true).build());
		expected.add(new InputParameter.Builder( "moon.core.Name").name( "New_name").from("").method("").main(false).build());
		assertEquals(expected, definition.getInputs());

		Iterator<RefactoringMechanismInstance> mechanism = definition.getPreconditions().iterator();

		checkPredicateAsMechanism(definition, new RefactoringMechanismInstance("NotExistsClassWithName", RefactoringMechanismType.PRECONDITION), mechanism.next(), "New_name");
		assertFalse(mechanism.hasNext());

		Iterator<String> actions = RefactoringMechanismInstance.getMechanismListClassNames(definition.getActions()).iterator();

		checkAction(definition, "RenameClass", actions.next());
		checkAction(definition, "RenameReferenceFile", actions.next());
		checkAction(definition, "RenameClassType", actions.next());
		checkAction(definition, "RenameGenericClassType", actions.next());
		checkAction(definition, "RenameConstructors", actions.next());
		checkAction(definition, "RenameJavaFile", actions.next());

		assertFalse(actions.hasNext());

		Iterator<RefactoringMechanismInstance> postconditions = definition.getPostconditions()
				.iterator();
		checkPredicateAsMechanism(definition, new RefactoringMechanismInstance("NotExistsClassWithName", POSTCONDITION),
				postconditions.next(),"Old_name");

		assertFalse(postconditions.hasNext());

		Iterator<RefactoringExample> examples = definition.getExamples().iterator();
		RefactoringExample example = examples.next();
		assertEquals(example.getBefore(), "ejemplo1_antes.txt"); //$NON-NLS-1$
		assertEquals(example.getAfter(), "ejemplo1_despues.txt"); //$NON-NLS-1$

		assertFalse(examples.hasNext());
	}

	private void checkPredicateAsMechanism(DynamicRefactoringDefinition definition,
			RefactoringMechanismInstance expectedMechanism, RefactoringMechanismInstance pre,
			String attributeExpected) {
		assertEquals(expectedMechanism, pre); //$NON-NLS-1$

		List<String[]> para1 = definition.getAmbiguousParameters(pre.getClassName(), pre.getType());
		assertEquals(para1.get(0)[0], attributeExpected); //$NON-NLS-1$
	}

	private void checkAction(DynamicRefactoringDefinition definition,
			String expectedActionName, String actionName) {
		List<String[]> para2;
		assertEquals(actionName, expectedActionName); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(actionName,
				RefactoringMechanismType.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$
	}

}