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

import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.SuppressWarnings;
import java.nio.charset.Charset;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javamoon.regenerate.CodeVisitor;
import javamoon.regenerate.RegenerateSourceFileStrategy;
import javamoon.regenerate.SourceCode;

import moon.traverse.Visitor;

import org.apache.log4j.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;

import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.ui.PlatformUI;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.util.io.JavaFileManager;

/**
 * Permite regenerar el código de los ficheros fuente del proyecto sobre el
 * que se trabaja actualmente a partir del modelo MOON utilizado para su
 * representación durante las refactorizaciones.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class CodeRegenerator {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(CodeRegenerator.class);
	
	/**
	 * Instancia única del regenerador.
	 * 
	 * Patrón de diseño Singleton.
	 */
	private static CodeRegenerator myInstance;
	
	/**
	 * Constructor.
	 * 
	 * Privado, siguiendo la estructura del patrón de diseño Singleton.
	 */
	private CodeRegenerator(){}

	/**
	 * Obtiene la instancia única del regenerador.
	 * 
	 * Patrón de diseño Singleton.
	 * 
	 * @return la instancia única del regenerador.
	 */
	public static CodeRegenerator getInstance(){
		if (myInstance == null)
			myInstance = new CodeRegenerator();
		return myInstance;
	}
	
	/**
	 * Dirige la regeneración de código, mostrándosela al usuario a través de un
	 * diálogo de progreso de Eclipse.
	 */
	public void refreshCode() {
		
		try {
			CodeRegenerationJob job = new CodeRegenerationJob();
				
			new CustomProgressDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().
				getShell()).run(true, true, job);
			
		} 
		catch (InterruptedException e) {
			// El usuario canceló el proceso.
			String message = Messages.CodeRegenerator_UserCancelled +
				".\n"; //$NON-NLS-1$
			logger.warn(message);
		}
		catch (Exception exception){
			exception.printStackTrace();
			String message = Messages.CodeRegenerator_NotRefreshed +
				".\n" + exception.getMessage();  //$NON-NLS-1$
			logger.fatal(message);
			Logger.getRootLogger().fatal(message);	
		}
	}
	
	/**
	 * Dirige la regeneración de código.
	 */
	public void refresh() {
		
		try {
			CodeRegenerationJob job = new CodeRegenerationJob();
			job.refresh();
			
		} 
		catch (Exception exception){
			exception.printStackTrace();
			String message = Messages.CodeRegenerator_NotRefreshed +
				".\n" + exception.getMessage();  //$NON-NLS-1$
			logger.fatal(message);
			Logger.getRootLogger().fatal(message);	
		}
	}
	
	
		
	/**
	 * Coordina la regeneración del código de las clases del modelo.
	 * 
	 * Se encarga de recuperar el código de cada una de ellas y 
	 * notificar al resto del entorno de trabajo de los cambios realizados.
	 */
	private class CodeRegenerationJob implements IRunnableWithProgress{
		
		/**
		 * Ficheros fuente Java del proyecto afectado.
		 */
		ArrayList<IFile> javaFiles;
		
		/**
		 * Directorios raíz de los ficheros fuente afectados.
		 */
		List<String> sourceDirs;
		
		/**
		 * Constructor.
		 */
		public CodeRegenerationJob(){
			// Se obtienen las rutas absolutas completas de los ficheros 
			// del proyecto afectado.
			javaFiles = JavaFileManager.getJavaProjectFiles(
				RefactoringPlugin.getDefault().getAffectedJavaProject().getProject());
			sourceDirs = 
				RefactoringPlugin.getDefault().getAffectedSourceDirs();
		}
		
		/**
		 * Ejecuta la operación de recuperación de código.
		 * 
		 * @param monitor el monitor de progreso que mostrará el avance de la
		 * operación de recuperación de código.
		 * 
		 * @throws InvocationTargetException si se produce cualquier clase de
		 * excepción se relanza envuelta en una excepción de este tipo.
		 * @throws InterruptedException si el usuario interrumpió el proceso
		 * pulsando sobre el botón de cancelación.
		 */
		@Override
		public void run(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
			
			monitor.beginTask(Messages.CodeRegenerator_Regenerating, 
				3 + javaFiles.size() * 8 + sourceDirs.size());
			try {
				monitor.subTask(Messages.CodeRegenerator_ProcessingFiles);
				for (int i = 0; i < javaFiles.size(); i++){
					monitor.subTask(Messages.CodeRegenerator_ObtainingName);
					String fileName = javaFiles.get(i).getName();
					checkForCancellation(monitor);
					monitor.worked(1);
					
					monitor.subTask(Messages.CodeRegenerator_CreatingVisitor);
					SourceCode code = new SourceCode();
					Visitor visitor = new CodeVisitor(code);
					checkForCancellation(monitor);
					monitor.worked(1);
					
					monitor.subTask(Messages.CodeRegenerator_CreatingStrategy);
					RegenerateSourceFileStrategy reg = RegenerateSourceFileStrategy.getInstance();
					checkForCancellation(monitor);
					monitor.worked(1);
					
					monitor.subTask(Messages.CodeRegenerator_SettingFile +
						": " + fileName + "..."); //$NON-NLS-1$ //$NON-NLS-2$
					reg.setFileToRegenerate(fileName);
					checkForCancellation(monitor);
					monitor.worked(1);
					
					Object[] messageArgs = {fileName};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.CodeRegenerator_Traversing);
					
					monitor.subTask(formatter.format(messageArgs) + "..."); //$NON-NLS-1$
					reg.traverse(ModelGenerator.getInstance().getModel(), visitor);
					checkForCancellation(monitor);
					monitor.worked(1);
					
					formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.CodeRegenerator_ObtainingStream);
					
					monitor.subTask(formatter.format(messageArgs) + "..."); //$NON-NLS-1$
					String sourceCode = ((CodeVisitor)visitor).getCode();
					checkForCancellation(monitor);
					monitor.worked(1);
					
					monitor.subTask(Messages.CodeRegenerator_Formatting);
					sourceCode = formatCompilationUnit(sourceCode);
					ByteArrayInputStream stream = new ByteArrayInputStream(
						sourceCode.getBytes(Charset.defaultCharset()));
					checkForCancellation(monitor);
					monitor.worked(1);
										
					monitor.subTask(Messages.CodeRegenerator_SettingContents);
					javaFiles.get(i).setContents(stream, IFile.KEEP_HISTORY, 
						new NullProgressMonitor());
					javaFiles.get(i).refreshLocal(IResource.DEPTH_INFINITE,
						new NullProgressMonitor());
					monitor.worked(1);
				}
				monitor.worked(1);
				
				monitor.subTask(Messages.CodeRegenerator_Formatting);
				monitor.worked(1);
				
			}
			catch(CoreException e){
				String message = Messages.CodeRegenerator_NotRefreshed +
					".\n" + e.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
			}
			finally {
				monitor.done();
			}
		}
				
	
		/**
		 * Comprueba si un monitor de progreso ha recibido una orden de 
		 * cancelación por parte del usuario.
		 * 
		 * @param monitor el monitor cuyo estado de cancelación se comprueba.
		 * 
		 * @throws InterruptedException si el monitor ha recibido una orden de
		 * cancelación por parte del usuario.
		 */
		private void checkForCancellation(IProgressMonitor monitor) 
			throws InterruptedException {
			if(monitor.isCanceled())
				throw new InterruptedException(
					Messages.CodeRegenerator_UserCancelled +
					".\n"); //$NON-NLS-1$
		}
		
		/**
		 * Regenera el código pero no crea un diálogo de progreso.
		 */
		public void refresh(){
			
			try {
				
				for (int i = 0; i < javaFiles.size(); i++){
					
					String fileName = javaFiles.get(i).getName();
					SourceCode code = new SourceCode();
					Visitor visitor = new CodeVisitor(code);
					RegenerateSourceFileStrategy reg = RegenerateSourceFileStrategy.getInstance();
					reg.setFileToRegenerate(fileName);
					reg.traverse(ModelGenerator.getInstance().getModel(), visitor);
					String sourceCode = ((CodeVisitor)visitor).getCode();
					sourceCode = formatCompilationUnit(sourceCode);
					ByteArrayInputStream stream = new ByteArrayInputStream(
						sourceCode.getBytes(Charset.defaultCharset()));
					javaFiles.get(i).setContents(stream, IFile.KEEP_HISTORY, 
						new NullProgressMonitor());
					javaFiles.get(i).refreshLocal(IResource.DEPTH_INFINITE,
						new NullProgressMonitor());
				}
			}
			catch(CoreException e){
				String message = Messages.CodeRegenerator_NotRefreshed +
					".\n" + e.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
			}
			
			
		}

		/**
		 * Formatea el código fuente correspondiente a una clase utilizando el 
		 * API de Eclipse para el formateo de código fuente Java.
		 * 
		 * @param sourceCode el código fuente original de la clase.
		 * 
		 * @return el código fuente formateado de la clase, o el código original
		 * si se produjo un error durante el proceso.
		 */
		private String formatCompilationUnit(String sourceCode){
			@SuppressWarnings("unchecked") //$NON-NLS-1$
			Map<String, String> options =
				(Map<String, String>)DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			
			options.put(JavaCore.COMPILER_COMPLIANCE, 
				JavaCore.VERSION_1_6);
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, 
				JavaCore.VERSION_1_6);
			options.put(JavaCore.COMPILER_SOURCE, 
				JavaCore.VERSION_1_6);
			
			final CodeFormatter formatter = 
				ToolFactory.createCodeFormatter(options);
			
			final TextEdit edit = formatter.format(
				CodeFormatter.K_COMPILATION_UNIT, // Se formatea una clase.
				sourceCode, 0, sourceCode.length(), 0, 
				System.getProperty("line.separator")); //$NON-NLS-1$
			
			IDocument document = new Document(sourceCode);
			try {
				edit.apply(document);
			}
			catch (Exception e){
				return sourceCode;
			}
			return document.get();
		}
	}
}