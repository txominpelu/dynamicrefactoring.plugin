package dynamicrefactoring.plugin.xml.classifications.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.junit.Test;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.XmlClassificationsReader;

public abstract class AbstractClassificationsReaderTest {
	
	public static final String TESTDATA_CLASSIFICATIONS_XML_READING_DIR = "./testdata/ClassificationsXmlReading/";
	private static final String TESTDATA_INVALIDCLASSIFICATIONS_XML = TESTDATA_CLASSIFICATIONS_XML_READING_DIR +  "invalidclassifications.xml";
	private static final String TESTDATA_CLASSIFICATIONS_XML = TESTDATA_CLASSIFICATIONS_XML_READING_DIR + "classifications.xml";
	protected XmlClassificationsReader lector;

	@Test(expected=ValidationException.class)
	public final void testReadClassificationsNonValidXml() throws ValidationException {
		lector.readClassifications(TESTDATA_INVALIDCLASSIFICATIONS_XML);
	}
	
	@Test
	public final void testReadClassificationsXml() throws ValidationException {
		assertEquals(getExpectedClassification(),lector.readClassifications(TESTDATA_CLASSIFICATIONS_XML));
	}
	
	@Test
	public final void testReadClassificationsDosLecturas() throws ValidationException {
		try{
			lector.readClassifications(TESTDATA_INVALIDCLASSIFICATIONS_XML);
			fail("Deberia saltar la excepcion de xml invalido. (ValidationException)");
		}catch(ValidationException e){}
		assertEquals(getExpectedClassification(),lector.readClassifications(TESTDATA_CLASSIFICATIONS_XML));
	}
	
	

	/**
	 * Obtiene las clasificaciones que se deberian leer del fichero
	 * testdata/ClassificationsXmlReading/classifications.xml.
	 * 
	 * @return clasificaciones esperadas del fichero classifications.xml
	 */
	public Set<Classification> getExpectedClassification(){
		Set<Category> categories = new HashSet<Category>();
		categories.add(new Category("Fowler" , "ComposingMethods"));
		categories.add(new Category("Fowler" ,"MakingMethodCallsSimpler"));
		categories.add(new Category("Fowler" ,"MovingFeatures"));
		categories.add(new Category("Fowler" ,"Organizing data"));
		categories.add(new Category("Fowler" ,"Symplifying conditional expressions"));
		Set<Category> categoriesScope = new HashSet<Category>();
		for(Scope s: Scope.values()){
			if(!s.equals(Scope.BOUNDED_PAR)){
				categoriesScope.add(new Category("Scope" ,s.toString()));
			}
		}
		Set<Classification> classifications = new HashSet<Classification>();
		classifications.add(new SimpleUniLevelClassification("Scope", "MiDescripcion", categoriesScope));
		classifications.add(new SimpleUniLevelClassification("Fowler", "", categories, true));
		return classifications;
		
	}
}
