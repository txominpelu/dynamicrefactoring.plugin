package dynamicrefactoring.domain.metadata.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class ElementCatalogTest {

	public static final String MI_CLASSIFICATION_DESCRIPTION = "Midescripcion";
	private static final Category CATEGORY_MOVE = new Category("", "Move");
	public static final CategoryCondition<Element> CATEGORY_CONDITION_EXTRACT = new CategoryCondition<Element>(
					"", "Extract");
	private ClassifiedElements<Element> classifiedElements;
	private Set<Element> refactorings;
	private ElementCatalog<Element> catalog;

	public static final String TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT = "./testdata/ElementCatalogTests/entradafiltradaporextract.txt";

	@Before
	public void setUp() throws Exception {
		refactorings = MetadataDomainTestUtils
				.readRefactoringsFromFile(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		classifiedElements = MetadataDomainTestUtils
				.readClassifiedElements(MetadataDomainTestUtils.TESTDATA_ENTRADASINFILTRAR_FILE);
		catalog = new ElementCatalog<Element>(refactorings,
				new SimpleUniLevelClassification(
						MetadataDomainTestUtils.FOWLER_CLASSIFICATION_NAME,MI_CLASSIFICATION_DESCRIPTION,
						classifiedElements.getClassification().getCategories()));
	}

	@Test
	public void filteringForExtractTest() throws IOException {
		final ClassifiedElements<Element> expected = MetadataDomainTestUtils
				.readClassifiedElements(ElementCatalogTest.TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT);
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected, catalog.getClassificationOfElements(true));
	}
	
	@Test
	public void filteringForExtractAndGetWithoutFilteredTest() throws IOException {
		final ClassifiedElements<Element> withFiltered = MetadataDomainTestUtils
				.readClassifiedElements(ElementCatalogTest.TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT);
		// Eliminamos la categoria filtered de los elementos clasificados
		HashMap<Category, Set<Element>> expected = new HashMap<Category,Set<Element>>();
		for(Category c : withFiltered.getClassification().getCategories()){
			if(! c.equals(Category.FILTERED_CATEGORY)){
				expected.put(c, withFiltered.getCategoryChildren(c));
			}
		}
		Classification clasif = new SimpleUniLevelClassification(withFiltered.getClassification().getName(), withFiltered.getClassification().getDescription(), expected.keySet());
		
		catalog.addConditionToFilter(CATEGORY_CONDITION_EXTRACT);
		assertEquals(new SimpleClassifiedElements<Element>(clasif, expected), catalog.getClassificationOfElements(false));
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
				.newInstance(MetadataDomainTestUtils.getOtherClassification());
		assertEquals(catalog.getAllElements(), copia.getAllElements());
		assertEquals(catalog.getAllFilterConditions(),
				copia.getAllFilterConditions());
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
						classifiedElements.getClassification().getCategories()),
				filterConditions);
		final ClassifiedElements<Element> expected = MetadataDomainTestUtils
				.readClassifiedElements(ElementCatalogTest.TESTDATA_ENTRADA_FILTRADA_POR_EXTRACT);
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
