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
import dynamicrefactoring.writer.RefactoringPlanWriter;
import dynamicrefactoring.util.io.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test; 


/**
 * Comprueba que funciona correctamente el proceso de escritura de un plan
 * de refactorizaciones dinámicas en un fichero XML.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * 
 */
public class TestRefactoringPlanWriter{

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se añade la
	 * información mínima necesaria. Es decir se inicializa el fichero con el 
	 * elemento RefactoringPlan.
	 * 
	 * @throws Exception si se produce un error al escribir el plan.
	 */
	@Test
	public void testWritingPlanWithMinimunInformation() throws Exception{
		FileReader fr1 = null;
		FileReader fr2 = null;

		RefactoringPlanWriter.getInstance();

		File archivo1 = new File(RefactoringConstants.REFACTORING_PLAN_FILE); //$NON-NLS-1$
		File archivo2 = new File(
		"./testdata/XML/Reader/refactoringPlan/minimunRefactoringPlan.xml"); //$NON-NLS-1$
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
		RefactoringPlanWriter.getInstance().reset();
		//Borramos el archivo generado para dejar la aplicación como se encontraba.
		FileManager.deleteFile(RefactoringConstants.REFACTORING_PLAN_FILE);

	}

	/**
	 * Comprueba que la escritura se realiza correctamente cuando se añade un par de 
	 * refactorizaciones al plan de refactorizaciones.
	 * 
	 * Esta información es: la información de la refactorización consta de su nombre
	 * , fecha de ejecución y parámetros.
	 * 
	 * @throws Exception si se produce un error durante la escritura del plan.
	 */
	@Test
	public void testWritingRefactoringIntoThePlan() throws Exception{
		FileReader fr1 = null;
		FileReader fr2 = null;

		RefactoringPlanWriter writer = RefactoringPlanWriter.getInstance();
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("Name" ,"p4" );
		parameters.put("Method","paqueteA.C~met1%int%Double%short");
		parameters.put("Type","int");
		parameters.put("Class","paqueteA.C");
		
		writer.writeRefactoring("Add Parameter", "02/20/09 - 12:42:34", parameters);
		
		parameters.clear();
		parameters.put("Source_class","paqueteA.H"  );
		parameters.put("Attribute" ,"paqueteA.H#atributo1" );
		parameters.put("Target_class","paqueteA.B");
		
		writer.writeRefactoring("Move Field", "02/20/09 - 12:43:26", parameters);
		
		parameters.clear();
		parameters.put("Name","nuevo_metodo"  );
		parameters.put("Fragment" ,"11,8,11,29,paqueteA.E,System.out.println(a);" );
		
		writer.writeRefactoring("ExtractMethod", "02/20/09 - 12:44:58", parameters);
		

		File archivo1 = new File(RefactoringConstants.REFACTORING_PLAN_FILE); //$NON-NLS-1$
		File archivo2 = new File(
		"./testdata/XML/Reader/refactoringPlan/completeRefactoringPlan.xml"); //$NON-NLS-1$
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
		RefactoringPlanWriter.getInstance().reset();
		//Borramos el archivo generado para dejar la aplicación como se encontraba.
		FileManager.deleteFile(RefactoringConstants.REFACTORING_PLAN_FILE);
	}
	
	/**
	 * Comprueba que la eliminación de una refactorización del fichero del plan
	 * de refactorizaciones se realiza de forma correcta.
	 * 
	 * @throws Exception si se produce un error durante la escritura del plan.
	 */
	@Test
	public void testDeleteRefactoringFromThePlan() throws Exception{
		FileReader fr1 = null;
		FileReader fr2 = null;

		RefactoringPlanWriter writer = RefactoringPlanWriter.getInstance();
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("Name" ,"p4" );
		parameters.put("Method","paqueteA.C~met1%int%Double%short");
		parameters.put("Type","int");
		parameters.put("Class","paqueteA.C");
		
		writer.writeRefactoring("Add Parameter", "02/20/09 - 12:42:34", parameters);
		
		parameters.clear();
		parameters.put("Source_class","paqueteA.H"  );
		parameters.put("Attribute" ,"paqueteA.H#atributo1" );
		parameters.put("Target_class","paqueteA.B");
		
		writer.writeRefactoring("Move Field", "02/20/09 - 12:43:26", parameters);
		
		writer.deleteRefactorings("Add Parameter", "02/20/09 - 12:42:34");
		

		File archivo1 = new File(RefactoringConstants.REFACTORING_PLAN_FILE); //$NON-NLS-1$
		File archivo2 = new File(
		"./testdata/XML/Reader/refactoringPlan/minimunRefactoringPlan.xml"); //$NON-NLS-1$
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
		RefactoringPlanWriter.getInstance().reset();
		//Borramos el archivo generado para dejar la aplicación como se encontraba.
		FileManager.deleteFile(RefactoringConstants.REFACTORING_PLAN_FILE);
	}
	
	/**
	 * Comprueba que la eliminación de un grupo de refactorizaciones del fichero del plan
	 * de refactorizaciones se realiza de forma correcta.
	 * 
	 * Aunque se eliminen varias refactorizaciones del plan, solo se manda eliminar 
	 * una. Pero el funcionamiento del método hace que también se borren las que se
	 * han producido después.
	 * 
	 * @throws Exception si se produce un error durante la escritura del plan.
	 */
	@Test
	public void testDeleteRefactoringsFromThePlan() throws Exception{
		FileReader fr1 = null;
		FileReader fr2 = null;

		RefactoringPlanWriter writer = RefactoringPlanWriter.getInstance();
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("Name" ,"p4" );
		parameters.put("Method","paqueteA.C~met1%int%Double%short");
		parameters.put("Type","int");
		parameters.put("Class","paqueteA.C");
		
		writer.writeRefactoring("Add Parameter", "02/20/09 - 12:42:34", parameters);
		writer.deleteRefactorings("Add Parameter", "02/20/09 - 12:42:34");
		

		File archivo1 = new File(RefactoringConstants.REFACTORING_PLAN_FILE); //$NON-NLS-1$
		File archivo2 = new File(
		"./testdata/XML/Reader/refactoringPlan/minimunRefactoringPlan.xml"); //$NON-NLS-1$
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
		RefactoringPlanWriter.getInstance().reset();
		//Borramos el archivo generado para dejar la aplicación como se encontraba.
		FileManager.deleteFile(RefactoringConstants.REFACTORING_PLAN_FILE);
	}
	
	
}
