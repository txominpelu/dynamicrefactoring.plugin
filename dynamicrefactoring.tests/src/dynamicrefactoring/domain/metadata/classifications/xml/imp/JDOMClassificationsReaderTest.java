package dynamicrefactoring.plugin.xml.classifications.imp;


import org.junit.Before;

public class JDOMClassificationsReaderTest extends AbstractClassificationsReaderTest{

	@Before
	public void setUp() throws Exception {
		lector = ClassificationsReaderFactory.getReader(ClassificationsReaderFactory.ClassificationsReaderTypes.JDOM_READER);
	}

}
