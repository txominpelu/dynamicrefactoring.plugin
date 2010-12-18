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


import moon.core.classdef.FormalArgument;
import moon.core.classdef.LocalDec;
import moon.core.entity.Entity;
import moon.core.expression.CallExprLength1;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.CreationInstr;
import moon.core.instruction.Instr;

import refactoring.engine.Function;

/**
 * Gets the set of entities read locally in an instruction set.
 * 
 * @author rmartico
 * @since JavaMoon-2.3.0
 */
public class LocalReadEntitiesAccessed extends Function {
	/**
	 * Instr.
	 */
	private List<Instr> listInstr;
	
	/**
	 * List.
	 */
	private Collection<Entity> collection;
	/**
	 * Constructor.
	 * 
	 * @param instr lista de instrucciones.
	 */
	public LocalReadEntitiesAccessed(List<Instr> instr) {
		this.listInstr = instr;
	}

	/**
	 * Gets the set of entities of the class, including inheritance.
	 * 
	 * @return entities
	 */
	@Override
	public Collection getCollection() {		
		collection = new ArrayList<Entity>();
		for (Instr instr : listInstr){
			if (instr instanceof CompoundInstr){
				this.visitCompoundInstr((CompoundInstr)instr); // composite
			}
			else{				
				this.visit(instr); //leaf;
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
	 * visitCompoundInstr.
	 * @param instr instr.
	 */
	private void visitCompoundInstr(CompoundInstr instr){
		List<Instr> list = instr.getInstructions();
		for (Instr in : list){
			if (in instanceof CompoundInstr){
				this.visitCompoundInstr((CompoundInstr)in); // composite
			}
			else{				
				this.visit(in);	 // leaf		
			}
		}		
	}
	
	/**
	 * visit.
	 * @param instr instr
	 */
	private void visit(Instr instr){
		if (instr instanceof AssignmentInstr){
			 Expr exprRight = ((AssignmentInstr) instr).getRighSide();
			 if (exprRight instanceof CallExprLength1){
				 checkAddEntity(((CallExprLength1) exprRight).getFirstElement());
				 for (Expr expr : ((CallExprLength1) exprRight).getRealArguments()){
					 if (expr instanceof CallExprLength1){
						 checkAddEntity(((CallExprLength1) expr).getFirstElement());
					 }
				 }
			 }
		}
		else if (instr instanceof CreationInstr){
			checkAddEntity(((CreationInstr) instr).getEntity());
		}		
	}

	/**
	 * checkAddEntity.
	 * @param entity entity.
	 */
	private void checkAddEntity(Entity entity) {
		
		if (entity instanceof LocalDec || entity instanceof FormalArgument){
			if (!collection.contains(entity)){
				collection.add(entity);
			}
		}
	}	
}
 