package dynamicrefactoring.domain.metadata.imp;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;

public final class SimpleClassifiedElements<K> implements ClassifiedElements<K> {

	private final Map<Category, Set<K>> classifiedElements;
	private final Classification classification;

	/**
	 * Crea una categoria a partir de su nombre y su conjunto de categorias
	 * junto con los hijos que estas tienen.
	 * 
	 * @param name
	 *            nombre de la clasificacion
	 * @param subcategories
	 *            subcategorias con sus hijos
	 */
	public SimpleClassifiedElements(Classification classification,
			Map<Category, Set<K>> classifiedElements) {
		this.classification = classification;
		this.classifiedElements = ImmutableMap.copyOf(classifiedElements);
	}

	/**
	 * Conjunto de hijos de la categoria.
	 */
	@Override
	public Classification getClassification() {
		return this.classification;
	}

	@Override
	public Set<K> getCategoryChildren(Category category) {
		return classifiedElements.get(category);
	}

	/**
	 * Dos elementos clasificados son iguales si 
	 * ambos son instancias de esta clase y ambos tienen
	 * las mismas categorias y dichas
	 * categorias tienen los mismos elementos.
	 * 
	 * @param objeto
	 *            a comparar con el actual
	 * @return verdadero si las dos son clasificaciones y tienen las mismas
	 *         categorias y las categorias tienen los mismos hijos
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SimpleClassifiedElements) {
			SimpleClassifiedElements<?> otra = (SimpleClassifiedElements<?>) o;
			return getClassification().equals(otra.getClassification())
					&& classifiedElements.equals(otra.classifiedElements);

		}
		return false;
	}

	/**
	 * De acuerdo a la definicion de equals de este objeto y de acuerdo a los
	 * convenciones de Object.
	 * 
	 * @return hashcode del objeto
	 */
	@Override
	public int hashCode(){
		return Objects.hashCode(getClassification(), classifiedElements.hashCode());
	}

	@Override
	public String toString() {
		return classifiedElements.toString();
	}

}
