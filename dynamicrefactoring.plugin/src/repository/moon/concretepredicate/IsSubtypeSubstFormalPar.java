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


import moon.core.genericity.FormalPar;
import moon.core.classdef.ClassType;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.FormalParSubstitutionCollector;
import repository.moon.concretefunction.SupertypeCollector;

/**
 * Comprueba si todas las sustituciones de un parámetro formal en un modelo
 * son descendientes de un tipo determinado.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class IsSubtypeSubstFormalPar extends Predicate {
   
	/**
	 * Parámetro formal cuyas sustituciones en el modelo se deben estudiar.
	 */
    private FormalPar formalPar;
    
	/**
	 * Supertipo que deben tener en común todas las sustituciones del parámetro.
	 */
    private ClassType classType;
    
    /**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado <code>IsSubtypeSubstFormalPar
	 * </code>.
	 * 
	 * @param formalPar parámetro formal cuyas sustituciones se deben estudiar.
	 * @param classType supertipo que deben tener en común todas las sustituciones
	 * del parámetro.
	 */
    public IsSubtypeSubstFormalPar(FormalPar formalPar, ClassType classType) {
    	
        super("IsSubtypeSubstFormalPar:\n\t" + //$NON-NLS-1$
        	"Checks whether all the substitutions for the given formal " + //$NON-NLS-1$
        	"parameter " + '"' + formalPar.getName().toString() + '"' + //$NON-NLS-1$
        	" are subtypes of the given type " + '"' +  //$NON-NLS-1$
        	classType.getName().toString() + '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
        
        this.formalPar = formalPar;
        this.classType = classType;
    }

   /**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si todas las sustituciones son descendientes 
	 * del tipo representado por {@link #classType}; <code>false</code> en 
	 * caso contrario.
	 */	 
    @Override
    public boolean isValid() {

        FormalParSubstitutionCollector collector = 
        	new FormalParSubstitutionCollector(formalPar);

        Collection<ClassType> substitutions = collector.getCollection();
        Iterator<ClassType> substitutionsIt = substitutions.iterator();

        // Para cada sustitución.
        while (substitutionsIt.hasNext()) {
        	
            ClassType ct = (ClassType) substitutionsIt.next();
            
            // Se buscan sus supertipos.
            SupertypeCollector superTypeCollector = new SupertypeCollector(ct);
            Collection<ClassType> supertypes = superTypeCollector.getCollection();
            Iterator<ClassType> superTypesIt = supertypes.iterator();
            
            // El supertipo común buscado debería estar en la lista.
            boolean found = false;
            while (superTypesIt.hasNext()) {
                ClassType nextSuperType = (ClassType) superTypesIt.next();
                if (nextSuperType.equals(classType)) {
                    found = true;
                    break;
                }
            }
            
            // En cuanto se encuentre una sustitución que no cumple la condición
            // se puede devolver falso.
            if (!found)
                return false;
        }
        return true;
    }
}