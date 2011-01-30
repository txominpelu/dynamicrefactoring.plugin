package dynamicrefactoring.domain.metadata.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.interfaz.cli.test.utils.StubDataUtils;

public class ElementCatalogTest {

	private static final Category CATEGORY_MOVE = new Category("", "Move");
	private static final CategoryCondition<Element> CATEGORY_CONDITION_EXTRACT = new CategoryCondition<Element>(
					"", "Extract");
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
	public void filteringForExtractTest() throws IOException {
		final ClassifiedElements<Element> expected = StubDataUtils
				.readClassifiedElements("./testdata/entradafiltradaporextract.txt");
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected, catalog.getClassificationOfElements(true));
	}

	@Test
	public void filteringForExtractTestAndUnfilter() {
		final ClassifiedElements<Element> expected = catalog
				.getClassificationOfElements(true);
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		catalog.removeConditionFromFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected, catalog.getClassificationOfElements(true));
	}

	@Test
	public void removeConditionTests() {
		CategoryCondition<Element> condition = CATEGORY_CONDITION_EXTRACT;
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
		CategoryCondition<Element> conditionExtract = CATEGORY_CONDITION_EXTRACT;
		CategoryCondition<Element> conditionMove = new CategoryCondition<Element>(
				"", "Move");
		catalog.addConditionToFilter(conditionExtract);
		Set<Element> expected = catalog.getClassificationOfElements(true)
				.getCategoryChildren(CATEGORY_MOVE);
		catalog.addConditionToFilter(conditionMove);
		catalog.removeConditionFromFilter(conditionMove);
		assertEquals(expected, catalog.getClassificationOfElements(true)
				.getCategoryChildren(CATEGORY_MOVE));
	}

	@Test
	public void conditionIsAddedTests() {
		CategoryCondition<Element> condition = CATEGORY_CONDITION_EXTRACT;
		catalog.addConditionToFilter(condition);
		assertTrue(catalog.getAllFilterConditions().contains(condition));
	}

	/**
	 * Comprueba que al crear un catalogo nuevo desde otro existente con otra
	 * clasificacion, el nuevo catalogo tiene los mismo elementos y filtros.
	 */
	@Test
	public void newInstanceTest() {
		catalog.addConditionToFilter(new CategoryCondition<Element>(CATEGORY_MOVE));
		ElementCatalog<Element> copia = (ElementCatalog<Element>) catalog
				.newInstance(StubDataUtils.getOtherClassification());
		assertEquals(catalog.getAllElements(), copia.getAllElements());
		assertEquals(catalog.getAllFilterConditions(),
				copia.getAllFilterConditions());
		assertEquals(StubDataUtils.getOtherClassification(),
				copia.getClassification());

	}

	@Test
	public void seAplicanFiltrosEnConstructorTest() throws IOException {
		CategoryCondition<Element> condition = CATEGORY_CONDITION_EXTRACT;
		List<Predicate<Element>> filterConditions = new ArrayList<Predicate<Element>>();
		filterConditions.add(condition);
		ElementCatalog<Element> otroCatalogo = new ElementCatalog<Element>(
				refactorings,
				new SimpleUniLevelClassification(
						StubDataUtils.FOWLER_CLASSIFICATION_NAME,
						classifiedElements.getClassification().getCategories()),
				filterConditions);
		final ClassifiedElements<Element> expected = StubDataUtils
				.readClassifiedElements("./testdata/entradafiltradaporextract.txt");
		assertEquals(expected, otroCatalogo.getClassificationOfElements(true));
	}

	@Test
	public void removeAllFilterConditionsTest() {
		final ClassifiedElements<Element> expected = catalog
				.getClassificationOfElements(true);
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected, catalog.removeAllFilterConditions()
				.getClassificationOfElements(true));
	}

}
