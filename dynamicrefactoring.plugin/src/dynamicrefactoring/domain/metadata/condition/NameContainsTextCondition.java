package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

public class NameContainsTextCondition<K extends Element> implements Predicate<K> {
	
	private String text;

	/**
	 * Crea una instancia de la condicion.
	 * 
	 * @param text texto que se buscara en el nombre del elemento
	 */
	public NameContainsTextCondition(String text){
		this.text = text;
	}

	/**
	 * Devuelve si el nombre del elemento contiene el texto
	 * con el que se definio la condicion.
	 * 
	 * @param arg0 elemento en el que se buscara si el nombre contiene el texto
	 */
	@Override
	public boolean apply(Element arg0) {
		return arg0.getName().contains(getText());
	}

	/**
	 * Son iguales si ambas contienen el mismo texto.
	 * 
	 * @param otra
	 *            a comparar
	 * @return verdadero si filtran por el mismo texto
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof NameContainsTextCondition){
			NameContainsTextCondition<?> otra = (NameContainsTextCondition<?>) o;
			return otra.getText().equals(getText());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return getText().hashCode();
	}

	private String getText() {
		return text;
	}

}
