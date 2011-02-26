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

import java.util.ArrayList;
import java.util.Collection;

import moon.core.Model;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import refactoring.engine.Function;
import repository.moon.MOONRefactoring;

/**
 * Obtiene los subtipos de un determinado <code>ClassType</code>.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente.</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes.</A>
 */
public class SubtypeCollector extends Function {
	
	/**
	 * El <code>ClassType</code> cuyos subtipos se deben obtener.
	 */
	private ClassType classType;
	
	/**
	 * Modelo MOON en que se buscan los subtipos.
	 */
	private Model model;
	
	/**
	 * Constructor.
	 * 
	 * Obtiene una nueva instancia de <code>SubtypeCollector</code>.
	 * 
	 * @param classType el <code>ClassType</code> cuyos subtipos se quieren
	 * obtener.
	 */
	public SubtypeCollector(ClassType classType) {
		super();
		this.classType = classType;
	}
	
	/**
	 * Constructor.
	 * 
	 * Obtiene una nueva instancia de <code>SubtypeCollector</code>.
	 * 
	 * @param classType el <code>ClassType</code> cuyos subtipos se quieren
	 * obtener.
	 * @param model el modelo MOON en que se buscan los subtipos.
	 */
	public SubtypeCollector(ClassType classType, Model model) {
		super();
		this.classType = classType;
		this.model = model;
	}
	
	/**
	 * Funci�n que devuelve el valor.
	 *  
	 * @return <code>null</code>.
	 */
	@Override
	public Object getValue() {
		return null;
	}

	/**
	 * Obtiene todos los subtipos del <code>ClassType</code> representado por
	 * {@link #classType}, incluyendo aqu�llos que se puedan obtener 
	 * recursivamente a trav�s de sus subtipos directos.
	 * 
	 * @return los subtipos del <code>ClassType</code> representado por
	 * {@link #classType}, incluyendo aqu�llos que se puedan obtener
	 * recursivamente a trav�s de sus subtipos directos.
	 */	
	@Override
	public Collection<ClassType> getCollection() {
		Collection<ClassType> subtypes = new ArrayList<ClassType>();

		// Un tipo es subtipo de si mismo.
		subtypes.add(classType);
		addSubclasses(classType, subtypes);
		
		return subtypes;
	}

	/**
	 * A�ade todos los subtipos de una clase a una colecci�n.
	 * 
	 * @param ct <code>ClassType</code> cuyos subtipos se quieren obtener.
	 * @param subtypes colecci�n a la que a�adir los subtipos hallados.
	 */
	private void addSubclasses(ClassType ct, Collection<ClassType> subtypes) {

		if (this.model == null)
			model = MOONRefactoring.getModel();
		
		Collection<ClassDef> classes = model.getClassDef();
		
		for (ClassDef modelClass : classes){
			// Se obtienen las superclases de cada clase del modelo.
			SuperclassCollector ancestorCollector = 
				new SuperclassCollector(modelClass);
			Collection<ClassDef> ancestors = ancestorCollector.getCollection();
			
			// Se busca una superclase que coincida con la de nuestro tipo.
			if (ancestors.contains(ct.getClassDef()))
				// Si entre sus superclases (buscadas recursivamente) est� nuestra
				// superclase, es que es un subtipo.
				subtypes.add(modelClass.getClassType());
		}
		
		return;
	}
}