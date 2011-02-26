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

import moon.core.Name;
import moon.core.classdef.FormalArgument;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite renombrar un argumento formal de un m�todo en una representaci�n MOON
 * de un modelo Java.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameFormalArg extends Action {

	/**
	 * Par�metro formal que se debe renombrar.
	 */
	private FormalArgument formalArg;
		
	/**
	 * Nuevo nombre que se dar� al par�metro.
	 */
	private Name newName;
	
	/**
	 * Nombre del par�metro antes del renombrado.
	 */
	private Name originalName;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameFormalArg.
	 *
	 * @param formalArg el par�metro cuyo nombre se desea cambiar.
	 * @param newName el nuevo nombre que se dar� al par�metro.
	 */	
	public RenameFormalArg(FormalArgument formalArg, Name newName){	
		super();
		
		this.formalArg = formalArg;
		this.newName = newName;
		this.originalName = formalArg.getName();

		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el renombrado del par�metro formal.
	 */
	public void run() {		
		listenerReg.notify("# run():RenameFormalArg #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Renaming formal argument from \"" +  //$NON-NLS-1$
			formalArg.getName() + "\" to " + newName); //$NON-NLS-1$
		
		formalArg.setName(newName);
	}

	/**
	 * Deshace el renombrado del argumento formal.
	 */
	public void undo() {		
		listenerReg.notify("# undo():RenameFormalArg #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Undoing formal argument renaming, from \""  //$NON-NLS-1$
			+ formalArg.getName() + "\" to " + originalName); //$NON-NLS-1$
			
		formalArg.setName(originalName);
	}
}