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

package repository.java.concreterefactoring;

import moon.core.Model;
import moon.core.classdef.MethDec;
import repository.java.concreteaction.AddOverrideAnnotation;
import repository.java.concretepredicate.NotHasOverrideAnnotation;
import repository.moon.MOONRefactoring;
import repository.moon.concretepredicate.MethodSignatureIsInSuperclass;
import repository.java.concretepredicate.HasOverrideAnnotation;

/**
 * 
 * Añade una anotación override a un método.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class OverrideAnnotation extends MOONRefactoring {

	/**
	 * Name.
	 */
	private static final String NAME = "Override";
	
	/**
	 * añade una notación.
	 * 
	 * @param methDec método sobre el que se añade la notación.
	 * @param model modelo sobre el que se esta trabajando.
	 */
	public OverrideAnnotation(MethDec methDec, Model model) {
		super(NAME,model);
		// Preconditions
		this.addPrecondition(new NotHasOverrideAnnotation(methDec));
	    this.addPrecondition(new MethodSignatureIsInSuperclass(methDec, methDec.getClassDef()));
				
				
		// Actions
		this.addAction(new AddOverrideAnnotation(methDec));
		
		// Postconditions
		this.addPostcondition(new HasOverrideAnnotation(methDec));
		
	}
}
