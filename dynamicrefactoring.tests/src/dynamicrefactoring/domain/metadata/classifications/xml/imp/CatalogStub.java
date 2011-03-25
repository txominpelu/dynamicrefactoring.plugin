package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import javax.xml.bind.ValidationException;

import dynamicrefactoring.domain.XMLRefactoringsCatalog;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.AbstractCatalog;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

public class CatalogStub extends AbstractCatalog implements ClassificationsCatalog {
	
	private static final String CLASSIFICATIONS_EDITOR_TEST_XML_FILE = AbstractClassificationsReaderTest.TESTDATA_CLASSIFICATIONS_XML_READING_DIR + "classificationsForClassificationsEditorTests.xml";
	
	
	public CatalogStub() throws ValidationException{
			super(AbstractCatalog.getClassificationsFromFile(CLASSIFICATIONS_EDITOR_TEST_XML_FILE), XMLRefactoringsCatalog.getInstance());
	}

}
