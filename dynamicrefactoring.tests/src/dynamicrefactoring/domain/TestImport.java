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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.xml.ExportImportUtilities;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.io.FileManager;

/**
 * Comprueba que funciona correctamente el proceso de importación de
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class TestImport {

	private static final String TEMP_DIR = FilenameUtils
			.separatorsToSystem(".\\temp");

	/**
	 * Prepara los directorios para las pruebas
	 * previas a los tests.
	 * @throws IOException excepcion de I/o
	 * @throws XMLRefactoringReaderException excepcion de lectura de xml
	 */
	@Before
	public void setUp() throws IOException,
			XMLRefactoringReaderException {
		FileManager.createDir(TEMP_DIR);
		FileManager.copyBundleDirToFileSystem("/DynamicRefactorings/Rename Class/", RefactoringPlugin.getCommonPluginFilesDir());
		// Primero exportamos la refactorización Rename Class a un directorio
		// temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(TEMP_DIR,
				getRenameClassXmlFile(), false);

		// Depu�s de exportarla vamos a eliminar la carpeta de Rename Class
		// proveniente de
		// la carpeta de refactorizaciones din�micas
		FileManager.emptyDirectories(getRenameClassDir());
		FileManager.deleteDirectories(getRenameClassDir(), true);
	}

	/**
	 * Elimina los directorios generados para restaurar
	 * el estado de los directorios al que se encontraba antes de ejecutar 
	 * los tests.
	 */
	@After
	public void tearDown() {
		// Borramos el directorio temporal al final del test
		FileManager.deleteDirectories(RefactoringPlugin.getDynamicRefactoringsDir() + File.separator + "Rename Class", true);
		FileManager.emptyDirectories(TEMP_DIR);
		FileManager.deleteDirectories(TEMP_DIR, true);
	}

	/**
	 * Comprueba que el proceso de importación de la refactorización din�mica
	 * Rename Class a partir de un directorio temporal "./temp" se ha realizado
	 * correctamente.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 * @throws IOException
	 *             IOException.
	 */
	@Test
	public void testImportRefactoring() throws XMLRefactoringReaderException, IOException{
		
		// Eliminamos alguno de los ficheros .class requeridos por dicha
		// refactorización para
		// comprobar que tras la importación esos ficheros se encuentran donde
		// deben
		final String renameClassFile = getMoonRefactoryDir() + "concreteaction"
				+ File.separatorChar + "RenameClass.class";
		final String notExistsClassWithNameClassFile = getMoonRefactoryDir()
				+ "concretepredicate" + File.separatorChar
				+ "NotExistsClassWithName.class";
		
		FileManager.deleteFile(renameClassFile);
		FileManager.deleteFile(notExistsClassWithNameClassFile);
		
		// Importamos la refactorización
		ExportImportUtilities.ImportRefactoring(FilenameUtils.separatorsToSystem(".\\temp\\Rename Class\\Rename Class.xml"),false);
		
		// Comprobamos que existe el fichero de definición de la refactorización
		// y los .class que
		// anteriormente hab�amos borrado
		assertEquals(true, new File(getRenameClassXmlFile()).exists());
		assertEquals(true, new File(renameClassFile).exists());
		assertEquals(true, new File(notExistsClassWithNameClassFile).exists());
		
	}

	/**
	 * Comprueba que el proceso de importación de la refactorización din�mica
	 * Rename Class a partir de un directorio temporal "./temp" devuelve la
	 * cadena NotExistsClassWithName en el caso de borrar este mecanismo de la
	 * carpeta donde esta exportada la refactorización y del repositorio.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 * @throws IOException
	 *             IOException.
	 */
	@Test
	public void testImportErroneousRefactoring() throws XMLRefactoringReaderException, IOException{
		
		// Eliminamos alguno de los ficheros .class requeridos por dicha
		// refactorización tanto
		// del repositorio como de la carpeta exportada para ver que la
		// improtación devuelve
		//el nombre de este fichero indicando que no existe.
		final String notExistClassWithNameClassFile = FilenameUtils
				.separatorsToSystem(getMoonRefactoryDir()
						+ "concretepredicate\\NotExistsClassWithName.class");
		final String tempRenameNotExistClassWithNameClassFile = FilenameUtils
				.separatorsToSystem(TEMP_DIR
						+ "\\Rename Class\\NotExistsClassWithName.class");
		final String tempNotExistClassWithNameClassFile = TEMP_DIR
				+ File.separatorChar + "NotExistsClassWithName.class";
		
		FileManager.copyFile(new File(notExistClassWithNameClassFile),new File(tempNotExistClassWithNameClassFile));
		FileManager.deleteFile(notExistClassWithNameClassFile);
		FileManager.deleteFile(tempRenameNotExistClassWithNameClassFile);
		
		// comprobamos que el nombre devuelto es el del fichero que no
		// encuentra.
		try {
			// Importamos la refactorización
			ExportImportUtilities
					.ImportRefactoring(
					FilenameUtils.separatorsToSystem(TEMP_DIR
							+ "\\Rename Class\\Rename Class.xml"),
							false);
			fail("No salto la excepcion esperada.");
		} catch (FileNotFoundException e) {
			// Comprobamos que no existe el fichero del mecanismo que hemos
			// borrado de la carpeta
			// de la refactorización y del repositorio.
			assertEquals(false,
					new File(notExistClassWithNameClassFile).exists());
		} finally {
			// Reponemos el mecanismo
			FileManager.copyFile(new File(tempNotExistClassWithNameClassFile),
					new File(notExistClassWithNameClassFile));
		}
		
	}

	private String getMoonRefactoryDir() {
		return FilenameUtils
				.separatorsToSystem(RefactoringConstants.REFACTORING_CLASSES_DIR
						+ "repository\\moon\\");
	}

	private String getRenameClassXmlFile() {
		return FilenameUtils.separatorsToSystem(getRenameClassDir()
				+ "\\Rename Class.xml");
	}

	private String getRenameClassDir() {
		return FilenameUtils.separatorsToSystem(RefactoringPlugin
				.getDynamicRefactoringsDir() + "\\Rename Class");
	}

}
