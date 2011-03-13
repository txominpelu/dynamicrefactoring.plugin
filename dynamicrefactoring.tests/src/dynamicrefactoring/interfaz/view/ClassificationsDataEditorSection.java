package dynamicrefactoring.interfaz.view;

import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.Catalog;

public class ClassificationsDataEditorSection {

	/**
	 * Nombre de la clasificacion a editar.
	 */
	private String classification;
	
	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private Catalog catalog;

	/**
	 * Crea un editor de datos de una clasificacion.
	 * @param classification nombre de la clasificacion a editar
	 * @param catalog catalogo de clasificaciones
	 */
	public ClassificationsDataEditorSection(String classification, Catalog catalog) {
		this.classification = classification;
		this.catalog = catalog;
		
	}

	/**
	 * Cambia el nombre de una categoría dentro de
	 * la clasificacion.
	 * 
	 * @param oldName
	 * @param newName
	 */
	public void renameCategory(String oldName, String newName) {
		catalog.renameCategory(classification, oldName, newName);
	}

	/**
	 * Obtiene la clasificacion que se esta editando
	 * en la interfaz.
	 * 
	 * @return clasificacion en edicion
	 */
	public Classification getClassification() {
		return catalog.getClassification(classification);
	}

	/**
	 * Se encarga de agregar una categoria a la clasificacion
	 * en edicion.
	 * 
	 * @param categoryNewName nombre de la nueva categoria
	 */
	public void addCategory(String categoryNewName) {
		catalog.addCategory(classification, categoryNewName);
	}

	/**
	 * Se encarga de eliminar una categoria de la clasificacion
	 * en edicion.
	 * 
	 * Si la categoria no existe en la clasificacion saltara
	 * una excepcion.
	 * 
	 * @param categoryName	nombre de la categoria a eliminar
	 */
	public void removeCategory(String categoryName) {
		catalog.removeCategory(classification, categoryName);
		
	}

}
