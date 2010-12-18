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


import java.util.Collection;

import moon.core.classdef.ClassType;

import moon.core.genericity.FormalPar;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.FormalParSubstitutionCollector;

/**
 * Comprueba si todas las sustituciones de un parámetro formal corresponden a un 
 * determinado tipo.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsSingleGenericInstance extends Predicate {
	
	/**
	 * Parámetro formal del que se quiere saber si las sustituciones son únicas.
	 */
	private FormalPar formalParam;
	
	/**
	 * Tipo con el que deberán corresponderse todas las sustituciones.
	 */
	private ClassType ctype;
	
	/**
	 * Constructor.
	 *
	 * Devuelve una nueva instancia del predicado <code>IsSubtypeBoundType</code>.
	 *
	 * @param formalParam parámetro formal cuyas sustituciones se estudian.
	 * @param ctype tipo con el que deben conformar las sustituciones.
	 */
	public IsSingleGenericInstance(FormalPar formalParam, ClassType ctype) {
		super("IsSingleGenericInstance:\n\t" + //$NON-NLS-1$
			  "Checks whether all the substitutions for the given formal " +  //$NON-NLS-1$
			  "parameter " + '"' + formalParam.getName().toString() + '"' + //$NON-NLS-1$
			  " correspond to the given type " + '"' +  //$NON-NLS-1$
			  ctype.getName().toString() + '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.formalParam = formalParam;
		this.ctype = ctype;
	}

	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si todas las sustituciones del parámetro formal 
	 * corresponden a un determinado tipo; <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {
		FormalParSubstitutionCollector sub = 
			new FormalParSubstitutionCollector(formalParam);
		Collection<ClassType> substitutions = sub.getCollection();
		
		if(substitutions.size() > 1)
			return false;
		else {
			if(substitutions.size() == 0)
				// No está completamente instanciado.
				return true;
			if(substitutions.size() > 0){
				if(!substitutions.contains(ctype))
					return false;
			}				
		}
		return true;
	}
}