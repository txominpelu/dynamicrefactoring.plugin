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


import javamoon.core.expression.JavaCallExpr;

import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;

import moon.core.entity.FunctionDec;
import moon.core.entity.Result;

import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.expression.Expr;

import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.Instr;

import refactoring.engine.Predicate;
import repository.moon.MOONRefactoring;
import repository.moon.RepositoryUtils;
import repository.moon.concretefunction.MethodInstructionsCollector;

/**
 * Permite verificar que no existen llamadas a un determinado método en ninguna
 * clase de un modelo MOON-Java.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class NotExistsCallToThisMethod extends Predicate {
	
	/**
	 * El método cuyas llamadas se buscan.
	 */
	private MethDec method;
	
	/**
	 * Identificadores de las expresiones ya analizadas, para evitar bucles debidos
	 * a la posiblidad de que <code>leftSide</code> o <code>rightSide</code> apunten
	 * a su vez a la expresión original.
	 */
	private ArrayList<Integer> exprIds;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de NotExistsCallToThisMethod.
	 *
	 * @param method el método cuyas llamadas se buscan.
	 */
	public NotExistsCallToThisMethod(MethDec method) {
		
		super("NotExistsCallToThisMethod:\n\t" + //$NON-NLS-1$
			  "Makes sure no calls to the given method " + //$NON-NLS-1$
			  '"' + method.getName().toString() + '"' + " exist" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
			  
		this.method = method;
		
		exprIds = new ArrayList<Integer>();
	}	
	
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si no existen llamadas al método en todo el 
	 * modelo; <code>false</code>, en caso contrario.
	 */	 
	@Override
	public boolean isValid() {
		
		Collection<ClassDef> allClasses = 
			MOONRefactoring.getModel().getClassDefSourceAvailable();
				
		for (ClassDef nextClass : allClasses){
			List<MethDec> modelMethods = nextClass.getMethDec();
			
			for (MethDec nextMethod : modelMethods){				
				MethodInstructionsCollector mic = 
					new MethodInstructionsCollector(nextMethod);
				
				for (Instr instruction : mic.getCollection()){
					
					if(instruction instanceof CallInstr){											
						if(! checkCallInstr((CallInstr)instruction))
							return false;

					}					
					else {
						if(instruction instanceof AssignmentInstr){							
							Expr expresion = 
								((AssignmentInstr)instruction).getRighSide();
											
							
							if(expresion instanceof CallExpr)
								if(!(checkCallExpr((CallExpr)expresion)))
									return false;
						}
					}
				}
			}	
		}
	
	return true;
	
	}
	
	/**
	 * Comprueba que una expresión de llamada de longitud uno no contiene una
	 * llamada al método especificado.
	 *
	 * @param exp la expresión de llamada de longitud uno.
	 *
	 * @return <code>true</code> si la expresión no contiene ninguna llamada al
	 * método; <code>false</code> en caso contrario.
	 */
	private boolean checkCallExpr(CallExpr exp){
		
		if(exp.getFirstElement() instanceof Result){			
			Result result = (Result)exp.getFirstElement();
									
			if(method instanceof FunctionDec){
				Result methRes = ((FunctionDec)method).getFunctionResultEntity();
				if(methRes.getFunctionDec().getUniqueName().equals(result.getFunctionDec().getUniqueName()))
					return false;
			}
		}
		
		// FIXME: Java dependent code.
		if (exp instanceof JavaCallExpr){
			Expr leftSide = ((JavaCallExpr)exp).getLeftSide();
			Expr rightSide = ((JavaCallExpr)exp).getRightSide();
			if (leftSide != null && ! exprIds.contains(leftSide.getId())){
				exprIds.add(leftSide.getId());
				if (!checkExpr(leftSide))
					return false;
			}
			if (rightSide != null && ! exprIds.contains(rightSide.getId())){
				exprIds.add(rightSide.getId());
				if (!checkExpr(rightSide))
					return false;
			}

			// RMS
			// if we have a method invocation, we have a JavaVoidResult
			// and a routine invocation
			else {
				return ! RepositoryUtils.isCallToVoidMethod(exp,method);			
			}
			
		}
		
		return true;
	}

	/**
	 * Comprueba que una expresión no contiene una llamada al método 
	 * especificado.
	 * 
	 * @param expr la expresión en la que se estudia la posible presencia de 
	 * llamadas al método.
	 * 
	 * @return <code>true</code> si la expresión no contiene ninguna llamada al
	 * método especificado; <code>false</code> en caso contrario.
	 */
	private boolean checkExpr(Expr expr){
		if (expr instanceof CallExpr)
			if (!checkCallExpr((CallExpr)expr))
				return false;
		return true;
	}
	
	/**
	 * Comprueba que una instrucción de llamada de longitud uno no contiene una
	 * llamada al método especificado.
	 *
	 * @param instr la instrucción de llamada de longitud uno.
	 *
	 * @return <code>true</code> si la instrucción no contiene ninguna llamada 
	 * al método; <code>false</code> en caso contrario.
	 */
	private boolean checkCallInstr(CallInstr instr){
		
		if(instr.getRoutineDec().equals(method))
			return false;
												
		for(int i=0; i < instr.getRealArguments().size(); i++){			
			Expr param = instr.getRealArgument(i);
			
			if(param instanceof CallExpr)				
				if(! (checkCallExpr((CallExpr)param)))
					return false;			
		}
		
		return true;
	}
}