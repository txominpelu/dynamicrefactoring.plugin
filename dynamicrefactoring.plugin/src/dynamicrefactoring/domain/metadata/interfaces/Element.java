package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Set;


public interface Element {

	String getName();

	/**
	 * Devuelve si el elemento pertenece a la categoria pasada.
	 * 
	 * @param category
	 *            categoria
	 * @return si esta refactorizacion pertenece a la categoria
	 */
	boolean belongsTo(Category category);

}
