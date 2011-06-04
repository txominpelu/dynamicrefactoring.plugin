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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.xml.ExportImportUtilities;
import dynamicrefactoring.domain.xml.XMLRefactoringUtils;
import dynamicrefactoring.interfaz.ButtonTextProvider;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.io.FileManager;

/**
 * Proporciona un asistente que permite buscar e importar refactorizaciones
 * dinámicas existentes fuera del <i>plugin</i>.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ImportWizard extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(ImportWizard.class);
		
	/**
	 * Tabla con la lista de refactorizaciones encontradas en el directorio especificado.
	 */
	private Table tb_Refactorings;
	
	/**
	 * Ruta del directorio en que se deben buscar las refactorizaciones.
	 */
	private Text t_Input;

	/**
	 * Botón que lanza el proceso de importación.
	 */
	private Button bt_Import;

	/**
	 * Casilla de selección que indica si la búsqueda ha de ser recursiva o no.
	 */
	private Button cbt_Recursive;
	
	/**
	 * Mensaje informativo mostrado al usuario en cada momento.
	 */
	private Text t_Message;
	
	/**
	 * Tabla de refactorizaciones que ya forman parte del <i>plugin</i>.
	 */
	private HashMap<String, String> existing;

	/**
	 * Nombres de las refactorizaciones que, de importarse, sobreescribirían
	 * otras ya existentes.
	 */
	private ArrayList<String> overwritten;

	/**
	 * Consejo mostrado al usuario sobre la búsqueda de refactorizaciones.
	 */
	private String advise;
	
	/**
	 * Icono mostrado junto al texto de aviso.
	 */
	private Label lb_Icon;
	
	/**
	 * Refactorizaciones encontradas en el directorio destino.
	 */
	private HashMap<String, String> refactorings;

	/**
	 * Crea la ventana de diálogo.
	 * 
	 * @param parentShell
	 *            <i>shell</i> padre de la ventana de diálogo.
	 */
	public ImportWizard(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Crea el contenido de la ventana de diálogo.
	 * 
	 * @param parent
	 *            componente padre de los contenidos de la ventana.
	 * 
	 * @return el control asociado al área de diálogo.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		t_Input = new Text(container, SWT.BORDER);
		t_Input.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Input.setEnabled(false);
		t_Input.setToolTipText(Messages.ImportWizard_SelectInputFolder);
		t_Input.setBounds(10, 34, 234, 25);
		t_Input.addModifyListener(new InputListener());

		final Button bt_Examine = new Button(container, SWT.NONE);
		bt_Examine.setText("..."); //$NON-NLS-1$
		bt_Examine.setBounds(250, 36, 24, 23);
		bt_Examine.addSelectionListener(new FolderSelectionListener(
			t_Input, getShell(), Messages.ImportWizard_SelectImportFolder +
			".")); //$NON-NLS-1$

		final Label lb_Input = new Label(container, SWT.NONE);
		lb_Input.setText(Messages.ImportWizard_InputFolder);
		lb_Input.setBounds(10, 15, 353, 13);
		
		lb_Icon = new Label(container, SWT.CENTER);
		lb_Icon.setBounds(9, 303, 25, 25);

		tb_Refactorings = new Table(container, SWT.CHECK | SWT.MULTI | SWT.BORDER);
		tb_Refactorings.setSortDirection(SWT.UP);
		tb_Refactorings.setToolTipText(Messages.ImportWizard_SelectRefactorings);
		tb_Refactorings.setLinesVisible(true);
		tb_Refactorings.setHeaderVisible(true);
		tb_Refactorings.setBounds(10, 95, 383, 202);
		tb_Refactorings.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e){
				int count = 0;
				for (int i = 0; i < tb_Refactorings.getItemCount(); i++)
					if (tb_Refactorings.getItem(i).getChecked())
						count++;
				
				if (count > 0)
					bt_Import.setEnabled(true);
				else
					bt_Import.setEnabled(false);
				
				double limits[] = {0, 1, ChoiceFormat.nextDouble(1)};
				String formats[] = {
					Messages.ImportWizard_0Imported, 
					Messages.ImportWizard_1Imported,
					Messages.ImportWizard_SeveralImported};
				ChoiceFormat form = new ChoiceFormat(limits, formats);
				
				Object[] messageArgs = {count};
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(form.format(count));
				
				t_Message.setText(formatter.format(messageArgs) + "."); //$NON-NLS-1$
				lb_Icon.setImage(RefactoringImages.getInfoIcon());
			}
		});

		final TableColumn cl_Name = new TableColumn(tb_Refactorings, SWT.NONE);
		cl_Name.setWidth(379);
		cl_Name.setText(Messages.ImportWizard_Name);

		final Label theFollowingRefactoringsLabel = new Label(container, SWT.NONE);
		theFollowingRefactoringsLabel.setText(Messages.ImportWizard_FoundRefactorings);
		theFollowingRefactoringsLabel.setBounds(10, 76, 383, 13);

		t_Message = new Text(container, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		t_Message.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN));
		t_Message.setEditable(false);
		t_Message.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		t_Message.setBounds(40, 312, 353, 64);

		cbt_Recursive = new Button(container, SWT.CHECK);
		cbt_Recursive.setToolTipText(Messages.ImportWizard_SelectRecursive);
		cbt_Recursive.setImage(RefactoringImages.getRecursiveIcon());
		cbt_Recursive.setText(Messages.ImportWizard_Recursive);
		cbt_Recursive.setBounds(294, 40, 107, 16);
		
		try {
			existing = DynamicRefactoringLister.getInstance().
				getDynamicRefactoringNameList(
					RefactoringPlugin.getDynamicRefactoringsDir(), true, null);
		}
		catch(Exception exception){
			logger.error(Messages.ImportWizard_ErrorBuilding +
				":\n\n" + exception.getMessage()); //$NON-NLS-1$
			throw new RuntimeException(exception);
		}
				
		return container;
	}

	/**
	 * Crea el contenido de la barra de botones.
	 * 
	 * @param parent elemento padre de los contenidos de la barra de botones.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		bt_Import = createButton(parent, IDialogConstants.OK_ID, Messages.ImportWizard_Import, true);
		bt_Import.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
			ButtonTextProvider.getCancelText(), false);
	}

	/**
	 * Obtiene el tamaño inicial de la ventana de diálogo.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(411, 458);
	}

	/**
	 * Prepara la ventana de diálogo para su apertura.
	 * 
	 * @param newShell
	 *            <i>shell</i> que abrirá la ventana.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ImportWizard_ImportRefactorings);
		newShell.setImage(RefactoringImages.getImportIcon());
	}
	
	/**
	 * Puebla la tabla con los nombres de las refactorizaciones encontradas.
	 */
	private void fillInTable(){
		for (Entry<String, String> entry : refactorings.entrySet()){
			// Se crea la nueva entrada de la tabla.
			TableItem item = new TableItem(tb_Refactorings, SWT.BORDER);
			item.setText(0, entry.getKey());
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN));
		}
	}
	
	/**
	 * Elimina los elementos de la tabla.
	 */
	private void cleanTable(){
		tb_Refactorings.removeAll();
	}

	/**
	 * Implementa la funcionalidad de importación, lanzada como respuesta a la
	 * pulsación del botón correspondiente.
	 * 
	 * @param buttonId
	 *            identificador del botón que ha sido pulsado en el diálogo.
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID){
			
			ArrayList<String> checked = new ArrayList<String>(tb_Refactorings.getItemCount());
			for (int i = 0; i < tb_Refactorings.getItemCount(); i++)
				if (tb_Refactorings.getItem(i).getChecked())
					checked.add(tb_Refactorings.getItem(i).getText(0));
			
			if(checked.size() > 0){
				
				String[] names = new String[checked.size()];
				names = checked.toArray(names);
				
				try {			
					ImportJob job = new ImportJob(names);				
					new CustomProgressDialog(getShell()).run(true, false, job);
					
					double limits[] = {0, 1, ChoiceFormat.nextDouble(1)};
					String formats[] = {
						Messages.ImportWizard_0WereImproted, 
						Messages.ImportWizard_1WereImproted,
						Messages.ImportWizard_SeveralWereImproted};
					ChoiceFormat form = new ChoiceFormat(limits, formats);
					
					Object[] messageArgs = {checked.size()};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(form.format(checked.size()));
					
					MessageDialog.openInformation(getShell(), Messages.ImportWizard_ImportDone, 
						formatter.format(messageArgs) + "."); //$NON-NLS-1$
				}
				catch (InterruptedException e) {
					// El usuario canceló el proceso.
					logger.warn(e.getMessage());
				}
				catch (Exception exception){
					String message = Messages.ImportWizard_NotAllImported +
						":\n\n" + exception.getMessage();  //$NON-NLS-1$
					logger.fatal(message);
					MessageDialog.openError(getShell(), Messages.ImportWizard_Error, message);
				}				
			}
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Actualiza el mensaje de aviso mostrado en la ventana, en función de los
	 * valores de configuración actuales resultantes de la última búsqueda.
	 */
	private void updateMessage() {
		if (overwritten != null && overwritten.size() > 0){
			String names = overwritten.get(0);
			for (int i = 1; i < overwritten.size(); i++){
				names += ", " + overwritten.get(i); //$NON-NLS-1$
			}
			
			Object[] messageArgs = {names};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.ImportWizard_NamesAlreadyExist);
			
			t_Message.setText(formatter.format(messageArgs) + ". " + //$NON-NLS-1$
				Messages.ImportWizard_WillOverwrite +
				"."); //$NON-NLS-1$
			lb_Icon.setImage(RefactoringImages.getWarningIcon());
			
		}						
		else if (advise == null){
			Object[] messageArgs = {refactorings.size()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.ImportWizard_NumberFound);
			
			t_Message.setText(formatter.format(messageArgs) + "."); //$NON-NLS-1$
			lb_Icon.setImage(RefactoringImages.getInfoIcon());
		}
		else{
			t_Message.setText(advise);
			lb_Icon.setImage(RefactoringImages.getWarningIcon());
		}
	}

	/**
	 * Actualiza la tabla de refactorizaciones encontradas que se pueden importar.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class InputListener implements ModifyListener{

		/**
		 * Recibe notificaciones cada vez que se modifica el texto observado.
		 * 
		 * @param e
		 *            el evento de modificación del texto.
		 */
		@Override
		public void modifyText(ModifyEvent e){
			Text field = (Text)e.getSource();
			
			try {
				refactorings = new HashMap<String, String>();
				overwritten = new ArrayList<String>();
				
				try {			
					RefactoringSearchJob job = new RefactoringSearchJob(
						field.getText().trim(), cbt_Recursive.getSelection());				
					new CustomProgressDialog(getShell()).run(true, true, job);
					
					cleanTable();
					fillInTable();					
					updateMessage();
				}
				catch (InterruptedException exception) {
					// El usuario canceló el proceso.
					logger.warn(exception.getMessage());
				}
				catch (Exception exception){
					String message = Messages.ImportWizard_ErrorWhileLooking +
						".\n\n:" + exception.getMessage(); //$NON-NLS-1$
					logger.fatal(message);
					MessageDialog.openError(getShell(), Messages.ImportWizard_Error, message);
				}
			}
			catch(Exception exception){
				String message = Messages.ImportWizard_FolderCannotBeAccessed;
				logger.error(message + ": " + exception.getMessage()); //$NON-NLS-1$
				t_Message.setText(message);
				lb_Icon.setImage(RefactoringImages.getErrorIcon());
			}
		}
	}

	/**
	 * Permite lanzar el trabajo de importación de refactorizaciones y hacer un
	 * seguimiento de su progreso.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ImportJob implements IRunnableWithProgress{
		
		/**
		 * Nombres de las refactorizaciones que se deben importar.
		 */
		private String[] names;
		
		/**
		 * Constructor.
		 * 
		 * @param names nombres de las refactorizaciones que se deben importar.
		 */
		public ImportJob(String[] names){
			this.names = names;
		}

		/**
		 * Ejecuta el trabajo de importación de refactorizaciones.
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
		public void run(IProgressMonitor monitor) throws 
			InvocationTargetException, InterruptedException {
			
			monitor.beginTask(Messages.ImportWizard_Importing, names.length);
			try {
				
				for(String next : names){
					
					Object[] messageArgs = {next};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.ImportWizard_ImportingFile);					
					monitor.subTask(formatter.format(messageArgs) + "..."); //$NON-NLS-1$

					// Se obtiene la ruta del fichero con la definición.
					String definition = refactorings.get(next);
					String folder = new File(definition).getParent();
				
					try {
						ExportImportUtilities.ImportRefactoring(
								definition, false);
					} catch (FileNotFoundException e) {

						// Elimina la carpeta de la refactorización ya que
							// si ha llegado
							//a este punto quiere decir que no se ha podido completar la tarea
							//adecuadamente.
							StringTokenizer st_namefolder = new StringTokenizer(folder, "" + File.separatorChar + "");
							String namefolder = "";
							while(st_namefolder.hasMoreTokens()){
								namefolder = st_namefolder.nextElement().toString();
							}
							FileManager.emptyDirectories(RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "" + namefolder);
							FileManager.deleteDirectories(RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "" + namefolder, true);
						throw e;

					} catch (IOException e) {
						messageArgs = new Object[] { folder };
						formatter = new MessageFormat(""); //$NON-NLS-1$
						formatter.applyPattern(Messages.ImportWizard_NotCopied);
						throw new Exception(formatter.format(messageArgs) + "."); //$NON-NLS-1$
					}

					monitor.worked(1);
				}
			}
			catch (Exception exception){
				String message = Messages.ImportWizard_ErrorImporting + 
					":\n\n" + exception.getMessage(); //$NON-NLS-1$
				logger.error(message);
				throw new InvocationTargetException(exception);
			}
			finally{
				monitor.done();
			}
		}
	}

	/**
	 * Permite lanzar el trabajo de búsqueda de refactorizaciones y hacer un
	 * seguimiento de su progreso.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RefactoringSearchJob implements IRunnableWithProgress{
		
		/**
		 * Directorio a partir del que se buscan refactorizaciones.
		 */
		private String folder;

		/**
		 * Si la búsqueda ha de ser recursiva o no.
		 */
		private boolean recursive;

		/**
		 * Constructor.
		 * 
		 * @param folder
		 *            directorio a partir del que se deben buscar las
		 *            refactorizaciones.
		 * @param recursive
		 *            si la búsqueda debe ser recursiva o no.
		 */
		public RefactoringSearchJob(String folder, boolean recursive){
			this.folder = folder;
			this.recursive = recursive;
		}

		/**
		 * Ejecuta el trabajo de búsqueda de refactorizaciones.
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
		public void run(IProgressMonitor monitor) throws 
			InvocationTargetException, InterruptedException {
			
			monitor.beginTask(Messages.ImportWizard_Looking, IProgressMonitor.UNKNOWN);
			try {
				monitor.subTask(Messages.ImportWizard_Traversing);
				DynamicRefactoringLister lister = DynamicRefactoringLister.getInstance();
				refactorings = lister.getDynamicRefactoringNameList(
					folder, recursive, monitor);
				monitor.worked(1);
				
				String[] names = refactorings.keySet().toArray(new String[0]);
				monitor.beginTask(Messages.ImportWizard_Validating, names.length);
				for (int i = 0; i < names.length; i++){
					String path = refactorings.get(names[i]);
					
					Object[] messageArgs = {names[i]};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.ImportWizard_ValidatingFile);
					
					monitor.subTask(formatter.format(messageArgs) + "..."); //$NON-NLS-1$
					
					try {
						// Se intenta obtener la definición de la siguiente
						// refactorización.
						XMLRefactoringUtils.getRefactoringDefinition(path);
						
						// Solo se recogen refactorizaciones cuya carpeta se
						// llame igual que su fichero.
						File definition = new File(path);
						String fileName = definition.getName();
						String folderName = definition.getParentFile().getName();
						fileName = fileName.substring(0, 
							fileName.toLowerCase().lastIndexOf(".xml")); //$NON-NLS-1$
						if (! fileName.equals(folderName)){
							advise = Messages.ImportWizard_FoundDiscarded +
								": " + Messages.ImportWizard_MakeSureSameName + //$NON-NLS-1$
								"."; //$NON-NLS-1$
							throw new Exception();
						}
						
						// Si ya hay una refactorización con ese nombre.
						if (existing.containsKey(names[i])){
							String old = new File(existing.get(names[i])).getCanonicalPath();
							String now = new File(path).getCanonicalPath();
							// Si es una de las existentes, se descarta.
							if (old.equals(now))
								refactorings.remove(names[i]);
							// Si no, se añade un aviso de sobreescritura.
							else
								overwritten.add(names[i].substring(0, 
									names[i].indexOf(" ("))); //$NON-NLS-1$
						}
					}
					catch (Exception exception){
						refactorings.remove(names[i]);
					}
					
					monitor.worked(1);
					
					if (refactorings.size() == 0)
						advise = Messages.ImportWizard_NoneFound;
				}				
			}
			catch (Exception exception){
				String message = Messages.ImportWizard_ErrorLooking + 
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