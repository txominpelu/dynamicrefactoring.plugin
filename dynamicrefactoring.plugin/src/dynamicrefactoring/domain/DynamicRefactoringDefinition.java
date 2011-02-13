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

package dynamicrefactoring.domain;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.java2html.Java2HTML;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReader;
import dynamicrefactoring.reader.XMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReaderImp;
import dynamicrefactoring.util.ScopeLimitedLister;

/**
 * Contiene la definición de una refactorización dinámica.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringDefinition implements Element,
		Comparable<DynamicRefactoringDefinition> {

	/**
	 * Nombre de la refactorización.
	 */
	private String name;

	/**
	 * Descripción de la refactorización.
	 */
	private String description;

	/**
	 * Ruta de la imagen asociada a la refactorización.
	 */
	private String image;

	/**
	 * Motivación de la refactorización.
	 */
	private String motivation;

	/**
	 * Las entradas que debe proporcionar el usuario a la refactorización.
	 */
	private ArrayList<String[]> inputs;

	/**
	 * Los nombres de las precondiciones de la refactorización.
	 */
	private ArrayList<String> preconditions;

	/**
	 * Los nombres de las acciones de la refactorización.
	 */
	private ArrayList<String> actions;

	/**
	 * Los nombres de las postcondiciones de la refactorización.
	 */
	private ArrayList<String> postconditions;

	/**
	 * Los valores para los parámetros ambiguos, que se obtienen de la
	 * definición de la refactorización.
	 */
	private HashMap<String, ArrayList<String[]>>[] ambiguousParameters;

	/**
	 * Los ejemplos de esta refactorización.
	 */
	private ArrayList<String[]> examples;

	/**
	 * Lista de categorias a las que la refactorizacion pertenece.
	 */
	private Set<Category> categories;

	private List<String> keywords;

	/**
	 * Constructor.
	 */
	@SuppressWarnings({ "unchecked" })//$NON-NLS-1$
	public DynamicRefactoringDefinition() {
		name = new String();
		description = new String();
		image = new String();
		motivation = new String();
		categories = new HashSet<Category>();
		keywords = new ArrayList<String>();

		inputs = new ArrayList<String[]>();
		preconditions = new ArrayList<String>();
		actions = new ArrayList<String>();
		postconditions = new ArrayList<String>();

		ambiguousParameters = (HashMap<String, ArrayList<String[]>>[]) new HashMap[3];
		for (int i = 0; i < ambiguousParameters.length; i++)
			ambiguousParameters[i] = new HashMap<String, ArrayList<String[]>>();

		examples = new ArrayList<String[]>();
	}

	/**
	 * Devuelve el nombre de la refactorización.
	 * 
	 * @return una cadena con el nombre de la refactorización.
	 * 
	 * @see #setName
	 */
	public String getName() {
		return name;
	}

	/**
	 * Asigna el nombre de la refactorización.
	 * 
	 * @param name
	 *            una cadena con el nombre de la refactorización.
	 * 
	 * @see #getName
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Devuelve la descripción de la refactorización.
	 * 
	 * @return una cadena con la descripción.
	 * 
	 * @see #setDescription
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Asigna la descripción de la refactorización.
	 * 
	 * @param description
	 *            una cadena con la descripción.
	 * 
	 * @see #getDescription
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Devuelve la ruta de la imagen asociada a la refactorización.
	 * 
	 * @return una cadena con la ruta a la imagen.
	 * 
	 * @see #setImage
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Asigna la ruta de la imagen asociada a la refactorización.
	 * 
	 * @param image
	 *            una cadena con la ruta a la imagen.
	 * 
	 * @see #getImage
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Devuelve la motivación de la refactorización.
	 * 
	 * @return una cadena con la motivación de la refactorización.
	 * 
	 * @see #setMotivation
	 */
	public String getMotivation() {
		return motivation;
	}

	/**
	 * Establece la motivación de la refactorización.
	 * 
	 * @param motivation
	 *            una cadena con la motivación de la refactorización.
	 * 
	 * @see #getMotivation
	 */
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	/**
	 * Devuelve las entradas que se deben solicitar al usuario para construir la
	 * refactorización.
	 * 
	 * @return una lista de <i>arrays</i> de cadenas con la información de esas
	 *         entradas.
	 * 
	 * @see #setInputs
	 */
	public ArrayList<String[]> getInputs() {
		return inputs;
	}

	/**
	 * Devuelve las entradas que se deben solicitar al usuario para construir la
	 * refactorización en una tabla asociativa de tipo <i>hash</i> que permite
	 * encontrar la entrada con un nombre determinado de forma fácil.
	 * 
	 * @return un mapa asociativo <i>hash</i> organizado de tal modo que las
	 *         claves son los nombres de cada una de las entradas, y el valor en
	 *         cada caso un <i>array</i> de cadenas con todos los atributos de
	 *         la entrada con el nombre especificado.
	 * 
	 * @see #getInputs para obtener las entradas como una lista.
	 */
	public HashMap<String, String[]> getInputsAsHash() {
		HashMap<String, String[]> map = new HashMap<String, String[]>();

		for (String[] input : inputs)
			// El nombre es el segundo atributo (posición 1 del array).
			map.put(input[1], input);

		return map;
	}

	/**
	 * Asigna las entradas que se deben solicitar al usuario para construir la
	 * refactorización.
	 * 
	 * @param inputs
	 *            lista de cadenas con la información de esas entradas.
	 * 
	 * @see #getInputs
	 */
	public void setInputs(ArrayList<String[]> inputs) {
		this.inputs = inputs;
	}

	/**
	 * Devuelve los nombres de las precondiciones de la refactorización.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres.
	 * 
	 * @see #setPreconditions
	 */
	public ArrayList<String> getPreconditions() {
		return preconditions;
	}

	/**
	 * Establece los nombres de las precondiciones de la refactorización.
	 * 
	 * @param preconditions
	 *            lista de cadenas con los nombres de las precondiciones.
	 * 
	 * @see #getPreconditions
	 */
	public void setPreconditions(ArrayList<String> preconditions) {
		this.preconditions = preconditions;
	}

	/**
	 * Devuelve los nombres de las acciones de la refactorización.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres.
	 * 
	 * @see #setActions
	 */
	public ArrayList<String> getActions() {
		return actions;
	}

	/**
	 * Establece los nombres de las acciones de la refactorización.
	 * 
	 * @param actions
	 *            lista de cadenas con los nombres de las acciones.
	 * 
	 * @see #getActions
	 */
	public void setActions(ArrayList<String> actions) {
		this.actions = actions;
	}

	/**
	 * Devuelve los nombres de las postcondiciones de la refactorización.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres.
	 * 
	 * @see #setPostconditions
	 */
	public ArrayList<String> getPostconditions() {
		return postconditions;
	}

	/**
	 * Establece los nombres de las postcondiciones de la refactorización.
	 * 
	 * @param postconditions
	 *            lista de cadenas con los nombres de las postcondiciones.
	 * 
	 * @see #getPostconditions
	 */
	public void setPostconditions(ArrayList<String> postconditions) {
		this.postconditions = postconditions;
	}

	/**
	 * Devuelve los parámetros ambiguos de la refactorización.
	 * 
	 * @return los parámetros ambiguos de la refactorización.
	 * 
	 * @see #setAmbiguousParameters
	 */
	public HashMap<String, ArrayList<String[]>>[] getAmbiguousParameters() {
		return ambiguousParameters;
	}

	/**
	 * Devuelve los parámetros ambiguos para una precondición, acción o
	 * postcondición determinada.
	 * 
	 * @param name
	 *            nombre simple de la precondición, acción o postcondición cuyos
	 *            parámetros ambiguos se deben obtener.
	 * @param typePart
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#ACTION} o
	 *            {@link RefactoringConstants#POSTCONDITION}.
	 * 
	 * @return lista de <i>arrays</i> de cadenas con los atributos de dichos
	 *         parámetros.
	 * 
	 * @see #setAmbiguousParameters
	 */
	public ArrayList<String[]> getAmbiguousParameters(String name, int typePart) {

		// Se obtienen todas las entradas del predicado o acción.
		ArrayList<String[]> inputs = ambiguousParameters[typePart].get(name);

		if (inputs != null) {
			ArrayList<String[]> params = new ArrayList<String[]>();

			// Se crea una copia de la lista de entradas del predicado o acción.
			for (String[] param : inputs) {
				String[] temp = Arrays.copyOf(param, param.length);
				params.add(temp);
			}

			if (params.size() > 0)
				return params;
		}
		return null;
	}

	/**
	 * Establece los parámetros ambiguos de la refactorización.
	 * 
	 * @param ambiguousParameters
	 *            los parámetros ambiguos de la refactorización.
	 * 
	 * @see #getAmbiguousParameters
	 */
	public void setAmbiguousParameters(
			HashMap<String, ArrayList<String[]>>[] ambiguousParameters) {

		this.ambiguousParameters = ambiguousParameters;
	}

	/**
	 * Devuelve los ejemplos de la refactorización.
	 * 
	 * @return una lista de arrays de cadenas con los atributos de cada ejemplo.
	 * 
	 * @see #setExamples
	 */
	public ArrayList<String[]> getExamples() {
		return examples;
	}

	/**
	 * Establece los ejemplos a la refactorización.
	 * 
	 * @param examples
	 *            lista de arrays de cadenas con los atributos de cada ejemplo.
	 *            Cada array de cadenas contendrá dos cadenas, una con la ruta
	 *            del fichero que contiene el estado del ejemplo antes de la
	 *            refactorización, y otra con la ruta del que contiene el estado
	 *            después de la refactorización.
	 * 
	 * @see #getExamples
	 */
	public void setExamples(ArrayList<String[]> examples) {
		this.examples = examples;
	}

	/**
	 * Devuelve la definición de una refactorización a partir de un fichero.
	 * 
	 * @param refactoringFilePath
	 *            ruta al fichero que define la refactorización.
	 * 
	 * @return la definición de la refactorización descrita en el fichero.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al cargar la refactorización desde el
	 *             fichero indicado.
	 */
	public static DynamicRefactoringDefinition getRefactoringDefinition(
			String refactoringFilePath) throws RefactoringException {

		DynamicRefactoringDefinition definition;
		try {
			XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
			XMLRefactoringReaderImp implementor = f
					.makeXMLRefactoringReaderImp(new File(refactoringFilePath));
			XMLRefactoringReader temporaryReader = new XMLRefactoringReader(
					implementor);
			definition = temporaryReader.getDynamicRefactoringDefinition();
		} catch (Exception e) {
			Object[] messageArgs = { refactoringFilePath };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoringDefinition_ErrorLoading);

			throw new RefactoringException(formatter.format(messageArgs)
					+ ".\n" + //$NON-NLS-1$
					e.getMessage());
		}
		return definition;
	}

	@Override
	public boolean belongsTo(Category category) {
		Scope scope = new ScopeLimitedLister().getRefactoringScope(this);
		// FIXME: provisionalmente hasta corregir que devuelve nulo
		Set<Category> categoriesItBelongs = new HashSet<Category>(
				getCategories());
		if (scope != null)
			categoriesItBelongs.add(new Category("scope", scope.toString()));

		return categoriesItBelongs.contains(category);
	}

	/**
	 * Las refactorizaciones se  ordenan en base a su nombre.
	 * 
	 * @param refactorToCompare refactorizacion a comparar con la actual
	 * @return sigue el criterio de {@link java.lang.String#compareTo(String)} al comparar los nombres
	 */
	@Override
	public int compareTo(DynamicRefactoringDefinition refactorToCompare) {
		return name.compareTo(refactorToCompare.getName());
	}

	@Override
	public Set<Category> getCategories() {
		return new HashSet<Category>(categories);
	}

	/**
	 * Establece el conjunto de categorías a las que el elemento va a
	 * pertenecer.
	 * 
	 * @param categories
	 *            categorias a las que el elemento pertenecera
	 */
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	/**
	 * Obtiene el conjunto de palabras claves que describen la refactorización.
	 * 
	 * @return conjunto de palabras claves
	 */
	public List<String> getKeywords() {
		return new ArrayList<String>(keywords);
	}

	/**
	 * Asigna la lista de palabras claves que describen 
	 * la refactorización.
	 * 
	 * @param keywords conjunto de palabras clave
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
		
	}
}