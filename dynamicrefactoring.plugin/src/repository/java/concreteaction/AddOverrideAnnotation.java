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


import javamoon.core.JavaName;
import javamoon.core.classdef.JavaType;
import javamoon.core.entity.JavaAnnotationReference;
import javamoon.core.entity.JavaFunctionDec;
import javamoon.core.entity.JavaRoutineDec;
import moon.core.classdef.MethDec;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

/**
 * A�ade una la anotación @override encima del método que esta siendo redefinido. 
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class AddOverrideAnnotation extends Action{
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;

	/**
	 * Método al que se a�adir�n las anotaciones.
	 */
	private MethDec methdec;
	
	/**
	 * Anotaciones override.
	 */
	private static final String type = "java.lang.Override"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	/**
	 * Constructor.
	 * 
	 * @param method método al que se a�adir�n las anotaciones.
	 */
	public AddOverrideAnnotation(MethDec method){
		this.methdec = method;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * A�ade la anotación correspondiente.
	 */
	@Override
	public void run() {
		listenerReg.notify("# run():AddOverrideAnnotation #"); //$NON-NLS-1$
		
		JavaType javaType = (JavaType) MOONRefactoring.getModel().getType( new JavaName(type));
		JavaAnnotationReference jar = new JavaAnnotationReference(javaType,false,-1,-1);
		if (methdec instanceof JavaRoutineDec){
			((JavaRoutineDec)methdec).format(jar);
			((JavaRoutineDec)methdec).add(jar);
		}
		else{
			((JavaFunctionDec)methdec).format(jar);
			((JavaFunctionDec)methdec).add(jar);
		}
	}

	/**
	 * Deshace la anotación.
	 */
	@Override
	public void undo() {
		listenerReg.notify("# undo():AddOverrideAnnotation #"); //$NON-NLS-1$
		
		JavaType javaType = (JavaType) MOONRefactoring.getModel().getType(
				new JavaName(type));
		JavaAnnotationReference jar = new JavaAnnotationReference(javaType,
				false, -1, -1);
		if (methdec instanceof JavaRoutineDec) {
			((JavaRoutineDec) methdec).remove(jar);
			
		} else {
			((JavaFunctionDec) methdec).remove(jar);						
		}
	}
}
