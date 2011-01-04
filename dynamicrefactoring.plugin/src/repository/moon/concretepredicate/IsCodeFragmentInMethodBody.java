package repository.moon.concretepredicate;

import moon.core.instruction.CodeFragment;
import refactoring.engine.Predicate;

/**
 * Checks if a code fragmen is in a method body.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 *
 */
public class IsCodeFragmentInMethodBody extends Predicate {

	private CodeFragment codeFragment;

	public IsCodeFragmentInMethodBody(CodeFragment codeFragment) {
		super("");
		this.codeFragment = codeFragment;
	}

	@Override
	public boolean isValid() {
		if (codeFragment.getMethDec()!=null){
			return true;
		}
		return false;
	}
}
