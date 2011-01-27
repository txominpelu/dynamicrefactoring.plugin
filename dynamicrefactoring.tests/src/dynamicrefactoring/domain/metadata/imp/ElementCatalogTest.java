package dynamicrefactoring.domain.metadata.imp;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.interfaz.cli.test.utils.StubDataUtils;

public class ElementCatalogTest {

	private Map<Category, Set<Element>> classifiedElements;
	private Set<Element> refactorings;
	private ElementCatalog<Element> catalog;

	@Before
	public void setUp() throws Exception {
		refactorings = StubDataUtils.readRefactoringsFromFile(StubDataUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		classifiedElements = StubDataUtils.readClassifiedElements(StubDataUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		catalog = new ElementCatalog<Element>(refactorings,
				StubDataUtils.getFowlerClassification(), StubDataUtils.FOWLER_CLASSIFICATION_NAME);
	}

	@Test
	public void filteringForExtractTest() {
		final ClassifiedElements<Element> expected = new SimpleClassifiedElements<Element>(
				StubDataUtils.FOWLER_CLASSIFICATION_NAME,
				StubDataUtils.readClassifiedElements("./testdata/entradafiltradaporextract.txt"));
		catalog.addConditionToFilter(new CategoryCondition<Element>("Extract"));
		assertEquals(expected, catalog.getClassificationOfElements(true));
	}

	@Test
	public void filteringForExtractTestAndUnfilter() {
		final ClassifiedElements<Element> expected = catalog
				.getClassificationOfElements(true);
		catalog.addConditionToFilter(new CategoryCondition<Element>("Extract"));
		catalog.removeConditionFromFilter(new CategoryCondition<Element>(
				"Extract"));
		assertEquals(expected, catalog.getClassificationOfElements(true));
	}
	
	@Test
	public void conditionIsAddedTests(){
		CategoryCondition<Element> condition = new CategoryCondition<Element>("Extract");
		catalog.addConditionToFilter(condition);
		assertTrue(catalog.getAllFilterConditions().contains(condition));
	}
	
	@Test
	public void removeConditionTests(){
		CategoryCondition<Element> condition = new CategoryCondition<Element>("Extract");
		catalog.addConditionToFilter(condition);
		assertTrue(catalog.getAllFilterConditions().contains(condition));
		catalog.removeConditionFromFilter(condition);
		assertEquals(0, catalog.getAllFilterConditions().size());
	}
}
