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

import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.Type;
import moon.core.genericity.BoundS;
import moon.core.genericity.FormalPar;
import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;
import repository.moon.concretefunction.SuperclassCollector;

/**
 * Comprueba que el tipo de acotaci�n que ocupa una posici�n determinada en la
 * lista de acotaciones de un cierto par�metro formal en las subclases de una 
 * clase gen�rica es en todos los casos subtipo de un cierto tipo.
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsSubtypeBoundDesc extends Predicate {

	/**
	 * Par�metro formal cuyos tipos de acotaci�n en la jerarqu�a de herencia se
	 * estudian.
	 */
	private FormalPar formalPar;
	
	/**
	 * Clase a la que pertenece el par�metro formal.
	 */
	private ClassDef classDef;
	
	/**
	 * Tipo del que deben ser subtipo todos los tipos de acotaci�n del par�metro
	 * formal {@link #formalPar} que ocupan una cierta posici�n en las subclases 
	 * de {@link #classDef}.
	 */
	private ClassType classType;
	
	/**
	 * Posici�n de los tipos de acotaci�n del par�metro formal que deben ser
	 * subtipo del tipo indicado por {@link #classType}.
	 */
	private int position;
	
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsSubtypeBoundDesc</code>.
	 *
	 * @param formalPar par�metro formal cuyos tipos de acotaci�n en la jerarqu�a
	 * de herencia se estudian.
	 * @param referenceType tipo de acotaci�n a partir del cual se calcula la 
	 * posici�n de las acotaciones que deben cumplir la condici�n de subtipado.
	 * @param classType tipo del que deben ser subtipo todos los tipos de acotaci�n
	 * del par�metro que ocupen una determinada posici�n en la lista de acotaciones.
	 */
	public IsSubtypeBoundDesc(FormalPar formalPar,
		ClassType referenceType, ClassType classType) {
		
		super("IsSubtypeBoundDesc: \n\t" + //$NON-NLS-1$
			"Checks whether the formal parameter " + "'" +   //$NON-NLS-1$ //$NON-NLS-2$
			formalPar.getName().toString() + "'" +  //$NON-NLS-1$
			" has a bounding type that is a subtype of " +  //$NON-NLS-1$
			classType.getUniqueName().toString() + " in all the " + //$NON-NLS-1$
			"subclasses of the given class " + '"' +  //$NON-NLS-1$
			formalPar.getClassDef().getName().toString() + '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
				
		this.formalPar = formalPar;
		this.classDef = formalPar.getClassDef();
		this.classType = classType;
		
		this.position = ((BoundS)formalPar).getBounds().indexOf(referenceType);
	}
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si en todas las subclases de {@link #classDef}
	 * el tipo de acotaci�n del par�metro formal {@link #formalPar} en la 
	 * posici�n indicada es subtipo del tipo representado por {@link #classType}. 
	 */	 
	public boolean isValid() {
		
		// Posici�n del par�metro formal en la superclase.
		int fpPosition = classDef.getFormalPars().indexOf(formalPar);
		
		// Lista de todas las clases del modelo.
		Collection<ClassDef> classes = MOONRefactoring.getModel().getClassDefSourceAvailable();
		
		for (ClassDef modelClass : classes){
			// Se obtienen las superclases de cada clase del modelo.
			SuperclassCollector ancestorCollector = 
				new SuperclassCollector(modelClass);
			Collection<ClassDef> ancestors = ancestorCollector.getCollection();
			
			// Se busca una superclase gen�rica que coincida con nuestra clase.
			for (ClassDef nextAncestor : ancestors){
				if (nextAncestor.isGeneric() && nextAncestor == classDef){
					// Se ha encontrado una, luego #modelClass es una subclase
					// gen�rica de la superclase estudiada.
					
					FormalPar subClassFormalPar = null;
					if (modelClass.getFormalPars().size() > fpPosition){
						// Se toma el par�metro formal equivalente al par�metro formal
						// de la superclase.
						subClassFormalPar = modelClass.getFormalPars().get(fpPosition);
					}
					
					// Si no est� acotado, no cumple la condici�n.
					if (subClassFormalPar == null || ! (subClassFormalPar instanceof BoundS))
						return false;
					BoundS subClassBound = (BoundS)subClassFormalPar;
					// Se toma el tipo de acotaci�n de la posici�n estudiada.
					Type boundType = subClassBound.getBounds().get(position);
					
					// Si no es un tipo ClassType, no cumple la condici�n.
					if (! (boundType instanceof ClassType))
						return false;
					IsSubtype subtype = new IsSubtype((ClassType)boundType, classType);
					if (! subtype.isValid())
						// En cuanto se encuentra una instancia que no lo cumple
						// se devuelve falso. Todas deben cumplirlo.
						return false;
				}
			}
		}
		// Si no se ha encontrado ning�n caso contrario, se devuelve verdadero.
		return true;
	}
}