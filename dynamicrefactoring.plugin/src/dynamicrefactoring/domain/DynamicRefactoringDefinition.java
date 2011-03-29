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
import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.plugin.xml.classifications.imp.PluginCatalog;
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
	private List<String[]> inputs;

	/**
	 * Los nombres de las precondiciones de la refactorizaci�n.
	 */
	private List<String> preconditions;

	/**
	 * Los nombres de las acciones de la refactorizaci�n.
	 */
	private List<String> actions;

	/**
	 * Los nombres de las postcondiciones de la refactorizaci�n.
	 */
	private List<String> postconditions;

	/**
	 * Los valores para los par�metros ambiguos, que se obtienen de la
	 * definici�n de la refactorizaci�n.
	 */
	private HashMap<String, List<String[]>>[] ambiguousParameters;

	/**
	 * Los ejemplos de esta refactorizaci�n.
	 */
	private List<String[]> examples;

	/**
	 * Conjunto de categorias a las que la refactorizacion pertenece.
	 */
	private Set<Category> categories;

	/**
	 * Conjunto de palabras clave de la refactorizacion.
	 */
	private Set<String> keywords;

	private DynamicRefactoringDefinition(Builder builder) {
		name = builder.name;
		description = builder.description;
		image = builder.image;
		motivation = builder.motivation;
		categories = builder.categories;
		keywords = builder.keywords;

		inputs = builder.inputs;
		preconditions = builder.preconditions;
		actions = builder.actions;
		postconditions = builder.postconditions;

		ambiguousParameters = builder.ambiguousParameters;

		examples = builder.examples;
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
	 * Devuelve las entradas que se deben solicitar al usuario para construir la
	 * refactorizaci�n.
	 * 
	 * @return una lista de <i>arrays</i> de cadenas con la informaci�n de
	 *         esas entradas.
	 * 
	 * @see #setInputs
	 */
	public List<String[]> getInputs() {
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
	 * Devuelve los nombres de las precondiciones de la refactorizaci�n.
	 * 
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setPreconditions
	 */
	public List<String> getPreconditions() {
		return preconditions;
	}

	/**
	 * Devuelve los nombres de las acciones de la refactorizaci�n.
	 * 
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setActions
	 */
	public List<String> getActions() {
		return new ArrayList<String>(actions);
	}

	/**
	 * Devuelve los nombres de las postcondiciones de la refactorizaci�n.
	 * 
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setPostconditions
	 */
	public List<String> getPostconditions() {
		return new ArrayList<String>(postconditions);
	}

	/**
	 * Devuelve los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @return los par�metros ambiguos de la refactorizaci�n.
	 * 
	 * @see #setAmbiguousParameters
	 */
	public HashMap<String, List<String[]>>[] getAmbiguousParameters() {
		return ambiguousParameters;
	}

	/**
	 * Devuelve los par�metros ambiguos para una precondici�n, acci�n o
	 * postcondici�n determinada.
	 * 
	 * @param name
	 *            nombre simple de la precondici�n, acci�n o postcondici�n
	 *            cuyos par�metros ambiguos se deben obtener.
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
	public List<String[]> getAmbiguousParameters(String name, int typePart) {

		// Se obtienen todas las entradas del predicado o accion.
		List<String[]> inputs = ambiguousParameters[typePart].get(name);

		if (inputs != null) {
			List<String[]> params = new ArrayList<String[]>();

			// Se crea una copia de la lista de entradas del predicado o accion.
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
	 * Devuelve los ejemplos de la refactorizaci�n.
	 * 
	 * @return una lista de arrays de cadenas con los atributos de cada ejemplo.
	 * 
	 * @see #setExamples
	 */
	public List<String[]> getExamples() {
		return examples;
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
	 *             si se produce un error al cargar la refactorizaci�n desde
	 *             el fichero indicado.
	 */
	public static DynamicRefactoringDefinition getRefactoringDefinition(
			String refactoringFilePath) throws RefactoringException {

		DynamicRefactoringDefinition definition;
		try {
			XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
			XMLRefactoringReaderImp implementor = f
					.makeXMLRefactoringReaderImp();
			XMLRefactoringReader temporaryReader = new XMLRefactoringReader(
					implementor);
			definition = temporaryReader
					.getDynamicRefactoringDefinition(new File(
							refactoringFilePath));
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
								PluginCatalog.SCOPE_CLASSIFICATION);
					}

				});

		// FIXME: Internacionalizar
		Preconditions.checkArgument(refactScopeCategories.size() > 0,
				"All refactorings must belong to at least one scope.");

		return Collections2
				.transform(refactScopeCategories,
						new Function<Category, Scope>() {

							/**
							 * Obtenemos el scope de las categorias obtenidas
							 * anteriormente.
							 * 
							 * @param arg0
							 * @return el scope de las categorias obtenidas
							 *         anteriormente
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
		word = word.toLowerCase().trim();
		return keywords.contains(word);
	}

	@Override
	public boolean containsText(String text) {
		text = text.toLowerCase().trim();
		return name.toLowerCase().contains(text)
				|| description.toLowerCase().contains(text)
				|| motivation.toLowerCase().contains(text);
	}

	@Override
	public boolean containsInputType(String inputType) {
		for(String[] input: inputs){
			if(input[0].equalsIgnoreCase(inputType))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsRootInputType(String rootInputType) {
		for(String[] input: inputs){
			if(input[0].equalsIgnoreCase(rootInputType) && 
			   input[4]!=null &&
			   input[4].equalsIgnoreCase("true"))
				return true;
		}
		return false;
	}
	@Override
	public boolean containsPrecondition(String precondition) {
		return preconditions.contains(precondition);
	}

	@Override
	public boolean containsAction(String action) {
		return actions.contains(action);
	}

	@Override
	public boolean containsPostcondition(String postcondition) {
		return postconditions.contains(postcondition);
	}
	
	/**
	 * Las refactorizaciones se ordenan en base a su nombre.
	 * 
	 * @param refactorToCompare
	 *            refactorizacion a comparar con la actual
	 * @return sigue el criterio de {@link java.lang.String#compareTo(String)}
	 *         al comparar los nombres
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
	 * Obtiene el conjunto de palabras claves que describen la
	 * refactorizaci�n.
	 * 
	 * @return conjunto de palabras claves
	 */
	public Set<String> getKeywords() {
		return new HashSet<String>(keywords);
	}

	/**
	 * Dos refactorizaciones son consideradas iguales si tienen el mismo nombre.
	 * Esto es debido a que en el catalogo de refactorizaciones {@link Catalog}
	 * se considera que el nombre es un identificador unico y por tanto no puede
	 * haber dos refactorizaciones con el mismo nombre.
	 * 
	 * @param object
	 *            objeto a comparar
	 * @return verdadero si el objeto a comparar es una definicion de
	 *         refactorizacion con el mismo nombre que la actual
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof DynamicRefactoringDefinition) {
			return ((DynamicRefactoringDefinition) object).getName().equals(
					getName());
		}
		return false;
	}

	/**
	 * Definido en base a la convencion especificada en {@link Object} sobre
	 * hashCode() y teniendo en cuento el metodo {@link #equals(Object)} de esta
	 * misma clase.
	 * 
	 * @return codigo hash
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * Genera un builder de refactorizaciones preconfigurado con los parametros
	 * de la refactorizacion actual.
	 * 
	 * @return builder con los parametros de la refactorizacion
	 */
	public final Builder getBuilder() {
		return new Builder(getName()).actions(getActions())
				.ambiguousParameters(getAmbiguousParameters())
				.categories(getCategories()).description(getDescription())
				.examples(getExamples()).image(getImage()).inputs(getInputs())
				.keywords(getKeywords()).motivation(getMotivation())
				.postconditions(getPostconditions())
				.preconditions(getPreconditions());
	}

	/**
	 * Constructor de definiciones de refactorizaciones siguiendo el patrón
	 * Builder.
	 * 
	 * @author imediava
	 * 
	 */
	public static class Builder {

		private Set<Category> categories;
		private Set<String> keywords = new HashSet<String>();
		private List<String[]> examples = new ArrayList<String[]>();
		private String name;
		private String description;
		private String image = "";
		private String motivation;
		private List<String[]> inputs;
		private List<String> preconditions;
		private List<String> actions;
		private List<String> postconditions;
		private HashMap<String, List<String[]>>[] ambiguousParameters;

		/**
		 * Crea un builder para crear una definicion de refactorizacion con el
		 * nombre dado.
		 * 
		 * @param refactoringName
		 *            nombre que tendra la refactorizacion que cree el builder
		 */
		public Builder(String refactoringName) {
			this.name = refactoringName;
			ambiguousParameters = (HashMap<String, List<String[]>>[]) new HashMap[3];
			for (int i = 0; i < ambiguousParameters.length; i++)
				ambiguousParameters[i] = new HashMap<String, List<String[]>>();
		}

		/**
		 * Obtiene la definicion de la refactorizacion cuyos parametros se han
		 * establecido en el builder.
		 * 
		 * Lanzara excepcion {@link IllegalArgumentException} si no se cumple
		 * alguna de las reglas de definicion de una refactorizacion expresadas
		 * en los ficheros XSD o DTD. (Por ejemplo no se ha asignado valor a un
		 * campo obligatorio)
		 * 
		 * @return definicion de la refactorizacion
		 */
		public DynamicRefactoringDefinition build() {
			DynamicRefactoringDefinition definition = new DynamicRefactoringDefinition(
					this);
			checkParameterNotNull(name, "name");
			checkParameterNotNull(categories, "categories");
			checkParameterNotNull(description, "description");
			checkParameterNotNull(motivation, "motivation");
			checkParameterNotNull(inputs, "inputs");
			checkParameterNotNull(preconditions, "preconditions");
			checkParameterNotNull(actions, "actions");
			checkParameterNotNull(postconditions, "postconditions");
			checkParameterNotNull(ambiguousParameters, "ambiguousParameters");
			checkParameterNotNull(image, "image");
			checkParameterNotNull(examples, "examples");
			checkParameterNotNull(keywords, "keywords");
			return definition;
		}

		private void checkParameterNotNull(Object parameter,
				String parameterName) {
			Preconditions
					.checkArgument(
							parameter != null,
							String.format(
									"\'%s\'  parameter is not optional. A value different from null must be given.",
									parameterName));
		}

		/**
		 * Establece el conjunto de categor�as a las que el elemento va a
		 * pertenecer.
		 * 
		 * @param categories
		 *            categorias a las que el elemento pertenecera
		 */
		public Builder categories(Set<Category> categories) {
			this.categories = categories;
			return this;
		}

		/**
		 * Asigna el conjunto de palabras claves que describen la
		 * refactorizaci�n.
		 * 
		 * @param keywords
		 *            conjunto de palabras clave
		 * @return devuelve el builder con el nuevo parametro
		 */
		public Builder keywords(Set<String> keywords) {
			this.keywords = keywords;
			return this;
		}

		/**
		 * Establece los ejemplos a la refactorizaci�n.
		 * 
		 * @param examples
		 *            lista de arrays de cadenas con los atributos de cada
		 *            ejemplo. Cada array de cadenas contendr� dos cadenas,
		 *            una con la ruta del fichero que contiene el estado del
		 *            ejemplo antes de la refactorizaci�n, y otra con la ruta
		 *            del que contiene el estado despu�s de la
		 *            refactorizaci�n.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getExamples
		 */
		public Builder examples(List<String[]> examples) {
			this.examples = examples;
			return this;
		}

		/**
		 * Asigna la descripci�n de la refactorizaci�n.
		 * 
		 * @param description
		 *            una cadena con la descripci�n.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getDescription
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Asigna la ruta de la imagen asociada a la refactorizaci�n.
		 * 
		 * @param image
		 *            una cadena con la ruta a la imagen.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getImage
		 */
		public Builder image(String image) {
			this.image = image;
			return this;
		}

		/**
		 * Establece la motivaci�n de la refactorizaci�n.
		 * 
		 * @param motivation
		 *            una cadena con la motivaci�n de la refactorizaci�n.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getMotivation
		 */
		public Builder motivation(String motivation) {
			this.motivation = motivation;
			return this;
		}

		/**
		 * Asigna las entradas que se deben solicitar al usuario para construir
		 * la refactorizaci�n.
		 * 
		 * @param inputs
		 *            lista de cadenas con la informaci�n de esas entradas.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getInputs
		 */
		public Builder inputs(List<String[]> inputs) {
			this.inputs = inputs;
			return this;
		}

		/**
		 * Establece los nombres de las precondiciones de la refactorizaci�n.
		 * 
		 * @param preconditions
		 *            lista de cadenas con los nombres de las precondiciones.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getPreconditions
		 */
		public Builder preconditions(List<String> preconditions) {
			this.preconditions = preconditions;
			return this;
		}

		/**
		 * Establece los nombres de las acciones de la refactorizaci�n.
		 * 
		 * @param actions
		 *            lista de cadenas con los nombres de las acciones.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getActions
		 */
		public Builder actions(List<String> actions) {
			this.actions = actions;
			return this;
		}

		/**
		 * Establece los nombres de las postcondiciones de la refactorizaci�n.
		 * 
		 * @param postconditions
		 *            lista de cadenas con los nombres de las postcondiciones.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getPostconditions
		 */
		public Builder postconditions(List<String> postconditions) {
			this.postconditions = postconditions;
			return this;
		}

		/**
		 * Establece los par�metros ambiguos de la refactorizaci�n.
		 * 
		 * @param ambiguousParameters
		 *            los par�metros ambiguos de la refactorizaci�n.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getAmbiguousParameters
		 */
		public Builder ambiguousParameters(
				HashMap<String, List<String[]>>[] ambiguousParameters) {

			this.ambiguousParameters = ambiguousParameters;
			return this;
		}

	}

}