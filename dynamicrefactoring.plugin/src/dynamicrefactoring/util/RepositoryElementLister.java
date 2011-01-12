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
import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.util.io.filter.ClassFilter;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;

import java.util.HashMap;

/**
 * Obtiene la lista de predicados y acciones disponibles.
 *
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RepositoryElementLister {

	/**
	 * La instancia única de la clase.
	 * 
	 * Patrón de diseño Singleton.
	 */
	private static RepositoryElementLister instance;
	
	/**
	 * El nombre del directorio origen de las precondiciones.
	 */
	private String sourcePreconditionDir;
	
	/**
	 * El nombre del directorio origen de las postcondiciones.
	 */
	private String sourcePostconditionDir;
	
	/**
	 * El nombre del directorio origen de las acciones.
	 */
	private String sourceActionDir;
	
	/**
	 * El nombre del directorio origen de las acciones dependientes de Java.
	 */
	private String sourceJavaActionDir;
	
	/**
	 * El nombre del directorio origen de los predicados dependientes de Java.
	 */
	private String sourceJavaPredicateDir;
	
	/**
	 * El filtro para los nombres de fichero.
	 */
	private FilenameFilter fileFilter;
	
	/**
	 * Constructor.
	 * 
	 * Patrón de diseño Singleton.
	 */
	private RepositoryElementLister() {		
		sourcePreconditionDir = 
			RefactoringConstants.PRECONDITION_DIR;
		sourcePostconditionDir = 
			RefactoringConstants.POSTCONDITION_DIR;
		sourceActionDir = 
			RefactoringConstants.ACTION_DIR;
		sourceJavaActionDir =
			RefactoringConstants.JAVA_ACTION_DIR;
		sourceJavaPredicateDir =
			RefactoringConstants.JAVA_PREDICATE_DIR;
		
		fileFilter = new ClassFilter();
	}
	
	/**
	 * Devuelve la instancia única de la clase.
	 * 
	 * Patrón de diseño Singleton.
	 * 
	 * @return la instancia única de la clase.
	 */
	public static RepositoryElementLister getInstance() {		
		if(instance == null)
			instance = new RepositoryElementLister();
		return instance;
	}
		
	/**
	 * Obtiene un conjunto de pares nombre-fichero de los ficheros con 
	 * precondiciones disponibles en un directorio.
	 * 
	 * @return una tabla de pares en la que se usa como índice el nombre 
	 * comprensible del fichero y como contenido la ruta del fichero; 
	 * para cada fichero encontrado.
	 * 
	 * @throws IOException cuando no existe el directorio, o bien no es 
	 * un directorio.
	 */
    public HashMap<String, String> getPreconditionList() throws IOException {

        File dir = new File(sourcePreconditionDir);
        if (!dir.exists())
        	throw new IOException(
        		Messages.RepositoryElementLister_PreconditionsDirNotExists + ".\n"); //$NON-NLS-1$
        else if (!dir.isDirectory())
        	throw new IOException(
        		Messages.RepositoryElementLister_InvalidPreconditionsPath 
        		+ ".\n"); //$NON-NLS-1$
        
        HashMap<String, String> h = new HashMap<String, String>();
        listFiles(dir, h);
        return h;
    }
    
	/**
	 * Obtiene un conjunto de pares nombre-fichero de los ficheros con 
	 * postcondiciones disponibles en un directorio.
	 * 
	 * @return una tabla en la que se usa como índice el nombre comprensible 
	 * del fichero y como contenido la ruta del fichero; 
	 * para cada fichero encontrado.
	 * 
	 * @throws IOException cuando no existe el directorio, o bien no es 
	 * un directorio.
	 */
    public HashMap<String, String> getPostconditionList() throws IOException {

        File dir = new File(sourcePostconditionDir);
        if (!dir.exists())
        	throw new IOException(
        	Messages.RepositoryElementLister_PostconditionsDirNotExists + ".\n"); //$NON-NLS-1$
        else if (!dir.isDirectory())
        	throw new IOException(
       			Messages.RepositoryElementLister_InvalidPostconditionsPath
        		+ ".\n"); //$NON-NLS-1$
        
        HashMap<String, String> h = new HashMap<String, String>();
        listFiles(dir, h);
        return h;
    }
    
	/**
	 * Obtiene un conjunto de pares nombre-fichero de los ficheros con 
	 * acciones o predicados disponibles en un directorio.
	 * 
	 * @param sourceDir ruta del directorio en que se buscarán los elementos
	 * del repositorio.
	 * 
	 * @return una tabla en la que se usa como índice el nombre comprensible 
	 * del fichero y como contenido la ruta del fichero; para cada fichero 
	 * encontrado.
	 * 
	 * @throws IOException cuando no existe el directorio, o bien no es 
	 * un directorio.
	 */
    private HashMap<String, String> getElementList(String sourceDir) 
    	throws IOException {

        File dir = new File(sourceDir);
        if (!dir.exists())
        	throw new IOException(
        		Messages.RepositoryElementLister_RepositoryDirNotExists
        		+ ".\n"); //$NON-NLS-1$
        else if (!dir.isDirectory())
        	throw new IOException(
        		Messages.RepositoryElementLister_InvalidElementsPath
        		+ ".\n"); //$NON-NLS-1$
                
        HashMap<String, String> h = new HashMap<String, String>();
        listFiles(dir, h);
        
        return h;
    }
    
    /**
     * Obtiene el conjunto de acciones independientes del lenguaje encontradas
     * en el directorio por defecto de las acciones del repositorio.
     * 
     * @return una tabla asociativa con pares <nombre, fichero> para cada una
     * de las acciones independientes del lenguaje encontradas.
     * 
     * @throws IOException si se el directorio de acciones por defecto no 
     * existe o no se consigue acceder a él.
     */
    public HashMap<String, String> getIndependentActionList() throws IOException {
    	return getElementList(sourceActionDir);
    }
    
    /**
     * Obtiene el conjunto de precondiciones independientes del lenguaje 
     * encontradas en el directorio por defecto de precondiciones del repositorio.
     * 
     * @return una tabla asociativa con pares <nombre, fichero> para cada una
     * de las precondiciones independientes del lenguaje encontradas.
     * 
     * @throws IOException si se el directorio de precondiciones por defecto no 
     * existe o no se consigue acceder a él.
     */
    public HashMap<String, String> getIndependentPreconditionList() throws IOException {
    	return getElementList(sourcePreconditionDir);
    }
    
    /**
     * Obtiene el conjunto de postcondiciones independientes del lenguaje 
     * encontradas en el directorio por defecto de postcondiciones del repositorio.
     * 
     * @return una tabla asociativa con pares <nombre, fichero> para cada una
     * de las postcondiciones independientes del lenguaje encontradas.
     * 
     * @throws IOException si se el directorio de postcondiciones por defecto no 
     * existe o no se consigue acceder a él.
     */
    public HashMap<String, String> getIndependentPostconditionList() throws IOException {
    	return getElementList(sourcePostconditionDir);
    }
   
    /**
     * Obtiene el conjunto de acciones dependientes del lenguaje Java encontradas
     * en el directorio por defecto de las acciones del repositorio dependientes 
     * de Java.
     * 
     * @return una tabla asociativa con pares <nombre, fichero> para cada una
     * de las acciones dependientes de Java encontradas.
     * 
     * @throws IOException si se el directorio de acciones para Java por defecto 
     * no existe o no se consigue acceder a él.
     */
    public HashMap<String, String> getJavaActionList() throws IOException {
    	return getElementList(sourceJavaActionDir);
    }
    
    /**
     * Obtiene el conjunto de predicados dependientes del lenguaje Java encontrados
     * en el directorio por defecto de los predicados del repositorio dependientes 
     * de Java.
     * 
     * @return una tabla asociativa con pares <nombre, fichero> para cada uno
     * de los predicados dependientes de Java encontrados.
     * 
     * @throws IOException si se el directorio de predicados para Java por defecto 
     * no existe o no se consigue acceder a él.
     */
    public HashMap<String, String> getJavaPredicateList() throws IOException {
    	return getElementList(sourceJavaPredicateDir);
    }

    /**
     * Obtiene el conjunto de todas las acciones encontradas en los directorios
     * por defecto de acciones del repositorio, dependientes o independientes del
     * lenguaje.
     * 
     * @return el conjunto de todas las acciones encontradas en los directorios
     * por defecto de acciones del repositorio, dependientes o independientes del
     * lenguaje.
     * 
     * @throws IOException si no se consigue acceder a alguno de los directorios
     * de acciones.
     */
    public HashMap<String, String> getAllActionsList() throws IOException {
    	HashMap <String, String> javaActions = getJavaActionList();
    	HashMap <String, String> independent = getIndependentActionList();
    	
    	independent.putAll(javaActions);
    	
    	return independent;
    }

    /**
     * Obtiene el conjunto de todos los predicados encontrados en los directorios
     * por defecto de predicados del repositorio, dependientes o independientes del
     * lenguaje.
     * 
     * @return el conjunto de todos los predicados encontrados en los directorios
     * por defecto de predicados del repositorio, dependientes o independientes del
     * lenguaje.
     * 
     * @throws IOException si no se consigue acceder a alguno de los directorios
     * de predicados.
     */
    public HashMap<String, String> getAllPredicatesList() throws IOException {
    	HashMap <String, String> javaPredicates = getJavaPredicateList();
    	HashMap <String, String> independent = getIndependentPreconditionList();
    	
    	independent.putAll(javaPredicates);
    	
    	return independent;
    }
    
    /**
     * Genera una tabla con los ficheros de un directorio.
	 * 
	 * @param dir el directorio raíz desde donde se comienza el listado.
	 * @param h una tabla con el nombre del fichero como clave y la ruta 
	 * al mismo como contenido. Se usa como valor de retorno.
	 */
    private void listFiles(File dir, HashMap<String, String> h) {

    	// Si es un directorio se continúa recursivamente.
        if (dir.isDirectory()) {
            String[] hijos = dir.list();
            for (int i=0; i < hijos.length; i++)
            	listFiles(new File(dir, hijos[i]), h);
        }
    	// Si es un fichero de refactorización se almacena en la tabla.
        else if(fileFilter.accept(dir, dir.getName()) == true){
        	h.put(FileManager.getFilePathWithoutExtension(
        		dir.getName()), dir.getName());        		
        }
    }
}