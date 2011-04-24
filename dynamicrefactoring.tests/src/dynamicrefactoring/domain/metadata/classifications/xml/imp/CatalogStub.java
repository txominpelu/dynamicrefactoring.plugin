package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import javax.xml.bind.ValidationException;

import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.xml.XMLRefactoringsCatalog;

public class CatalogStub extends AbstractCatalog implements ClassificationsCatalog {
	
	private static final String CLASSIFICATIONS_EDITOR_TEST_XML_FILE = AbstractClassificationsReaderTest.TESTDATA_CLASSIFICATIONS_XML_READING_DIR + "classificationsForClassificationsEditorTests.xml";
	
	
	public CatalogStub() throws ValidationException{
			super(AbstractCatalog.getClassificationsFromFile(CLASSIFICATIONS_EDITOR_TEST_XML_FILE, true), XMLRefactoringsCatalog.getInstance());
	}

}
