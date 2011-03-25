package dynamicrefactoring.domain.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Comprueba si una refactorizacion dada tiene un nombre dado.
 * 
 * @author imediava
 * 
 */
public class SameNamePredicate implements
		Predicate<DynamicRefactoringDefinition> {

	private String name;

	/**
	 * Crea el predicado.
	 * 
	 * @param name
	 *            nombre
	 */
	public SameNamePredicate(String name) {
		this.name = name;
	}

	@Override
	public boolean apply(DynamicRefactoringDefinition arg0) {
		return arg0.getName().equals(name);
	}

}