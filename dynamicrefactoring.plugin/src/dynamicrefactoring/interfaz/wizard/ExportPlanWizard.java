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

package dynamicrefactoring.interfaz.wizard;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.ExportImportUtilities;
import dynamicrefactoring.interfaz.ButtonTextProvider;
import dynamicrefactoring.interfaz.CustomProgressDialog;

/**
 * Proporciona un asistente que permite exportar un plan formado por un conjunto de 
 * refactorizaciones din�micas existentes.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class ExportPlanWizard extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	protected static Logger logger = Logger.getLogger(ExportPlanWizard.class);
	
	/**
	 * Campo de texto en que se almacena la ruta del directorio al que se
	 * exportar�n las refactorizaciones.
	 */
	private Text t_Output;
	
	/**
	 * Bot�n que permite iniciar la exportaci�n.
	 */
	private Button bt_Export;
		
	/**
	 * Crea la ventana de di�logo.
	 * 
	 * @param parentShell la <i>shell</i> padre de esta ventana.
	 */
	public ExportPlanWizard(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Crea el contenido de la ventana de di�logo.
	 * 
	 * @param parent el elemento padre para los contenidos de la ventana.
	 * 
	 * @return el control del �rea de di�logo.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		final Label lb_Output = new Label(container, SWT.NONE);
		lb_Output.setText(Messages.ExportWizard_OutputFolder);
		lb_Output.setBounds(10, 8, 326, 13);

		t_Output = new Text(container, SWT.BORDER);
		t_Output.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Output.setEditable(false);
		t_Output.setToolTipText(Messages.ExportWizard_SelectOutput);
		t_Output.setBounds(10, 27, 355, 25);
		t_Output.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){
				if (t_Output.getText() != null && t_Output.getText().length() > 0)
					bt_Export.setEnabled(true);
				else
					bt_Export.setEnabled(false);
			}
		});
		
		final Button bt_Examine = new Button(container, SWT.NONE);
		bt_Examine.setToolTipText(Messages.ExportWizard_SelectOutput);
		bt_Examine.setText("..."); //$NON-NLS-1$
		bt_Examine.setBounds(371, 29, 27, 23);
		bt_Examine.addSelectionListener(new FolderSelectionListener(
			t_Output, getShell(), 
			Messages.ExportWizard_SelectOutputFolder +
			".")); //$NON-NLS-1$
		
		return container;
	}

	/**
	 * Crea el contenido de la barra de botones.
	 * 
	 * @param parent el elemento padre de los botones.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		bt_Export = createButton(parent, IDialogConstants.OK_ID, Messages.ExportWizard_Export, true);
		bt_Export.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, 
			ButtonTextProvider.getCancelText(), false);
		t_Output.setText(RefactoringPlugin.getDefault().getExportRefactoringPlanPreference());
	}

	/**
	 * Obtiene el tama�o inicial de la ventana de di�logo.
	 * 
	 * @return el tama�o inicial de la ventana de di�logo.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(416, 152);//402
	}

	/**
	 * Prepara la <i>shell</i> para su apertura.
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ExportPlanWizard_ExportRefactoringPlan);
		newShell.setImage(RefactoringImages.getExportPlanIcon());
	}
	
	

	/**
	 * Implementa la funcionalidad de exportaci�n de un plan de refactorizaciones, lanzada como 
	 * respuesta a la pulsaci�n del bot�n correspondiente.
	 * 
	 * @param buttonId identificador del bot�n que ha sido pulsado en el di�logo.
	 */
	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == IDialogConstants.OK_ID){
			
				
				try {			
					ExportPlanJob job = new ExportPlanJob(t_Output.getText().trim());				
					new CustomProgressDialog(getShell()).run(true, false, job);

										
					MessageDialog.openInformation(getShell(), Messages.ExportWizard_ExportDone, 
							Messages.ExportWizard_Successfully ); //$NON-NLS-1$
				}
				catch (InterruptedException e) {
					// El usuario cancel� el proceso.			 
					logger.warn(e.getMessage());
				}
				catch (Exception exception){
					String message = Messages.ExportPlanWizard_ExportFail +
						":\n\n" + exception.getMessage();  //$NON-NLS-1$
					logger.fatal(message);
					MessageDialog.openError(getShell(), Messages.ExportWizard_Error, message);
				}				
			}
		super.buttonPressed(buttonId);
	}
	
	/**
	 * Permite lanzar el trabajo de exportaci�n de refactorizaciones y hacer un
	 * seguimiento de su progreso.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 */
	private class ExportPlanJob implements IRunnableWithProgress{
		
		/**
		 * Directorio donde se crear�n las copias de las carpetas de cada una de
		 * las refactorizaciones exportadas.
		 */
		String destination;
		
		/**
		 * Constructor.
		 * 
		 * @param destination directorio donde se crear�n las copias de las
		 * refactorizaciones exportadas.
		 */
		public ExportPlanJob(String destination){
			this.destination = destination;
		}

		/**
		 * Ejecuta el trabajo de exportaci�n de refactorizaciones.
		 * 
		 * @param monitor el monitor de progreso que deber� usarse para mostrar
		 * el progreso.
		 * 
		 * @throws InvocationTargetException utilizada como envoltura si el m�todo 
		 * debe propagar una excepci�n (<i>checked exception</i>). Las excepciones
		 * de tipo <i>runtime exception</i> se envuelven autom�ticamente en una
		 * excepci�n de este tipo por el contexto que efect�a la llamada.
		 * @throws InterruptedException si la operaci�n detecta una solicitud de 
		 * cancelaci�n (no disponible).
		 * 
		 * @see IRunnableWithProgress#run(IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor) throws 
			InvocationTargetException, InterruptedException {			
			
			monitor.beginTask(Messages.ExportPlanWizard_Exporting, 1);
			try {
					ExportImportUtilities.exportRefactoringPlan(destination);
					monitor.worked(1);

			}
			catch (Exception exception){
				String message = Messages.ExportPlanWizard_ErrorExporting + 
					":\n\n" + exception.getMessage(); //$NON-NLS-1$
				logger.error(message);
				
				throw new InvocationTargetException(exception);
			}
			finally{
				monitor.done();
			}
		}
	}
}