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

package dynamicrefactoring.integration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;

import javamoon.construct.binary.BinaryLoader;
import javamoon.construct.source.SourceLoader;
import javamoon.core.JavaModel;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.util.io.JavaFileManager;
import dynamicrefactoring.util.selection.SelectionInfo;

/**
 * Permite generar el modelo MOON correspondiente al proyecto Java sobre el
 * que se trabaja en un momento determinado.
 * 
 * <p>Determina qu� bibliotecas b�sicas del API de Java formar�n parte del
 * modelo, y proporciona funciones de acceso al modelo MOON sobre el que se
 * trabaja en un instante determinado. 
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ModelGenerator {

	/**
	 * El nombre con el que se crea por defecto el fichero .mod que contendr�
	 * el modelo MOON-Java obtenido a partir de los fuentes .java.
	 */
	public final static String DEFAULT_MOD_NAME = "out.mod"; //$NON-NLS-1$
	
	/**
	 * La ruta del fichero "rt.jar" de Java, relativa a la ra�z del directorio de 
	 * instalaci�n de Java que utilice un proyecto.
	 */
	private final static String BASIC_JAR =  
		File.separatorChar + "lib" + //$NON-NLS-1$ //$NON-NLS-2$
		File.separatorChar + "rt.jar"; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Extensi�n de los ficheros de bibliotecas JAR.
	 */
	private final static String LIB_EXTENSION = ".jar"; //$NON-NLS-1$
	
	/**
	 * Bibliotecas de Java que se cargan por defecto con el modelo.
	 */
	private final static String[] LIBRARIES = {"java.lang", "java.util", //$NON-NLS-1$ //$NON-NLS-2$
		"java.io", "java.lang.annotation"}; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(ModelGenerator.class);
	
	/**
	 * Proyecto Java cuyo modelo MOON se genera.
	 */
	private IJavaProject project;

	/**
	 * Instancia �nica del generador.
	 * 
	 * Patr�n de dise�o Singleton.
	 */
	private static ModelGenerator myInstance;
	
	/**
	 * Constructor.
	 * 
	 * Privado, seg�n la estructura del patr�n de dise�o Singleton.
	 */
	private ModelGenerator(){}

	/**
	 * Obtiene la instancia �nica del generador.
	 * 
	 * Patr�n de dise�o Singleton.
	 * 
	 * @return la instancia �nica del generador.
	 */
	public static ModelGenerator getInstance(){
		if (myInstance == null)
			myInstance = new ModelGenerator();
		return myInstance;
	}

	/**
	 * Genera el modelo MOON.
	 * 
	 * @param info proveedor de informaci�n sobre el elemento que se 
	 * encuentre actualmente seleccionado en Eclipse, y que servir� para 
	 * determinar el proyecto a partir del que se generar� el modelo MOON.
	 * 
	 * @param createwindow indica si se quiere mostrar al usuario una ventana
	 * indicando el proceso de generaci�n del modelo o no.
	 * 
	 * @param notFromSelection indica si el modelo se gener� a partir de un elemento 
	 * seleccionado o no.
	 * 
	 * @return <code>true</code> si se pudo generar el modelo correctamente; 
	 * <code>false</code> en caso contrario.
	 */
	public boolean generateModel(SelectionInfo info, boolean createwindow, boolean notFromSelection){
		
		try {
			if(notFromSelection==true){
				RefactoringPlugin.getDefault().getAffectedProject();//Establece el java project
				project=RefactoringPlugin.getDefault().getAffectedJavaProject();
			}else{
				project = info.getJavaProjectForSelection();
			}
			
			if(createwindow){
				GenerationJob job = new GenerationJob(JavaFileManager.getSourceDirsForProject(project));
				new CustomProgressDialog(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().
					getShell()).run(true, true, job);
			}else{//Carga el modelo sin mostrar una ventana de progreso
				JavaModel.getInstance().reset();
				loadBasicLibraries();
				SourceLoader sourceLoader = new SourceLoader();
				for(String directory : JavaFileManager.getSourceDirsForProject(project)){
					sourceLoader.loadFromDirectory(directory);
				}
				JavaModel.save(DEFAULT_MOD_NAME);	
			}
			
			return true;
		} 
		catch (InterruptedException e) {
			// El usuario cancel� el proceso.			 
			logger.warn(e.getMessage());
		}
		catch (Exception exception){
			String message = Messages.ModelGenerator_NotGenerated + ".\n" //$NON-NLS-1$
				+ exception.getMessage(); 
			logger.fatal(message);
			Logger.getRootLogger().fatal(message);	
		}
		
		return false;
	}
	
	/**
	 * Carga el modelo MOON guardado en el fichero por defecto.
	 * 
	 * @see ModelGenerator#DEFAULT_MOD_NAME
	 */
	public void loadMOONModel(){
	
		try {
			JavaModel.load(DEFAULT_MOD_NAME);
		}
		catch (Exception exception){
			String message = Messages.ModelGenerator_NotLoaded +
				".\n" + exception.getLocalizedMessage(); //$NON-NLS-1$
			logger.error(message);
			Logger.getRootLogger().error(message);
		}
	}

	/**
	 * Realiza la carga de bibliotecas b�sicas del API de Java.
	 * 
	 * Es un paso previo necesario para poder utilizar las clases de dichos
	 * paquetes en las clases a partir de las cuales se quiere generar un modelo.
	 * 
	 * @throws Exception si se produce un error al acceder a los ficheros JAR
	 * que se supone contienen las bibliotecas.
	 */
	private void loadBasicLibraries() throws Exception {
		
		String JRE_root = JavaRuntime.getVMInstall(project).
			getInstallLocation().getAbsolutePath();
		
		BinaryLoader binaryLoader = new BinaryLoader();
		
		String rtPath = null;

		IClasspathEntry[] classpath = project.getResolvedClasspath(true);
		for (int i = 0; i < classpath.length; i++){
			if (classpath[i].getEntryKind() == IClasspathEntry.CPE_LIBRARY &&
				classpath[i].getContentKind() == IPackageFragmentRoot.K_BINARY){
				IPath path = classpath[i].getPath();
				if (path.toOSString().endsWith(BASIC_JAR))
					rtPath = path.toOSString();
				// Se a�aden el resto de bibliotecas de forma normal.
				else if (path.toOSString().toLowerCase().endsWith(LIB_EXTENSION))
					// Salvo las que pertenezcan al JRE o el JDK.
					if (! path.toOSString().startsWith(JRE_root)){
						// Si la ruta no es relativa al proyecto.
						if(! path.toOSString().startsWith(project.getPath().toOSString()))
							binaryLoader.addClassesFromJar(path.toOSString());
						// Para las rutas relativas al proyecto.
						else {
							String relativePath = path.removeFirstSegments(1).toOSString();
							if (project.getJavaProject().getResource() != null){
								String projectPath = 
									project.getJavaProject().getResource().getLocation().toOSString();
								binaryLoader.addClassesFromJar(
									projectPath + File.separatorChar + //$NON-NLS-1$
									relativePath);
							}
						}
					}
			}
		}
		
		if (rtPath == null)			
			rtPath = JRE_root + File.separatorChar + BASIC_JAR; //$NON-NLS-1$
		
		binaryLoader.addClassesFromJar(rtPath);
		
//		for (int i = 0; i < LIBRARIES.length; i++)
//			binaryLoader.addClassesFromPackageInJar(LIBRARIES[i], rtPath);
		
		binaryLoader.load();
	}
	
	/**
	 * Obtiene el modelo MOON cargado en el momento de la invocaci�n.
	 * 
	 * @return el modelo MOON cargado en el momento de la invocaci�n.
	 */
	public JavaModel getModel(){
		return JavaModel.getInstance();
	}

	/**
	 * Permite lanzar el trabajo de generaci�n del modelo JavaMOON y hacer un
	 * seguimiento de su progreso.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class GenerationJob implements IRunnableWithProgress{
		
		/**
		 * Directorios que contienen los ficheros fuente Java a partir de los que
		 * se generar� el modelo JavaMOON. 
		 */
		List<String> sourceDirectories;
		
		/**
		 * Constructor.
		 * 
		 * @param sourceDirectories directorios que contienen los ficheros fuente
		 * Java a partir de los que se generar� el modelo JavaMOON.
		 */
		public GenerationJob(List<String> sourceDirectories){
			this.sourceDirectories = sourceDirectories;
		}

		/**
		 * Ejecuta el trabajo de generaci�n del modelo MOON.
		 * 
		 * @param monitor el monitor de progreso que deber� usarse para mostrar
		 * el progreso y recibir solicitudes de cancelaci�n.
		 * 
		 * @throws InvocationTargetException utilizada como envoltura si el m�todo 
		 * debe propagar una excepci�n (<i>checked exception</i>). Las excepciones
		 * de tipo <i>runtime exception</i> se envuelven autom�ticamente en una
		 * excepci�n de este tipo por el contexto que efect�a la llamada.
		 * @throws InterruptedException si la operaci�n detecta una solicitud de 
		 * cancelaci�n.
		 * 
		 * @see IRunnableWithProgress#run(IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor) throws 
			InvocationTargetException, InterruptedException {
			
			monitor.beginTask(Messages.ModelGenerator_BeginningGeneration, 2);
			try {
				monitor.subTask(Messages.ModelGenerator_Resetting);
				JavaModel.getInstance().reset();
				checkForCancellation(monitor);
				monitor.worked(1);				
			
				monitor.subTask(Messages.ModelGenerator_Libraries);
				loadBasicLibraries();
				checkForCancellation(monitor);
				monitor.worked(1);				
		
				monitor.beginTask(Messages.ModelGenerator_Generating, 
					3 + sourceDirectories.size());
				monitor.subTask(Messages.ModelGenerator_CreatingLoader);
				SourceLoader sourceLoader = new SourceLoader();
				checkForCancellation(monitor);
				monitor.worked(1);
		
				monitor.subTask(Messages.ModelGenerator_LoadingSource);
				for(String directory : sourceDirectories){
					Object[] messageArgs = {directory};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.ModelGenerator_LoadingFiles + "..."); //$NON-NLS-1$
					
					monitor.subTask(formatter.format(messageArgs));
					sourceLoader.loadFromDirectory(directory);
					checkForCancellation(monitor);
					monitor.worked(1);
				}
				checkForCancellation(monitor);
				monitor.worked(1);
						
				monitor.subTask(Messages.ModelGenerator_Saving);
				JavaModel.save(DEFAULT_MOD_NAME);
				checkForCancellation(monitor);
				monitor.worked(1);
			}
			catch (Exception exception){
				String message = Messages.ModelGenerator_NotGenerated +
					".\n" + exception.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				
				if(exception instanceof InterruptedException)
					throw (InterruptedException)exception;
				else
					throw new InvocationTargetException(exception);
			}
			finally{
				monitor.done();
			}
		}

		/**
		 * Comprueba si un monitor de progreso ha recibido una orden de 
		 * cancelaci�n por parte del usuario.
		 * 
		 * @param monitor el monitor cuyo estado de cancelaci�n se comprueba.
		 * 
		 * @throws InterruptedException si el monitor ha recibido una orden de
		 * cancelaci�n por parte del usuario.
		 */
		private void checkForCancellation(IProgressMonitor monitor) 
			throws InterruptedException {
			if(monitor.isCanceled())
				throw new InterruptedException(
					Messages.ModelGenerator_UserCancelled + ".\n"); //$NON-NLS-1$
		}
	}
}