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


import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import refactoring.engine.Function;
import refactoring.engine.Predicate;
import repository.moon.concretefunction.FormalArgRetriever;

/**
 * Permite verificar que existe el par�metro especificado dentro de la signatura
 * de un m�todo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class ExistsFormalArgInMethod extends Predicate {
	
	/**
	 * Par�metro formal cuya presencia en un m�todo se quiere comprobar.
	 */
	private FormalArgument formalArg;
	
	/**
	 * M�todo en el que se busca un cierto par�metro formal.
	 */
	private MethDec methDec;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado ExistsFormalArgInMethod.
	 *
	 * @param formalArg el par�metro formal que se desea buscar.
	 * @param methDec el m�todo en que se busca el par�metro formal.
	 */
	public ExistsFormalArgInMethod(FormalArgument formalArg, MethDec methDec) {
		
		super("ExistsFormalArgInMethod: \n\t" + //$NON-NLS-1$
			  "Checks whether the formal argument " + '"' +  //$NON-NLS-1$
			  formalArg.getName().toString() + '"' +
			  " belongs to the method " + '"' + methDec.getName().toString() +  //$NON-NLS-1$
			  '"' + "or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.formalArg = formalArg;
		this.methDec = methDec;
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si existe el parametro en el metodo 
	 * especificado, <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {		
		Function search = new FormalArgRetriever(formalArg, methDec);
		
		if (search.getValue()!= null )
			return true;
		return false;		
	}
}