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

import java.util.Collection;
import java.util.List;

import javamoon.core.ModifierSet;
import javamoon.core.classdef.JavaClassDef;
import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import javamoon.core.instruction.JavaInstrNoMoon;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import moon.core.entity.Entity;
import moon.core.instruction.CodeFragment;
import moon.core.instruction.Instr;
import refactoring.engine.Action;
import refactoring.engine.Function;
import repository.RelayListenerRegistry;
import repository.moon.concretefunction.LocalEntitiesAccessedAfterCodeFragment;
import repository.moon.concretefunction.LocalEntitiesInLoopReentrance;

/**
 * Add the extracted method.
 *
 * @author <A HREF="mailto:rmartico@ubu.es">Ra�l Marticorena</A>
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
	 * Clase a la que se mover� el m�todo.
	 */
	private ClassDef classDef;
	
	/**
	 * M�todo que se va a mover de una clase a otra.
	 */
	 private MethDec method;
	 
	 /**
	  * Receptor de los mensajes enviados por la acci�n concreta.
	  */
	 private RelayListenerRegistry listenerReg;
	 	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MoveMethod.
	 * @param method m�todo que se va a mover de una clase a otra.
	 * @param classDefDest clase a la que se mover� el m�todo.
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
	 * Ejecuta el movimiento del m�todo de una clase a otra.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():ExtractMethod #"); //$NON-NLS-1$

		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDef.getName().toString()); //$NON-NLS-1$
		
		MethDec methDec = null;
		
		Function function = new LocalEntitiesAccessedAfterCodeFragment(fragment);
		
		LocalEntitiesInLoopReentrance leilr = new LocalEntitiesInLoopReentrance(fragment);
		Collection<Entity> col = leilr.getCollection();
		
		// if the original method was static we should make the new method static
		MethDec originalMethDec = fragment.getMethDec();
		int modifiers;
		if (originalMethDec instanceof JavaRoutineDec){
			modifiers = ((JavaRoutineDec)originalMethDec).getModifiers();
		}
		else{
			modifiers = ((JavaFunctionDec)originalMethDec).getModifiers();
		}
		int newModifier = 0;
		if (ModifierSet.isStatic(modifiers)){
			newModifier = ModifierSet.STATIC;
		}
		
		
		if (function.getCollection().size()==0 && col.size()==0){
			// routine
			methDec = new JavaRoutineDec(newModifier,name, -1,-1);
		}
		else{
			// function
			methDec = new JavaFunctionDec(newModifier,name,null, -1,-1,false);
		}		
		
		List<Instr> list = fragment.getFlattenedInstructionsInMethod();
		// adding instructions to new method
		methDec.add(new JavaInstrNoMoon("{",-1,-1));

		
		for (Instr instr : list){
		
			methDec.add(instr);
			//System.err.println("instr: " + instr.toString());
		}	
			
		classDef.format(methDec);		
		classDef.add(methDec);
		methDec.setClassDef(classDef);
		
		// Recalculate the new number of lines in the class to put the last {
		int numberOfInstructions = fragment.getMethDec().getFlattenedInstructions().size();
		int endOfClass = ((JavaClassDef) classDef).getEndLine();			
		// FIXME why do we have to *2 size?
		((JavaClassDef) classDef).setEndLine(endOfClass + numberOfInstructions*2);
		
		
		
		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " to " + classDef.getName().toString());				 //$NON-NLS-1$
		
		
	}

	/**
	 * Deshace el movimiento del m�todo, devolvi�ndolo a su clase de origen y 
	 * elimin�ndolo de la nueva clase destino.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():MoveMethod #"); //$NON-NLS-1$
		
		AddExtractedMethod undoMove = new AddExtractedMethod(name, fragment);

		undoMove.run();
	}
}