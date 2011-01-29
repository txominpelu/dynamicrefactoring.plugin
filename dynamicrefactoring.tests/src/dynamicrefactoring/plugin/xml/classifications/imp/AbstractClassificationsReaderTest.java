package dynamicrefactoring.plugin.xml.classifications.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
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
	
	private static final String TESTDATA_INVALIDCLASSIFICATIONS_XML = "/testdata/ClassificationsXmlReading/invalidclassifications.xml";
	private static final String TESTDATA_CLASSIFICATIONS_XML = "/testdata/ClassificationsXmlReading/classifications.xml";
	protected XmlClassificationsReader lector;

	@Test(expected=ValidationException.class)
	public final void testReadClassificationsNonValidXml() throws ValidationException {
		InputStream ficheroInvalido = getClass().getResourceAsStream(TESTDATA_INVALIDCLASSIFICATIONS_XML);
		lector.readClassifications(ficheroInvalido);
	}
	
	@Test
	public final void testReadClassificationsXml() throws ValidationException {
		InputStream ficheroInvalido = getClass().getResourceAsStream(TESTDATA_CLASSIFICATIONS_XML);
		assertEquals(getExpectedClassification(),lector.readClassifications(ficheroInvalido));
	}
	
	@Test
	public final void testReadClassificationsDosLecturas() throws ValidationException {
		try{
			lector.readClassifications(getClass().getResourceAsStream(TESTDATA_INVALIDCLASSIFICATIONS_XML));
			fail("Deberia saltar la excepcion de xml invalido. (ValidationException)");
		}catch(ValidationException e){}
		InputStream ficheroInvalido = getClass().getResourceAsStream(TESTDATA_CLASSIFICATIONS_XML);
		assertEquals(getExpectedClassification(),lector.readClassifications(ficheroInvalido));
	}
	
	

	/**
	 * Obtiene las clasificaciones que se deberian leer del fichero
	 * testdata/ClassificationsXmlReading/classifications.xml.
	 * 
	 * @return clasificaciones esperadas del fichero classifications.xml
	 */
	public Set<Classification> getExpectedClassification(){
		Set<Category> categories = new HashSet<Category>();
		categories.add(new Category("Fowler.ComposingMethods"));
		categories.add(new Category("Fowler.MakingMethodCallsSimpler"));
		categories.add(new Category("Fowler.MovingFeatures"));
		categories.add(new Category("Fowler.Organizing data"));
		categories.add(new Category("Fowler.Symplifying conditional expressions"));
		Set<Category> categoriesScope = new HashSet<Category>();
		for(Scope s: Scope.values()){
			if(!s.equals(Scope.SCOPE_BOUNDED_PAR)){
				categoriesScope.add(new Category("scope." + s.toString()));
			}
		}
		Set<Classification> classifications = new HashSet<Classification>();
		classifications.add(new SimpleUniLevelClassification("scope", categoriesScope));
		classifications.add(new SimpleUniLevelClassification("Fowler", categories));
		return classifications;
		
	}
}