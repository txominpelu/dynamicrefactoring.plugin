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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import dynamicrefactoring.domain.RefactoringMechanism;
import dynamicrefactoring.domain.metadata.interfaces.Category;
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
public class RefactoringWriterTest {

	private static final String CLASS = "Class";
	private static final String NEW_NAME = "New_name";
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

		// A�adir entradas
		ArrayList<String[]> entradas = new ArrayList<String[]>();
		String entrada[] = new String[5];
		entrada[0] = "moon.core.Model"; //$NON-NLS-1$
		entrada[1] = "Model"; //$NON-NLS-1$
		entrada[2] = ""; //$NON-NLS-1$
		entrada[3] = ""; //$NON-NLS-1$
		entrada[4] = "false"; //$NON-NLS-1$

		entradas.add(entrada);

		rd.inputs(addSimpleInputs());
		rd.categories(new HashSet<Category>());
		rd.keywords(new HashSet<String>());
		rd.preconditions(new ArrayList<String>());
		rd.actions(new ArrayList<String>());
		rd.postconditions(new ArrayList<String>());
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
		ArrayList<String> preconditions = new ArrayList<String>();
		preconditions.add("NotExistsClassWithName (1)"); //$NON-NLS-1$
		rd.preconditions(preconditions);

		ArrayList<String> actions = new ArrayList<String>();
		actions.add("RenameClass (1)"); //$NON-NLS-1$
		actions.add("RenameReferenceFile (1)"); //$NON-NLS-1$
		actions.add("RenameClassType (1)"); //$NON-NLS-1$
		actions.add("RenameGenericClassType (1)"); //$NON-NLS-1$
		actions.add("RenameConstructors (1)"); //$NON-NLS-1$
		actions.add("RenameJavaFile (1)"); //$NON-NLS-1$
		rd.actions(actions);

		ArrayList<String> postconditions = new ArrayList<String>();
		postconditions.add("NotExistsClassWithName (1)"); //$NON-NLS-1$
		rd.postconditions(postconditions);

		// A�adiendo los par�metros ambiguos.
		@SuppressWarnings({ "unchecked" })//$NON-NLS-1$
		HashMap<String, List<String[]>>[] map = (HashMap<String, List<String[]>>[]) new HashMap[3];

		map[RefactoringMechanism.PRECONDITION.ordinal()] = new HashMap<String, List<String[]>>();
		map[RefactoringMechanism.ACTION.ordinal()] = new HashMap<String, List<String[]>>();
		map[RefactoringMechanism.POSTCONDITION.ordinal()] = new HashMap<String, List<String[]>>();

		List<String[]> ambiguous1 = new ArrayList<String[]>();
		String[] amb1 = new String[1];
		amb1[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous1.add(amb1);
		map[RefactoringMechanism.PRECONDITION.ordinal()].put(
				"NotExistsClassWithName (1)", ambiguous1); //$NON-NLS-1$

		List<String[]> ambiguous2 = new ArrayList<String[]>();
		String[] amb2a = new String[1];
		amb2a[0] = CLASS; //$NON-NLS-1$
		String[] amb2b = new String[1];
		amb2b[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous2.add(amb2a);
		ambiguous2.add(amb2b);
		map[RefactoringMechanism.ACTION.ordinal()].put(
				"RenameClass (1)", ambiguous2); //$NON-NLS-1$

		List<String[]> ambiguous3 = new ArrayList<String[]>();
		String[] amb3a = new String[1];
		amb3a[0] = CLASS; //$NON-NLS-1$
		String[] amb3b = new String[1];
		amb3b[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous3.add(amb3a);
		ambiguous3.add(amb3b);
		map[RefactoringMechanism.ACTION.ordinal()].put(
				"RenameReferenceFile (1)", ambiguous3); //$NON-NLS-1$

		List<String[]> ambiguous4 = new ArrayList<String[]>();
		String[] amb4a = new String[1];
		amb4a[0] = CLASS; //$NON-NLS-1$
		String[] amb4b = new String[1];
		amb4b[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous4.add(amb4a);
		ambiguous4.add(amb4b);
		map[RefactoringMechanism.ACTION.ordinal()].put(
				"RenameClassType (1)", ambiguous4); //$NON-NLS-1$

		List<String[]> ambiguous5 = new ArrayList<String[]>();
		String[] amb5a = new String[1];
		amb5a[0] = CLASS; //$NON-NLS-1$
		String[] amb5b = new String[1];
		amb5b[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous5.add(amb5a);
		ambiguous5.add(amb5b);
		map[RefactoringMechanism.ACTION.ordinal()].put(
				"RenameGenericClassType (1)", ambiguous5); //$NON-NLS-1$

		List<String[]> ambiguous6 = new ArrayList<String[]>();
		String[] amb6a = new String[1];
		amb6a[0] = CLASS; //$NON-NLS-1$
		String[] amb6b = new String[1];
		amb6b[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous6.add(amb6a);
		ambiguous6.add(amb6b);
		map[RefactoringMechanism.ACTION.ordinal()].put(
				"RenameConstructors (1)", ambiguous6); //$NON-NLS-1$

		List<String[]> ambiguous7 = new ArrayList<String[]>();
		String[] amb7a = new String[1];
		amb7a[0] = CLASS; //$NON-NLS-1$
		String[] amb7b = new String[1];
		amb7b[0] = NEW_NAME; //$NON-NLS-1$
		ambiguous7.add(amb7a);
		ambiguous7.add(amb7b);
		map[RefactoringMechanism.ACTION.ordinal()].put(
				"RenameJavaFile (1)", ambiguous7); //$NON-NLS-1$

		List<String[]> ambiguous8 = new ArrayList<String[]>();
		String[] amb8 = new String[1];
		amb8[0] = "Old_name"; //$NON-NLS-1$
		ambiguous8.add(amb8);
		map[RefactoringMechanism.POSTCONDITION.ordinal()].put(
				"NotExistsClassWithName (1)", ambiguous8); //$NON-NLS-1$

		rd.ambiguousParameters(map);

		// a�adiendo los ejemplos
		ArrayList<String[]> ejemplos = new ArrayList<String[]>();
		String ejemplo1[] = new String[2];
		ejemplo1[0] = "ejemplo1_antes.txt"; //$NON-NLS-1$
		ejemplo1[1] = "ejemplo1_despues.txt"; //$NON-NLS-1$
		ejemplos.add(ejemplo1);
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
		ArrayList<String[]> ejemplos = new ArrayList<String[]>();
		ejemplos.add(new String[] { "", "" });
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
		ArrayList<String> preconditions = new ArrayList<String>();
		preconditions.add("ExistsClass (1)"); //$NON-NLS-1$
		builder.preconditions(preconditions);

		ArrayList<String> actions = new ArrayList<String>();
		actions.add("RenameClass (1)"); //$NON-NLS-1$
		builder.actions(actions);

		ArrayList<String> postconditions = new ArrayList<String>();
		postconditions.add("ExistsClass (1)"); //$NON-NLS-1$
		builder.postconditions(postconditions);

		return builder.build();
	}
}