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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.RefactoringMechanismType;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.interfaces.Category;

/**
 * Utiliza la implementaci�n basada en JDOM para leer los ficheros XML que
 * definen refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JDOMXMLRefactoringReaderImp implements XMLRefactoringReaderImp {


	/**
	 * Lee cada uno de los componentes de la refactorizaci�n a partir de la
	 * definici�n contenida en el fichero.
	 * 
	 * @param file
	 *            el fichero con la definici�n de la refactorizaci�n.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             si se produce un error al cargar la refactorizaci�n desde el
	 *             fichero XML.
	 */
	private DynamicRefactoringDefinition readFile(InputStream in) throws XMLRefactoringReaderException {

		final Element root;
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			// El atributo SYSTEM del DOCTYPE de la definici�n XML de la
			// refactorizaci�n es solo la parte relativa de la ruta del fichero
			// DTD. Se le antepone la ruta del directorio del plugin que
			// contiene los ficheros de refactorizaciones din�micas.
			Document doc = builder.build(in, RefactoringPlugin.getNonEditableDynamicRefactoringsDir());
			root = doc.getRootElement();
			in.close();
		} catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception);
		} catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception);
		}
		Builder builder = readInformationRefactoring(root);
		builder = readInputsRefactoring(root, builder);
		builder = readMechanismRefactoring(root, builder);
		builder = readExamplesRefactoring(root,builder);
		return builder.build();
	}

	/**
	 * Lee el nombre, la descripci�n y la motivaci�n de la refactorizaci�n a
	 * partir de la definici�n contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento ra�z del �rbol XML que define la refactorizaci�n.
	 */
	private DynamicRefactoringDefinition.Builder readInformationRefactoring(
			Element root) {

		// Se obtiene el nombre de la refactorizaci�n.
		final DynamicRefactoringDefinition.Builder builder = new DynamicRefactoringDefinition.Builder(
				root.getAttributeValue(NAME_ATTRIBUTE));

		Element information = root.getChild(INFORMATION_ELEMENT);

		// Se obtiene la descripcion de la refactorizaci�n.
		builder.description(information.getChildTextTrim(DESCRIPTION_ELEMENT));

		// Se obtiene la imagen que describe la refactorizaci�n.
		Element imageElement = information.getChild(IMAGE_ELEMENT);

		if (imageElement != null)
			builder.image(imageElement.getAttributeValue(SRC_IMAGE_ATTRIBUTE));

		// Se obtiene la categorizacion de la refactorizaci�n.
		Element categoryElement = information.getChild(CATEGORIZATION_ELEMENT);

		builder.categories(readCategoriesElements(categoryElement
					.getChildren(CLASSIFICATION_ELEMENT)));

		// Se obtienen las palabras claves que describe la refactorizaci�n.
		Element keywordElement = information.getChild(KEYWORDS_ELEMENT);

		if (keywordElement != null) {
			builder.keywords(readKeywordElements(keywordElement
					.getChildren(KEYWORD_ELEMENT)));
		}

		// Se obtiene la motivacion de la refactorizaci�n.
		builder.motivation(information.getChildTextTrim(MOTIVATION_ELEMENT));
		return builder;
	}

	/**
	 * Obtiene una lista de las palabras claves definidas en el fichero xml para
	 * la refactorizaci�n.
	 * 
	 * @param children
	 *            lista de elementos "keyword" en el fichero xml
	 * @return conjunto de palabras clave
	 */
	private Set<String> readKeywordElements(List<Element> children) {
		Set<String> keywords = new HashSet<String>();
		for (Element keywordElement : children) {
			keywords.add(keywordElement.getTextTrim());
		}
		return keywords;
	}

	/**
	 * Crea la lista de categorias a las que la refactorizaci�n pertenece.
	 * 
	 * @param children
	 *            lista de elementos de tipo category
	 * @return conjunto de categorias a las que la refactorizaci�n pertenece
	 */
	private Set<Category> readCategoriesElements(List<Element> children) {
		Set<Category> categorias = new HashSet<Category>();
		for (Element classification : children) {
			String classificationName = classification
					.getAttributeValue(CLASSIFICATION_NAME_ATTRIBUTE);
			List<Element> categoryList = classification
					.getChildren(CATEGORY_ELEMENT);
			for (Element category : categoryList) {
				categorias.add(new Category(classificationName, category
						.getTextTrim()));
			}
		}

		return categorias;
	}

	/**
	 * Lee la lista de entradas que debe proporcionar el usuario a la
	 * refactorizaci�n, a partir de la definici�n contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento ra�z del �rbol XML que define la refactorizaci�n.
	 * @return 
	 */
	@SuppressWarnings({ "unchecked" })//$NON-NLS-1$
	private Builder readInputsRefactoring(Element root, Builder builder) {

		Element inputsElement = root.getChild(INPUTS_ELEMENT);

		// Se obtienen las entradas de la refactorizaci�n.
		List<Element> in = inputsElement.getChildren(INPUT_ELEMENT);
		return builder.inputs(readInputsElements(in));
	}

	/**
	 * Obtiene el tipo, el nombre, el origen, el m�todo y el car�cter de entrada
	 * principal o no de cada uno de los par�metros de una lista de entradas de
	 * tipo <i>input</i>.
	 * 
	 * @param in
	 *            la lista de entradas de tipo <i>input</i>.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con el tipo de la entrada,
	 *         su nombre, la clase de origen del par�metro, el m�todo mediante
	 *         el que se puede obtener y si se trata de la entrada principal de
	 *         la refactorizaci�n, en ese orden.
	 */
	private ArrayList<InputParameter> readInputsElements(List<Element> in) {

		ArrayList<InputParameter> inputs = new ArrayList<InputParameter>();

		for (Element input : in) {
			inputs.add(new InputParameter.Builder(input.getAttributeValue(TYPE_INPUT_ATTRIBUTE)).name(input.getAttributeValue(NAME_INPUT_ATTRIBUTE))
					.from(input.getAttributeValue(FROM_INPUT_ATTRIBUTE))
					.method(input.getAttributeValue(METHOD_INPUT_ATTRIBUTE))
					.main(Boolean.valueOf(input.getAttributeValue(ROOT_INPUT_ATTRIBUTE))).build());
		}
		return inputs;
	}

	/**
	 * Lee las precondiciones, acciones y postcondiciones de la refactorizaci�n
	 * a partir de la definici�n contenida en el fichero.
	 * @param builder 
	 * 
	 * @return mecanismos de la refactorizaci�n.
	 */
	@SuppressWarnings({ "unchecked" })//$NON-NLS-1$
	private Builder readMechanismRefactoring(Element root, Builder builder) {

		Element mechanism = root.getChild(MECHANISM_ELEMENT);

		// Se obtienen las precondiciones de la refactorizaci�n.
		Element preconditionsElement = mechanism
				.getChild(PRECONDITIONS_ELEMENT);
		List<Element> pre = preconditionsElement
				.getChildren(PRECONDITION_ELEMENT);
		
		builder.preconditions(readMechanismElementsAsRefactoringMechanismInstance(pre,
				RefactoringMechanismType.PRECONDITION));

		// Se obtienen las acciones de la refactorizaci�n.
		Element actionsElement = mechanism.getChild(ACTIONS_ELEMENT);
		List<Element> ac = actionsElement.getChildren(ACTION_ELEMENT);
		builder.actions(readMechanismElementsAsRefactoringMechanismInstance(ac,
				RefactoringMechanismType.ACTION));

		// Se obtienen las postcondiciones de la refactorizaci�n.
		Element postconditionsElement = mechanism
				.getChild(POSTCONDITIONS_ELEMENT);
		List<Element> post = postconditionsElement
				.getChildren(POSTCONDITION_ELEMENT);

		builder.postconditions(readMechanismElementsAsRefactoringMechanismInstance(post,
				RefactoringMechanismType.POSTCONDITION));

		return builder;
	}

	private List<RefactoringMechanismInstance> readMechanismElementsAsRefactoringMechanismInstance(List<Element> elements, RefactoringMechanismType type) {

		ArrayList<RefactoringMechanismInstance> elementNames = new ArrayList<RefactoringMechanismInstance>();

		for (Element element : elements) {
			String elementName = element.getAttributeValue(NAME_ATTRIBUTE);
			elementNames.add(new RefactoringMechanismInstance(dynamicrefactoring.util.PluginStringUtils
					.getClassName(elementName), getParametersOfMechanism(element.getChildren(PARAM_ELEMENT)), type));
		}
		return elementNames;
	}
	/**
	 * Lee la lista de parametros ambiguos asociados a un elemento, que puede
	 * ser una precondicion, una accion o una postcondicion.
	 * 
	 * @param params
	 *            elemento xml con la lista de parametros del mecanismo.
	 * @return lista de los nombres de los parametros del mecanismo
	 */
	private List<String> getParametersOfMechanism(List<Element> params) {

		List<String> parametersList = new ArrayList<String>();

		for (Element param : params) {
			parametersList.add(param.getAttributeValue(NAME_ATTRIBUTE));
		}
		return parametersList;
	}

	/**
	 * Lee los ejemplos de la refactorizaci�n a partir de la definici�n
	 * contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento ra�z del �rbol XML que define la refactorizaci�n.
	 */
	@SuppressWarnings({ "unchecked" })//$NON-NLS-1$
	private Builder readExamplesRefactoring(Element root, Builder builder) {

		// Se obtienen los ejemplos de la refactorizaci�n.
		Element examplesElement = root.getChild(EXAMPLES_ELEMENT);
		if (examplesElement != null) {
			List<Element> ex = examplesElement.getChildren(EXAMPLE_ELEMENT);
			builder.examples(readExamplesElements(ex));
		}
		return builder;
	}

	/**
	 * Lee los atributos de los ejemplos de una lista de ejemplos a partir de la
	 * definici�n contenida en el fichero.
	 * 
	 * @param examples
	 *            lista de ejemplos.
	 * 
	 * @return lista de arrays de cadenas que contienen para cada ejemplo sus
	 *         atributos.
	 */
	private List<RefactoringExample> readExamplesElements(List<Element> examples) {

		ArrayList<RefactoringExample> completed = new ArrayList<RefactoringExample>();

		for (Element example : examples) {
			completed.add(new RefactoringExample(example
					.getAttributeValue(BEFORE_EXAMPLE_ATTRIBUTE), example
					.getAttributeValue(AFTER_EXAMPLE_ATTRIBUTE)));
		}
		return completed;
	}

	/**
	 * Devuelve la definici�n de la refactorizaci�n.
	 * 
	 * @return la definici�n de la refactorizaci�n.
	 */
	@Override
	public DynamicRefactoringDefinition getDynamicRefactoringDefinition(
			File file) {
		try {
			return readFile(FileUtils.openInputStream(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw Throwables.propagate(e);
		}
	}
	
	/**
	 * Devuelve la definici�n de la refactorizaci�n.
	 * 
	 * @return la definici�n de la refactorizaci�n.
	 */
	public DynamicRefactoringDefinition getDynamicRefactoringDefinition(
			InputStream in) {
		try {
			return readFile(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Obtiene del fichero temporal que guarda las refactorizaciones
	 * disponibles, aquellas que son ejecutables con el par�metro de entrada del
	 * tipo se�alado por el par�metro scope.
	 * 
	 * @param scopeClass
	 *            tipo de la entrada principal de la refactorizaci�n.
	 * @param path_file
	 *            ruta del fichero xml en donde estan descritas las
	 *            refactorizaciones disponibles.
	 * @return <code>HashMap</code> cuyas claves son el nombre de las
	 *         refactorizaciones y los valores la ruta del fichero que contiene
	 *         la definici�n de la refactorizaci�n en caso de ser din�mica o la
	 *         cadena vacia en caso de ser est�tica.
	 * @throws XMLRefactoringReaderException
	 *             lanzado en caso de que no se pueda leer el fichero xml.
	 */
	public static Map<String, String> readAvailableRefactorings(
			Scope scopeClass, String path_file)
			throws XMLRefactoringReaderException {
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder
					.build(new File(path_file).toURI().toString());
			Element rootElement = doc.getRootElement();
			return readRefactoringData(rootElement, scopeClass);
		} catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception);
		} catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception);
		}

	}

	/**
	 * Devuelve la informacion (nombre, ruta) del conjunto de refactorizaciones
	 * cuyo ambito es el pasado.
	 * 
	 * @param scope
	 *            ambito que filtra las refactorizaciones a obtener
	 * @return mapa cuyas claves son los nombres y los valores son las rutas de
	 *         los ficheros con las definiciones de las refactorizaciones
	 */
	private static Map<String, String> readRefactoringData(Element root,
			Scope scope) {
		// FIXME: Si se pasa como scope SCOPE_BOUNDED_PAR debe devolver null o
		// algo asi
		HashMap<String, String> refactorings = new HashMap<String, String>();
		Element classdef = root.getChild(scope.getXmlTag());
		for (int i = 0; i < classdef.getChildren().size(); i++) {
			Element refactor = (Element) classdef.getChildren().get(i);
			String name = refactor.getAttribute("name").getValue();
			String path = refactor.getAttribute("path").getValue();
			refactorings.put(name, path);
		}
		return refactorings;
	}

}
