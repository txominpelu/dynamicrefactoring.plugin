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
import javamoon.core.classdef.JavaType;
import javamoon.core.entity.JavaAnnotationReference;
import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import refactoring.engine.Action;
import repository.moon.MOONRefactoring;

/**
 * Adds the JUnit4 annotations. For example, @Test,
 * @Before and @After depending on the name convention.
 * 
 * @author Ra�l Marticorena
 *
 */
public class AddJUnit4Annotation extends Action{

	/**
	 * Class.
	 */
	private JavaClassDef javaClassDef;
	
	/**
	 * Undo list.
	 */
	private List<MethDec> undoList;
	
	/**
	 * Prefixes.
	 */
	private static final String[] prefix = { "test", "setUp", "tearDown"};
	
	/**
	 * Annotations.
	 */
	private static final String[] type = { "org.junit.Test", "org.junit.Before", "org.junit.After"};
	
	/**
	 * Constructor.
	 * 
	 * @param jcd Clase en la que se a�adir� la anotaci�n.
	 */
	public AddJUnit4Annotation(ClassDef jcd){
		this.javaClassDef = (JavaClassDef) jcd;
		this.undoList = new ArrayList<MethDec>();
	}
	
	/**
	 * Adds the correct annotation depending on the method prefix.
	 */
	@Override
	public void run() {
		// for each prefix...
		for (int i = 0; i<prefix.length; i++){
			// for each method...
			for (MethDec md : javaClassDef.getMethDec()){
				if (md.getName().toString().startsWith(prefix[i])){
					undoList.add(md);
					JavaType javaType = (JavaType) MOONRefactoring.getModel().getType( new JavaName(type[i]));
					JavaAnnotationReference jar = new JavaAnnotationReference(javaType,false,-1,-1);
					if (md instanceof JavaRoutineDec){
						((JavaRoutineDec)md).format(jar);
						((JavaRoutineDec)md).add(jar);
					}
					else{
						((JavaFunctionDec)md).format(jar);
						((JavaFunctionDec)md).add(jar);
					}
				}
			} // for each method
		} // for each prefix
	}

	/**
	 * Undo.
	 */
	@Override
	public void undo() {		
		// for each modified method...
		for (MethDec md : undoList) {
			// for each prefix
			for (int i = 0; i < prefix.length; i++) {
				if (md.getName().toString().startsWith(prefix[i])) {
					JavaType javaType = (JavaType) MOONRefactoring.getModel().getType(
							new JavaName(type[i]));
					JavaAnnotationReference jar = new JavaAnnotationReference(javaType,
							false, -1, -1);
					if (md instanceof JavaRoutineDec) {
						((JavaRoutineDec) md).remove(jar);
						
					} else {
						((JavaFunctionDec) md).remove(jar);						
					}
				} // if contains the prefix
			} // for each prefix
		} // for each method
	} // undo

} // AddJUnit4Annotation
