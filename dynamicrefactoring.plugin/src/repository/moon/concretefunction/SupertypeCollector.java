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
import java.util.List;

import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.inheritance.InheritanceClause;
import refactoring.engine.Function;

/**
 * Obtiene los supertipos de un determinado <code>ClassType</code>.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente.</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes.</A>
 */
public class SupertypeCollector extends Function {
	
	/**
	 * El <code>ClassType</code> del que se obtendr�n los supertipos.
	 */
	private ClassType classType;
    
	/**
	 * Constructor.
	 * 
	 * Obtiene una instancia de SupertypeCollector.
	 * 
	 * @param classType el <code>ClassType</code> cuyos supertipos se quieren
	 * obtener.
	 */
	public SupertypeCollector(ClassType classType) {
		super();
		this.classType = classType;
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
	 * Obtiene todos los supertipos del <code>ClassType</code> representado por
	 * {@link #classType}, incluyendo aqu�llos que se puedan obtener 
	 * recursivamente a través de sus supertipos directos.
	 * 
	 * @return los supertipos del <code>ClassType</code> representado por
	 * {@link #classType}, incluyendo aqu�llos que se puedan obtener
	 * recursivamente a través de sus supertipos directos.
	 */	
	@Override
	public Collection<ClassType> getCollection() {
		Collection<ClassType> supertypes = new ArrayList<ClassType>();

		// Un tipo es subtipo de si mismo.
		supertypes.add(classType);
		recursiveAdd(classType, supertypes);

		return supertypes;
	}

	/**
	 * A�ade recursivamente los supertipos.
	 * 
	 * @param ct <code>ClassType</code> cuyos supertipos se quieren obtener.
	 * @param supertypes colecci�n a la que a�adir los supertipos hallados.
	 */
	private void recursiveAdd(ClassType ct, Collection<ClassType> supertypes) {

		// Clase que implementa el tipo.
		ClassDef df = ct.getClassDef();
		List<InheritanceClause> inheritanceClauses = df.getInheritanceClause();
			
		for (InheritanceClause ic : inheritanceClauses){
			if(ic.getType() instanceof ClassType){
				ClassType nextClassType = (ClassType)ic.getType();
				supertypes.add(nextClassType);				
				recursiveAdd(nextClassType, supertypes);
			}
		}
		
		return;
	}
}