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

package dynamicrefactoring.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.io.FileManager;

/**
 * Comprueba que funciona correctamente el proceso de escritura de las
 * refactorizaciones disponibles para cada �mbito.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class AvailableRefactoringsWriterTest {

	@Before
	public void testSetup() throws IOException {
		manageDirectories();
	}

	/**
	 * Prepara los directorios al estado necesario antes de ejecutar los tests.
	 * 
	 * @throws IOException
	 */
	private void manageDirectories() throws IOException {
		FileManager.copyFolder(RefactoringPlugin.getDynamicRefactoringsDir(),
				"./testdata/XML/Writer/availableRefactorings");
		FileManager.emptyDirectories(RefactoringPlugin
				.getDynamicRefactoringsDir());
		FileManager.deleteDirectories(
				RefactoringPlugin.getDynamicRefactoringsDir(), false);
	}

	@After
	public void tearDown() throws IOException {
		restoreDirectories();
	}

	/**
	 * Devuelve el estado de los directorios al estado inicial antes de iniciar
	 * los tests.
	 * 
	 * @throws IOException
	 */
	private void restoreDirectories() throws IOException {
		// Reponemos las refactorizaciones que habia antes
		FileManager.emptyDirectories(RefactoringPlugin
				.getDynamicRefactoringsDir());
		FileManager.deleteDirectories(
				RefactoringPlugin.getDynamicRefactoringsDir(), false);
		FileManager
				.copyFolder(
						"./testdata/XML/Writer/availableRefactorings/DynamicRefactorings",
						new File(RefactoringPlugin.getDynamicRefactoringsDir())
								.getParent());
		// Borramos la carpeta con las refactorizaciones del directorio temporal
		FileManager
				.emptyDirectories("./testdata/XML/Writer/availableRefactorings/DynamicRefactorings");
		FileManager
				.deleteDirectories(
						"./testdata/XML/Writer/availableRefactorings/DynamicRefactorings",
						true);
		// Borramos el archivo generado para dejar la aplicaci�n como se
		// encontraba.
		FileManager.deleteFile(RefactoringConstants.REFACTORING_TYPES_FILE);
	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se a�ade la
	 * informaci�n m�nima necesaria. Es decir no hay ninguna refactorizaci�n en
	 * la aplicaci�n.
	 * 
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testWritingWithMinimunInformation() throws Exception {

		new JDOMXMLRefactoringWriterImp(null).writeFileToLoadRefactoringTypes();

		assertTrue(FileUtils.contentEquals(new File(RefactoringConstants.REFACTORING_TYPES_FILE), new File(
		"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml")));
	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando solo hay
	 * refactorizaciones de algunos �mbitos y de otros no.
	 * 
	 * 
	 * @throws Exception
	 *             si se produce un error durante la escritura.
	 */
	@Test
	public void testWritingSomeScopes() throws Exception {

		String availableRefactoringsSomeScopesDir = "testdata/XML/Writer/availableRefactorings/someScopes";
		FileManager.copyBundleDirToFileSystem(
				availableRefactoringsSomeScopesDir,
				RefactoringPlugin.getDynamicRefactoringsDir());

		new JDOMXMLRefactoringWriterImp(null).writeFileToLoadRefactoringTypes();

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.CLASS, 0);

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.METHOD, 2);

		compareRefactoringPath(Scope.METHOD,
				availableRefactoringsSomeScopesDir, "Add Parameter");
		compareRefactoringPath(Scope.METHOD,
				availableRefactoringsSomeScopesDir, "AddOverrideAnnotation");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.ATTRIBUTE, 1);
		compareRefactoringPath(Scope.ATTRIBUTE,
				availableRefactoringsSomeScopesDir, "Move Field");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.FORMAL_ARG, 0);

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.FORMAL_PAR, 0);

	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando hay
	 * refactoriozaciones de todos los �mbitos.
	 * 
	 * 
	 * @throws Exception
	 *             si se produce un error durante la escritura.
	 */
	@Test
	public void testWritingAllScopes() throws Exception {

		String availableRefactoringsSomeScopesDir = "testdata/XML/Writer/availableRefactorings/allScopes";
		FileManager.copyBundleDirToFileSystem(
				availableRefactoringsSomeScopesDir,
				RefactoringPlugin.getDynamicRefactoringsDir());

		new JDOMXMLRefactoringWriterImp(null).writeFileToLoadRefactoringTypes();

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.CLASS, 1);
		compareRefactoringPath(Scope.CLASS,
				availableRefactoringsSomeScopesDir, "EnumeratedTypes");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.METHOD, 1);
		compareRefactoringPath(Scope.METHOD,
				availableRefactoringsSomeScopesDir, "Add Parameter");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.ATTRIBUTE, 1);
		compareRefactoringPath(Scope.ATTRIBUTE,
				availableRefactoringsSomeScopesDir, "Move Field");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.FORMAL_ARG, 1);
		compareRefactoringPath(Scope.FORMAL_ARG,
				availableRefactoringsSomeScopesDir, "Remove Parameter");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.FORMAL_PAR, 1);
		compareRefactoringPath(Scope.FORMAL_PAR,
				availableRefactoringsSomeScopesDir, "Specialize Bound S");

		assertNumberOfRefactoringsExpectedForScope(
				availableRefactoringsSomeScopesDir, Scope.CODE_FRAGMENT,
				1);
		compareRefactoringPath(Scope.CODE_FRAGMENT,
				availableRefactoringsSomeScopesDir, "ExtractMethod");

	}

	/**
	 * Comprueba que las refactorizaciones son las esperadas para el ambito
	 * especificado.
	 * 
	 * @param availableRefactoringsSomeScopesDir
	 * @param scope
	 * @param numberOfExpectedRefactorings
	 * @param refactoringName
	 * @throws XMLRefactoringReaderException
	 */
	private void assertNumberOfRefactoringsExpectedForScope(
			String availableRefactoringsSomeScopesDir, Scope scope,
			int numberOfExpectedRefactorings)
			throws XMLRefactoringReaderException {
		HashMap<String, String> refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(scope,
						RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(numberOfExpectedRefactorings, refactorings.size());

	}

	/**
	 * Se asegura que la ruta de la refactorizacion concuerda con la obtenida en
	 * el mapa de refactorizaciones.
	 * 
	 * @param availableRefactoringsSomeScopesDir
	 *            directorio de refactorizaciones de prueba
	 * @param refactoringName
	 *            nombre de la refactorizacion
	 * @throws XMLRefactoringReaderException
	 */
	private void compareRefactoringPath(Scope scope,
			String availableRefactoringsSomeScopesDir, String refactoringName)
			throws XMLRefactoringReaderException {
		HashMap<String, String> refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(scope,
						RefactoringConstants.REFACTORING_TYPES_FILE);
		assertEquals(refactorings.get(refactoringName),
				FilenameUtils.separatorsToSystem(RefactoringPlugin
						.getDynamicRefactoringsDir()
						+ availableRefactoringsSomeScopesDir
						+ File.separator
						+ refactoringName
						+ File.separator
						+ refactoringName
						+ ".xml"));
	}

	/**
	 * Comprueba la adicci�n de una nueva refactorizaci�n dentro del fichero de
	 * refactorizaciones disponibles. Para ello a�ade una refactorizaci�n por
	 * cada �mbito disponible para el elemento principal de la refactorizaci�n.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testAddingRefactoringInformation() throws Exception {

		JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(
				null);
		writer.writeFileToLoadRefactoringTypes();
		writer.addNewRefactoringToXml(Scope.CLASS, "EnumeratedTypes",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "EnumeratedTypes"
						+ File.separatorChar + "EnumeratedTypes.xml");
		writer.addNewRefactoringToXml(Scope.METHOD, "Add Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Add Parameter"
						+ File.separatorChar + "Add Parameter.xml");
		writer.addNewRefactoringToXml(Scope.ATTRIBUTE, "Move Field",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Move Field"
						+ File.separatorChar + "Move Field.xml");
		writer.addNewRefactoringToXml(Scope.FORMAL_ARG,
				"Remove Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Remove Parameter"
						+ File.separatorChar + "Remove Parameter.xml");
		writer.addNewRefactoringToXml(Scope.FORMAL_PAR,
				"Specialize Bound S",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Specialize Bound S"
						+ File.separatorChar + "Specialize Bound S.xml");
		writer.addNewRefactoringToXml(Scope.CODE_FRAGMENT,
				"ExtractMethod", RefactoringPlugin.getDynamicRefactoringsDir()
						+ "" + File.separatorChar + "ExtractMethod"
						+ File.separatorChar + "ExtractMethod.xml");

		HashMap<String, String> refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.CLASS,
						RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("EnumeratedTypes"),
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "EnumeratedTypes"
						+ File.separatorChar + "EnumeratedTypes.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.METHOD,
						RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("Add Parameter"),
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Add Parameter"
						+ File.separatorChar + "Add Parameter.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.ATTRIBUTE,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("Move Field"),
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Move Field"
						+ File.separatorChar + "Move Field.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.FORMAL_ARG,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("Remove Parameter"),
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Remove Parameter"
						+ File.separatorChar + "Remove Parameter.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.FORMAL_PAR,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("Specialize Bound S"),
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Specialize Bound S"
						+ File.separatorChar + "Specialize Bound S.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.CODE_FRAGMENT,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("ExtractMethod"),
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "ExtractMethod"
						+ File.separatorChar + "ExtractMethod.xml");

	}

	/**
	 * Comprueba la eliminaci�n de una refactorizaci�n dentro del fichero de
	 * refactorizaciones disponibles.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testDeleteRefactoringInformation() throws Exception {

		JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(
				null);
		writer.writeFileToLoadRefactoringTypes();
		writer.addNewRefactoringToXml(Scope.CLASS, "EnumeratedTypes",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "EnumeratedTypes"
						+ File.separatorChar + "EnumeratedTypes.xml");
		writer.deleteRefactoringFromXml(Scope.CLASS, "EnumeratedTypes");
		writer.addNewRefactoringToXml(Scope.METHOD, "Add Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Add Parameter"
						+ File.separatorChar + "Add Parameter.xml");
		writer.deleteRefactoringFromXml(Scope.METHOD, "Add Parameter");
		writer.addNewRefactoringToXml(Scope.ATTRIBUTE, "Move Field",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Move Field"
						+ File.separatorChar + "Move Field.xml");
		writer.deleteRefactoringFromXml(Scope.ATTRIBUTE, "Move Field");
		writer.addNewRefactoringToXml(Scope.FORMAL_ARG,
				"Remove Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Remove Parameter"
						+ File.separatorChar + "Remove Parameter.xml");
		writer.deleteRefactoringFromXml(Scope.FORMAL_ARG,
				"Remove Parameter");
		writer.addNewRefactoringToXml(Scope.FORMAL_PAR,
				"Specialize Bound S",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Specialize Bound S"
						+ File.separatorChar + "Specialize Bound S.xml");
		writer.deleteRefactoringFromXml(Scope.FORMAL_PAR,
				"Specialize Bound S");
		writer.addNewRefactoringToXml(Scope.CODE_FRAGMENT,
				"ExtractMethod", RefactoringPlugin.getDynamicRefactoringsDir()
						+ "" + File.separatorChar + "ExtractMethod"
						+ File.separatorChar + "ExtractMethod.xml");
		writer.deleteRefactoringFromXml(Scope.CODE_FRAGMENT,
				"ExtractMethod");

		FileReader fr1 = null;
		FileReader fr2 = null;

		File archivo1 = new File(RefactoringConstants.REFACTORING_TYPES_FILE); //$NON-NLS-1$
		File archivo2 = new File(
				"./testdata/XML/Reader/availableRefactorings/minimunInformation.xml"); //$NON-NLS-1$
		String linea1, linea2;
		fr1 = new FileReader(archivo1);
		fr2 = new FileReader(archivo2);
		BufferedReader br1 = new BufferedReader(fr1);
		BufferedReader br2 = new BufferedReader(fr2);
		while ((linea1 = br1.readLine()) != null
				&& (linea2 = br2.readLine()) != null) {
			assertEquals(linea1, linea2);
		}
		fr1.close();
		fr2.close();

	}

	/**
	 * Comprueba el renombrado de una refactorizaci�n dentro del fichero de
	 * refactorizaciones disponibles.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testRenameRefactoring() throws Exception {

		JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(
				null);
		writer.writeFileToLoadRefactoringTypes();

		writer.addNewRefactoringToXml(Scope.CLASS, "EnumeratedTypes",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "EnumeratedTypes"
						+ File.separatorChar + "EnumeratedTypes.xml");
		writer.renameRefactoringIntoXml(Scope.CLASS, "OtherName",
				"EnumeratedTypes");
		writer.addNewRefactoringToXml(Scope.METHOD, "Add Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Add Parameter"
						+ File.separatorChar + "Add Parameter.xml");
		writer.renameRefactoringIntoXml(Scope.METHOD, "OtherName1",
				"Add Parameter");
		writer.addNewRefactoringToXml(Scope.ATTRIBUTE, "Move Field",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Move Field"
						+ File.separatorChar + "Move Field.xml");
		writer.renameRefactoringIntoXml(Scope.ATTRIBUTE, "OtherName2",
				"Move Field");
		writer.addNewRefactoringToXml(Scope.FORMAL_ARG,
				"Remove Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Remove Parameter"
						+ File.separatorChar + "Remove Parameter.xml");
		writer.renameRefactoringIntoXml(Scope.FORMAL_ARG, "OtherName3",
				"Remove Parameter");
		writer.addNewRefactoringToXml(Scope.FORMAL_PAR,
				"Specialize Bound S",
				RefactoringPlugin.getDynamicRefactoringsDir() + ""
						+ File.separatorChar + "Specialize Bound S"
						+ File.separatorChar + "Specialize Bound S.xml");
		writer.renameRefactoringIntoXml(Scope.FORMAL_PAR, "OtherName4",
				"Specialize Bound S");
		writer.addNewRefactoringToXml(Scope.CODE_FRAGMENT,
				"ExtractMethod", RefactoringPlugin.getDynamicRefactoringsDir()
						+ "" + File.separatorChar + "ExtractMethod"
						+ File.separatorChar + "ExtractMethod.xml");
		writer.renameRefactoringIntoXml(Scope.CODE_FRAGMENT,
				"OtherName5", "ExtractMethod");

		HashMap<String, String> refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.CLASS,
						RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("OtherName"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ "/OtherName/OtherName.xml");

		refactorings = JDOMXMLRefactoringReaderImp
				.readAvailableRefactorings(Scope.METHOD,
						RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("OtherName1"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ "/OtherName1/OtherName1.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.ATTRIBUTE,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("OtherName2"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ "/OtherName2/OtherName2.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.FORMAL_ARG,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("OtherName3"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ "/OtherName3/OtherName3.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.FORMAL_PAR,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("OtherName4"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ "/OtherName4/OtherName4.xml");

		refactorings = JDOMXMLRefactoringReaderImp.readAvailableRefactorings(
				Scope.CODE_FRAGMENT,
				RefactoringConstants.REFACTORING_TYPES_FILE);

		assertEquals(1, refactorings.size());
		assertEquals(refactorings.get("OtherName5"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ "/OtherName5/OtherName5.xml");

	}

}
