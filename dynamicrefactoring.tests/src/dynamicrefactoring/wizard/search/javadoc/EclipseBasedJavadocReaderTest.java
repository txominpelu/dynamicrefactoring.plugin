package dynamicrefactoring.wizard.search.javadoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class EclipseBasedJavadocReaderTest {

	/**
	 * Clase que no existe en el classpath del lector.
	 */
	private static final String NOT_EXISTING_CLASS = "tal.Cual";
	private static final String EXISTING_CLASS = "moon.core.Model";
	private static EclipseBasedJavadocReader javadocReader;

	/**
	 * Obtiene la instancia del lector de javadoc.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		javadocReader = EclipseBasedJavadocReader.INSTANCE;

	}

	/**
	 * Comprueba que se obtiene correctamente el javadoc de una clase que esta
	 * en el classpath del lector.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testReadJavadocForExistingType() throws IOException {
		assertEquals(FileUtils.readFileToString(new File("./testdata/"
				+ "expected-core.moon.Model-description.txt")),
				javadocReader.getTypeJavaDocAsHtml(EXISTING_CLASS));
	}

	/**
	 * Comprueba que salta una excepcion si se intenta obtener el javadoc de una
	 * clase que no existe en el classpath del lector.
	 * 
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadJavadocForNotExistingType() throws IOException {
		javadocReader.getTypeJavaDocAsHtml(NOT_EXISTING_CLASS);
	}

	/**
	 * Comprueba que si se intenta obtener el javadoc de una clase de cuya
	 * biblioteca no se tiene el javadoc (en este caso una clase del JRE) cuando
	 * se pide su javadoc se devuelve la cadena vacia.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testReadJavadocForTypeWithoutJavadoc() throws IOException {
		assertTrue(javadocReader.getTypeJavaDocAsHtml("java.lang.String").isEmpty());
	}


}
