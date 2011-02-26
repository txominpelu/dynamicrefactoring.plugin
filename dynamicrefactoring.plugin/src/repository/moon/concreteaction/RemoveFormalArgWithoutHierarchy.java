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


import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.instruction.Instr;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;
import repository.moon.concretefunction.MethodCollector;

/**
 * Permite eliminar un argumento formal de la signatura de un m�todo.<p>
 *
 * Se ocupa de eliminarlo tanto en la definici�n del m�todo, como en todas las
 * llamadas al mismo.<p>
 *
 * No tiene en cuenta las posibles implicaciones derivadas del hecho de que 
 * existan otras clases de la jerarqu�a de herencia que definan o redefinan el 
 * mismo m�todo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RemoveFormalArgWithoutHierarchy extends Action {
	
	/**
	 * El par�metro formal que se va a eliminar de la signatura del m�todo.
	 */
	private FormalArgument deletedParameter;
	
	/**
	 * El m�todo de cuya signatura se va a eliminar el argumento formal.
	 */
	private MethDec method;
			
	/**
	 * La posici�n que ocupa el argumento formal dentro de la signatura del m�todo.
	 */
	private int paramPosition;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
			 
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RemoveFormalArgWithoutHierarchy.
	 *
	 * @param formalArg el argumento formal que se va a eliminar.
	 * @param method el m�todo de cuya signatura se va a eliminar un argumento.
	 */	
	public RemoveFormalArgWithoutHierarchy(FormalArgument formalArg, MethDec method){
		
		super();
		
		this.deletedParameter = formalArg;
		this.method = method;
		
		this.paramPosition = 
			method.getIndexFormalArg(deletedParameter.getUniqueName().toString());
		
		listenerReg = RelayListenerRegistry.getInstance();
	}	
	
	/**
	 * Elimina un par�metro formal de la signatura de un m�todo.
	 */
	public void run() {
		
		listenerReg.notify("# run():RemoveFormalArgWithoutHierarchy #"); //$NON-NLS-1$
			
		Collection<ClassDef> allClasses = MOONRefactoring.getModel().getClassDef();
		Iterator<ClassDef> classIterator = allClasses.iterator();
		Collection<MethDec> allMethods = new Vector<MethDec>();
		
		while(classIterator.hasNext()){
			MethodCollector methColl = 
				new MethodCollector((ClassDef)classIterator.next());
			Collection<MethDec> classMethods = methColl.getCollection();
			allMethods.addAll(classMethods);
		}		
		Iterator<MethDec> methodIterator = allMethods.iterator();
		
		while(methodIterator.hasNext()){
			MethDec nextMethod = methodIterator.next();
			Iterator<Instr> instrIterator = nextMethod.getInstructions().iterator();
			RemoveFormalArgFromInstructions remParam = 
				new RemoveFormalArgFromInstructions(
					instrIterator, deletedParameter, method);
			remParam.run();
		}
		
		listenerReg.notify("\t- Removing formal argument " +  //$NON-NLS-1$
			deletedParameter.getUniqueName().toString());
			
		method.remove(deletedParameter);	
	}

	/**
	 * Restaura el par�metro formal a la signatura del m�todo.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():RemoveFormalArgWithoutHierarchy #"); //$NON-NLS-1$
		
		listenerReg.notify("\t- Restoring formal argument " + //$NON-NLS-1$
			deletedParameter.getUniqueName());
		
		method.add(deletedParameter, paramPosition);		
	}
}