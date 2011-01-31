package dynamicrefactoring.domain.metadata.imp;

import java.util.Set;

import com.google.common.base.Objects;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Clasificacion de un solo nivel. Solo tiene un nivel de categorias.
 * 
 * @author imediava
 * 
 */
public final class SimpleUniLevelClassification implements Classification {

	private final Set<Category> categories;
	private final String name;
	private final String description;
	/**
	 * Si la clasificacion admite o no elementos en mas de una categoria.
	 */
	private final boolean multicategory;

	/**
	 * Crea una clasificacion de un solo nivel y en la que los elementos solo
	 * pueden pertenecer a una categoria.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param description
	 *            descripcion de la clasificacion
	 * @param categories
	 *            lista de categorias de la clasificacion
	 */
	public SimpleUniLevelClassification(String classificationName,
			String description, Set<Category> categories) {
		this(classificationName, description, categories, false);
	}

	/**
	 * Crea una clasificacion de un solo nivel.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param description
	 *            descripcion de la clasificacion
	 * @param multicategory
	 *            si la clasificacion admite o no elementos en mas de una
	 *            categoria
	 * @param categories
	 *            lista de categorias de la clasificacion
	 */
	public SimpleUniLevelClassification(String classificationName,
			String description, Set<Category> categories, boolean multicategory) {
		this.categories = categories;
		this.name = classificationName;
		this.description = description;
		this.multicategory = multicategory;
	}

	@Override
	public Set<Category> getCategories() {
		return categories;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Si la clasificacion admite o no elementos en mas de una categoria.
	 * 
	 * @return verdadero si la clasificacion admite o no elementos en mas de una
	 *         categoria.
	 */
	@Override
	public boolean isMultiCategory() {
		return this.multicategory;
	}

	/**
	 * Dos clasificaciones son iguales si tienen las mismas categorias, el mismo
	 * nombre, la misma descripcion y ambas tienen el mismo criterio a la hora
	 * de permitir o no que sus elementos pertenezcan a mas de una categoria.
	 * 
	 * @param o
	 *            objeto a comparar
	 * @return verdadero si sus atributos son iguales
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Classification) {
			Classification otra = (Classification) o;
			return otra.getCategories().equals(getCategories())
					&& getName().equals(otra.getName())
					&& getDescription().equals(otra.getDescription())
					&& isMultiCategory() == otra.isMultiCategory();
		}
		return false;
	}

	/**
	 * Ensures that s1.equals(s2) implies that s1.hashCode()==s2.hashCode() for
	 * any two classification s1 and s2, as required by the general contract of
	 * Object.hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(getCategories(), this.getName(),
				this.getDescription(), this.multicategory);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getName())
				.add("description", getDescription())
				.add("multicategory", isMultiCategory())
				.add("categories", getCategories()).toString();
	}

	@Override
	public int compareTo(Classification o) {
		return name.compareTo(o.getName());
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
