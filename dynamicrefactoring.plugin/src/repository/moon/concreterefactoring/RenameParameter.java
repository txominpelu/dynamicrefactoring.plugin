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
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.RenameFormalArg;
import repository.moon.concretepredicate.ExistsFormalArgInMethod;
import repository.moon.concretepredicate.HasNotFormalArgWithName;
import repository.moon.concretepredicate.NotExistsLocalDecWithName;

/**
 * Permite renombrar un parámetro formal dentro de la signatura de un método
 * perteneciente a una clase de un modelo.<p>
 *
 * Comprueba que exista el parámetro que se desea renombrar, que no exista ya 
 * otro parámetro en el método con el nuevo nombre y que no existen variables 
 * locales a ese método con nombre igual al nuevo.<p>
 *
 * Si la comprobación no falla, lleva a cabo el renombrado.<p>
 *
 * Finalmente, comprueba que el renombrado se ha llevado a cabo con �xito.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class RenameParameter extends MOONRefactoring {

	/**
	 * Nombre de la refactorizacion concreta.
	 */
	private static final String NAME = "RenameParameter"; //$NON-NLS-1$
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameParameter.
	 *
	 * @param newname el nuevo nombre que se le va a dar al argumento formal.
	 * @param formalArg el parámetro que se desea renombrar.
	 * @param meth el método que contiene el parámetro que se desea renombrar.
	 * @param model el modelo sobre el que se ejecuta la refactorización.
	 */
	public RenameParameter(Name newname, FormalArgument formalArg, MethDec meth,
		Model model) {
		
		super(NAME, model);
				
		this.addPrecondition(new ExistsFormalArgInMethod(formalArg, meth));
		
		this.addPrecondition(new HasNotFormalArgWithName(meth, newname));
		
		this.addPrecondition(new NotExistsLocalDecWithName(meth, newname));
		
		
		this.addAction(new RenameFormalArg(formalArg, newname));
		
		
		this.addPostcondition(new HasNotFormalArgWithName(meth, formalArg.getName()));
		
		this.addPostcondition(new ExistsFormalArgInMethod(formalArg, meth));
	}
}