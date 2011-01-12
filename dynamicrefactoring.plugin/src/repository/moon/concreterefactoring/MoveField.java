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

import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.*;
import repository.moon.concretepredicate.*;

/**
 * Permite mover un campo de una clase de un modelo a otra clase del modelo.<p>
 *
 * Comprueba que el atributo pertenezca a la clase indicada, y que no exista ya 
 * otro atributo en la clase de destino con el mismo nombre que el atributo de 
 * la clase de origen que se va a mover. También verifica que el atributo no 
 * esté siendo utilizado desde ninguna clase del modelo. Las clases de origen
 * y de destino no pueden ser la misma.<p>
 *
 * Si las comprobaciones no fallan, se lleva a cabo la refactorización.<p>
 *
 * Finalmente, comprueba que el proceso se ha llevado a cabo con éxito.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class MoveField extends MOONRefactoring {
	
	/**
	 * Nombre de la refactorización concreta.
	 */
	private static final String NAME = "MoveField"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MoveField.
	 *
	 * @param attribute el atributo que se desea mover de una clase a otra.
	 * @param dest la clase de destino del atributo.
	 * @param model el modelo MOON que contiene a las clases implicadas.
	 */
	public MoveField(AttDec attribute, ClassDef dest, Model model) {		
		super(NAME, model);
		
		ClassDef origin = attribute.getClassDef();
		
		this.addPrecondition(new ExistsClass(origin));
		
		this.addPrecondition(new ExistsClass(dest));
		
		this.addPrecondition(new NotEqualClasses(origin, dest));
		
		this.addPrecondition(new ExistsAttributeInClass(attribute, origin));
		
		this.addPrecondition(new NotExistsAttributeInClass(attribute, dest));
		
		this.addPrecondition(new AttributeIsNotUsedInClass(attribute, origin));
		
		this.addPrecondition(new AttributeIsNotUsedInModel(attribute)); 
				
				
		this.addAction(new MoveAttribute(attribute, dest));
		
		
		this.addPostcondition(new NotExistsAttributeInClass(attribute, origin));
		
		this.addPostcondition(new ExistsAttributeInClass(attribute, dest));
	}
}