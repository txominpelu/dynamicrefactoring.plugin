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
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.Scope;

/**
 * Utiliza la implementación basada en JDOM para leer los ficheros XML que
 * definen refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JDOMXMLRefactoringReaderImp implements XMLRefactoringReaderImp {

	/**
	 * La definición de la refactorización obtenida.
	 */
	private DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * La raíz del contenido del fichero.
	 */
	private Element root;

	/**
	 * Constructor.
	 * 
	 * @param file
	 *            el fichero con la definición de la refactorización.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             si se produce un error al crear el lector y procesar el
	 *             fichero con la refactorización.
	 */
	public JDOMXMLRefactoringReaderImp(File file) throws XMLRefactoringReaderException {

		this.refactoringDefinition = new DynamicRefactoringDefinition();
		readFile(file);
	}

	/**
	 * Lee cada uno de los componentes de la refactorización a partir de la
	 * definición contenida en el fichero.
	 * 
	 * @param file
	 *            el fichero con la definición de la refactorización.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             si se produce un error al cargar la refactorización desde el
	 *             fichero XML.
	 */
	private void readFile(File file) throws XMLRefactoringReaderException {

		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			// El atributo SYSTEM del DOCTYPE de la definición XML de la
			// refactorización es solo la parte relativa de la ruta del fichero
			// DTD. Se le antepone la ruta del directorio del plugin que 
			// contiene los ficheros de refactorizaciones dinámicas.
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
	 * Lee el nombre, la descripción y la motivación de la refactorización a
	 * partir de la definición contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento raíz del árbol XML que define la refactorización.
	 */
	private void readInformationRefactoring(Element root) {

		// Se obtiene el nombre de la refactorización.
		refactoringDefinition.setName(root.getAttributeValue(NAME_ATTRIBUTE));

		Element information = root.getChild(INFORMATION_ELEMENT);

		// Se obtiene la descripcion de la refactorización.
		refactoringDefinition.setDescription(
			information.getChildTextTrim(DESCRIPTION_ELEMENT));

		// Se obtiene la imagen que describe la refactorización.
		Element imageElement = information.getChild(IMAGE_ELEMENT);
		
		if(imageElement != null)
			refactoringDefinition.setImage(
				imageElement.getAttributeValue(SRC_IMAGE_ATTRIBUTE));

		// Se obtiene la motivacion de la refactorización.
		refactoringDefinition.setMotivation(
			information.getChildTextTrim(MOTIVATION_ELEMENT));
	}

	/**
	 * Lee la lista de entradas que debe proporcionar el usuario a la
	 * refactorización, a partir de la definición contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento raíz del árbol XML que define la refactorización.
	 */
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	private void readInputsRefactoring(Element root) {

		Element inputsElement = root.getChild(INPUTS_ELEMENT);

		// Se obtienen las entradas de la refactorización.
		List<Element> in = inputsElement.getChildren(INPUT_ELEMENT);
		refactoringDefinition.setInputs(readInputsElements(in));
	}

	/**
	 * Obtiene el tipo, el nombre, el origen, el método y el carácter de entrada
	 * principal o no de cada uno de los parámetros de una lista de entradas de
	 * tipo <i>input</i>.
	 * 
	 * @param in
	 *            la lista de entradas de tipo <i>input</i>.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con el tipo de la entrada,
	 *         su nombre, la clase de origen del parámetro, el método mediante
	 *         el que se puede obtener y si se trata de la entrada principal de
	 *         la refactorización, en ese orden.
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
	 * Lee las precondiciones, acciones y postcondiciones de la refactorización
	 * a partir de la definición contenida en el fichero.
	 * 
	 * @return mecanismos de la refactorización.
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

		// Se obtienen las precondiciones de la refactorización.
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

		// Se obtienen las acciones de la refactorización.
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

		// Se obtienen las postcondiciones de la refactorización.
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
	 * postcondiciones, y la lista de parámetros ambiguos asociados a cada uno.
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
	 * Lee la lista de parámetros ambiguos asociados a un elemento, que puede
	 * ser una precondición, una acción o una postcondición.
	 * 
	 * @param elementName
	 *            el nombre del elemento cuyos parámetros ambiguos han de
	 *            leerse.
	 * @param type
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#ACTION} o
	 *            {@link RefactoringConstants#POSTCONDITION}.
	 * @param params
	 *            la lista de parámetros ambiguos del elemento.
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
	 * Lee los ejemplos de la refactorización a partir de la definición
	 * contenida en el fichero.
	 * 
	 * @param root
	 *            el elemento raíz del árbol XML que define la refactorización.
	 */
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	private void readExamplesRefactoring(Element root) {

		// Se obtienen los ejemplos de la refactorización.
		Element examplesElement = root.getChild(EXAMPLES_ELEMENT);
		if(examplesElement != null) {
			List<Element> ex = examplesElement.getChildren(EXAMPLE_ELEMENT);
			refactoringDefinition.setExamples(readExamplesElements(ex));
		}
	}

	/**
	 * Lee los atributos de los ejemplos de una lista de ejemplos a partir de la
	 * definición contenida en el fichero.
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
	 * Devuelve la definición de la refactorización.
	 * 
	 * @return la definición de la refactorización.
	 */
	@Override
	public DynamicRefactoringDefinition getDynamicRefactoringDefinition() {
		return refactoringDefinition;
	}

	/**
	 * Obtiene del fichero temporal que guarda las refactorizaciones
	 * disponibles, aquellas que son ejecutables con el parámetro de entrada del
	 * tipo señalado por el parámetro scope.
	 * 
	 * @param scopeClass
	 *            tipo de la entrada principal de la refactorización.
	 * @param path_file
	 *            ruta del fichero xml en donde estan descritas las
	 *            refactorizaciones disponibles.
	 * @return <code>HashMap</code> cuyas claves son el nombre de las
	 *         refactorizaciones y los valores la ruta del fichero que contiene
	 *         la definición de la refactorización en caso de ser dinámica o la
	 *         cadena vacia en caso de ser estática.
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
