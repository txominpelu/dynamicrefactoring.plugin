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
import moon.core.genericity.FormalPar;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite borrar un parámetro formal de una clase.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RemoveFormalParameter extends Action {
 
	/**
	 * Clase de la que eliminar el parámetro formal.
	 */
	private ClassDef classDef;
	
	/**
	 * Parámetro formal que se debe eliminar.
	 */
	private FormalPar formalPar;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Constructor de la acción eliminar parámetro formal.
	 *
	 * @param formalPar parámetro formal que se debe eliminar.
	 */	
	public RemoveFormalParameter(FormalPar formalPar) {
		super();
		this.classDef = formalPar.getClassDef();
		this.formalPar = formalPar;	
		
		listenerReg = RelayListenerRegistry.getInstance();
	}

	/**
	 * Ejecuta la eliminación del parámetro formal.
	 */
	@Override
	public void run() {
		
		listenerReg.notify("# run():RemoveFormalParameter #"); //$NON-NLS-1$
		
		String uniqueName = formalPar.getUniqueName().toString(); 
		
		classDef.remove(formalPar);		
		
		listenerReg.notify("\t- Removing formal parameter "  //$NON-NLS-1$
			+ uniqueName + " from class " + classDef.getName().toString()); //$NON-NLS-1$
	}

	/**
	 * Deshace la eliminación del parámetro formal, incluy�ndolo de nuevo en 
	 * la clase.
	 */
	@Override
	public void undo() {
		classDef.add(formalPar);
	}
}