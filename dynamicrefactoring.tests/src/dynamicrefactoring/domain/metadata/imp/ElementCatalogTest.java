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

/**
 * Test de catalogo de elementos unicategoria.
 * 
 *
 */
public class ElementCatalogTest {

	public static final String MI_CLASSIFICATION_DESCRIPTION = "Midescripcion";
	private static final Category CATEGORY_MOVE = new Category("", "Move");
	public static final CategoryCondition<Element> CATEGORY_CONDITION_EXTRACT = new CategoryCondition<Element>("", "Extract");
	private Set<Category> categories;
	private Set<Element> refactorings;
	private ElementCatalog<Element> catalog;

	public static final String TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT = "./testdata/ElementCatalogTests/entradafiltradaporextract.txt";

	@Before
	public void setUp() throws Exception {
		refactorings = MetadataDomainTestUtils
				.readRefactoringsFromFile(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		categories=MetadataDomainTestUtils
				.readClassifiedElements(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE, false)[0].getClassification().getCategories();
		catalog = new ElementCatalog<Element>(refactorings,
				new SimpleUniLevelClassification(
						MetadataDomainTestUtils.FOWLER_CLASSIFICATION_NAME,MI_CLASSIFICATION_DESCRIPTION,
						categories,false));
	}

	@Test
	public void filteringForExtractTest() throws IOException {
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
				.readClassifiedElements(ElementCatalogTest.TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT, false);
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected[0], catalog.getClassificationOfElements());
		assertEquals(expected[1], catalog.getClassificationOfFilteredElements());
	}

	@Test
	public void filteringForExtractTestAndUnfilter() {
		final ClassifiedElements<Element> expected = catalog.getClassificationOfElements();
		final ClassifiedElements<Element> expectedFiltered = catalog.getClassificationOfFilteredElements();
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		catalog.removeConditionFromFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected, catalog.getClassificationOfElements());
		assertEquals(expectedFiltered, catalog.getClassificationOfFilteredElements());
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
		CategoryCondition<Element> conditionMove = new CategoryCondition<Element>("", "Move");
		catalog.addConditionToFilter(conditionExtract);
		Set<Element> expected = 
			catalog.getClassificationOfElements().getCategoryChildren(CATEGORY_MOVE);
		Set<Element> expectedFiltered = 
			catalog.getClassificationOfFilteredElements().getCategoryChildren(CATEGORY_MOVE);
		catalog.addConditionToFilter(conditionMove);
		catalog.removeConditionFromFilter(conditionMove);
		assertEquals(expected, 
				catalog.getClassificationOfElements().getCategoryChildren(CATEGORY_MOVE));
		assertEquals(expectedFiltered, 
				catalog.getClassificationOfFilteredElements().getCategoryChildren(CATEGORY_MOVE));
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
				.newInstance(MetadataDomainTestUtils.getOtherClassification());
		assertEquals(catalog.getAllElements(), copia.getAllElements());
		assertEquals(catalog.getAllFilterConditions(),copia.getAllFilterConditions());
		assertEquals(MetadataDomainTestUtils.getOtherClassification(),
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
						MetadataDomainTestUtils.FOWLER_CLASSIFICATION_NAME,MI_CLASSIFICATION_DESCRIPTION,
						categories,false),
				filterConditions);
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
				.readClassifiedElements(ElementCatalogTest.TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT, false);
		assertEquals(expected[0], otroCatalogo.getClassificationOfElements());
		assertEquals(expected[1], otroCatalogo.getClassificationOfFilteredElements());
	}

	@Test
	public void removeAllFilterConditionsTest() {
		final ClassifiedElements<Element> expected = catalog
				.getClassificationOfElements();
		final ClassifiedElements<Element> expectedFiltered = catalog
				.getClassificationOfFilteredElements();
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		catalog=(ElementCatalog<Element>) catalog.removeAllFilterConditions();
		assertEquals(expected, catalog.getClassificationOfElements());
		assertEquals(expectedFiltered, catalog.getClassificationOfFilteredElements());
	}

}
