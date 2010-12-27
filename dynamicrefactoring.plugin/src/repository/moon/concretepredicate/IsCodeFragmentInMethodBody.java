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

	/**
	 * CodeFragment.
	 */
	private CodeFragment codeFragment;

	/**
	 * Constructs an IsCodeFragmentInMethodBody object.
	 *
	 * @param codeFragment  a CodeFragment object
	 */

	public IsCodeFragmentInMethodBody(CodeFragment codeFragment) {
		super("IsCodeFragmentInMethodBody:\n\t" + //$NON-NLS-1$
			  "Checks that the code fragment selected is in the body of a method");
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
