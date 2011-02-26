package dynamicrefactoring.plugin.xml.classifications.imp;

import org.junit.Before;

public class JAXBClassificationsReaderTest extends AbstractClassificationsReaderTest {


	@Before
	public void setUp() throws Exception {
		lector = ClassificationsReaderFactory.getReader(ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER);
	}



}
