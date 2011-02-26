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

import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;

/**
 * Permite verificar que un cierto m�todo no es abstracto.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class MethodIsNotDeferred extends Predicate {
	
	/**
	 * M�todo cuyo car�cter de m�todo abstracto se quiere verificar.
	 */
	private MethDec method;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MethodIsNotDeferred.
	 *
	 * @param method el m�todo cuyo car�cter de m�todo abstracto se quiere 
	 * verificar.
	 */
	public MethodIsNotDeferred(MethDec method) {
		super("MethodIsNotDeferred:\n\t" + //$NON-NLS-1$
			  "Makes sure the given method " + '"' + method.getName().toString() +  //$NON-NLS-1$
			  '"' + " is not abstract" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.method = method;
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el m�todo no es abstracto; <code>false
	 * </code> en caso contrario.
	 */	 
	@Override
	public boolean isValid() {
		return (!method.isDeferred());		
	}
}