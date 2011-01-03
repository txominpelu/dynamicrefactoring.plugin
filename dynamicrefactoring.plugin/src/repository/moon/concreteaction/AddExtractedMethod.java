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

import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.Name;
import moon.core.classdef.*;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.Instr;

import refactoring.engine.Action;
import refactoring.engine.Function;
import repository.RelayListenerRegistry;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;

/**
 * Permite añadir un método que contiene la funcionalidad de un fragmento de código.
 *
 * @author <A HREF="mailto:rmartico@ubu.es">Raúl Marticorena</A>
 */ 
public class AddExtractedMethod extends Action {
	
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
	 * Obtiene una nueva instancia de AddExtractedMethod.
	 * @param name nombre del nuevo método a ser creado.
	 * @param fragment fragmento de código a ser tratado.
	 */	
	public AddExtractedMethod(Name name, CodeFragment fragment){
		super();
		this.name = name;
		this.classDef = fragment.getClassDef();
		this.method = fragment.getMethDec();
		this.fragment = fragment;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Añade el nuevo método.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():AddExtractedMethod #"); //$NON-NLS-1$

		listenerReg.notify("\t- Adding method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDef.getName().toString()); //$NON-NLS-1$
		
		//classDefSource.remove(method);
		MethDec methDec = null;
		
		Function function = new LocalEntitiesAccessedAfterCodeFragment(fragment);
		if (function.getCollection().size()==0){
			// routine
			methDec = new JavaRoutineDec(0,name,-1,-1);
		}
		else{
			// function
			methDec = new JavaFunctionDec(0,name,null, -1,-1, false);
		}	
		
		
		
		List<Instr> list = fragment.getInstructionsInMethod();
		// adding instructions to new method
		methDec.add(new JavaInstrNoMoon("{",-1,-1));
		for (Instr instr : list){
			methDec.add(instr);
		}
		//methDec.add(new JavaInstrNoMoon("}",-1,-1));
		
			
		classDef.format(methDec);
		classDef.add(methDec);
		methDec.setClassDef(classDef);
			 //$NON-NLS-1$
	}

	/**
	 * Deshace la adición del nuevo método.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():AddExtractedMethod #"); //$NON-NLS-1$
		
		AddExtractedMethod undoMove = new AddExtractedMethod(name, fragment);

		undoMove.run();
	}
}