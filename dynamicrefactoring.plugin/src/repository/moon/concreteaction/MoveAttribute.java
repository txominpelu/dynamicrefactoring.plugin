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
 * Permite mover un atributo de una clase a otra dentro de un modelo MOON.<p>
 *
 * Para poder llevarse a cabo el movimiento, el atributo no debe estar siendo 
 * utilizado por ninguna clase del modelo, ni tan siquiera la propia clase de
 * origen del atributo. De otra manera, el modelo podría quedar en un estado
 * inconsistente.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MoveAttribute extends Action {
	
	/**
	 * Atributo que se debe mover de una clase a otra.
	 */
	private AttDec att;
	
	/**
	 * La clase de destino para el atributo que se va a mover.
	 */
	private ClassDef dest;
	
	/**
	 * La clase de origen del atributo que se va a mover.
	 */
	private ClassDef origin;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
			
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de la acción MoveAttribute.
	 *
	 * @param att el atributo que se va a mover de una clase a otra.
	 * @param dest la clase de destino del atributo.
	 */
	public MoveAttribute (AttDec att, ClassDef dest){
		super();
		
		this.att = att;
		this.origin = att.getClassDef();
		this.dest = dest;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}	
	
	/**
	 * Ejecuta el movimiento del atributo de la clase de origen al tipo de destino.
	 */
	public void run(){		
		listenerReg.notify("# run():MoveAttribute #"); //$NON-NLS-1$
		
		AddAttribute addAtt = new AddAttribute(att, dest);
		RemoveAttribute remAtt = new RemoveAttribute(att);
		
		remAtt.run();
		addAtt.run();
	}	
	
	/**
	 * Restaura el atributo a su clase original, eliminándolo a su vez de la
	 * de destino.
	 */
	public void undo(){
		listenerReg.notify("# undo():MoveAttribute #"); //$NON-NLS-1$
		
		MoveAttribute undoMove = new MoveAttribute(att, origin);
		undoMove.run();
	}
}