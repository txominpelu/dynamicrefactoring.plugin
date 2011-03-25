package dynamicrefactoring.plugin.xml.classifications.imp;

import java.util.HashSet;

import javax.xml.bind.ValidationException;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

public class CatalogStub extends AbstractCatalog implements ClassificationsCatalog {
	
	private static final String CLASSIFICATIONS_EDITOR_TEST_XML_FILE = AbstractClassificationsReaderTest.TESTDATA_CLASSIFICATIONS_XML_READING_DIR + "classificationsForClassificationsEditorTests.xml";
	
	
	public CatalogStub() throws ValidationException{
			super(AbstractCatalog.getClassificationsFromFile(CLASSIFICATIONS_EDITOR_TEST_XML_FILE), new HashSet<DynamicRefactoringDefinition>());
	}

}
