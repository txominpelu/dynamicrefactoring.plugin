package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Condición por si un elemento contiene como entrada un tipo.
 * 
 * @author XPMUser
 * @param <K> tipo del elemento sobre el que se aplica la condición
 *
 */
public class InputTypeCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición
	 */
	public static final String NAME="inputType";
	
	private String inputType;
	
	/**
	 * Constructor.
	 * 
	 * @param inputType tipo de entrada
	 */
	public InputTypeCondition(String inputType){
		this.inputType=inputType;
	}
	
	/**
	 * Obtiene el tipo de entrada que define la condición.
	 * 
	 * @return tipo de entrada que define la condición
	 */
	public String getInputType(){
		return inputType;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsInputType(inputType);
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
