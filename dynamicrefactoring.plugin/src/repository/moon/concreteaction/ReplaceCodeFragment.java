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

package repository.moon.concreteaction;

import java.util.ArrayList;
import java.util.List;

import javamoon.core.DefinitionLanguage;
import javamoon.core.expression.JavaCallExprLength1;
import javamoon.core.instruction.JavaAssignmentInstr;
import javamoon.core.instruction.JavaCallInstrLength1;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.Name;
import moon.core.classdef.*;
import moon.core.entity.Entity;
import moon.core.entity.FunctionDec;
import moon.core.entity.RoutineDec;
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.CreationInstr;
import moon.core.instruction.Instr;

import refactoring.engine.Action;
import refactoring.engine.Function;
import repository.RelayListenerRegistry;
import repository.moon.concretefunction.LocalEntitiesAccessed;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;

/**
 * Replaces the code fragment with method invocation.
 * 
 * @author <A HREF="mailto:rmartico@ubu.es">Raúl Marticorena</A>
 */ 
public class ReplaceCodeFragment extends Action {
	
	/**
	 * Code fragment.
	 */
	private CodeFragment fragment;
	 
	/**
	 * Name.
	 */
	private Name name;
	
	 /**
	  * Receptor de los mensajes enviados por la acción concreta.
	  */
	 private RelayListenerRegistry listenerReg;
	 	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de ReplaceCodeFragment
	 * @param name nombre del nuevo método a ser creado.
	 * @param fragment fragmento de código a ser tratado.
	 */	
	public ReplaceCodeFragment(Name name, CodeFragment fragment){
		super();
		this.fragment = fragment;
		this.name = name;
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Moves the instructions.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():ReplaceCodeFragment #"); //$NON-NLS-1$

		listenerReg.notify("\t- Removing instructions " + fragment.toString() //$NON-NLS-1$
			+ " from " + fragment.getClassDef().getName().toString()); //$NON-NLS-1$
		
		
		MethDec methDec = fragment.getMethDec();
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
		
		boolean found = false;
		for (int i = 0; i < newBodyMethod.size(); i++){
			Instr instr = newBodyMethod.get(i);
			if (!isInFragment(instr)){
				// if the instructions is not in the fragment add...
				newBodyMethodAux.add(instr);
			}
			else{
				if (!found){
					List<MethDec> list = this.fragment.getClassDef().getMethDecByName(name);
				
					MethDec newMethDec = list.get(0);
					if (newMethDec.hasReturnType()){
						// Gets arguments...
						List<Expr> arguments = new ArrayList<Expr>();
						Function function = new LocalEntitiesAccessed(fragment.getInstructionsInMethod());
						List<Entity> entities = (List<Entity>) function.getCollection();
						for (Entity entity : entities){
							Expr ce = new JavaCallExprLength1(entity);
							arguments.add(ce);
						}
						
						Function function2 = new LocalEntitiesAccessedAfterCodeFragment(fragment);
						List<Entity> listAux = (List<Entity>) function2.getCollection();
						Entity entity = (Entity) listAux.get(0);
						CallExpr ce = new JavaCallExprLength1(entity);
						
						CallExpr ce2 = new JavaCallExprLength1(((FunctionDec) newMethDec).getResultEntity(),arguments);
						
						AssignmentInstr assignmentInstr = new JavaAssignmentInstr((Expr)ce,DefinitionLanguage.ASSIGNMENT,(Expr)ce2,(int)instr.getLine(),(int)instr.getColumn());
						
						newBodyMethodAux.add(assignmentInstr);
						newBodyMethodAux.add(new JavaInstrNoMoon(DefinitionLanguage.ENDLINE,-1,-1));
						
					}
					else{
						// no return 
						List<Expr> arguments = new ArrayList<Expr>();
						Function function = new LocalEntitiesAccessed(fragment.getInstructionsInMethod());
						List<Entity> entities = (List<Entity>) function.getCollection();
						for (Entity entity : entities){
							Expr ce = new JavaCallExprLength1(entity);
							arguments.add(ce);
						}
						CallInstr jcil1 = new JavaCallInstrLength1((RoutineDec) newMethDec,arguments,(int)instr.getLine(),(int)instr.getColumn());
						newBodyMethodAux.add(jcil1);
						newBodyMethodAux.add(new JavaInstrNoMoon(DefinitionLanguage.ENDLINE,-1,-1));
					}
			
					found =  true;
				}
			}
		}
			
		
		
		methDec.setInstructions(newBodyMethodAux);

		
		listenerReg.notify("\t- Removing instructions" + fragment.toString() //$NON-NLS-1$
			+ " to " + fragment.getClassDef().getName().toString());				 //$NON-NLS-1$
	}

	/**
	 * Deshace el reemplazo del fragmento de código.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():ReplaceCodeFragment #"); //$NON-NLS-1$
		
		ReplaceCodeFragment undo = new ReplaceCodeFragment(name, fragment);

		undo.run();
	}
	
	/**
	 * isInFragment.
	 * @param instr instr
	 * @return verdadero si es un fragmento.
	 */
	private boolean isInFragment(Instr instr){
		if (instr instanceof CallInstr ||
				instr instanceof CreationInstr ||
				instr instanceof AssignmentInstr || 
				instr instanceof JavaInstrNoMoon){ // FIXME Language dependent
			if ( (instr.getLine() + fragment.getMethDec().getLine()) >= fragment.getLine() && 
					(instr.getLine() + fragment.getMethDec().getLine()) <= fragment.getEndLine()){
				return true;
			}					
		}
		return false;
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