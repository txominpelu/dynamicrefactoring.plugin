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

package repository.java.concretepredicate;

import java.util.List;
import javamoon.core.entity.JavaAnnotationReference;
import javamoon.core.entity.JavaRoutineDec;
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Check if is a test method in JUnit4. It contains an annotation
 * @Test.
 * 
 * @author Raúl Marticorena
 *
 */
public class IsJUnit4TestMethod extends Predicate{

	/**
	 * Method.
	 */
	private MethDec methDec;
	
	/**
	 * Constructor.
	 * 
	 * @param methDec método.
	 */
	public IsJUnit4TestMethod(MethDec methDec){
		super("IsJUnit4TestMethod");
		this.methDec = methDec;
	}
	
	/**
	 * Checks if contains a fail instruction.
	 */
	@Override
	public boolean isValid() {		

		if (methDec instanceof JavaRoutineDec){
			List<JavaAnnotationReference> list = ((JavaRoutineDec) methDec).getAnnotations();
			for (JavaAnnotationReference jar : list){
				
				if (jar.getType().getUniqueName().toString().equals("org.junit.Test")){
					return true;
				}
			}
			return false;
		}
		else{
			return false;
		}
	}
	


}
