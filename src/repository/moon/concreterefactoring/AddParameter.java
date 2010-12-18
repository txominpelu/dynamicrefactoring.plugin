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

import repository.java.concreteaction.AddImportClause;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.*;
import repository.moon.concretepredicate.*;

/**
 * Permite añadir un nuevo parámetro a la signatura de un método.<p>
 *
 * Verifica que no exista ya otro parámetro en el método con el mismo nombre y
 * que no existen variables locales a ese método con nombre igual al del nuevo 
 * parámetro. Además, comprueba que no exista ya un método con la signatura que 
 * tendría el método afectado una vez llevada a cabo la refactorización, ni en 
 * la propia clase, ni en clases a las que se deba extender el cambio de 
 * signatura a causa de las relaciones de herencia.<p>
 *
 * Si la comprobación no falla, añade el parámetro formal en la definición del 
 * método en la clase correspondiente. Además, incluye en todas las llamadas al
 * método el valor por defecto para el tipo del argumento, como parámetro real.<p>
 *
 * Finalmente, comprueba que el proceso se ha llevado a cabo con éxito.<p>
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class AddParameter extends MOONRefactoring {

	/**
	 * Nombre de la refactorización concreta.
	 */
	private static final String NAME = "AddParameter"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de AddParameter.
	 * 
	 * @param method el método a cuya signatura se va a añadir el parámetro.
	 * @param type el tipo {@link moon.core.classdef.Type} del parámetro.
	 * @param name el nombre del nuevo parámetro formal.
	 * @param model el modelo sobre el que se ejecuta la refactorización
	 */
	public AddParameter(MethDec method, Type type, Name name, Model model) {
		
		super(NAME, model);
		
		Name newMethodUniqueName = 
			method.getUniqueName().concat('%' + type.toString());
				
		this.addPrecondition(new HasNotFormalArgWithName(method, name));
		
		this.addPrecondition(new NotExistsLocalDecWithName(method, name));
			
		this.addPrecondition(new NotExistsMethodWithNameInClass(method.getClassDef(),
			newMethodUniqueName)); 
		
		this.addPrecondition(new MethodIsNotInSubNorSuperclass(
			method.getClassDef(), newMethodUniqueName.toString()));		
		
		
		this.addAction(new AddFormalArg(method, name, type));
		
		this.addAction(new AddImportClause(method.getClassDef(), type));
		
				
		this.addPostcondition(new HasFormalArgWithName(method, name));
		
		this.addPostcondition(new ExistsMethodWithNameInClass(method.getClassDef(), 
			newMethodUniqueName));
	}
}