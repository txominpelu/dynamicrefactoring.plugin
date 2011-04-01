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

import javamoon.core.JavaName;
import javamoon.core.classdef.JavaClassDef;
import javamoon.core.classdef.JavaImport;
import javamoon.core.classdef.JavaPackage;
import javamoon.core.classdef.JavaType;
import moon.core.classdef.ClassDef;
import refactoring.engine.Action;
import repository.moon.MOONRefactoring;

/**
 * Adds the particular java import clauses in the JUnit 4 version.
 * 
 * @author Raúl Marticorena
 * 
 */
public class AddJUnit4Imports extends Action{
	
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
	 * @param cd class
	 */
	public AddJUnit4Imports(ClassDef cd){
		this.classDef = (JavaClassDef)cd;
		this.undoList = new ArrayList<JavaImport>();
	}
	
	/**
	 * Adds the JUnit 4 import clauses.
	 */
	@Override
	public void run() {
		// add the imports
		String[] imports = {"org.junit.Test", 
				"org.junit.Before",
				"org.junit.After"};
		for (String s : imports){
			JavaType jt1= (JavaType) MOONRefactoring.getModel().getType(new JavaName(s));
			JavaImport ji1 = new JavaImport(jt1,true,-1,-1,false);
			classDef.format(ji1);
			classDef.add(ji1);	
			undoList.add(ji1);
		}
		
		// add the static imports
		JavaType jUnitAssertType = new JavaType(
				new JavaName("org.junit.Assert"), new JavaClassDef(
						new JavaName("Assert"), new JavaPackage(new JavaName(
								"org.junit"))));

		String[] staticImports = {"assertEquals","assertFalse",
				"assertTrue",
				"fail"};
		for (String s : staticImports){
			JavaImport ji2 = new JavaImport(jUnitAssertType,true,-1,-1,true);
			ji2.setPropertyName(new JavaName(s));
			classDef.format(ji2);
			classDef.add(ji2);
			undoList.add(ji2);
		}
	}

	/**
	 * Undo removing the java import clauses.
	 */
	@Override
	public void undo() {
		for (JavaImport ji : undoList){
			classDef.remove(ji);
		}		
	}
} // AddJUnit4ImportsAction


