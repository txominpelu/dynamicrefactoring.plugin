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

package repository.moon.concretepredicate;

import java.util.*;

import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;

import refactoring.engine.Predicate;

/**
 * Permite verificar que existe un método con un cierto nombre único (es decir,
 * con una cierta signatura) en un clase determinada.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class ExistsMethodInClass extends Predicate {
		
	/**
	 * Nombre único del método cuya presencia en una clase se quiere comprobar.
	 */
	private MethDec method;
	
	/**
	 * Clase en la que se busca un método con cierto nombre.
	 */
	private ClassDef classDef;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ExistsMethodInClass.
	 * @param method el nombre único del método que se desea buscar.
	 * @param clase clase en la que se comprueba la existe
	 */
	public ExistsMethodInClass(MethDec method, ClassDef clase) {
		
		super("ExistsMethodInClass: \n\t" + //$NON-NLS-1$
			  "Checks whether the given method " + '"' + method.getName() + '"' + //$NON-NLS-1$
			  " belongs to the class " + '"' + method.getClassDef().getName() +  //$NON-NLS-1$
			  '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.method = method;
		this.classDef = clase;
	}	
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si existe el método en la clase especificada, 
	 * <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		List<MethDec> methods = classDef.getMethDec();
		
		for(MethDec nextMethod : methods)
			if (nextMethod.getUniqueName().equals(method.getUniqueName()))
				return true;
		
		return false;		
	}
}
