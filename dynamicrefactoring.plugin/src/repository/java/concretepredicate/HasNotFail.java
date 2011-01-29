package repository.java.concretepredicate;

import javamoon.core.instruction.JavaCallInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Predicate;

/**
 * Check if contains a fail instruction.
 * 
 * @author Ra�l Marticorena
 *
 */
public class HasNotFail extends Predicate{

	/**
	 * Initial instr.
	 */
	private Instr instr;
	
	/**
	 * Constructor.
	 * 
	 * @param instr instruction
	 */
	public HasNotFail(Instr instr){
		super("");
		this.instr = instr;
	}
	
	/**
	 * Checks if contains a fail instruction.
	 */
	@Override
	public boolean isValid() {		
		return notContainFail(instr);
	}
	
	/**
	 * Checks if not contains fail.
	 * 
	 * @param instr instr
	 * @return true if not contains fail, false other case
	 */
	private boolean notContainFail(Instr instr){
		
		if (instr instanceof CompoundInstr){
			boolean result = true;
			for (Instr instr2 : ((CompoundInstr) instr).getInstructions()){
				result =  result && notContainFail(instr2);
			}
			
			return result;
		}
		else{
			if (instr instanceof JavaCallInstr){
				String name = ((JavaCallInstr) instr).getRoutineDec().getName().toString();
				if (name.toString().equals("fail")){					
					return false;
				}
			}
			else{
				return true;
			}
		}
		return false;
	}

}
