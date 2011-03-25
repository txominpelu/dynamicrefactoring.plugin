package dynamicrefactoring.domain;

import dynamicrefactoring.RefactoringPlugin;

public class RefactoringCatalogStub extends AbstractRefactoringsCatalog implements RefactoringsCatalog {

	public RefactoringCatalogStub() {
		super(XMLRefactoringsCatalog.getRefactoringsFromDir(RefactoringPlugin.getDynamicRefactoringsDir()));
	}

}
