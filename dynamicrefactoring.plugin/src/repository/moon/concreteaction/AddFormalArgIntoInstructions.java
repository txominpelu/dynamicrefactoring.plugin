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

import java.util.ArrayList;
import java.util.List;

import javamoon.core.expression.JavaCallExpr;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.entity.FunctionDec;
import moon.core.entity.Result;
import moon.core.entity.RoutineDec;
import moon.core.expression.CallExpr;
import moon.core.expression.Expr;
import moon.core.expression.constant.ManifestConstant;
import moon.core.instruction.AssignmentInstr;
import moon.core.instruction.CallInstr;
import moon.core.instruction.CompoundInstr;
import moon.core.instruction.Instr;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;
import repository.moon.RepositoryUtils;

/**
 * Permite incluir un nuevo par�metro real en las instrucciones que contengan
 * una llamada a un determinado m�todo.<p>
 *
 * Si el par�metro real es de alguno de los tipos primitivos, le asigna su
 * valor habitual por defecto. Si es cualquier otro tipo de referencia, le 
 * asigna el valor null.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class AddFormalArgIntoInstructions extends Action {
	
	/**
	 * Instrucciones en las que puede que haya que a�adir un nuevo argumento real.
	 */
	private List<Instr> instructionList;
	
	/**
	 * Identificadores de las expresiones ya analizadas, para evitar bucles debidos
	 * a la posiblidad de que <code>leftSide</code> o <code>rightSide</code> apunten
	 * a su vez a la expresi�n original.
	 */
	private ArrayList<Integer> exprIds;
	
	/**
	 * El argumento formal que se ha a�adido al m�todo.
	 */
	private FormalArgument newParameter;
	
	/**
	 * El m�todo a cuyas llamadas hay que incluir un nuevo argumento real.
	 */
	private MethDec method;
		
	/**
	 * Valor por defecto que se asignar� al nuevo argumento.
	 */
	private ManifestConstant defaultValue;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de <code>AddFormalArgIntoInstructions</code>.
	 *
	 * @param instructions las instrucciones en las que, de contener llamadas al 
	 * m�todo, habr� que incluir un nuevo argumento real.
	 * @param parameter el argumento formal del m�todo en cuyas llamadas hay que 
	 * incluir un nuevo argumento real correspondiente.
	 * @param method el m�todo en cuyas llamadas se va a incluir un argumento.
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
	 * A�ade un argumento real a las llamadas a un m�todo.
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
	 * Elimina el nuevo argumento real de las llamadas al m�todo.
	 */
	@Override
	public void undo() {
		
		listenerReg.notify("# undo():AddFormalArgIntoInstructions #"); //$NON-NLS-1$
		
		exprIds = new ArrayList<Integer>();
		
		for(Instr instruction : instructionList)
			removeFormalArg(instruction);
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
			
			Result result = (Result)exp.getFirstElement();
							
			if(method instanceof FunctionDec)
				if(result.getFunctionDec() == method)
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
	
	/**
	 * A�ade el valor por defecto para el argumento formal en una instrucci�n.
	 * 
	 * @param instr instrucci�n en la que puede ser necesario a�adir un valor para
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
	 * A�ade el valor por defecto para el argumento formal en las componentes 
	 * de una instrucci�n compuesta en que sea necesario.
	 * 
	 * @param instr instrucci�n compuesta en cuyas componentes se a�adir� el 
	 * nuevo argumento en caso de ser necesario.
	 */
	private void addFormalArg(CompoundInstr instr){
		List<Instr> instructions = instr.getInstructions();
		for (Instr next : instructions)
			addFormalArg(next);
	}

	/**
	 * A�ade el valor por defecto para el argumento formal en una instrucci�n de
	 * llamada de longitud uno, en caso de que sea necesario.
	 * 
	 * @param instr instrucci�n de llamada de longitud uno.
	 */
	private void addFormalArg(CallInstr instr){
		RoutineDec routine = instr.getRoutineDec();
		
		if (routine == method){
			List<Expr> arguments = instr.getRealArguments();
			
			for (Expr next : arguments)
				if(next instanceof CallExpr)
					addFormalArg((CallExpr) next);
			
			arguments.add(defaultValue);
		}
	}

	
	/**
	 * A�ade el valor por defecto para el argumento formal en una instrucci�n de
	 * asignaci�n, en caso de que sea necesario.
	 * 
	 * @param instr instrucci�n de asignaci�n.
	 */
	private void addFormalArg(AssignmentInstr instr){
		
		addFormalArg(instr.getRighSide());
	}
	
	/**
	 * A�ade el valor por defecto para el argumento formal en una expresi�n de
	 * llamada de longitud uno, en caso de que sea necesario.
	 * 
	 * @param expr expresi�n de llamada de longitud uno.
	 */
	private void addFormalArg(CallExpr expr){
		
		
		if (checkCallExpr(expr)){
			// Se a�ade AL FINAL de la lista 
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
			if(nextAtom instanceof CallExpr)
				addFormalArg((CallExpr) nextAtom);
	}

	/**
	 * A�ade el valor por defecto para el argumento formal en una expresi�n, en
	 * caso de que sea necesario.
	 * 
	 * @param expr expresi�n a la que se a�ade un nuevo argumento en caso de 
	 * ser necesario.
	 */
	private void addFormalArg(Expr expr){
		if (expr instanceof CallExpr){			
			addFormalArg((CallExpr)expr);
		}
	}

	/**
	 * Si una instrucci�n contiene una llamada al m�todo modificado, elimina su
	 * �ltimo argumento real de la llamada.
	 *  
	 * @param instr instrucci�n de la que se elimina el �ltimo argumento, en caso
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
	 * Si una instrucci�n compuesta contiene llamadas al m�todo modificado, elimina
	 * en cada caso el �ltimo argumento formal de la llamada.
	 * 
	 * @param instr instrucci�n compuesta de la que se elimina el �ltimo argumento
	 * formal de las llamadas al m�todo, en caso de ser necesario.
	 */
	private void removeFormalArg(CompoundInstr instr){
		List<Instr> instructions = instr.getInstructions();
		for (Instr next : instructions)
			removeFormalArg(next);
	}

	/**
	 * Si una instrucci�n de llamada de longitud uno contiene llamadas al m�todo 
	 * modificado, elimina en cada caso el �ltimo argumento formal de la llamada.
	 * 
	 * @param instr instrucci�n de llamada de longitud uno de la que se elimina el 
	 * �ltimo argumento formal de las llamadas al m�todo, en caso de ser necesario.
	 */
	private void removeFormalArg(CallInstr instr){
		RoutineDec routine = instr.getRoutineDec();
		
		if (routine == method){
			List<Expr> arguments = instr.getRealArguments();
			arguments.remove(arguments.size() - 1);
			
			for (Expr next : arguments)
				if(next instanceof CallExpr)
					removeFormalArg((CallExpr) next);	
		}
	}

	/**
	 * Si una instrucci�n de asignaci�n contiene llamadas al m�todo 
	 * modificado, elimina en cada caso el �ltimo argumento formal de la llamada.
	 * 
	 * @param instr instrucci�n de asignaci�n de la que se elimina el 
	 * �ltimo argumento formal de las llamadas al m�todo, en caso de ser necesario.
	 */
	private void removeFormalArg(AssignmentInstr instr){
		removeFormalArg(instr.getRighSide());
	}

	/**
	 * Si una expresi�n de llamada de longitud uno contiene llamadas al m�todo 
	 * modificado, elimina en cada caso el �ltimo argumento formal de la llamada.
	 * 
	 * @param expr expresi�n de llamada de longitud dos de la que se elimina el 
	 * �ltimo argumento formal de las llamadas al m�todo, en caso de ser necesario.
	 */
	private void removeFormalArg(CallExpr expr){
		if (checkCallExpr(expr)){
			// Se a�ade EL �LTIMO de la lista.
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
			if(nextAtom instanceof CallExpr)
				removeFormalArg((CallExpr) nextAtom);
	}
	
	
	/**
	 * Si una expresi�n contiene llamadas al m�todo 
	 * modificado, elimina en cada caso el �ltimo argumento formal de la llamada.
	 * 
	 * @param expr expresi�n de la que se elimina el 
	 * �ltimo argumento formal de las llamadas al m�todo, en caso de ser necesario.
	 */
	private void removeFormalArg(Expr expr){
		if (expr instanceof CallExpr)
			removeFormalArg((CallExpr)expr);
	}
	
	
}