package dynamicrefactoring.domain.metadata.interfaces;

import com.google.common.base.Objects;



public final class Category implements Comparable<Category> {
	
	public final static Category NONE_CATEGORY = new Category("", "None");

	public final static Category FILTERED_CATEGORY = new Category("", "Filtered");

	private final String name;

	private final String parent;

	/**
	 * Crea una categoria a partir de su nombre y su conjunto de hijos.
	 * 
	 * @param parent
	 *            padre de la categoria
	 * @param name nombre de la categoria
	 */
	public Category(String parent, String name) {
		this.name = name;
		this.parent = parent;
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
			return otra.getName().equals(this.getName()) && otra.getParent().equals(this.getParent());
		}
		return false;
	}


	public String getParent() {
		return this.parent;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getName(), this.getParent());
	}

	@Override
	public int compareTo(Category o) {
		return name.compareTo(o.getName());
	}

}
