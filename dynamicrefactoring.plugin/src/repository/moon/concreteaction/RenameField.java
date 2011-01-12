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


import java.util.*;

import moon.core.classdef.*;

import moon.core.Name;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite renombrar un atributo de una representación MOON de un modelo Java.<p>
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Laura Fuente de la Fuente</A>
 */ 
public class RenameField extends Action {
	
	/**
	 * Atributo que va a ser renombrado.
	 */
	private AttDec att;
	
	
	/**
	 * Nuevo nombre que se dará al atributo.
	 */
	private Name newName;
	
	/**
	 * Nombre del atributo antes del renombrado.
	 */
	private Name originalName;
	
	
	/**
	 * Elemento auxiliar para renombrar el método en caso de que aparezca en
	 * clases superiores o inferiores en la jerarquía de herencia.
	 */
	private Vector<RenameMethodWithoutHierarchy> renMethInOtherClassVec;

	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameMethod.
	 *
	 * @param newName el nuevo nombre que se dará al atributo.
	 * @param att atributo a ser modificado.
	 */
	public RenameField ( AttDec att, Name newName){
			
		super();
		
		this.att = att;
		this.newName = newName;
		this.originalName = att.getName();
		
		renMethInOtherClassVec = new Vector<RenameMethodWithoutHierarchy>(10,1);
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el renombrado del método.
	 */
	public void run(){
		
		listenerReg.notify("# run():RenameField #"); //$NON-NLS-1$
			
		att.setName(newName);
	}
	
	/**
	 * Deshace el renombrado del atributo.
	 */
	public void undo(){
		
		listenerReg.notify("# undo():RenameField #");		 //$NON-NLS-1$
		
		if(! renMethInOtherClassVec.isEmpty())
			for(int i=0; i < renMethInOtherClassVec.size(); i++)
				renMethInOtherClassVec.get(i).undo();
		
		listenerReg.notify("\t- Undoing attribute renaming"); //$NON-NLS-1$
		listenerReg.notify("\t\tFormer name: \"" + att.getName().toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		listenerReg.notify("\t\tNew name: \"" + originalName.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		
		att.setName(originalName);
	}	
	
}