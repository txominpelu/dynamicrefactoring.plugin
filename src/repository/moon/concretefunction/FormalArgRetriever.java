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

import refactoring.engine.Function;

import moon.core.classdef.MethDec;
import moon.core.classdef.FormalArgument;

import java.util.*;

/**
 * Permite obtener un parámetro con un nombre concreto (en el caso de que exista)
 * de la signatura de un método de una clase determinada.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class FormalArgRetriever extends Function {
	
	/**
	 * Método del que se quiere obtener un parámetro con un nombre concreto.
	 */
	private MethDec methDec;
	
	/**
	 * Parámetro cuya existencia dentro de la signatura de un método se quiere 
	 * verificar.
	 */
	private FormalArgument formalArg;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de FormalArgRetriever.
	 * @param formalArg el parámetro cuya existencia en la signatura del método
	 * se quiere comprobar.
	 * @param methDec el método de cuya signatura se quiere obtener el parámetro 
	 * formal con un nombre concreto.
	 */
	public FormalArgRetriever(FormalArgument formalArg, MethDec methDec) {
		super();
		
		this.methDec = methDec;
		this.formalArg = formalArg;
	}

	/**
	 * Sin implementación.
	 *
	 * @return null.
	 */
	public Collection<FormalArgument> getCollection() {
		return null;
	}
	
	/**
	 * Obtiene el parámetro formal con un nombre concreto de la signatura de un
	 * método en caso de que exista.
	 *
	 * @return el parámetro con el nombre especificado.
	 */
	public Object getValue() {
			
		List<FormalArgument> formalArguments = methDec.getFormalArgument();
		
		for (FormalArgument fa : formalArguments)
			if (fa.getUniqueName().equals(formalArg.getUniqueName()))
				return fa;
		
		return null;
	}
}