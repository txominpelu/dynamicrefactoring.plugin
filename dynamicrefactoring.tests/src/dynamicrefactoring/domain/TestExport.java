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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.xml.ExportImportUtilities;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.PluginStringUtils;
import dynamicrefactoring.util.io.FileManager;

/**
 * Comprueba que funciona correctamente el proceso de exportación de
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class TestExport {

	private static final String RENAME_CLASS_XML_FILE = FilenameUtils
			.separatorsToSystem(RefactoringPlugin.getDynamicRefactoringsDir()
					+ "\\Rename Class\\Rename Class.xml");
	private static final String TEMP_DIR = FilenameUtils
			.separatorsToSystem(".\\temp");

	/**
	 * Preparacion previa a los tests.
	 * @throws IOException 
	 */
	@Before
	public void setUp() throws IOException {
		FileManager.createDir(TEMP_DIR);
		FileManager.copyBundleDirToFileSystem("/DynamicRefactorings/Rename Class/", RefactoringPlugin.getCommonPluginFilesDir());
	}

	/**
	 * Devuelve todo a la situacion anterior a que los tests se ejecutaran.
	 */
	@After
	public void tearDown() {
		// Borramos el directorio temporal al final del test
		FileManager.emptyDirectories(TEMP_DIR);
		FileManager.deleteDirectories(TEMP_DIR, true);
		FileManager.deleteDirectories(RefactoringPlugin.getDynamicRefactoringsDir() + File.separator + "Rename Class", true);
	}

	/**
	 * Comprueba que el proceso de exportación de la refactorización dinámica
	 * Rename Class a un directorio temporal "./temp" se ha realizado
	 * correctamente.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 * @throws IOException
	 *             IOException.
	 */
	@Test
	public void testExportRefactoring() throws XMLRefactoringReaderException,
			IOException {

		// Primero exportamos la refactorización Rename Class a un directorio
		// temporal que luego eliminaremos
		ExportImportUtilities.ExportRefactoring(TEMP_DIR,
				RENAME_CLASS_XML_FILE, false);

		JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp();

		for (String element : RefactoringMechanismInstance.getMechanismListClassNames(reader
						.getDynamicRefactoringDefinition(new File(
								RENAME_CLASS_XML_FILE)).getAllMechanisms())) {

			String name = FilenameUtils.getName(PluginStringUtils
					.splitGetLast(element, "."));
			String namefolder = FilenameUtils.getName(new File(
					RENAME_CLASS_XML_FILE).getParent());

			File resultFile = new File(TEMP_DIR + File.separatorChar
					+ namefolder + File.separatorChar + name + ".class");
			assertEquals(true, resultFile.exists());
		}

	}

	/**
	 * Comprueba que el proceso de exportación de la refactorización dinámica
	 * Rename Class a un directorio temporal "./temp" teniendo en cuenta que uno
	 * de los ficheros .class requeridos no se encuentra en el repositorio.
	 * 
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 * @throws IOException
	 *             IOException.
	 */
	@Test
	public void testExportFileNotExists() throws XMLRefactoringReaderException,
			IOException {

		final String refactoringName = "RenameClass.class";

		final String definitionFolderName = new File(RENAME_CLASS_XML_FILE)
				.getParentFile().getName();
		final String ficheroOrigen = FilenameUtils
				.separatorsToSystem(RefactoringConstants.REFACTORING_CLASSES_DIR
						+ "repository\\moon\\concreteaction\\"
						+ refactoringName);

		try {
			// Copiamos uno de los ficheros .class que necesita la
			// refactorización al directorio
			// temporal y luego lo borramos para que posteriormente salte la
			// excepción.

			FileUtils.copyFileToDirectory(new File(ficheroOrigen), new File(
					TEMP_DIR));
			FileManager.deleteFile(ficheroOrigen);

			ExportImportUtilities.ExportRefactoring(TEMP_DIR,
					RENAME_CLASS_XML_FILE, false);

		} catch (IOException e) {
			// Comprobamos que el directorio en el que se generaría la
			// refactorización no existe al no
			// poderse completar la operación.
			assertEquals(false, new File(TEMP_DIR + File.separatorChar
					+ definitionFolderName).exists());

			// Reponemos el fichero .class que hab�amos borrado para comprobar
			// que saltaba la
			// excepción.

			FileManager.copyFile(new File(TEMP_DIR + File.separatorChar
					+ refactoringName), new File(ficheroOrigen));

			assertEquals(true, new File(ficheroOrigen).exists());

		}
	}

}
