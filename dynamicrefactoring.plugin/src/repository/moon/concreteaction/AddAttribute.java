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
 * Permite añadir un atributo a una clase de un modelo MOON.<p>
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class AddAttribute extends Action {
	
	/**
	 * Atributo que se debe añadir a la clase destino.
	 */
	private AttDec att;
	
	/**
	 * Clase de destino para el atributo.
	 */
	private ClassDef dest;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de la acción RenameJavaFile.
	 *
	 * @param att el atributo que se va a añadir a una clase.
	 * @param dest la clase de destino del atributo.
	 */
	public AddAttribute (AttDec att, ClassDef dest){
		super();
		
		this.att = att;
		this.dest = dest;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta la adición del atributo a la clase destino.
	 */
	public void run(){	
		listenerReg.notify("# run():AddAttribute #"); //$NON-NLS-1$
		att.setClassDef(dest);
		dest.format(att);
		dest.add(att);		
	}
	
	/**
	 * Elimina el atributo añadido de la clase destino.
	 */
	public void undo(){		
		listenerReg.notify("# undo():AddAttribute #"); //$NON-NLS-1$
		
		RemoveAttribute undoAdd = new RemoveAttribute(att);
		undoAdd.run();		
	}
}