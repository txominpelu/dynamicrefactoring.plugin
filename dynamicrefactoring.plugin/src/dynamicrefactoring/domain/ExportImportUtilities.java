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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.RefactoringPlanReader;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.ScopeLimitedLister;
import dynamicrefactoring.util.io.FileManager;

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
	 * @param destination
	 *            directorio a donde se quiere exportar la refactorización.
	 * @param definition
	 *            ruta del fichero con la definición de la refactorización.
	 * @param createFolders
	 *            indica si los ficheros .class se copian en al carpeta raíz o
	 *            si se generá la estructura de carpetas correspondiente.
	 * @throws IOException
	 *             IOException.
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 */
	public static void ExportRefactoring(String destination, String definition, boolean createFolders) throws IOException, XMLRefactoringReaderException{
		String folder = new File(definition).getParent();
		FileManager.copyFolder(folder, destination);
		String definitionFolderName = FilenameUtils.getName(folder);
		JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp(new File(definition));
		
		for(String rule : reader.readMechanismRefactoring()){
			String className = splitGetLast(rule, "."); //$NON-NLS-1$
			
			String rulePath = rule.replace('.', File.separatorChar);
			

			File currentFile = new File(
					RefactoringConstants.REFACTORING_CLASSES_DIR
							+ File.separatorChar + rulePath + ".class"); //$NON-NLS-1$
			File destinationFile = new File(destination + File.separatorChar
					+ definitionFolderName + File.separatorChar + className
					+ ".class"); //$NON-NLS-1$
			File destinationFolder = new File(destination);
			File newFolder = new File( destinationFolder.getParent()+ File.separatorChar + new File(rulePath).getParent());
			File newFile = new File(new File(destination).getParent()
					+ File.separatorChar + rulePath + ".class"); //$NON-NLS-1$
			
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
					// falta algún fichero .class necesario en esta
					// refactorización
					//En este caso se borra la carpeta generada en destino ya
					// que no estará completa
					FileManager.emptyDirectories(destination + File.separatorChar + definitionFolderName);
					FileManager.deleteDirectories(destination + File.separatorChar + definitionFolderName, true);
					throw new IOException(
							Messages.ExportImportUtilities_ClassesNotFound
									+ currentFile.getPath());
				}
				
			}
		    
		}
		
	}

	/**
	 * Divide la cadena en partes utilizando como token delim y devuelve la
	 * última de las particiones hechas.
	 * 
	 * @param cadena
	 *            Cadena a dividir
	 * @param delim
	 *            Token para hacer la division
	 * @return
	 */
	public static String splitGetLast(String cadena, String delim) {
		String name = ""; //$NON-NLS-1$
		StringTokenizer st_name = new StringTokenizer(cadena,delim);
		while(st_name.hasMoreTokens()){
			name = st_name.nextElement().toString();
		}
		return name;
	}

	/**
	 * Se encarga del proceso de exportación de un plan de refactorizaciones
	 * dinámicas.
	 * 
	 * @param destination
	 *            directorio a donde se quiere exportar el plan.
	 * @throws IOException
	 *             IOException.
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException.
	 */
	public static void exportRefactoringPlan(String destination)throws IOException, XMLRefactoringReaderException{
		if (new File(destination + "/refactoringPlan").exists()) { //$NON-NLS-1$
			FileManager.emptyDirectories(destination + "/refactoringPlan"); //$NON-NLS-1$
			FileManager.deleteDirectories(
					destination + "/refactoringPlan", true); //$NON-NLS-1$
		}
		// Creamos el directorio donde se guardará el plan.
		new File(destination + "/refactoringPlan").mkdir(); //$NON-NLS-1$
		// Copiamos el fichero xml que guarda la información relativa al plan.
		String planFile = new String(RefactoringConstants.REFACTORING_PLAN_FILE);
		String dtdFile = new String(RefactoringConstants.REFACTORING_PLAN_DTD);
		FileManager.copyFile(new File(RefactoringConstants.REFACTORING_PLAN_FILE)
, new File(
				destination + "/refactoringPlan" //$NON-NLS-1$
					+ planFile.substring(planFile.lastIndexOf('/'))) );
		FileManager.copyFile(new File(RefactoringConstants.REFACTORING_PLAN_DTD)
, new File(
						destination + "/refactoringPlan" //$NON-NLS-1$
				+ dtdFile.substring(dtdFile.lastIndexOf('/'))) );
		//Creamos una carpeta donde guardaremos las refactorizaciones.
		String refactoringDestination = destination
				+ "/refactoringPlan/refactorings"; //$NON-NLS-1$
		new File(refactoringDestination).mkdir();
		
		//Pasamos a exportar las refactorizaciones necesarias dentro de la carpeta anterior.
		ArrayList<String> refactorings = RefactoringPlanReader.readAllRefactoringsFromThePlan();
		HashMap<String, String> allRefactorings = 
			DynamicRefactoringLister.getInstance().getDynamicRefactoringNameList(
				RefactoringPlugin.getDynamicRefactoringsDir(), true, null);
		
		for(String next : refactorings){
			String key = next + " (" + next + ".xml)"; //$NON-NLS-1$ //$NON-NLS-2$
			String definition = allRefactorings.get(key);// ruta del fichero de
															// definición de al
															// refactorización
			ExportRefactoring(refactoringDestination ,definition,true);
		}
	}

	/**
	 * Se encarga del proceso de importación de una refactorización dinámica.
	 * 
	 * @param definition
	 *            ruta del fichero con la definición de la refactorización.
	 * @param importingFromPlan
	 *            indica si la importación de la refactorización ha sido
	 *            solicitada cuando se importaba un plan de refactorizaciones.
	 * @throws IOException
	 *             IOException en caso de fallo al copiar la carpeta
	 * @throws XMLRefactoringReaderException
	 *             XMLRefactoringReaderException
	 */
	public static void ImportRefactoring(String definition,
			boolean importingFromPlan) throws IOException,
			XMLRefactoringReaderException {
		File definitionFile = new File(definition);
		final String originalFolder = definitionFile.getParent();
		String namefolder = definitionFile.getParentFile().getName();
		
		FileUtils.copyDirectoryToDirectory(new File(originalFolder), new File(
				RefactoringPlugin.getDynamicRefactoringsDir()));
		
		//Pasamos a copiar los .class de las precondiciones, postcondiciones
		// y acciones en su lugar
		JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp(definitionFile);
		
		for(String predicado : reader.readMechanismRefactoring()){
				copyRefactoringFileClassIfNeeded(importingFromPlan, originalFolder,
						predicado);
		}
		
		// actualizamos el fichero refactorings.xml que guarda la información de
		// las refactorizaciones
		// de la aplicación.
		updateRefactoringsXml(definition, namefolder);

		if (importingFromPlan) {
			FileManager
					.emptyDirectories(RefactoringPlugin.getDynamicRefactoringsDir()
							+ File.separatorChar
							+ namefolder
							+ File.separatorChar + "repository");
			FileManager.deleteDirectories(
					RefactoringPlugin.getDynamicRefactoringsDir()
							+ File.separatorChar + namefolder
							+ File.separatorChar + "repository", true);

		} else {
			// Borramos los .class para no tener almacenada la misma información
			// en dos sitios
			deleteClassFilesFromRefactoringsDir(namefolder, reader);
		}
	}

	/**
	 * Copia el fichero .class asociado con una refactorizacion al directorio
	 * que le corresponde al importarlo.
	 * 
	 * @param importingFromPlan
	 *            si se importa de un plan o no
	 * @param originalFolder
	 *            carpeta en la que se encuentra la definicion de la
	 *            refactorizacion
	 * @param predicateName
	 *            nombre del predicado
	 * @throws IOException
	 *             si la carpeta origen no existe
	 */
	private static void copyRefactoringFileClassIfNeeded(boolean importingFromPlan,
			final String originalFolder, String predicateName)
			throws IOException {
		final String rutaDirectorioPredicadoClass = RefactoringConstants.REFACTORING_CLASSES_DIR
				+ File.separatorChar
				+ predicateName.replace('.', File.separatorChar) + ".class";
		if (!(new File(rutaDirectorioPredicadoClass).exists())) {
			final File originalFile = (importingFromPlan ? getClassFileFromPlanBinDir(
					originalFolder, predicateName) : getClassFileFromBinDir(
					originalFolder, predicateName));
			if (originalFile.exists()) {
				FileManager.copyFile(originalFile, new File(
						rutaDirectorioPredicadoClass));

			} else {
				throw new FileNotFoundException(Messages.FileNotFoundMessage0
						+ originalFile.getPath());
			}
		}
	}


	/**
	 * Actualiza el fichero con las nuevas refactorizaciones.
	 * 
	 * @param definition
	 * @param namefolder
	 */
	private static void updateRefactoringsXml(String definition,
			String namefolder) {
		try{
			DynamicRefactoringDefinition r_definition = DynamicRefactoringDefinition.getRefactoringDefinition(
				definition);
			int scope = new ScopeLimitedLister().getRefactoringScope(r_definition);
			new dynamicrefactoring.writer.JDOMXMLRefactoringWriterImp(null).addNewRefactoringToXml(scope,namefolder,definition);
		}catch(RefactoringException e){
			e.printStackTrace();
		}
	}

	/**
	 * Elimina los ficheros de clase del directorio de refactorizaciones
	 * (DynamicRefactorings).
	 * 
	 * @param namefolder
	 * @param reader
	 */
	private static void deleteClassFilesFromRefactoringsDir(String namefolder,
			JDOMXMLRefactoringReaderImp reader) {
		for (String element : reader.readMechanismRefactoring()) {
			String name = splitGetLast(element, "."); //$NON-NLS-1$
			if (new File(RefactoringPlugin.getDynamicRefactoringsDir()
					+ File.separatorChar + namefolder + File.separatorChar
					+ name + ".class").exists()) //$NON-NLS-1$
				FileManager
						.deleteFile(RefactoringPlugin.getDynamicRefactoringsDir()
								+ File.separatorChar
								+ namefolder
								+ File.separatorChar + name + ".class"); //$NON-NLS-1$
		}
	}

	/**
	 * Obtiene el fichero de clase de una refactorizacion que proviene de un
	 * plan de refactorizaciones.
	 * 
	 * Estos ficheros se encuentran en el directorio de binarios de un plan de
	 * refactorizaciones.
	 * 
	 * @param originalFolder
	 * @param predicateName
	 * @return
	 */
	private static File getClassFileFromPlanBinDir(final String originalFolder,
			final String predicateName) {
		
		return new File(new File(originalFolder).getParentFile().getParent()
				+ File.separatorChar
				+ predicateName.replace('.', File.separatorChar) + ".class");
	}

	/**
	 * Obtiene el fichero de clase de una refactorizacion que proviene de un
	 * plan de refactorizaciones.
	 * 
	 * Estos ficheros se encuentran en el directorio de binarios de un plan de
	 * refactorizaciones.
	 * 
	 * @param originalFolder
	 * @param predicateName
	 * @return
	 */
	private static File getClassFileFromBinDir(final String originalFolder,
			String predicateName) {
		final String name = splitGetLast(predicateName, ".");
		return new File(originalFolder + File.separatorChar + name + ".class");
	}


}
