package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Element;

public class KeyWordCondition<K extends Element> implements Predicate<K> {

	public static final String NAME="key";
	
	private String keyWord;

	/**
	 * Crea una instancia de la condicion.
	 * 
	 * @param text texto que se buscara en la información relativa al elemento
	 */
	public KeyWordCondition(String keyWord){
		this.keyWord = keyWord.toLowerCase().trim();
	}

	private String getKeyWord() {
		return keyWord;
	}
	@Override
	public boolean apply(K arg0) {
		return arg0.belongsTo(keyWord);
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
		if (o instanceof KeyWordCondition){
			KeyWordCondition<?> otra = (KeyWordCondition<?>) o;
			return otra.getKeyWord().equals(keyWord);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return keyWord.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + keyWord;
	}

}

