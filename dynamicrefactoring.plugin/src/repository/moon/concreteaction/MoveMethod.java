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

import moon.core.classdef.*;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite mover un método de una clase a otra del modelo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MoveMethod extends Action {

	/**
	 * Clase a la que pertenece originalmente el método.
	 */
	private ClassDef classDefSource;
		
	/**
	 * Clase a la que se moverá el método.
	 */
	private ClassDef classDefDest;
	
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
	public MoveMethod(MethDec method, ClassDef classDefDest){
		super();
		
		this.classDefSource = method.getClassDef();
		this.classDefDest = classDefDest;
		this.method = method;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el movimiento del método de una clase a otra.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():MoveMethod #"); //$NON-NLS-1$

		listenerReg.notify("\t- Removing method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDefSource.getName().toString()); //$NON-NLS-1$
		classDefSource.remove(method);
		
		classDefDest.format(method);
		classDefDest.add(method);
		method.setClassDef(classDefDest);
		
		listenerReg.notify("\t- Adding method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " to " + classDefDest.getName().toString());				 //$NON-NLS-1$
	}

	/**
	 * Deshace el movimiento del método, devolviéndolo a su clase de origen y 
	 * eliminándolo de la nueva clase destino.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():MoveMethod #"); //$NON-NLS-1$
		
		MoveMethod undoMove = new MoveMethod(method, classDefSource);

		undoMove.run();
	}
}