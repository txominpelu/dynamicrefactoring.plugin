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

import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite eliminar un atributo de una clase de un modelo MOON.<p>
 *
 * Antes de eliminar el atributo se deber�a comprobar que no est� siendo 
 * utilizado por ninguna clase. De otra manera, el modelo podr�a quedar en un
 * estado inconsistente.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RemoveAttribute extends Action {
	
	/**
	 * Atributo que se debe eliminar de la clase.
	 */
	private AttDec att;
	
	/**
	 * Clase de la que se eliminar� el atributo.
	 */
	private ClassDef classDef;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
			
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de la acci�n RemoveAttribute.
	 *
	 * @param att el atributo que se va a eliminar de una clase.
	 */
	public RemoveAttribute (AttDec att){
		super();
		
		this.att = att;
		this.classDef = att.getClassDef();
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta la eliminaci�n del atributo de la clase.
	 */
	public void run(){		
		listenerReg.notify("# run():RemoveAttribute #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Removing attribute " + att.getName().toString() +  //$NON-NLS-1$
						   " from class " + classDef.getUniqueName().toString()); //$NON-NLS-1$
						   		
		classDef.remove(att);
		att.setClassDef(null);
	}	
	
	/**
	 * Restaura el atributo a la clase de la que se elimin�.
	 */
	public void undo(){		
		listenerReg.notify("# undo():RemoveAttribute #"); //$NON-NLS-1$
		
		AddAttribute undoRemove = new AddAttribute(att, classDef);
		undoRemove.run();
	}
}