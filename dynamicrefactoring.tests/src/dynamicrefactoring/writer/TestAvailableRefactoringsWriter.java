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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.interfaz.SelectRefactoringWindow;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.util.io.FileManager;

/**
 * Comprueba que funciona correctamente el proceso de escritura de las
 * refactorizaciones disponibles para cada ámbito.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class TestAvailableRefactoringsWriter{

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
		// Borramos el archivo generado para dejar la aplicación como se
		// encontraba.
		FileManager.deleteFile(RefactoringConstants.REFACTORING_TYPES_FILE);
	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se añade la
	 * información mínima necesaria. Es decir no hay ninguna refactorización en
	 * la aplicación.
	 * 
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testWritingWithMinimunInformation() throws Exception{
		
		new JDOMXMLRefactoringWriterImp(null).writeFileToLoadRefactoringTypes();
		
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
				&& (linea2 = br2.readLine()) != null){
			assertEquals(linea1, linea2);
		}
		fr1.close();
		fr2.close();
	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando solo hay
	 * refactorizaciones de algunos ámbitos y de otros no.
	 * 
	 * 
	 * @throws Exception
	 *             si se produce un error durante la escritura.
	 */
	@Test
	public void testWritingSomeScopes() throws Exception{

		String availableRefactoringsSomeScopesDir = "testdata/XML/Writer/availableRefactorings/someScopes";
		FileManager.copyBundleDirToFileSystem(
				availableRefactoringsSomeScopesDir,
				RefactoringPlugin.getDynamicRefactoringsDir());

		new JDOMXMLRefactoringWriterImp(null).writeFileToLoadRefactoringTypes();
		
		HashMap<String,String> refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_CLASS,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(0,refactorings.size());
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_METHOD,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(2,refactorings.size());
		assertEquals(
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ availableRefactoringsSomeScopesDir
 + File.separatorChar
				+ "Add Parameter" + File.separatorChar + "Add Parameter.xml",
				refactorings.get("Add Parameter"));
		assertEquals(refactorings.get("AddOverrideAnnotation"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "AddOverrideAnnotation"
						+ File.separatorChar + "AddOverrideAnnotation.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Move Field"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "Move Field"
						+ File.separatorChar + "Move Field.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_ARG,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(0,refactorings.size());

		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_PAR,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(0,refactorings.size());


	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando hay
	 * refactoriozaciones de todos los ámbitos.
	 * 
	 * 
	 * @throws Exception
	 *             si se produce un error durante la escritura.
	 */
	@Test
	public void testWritingAllScopes() throws Exception{

		String availableRefactoringsSomeScopesDir = "testdata/XML/Writer/availableRefactorings/allScopes";
		FileManager.copyBundleDirToFileSystem(
				availableRefactoringsSomeScopesDir,
				RefactoringPlugin.getDynamicRefactoringsDir());

		new JDOMXMLRefactoringWriterImp(null).writeFileToLoadRefactoringTypes();
		
		HashMap<String,String> refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_CLASS,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("EnumeratedTypes"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "EnumeratedTypes"
						+ File.separatorChar + "EnumeratedTypes.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_METHOD,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Add Parameter"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "Add Parameter"
						+ File.separatorChar + "Add Parameter.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Move Field"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "Move Field"
						+ File.separatorChar + "Move Field.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_ARG,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Remove Parameter"),
				RefactoringPlugin.getDynamicRefactoringsDir()
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "Remove Parameter"
						+ File.separatorChar + "Remove Parameter.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_PAR,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Specialize Bound S"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "Specialize Bound S"
						+ File.separatorChar + "Specialize Bound S.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("ExtractMethod"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
						+ availableRefactoringsSomeScopesDir
						+ File.separatorChar + "ExtractMethod"
						+ File.separatorChar + "ExtractMethod.xml");

		//Reponemos las refactorizaciones que habia antes

	}

	/**
	 * Comprueba la adicción de una nueva refactorización dentro del fichero de
	 * refactorizaciones disponibles. Para ello añade una refactorización por
	 * cada ámbito disponible para el elemento principal de la refactorización.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testAddingRefactoringInformation() throws Exception{
		
		JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(null);
		writer.writeFileToLoadRefactoringTypes();
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_CLASS, "EnumeratedTypes"
				,RefactoringPlugin.getDynamicRefactoringsDir()+ "" + File.separatorChar + "EnumeratedTypes" + File.separatorChar + "EnumeratedTypes.xml");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_METHOD,"Add Parameter"
				, RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "Add Parameter" + File.separatorChar + "Add Parameter.xml");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_ATTRIBUTE, "Move Field",
				RefactoringPlugin.getDynamicRefactoringsDir()+ "" + File.separatorChar + "Move Field" + File.separatorChar + "Move Field.xml");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_FORMAL_ARG, "Remove Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir()
				+ "" + File.separatorChar + "Remove Parameter" + File.separatorChar + "Remove Parameter.xml");
	    writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_FORMAL_PAR, "Specialize Bound S",
	    		RefactoringPlugin.getDynamicRefactoringsDir()	
	    		+ "" + File.separatorChar + "Specialize Bound S" + File.separatorChar + "Specialize Bound S.xml");
	    writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT, "ExtractMethod",
	    		RefactoringPlugin.getDynamicRefactoringsDir()	
	    		+ "" + File.separatorChar + "ExtractMethod" + File.separatorChar + "ExtractMethod.xml");
		
		HashMap<String,String> refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_CLASS,
					 RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("EnumeratedTypes"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "" + File.separatorChar + "EnumeratedTypes" + File.separatorChar + "EnumeratedTypes.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_METHOD,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Add Parameter"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "" + File.separatorChar + "Add Parameter" + File.separatorChar + "Add Parameter.xml");

		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Move Field"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
				+ "" + File.separatorChar + "Move Field" + File.separatorChar + "Move Field.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_ARG,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Remove Parameter"),
				RefactoringPlugin.getDynamicRefactoringsDir()
				+ "" + File.separatorChar + "Remove Parameter" + File.separatorChar + "Remove Parameter.xml");

		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_PAR,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("Specialize Bound S"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
				+ "" + File.separatorChar + "Specialize Bound S" + File.separatorChar + "Specialize Bound S.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("ExtractMethod"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
				+ "" + File.separatorChar + "ExtractMethod" + File.separatorChar + "ExtractMethod.xml");
		
	}

	/**
	 * Comprueba la eliminación de una refactorización dentro del fichero de
	 * refactorizaciones disponibles.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testDeleteRefactoringInformation() throws Exception{
		
		JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(null);
		writer.writeFileToLoadRefactoringTypes();
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_CLASS, "EnumeratedTypes"
				,RefactoringPlugin.getDynamicRefactoringsDir()+ "" + File.separatorChar + "EnumeratedTypes" + File.separatorChar + "EnumeratedTypes.xml");
		writer.deleteRefactoringFromXml(SelectRefactoringWindow.SCOPE_CLASS, "EnumeratedTypes");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_METHOD,"Add Parameter"
				, RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "Add Parameter" + File.separatorChar + "Add Parameter.xml");
		writer.deleteRefactoringFromXml(SelectRefactoringWindow.SCOPE_METHOD, "Add Parameter");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_ATTRIBUTE, "Move Field",
				RefactoringPlugin.getDynamicRefactoringsDir()+ "" + File.separatorChar + "Move Field" + File.separatorChar + "Move Field.xml");
		writer.deleteRefactoringFromXml(SelectRefactoringWindow.SCOPE_ATTRIBUTE, "Move Field");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_FORMAL_ARG, "Remove Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir()
				+ "" + File.separatorChar + "Remove Parameter" + File.separatorChar + "Remove Parameter.xml");
		writer.deleteRefactoringFromXml(SelectRefactoringWindow.SCOPE_FORMAL_ARG, "Remove Parameter");
	    writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_FORMAL_PAR, "Specialize Bound S",
	    		RefactoringPlugin.getDynamicRefactoringsDir()	
	    		+ "" + File.separatorChar + "Specialize Bound S" + File.separatorChar + "Specialize Bound S.xml");
	    writer.deleteRefactoringFromXml(SelectRefactoringWindow.SCOPE_FORMAL_PAR, "Specialize Bound S");
	    writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT, "ExtractMethod",
	    		RefactoringPlugin.getDynamicRefactoringsDir()	
	    		+ "" + File.separatorChar + "ExtractMethod" + File.separatorChar + "ExtractMethod.xml");
	    writer.deleteRefactoringFromXml(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT, "ExtractMethod");
		
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
				&& (linea2 = br2.readLine()) != null){
			assertEquals(linea1, linea2);
		}
		fr1.close();
		fr2.close();
		
	}

	/**
	 * Comprueba el renombrado de una refactorización dentro del fichero de
	 * refactorizaciones disponibles.
	 * 
	 * @throws Exception
	 *             si se produce un error al escribir.
	 */
	@Test
	public void testRenameRefactoring() throws Exception{
		
		JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(null);
		writer.writeFileToLoadRefactoringTypes();
		
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_CLASS, "EnumeratedTypes"
				,RefactoringPlugin.getDynamicRefactoringsDir()+ "" + File.separatorChar + "EnumeratedTypes" + File.separatorChar + "EnumeratedTypes.xml");
		writer.renameRefactoringIntoXml(SelectRefactoringWindow.SCOPE_CLASS
				,"OtherName", "EnumeratedTypes");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_METHOD,"Add Parameter"
				, RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "Add Parameter" + File.separatorChar + "Add Parameter.xml");
		writer.renameRefactoringIntoXml(SelectRefactoringWindow.SCOPE_METHOD
				,"OtherName1", "Add Parameter");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_ATTRIBUTE, "Move Field",
				RefactoringPlugin.getDynamicRefactoringsDir()+ "" + File.separatorChar + "Move Field" + File.separatorChar + "Move Field.xml");
		writer.renameRefactoringIntoXml(SelectRefactoringWindow.SCOPE_ATTRIBUTE
				,"OtherName2", "Move Field");
		writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_FORMAL_ARG, "Remove Parameter",
				RefactoringPlugin.getDynamicRefactoringsDir()
				+ "" + File.separatorChar + "Remove Parameter" + File.separatorChar + "Remove Parameter.xml");
		writer.renameRefactoringIntoXml(SelectRefactoringWindow.SCOPE_FORMAL_ARG
				,"OtherName3", "Remove Parameter");
	    writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_FORMAL_PAR, "Specialize Bound S",
	    		RefactoringPlugin.getDynamicRefactoringsDir()	
	    		+ "" + File.separatorChar + "Specialize Bound S" + File.separatorChar + "Specialize Bound S.xml");
	    writer.renameRefactoringIntoXml(SelectRefactoringWindow.SCOPE_FORMAL_PAR
				,"OtherName4", "Specialize Bound S");
	    writer.addNewRefactoringToXml(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT, "ExtractMethod",
	    		RefactoringPlugin.getDynamicRefactoringsDir()	
	    		+ "" + File.separatorChar + "ExtractMethod" + File.separatorChar + "ExtractMethod.xml");
	    writer.renameRefactoringIntoXml(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT
				,"OtherName5", "ExtractMethod");
	    
	
		HashMap<String,String> refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_CLASS,
					 RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("OtherName"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "/OtherName/OtherName.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_METHOD,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("OtherName1"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "/OtherName1/OtherName1.xml");

		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
			(SelectRefactoringWindow.SCOPE_ATTRIBUTE,
					RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("OtherName2"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "/OtherName2/OtherName2.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_ARG,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("OtherName3"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "/OtherName3/OtherName3.xml");

		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_FORMAL_PAR,
				RefactoringConstants.REFACTORING_TYPES_FILE);
	
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("OtherName4"),
				RefactoringPlugin.getDynamicRefactoringsDir() 
				+ "/OtherName4/OtherName4.xml");
		
		refactorings 
		=new JDOMXMLRefactoringReaderImp().readAvailableRefactorings
		(SelectRefactoringWindow.SCOPE_CODE_FRAGMENT,
				RefactoringConstants.REFACTORING_TYPES_FILE);
		
		assertEquals(1,refactorings.size());
		assertEquals(refactorings.get("OtherName5"),
				RefactoringPlugin.getDynamicRefactoringsDir()	
				+ "/OtherName5/OtherName5.xml");
		
	}
	
	
}
