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


import java.io.File;

import javamoon.core.JavaFile;
import javamoon.core.JavaModel;
import javamoon.core.JavaName;
import javamoon.core.classdef.JavaClassDef;
import moon.core.Name;
import moon.core.classdef.ClassDef;
import refactoring.engine.Action;
import repository.RelayListenerRegistry;

/**
 * Permite modificar el atributo de una clase donde se almacena el nombre del
 * fichero de origen en que se define dicha clase.
 * 
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Angel Lopez Campo</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 */ 
public class RenameReferenceFile extends Action {

	/**
	 * Clase cuyo fichero de referencia se debe actualizar.
	 */
	private ClassDef classDef;
		
	/**
	 * Nuevo nombre que se dar� al fichero que contiene la clase.
	 */
	private String newName;
	
	/**
	 * Nombre del fichero antes del renombrado.
	 */
	private String originalName;
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Constructor.<p>
	 *
	 * Obtiene una nueva instancia de RenameReferenceFile.
	 *
	 * @param classDef la clase cuyo fichero de referencia se desea actualizar.
	 * @param newName el nuevo nombre que se dar� al fichero en que se define la 
	 * clase.
	 */	
	public RenameReferenceFile(ClassDef classDef, Name newName){		
		super();
		
		this.classDef = classDef;
		this.originalName = classDef.getName().toString();
		this.newName = newName.toString();

		listenerReg = RelayListenerRegistry.getInstance();
	}
		
	/** 
	 * Ejecuta el renombrado del fichero que contiene la clase y el atributo
	 * que almacena el fichero de referencia.
	 */
	public void run() {		
		listenerReg.notify("# run():RenameReferenceFile #"); //$NON-NLS-1$
		
		int nameFileFirstIndex;
		String oldPath;	
		String newPath;

		
		oldPath = ((JavaClassDef)classDef).getSourceFile().getPath().toString()+"" + File.separatorChar + ""+ originalName + ".java";
		nameFileFirstIndex = oldPath.lastIndexOf(originalName + ".java"); //$NON-NLS-1$
		newPath = oldPath.substring(0, nameFileFirstIndex)+ newName + ".java"; //$NON-NLS-1$
		
		listenerReg.notify("\t- Updating reference source file: from " +  //$NON-NLS-1$
			originalName + " to " + newName); //$NON-NLS-1$
		
		//((JavaClassDef)classDef).setJavaFile(new JavaFile(new JavaName(newPath),classDef.getName(),0));
		
		// RMS Modified Solution 1
		/*
		JavaPackageReference jpr = ((JavaClassDef)classDef).getJavaFile().getPackageReference();
		JavaFile jf = new JavaFile(new JavaName(newPath),classDef.getName(),0);
		jf.setPackageReference(jpr);
		((JavaClassDef)classDef).setJavaFile(jf);
		*/
		
		// RMS Modified Solution 2
		
		
		JavaModel.getInstance().remove(((JavaClassDef)classDef).getJavaFile());
		((JavaClassDef)classDef).getJavaFile().setName(new JavaName(newName + ".java"));
		JavaModel.getInstance().add(((JavaClassDef)classDef).getJavaFile());	
	}
		
	/**
	 * Deshace el renombrado del fichero que contiene la clase y el atributo
	 * que almacena el fichero de referencia.
	 */
	public void undo() {
		
		listenerReg.notify("# undo():RenameReferenceFile #"); //$NON-NLS-1$
		
		int nameFileFirstIndex;		
		String oldPath;
		String newPath;
		
		newPath = ((JavaClassDef) classDef).getSourceFile().getCanonicalName()
				.toString();
		nameFileFirstIndex = newPath.lastIndexOf(newName + ".java"); //$NON-NLS-1$
		oldPath=newPath.substring(0, nameFileFirstIndex)+ originalName + ".java"; //$NON-NLS-1$
				
		listenerReg.notify(
			"\t- Undoing reference source file update: from " //$NON-NLS-1$
			+ newName + " to " + originalName); //$NON-NLS-1$
				
	    ((JavaClassDef)classDef).setJavaFile(new JavaFile(new JavaName(oldPath),classDef.getName(),0));
	}
}