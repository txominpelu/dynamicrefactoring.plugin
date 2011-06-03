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

import moon.core.classdef.LocalDec;
import moon.core.classdef.MethDec;
import refactoring.engine.Function;

/**
 * Permite obtener el conjunto de variables locales de un método determinado.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class LocalDecCollector extends Function {
	
	/**
	 * Método cuyas variables locales se desea obtener.
	 */
	private MethDec methDec;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de LocalDecCollector.
	 *
	 * @param methDec el método cuyas variables locales se quieren obtener.
	 */
	public LocalDecCollector(MethDec methDec) {
		super();
		
		this.methDec = methDec;
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
	 * Obtiene el conjunto de variables locales de un método.
	 *
	 * @return el conjunto de variables locales del método.
	 */
	public Collection<LocalDec> getCollection() {
		return methDec.getLocalDecs();
	}
}