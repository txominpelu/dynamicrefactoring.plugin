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
import javamoon.core.expression.JavaCallExpr;
import javamoon.core.instruction.JavaFalseAssignmentInstr;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import moon.core.entity.Entity;
import moon.core.entity.FunctionDec;
import moon.core.expression.Expr;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.Instr;
import refactoring.engine.Action;
import refactoring.engine.Function;
import repository.RelayListenerRegistry;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;
import repository.moon.concretefunction.LocalEntitiesInLoopReentrance;

/**
 * Adds the return instruction.
 *
 * @author <A HREF="mailto:rmartico@ubu.es">Raúl Marticorena</A>
 * @since JavaMoon-2.3.0
 */ 
public class AddReturnCode extends Action {
	
	/**
	 * Code fragment.
	 */
	private CodeFragment fragment;

	/**
	 * Name.
	 */
	private Name name;
	
	/**
	 * Clase a la que se moverá el método.
	 */
	private ClassDef classDef;
	
	/**
	 * Método que se va a mover de una clase a otra.
	 */
	 private MethDec method;
	 
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
	public AddReturnCode(Name name, CodeFragment fragment){
		super();
		this.name = name;
		this.classDef = fragment.getClassDef();
		this.method = fragment.getMethDec();
		this.fragment = fragment;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Adds the return code.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():ExtractMethod #"); //$NON-NLS-1$

		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDef.getName().toString()); //$NON-NLS-1$
		
		
		List<MethDec> listMethDec = this.classDef.getMethDecByName(name);
		MethDec methDec = listMethDec.get(0);
		
		
		
		Function function = new LocalEntitiesAccessedAfterCodeFragment(fragment);
		LocalEntitiesInLoopReentrance leilr = new LocalEntitiesInLoopReentrance(fragment);
		Collection<Entity> col = leilr.getCollection();
		
		
		// no return neither loop reentrance...
		if (function.getCollection().size()==0 && col.size()==0){
			// routine
			// do nothing...
			methDec.add(new JavaInstrNoMoon("}",-1,-1));
		}
		else{
			// function
			List<Entity> list = (List<Entity>) function.getCollection();
			if (list.size()>0){
				// return type
				//((FunctionDec) methDec).setReturnType(list.get(0).getType());
				((FunctionDec) methDec).setFunctionResultEntity(list.get(0).getType());
			}
			else{				
				// loop reentrance
				List<Entity> aux = new ArrayList(col);
				//((FunctionDec) methDec).setReturnType(aux.get(0).getType());
				((FunctionDec) methDec).setFunctionResultEntity(aux.get(0).getType());
			}
			
			List<Instr> listInstr = methDec.getFlattenedInstructions();
			int end = (int)listInstr.get(listInstr.size()-1).getLine();
			
			methDec.add(new JavaInstrNoMoon(DefinitionLanguage.RETURN + DefinitionLanguage.BLANKSPACE,end+1,-1));
			
			Entity entityResult = null;
			if (list.size()>0){
				//entityResult = new JavaFunctionResult(new JavaName(DefinitionLanguage.RETURN), list.get(0).getType(), (FunctionDec) methDec);
				entityResult = ((FunctionDec) methDec).getFunctionResultEntity();
				entityResult.setLine(end+1);
			}
			else {
				//entityResult = new JavaFunctionResult(new JavaName(DefinitionLanguage.RETURN), aux.get(0).getType(), (FunctionDec) methDec);
				entityResult = ((FunctionDec) methDec).getFunctionResultEntity();
				entityResult.setLine(end+1);
			}
			JavaCallExpr jcel1 = new JavaCallExpr(entityResult);
			jcel1.setLine(entityResult.getLine());
			
			Entity entity = null;
			if (list.size()>0){
				entity = list.get(0);
			}
			else{
				List<Entity> aux = new ArrayList(col);
				entity = aux.get(0);
			}
			Expr expr = new JavaCallExpr(entity);
			expr.setLine(end+1);
			methDec.add(new JavaFalseAssignmentInstr(jcel1, expr, end+1, -1));
			methDec.add(new JavaInstrNoMoon(DefinitionLanguage.ENDLINE,end+1,-1));
			methDec.add(new JavaInstrNoMoon("}",end+2,-1));
		
		}
		
		
		
		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " to " + classDef.getName().toString());				 //$NON-NLS-1$
	}

	/**
	 * Deshace el movimiento del método, devolviéndolo a su clase de origen y 
	 * eliminándolo de la nueva clase destino.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():MoveMethod #"); //$NON-NLS-1$
		
		AddReturnCode undo = new AddReturnCode(name, fragment);

		undo.run();
	}
}