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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
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
	 * La definici�n de la refactorizaci�n obtenida.
	 */
	private DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * La ra�z del contenido del fichero.
	 */
	private Element root;

	/**
	 * Constructor.
	 * 
	 * @param file
	 *            el fichero con la definici�n de la refactorizaci�n.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             si se produce un error al crear el lector y procesar el
	 *             fichero con la refactorizaci�n.
	 */
	public JDOMXMLRefactoringReaderImp(File file) throws XMLRefactoringReaderException {

		this.refactoringDefinition = new DynamicRefactoringDefinition();
		readFile(file);
	}

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
	private void readFile(File file) throws XMLRefactoringReaderException {

		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			// El atributo SYSTEM del DOCTYPE de la definici�n XML de la
			// refactorizaci�n es solo la parte relativa de la ruta del fichero
			// DTD. Se le antepone la ruta del directorio del plugin que 
			// contiene los ficheros de refactorizaciones din�micas.
			Document doc = builder.build(file);
			root = doc.getRootElement();
		} 
		catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception.getMessage());
		} 
		catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception.getMessage());
		}
		readInformationRefactoring(root);
		readInputsRefactoring(root);
		readMechanismRefactoring();
		readExamplesRefactoring(root);
	}

	/**
	 * Lee el nombre, la descripci�n y la motivaci�n de la refactorizaci�n a
	 * partir de la definici�n contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento ra�z del �rbol XML que define la refactorizaci�n.
	 */
	private void readInformationRefactoring(Element root) {

		// Se obtiene el nombre de la refactorizaci�n.
		refactoringDefinition.setName(root.getAttributeValue(NAME_ATTRIBUTE));

		Element information = root.getChild(INFORMATION_ELEMENT);

		// Se obtiene la descripcion de la refactorizaci�n.
		refactoringDefinition.setDescription(
			information.getChildTextTrim(DESCRIPTION_ELEMENT));

		// Se obtiene la imagen que describe la refactorizaci�n.
		Element imageElement = information.getChild(IMAGE_ELEMENT);
		
		if(imageElement != null)
			refactoringDefinition.setImage(
				imageElement.getAttributeValue(SRC_IMAGE_ATTRIBUTE));

		// Se obtiene la categorizacion de la refactorizaci�n.
		Element categoryElement = information.getChild(CATEGORIZATION_ELEMENT);

		if (categoryElement != null){
			refactoringDefinition
					.setCategories(readCategoriesElements(categoryElement
							.getChildren(CLASSIFICATION_ELEMENT)));
		}
		
		// Se obtienen las palabras claves que describe la refactorizaci�n.
		Element keywordElement = information.getChild(KEYWORDS_ELEMENT);
		
		if (keywordElement != null){
			refactoringDefinition
					.setKeywords(readKeywordElements(keywordElement
							.getChildren(KEYWORD_ELEMENT)));
		}

		// Se obtiene la motivacion de la refactorizaci�n.
		refactoringDefinition.setMotivation(
			information.getChildTextTrim(MOTIVATION_ELEMENT));
	}

	/**
	 * Obtiene una lista de las palabras claves definidas
	 * en el fichero xml para la refactorizaci�n.
	 *  
	 * @param children lista de elementos "keyword" en el fichero xml
	 * @return conjunto de palabras clave
	 */
	private Set<String> readKeywordElements(List<Element> children) {
		Set<String> keywords = new HashSet<String>();
		for (Element keywordElement : children){
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
		for (Element classification : children){
			String classificationName = classification.getAttributeValue(CLASSIFICATION_NAME_ATTRIBUTE);
			List<Element> categoryList = classification.getChildren(CATEGORY_ELEMENT);
			for (Element category : categoryList) {
				categorias.add(new Category(classificationName , category.getTextTrim()));
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
	 */
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	private void readInputsRefactoring(Element root) {

		Element inputsElement = root.getChild(INPUTS_ELEMENT);

		// Se obtienen las entradas de la refactorizaci�n.
		List<Element> in = inputsElement.getChildren(INPUT_ELEMENT);
		refactoringDefinition.setInputs(readInputsElements(in));
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
	private ArrayList<String[]> readInputsElements(List<Element> in) {

		ArrayList<String[]> inputs = new ArrayList<String[]>();
		String[] inputContent;

		for (Element input : in){
			inputContent = new String[5];
			inputContent[0] = input.getAttributeValue(TYPE_INPUT_ATTRIBUTE);
			inputContent[1] = input.getAttributeValue(NAME_INPUT_ATTRIBUTE);
			inputContent[2] = input.getAttributeValue(FROM_INPUT_ATTRIBUTE);
			inputContent[3] = input.getAttributeValue(METHOD_INPUT_ATTRIBUTE);
			inputContent[4] = input.getAttributeValue(ROOT_INPUT_ATTRIBUTE);
			inputs.add(inputContent);
		}
		return inputs;
	}

	/**
	 * Lee las precondiciones, acciones y postcondiciones de la refactorizaci�n
	 * a partir de la definici�n contenida en el fichero.
	 * 
	 * @return mecanismos de la refactorizaci�n.
	 */
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	public ArrayList<String> readMechanismRefactoring() {
		ArrayList<String> elements;
		ArrayList<String> precondiciones = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		ArrayList<String> acciones = new ArrayList<String>();
		ArrayList<String> postconditions = new ArrayList<String>();
		ArrayList<String> postcondiciones = new ArrayList<String>();
		String postcond = "";
		String precond = "";
		String acti = "";

		Element mechanism = root.getChild(MECHANISM_ELEMENT);

		// Se obtienen las precondiciones de la refactorizaci�n.
		Element preconditionsElement = mechanism.getChild(PRECONDITIONS_ELEMENT);
		List<Element> pre = preconditionsElement.getChildren(PRECONDITION_ELEMENT);
		elements = readMechanismElements(pre, RefactoringConstants.PRECONDITION);
		//Bucle para quedarnos con el nombre no cualificado
		for(String precondition : elements ){
			StringTokenizer prec = new StringTokenizer(precondition,".");
			while(prec.hasMoreTokens()){
				precond = prec.nextElement().toString();
			}
			precondiciones.add(precond);
			
		}
		refactoringDefinition.setPreconditions(precondiciones);

		// Se obtienen las acciones de la refactorizaci�n.
		Element actionsElement = mechanism.getChild(ACTIONS_ELEMENT);
		List<Element> ac = actionsElement.getChildren(ACTION_ELEMENT);
		actions = readMechanismElements(ac, RefactoringConstants.ACTION);
		elements.addAll(actions);
		//Bucle para quedarnos con el nombre no cualificado
		for(String action : actions ){
			StringTokenizer accion = new StringTokenizer(action,".");
			while(accion.hasMoreTokens()){
				acti = accion.nextElement().toString();
			}
			acciones.add(acti);
			
		}
		refactoringDefinition.setActions(acciones);

		// Se obtienen las postcondiciones de la refactorizaci�n.
		Element postconditionsElement = mechanism.getChild(POSTCONDITIONS_ELEMENT);
		List<Element> post = postconditionsElement.getChildren(POSTCONDITION_ELEMENT);
		postconditions = readMechanismElements(post, RefactoringConstants.POSTCONDITION);
		elements.addAll(postconditions);
		//Bucle para quedarnos con el nombre no cualificado
		for(String postcondicion : postconditions ){
			StringTokenizer postcondition = new StringTokenizer(postcondicion,".");
			while(postcondition.hasMoreTokens()){
				postcond = postcondition.nextElement().toString();
			}
			postcondiciones.add(postcond);		
		}
		refactoringDefinition.setPostconditions(postcondiciones);

		return elements;
	}

	/**
	 * Lee el nombre de cada elemento de una lista de precondiciones, acciones o
	 * postcondiciones, y la lista de par�metros ambiguos asociados a cada uno.
	 * 
	 * @param elements
	 *            lista de elementos (precondiciones, acciones o
	 *            postcondiciones).
	 * @param type
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#ACTION} o
	 *            {@link RefactoringConstants#POSTCONDITION}.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres de los
	 *         elementos del repositorio.
	 */
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	private ArrayList<String> readMechanismElements(List<Element> elements,
		int type) {

		ArrayList<String> elementNames = new ArrayList<String>();
		
		List<Element> params;

		for (Element element : elements){
			String elementName = element.getAttributeValue(NAME_ATTRIBUTE); 
			elementNames.add(elementName);
			
			params = element.getChildren(PARAM_ELEMENT);
			if (!params.isEmpty()){
				StringTokenizer st_name = new StringTokenizer(elementName,".");
				String shortname="";
				while(st_name.hasMoreTokens()){
					shortname = st_name.nextElement().toString();
				}
				readAmbiguousParametersOfElement(shortname, type, params);
			}
		}
		return elementNames;
	}
	
	/**
	 * Obtiene una lista con los nombre de las distintas precondiciones, acciones y postcondiciones.
	 * 
	 * @return lista con las precondiciones, acciones y postcondiciones.
	 */
	public ArrayList<String> readAllreadMechanismElements(){
		ArrayList<String> elements = refactoringDefinition.getPreconditions();
		elements.addAll(refactoringDefinition.getActions());
		elements.addAll(refactoringDefinition.getPostconditions());
		return elements;
	}

	/**
	 * Lee la lista de par�metros ambiguos asociados a un elemento, que puede
	 * ser una precondici�n, una acci�n o una postcondici�n.
	 * 
	 * @param elementName
	 *            el nombre del elemento cuyos par�metros ambiguos han de
	 *            leerse.
	 * @param type
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#ACTION} o
	 *            {@link RefactoringConstants#POSTCONDITION}.
	 * @param params
	 *            la lista de par�metros ambiguos del elemento.
	 */
	private void readAmbiguousParametersOfElement(String elementName, int type,
		List<Element> params) {

		ArrayList<String[]> attributes = new ArrayList<String[]>();

		for (Element param : params){
			String[] attributesParam = new String[1];
			attributesParam[0] = param.getAttributeValue(NAME_ATTRIBUTE);
			attributes.add(attributesParam);
		}
		
		refactoringDefinition.getAmbiguousParameters()[type].put(
			elementName, attributes);
	}

	/**
	 * Lee los ejemplos de la refactorizaci�n a partir de la definici�n
	 * contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento ra�z del �rbol XML que define la refactorizaci�n.
	 */
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	private void readExamplesRefactoring(Element root) {

		// Se obtienen los ejemplos de la refactorizaci�n.
		Element examplesElement = root.getChild(EXAMPLES_ELEMENT);
		if(examplesElement != null) {
			List<Element> ex = examplesElement.getChildren(EXAMPLE_ELEMENT);
			refactoringDefinition.setExamples(readExamplesElements(ex));
		}
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
	private ArrayList<String[]> readExamplesElements(List<Element> examples) {

		ArrayList<String[]> completed = new ArrayList<String[]>();
		String[] exampleContent;
		
		for (Element example : examples){
			exampleContent = new String[2];
			exampleContent[0] = example.getAttributeValue(BEFORE_EXAMPLE_ATTRIBUTE);
			exampleContent[1] = example.getAttributeValue(AFTER_EXAMPLE_ATTRIBUTE);
			completed.add(exampleContent);
		}
		return completed;
	}

	/**
	 * Devuelve la definici�n de la refactorizaci�n.
	 * 
	 * @return la definici�n de la refactorizaci�n.
	 */
	@Override
	public DynamicRefactoringDefinition getDynamicRefactoringDefinition() {
		return refactoringDefinition;
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
	public static HashMap<String, String> readAvailableRefactorings(
			Scope scopeClass, String path_file)
			throws XMLRefactoringReaderException {
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new File(path_file).toURI().toString());
			Element rootElement = doc.getRootElement();
			return readRefactoringData(rootElement, scopeClass);
		} 
		catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception.getMessage());
		} 
		catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception.getMessage());
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
	private static HashMap<String, String> readRefactoringData(Element root,
			Scope scope) {
		// FIXME: Si se pasa como scope SCOPE_BOUNDED_PAR debe devolver null o
		// algo asi
		HashMap<String, String> refactorings = new HashMap<String, String>();
		Element classdef = root.getChild(scope.getXmlTag());
		for(int i=0;  i < classdef.getChildren().size(); i++){
			Element refactor = (Element)classdef.getChildren().get(i);
			String name = refactor.getAttribute("name").getValue();
			String path = refactor.getAttribute("path").getValue();
			refactorings.put(name, path);
		}
		return refactorings;
	}
	

}
