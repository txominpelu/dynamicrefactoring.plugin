package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Set;

/**
 * Catalogo que contiene las clasificaciones existentes en la aplicacion y
 * provee de metodos para editarlas y acceder a sus datos.
 * 
 * @author imediava
 * 
 */
public interface ClassificationsCatalog {

	/**
	 * Obtiene la clasificacion con el nombre dado. Antes debe haberse
	 * comprobado que existe con {@link #containsClassification(String)}
	 * 
	 * @param name
	 *            nombre de la clasificacion
	 * @return clasificacion con el nombre dado
	 * @throws IllegalArgumentException
	 *             si {@link #hasClassification(String)} es falso para el nombre
	 *             dado
	 */
	Classification getClassification(String name);

	/**
	 * Devuelve si el catalogo contiene alguna clasificacion con el nombre dado.
	 * (Los nombre son unicos y por tanto nunca podra haber mas de una.)
	 * 
	 * @param name
	 *            nombre de la clasificacion
	 * @return verdadero si existe una clasificacion con dicho nombre en el
	 *         catalogo, falso en caso contrario
	 */
	boolean containsClassification(String name);

	/**
	 * Cambia el nombre de la categoria <b>oldName<b> por <b>newName<b> en la
	 * clasificacion pasada.
	 * 
	 * @param classification
	 *            clasificacion
	 * @param oldName
	 *            nombre actual
	 * @param newName
	 *            nuevo nombre a establecer
	 */
	void renameCategory(String classification, String oldName, String newName);

	/**
	 * Agrega una categoria nueva a una clasificacion en el caso de que no
	 * exista ya. Si una categoria con el mismo nombre ya existe en el catalogo
	 * saltara una IllegalArgumentException.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion en que se insertara la categoria
	 * @param categoryName
	 *            nombre de la nueva categoria
	 */
	void addCategoryToClassification(String classificationName, String categoryName);



	/**
	 * Obtienes todas las clasificaciones del catálogo.
	 * 
	 * @return todas las clasificaciones del catalogo
	 */
	Set<Classification> getAllClassifications();

	/**
	 * Elimina la categoria de la clasificacion. Si la clasificacion solo tiene
	 * dicha categoria entonces tambien elimina dicha clasificacion del
	 * catalogo.
	 * 
	 * @param classification
	 *            clasificacion a la que la categoria pertenece
	 * @param categoryName
	 *            nombre de la categoria
	 */
	void removeCategory(String classification, String categoryName);
	
	/**
	 * Cambia el nombre de una clasificacion.
	 * 
	 * La clasificacion debe existir en el catalogo y no debe haber una
	 * clasificacion con el nombre nuevo, sino saltara una excepcion de
	 * argumento existente.
	 * 
	 * @param clasifName
	 *            nombre de la clasificacion actual
	 * @param clasifNewName
	 *            nuevo nombre que tomara la clasificacion
	 */
	void renameClassification(String clasifName, String clasifNewName);

	/**
	 * Agrega una clasificacion al catalogo de clasificaciones.
	 * 
	 * @param classification
	 */
	void addClassification(Classification classification);

	/**
	 * Elimna la clasificacion del catalogo y con ella todas sus categorias. Los
	 * elementos que pertenecian a alguna de las categorias de la
	 * refactorizacion deben ser tambien actualizados.
	 * 
	 * La refactorizacion a eliminar debe existir en el catalogo.
	 * 
	 * @param classification
	 *            nombre de la refactorizacion a utilizar
	 */
	void removeClassification(String classification);
	
	/**
	 * Agrega una categoria nueva a una refactorizacion. Si la refactorizacion
	 * ya pertenece a la categoria saltara una IllegalArgumentException. Si la
	 * categoria no existe en el catalogo tambien saltara la excepcion. Si la
	 * refactorizacion no pertenece al catalogo tambien saltara la excepcion.
	 * 
	 * @param refact
	 *            refactorizacion a la que se agregara la categoria
	 * @param classificationName
	 *            nombre de la clasificacion a la que pertenece la categoria
	 * @param categoryName
	 *            nombre de la nueva categoria
	 * @param catalog catalogo de clasificaciones
	 */
	void addCategoryToRefactoring(String refactName, String classificationName,
			String categoryName);

	/**
	 * Cambia el atributo multicategory de una refactorizacion siempre que no se
	 * intente hacer unicategoria una clasificacion que contiene alguna
	 * refactorizacion que pertenece a varias categorias de la clasificacion:
	 * {@link #classifHasMultiCategoryRefactorings(String)}
	 * 
	 * Si es asi el metodo devuelve una excepcion.
	 * 
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param isMultiCategory
	 *            el valor a dar al atributo multicategory de la clasificacion
	 */
	void setMultiCategory(String classificationName, boolean isMultiCategory);

	/**
	 * Devuelve si alguna de todas las refactorizaciones pertenece a varias
	 * categorias en la clasificacion.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @return true si al menos una de las refactorizaciones pertenece a varias
	 *         categorias en la clasificacion
	 */
	boolean classifHasMultiCategoryRefactorings(String classificationName);

	/**
	 * Asigna una descripcion nueva a una clasificacion existente.
	 * 
	 * La clasificacion debe existir en el catalogo.
	 * 
	 * Lanza {@link IllegalArgumentException}
	 *             si {@link #hasClassification(String)} es falso para el nombre
	 *             de clasificacion dado.
	 *             
	 * @param miClasificacion2
	 * @param string
	 */
	void setDescription(String miClasificacion2, String string);

}
