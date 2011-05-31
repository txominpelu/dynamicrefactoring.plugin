package dynamicrefactoring.domain.metadata.imp;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Pruebas sobre los conjuntos de elementos
 * clasificados.
 * 
 * @author imediava
 *
 */
public class SimpleClassifiedElementsTest {

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

	/**
	 * Configuracion previa para los tests.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		ClassifiedElements<Element>[] ce=MetadataDomainTestUtils
				.readClassifiedElements(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		classifiedElements = ce[0];
		filteredClassifiedElements = ce[1];

		Map<Category, Set<Element>> map = new HashMap<Category, Set<Element>>();
		for (Category c : classifiedElements.getClassification().getCategories()) {
			map.put(c, classifiedElements.getCategoryChildren(c));
		}
		copiaClassifiedElements = new SimpleClassifiedElements<Element>(new SimpleUniLevelClassification(
				classifiedElements.getClassification().getName(),ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION,map.keySet()), map);
		
		Map<Category, Set<Element>> filteredMap = new HashMap<Category, Set<Element>>();
		for (Category c : filteredClassifiedElements.getClassification().getCategories()) {;
			filteredMap.put(c, filteredClassifiedElements.getCategoryChildren(c));
		}
		copiaFilteredClassifiedElements = new SimpleClassifiedElements<Element>(new SimpleUniLevelClassification(
				filteredClassifiedElements.getClassification().getName(),ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION,filteredMap.keySet()), filteredMap);
	}

	/**
	 * Comprueba que las clasificaciones se crean correctamente.
	 */
	@Test
	public void testGetClassification() {
		assertEquals(classifiedElements.getClassification(),
				copiaClassifiedElements.getClassification());
		assertEquals(filteredClassifiedElements.getClassification(),
				copiaFilteredClassifiedElements.getClassification());
	}

	/**
	 * Comprueba que las categorías se construyen correctamente.
	 */
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

	/**
	 * Comprueba que los objetos son iguales mediante su 
	 * metodo equals.
	 */
	@Test
	public void testEqualsObject() {
		assertEquals(classifiedElements, copiaClassifiedElements);
		assertEquals(filteredClassifiedElements, copiaFilteredClassifiedElements);
	}

}
