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
 * Collector of catch exception block without fail instructions.
 * 
 * @author Raúl Marticorena
 *
 */
public class CatchExceptionWithoutFailCollector extends Function{
	
	/**
	 * Routine.
	 */
	private JavaRoutineDec jrd; 
	
	/**
	 * Constructor.
	 * 
	 * @param jrd routine
	 */
	public CatchExceptionWithoutFailCollector(JavaRoutineDec jrd){
		this.jrd = jrd; 
	}
	
	/**
	 * Gets collection.
	 */
	@Override
	public Collection getCollection() {
		return null;
	}

	/**
	 * Gets the catch without fail instructions.
	 * 
	 * @return exception to be thrown
	 */
	@Override
	public Object getValue() {
		List<Instr> list = jrd.getInstructions();
		List<ClassType> listEx = new ArrayList<ClassType>();
		for (int i = 0; i<list.size(); i++){
			Instr instr = list.get(i);
			recursiveFind(instr,listEx);
		}
		if (listEx.size()>0){
			
			return listEx.get(0);
		}
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
									
					JavaFalseLocalDec jfld =  (JavaFalseLocalDec)((CompoundInstr) instr).getInstructions().get(i+2);
					if (new HasNotFail(((CompoundInstr) instr).getInstructions().get(i+5)).isValid()){
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
