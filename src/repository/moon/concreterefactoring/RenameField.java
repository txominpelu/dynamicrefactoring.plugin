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

package repository.moon.concreterefactoring;


import moon.core.classdef.*;
import moon.core.Model;
import moon.core.Name;

import repository.moon.MOONRefactoring;
import repository.moon.concretepredicate.*;

/**
 * Permite renombrar un atributo perteneciente a una clase de un modelo.<p>
 *
 * Comprueba que no exista ya otro atributo en la misma clase con el nuevo nombre. 
 *
 * Si la comprobación no falla, lleva a cabo el renombrado.<p>
 *
 * Finalmente, comprueba que el renombrado se ha llevado a cabo con éxito.

 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class RenameField extends MOONRefactoring {
	
	/**
	 * Nombre de la refactorización concreta.
	 */
	private static final String NAME = "RenameField"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameMethod.
	 *
	 * @param att el atributo que se desea renombrar.
	 * @param classDef la clase que contiene el atributo que se desea renombrar.
	 * @param newName el nuevo nombre que se le va a dar al atributo.
	 * @param model el modelo que contiene la clase cuyo atributo se renombra.
	 */
	public RenameField(AttDec att, ClassDef classDef, Name newName, Model model) {
					
		super(NAME, model);
		
		Name oldName = att.getName();
			
		this.addPrecondition(new ExistsClass(classDef));
		
		this.addPrecondition(new ExistsAttributeWithNameInClass(classDef, oldName));
		
		this.addPrecondition(
			new NotExistsAttributeWithNameInClass(classDef, newName)); 
			
								
		this.addAction(new repository.moon.concreteaction.RenameField(
				att, newName));
				
		this.addPostcondition(new ExistsAttributeWithNameInClass(classDef, newName));
			
		this.addPostcondition(new NotExistsAttributeWithNameInClass(classDef, oldName));
	}
}