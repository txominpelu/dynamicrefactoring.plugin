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

package repository.moon.concreterefactoring;

import moon.core.Model;
import moon.core.classdef.*;
import moon.core.genericity.FormalPar;

import repository.java.concreteaction.AddImportClause;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.*;
import repository.moon.concretepredicate.*;

/**
 * Permite establecer los parámetros para una refactorización 
 * "ReplaceFormalParameterWithType".
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */
public class ReplaceFormalParameterWithType extends MOONRefactoring {

	/**
	 * Nombre de la refactorizacion.
	 */
    private static final String NAME = "ReplaceFormalParameterWithType"; //$NON-NLS-1$

    /**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de ReplaceFormalParameterWithType.
     * @param formalPar parámetro formal que se va a reemplazar por un tipo.
     * @param classType tipo por el que se va a reemplazar el parámetro formal.
     * @param model el modelo sobre el que se va a hacer la refactorización.
	 */
    public ReplaceFormalParameterWithType(FormalPar formalPar, 
    	ClassType classType, Model model) {
		
		super(NAME, model);
				
		ClassDef paramClass = formalPar.getClassDef();
		
		this.addPrecondition(new IsFormalPar(formalPar, paramClass));		
		this.addPrecondition(new IsSingleGenericInstance(formalPar, classType));

		this.addAction(new RemoveFormalParameter(formalPar));
		this.addAction(new SubstituteFormalParameter(formalPar, classType));
		this.addAction(new RemoveRealParameter(formalPar));
		this.addAction(new AddImportClause(paramClass, classType));
		
		this.addPostcondition(new IsNotFormalPar(formalPar, paramClass));
	}
}