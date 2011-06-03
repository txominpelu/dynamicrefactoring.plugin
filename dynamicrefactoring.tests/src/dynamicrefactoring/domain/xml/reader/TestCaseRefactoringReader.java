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
import dynamicrefactoring.domain.xml.RefactoringXMLTest;
import dynamicrefactoring.domain.xml.XMLRefactoringUtils;

/**
 * Comprueba que funciona correctamente el proceso de lectura de la definición
 * de una refactorización din�mica.
 * 
 * Indirectamente, se comprueba tambi�n el funcionamiento de las clases que
 * implementan los patrones Bridge y Factory Method.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 */
public class TestCaseRefactoringReader extends RefactoringXMLTest{

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
	 * Comprueba que la lectura no se realiza cuando la definición no contiene
	 * toda la información m�nima necesaria (no cumple las reglas del DTD).
	 * 
	 * Para ello se realiza la carga de una definición incompleta desde un
	 * fichero XML y luego se comprueba que se lanza una excepci�n de tipo
	 * DynamicRefactoringException.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test(expected = RefactoringException.class)
	public void testReadingWithIncompleteInformation() throws Exception {

		XMLRefactoringUtils
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ "IncompleteInformation" + XML_EXTENSION); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura no se realiza cuando la definición utiliza otra
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

