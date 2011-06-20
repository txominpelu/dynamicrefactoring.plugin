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
import java.text.ChoiceFormat;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.xml.ExportImportUtilities;
import dynamicrefactoring.interfaz.ButtonTextProvider;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.interfaz.DynamicRefactoringList;
import dynamicrefactoring.interfaz.RefactoringListFilter;
import dynamicrefactoring.interfaz.RefactoringListLabelProvider;
import dynamicrefactoring.interfaz.RefactoringListSorter;

/**
 * Proporciona un asistente que permite exportar un conjunto de
 * refactorizaciones dinámicas existentes.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 * 
 */
public class ExportWizard extends DynamicRefactoringList {

	/**
	 * Check que permite filtrar la lista de refactorizaciones para que 
	 * únicamente se muestren las propias del usuario.
	 */
	private Button filterButton;
	
	/**
	 * Filtro para que únicamente las refactorizaciones del
	 * usuario sean visibles en la lista de refactorizaciones.
	 */
	private RefactoringListFilter filter;
	
	/**
	 * Campo de texto en que se almacena la ruta del directorio al que se
	 * exportarán las refactorizaciones.
	 */
	private Text t_Output;

	/**
	 * Botón que permite iniciar la exportación.
	 */
	private Button btExport;

	/**
	 * Crea la ventana de diálogo.
	 * 
	 * @param parentShell
	 *            la <i>shell</i> padre de esta ventana.
	 * @param refactCatalog catálogo de refactorizaciones
	 */
	public ExportWizard(Shell parentShell, RefactoringsCatalog refactCatalog) {
		super(parentShell, refactCatalog);
	}

