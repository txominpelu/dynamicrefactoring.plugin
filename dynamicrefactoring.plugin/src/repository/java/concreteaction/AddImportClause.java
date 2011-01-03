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

import javamoon.core.classdef.JavaArrayType;
import javamoon.core.classdef.JavaClassDef;
import javamoon.core.classdef.JavaImport;
import javamoon.core.classdef.JavaType;
import javamoon.core.classdef.primitivetypes.JavaPrimitiveType;

import moon.core.classdef.ClassDef;
import moon.core.classdef.Type;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite añadir una sentencia de importación a una clase.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class AddImportClause extends Action {
	
	/**
	 * Receptor de los mensajes enviados por la acción concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Tipo de la sentencia de importación que se debe añadir.
	 */
	private Type type;
	
	/**
	 * Clase a la que se debe añadir la sentencia de importación.
	 */
	private ClassDef classdef;
	
	/**
	 * Cláusula de importación añadida a la clase.
	 */
	private JavaImport jimport;
	
	/**
	 * Constructor.<p>
	 * 
	 * Obtiene una nueva instancia de la acción <code>AddImportClause</code>.
	 *
	 * @param classdef clase a la que se debe añadir la sentencia de importación.
	 * @param type tipo que debe importar la sentencia de importación.
	 */
	public AddImportClause(ClassDef classdef, Type type){
		super();
		
		this.type = type;
		this.classdef = classdef;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Añade la sentencia de importación.
	 * 
	 * @see Action#run()
	 */
	@Override
	public void run(){	
		listenerReg.notify("# run():AddImportClause #"); //$NON-NLS-1$
		
		if (classdef instanceof JavaClassDef){
			if (type instanceof JavaType && 
				! isPrimitive((JavaType)type) &&
				! type.getUniqueName().toString().startsWith("<anonymous>")){ //$NON-NLS-1$
				JavaClassDef jclass = (JavaClassDef)classdef;
				JavaType jtype = (JavaType)type;
				
				jimport = new JavaImport(jtype,true, -1, -1, false);
				jclass.format(jimport);
				jclass.add(jimport);
			}
		}
	}
	
	/**
	 * Comprueba si un tipo es tipo primitivo.
	 * 
	 * @param type tipo sobre el que se realiza la comprobación.
	 * 
	 * @return <code>true</code> si es un tipo primitivo; <code>false</code>
	 * en caso contrario.
	 */
	private boolean isPrimitive(JavaType type){
		if (type instanceof JavaPrimitiveType)
			return true;
		if (type instanceof JavaArrayType){
			JavaArrayType array = (JavaArrayType)type;
			return isPrimitive(array.getType()); 
		}
		return false;
	}
			
	/**
	 * Deshace el renombrado del fichero Java.
	 * 
	 * @see Action#undo()
	 */
	@Override
	public void undo(){
		listenerReg.notify("# undo():AddImportClause #"); //$NON-NLS-1$
				
		if (classdef instanceof JavaClassDef)
			((JavaClassDef)classdef).remove(jimport);
	}
}