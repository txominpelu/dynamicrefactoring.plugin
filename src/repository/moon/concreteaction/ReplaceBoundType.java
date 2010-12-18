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
import moon.core.genericity.*;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite sustituir la acotación de un parámetro formal por una acotación
 * de otro tipo.
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ReplaceBoundType extends Action {

	/**
	 * Clase sobre la que sustituir la acotación.
	 */
	private ClassDef classDef;
	
	/**
	 * Parámetro formal cuyo tipo de acotación se debe sustituir.
	 */
	private BoundS formalPar;
	
	/**
	 * Nuevo tipo de acotación.
	 */
	private ClassType newBoundingType;
	
	/**
	 * Tipo de acotación original.
	 */
	private ClassType oldBoundingType;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Constructor.
	 * 
	 * @param formalPar parámetro formal cuyo tipo de acotación se debe sustituir.
	 * @param oldBoundingType el tipo de acotación original.
	 * @param newBoundingType nuevo tipo completamente instanciado por el que 
	 * sustituir el tipo de acotación original.
	 */
	public ReplaceBoundType(FormalPar formalPar, 
		ClassType oldBoundingType, ClassType newBoundingType) {
		super();
		
		if (formalPar instanceof BoundS){
			this.classDef = formalPar.getClassDef();
			this.formalPar = (BoundS)formalPar;
			this.oldBoundingType = oldBoundingType;
			this.newBoundingType = newBoundingType;
		}
		
		listenerReg = RelayListenerRegistry.getInstance();
	}

	/**
	 * Ejecuta la sustitución de la acotación del  parámetro formal.
	 */	
	public void run() {	
		listenerReg.notify("# run():ReplaceBoundType #"); //$NON-NLS-1$
		
		if (formalPar != null){
			int oldBoundPosition = formalPar.getBounds().indexOf(oldBoundingType);
		
			listenerReg.notify(
				"\t- Replacing current bound type for " + //$NON-NLS-1$
				"formal parameter " + formalPar.getName().toString() + " with "  //$NON-NLS-1$ //$NON-NLS-2$
				+ newBoundingType.getName().toString()
				+ " within the class " + classDef.getClassType().getName().toString()); //$NON-NLS-1$
			
			formalPar.getBounds().remove(oldBoundPosition);
			formalPar.getBounds().add(oldBoundPosition, newBoundingType);
		}
		else
			listenerReg.notify("\t- The formal parameter " + //$NON-NLS-1$
				"does not have any bounding types."); //$NON-NLS-1$
	}

	/**
	 * Deshace la sustitución de la acotación del parámetro formal.
	 */
	public void undo() {
		listenerReg.notify("# undo():ReplaceBoundType #"); //$NON-NLS-1$
		
		new ReplaceBoundType(formalPar, newBoundingType, oldBoundingType).run();
	}
}