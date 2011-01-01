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

import javamoon.core.entity.JavaVoidResult;
import javamoon.core.expression.JavaCallExpr;

import moon.core.classdef.*;
import moon.core.entity.*;
import moon.core.expression.*;
import moon.core.expression.constant.*;
import moon.core.instruction.*;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

/**
 * Permite incluir un nuevo parámetro real en las instrucciones que contengan
 * una llamada a un determinado método.<p>
 *
 * Si el parámetro real es de alguno de los tipos primitivos, le asigna su
 * valor habitual por defecto. Si es cualquier otro tipo de referencia, le 
 * asigna el valor null.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class AddFormalArgIntoInstructions extends Action {
	
	/**
	 * Instrucciones en las que puede que haya que añadir un nuevo argumento real.
	 */
	private List<Instr> instructionList;
	
	/**
	 * Identificadores de las expresiones ya analizadas, para evitar bucles debidos
	 * a la posiblidad de que <code>leftSide</code> o <code>rightSide</code> apunten
	 * a su vez a la expresión original.
	 */
	private ArrayList<Integer> exprIds;
	
	/**
	 * El argumento formal que se ha añadido al método.
	 */
	private FormalArgument newParameter;
	
	/**
	 * El método a cuyas llamadas hay que incluir un nuevo argumento real.
	 */
	private MethDec method;
		
	/**
	 * Valor por defecto que se asignará al nuevo argumento.
	 */
	private ManifestConstant defaultValue;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de <code>AddFormalArgIntoInstructions</code>.
	 *
	 * @param instructions las instrucciones en las que, de contener llamadas al 
	 * método, habrá que incluir un nuevo argumento real.
	 * @param parameter el argumento formal del método en cuyas llamadas hay que 
	 * incluir un nuevo argumento real correspondiente.
	 * @param method el método en cuyas llamadas se va a incluir un argumento.
	 */
	public AddFormalArgIntoInstructions(List<Instr> instructions, 
		FormalArgument parameter, MethDec method){
		
		super();
		 
		this.instructionList = instructions;
		this.newParameter = parameter;
		this.method = method;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}	
	
	/**
	 * Añade un argumento real a las llamadas a un método.
	 */
	@Override
	public void run() {
		
		listenerReg.notify("# run():AddFormalArgIntoInstructions #"); //$NON-NLS-1$
		
		if(newParameter.getType().getName().toString().equals("boolean")) //$NON-NLS-1$
			defaultValue = 
				MOONRefactoring.getModel().getMoonFactory().createBooleanConstant();
		else {
			if( newParameter.getType().getName().toString().equals("int") || //$NON-NLS-1$
				newParameter.getType().getName().toString().equals("byte") || //$NON-NLS-1$
				newParameter.getType().getName().toString().equals("short")|| //$NON-NLS-1$
				newParameter.getType().getName().toString().equals("long") || //$NON-NLS-1$
				newParameter.getType().getName().toString().equals("float")|| //$NON-NLS-1$
				newParameter.getType().getName().toString().equals("double"))  //$NON-NLS-1$
				defaultValue = 
					MOONRefactoring.getModel().getMoonFactory().createIntegerConstant(); 
			else {
				if(newParameter.getType().getName().toString().equals("char")) //$NON-NLS-1$
					defaultValue = 
						MOONRefactoring.getModel().getMoonFactory().createCharConstant();
				else 
					defaultValue = 
						MOONRefactoring.getModel().getMoonFactory().createNilConstant();
			}
		}
		defaultValue.setType(newParameter.getType());
		exprIds = new ArrayList<Integer>();
		
		for(Instr instruction : instructionList){			
			addFormalArg(instruction);
		}
	}

	/**
	 * Elimina el nuevo argumento real de las llamadas al método.
	 */
	@Override
	public void undo() {
		
		listenerReg.notify("# undo():AddFormalArgIntoInstructions #"); //$NON-NLS-1$
		
		exprIds = new ArrayList<Integer>();
		
		for(Instr instruction : instructionList)
			removeFormalArg(instruction);
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
	private boolean checkCallExprLength1(CallExprLength1 exp){
		
		if(exp.getFirstElement() instanceof Result){
			
			Result result = (Result)exp.getFirstElement();
							
			if(method instanceof FunctionDec)
				if(result.getFunctionDec() == method)
					return true;
		}
		
		// RMS
		// if we have a method invocation, we have a JavaVoidResult
		// and a routine invocation
		else {
			if (exp.getFirstElement() instanceof JavaVoidResult){
				
				JavaVoidResult jvr  = (JavaVoidResult) exp.getFirstElement();
				if (jvr.getJavaRoutine() == method){
					return true;
				}
			}			
		}
			
		return false;	
	}
	
	/**
	 * Añade el valor por defecto para el argumento formal en una instrucción.
	 * 
	 * @param instr instrucción en la que puede ser necesario añadir un valor para
	 * el argumento como argumento real.
	 */
	private void addFormalArg(Instr instr){
		if (instr instanceof CompoundInstr)
			addFormalArg((CompoundInstr)instr);
		else if (instr instanceof CallInstr)
			addFormalArg((CallInstr)instr);
		else if (instr instanceof AssignmentInstr){
			
			addFormalArg((AssignmentInstr)instr);
		}
	}

	/**
	 * Añade el valor por defecto para el argumento formal en las componentes 
	 * de una instrucción compuesta en que sea necesario.
	 * 
	 * @param instr instrucción compuesta en cuyas componentes se añadirá el 
	 * nuevo argumento en caso de ser necesario.
	 */
	private void addFormalArg(CompoundInstr instr){
		List<Instr> instructions = instr.getInstructions();
		for (Instr next : instructions)
			addFormalArg(next);
	}

	/**
	 * Añade el valor por defecto para el argumento formal en una instrucción de
	 * llamada de longitud uno, en caso de que sea necesario.
	 * 
	 * @param instr instrucción de llamada de longitud uno.
	 */
	private void addFormalArg(CallInstr instr){
		RoutineDec routine = instr.getRoutineDec();
		
		if (routine == method){
			List<Expr> arguments = instr.getRealArguments();
			
			for (Expr next : arguments)
				if(next instanceof CallExprLength1)
					addFormalArg((CallExprLength1) next);
			
			arguments.add(defaultValue);
		}
	}

	
	/**
	 * Añade el valor por defecto para el argumento formal en una instrucción de
	 * asignación, en caso de que sea necesario.
	 * 
	 * @param instr instrucción de asignación.
	 */
	private void addFormalArg(AssignmentInstr instr){
		
		addFormalArg(instr.getRighSide());
	}
	
	/**
	 * Añade el valor por defecto para el argumento formal en una expresión de
	 * llamada de longitud uno, en caso de que sea necesario.
	 * 
	 * @param expr expresión de llamada de longitud uno.
	 */
	private void addFormalArg(CallExprLength1 expr){
		
		
		if (checkCallExprLength1(expr)){
			// Se añade AL FINAL de la lista 
			// el valor por defecto para el nuevo argumento.
			expr.setRealArgument(defaultValue);
		}
		// FIXME: Java dependent code.
		else if (expr instanceof JavaCallExpr){
			
			Expr leftSide = ((JavaCallExpr)expr).getLeftSide();
			
			Expr rightSide = ((JavaCallExpr)expr).getRightSide();
			
			if (leftSide != null && ! exprIds.contains(leftSide.getId())){
				exprIds.add(leftSide.getId());
				addFormalArg(leftSide);
			}
			if (rightSide != null && ! exprIds.contains(rightSide.getId())){
				exprIds.add(rightSide.getId());
				addFormalArg(rightSide);
			}
		}
		
		for(Expr nextAtom : expr.getRealArguments())
			if(nextAtom instanceof CallExprLength1)
				addFormalArg((CallExprLength1) nextAtom);
	}

	/**
	 * Añade el valor por defecto para el argumento formal en una expresión, en
	 * caso de que sea necesario.
	 * 
	 * @param expr expresión a la que se añade un nuevo argumento en caso de 
	 * ser necesario.
	 */
	private void addFormalArg(Expr expr){
		if (expr instanceof CallExprLength1){			
			addFormalArg((CallExprLength1)expr);
		}
	}

	/**
	 * Si una instrucción contiene una llamada al método modificado, elimina su
	 * último argumento real de la llamada.
	 *  
	 * @param instr instrucción de la que se elimina el último argumento, en caso
	 * de ser necesario.
	 */
	private void removeFormalArg(Instr instr){
		if (instr instanceof CompoundInstr)
			removeFormalArg((CompoundInstr)instr);
		else if (instr instanceof CallInstr)
			removeFormalArg((CallInstr)instr);
		else if (instr instanceof AssignmentInstr)
			removeFormalArg((AssignmentInstr)instr);
	}
	
	/**
	 * Si una instrucción compuesta contiene llamadas al método modificado, elimina
	 * en cada caso el último argumento formal de la llamada.
	 * 
	 * @param instr instrucción compuesta de la que se elimina el último argumento
	 * formal de las llamadas al método, en caso de ser necesario.
	 */
	private void removeFormalArg(CompoundInstr instr){
		List<Instr> instructions = instr.getInstructions();
		for (Instr next : instructions)
			removeFormalArg(next);
	}

	/**
	 * Si una instrucción de llamada de longitud uno contiene llamadas al método 
	 * modificado, elimina en cada caso el último argumento formal de la llamada.
	 * 
	 * @param instr instrucción de llamada de longitud uno de la que se elimina el 
	 * último argumento formal de las llamadas al método, en caso de ser necesario.
	 */
	private void removeFormalArg(CallInstr instr){
		RoutineDec routine = instr.getRoutineDec();
		
		if (routine == method){
			List<Expr> arguments = instr.getRealArguments();
			arguments.remove(arguments.size() - 1);
			
			for (Expr next : arguments)
				if(next instanceof CallExprLength1)
					removeFormalArg((CallExprLength1) next);	
		}
	}

	/**
	 * Si una instrucción de asignación contiene llamadas al método 
	 * modificado, elimina en cada caso el último argumento formal de la llamada.
	 * 
	 * @param instr instrucción de asignación de la que se elimina el 
	 * último argumento formal de las llamadas al método, en caso de ser necesario.
	 */
	private void removeFormalArg(AssignmentInstr instr){
		removeFormalArg(instr.getRighSide());
	}

	/**
	 * Si una expresión de llamada de longitud uno contiene llamadas al método 
	 * modificado, elimina en cada caso el último argumento formal de la llamada.
	 * 
	 * @param expr expresión de llamada de longitud dos de la que se elimina el 
	 * último argumento formal de las llamadas al método, en caso de ser necesario.
	 */
	private void removeFormalArg(CallExprLength1 expr){
		if (checkCallExprLength1(expr)){
			// Se añade EL ÚLTIMO de la lista.
			List<Expr> arguments = expr.getRealArguments();
			arguments.remove(arguments.size() - 1);
		}

		// FIXME: Java dependent code.
		else if (expr instanceof JavaCallExpr){
			Expr leftSide = ((JavaCallExpr)expr).getLeftSide();
			Expr rightSide = ((JavaCallExpr)expr).getRightSide();
						
			if (leftSide != null && ! exprIds.contains(leftSide.getId())){
				exprIds.add(leftSide.getId());
				removeFormalArg(leftSide);
			}
			if (rightSide != null && ! exprIds.contains(rightSide.getId())){
				exprIds.add(rightSide.getId());
				removeFormalArg(rightSide);
			}
		}
		
		for(Expr nextAtom : expr.getRealArguments())
			if(nextAtom instanceof CallExprLength1)
				removeFormalArg((CallExprLength1) nextAtom);
	}
	
	
	/**
	 * Si una expresión contiene llamadas al método 
	 * modificado, elimina en cada caso el último argumento formal de la llamada.
	 * 
	 * @param expr expresión de la que se elimina el 
	 * último argumento formal de las llamadas al método, en caso de ser necesario.
	 */
	private void removeFormalArg(Expr expr){
		if (expr instanceof CallExprLength1)
			removeFormalArg((CallExprLength1)expr);
	}
	
	
}