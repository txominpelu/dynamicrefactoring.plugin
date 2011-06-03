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


import java.util.Iterator;

import moon.core.Name;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import refactoring.engine.Predicate;
import repository.moon.concretefunction.FormalArgCollector;

/**
 * Permite verificar que no existe ningun parámetro formal cuyo nombre coincida
 * con el indicado, dentro de la signatura de un método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class HasNotFormalArgWithName extends Predicate {
	
	/**
	 * Método en el que se busca un parámetro con cierto nombre.
	 */
	private MethDec methDec;
	
	/**
	 * Nombre del parámetro cuya presencia en un método se quiere comprobar.
	 */
	private Name name;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia del predicado HasNotFormalArgWithName.
	 *
	 * @param methDec el método en que se busca el parámetro.
	 * @param newname el nombre con el que se busca un parámetro en el método.
	 */
	public HasNotFormalArgWithName(MethDec methDec, Name newname) {
		
		super("HasNotFormalArgWithName:\n\t" + //$NON-NLS-1$
			"Makes sure a formal argument named " + '"' + newname.toString() + '"' + //$NON-NLS-1$
			"does not exist within the signature of the method " + //$NON-NLS-1$
			'"' + methDec.getName().toString() + '"' + ".\n\n"); //$NON-NLS-1$
		
		this.methDec = methDec;
		this.name = newname;
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existe el parámetro en el método 
	 * especificado; <code>false</code> en caso contrario.
	 */	 
	public boolean isValid() {
		
		FormalArgCollector search = new FormalArgCollector(methDec);
		
		Iterator<FormalArgument> formalArgs = search.getCollection().iterator();
		
		while(formalArgs.hasNext()){
			FormalArgument fa = formalArgs.next();
			if(fa.getName().equals(name))
				return false;
		}
		return true;		
	}
}