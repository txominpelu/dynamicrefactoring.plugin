package dynamicrefactoring.domain.metadata.imp;

import java.util.HashSet;
import java.util.Set;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class SimpleRefactoringDefinition implements Element {

	public final String name;
	private Set<Category> categories;

	/**
	 * Crea una definicion de una refactorizacion.
	 * 
	 * @param name
	 *            nombre de la refactorizacion
	 * @param refactCategories
	 *            categorias a las que pertenece
	 */
	public SimpleRefactoringDefinition(String name,
			Set<Category> refactCategories) {
		this.name = name;
		this.categories = refactCategories;
	}

	/**
	 * Obtiene el nombre de la refactorinzación.
	 */
	@Override
	public String getName() {
		return name;
	}


	public String toString() {
		return this.getName();
	}

	/**
	 * Devuelve si la refactorizacion pertenece a la categoria pasada.
	 * 
	 * @param category
	 *            categoria
	 * @return si esta refactorizacion pertenece a la categoria
	 */
	@Override
	public boolean belongsTo(Category category) {
		return categories.contains(category);
	}

	/**
	 * Compara los dos objetos. El otro objeto sera igual al actual si es una
	 * definicion de refactorizacion con el mismo nombre.
	 * 
	 * @param o
	 *            el otro objeto que se va a comparar con el actual
	 * @return verdadero si los dos son refactorizaciones y tienen el mismo
	 *         nombre, falso en caso contrario
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SimpleRefactoringDefinition) {
			SimpleRefactoringDefinition otra = (SimpleRefactoringDefinition) o;
			return (otra.getName().equals(this.getName()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public Set<Category> getCategories() {
		return new HashSet<Category>(categories);
	}

}
