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

import java.util.ArrayList;
import java.util.Collection;

import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.Type;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

/**
 * Permite renombrar el tipo de objetos definido por las clases gen�ricas de 
 * una clase.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameGenericClassType extends Action {

	/**
	 * Las acciones auxiliares utilizadas para el renombrado
	 * en las clases gen�ricas.
	 */
	private ArrayList<Action> actions;
	
	/**
	 * La clase cuyos tipos gen�ricos deben renombrarse.
	 */
	private ClassDef classDef;
	
	/**
	 * El nuevo nombre que debe asignarse a los tipos gen�ricos de la clase.
	 */
	private Name newName;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de <code>RenameGenericClassType</code>.
	 *
	 * @param classDef la clase cuyos tipos gen�ricos se deben renombrar.
	 * @param newName el nuevo que debe darse a los tipos renombrados.
	 */	
	public RenameGenericClassType(ClassDef classDef, Name newName){
		
		super();
		
		this.classDef = classDef;
		this.newName = newName;
		
		actions = new ArrayList<Action>();
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta los renombrado.
	 */
	public void run() {
		
		listenerReg.notify("# run():RenameGenericClassType #"); //$NON-NLS-1$
		
		Collection<Type> types = MOONRefactoring.getModel().getTypes();
		
		for (Type type : types)
			if (type instanceof ClassType && type.getClassDef() != null &&
				type.getClassDef().isGeneric() &&
				((ClassType)type).getClassDef() == classDef){
			
				actions.add(new RenameType(((ClassType)type), newName));
			}
		
		for (Action next : actions)
			next.run();
	}
	
	/**
	 * Deshace los renombrados.
	 */
	@Override
	public void undo() {
		listenerReg.notify("# undo():RenameGenericClassType #"); //$NON-NLS-1$
		
		for (Action next : actions)
			next.undo();
	}
}