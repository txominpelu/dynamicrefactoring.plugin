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

import moon.core.classdef.ClassDef;
import moon.core.genericity.FormalPar;
import refactoring.engine.Predicate;

/**
 * Comprueba que un parámetro formal no es un parámetro de una clase.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsNotFormalPar extends Predicate {
	
	/**
	 * Predicado opuesto.
	 */
	private Predicate predicate;
	
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsNotFormalPar</code>.
	 *
	 * @param formalParam parámetro formal cuya pertenencia a una clase se comprueba.
	 * @param classDef clase a la que no debe pertenecer el parámetro formal.
	 */
	public IsNotFormalPar(FormalPar formalParam, ClassDef classDef) {

		super("IsNotFormalPar:\n\t" + //$NON-NLS-1$
			  "Makes sure the formal parameter " + '"' +  //$NON-NLS-1$
			  formalParam.getName().toString() + '"' + 
			  " is not a formal parameter of the class " + '"' +  //$NON-NLS-1$
			  classDef.getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$
		
		predicate = new IsFormalPar(formalParam, classDef);
	}	

	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el parámetro formal no pertenece a la clase;
	 * <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {		
		return !predicate.isValid();	
	}
}