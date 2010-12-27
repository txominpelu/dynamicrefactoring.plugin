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

package repository.moon.concreteaction;

import java.util.ArrayList;
import java.util.List;

import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.inheritance.InheritanceClause;
import refactoring.engine.Action;

/**
 * Removes one inheritance clause in a class.
 * 
 * @author Raúl Marticorena
 *
 */
public class RemoveInheritanceClause  extends Action{
	/**
	 * Class.
	 */
	private ClassDef classDef;
	
	/**
	 * Ancestor.
	 */
	private ClassType type;
	
	/**
	 * Undo clause.
	 */
	private InheritanceClause undo;
	
	/**
	 * Constructor.
	 * 
	 * @param cd class
	 * @param type ancestor
	 */
	public RemoveInheritanceClause(ClassDef cd, ClassType type){
		this.classDef = cd;
		this.type = type;
	}

	/**
	 * Removes the inheritance clause.
	 */
	@Override
	public void run() {
		List<InheritanceClause> list = new ArrayList<InheritanceClause>(classDef.getInheritanceClause());
		for(InheritanceClause ic : list){
			if (ic.getType().getUniqueName().toString().compareTo(type.getUniqueName().toString())==0){
				undo = ic;
				classDef.remove(ic);
				break;
			}
		}
		
	}

	/**
	 * Undo.
	 */
	@Override
	public void undo() {
		classDef.add(undo);		
	}
}