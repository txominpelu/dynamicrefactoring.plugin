package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Condicion que indica si un elemento contiene una acción.
 * 
 * @author imediava
 * 
 * @param <K> tipo de elemento de la condicion 
 *
 */
public class ActionCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre.
	 */
	public static final String NAME="action";
	
	private String action;
	
	/**
	 * Constructor.
	 * 
	 * @param action nombre de la acción
	 */
	public ActionCondition(String action){
		this.action=action;
	}
	
	/**
	 * Obtiene la acción que define la condición.
	 * 
	 * @return acción que define la condición
	 */
	public String getAction(){
		return action;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsAction(action);
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
