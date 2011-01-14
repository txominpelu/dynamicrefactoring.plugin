package repository.java.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.DefinitionLanguage;
import javamoon.core.entity.JavaRoutineDec;
import javamoon.core.instruction.JavaFalseLocalDec;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.classdef.ClassType;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Function;
import repository.java.concretepredicate.HasNotFail;

/**
 * Collector of catch exception blocks with fail instructions.
 * 
 * @author Raúl Marticorena
 *
 */
public class CatchExceptionWithFailCollector extends Function{
	/**
	 * Routine.
	 */
	private JavaRoutineDec jrd; 
	
	/**
	 * Collector.
	 * 
	 * @param jrd routine
	 */
	public CatchExceptionWithFailCollector(JavaRoutineDec jrd){
		this.jrd = jrd; 
	}
	
	/**
	 * Gets the exceptions with fail.
	 * 
	 * @return exceptions
	 */
	@Override
	public Collection getCollection() {
		List<Instr> list = jrd.getInstructions();
		List<ClassType> listEx = new ArrayList<ClassType>();
		for (int i = 0; i<list.size(); i++){
			Instr instr = list.get(i);
			recursiveFind(instr,listEx);
		}
		return listEx;	
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
