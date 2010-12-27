package repository.moon.concretepredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.instruction.JavaCodeFragment;
import javamoon.core.instruction.JavaInstrNoMoon;

import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.entity.Entity;
import moon.core.entity.Result;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.CreationInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Function;
import refactoring.engine.Predicate;
import repository.moon.concretefunction.LocalEntitiesAccessed;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;

/**
 * Checks if there is just one return. Just one variable is written.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 *
 */
public class JustOneReturn extends Predicate {

	/**
	 * CodeFragment.
	 */
	private CodeFragment codeFragment;

	/**
	 * Constructs a JustOneReturn object.
	 *
	 * @param codeFragment  a CodeFragment object
	 */

	public JustOneReturn(CodeFragment codeFragment) {
		super("JustOneReturn: Checks that in the codeFragment selected" +
				" there is only one variable that is going to be read later in the" +
				" rest of the method");
		this.codeFragment = codeFragment;
	}

	@Override
	public boolean isValid() {
		Function functionBefore = new LocalEntitiesAccessedAfterCodeFragment(codeFragment);
		
		Collection col = functionBefore.getCollection();
		if (col.size()>1){
			return false;
		}
		return true;		
	}
	
	
}
