package dynamicrefactoring.interfaz.wizard.search.internal;

/**
 * Obtiene uno de los resultados de una busqueda de elementos en el indice.
 * 
 * @author imediava
 * 
 */
public class QueryResult {

	private String className;
	private String classDescription;

	protected QueryResult(String className, String classDescription) {
		this.className = className;
		this.classDescription=classDescription;
	}

	/**
	 * @return el nombre de la clase encontrada
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return el descripción de la clase encontrada
	 */
	public String getClassDescription() {
		return classDescription;
	}
	
	/**
	 * Una QueryResult queda definida por su nombre. No puede haber dos
	 * resultados para la misma busqueda con el mismo nombre de clase por tanto
	 * el nombre de clase es su clave.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof QueryResult) {
			return ((QueryResult) o).getClassName().equals(getClassName());
		}
		return false;
	}
}

