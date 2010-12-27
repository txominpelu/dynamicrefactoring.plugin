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

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.writer.JDOMXMLRefactoringWriterFactory;
import dynamicrefactoring.writer.XMLRefactoringWriter;
import dynamicrefactoring.writer.XMLRefactoringWriterFactory;
import dynamicrefactoring.writer.XMLRefactoringWriterImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test; 


/**
 * Comprueba que funciona correctamente el proceso de escritura de la definición
 * de una refactorización dinámica en un fichero XML.
 * 
 * Indirectamente, se comprueba también el funcionamiento de las clases que
 * implementan los patrones Bridge y Factory Method.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 */
public class TestCaseRefactoringWriter{
	
	/**
	 * Comprueba el lanzamiento de una excepción cuando hay un fallo en escritura.
	 * 
	 * @throws Exception Excepción lanzada tras al aparición de un fallo en lectura.
	 */
	@Test(expected=dynamicrefactoring.writer.XMLRefactoringWriterException.class)
	public void testWritingException() throws Exception{
			//Añadir información general
			DynamicRefactoringDefinition rd = new DynamicRefactoringDefinition();
			rd.setName("MinimumInformation"); //$NON-NLS-1$
			rd.setDescription("Descripcion."); //$NON-NLS-1$
			rd.setMotivation("Motivacion."); //$NON-NLS-1$
	
			//Añadir entradas
			ArrayList<String[]> entradas = new ArrayList<String[]>();
			String entrada[] = new String[5];
			entrada[0]= "moon.core.Model"; //$NON-NLS-1$
			entrada[1]= "Model"; //$NON-NLS-1$
			entrada[2]= ""; //$NON-NLS-1$
			entrada[3]= ""; //$NON-NLS-1$
			entrada[4]= "false"; //$NON-NLS-1$
	
			entradas.add(entrada);
			
			XMLRefactoringWriterFactory f = new JDOMXMLRefactoringWriterFactory();
			XMLRefactoringWriterImp implementor = f
			.makeXMLRefactoringWriterImp(rd);
			XMLRefactoringWriter writer = new XMLRefactoringWriter(implementor);
			writer
			.writeRefactoring(new File(
			"./testdata/XML/NoExiste")); //$NON-NLS-1$
	
	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se añade la
	 * información mínima necesaria. Para ello se da valor a los campos de un
	 * objeto de tipo DynamicRefactoringDefinition y luego se realiza la
	 * escritura; posteriormente se hace una comprobación del contenido del
	 * fichero creado con el contenido que debería tener.
	 * 
	 * Esta información es: el nombre, la descripción, la motivación, una
	 * entrada, una precondición, una acción y una postcondición; no tiene ni
	 * imagen, ni parámetros ambiguos ni ejemplos.
	 * 
	 * @throws Exception si se produce un error al escribir la definición de la
	 * refactorización.
	 */
	@Test
	public void testWritingWithMinimunInformation() throws Exception{
		FileReader fr1 = null;
		FileReader fr2 = null;

		//Añadir información general
		DynamicRefactoringDefinition rd = new DynamicRefactoringDefinition();
		rd.setName("MinimumInformation"); //$NON-NLS-1$
		rd.setDescription("Descripcion."); //$NON-NLS-1$
		rd.setMotivation("Motivacion."); //$NON-NLS-1$

		//Añadir entradas
		ArrayList<String[]> entradas = new ArrayList<String[]>();
		String entrada[] = new String[5];
		entrada[0]= "moon.core.Model"; //$NON-NLS-1$
		entrada[1]= "Model"; //$NON-NLS-1$
		entrada[2]= ""; //$NON-NLS-1$
		entrada[3]= ""; //$NON-NLS-1$
		entrada[4]= "false"; //$NON-NLS-1$

		entradas.add(entrada);

		String entrada2[] = new String[5];
		entrada2[0]= "moon.core.classdef.MethDec"; //$NON-NLS-1$
		entrada2[1]= "Method"; //$NON-NLS-1$
		entrada2[2]= ""; //$NON-NLS-1$
		entrada2[3]= ""; //$NON-NLS-1$
		entrada2[4]= "true"; //$NON-NLS-1$

		entradas.add(entrada2);

		rd.setInputs(entradas);

		//añadir precondiciones,acciones y postcondiciones
		ArrayList<String> preconditions = new ArrayList<String>();
		preconditions.add("ExistsClass (1)"); //$NON-NLS-1$
		rd.setPreconditions(preconditions);

		ArrayList<String> actions = new ArrayList<String>();
		actions.add("RenameClass (1)"); //$NON-NLS-1$
		rd.setActions(actions);

		ArrayList<String> postconditions = new ArrayList<String>();
		postconditions.add("ExistsClass (1)"); //$NON-NLS-1$
		rd.setPostconditions(postconditions);

		XMLRefactoringWriterFactory f = new JDOMXMLRefactoringWriterFactory();
		XMLRefactoringWriterImp implementor = f
		.makeXMLRefactoringWriterImp(rd);
		XMLRefactoringWriter writer = new XMLRefactoringWriter(implementor);
		writer
		.writeRefactoring(new File(
		"./testdata/XML/Writer")); //$NON-NLS-1$

		File archivo1 = new File(
		"./testdata/XML/Reader/MinimumInformation.xml"); //$NON-NLS-1$
		File archivo2 = new File(
		"./testdata/XML/Writer/MinimumInformation.xml"); //$NON-NLS-1$
		String linea1, linea2;
		fr1 = new FileReader(archivo1);
		fr2 = new FileReader(archivo2);
		BufferedReader br1 = new BufferedReader(fr1);
		BufferedReader br2 = new BufferedReader(fr2);
		while ((linea1 = br1.readLine()) != null
				&& (linea2 = br2.readLine()) != null){
			assertEquals(linea1, linea2);
			System.out.println("reader: " + linea1 );
			System.out.println("writer: " + linea2 );
		}

	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se añade toda
	 * la información posible. Para ello se da valor a los campos de un objeto
	 * de tipo DynamicRefactoringDefinition y luego se realiza la escritura;
	 * posteriormente se hace una comprobación del contenido del fichero creado
	 * con el contenido que debería tener.
	 * 
	 * Esta información es: el nombre, la descripción, la imagen, la motivación,
	 * varias entradas, precondiciones, acciones, postcondiciones, parámetros
	 * ambiguos y ejemplos.
	 * 
	 * @throws Exception si se produce un error durante la escritura de la
	 * definición de la refactorización.
	 */
	@Test
	public void testWritingWithFullInformation() throws Exception {

		FileReader fr1 = null;
		FileReader fr2 = null;

		//Añadir información general
		DynamicRefactoringDefinition rd = new DynamicRefactoringDefinition();
		rd.setName("FullInformation"); //$NON-NLS-1$
		rd.setDescription("Renames the class."); //$NON-NLS-1$
		rd.setImage("renameclass.JPG"); //$NON-NLS-1$
		rd.setMotivation("The name of class does not reveal its intention."); //$NON-NLS-1$

		//Añadir entradas
		ArrayList<String[]> entradas = new ArrayList<String[]>();
		String entrada1[] = new String[5];
		entrada1[0]= "moon.core.Name"; //$NON-NLS-1$
		entrada1[1]= "Old_name"; //$NON-NLS-1$
		entrada1[2]= "Class"; //$NON-NLS-1$
		entrada1[3]= "getName"; //$NON-NLS-1$
		entrada1[4]= "false";			 //$NON-NLS-1$
		entradas.add(entrada1);

		String entrada2[] = new String[5];
		entrada2[0]= "moon.core.Model"; //$NON-NLS-1$
		entrada2[1]= "Model"; //$NON-NLS-1$
		entrada2[2]= ""; //$NON-NLS-1$
		entrada2[3]= ""; //$NON-NLS-1$
		entrada2[4]= "false"; //$NON-NLS-1$
		entradas.add(entrada2);

		String entrada3[] = new String[5];
		entrada3[0]= "moon.core.classdef.ClassDef"; //$NON-NLS-1$
		entrada3[1]= "Class"; //$NON-NLS-1$
		entrada3[2]= ""; //$NON-NLS-1$
		entrada3[3]= ""; //$NON-NLS-1$
		entrada3[4]= "true"; //$NON-NLS-1$
		entradas.add(entrada3);

		String entrada4[] = new String[5];
		entrada4[0]= "moon.core.Name"; //$NON-NLS-1$
		entrada4[1]= "New_name"; //$NON-NLS-1$
		entrada4[2]= ""; //$NON-NLS-1$
		entrada4[3]= ""; //$NON-NLS-1$
		entrada4[4]= "false"; //$NON-NLS-1$
		entradas.add(entrada4);

		rd.setInputs(entradas);

		//añadir precondiciones,acciones y postcondiciones
		ArrayList<String> preconditions = new ArrayList<String>();
		preconditions.add("NotExistsClassWithName (1)"); //$NON-NLS-1$
		rd.setPreconditions(preconditions);


		ArrayList<String> actions = new ArrayList<String>();
		actions.add("RenameClass (1)"); //$NON-NLS-1$
		actions.add("RenameReferenceFile (1)"); //$NON-NLS-1$
		actions.add("RenameClassType (1)"); //$NON-NLS-1$
		actions.add("RenameGenericClassType (1)"); //$NON-NLS-1$
		actions.add("RenameConstructors (1)"); //$NON-NLS-1$
		actions.add("RenameJavaFile (1)"); //$NON-NLS-1$
		rd.setActions(actions);


		ArrayList<String> postconditions = new ArrayList<String>();
		postconditions.add("NotExistsClassWithName (1)"); //$NON-NLS-1$
		rd.setPostconditions(postconditions);


		// Añadiendo los parámetros ambiguos.
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		HashMap<String, ArrayList<String[]>>[] map = 
			(HashMap<String, ArrayList<String[]>>[])new HashMap[3];

		map[RefactoringConstants.PRECONDITION] = new HashMap<String, ArrayList<String[]>>();
		map[RefactoringConstants.ACTION] = new HashMap<String, ArrayList<String[]>>();
		map[RefactoringConstants.POSTCONDITION] = new HashMap<String, ArrayList<String[]>>();
		
		ArrayList<String[]> ambiguous1 = new ArrayList<String[]>();
		String[] amb1 = new String[1];
		amb1[0] = "New_name"; //$NON-NLS-1$
		ambiguous1.add(amb1);
		map[RefactoringConstants.PRECONDITION].put("NotExistsClassWithName (1)", ambiguous1); //$NON-NLS-1$
		
		ArrayList<String[]> ambiguous2 = new ArrayList<String[]>();
		String[] amb2a = new String[1];
		amb2a[0] = "Class"; //$NON-NLS-1$
		String[] amb2b = new String[1];
		amb2b[0] = "New_name"; //$NON-NLS-1$
		ambiguous2.add(amb2a);
		ambiguous2.add(amb2b);
		map[RefactoringConstants.ACTION].put("RenameClass (1)", ambiguous2); //$NON-NLS-1$

		ArrayList<String[]> ambiguous3 = new ArrayList<String[]>();
		String[] amb3a = new String[1];
		amb3a[0] = "Class"; //$NON-NLS-1$
		String[] amb3b = new String[1];
		amb3b[0] = "New_name"; //$NON-NLS-1$
		ambiguous3.add(amb3a);
		ambiguous3.add(amb3b);
		map[RefactoringConstants.ACTION].put("RenameReferenceFile (1)", ambiguous3); //$NON-NLS-1$

		ArrayList<String[]> ambiguous4 = new ArrayList<String[]>();
		String[] amb4a = new String[1];
		amb4a[0] = "Class"; //$NON-NLS-1$
		String[] amb4b = new String[1];
		amb4b[0] = "New_name"; //$NON-NLS-1$
		ambiguous4.add(amb4a);
		ambiguous4.add(amb4b);
		map[RefactoringConstants.ACTION].put("RenameClassType (1)", ambiguous4); //$NON-NLS-1$

		ArrayList<String[]> ambiguous5 = new ArrayList<String[]>();
		String[] amb5a = new String[1];
		amb5a[0] = "Class"; //$NON-NLS-1$
		String[] amb5b = new String[1];
		amb5b[0] = "New_name"; //$NON-NLS-1$
		ambiguous5.add(amb5a);
		ambiguous5.add(amb5b);
		map[RefactoringConstants.ACTION].put("RenameGenericClassType (1)", ambiguous5); //$NON-NLS-1$

		ArrayList<String[]> ambiguous6 = new ArrayList<String[]>();
		String[] amb6a = new String[1];
		amb6a[0] = "Class"; //$NON-NLS-1$
		String[] amb6b = new String[1];
		amb6b[0] = "New_name"; //$NON-NLS-1$
		ambiguous6.add(amb6a);
		ambiguous6.add(amb6b);
		map[RefactoringConstants.ACTION].put("RenameConstructors (1)", ambiguous6); //$NON-NLS-1$

		ArrayList<String[]> ambiguous7 = new ArrayList<String[]>();
		String[] amb7a = new String[1];
		amb7a[0] = "Class"; //$NON-NLS-1$
		String[] amb7b = new String[1];
		amb7b[0] = "New_name"; //$NON-NLS-1$
		ambiguous7.add(amb7a);
		ambiguous7.add(amb7b);
		map[RefactoringConstants.ACTION].put("RenameJavaFile (1)", ambiguous7); //$NON-NLS-1$
					
		ArrayList<String[]> ambiguous8 = new ArrayList<String[]>();
		String[] amb8 = new String[1];
		amb8[0] = "Old_name"; //$NON-NLS-1$
		ambiguous8.add(amb8);
		map[RefactoringConstants.POSTCONDITION].put("NotExistsClassWithName (1)", ambiguous8); //$NON-NLS-1$
		
		rd.setAmbiguousParameters(map);

		//añadiendo los ejemplos
		ArrayList<String[]> ejemplos = new ArrayList<String[]>();
		String ejemplo1[] = new String[2];
		ejemplo1[0] = "ejemplo1_antes.txt"; //$NON-NLS-1$
		ejemplo1[1] = "ejemplo1_despues.txt"; //$NON-NLS-1$
		ejemplos.add(ejemplo1);
		rd.setExamples(ejemplos);

		XMLRefactoringWriterFactory f = new JDOMXMLRefactoringWriterFactory();
		XMLRefactoringWriterImp implementor = f
		.makeXMLRefactoringWriterImp(rd);
		XMLRefactoringWriter writer = new XMLRefactoringWriter(implementor);
		writer
		.writeRefactoring(new File(
		"./testdata/XML/Writer")); //$NON-NLS-1$

		File archivo1 = new File(
		"./testdata/XML/Writer/FullInformation.xml"); //$NON-NLS-1$
		File archivo2 = new File(
		"./testdata/XML/Reader/FullInformation.xml"); //$NON-NLS-1$
		String linea1, linea2;
		fr1 = new FileReader(archivo1);
		fr2 = new FileReader(archivo2);
		BufferedReader br1 = new BufferedReader(fr1);
		BufferedReader br2 = new BufferedReader(fr2);
		while ((linea1 = br1.readLine()) != null
				&& (linea2 = br2.readLine()) != null)
			assertEquals(linea1, linea2);
	}
}