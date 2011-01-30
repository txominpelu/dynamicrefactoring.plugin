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
public final class SimpleUniLevelClassification implements Classification{

	private final Set<Category> categories;
	private final String name;
	private final String description;
	

	/**
	 * Crea una clasificacion de un solo nivel.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param categories
	 *            lista de categorias de la clasificacion
	 */
	public SimpleUniLevelClassification(String classificationName,
			Set<Category> categories) {
		this(classificationName, "", categories);
	}
	
	/**
	 * Crea una clasificacion de un solo nivel.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param categories
	 *            lista de categorias de la clasificacion
	 */
	public SimpleUniLevelClassification(String classificationName, String description,
			Set<Category> categories) {
		this.categories = categories;
		this.name = classificationName;
		this.description = description;
	}
	
	
	

	@Override
	public Set<Category> getCategories() {
		return categories;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Classification) {
			Classification otra = (Classification) o;
			return otra.getCategories().equals(getCategories())
					&& getName().equals(otra.getName()) && getDescription().equals(otra.getDescription());
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
		return Objects.hashCode(getCategories(),this.getName(),this.getDescription());
	}

	@Override
	public String toString() {
		return getName() + " (" + getDescription() +")" + "=" + getCategories();
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
