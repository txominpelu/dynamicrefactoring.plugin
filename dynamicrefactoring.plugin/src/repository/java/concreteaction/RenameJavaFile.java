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

package repository.java.concreteaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javamoon.core.JavaFile;
import javamoon.core.JavaModel;
import moon.core.Name;
import moon.core.classdef.ClassDef;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;

import refactoring.engine.Action;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.RenamingRegistry;
import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.util.io.JavaFileManager;

/**
 * Permite renombrar el fichero Java asociado a una clase.
 * 
 * <p>Es un predicado dependiente de Eclipse, por lo que no debe aplicarse
 * en otros entornos</p>.
 *
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RenameJavaFile extends Action {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(RenameJavaFile.class);
	
	/**
	 * Receptor de los mensajes enviados por la acci�n concreta.
	 */
	private RelayListenerRegistry listenerReg;
	
	/**
	 * Nombre �nico completo de la clase definida en el fichero antes del renombrado.
	 */
	private String oldUniqueName;
	
	/**
	 * Nombre simple de la clase definida en el fichero, antes del renombrado.
	 */
	private String oldName;
	
	/**
	 * Nuevo nombre simple de la clase definida en el fichero.
	 */
	private String newShortName;
	
	/**
	 * Nuevo nombre completamente cualificado de la clase tras el renombrado.
	 */
	private String newUniqueName;
	
	/**
	 * Renamed class.
	 */
	private ClassDef classDef;
	
	/**
	 * Formato utilizado para almacenar la hora de creaci�n de una operaci�n de
	 * renombrado.
	 */
	private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
		
	/**
	 * Constructor.<p>
	 * 
	 * Obtiene una nueva instancia de la acci�n <code>RenameJavaFile</code>.
	 *
	 * @param classdef clase cuyo fichero de definici�n se debe renombrar.
	 * @param newShortName nuevo nombre asigando a la clase renombrada.
	 */
	public RenameJavaFile(ClassDef classdef, Name newShortName){
		super();
		this.classDef = classdef;		
		this.oldUniqueName = classdef.getUniqueName().toString();
		this.oldName = classdef.getName().toString();
		this.newShortName = newShortName.toString();
		
		int position = this.oldUniqueName.lastIndexOf(this.oldName);
		newUniqueName = this.oldUniqueName.substring(0, position) +
			newShortName;
		
		listenerReg = RelayListenerRegistry.getInstance();
	}
	
	/**
	 * Ejecuta el renombrado del fichero Java.
	 * 
	 * @see Action#run()
	 */
	@Override
	public void run(){	
		
		listenerReg.notify("# run():RenameJavaFile #"); //$NON-NLS-1$
		
		IUndoableOperation refactoring = new RenamingOperation(new Date());
		IOperationHistory history = OperationHistoryFactory.getOperationHistory(); 
		IUndoContext context = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		
		refactoring.addContext(context);
		
		try {
			history.execute(refactoring, null, null);
		}
		catch (ExecutionException exception){
			MOONRefactoring.resetModel();
			String message = Messages.RenameJavaFile_RenamingNotUndone +
				":\n\n" + exception.getMessage();  //$NON-NLS-1$
			logger.error(message);
		}		
	}
			
	/**
	 * Deshace el renombrado del fichero Java.
	 * 
	 * @see Action#undo()
	 */
	@Override
	public void undo(){
		listenerReg.notify("# undo():RenameJavaFile #"); //$NON-NLS-1$
		
		JavaModel model = (JavaModel)MOONRefactoring.getModel();
		
		oldUniqueName = model.getMoonFactory().createName(newUniqueName).toString();
		oldName = model.getMoonFactory().createName(newShortName).toString();
		newShortName = model.getMoonFactory().createName(oldName).toString();
		
		run();
	}
	
	/**
	 * Implementa la operaci�n de renombrado como una operaci�n capaz de
	 * ser deshecha por el entorno de Eclipse.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RenamingOperation extends AbstractOperation {
		
		/**
		 * Cadena que identifica la operaci�n de refactorizaci�n a la hora de
		 * deshacerla dentro del entorno de operaciones de Eclipse.
		 */
		private String label;
		
		/**
		 * Nombre �nico que tendr� la clase tras el renombrado.
		 */
		private String oldUnique;
		
		/**
		 * Nombre simple que tendr� la clase tras el renombrado.
		 */
		private String oldShort;
		
		/**
		 * Nombre simple dela clase anterior al renombrado.
		 */
		private String newShort;
		
		/**
		 * Nombre �nico de la clase anterior al renombrado.
		 */
		private String newUnique;
		
		/**
		 * Constructor.
		 * 
		 * @param start fecha y hora de comienzo de la operaci�n.
		 */
		public RenamingOperation(Date start){
			super("Rename " + oldName + " (" +  dateFormat.format(start) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			label = getLabel();
			
			JavaModel model = (JavaModel)MOONRefactoring.getModel();
			
			newShort = model.getMoonFactory().createName(oldName).toString();
			newUnique = model.getMoonFactory().createName(oldUniqueName).toString();
		}
		
		/**
		 * Ejecuta la operaci�n de renombrado.
		 * 
		 * @param monitor monitor de progreso (no se utiliza).
		 * @param info informaci�n del entorno gr�fico (no se utiliza).
		 * 
		 * @return el estado final tras la ejecuci�n.
		 * 
		 * @see AbstractOperation#execute(IProgressMonitor, IAdaptable)
		 */
		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info){
			try {
				final IFile sourceFile = JavaFileManager.retrieveSourceFile(
					oldUniqueName, RefactoringPlugin.getDefault().getAffectedProject());
					
				// Si se ha encontrado el fichero fuente correspondiente.
				if (sourceFile != null){
					String originalAbsolutePath = sourceFile.getLocation().toOSString();
						
					// Se construye su nuevo nombre completo �nico, que representa su ruta.
					String pathString = sourceFile.getFullPath().toString();
					int namePosition = pathString.toLowerCase().lastIndexOf(
						oldName.toLowerCase() + ".java"); //$NON-NLS-1$
						
					final String newName = pathString.substring(0, namePosition) + 
						newShortName + ".java"; //$NON-NLS-1$
						
					renameSourceFile(sourceFile, newName, originalAbsolutePath, false);
					
					RenamingRegistry.getInstance().addRenamingOperation(label);
					RenamingRegistry.getInstance().addRenaming(oldUniqueName, newUniqueName);
					
					return Status.OK_STATUS;
				}
				return Status.CANCEL_STATUS;				
			}
			catch (Exception exception){
				String message = Messages.RenameJavaFile_NotCompleted +
					".\n" + exception.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				MOONRefactoring.resetModel();
				return Status.CANCEL_STATUS;
			}
		}
	
		/**
		 * Deshace la operaci�n de renombrado.
		 * 
		 * @param monitor monitor de progreso (no se utiliza).
		 * @param info informaci�n del entorno gr�fico (no se utiliza).
		 * 
		 * @return el estado final tras la ejecuci�n.
		 * 
		 * @see AbstractOperation#undo(IProgressMonitor, IAdaptable)
		 */
		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info){
			try {
				
				oldUnique = RenamingRegistry.getInstance().update(newUnique);
				oldShort = oldUnique.substring(oldUnique.lastIndexOf('.') + 1);
				
				final IFile sourceFile = JavaFileManager.retrieveSourceFile(
					oldUnique, RefactoringPlugin.getDefault().getAffectedProject());
					
				// Si se ha encontrado el fichero fuente correspondiente.
				if (sourceFile != null){
					String originalAbsolutePath = sourceFile.getLocation().toOSString();
						
					// Se construye su nuevo nombre completo �nico, que representa su ruta.
					String pathString = sourceFile.getFullPath().toString();
					int namePosition = pathString.toLowerCase().lastIndexOf(
						oldShort.toLowerCase() + ".java"); //$NON-NLS-1$
						
					final String newName = pathString.substring(0, namePosition) + 
						newShort + ".java"; //$NON-NLS-1$
						
					renameSourceFile(sourceFile, newName, originalAbsolutePath, true);
					
					return Status.OK_STATUS;
				}
				return Status.CANCEL_STATUS;				
			}
			catch (Exception exception){
				String message = Messages.RenameJavaFile_NotUndone +
					".\n" + exception.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				return Status.CANCEL_STATUS;
			}
		}
	
		/**
		 * Sin implementaci�n.
		 * 
		 * @param monitor monitor de progreso (no se utiliza).
		 * @param info informaci�n del entorno gr�fico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS}
		 * 
		 * @see AbstractOperation#redo(IProgressMonitor, IAdaptable)
		 */
		public IStatus redo(IProgressMonitor monitor, IAdaptable info){
			return Status.OK_STATUS;
		}
		
		/**
		 * Determina si la operaci�n se puede rehacer una vez deshecha. En 
		 * este caso, no se puede.
		 * 
		 * @return <code>false</code>.
		 */
		@Override
		public boolean canRedo(){
			return false;
		}
	
		/**
		 * Renombra el fichero fuente que contiene una clase.
		 *
		 * @param file el fichero que se debe renombrar. 
		 * @param newName el nuevo nombre completo del fichero.
		 * @param originalPath la ruta absoluta original del fichero.
		 * @param undo si se est� desaciendo un renombrado o no.
		 */
		private void renameSourceFile(final IFile file, final String newName, 
			String originalPath, boolean undo) {
					
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
					
			IWorkspaceRunnable myRunnable = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					file.move(new Path(newName), true, monitor);					
				}
			};
		
			try {
				workspace.run(myRunnable, file.getProject(),
					IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
					
				JavaModel model = (JavaModel)MOONRefactoring.getModel();
				if (model == null)
					model = ModelGenerator.getInstance().getModel();
					
				JavaFile oldFile = model.getJavaFile(
					model.getMoonFactory().createName(originalPath));
				
				// Se elimina el fichero con el nombre antiguo del modelo.
				model.remove(oldFile);
				
				IFile newSourceFile = null;
				if (! undo)
					// Se recupera el recurso de Eclipse asociado al fichero renombrado.
					newSourceFile = JavaFileManager.retrieveSourceFile(
						oldUniqueName.replaceAll(oldName, newShortName), 
						RefactoringPlugin.getDefault().getAffectedProject());
				else
					newSourceFile = JavaFileManager.retrieveSourceFile(
						oldUnique.replaceAll(oldShort, newShort), 
						RefactoringPlugin.getDefault().getAffectedProject());
								
				oldFile.setName(model.getMoonFactory().createName(
					newSourceFile.getName()));
				
				// Se a�ade el fichero renombrado al modelo.
				model.add(oldFile);
			}
			catch (CoreException e){
				String message = Messages.RenameJavaFile_NotRenamed +
					".\n" + e.getLocalizedMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
			}
		}
	}
}