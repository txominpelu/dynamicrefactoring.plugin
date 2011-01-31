package dynamicrefactoring.domain.metadata.imp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

import dynamicrefactoring.domain.metadata.imp.SimpleClassifiedElements;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;

class MetadataDomainTestUtils {
	
	public static final String FOWLER_CLASSIFICATION_NAME = "Fowler";
	
	public static ClassifiedElements<Element> readClassifiedElements(String file)
			throws IOException {
		Map<Category, Set<Element>> classifiedElements = new HashMap<Category, Set<Element>>();
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		// Read File Line By Line

		String classificationName = br.readLine().replaceAll("#", "");
		String strLine = "";
		while (( strLine = br.readLine()) != null) {
			String nextCategoryName = strLine.replaceAll("#", "");
			classifiedElements.put(new Category(getCategoryParent(strLine),
					getCategoryName(nextCategoryName)),
					readNextCategoryElements(br));

		}
		// Close the input stream
		in.close();
		return new SimpleClassifiedElements<Element>(new SimpleUniLevelClassification(classificationName, ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION, classifiedElements.keySet()),
				classifiedElements);

	}

	
	public static Classification getOtherClassification() {
		Set<Category> subcategories = new HashSet<Category>();
		subcategories.add(new Category("MiClasif", "Tal"));
		subcategories.add(new Category("MiClasif", "MovingFeatures"));
		subcategories.add(new Category("MiClasif", "OrganizingData"));
		return new SimpleUniLevelClassification("MiClasif", ElementCatalogTest.MI_CLASSIFICATION_DESCRIPTION, subcategories);
	}
	

	/**
	 * Obtiene la categoria definida como una clase de un paquete de java:
	 * 
	 * De: mipadre1.mipadre2.categoria
	 * 
	 * Se queda con: categoria
	 * 
	 * @param readLine
	 *            linea de texto con definicion de categoria formato:
	 *            #categoriParent.category
	 * @return la categoria contenida en la cadena
	 */
	private static String getCategoryName(String readLine) {
		return Iterators.getLast(Iterators.forArray(readLine.split("\\.")));
	}

	/**
	 * Dada una linea de texto con una definicion de una categoria. Obtiene el
	 * nombre del padre de la categoria como si fuera un paquete de una clase de
	 * java:
	 * 
	 * De: mipadre1.mipadre2.categoria
	 * 
	 * Se queda con: mipadre1.mipadre2
	 * 
	 * @param readLine
	 *            linea de texto con definicion de categoria formato:
	 *            #categoriParent.category
	 * @return el padre de la categoria contenida en la cadena
	 */
	private static String getCategoryParent(String readLine) {
		Preconditions.checkNotNull(readLine, "linea nula");
		if (readLine.contains(".")) {
			final String[] subcategories = readLine.replaceAll("#", "").split(
					"\\.");
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i <= subcategories.length - 2; i++) {
				buffer.append(subcategories[i] + ".");
			}
			return buffer.substring(0, buffer.length() - 1);

		}
		return "";
	}

	private static Element createRefDefinitionFromLine(String strLine) {
		Set<Category> refCategories = new HashSet<Category>();
		final String elementName = strLine.split("=")[0];
		final String[] categories = strLine.split("=")[1].split(",");
		for (String categoryName : categories) {
			refCategories.add(new Category(getCategoryParent(categoryName),
					getCategoryName(categoryName)));
		}
		return new SimpleRefactoringDefinition(elementName, refCategories);
	}

	

	public static Set<Element> readRefactoringsFromFile(String file) throws IOException {
		Set<Element> allElements = new HashSet<Element>();
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			if (!strLine.startsWith("#") && !strLine.startsWith("-")) {
				allElements.add(createRefDefinitionFromLine(strLine));
			}
		}
		// Close the input stream
		in.close();
		return allElements;
	}

	
	
	/**
	 * Obtiene los siguientes elementos de la categoria leyendo
	 * linea por linea has que se acabe el fichero o se encuentre
	 * una linea que empiece por "-".
	 * 
	 * @param br buffer de lectura
	 * @return lista de elementos leidos
	 * @throws IOException si hay algun problema al leer el buffer
	 */
	private static Set<Element> readNextCategoryElements(BufferedReader br)
			throws IOException {
		String strLine;
		Set<Element> toAddToNextCategory = new HashSet<Element>();
		while ((strLine = br.readLine()) != null && !strLine.startsWith("-")) {
			toAddToNextCategory.add(MetadataDomainTestUtils
					.createRefDefinitionFromLine(strLine));
		}
		return toAddToNextCategory;
	}



	public static final String TESTDATA_ENTRADASINFILTRAR_FILE = "./testdata/ElementCatalogTests/entradasinfiltrar.txt";

}
