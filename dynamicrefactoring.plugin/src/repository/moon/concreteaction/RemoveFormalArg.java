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

import moon.core.classdef.*;
import moon.core.instruction.Instr;
import moon.core.Name;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;
import repository.moon.concretefunction.*;

/**
 * Permite eliminar un argumento formal de la signatura de un método.<p>
 *
 * Se ocupa de eliminarlo tanto en la definición del método, como en todas las
 * llamadas al mismo.<p>
 *
 * Extiende el cambio a todas las clases que, por herencia, se puedan ver 
 * afectadas por un cambio en la signatura del método afectado.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RemoveFormalArg extends Action {
	
	/**
	 * El parámetro formal que se va a eliminar de la signatura del método.
	 */
	private FormalArgument deletedParameter;
	
	/**
	 * El método de cuya signatura se va a eliminar el argumento formal.
	 */
	private MethDec method;
	
	/**
	 * La clase a la que pertenece el método que se va a modificar.
	 */
	private ClassDef classDef;
		
	/**
	 * La posición que ocupa el argumento formal dentro de la signatura del
	 * método.
	 */
	private int paramPosition;	
		
	/**
	 * Elemento auxiliar para extender el cambio en el método a otras clases,
	 * en caso de que dicho método aparezca en clases superiores o inferiores en
	 * la jerarquía de herencia.
	 */
	private Vector<Action> removeParInOtherClassVec;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		 
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RemoveFormalArg.
	 *
	 * @param formalArg el argumento formal que se va a eliminar.
	 * @param method el método de cuya signatura se va a eliminar un argumento.
	 */	
	public RemoveFormalArg(FormalArgument formalArg, MethDec method){
		
		super();
		
		this.deletedParameter = formalArg;
		this.method = method;
		this.classDef = method.getClassDef();
		
		this.paramPosition = method.getIndexFormalArg(
			deletedParameter.getUniqueName().toString());
		
		this.removeParInOtherClassVec = new Vector<Action>(10,1);
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
		
	/**
	 * Elimina un parámetro formal de la signatura de un método.
	 */
	public void run() {
		
		listenerReg.notify("# run():RemoveFormalArg #"); //$NON-NLS-1$
		
		Collection<ClassDef> alreadyFoundClasses = new Vector<ClassDef>(10,1);
		
		ClassesAffectedByMethRenameCollector getSuperAndSubclasses =
			new ClassesAffectedByMethRenameCollector(classDef, 
				method.getUniqueName().toString(), alreadyFoundClasses,true);
				
		Collection<ClassDef> superAndSubclasses = 
			getSuperAndSubclasses.getCollection();
		
		superAndSubclasses.remove(classDef);
			
		removeArgFromSubAndSuperclasses(superAndSubclasses);	
			
		Collection<ClassDef> allClasses = MOONRefactoring.getModel().getClassDef();
		Iterator<ClassDef> classIterator = allClasses.iterator();
		Collection<MethDec> allMethods = new Vector<MethDec>();
		
		while(classIterator.hasNext()){
			MethodCollector methColl = 
				new MethodCollector(classIterator.next());
			Collection<MethDec> classMethods = methColl.getCollection();
			allMethods.addAll(classMethods);
		}		
		Iterator<MethDec> methodIterator = allMethods.iterator();
		
		while(methodIterator.hasNext()){
			MethDec nextMethod = methodIterator.next();
			Iterator<Instr> instrIterator = 
				nextMethod.getInstructions().iterator();
			RemoveFormalArgFromInstructions remParam =
				new RemoveFormalArgFromInstructions(
					instrIterator, deletedParameter, method);
			remParam.run();
		}
		
		listenerReg.notify("\t- Removing formal argument " + //$NON-NLS-1$
			deletedParameter.getUniqueName().toString());
		
		method.remove(deletedParameter);		
	}

	/**
	 * Restaura el parámetro formal a la signatura del método.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():RemoveFormalArg #"); //$NON-NLS-1$
		
		if(! removeParInOtherClassVec.isEmpty())
			for(int i = 0; i < removeParInOtherClassVec.size(); i++)
				removeParInOtherClassVec.get(i).undo();
		
		listenerReg.notify("\t- Restoring formal argument " + //$NON-NLS-1$
			deletedParameter.getUniqueName().toString());
		
		method.add(deletedParameter, paramPosition);
	}
	
	/**
	 * Elimina el parámetro de la signatura del método en las clases inferiores 
	 * y superiores de la jerarquía de herencia que, a través de herencia, 
	 * posean el mismo método (clases que hereden de la que posee el método
	 * afectado, o superclases de la misma que contengan el mismo método, y a su
	 * vez, recursivamente, subclases o superclases de las mismas).
	 *
	 * @param affectedClasses las clases de la jerarquía de herencia que se ven 
	 * afectadas por el cambio de la signatura del método.
	 */
	private void removeArgFromSubAndSuperclasses (
		Collection<ClassDef> affectedClasses){
		
		for (ClassDef affectedClass : affectedClasses){
			
			int indexOfMethodName =
				method.getUniqueName().toString().lastIndexOf('~');
			String methNameWithoutPath = 
				method.getUniqueName().toString().substring(indexOfMethodName);
			Name methUniqueName = 
				affectedClass.getUniqueName().concat(methNameWithoutPath);
			
			List<MethDec> affectedClassMethods = 
				affectedClass.getMethDecByName(method.getName());
			for(MethDec affectedClassMethod : affectedClassMethods)
				if (affectedClassMethod.getUniqueName().equals(methUniqueName)){
					
					for (FormalArgument nextArg : affectedClassMethod.getFormalArgument())
						if(nextArg.getName().equals(deletedParameter.getName())){
							Action action = new RemoveFormalArgWithoutHierarchy(
								nextArg, affectedClassMethod); 
							removeParInOtherClassVec.add(action);								
							action.run();
							break;							
						}
				}
		}
	}
}