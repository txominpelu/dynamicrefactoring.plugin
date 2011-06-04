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

import java.util.List;

import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.ClassType;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.LocalDec;
import moon.core.classdef.MethDec;
import moon.core.classdef.Type;
import moon.core.entity.Entity;
import moon.core.entity.FunctionDec;
import moon.core.genericity.FormalPar;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite sustituir un parámetro formal de una clase por un tipo
 * totalmente instanciado.
 *
 * @author <A HREF="mailto:sam0006@alu.ubu.es">Sara Alcalá Martín</A>
 * @author <A HREF="mailto:dbm0005@alu.ubu.es">Diego Bañuelos Molledo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SubstituteFormalParameter extends Action {

	/**
	 * Clase sobre la que sustituir el parámetro formal.
	 */
	private ClassDef classDef;
	
	/**
	 * Parámetro formal que se debe sustituir.
	 */
	private FormalPar formalPar;
	
	/**
	 * Tipo completamente instanciado por el que sustituir la clase.
	 */ 
	private ClassType classType;
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
		
	/**
	 * Constructor de la acción sustituir parámetro formal.
	 * 
	 * @param formalPar parámetro formal que se debe sustituir.
	 * @param classType tipo completamente instanciado por el que sustituir el 
	 * parámetro formal.
	 */
	public SubstituteFormalParameter(FormalPar formalPar, ClassType classType) {

		super();
		this.classDef = formalPar.getClassDef();
		this.formalPar = formalPar;
		this.classType = classType;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta la sustitución del parámetro formal.
	 */
	@Override
	public void run() {
		listenerReg.notify("# run():SubstituteFormalParameter #"); //$NON-NLS-1$

		List<MethDec> methods = classDef.getMethDec();
		for(MethDec method : methods)
			substitute(method, classType);
		
		List<AttDec> attributes = classDef.getAttributes();
		for(AttDec attribute : attributes)
			substitute(attribute, classType);
		
		listenerReg.notify("\t- Replacing formal parameter " +  //$NON-NLS-1$
			formalPar.getName().toString() + 
			" with completely instantiated type " +  //$NON-NLS-1$
			classType.getName().toString());
	}
		
	/**
	 * Sustituye el parámetro formal dentro de los métodos.
	 * 
	 * @param md método sobre el que hacer la sustitución.
	 * @param ct nuevo tipo para el método.
	 */
	private void substitute(MethDec md, ClassType ct){
		
		List<FormalArgument> argForm = md.getFormalArgument();
		for(FormalArgument fa : argForm)		
			substitute(fa, ct);
		 
		if (md instanceof FunctionDec){		
			FunctionDec f = (FunctionDec) md;
			// Si el tipo de retorno es el parámetro formal.
			if (f.getReturnType().equals(this.formalPar)){
				f.setFunctionResultEntity(ct);
				// FIXME: Es necesario? f.getFunctionResultEntity().setType(ct);	
			}			
		}
		
		// Variables locales.
		List<LocalDec> variables = md.getLocalDecs();
		for(LocalDec ld : variables)
			substitute(ld, ct);	
	} 
	
	/**
	 * Sustituye el parámetro formal en las entidades.
	 *
	 * @param e entidad sobre la que hacer el cambio.
	 * @param ct nuevo tipo para la entidad.
	 */
	private void substitute(Entity e, ClassType ct){			
		Type ctOfLocalDec = (Type) e.getType();		
		if (ctOfLocalDec.equals(this.formalPar))
			e.setType(ct);
	}
	
	/**
	 * Deshace la sustitución del parámetro formal.
	 * 
	 * ¡Sin implementación!
	 */
	@Override
	public void undo() {}
}