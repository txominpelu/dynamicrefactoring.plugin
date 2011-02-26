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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsStore;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReader;
import dynamicrefactoring.reader.XMLRefactoringReaderFactory;
import dynamicrefactoring.reader.XMLRefactoringReaderImp;

/**
 * Contiene la definici�n de una refactorizaci�n din�mica.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringDefinition implements Element,
		Comparable<DynamicRefactoringDefinition> {

	/**
	 * Nombre de la refactorizaci�n.
	 */
	private String name;

	/**
	 * Descripci�n de la refactorizaci�n.
	 */
	private String description;

	/**
	 * Ruta de la imagen asociada a la refactorizaci�n.
	 */
	private String image;

	/**
	 * Motivaci�n de la refactorizaci�n.
	 */
	private String motivation;

	/**
	 * Las entradas que debe proporcionar el usuario a la refactorizaci�n.
	 */
	private ArrayList<String[]> inputs;

	/**
	 * Los nombres de las precondiciones de la refactorizaci�n.
	 */
	private ArrayList<String> preconditions;

	/**
	 * Los nombres de las acciones de la refactorizaci�n.
	 */
	private ArrayList<String> actions;

	/**
	 * Los nombres de las postcondiciones de la refactorizaci�n.
	 */
	private ArrayList<String> postconditions;

	/**
	 * Los valores para los par�metros ambiguos, que se obtienen de la
	 * definici�n de la refactorizaci�n.
	 */
	private HashMap<String, ArrayList<String[]>>[] ambiguousParameters;

	/**
	 * Los ejemplos de esta refactorizaci�n.
	 */
	private ArrayList<String[]> examples;

	/**
	 * Conjunto de categorias a las que la refactorizacion pertenece.
	 */
	private Set<Category> categories;

	/**
	 * Conjunto de palabras clave de la refactorizacion.
	 */
	private Set<String> keywords;

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
		keywords = new HashSet<String>();

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
	 * Devuelve el nombre de la refactorizaci�n.
	 * 
	 * @return una cadena con el nombre de la refactorizaci�n.
	 * 
	 * @see #setName
	 */
	public String getName() {
		return name;
	}

	/**
	 * Asigna el nombre de la refactorizaci�n.
	 * 
	 * @param name
	 *            una cadena con el nombre de la refactorizaci�n.
	 * 
	 * @see #getName
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Devuelve la descripci�n de la refactorizaci�n.
	 * 
	 * @return una cadena con la descripci�n.
	 * 
	 * @see #setDescription
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Asigna la descripci�n de la refactorizaci�n.
	 * 
	 * @param description
	 *            una cadena con la descripci�n.
	 * 
	 * @see #getDescription
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Devuelve la ruta de la imagen asociada a la refactorizaci�n.
	 * 
	 * @return una cadena con la ruta a la imagen.
	 * 
	 * @see #setImage
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Asigna la ruta de la imagen asociada a la refactorizaci�n.
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
	 * Devuelve la motivaci�n de la refactorizaci�n.
	 * 
	 * @return una cadena con la motivaci�n de la refactorizaci�n.
	 * 
	 * @see #setMotivation
	 */
	public String getMotivation() {
		return motivation;
	}

	/**
	 * Establece la motivaci�n de la refactorizaci�n.
	 * 
	 * @param motivation
	 *            una cadena con la motivaci�n de la refactorizaci�n.
	 * 
	 * @see #getMotivation
	 */
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	/**
	 * Devuelve las entradas que se deben solicitar al usuario para construir la
	 * refactorizaci�n.
	 * 
	 * @return una lista de <i>arrays</i> de cadenas con la informaci�n de esas
	 *         entradas.
	 * 
	 * @see #setInputs
	 */
	public ArrayList<String[]> getInputs() {
		return inputs;
	}

	/**
	 * Devuelve las entradas que se deben solicitar al usuario para construir la
	 * refactorizaci�n en una tabla asociativa de tipo <i>hash</i> que permite
	 * encontrar la entrada con un nombre determinado de forma f�cil.
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
			// El nombre es el segundo atributo (posici�n 1 del array).
			map.put(input[1], input);

		return map;
	}

	/**
	 * Asigna las entradas que se deben solicitar al usuario para construir la
	 * refactorizaci�n.
	 * 
	 * @param inputs
	 *            lista de cadenas con la informaci�n de esas entradas.
	 * 
	 * @see #getInputs
	 */
	public void setInputs(ArrayList<String[]> inputs) {
		this.inputs = inputs;
	}

	/**
	 * Devuelve los nombres de las precondiciones de la refactorizaci�n.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres.
	 * 
	 * @see #setPreconditions
	 */
	public ArrayList<String> getPreconditions() {
		return preconditions;
	}

	/**
	 * Establece los nombres de las precondiciones de la refactorizaci�n.
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
	 * Devuelve los nombres de las acciones de la refactorizaci�n.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres.
	 * 
	 * @see #setActions
	 */
	public ArrayList<String> getActions() {
		return actions;
	}

	/**
	 * Establece los nombres de las acciones de la refactorizaci�n.
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
	 * Devuelve los nombres de las postcondiciones de la refactorizaci�n.
	 * 
	 * @return un <code>ArrayList</code> de cadenas con los nombres.
	 * 
	 * @see #setPostconditions
	 */
	public ArrayList<String> getPostconditions() {
		return postconditions;
	}

	/**
	 * Establece los nombres de las postcondiciones de la refactorizaci�n.
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
	 * Devuelve los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @return los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @see #setAmbiguousParameters
	 */
	public HashMap<String, ArrayList<String[]>>[] getAmbiguousParameters() {
		return ambiguousParameters;
	}

	/**
	 * Devuelve los par�metros ambiguos para una precondici�n, acci�n o
	 * postcondici�n determinada.
	 * 
	 * @param name
	 *            nombre simple de la precondici�n, acci�n o postcondici�n cuyos
	 *            par�metros ambiguos se deben obtener.
	 * @param typePart
	 *            {@link RefactoringConstants#PRECONDITION},
	 *            {@link RefactoringConstants#ACTION} o
	 *            {@link RefactoringConstants#POSTCONDITION}.
	 * 
	 * @return lista de <i>arrays</i> de cadenas con los atributos de dichos
	 *         par�metros.
	 * 
	 * @see #setAmbiguousParameters
	 */
	public ArrayList<String[]> getAmbiguousParameters(String name, int typePart) {


		// Se obtienen todas las entradas del predicado o accion.
		ArrayList<String[]> inputs = ambiguousParameters[typePart].get(name);

		if (inputs != null) {
			ArrayList<String[]> params = new ArrayList<String[]>();

			// Se crea una copia de la lista de entradas del predicado o accion.
			for (String[] param : inputs){
				String[] temp = Arrays.copyOf(param, param.length);
				params.add(temp);
			}

			if (params.size() > 0)
				return params;
		}
		return null;
	}

	/**
	 * Establece los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @param ambiguousParameters
	 *            los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @see #getAmbiguousParameters
	 */
	public void setAmbiguousParameters(
			HashMap<String, ArrayList<String[]>>[] ambiguousParameters) {

		this.ambiguousParameters = ambiguousParameters;
	}

	/**
	 * Devuelve los ejemplos de la refactorizaci�n.
	 * 
	 * @return una lista de arrays de cadenas con los atributos de cada ejemplo.
	 * 
	 * @see #setExamples
	 */
	public ArrayList<String[]> getExamples() {
		return examples;
	}

	/**
	 * Establece los ejemplos a la refactorizaci�n.
	 * 
	 * @param examples
	 *            lista de arrays de cadenas con los atributos de cada ejemplo.
	 *            Cada array de cadenas contendr� dos cadenas, una con la ruta
	 *            del fichero que contiene el estado del ejemplo antes de la
	 *            refactorizaci�n, y otra con la ruta del que contiene el estado
	 *            despu�s de la refactorizaci�n.
	 * 
	 * @see #getExamples
	 */
	public void setExamples(ArrayList<String[]> examples) {
		this.examples = examples;
	}

	/**
	 * Devuelve la definici�n de una refactorizaci�n a partir de un fichero.
	 * 
	 * @param refactoringFilePath
	 *            ruta al fichero que define la refactorizaci�n.
	 * 
	 * @return la definici�n de la refactorizaci�n descrita en el fichero.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al cargar la refactorizaci�n desde el
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
	
	/**
	 * Devuelve el ambito al que pertenece una refactorizacion.
	 * 
	 * @return ambito de la refactorizacion.
	 */
	public final Scope getRefactoringScope() {
		Collection<Category> refactScopeCategories = Collections2.filter(
				getCategories(), new Predicate<Category>() {

					/**
					 * Filtramos las categorias a las que la refactorizacion
					 * pertenece que pertenecen al grupo scope.
					 */
					@Override
					public boolean apply(Category arg0) {
						return arg0.getParent().equals(
								ClassificationsStore.SCOPE_CLASSIFICATION);
					}

				});

		// FIXME: Internacionalizar
		Preconditions.checkArgument(refactScopeCategories.size() > 0,
				"All refactorings must belong to at least one scope.");

		return Collections2.transform(refactScopeCategories,
				new Function<Category, Scope>() {

					/**
					 * Obtenemos el scope de las categorias obtenidas
					 * anteriormente.
					 * 
					 * @param arg0
					 * @return
					 */
					@Override
					public Scope apply(Category arg0) {
						return Scope.fromString(arg0.getName());
					}

				}).iterator().next();

	}

	@Override
	public final boolean belongsTo(Category category) {
		return categories.contains(category);
	}
	
	@Override
	public boolean belongsTo(String word) {
		word=word.toLowerCase().trim();
		return keywords.contains(word);
	}
	
	@Override
	public boolean containsText(String text) {
		text=text.toLowerCase().trim();
		return name.toLowerCase().contains(text) ||
			   description.toLowerCase().contains(text) ||
			   motivation.toLowerCase().contains(text);
	}
	
	/**
	 * Las refactorizaciones se  ordenan en base a su nombre.
	 * 
	 * @param refactorToCompare refactorizacion a comparar con la actual
	 * @return sigue el criterio de {@link java.lang.String#compareTo(String)} al comparar los nombres
	 */
	@Override
	public final int compareTo(DynamicRefactoringDefinition refactorToCompare) {
		return name.compareTo(refactorToCompare.getName());
	}

	@Override
	public final Set<Category> getCategories() {
		return new HashSet<Category>(categories);
	}

	/**
	 * Establece el conjunto de categor�as a las que el elemento va a
	 * pertenecer.
	 * 
	 * @param categories
	 *            categorias a las que el elemento pertenecera
	 */
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}


	/**
	 * Obtiene el conjunto de palabras claves que describen la refactorizaci�n.
	 * 
	 * @return conjunto de palabras claves
	 */
	public Set<String> getKeywords() {
		return new HashSet<String>(keywords);
	}

	/**
	 * Asigna el conjunto de palabras claves que describen 
	 * la refactorizaci�n.
	 * 
	 * @param keywords conjunto de palabras clave
	 */
	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
		
	}


}