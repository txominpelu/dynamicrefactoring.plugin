package repository.java.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.DefinitionLanguage;
import javamoon.core.classdef.JavaType;
import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import javamoon.core.entity.JavaThrows;
import javamoon.core.instruction.JavaCodeFragment;
import javamoon.core.instruction.JavaFalseLocalDec;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.classdef.ClassType;
import moon.core.classdef.MethDec;
import moon.core.entity.Result;
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.CreationInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Function;
import repository.java.concretepredicate.HasNotFail;

/**
 * Collector of exception thrown by code fragment.
 * 
 * @author Raúl Marticorena
 *
 */
public class ExceptionsThrow extends Function{
	/**
	 * Routine.
	 */
	private JavaCodeFragment fragment; 
	
	private List<JavaType> listThrow = new ArrayList<JavaType>();
	/**
	 * Collector.
	 * 
	 * @param jrd routine
	 */
	public ExceptionsThrow(JavaCodeFragment fragment){
		this.fragment = fragment;
		listThrow = new ArrayList<JavaType>();
	}
	
	/**
	 * Gets the exceptions with fail.
	 * 
	 * @return exceptions
	 */
	@Override
	public Collection getCollection() {
	
		List<Instr> list = fragment.getFlattenedInstructionsInMethod();
		for (Instr instr : list){			
			visit(instr);			
		}		
		return listThrow;
	}
	
	private void visit(Instr instr){
		if (instr instanceof CompoundInstr){
			List<Instr> list = ((CompoundInstr) instr).getInstructions();
			for (int i = 0; i<list.size();i++){
				visit(list.get(i));
			}			
		}
		else if (instr instanceof CallInstr){
			
			CallInstr callInstr = (CallInstr) instr;
			List<JavaThrows> exceptions = ((JavaRoutineDec) callInstr.getRoutineDec()).getException();
			for (JavaThrows jt : exceptions){
				if (!listThrow.contains(jt.getException())){
					this.listThrow.add(jt.getException());
				}
			}
		}
		else if (instr instanceof AssignmentInstr){
			
			AssignmentInstr assignmentInstr= (AssignmentInstr) instr;
			visit(assignmentInstr.getLeftSide());
			visit(assignmentInstr.getRighSide());
			
		}
		else if (instr instanceof CreationInstr){
			
			
			
		}
		return;
		
	}
	
	private void visit(Expr expr){
		if (expr instanceof CallExpr){
			CallExpr ce = ((CallExpr) expr); 
			if (ce.getFirstElement() instanceof Result){
				MethDec md = ((Result) ce.getFirstElement()).getFunctionDec();
				JavaFunctionDec jfd = (JavaFunctionDec) md;
				List<JavaThrows> list = jfd.getException();
				for (JavaThrows jt : list){
					if (!listThrow.contains(jt.getException())){
						this.listThrow.add(jt.getException());
					}
				}
			}
			// visit arguments
			List<Expr> listExpr = ce.getRealArguments();
			for (Expr exprArg : listExpr){
				visit(exprArg);
			}
			if (ce.getLeftSide()!=null){
				visit (ce.getLeftSide());
			}
		}
	}

	/**
	 * Gets value.
	 * 
	 * @return null
	 */
	@Override
	public Object getValue() {

		return null; // not found
	}
	
	/**
	 * Recursive find.
	 * 
	 * @param instr instruction
	 * @param list list of exceptions
	 */
	private void recursiveFind(Instr instr, List<ClassType> list){
		if (instr instanceof CompoundInstr){			
			List<Instr> lista = ((CompoundInstr) instr).getInstructions();
			for (int i = 0; i<lista.size(); i++){
				if (lista.get(i) instanceof JavaInstrNoMoon && ((JavaInstrNoMoon)lista.get(i)).getText().equals(DefinitionLanguage.CATCH)){
					/*				
					JavaFalseLocalDec jfld =  (JavaFalseLocalDec)((CompoundInstr) instr).getInstructions().get(i+2);					
					
					if (!(new HasNotFail(((CompoundInstr) instr).getInstructions().get(i+5)).isValid())){
						list.add((ClassType) jfld.getJavaLocalDec().getType());
					}*/
					JavaFalseLocalDec jfld =  (JavaFalseLocalDec) lista.get(i+2);					
					
					if (!(new HasNotFail( (lista.get(i+4))).isValid())){
					
						list.add((ClassType) jfld.getJavaLocalDec().getType());
					}
					
				}
				else{
					recursiveFind(lista.get(i),list);
				} // else
			} // for
		} // if
		return;
	} // recursveFind
}
