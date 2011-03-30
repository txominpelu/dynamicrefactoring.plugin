package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Set;


public interface Element {

	String getName();

	/**
	 * Devuelve si el elemento pertenece a la categoria pasada.
	 * 
	 * @param category
	 *            categoria
	 * @return si el elemento pertenece a la categoria
	 */
	boolean belongsTo(Category category);
	
	/**
	 * Devuelve si el elemento pertenece a la palabra clave pasada.
	 * 
	 * @param keyWord
	 *            palabra clave
	 * @return si el elemento pertenece a la palabra clave
	 */
	boolean belongsTo(String keyWord);

	/**
	 * Devuelve si en la información relativa al elemento se encuentra 
	 * el texto pasado.
	 * 
	 * @param text
	 *            texto
	 * @return verdadero si en la información relativa al elemento se encuentra el texto,
	 * falso en caso contrario
	 */
	boolean containsText(String text);
	
	/**
	 * Devuelve si el elemento tiene entre sus tipos de entrada 
	 * el indicado por el parámetro.
	 * 
	 * @param inputType tipo de entrada
	 * @return verdadero si tiene ese tipo de entrada, falso en caso contrario
	 */
	boolean containsInputType(String inputType);
	
	/**
	 * Devuelve si el elemento tiene como tipo de entrada raiz
	 * el indicado por el parámetro.
	 * 
	 * @param rootInputType tipo de entrada raiz
	 * @return verdadero si tiene ese tipo de entrada raiz, falso en caso contrario
	 */
	boolean containsRootInputType(String rootInputType);
	
	/**
	 * Devuelve si el elemento tiene como precondición
	 * la indicada por el parámetro.
	 * 
	 * @param precondition precondición
	 * @return verdadero si tiene ese esa precondición, falso en caso contrario
	 */
	boolean containsPrecondition(String precondition);
	
	/**
	 * Devuelve si el elemento tiene como acción
	 * la indicada por el parámetro.
	 * 
	 * @param action acción
	 * @return verdadero si tiene ese esa acción, falso en caso contrario
	 */
	boolean containsAction(String action);
	
	/**
	 * Devuelve si el elemento tiene como postcondición
	 * la indicada por el parámetro.
	 * 
	 * @param postcondition postcondición
	 * @return verdadero si tiene ese esa postcondición, falso en caso contrario
	 */
	boolean containsPostcondition(String postcondition);
	
	/**
	 * Obtiene las categoríaas a las que el elemento pertenece.
	 * 
	 * @return conjunto de categorias a las que el elemento pertenece.
	 */
	Set<Category> getCategories();

}
