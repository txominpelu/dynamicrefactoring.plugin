package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Set;

/**
 * Define todas las subcategorias que contiene la clasificacion.
 * 
 * @author imediava
 * 
 */
public interface Classification {

	/**
	 * Obtiene un conjunto con las categorias de la clasificacion.
	 * 
	 * @return lista de categorias y subcategorias si las hubiera de la
	 *         clasificacion
	 */
	Set<Category> getCategories();

	/**
	 * Obtiene el nombre de la clasificacion.
	 * 
	 * @return nombre de la clasificacion
	 */
	String getName();

	/**
	 * Dos clasificacion seran iguales si tienen el mismo nombre y las mismas
	 * categorias.
	 * 
	 * @param o
	 *            objeto a comparar
	 * @return verdadero si las classificaciones tienen el mismo nombre y las
	 *         mismas categorias falso en caso contrario
	 */
	boolean equals(Object o);

}
