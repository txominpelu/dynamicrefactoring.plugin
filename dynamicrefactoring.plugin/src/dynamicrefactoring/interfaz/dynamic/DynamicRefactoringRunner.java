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

package dynamicrefactoring.interfaz.dynamic;

import java.util.List;
import java.util.Map;

import refactoring.engine.Refactoring;
import dynamicrefactoring.RefactoringRunner;
import dynamicrefactoring.domain.DynamicRefactoring;
import dynamicrefactoring.domain.InputParameter;

/**
 * Permite ejecutar una operación de refactorización basado en una
 * refactorización compuesta dinámicamente, mostrando al usuario el progreso
 * de la operación y permitiendo su cancelación hasta cierto momento de la
 * refactorización.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringRunner extends RefactoringRunner {

	/**
	 * Refactorización dinámica concreta que permite ejecutar.
	 */
	private DynamicRefactoring refactoring;

	/**
	 * Constructor.
	 * 
	 * @param refactoring
	 *            refactorización dinámica concreta que permite ejecutar.
	 */
	public DynamicRefactoringRunner(DynamicRefactoring refactoring){
		this.refactoring = refactoring;
	}

	/**
	 * Obtiene el nombre de la refactorización que se permite ejecutar.
	 * 
	 * @return el nombre de la refactorización que se permite ejecutar.
	 */
	@Override
	protected String getRefactoringName(){
		return refactoring.getName();
	}

	/**
	 * Obtiene la refactorización que se permite ejecutar.
	 * 
	 * @return la refactorización que se permite ejecutar.
	 */
	@Override
	protected Refactoring getRefactoring(){
		return refactoring;
	}

	/**
	 * Obtiene la definicion entradas de una refactorización.
	 * 
	 * @return Descripción de los parámetros de entrada a la
	 *         refactorización.
	 */
	public List<InputParameter> getInputs() {
		return refactoring.getInputs();
	}

	/**
	 * Obtiene la definicion entradas de una refactorización.
	 * 
	 * @return Tabla con los parámetros de entrada a la refactorización.
	 */
	public Map<String, Object> getInputsParameters(){
		return refactoring.getInputsParameters();
	}
	
}