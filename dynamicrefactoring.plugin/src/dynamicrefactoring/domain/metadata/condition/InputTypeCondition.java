package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * 
 * @author XPMUser
 *
 */
public class InputTypeCondition <K extends Element> implements Predicate<K> {

	public static final String NAME="inputType";
	
	private String inputType;
	
	public InputTypeCondition(String inputType){
		this.inputType=inputType;
	}
	
	public String getInputType(){
		return inputType;
	}
	
	/**
	 * Son iguales si ambas contienen el mismo tipo de entrada.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por el mismo tipo de entrada
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof InputTypeCondition){
			InputTypeCondition<?> otra = (InputTypeCondition<?>) o;
			return otra.getInputType().equals(inputType);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return inputType.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + inputType.toString();
	}
}
