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


import moon.core.Model;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import repository.moon.MOONRefactoring;
import repository.moon.concretepredicate.ExistsClass;
import repository.moon.concretepredicate.ExistsMethodWithNameInClass;
import repository.moon.concretepredicate.MethodIsNotConstructor;
import repository.moon.concretepredicate.MethodIsNotInSubNorSuperclass;
import repository.moon.concretepredicate.NotExistsMethodWithNameInClass;

/**
 * Permite renombrar un m�todo perteneciente a una clase de un modelo.<p>
 *
 * Comprueba que no exista ya otro m�todo en la misma clase con el nuevo nombre 
 * (o en otras clases de la jerarqu�a de herencia), que el m�todo pertenezca a 
 * la clase especificada, y que no sea constructor.<p>
 *
 * Si la comprobaci�n no falla, lleva a cabo el renombrado.<p>
 *
 * Finalmente, comprueba que el renombrado se ha llevado a cabo con �xito.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class RenameMethod extends MOONRefactoring {
	
	/**
	 * Nombre de la refactorizaci�n concreta.
	 */
	private static final String NAME = "RenameMethod"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameMethod.
	 *
	 * @param method el m�todo que se desea renombrar.
	 * @param classDef la clase que contiene el m�todo que se desea renombrar.
	 * @param newName el nuevo nombre que se le va a dar al m�todo.
	 * @param newUniqueName nuevo nombre �nico que tendr� el m�todo.
	 * @param model el modelo que contiene la clase cuyo m�todo se renombra.
	 */
	public RenameMethod(MethDec method, ClassDef classDef, Name newName,
		Name newUniqueName, Model model) {
					
		super(NAME, model);
		
		Name oldUniqueName = method.getUniqueName();
			
		this.addPrecondition(new ExistsClass(classDef));
		
		this.addPrecondition(new ExistsMethodWithNameInClass(classDef, oldUniqueName));
		
		this.addPrecondition(new MethodIsNotConstructor(method));
				
		this.addPrecondition(
			new NotExistsMethodWithNameInClass(classDef, newUniqueName)); 
			
		this.addPrecondition(
			new MethodIsNotInSubNorSuperclass(classDef, newUniqueName.toString()));
								
		this.addAction(new repository.moon.concreteaction.RenameMethod(
			method, classDef, newName));
				
		this.addPostcondition(new ExistsMethodWithNameInClass(classDef, newUniqueName));
			
		this.addPostcondition(new NotExistsMethodWithNameInClass(classDef, oldUniqueName));
	}
}