/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente

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

package dynamicrefactoring;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javamoon.core.JavaModel;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import refactoring.engine.PostconditionException;
import refactoring.engine.PreconditionException;
import refactoring.engine.Refactoring;
import repository.moon.MOONRefactoring;
import dynamicrefactoring.domain.RefactoringSummary;
import dynamicrefactoring.domain.xml.writer.RefactoringPlanWriter;
import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.interfaz.dynamic.DynamicRefactoringRunner;

/**
 * Permite ejecutar una operaci�n de refactorizaci�n, mostrando al usuario el
 * progreso de la operaci�n y permitiendo su cancelaci�n hasta cierto momento de
 * la refactorizaci�n.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class RefactoringRunner implements IRunnableWithProgress {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(RefactoringRunner.class);
	
	/**
	 * Si la refactorizaci�n se complet� con �xito o no.
	 */
	private boolean done;
	
	/**
	 * Ruta del fichero .mod en que se guarda el modelo actual antes de la
	 * ejecuci�n de la refactorizaci�n.
	 */
	private String backup;
	
	/**
	 * Fecha de ejecuci�n de la refactorizaci�n.
	 */
	private Date date;
	
	/**
	 * Refactorizaci�n previa a la que se ejecuta.
	 */
	private String previous;
	
	/**
	 * Parametros de entrada de la refactorizaci�n. Pares de la forma nombre del par�metros y
	 * nombre cualificado del mismo dentro del modelo.
	 */
	private Map<String,String> inputParameters;
	
	/**
	 * Indica si la ejecuci�n del plan esta siendo llamada por el plan de refactorizaciones o no.
	 */
	private boolean fromPlan=false;
	
	/**
	 * Formato utilizado para almacenar la hora de creaci�n de una operaci�n de
	 * refactorizaci�n.
	 */
	private final DateFormat dateFormat = new SimpleDateFormat(Messages.RefactoringRunner_DateFormat);
	
	/**
	 * Dirige la ejecuci�n de una refactorizaci�n concreta, mostr�ndole al 
	 * usuario el progreso de la misma a trav�s de una ventana de progreso.
	 */
	public void runRefactoring(){
		IUndoableOperation refactoring = new RefactoringOperation(new Date());
		IOperationHistory history = OperationHistoryFactory.getOperationHistory(); 
		IUndoContext context = PlatformUI.getWorkbench().getOperationSupport().getUndoContext();
		
		refactoring.addContext(context);
		
		try {
			if(history.execute(refactoring, null, null) == Status.OK_STATUS && done && getInstance() instanceof DynamicRefactoringRunner){
					//a�adimos al xml los datos de la refactorizaci�n que se acaba de ejecutar.
					System.out.println("Ejecutada refactorizaci�n din�mica " + getRefactoringName());
					DateFormat dFormat = new SimpleDateFormat(Messages.RefactoringRunner_DateFormat2 + " - HH:mm:ss");  //$NON-NLS-1$
					String fecha = dFormat.format(date);
					RefactoringPlanWriter.getInstance().writeRefactoring(getRefactoringName(),fecha, inputParameters);
				}
		}catch (ExecutionException exception){
			MOONRefactoring.resetModel();
			Object[] messageArgs = {getRefactoringName()};
			MessageFormat formatter = new MessageFormat("");  //$NON-NLS-1$
			formatter.applyPattern(Messages.RefactoringRunner_CouldNotRun);
			
			String message = formatter.format(messageArgs) +
				":\n\n" + exception.getMessage();    //$NON-NLS-1$
			logger.error(message);
			RefactoringPlugin.getDefault().fireRefactoringFailed(
				getRefactoringName(), message);
		}
	}
	
	/**
	 * Permite establecer el valor del atributo fromPlan. 
	 * @param fromPlan tru en caso de que la ejecuci�n provenga del plan de refactorizaciones.
	 */
	public void setFromPlan(boolean fromPlan){
		this.fromPlan=fromPlan;
	}
	
	/**
	 * Obtiene el nombre de la refactorizaci�n que se permite ejecutar.
	 * 
	 * @return el nombre de la refactorizaci�n que se permite ejecutar.
	 */
	protected abstract String getRefactoringName();
	
	/**
	 * Obtiene la refactorizaci�n que se permite ejecutar.
	 * 
	 * @return la refactorizaci�n que se permite ejecutar.
	 */
	protected abstract Refactoring getRefactoring();
	
	/**
	 * Establece los parametros de entrada de una refactorizaci�n. 
	 * 
	 * @param inputParameters par�metros de entrada.
	 */
	public void setInputParameters(HashMap<String,String> inputParameters){
		this.inputParameters = inputParameters;
	}
		
	/**
	 * Ejecuta la refactorizaci�n de manera que su progreso pueda ser seguido
	 * por un monitor de progreso.
	 * 
	 * @param monitor el monitor de progreso que mostrar� el avance de la
	 * operaci�n de refactorizaci�n.
	 * 
	 * @throws InvocationTargetException si se produce alguna clase de excepci�n
	 * durante la ejecuci�n, se relanza envuelta en este tipo de excepci�n.
	 * @throws InterruptedException si el usuario interrumpi� el proceso
	 * pulsando el bot�n de cancelaci�n.
	 */
	@Override
	public void run(IProgressMonitor monitor) 
		throws InvocationTargetException, InterruptedException {
		
		Object[] messageArgs = {getRefactoringName()};
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.RefactoringRunner_RunningRefactoring);		
		
		monitor.beginTask(formatter.format(messageArgs), 3);
		
		try {
			// Se guarda una copia del estado del modelo antes de la 
			// refactorizaci�n.
			monitor.subTask(Messages.RefactoringRunner_Saving);
			backup = RefactoringPlugin.getDefault().getNextBackupDestiny();
			previous = RefactoringPlugin.getDefault().getCurrentRefactoring();
			JavaModel.save(backup);
			
			checkForCancellation(monitor);
			monitor.worked(1);
			
			// Ejecuta la refactorizaci�n.
			monitor.subTask(Messages.RefactoringRunner_Running + "..."); //$NON-NLS-1$
			getRefactoring().run();
			monitor.worked(1);
		}
		catch(PreconditionException precondition){
			monitor.done();
			done = false;
			RefactoringPlugin.getDefault().fireRefactoringFailed(
				getRefactoringName(),
				Messages.RefactoringRunner_PreconditionFailed + ":" + precondition.getMessage()); //$NON-NLS-1$
		}
		catch(PostconditionException postcondition){
			done = false;
			getRefactoring().undoActions();
			RefactoringPlugin.getDefault().fireRefactoringFailed(
				getRefactoringName(),
				Messages.RefactoringRunner_PostconditionFailed + ":" + postcondition.getMessage());  //$NON-NLS-1$
		}
		catch (Exception e){			
			done = false;
			getRefactoring().undoActions();
			RefactoringPlugin.getDefault().fireRefactoringFailed(
				getRefactoringName(),
				Messages.RefactoringRunner_ErrorRefactoring + ".\n" +  //$NON-NLS-1$
				e.getMessage());
		}
		finally {
			monitor.done();
			if (monitor.isCanceled()){
				throw new InterruptedException(
					Messages.RefactoringRunner_ProcessCancelled + ".");  //$NON-NLS-1$
			}
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
		if(monitor.isCanceled()){
			throw new InterruptedException(
				Messages.RefactoringRunner_UserCancelled + ".\n");  //$NON-NLS-1$
		}
	}

	/**
	 * Obtiene el ejecutor de refactorizaciones actual.
	 * 
	 * @return la instancia actual del ejecutor de refactorizaciones.
	 */
	private RefactoringRunner getInstance(){
		return this;
	}
	
	/**
	 * Implementa la operaci�n de refactorizaci�n como una operaci�n capaz de
	 * ser deshecha por el entorno de Eclipse.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RefactoringOperation extends AbstractOperation {
		
		/**
		 * Cadena que identifica la operaci�n de refactorizaci�n a la hora de
		 * deshacerla dentro del entorno de operaciones de Eclipse.
		 */
		private String label;
		
		/**
		 * Constructor.
		 * 
		 * @param start hora a la que se construye la operaci�n de refactorizaci�n.
		 */
		public RefactoringOperation(Date start){
			super(getRefactoringName() + " (" +  dateFormat.format(start) + ")");  //$NON-NLS-1$ //$NON-NLS-2$
			label = getLabel();
		}
		
		/**
		 * Ejecuta la operaci�n de refactorizaci�n.
		 * 
		 * @param monitor monitor de progreso (no se utiliza).
		 * @param info informaci�n del entorno gr�fico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS} si el proceso se ejecut�
		 * correctamente; {@link Status#CANCEL_STATUS}, si no.
		 * 
		 * @see AbstractOperation#execute(IProgressMonitor, IAdaptable)
		 */
		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info){
			try {			
				CustomProgressDialog dialog = new CustomProgressDialog(null);
				RefactoringPlugin.getDefault().fireRefactoringStarted(
					getRefactoringName());
				done = true;
				if(!fromPlan){//Si no se ejecuta desde el plan se abre una barra de progreso.
					dialog.run(true, true, getInstance());
				}else{
					backup = RefactoringPlugin.getDefault().getNextBackupDestiny();
					previous = RefactoringPlugin.getDefault().getCurrentRefactoring();
					JavaModel.save(backup);
					getRefactoring().run();
				}
				
				MOONRefactoring.resetModel();

				// Si se realiz� la refactorizaci�n
				if(done){

					try {
						JavaModel.save(ModelGenerator.DEFAULT_MOD_NAME);
					} catch (IOException ex){
						String message = Messages.RefactoringRunner_NotStored + 
							":\n\n" + ex.getMessage();  //$NON-NLS-1$
						logger.error(message);
						Logger.getRootLogger().error(message);
					}
					if(!fromPlan){
						RefactoringPlugin.getDefault().updateEnvironment(
								getRefactoringName());
					}else{
						RefactoringPlugin.getDefault().storeRefactoring(getRefactoringName());
					}
					
					if (RenamingRegistry.getInstance().isPending())
						RenamingRegistry.getInstance().bindRenaming(label);
				}
				date = new Date();
				
				RefactoringPlugin.getDefault().fireRefactoringFinished(
					new RefactoringSummary(getRefactoringName(),date, label));

				return Status.OK_STATUS;
			} 
			catch (InterruptedException e) {
				//e.printStackTrace();
				RefactoringPlugin.getDefault().fireRefactoringFinished(
					new RefactoringSummary(getRefactoringName(), new Date(), label));
				
				// El usuario cancel� el proceso.
				String message = Messages.RefactoringRunner_UserCancelled + ".\n";  //$NON-NLS-1$
				logger.warn(message);
				MOONRefactoring.resetModel();
				return Status.CANCEL_STATUS;
			}catch (Exception exception){
				exception.printStackTrace();
				RefactoringPlugin.getDefault().fireRefactoringFinished(
					new RefactoringSummary(getRefactoringName(), new Date(), label));
				
				String message = Messages.RefactoringRunner_NotCompleted +
					".\n" + exception.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				MOONRefactoring.resetModel();
				return Status.CANCEL_STATUS;
			}
		}
	
		/**
		 * Deshace la operaci�n de refactorizaci�n.
		 * 
		 * @param monitor monitor de progreso (no se utiliza).
		 * @param info informaci�n del entorno gr�fico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS} si el proceso se ejecut�
		 * correctamente; {@link Status#CANCEL_STATUS}, si no.
		 * 
		 * @see AbstractOperation#undo(IProgressMonitor, IAdaptable)
		 */
		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info){
			try {
				UndoJob job = new UndoJob();
						
				new CustomProgressDialog(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
					).run(true, false, job);
				
				RefactoringPlugin.getDefault().updateEnvironment(previous);
				
				RefactoringPlugin.getDefault().fireRefactoringUndone(label);
				
					
				return Status.OK_STATUS;
			}
			catch (Exception exception){
				exception.printStackTrace();
				String message = Messages.RefactoringRunner_NotUndone +
					".\n" + exception.getMessage();  //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				monitor.done();
				return Status.CANCEL_STATUS;
			}
		}

		/**
		 * Sin implementaci�n.
		 * 
		 * @param monitor monitor de progreso (no se utiliza).
		 * @param info informaci�n del entorno gr�fico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS}.
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
	}
	
	/**
	 * Envuelve en un "trabajo" o <code>job</code> el proceso de deshacer
	 * la refactorizaci�n ejecutada.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class UndoJob implements IRunnableWithProgress{
				
		/**
		 * Ejecuta la operaci�n de deshacer la refactorizaci�n.
		 * 
		 * @param monitor el monitor de progreso que mostrar� el avance de la
		 * operaci�n de recuperaci�n de c�digo.
		 * 
		 * @throws InvocationTargetException si se produce cualquier clase de
		 * excepci�n se relanza envuelta en una excepci�n de este tipo.
		 * @throws InterruptedException si el usuario interrumpi� el proceso
		 * pulsando sobre el bot�n de cancelaci�n (deshabilitado).
		 */
		@Override
		public void run(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
			
			try {				
				monitor.beginTask(Messages.RefactoringRunner_Undoing, IProgressMonitor.UNKNOWN); 
				monitor.subTask(Messages.RefactoringRunner_Loading);
				JavaModel.load(backup);
			}
			catch(Exception e){
				String message = Messages.RefactoringRunner_RefactoringNotUndone + 
					":\n\n" + e.getMessage();   //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				throw new InvocationTargetException(e);
			}
			finally {
				monitor.done();
			}
		}
	}
}