package dynamicrefactoring.plugin.xml.classifications.imp;

import javax.xml.bind.ValidationException;

import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

public class ClassificationsCatalogStub extends ClassificationsManager implements ClassificationsCatalog {
	
	private static final String CLASSIFICATIONS_EDITOR_TEST_XML_FILE = AbstractClassificationsReaderTest.TESTDATA_CLASSIFICATIONS_XML_READING_DIR + "classificationsForClassificationsEditorTests.xml";
	
	
	public ClassificationsCatalogStub() throws ValidationException{
			super(CLASSIFICATIONS_EDITOR_TEST_XML_FILE);
	}

}
