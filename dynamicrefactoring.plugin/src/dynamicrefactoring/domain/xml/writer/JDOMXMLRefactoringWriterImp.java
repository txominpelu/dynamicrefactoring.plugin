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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.RefactoringMechanismType;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderException;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderImp;
import dynamicrefactoring.util.PluginStringUtils;

/**
 * Utiliza la implementaci�n basada en JDOM para escribir los ficheros XML de
 * refactorizaciones din�micas.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public final class JDOMXMLRefactoringWriterImp implements
		XMLRefactoringWriterImp {

	/**
	 * La definici�n de refactorizaci�n que se debe escribir.
	 */
	private DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * Constructor.
	 * 
	 * @param refactoringDefinition
	 *            definici�n de la refactorizaci�n que se debe escribir.
	 */
	public JDOMXMLRefactoringWriterImp(
			DynamicRefactoringDefinition refactoringDefinition) {

		this.refactoringDefinition = refactoringDefinition;
		initializeFormat();
	}

	/**
	 * Genera el documento de JDOM que representa la refactorizacion.
	 * 
	 * @return documento de jdom con la representacion de la refactorizacion
	 */
	@Override
	public Document getDocumentOfRefactoring() {
		Element refactoring = new Element(
				XMLRefactoringReaderImp.REFACTORING_TAG);

		DocType type = new DocType(refactoring.getName(), "refactoringDTD.dtd"); //$NON-NLS-1$

		refactoring.setAttribute("name", refactoringDefinition.getName()); //$NON-NLS-1$

		constructRefactoringInformation(refactoring);
		constructRefactoringInputs(refactoring);
		constructRefactoringMechanism(refactoring);
		constructRefactoringExamples(refactoring);

		return new Document(refactoring, type);
	}

	/**
	 * Cambia el nombre de una refactorinzaci�n en el fichero temporal que
	 * alamacena las refactorizaciones seg�n el ambito al que pertenece su
	 * entrada principal.
	 * 
	 * @param scope
	 *            ambito de la refactorizaci�n.
	 * @param newName
	 *            nuevo nombre.
	 * @param originalName
	 *            nombre anterior.
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 */
	public void renameRefactoringIntoXml(Scope scope, String newName,
			String originalName) throws XMLRefactoringReaderException {

		String path = RefactoringPlugin.getDynamicRefactoringsDir() + "/"
				+ newName + "/" + newName + ".xml";
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new File(
					RefactoringConstants.REFACTORING_TYPES_FILE).toURI()
					.toString());
			Element root = doc.getRootElement();

			// Renombramos el elemento
			Element elementDef = root.getChild(scope.getXmlTag());
			for (int i = 0; i < elementDef.getChildren().size(); i++) {
				Element refactor = (Element) elementDef.getChildren().get(i);
				if (refactor.getAttribute("name").getValue()
						.equals(originalName)) {
					refactor.getAttribute("name").setValue(newName);
					refactor.getAttribute("path").setValue(path);
					break;
				}
			}

			writeToFile(RefactoringConstants.REFACTORING_TYPES_FILE, doc);
		} catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception.getMessage());
		} catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception.getMessage());
		}
	}

	/**
	 * Escribe el fichero XML a partir de la definici�n de la refactorizaci�n.
	 * 
	 * @param dir
	 *            directorio donde se guardar� el fichero.
	 * 
	 * @throws XMLRefactoringWriterException
	 *             si se produce un error al intentar almacenar la definici�n de
	 *             la refactorizaci�n en el fichero.
	 */
	@Override
	public void writeRefactoring(File dir) throws XMLRefactoringWriterException {
		try {
			writeToFile(dir.getPath() + File.separatorChar
					+ refactoringDefinition.getName() + //$NON-NLS-1$
					RefactoringConstants.FILE_EXTENSION,
					getDocumentOfRefactoring());
		} catch (Exception e) {
			throw new XMLRefactoringWriterException(e.getMessage());
		}
	}

	/**
	 * Escribe los elementos con la informaci�n general de la refactorizaci�n.
	 * 
	 * <p>
	 * Almacena la descripci�n b�sica de la refactorizaci�n, la ruta de la
	 * imagen asociada a la refactorizaci�n y la motivaci�n de la misma.
	 * </p>
	 * 
	 * @param refactoringElement
	 *            el elemento XML ra�z a partir del cual se a�adir� el elemento
	 *            hijo con la informaci�n b�sica de la refactorizaci�n.
	 */
	private void constructRefactoringInformation(Element refactoringElement) {

		Element information = new Element(
				XMLRefactoringReaderImp.INFORMATION_ELEMENT);

		if (refactoringDefinition.getDescription().length() != 0) {
			Element description = new Element(
					XMLRefactoringReaderImp.DESCRIPTION_ELEMENT);
			description.setText(refactoringDefinition.getDescription());
			information.addContent(description);
		}

		// En el DTD se especifica que la imagen es requerida;
		// mejor que no lo sea.
		if (refactoringDefinition.getImage().length() != 0) {
			Element image = new Element(XMLRefactoringReaderImp.IMAGE_ELEMENT);
			image.setAttribute("src", refactoringDefinition.getImage()); //$NON-NLS-1$
			information.addContent(image);
		}

		if (refactoringDefinition.getMotivation().length() != 0) {
			Element motivation = new Element(
					XMLRefactoringReaderImp.MOTIVATION_ELEMENT);
			motivation.setText(refactoringDefinition.getMotivation());
			information.addContent(motivation);
		}

		// Agrega las palabras clave que describen la refactorizacion
		if (refactoringDefinition.getKeywords().size() > 0) {
			Element keywordsElement = createKeywordsElement();
			information.addContent(keywordsElement);

		}

		// Agrega las categorias a las que pertence la refactorizacion
		if (refactoringDefinition.getCategories().size() > 0) {
			Element categorizationElement = createCategorizationElement(createCategoriesMapByParent());
			information.addContent(categorizationElement);

		}

		refactoringElement.addContent(information);
	}

	/**
	 * Crea el elemento xml con la lista de palabras clave de la
	 * refactorizacion.
	 * 
	 * @return elemento xml con la lista de palabras clave de la refactorizacion
	 */
	private Element createKeywordsElement() {
		Element keywordsElement = new Element(
				XMLRefactoringReaderImp.KEYWORDS_ELEMENT);
		for (String keyword : refactoringDefinition.getKeywords()) {

			Element keywordElem = new Element(
					XMLRefactoringReaderImp.KEYWORD_ELEMENT);
			keywordElem.setText(keyword);
			keywordsElement.addContent(keywordElem);
		}
		return keywordsElement;
	}

	/**
	 * Crea el elemento categorization que contiene las categorias a las que
	 * pertenece una refactorizacion dentro de las clasificaciones existentes.
	 * 
	 * @param categoriesMapByParent
	 *            mapa que contiene como claves los nombres de las
	 *            clasificaciones y como valores las categorias a las que la
	 *            refactorizacion pertenece en esa clasificacion
	 * @return elemento de jdom con la categorizacion de la refactorizacion
	 */
	private Element createCategorizationElement(
			ImmutableListMultimap<String, Category> categoriesMapByParent) {
		Element categorizationElement = new Element(
				XMLRefactoringReaderImp.CATEGORIZATION_ELEMENT);
		for (String classificationName : categoriesMapByParent.keySet()) {

			Element classificationElement = new Element(
					XMLRefactoringReaderImp.CLASSIFICATION_ELEMENT);
			classificationElement.setAttribute(
					XMLRefactoringReaderImp.CLASSIFICATION_NAME_ATTRIBUTE,
					classificationName);
			for (Category c : categoriesMapByParent.get(classificationName)) {
				Element categoryElement = new Element(
						XMLRefactoringReaderImp.CATEGORY_ELEMENT);
				categoryElement.addContent(c.getName());
				classificationElement.addContent(categoryElement);
			}
			categorizationElement.addContent(classificationElement);
		}
		return categorizationElement;
	}

	/**
	 * Crea un mapa que contiene como claves los nombres de las clasificaciones
	 * y como valores las categorias a las que la refactorizacion pertenece en
	 * esa clasificacion.
	 * 
	 * @return mapa que contiene como claves los nombres de las clasificaciones
	 *         y como valores las categorias a las que la refactorizacion
	 *         pertenece en esa clasificacion
	 */
	private ImmutableListMultimap<String, Category> createCategoriesMapByParent() {
		Builder<String, Category> builder = new ImmutableListMultimap.Builder<String, Category>();
		for (Category c : refactoringDefinition.getCategories()) {
			builder.put(c.getParent(), c);
		}
		ImmutableListMultimap<String, Category> categoriesMapByParent = builder
				.build();
		return categoriesMapByParent;
	}

	/**
	 * Escribe los elementos de las entradas de la refactorizaci�n.
	 * 
	 * @param refactoringElement
	 *            el elemento XML ra�z a partir del cual se a�adir� el elemento
	 *            hijo con la informaci�n acerca de las entradas de la
	 *            refactorizaci�n.
	 */
	private void constructRefactoringInputs(Element refactoringElement) {

		Element inputs = new Element(XMLRefactoringReaderImp.INPUTS_ELEMENT);

		for (InputParameter inputDefinition : refactoringDefinition.getInputs()) {
			Element input = new Element(XMLRefactoringReaderImp.INPUT_ELEMENT);
			input.setAttribute(XMLRefactoringReaderImp.TYPE_INPUT_ATTRIBUTE,
					inputDefinition.getType());
			if (inputDefinition.getName().length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.NAME_INPUT_ATTRIBUTE,
						inputDefinition.getName());
			if (inputDefinition.getFrom() != null
					&& inputDefinition.getFrom().length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.FROM_INPUT_ATTRIBUTE,
						inputDefinition.getFrom());
			if (inputDefinition.getMethod() != null
					&& inputDefinition.getMethod().length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.METHOD_INPUT_ATTRIBUTE,
						inputDefinition.getMethod());
			input.setAttribute(XMLRefactoringReaderImp.ROOT_INPUT_ATTRIBUTE,
					String.valueOf(inputDefinition.isMain()));
			inputs.addContent(input);
		}

		refactoringElement.addContent(inputs);
	}

	/**
	 * Escribe los elementos correspondientes a los predicados y acciones que
	 * componen la refactorizaci�n.
	 * 
	 * @param refactoringElement
	 *            el elemento XML ra�z a partir del cual se a�adir� el elemento
	 *            hijo con la informaci�n acerca de los predicados y las
	 *            acciones que componen la refactorizaci�n.
	 */
	private void constructRefactoringMechanism(Element refactoringElement) {

		Element mechanism = new Element(
				XMLRefactoringReaderImp.MECHANISM_ELEMENT);

		mechanism.addContent(createMechanismElement(
				RefactoringMechanismType.PRECONDITION,
				XMLRefactoringReaderImp.PRECONDITIONS_ELEMENT,
				XMLRefactoringReaderImp.PRECONDITION_ELEMENT,
				refactoringDefinition
								.getPreconditions()));

		mechanism.addContent(createMechanismElement(
				RefactoringMechanismType.ACTION,
				XMLRefactoringReaderImp.ACTIONS_ELEMENT,
				XMLRefactoringReaderImp.ACTION_ELEMENT,
				refactoringDefinition
								.getActions()));

		mechanism.addContent(createMechanismElement(
				RefactoringMechanismType.POSTCONDITION,
				XMLRefactoringReaderImp.POSTCONDITIONS_ELEMENT,
				XMLRefactoringReaderImp.POSTCONDITION_ELEMENT,
				refactoringDefinition
								.getPostconditions()));

		refactoringElement.addContent(mechanism);
	}

	/**
	 * Crea el mecanismo de una refactorizacion.
	 * 
	 * @param type
	 * @param parentTagName
	 * @param childTagName
	 * @param mechanismElements
	 * @return
	 */
	private Element createMechanismElement(RefactoringMechanismType type,
			String parentTagName, String childTagName,
			List<RefactoringMechanismInstance> mechanismElements) {
		Element mechanismElement = new Element(parentTagName);

		for (final RefactoringMechanismInstance mechanism : mechanismElements) {
			final Element childElement = new Element(childTagName);
			childElement.setAttribute(XMLRefactoringReaderImp.NAME_ATTRIBUTE,
					PluginStringUtils.getMechanismFullyQualifiedName(type,mechanism.getClassName()));
			constructAmbiguousParameters(childElement, mechanism,
					type);
			mechanismElement.addContent(childElement);
		}
		return mechanismElement;
	}

	/**
	 * Escribe los elementos de los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @param partOfRefactoring
	 *            el elemento de par�metros ambiguos.
	 * @param nameOfPart
	 *            el elemento del nombre del par�metro ambiguo.
	 * @param typeOfPart
	 *            el elemento del tipo del par�metro ambiguo.
	 */
	private void constructAmbiguousParameters(Element partOfRefactoring,
			RefactoringMechanismInstance nameOfPart, RefactoringMechanismType typeOfPart) {

		for (String ambiguousParameter : nameOfPart.getInputParameters()) {
			Element param = new Element(XMLRefactoringReaderImp.PARAM_ELEMENT);
			param.setAttribute(XMLRefactoringReaderImp.NAME_PARAM_ATTRIBUTE,
					ambiguousParameter);
			partOfRefactoring.addContent(param);
		}
	}

	/**
	 * Escribe los elementos de los ejemplos de la refactorizaci�n.
	 * 
	 * @param refactoringElement
	 *            el elemento XML ra�z a partir del cual se a�adir� el elemento
	 *            hijo con la informaci�n acerca de los ejemplos asociados a la
	 *            refactorizaci�n.
	 */
	private void constructRefactoringExamples(Element refactoringElement) {

		Element examples = new Element(XMLRefactoringReaderImp.EXAMPLES_ELEMENT);

		for (RefactoringExample ex : refactoringDefinition.getExamples()) {
			if (!(ex.getBefore().isEmpty() && ex.getAfter().isEmpty())) {
				Element example = new Element(
						XMLRefactoringReaderImp.EXAMPLE_ELEMENT);
				example.setAttribute(
						XMLRefactoringReaderImp.BEFORE_EXAMPLE_ATTRIBUTE,
						ex.getBefore());
				example.setAttribute(
						XMLRefactoringReaderImp.AFTER_EXAMPLE_ATTRIBUTE,
						ex.getAfter());
				examples.addContent(example);
			}
		}
		refactoringElement.addContent(examples);
	}

	/**
	 * Escribe los datos del documento XML en el fichero indicado.
	 * 
	 * @param fname
	 *            el nombre del fichero.
	 * @param doc
	 *            el documento XML.
	 * 
	 * @throws IOException
	 *             si se produce un error de lectura escritura al trasladar los
	 *             datos del documento XML al fichero.
	 */
	private static void writeToFile(String fname, Document doc)
			throws IOException {

		FileOutputStream out = new FileOutputStream(fname);
		XMLOutputter op = new XMLOutputter(initializeFormat());

		op.output(doc, out);
		out.flush();
		out.close();
	}

	/**
	 * Inicializa las opciones de formato del fichero XML.
	 * 
	 * @return
	 */
	private static Format initializeFormat() {
		Format format = Format.getPrettyFormat();
		format.setIndent("\t"); //$NON-NLS-1$
		format.setLineSeparator("\n"); //$NON-NLS-1$
		format.setExpandEmptyElements(false);
		format.setEncoding("ISO-8859-1"); //$NON-NLS-1$
		return format;
	}

}