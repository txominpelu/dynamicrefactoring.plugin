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

import moon.core.classdef.ClassDef;
import moon.core.Model;
import moon.core.Name;

import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.RenameClassType;
import repository.moon.concreteaction.RenameConstructors;
import repository.moon.concreteaction.RenameGenericClassType;
import repository.moon.concreteaction.RenameReferenceFile;
import repository.moon.concretepredicate.ExistsClass;
import repository.moon.concretepredicate.NotExistsClassWithName;

/**
 * Permite renombrar una clase de un modelo.<p>
 *
 * Comprueba que exista la clase que se desea renombrar y que no exista ya otra 
 * clase en el modelo con el nuevo nombre.<p>
 *
 * Si la comprobación no falla, lleva a cabo el renombrado.<p>
 *
 * Finalmente, comprueba que el renombrado se ha llevado a cabo con éxito.<p>
 *
 * Debido a que ni en MOON, ni en su extensión para Java existen paquetes como 
 * tal (aunque se está trabajando en su inclusión), se ha impuesto como
 * restriccion para realizar el renombrado de la clase, que no exista en el 
 * modelo ya una clase con el nuevo nombre. Esto puede ser una de las
 * futuras modificaciones que podrían llevarse a cabo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 */
public class RenameClassTestVersion extends MOONRefactoring {

	/**
	 * Nombre de la refactorización concreta.
	 */
	private static final String NAME = "RenameClass"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameClass.
	 *
	 * @param newname el nuevo nombre que se le va a dar a la clase.
	 * @param classDef la clase que se desea renombrar.
	 * @param model el modelo que contiene la clase que se desea renombrar.
	 */
	public RenameClassTestVersion(Name newname, ClassDef classDef, Model model) {
		
		super(NAME, model);
		
		this.addPrecondition(new ExistsClass(classDef));
		
		this.addPrecondition(new NotExistsClassWithName(newname));
		
		this.addAction(new repository.moon.concreteaction.RenameClass(
			classDef, newname));
		
		this.addAction(new RenameReferenceFile(classDef, newname));
		
		this.addAction(new RenameClassType(classDef, newname));
				
		this.addAction(new RenameGenericClassType(classDef, newname));
		
		this.addAction(new RenameConstructors(classDef, newname));
		
		this.addPostcondition(new NotExistsClassWithName(classDef.getName()));	
		
		this.addPostcondition(new ExistsClass(classDef));
	}
}