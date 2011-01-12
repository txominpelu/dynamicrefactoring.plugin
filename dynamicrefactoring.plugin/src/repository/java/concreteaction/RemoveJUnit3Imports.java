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

package repository.java.concreteaction;

import java.util.ArrayList;
import java.util.List;

import moon.core.classdef.ClassDef;

import javamoon.core.classdef.JavaClassDef;
import javamoon.core.classdef.JavaImport;
import refactoring.engine.Action;

/**
 * Removes the JUnit3 imports.
 * 
 * @author Raúl Marticorena
 *
 */
public class RemoveJUnit3Imports extends Action{

	/**
	 * Class.
	 */
	private JavaClassDef classDef;
	
	/**
	 * Undo list.
	 */
	private List<JavaImport> undoList;
	
	/**
	 * Constructor.
	 * 
	 * @param cd Clase de la que se eliminan los imports. 
	 */
	public RemoveJUnit3Imports(ClassDef cd){
		this.classDef = (JavaClassDef) cd;
		this.undoList = new ArrayList<JavaImport>();
	}
	
	/**
	 * Removes the import clauses.
	 */
	@Override
	public void run() {
		List<JavaImport> l = classDef.getImport();
		List<JavaImport> list = new ArrayList<JavaImport>(l);
		for (JavaImport javaImport : list){			
			if (javaImport.getUniqueName().toString().contains("junit.framework")){
				undoList.add(javaImport);
				classDef.remove(javaImport);	
			}
		}		
	}

	/**
	 * Undo.
	 */
	@Override
	public void undo() {
		for(JavaImport javaImport : undoList){
			classDef.add(javaImport);
		}		
	}
}
