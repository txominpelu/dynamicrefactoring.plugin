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

import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import refactoring.engine.Function;

/**
 * Permite obtener el conjunto de todos los métodos de una clase determinada.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodCollector extends Function {
	
	/**
	 * Clase cuyos métodos se quieren obtener.
	 */
	private ClassDef classDef;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodCollector.
	 *
	 * @param classDef la clase cuyos métodos se quieren obtener.
	 */
	public MethodCollector(ClassDef classDef) {
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
	 * Obtiene el conjunto de métodos de una clase dada.
	 *
	 * @return El conjunto de métodos de una clase.
	 */
	public Collection<MethDec> getCollection() {
		return classDef.getMethDec();
	}
}