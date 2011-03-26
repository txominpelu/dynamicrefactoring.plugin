package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * 
 * @author XPMUser
 *
 */
public class ActionCondition <K extends Element> implements Predicate<K> {

	public static final String NAME="action";
	
	private String action;
	
	public ActionCondition(String action){
		this.action=action;
	}
	
	public String getAction(){
		return action;
	}
	
	/**
	 * Son iguales si ambas contienen la misma acción.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma acción
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof ActionCondition){
			ActionCondition<?> otra = (ActionCondition<?>) o;
			return otra.getAction().equals(action);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return action.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + action.toString();
	}
}
