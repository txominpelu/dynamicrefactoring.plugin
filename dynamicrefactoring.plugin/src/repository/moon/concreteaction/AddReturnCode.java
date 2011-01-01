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

import java.util.List;

import javamoon.core.DefinitionLanguage;
import javamoon.core.JavaName;
import javamoon.core.entity.JavaResult;
import javamoon.core.expression.JavaCallExpr;
import javamoon.core.instruction.JavaFalseAssigmentInstr;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.Name;
import moon.core.classdef.*;
import moon.core.entity.Entity;
import moon.core.entity.FunctionDec;
import moon.core.expression.Expr;
import moon.core.instruction.CodeFragment;

import refactoring.engine.Action;
import refactoring.engine.Function;
import repository.RelayListenerRegistry;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;

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
	 * Método al que se le añade el valor de retorno.
	 */
	 private MethDec method;
	 
	 /**
	  * Receptor de los mensajes enviados por la acción concreta.
	  */
	 private RelayListenerRegistry listenerReg;
	 	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de AddReturnCode.
	 * @param name nombre del nuevo método a ser creado.
	 * @param fragment fragmento de código a ser tratado.
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
		listenerReg.notify("# run():AddReturnCode #"); //$NON-NLS-1$

		listenerReg.notify("\t- Adding return code to" + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDef.getName().toString()); //$NON-NLS-1$
		
		
		List<MethDec> listMethDec = this.classDef.getMethDecByName(name);
		MethDec methDec = listMethDec.get(0);
		
		Function function = new LocalEntitiesAccessedAfterCodeFragment(fragment);
		if (function.getCollection().size()==0){
			// routine
			// do nothing...
			methDec.add(new JavaInstrNoMoon("}",-1,-1));
		}
		else{
			// function
			List<Entity> list = (List<Entity>) function.getCollection();
			((FunctionDec) methDec).setReturnType(list.get(0).getType());
			
			methDec.add(new JavaInstrNoMoon(DefinitionLanguage.RETURN + DefinitionLanguage.SPACE,-1,-1));
			
			Entity entityResult = new JavaResult(new JavaName(DefinitionLanguage.RETURN), list.get(0).getType(), (FunctionDec) methDec);
			JavaCallExpr jcel1 = new JavaCallExpr(entityResult);
			Expr expr = new JavaCallExpr(list.get(0));
			methDec.add(new JavaFalseAssigmentInstr(jcel1, expr,-1, -1));
			methDec.add(new JavaInstrNoMoon(DefinitionLanguage.ENDLINE,-1,-1));
			methDec.add(new JavaInstrNoMoon("}",-1,-1));
		}
		
	}

	/**
	 * Deshace la adición del código de retorno.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():AddReturnCode#"); //$NON-NLS-1$
		
		AddReturnCode undo = new AddReturnCode(name, fragment);

		undo.run();
	}
}