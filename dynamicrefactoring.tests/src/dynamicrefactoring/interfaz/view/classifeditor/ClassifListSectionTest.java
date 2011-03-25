package dynamicrefactoring.interfaz.view.classifeditor;


import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.imp.CatalogStub;

public class ClassifListSectionTest {

	private static final String NEW_CLASSIF_NAME = "NewClassifName";

	private static final String CLASIF_INICIAL = "ClasifPrueba";
	
	private ClassificationsCatalog catalog;
	private ClassifListSection editor;

	@Before
	public void setUp() throws Exception {
		catalog = new CatalogStub();
		editor = new ClassifListSection(catalog);
	}
	
	/**
	 * Comprueba que si se pide al editor renombrar una clasificacion
	 * lo hace correctamente.
	 */
	@Test
	public void renameClassificationTest(){
		Set<Classification> expectedClassifications = catalog.getAllClassifications();
		Classification classifToRename = getNewClassification();
		expectedClassifications.remove(classifToRename);
		expectedClassifications.add(classifToRename.rename(NEW_CLASSIF_NAME));
		editor.renameClassification(CLASIF_INICIAL, NEW_CLASSIF_NAME);
		assertEquals(expectedClassifications, catalog.getAllClassifications());
	}
	
	/**
	 * Comprueba que si se pide al editor agregar una clasificacion
	 * lo hace correctamente.
	 */
	@Test
	public void addClassificationTest(){
		Set<Classification> expectedClassifications = catalog.getAllClassifications();
		expectedClassifications.add(getNewClassification());
		editor.addClassification(getNewClassification());
		assertEquals(expectedClassifications, catalog.getAllClassifications());
	}

	
	
	/**
	 * Comprueba que si se pide al editor eliminar una categoria
	 * lo hace correctamente.
	 */
	@Test
	public void removeClassificationTest(){
		Set<Classification> expectedClassifications = catalog.getAllClassifications();
		expectedClassifications.add(getNewClassification());
		expectedClassifications.remove(catalog.getClassification(CLASIF_INICIAL));
		editor.removeClassification(CLASIF_INICIAL);
		assertEquals(expectedClassifications, catalog.getAllClassifications());
	}
	
	/**
	 * Obtiene la classificacion nueva utilizada para todas las pruebas.
	 * 
	 * @return clasificacion nueva
	 */
	private Classification getNewClassification() {
		Classification classifToRename = catalog.getClassification(CLASIF_INICIAL);
		return classifToRename;
	}
	

}
