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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
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

	private static final String ACTION = "concreteaction.";
	private static final String PREDICATE = "concretepredicate.";
	private static final String REPOSITORY_MOON = "repository.moon.";
	private static final String REPOSITORY_JAVA = "repository.java.";
	private static final String METHOD = "Method";
	private static final String FALSE = "false";
	private static final String TRUE = "true";
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

		Iterator<String[]> entradas = definition.getInputs().iterator();
		String[] entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Model"); //$NON-NLS-1$
		assertEquals(entrada[1], "Model"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], FALSE); //$NON-NLS-1$
		String[] entrada2 = entradas.next();
		assertEquals(entrada2[0], "moon.core.classdef.MethDec"); //$NON-NLS-1$
		assertEquals(entrada2[1], METHOD); //$NON-NLS-1$
		assertNull(entrada2[2]);
		assertNull(entrada2[3]);
		assertEquals(entrada2[4], TRUE); //$NON-NLS-1$
		assertFalse(entradas.hasNext());

		Iterator<String> preconditions = definition.getPreconditions()
				.iterator();
		String pre = preconditions.next();
		assertEquals(pre, "ExistsClass"); //$NON-NLS-1$
		assertFalse(preconditions.hasNext());

		Iterator<String> actions = definition.getActions().iterator();
		String a = actions.next();

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

		Iterator<String[]> entradas = definition.getInputs().iterator();
		String[] entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Name"); //$NON-NLS-1$
		assertEquals(entrada[1], "Old_name"); //$NON-NLS-1$
		assertEquals(entrada[2], "Class"); //$NON-NLS-1$
		assertEquals(entrada[3], "getName"); //$NON-NLS-1$
		assertEquals(entrada[4], FALSE); //$NON-NLS-1$

		entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Model"); //$NON-NLS-1$
		assertEquals(entrada[1], "Model"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], FALSE); //$NON-NLS-1$

		entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.classdef.ClassDef"); //$NON-NLS-1$
		assertEquals(entrada[1], "Class"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], TRUE); //$NON-NLS-1$

		entrada = entradas.next();
		assertEquals(entrada[0], "moon.core.Name"); //$NON-NLS-1$
		assertEquals(entrada[1], "New_name"); //$NON-NLS-1$
		assertNull(entrada[2]);
		assertNull(entrada[3]);
		assertEquals(entrada[4], FALSE); //$NON-NLS-1$

		assertFalse(entradas.hasNext());

		Iterator<String> preconditions = definition.getPreconditions()
				.iterator();
		String pre = preconditions.next();
		assertEquals(pre, "NotExistsClassWithName"); //$NON-NLS-1$

		List<String[]> para1 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.PRECONDITION, pre),
				RefactoringConstants.PRECONDITION);
		assertEquals(para1.get(0)[0], "New_name"); //$NON-NLS-1$

		assertFalse(preconditions.hasNext());

		Iterator<String> actions = definition.getActions().iterator();
		List<String[]> para2;
		String a = actions.next();
		assertEquals(a, "RenameClass"); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.ACTION, a),
				RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameReferenceFile"); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.ACTION, a),
				RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameClassType"); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.ACTION, a),
				RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameGenericClassType"); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.ACTION, a),
				RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameConstructors"); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.ACTION, a),
				RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		a = actions.next();
		assertEquals(a, "RenameJavaFile"); //$NON-NLS-1$

		para2 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.ACTION, a),
				RefactoringConstants.ACTION);
		assertEquals(para2.get(0)[0], "Class"); //$NON-NLS-1$
		assertEquals(para2.get(1)[0], "New_name"); //$NON-NLS-1$

		assertFalse(actions.hasNext());

		Iterator<String> postconditions = definition.getPostconditions()
				.iterator();
		String post = postconditions.next();
		assertEquals(post, "NotExistsClassWithName"); //$NON-NLS-1$

		List<String[]> para3 = definition.getAmbiguousParameters(
				dynamicrefactoring.util.StringUtils
						.getMechanismFullyQualifiedName(
								RefactoringConstants.POSTCONDITION, post),
				RefactoringConstants.POSTCONDITION);
		assertEquals(para3.get(0)[0], "Old_name"); //$NON-NLS-1$

		assertFalse(postconditions.hasNext());

		Iterator<String[]> examples = definition.getExamples().iterator();
		String[] example = examples.next();
		assertEquals(example[0], "ejemplo1_antes.txt"); //$NON-NLS-1$
		assertEquals(example[1], "ejemplo1_despues.txt"); //$NON-NLS-1$

		assertFalse(examples.hasNext());
	}
}