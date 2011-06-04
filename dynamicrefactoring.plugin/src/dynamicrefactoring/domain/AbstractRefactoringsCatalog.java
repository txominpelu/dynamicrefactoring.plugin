package dynamicrefactoring.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import dynamicrefactoring.domain.condition.SameNamePredicate;
import dynamicrefactoring.domain.metadata.condition.ActionCondition;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.condition.InputTypeCondition;
import dynamicrefactoring.domain.metadata.condition.PostconditionCondition;
import dynamicrefactoring.domain.metadata.condition.PreconditionCondition;
import dynamicrefactoring.domain.metadata.condition.RootInputTypeCondition;

/**
 * Clase abstracta con metodos comunes a los catalogos de refactorizaciones.
 * 
 * Guarda las refactorizaciones exclusivamente en memoria.
 * 
 * @author imediava
 *
 */
public abstract class AbstractRefactoringsCatalog implements RefactoringsCatalog {

	private final Set<DynamicRefactoringDefinition> refactorings;

	/**
	 * Crea un catalogo de refactorizaciones a partir de un conjunto
	 * de refactorizaciones.
	 * 
	 * @param refactorings refactorizaciones iniciales
	 */
	public AbstractRefactoringsCatalog(
			Set<DynamicRefactoringDefinition> refactorings) {
		this.refactorings = new HashSet<DynamicRefactoringDefinition>(refactorings);
	}

	@Override
	public final DynamicRefactoringDefinition getRefactoring(String refactName) {
		Collection<DynamicRefactoringDefinition> refactoringsForName = Collections2
				.filter(ImmutableSet.copyOf(refactorings),
						new SameNamePredicate(refactName));
		Preconditions.checkArgument(!refactoringsForName.isEmpty(),
				"There's no refactoring with name:" + refactName);
		Preconditions.checkArgument(!(refactoringsForName.size() > 1),
				"There's more than one refactoring with name:" + refactName
						+ " . Refactoring's names must be unique.");
		return refactoringsForName.iterator().next();
	}
	
	@Override
	public final boolean hasRefactoring(final String name) {
		return !Collections2.filter(ImmutableSet.copyOf(refactorings),
				new SameNamePredicate(name)).isEmpty();
	}

	@Override
	public void updateRefactoring(String oldRefactName, DynamicRefactoringDefinition refactoring){
		Preconditions.checkArgument(hasRefactoring(oldRefactName));
		refactorings.remove(getRefactoring(oldRefactName));
		refactorings.add(refactoring);
	}

	@Override
	public void addRefactoring(DynamicRefactoringDefinition refactoring){
		Preconditions.checkArgument(!refactorings.contains(refactoring));
		refactorings.add(refactoring);
	}

	@Override
	public Set<DynamicRefactoringDefinition> getAllRefactorings() {
		return new HashSet<DynamicRefactoringDefinition>(refactorings);
	}
	
	@Override
	public final Set<DynamicRefactoringDefinition> getRefactoringBelongingTo(
			String classification, String categoryName) {
		return new HashSet<DynamicRefactoringDefinition>(Sets.filter(getAllRefactorings(),
				new CategoryCondition<DynamicRefactoringDefinition>(
						classification, categoryName)));
	}
	

	@Override
	public void removeRefactoring(String refactoringName) {
		Preconditions.checkArgument(hasRefactoring(refactoringName));
		final DynamicRefactoringDefinition refactToRemove = getRefactoring(refactoringName);
		refactorings.remove(refactToRemove);
	}
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que tiene entre
	 * sus tipos de entrada el indicado por el parámetro.
	 * 
	 * @param inputType
	 *            tipo de entrada
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	@Override
	public Set<DynamicRefactoringDefinition> getRefactoringsContainsInputType(
			String inputType) {

		return new HashSet<DynamicRefactoringDefinition>(
				Collections2.filter(ImmutableSet.copyOf(getAllRefactorings()),
						new InputTypeCondition<DynamicRefactoringDefinition>(
								inputType)));

	}

	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que tiene como
	 * tipo de entrada raiz el indicado por el parámetro.
	 * 
	 * @param rootInputType
	 *            tipo de entrada de la entrada raiz
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	@Override
	public Set<DynamicRefactoringDefinition> getRefactoringsContainsRootInputType(
			String rootInputType) {

		return new HashSet<DynamicRefactoringDefinition>(Collections2.filter(
				ImmutableSet.copyOf(getAllRefactorings()),
				new RootInputTypeCondition<DynamicRefactoringDefinition>(
						rootInputType)));

	}

	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que tiene entre
	 * sus precondiciones la precondición indicada por el parámetro.
	 * 
	 * @param precondition
	 *            precondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	@Override
	public Set<DynamicRefactoringDefinition> getRefactoringsContainsPrecondition(
			String precondition) {

		return new HashSet<DynamicRefactoringDefinition>(Collections2.filter(
				ImmutableSet.copyOf(getAllRefactorings()),
				new PreconditionCondition<DynamicRefactoringDefinition>(
						precondition)));

	}

	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que tiene entre
	 * sus acciones la acción indicada por el parámetro.
	 * 
	 * @param action
	 *            acción
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	@Override
	public Set<DynamicRefactoringDefinition> getRefactoringsContainsAction(
			String action) {

		return new HashSet<DynamicRefactoringDefinition>(Collections2.filter(
				ImmutableSet.copyOf(getAllRefactorings()),
				new ActionCondition<DynamicRefactoringDefinition>(action)));

	}

	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que tiene entre
	 * sus postcondiciones la postcondición indicada por el parámetro.
	 * 
	 * @param postcondition
	 *            postcondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	@Override
	public Set<DynamicRefactoringDefinition> getRefactoringsContainsPostcondition(
			String postcondition) {

		return new HashSet<DynamicRefactoringDefinition>(Collections2.filter(
				ImmutableSet.copyOf(getAllRefactorings()),
				new PostconditionCondition<DynamicRefactoringDefinition>(
						postcondition)));

	}
	

}
