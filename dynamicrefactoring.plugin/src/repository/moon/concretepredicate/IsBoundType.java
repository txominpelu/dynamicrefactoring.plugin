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

import moon.core.classdef.ClassType;
import moon.core.genericity.BoundS;
import moon.core.genericity.FormalPar;
import refactoring.engine.Predicate;

/**
 * Comprueba si un determinado <code>classType</code> forma parte de las 
 * acotaciones de un cierto parámetro formal.
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsBoundType extends Predicate {

	/**
	 * Parámetro formal entre cuyos tipos de acotación debe encontrarse el tipo 
	 * tratado.
	 */
	private FormalPar formalPar;
	
	/**
	 * Tipo que debe formar parte de los tipos de acotación de un parámetro formal.
	 */
	private ClassType classType;	
	
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsBoundType</code>.
	 *
	 * @param formalPar parámetro formal a cuya lista de acotaciones debe 
	 * pertenecer el tipo representado por {@link #classType}.
	 * @param classType tipo que debe formar parte de los tipos de acotación del
	 * parámetro formal representado por {@link #formalPar}.
	 */
	public IsBoundType(FormalPar formalPar, ClassType classType) {
		super("IsBoundType:\n\t" + //$NON-NLS-1$
			"Checks whether the given type " + "'" +  //$NON-NLS-1$ //$NON-NLS-2$
			classType.getName().toString() + "'" + //$NON-NLS-1$
			" is a bounding type of the formal parameter " + "'" +  //$NON-NLS-1$ //$NON-NLS-2$
			formalPar.getName().toString() + "'" + "or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		this.formalPar = formalPar;
		this.classType = classType;
	}

	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el tipo representado por {@link #classType}
	 * es uno de los tipos de acotación de {@link #formalPar}; <code>false</code>
	 * en caso contrario.
	 */	 
	public boolean isValid() {
		if (formalPar instanceof BoundS)
			if (((BoundS)formalPar).getBounds().contains(classType))
				return true;
		return false;
	}
}