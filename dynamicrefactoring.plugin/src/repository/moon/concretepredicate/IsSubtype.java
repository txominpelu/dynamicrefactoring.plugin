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


import java.util.ArrayList;
import java.util.Collection;

import moon.core.Name;
import moon.core.classdef.ClassType;
import refactoring.engine.Predicate;
import repository.moon.concretefunction.SupertypeCollector;

/**
 * Comprueba si un tipo es subtipo de otro.	
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 *
 */
public class IsSubtype extends Predicate {

	/**
	 * Tipo cuyo carácter de subtipo de otro se debe estudiar.
	 */
	private ClassType subType;
	
	/**
	 * Tipo del que debería ser subtipo {@link #subType}.  
	 */
	private ClassType superType;

    /**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsSubtype</code>.
	 *
	 * @param subType tipo cuyo carácter de subtipo de otro se debe estudiar.
	 * @param superType tipo del que debería ser subtipo {@link #subType}.
	 */
    public IsSubtype(ClassType subType, ClassType superType) {
    	super("IsSubtype:\n\t" +  //$NON-NLS-1$
    		"Checks whether the given type " + '"' +  //$NON-NLS-1$
    		subType.getName().toString() + '"' + " is a subtype of "  //$NON-NLS-1$
    		+ superType.getName().toString() + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$

    	this.subType = subType;
    	this.superType = superType;
    }

   /**
    * Comprueba el valor de verdad del predicado.
    * 
    * @return <code>true</code> si el tipo indicado por {@link #subType} es 
    * subtipo del representado por {@link #superType}; <code>false</code> 
    * en caso contrario.
    */
    public boolean isValid() {
        SupertypeCollector supertypeCollector = new SupertypeCollector(this.subType);
		Collection<ClassType> supertypes = supertypeCollector.getCollection();
		
		Name superUniqueName = superType.getUniqueName();

		// Se busca la superclase a través de su nombre único para evitar
		// el problema de los tipos decorados, que aun conteniendo el mismo
		// tipo interno, representan un objeto distinto, una "envoltura".
		ArrayList<Name> superNames = new ArrayList<Name>();
		for (ClassType nextSuperType : supertypes)
			superNames.add(nextSuperType.getUniqueName());
		
		if (superNames.contains(superUniqueName))
			return true;
        return false;
    }
}