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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;

import dynamicrefactoring.RefactoringConstants;

/**
 * Obtiene las clases de MOON disponibles.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Àngel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class MOONTypeLister {

	private static final String CLASS_EXTENSION = ".class";
	/**
	 * La instancia unica de la clase.
	 * 
	 * Patron de diseño Singleton.
	 */
	private static MOONTypeLister instance;
	
	/**
	 * Constructor.
	 * 
	 * Patron de diseño Singleton.
	 */
	private MOONTypeLister() {}
		
	/**
	 * Devuelve la instancia unica de la clase.
	 * 
	 * @return la instancia unica de la clase.
	 */
	public static MOONTypeLister getInstance() {		
		if(instance == null)
			instance = new MOONTypeLister();
		return instance;
	}	
	
	/**
	 * Obtiene la lista de clases MOON y de su extension para Java
	 * con sus nombres completamente cualificados.
	 * 
	 * @return una lista con los nombres de las clases.
	 */
    public List<String> getTypeNameList() {

    	
    	List<String> files = new ArrayList<String>();
    	
    	// Biblioteca con el nucleo de MOON.
        try {
			addLibraryTypes(RefactoringConstants.MOONCORE_DIR, files);
		
        // Biblioteca con la extension para Java de MOON.
        addLibraryTypes(RefactoringConstants.JAVAEXTENSION_DIR, files);

        } catch (IOException e) {
			throw Throwables.propagate(e);
		}
        return files;
    }
    
    
    /**
	 * Obtiene la lista de clases MOON y de su extension para Java
	 * con sus nombres completamente cualificados.
	 * 
	 * @return un lista con los nombres de las clases.
	 */
	public  java.util.List<String> getInputTypeNames() {
		java.util.List<String> proposals = new ArrayList<String>(
				Collections2.transform(MOONTypeLister.getInstance()
						.getTypeNameList(), new Function<String, String>() {

					@Override
					public String apply(String fullyQualifiedName) {
						return PluginStringUtils
								.getClassName(fullyQualifiedName);
					}
				}));
		return proposals;
	}
    
    /**
     * A�ade a una colecci�n las clases encontradas en una biblioteca <i>JAR</i>
     * que pertenezcan a uno de los paquetes del n�cleo de MOON o de JavaMOON
     * <code>moon.core.classdef</code>, <code>moon.core.genericity</code> o
     * <code>javamoon.core</code>.
     * 
     * @param library biblioteca <i>JAR</i> en que buscar nuevas clases.
     * @param collection colecci�n a la que se a�aden las clases encontradas.
     * 
     * @throws IOException si no se encuentra o no existe el fichero <code>.jar
     * </code> de la biblioteca.
     */
    private void addLibraryTypes(String library, List<String> collection) 
    	throws IOException {
    	
    	File dir = new File(library);
        if (!dir.exists()) {
        	throw new IOException(
        		Messages.MOONTypeLister_FileNotExists +
        		":\n" + library + ".\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
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
				entry.getName().endsWith(CLASS_EXTENSION)) { //$NON-NLS-1$
        		
        		final String className = getClassNameFromEntry(entry);
        		if (!collection.contains(className)) {	
        			collection.add(className);
        		}
        	}
        }
    }

    /**
     * Obtiene el nombre de una clase dada su entry.
     * Pasa de:
     * 
     * javamoon/core/instruction/JavaThisConstructorInstr.class
     * 
     * a:
     * 
     * javamoon.core.instruction.JavaThisConstructorInstr
     * 
     * @param entry entrada del fichero de clase
     * @return nombre completamente cualificado de la clase
     */
	private String getClassNameFromEntry(JarEntry entry) {
		return entry.getName().substring(
				0, entry.getName().length() - CLASS_EXTENSION.length()).replace("/", ".");
	}
}