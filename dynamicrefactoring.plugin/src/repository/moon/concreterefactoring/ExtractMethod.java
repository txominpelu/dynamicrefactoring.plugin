package repository.moon.concreterefactoring;

import moon.core.Model;
import moon.core.Name;
import moon.core.instruction.CodeFragment;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.AddExtractedFormalArgToMethod;
import repository.moon.concreteaction.AddExtractedMethod;
import repository.moon.concreteaction.AddReturnCode;
import repository.moon.concreteaction.ReplaceCodeFragment;
import repository.moon.concretepredicate.IsCodeFragmentInMethodBody;
import repository.moon.concretepredicate.JustOneReturn;

/**
 * 
 * Extract Method Refactoring (110).
 * 
 * @author Raúl Marticorena
 * @since MOON-2.3.0
 */
public class ExtractMethod extends MOONRefactoring {

	/**
	 * Name.
	 */
	private static final String NAME = "ExtractMethod";
	
	/**
	 * Extract Method.
	 *  
	 * @param name name
	 * @param codeFragment code fragment
	 * @param model model
	 */
	public ExtractMethod(Name name, CodeFragment codeFragment, Model model) {
		super(NAME,model);
		// Preconditions
		// is a fragment of a method boyd
		this.addPrecondition(new IsCodeFragmentInMethodBody(codeFragment));
		// just one return variable
		this.addPrecondition(new JustOneReturn(codeFragment));
			
				
		// Actions
		//this.addAction(new AddMethod(name, formalArguments, returnType, instructions));
		
		
		this.addAction(new AddExtractedMethod(name, codeFragment));
		this.addAction(new AddExtractedFormalArgToMethod(name, codeFragment));
		this.addAction(new AddReturnCode(name, codeFragment));
		this.addAction(new ReplaceCodeFragment(name,codeFragment));

		
		// Postconditions
		// not available now... 
		
	}
}
