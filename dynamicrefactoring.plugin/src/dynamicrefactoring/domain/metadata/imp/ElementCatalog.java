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

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedFilterableCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class ElementCatalog<K extends Element> implements ClassifiedFilterableCatalog<K> {

	/**
	 * Lista de condiciones que conforman el filtro actual aplicado
	 */
	private List<Predicate<K>> filter;

	/**
	 * Elementos clasificados.
	 */
	private Map<Category, Set<K>> classifiedElements;

	/**
	 * Nombre de la clasificacion que divide por categorias los elementos.
	 */
	private final String classificationName;

	/**
	 * Crea un catalog de elementos con los elementos que se le pasa y
	 * clasificando los elementos en las categorias pasadas.
	 * 
	 * @param allElements
	 *            elementos que componen el catalogo
	 * @param categories
	 *            conjunto de categorias que contiene la clasificacion en la que
	 *            se basa este catalogo
	 * @param classificationName nombre de la clasificacion
	 */
	public ElementCatalog(Set<K> allElements, Set<Category> categories,
			String classificationName) {
		this.filter = new ArrayList<Predicate<K>>();
		this.classificationName = classificationName;
		initializeClassifiedElements(categories);
		classify(allElements);
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
		for (Entry<Category, Set<K>> entry : this.classifiedElements
				.entrySet()) {
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


	@Override
	public void removeConditionFromFilter(Predicate<K> conditionToRemove) {
		Collection<K> toUnfilter = ImmutableList.copyOf(Collections2.filter(
				classifiedElements.get(Category.FILTERED_CATEGORY),Predicates.not(conditionToRemove)));
		classifiedElements.get(Category.FILTERED_CATEGORY)
				.removeAll(toUnfilter);
		classify(toUnfilter);
		this.filter.remove(conditionToRemove);
	}

	/**
	 * Elimina la condicion cuyo indice es el pasado.
	 * 
	 * @param index
	 *            indice de la condicion a eliminar
	 */
	@Override
	public void removeConditionFromFilter(int index) {
		Predicate<K> condition = getAllFilterConditions().get(index);
		this.removeConditionFromFilter(condition);
	}

	@Override
	public ClassifiedElements<K> getClassificationOfElements(boolean showFiltered) {
		HashMap<Category, Set<K>> toReturn = new HashMap<Category, Set<K>>(
				classifiedElements);
		if (!showFiltered) {
			toReturn.remove(Category.FILTERED_CATEGORY);
		}
		return new SimpleClassifiedElements<K>(this.classificationName, toReturn);
	}

	@Override
	public List<Predicate<K>> getAllFilterConditions() {
		return new ArrayList<Predicate<K>>(this.filter);
	}

	/**
	 * Devuelve el nombre de la clasificación.
	 * 
	 * @return una cadena con el nombre de la clasificación.
	 */
	public String getClassificationName() {
		return classificationName;
	}
}
