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


import java.util.*;

import moon.core.Name;
import moon.core.classdef.LocalDec;
import moon.core.classdef.MethDec;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.LocalDecCollector;

/**
 * Permite verificar que no existe ninguna variable local cuyo nombre coincida 
 * con el especificado, dentro de la signatura de un método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class NotExistsLocalDecWithName extends Predicate {
	
	/**
	 * Método en el que se busca una variable local con cierto nombre.
	 */
	private MethDec methDec;
	
	/**
	 * Nombre de la variable local cuya presencia se quiere comprobar.
	 */
	private Name newname;
		
	/** 
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado NotExistsLocalDecWithName.
	 *
	 * @param methDec el método en que se busca la variable local.
	 * @param newname el nombre con el que se busca una variable local al método.
	 */
	public NotExistsLocalDecWithName(MethDec methDec, Name newname) {
		
		super("NotExistsLocalDecWithName:\n\t" + //$NON-NLS-1$
			"Makes sure no local variable exists within " + //$NON-NLS-1$
			'"' + methDec.getName().toString() + '"' + " with the given name " +  //$NON-NLS-1$
			'"' + newname.toString() + '"' + ".\n\n"); //$NON-NLS-1$
		
		this.methDec = methDec;
		this.newname = newname;
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe la variable local en el método 
	 * especificado, <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		LocalDecCollector search = new LocalDecCollector(methDec);
		
		Iterator<LocalDec> localDec = search.getCollection().iterator();
		
		while(localDec.hasNext()){
			LocalDec ld = localDec.next();
			if(ld.getName().equals(newname))
				return false;
		}
		return true;		
	}
}