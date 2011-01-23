package dynamicrefactoring.domain.metadata.interfaces;



public final class Category implements Comparable<Category> {
	
	public final static Category NONE_CATEGORY = new Category("None");

	public final static Category FILTERED_CATEGORY = new Category("Filtered");

	private final String name;

	/**
	 * Crea una categoria a partir de su nombre y su conjunto de hijos.
	 * 
	 * @param name
	 *            nombre
	 * @param children
	 *            hijos
	 */
	public Category(String name) {
		this.name = name;
	}
	
	/**
	 * Devuelve el nombre de la categoria
	 * 
	 * @return nombre de la categoria
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * Dos categorias son iguales si tienen el mismo nombre.
	 */
	@Override 
	public boolean equals(Object o) {
		if (o instanceof Category) {
			Category otra = (Category) o;
			return otra.getName().equals(this.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public int compareTo(Category o) {
		return name.compareTo(o.getName());
	}

}
