package dynamicrefactoring.domain.metadata.classifications.xml.imp;


import org.junit.Before;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.ClassificationsReaderFactory;

public class JDOMClassificationsReaderTest extends AbstractClassificationsReaderTest{

	@Before
	public void setUp() throws Exception {
		lector = ClassificationsReaderFactory.getReader(ClassificationsReaderFactory.ClassificationsReaderTypes.JDOM_READER);
	}

}
