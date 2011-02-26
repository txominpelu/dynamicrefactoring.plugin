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
import java.util.List;

import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import refactoring.engine.Function;

/**
 * Permite obtener un par�metro con un nombre concreto (en el caso de que exista)
 * de la signatura de un m�todo de una clase determinada.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class FormalArgRetriever extends Function {
	
	/**
	 * M�todo del que se quiere obtener un par�metro con un nombre concreto.
	 */
	private MethDec methDec;
	
	/**
	 * Par�metro cuya existencia dentro de la signatura de un m�todo se quiere 
	 * verificar.
	 */
	private FormalArgument formalArg;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de FormalArgRetriever.
	 * @param formalArg el par�metro cuya existencia en la signatura del m�todo
	 * se quiere comprobar.
	 * @param methDec el m�todo de cuya signatura se quiere obtener el par�metro 
	 * formal con un nombre concreto.
	 */
	public FormalArgRetriever(FormalArgument formalArg, MethDec methDec) {
		super();
		
		this.methDec = methDec;
		this.formalArg = formalArg;
	}

	/**
	 * Sin implementaci�n.
	 *
	 * @return null.
	 */
	public Collection<FormalArgument> getCollection() {
		return null;
	}
	
	/**
	 * Obtiene el par�metro formal con un nombre concreto de la signatura de un
	 * m�todo en caso de que exista.
	 *
	 * @return el par�metro con el nombre especificado.
	 */
	public Object getValue() {
			
		List<FormalArgument> formalArguments = methDec.getFormalArgument();
		
		for (FormalArgument fa : formalArguments)
			if (fa.getUniqueName().equals(formalArg.getUniqueName()))
				return fa;
		
		return null;
	}
}