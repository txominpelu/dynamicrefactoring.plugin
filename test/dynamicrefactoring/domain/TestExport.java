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

package dynamicrefactoring.domain;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.ExportImportUtilities;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.XMLRefactoringReaderException;

import static org.junit.Assert.*;


/**
 * Comprueba que funciona correctamente el proceso de exportación de refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 *
 */
public class TestExport {
	
	/**
	 * Comprueba que el proceso de exportación de la refactorización  dinámica Rename Class
	 * a un directorio temporal "./temp" se ha realizado correctamente.
	 * 
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 * @throws IOException IOException.
	 */
	@Test
	public void testExportRefactoring() throws XMLRefactoringReaderException, IOException{
		String destination = "./temp";
		FileManager.createDir(destination);
		//String definition = RefactoringConstants.DYNAMIC_REFACTORING_DIR + "\\Rename Class\\Rename Class.xml";
		String definitionOriginal = RefactoringConstants.DYNAMIC_REFACTORING_DIR + "\\Rename Class\\Rename Class.xml";
		
		String definition = FilenameUtils.separatorsToSystem(definitionOriginal);
		
		//Primero exportamos la refactorización Rename Class a un directorio temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(destination,definition,false );
		
		JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp(new File(definition));
		
		for(String element : reader.readMechanismRefactoring()){
			
			String name = FilenameUtils.getName(ExportImportUtilities.splitGetLast(element, "."));
			/*StringTokenizer st_name = new StringTokenizer(element,".");
			while(st_name.hasMoreTokens()){
				name = st_name.nextElement().toString();
			}*/
			
			String namefolder = FilenameUtils.getName(new File(definition).getParent());
			/*StringTokenizer st_namefolder = new StringTokenizer(new File(definition).getParent(), "\\");
			String namefolder = "";
			while(st_namefolder.hasMoreTokens()){
				namefolder = st_namefolder.nextElement().toString();
			}*/
			File resultFile = new File (destination + File.separatorChar + namefolder + File.separatorChar + name + ".class");
			assertEquals(true , resultFile.exists());
		}
		
		//Borramos el directorio temporal al final del test
		FileManager.emptyDirectories(destination);
		FileManager.deleteDirectories(destination, true);
	}
	
	/**
	 * Comprueba que el proceso de exportación de la refactorización  dinámica Rename Class
	 * a un directorio temporal "./temp" teniendo en cuenta que uno de los ficheros .class
	 * requeridos no se encuentra en el repositorio.
	 * 
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 * @throws IOException IOException.
	 */
	@Test(expected=IOException.class)
	public void testExportFileNotExists() throws XMLRefactoringReaderException, IOException{
		FileManager.createDir("./temp");
		String destination = "./temp";
		String definition = RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class\\Rename Class.xml";
		StringTokenizer st_namefolder = new StringTokenizer(new File(definition).getParent(), "\\");
		String namefolder = "";
		while(st_namefolder.hasMoreTokens()){
			namefolder = st_namefolder.nextElement().toString();
		}
		
		try{
		//Copiamos uno de los ficheros .class que necesita la refactorización al directorio
		//temporal y luego lo borramos para que posteriormente salte la excepción.
		FileManager.copyFile(new File(".\\bin\\repository\\moon\\concreteaction\\RenameClass.class"), new File(".\\temp\\RenameClass.class"));
		FileManager.deleteFile(".\\bin\\repository\\moon\\concreteaction\\RenameClass.class");
		
		ExportImportUtilities.ExportRefactoring(destination,definition,false );
		
		}catch(IOException e){			
			//Comprobamos que el directorio en el que se generaría la refactorización no existe al no 
			//poderse completar la operación.
			assertEquals(false, new File(destination + "//" + namefolder).exists());
			
			//Reponemos el fichero .class que habíamos borrado para comprobar que saltaba la
			//excepción.
			FileManager.copyFile(new File(".\\temp\\RenameClass.class"), new File(".\\bin\\repository\\moon\\concreteaction\\RenameClass.class"));
			
			assertEquals(true, new File(".\\bin\\repository\\moon\\concreteaction\\RenameClass.class").exists() );
			
			//Borramos el directorio temporal al final del test
			FileManager.emptyDirectories(destination);
			FileManager.deleteDirectories(destination, true);
			throw e;
		}
	}

	
}	

