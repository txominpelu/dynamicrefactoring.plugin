package dynamicrefactoring.domain.xml;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.AbstractRefactoringsCatalog;
import dynamicrefactoring.domain.RefactoringsCatalog;

public class RefactoringCatalogStub extends AbstractRefactoringsCatalog implements RefactoringsCatalog {

	public RefactoringCatalogStub() {
		super(XMLRefactoringsCatalog.getRefactoringsFromDir(RefactoringPlugin.getDynamicRefactoringsDir(),true));
	}

}
