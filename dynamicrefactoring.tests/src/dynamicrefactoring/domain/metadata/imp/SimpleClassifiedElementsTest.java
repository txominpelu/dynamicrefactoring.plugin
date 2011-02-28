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
	 * Elementos filtrados clasificados leidos de un fichero.
	 */
	private ClassifiedElements<Element> filteredClassifiedElements;
	/**
	 * Copia de elementos clasificados para pruebas.
	 */
	private SimpleClassifiedElements<Element> copiaClassifiedElements;
	/**
	 * Copia de elementos filtrados clasificados para pruebas.
	 */
	private SimpleClassifiedElements<Element> copiaFilteredClassifiedElements;

	@Before
	public void setUp() throws Exception {
		Set<Element> refactorings = MetadataDomainTestUtils
				.readRefactoringsFromFile(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		ClassifiedElements<Element>[] ce=MetadataDomainTestUtils
				.readClassifiedElements(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		classifiedElements = ce[0];
		filteredClassifiedElements = ce[1];
		assertEquals(classifiedElements.getClassification(),filteredClassifiedElements.getClassification());
		classification = classifiedElements.getClassification();

		Map<Category, Set<Element>> map = new HashMap<Category, Set<Element>>();
		Map<Category, Set<Element>> filteredMap = new HashMap<Category, Set<Element>>();
		for (Category c : classifiedElements.getClassification().getCategories()) {
			map.put(c, classifiedElements.getCategoryChildren(c));
			map.put(c, filteredClassifiedElements.getCategoryChildren(c));
		}
		copiaClassifiedElements = new SimpleClassifiedElements<Element>(new SimpleUniLevelClassification(
				classifiedElements.getClassification().getName(),ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION,map.keySet()), map);
		copiaFilteredClassifiedElements = new SimpleClassifiedElements<Element>(new SimpleUniLevelClassification(
				filteredClassifiedElements.getClassification().getName(),ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION,filteredMap.keySet()), filteredMap);
	}

	@Test
	public void testGetClassification() {
		assertEquals(classifiedElements.getClassification(),
				copiaClassifiedElements.getClassification());
		assertEquals(filteredClassifiedElements.getClassification(),
				copiaFilteredClassifiedElements.getClassification());
	}

	@Test
	public void testGetCategoryChildren() {
		for (Category c : classifiedElements.getClassification()
				.getCategories()) {
			assertEquals(classifiedElements.getCategoryChildren(c),
					copiaClassifiedElements.getCategoryChildren(c));
		}
		for (Category c : filteredClassifiedElements.getClassification()
				.getCategories()) {
			assertEquals(filteredClassifiedElements.getCategoryChildren(c),
					copiaFilteredClassifiedElements.getCategoryChildren(c));
		}
	}

	@Test
	public void testEqualsObject() {
		assertEquals(classifiedElements, copiaClassifiedElements);
		assertEquals(filteredClassifiedElements, copiaFilteredClassifiedElements);
	}

}
