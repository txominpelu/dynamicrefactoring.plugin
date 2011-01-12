/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente De La Fuente

This file is part of Foobar

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package repository.java.concreteaction;

import java.util.ArrayList;
import java.util.List;

import javamoon.core.entity.JavaFunctionResult;
import javamoon.core.expression.JavaCallExpr;
import javamoon.core.instruction.JavaFalseAssignmentInstr;
import moon.core.classdef.MethDec;
import moon.core.expression.Expr;
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
				if (instr instanceof JavaFalseAssignmentInstr){
					Expr expr = ((JavaFalseAssignmentInstr) instr).getRighSide();
					String name = ((JavaFunctionResult) (((JavaCallExpr) expr)).getFirstElement()).toString();					
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
