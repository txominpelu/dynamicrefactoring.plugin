package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

public class TextCondition<K extends Element> implements Predicate<K> {
	
	public static final String NAME="text";
	
	private String text;

	/**
	 * Crea una instancia de la condicion.
	 * 
	 * @param text texto que se buscara en la información relativa al elemento
	 */
	public TextCondition(String text){
		this.text = text.toLowerCase().trim();
	}

	private String getText() {
		return text;
	}
	
	/**
	 * Devuelve si la información relativa al elemento contiene el texto
	 * con el que se definió la condición.
	 * 
	 * @param arg0 elemento en el que se realiza la búsqueda
	 */
	@Override
	public boolean apply(Element arg0) {
		return arg0.containsText(text);
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
		if (o instanceof TextCondition){
			TextCondition<?> otra = (TextCondition<?>) o;
			return otra.getText().equals(text);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return text.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + text;
	}

}
