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

package dynamicrefactoring.util.io;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Proporciona funciones que permiten manejar los ficheros que componen un 
 * proyecto Java en Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JavaFileManager {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(JavaFileManager.class);
	
	/**
	 * Extensión esperada para los ficheros fuente Java.
	 */
	private static final String JAVA_FILE_EXTENSION = "java"; //$NON-NLS-1$
	
	/**
	 * Tipo de contenido asociado a los ficheros fuente Java.
	 */
	public static final String JAVA_CONTENT_TYPE = "org.eclipse.jdt.core.javaSource"; //$NON-NLS-1$

	/**
	 * Recupera los ficheros fuente Java de todos los proyectos abiertos en el espacio de trabajo.
	 * 
	 * @return los ficheros fuente Java de los proyectos abiertos en el espacio de trabajo.
	 */
	public static ArrayList<String> getOpenSourceFiles(){
		
		ArrayList<String> sourceFilePaths = new ArrayList<String>();
		
		IProject projects[] = retrieveProjectList();
		
		for (int i = 0; i < projects.length; i++){
			sourceFilePaths.addAll(getJavaProjectFilesNames(projects[i]));
		}
		
		return sourceFilePaths;
	}
	
	/** 
	 * Obtiene el conjunto de proyectos disponibles en el espacio de trabajo.
	 * 
	 * @return el conjunto de proyectos disponibles en el espacio de trabajo.
	 */
	private static IProject[] retrieveProjectList(){
		IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
		return wsroot.getProjects();		
	}

	/**
	 * Obtiene los nombres de todos los ficheros fuente Java de un proyecto.
	 * 
	 * @param project el proyecto de cuyos ficheros fuente se quieren obtener los nombres.
	 * 
	 * @return los nombrse de todos los ficheros fuente Java del proyecto #project. 
	 */
	public static ArrayList<String> getJavaProjectFilesNames(IProject project){
		ArrayList<IFile> files = getJavaProjectFiles(project);
		
		if (files != null){
			IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
			ArrayList<String> names = new ArrayList<String>();
			for (int i = 0; i < files.size(); i++)
				names.add(wsroot.getLocation().toOSString() +
					files.get(i).getFullPath().toOSString());
			return names;
		}
		return null;
	}
	
	/**
	 * Obtiene el conjunto de ficheros fuente Java de un proyecto.
	 * 
	 * @param project el proyecto cuyos ficheros fuente se quieren obtener.
	 * 
	 * @return el conjunto de ficheros fuente Java del proyecto #project. 
	 */
	public static ArrayList<IFile> getJavaProjectFiles(IProject project){
		if (project.exists() && project.isOpen()){
			try {
				ArrayList<IFile> projectFiles = new ArrayList<IFile>();
				
				IResource resources[] = project.members();
				
				for (int j = 0; j < resources.length; j++){
					if (resources[j].exists()){
						if (resources[j] instanceof IFolder)
							getFolderFiles((IFolder) resources[j], projectFiles);
						else
							addJavaFileToList(resources[j], projectFiles);							
					}
				}
				
				return projectFiles;
			}
			catch (CoreException e){
				String message = ""; //$NON-NLS-1$
				if (project != null){
					Object[] messageArgs = {project.getName()};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(
						Messages.JavaFileManager_FilesNotRetrieved);
					
					message = formatter.format(messageArgs) + ". " + e.getLocalizedMessage(); //$NON-NLS-1$
				}
				else
					message = Messages.JavaFileManager_ProjectFilesNotRetrieved +
						": " + e.getLocalizedMessage(); //$NON-NLS-1$
				
				logger.error(message);
				
				return null;
			}
		}
		return null;
	}

	/**
	 * Obtiene el fichero fuente Java que representa la mejor coincidencia para un
	 * cierto nombre único de clase.
	 * 
	 * Busca entre todos los ficheros Java del proyecto aquél cuyo nombre 
	 * completamente cualificado sea la mejor coincidencia para el nombre único de
	 * clase determinado por #MOONUniqueName en el formato utilizado en MOON.
	 * 
	 * @param MOONUniqueName el nombre único de la clase cuyo fichero fuente se 
	 * busca. Debe estar en el formato utilizado por el modelo MOON, donde el 
	 * carácter <code>'.'</code> se utiliza como separador entre los nombres de los 
	 * paquetes y el de la clase. No debe incluir la extensión <code>'.java'</code>.
	 * @param project el proyecto en el que se buscará el fichero fuente de la clase.
	 * 
	 * @return el fichero fuente Java que represente la mejor coincidencia para un
	 * cierto nombre único de clase. <code>null</code> si no se encuentra ninguna 
	 * coincidencia.
	 */
	public static IFile retrieveSourceFile(String MOONUniqueName, IProject project) {
			
		ArrayList<IFile> javaFiles = getJavaProjectFiles(project);
		
		// Las clases sin paquete en MOON se asignan al paquete por
		// defecto "<anonymous>". Se elimina esta asignación.
		if (MOONUniqueName.startsWith("<anonymous>")) //$NON-NLS-1$
			MOONUniqueName = MOONUniqueName.substring(
				MOONUniqueName.indexOf("<anonymous>") + 12);  //$NON-NLS-1$
			
		int bestMatch = -1;
		int bestFile = -1;
			
		for (int i = 0; i < javaFiles.size(); i++){			
			IFile nextFile = javaFiles.get(i);
			IPath nextPath = nextFile.getProjectRelativePath().removeFileExtension();
				
			String MOONequivalent = nextPath.toString().replace('/', '.');
				
			if (MOONequivalent.endsWith(MOONUniqueName)){
				for (int j = 0; j < nextPath.segments().length; j++){
					nextPath = nextPath.removeFirstSegments(1);
					if (nextPath.toString().replace('/', '.').equals(MOONUniqueName)){
						if (bestMatch == -1 || j < bestMatch){
							bestMatch = j;
							bestFile = i;
							break;
						}
					}
				}
			}
		}
			
		// Si se ha encontrado el fichero fuente correspondiente.
		if (bestFile != -1){
			IFile sourceFile = javaFiles.get(bestFile);
			return sourceFile;
		}
		
		return null;
	}
	
	/**
	 * Añade un recurso de tipo fichero a una lista de ficheros fuente Java.
	 * Se encarga de verificar que la extensión del fichero es la adecuada, según el
	 * contenido de #JAVA_FILE_EXTENSION, así como de comprobar que el tipo de 
	 * contenido es código fuente Java.
	 * 
	 * @param resourceFile el recurso del espacio de trabajo que se quiere añadir.
	 * @param sourceFileList la lista de ficheros fuente Java a la que se debe añadir.
	 * 
	 * @throws CoreException si se produce un error al acceder al tipo de 
	 * contenido del fichero que se desea añadir.
	 */
	private static void addJavaFileToList(IResource resourceFile, 
		ArrayList<IFile> sourceFileList) throws CoreException{
		
		if (resourceFile.exists() && resourceFile instanceof IFile)
			if (((IFile)resourceFile).getFileExtension().equalsIgnoreCase(JAVA_FILE_EXTENSION)){
				IFile file = (IFile) resourceFile;
				
				if (file.getContentDescription().getContentType().equals(
					Platform.getContentTypeManager().getContentType(
						JAVA_CONTENT_TYPE)))					
					sourceFileList.add(file);
			}
	}

	/**
	 * Completa la lista de ficheros fuente Java existentes en un directorio.
	 * Recorre recursivamente el directorio y sus subdirectorios y para cada fichero
	 * Java que encuentra, añade su ruta relativa a la raíz del proyecto a la lista
	 * de rutas de ficheros fuente.
	 *  
	 * @param folder el directorio a partir del cual se busca.
	 * @param sourceFileList la lista de rutas relativas de los ficheros Java.
	 */	
	private static void getFolderFiles (IFolder folder, 
		ArrayList<IFile> sourceFileList){

		if (folder.exists()){
			try {
				IResource resources[] = folder.members();
				
				for (int i = 0; i < resources.length; i++){
					if (resources[i].exists() && resources[i] instanceof IFolder)
						getFolderFiles((IFolder)resources[i], sourceFileList);
					else
						addJavaFileToList(resources[i], sourceFileList);
				}
			}
			catch (CoreException e){
				String message = ""; //$NON-NLS-1$
				if (folder != null){
					Object[] messageArgs = {folder.getName()};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(
						Messages.JavaFileManager_FilesInFolderNotRetrieved);
					message = formatter.format(messageArgs) +
						". " + e.getLocalizedMessage(); //$NON-NLS-1$
				}
				else
					message = Messages.JavaFileManager_FolderFilesNotRetrieved +
						": " + e.getLocalizedMessage(); //$NON-NLS-1$
				
				logger.error(message);
			}
		}
	}

	/**
	 * Obtiene la lista de directorios de fuentes del proyecto seleccionado.
	 * 
	 * @param project proyecto cuyos directorios de fuentes se deben obtener.
	 * 
	 * @return la lista de directorios de fuentes del proyecto seleccionado.
	 */
	public static List<String> getSourceDirsForProject(IJavaProject project){
		
		try {
			if (project != null){
				IPackageFragmentRoot[] dirs = 
					project.getAllPackageFragmentRoots();
				List<String> dirPaths = new ArrayList<String>();
				for(int i = 0; i < dirs.length; i++)
					if ( dirs[i].getKind() == IPackageFragmentRoot.K_SOURCE &&
						!dirs[i].isArchive())
						dirPaths.add(
							dirs[i].getResource().getLocation().toOSString());
				return dirPaths;
			}
		}
		catch (JavaModelException exception){
			logger.error(Messages.JavaFileManager_PackageRootNotFound +
				"." + exception.getLocalizedMessage()); //$NON-NLS-1$
		}
		return null;
	}
}