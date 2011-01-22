package dynamicrefactoring.domain.metadata.interfaces;

import java.util.List;

import com.google.common.base.Predicate;

public interface ClassifiedFilterableCatalog<K> {

	/**
	 * Agrega un condicion al filtro actual que se esta aplicando al catalogo.
	 * 
	 * @param filter
	 */
	void addConditionToFilter(Predicate<K> filter);

	/**
	 * Elimina una condicion ya existente en el filtro actual aplicado al
	 * catalogo. La funcionalida es equivalente a removeConditionFromFilter
	 * pasando la propia condicion.
	 * 
	 * @param index
	 *            indice de la coleccion a
	 */
	void removeConditionFromFilter(int index);

	/**
	 * Elimina una condicion ya existente en el filtro actual aplicado al
	 * catalogo.
	 * 
	 * @param conditionToRemove
	 *            condicion a eliminar
	 */
	void removeConditionFromFilter(Predicate<K> conditionToRemove);

	/**
	 * Obtiene con todos las condiciones que componen el filtro.
	 * 
	 * @return lista con todas las condiciones del filtro
	 */
	List<Predicate<K>> getAllFilterConditions();

	/**
	 * Devuelve el conjunto de los elementos que contiene el catalogo
	 * clasificado por categorias.
	 * 
	 * @param showFiltered
	 *            si es false no mostrara el grupo <Filtered>
	 */
	ClassifiedElements<K> getClassificationOfElements(boolean showFiltered);



}
