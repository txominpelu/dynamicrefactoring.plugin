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
 * Permite a�adir una sentencia de importaci�n a una clase.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class AddImportClause extends Action {
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Tipo de la sentencia de importaci�n que se debe a�adir.
	 */
	private Type type;
	
	/**
	 * Clase a la que se debe a�adir la sentencia de importaci�n.
	 */
	private ClassDef classdef;
	
	/**
	 * Cl�usula de importaci�n a�adida a la clase.
	 */
	private JavaImport jimport;
	
	/**
	 * Constructor.<p>
	 * 
	 * Obtiene una nueva instancia de la acci�n <code>AddImportClause</code>.
	 *
	 * @param classdef clase a la que se debe a�adir la sentencia de importaci�n.
	 * @param type tipo que debe importar la sentencia de importaci�n.
	 */
	public AddImportClause(ClassDef classdef, Type type){
		super();
		
		this.type = type;
		this.classdef = classdef;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * A�ade la sentencia de importaci�n.
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
	 * @param type tipo sobre el que se realiza la comprobaci�n.
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