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

package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javamoon.core.instruction.JavaCodeFragment;

import moon.core.classdef.MethDec;
import moon.core.entity.Entity;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;

import refactoring.engine.Function;

/**
 * Gets the set of entities accesed locally in an instruction set.
 * 
 * @author rmartico
 * @since JavaMoon-2.3.0
 */
public class LocalEntitiesAccessedAfterCodeFragment extends Function {
	/**
	 * Instr.
	 */
	private CodeFragment codeFragment;
	
	/**
	 * List.
	 */
	private Collection<Entity> collection;
	/**
	 * Constructor.
	 * 
	 * @param codeFragment codeFragment.
	 */
	public LocalEntitiesAccessedAfterCodeFragment(CodeFragment codeFragment) {
		this.codeFragment = codeFragment;
	}

	/**
	 * Gets the set of entities of the class, including inheritance.
	 * 
	 * @return entities
	 */
	@Override
	public Collection getCollection() {		
		collection = new ArrayList<Entity>();
		Function functionBefore = new LocalEntitiesAccessed(codeFragment.getInstructionsInMethod());
		

		// duplicate code. Change or move... see RemoveCodeFragment
		MethDec methDec = codeFragment.getMethDec();
		List<Instr> bodyMethod = methDec.getInstructions();
		List<Instr> newBodyMethod = new ArrayList<Instr>();
		List<Instr> newBodyMethodAux = new ArrayList<Instr>();
		// removing instructions to new method
		
		for (Instr instr : bodyMethod){
			if (! (instr instanceof CompoundInstr )){
				newBodyMethod.add(instr);
			}
			else {
				visit((CompoundInstr)instr, newBodyMethod);
			}
		}	

		
		long lastLine =  newBodyMethod.get(newBodyMethod.size()-1).getLine() + methDec.getLine();
		
		
		CodeFragment codeAfterCodeFragment = new JavaCodeFragment((int)codeFragment.getEndLine()+1,0,(int)lastLine,0,
													codeFragment.getClassDef(),"");
		
		List<Instr> codeAfter = codeAfterCodeFragment.getInstructionsInMethod();
		if (codeAfter!=null && codeAfter.size()>0){
			Function functionAfter = new LocalReadEntitiesAccessed(codeAfter);
		
			List<Entity> candidateBefore = (List<Entity>) functionBefore.getCollection();
			List<Entity> candidateAfter = (List<Entity>) functionAfter.getCollection();
			List<Entity> finalList= new ArrayList<Entity>();
			for (Entity entityR : candidateAfter){
				for (Entity entityRW : candidateBefore){
					if (entityR == entityRW){
						collection.add(entityR);
					}
				}
			}
		}
		return collection;
		
	}

	/**
	 * getValue.
	 * 
	 * @return null.
	 */
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * visit.
	 * @param compound compound.
	 * @param newBodyMethod newBodyMethod.
	 */
	private void visit(CompoundInstr compound, List<Instr> newBodyMethod){
		for (Instr instr : compound.getInstructions()){
			if (! (instr instanceof CompoundInstr )){				
				newBodyMethod.add(instr);
			}
			else {
				visit((CompoundInstr)instr, newBodyMethod);
			}
		}
	}
}
 