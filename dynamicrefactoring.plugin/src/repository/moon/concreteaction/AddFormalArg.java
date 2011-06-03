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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.instruction.Instr;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;
import repository.moon.concretefunction.ClassesAffectedByMethRenameCollector;
import repository.moon.concretefunction.MethodCollector;

/**
 * Permite incluir un nuevo argumento formal en la signatura de un método.<p>
 *
 * Se ocupa de incluir como parámetro real un valor por defecto en todas las 
 * llamadas al método que existan en las clases del modelo. Si el argumento
 * es de alguno de los tipos primitivos, se asignar� como valor real el valor 
 * habitual por defecto de cada tipo; si no, se le asignar� un valor nulo.
 *
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class AddFormalArg extends Action {

	/**
	 * El nuevo parámetro formal que se va a a�adir al método.
	 */
	FormalArgument newParameter;
	
	/**
	 * El método a cuya lista de parámetros se va a a�adir el nuevo argumento.
	 */
	MethDec method;
	
	/**
	 * La clase a la que pertenece el método que se va a modificar.
	 */
	private ClassDef classDef;
		
	/**
	 * El nombre único del método modificado antes del cambio de signatura.
	 */
	private String originalUniqueName;
	
	/**
	 * Elemento auxiliar para extender el cambio en el método a otras clases,
	 * en caso de que aparezca en clases superiores o inferiores en la jerarquía
	 * de herencia.
	 */
	private Vector<AddFormalArgWithoutHierarchy> addParInOtherClassVec;
	
	/**
	 * Elemento auxiliar para extender el cambio en el método a todas las
	 * instrucciones con llamadas al método.
	 */
	private ArrayList<AddFormalArgIntoInstructions> addIntoInstr;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
			 
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de AddFormalArg.
	 *
	 * @param method el método a cuya signatura se va a a�adir un argumento.
	 * @param name el nombre del nuevo parámetro formal.
	 * @param type el tipo del nuevo parámetro formal.
	 */	
	public AddFormalArg(MethDec method, Name name, Type type){
		
		super();
		
		this.method = method;
		this.classDef = method.getClassDef();
		this.originalUniqueName = method.getUniqueName().toString();
		this.addParInOtherClassVec = new Vector<AddFormalArgWithoutHierarchy>(10,1);
		this.addIntoInstr = new ArrayList<AddFormalArgIntoInstructions>();
		
		newParameter = MOONRefactoring.getModel().getMoonFactory().
			createFormalArgument(name, type);
		newParameter.setMethDec(method);
		
		listenerReg = RelayListenerRegistry.getInstance();
	}	
	
	/**
	 * A�ade un parámetro formal a la signatura de un método.
	 */
	public void run() {
		
		listenerReg.notify("# run():AddFormalArg #"); //$NON-NLS-1$
		
		addIntoHierarchy();
		
		method.add(newParameter);
		
		String name = newParameter.getUniqueName().toString(); 
		listenerReg.notify("\t- Adding formal argument " + name); //$NON-NLS-1$
			
		Collection<ClassDef> allClasses = 
			MOONRefactoring.getModel().getClassDefSourceAvailable();
		Collection<MethDec> allMethods = new Vector<MethDec>();
			
		for (ClassDef next : allClasses){
			MethodCollector methColl = new MethodCollector(next);
			Collection<MethDec> classMethods = methColl.getCollection();
			allMethods.addAll(classMethods);
		}
		

		for (MethDec next : allMethods){
			List<Instr> instrList = next.getInstructions();
			AddFormalArgIntoInstructions addParam = new AddFormalArgIntoInstructions(
				instrList, newParameter, method);
			addIntoInstr.add(addParam);
			addParam.run();
		}

	}

	/**
	 * Extiende la adición del argumento formal a las clases de la jerarquía. 
	 */
	void addIntoHierarchy() {
		Collection<ClassDef> alreadyFoundClasses = new Vector<ClassDef>(10,1);
		
		ClassesAffectedByMethRenameCollector getSuperAndSubclasses =
			new ClassesAffectedByMethRenameCollector(
				classDef, method.getUniqueName().toString(), 
				alreadyFoundClasses,true);
				
		Collection<ClassDef> superAndSubclasses = 
			getSuperAndSubclasses.getCollection();
		superAndSubclasses.remove(classDef);
		
		
		addFormalArgIntoSubAndSuperclasses(getSuperAndSubclasses.getCollection());
	}

	/**
	 * Elimina el nuevo parámetro formal de la signatura del método.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():AddFormalArg #"); //$NON-NLS-1$
		
		if(! addParInOtherClassVec.isEmpty())
			for(int i = 0; i < addParInOtherClassVec.size(); i++)
				addParInOtherClassVec.get(i).undo();
		
		if(! addIntoInstr.isEmpty())
			for(AddFormalArgIntoInstructions next : addIntoInstr)
				next.undo();
		
		listenerReg.notify("\t- Undoing formal argument addition"); //$NON-NLS-1$
		
		if(method.getFormalArgument().size() > 0)
			method.removeFormalArg(method.getFormalArgument().size()-1);
	}
	
	/**
	 * A�ade el parámetro en la signatura del método en las clases inferiores 
	 * y superiores de la jerarquía de herencia que, a través de herencia, 
	 * posean el mismo método (clases que hereden de la que posee el método
	 * afectado o superclases de la misma que contengan el mismo método, y a su
	 * vez, recursivamente, subclases o superclases de las mismas).
	 *
	 * @param affectedClasses las clases de la jerarquía de herencia que se ven 
	 * afectadas por el cambio de la signatura del método.
	 */
	private void addFormalArgIntoSubAndSuperclasses (
		Collection<ClassDef> affectedClasses){
		
		int i = 0;
		
		Iterator<ClassDef> classesIt = affectedClasses.iterator();
		
		while(classesIt.hasNext()){			
			ClassDef affectedClass = classesIt.next();
			
			int indexOfMethodName = originalUniqueName.lastIndexOf('~');
			String methNameWithoutPath = originalUniqueName.substring(indexOfMethodName);
			String methUniqueName = affectedClass.getUniqueName() + methNameWithoutPath;
			
			List<MethDec> methodsWithName = 
				affectedClass.getMethDecByName(method.getName());
			for (MethDec affectedClassMethod : methodsWithName)
				if (affectedClassMethod.getUniqueName().toString().equals(methUniqueName)){
					addParInOtherClassVec.add(new AddFormalArgWithoutHierarchy(
						affectedClassMethod, newParameter.getName(), 
						newParameter.getType()));
					addParInOtherClassVec.get(i).run();
					i++;
					break;
				}
		}
		
	}
}