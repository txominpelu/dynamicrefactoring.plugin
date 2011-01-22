package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Devuelve
 * 
 * @author imediava
 * 
 */
public class CategoryCondition<K extends Element> implements Predicate<K> {

	private Category category;
	
	public Category getCategory() {
		return category;
	}

	public CategoryCondition(Category category) {
		this.category = category;
	}

	public CategoryCondition(String categoryName) {
		this.category = new Category(categoryName);
	}

	@Override
	public boolean apply(K arg0) {
		return arg0.belongsTo(getCategory());
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
			return otra.getCategory().equals(getCategory());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return getCategory().getName().hashCode();
	}

	

}
