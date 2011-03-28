package dynamicrefactoring.domain;

import static dynamicrefactoring.domain.RefactoringMechanism.ACTION;
import static dynamicrefactoring.domain.RefactoringMechanism.isPredicateJavaDependent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;

/**
 * Tests del RefactoringMechanism.
 * 
 * @author imediava
 * 
 */
public class RefactoringMechanismTest {


	/**
	 * Comprueba que se obtiene bien el directorio de un tipo de
	 * refactoringMechanism.
	 */
	@Test
	public final void testGetElementIndependentDir() {
		assertEquals(
				RefactoringConstants.REFACTORING_CLASSES_DIR
						+ ACTION.getMechanismIndependentPackage().replace(".",
								File.separator),
				ACTION.getElementIndependentDir());
	}

	/**
	 * Comprueba que se obtiene bien el paquete de un tipo de
	 * refactoringMechanism.
	 */
	@Test
	public final void testGetMechanismIndependentPackage() {
		assertEquals(RefactoringConstants.ACTIONS_PACKAGE.substring(0,
				RefactoringConstants.ACTIONS_PACKAGE.length() - 1),
				ACTION.getMechanismIndependentPackage());
	}


	/**
	 * Comprueba que se hace bien la comprobacion de si un predicado es
	 * dependiente de Java o no.
	 */
	@Test
	public final void testIsPredicateJavaDependentFalse() {
		assertFalse(isPredicateJavaDependent("AddAtribute"));
	}

	/**
	 * Comprueba que se hace bien la comprobacion de si un elemento es
	 * dependiente de Java o no.
	 */
	@Test
	public final void testIsElementJavaDependent() {
		assertTrue(ACTION.isElementJavaDependent("AddImportClause"));
	}

}
