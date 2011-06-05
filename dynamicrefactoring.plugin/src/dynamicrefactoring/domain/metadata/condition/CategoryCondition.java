package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Devuelve
 * 
 * @author imediava
 * @param <K> tipo de la condición
 * 
 */
public class CategoryCondition<K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="category";
	
	private Category category;

	/**
	 * Constructor por categoría.
	 * 
	 * @param category categoría
	 */
	public CategoryCondition(Category category) {
		this.category = category;
	}

	
	/**
	 * Constructor por los elementos que definen una categoría.
	 * 
	 * @param parent nombre de la clasificación padre de la categoría
	 * @param categoryName nombre de la categoría
	 */
	public CategoryCondition(String parent, String categoryName) {
		this.category = new Category(parent.trim(), categoryName.trim());
	}

	/**
	 * Obtiene la categoría que define la condición.
	 * 
	 * @return categoría que define la condición
	 */
	public Category getCategory() {
		return category;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.belongsTo(category);
	}
	
	/**
	 * Son iguales si ambas contienen la misma categoria.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma categoria
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof CategoryCondition){
			CategoryCondition<?> otra = (CategoryCondition<?>) o;
			return otra.getCategory().equals(category);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return category.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + category.toString();
	}

	

}
