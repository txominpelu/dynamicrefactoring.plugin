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
	 * @param oldRefactoringName nombre de la refactorizacion a modificar
	 * @param refactoring refactorizacion a actualizar
	 */
	void updateRefactoring(String oldRefactoringName,
			DynamicRefactoringDefinition refactoring);

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

	/**
	 * Elimina la refactorizacion con el nombre dado del
	 * catalogo.
	 * 
	 * Saltara {@link IlegalArgumentException} si no existe
	 * la refactorizacion en el catalogo. 
	 * 
	 * @param refactoringName nombre de la refactorizacion a eliminar
	 */
	void removeRefactoring(String refactoringName);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus tipos de entrada el indicado por el parámetro.
	 * 
	 * @param inputType tipo de entrada
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsInputType(String inputType);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene como tipo de entrada raiz el indicado por el parámetro.
	 * 
	 * @param rootInputType tipo de entrada de la entrada raiz
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsRootInputType(String rootInputType);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus precondiciones la precondición indicada por el parámetro.
	 * 
	 * @param precondition precondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsPrecondition(String precondition);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus acciones la acción indicada por el parámetro.
	 * 
	 * @param action acción
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsAction(String action);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus postcondiciones la postcondición indicada por el parámetro.
	 * 
	 * @param postcondition postcondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsPostcondition(String postcondition);
	

	

}
