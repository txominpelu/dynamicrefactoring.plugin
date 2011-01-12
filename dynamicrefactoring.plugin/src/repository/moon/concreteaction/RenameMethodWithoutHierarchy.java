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
import moon.core.Name;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite renombrar un método de una representación MOON de un modelo Java.<p>
 *
 * No tiene en cuenta las clases que se puedan ver afectadas por el renombrado
 * a causa de su relación a través de herencia con la clase sobre la que se 
 * lleva a cabo el renombrado de un método.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameMethodWithoutHierarchy extends Action {
	
	/**
	 * Método que se debe renombrar.
	 */
	private MethDec method;
			
	/**
	 * Nuevo nombre que se dará al método.
	 */
	private Name newName;
	
	/**
	 * Nombre del método antes del renombrado.
	 */
	private Name originalName;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
				
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameMethodWithoutHierarchy.
	 *
	 * @param method el método cuyo nombre se desea cambiar.
	 * @param newName el nuevo nombre que se dará al método.
	 */
	public RenameMethodWithoutHierarchy (MethDec method, Name newName){
			
		super();
		
		this.method = method;
		this.newName = newName;
		this.originalName = method.getName();
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el renombrado del método.
	 */
	public void run(){
		
		listenerReg.notify("# run():RenameMethodWithoutHierarhcy #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Renaming method"); //$NON-NLS-1$
		listenerReg.notify("\t\tFormer name: \"" + method.getName().toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		listenerReg.notify("\t\tNew name: \"" + newName.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		
		method.setName(newName);
	}
	
	/**
	 * Deshace el renombrado del método.
	 */
	public void undo(){
		
		listenerReg.notify("# undo():RenameMethodWithoutHierarchy #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Undoing method renaming"); //$NON-NLS-1$
		listenerReg.notify("\t\tFormer name: \"" + method.getName().toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		listenerReg.notify("\t\tNew name: \"" + originalName.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		
		method.setName(originalName);
	}
}