	/**
	 * Crea el contenido de la ventana de diálogo.
	 * 
	 * @param parent
	 *            el elemento padre para los contenidos de la ventana.
	 * 
	 * @return el control del área de diálogo.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		availableRefListViewer = new TableViewer(container, SWT.BORDER | SWT.MULTI );
		availableRefListViewer.setLabelProvider(new RefactoringListLabelProvider());
		availableRefListViewer.setContentProvider(new ArrayContentProvider());
		availableRefListViewer.setSorter(new RefactoringListSorter());
		availableRefListViewer.getTable().setToolTipText(Messages.ExportWizard_AvailableTooltip);
		availableRefListViewer.getTable().addSelectionListener(new RefactoringSelectionListener());
		availableRefListViewer.getTable().setBounds(10, 33, 388, 205);
		
		filter=new RefactoringListFilter();
		
		filterButton = new Button(container, SWT.CHECK);
		filterButton.setText(Messages.ExportWizard_Filter);
		filterButton.setBounds(20, 238, 300, 30);
		filterButton.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		        if (((Button) event.widget).getSelection())
		        	availableRefListViewer.addFilter(filter);
		        else
		        	availableRefListViewer.removeFilter(filter);
		      }
		    });
		
		final Label lb_Output = new Label(container, SWT.NONE);
		lb_Output.setText(Messages.ExportWizard_OutputFolder);
		lb_Output.setBounds(10, 278, 326, 13);

		t_Output = new Text(container, SWT.BORDER);
		t_Output.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		t_Output.setEditable(false);
		t_Output.setToolTipText(Messages.ExportWizard_SelectOutput);
		t_Output.setBounds(10, 297, 355, 25);
		t_Output.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (availableRefListViewer.getTable().getSelectionCount() > 0
						&& t_Output.getText() != null
						&& t_Output.getText().length() > 0)
					btExport.setEnabled(true);
				else
					btExport.setEnabled(false);
			}
		});

		final Button bt_Examine = new Button(container, SWT.NONE);
		bt_Examine.setToolTipText(Messages.ExportWizard_SelectOutput);
		bt_Examine.setText("..."); //$NON-NLS-1$
		bt_Examine.setBounds(371, 299, 27, 23);
		bt_Examine.addSelectionListener(new FolderSelectionListener(t_Output,
				getShell(), Messages.ExportWizard_SelectOutputFolder + ".")); //$NON-NLS-1$

		final Label lb_Available = new Label(container, SWT.NONE);
		lb_Available.setText(Messages.ExportWizard_AvailableRefactorings);
		lb_Available.setBounds(10, 14, 388, 13);

		fillInRefactoringList();

		return container;
	}

	/**
	 * Crea el contenido de la barra de botones.
	 * 
	 * @param parent
	 *            el elemento padre de los botones.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		btExport = createButton(parent, IDialogConstants.OK_ID,
				Messages.ExportWizard_Export, true);
		btExport.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				ButtonTextProvider.getCancelText(), false);
	}

	/**
	 * Obtiene el tamaño inicial de la ventana de diálogo.
	 * 
	 * @return el tamaño inicial de la ventana de diálogo.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(416, 402);
	}

	/**
	 * Prepara la <i>shell</i> para su apertura.
	 * 
	 * @param newShell nueva shell
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ExportWizard_ExportRefactorings);
		newShell.setImage(RefactoringImages.getExportIcon());
	}

	/**
	 * Actualiza el estado del botón de exportación en función de la selección
	 * sobre la lista de refactorizaciones disponibles.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RefactoringSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificación de que un elemento de la lista de
		 * refactorizaciones dinámicas disponibles ha sido seleccionado.
		 * 
		 * @param e
		 *            el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (availableRefListViewer.getTable().getSelectionCount() > 0
					&& t_Output.getText() != null
					&& t_Output.getText().length() > 0)
				btExport.setEnabled(true);
			else
				btExport.setEnabled(false);
		}

		/**
		 * Comportamiento ante el evento de selección por defecto.
		 * 
		 * @param e evento de selección.
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}

	/**
	 * Implementa la funcionalidad de exportación, lanzada como respuesta a la
	 * pulsación del botón correspondiente.
	 * 
	 * @param buttonId
	 *            identificador del botón que ha sido pulsado en el diálogo.
	 */
	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == IDialogConstants.OK_ID) {

			int selectionCount=availableRefListViewer.getTable().getSelectionCount();
			if (selectionCount > 0) {
				try {
					String names[]=new String[selectionCount];
					int i=0;
					for(TableItem item : availableRefListViewer.getTable().getSelection()){
						names[i]=((DynamicRefactoringDefinition)item.getData()).getName();
						i++;
					}
					ExportJob job = new ExportJob(names, t_Output.getText().trim());
					new CustomProgressDialog(getShell()).run(true, false, job);

					double limits[] = { 0, 1, ChoiceFormat.nextDouble(1) };
					String formats[] = { Messages.ExportWizard_0Exported,
							Messages.ExportWizard_1Exported,
							Messages.ExportWizard_SeveralExported };
					ChoiceFormat form = new ChoiceFormat(limits, formats);

					Object[] messageArgs = { selectionCount };
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(form.format(selectionCount));

					MessageDialog.openInformation(getShell(),
							Messages.ExportWizard_ExportDone,
							formatter.format(messageArgs) + "."); //$NON-NLS-1$
				} catch (InterruptedException e) {
					// El usuario canceló el proceso.
					logger.warn(e.getMessage());
				} catch (Exception exception) {
					String message = Messages.ExportWizard_NotAllExported
							+ ":\n\n" + exception.getMessage(); //$NON-NLS-1$
					logger.fatal(message);
					MessageDialog.openError(getShell(),
							Messages.ExportWizard_Error, message);
				}
			}
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Permite lanzar el trabajo de exportación de refactorizaciones y hacer un
	 * seguimiento de su progreso.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ExportJob implements IRunnableWithProgress {

		/**
		 * Nombres de las refactorizaciones que se exportarán.
		 */
		String[] refactorings;

		/**
		 * Directorio donde se crearán las copias de las carpetas de cada una de
		 * las refactorizaciones exportadas.
		 */
		String destination;

		/**
		 * Constructor.
		 * 
		 * @param refactorings
		 *            nombres de las refactorizaciones que se exportarán.
		 * @param destination
		 *            directorio donde se crearán las copias de las
		 *            refactorizaciones exportadas.
		 */
		public ExportJob(String[] refactorings, String destination) {
			this.refactorings = refactorings;
			this.destination = destination;
		}

		/**
		 * Ejecuta el trabajo de exportación de refactorizaciones.
		 * 
		 * @param monitor
		 *            el monitor de progreso que deberá usarse para mostrar el
		 *            progreso.
		 * 
		 * @throws InvocationTargetException
		 *             utilizada como envoltura si el método debe propagar una
		 *             excepción (<i>checked exception</i>). Las excepciones de
		 *             tipo <i>runtime exception</i> se envuelven
		 *             automáticamente en una excepción de este tipo por el
		 *             contexto que efectúa la llamada.
		 * @throws InterruptedException
		 *             si la operación detecta una solicitud de cancelación (no
		 *             disponible).
		 * 
		 * @see IRunnableWithProgress#run(IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {

			monitor.beginTask(Messages.ExportWizard_Exporting,
					refactorings.length);
			try {

				for (String next : refactorings) {

					Object[] messageArgs = { next };
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter
							.applyPattern(Messages.ExportWizard_ExportingRefactoring);
					monitor.subTask(formatter.format(messageArgs) + "..."); //$NON-NLS-1$

					// Se obtiene la ruta del fichero con la definición de la
					// refactorización.
					if (refactCatalog.hasRefactoring(next)) {
						String refactName = refactCatalog.getRefactoring(next)
								.getName();
						ExportImportUtilities
								.exportRefactoring(
										destination,
										refactCatalog.getRefactoring(next)
												.getXmlRefactoringDefinitionFilePath(),
										false);
					}
					monitor.worked(1);
				}
			} catch (Exception exception) {
				String message = Messages.ExportWizard_ErrorExporting
						+ ":\n\n" + exception.getMessage(); //$NON-NLS-1$
				logger.error(message);

				throw new InvocationTargetException(exception);
			} finally {
				monitor.done();
			}
		}
	}
}