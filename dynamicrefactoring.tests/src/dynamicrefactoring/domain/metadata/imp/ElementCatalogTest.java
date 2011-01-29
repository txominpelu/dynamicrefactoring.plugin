package dynamicrefactoring.domain.metadata.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.interfaz.cli.test.utils.StubDataUtils;

public class ElementCatalogTest {

	private ClassifiedElements<Element> classifiedElements;
	private Set<Element> refactorings;
	private ElementCatalog<Element> catalog;

	@Before
	public void setUp() throws Exception {
		refactorings = StubDataUtils
				.readRefactoringsFromFile(StubDataUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		classifiedElements = StubDataUtils
				.readClassifiedElements(StubDataUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		catalog = new ElementCatalog<Element>(refactorings,
				new SimpleUniLevelClassification(
						StubDataUtils.FOWLER_CLASSIFICATION_NAME,
						classifiedElements.getClassification().getCategories()));
	}

	@Test
	public void filteringForExtractTest() {
		final ClassifiedElements<Element> expected = StubDataUtils
				.readClassifiedElements("./testdata/entradafiltradaporextract.txt");
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
	public void removeConditionTests() {
		CategoryCondition<Element> condition = new CategoryCondition<Element>(
				"Extract");
		catalog.addConditionToFilter(condition);
		assertTrue(catalog.getAllFilterConditions().contains(condition));
		catalog.removeConditionFromFilter(condition);
		assertEquals(0, catalog.getAllFilterConditions().size());
	}

	/**
	 * Añadir dos condiciones que filtran por una categoria y ver si al quitar
	 * el filtro por la segunda categoría siguen estando los elementos de dicha
	 * categoría como estaban antes de haber añadido el segundo filtro.
	 */
	@Test
	public void removeConditionWhenTwoApplyToFilteredTests() {
		CategoryCondition<Element> conditionExtract = new CategoryCondition<Element>(
				"Extract");
		CategoryCondition<Element> conditionMove = new CategoryCondition<Element>(
				"Move");
		catalog.addConditionToFilter(conditionExtract);
		Set<Element> expected = catalog.getClassificationOfElements(true)
				.getCategoryChildren(new Category("Move"));
		catalog.addConditionToFilter(conditionMove);
		catalog.removeConditionFromFilter(conditionMove);
		assertEquals(expected, catalog.getClassificationOfElements(true)
				.getCategoryChildren(new Category("Move")));
	}

	@Test
	public void conditionIsAddedTests() {
		CategoryCondition<Element> condition = new CategoryCondition<Element>(
				"Extract");
		catalog.addConditionToFilter(condition);
		assertTrue(catalog.getAllFilterConditions().contains(condition));
	}

	/**
	 * Comprueba que al crear un catalogo nuevo desde otro existente con otra
	 * clasificacion, el nuevo catalogo tiene los mismo elementos y filtros.
	 */
	@Test
	public void newInstanceTest() {
		catalog.addConditionToFilter(new CategoryCondition<Element>("Move"));
		ElementCatalog<Element> copia = (ElementCatalog<Element>) catalog
				.newInstance(StubDataUtils.getOtherClassification());
		assertEquals(catalog.getAllElements(), copia.getAllElements());
		assertEquals(catalog.getAllFilterConditions(),
				copia.getAllFilterConditions());
		assertEquals(StubDataUtils.getOtherClassification(),
				copia.getClassification());

	}

}