		XMLRefactoringUtils
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ "NotARefactoring" + XML_EXTENSION); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando la definición
	 * contiene la información m�nima necesaria. Para ello se realiza la carga
	 * de la definición de una refactorización desde un fichero XML y luego se
	 * comprueba el valor de todos los campos recuperados.
	 * 
	 * Esta información es: el nombre, la descripci�n, la motivación, una
	 * entrada, una precondición, una acción y una postcondición; no tiene ni
	 * imagen, ni parámetros ambiguos ni ejemplos.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la lectura.
	 */
	@Test
	public void testReadingWithMinimunInformation() throws Exception {

		DynamicRefactoringDefinition definition = XMLRefactoringUtils
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ MINIMUM_INFORMATION_REFACTORING + XML_EXTENSION); //$NON-NLS-1$

		//assertEquals(definition.getName(), ); //$NON-NLS-1$

		assertMinimumInformationEqual(MINIMUM_INFORMATION_REFACTORING, definition, new HashSet<String>(), getMinimumInformationRefactCategories());

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

		DynamicRefactoringDefinition definition = XMLRefactoringUtils
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ REFACTORING_WITH_CLASSIFICATION + XML_EXTENSION); //$NON-NLS-1$

		// Comprobar categorias
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA1));
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA2));
		assertMinimumInformationEqual(REFACTORING_WITH_CLASSIFICATION, definition, new HashSet<String>(), expectedCategories);

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

		DynamicRefactoringDefinition definition = XMLRefactoringUtils
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ REFACTORING_WITH_TWO_CLASSIFICATIONS + XML_EXTENSION); //$NON-NLS-1$
		
		// Comprobar categorias
		Set<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA1));
		expectedCategories.add(new Category(MI_CLASSIFICATION, MI_CATEGORIA2));
		expectedCategories.add(new Category(MI_CLASSIFICATION2, MI_CATEGORIA1));
		expectedCategories.add(new Category(MI_CLASSIFICATION2, MI_CATEGORIA2));

		assertMinimumInformationEqual(REFACTORING_WITH_TWO_CLASSIFICATIONS, definition, new HashSet<String>(), expectedCategories);

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

		DynamicRefactoringDefinition definition = XMLRefactoringUtils
				.getRefactoringDefinition(TESTDATA_XML_READER_DIR
						+ MINIMUM_INFORMATION_WITH_KEYWORDS + XML_EXTENSION); //$NON-NLS-1$
		
		// Comprobar categorias
		Set<String> expectedKeywords = new HashSet<String>();
		expectedKeywords.add(KEY_WORD1);
		expectedKeywords.add(KEY_WORD2);
		
		

		assertMinimumInformationEqual(MINIMUM_INFORMATION_WITH_KEYWORDS, definition, expectedKeywords, getMinimumInformationRefactCategories());

	}

	
	/**
	 * Obtiene las categorias de la refactorizacion con informacion minima.
	 * @return categorias de la refactorizacion con informacion minima
	 */
	private Set<Category> getMinimumInformationRefactCategories() {
		HashSet<Category> expectedCategories = new HashSet<Category>();
		expectedCategories.add(new Category("MiClasificacion","MiCategoria"));
		return expectedCategories;
	}

	/**
	 * Comprueba que la refactorizacion leida del xml es exactamente
	 * igual a la que se esperaba.
	 * 
	 * @param expectedName nombre esperado de la refactorizacion
	 * @param definition refactorizacion leida del xml
	 */
	private void assertMinimumInformationEqual(String expectedName, 
			DynamicRefactoringDefinition definition, Set<String> expectedKeyWords, Set<Category> expectedCategories) {
		
		
		List<RefactoringMechanismInstance> expectedPreconditions = new ArrayList<RefactoringMechanismInstance>();
		expectedPreconditions.add(new RefactoringMechanismInstance("ExistsClass", new ArrayList<String>(), RefactoringMechanismType.PRECONDITION));
		
		List<RefactoringMechanismInstance> expectedActions = new ArrayList<RefactoringMechanismInstance>();
		expectedActions.add(new RefactoringMechanismInstance("RenameClass", new ArrayList<String>(), RefactoringMechanismType.ACTION));
		
		List<RefactoringMechanismInstance> expectedPostConditions = new ArrayList<RefactoringMechanismInstance>();
		expectedPostConditions.add(new RefactoringMechanismInstance("ExistsClass", new ArrayList<String>(), RefactoringMechanismType.POSTCONDITION));
		
		final DynamicRefactoringDefinition expectedRefactoring = new DynamicRefactoringDefinition.Builder(expectedName)
					.description("Descripcion.")
					.motivation("Motivacion.")
					.keywords(expectedKeyWords)
					.categories(expectedCategories)
					.image("")
					.inputs(getMinimumInformationRefactInputs())
					.preconditions(expectedPreconditions)
					.actions(expectedActions)
					.postconditions(expectedPostConditions)
					.build();
		assertTrue(definition.exactlyEquals(expectedRefactoring));
	}

	/**
	 * Obtiene los parametros de entrada esperados para las refactorizaciones
	 * con informacion minima.
	 * 
	 * @return parametros de entrada esperados
	 */
	private List<InputParameter> getMinimumInformationRefactInputs() {
		List<InputParameter> expectedParameters = new ArrayList<InputParameter>();
		expectedParameters.add(new InputParameter.Builder("moon.core.Model").name("Model").build());
		expectedParameters.add(new InputParameter.Builder("moon.core.classdef.MethDec").name(METHOD).main(true).build());
		return expectedParameters;
	}

	/**
	 * Comprueba que la lectura se realiza correctamente cuando la definición
	 * contiene toda la informacion posible. Para ello se realiza la carga de la
	 * definicion de una refactorizacion desde un fichero XML y luego se
	 * comprueba el valor de todos los campos recuperados.
	 * 
	 * Esta informacion es: el nombre, la descripcion, la imagen, la motivacion,
	 * varias entradas, precondiciones, acciones, postcondiciones, parametros
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

		checkPredicateAsMechanism( new RefactoringMechanismInstance("NotExistsClassWithName", getPreconditionsParameters(), RefactoringMechanismType.PRECONDITION), mechanism.next());
		assertFalse(mechanism.hasNext());

		Iterator<RefactoringMechanismInstance> actions = definition.getActions().iterator();

		checkAction(definition, new RefactoringMechanismInstance("RenameClass", getActionsParameters(), RefactoringMechanismType.ACTION), actions.next());
		checkAction(definition, new RefactoringMechanismInstance("RenameReferenceFile", getActionsParameters(), RefactoringMechanismType.ACTION), actions.next());
		checkAction(definition, new RefactoringMechanismInstance("RenameClassType", getActionsParameters(), RefactoringMechanismType.ACTION) , actions.next());
		checkAction(definition, new RefactoringMechanismInstance("RenameGenericClassType", getActionsParameters(), RefactoringMechanismType.ACTION), actions.next());
		checkAction(definition, new RefactoringMechanismInstance("RenameConstructors", getActionsParameters(), RefactoringMechanismType.ACTION), actions.next());
		checkAction(definition, new RefactoringMechanismInstance("RenameJavaFile", getActionsParameters(), RefactoringMechanismType.ACTION), actions.next());

		assertFalse(actions.hasNext());

		Iterator<RefactoringMechanismInstance> postconditions = definition.getPostconditions()
				.iterator();
		checkPredicateAsMechanism( new RefactoringMechanismInstance("NotExistsClassWithName", getPostConditionsParameters(), POSTCONDITION),
				postconditions.next());

		assertFalse(postconditions.hasNext());

		Iterator<RefactoringExample> examples = definition.getExamples().iterator();
		RefactoringExample example = examples.next();
		assertEquals(example.getBefore(), "ejemplo1_antes.txt"); //$NON-NLS-1$
		assertEquals(example.getAfter(), "ejemplo1_despues.txt"); //$NON-NLS-1$

		assertFalse(examples.hasNext());
	}

	private void checkPredicateAsMechanism(RefactoringMechanismInstance expectedMechanism, RefactoringMechanismInstance pre) {
		assertEquals(expectedMechanism, pre); 
	}

	private void checkAction(DynamicRefactoringDefinition definition,
			RefactoringMechanismInstance expectedAction, RefactoringMechanismInstance action) {
		assertEquals(expectedAction, action); //$NON-NLS-1$
	}

}