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
import java.util.Collection;
import java.util.List;

import javamoon.core.DefinitionLanguage;
import javamoon.core.entity.JavaLocalDec;
import javamoon.core.expression.JavaCallExpr;
import javamoon.core.instruction.JavaAssignmentInstr;
import javamoon.core.instruction.JavaCallInstr;
import javamoon.core.instruction.JavaFalseLocalDec;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.Name;
import moon.core.classdef.LocalDec;
import moon.core.classdef.MethDec;
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
import repository.moon.concretefunction.LocalEntitiesDeclared;
import repository.moon.concretefunction.LocalEntitiesInLoopReentrance;

/**
 * Replaces the code fragment with method invocation.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class ReplaceCodeFragment extends Action {
	
	/**
	 * Code fragment.
	 */
	private CodeFragment fragment;
	 
	/**
	 * Name
	 */
	private Name name;
	
	 /**
	  * Receptor de los mensajes enviados por la acción concreta.
	  */
	 private RelayListenerRegistry listenerReg;
	 	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MoveMethod.
	 * @param method método que se va a mover de una clase a otra.
	 * @param classDefDest clase a la que se moverá el método.
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
		listenerReg.notify("# run():ExtractMethod #"); //$NON-NLS-1$

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
						// With return...
						// Gets arguments...
						
						Function function3 = new LocalEntitiesDeclared(fragment);
						List<Entity> listLocalDeclared = (List<Entity>) function3.getCollection();
												
						Function function = new LocalEntitiesAccessed(fragment.getFlattenedInstructionsInMethod());
						List<Entity> entities = (List<Entity>) function.getCollection();
						List<Expr> arguments = new ArrayList<Expr>();
						for (Entity entity : entities){
							if (!listLocalDeclared.contains(entity)){
								Expr ce = new JavaCallExpr(entity);
								ce.setLine((int)instr.getLine());
								arguments.add(ce);
							}
						}
						
						Function function2 = new LocalEntitiesAccessedAfterCodeFragment(fragment);
						List<Entity> listAux = (List<Entity>) function2.getCollection();
				
						
						Entity entity = null;
						if (listAux.size()>0){
							entity = listAux.get(0);
						}
						else{
							Function functionLoop = new LocalEntitiesInLoopReentrance(fragment);
							List<Entity> loopList = (List<Entity>) functionLoop.getCollection();
				
							if (loopList.size()>0){
								entity = loopList.get(0);
							}
						}
						
						
						// if there are local variables declared and return entity is declared	
												
						// Remove local decs..
						List<LocalDec> tmp = new ArrayList<LocalDec>();
						for (Entity entity2 : listLocalDeclared){
							List<LocalDec> listLocalDec = methDec.getLocalDecs();
							for (LocalDec ld : listLocalDec){
								if (ld.getName().equals(entity2.getName()) && ld.getType().equals(entity2.getType())){
									tmp.add(ld);
								}
							}
						}
						for (LocalDec ld : tmp){							
							methDec.remove(ld);
						}
						//

						//if (listLocalDeclared.size()>0 && entity == (Entity) listLocalDeclared.get(0)){
						if (entity!=null && listLocalDeclared.size()>0 && listLocalDeclared.contains(entity)){
							
							// declare a new local entity
							
							LocalDec ld = new JavaLocalDec(false, entity.getName(),entity.getType(),
									(int)instr.getLine(),(int)instr.getColumn(),false,false);
							
							Instr instrFalseLocalDec = new JavaFalseLocalDec((JavaLocalDec)ld,(int)ld.getLine(),(int)ld.getColumn());
							
							fragment.getMethDec().add(ld);
							
							CallExpr ce = new JavaCallExpr(ld);
							ce.setLine((int)instr.getLine());
							
							CallExpr ce2 = new JavaCallExpr(((FunctionDec) newMethDec).getFunctionResultEntity(),arguments);
							ce2.setLine((int)instr.getLine());
							
							AssignmentInstr assignmentInstr = new JavaAssignmentInstr((Expr)ce,DefinitionLanguage.ASSIGNMENT,(Expr)ce2,
									(int)instr.getLine(),(int)instr.getColumn(), (JavaFalseLocalDec) instrFalseLocalDec);
						
							newBodyMethodAux.add(assignmentInstr);
							newBodyMethodAux.add(new JavaInstrNoMoon(DefinitionLanguage.ENDLINE,-1,-1));
							
						}
						else if (entity!=null){
							
							// only catch the return value
							CallExpr ce = new JavaCallExpr(entity);
							ce.setLine((int)instr.getLine());
						
							CallExpr ce2 = new JavaCallExpr(((FunctionDec) newMethDec).getFunctionResultEntity(),arguments);
							ce2.setLine((int)instr.getLine());
							
							AssignmentInstr assignmentInstr = new JavaAssignmentInstr(ce,DefinitionLanguage.ASSIGNMENT,ce2,(int)instr.getLine(),(int)instr.getColumn());
						
							newBodyMethodAux.add(assignmentInstr);
							newBodyMethodAux.add(new JavaInstrNoMoon(DefinitionLanguage.ENDLINE,-1,-1));
						}
					}
					else{
						// no return 
						List<Expr> arguments = new ArrayList<Expr>();
						//Function function = new LocalEntitiesAccessed(fragment.getFlattenedInstructionsInMethod());
						//List<Entity> entities = (List<Entity>) function.getCollection();
						LocalEntitiesAccessed lea = new LocalEntitiesAccessed(this.fragment.getFlattenedInstructionsInMethod());
						
						
						LocalEntitiesDeclared led = new LocalEntitiesDeclared(this.fragment);
						List localEntitiesDeclared = new ArrayList(led.getCollection());
						
							
						List<Entity> declared = (List<Entity>) lea.getCollection();
						
						// remove the local declared entities 
						List<Entity> listAux = new ArrayList<Entity>();
						for (int j = 0; j<declared.size();j++){
							if (!localEntitiesDeclared.contains(declared.get(j))){
								listAux.add(declared.get(j));
							}
						}
						
						
						// Loop Reentrance
						LocalEntitiesInLoopReentrance leilr = new LocalEntitiesInLoopReentrance(fragment);
						Collection<Entity> col = leilr.getCollection();
						for (Entity entity : col){
							if (!listAux.contains(entity)){
								listAux.add(entity);
							}
						}
						
						
						for (Entity entity : listAux){
							Expr ce = new JavaCallExpr(entity);
							ce.setLine((int)instr.getLine());
							arguments.add(ce);
						}
						
						CallInstr jcil1 = new JavaCallInstr((RoutineDec) newMethDec,arguments,(int)instr.getLine(),(int)instr.getColumn());
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
	 * Deshace el movimiento del método, devolviéndolo a su clase de origen y 
	 * eliminándolo de la nueva clase destino.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():MoveMethod #"); //$NON-NLS-1$
		
		ReplaceCodeFragment undo = new ReplaceCodeFragment(name, fragment);

		undo.run();
	}
	
	private boolean isInFragment(Instr instr){
		/*
		if (instr instanceof CallInstr ||
				instr instanceof CreationInstr ||
				instr instanceof AssignmentInstr || 
				instr instanceof JavaFalseLocalDec || // FIXME Language dependent
				instr instanceof JavaInstrNoMoon){ // FIXME Language dependent
			if ( (instr.getLine() + fragment.getMethDec().getLine()) >= fragment.getLine() && 
					(instr.getLine() + fragment.getMethDec().getLine()) <= fragment.getEndLine()){
				return true;
			}					
		}
		return false;*/
		List<Instr> list = this.fragment.getFlattenedInstructionsInMethod();
		for (Instr instrInFragment : list) {
			if (instrInFragment.getId()==instr.getId()){
				return true;
			}
		}
		return false;
		
	}
	
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