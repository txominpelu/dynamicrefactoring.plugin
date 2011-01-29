package repository.java.concreteaction;

import java.util.ArrayList;
import java.util.List;

import javamoon.core.instruction.JavaCallInstr;
import moon.core.classdef.MethDec;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Action;

/**
 * Removes fail instructions.
 * 
 * @author Raúl Marticorena
 *
 */
public class RemoveJUnit3FailInstructions  extends Action{

	/**
	 * Method.
	 */
	private MethDec methDec;
	
	/**
	 * Undo list.
	 */
	private List<Instr> undoInstructions;
	
	/**
	 * Constructor.
	 * 
	 * @param methDec method
	 */
	public RemoveJUnit3FailInstructions(MethDec methDec){
		this.methDec = methDec;
	}
	
	/**
	 * Run.
	 */
	@Override
	public void run() {
		List<Instr> list = methDec.getInstructions();
		undoInstructions = methDec.copy();
		for (Instr instr : list) {		
			if (instr instanceof CompoundInstr) {				
				remove((CompoundInstr) instr);
			}
		}
	}

	/**
	 * Undo.
	 */
	@Override
	public void undo() {		
		methDec.setInstructions(undoInstructions);		
	}
	
	/**
	 * Removes fail instructions.
	 * 
	 * @param compoundInstr compound instruction
	 */
	private void remove(CompoundInstr compoundInstr){
		List<Instr> listAux = compoundInstr.getInstructions();
		List<Instr> list = new ArrayList<Instr>(listAux);
		for (int i = 0; i<list.size(); i++){
			Instr instr = list.get(i);
			if (instr instanceof CompoundInstr){
				remove((CompoundInstr)instr);
			}
			else{		
				// Remove fail in false assignment instr...
				if (instr instanceof JavaCallInstr){
					String name = ((JavaCallInstr) instr).getRoutineDec().getName().toString();
					if (name.toString().equals("fail")){						
						compoundInstr.removeInstruction(instr);
						Instr semicolon  = list.get(i+1);
						compoundInstr.removeInstruction(semicolon);
					}
				}	
			}
		}
	}

}
