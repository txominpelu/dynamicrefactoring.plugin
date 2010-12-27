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


import moon.core.classdef.ClassType;
import moon.core.Name;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

/**
 * Permite actualizar el tipo de objetos definido por una clase a la que se 
 * ha cambiado de nombre, de manera que se mantenga la consistencia entre ambos.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameType extends Action {

	/**
	 * Tipo de la clase que se debe renombrar.
	 */
	private ClassType classType;
		
	/**
	 * Nuevo nombre que se dará al tipo de la clase, que coincide 
	 * con el nombre de ésta.
	 */
	private Name newName;
	
	/**
	 * Nombre del tipo antes del renombrado.
	 */
	private Name originalName;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
				
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameClassType.
	 *
	 * @param type el tipo cuyo nombre se desea cambiar.
	 * @param newName el nuevo nombre de la clase.
	 */	
	public RenameType(ClassType type, Name newName){
		
		super();
		
		this.classType = type;

		if(classType != null){
			this.originalName = classType.getName();
			this.newName = newName;			
		}
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el renombrado del tipo definido por la clase.
	 */
	public void run() {
		
		listenerReg.notify("# run():RenameClassType #"); //$NON-NLS-1$
		
		if(classType != null){
			Name auxName = newName;
			
			if(originalName.toString().contains("<")){ //$NON-NLS-1$
				String aux = originalName.toString().substring(
					originalName.toString().indexOf("<")); //$NON-NLS-1$
				newName = newName.concat(aux); 
			}
					
			listenerReg.notify("\t- Renaming type with name: " +  //$NON-NLS-1$
				originalName.toString() + " to " + newName.toString()); //$NON-NLS-1$
						
			MOONRefactoring.getModel().remove(classType);
			
			classType.setName(newName);
						
			MOONRefactoring.getModel().add(classType);
			
			newName = auxName;
		}	
	}	
	
	/**
	 * Deshace el renombrado del tipo definido por la clase.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():RenameClassType #"); //$NON-NLS-1$
		
		if(classType != null){
			
			listenerReg.notify("\t- Undoing type renaming from \"" +  //$NON-NLS-1$
				newName.toString() + "\" to \"" + originalName.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			
			MOONRefactoring.getModel().remove(classType);
			
			classType.setName(originalName);
			
			MOONRefactoring.getModel().add(classType);
		}
		else
		 	listenerReg.notify("\t- It is a generic class; " + //$NON-NLS-1$
		 		"the associated type won't be modified."); //$NON-NLS-1$
	}
}