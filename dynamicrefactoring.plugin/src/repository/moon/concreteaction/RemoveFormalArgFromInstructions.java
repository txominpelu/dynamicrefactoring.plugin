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

package repository.moon.concreteaction;


import java.util.*;

import javamoon.core.entity.JavaFunctionResult;

import moon.core.classdef.*;
import moon.core.entity.*;
import moon.core.instruction.*;
import moon.core.expression.*;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.RepositoryUtils;
import repository.moon.concretefunction.FormalArgRetriever;

/**
 * Permite eliminar un parámetro real de las instrucciones que contengan
 * una llamada a un determinado método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RemoveFormalArgFromInstructions extends Action {
	
	/**
	 * Instrucciones en las que puede que haya que eliminar un argumento real.
	 */
	private Iterator<Instr> instructionIterator;
	
	/**
	 * El parámetro formal que se trata de eliminar de las llamadas a un método.
	 */
	private FormalArgument deletedParameter;
	
	/**
	 * El método de cuyas llamadas hay que eliminar un cierto parámetro real.
	 */
	private MethDec method;
	
	/**
	 * La posición ocupada por el argumento formal en la signatura del método.
	 */
	private int argPosition;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
			 
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RemoveFormalArgFromInstructions.
	 *
	 * @param instrIt las instrucciones en las que, de contener llamadas al 
	 * método, habrá que eliminar un argumento real.
	 * @param parameter el parámetro formal del método cuyos valores actuales
	 * deben eliminarse de las llamadas al método.
	 * @param method el método de cuyas llamadas se va a eliminar un argumento.
	 */	
	public RemoveFormalArgFromInstructions(Iterator<Instr> instrIt, 
		FormalArgument parameter, MethDec method){
		
		super();
		
		this.instructionIterator = instrIt;
		this.deletedParameter = parameter;
		this.method = method;
		
		this.argPosition = method.getIndexFormalArg(
			parameter.getUniqueName().toString());
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Elimina un determinado parámetro actual de las llamadas a un método.
	 */
	public void run() {
		
		FormalArgRetriever getFormalArg = new FormalArgRetriever(this.deletedParameter, method);
		
		assert getFormalArg.getValue() != null : 
			"Formal argument doesn't belong to the method"; //$NON-NLS-1$
		
		listenerReg.notify("# run():RemoveFormalArgFromInstructions #"); //$NON-NLS-1$
		
		while(instructionIterator.hasNext()){
			Instr instruction = instructionIterator.next();
			
			if(instruction instanceof CompoundInstr){
				RemoveFormalArgFromInstructions remParam = 
					new RemoveFormalArgFromInstructions(
						((CompoundInstr)instruction).getInstructions().iterator(),
						deletedParameter, method);
				remParam.run();
			}
			else {
				if(instruction instanceof CallInstr){
					
					if(((CallInstr)instruction).getRoutineDec().equals(
						method))
						((CallInstr)instruction).getRealArguments().remove(
							argPosition);
					
					for(int i=0; i<((CallInstr)instruction).
						getRealArguments().size(); i++){
						
						Expr param = 
							((CallInstr)instruction).getRealArgument(i);
						
						if(param instanceof CallExpr)							
							if(checkCallExpr((CallExpr)param))
								((CallExpr)param).getRealArguments().
									remove(argPosition);
					}
				}
				else					
					if(instruction instanceof AssignmentInstr){
						
						Expr expresion = 
							((AssignmentInstr)instruction).getRighSide();
												
						if(expresion instanceof CallExpr)
							if(checkCallExpr((CallExpr)expresion))
								((CallExpr)expresion).getRealArguments().
									remove(argPosition);
					}
			}
		}	
	}

	/**
	 * Restaura los valores actuales del parámetro formal en las llamadas al 
	 * método.<p>
	 *
	 * Actualmente, sin implementación.
	 */
	public void undo() {
		listenerReg.notify(
			"# undo():RemoveFormalArgFromInstructions - ¡NOT IMPLEMENTED! #");		 //$NON-NLS-1$
	}
	
	/**
	 * Comprueba si una expresión de llamada de longitud uno contiene una
	 * llamada al método especificado.
	 *
	 * @param exp la expresión de llamada de longitud uno.
	 *
	 * @return <code>true</code> si la expresión contiene una llamada al
	 * método, <code>false</code> en caso contrario.
	 */
	private boolean checkCallExpr(CallExpr exp){
		
		if(exp.getFirstElement() instanceof Result){
			
			Result result = (Result)((CallExpr)exp).getFirstElement();
									
			if(method instanceof FunctionDec && 
				result.getFunctionDec() == method)
				return true;
		}
		
		// RMS
		// if we have a method invocation, we have a JavaVoidResult
		// and a routine invocation
		else {
			return RepositoryUtils.isCallToMethod(exp, method);		
		}
		
		return false;				
	}
}