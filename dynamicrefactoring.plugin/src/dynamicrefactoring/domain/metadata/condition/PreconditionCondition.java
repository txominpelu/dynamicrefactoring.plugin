package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Condición por si el elemento contiene una
 * precondición.
 * 
 * @author XPMUser
 * @param <K> tipo del elemento
 *
 */
public class PreconditionCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición
	 */
	public static final String NAME="precondition";
	
	private String precondition;
	
	/**
	 * Constructor.
	 * 
	 * @param precondition precondición
	 */
	public PreconditionCondition(String precondition){
		this.precondition=precondition;
	}
	
	/**
	 * Obtiene la precondición que define la condición.
	 * 
	 * @return precondición que define la condición
	 */
	public String getPrecondition(){
		return precondition;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsPrecondition(precondition);
	}
	
	/**
	 * Son iguales si ambas contienen la misma precondición.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma precondición
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof PreconditionCondition){
			PreconditionCondition<?> otra = (PreconditionCondition<?>) o;
			return otra.getPrecondition().equals(precondition);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return precondition.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + precondition.toString();
	}
}
