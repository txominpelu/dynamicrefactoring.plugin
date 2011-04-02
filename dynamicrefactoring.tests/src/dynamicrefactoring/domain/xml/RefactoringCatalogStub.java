package dynamicrefactoring.domain.xml;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.AbstractRefactoringsCatalog;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.xml.XMLRefactoringsCatalog;

public class RefactoringCatalogStub extends AbstractRefactoringsCatalog implements RefactoringsCatalog {

	public RefactoringCatalogStub() {
		super(XMLRefactoringsCatalog.getRefactoringsFromDir(RefactoringPlugin.getDynamicRefactoringsDir()));
	}

}
