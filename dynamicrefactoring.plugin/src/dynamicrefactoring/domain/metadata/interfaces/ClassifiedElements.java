package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Set;

/**
 * Clasificacion de un conjunto de elementos por categorias.
 * 
 * Cada categoria de la clasificacion contendra un conjunto de hijos.
 * 
 * @author imediava
 * 
 * @param <K>
 *            Tipo de los elementos que forman la categoria
 */
public interface ClassifiedElements<K> {



	/**
	 * Obtiene los hijos que corresponden a una categoria en la clasificacion.
	 * 
	 * @param category
	 *            categoria de la que se quieren obtener los hijos
	 * @return hijos de la categoria
	 */
	Set<K> getCategoryChildren(Category category);

	/**
	 * Obtiene la clasificacion que defina la estructura de los elementos
	 * clasificados.
	 * 
	 * @return Clasificacion por la que estos elementos estan organizados
	 */
	Classification getClassification();

}
