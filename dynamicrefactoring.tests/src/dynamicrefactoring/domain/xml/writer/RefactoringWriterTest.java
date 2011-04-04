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

package dynamicrefactoring.domain.xml.writer;

import static dynamicrefactoring.domain.RefactoringMechanismType.ACTION;
import static dynamicrefactoring.domain.RefactoringMechanismType.POSTCONDITION;
import static dynamicrefactoring.domain.RefactoringMechanismType.PRECONDITION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.filter.ElementFilter;
import org.junit.Test;

import com.google.common.base.Preconditions;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.xml.RefactoringXMLTest;
import dynamicrefactoring.domain.xml.reader.TestCaseRefactoringReader;
/**
 * Comprueba que funciona correctamente el proceso de escritura de la definici�n
 * de una refactorizaci�n din�mica en un fichero XML.
 * 
 * Indirectamente, se comprueba tambi�n el funcionamiento de las clases que
 * implementan los patrones Bridge y Factory Method.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 */
public final class RefactoringWriterTest extends RefactoringXMLTest{

	private static final String RENAME_CLASS = "RenameClass";

	private static final String MOTIVACION = "Motivacion.";
	private static final String DESCRIPCION = "Descripcion.";
	/**
	 * Ruta al directorio en que se exportan las refactorizaciones.
	 */
	private static final String TESTDATA_XML_WRITER_DIR = "./testdata/XML/Writer/";

