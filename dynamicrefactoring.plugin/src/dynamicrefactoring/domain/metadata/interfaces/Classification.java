package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Set;

/**
 * Define todas las subcategorias que contiene la clasificacion.
 * 
 * @author imediava
 * 
 */
public interface Classification extends Comparable<Classification>{

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
	 * Dos clasificaciones son iguales si tienen las mismas categorias,
	 * el mismo nombre, la misma descripcion y ambas tienen el mismo
	 * criterio a la hora de permitir o no que sus elementos
	 * pertenezcan a mas de una categoria.
	 * 
	 * @param o objeto a comparar
	 * @return verdadero si sus atributos son iguales
	 */
	boolean equals(Object o);

	/**
	 * Obtener la descripcion de la clasificacion.
	 * 
	 * @return descripcion de la clasificacion
	 */
	String getDescription();

	/**
	 * Si la clasificacion admite o no elementos en mas de una categoria.
	 * 
	 * @return verdadero si la clasificacion admite o no elementos en mas de una categoria.
	 */
	boolean isMultiCategory();

}
