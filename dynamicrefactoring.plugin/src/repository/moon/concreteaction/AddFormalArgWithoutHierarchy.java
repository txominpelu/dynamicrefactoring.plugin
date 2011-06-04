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
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import refactoring.engine.Action;
import repository.java.concreteaction.AddImportClause;

/**
 * Permite añadir un parámetro a la signatura de un método de una representación 
 * MOON de un modelo.<p>
 *
 * No tiene en cuenta las clases que, por herencia, se puedan ver afectadas por
 * un cambio en la signatura del método.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class AddFormalArgWithoutHierarchy extends AddFormalArg {
	
	/**
	 * Acción auxiliar que permite añadir la sentencia de importación si es
	 * necesario.
	 */
	private Action helper;	
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de AddFormalArgWithoutHierarchy
	 *
	 * @param method el método en cuya signatura se va a añadir un parámetro.
	 * @param paramName el nombre que se dará al nuevo parámetro formal.
	 * @param paramType el tipo que tendrá el nuevo parámetro formal.
	 */
	public AddFormalArgWithoutHierarchy (MethDec method,  
		Name paramName, Type paramType){
		
		super(method, paramName, paramType);
	}
	
	/**
	 * No se debe extender la acción a las suclases ni superclases.
	 */
	void addIntoHierarchy(){
		helper = new AddImportClause(method.getClassDef(), newParameter.getType());
		helper.run();
	}
	
	/**
	 * Elimina el argumento formal añadido.
	 */
	public void undo(){
		super.undo();
		helper.undo();
	}
}