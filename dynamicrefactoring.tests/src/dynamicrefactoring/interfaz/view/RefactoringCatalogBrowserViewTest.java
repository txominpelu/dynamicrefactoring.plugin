/**
 * 
 */
package dynamicrefactoring.interfaz.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.interfaz.wizard.RefactoringWizardPage1Object;

/**
 * Pruebas de la vista del
 *  RefactoringCatalogBrowser.
 *  
 * @author imediava
 * 
 */
public final class RefactoringCatalogBrowserViewTest {

	private static final String EXAMPLES_TAB_TITLE = "Examples";

	private static final String IMAGE_TAB_TITLE = "Image";

	private static final String[] MINIMUM_TAB_TITLES = new String[] {
			"Overview", "Inputs", "Mechanism" };

	private RefactoringCatalogBrowserViewPage page;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
						.forceActive();
			}
		});

		bot.waitUntil(new DefaultCondition() {

			public boolean test() throws Exception {
				return bot
						.menu(RefactoringWizardPage1Object.DYNAMIC_REFACTORING_MENU_TEXT)
						.isEnabled();
			}

			public String getFailureMessage() {
				return "Menu bar not available";
			}
		});
		page = new RefactoringCatalogBrowserViewPage(bot);
	}

	/**
	 * Selecciona en el editor un campo y comprueba que aparecen las
	 * refactorizaciones adecuadas.
	 */
	@Test
	public void classifyForScope() {
		page.changeSelectedClassification("Scope");
		assertEquals(getSortedScopeCategories(), page.getExistingCategories());
	}

	/**
	 * Obtiene la lista de categorias de la clasificacion Scope, ordenadas de
	 * forma alfabética.
	 * 
	 * @return lista de categorias de la clasificacion Scope, ordenadas de forma
	 *         alfabética
	 */
	private List<String> getSortedScopeCategories() {
		List<String> categorias = new ArrayList<String>();
		for (Scope ambito : Scope.values()) {
			if (!ambito.equals(Scope.BOUNDED_PAR)) {
				categorias.add(ambito.toString());
			}
		}
		Collections.sort(categorias);
		return categorias;
	}

	/**
	 * Comprueba que se muestra de forma correcta una refactorizacion sin
	 * imagenes, ni ejemplos.
	 */
	@Test
	public void refactoringWithMinimumPanels() {
		page.changeSelectedClassification("None");
		page.selectRefactoring("None", "Rename Field");
		assertEquals(Arrays.asList(MINIMUM_TAB_TITLES),
				page.getSummaryPanelTabsTitles());
	}

	/**
	 * Comprueba que se muestra de forma correcta una refactorizacion con imagen
	 * pero sin ejemplos.
	 */
	@Test
	public void refactoringWithImage() {
		page.changeSelectedClassification("None");
		page.selectRefactoring("None", "Add Parameter");
		assertEquals(
				ImmutableList.builder().add(MINIMUM_TAB_TITLES)
						.add(IMAGE_TAB_TITLE).build(),
				page.getSummaryPanelTabsTitles());
	}

	/**
	 * Comprueba que se muestra de forma correcta una refactorizacion sin imagen
	 * pero con ejemplos.
	 */
	@Test
	public void refactoringWithExamples() {
		page.changeSelectedClassification("None");
		page.selectRefactoring("None", "ExtractMethod");
		assertEquals(
				ImmutableList.builder().add(MINIMUM_TAB_TITLES)
						.add(EXAMPLES_TAB_TITLE).build(),
				page.getSummaryPanelTabsTitles());
	}

}
