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

package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javamoon.core.classdef.JavaDecoratorType;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.Type;
import moon.core.genericity.FormalPar;
import refactoring.engine.Function;
import repository.moon.MOONRefactoring;

/**
 * Permite obtener todas las sustituciones existentes de un parámetro formal.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class FormalParSubstitutionCollector extends Function {

	/**
	 * La clase a la que pertenece el parámetro formal.
	 */
	private ClassDef classDef;
	
	/**
	 * El parámetro formal cuyas sustituciones se quieren obtener.
	 */
	private FormalPar formalPar;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de FormalParSubstitutionCollector.
	 *
	 * @param formalPar el parámetro formal cuyas sustituciones se deben obtener.
	 */
	public FormalParSubstitutionCollector(FormalPar formalPar) {
		super();
		this.classDef = formalPar.getClassDef();
		this.formalPar = formalPar;
	}
	
	/**
	 * Sin implementación.
	 *
	 * @return null.
	 */
	@Override
	public Object getValue() {
		return null;
	}
	
	/**
	 * Devuelve todos las sustituciones del parámetro formal.
	 * 
	 * @return todas las sustituciones del parámetro formal.
	 */
	@Override
	public Collection<ClassType> getCollection() {
		ArrayList<ClassType> substitutions = new ArrayList<ClassType>();
		
		// Se obtiene la posición del parámetro formal en la lista de 
		// parámetros formales de la clase.
		int index = 0;
		List<FormalPar> classParameters = this.classDef.getFormalPars();
		for(FormalPar nextPar : classParameters){
			if (nextPar.getName().equals(formalPar.getName()))
				break;
			index++;
		}
				
		Iterator<Type> modelTypes = MOONRefactoring.getModel().getTypes().iterator();		
		
		// Se recorren todos los tipos del modelo.
		while(modelTypes.hasNext()){
			Type nextType = modelTypes.next();
			// Para cada uno que se encuentre definido a partir de la clase tratada.
			if (nextType instanceof ClassType  && 
				nextType.getClassDef() != null &&
				nextType.getClassDef().getUniqueName().equals(
					classDef.getUniqueName())){
				ClassType cType = (ClassType)nextType;
				
				if (cType.getRealParameters().size() > 0){
					Type realType = cType.getRealParameters().get(index);
				
					if (realType.getClassDef() != null)
						if (! substitutions.contains(
							realType.getClassDef().getClassType()))
						substitutions.add(realType.getClassDef().getClassType());

					if (realType instanceof JavaDecoratorType){
						Type javaType = 
							((JavaDecoratorType)realType).getType();
						if (javaType.getClassDef() != null)
							if (! substitutions.contains(
								javaType.getClassDef().getClassType()))
							substitutions.add(javaType.getClassDef().getClassType());
					}
				}
			}
		}
		
		return substitutions;
	}
}