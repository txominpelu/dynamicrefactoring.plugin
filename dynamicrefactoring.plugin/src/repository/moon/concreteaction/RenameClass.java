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

import moon.core.classdef.ClassDef;
import moon.core.Name;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

/**
 * Permite renombrar una clase de una representación MOON de un modelo Java.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameClass extends Action {

	/**
	 * Clase que se debe renombrar.
	 */
	private ClassDef classDef;
		
	/**
	 * Nuevo nombre que se dará a la clase.
	 */
	private Name newName;
		
	/**
	 * Nombre de la clase antes del renombrado.
	 */
	private Name originalName;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameClass.
	 *
	 * @param classDef la clase cuyo nombre se desea cambiar.
	 * @param newName el nuevo nombre que se dará a la clase.
	 */	
	public RenameClass(ClassDef classDef, Name newName){
		super();
		
		this.classDef = classDef;
		this.newName = newName;
		this.originalName = classDef.getName();
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
		
	/**
	 * Ejecuta el renombrado de la clase.
	 */
	public void run() {
		
		listenerReg.notify("# run():RenameClass #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Renaming class with name: " +  //$NON-NLS-1$
			originalName.toString() + " to " + newName.toString()); //$NON-NLS-1$
		
		MOONRefactoring.getModel().remove(classDef);
				
		classDef.setName(newName);
		
		MOONRefactoring.getModel().add(classDef);
	}

	/**
	 * Deshace el renombrado de la clase.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():RenameClass #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Undoing class renaming from " +  //$NON-NLS-1$
			newName.toString() + " to " + originalName.toString()); //$NON-NLS-1$
		
		MOONRefactoring.getModel().remove(classDef);
		
		classDef.setName(originalName);
		
		MOONRefactoring.getModel().add(classDef);
	}
}