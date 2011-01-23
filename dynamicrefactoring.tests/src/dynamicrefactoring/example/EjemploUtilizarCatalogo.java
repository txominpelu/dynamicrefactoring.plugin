package dynamicrefactoring.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.condition.NameContainsTextCondition;
import dynamicrefactoring.domain.metadata.imp.ElementCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.domain.metadata.interfaces.Element;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.XMLRefactoringReaderException;

public class EjemploUtilizarCatalogo {

	/**
	 * Catálogo.
	 */
	private ElementCatalog<DynamicRefactoringDefinition> catalog;

	@Before
	public void setUp() throws XMLRefactoringReaderException,
	RefactoringException {

		Set<Category> categories = new HashSet<Category>();
		Set<DynamicRefactoringDefinition> refactorings = new HashSet<DynamicRefactoringDefinition>();
		for (Scope scope : Scope.values()) {
			if (!scope.equals(Scope.SCOPE_BOUNDED_PAR)) {
				categories.add(new Category("scope." + scope.toString()));
				final HashMap<String, String> refactoringsNamePathMap = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(scope,
						RefactoringConstants.REFACTORING_TYPES_FILE);
				for (Map.Entry<String, String> entry : refactoringsNamePathMap
						.entrySet()) {
					refactorings.add(DynamicRefactoringDefinition
							.getRefactoringDefinition(entry.getValue()));
				}
			}
		}
		catalog = new ElementCatalog<DynamicRefactoringDefinition>(
				refactorings, categories, "scope");
	}

	@Test
	public void pruebaCatalogo() {
		list(true);
		catalog.addConditionToFilter(new CategoryCondition<DynamicRefactoringDefinition>(
				"scope." + Scope.SCOPE_CLASS));
		list(true);

		catalog.addConditionToFilter(new NameContainsTextCondition<DynamicRefactoringDefinition>(
		"Extract"));

		list(true);
		catalog.removeConditionFromFilter(new CategoryCondition<DynamicRefactoringDefinition>(
				"scope." + Scope.SCOPE_CLASS));
		list(true);
	}

	private void list(boolean showFiltered) {
		ClassifiedElements<DynamicRefactoringDefinition> classifiedElements = catalog
		.getClassificationOfElements(showFiltered);
		SortedSet<Category> listaOrdenada = new TreeSet<Category>(
				new Comparator<Category>() {

					@Override
					public int compare(Category arg0, Category arg1) {
						if (isSpecialCategory(arg0)) {
							return 1;
						} else if (isSpecialCategory(arg1)) {
							return -1;
						}
						return arg0.getName().compareTo(arg1.getName());
					}

					private boolean isSpecialCategory(Category arg0) {
						return arg0.equals(Category.FILTERED_CATEGORY)
						|| arg0.equals(Category.NONE_CATEGORY);
					}

				});
		listaOrdenada.addAll(classifiedElements.getClassification()
				.getCategories());
		for (Category c : listaOrdenada) {
			System.out.println("+ " + c.getName());
			for (Element hijo : classifiedElements.getCategoryChildren(c)) {
				System.out.println("\t- " + hijo.getName());
			}
		}
	}

}
