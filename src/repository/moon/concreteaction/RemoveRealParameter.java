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
import moon.core.genericity.*;

import refactoring.engine.*;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;

/**
 * Permite borrar un parámetro real de una clase.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RemoveRealParameter extends Action{

	/**
	 * Clase sobre la que eliminar el parámetro formal.
	 */
	private ClassDef classDef;
	
	/**
	 * Parámetro formal sobre el que se hacen las sustituciones del parámetro 
	 * real.
	 */
	private FormalPar formalPar;
	
	/**
	 * Conjunto de tipos del modelo.
	 */
	private Collection<Type> types;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor de la acción que permite borrar un parámetro real.
	 *
	 * @param formalPar parámetro formal sobre el que se hacen las sustituciones 
	 * del parámetro real.
	 */
	public RemoveRealParameter(FormalPar formalPar) {
		super();
		this.classDef = formalPar.getClassDef();
		this.formalPar = formalPar;
		this.types = MOONRefactoring.getModel().getTypes();;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta la eliminación del parámetro real.
	 */
	public void run() {
		listenerReg.notify("# run():RemoveRealParameter #"); //$NON-NLS-1$
		
		// Se obtiene la posición del parámetro formal en la lista 
		// de parámetros formales de la clase.
		int index = 0;
		List<FormalPar> classParameters = this.classDef.getFormalPars();
		for(FormalPar nextPar : classParameters){
			if (nextPar.getName().equals(formalPar.getName()))
				break;
			index++;
		}
		
		Iterator<Type> itTypes = types.iterator();
		while(itTypes.hasNext()){
			Type ct = itTypes.next();
			if (ct instanceof ClassType){
				ClassDef cd = ((ClassType) ct).getClassDef();
				if (cd != null && cd.equals(this.classDef)){
					ClassType ctype = (ClassType) ct;
					if(ctype.getRealParameters().size() > index){
						listenerReg.notify(
							"\tRemoving real parameter:" +  //$NON-NLS-1$
							((Type)ctype.getRealParameters().get(
								index)).getName().toString() +
							" from " + ctype.getName().toString()); //$NON-NLS-1$
						ctype.removeRealPar(index);			
					}
					String parametros = ""; //$NON-NLS-1$
					if(ctype.getRealParameters().size() > 0){
						parametros += "<"; //$NON-NLS-1$
						Iterator<Type> it = ctype.getRealParameters().iterator();
						while(it.hasNext()){
							parametros += ((ClassType)it.next()).getName();
							if(it.hasNext()){
								parametros += ","; //$NON-NLS-1$
							}
						}
						parametros += ">"; //$NON-NLS-1$
					}
					
					ctype.setName(cd.getName().concat(parametros));		
				}
			}
		}
	}

	/**
	 * Deshace la eliminación del parámetro real, incluyéndolo de nuevo en 
	 * la clase.
	 * 
	 * ¡Sin implementación!
	 */
	@Override
	public void undo() {}
}