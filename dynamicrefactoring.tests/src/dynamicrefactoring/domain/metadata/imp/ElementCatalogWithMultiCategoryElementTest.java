package dynamicrefactoring.domain.metadata.imp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedFilterableCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class ElementCatalogWithMultiCategoryElementTest {

	private static final CategoryCondition<Element> DUPLICATEDCODE_CATEGORYCONDITION = new CategoryCondition<Element>("BadSmells","DuplicatedCode");

	private ElementCatalog<Element> catalog;
	
	public static final String INICIAL_MULT_SIN_FILTRAR = "./testdata/ElementCatalogTests/multicategoryelements/inicial.txt";
	public static final String INICIAL_FILTRADO_POR_EXTRACT = "./testdata/ElementCatalogTests/multicategoryelements/filtradoporExtract.txt";
	public static final String INICIAL_FILTRADO_POR_DUPLICATEDCODE = "./testdata/ElementCatalogTests/multicategoryelements/filtradoporDuplicatedCode.txt";
	
	@Before
	public void setUp() throws Exception {
		Set<Element> refactorings = MetadataDomainTestUtils
				.readRefactoringsFromFile(INICIAL_MULT_SIN_FILTRAR);
		ClassifiedElements<Element> classifiedElements = MetadataDomainTestUtils
				.readClassifiedElements(INICIAL_MULT_SIN_FILTRAR)[0];
		catalog = new ElementCatalog<Element>(refactorings,
				new SimpleUniLevelClassification(
						"BadSmells",ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION,
						classifiedElements.getClassification().getCategories(),true, true));
	}

	
	@Test(expected=IllegalArgumentException.class)
	public final void throwsExceptionIfNotMultiCategory() throws IOException {
		Set<Element> refactorings = 
			MetadataDomainTestUtils.readRefactoringsFromFile(INICIAL_MULT_SIN_FILTRAR);
		ClassifiedElements<Element> classifiedElements[] = 
			MetadataDomainTestUtils.readClassifiedElements(INICIAL_MULT_SIN_FILTRAR);
		ClassifiedFilterableCatalog<Element > otroCatalogo = 
			new ElementCatalog<Element>(refactorings,
										new SimpleUniLevelClassification(
												"BadSmells",ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION,
												classifiedElements[0].getClassification().getCategories(),false, true));
	}
	
	@Test
	public final void testCargaInicial() throws IOException {
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
		.readClassifiedElements(INICIAL_MULT_SIN_FILTRAR);
		assertEquals(expected[0], catalog.getClassificationOfElements());
		assertEquals(expected[1], catalog.getClassificationOfFilteredElements());
	}
	
	@Test
	public final void testFiltradoPorExtract() throws IOException {
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
		.readClassifiedElements(INICIAL_FILTRADO_POR_EXTRACT);
		catalog.addConditionToFilter(ElementCatalogTest.CATEGORY_CONDITION_EXTRACT);
		assertEquals(expected[0], catalog.getClassificationOfElements());
		assertEquals(expected[1], catalog.getClassificationOfFilteredElements());
	}

	@Test
	public final void testFiltradoPorDuplicatedCode() throws IOException {
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
		.readClassifiedElements(INICIAL_FILTRADO_POR_DUPLICATEDCODE);
		catalog.addConditionToFilter(DUPLICATEDCODE_CATEGORYCONDITION);
		assertEquals(expected[0], catalog.getClassificationOfElements());
		assertEquals(expected[1], catalog.getClassificationOfFilteredElements());
	}
	
	@Test
	public final void testFiltradoPorDuplicatedCodeYDesfiltrar() throws IOException {
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
		.readClassifiedElements(INICIAL_MULT_SIN_FILTRAR);
		catalog.addConditionToFilter(DUPLICATEDCODE_CATEGORYCONDITION);
		catalog.removeConditionFromFilter(DUPLICATEDCODE_CATEGORYCONDITION);
		assertEquals(expected[0], catalog.getClassificationOfElements());
		assertEquals(expected[1], catalog.getClassificationOfFilteredElements());
	}
	
	@Test
	public final void testFiltradoPorDosYDesfiltrarPorUno() throws IOException {
		final ClassifiedElements<Element> expected[] = MetadataDomainTestUtils
		.readClassifiedElements(INICIAL_FILTRADO_POR_DUPLICATEDCODE);
		catalog.addConditionToFilter(DUPLICATEDCODE_CATEGORYCONDITION);
		catalog.addConditionToFilter(new CategoryCondition<Element>("BadSmells","LargeClass"));
		catalog.removeConditionFromFilter(new CategoryCondition<Element>("BadSmells","LargeClass"));
		assertEquals(expected[0], catalog.getClassificationOfElements());
		assertEquals(expected[1], catalog.getClassificationOfFilteredElements());
	}

}
