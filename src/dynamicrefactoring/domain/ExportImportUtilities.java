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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.RefactoringPlanReader;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.ScopeLimitedLister;

/**
 * Proporciona una serie de métodos que se encargan de la exportación e 
 * importación de refactorizaciones dinámicas.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class ExportImportUtilities {
	
	/**
	 * Se encarga del proceso de exportación de una refactorización dinámica.
	 * 
	 * @param destination directorio a donde se quiere exportar la refactorización.
	 * @param definition ruta del fichero con la definición de la refactorización.
	 * @param createFolders indica si los ficheros .class se copian en al carpeta raíz
	 * 	      o si se generá la estructura de carpetas correspondiente.
	 * @throws IOException IOException.
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 */
	public static void ExportRefactoring(String destination, String definition, boolean createFolders) throws IOException, XMLRefactoringReaderException{
		String folder = new File(definition).getParent();
		FileManager.copyFolder(folder, destination);
		String definitionFolderName = FilenameUtils.getName(folder);
		JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp(new File(definition));
		
		for(String rule : reader.readMechanismRefactoring()){
			String className = splitGetLast(rule,".");
			
			String rulePath = rule.replace('.', File.separatorChar);
			
			//String classPath = RefactoringConstants.REFACTORING_CLASSES + "\\" + element + ".class";
			File currentFile = new File(RefactoringConstants.REFACTORING_CLASSES + File.separatorChar + rulePath + ".class");
			File destinationFile = new File (destination + File.separatorChar + definitionFolderName + File.separatorChar + className + ".class");
			File destinationFolder = new File(destination);
			File newFolder = new File( destinationFolder.getParent()+ File.separatorChar + new File(rulePath).getParent());
			File newFile = new File (new File(destination).getParent()+ File.separatorChar + rulePath + ".class");
			
			// Si no existe el destino y si el actual
			if(!destinationFile.exists() && currentFile.exists()){
				if(!createFolders){
					FileManager.copyFile(currentFile, destinationFile);
				}else{
					if(! newFolder.exists()){
						newFolder.mkdirs();
					}
					FileManager.copyFile(currentFile, newFile );
				}
			}else{
				if(! currentFile.exists()){
					//falta algún fichero .class necesario en esta refactorización
					//En este caso se borra la carpeta generada en destino ya
					//que no estará completa
					FileManager.emptyDirectories(destination + File.separatorChar + definitionFolderName);
					FileManager.deleteDirectories(destination + File.separatorChar + definitionFolderName, true);
					throw new IOException(Messages.ExportImportUtilities_ClassesNotFound);
				}
				
			}
		    
		}
		
	}

	/**
	 * Divide la cadena en partes utilizando como token delim y devuelve
	 * la última de las particiones hechas.
	 * 
	 * @param cadena Cadena a dividir
	 * @param delim Token para hacer la division
	 * @return
	 */
	public static String splitGetLast(String cadena, String delim) {
		String name = "";
		StringTokenizer st_name = new StringTokenizer(cadena,delim);
		while(st_name.hasMoreTokens()){
			name = st_name.nextElement().toString();
		}
		return name;
	}
	
	/**
	 * Se encarga del proceso de exportación de un plan de refactorizaciones dinámicas.
	 * 
	 * @param destination directorio a donde se quiere exportar el plan.
	 * @throws IOException IOException.
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 */
	public static void exportRefactoringPlan(String destination)throws IOException, XMLRefactoringReaderException{
		if(new File(destination + "/refactoringPlan").exists()){
			FileManager.emptyDirectories(destination + "/refactoringPlan");
			FileManager.deleteDirectories(destination + "/refactoringPlan",true);
		}
		//Creamos el directorio donde se guardará el plan.
		new File(destination + "/refactoringPlan").mkdir();
		//Copiamos el fichero xml que guarda la información relativa al plan.
		String planFile = new String(RefactoringConstants.REFACTORING_PLAN_FILE);
		String dtdFile = new String(RefactoringConstants.REFACTORING_PLAN_DTD);
		FileManager.copyFile(new File(RefactoringConstants.REFACTORING_PLAN_FILE)
			,new File(destination + "/refactoringPlan" 
					+ planFile.substring(planFile.lastIndexOf('/'))) );
		FileManager.copyFile(new File(RefactoringConstants.REFACTORING_PLAN_DTD)
		,new File(destination + "/refactoringPlan" 
				+ dtdFile.substring(dtdFile.lastIndexOf('/'))) );
		//Creamos una carpeta donde guardaremos las refactorizaciones.
		String refactoringDestination = destination + "/refactoringPlan/refactorings" ; 
		new File(refactoringDestination).mkdir();
		
		//Pasamos a exportar las refactorizaciones necesarias dentro de la carpeta anterior.
		ArrayList<String> refactorings = RefactoringPlanReader.readAllRefactoringsFromThePlan();
		HashMap<String, String> allRefactorings = 
			DynamicRefactoringLister.getInstance().getDynamicRefactoringNameList(
				RefactoringConstants.DYNAMIC_REFACTORING_DIR, true, null);
		
		for(String next : refactorings){
			String key = next + " (" + next + ".xml)";
			String definition = allRefactorings.get(key);//ruta del fichero de definición de al refactorización
			ExportRefactoring(refactoringDestination ,definition,true);
		}
	}
	
	/**
	 * Se encarga del proceso de importación de una refactorización dinámica.
	 * 
	 * @param definition ruta del fichero con la definición de la refactorización.
	 * @param fromPlan indica si la importación de la refactorización ha sido solicitada
	 * 	      cuando se importaba un plan de refactorizaciones.
	 * @return nombre del fichero .class que no existe, cadena vacía en caso de que todo
	 * 			funcione bien o folder_fail en caso de fallo al copiar la carpeta.
	 * @throws IOException IOException
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException
	 */
	public static String ImportRefactoring(String definition, boolean fromPlan) throws IOException, XMLRefactoringReaderException{
		File definitionFile = new File(definition);
		String folder = definitionFile.getParent();
		String namefolder = definitionFile.getParentFile().getName();
		
		if (! FileManager.copyFolder(folder, 
				RefactoringConstants.DYNAMIC_REFACTORING_DIR)){
			return "folder_fail";
		}
		
		//Pasamos a copiar los .class de las precondiciones, postcondiciones
		// y acciones en su lugar
		JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp(definitionFile);
		
		for(String predicado : reader.readMechanismRefactoring()){
			String name = splitGetLast(predicado, ".");
			String rutaPredicadoClass = predicado.replace('.', File.separatorChar) + ".class";
			String rutaDirectorioPredicadoClass = RefactoringConstants.REFACTORING_CLASSES + File.separatorChar + rutaPredicadoClass;

			if(!(new File(rutaDirectorioPredicadoClass).exists())){//En caso de que el fichero .class no se encuentre en el repositorio
				if(fromPlan){
					if(new File(new File(folder).getParentFile().getParent() + File.separatorChar + rutaPredicadoClass).exists()){							
						FileManager.copyFile(new File(new File(folder).getParentFile().getParent() + File.separatorChar + rutaPredicadoClass),new File(rutaDirectorioPredicadoClass));		
					}else{
						return name;
					}
				}else{
					if(new File(folder + File.separatorChar + name + ".class").exists()){							
						FileManager.copyFile(new File(folder + File.separatorChar + name + ".class"),new File(rutaDirectorioPredicadoClass));		
					}else{
						return name;
					}
				}
			}
		    
		}
		
		//actualizamos el fichero refactorings.xml que guarda la información de las refactorizaciones
		//de la aplicación.
		try{
			DynamicRefactoringDefinition r_definition = DynamicRefactoringDefinition.getRefactoringDefinition(
				definition);
			int scope = new ScopeLimitedLister().getRefactoringScope(r_definition);
			new dynamicrefactoring.writer.JDOMXMLRefactoringWriterImp(null).addNewRefactoringToXml(scope,namefolder,definition);
		}catch(RefactoringException e){
			e.printStackTrace();
		}
		
		if(fromPlan==false){
			//Borramos los .class de la carpeta en la que se guarda la refactorización
			//para no tener almacenada la misma información en dos sitios
			for(String element : reader.readMechanismRefactoring()){
				String name = splitGetLast(element, ".");
				if(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separatorChar + namefolder + File.separatorChar + name + ".class").exists())
					FileManager.deleteFile(RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separatorChar + namefolder + File.separatorChar + name + ".class");
			}
		}else{
			FileManager.emptyDirectories(RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separatorChar + namefolder + File.separatorChar + "repository");
			FileManager.deleteDirectories(RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separatorChar + namefolder + File.separatorChar +"repository",true);
		}
		
		return "";
	}


}
