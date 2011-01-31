package dynamicrefactoring.domain.metadata.imp;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class SimpleClassifiedElementsTest {

	private Classification classification;
	/**
	 * Elementos clasificados leidos de un fichero.
	 */
	private ClassifiedElements<Element> classifiedElements;
	/**
	 * Copia de elementos clasificados para pruebas.
	 */
	private SimpleClassifiedElements<Element> copiaClassifiedElements;

	@Before
	public void setUp() throws Exception {
		Set<Element> refactorings = MetadataDomainTestUtils
				.readRefactoringsFromFile(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		classification = MetadataDomainTestUtils.readClassifiedElements(
				MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE)
				.getClassification();
		classifiedElements = MetadataDomainTestUtils
				.readClassifiedElements(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		Map<Category, Set<Element>> mapa = new HashMap<Category, Set<Element>>();
		for (Category c : classifiedElements.getClassification()
				.getCategories()) {
			mapa.put(c, classifiedElements.getCategoryChildren(c));
		}
		copiaClassifiedElements = new SimpleClassifiedElements<Element>(
				classifiedElements.getClassification().getName(), mapa);
	}

	@Test
	public void testGetClassification() {
		assertEquals(classifiedElements.getClassification(),
				copiaClassifiedElements.getClassification());
	}

	@Test
	public void testGetCategoryChildren() {
		for (Category c : classifiedElements.getClassification()
				.getCategories()) {
			assertEquals(classifiedElements.getCategoryChildren(c),
					copiaClassifiedElements.getCategoryChildren(c));
		}
	}

	@Test
	public void testEqualsObject() {
		assertEquals(classifiedElements, copiaClassifiedElements);
	}

}
