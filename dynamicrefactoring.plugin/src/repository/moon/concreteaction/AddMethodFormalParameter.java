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
import java.util.Collections;
import java.util.List;

import javamoon.core.classdef.JavaDecoratorType;
import javamoon.core.classdef.JavaGenericArrayType;
import javamoon.core.classdef.JavaWildcardType;
import javamoon.core.genericity.JavaBoundS;
import javamoon.core.genericity.JavaSuperBoundS;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.entity.Entity;
import moon.core.genericity.FormalPar;
import moon.core.instruction.CodeFragment;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Adds new formal parameter to the method if it would be necessary.
 * It includes the bounds.
 * 
 * @author <A HREF="mailto:rmartico@ubu.es">Raúl Marticorena</A>
 */ 
public class AddMethodFormalParameter extends Action {
	
	/**
	 * Code fragment.
	 */
	private CodeFragment fragment;

	/**
	 * Name.
	 */
	private Name name;
	
	/**
	 * Clase a la que se mover� el método.
	 */
	private ClassDef classDef;
	
	/**
	 * Método que se va a mover de una clase a otra.
	 */
	 private MethDec method;
	 
	 /**
	  * Receptor de los mensajes enviados por la acción concreta.
	  */
	 private RelayListenerRegistry listenerReg;
	 	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de MoveMethod.
	 * @param method método que se va a mover de una clase a otra.
	 * @param classDefDest clase a la que se mover� el método.
	 */	
	public AddMethodFormalParameter(Name name, CodeFragment fragment){
		super();
		this.name = name;
		this.classDef = fragment.getClassDef();
		this.method = fragment.getMethDec();
		this.fragment = fragment;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el movimiento del método de una clase a otra.
	 */
	@Override
	public void run() {		
		listenerReg.notify("# run():ExtractMethod #"); //$NON-NLS-1$

		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " from " + classDef.getName().toString()); //$NON-NLS-1$
		
		
		List<MethDec> listMethDec = this.classDef.getMethDecByName(name);
		
		MethDec methDec = listMethDec.get(0);
		List<Entity> aux = new ArrayList<Entity>(methDec.getLocalDecs().size());
		Collections.copy(aux, methDec.getLocalDecs());
		Collections.addAll(aux, methDec.getFormalArgument().toArray(new FormalArgument[0]));
		
		List<FormalPar> formalParList = new ArrayList<FormalPar>();
		for(Entity entity : aux){
			Type auxType = entity.getType();
			if (auxType instanceof JavaDecoratorType){
				auxType = ((JavaDecoratorType) auxType).getType();
			}
			
			
			if (auxType instanceof FormalPar){
				formalParList.add((FormalPar) auxType);
			}
			else if (auxType instanceof JavaGenericArrayType){
				formalParList.add((FormalPar) ((JavaGenericArrayType) auxType).getType());
			}		
			else if (auxType instanceof ClassType){
				ClassType ct = (ClassType) auxType;
				List<FormalPar> listAux = new ArrayList<FormalPar>();
				extractFormalPar(ct,listAux);
				
				for (FormalPar fp : listAux){
					formalParList.add(fp);
				}
				
			}
			
		}
		
		for(FormalPar fp : formalParList){
			
			// if it is not a formal parameter in class
			// and it is not a formal parameter in current method
			// add the new formal par
			if (methDec.getClassDef().getFormalPar(fp.getName())==null &&
					methDec.getFormalPar(fp.getName())==null){
				
				JavaBoundS jbs = new JavaBoundS(fp.getName(),methDec,-1,-1);
				// add bounds
				if (fp instanceof JavaBoundS){
					for (Type bound : ((JavaBoundS) fp).getBounds()){
						jbs.add(bound);						
					}
				}
				else if (fp instanceof JavaSuperBoundS){
					for (Type bound : ((JavaSuperBoundS) fp).getBounds()){
						jbs.add(bound);
					}
				}
				methDec.add(jbs);
				
				
				
			}
		}
		
		
		
		listenerReg.notify("\t- Extracting method " + method.getUniqueName().toString() //$NON-NLS-1$
			+ " to " + classDef.getName().toString());				 //$NON-NLS-1$
		
		
	}

	/**
	 * Deshace el movimiento del método, devolvi�ndolo a su clase de origen y 
	 * elimin�ndolo de la nueva clase destino.
	 */
	@Override
	public void undo() {		
		listenerReg.notify("# undo():MoveMethod #"); //$NON-NLS-1$
		
		AddMethodFormalParameter undo = new AddMethodFormalParameter(name, fragment);

		undo.run();
	}
	
	private void extractFormalPar(ClassType ct, List<FormalPar> list){
		
		if (ct instanceof JavaWildcardType){
			JavaWildcardType jw = (JavaWildcardType) ct;
			List<Type> lowerBounds = jw.getLowerBounds();
			for (Type typeJW : lowerBounds){
				if (typeJW instanceof FormalPar){
					list.add((FormalPar)typeJW);
					return;
				}
				else{
					extractFormalPar((ClassType) typeJW, list);
				}
			}
			List<Type> upperBounds = jw.getUpperBounds();
			for (Type typeJW : upperBounds){
				if (typeJW instanceof FormalPar){
					list.add((FormalPar)typeJW);
					return;
				}
				else{
					extractFormalPar((ClassType) typeJW, list);
				}
			}
		}
		
		for (Type type : ct.getRealParameters()){
		
			if (type instanceof FormalPar){
				
				list.add((FormalPar)type);
				FormalPar fp = (FormalPar) type;
				if (fp instanceof JavaBoundS){
					JavaBoundS jbs = (JavaBoundS) fp;
					for (Type type2 : jbs.getBounds()){
						if (type2 instanceof FormalPar){
							list.add((FormalPar)type2);
						}
						else{
							extractFormalPar((ClassType)type2,list);
						}
					}
				}
				else if (fp instanceof JavaSuperBoundS){
					JavaSuperBoundS jbs = (JavaSuperBoundS) fp;
					for (Type type2 : jbs.getBounds()){
						if (type2 instanceof FormalPar){
							list.add((FormalPar)type2);
						}
						else{
							extractFormalPar((ClassType)type2,list);
						}
					}
				}
			}
			else{
				
				extractFormalPar((ClassType) type,list);
			}
		}
	}
}