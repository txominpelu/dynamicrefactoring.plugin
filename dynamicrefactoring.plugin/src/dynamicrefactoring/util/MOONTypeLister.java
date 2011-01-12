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

package dynamicrefactoring.util;

import dynamicrefactoring.RefactoringConstants;

import java.io.File;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Obtiene las clases de MOON disponibles.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class MOONTypeLister {

	/**
	 * La instancia única de la clase.
	 * 
	 * Patrón de diseño Singleton.
	 */
	private static MOONTypeLister instance;
	
	/**
	 * Constructor.
	 * 
	 * Patrón de diseño Singleton.
	 */
	private MOONTypeLister() {}
		
	/**
	 * Devuelve la instancia única de la clase.
	 * 
	 * @return la instancia única de la clase.
	 */
	public static MOONTypeLister getInstance() {		
		if(instance == null)
			instance = new MOONTypeLister();
		return instance;
	}	
	
	/**
	 * Obtiene la lista de clases MOON y de su extensión para Java.
	 * 
	 * @return un array con los nombres de las clases.
	 * 
	 * @throws IOException cuando no existe el fichero del núcleo de MOON o de la
	 * extensión JavaMOON.
	 */
    public String[] getTypeNameList() throws IOException {

    	Vector<String> files = new Vector<String>();
    	
    	// Biblioteca con el núcleo de MOON.
        addLibraryTypes(RefactoringConstants.MOONCORE_DIR, files);
        // Biblioteca con la extensión para Java de MOON.
        addLibraryTypes(RefactoringConstants.JAVAEXTENSION_DIR, files);
        
        String[] retorno = new String[files.size()];
        for(int i=0; i<files.size(); i++)
        	retorno[i] = files.elementAt(i);

        return retorno;
    }
    
    /**
     * Añade a una colección las clases encontradas en una biblioteca <i>JAR</i>
     * que pertenezcan a uno de los paquetes del núcleo de MOON o de JavaMOON
     * <code>moon.core.classdef</code>, <code>moon.core.genericity</code> o
     * <code>javamoon.core</code>.
     * 
     * @param library biblioteca <i>JAR</i> en que buscar nuevas clases.
     * @param collection colección a la que se añaden las clases encontradas.
     * 
     * @throws IOException si no se encuentra o no existe el fichero <code>.jar
     * </code> de la biblioteca.
     */
    private void addLibraryTypes(String library, Vector<String> collection) 
    	throws IOException {
    	
    	File dir = new File(library);
        if (!dir.exists())
        	throw new IOException(
        		Messages.MOONTypeLister_FileNotExists +
        		":\n" + library + ".\n"); //$NON-NLS-1$ //$NON-NLS-2$
        
        JarFile jarFile = new JarFile(library);
        Enumeration<JarEntry> fileEnum = jarFile.entries();
        
        while (fileEnum.hasMoreElements()) {
        	JarEntry entry = fileEnum.nextElement();
        
        	if(((entry.getName().startsWith("moon/core") &&  //$NON-NLS-1$
        			entry.getName().lastIndexOf("/") == 9) || //$NON-NLS-1$
        		entry.getName().startsWith("moon/core/classdef") || //$NON-NLS-1$
				entry.getName().startsWith("moon/core/genericity") ||  //$NON-NLS-1$
				entry.getName().startsWith("javamoon/core")) && //$NON-NLS-1$
				entry.getName().indexOf("Test") == -1 &&  //$NON-NLS-1$
				entry.getName().endsWith(".class")) //$NON-NLS-1$
        		
        		if (!collection.contains(entry.getName().substring(
            		0, entry.getName().length()-6)))
            		
        			collection.add(
            			entry.getName().substring(0, entry.getName().length()-6));
        }
    }
}