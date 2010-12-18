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

import javamoon.core.expression.*;
import javamoon.core.entity.JavaVoidResult;

import moon.core.classdef.MethDec;
import moon.core.entity.SignatureEntity;
import moon.core.entity.Entity;

import moon.core.instruction.*;
import moon.core.expression.*;

import refactoring.engine.Predicate;
import repository.moon.concretefunction.MethodInstructionsCollector;

/**
 * Permite verificar si una determinada <code>SignatureEntity</code> (atributo 
 * de clase o argumento formal) es utilizada dentro del cuerpo de un cierto 
 * método.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class SignatureEntityIsUsedInMethod extends Predicate {
	
	/**
	 * Entidad cuya utilización en el cuerpo de un método se quiere estudiar.
	 */
	private SignatureEntity sigEnt;
	
	/**
	 * Método en cuyo cuerpo se estudiará la utilización de la entidad.
	 */
	private MethDec methDec;
	
	/**
	 * Identificadores de las expresiones ya analizadas, para evitar bucles debidos
	 * a la posiblidad de que <code>leftSide</code> o <code>rightSide</code> apunten
	 * a su vez a la expresión original.
	 */
	private ArrayList<Integer> exprIds;
	
	/**
	 * Constructor.<p>
	 *
	 * Devuelve una nueva instancia del predicado 
	 * SignatureEntityIsNotUsedInMethod.
	 *
	 * @param ent la entidad de signatura cuyo uso se desea estudiar.
	 * @param methDec el método en cuyo cuerpo se estudiará el uso de la entidad.
	 */
	public SignatureEntityIsUsedInMethod(SignatureEntity ent, MethDec methDec) {
		
		super("SignatureEntityIsUsedInMethod:\n\t" + //$NON-NLS-1$
			  "Checks whether the given entity " + '"' +  //$NON-NLS-1$
			  ent.getName().toString() + '"' + " is being used within the body "  //$NON-NLS-1$
			  + "of the given method " + '"' + methDec.getName().toString() +  //$NON-NLS-1$
			  '"' + " or not" + ".\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.sigEnt = ent;
		this.methDec = methDec;
		this.exprIds = new ArrayList<Integer>();
	}
		
	/**
	 * Comprueba el valor de verdad del predicado.
	 * 
	 * @return <code>true</code> si el valor de la entidad de signatura se 
	 * emplea en algún punto del cuerpo del método, <code>false</code> en caso 
	 * contrario.
	 */	 
	public boolean isValid() {		
		MethodInstructionsCollector collector = 
			new MethodInstructionsCollector(methDec);
		
		return checkInstructions(collector.getCollection());		
	}

	/**
	 * Comprueba si la entidad de signatura es usada en alguna de las 
	 * instrucciones de una colección.
	 *
	 * @param instructionsColl el conjunto de instrucciones entre las que se 
	 * estudia el posible uso de la entidad de signatura.
	 *
	 * @return <code>true</code> si la entidad es utilizada en al menos una 
	 * instrucción; <code>false</code> en caso contrario.
	 */
	private boolean checkInstructions(Collection<Instr> instructionsColl) {
		
		for (Instr instruction : instructionsColl){
 
			if(instruction instanceof AssignmentInstr){
				//aqui
				if(checkAssignmentInstr((AssignmentInstr) instruction))
					return true;
			}
			else if (instruction instanceof CallInstr) {
				if(checkCallInstr((CallInstr) instruction))
					return true;
			}
			else if (instruction instanceof CreationInstr) {
				if(checkCreationInstr((CreationInstr) instruction))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Comprueba si la entidad de signatura es usada en una instrucción de 
	 * asignación.
	 *
	 * @param instr la instrucción de asignación en la que se estudia el posible
	 * uso de la entidad de signatura.
	 *
	 * @return <code>true</code> si la entidad es utilizada en la instrucción, 
	 * <code>false</code> en caso contrario.
	 */
	private boolean checkAssignmentInstr(AssignmentInstr instr) {
		
		Expr leftSide = instr.getLeftSide();
		Expr right = instr.getRighSide();
		
		/*
		// FIXME: Java dependent code
		if (leftSide instanceof JavaArtificialEntityExpression){
			
			if(((JavaArtificialEntityExpression)leftSide).getExpression()
				instanceof CallExprLength1)
				if(checkCallExprLength1(
					((JavaArtificialEntityExpression)leftSide).getExpression()))
					return true;
		}*/
		if(leftSide instanceof JavaCallExprLength1){
			
			if(checkCallExprLength1((CallExprLength1) leftSide))
				return true;
		}
		
		if(right instanceof JavaCallExprLength1){
			
			if(checkCallExprLength1((CallExprLength1) right))
				return true;
		}
		

		return false;		
	}
	
	/**
	 * Comprueba si la entidad de signatura es usada en una instrucción de 
	 * llamada.
	 *
	 * @param callInstr la instrucción de llamada en la que se estudia el posible
	 * uso de la entidad de signatura.
	 *
	 * @return <code>true</code> si la entidad es utilizada en la instrucción, 
	 * <code>false</code> en caso contrario.
	 */
	private boolean checkCallInstr(CallInstr callInstr) {
		
		if(callInstr instanceof CallInstr){
			List<Expr> arguments = 
				((CallInstr)callInstr).getRealArguments();
			for (Expr argument : arguments)
				if (argument instanceof CallExprLength1)
					if (checkCallExprLength1((CallExprLength1)argument))
						return true;
		}
		return false;
	}
	
	/**
	 * Comprueba si la entidad de signatura es usada en una instrucción de 
	 * creación.
	 *
	 * @param creationInstr la instrucción de instanciación en la que se 
	 * estudia el posible uso de la entidad de signatura.
	 *
	 * @return <code>true</code> si la entidad es utilizada en la instrucción, 
	 * <code>false</code> en caso contrario.
	 */
	private boolean checkCreationInstr(CreationInstr creationInstr) {
		Entity entity = creationInstr.getEntity();
		
		return (entity == sigEnt);
	}
	
	/**
	 * Comprueba si la entidad de signatura forma parte de una CallExprLength1.
	 *
	 * @param callExpr la expresión de llamada de longitud uno en la que se 
	 * estudia el posible uso de la entidad de signatura.
	 *
	 * @return <code>true</code> si la entidad es utilizada en la expresión 
	 * de llamada; <code>false</code> en caso contrario.
	 */
	private boolean checkCallExprLength1(CallExprLength1 callExpr){
		
		if (callExpr.getFirstElement() == sigEnt)
			return true;
		
		List<Expr> realArguments = callExpr.getRealArguments();
		for (Expr argument : realArguments)
			if (argument instanceof CallExprLength1 &&
				checkCallExprLength1((CallExprLength1)argument))
				return true;
		
		// FIXME: Java dependent code.
		if (callExpr instanceof JavaCallExprLength1){
			Expr leftSide = ((JavaCallExprLength1)callExpr).getLeftSide();
			Expr rightSide = ((JavaCallExprLength1)callExpr).getRightSide();
			if (leftSide != null && ! exprIds.contains(leftSide.getId())){
				exprIds.add(leftSide.getId());
				if(checkExpr(leftSide))
					return true;
			}
			if (rightSide != null && ! exprIds.contains(rightSide.getId())){
				exprIds.add(rightSide.getId());
				if(checkExpr(rightSide))
					return true;
			}
		}
		
		// FIXME: Java dependent code
		if (callExpr instanceof JavaCallExprLength1Creation){
			CallExprLength1 subexpr = ((JavaCallExprLength1Creation)callExpr).getExpression();
			return checkExpr(subexpr);
		}
		
		// RMS
		// if we have a method invocation, we have a JavaVoidResult
		// and a routine invocation
		else {
			if (callExpr.getFirstElement() instanceof JavaVoidResult){
				
				JavaVoidResult jvr  = (JavaVoidResult) callExpr.getFirstElement();
				if (jvr.getJavaRoutine() == this.methDec){
					return true;
				}
			}			
		}
		
		return false;
	}
	
	
	/**
	 * Comprueba si la entidad de signatura forma parte de una Expr.
	 * 
	 * @param expr la expresión en la que se estudia el posible uso de la
	 * entidad de signatura.
	 * 
	 * @return <code>true</code> si la entidad es utilizada en la expresión
	 * de llamada; <code>false</code> en caso contrario.
	 */
	private boolean checkExpr(Expr expr){
		if (expr instanceof CallExprLength1)
			if (checkCallExprLength1((CallExprLength1)expr))
				return true;
		return false;
	}
}