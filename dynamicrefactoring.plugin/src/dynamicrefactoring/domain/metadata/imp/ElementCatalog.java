package dynamicrefactoring.domain.metadata.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

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
	 * Crea un catalog de elementos con los elementos que se le pasa con los filtros pasados y clasificando los elementos en las categorias pasadas.
	 * 
	 * @param allElements
	 *            elementos que componen el catalogo
	 * @param categories
	 *            conjunto de categorias que contiene la clasificacion en la que
	 *            se basa este catalogo
	 * @param filterConditionList condiciones de filtrado del nuevo catalogo de elementos
	 * @param classificationName
	 *            nombre de la clasificacion
	 */
	public ElementCatalog(Set<K> allElements, Classification classification,
			List<Predicate<K>> filterConditionList) {
		this.filter = new ArrayList<Predicate<K>>();
		this.classification = classification;
		initializeClassifiedElements(classification.getCategories());
		classify(allElements);
		for(Predicate<K> condition: filterConditionList){
			this.addConditionToFilter(condition);
		}
	}

	
	private void initializeClassifiedElements(
			Set<Category> classificationCategories) {
		this.classifiedElements = new HashMap<Category, Set<K>>();
		for (Category c : classificationCategories) {
			classifiedElements.put(c, new HashSet<K>());
		}
		classifiedElements.put(Category.NONE_CATEGORY, new HashSet<K>());
		classifiedElements.put(Category.FILTERED_CATEGORY, new HashSet<K>());
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
					elementsLeftWithNoCategory, new CategoryCondition<K>(
							category));
			classifiedElements.get(category).addAll(belongToCategory);
			elementsLeftWithNoCategory.removeAll(belongToCategory);
		}
		classifiedElements.get(Category.NONE_CATEGORY).addAll(
				elementsLeftWithNoCategory);
	}

	@Override
	public void addConditionToFilter(Predicate<K> condition) {
		for (Entry<Category, Set<K>> entry : this.classifiedElements.entrySet()) {
			if (!entry.getKey().equals(Category.FILTERED_CATEGORY)) {
				Collection<K> toFilter = new HashSet<K>(Collections2.filter(
						entry.getValue(), Predicates.not(condition)));
				entry.getValue().removeAll(toFilter);
				classifiedElements.get(Category.FILTERED_CATEGORY).addAll(
						toFilter);
			}
		}
		this.filter.add(condition);
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
		this.filter.remove(conditionToRemove);
		Collection<K> filter = ImmutableList.copyOf(Collections2.filter(
				classifiedElements.get(Category.FILTERED_CATEGORY),
				Predicates.not(getPredicateForAllConditions())));

		Collection<K> toUnfilter = ImmutableList.copyOf(Collections2.filter(classifiedElements.get(Category.FILTERED_CATEGORY),
				getPredicateForAllConditions()));

		classifiedElements.put(Category.FILTERED_CATEGORY, new HashSet<K>(
				filter));

		classify(toUnfilter);

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
	public ClassifiedElements<K> getClassificationOfElements(
			boolean showFiltered) {
		HashMap<Category, Set<K>> toReturn = new HashMap<Category, Set<K>>();
		if (!showFiltered) {
			toReturn.remove(Category.FILTERED_CATEGORY);
		}
		//Hacemos copias defensivas de los sets
		for(Category category: classifiedElements.keySet()){
			toReturn.put(category, ImmutableSet.copyOf(classifiedElements.get(category)));
		}
		return new SimpleClassifiedElements<K>(this.classification.getName(),
				toReturn);
	}

	@Override
	public List<Predicate<K>> getAllFilterConditions() {
		return new ArrayList<Predicate<K>>(this.filter);
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
	 * Obtiene todos los elementos del catalogo. Util para los tests.
	 * 
	 * @return todos los elementos contenidos en el catalogo.
	 */
	protected Set<K> getAllElements() {
		Set<K> allElements = new HashSet<K>();
		for (Category c : classifiedElements.keySet()) {
			allElements.addAll(classifiedElements.get(c));
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
				classification.getCategories());
	}
}
