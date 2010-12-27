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

package repository.moon.concretefunction;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.inheritance.InheritanceClause;

import refactoring.engine.Function;

/**
 * Permite obtener los ancestros de una determinada clase como 
 * <code>ClassDef</code>.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SuperclassCollector extends Function {
	
	/**
	 * Clase cuyos ancestros se quieren obtener.
	 */
	private ClassDef classDef;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de SuperclassCollector.
	 *
	 * @param classDef la clase cuyos ancestros se quieren obtener.
	 */
	public SuperclassCollector(ClassDef classDef) {
		super();
		this.classDef = classDef;
	}
	
	/**
	 * Sin implementación.
	 *
	 * @return null.
	 */
	public Object getValue() {
		return null;
	}
	
	/**
	 * Devuelve todos los ancestros de la clase seleccionada.
	 * 
	 * @return los ancestros de la clase seleccionada.
	 */
	public Collection<ClassDef> getCollection() {
		Collection<ClassDef> ancestors = new Vector<ClassDef>();
		recursiveAdd(classDef, ancestors);
		return ancestors;
	}
	
	/**
	 * Llamada recursiva que va subiendo por el árbol de ancestros y
	 * añadiéndolos a la lista.
	 * 
	 * @param cd clase cuyos ancestros se quieren obtener.
	 * @param ancestors lista de ancestros.
	 */
	private void recursiveAdd(ClassDef cd, Collection<ClassDef> ancestors) {

		List<InheritanceClause> collection = cd.getInheritanceClause();
		if (collection.isEmpty())
			return;

		else {
			for(InheritanceClause ic : collection){
				if(ic.getType() instanceof ClassType){
					ClassType ct2 = (ClassType)ic.getType();
					ancestors.add(ct2.getClassDef());
					// Se añaden a su vez los ancestros de los ancestros.
					recursiveAdd(ct2.getClassDef(), ancestors);
				}
			}
		}
		return;
	}	
}
