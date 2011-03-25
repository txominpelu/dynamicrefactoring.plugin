package dynamicrefactoring.domain;

import java.util.Set;


public interface RefactoringsCatalog {

	/**
	 * Obtiene la refactorizacion del catalogo cuyo nombre
	 * es el pasado.
	 * 
	 * Saltara {@link IllegalArgumentException} si {@link #hasRefactoring(String)}
	 * devuelve falso para el nombre pasado, es decir, si no existe una refactorizacion
	 * con dicho nombre en el catalogo.
	 * 
	 * @param refactName
	 * @return definicion de la refactorizacion
	 */
	DynamicRefactoringDefinition getRefactoring(String refactName);

	/**
	 * Obtiene si existe una refactorizacion con el nombre
	 * pasado en el catalogo.
	 * 
	 * @param name nombre de la refactorizacion a comprobar
	 * @return verdadero si existe una refactorizacion con dicho nombre en el catalogo
	 */
	boolean hasRefactoring(String name);
	

	/**
	 * Actualiza los datos de la refactorizacion en el catálogo.
	 * 
	 * Basicamente reemplaza la refactorizacion con el mismo nombre
	 * que la actual por la que se pasa.
	 * 
	 * Saltara {@link IlegalArgumentException} si la refactorizacion
	 * no existe en el catalogo. 
	 * 
	 * @param refactoring refactorizacion a actualizar
	 */
	void updateRefactoring(DynamicRefactoringDefinition refactoring);

	/**
	 * Agrega la siguiente refactorizacion al catalogo.
	 * 
	 * Saltara {@link IlegalArgumentException} si una refactorizacion
	 * con el mismo nombre ya existe en el catalogo. 
	 * 
	 * @param refactoring refactorizacion a agregar
	 */
	void addRefactoring(DynamicRefactoringDefinition refactoring);

	/**
	 * Obtiene el conjunto de todas las refactorizaciones contenidas
	 * en el catalogo.
	 * 
	 * @return conjunto de refactorizaciones contenidas en el catalogo
	 */
	Set<DynamicRefactoringDefinition> getAllRefactorings();

}
