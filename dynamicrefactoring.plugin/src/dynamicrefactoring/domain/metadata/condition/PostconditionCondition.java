package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Condición por si el elemento contiene una
 * postcondición.
 * 
 * @author XPMUser
 * @param <K> tipo del elemento
 *
 */
public final class PostconditionCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="postcondition";
	
	private String postcondition;
	
	/**
	 * Constructor.
	 * 
	 * @param postcondition nombre de la postcondición
	 */
	public PostconditionCondition(String postcondition){
		this.postcondition=postcondition;
	}
	
	/**
	 * Obtiene la postcondición que define la condición.
	 * @return postcondición que define la condición
	 */
	public String getPostcondition(){
		return postcondition;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsPostcondition(postcondition);
	}
	
	/**
	 * Son iguales si ambas contienen la misma postcondición.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma postcondición
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof PostconditionCondition){
			PostconditionCondition<?> otra = (PostconditionCondition<?>) o;
			return otra.getPostcondition().equals(postcondition);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return postcondition.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + postcondition.toString();
	}
}
