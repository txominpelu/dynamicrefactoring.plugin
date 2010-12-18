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
import moon.core.classdef.ClassType;
import moon.core.genericity.FormalPar;

import repository.java.concreteaction.AddImportClause;
import repository.moon.MOONRefactoring;
import repository.moon.concreteaction.*;
import repository.moon.concretepredicate.*;

/**
 * Permite establecer los parámetros para una refactorización "SpecializeBoundF".
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SpecializeBoundF extends MOONRefactoring {

	/**
	 * Nombre de la refactorizacion.
	 */
	private static final String NAME = "SpecializeBoundF"; //$NON-NLS-1$
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de <code>SpecializeBoundF</code>.
	 * @param formalPar parámetro formal sobre el que se quiere eliminar la acotación F.
	 * @param oldInstantiation tipo de acotación original, instanciado a partir de
	 * una clase genérica y el propio parámetro formal como parámetro real de
	 * instanciación.
	 * @param newInstantiation nuevo tipo de acotación instanciado con el subtipo
	 * de la propia clase genérica adecuado.
	 * @param model el modelo sobre el que se va a efectuar la refactorización.
	 */
	public SpecializeBoundF(FormalPar formalPar, ClassType oldInstantiation, 
		ClassType newInstantiation, Model model){
		
		super(NAME, model);
		
		this.addPrecondition(new IsBoundF(formalPar));		
		this.addPrecondition(
			new IsSubtypeBoundDesc(formalPar, oldInstantiation, newInstantiation));
		this.addPrecondition(
			new IsSingleGenericInstance(formalPar, newInstantiation));
		
		this.addAction(
			new ReplaceBoundType(formalPar, oldInstantiation, newInstantiation));
		this.addAction(new AddImportClause(formalPar.getClassDef(), newInstantiation));
		
		this.addPostcondition(new IsBoundType(formalPar, newInstantiation));
	}	
}