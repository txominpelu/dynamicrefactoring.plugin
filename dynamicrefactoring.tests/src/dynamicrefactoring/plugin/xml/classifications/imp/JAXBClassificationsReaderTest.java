package dynamicrefactoring.plugin.xml.classifications.imp;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.imp.JAXBClassificationsReader;

public class JAXBClassificationsReaderTest extends AbstractClassificationsReaderTest {


	@Before
	public void setUp() throws Exception {
		lector = ClassificationsReaderFactory.getReader(ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER);
	}

	
	
}
