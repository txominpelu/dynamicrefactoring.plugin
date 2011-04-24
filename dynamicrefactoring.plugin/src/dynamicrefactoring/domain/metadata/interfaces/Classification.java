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

	/**
	 * Determina si la clasificación contiente la categoria pasada parámetro.
	 * 
	 * @param categoria
	 * @return verdadero si la clasificación contien la categoria, falso en
	 *         caso contrario.
	 */
	boolean containsCategory(Category cat);

	/**
	 * Devuelve una clasificacion copia de la actual pero
	 * en la que se ha sustituido el nombre de la categoria
	 * oldName por newName
	 * 
	 * @param oldName nombre actual de la categoria a cambiar
	 * @param newName nuevo nombre
	 * @return clasificacion con los cambios aplicados
	 */
	Classification renameCategory(String oldName, String newName);

	/**
	 * Devuelve una clasificacion copia de la actual pero en la que se ha
	 * añadido la nueva categoria.
	 * 
	 * @param category
	 *            categoria a agregar
	 * @return clasificacion con los cambios aplicados
	 */
	Classification addCategory(Category category);

	/**
	 * Devuelve una clasificacion copia de la actual pero
	 * en la que se ha eliminado la categoria.
	 * 
	 * @param category categoria a agregar
	 * @return clasificacion con los cambios aplicados
	 */
	Classification removeCategory(Category category);

	/**
	 * Obtiene una copia de la clasificacion actual con el nombre cambiado. Esto
	 * tambien significa que todas las refactorizaciones que pertenecian a ella
	 * cambia su padre al nuevo nombre de clasificacion.
	 * 
	 * @param clasifNewName
	 *            nuevo nombre de la clasificacion
	 * @return copia de la clasificacion actual con un nuevo nombre
	 */
	Classification rename(String clasifNewName);
	
	/**
	 * Obtiene si la clasificacion es editable o es de 
	 * sólo lectura.
	 * 
	 * @return verdadero si la clasificacion se puede editar
	 */
	boolean isEditable();
}
