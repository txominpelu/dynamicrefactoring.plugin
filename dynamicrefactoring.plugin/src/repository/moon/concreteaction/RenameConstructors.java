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

import java.util.List;

import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite renombrar los constructores de una clase, actualizando su nombre para
 * que coincida con el de la clase.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameConstructors extends Action {

	/**
	 * Clase cuyo constructor o constructores se van a actualizar.
	 */
	private ClassDef classDef;
		
	/**
	 * Nuevo nombre que se deberí dar a los constructores.
	 */
	private Name newName;
	
	/**
	 * Nombre de la clase (y, por tanto, de sus constructores) 
	 * antes del renombrado.
	 */
	private Name originalName;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameConstructors.
	 *
	 * @param classDef la clase cuyos constructores se desea cambiar de nombre.
	 * @param newName el nuevo nombre que se darí a los constructores de la clase.
	 */	
	public RenameConstructors(ClassDef classDef, Name newName){
		
		super();
		
		this.classDef = classDef;
		this.newName = newName;
		this.originalName = classDef.getName();

		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta la actualización de nombre de los constructores.
	 */
	public void run() {
		
		listenerReg.notify("# run():RenameConstructors #"); //$NON-NLS-1$
		
		List<MethDec> constructors = classDef.getMethDecByName(originalName);
		
		for (MethDec next : constructors)
			substituteName(next, newName);
	}
	
	/**
	 * Renombra los constructores de la clase para mantener la consistencia de 
	 * nombres entre ambos.
	 *
	 * @param md método constructor que se renombrarí.
	 * @param name el nombre que se asignarí al constructor.
	 */
	public void substituteName(MethDec md, Name name){			
		
		listenerReg.notify("\t- Renaming constructor"); //$NON-NLS-1$
		listenerReg.notify("\t\tFormer name: \"" + md.getName().toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		listenerReg.notify("\t\tNew name: "+ name.toString() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		
		md.setName(name);
	}

	/**
	 * Deshace el renombrado de los constructores.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():SubstituteNameConstructor #"); //$NON-NLS-1$
		
		List<MethDec> constructors = classDef.getMethDecByName(newName);
		
		for (MethDec next : constructors)
			substituteName(next, originalName);	
	}
}