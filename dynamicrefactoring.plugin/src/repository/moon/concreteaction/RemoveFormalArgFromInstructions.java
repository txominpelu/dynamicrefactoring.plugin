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


import java.util.Iterator;

import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.entity.FunctionDec;
import moon.core.entity.Result;
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.RepositoryUtils;
import repository.moon.concretefunction.FormalArgRetriever;

/**
 * Permite eliminar un par�metro real de las instrucciones que contengan
 * una llamada a un determinado m�todo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RemoveFormalArgFromInstructions extends Action {
	
	/**
	 * Instrucciones en las que puede que haya que eliminar un argumento real.
	 */
	private Iterator<Instr> instructionIterator;
	
	/**
	 * El par�metro formal que se trata de eliminar de las llamadas a un m�todo.
	 */
	private FormalArgument deletedParameter;
	
	/**
	 * El m�todo de cuyas llamadas hay que eliminar un cierto par�metro real.
	 */
	private MethDec method;
	
	/**
	 * La posici�n ocupada por el argumento formal en la signatura del m�todo.
	 */
	private int argPosition;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
			 
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RemoveFormalArgFromInstructions.
	 *
	 * @param instrIt las instrucciones en las que, de contener llamadas al 
	 * m�todo, habr� que eliminar un argumento real.
	 * @param parameter el par�metro formal del m�todo cuyos valores actuales
	 * deben eliminarse de las llamadas al m�todo.
	 * @param method el m�todo de cuyas llamadas se va a eliminar un argumento.
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
	 * Elimina un determinado par�metro actual de las llamadas a un m�todo.
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
	 * Restaura los valores actuales del par�metro formal en las llamadas al 
	 * m�todo.<p>
	 *
	 * Actualmente, sin implementaci�n.
	 */
	public void undo() {
		listenerReg.notify(
			"# undo():RemoveFormalArgFromInstructions - �NOT IMPLEMENTED! #");		 //$NON-NLS-1$
	}
	
	/**
	 * Comprueba si una expresi�n de llamada de longitud uno contiene una
	 * llamada al m�todo especificado.
	 *
	 * @param exp la expresi�n de llamada de longitud uno.
	 *
	 * @return <code>true</code> si la expresi�n contiene una llamada al
	 * m�todo, <code>false</code> en caso contrario.
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
			return RepositoryUtils.isCallToVoidMethod(exp, method);		
		}
		
		return false;				
	}
}