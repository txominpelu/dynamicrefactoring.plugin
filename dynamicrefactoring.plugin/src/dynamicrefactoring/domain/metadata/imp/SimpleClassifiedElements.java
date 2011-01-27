package dynamicrefactoring.domain.metadata.imp;

import java.util.Map;
import java.util.Set;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;

public final class SimpleClassifiedElements<K> implements ClassifiedElements<K> {

	private final Map<Category, Set<K>> classifiedElements;
	private final SimpleUniLevelClassification classification;

	/**
	 * Crea una categoria a partir de su nombre y su conjunto de categorias
	 * junto con los hijos que estas tienen.
	 * 
	 * @param name
	 *            nombre de la clasificacion
	 * @param subcategories
	 *            subcategorias con sus hijos
	 */
	public SimpleClassifiedElements(String name,
			Map<Category, Set<K>> classifiedElements) {
		this.classification = new SimpleUniLevelClassification(name,
				classifiedElements.keySet());
		this.classifiedElements = classifiedElements;
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
	 * Dos clasificaciones son iguales si tienen las mismas categorias y dichas
	 * categorias tienen los mismos elementos.
	 * 
	 * @param objeto
	 *            a comparar con el actual
	 * @return verdadero si las dos son clasificaciones y tienen las mismas
	 *         categorias y las categorias tienen los mismos hijos
	 */
	@Override
	public boolean equals(Object o){
		if(o instanceof ClassifiedElements){
			ClassifiedElements<K> otra = (ClassifiedElements<K>) o;
			return getClassification().equals(otra.getClassification());
			
		}
		return false;
	}

	@Override
	public String toString() {
		return classifiedElements.toString();
	}

}
