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

import java.io.IOException;
import java.io.File;

import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.io.FileManager;

import static org.junit.Assert.*;

/**
 * Comprueba que funciona correctamente el proceso de importación de refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 *
 */
public class TestImport {
	
	/**
	 * Comprueba que el proceso de importación de la refactorización  dinámica Rename Class
	 * a partir de un directorio temporal "./temp" se ha realizado correctamente.
	 * 
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 * @throws IOException IOException.
	 */
	@Test
	public void testImportRefactoring() throws XMLRefactoringReaderException, IOException{
		FileManager.createDir("./temp");
		String destination = "./temp";
		String definition = RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class\\Rename Class.xml";
		//Primero exportamos la refactorización Rename Class a un directorio temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(destination,definition,false );
		
		//Depués de exportarla vamos a eliminar la carpeta de Rename Class proveniente de
		//la carpeta de refactorizaciones dinámicas
		FileManager.emptyDirectories(RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class");
		FileManager.deleteDirectories(RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class", true);
		
		//Eliminamos alguno de los ficheros .class requeridos por dicha refactorización para
		//comprobar que tras la importación esos ficheros se encuentran donde deben
		FileManager.deleteFile(".\\bin\\repository\\moon\\concreteaction\\RenameClass.class");
		FileManager.deleteFile(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class");
		
		//Importamos la refactorización
		ExportImportUtilities.ImportRefactoring("./temp/Rename Class/Rename Class.xml",false);
		
		//Comprobamos que existe el fichero de definición de la refactorización y los .class que
		//anteriormente habíamos borrado
		assertEquals(true, new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class\\Rename Class.xml").exists());
		assertEquals(true, new File(".\\bin\\repository\\moon\\concreteaction\\RenameClass.class").exists());
		assertEquals(true, new File(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class").exists());
		
		//Borramos el directorio temporal al final del test
		FileManager.emptyDirectories(destination);
		FileManager.deleteDirectories(destination, true);
	}
	
	/**
	 * Comprueba que el proceso de importación de la refactorización  dinámica Rename Class
	 * a partir de un directorio temporal "./temp" devuelve la cadena NotExistsClassWithName
	 * en el caso de borrar este mecanismo de la carpeta donde esta exportada la 
	 * refactorización y del repositorio.
	 * 
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 * @throws IOException IOException.
	 */
	@Test
	public void testImportErroneousRefactoring() throws XMLRefactoringReaderException, IOException{
		FileManager.createDir("./temp");
		String destination = "./temp";
		String definition = RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class\\Rename Class.xml";
		//Primero exportamos la refactorización Rename Class a un directorio temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(destination,definition,false );
		
		//Depués de exportarla vamos a eliminar la carpeta de Rename Class proveniente de
		//la carpeta de refactorizaciones dinámicas
		FileManager.emptyDirectories(RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class");
		FileManager.deleteDirectories(RefactoringConstants.DYNAMIC_REFACTORING_DIR + ".\\Rename Class", true);
		
		//Eliminamos alguno de los ficheros .class requeridos por dicha refactorización tanto
		//del repositorio como de la carpeta exportada para ver que la improtación devuelve
		//el nombre de este fichero indicando que no existe.
		FileManager.copyFile(new File(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class"),new File(".\\temp\\NotExistsClassWithName.class"));
		FileManager.deleteFile(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class");
		FileManager.deleteFile(".\\temp\\Rename Class\\NotExistsClassWithName.class");
		
		//Importamos la refactorización
		String name=ExportImportUtilities.ImportRefactoring("./temp/Rename Class/Rename Class.xml",false);
		
		//comprobamos que el nombre devuelto es el del fichero que no encuentra.
		assertEquals(true,name.equals("NotExistsClassWithName"));
		
		//Comprobamos que no existe el fichero del mecanismo que hemos borrado de la carpeta
		//de la refactorización y del repositorio.
		assertEquals(false, new File(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class").exists());
		
		//Reponemos el mecanismo
		FileManager.copyFile(new File(".\\temp\\NotExistsClassWithName.class"),new File(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class"));
		
		//Borramos el directorio temporal al final del test
		FileManager.emptyDirectories(destination);
		FileManager.deleteDirectories(destination, true);
	}

}
