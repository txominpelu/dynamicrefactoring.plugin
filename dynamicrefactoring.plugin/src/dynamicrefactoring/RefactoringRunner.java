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

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import dynamicrefactoring.domain.RefactoringSummary;
import dynamicrefactoring.domain.xml.writer.RefactoringPlanWriter;
import dynamicrefactoring.integration.CodeRegenerator;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.interfaz.dynamic.DynamicRefactoringRunner;
import dynamicrefactoring.util.io.JavaFileManager;

/**
 * Permite ejecutar una operación de refactorización, mostrando al usuario el
 * progreso de la operación y permitiendo su cancelación hasta cierto momento de
 * la refactorización.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class RefactoringRunner {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(RefactoringRunner.class);

	/**
	 * Parametros de entrada de la refactorización. Pares de la forma nombre del
	 * parámetros y nombre cualificado del mismo dentro del modelo.
	 */
	private Map<String, String> inputParameters;

	/**
	 * Indica si la ejecución del plan esta siendo llamada por el plan de
	 * refactorizaciones o no.
	 */
	private boolean fromPlan = false;

	/**
	 * Formato utilizado para almacenar la hora de creación de una operación de
	 * refactorización.
	 */
	private final static DateFormat dateFormat = new SimpleDateFormat(
			Messages.RefactoringRunner_DateFormat);

	/**
	 * Dirige la ejecución de una refactorización concreta, mostrándole al
	 * usuario el progreso de la misma a través de una ventana de progreso.
	 */
	public void runRefactoring() {
		IUndoableOperation refactoring = new RefactoringOperation(new Date(), getRefactoring(), getRefactoringName(), this.fromPlan);
		IOperationHistory history = OperationHistoryFactory
				.getOperationHistory();
		IUndoContext context = PlatformUI.getWorkbench().getOperationSupport()
				.getUndoContext();

		refactoring.addContext(context);

		try {
			if (history.execute(refactoring, null, null) == Status.OK_STATUS
					&& getInstance() instanceof DynamicRefactoringRunner) {
				// añadimos al xml los datos de la refactorización que se acaba
				// de ejecutar.
				System.out.println("Ejecutada refactorizacion dinamica "
						+ getRefactoringName());
				DateFormat dFormat = new SimpleDateFormat(
						Messages.RefactoringRunner_DateFormat2 + " - HH:mm:ss"); //$NON-NLS-1$
				String fecha = dFormat.format(new Date());
				RefactoringPlanWriter.getInstance().writeRefactoring(
						getRefactoringName(), fecha, inputParameters);
			}
		} catch (ExecutionException exception) {
			MOONRefactoring.resetModel();
			Object[] messageArgs = { getRefactoringName() };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.RefactoringRunner_CouldNotRun);

			String message = formatter.format(messageArgs)
					+ ":\n\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			RefactoringPlugin.getDefault().fireRefactoringFailed(
					getRefactoringName(), message);
		}
	}

	/**
	 * Permite establecer el valor del atributo fromPlan.
	 * 
	 * @param fromPlan
	 *            tru en caso de que la ejecución provenga del plan de
	 *            refactorizaciones.
	 */
	public void setFromPlan(boolean fromPlan) {
		this.fromPlan = fromPlan;
	}

	/**
	 * Obtiene el nombre de la refactorización que se permite ejecutar.
	 * 
	 * @return el nombre de la refactorización que se permite ejecutar.
	 */
	protected abstract String getRefactoringName();

	/**
	 * Obtiene la refactorización que se permite ejecutar.
	 * 
	 * @return la refactorización que se permite ejecutar.
	 */
	protected abstract Refactoring getRefactoring();

	/**
	 * Establece los parametros de entrada de una refactorización.
	 * 
	 * @param inputParameters
	 *            parámetros de entrada.
	 */
	public void setInputParameters(HashMap<String, String> inputParameters) {
		this.inputParameters = inputParameters;
	}

	/**
	 * Obtiene el ejecutor de refactorizaciones actual.
	 * 
	 * @return la instancia actual del ejecutor de refactorizaciones.
	 */
	private RefactoringRunner getInstance() {
		return this;
	}

	/**
	 * Implementa la operación de refactorización como una operación capaz de
	 * ser deshecha por el entorno de Eclipse.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private static class RefactoringOperation extends AbstractOperation
			implements IRunnableWithProgress {

		private static final int ONE_SECOND = 1000;
		
		/**
		 * Cadena que identifica la operación de refactorización a la hora de
		 * deshacerla dentro del entorno de operaciones de Eclipse.
		 */
		private String label;

		/**
		 * Fecha a la que se deben devolver los ficheros para deshacer la
		 * refactorizacion.
		 */
		private Date datePreviousToRefactoring;

		/**
		 * Refactorizacion a eliminar. Se debe hacer nula justo despues de
		 * ejecutarla para no mantener referencias espúreas.
		 */
		private Refactoring refactoring;

		/**
		 * Nombre de la refactorizacion.
		 */
		private String name;

		/**
		 * Si la refactorizacion pertenece a un plan de refactorizacion o no.
		 */
		private boolean fromPlan;

		/**
		 * Constructor.
		 * 
		 * @param start
		 *            hora a la que se construye la operación de
		 *            refactorización.
		 */
		public RefactoringOperation(Date start, Refactoring refactoring, String name,
				boolean fromPlan) {
			super(name + " (" + dateFormat.format(start) + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			this.refactoring = refactoring;
			this.name = name;
			this.fromPlan = fromPlan;
			label = getLabel();
		}

		/**
		 * Ejecuta la operación de refactorización.
		 * 
		 * @param monitor
		 *            monitor de progreso (no se utiliza).
		 * @param info
		 *            información del entorno gráfico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS} si el proceso se ejecutó
		 *         correctamente; {@link Status#CANCEL_STATUS}, si no.
		 * 
		 * @see AbstractOperation#execute(IProgressMonitor, IAdaptable)
		 */
		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info) {
			try {
				CustomProgressDialog dialog = new CustomProgressDialog(null);
				RefactoringPlugin.getDefault().fireRefactoringStarted(name);
				datePreviousToRefactoring = new Date(
						getLastFileModificationTime() + ONE_SECOND);
				if (!fromPlan) {// Si no se ejecuta desde el plan se abre una
								// barra de progreso.
					dialog.run(true, true, this);
				} else {
					refactoring.run();
				}

				CodeRegenerator.getInstance().refresh();
				if (RenamingRegistry.getInstance().isPending()) {
					RenamingRegistry.getInstance().bindRenaming(label);
				}

				MOONRefactoring.resetModel();

				RefactoringPlugin.getDefault().fireRefactoringFinished(
						new RefactoringSummary(name,
								new Date(), label));
				
				refactoring = null;
				
				return Status.OK_STATUS;
			} catch (InterruptedException e) {
				RefactoringPlugin.getDefault().fireRefactoringFinished(
						new RefactoringSummary(name, new Date(), label));

				// El usuario canceló el proceso.
				String message = Messages.RefactoringRunner_UserCancelled
						+ ".\n"; //$NON-NLS-1$
				logger.warn(message);
				MOONRefactoring.resetModel();
				return Status.CANCEL_STATUS;
			} catch (Exception exception) {
				exception.printStackTrace();
				RefactoringPlugin.getDefault().fireRefactoringFinished(
						new RefactoringSummary(name, new Date(), label));

				String message = Messages.RefactoringRunner_NotCompleted
						+ ".\n" + exception.getMessage(); //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				MOONRefactoring.resetModel();
				return Status.CANCEL_STATUS;
			}
		}

		/**
		 * Devuelve de entre todos los ficheros la fecha de modificacion del
		 * ultimo de los modificados.
		 * 
		 * @return fecha de modificacion del ultimo fichero modificado
		 */
		private long getLastFileModificationTime() {
			Collection<Long> fileModificationTimes = Collections2.transform(
					JavaFileManager.getJavaProjectFiles(RefactoringPlugin
							.getDefault().getAffectedProject()),
					new Function<IFile, Long>() {

						@Override
						public Long apply(IFile arg0) {
							return arg0.getLocalTimeStamp();
						}

					});
			return Collections.max(fileModificationTimes);
		}

		/**
		 * Comprueba si un monitor de progreso ha recibido una orden de
		 * cancelación por parte del usuario.
		 * 
		 * @param monitor
		 *            el monitor cuyo estado de cancelación se comprueba.
		 * 
		 * @throws InterruptedException
		 *             si el monitor ha recibido una orden de cancelación por
		 *             parte del usuario.
		 */
		private void checkForCancellation(IProgressMonitor monitor)
				throws InterruptedException {
			if (monitor.isCanceled()) {
				throw new InterruptedException(
						Messages.RefactoringRunner_UserCancelled + ".\n"); //$NON-NLS-1$
			}
		}

		/**
		 * Ejecuta la refactorización de manera que su progreso pueda ser
		 * seguido por un monitor de progreso.
		 * 
		 * @param monitor
		 *            el monitor de progreso que mostrará el avance de la
		 *            operación de refactorización.
		 * 
		 * @throws InvocationTargetException
		 *             si se produce alguna clase de excepción durante la
		 *             ejecución, se relanza envuelta en este tipo de excepción.
		 * @throws InterruptedException
		 *             si el usuario interrumpió el proceso pulsando el botón de
		 *             cancelación.
		 */
		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {

			Object[] messageArgs = { name };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.RefactoringRunner_RunningRefactoring);

			monitor.beginTask(formatter.format(messageArgs), 3);

			try {
				// Se guarda una copia del estado del modelo antes de la
				// refactorización.
				monitor.subTask(Messages.RefactoringRunner_Saving);

				checkForCancellation(monitor);
				monitor.worked(1);

				// Ejecuta la refactorización.
				monitor.subTask(Messages.RefactoringRunner_Running + "..."); //$NON-NLS-1$
				refactoring.run();
				monitor.worked(1);
			} catch (PreconditionException precondition) {
				monitor.done();
				RefactoringPlugin.getDefault().fireRefactoringFailed(
						name,
						Messages.RefactoringRunner_PreconditionFailed
								+ ":" + precondition.getMessage()); //$NON-NLS-1$
			} catch (PostconditionException postcondition) {
				refactoring.undoActions();
				RefactoringPlugin.getDefault().fireRefactoringFailed(
						name,
						Messages.RefactoringRunner_PostconditionFailed
								+ ":" + postcondition.getMessage()); //$NON-NLS-1$
			} catch (Exception e) {
				refactoring.undoActions();
				RefactoringPlugin.getDefault().fireRefactoringFailed(name,
						Messages.RefactoringRunner_ErrorRefactoring + ".\n" + //$NON-NLS-1$
								e.getMessage());
			} finally {
				monitor.done();
				if (monitor.isCanceled()) {
					throw new InterruptedException(
							Messages.RefactoringRunner_ProcessCancelled + "."); //$NON-NLS-1$
				}
			}
		}

		/**
		 * Deshace la operación de refactorización.
		 * 
		 * @param monitor
		 *            monitor de progreso (no se utiliza).
		 * @param info
		 *            información del entorno gráfico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS} si el proceso se ejecutó
		 *         correctamente; {@link Status#CANCEL_STATUS}, si no.
		 * 
		 * @see AbstractOperation#undo(IProgressMonitor, IAdaptable)
		 */
		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info) {
			try {
				UndoJob job = new UndoJob(datePreviousToRefactoring);

				new CustomProgressDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell()).run(true,
						false, job);

				RefactoringPlugin.getDefault().fireRefactoringUndone(label);

				return Status.OK_STATUS;
			} catch (Exception exception) {
				exception.printStackTrace();
				String message = Messages.RefactoringRunner_NotUndone
						+ ".\n" + exception.getMessage(); //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				monitor.done();
				return Status.CANCEL_STATUS;
			}
		}

		/**
		 * Sin implementación.
		 * 
		 * @param monitor
		 *            monitor de progreso (no se utiliza).
		 * @param info
		 *            información del entorno gráfico (no se utiliza).
		 * 
		 * @return {@link Status#OK_STATUS}.
		 * 
		 * @see AbstractOperation#redo(IProgressMonitor, IAdaptable)
		 */
		public IStatus redo(IProgressMonitor monitor, IAdaptable info) {
			return Status.OK_STATUS;
		}

		/**
		 * Determina si la operación se puede rehacer una vez deshecha. En este
		 * caso, no se puede.
		 * 
		 * @return <code>false</code>.
		 */
		@Override
		public boolean canRedo() {
			return false;
		}
	}

	/**
	 * Envuelve en un "trabajo" o <code>job</code> el proceso de deshacer la
	 * refactorización ejecutada.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private static class UndoJob implements IRunnableWithProgress {

		/**
		 * Fecha a la que hay que devolver el estado de los ficheros para
		 * deshacer la refactorizacion.
		 */
		private final Date dateToRevertFiles;

		public UndoJob(Date dateToRevertFiles) {
			this.dateToRevertFiles = dateToRevertFiles;
		}

		/**
		 * Ejecuta la operación de deshacer la refactorización.
		 * 
		 * @param monitor
		 *            el monitor de progreso que mostrará el avance de la
		 *            operación de recuperación de código.
		 * 
		 * @throws InvocationTargetException
		 *             si se produce cualquier clase de excepción se relanza
		 *             envuelta en una excepción de este tipo.
		 * @throws InterruptedException
		 *             si el usuario interrumpió el proceso pulsando sobre el
		 *             botón de cancelación (deshabilitado).
		 */
		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {

			try {

				monitor.beginTask(Messages.RefactoringRunner_Undoing,
						IProgressMonitor.UNKNOWN);
				monitor.subTask(Messages.RefactoringRunner_Loading);

				ArrayList<IFile> ficherosFuente = JavaFileManager
						.getJavaProjectFiles(RefactoringPlugin.getDefault()
								.getAffectedProject());
				for (IFile fichero : ficherosFuente) {

					IFileState[] states = fichero.getHistory(null);
					if (states.length > 0) {
						fichero.setContents(
								getFileStatePreviousToRefactoring(states,
										dateToRevertFiles), IFile.KEEP_HISTORY,
								null);
						fichero.refreshLocal(IResource.DEPTH_INFINITE, null);
					}
				}

			} catch (Exception e) {
				String message = Messages.RefactoringRunner_RefactoringNotUndone
						+ ":\n\n" + e.getMessage(); //$NON-NLS-1$
				logger.fatal(message);
				Logger.getRootLogger().fatal(message);
				throw new InvocationTargetException(e);
			} finally {
				monitor.done();
			}
		}

		/**
		 * Devuelve el estado del archivo no inmediatamente anterior a la fecha
		 * pasada sino el anterior.
		 * 
		 * @param states
		 *            conjunto de estados del archivo ordenados de mas reciente
		 *            a mas antiguo
		 * @param refactoringDate
		 *            fecha
		 * @return estado inmediatamente anterior a la fecha
		 */
		private IFileState getFileStatePreviousToRefactoring(
				IFileState[] states, Date refactoringDate) {
			int i = 0;
			while (states.length > i
					&& states[i].getModificationTime() > refactoringDate
							.getTime()) {
				i++;
			}
			return states[i];
		}
	}
}