	/**
	 * Comprueba el lanzamiento de una excepci�n cuando hay un fallo en
	 * escritura.
	 * 
	 * @throws Exception
	 *             Excepci�n lanzada tras al aparici�n de un fallo en lectura.
	 */
	@Test(expected = dynamicrefactoring.domain.xml.writer.XMLRefactoringWriterException.class)
	public void testWritingException() throws Exception {
		DynamicRefactoringDefinition.Builder rd = createRefactoringDefinition(
				TestCaseRefactoringReader.MINIMUM_INFORMATION_REFACTORING,
				DESCRIPCION, MOTIVACION);

		rd.inputs(addSimpleInputs());
		rd.categories(new HashSet<Category>());
		rd.keywords(new HashSet<String>());
		rd.preconditions(new ArrayList<RefactoringMechanismInstance>());
		rd.actions(new ArrayList<RefactoringMechanismInstance>());
		rd.postconditions(new ArrayList<RefactoringMechanismInstance>());
		rd.motivation("");

		XMLRefactoringWriterFactory f = new JDOMXMLRefactoringWriterFactory();
		XMLRefactoringWriterImp implementor = f.makeXMLRefactoringWriterImp(rd
				.build());
		XMLRefactoringWriter writer = new XMLRefactoringWriter(implementor);
		writer.writeRefactoring(new File("./testdata/XML/NoExiste")); //$NON-NLS-1$

	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se a�ade la
	 * informaci�n m�nima necesaria. Para ello se da valor a los campos de un
	 * objeto de tipo DynamicRefactoringDefinition y luego se realiza la
	 * escritura; posteriormente se hace una comprobaci�n del contenido del
	 * fichero creado con el contenido que deber�a tener.
	 * 
	 * Esta informaci�n es: el nombre, la descripci�n, la motivaci�n, una
	 * entrada, una precondici�n, una acci�n y una postcondici�n; no tiene ni
	 * imagen, ni par�metros ambiguos ni ejemplos.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir la definici�n de la
	 *             refactorizaci�n.
	 */
	@Test
	public void testWritingWithMinimunInformation() throws Exception {
		HashSet<Category> categories = new HashSet<Category>();
		categories.add(new Category("MiClasificacion", "MiCategoria"));
		assertMinimumInformationDefinition(
				TestCaseRefactoringReader.MINIMUM_INFORMATION_REFACTORING,
				new HashSet<String>(), categories);
	}

	/**
	 * Similar a {@link #testWritingWithMinimunInformation} pero en este caso el
	 * fichero xml tiene palabras clave definidas por lo tanto tambien hay que
	 * comprobar que estas se escriben bien.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir la definici�n de la
	 *             refactorizaci�n.
	 */
	@Test
	public void testWritingWithMinimunInformationAndKeyWords() throws Exception {
		HashSet<Category> categories = new HashSet<Category>();
		categories.add(new Category("MiClasificacion", "MiCategoria"));
		Set<String> keywords = new HashSet<String>();
		keywords.add(TestCaseRefactoringReader.KEY_WORD1);
		keywords.add(TestCaseRefactoringReader.KEY_WORD2);
		assertMinimumInformationDefinition(
				TestCaseRefactoringReader.MINIMUM_INFORMATION_WITH_KEYWORDS,
				keywords, categories);
	}

	/**
	 * Similar a {@link #testWritingWithMinimumInformation} pero en este caso el
	 * fichero xml tiene categorias definidas por lo tanto tambien hay que
	 * comprobar que estas se escriben bien.
	 * 
	 * @throws IOException
	 * @throws XMLRefactoringWriterException
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir la definici�n de la
	 *             refactorizaci�n.
	 */
	@Test
	public void testWritingWithMinimunInformationAndCategories()
			throws XMLRefactoringWriterException, IOException {

		Set<Category> categories = new HashSet<Category>();
		categories.add(new Category(
				TestCaseRefactoringReader.MI_CLASSIFICATION,
				TestCaseRefactoringReader.MI_CATEGORIA1));
		categories.add(new Category(
				TestCaseRefactoringReader.MI_CLASSIFICATION,
				TestCaseRefactoringReader.MI_CATEGORIA2));
		assertMinimumInformationDefinition(
				TestCaseRefactoringReader.REFACTORING_WITH_CLASSIFICATION,
				new HashSet<String>(), categories);
	}

	/**
	 * Crea una refactorizacion dinamica con la informacion que le pasamos y
	 * cierta informacion que asume por defecto para las
	 * MinimumInformationDefinition y escribe la refactorizacion a un fichero
	 * comprobando que lo escrito es lo esperado.
	 * 
	 * @param refactoringName
	 *            nombre de la refactorizacion
	 * @param keywords
	 *            palabras clave de la refactorizacion
	 * @param categories
	 *            categorias de la refactorizacion
	 * @throws XMLRefactoringWriterException
	 * @throws IOException
	 * @throws Exception
	 *             si se produce un error al escribir la definici�n de la
	 *             refactorizaci�n.
	 */
	private void assertMinimumInformationDefinition(String refactoringName,
			Set<String> keywords, Set<Category> categories)
			throws XMLRefactoringWriterException, IOException {
		Preconditions.checkNotNull(keywords);
		Preconditions.checkNotNull(categories);
		Preconditions.checkNotNull(refactoringName);
		DynamicRefactoringDefinition.Builder rd = createRefactoringDefinition(
				refactoringName, DESCRIPCION, MOTIVACION);
		rd.inputs(addSimpleInputs());
		rd.categories(categories);
		rd.keywords(keywords);
		addSimplePredicates(rd);
		writeRefactoring(rd.build());
		assertTrue(FileUtils.contentEquals(new File(
				TestCaseRefactoringReader.TESTDATA_XML_READER_DIR
						+ refactoringName
						+ TestCaseRefactoringReader.XML_EXTENSION), //$NON-NLS-1$
				new File(TESTDATA_XML_WRITER_DIR + refactoringName
						+ TestCaseRefactoringReader.XML_EXTENSION))); //$NON-NLS-1$
	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se a�ade toda
	 * la informaci�n posible. Para ello se da valor a los campos de un objeto
	 * de tipo DynamicRefactoringDefinition y luego se realiza la escritura;
	 * posteriormente se hace una comprobaci�n del contenido del fichero creado
	 * con el contenido que deber�a tener.
	 * 
	 * Esta informaci�n es: el nombre, la descripci�n, la imagen, la motivaci�n,
	 * varias entradas, precondiciones, acciones, postcondiciones, par�metros
	 * ambiguos y ejemplos.
	 * 
	 * @throws Exception
	 *             si se produce un error durante la escritura de la definici�n
	 *             de la refactorizaci�n.
	 */
	@Test
	public void testWritingWithFullInformation() throws Exception {

		// A�adir informaci�n general
		DynamicRefactoringDefinition.Builder rd = createRefactoringDefinition(
				"FullInformation", "Renames the class.",
				"The name of class does not reveal its intention.");
		rd.image("renameclass.JPG"); //$NON-NLS-1$

		// A�adir entradas
		ArrayList<InputParameter> entradas = new ArrayList<InputParameter>();
		entradas.add(new InputParameter.Builder("moon.core.Name")
				.name("Old_name").from(CLASS).method("getName").main(false)
				.build());

		entradas.add(new InputParameter.Builder("moon.core.Model")
				.name("Model").from("").method("").main(false).build());

		entradas.add(new InputParameter.Builder("moon.core.classdef.ClassDef")
				.name(CLASS).from("").method("").main(true).build());

		entradas.add(new InputParameter.Builder("moon.core.Name")
				.name(NEW_NAME).from("").method("").main(false).build());

		rd.inputs(entradas);

		// a�adir precondiciones,acciones y postcondiciones
		ArrayList<RefactoringMechanismInstance> preconditions = new ArrayList<RefactoringMechanismInstance>();
		List<String> preconditionParameters = getPreconditionsParameters();
		preconditions.add(new RefactoringMechanismInstance("NotExistsClassWithName", preconditionParameters,  PRECONDITION)); //$NON-NLS-1$
		rd.preconditions(preconditions);

		ArrayList<RefactoringMechanismInstance> actions = new ArrayList<RefactoringMechanismInstance>();
		List<String> actionParameters = getActionsParameters();
		actions.add(new RefactoringMechanismInstance(RENAME_CLASS, actionParameters, ACTION)); //$NON-NLS-1$
		actions.add(new RefactoringMechanismInstance("RenameReferenceFile", actionParameters, ACTION)); //$NON-NLS-1$
		actions.add(new RefactoringMechanismInstance("RenameClassType", actionParameters, ACTION)); //$NON-NLS-1$
		actions.add(new RefactoringMechanismInstance("RenameGenericClassType", actionParameters, ACTION)); //$NON-NLS-1$
		actions.add(new RefactoringMechanismInstance("RenameConstructors", actionParameters, ACTION)); //$NON-NLS-1$
		actions.add(new RefactoringMechanismInstance("RenameJavaFile", actionParameters, ACTION)); //$NON-NLS-1$
		rd.actions(actions);

		ArrayList<RefactoringMechanismInstance> postconditions = new ArrayList<RefactoringMechanismInstance>();
		List<String> postConditionParameters = getPostConditionsParameters();
		postconditions.add(new RefactoringMechanismInstance("NotExistsClassWithName", postConditionParameters, POSTCONDITION)); //$NON-NLS-1$
		rd.postconditions(postconditions);

		// a�adiendo los ejemplos
		ArrayList<RefactoringExample> ejemplos = new ArrayList<RefactoringExample>();
		ejemplos.add(new RefactoringExample("ejemplo1_antes.txt","ejemplo1_despues.txt"));
		rd.examples(ejemplos);

		HashSet<Category> categories = new HashSet<Category>();
		categories.add(new Category("MiClasificacion", "MiCategoria"));
		rd.categories(categories);
		rd.keywords(new HashSet<String>());

		writeRefactoring(rd.build());

		assertTrue(FileUtils
				.contentEquals(
						new File(
								TestCaseRefactoringReader.TESTDATA_XML_READER_DIR
										+ "FullInformation.xml"), new File(TESTDATA_XML_WRITER_DIR + "FullInformation.xml"))); //$NON-NLS-1$

	}

	/**
	 * Comprueba que al exportar una refactorizacion en la que los ejemplos
	 * tienen rutas vacias no se generan etiquetas {@code <example>}.
	 * 
	 * @throws XMLRefactoringWriterException
	 *             si falla al exportar la refactorizacion
	 */
	@Test
	public void testNotGenerateEmptyExamples()
			throws XMLRefactoringWriterException {
		DynamicRefactoringDefinition.Builder builder = createRefactoringDefinition(
				"NotGenerateEmptyExamples", DESCRIPCION, MOTIVACION);

		// Agregamos las entradas
		builder.inputs(addSimpleInputs());

		// Agregamos un ejemplo
		List<RefactoringExample> ejemplos = new ArrayList<RefactoringExample>();
		ejemplos.add(new RefactoringExample("",""));
		builder.examples(ejemplos);

		builder.categories(new HashSet<Category>());
		builder.keywords(new HashSet<String>());

		addSimplePredicates(builder);

		XMLRefactoringWriterFactory f = new JDOMXMLRefactoringWriterFactory();
		XMLRefactoringWriterImp implementor = f
				.makeXMLRefactoringWriterImp(builder.build());

		Document refactoringDefinitionDoc = implementor
				.getDocumentOfRefactoring();

		// Comprobamos que no hay etiquetas example en el documento
		assertFalse(
				"Se han generado ejemplos con rutas vacias.",
				refactoringDefinitionDoc.getDescendants(
						new ElementFilter("example")).hasNext());

	}

	/**
	 * Escribe la refactorizacion sobre un fichero que se guardara en el
	 * directorio especificado en TESTDATA_XML_WRITER_DIR.
	 * 
	 * @param rd
	 *            definicion de la refactorizacion
	 * @throws XMLRefactoringWriterException
	 *             si se produce un error al escribir una refactorizaci�n.
	 */
	private void writeRefactoring(DynamicRefactoringDefinition rd)
			throws XMLRefactoringWriterException {
		XMLRefactoringWriterFactory f = new JDOMXMLRefactoringWriterFactory();
		XMLRefactoringWriterImp implementor = f.makeXMLRefactoringWriterImp(rd);
		XMLRefactoringWriter writer = new XMLRefactoringWriter(implementor);
		writer.writeRefactoring(new File(TESTDATA_XML_WRITER_DIR)); //$NON-NLS-1$
	}

	/**
	 * Crea una refactorizacion con su nombre, descripcion y motivacion.
	 * 
	 * @param name
	 *            Nombre de la refactorizacion
	 * @param description
	 *            Descripcion de la refactorizacion
	 * @param motivation
	 *            Motivacion de la refactorizacion
	 * 
	 * @return la refactorizacion creada
	 */
	private Builder createRefactoringDefinition(String name,
			String description, String motivation) {
		// A�adir informaci�n general
		return new DynamicRefactoringDefinition.Builder(name).description(
				description).motivation(motivation);
	}

	/**
	 * Crea un conjunto sencillo de entradas para las pruebas.
	 * 
	 * @return lista de entradas de prueba
	 */
	private List<InputParameter> addSimpleInputs() {
		ArrayList<InputParameter> entradas = new ArrayList<InputParameter>();
		entradas.add(new InputParameter.Builder("moon.core.Model")
				.name("Model").build());
		entradas.add(new InputParameter.Builder("moon.core.classdef.MethDec")
				.name("Method").main(true).build());
		return entradas;
	}

	/**
	 * Agrega un conjunto de precondiciones, acciones y poscondiciones de
	 * prueba.
	 * 
	 * @param rd
	 *            definicion de la refactorizacion
	 */
	private DynamicRefactoringDefinition addSimplePredicates(Builder builder) {
		// a�adir precondiciones,acciones y postcondiciones
		ArrayList<RefactoringMechanismInstance> preconditions = new ArrayList<RefactoringMechanismInstance>();
		preconditions.add(new RefactoringMechanismInstance("ExistsClass", new ArrayList<String>() ,PRECONDITION)); //$NON-NLS-1$
		builder.preconditions(preconditions);

		ArrayList<RefactoringMechanismInstance> actions = new ArrayList<RefactoringMechanismInstance>();
		actions.add(new RefactoringMechanismInstance(RENAME_CLASS, new ArrayList<String>(), ACTION)); //$NON-NLS-1$
		builder.actions(actions);

		ArrayList<RefactoringMechanismInstance> postconditions = new ArrayList<RefactoringMechanismInstance>();
		postconditions.add(new RefactoringMechanismInstance("ExistsClass", new ArrayList<String>() , POSTCONDITION)); //$NON-NLS-1$
		builder.postconditions(postconditions);

		return builder.build();
	}
}