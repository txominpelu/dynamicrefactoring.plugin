package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * 
 * @author XPMUser
 *
 */
public class PostconditionCondition <K extends Element> implements Predicate<K> {

	public static final String NAME="postcondition";
	
	private String postcondition;
	
	public PostconditionCondition(String postcondition){
		this.postcondition=postcondition;
	}
	
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
