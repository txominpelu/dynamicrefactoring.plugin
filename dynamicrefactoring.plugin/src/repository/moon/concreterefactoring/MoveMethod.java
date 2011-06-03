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
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import repository.moon.MOONRefactoring;
import repository.moon.concretepredicate.ExistsClass;
import repository.moon.concretepredicate.ExistsMethodInClass;
import repository.moon.concretepredicate.ExistsMethodWithNameInClass;
import repository.moon.concretepredicate.MethodIsNotAlreadyInSuperclasses;
import repository.moon.concretepredicate.MethodIsNotConstructor;
import repository.moon.concretepredicate.MethodIsNotDeferred;
import repository.moon.concretepredicate.MethodNotUsesClassAttribute;
import repository.moon.concretepredicate.NotEqualClasses;
import repository.moon.concretepredicate.NotExistsCallToThisMethod;
import repository.moon.concretepredicate.NotExistsMethodInClass;
import repository.moon.concretepredicate.NotExistsMethodWithNameInClass;

/**
 * Permite mover un metodo de una clase de un modelo a otra clase del modelo.<p>
 *
 * Comprueba que existan ambas clases en el modelo y que no sean iguales, asi 
 * como que el metodo pertenezca a la clase seleccionada como clase de origen y 
 * no esta ya en la clase de destino. No se podra amover el metodo tampoco si se
 * trata de un constructor o si existen llamadas al mismo en alguna clase del 
 * modelo.<p>
 *
 * Si las comprobaciones no fallan, se lleva a cabo la refactorizacion.<p>
 *
 * Finalmente, comprueba que el proceso se ha llevado a cabo con exito.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class MoveMethod extends MOONRefactoring {

	/**
	 * Nombre de la refactorizacion.
	 */
	private static final String NAME = "MoveMethod"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MoveMethod.
	 * @param classDefDest la clase a la que se intentara mover el metodo.
	 * @param method el metodo que se desea mover.
	 * @param model el modelo que contiene el metodo que se desea mover.
	 */
	public MoveMethod(ClassDef classDefDest, MethDec method, Model model) {
		
		super(NAME, model);
		
		ClassDef sourceClass = method.getClassDef();

		int parameterTypesFirstIndex = 
			method.getUniqueName().toString().indexOf('%');
		
		String parameterTypesPart = ""; //$NON-NLS-1$
		if (parameterTypesFirstIndex >= 0)
			parameterTypesPart = method.getUniqueName().toString().substring(
				parameterTypesFirstIndex);

		this.addPrecondition(new ExistsMethodInClass(method,sourceClass));
		
		this.addPrecondition(new ExistsClass(sourceClass));
		
		this.addPrecondition(new ExistsClass(classDefDest));
		
		this.addPrecondition(new NotEqualClasses(sourceClass, classDefDest));
		
		this.addPrecondition(
			new ExistsMethodWithNameInClass(
				sourceClass, method.getUniqueName()));
		
		this.addPrecondition(new NotExistsMethodWithNameInClass(
			classDefDest, classDefDest.getUniqueName().concat(
					'~' + method.getName().toString() + parameterTypesPart)));
			
		this.addPrecondition(new MethodIsNotAlreadyInSuperclasses(method, classDefDest));
			
		this.addPrecondition(new MethodIsNotConstructor(method));
		
		this.addPrecondition(new NotExistsCallToThisMethod(method));
		
		this.addPrecondition(new MethodNotUsesClassAttribute(method));
				
		this.addPrecondition(new MethodIsNotDeferred(method));
		
		
		this.addAction(new repository.moon.concreteaction.MoveMethod(
			method, classDefDest));
		
		this.addPostcondition(new NotExistsMethodInClass(method,sourceClass));
		this.addPostcondition(new ExistsMethodWithNameInClass(
			classDefDest, classDefDest.getUniqueName().concat(
					'~' + method.getName().toString() + parameterTypesPart)));
			
		this.addPostcondition(new NotExistsMethodWithNameInClass(
			sourceClass, sourceClass.getUniqueName().concat(
					'~' + method.getName().toString() + parameterTypesPart)));
		
	}
}