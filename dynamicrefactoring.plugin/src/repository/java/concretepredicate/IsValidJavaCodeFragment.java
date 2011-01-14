package repository.java.concretepredicate;

import javamoon.utils.EclipsePrettyPrinter;
import moon.core.instruction.CodeFragment;
import refactoring.engine.Predicate;

/**
 * Checks if a code fragment is a valid set of statements,
 * expression, etc. sintactically correct
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 *
 */
public class IsValidJavaCodeFragment extends Predicate {

	private CodeFragment codeFragment;

	public IsValidJavaCodeFragment(CodeFragment codeFragment) {
		super("IsValidJavaCodeFragment");
		this.codeFragment = codeFragment;
	}

	@Override
	public boolean isValid() {
		return EclipsePrettyPrinter.canBeFormatted(codeFragment.getText());
	}
}
