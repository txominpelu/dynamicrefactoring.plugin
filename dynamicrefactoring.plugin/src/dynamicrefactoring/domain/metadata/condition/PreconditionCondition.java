package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * 
 * @author XPMUser
 *
 */
public class PreconditionCondition <K extends Element> implements Predicate<K> {

	public static final String NAME="precondition";
	
	private String precondition;
	
	public PreconditionCondition(String precondition){
		this.precondition=precondition;
	}
	
	public String getPrecondition(){
		return precondition;
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
