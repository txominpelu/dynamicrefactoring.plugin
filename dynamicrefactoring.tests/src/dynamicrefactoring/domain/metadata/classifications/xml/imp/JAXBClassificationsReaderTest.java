package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import org.junit.Before;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.ClassificationsReaderFactory;

public class JAXBClassificationsReaderTest extends AbstractClassificationsReaderTest {


	@Before
	public void setUp() throws Exception {
		lector = ClassificationsReaderFactory.getReader(ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER);
	}



}
