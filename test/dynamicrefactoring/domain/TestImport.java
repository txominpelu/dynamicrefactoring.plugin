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

import org.apache.commons.io.FilenameUtils;
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
		String destination = FilenameUtils.separatorsToSystem(".\\temp");
		FileManager.createDir(destination);
		String renameClassDir = FilenameUtils.separatorsToSystem(RefactoringConstants.DYNAMIC_REFACTORING_DIR + "\\Rename Class");
		String definition = FilenameUtils.separatorsToSystem(renameClassDir + "\\Rename Class.xml");
		 
		//Primero exportamos la refactorización Rename Class a un directorio temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(destination,definition,false );
		
		//Depués de exportarla vamos a eliminar la carpeta de Rename Class proveniente de
		//la carpeta de refactorizaciones dinámicas
		FileManager.emptyDirectories(renameClassDir);
		FileManager.deleteDirectories(renameClassDir, true);
		
		//Eliminamos alguno de los ficheros .class requeridos por dicha refactorización para
		//comprobar que tras la importación esos ficheros se encuentran donde deben
		String repositoryDir = FilenameUtils.separatorsToSystem(".\\bin\\repository\\moon\\");
		String renameClassFile = repositoryDir + "concreteaction" + File.separatorChar + "RenameClass.class";
		String notExistsClassWithNameClassFile = repositoryDir + "concretepredicate" + File.separatorChar + "NotExistsClassWithName.class";
		
		FileManager.deleteFile(renameClassFile);
		FileManager.deleteFile(notExistsClassWithNameClassFile);
		
		//Importamos la refactorización
		ExportImportUtilities.ImportRefactoring(FilenameUtils.separatorsToSystem(".\\temp\\Rename Class\\Rename Class.xml"),false);
		
		//Comprobamos que existe el fichero de definición de la refactorización y los .class que
		//anteriormente habíamos borrado
		assertEquals(true, new File(definition).exists());
		assertEquals(true, new File(renameClassFile).exists());
		assertEquals(true, new File(notExistsClassWithNameClassFile).exists());
		
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
		String destination = FilenameUtils.separatorsToSystem(".\\temp");
		FileManager.createDir(destination);
		final String definition = FilenameUtils.separatorsToSystem(RefactoringConstants.DYNAMIC_REFACTORING_DIR + "\\Rename Class\\Rename Class.xml");
		//Primero exportamos la refactorización Rename Class a un directorio temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(destination,definition,false );
		
		//Depués de exportarla vamos a eliminar la carpeta de Rename Class proveniente de
		//la carpeta de refactorizaciones dinámicas
		final String renameClassDir = FilenameUtils.separatorsToSystem(RefactoringConstants.DYNAMIC_REFACTORING_DIR + "\\Rename Class");
		FileManager.emptyDirectories(renameClassDir);
		FileManager.deleteDirectories(renameClassDir, true);
		
		//Eliminamos alguno de los ficheros .class requeridos por dicha refactorización tanto
		//del repositorio como de la carpeta exportada para ver que la improtación devuelve
		//el nombre de este fichero indicando que no existe.
		final String notExistClassWithNameClassFile = FilenameUtils.separatorsToSystem(".\\bin\\repository\\moon\\concretepredicate\\NotExistsClassWithName.class");
		final String tempRenameNotExistClassWithNameClassFile = FilenameUtils.separatorsToSystem(".\\temp\\Rename Class\\NotExistsClassWithName.class");
		final String tempNotExistClassWithNameClassFile = destination + File.separatorChar + "NotExistsClassWithName.class";
		
		FileManager.copyFile(new File(notExistClassWithNameClassFile),new File(tempNotExistClassWithNameClassFile));
		FileManager.deleteFile(notExistClassWithNameClassFile);
		FileManager.deleteFile(tempRenameNotExistClassWithNameClassFile);
		
		//Importamos la refactorización
		String name=ExportImportUtilities.ImportRefactoring(FilenameUtils.separatorsToSystem(".\\temp\\Rename Class\\Rename Class.xml"),false);
		
		//comprobamos que el nombre devuelto es el del fichero que no encuentra.
		assertEquals(true,name.equals("NotExistsClassWithName"));
		
		//Comprobamos que no existe el fichero del mecanismo que hemos borrado de la carpeta
		//de la refactorización y del repositorio.
		assertEquals(false, new File(notExistClassWithNameClassFile).exists());
		
		//Reponemos el mecanismo
		FileManager.copyFile(new File(tempNotExistClassWithNameClassFile),new File(notExistClassWithNameClassFile));
		
		//Borramos el directorio temporal al final del test
		FileManager.emptyDirectories(destination);
		FileManager.deleteDirectories(destination, true);
	}

}
