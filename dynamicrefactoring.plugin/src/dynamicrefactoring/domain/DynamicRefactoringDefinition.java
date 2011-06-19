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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Element;

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
	private List<InputParameter> inputs;

	/**
	 * Los nombres de las precondiciones de la refactorización.
	 */
	private List<RefactoringMechanismInstance> preconditions;

	/**
	 * Los nombres de las acciones de la refactorización.
	 */
	private List<RefactoringMechanismInstance> actions;

	/**
	 * Los nombres de las postcondiciones de la refactorización.
	 */
	private List<RefactoringMechanismInstance> postconditions;

	/**
	 * Los ejemplos de esta refactorización.
	 */
	private List<RefactoringExample> examples;

	/**
	 * Conjunto de categorias a las que la refactorizacion pertenece.
	 */
	private Set<Category> categories;

	/**
	 * Conjunto de palabras clave de la refactorizacion.
	 */
	private Set<String> keywords;

	/**
	 * Si la refactorizacion es editable o por el contrario pertenece al plugin
	 * y no se puede editar.
	 */
	private boolean isEditable;

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

		examples = builder.examples;
		isEditable = builder.isEditable;
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
	 * Devuelve las entradas que se deben solicitar al usuario para construir la
	 * refactorización.
	 * 
	 * @return una lista de <i>arrays</i> de cadenas con la información de esas
	 *         entradas.
	 * 
	 * @see #setInputs
	 */
	public List<InputParameter> getInputs() {
		return inputs;
	}

	/**
	 * Devuelve si la refactorizacion es editable (puesto que pertenece al
	 * usuario) o es solo de escritura (pertenece al plugin).
	 * 
	 * @return verdadero si la refactorizacion es del usuario y por tanto se
	 *         puede editar
	 */
	public boolean isEditable() {
		return isEditable;
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
	public HashMap<String, InputParameter> getInputsAsHash() {
		HashMap<String, InputParameter> map = new HashMap<String, InputParameter>();

		for (InputParameter input : inputs)
			// El nombre es el segundo atributo (posición 1 del array).
			map.put(input.getName(), input);

		return map;
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
	 * Devuelve los nombres de las precondiciones de la refactorización.
	 * 
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setPreconditions
	 */
	public List<RefactoringMechanismInstance> getPreconditions() {
		return new ArrayList<RefactoringMechanismInstance>(preconditions);
	}

	/**
	 * Devuelve los nombres de las acciones de la refactorización.
	 * 
	 * @return un <code>List</code> de mecanismos con los nombres.
	 * 
	 * @see #setActions
	 */
	public List<RefactoringMechanismInstance> getActions() {
		return new ArrayList<RefactoringMechanismInstance>(actions);
	}

	/**
	 * Devuelve los nombres de las postcondiciones de la refactorización.
	 * 
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setPostconditions
	 */
	public List<RefactoringMechanismInstance> getPostconditions() {
		return new ArrayList<RefactoringMechanismInstance>(postconditions);
	}

	/**
	 * Devuelve los ejemplos de la refactorización.
	 * 
	 * @return una lista de arrays de cadenas con los atributos de cada ejemplo.
	 * 
	 * @see #setExamples
	 */
	public List<RefactoringExample> getExamples() {
		return examples;
	}

	/**
	 * Obtiene la lista de ejemplos de la refactorizacion pero con rutas
	 * absolutas a los ficheros de ejemplo.
	 * 
	 * @return rutas absolutas a los ficheros de ejemplo de la refactorizacion
	 */
	public List<RefactoringExample> getExamplesAbsolutePath() {
		List<RefactoringExample> absolutePathExamples = new ArrayList<RefactoringExample>();
		for (RefactoringExample ejemplo : getExamples()) {
			absolutePathExamples.add(new RefactoringExample(
					getRefactoringFileFullPath(ejemplo.getBefore()),
					getRefactoringFileFullPath(ejemplo.getAfter())));
		}
		return absolutePathExamples;
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
						return arg0
								.getParent()
								.equals(PluginClassificationsCatalog.SCOPE_CLASSIFICATION);
					}

				});

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

	/**
	 * Devuelve si la refactorizacion pertenece a un scope.
	 * 
	 * @param categories
	 *            conjunto de categorias de la refactorizacion
	 * 
	 * @param definition
	 *            definicion de la refactorizacion
	 * @return si pertenece a un scope
	 */
	public static boolean containsScopeCategory(Set<Category> categories) {
		for (Category c : categories) {
			if (c.getParent().equals(
					PluginClassificationsCatalog.SCOPE_CLASSIFICATION)) {
				return true;
			}
		}
		return false;
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
		for (InputParameter input : inputs) {
			if (input.getType().equalsIgnoreCase(inputType))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsRootInputType(String rootInputType) {
		for (InputParameter input : inputs) {
			if (input.getType().equalsIgnoreCase(rootInputType)
					&& input.isMain())
				return true;
		}
		return false;
	}

	@Override
	public boolean containsPrecondition(String precondition) {
		return !getMechanismsWithName(precondition,
				RefactoringMechanismType.PRECONDITION).isEmpty();
	}

	@Override
	public boolean containsAction(String action) {
		return !getMechanismsWithName(action, RefactoringMechanismType.ACTION)
				.isEmpty();
	}

	@Override
	public boolean containsPostcondition(String postcondition) {
		return !getMechanismsWithName(postcondition,
				RefactoringMechanismType.POSTCONDITION).isEmpty();
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
	 * Obtiene el conjunto de palabras claves que describen la refactorización.
	 * 
	 * @return conjunto de palabras claves
	 */
	public Set<String> getKeywords() {
		return new HashSet<String>(keywords);
	}

	/**
	 * Dos refactorizaciones son consideradas iguales si tienen el mismo nombre.
	 * Esto es debido a que en el catalogo de refactorizaciones
	 * {@link ClassificationsCatalog} se considera que el nombre es un
	 * identificador unico y por tanto no puede haber dos refactorizaciones con
	 * el mismo nombre.
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
	 * Devuelve si otra refactorizacion es exactamente igual a otra. Es decir si
	 * todos sus campos contienen los mismos valores.
	 * 
	 * @param otra
	 *            refactorizacion a comparar
	 * @return verdaderos si son iguales para cada campo de cada refactorizacion
	 */
	public boolean exactlyEquals(DynamicRefactoringDefinition otra) {
		return Objects.equal(getActions(), otra.getActions())
				&& Objects.equal(getCategories(), otra.getCategories())
				&& Objects.equal(getExamples(), otra.getExamples())
				&& Objects.equal(getImage(), otra.getImage())
				&& Objects.equal(getInputs(), otra.getInputs())
				&& Objects.equal(getMotivation(), otra.getMotivation())
				&& Objects.equal(getName(), otra.getName())
				&& Objects.equal(getDescription(), otra.getDescription())
				&& Objects.equal(getKeywords(), otra.getKeywords())
				&& Objects.equal(getPostconditions(), otra.getPostconditions())
				&& Objects.equal(getPreconditions(), otra.getPreconditions())
				&& Objects.equal(isEditable(), otra.isEditable());
	}

	@Override
	public String toString() {
		return getName();
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
	 * Obtiene todos los mecanismos de un tipo (precond, acciones y
	 * postcondiciones) para una refactorización.
	 * 
	 * @param type
	 *            tipo de mecanismo
	 * 
	 * @return todas las precondiciones, acciones y postcondiciones
	 */
	public List<RefactoringMechanismInstance> getAllTypeMechanisms(
			RefactoringMechanismType type) {
		switch (type) {
		case PRECONDITION:
			return getPreconditions();
		case ACTION:
			return getActions();
		default:
			return getPostconditions();
		}
	}

	/**
	 * Devuelve el mecanismo del tipo dado con el nombre pasado de la
	 * refactorización.
	 * 
	 * @param mechanismName
	 *            nombre del mecanismo a obtener
	 * @param type
	 *            tipo del mecanismo (precondición, acción o postcondición)
	 * @return mecanismo de la refactorización con el nombre pasado
	 */
	public List<RefactoringMechanismInstance> getMechanismsWithName(
			final String mechanismName, RefactoringMechanismType type) {
		Collection<RefactoringMechanismInstance> filtradas = Collections2
				.filter(getAllTypeMechanisms(type),
						new Predicate<RefactoringMechanismInstance>() {

							@Override
							public boolean apply(
									RefactoringMechanismInstance arg0) {
								return arg0.getClassName()
										.equals(mechanismName);
							}
						});
		return new ArrayList<RefactoringMechanismInstance>(filtradas);

	}

	/**
	 * Obtiene todos los mecanismos (precond, acciones y postcondiciones) de la
	 * refactorizacion.
	 * 
	 * 
	 * @return todas las precondiciones, acciones y postcondiciones
	 */
	public List<RefactoringMechanismInstance> getAllMechanisms() {
		final List<RefactoringMechanismInstance> lista = getPreconditions();
		lista.addAll(getActions());
		lista.addAll(getPreconditions());
		return lista;
	}

	/**
	 * Genera un builder de refactorizaciones preconfigurado con los parametros
	 * de la refactorizacion actual.
	 * 
	 * @return builder con los parametros de la refactorizacion
	 */
	public final Builder getBuilder() {
		return new Builder(getName()).actions(getActions())
				.categories(getCategories()).description(getDescription())
				.examples(getExamples()).image(getImage()).inputs(getInputs())
				.keywords(getKeywords()).motivation(getMotivation())
				.postconditions(getPostconditions())
				.preconditions(getPreconditions()).isEditable(isEditable());
	}

	/**
	 * Obtiene la ruta absoluta de la imagen en el sistema de ficheros.
	 * 
	 * @return ruta absoluta de la imagen
	 */
	public String getImageAbsolutePath() {
		return getRefactoringFileFullPath(getImage());
	}

	/**
	 * Obtiene ruta donde se guarda el fichero de definicion de la
	 * refactorizacion pasada.
	 * 
	 * @param refactName
	 *            nombre de la refactorizacion
	 * @return ruta donde se guarda la definicion de la refactorizacion
	 */
	public String getXmlRefactoringDefinitionFilePath() {
		return getRefactoringDirectoryFile().getPath() + File.separator
				+ getName() + RefactoringConstants.FILE_EXTENSION;

	}

	/**
	 * Obtiene un fichero cuya ruta sera la del directorio donde se guardara el
	 * fichero de definicion de la refactorizacion.
	 * 
	 * @param refactoringName
	 * 
	 * @param refact
	 *            nombre de la refactorizacion
	 * @return fichero con la ruta donde se guardara la definicion de la
	 *         refactorizacion
	 */
	public File getRefactoringDirectoryFile() {
		if (!isEditable()) {
			return new File(
					RefactoringPlugin.getNonEditableDynamicRefactoringsDir()
							+ File.separator + getName() + File.separator);
		}
		return new File(RefactoringPlugin.getDynamicRefactoringsDir()
				+ File.separator + getName() + File.separator);
	}

	/**
	 * Dado una ruta a un fichero devuelve esta misma si es una ruta absoluta o
	 * devuelve la ruta tomando como directorio base el directorio de la
	 * refactorizacion si es una ruta relativa.
	 * 
	 * @param filePath
	 *            ruta de un fichero
	 * @return ruta absoluta del fichero
	 */
	private String getRefactoringFileFullPath(String filePath) {
		if (filePath.isEmpty() || new File(filePath).isAbsolute()) {
			return filePath;
		}
		return getRefactoringDirectoryFile() + File.separator + filePath;
	}

	/**
	 * Constructor de definiciones de refactorizaciones siguiendo el patrón
	 * Builder.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 * 
	 */
	public static final class Builder {

		private boolean isEditable = false;
		private Set<Category> categories;
		private Set<String> keywords = new HashSet<String>();
		private List<RefactoringExample> examples = new ArrayList<RefactoringExample>();
		private String name;
		private String description;
		private String image = "";
		private String motivation;
		private List<InputParameter> inputs;
		private List<RefactoringMechanismInstance> preconditions;
		private List<RefactoringMechanismInstance> actions;
		private List<RefactoringMechanismInstance> postconditions;

		/**
		 * Crea un builder para crear una definicion de refactorizacion con el
		 * nombre dado.
		 * 
		 * @param refactoringName
		 *            nombre que tendra la refactorizacion que cree el builder
		 */
		public Builder(String refactoringName) {
			this.name = refactoringName;
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
			checkParameterNotNull(image, "image");
			checkParameterNotNull(examples, "examples");
			checkParameterNotNull(keywords, "keywords");

			// Preconditions.checkArgument(containsScopeCategory(definition.getCategories()),
			// "The refactoring must belong to at least one scope.");
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
		 * Establece el conjunto de categorías a las que el elemento va a
		 * pertenecer.
		 * 
		 * @param categories
		 *            categorias a las que el elemento pertenecera
		 * @return builder con las nuevas categorias agregadas
		 */
		public Builder categories(Set<Category> categories) {
			this.categories = categories;
			return this;
		}

		/**
		 * Asigna un nombre distinto al actual.
		 * 
		 * @param name
		 *            nuevo nombre que se asignara
		 * @return builder con el nuevo nombre agregado
		 */
		public Builder name(String name) {
			this.name = name.trim();
			return this;
		}

		/**
		 * Asigna el conjunto de palabras claves que describen la
		 * refactorización.
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
		 * Establece los ejemplos a la refactorización.
		 * 
		 * @param list
		 *            lista de arrays de cadenas con los atributos de cada
		 *            ejemplo. Cada array de cadenas contendrá dos cadenas, una
		 *            con la ruta del fichero que contiene el estado del ejemplo
		 *            antes de la refactorización, y otra con la ruta del que
		 *            contiene el estado después de la refactorización.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getExamples
		 */
		public Builder examples(List<RefactoringExample> list) {
			this.examples = list;
			return this;
		}

		/**
		 * Asigna el atributo editable de la refactorizacion que si no es
		 * asignado toma como valor por defecto false.
		 * 
		 * @param isEditable
		 *            si la refactorizacion sera editable
		 * @return devuelve el builder con el nuevo parametro
		 * @see DynamicRefactoringDefinition#isEditable()
		 */
		public Builder isEditable(boolean isEditable) {
			this.isEditable = isEditable;
			return this;
		}

		/**
		 * Asigna la descripción de la refactorización.
		 * 
		 * @param description
		 *            una cadena con la descripción.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getDescription
		 */
		public Builder description(String description) {
			this.description = description.trim();
			return this;
		}

		/**
		 * Asigna la ruta de la imagen asociada a la refactorización.
		 * 
		 * @param image
		 *            una cadena con la ruta a la imagen.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getImage
		 */
		public Builder image(String image) {
			if (image == null)
				image = "";
			this.image = image.trim();
			return this;
		}

		/**
		 * Establece la motivación de la refactorización.
		 * 
		 * @param motivation
		 *            una cadena con la motivación de la refactorización.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getMotivation
		 */
		public Builder motivation(String motivation) {
			this.motivation = motivation.trim();
			return this;
		}

		/**
		 * Asigna las entradas que se deben solicitar al usuario para construir
		 * la refactorización.
		 * 
		 * @param inputs
		 *            lista de cadenas con la información de esas entradas.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getInputs
		 */
		public Builder inputs(List<InputParameter> inputs) {
			this.inputs = inputs;
			return this;
		}

		/**
		 * Establece los nombres de las precondiciones de la refactorización.
		 * 
		 * @param preconditions
		 *            lista de cadenas con los nombres de las precondiciones.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getPreconditions
		 */
		public Builder preconditions(
				List<RefactoringMechanismInstance> preconditions) {
			this.preconditions = preconditions;
			return this;
		}

		/**
		 * Establece los nombres de las acciones de la refactorización.
		 * 
		 * @param actions
		 *            lista de cadenas con los nombres de las acciones.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getActions
		 */
		public Builder actions(List<RefactoringMechanismInstance> actions) {
			this.actions = actions;
			return this;
		}

		/**
		 * Establece los nombres de las postcondiciones de la refactorización.
		 * 
		 * @param postconditions
		 *            lista de cadenas con los nombres de las postcondiciones.
		 * @return devuelve el builder con el nuevo parametro
		 * 
		 * @see #getPostconditions
		 */
		public Builder postconditions(
				List<RefactoringMechanismInstance> postconditions) {
			this.postconditions = postconditions;
			return this;
		}

	}

}
