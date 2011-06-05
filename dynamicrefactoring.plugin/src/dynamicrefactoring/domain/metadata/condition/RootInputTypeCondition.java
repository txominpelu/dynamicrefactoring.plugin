package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Condición que indica si un elemento tiene un tipo
 * como el tipo de su entrada raíz.
 * 
 * @author XPMUser
 * @param <K> tipo del elemento
 *
 */
public final class RootInputTypeCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="rootInputType";
	
	private String rootInputType;
	
	/**
	 * Constructor.
	 * 
	 * @param rootInputType tipo
	 */
	public RootInputTypeCondition(String rootInputType){
		this.rootInputType=rootInputType;
	}
	
	/**
	 * Devuelve el tipo que define la condición.
	 * 
	 * @return tipo que define la condición
	 */
	public String getRootInputType(){
		return rootInputType;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsRootInputType(rootInputType);
	}
	
	/**
	 * Son iguales si ambas contienen el mismo tipo de entrada raiz.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por el mismo tipo de entrada raiz
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof RootInputTypeCondition){
			RootInputTypeCondition<?> otra = (RootInputTypeCondition<?>) o;
			return otra.getRootInputType().equals(rootInputType);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return rootInputType.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + rootInputType.toString();
	}
	
}
