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
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.CreationInstr;
import moon.core.instruction.Instr;

import refactoring.engine.Function;

/**
 * Gets the set of entities accesed locally in an instruction set.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 */
public class LocalEntitiesAccessed extends Function {
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
	 * @param instr instr.
	 */
	public LocalEntitiesAccessed(List<Instr> instr) {
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
	 * @return nulo.
	 */
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Visits a compound instruction.
	 * 
	 * @param instr instruction
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
	 * Visits an instruction.
	 * 
	 * @param instr instruction
	 */
	private void visit(Instr instr){

		if (instr instanceof AssignmentInstr){
			 Expr exprLeft = ((AssignmentInstr) instr).getLeftSide();
			 if (exprLeft instanceof CallExpr){
				 checkAddEntity(((CallExpr) exprLeft).getFirstElement());
				// visit arguments...
					List<Expr> listArguments = ((CallExpr)exprLeft).getRealArguments();
					for (Expr expr2 : listArguments){
						if (expr2 instanceof CallExpr){
							visit((CallExpr)expr2);
						}
					}
			 }
			 
			 
			 Expr expr = ((AssignmentInstr) instr).getRighSide();
			 if (expr instanceof CallExpr){
				 visit((CallExpr) expr);
				// visit arguments...
				List<Expr> listArguments = ((CallExpr)expr).getRealArguments();
				for (Expr expr2 : listArguments){
					if (expr2 instanceof CallExpr){
						visit((CallExpr)expr2);
					}
				}
			 }
		}
		else if (instr instanceof CreationInstr){
			checkAddEntity(((CreationInstr) instr).getEntity());
		}
		else if (instr instanceof CallInstr){
			CallInstr callInstr = ((CallInstr) instr);
			visit(callInstr.getLeftSide());
			List<Expr> listExpr = callInstr.getRealArguments();
			for (Expr expr : listExpr){
				if (expr instanceof CallExpr){
					// visit expression
					visit((CallExpr)expr);
					// visit arguments...
					List<Expr> listArguments = ((CallExpr)expr).getRealArguments();
					for (Expr expr2 : listArguments){
						if (expr2 instanceof CallExpr){
							visit((CallExpr)expr2);
						}
					}					
				}
			}
		}
	}

	/**
	 * Checks if an entity is accessed in the code fragment.
	 * 
	 * @param entity entity
	 */
	private void checkAddEntity(Entity entity) {
		
		if (entity instanceof LocalDec || entity instanceof FormalArgument){
			if (!collection.contains(entity)){
				collection.add(entity);
			}
		}
	}	
	
	
	/**
	 * Visits the left side of an expression adding the entity.
	 * 
	 * @param cel1 expression to visit
	 */
	private void visit(CallExpr cel1){
		Entity entity = cel1.getFirstElement();
		checkAddEntity(entity);
		
		if (cel1.getLeftSide()!=null && cel1.getLeftSide() instanceof CallExpr){			
			visit((CallExpr) cel1.getLeftSide());
		}		
	}
}
 