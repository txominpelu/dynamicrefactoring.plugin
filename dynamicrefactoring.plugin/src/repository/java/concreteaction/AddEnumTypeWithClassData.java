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

package repository.java.concreteaction;


import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import moon.core.Name;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import javamoon.core.ModifierSet;
import javamoon.core.classdef.JavaEnum;
import javamoon.core.entity.JavaAttDec;
import javamoon.core.classdef.JavaClassDef;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

import javamoon.core.entity.JavaEnumConstant;
import moon.core.expression.Expr;
import javamoon.core.expression.JavaCallExprCreation;

/**
 * Permite añadir un tipo enumerado al modelo a partir de la información de 
 * una clase que pretende sustituir a un tipo enumerado.<p>
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class AddEnumTypeWithClassData extends Action {
	
	
	/**
	 * Clase que contiene la información necesaria para crear el tipo enumerado.
	 */
	private ClassDef classDef;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de la acción AddEnumTypeWithClassData.
	 *
	 * @param classDef clase que contiene la información necesaria para crear el tipo enumerado.
	 */
	public AddEnumTypeWithClassData ( ClassDef classDef){
		super();
		
		this.classDef = classDef;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta la acción de convertir una clase en tipo enumerado.
	 */
	public void run(){	
		
		listenerReg.notify("# run():AddEnumTypeWithClassData #"); //$NON-NLS-1$

		List<AttDec> class_attributes = classDef.getAttributes();		
		
		Name className = this.classDef.getName();
		JavaEnum enumtype = new JavaEnum(className, classDef.getNameSpace(), ((JavaClassDef) classDef).getSourceFile());
		
		enumtype.copy(classDef);
		enumtype.setEndLine(((JavaClassDef) classDef).getEndLine());
		
		List<AttDec> attributes = new ArrayList<AttDec>();
		for(AttDec at: class_attributes )
			attributes.add(at);
		
		int posicion=0;//posicion que ocupa cada tipo enumerado dentro del conjunto de tipos enumerados.
		
		for(int i=0; i<attributes.size(); i++){
			
			JavaAttDec attribute = (JavaAttDec)attributes.get(i);
			if(ModifierSet.isFinal(attribute.getModifiers()) && 
					ModifierSet.isStatic(attribute.getModifiers()) &&
					ModifierSet.isPublic(attribute.getModifiers()) &&
					attribute.getType().toString().equals(classDef.getName().toString())){
			      enumtype.remove(attribute);
			      JavaEnumConstant constant = new JavaEnumConstant(attribute.getModifiers(), attribute.getName(), 
							attribute.getType(), false,posicion , (int)attribute.getLine(), 
							(int)attribute.getColumn());
			      
			      posicion++;
			      try{
			    	  if(attribute.hasInitializer()&& attribute.getInitializer().getClass().equals(Class.forName("javamoon.core.expression.JavaCallExprCreation"))){
			    		  List<Expr> inicializacion =  new ArrayList<Expr>();
			    		  for(Expr e : ((JavaCallExprCreation)attribute.getInitializer()).getExpression().getRealArguments()){
			    			  inicializacion.add(e);
			    		  }
			    		  constant.setSignature(inicializacion);
			    	  }
			    	 
			    	 
			    	  enumtype.add(constant);
			    	  enumtype.reformat(constant.getLine());
			    	  
			    	  
			      }catch(Exception e){e.printStackTrace();}
			}
		
		}
		
		
		
		((JavaClassDef) this.classDef).getJavaFile().remove((JavaClassDef)classDef);		
		((JavaClassDef) this.classDef).getJavaFile().add(enumtype);
		//enumtype.setEndLine(2000);
		

		MOONRefactoring.getModel().remove(classDef);
		MOONRefactoring.getModel().add(enumtype);
		
	}
	
	/**
	 * Elimina el atributo añadido de la clase destino.
	 */
	public void undo(){		
		listenerReg.notify("# undo():AddEnumTypeWithClassData #"); //$NON-NLS-1$
		try{
			Class<?> enumtype = Class.forName("javamoon.core.classdef.JavaEnum");

			Collection<ClassDef> col = MOONRefactoring.getModel().getClassDef();
			Collection<ClassDef> classes = new ArrayList<ClassDef>();
			col.addAll(classes);
			for(ClassDef c: classes){
				if(c.getName().toString().equals(classDef.getName().toString()) && 
						c.getClass().equals(enumtype)){
					((JavaClassDef) this.classDef).getJavaFile().remove((JavaEnum)c);		
					((JavaClassDef) this.classDef).getJavaFile().add((JavaClassDef)classDef);

					MOONRefactoring.getModel().remove(c);
					MOONRefactoring.getModel().add(classDef);

				}
			}
		}catch(ClassNotFoundException exception){
			System.out.println("The class javamoon.core.classdef.JavaEnum is not found" );
		}
		
	}
}