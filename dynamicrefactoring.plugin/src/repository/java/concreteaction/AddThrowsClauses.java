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

package repository.java.concreteaction;

import java.util.List;

import javamoon.core.classdef.JavaType;
import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import javamoon.core.entity.JavaThrows;
import javamoon.core.instruction.JavaCodeFragment;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.instruction.CodeFragment;
import refactoring.engine.Action;
import refactoring.engine.Function;
import repository.RelayListenerRegistry;
import repository.java.concretefunction.ExceptionsThrow;

/**
 * Permite mover un método de una clase a otra del modelo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class AddThrowsClauses extends Action {
	
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
	public AddThrowsClauses(Name name, CodeFragment fragment){
		super();
		this.name = name;
		this.classDef = fragment.getClassDef();
		this.method = fragment.getMethDec();
		this.fragment = fragment;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el movimiento del método de una clase a otra.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():ExtractMethod #"); //$NON-NLS-1$

		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDef.getName().toString()); //$NON-NLS-1$
		
		
		List<MethDec> listMethDec = this.classDef.getMethDecByName(name);
		MethDec methDec = listMethDec.get(0);
		
		
		Function function = new ExceptionsThrow((JavaCodeFragment)fragment);
		List<Type> exceptions = (List<Type>) function.getCollection();
		for (Type jt : exceptions) {
		
			if (methDec instanceof JavaRoutineDec){
				((JavaRoutineDec) methDec).add(new JavaThrows(jt,-1,-1));
			}
			else{
				((JavaFunctionDec) methDec).add(new JavaThrows(jt,-1,-1));
			}
			
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
		
		AddThrowsClauses undo = new AddThrowsClauses(name, fragment);

		undo.run();
	}
}