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

package dynamicrefactoring.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.dynamic.RepositoryElementProcessor;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.reader.XMLRefactoringReaderImp;
import dynamicrefactoring.util.ScopeLimitedLister;

/**
 * Utiliza la implementación basada en JDOM para escribir los ficheros XML de
 * refactorizaciones dinámicas.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JDOMXMLRefactoringWriterImp implements XMLRefactoringWriterImp {

	/**
	 * La definición de refactorización que se debe escribir.
	 */
	private DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * El formato del fichero XML.
	 */
	private Format format;

	/**
	 * Constructor.
	 * 
	 * @param refactoringDefinition
	 *            definición de la refactorización que se debe escribir.
	 */
	public JDOMXMLRefactoringWriterImp(
			DynamicRefactoringDefinition refactoringDefinition) {

		this.refactoringDefinition = refactoringDefinition;
		initializeFormat();
	}

	/**
	 * Escribe el fichero temporal que guarda las refactorizaciones disponibles
	 * para los diferentes tipos de entrada pricipal de la refactorización
	 * posibles.
	 */
	public void writeFileToLoadRefactoringTypes() {
		try {
			Element refactoring;

			Element root = new Element("dynamicRefactorings");

			DocType type = new DocType(root.getName(), "refactoringsDTD.dtd"); //$NON-NLS-1$

			for (Scope scope : Scope.values()) {
				if (scope != Scope.BOUNDED_PAR) {
					addRefactoringElementForScopeToRoot(root, scope);
				}
			}

			Document newdoc = new Document(root, type);

			writeToFile(RefactoringConstants.REFACTORING_TYPES_FILE, newdoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea la etiqueta con las refactorizaciones disponibles para el ambito
	 * pasado y los agrega al elemento root.
	 * 
	 * @param root
	 *            elemento root
	 * @param scope
	 *            ambito del que se va a generar la etiqueta.
	 */
	private void addRefactoringElementForScopeToRoot(Element root, Scope scope) {
		Element refactoring;
		Element classdef = new Element(scope.getXmlTag());

		// añdimos las refactorizaciones dinámicas al grupo de classdef
		for (Map.Entry<String, String> n_refactoring : ScopeLimitedLister
				.getAvailableRefactorings(scope).entrySet()) {
			refactoring = new Element("refactoring");
			refactoring.setAttribute("name", n_refactoring.getKey());
			refactoring.setAttribute("path", n_refactoring.getValue());
			classdef.addContent(refactoring);
		}
		root.addContent(classdef);
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
	 * Elimina una refactoriazción dentro del fichero refactorings.xml.
	 * 
	 * @param scope
	 *            ámbito de la refactorización.
	 * @param name
	 *            Nombre de la refactorización.
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 */
	public void deleteRefactoringFromXml(Scope scope, String name)
			throws XMLRefactoringReaderException {
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new File(
					RefactoringConstants.REFACTORING_TYPES_FILE).toURI()
					.toString());
			removeScopeTag(name, scope, doc);
			writeToFile(RefactoringConstants.REFACTORING_TYPES_FILE, doc);
		} catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception.getMessage());
		} catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception.getMessage());
		}
	}

	/**
	 * Elimina del elemento raiz del fichero de refactorizaciones disponibles
	 * representado por el argumento doc la refactorizacion con el nombre y el
	 * ambito dados.
	 * 
	 * @param refactoringName
	 *            nombre de la refactorizacion a borrar
	 * @param refactoringScope
	 *            ambito de la refactorizacion a borrar
	 * @param doc
	 *            documento de JDOM que representa el fichero de
	 *            refactorizaciones disponibles
	 */
	private void removeScopeTag(String refactoringName, Scope refactoringScope,
			Document doc) {
		Element classdef = doc.getRootElement().getChild(
				refactoringScope.getXmlTag());
		for (int i = 0; i < classdef.getChildren().size(); i++) {
			Element refactor = (Element) classdef.getChildren().get(i);
			if (refactor.getAttribute("name").getValue()
					.equals(refactoringName)) {
				classdef.removeContent(refactor);
				break;
			}
		}
	}

	/**
	 * Cambia el nombre de una refactorinzación en el fichero temporal que
	 * alamacena las refactorizaciones según el ambito al que pertenece su
	 * entrada principal.
	 * 
	 * @param scope
	 *            ambito de la refactorización.
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
	 * Añade una línea dentro del fichero refactorings.xml.
	 * 
	 * @param scope
	 *            ámbito de la refactorización.
	 * @param name
	 *            Nombre de la refactorización.
	 * @param path
	 *            Nombre de la ruta del fichero de definición de la
	 *            refactorización.
	 */
	public void addNewRefactoringToXml(Scope scope, String name, String path) {
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			// El atributo SYSTEM del DOCTYPE de la definición XML de la
			// refactorización es solo la parte relativa de la ruta del fichero
			// DTD. Se le antepone la ruta del directorio del plugin que
			// contiene los ficheros de refactorizaciones dinámicas.
			Document doc = builder.build(new File(
					RefactoringConstants.REFACTORING_TYPES_FILE).toURI()
					.toString());
			Element root = doc.getRootElement();
			Element refactoring = new Element("refactoring");
			refactoring.setAttribute("name", name);
			refactoring.setAttribute("path", path);
			root.getChild(scope.getXmlTag()).addContent(refactoring);
			writeToFile(RefactoringConstants.REFACTORING_TYPES_FILE, doc);
		} catch (JDOMException jdomexception) {

		} catch (IOException ioexception) {

		}
	}

	/**
	 * Escribe el fichero XML a partir de la definición de la refactorización.
	 * 
	 * @param dir
	 *            directorio donde se guardará el fichero.
	 * 
	 * @throws XMLRefactoringWriterException
	 *             si se produce un error al intentar almacenar la definición de
	 *             la refactorización en el fichero.
	 */
	@Override
	public void writeRefactoring(File dir) throws XMLRefactoringWriterException {
		try {
			writeToFile(
					dir.getPath()
							+ System.getProperty("file.separator") + refactoringDefinition.getName() + //$NON-NLS-1$
							RefactoringConstants.FILE_EXTENSION,
					getDocumentOfRefactoring());
		} catch (Exception e) {
			throw new XMLRefactoringWriterException(e.getMessage());
		}
	}

	/**
	 * Escribe los elementos con la información general de la refactorización.
	 * 
	 * <p>
	 * Almacena la descripción básica de la refactorización, la ruta de la
	 * imagen asociada a la refactorización y la motivación de la misma.
	 * </p>
	 * 
	 * @param refactoringElement
	 *            el elemento XML raíz a partir del cual se añadirá el elemento
	 *            hijo con la información básica de la refactorización.
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
		if (refactoringDefinition.getKeywords().size() > 0){
			Element keywordsElement = createKeywordsElement();
			information.addContent(keywordsElement);

		}
		
		// Agrega las categorias a las que pertence la refactorizacion
		if (refactoringDefinition.getCategories().size() > 0){
			Element categorizationElement = createCategorizationElement(createCategoriesMapByParent());
			information.addContent(categorizationElement);

		}

		refactoringElement.addContent(information);
	}

	/**
<<<<<<< HEAD
=======
	 * Crea el elemento xml con la lista de palabras
	 * clave de la refactorizacion.
	 * 
	 * @return elemento xml con la lista de palabras
	 * 	clave de la refactorizacion
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
>>>>>>> caso-76
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
	 * Crea un mapa que contiene como claves los nombres de las
	 * clasificaciones y como valores las categorias a las que la
	 * refactorizacion pertenece en esa clasificacion.
	 * 
	 * @return mapa que contiene como claves los nombres de las
	 *            clasificaciones y como valores las categorias a las que la
	 *            refactorizacion pertenece en esa clasificacion
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
	 * Escribe los elementos de las entradas de la refactorización.
	 * 
	 * @param refactoringElement
	 *            el elemento XML raíz a partir del cual se añadirá el elemento
	 *            hijo con la información acerca de las entradas de la
	 *            refactorización.
	 */
	private void constructRefactoringInputs(Element refactoringElement) {

		Element inputs = new Element(XMLRefactoringReaderImp.INPUTS_ELEMENT);

		for (String[] inputDefinition : refactoringDefinition.getInputs()) {
			Element input = new Element(XMLRefactoringReaderImp.INPUT_ELEMENT);
			input.setAttribute(XMLRefactoringReaderImp.TYPE_INPUT_ATTRIBUTE,
					inputDefinition[0]);
			if (inputDefinition[1].length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.NAME_INPUT_ATTRIBUTE,
						inputDefinition[1]);
			if (inputDefinition[2].length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.FROM_INPUT_ATTRIBUTE,
						inputDefinition[2]);
			if (inputDefinition[3].length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.METHOD_INPUT_ATTRIBUTE,
						inputDefinition[3]);
			if (inputDefinition[4].length() != 0)
				input.setAttribute(
						XMLRefactoringReaderImp.ROOT_INPUT_ATTRIBUTE,
						inputDefinition[4]);
			inputs.addContent(input);
		}

		refactoringElement.addContent(inputs);
	}

	/**
	 * Escribe los elementos correspondientes a los predicados y acciones que
	 * componen la refactorización.
	 * 
	 * @param refactoringElement
	 *            el elemento XML raíz a partir del cual se añadirá el elemento
	 *            hijo con la información acerca de los predicados y las
	 *            acciones que componen la refactorización.
	 */
	private void constructRefactoringMechanism(Element refactoringElement) {

		Element mechanism = new Element(
				XMLRefactoringReaderImp.MECHANISM_ELEMENT);

		Element preconditions = new Element(
				XMLRefactoringReaderImp.PRECONDITIONS_ELEMENT);

		for (String pre : refactoringDefinition.getPreconditions()) {
			Element precondition = new Element(
					XMLRefactoringReaderImp.PRECONDITION_ELEMENT);

			// Comprueba cual es el paquete en donde se encuentra la
			// precondición
			String preconditionPack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isPredicateJavaDependent(pre
					.substring(0, pre.length() - 4)))
				preconditionPack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else
				preconditionPack = RefactoringConstants.PREDICATES_PACKAGE;
			// con esta parte de la instrucción: pre.substring(0,pre.length()-4)
			// lo que hacemos
			// es quitar el número de la precondicion para que pueda ser
			// interpretado por
			// el motor de las refactorizaciones.
			precondition.setAttribute(XMLRefactoringReaderImp.NAME_ATTRIBUTE,
					preconditionPack + pre.substring(0, pre.length() - 4));
			constructAmbiguousParameters(precondition, pre, 0);
			preconditions.addContent(precondition);
		}
		mechanism.addContent(preconditions);

		Element actions = new Element(XMLRefactoringReaderImp.ACTIONS_ELEMENT);

		for (String act : refactoringDefinition.getActions()) {
			Element action = new Element(XMLRefactoringReaderImp.ACTION_ELEMENT);

			// Comprueba cual es el paquete en donde se encuentra la
			// precondición
			String actionPack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isActionJavaDependent(act.substring(
					0, act.length() - 4)))
				actionPack = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
			else
				actionPack = RefactoringConstants.ACTIONS_PACKAGE;

			// con esta parte de la instrucción: act.substring(0,act.length()-4)
			// lo que hacemos
			// es quitar el número de la acción para que pueda ser interpretado
			// por
			// el motor de las refactorizaciones.
			action.setAttribute(XMLRefactoringReaderImp.NAME_ATTRIBUTE,
					actionPack + act.substring(0, act.length() - 4));
			constructAmbiguousParameters(action, act, 1);
			actions.addContent(action);
		}
		mechanism.addContent(actions);

		Element postconditions = new Element(
				XMLRefactoringReaderImp.POSTCONDITIONS_ELEMENT);

		for (String post : refactoringDefinition.getPostconditions()) {
			Element postcondition = new Element(
					XMLRefactoringReaderImp.POSTCONDITION_ELEMENT);

			// Comprueba cual es el paquete en donde se encuentra la
			// postcondición
			String postconditionPack = ""; //$NON-NLS-1$
			if (RepositoryElementProcessor.isPredicateJavaDependent(post
					.substring(0, post.length() - 4)))
				postconditionPack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else
				postconditionPack = RefactoringConstants.PREDICATES_PACKAGE;

			// con esta parte de la instrucción:
			// post.substring(0,post.length()-4) lo que hacemos
			// es quitar el número de la postcondición para que pueda ser
			// interpretado por
			// el motor de las refactorizaciones.
			postcondition.setAttribute(XMLRefactoringReaderImp.NAME_ATTRIBUTE,
					postconditionPack + post.substring(0, post.length() - 4));
			constructAmbiguousParameters(postcondition, post, 2);
			postconditions.addContent(postcondition);
		}
		mechanism.addContent(postconditions);

		refactoringElement.addContent(mechanism);
	}

	/**
	 * Escribe los elementos de los parámetros ambiguos de la refactorización.
	 * 
	 * @param partOfRefactoring
	 *            el elemento de parámetros ambiguos.
	 * @param nameOfPart
	 *            el elemento del nombre del parámetro ambiguo.
	 * @param typeOfPart
	 *            el elemento del tipo del parámetro ambiguo.
	 */
	private void constructAmbiguousParameters(Element partOfRefactoring,
			String nameOfPart, int typeOfPart) {

		ArrayList<String[]> ambiguousParameters = refactoringDefinition
				.getAmbiguousParameters(nameOfPart, typeOfPart);

		if (ambiguousParameters != null) {
			for (String[] ambiguousParameter : ambiguousParameters) {
				Element param = new Element(
						XMLRefactoringReaderImp.PARAM_ELEMENT);
				param.setAttribute(
						XMLRefactoringReaderImp.NAME_PARAM_ATTRIBUTE,
						ambiguousParameter[0]);
				partOfRefactoring.addContent(param);
			}
		}
	}

	/**
	 * Escribe los elementos de los ejemplos de la refactorización.
	 * 
	 * @param refactoringElement
	 *            el elemento XML raíz a partir del cual se añadirá el elemento
	 *            hijo con la información acerca de los ejemplos asociados a la
	 *            refactorización.
	 */
	private void constructRefactoringExamples(Element refactoringElement) {

		Element examples = new Element(XMLRefactoringReaderImp.EXAMPLES_ELEMENT);

		for (String[] ex : refactoringDefinition.getExamples()) {
			if (!(ex[0].isEmpty() && ex[1].isEmpty())) {
				Element example = new Element(
						XMLRefactoringReaderImp.EXAMPLE_ELEMENT);
				example.setAttribute(
						XMLRefactoringReaderImp.BEFORE_EXAMPLE_ATTRIBUTE, ex[0]);
				example.setAttribute(
						XMLRefactoringReaderImp.AFTER_EXAMPLE_ATTRIBUTE, ex[1]);
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
	private void writeToFile(String fname, Document doc) throws IOException {

		FileOutputStream out = new FileOutputStream(fname);
		XMLOutputter op = new XMLOutputter(format);

		op.output(doc, out);
		out.flush();
		out.close();
	}

	/**
	 * Inicializa las opciones de formato del fichero XML.
	 */
	private void initializeFormat() {
		format = Format.getPrettyFormat();
		format.setIndent("\t"); //$NON-NLS-1$
		format.setLineSeparator("\n"); //$NON-NLS-1$
		format.setExpandEmptyElements(false);
		format.setEncoding("ISO-8859-1"); //$NON-NLS-1$
	}

	/**
	 * Devuelve el formato del fichero XML.
	 * 
	 * @return el formato del fichero XML.
	 * 
	 * @see #setFormat
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Asigna un formato al fichero XML.
	 * 
	 * @param format
	 *            el formato que se debe asignar al fichero XML.
	 * 
	 * @see #getFormat
	 */
	public void setFormat(Format format) {
		this.format = format;
	}
}