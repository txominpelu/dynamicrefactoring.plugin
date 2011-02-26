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

import java.util.List;

import moon.core.classdef.ClassType;
import moon.core.classdef.Type;
import moon.core.genericity.BoundS;
import moon.core.genericity.FormalPar;
import refactoring.engine.Predicate;

/**
 * Comprueba que un par�metro formal de una clase presenta acotaci�n F.
 * 
 * <p>Para que un par�metro formal presente acotaci�n F deber� estar acotado por una
 * derivaci�n o instanciaci�n gen�rica no completa donde se utilice - recursivamente -
 * el propio par�metro formal.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsBoundF extends Predicate {

	/**
	 * Par�metro formal para el que se busca una acotaci�n F.
	 */
	private FormalPar formalParam;
		
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsBoundF</code>.
	 *
	 * @param formalParam par�metro formal sobre el que se estudia la presencia
	 * de una acotaci�n F.
	 */
	public IsBoundF(FormalPar formalParam) {
		super("IsBoundF:\n\t" + //$NON-NLS-1$
			  "Checks whether the class " + '"' +  //$NON-NLS-1$
			  formalParam.getClassDef().getName().toString() + '"' +
			  " has an F bounding or not." + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.formalParam = formalParam;
	}

	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el par�metro formal contiene una acotaci�n F;
	 * <code>false</code> en caso contrario.
	 */
	@Override
	public boolean isValid() {
		// El par�metro formal tiene que estar acotado.
		if (! (formalParam instanceof BoundS))
			return false;
		
		List<Type> bounds = ((BoundS)formalParam).getBounds();
		
		// Para cada acotaci�n.
		for(Type nextBound : bounds)
			// Se busca una acotaci�n por un tipo gen�rico.
			if(nextBound.getClassDef().isGeneric())
				// Adem�s, debe ser un tipo obtenido a partir de una clase.
				if(nextBound instanceof ClassType){
					// Se obtienen los par�metros con los que se instancia el tipo
					// gen�rico en la acotaci�n.
					List<Type> a = ((ClassType)nextBound).getRealParameters();
					
					// Si uno de los par�metros es el par�metro formal en cuesti�n...
					if (a.contains(formalParam))
						return true;
				}						
		
		return false;
	}
}