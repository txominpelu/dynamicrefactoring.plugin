package dynamicrefactoring.interfaz.cli.test.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dynamicrefactoring.domain.metadata.imp.SimpleRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;

public class StubDataUtils {

	public static Map<Category, Set<Element>> readClassifiedElements(
			String file) {
		Map<Category, Set<Element>> classifiedElements = null;
		try{
		    // Get the object of DataInputStream
			DataInputStream in = new DataInputStream(new FileInputStream(file));
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
			classifiedElements = new HashMap<Category, Set<Element>>();
		    //Read File Line By Line
			Set<Element> toAddToNextCategory = new HashSet<Element>();
			Category category = new Category(br.readLine().replaceAll("#", ""));
		    while ((strLine = br.readLine()) != null)   {
		    	if(! strLine.startsWith("#")){
		    		toAddToNextCategory.add(StubDataUtils.createRefDefinitionFromLine(strLine));
		    	}else{
		    		String nextCategoryName = strLine.replaceAll("#","");
					classifiedElements.put(category, toAddToNextCategory);
					toAddToNextCategory = new HashSet<Element>();
		    		category = new Category(nextCategoryName);
		    	}
		    }
			classifiedElements.put(category, toAddToNextCategory);
		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		return classifiedElements;
	
	}

	private static Element createRefDefinitionFromLine(String strLine) {
		Set<Category> refCategories = new HashSet<Category>();
		final String elementName = strLine.split("=")[0];
		final String[] categories = strLine.split("=")[1].split(",");
		for(String categoryName: categories){
			refCategories.add(new Category(categoryName));
		}
		return new SimpleRefactoringDefinition(elementName, refCategories);
	}

	public static Set<Category> getFowlerClassification() {
		Set<Category> subcategories = new HashSet<Category>();
		subcategories.add(new Category("Fowler.ComposingMethods"));
		subcategories.add(new Category("Fowler.MovingFeatures"));
		subcategories.add(new Category("Fowler.OrganizingData"));
		return subcategories;
	}

	public static final String FOWLER_CLASSIFICATION_NAME = "Fowler";
	public static final String TESTDATA_ENTRADASINFILTRAR_FILE = "./testdata/entradasinfiltrar.txt";
	public static Set<Element> readRefactoringsFromFile(String file) {
		Set<Element> allElements = new HashSet<Element>();
		try {
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (!strLine.startsWith("#")) {
					allElements.add(createRefDefinitionFromLine(strLine));
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return allElements;
	}



}
