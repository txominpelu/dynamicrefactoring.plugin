package dynamicrefactoring.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.domain.condition.SameNamePredicate;

class AbstractRefactoringsCatalog implements RefactoringsCatalog {

	private final Set<DynamicRefactoringDefinition> refactorings;

	public AbstractRefactoringsCatalog(
			Set<DynamicRefactoringDefinition> refactorings) {
		this.refactorings = refactorings;
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
	public void updateRefactoring(DynamicRefactoringDefinition refactoring){
		Preconditions.checkArgument(refactorings.contains(refactoring));
		refactorings.remove(refactoring);
		refactorings.add(refactoring);
	}

	@Override
	public void addRefactoring(DynamicRefactoringDefinition refactoring){
		Preconditions.checkArgument(!refactorings.contains(refactoring));
		refactorings.add(refactoring);
	}

	@Override
	public Set<DynamicRefactoringDefinition> getAllRefactorings() {
		return new HashSet(refactorings);
	}
	

}
