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
	 * Devuelve en la información relativa al elemento se encuentra 
	 * el texto pasado.
	 * 
	 * @param text
	 *            texto
	 * @return si en la información relativa al elemento se encuentra el texto
	 */
	boolean containsText(String text);
	
	/**
	 * Obtiene las categor�as a las que el elemento pertenece.
	 * 
	 * @return conjunto de categorias a las que el elemento pertenece.
	 */
	Set<Category> getCategories();

}
