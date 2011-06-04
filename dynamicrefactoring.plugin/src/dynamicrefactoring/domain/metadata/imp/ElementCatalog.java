package dynamicrefactoring.domain.metadata.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedFilterableCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public final class ElementCatalog<K extends Element> implements
		ClassifiedFilterableCatalog<K> {

	/**
	 * Lista de condiciones que conforman el filtro actual aplicado
	 */
	private List<Predicate<K>> filter;

	/**
	 * Elementos clasificados.
	 */
	private Map<Category, Set<K>> classifiedElements;
	
	/**
	 * Elementos clasificados filtrados.
	 */
	private Map<Category, Set<K>> filteredClassifiedElements;

	/**
	 * Clasificacion que divide por categorias los elementos.
	 */
	private final Classification classification;

	/**
	 * Crea un catalogo de elementos con los elementos que se le pasa sin ningun
	 * filtro y clasificando los elementos en las categorias pasadas.
	 * 
	 * @param allElements
	 *            elementos que componen el catalogo
	 * @param categories
	 *            conjunto de categorias que contiene la clasificacion en la que
	 *            se basa este catalogo
	 * @param classificationName
	 *            nombre de la clasificacion
	 */
	public ElementCatalog(Set<K> allElements, Classification classification) {
		this(allElements, classification, new ArrayList<Predicate<K>>());

	}

	/**
	 * Crea un catalog de elementos con los elementos que se le pasa con los
	 * filtros pasados y clasificando los elementos en las categorias pasadas.
	 * 
	 * @param allElements
	 *            elementos que componen el catalogo
	 * @param categories
	 *            conjunto de categorias que contiene la clasificacion en la que
	 *            se basa este catalogo
	 * @param filterConditionList
	 *            condiciones de filtrado del nuevo catalogo de elementos
	 * @param classificationName
	 *            nombre de la clasificacion
	 */
	public ElementCatalog(Set<K> allElements, Classification classification,
			List<Predicate<K>> filterConditionList) {
		Preconditions
				.checkArgument(classification.isMultiCategory()
								|| !hasMultiCategoryElements(allElements,
										classification.getCategories()),
						"Some of the elements belong to more than one category" + "" +
								" though the classification doesn't allow multiple-category elements");
		this.filter = new ArrayList<Predicate<K>>();
		this.classification = classification;
		initializeClassifiedElements(classification.getCategories());
		initializeFilteredClassifiedElements(classification.getCategories());
		classify(allElements);
		for(Predicate<K> condition : filterConditionList) {
			addConditionToFilter(condition);
		}
	}

	/**
	 * Devuelve si alguno de los elementos pertenece a mas de una de las
	 * categorias.
	 * 
	 * @param allElements
	 *            conjunto de elementos
	 * @param categories
	 *            conjunto de categorias
	 * @return si alguno de los elementos pertenece a mas de una de las
	 *         categorias
	 */
	private boolean hasMultiCategoryElements(Set<K> allElements,
			Set<Category> categories) {
		for (K element : allElements) {
			if (Sets.intersection(element.getCategories(), categories).size() > 1) {
				return true;
			}
		}
		return false;
	}

	private void initializeClassifiedElements(
			Set<Category> classificationCategories) {
		classifiedElements = new HashMap<Category, Set<K>>();
		for(Category c : classificationCategories) {
			classifiedElements.put(c, new HashSet<K>());
		}
		classifiedElements.put(Category.NONE_CATEGORY, new HashSet<K>());
	}

	private void initializeFilteredClassifiedElements(
			Set<Category> classificationCategories) {
		filteredClassifiedElements = new HashMap<Category, Set<K>>();
		for(Category c : classificationCategories) {
			filteredClassifiedElements.put(c, new HashSet<K>());
		}
		filteredClassifiedElements.put(Category.NONE_CATEGORY, new HashSet<K>());
	}
	
	/**
	 * Clasifica los elementos pasados dentro de las categorias definidas en el
	 * catalogo.
	 * 
	 * @param elementsToClassify
	 */
	private void classify(Collection<K> elementsToClassify) {
		ArrayList<K> elementsLeftWithNoCategory = new ArrayList<K>(
				elementsToClassify);
		for (Category category : classifiedElements.keySet()) {
			Collection<K> belongToCategory = Collections2.filter(
					elementsToClassify, new CategoryCondition<K>(category));
			classifiedElements.get(category).addAll(belongToCategory);
			elementsLeftWithNoCategory.removeAll(belongToCategory);
		}
		classifiedElements.get(Category.NONE_CATEGORY).addAll(
				elementsLeftWithNoCategory);
	}

	@Override
	public void addConditionToFilter(Predicate<K> condition) {
		for (Entry<Category, Set<K>> entry : classifiedElements.entrySet()) {
				Collection<K> toFilter = new HashSet<K>(Collections2.filter(
						entry.getValue(), Predicates.not(condition)));
				classifiedElements.get(entry.getKey()).removeAll(toFilter);
				filteredClassifiedElements.get(entry.getKey()).addAll(toFilter);
		}
		filter.add(condition);
	}

	/**
	 * Devuelve un predicado con todas las condiciones del filtro juntas.
	 * 
	 * @return predicado con todas las condiciones
	 */
	private Predicate<K> getPredicateForAllConditions() {
		return Predicates.and(filter);
	}

	@Override
	public void removeConditionFromFilter(Predicate<K> conditionToRemove) {	
		//comprobamos si cada uno sigue estando filtrado con el filtro resultante
		//de haber eliminado la condicion que se va a eliminar
		filter.remove(conditionToRemove);
		for (Entry<Category, Set<K>> entry : filteredClassifiedElements.entrySet()) {
			Collection<K> toUnFilter = new HashSet<K>(Collections2.filter(
					entry.getValue(), getPredicateForAllConditions()));
			filteredClassifiedElements.get(entry.getKey()).removeAll(toUnFilter);
			classifiedElements.get(entry.getKey()).addAll(toUnFilter);
		}
		

	}

	/**
	 * Genera un catalogo nuevo con los mismos elementos y categorias pero
	 * inicialmente ninguna condicion.
	 * 
	 * @return catalogo con los mismos elementos y clasificaciones que el actual
	 *         pero sin condiciones
	 */
	@Override
	public ClassifiedFilterableCatalog<K> removeAllFilterConditions() {
		return new ElementCatalog<K>(getAllElements(), classification);
	}

	@Override
	public ClassifiedElements<K> getClassificationOfElements() {
		HashMap<Category, Set<K>> toReturn = new HashMap<Category, Set<K>>();

		// Hacemos copias defensivas de los sets
		for (Category category : classifiedElements.keySet()) {
			toReturn.put(category,
					ImmutableSet.copyOf(classifiedElements.get(category)));
		}
		
		return new SimpleClassifiedElements<K>(
				new SimpleUniLevelClassification(classification.getName(),
						classification.getDescription(), toReturn.keySet()),
				toReturn);
	}

	@Override
	public ClassifiedElements<K> getClassificationOfFilteredElements() {
		HashMap<Category, Set<K>> toReturn = new HashMap<Category, Set<K>>();

		// Hacemos copias defensivas de los sets
		for (Category category : filteredClassifiedElements.keySet()) {
			toReturn.put(category,
					ImmutableSet.copyOf(filteredClassifiedElements.get(category)));
		}
		
		return new SimpleClassifiedElements<K>(
				new SimpleUniLevelClassification(classification.getName(),
						classification.getDescription(), toReturn.keySet()),
				toReturn);
	}
	
	@Override
	public List<Predicate<K>> getAllFilterConditions() {
		return new ArrayList<Predicate<K>>(filter);
	}

	/**
	 * Construye una catálogo con los mismos filtros y elementos que el actual
	 * pero con una clasificacion distinta.
	 * 
	 * @param classification
	 *            clasificacion a aplicar al nuevo catalogo
	 * @return nuevo catalogo en el que solo cambia que se le aplica una nueva
	 *         clasificacion
	 */
	@Override
	public ClassifiedFilterableCatalog<K> newInstance(
			Classification classification) {
		return new ElementCatalog<K>(getAllElements(), classification, filter);
	}

	/**
	 * Obtiene todos los elementos del catalogo. 
	 * 
	 * @return todos los elementos contenidos en el catalogo.
	 */
	protected Set<K> getAllElements() {
		Set<K> allElements = new HashSet<K>();
		for (Category c : classifiedElements.keySet()) {
			allElements.addAll(classifiedElements.get(c));
		}
		for (Category c : filteredClassifiedElements.keySet()) {
			allElements.addAll(filteredClassifiedElements.get(c));
		}
		
		return allElements;
	}

	/**
	 * Devuelve la clasificación que divide en categorias los elementos de este
	 * catalogo.
	 * 
	 * @return clasificacion que divide en categorias el catalogo.
	 */
	@Override
	public Classification getClassification() {
		return new SimpleUniLevelClassification(classification.getName(),
				classification.getDescription(), classification.getCategories());
	}

	/**
	 * Determina si se encuentra vacio el filtro de condiciones.
	 * 
	 * @return devuelve verdadero si el filtro se encuentra vacio de condiciones, 
	 * falso en caso contrario.
	 */
	@Override
	public boolean isEmptyFilter() {
		return filter.isEmpty();
	}

	/**
	 * Determina si existen elementos filtrados.
	 * 
	 * @return devuelve verdadero si exiten elementos filtrados, falso en caso contrario.
	 */
	@Override
	public boolean hasFilteredElements() {
		Set<K> allFilteredElements = new HashSet<K>();
		for (Category c : filteredClassifiedElements.keySet()) {
			allFilteredElements.addAll(filteredClassifiedElements.get(c));
		}
		return !allFilteredElements.isEmpty();
	}
}
