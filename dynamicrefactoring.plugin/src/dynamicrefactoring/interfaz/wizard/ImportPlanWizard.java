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
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.ExportImportUtilities;
import dynamicrefactoring.domain.RefactoringPlanExecutor;
import dynamicrefactoring.domain.xml.reader.RefactoringPlanReader;
import dynamicrefactoring.interfaz.ButtonTextProvider;
import dynamicrefactoring.interfaz.CustomProgressDialog;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.io.FileManager;

/**
 * Proporciona un asistente que permite buscar e importar un plan de
 * refactorizaciones din�micas existentes fuera del <i>plugin</i>.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class ImportPlanWizard extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(ImportPlanWizard.class);

	/**
	 * Tabla con la lista de refactorizaciones que conforman el plan de
	 * refactorizaci�n.
	 */
	private Table tb_Refactorings;
	
	/**
	 * Ruta del directorio en que se deben buscar las refactorizaciones.
	 */
	private Text t_Input;

	/**
	 * Bot�n que lanza el proceso de ejecuci�n de la refactorizaci�n.
	 */
	private Button bt_Execute;

	/**
	 * Propiedad asociada a las filas de la tabla que indica qu� bot�n tienen
	 * asociado cada una.
	 */
	private final String BUTTON_PROPERTY = "Button"; //$NON-NLS-1$
	
	/**
	 * Conjunto de refactorizaciones que confoman el plan de refactorizaciones.
	 */
	private ArrayList<String> plan ;
	
	/**
	 * Mensaje informativo mostrado al usuario en cada momento.
	 */
	private Text t_Message;
	
	/**
	 * Tabla de refactorizaciones que ya forman parte del <i>plugin</i>.
	 */
	private HashMap<String, String> existing;

	/**
	 * Consejo mostrado al usuario sobre la b�squeda de refactorizaciones.
	 */
	private String advise;
	
	/**
	 * Icono mostrado junto al texto de aviso.
	 */
	private Label lb_Icon;
	
	
	/**
	 * Refactorizaciones que conforman el plan.
	 */
	HashMap<String,String> refactorings;
	
	/**
	 * Refactorizaciones que van a ser ejecutadas.
	 */
	Map<String,String> refactorings_to_execute;
	
	/**
	 * Refactorizaciones que van a ser importadas.
	 */
	HashMap<String,String> refactorings_to_import;

	/**
	 * Crea la ventana de di�logo.
	 * 
	 * @param parentShell
	 *            <i>shell</i> padre de la ventana de di�logo.
	 */
	public ImportPlanWizard(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Crea el contenido de la ventana de di�logo.
	 * 
	 * @param parent
	 *            componente padre de los contenidos de la ventana.
	 * 
	 * @return el control asociado al �rea de di�logo.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		t_Input = new Text(container, SWT.BORDER);
		t_Input.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Input.setEnabled(false);
		t_Input.setToolTipText(Messages.ImportWizard_SelectInputFolder);
		t_Input.setBounds(10, 34, 340, 25);
		t_Input.addModifyListener(new InputListener());
		

		final Button btExamine = new Button(container, SWT.NONE);
		btExamine.setText("..."); //$NON-NLS-1$
		btExamine.setBounds(352, 36, 24, 23);
		btExamine.addSelectionListener(new FolderSelectionListener(
			t_Input, getShell(), Messages.ImportPlanWizard_SelectImportFolder+
			".")); //$NON-NLS-1$

		final Label lb_Input = new Label(container, SWT.NONE);
		lb_Input.setText(Messages.ImportWizard_InputFolder);
		lb_Input.setBounds(10, 15, 353, 13);
		
		lb_Icon = new Label(container, SWT.CENTER);
		lb_Icon.setBounds(9, 303, 25, 25);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 2;
		
		tb_Refactorings = new Table(container, SWT.BORDER);
		tb_Refactorings.setLayoutData(gridData);
		tb_Refactorings.setToolTipText(Messages.ImportPlanWizard_PlanRefactorings);
		tb_Refactorings.setLinesVisible(true);
		tb_Refactorings.setHeaderVisible(true);
		tb_Refactorings.setBounds(10, 95, 383, 202);
		

		final TableColumn cl_Name = new TableColumn(tb_Refactorings, SWT.NONE);
		cl_Name.setWidth(239);
		cl_Name.setText(Messages.ImportPlanWizard_Name);
		cl_Name.setResizable(true);
		
		final TableColumn cl_Execute = new TableColumn(tb_Refactorings, SWT.CENTER);
		cl_Execute.setWidth(70);
		cl_Execute.setText(Messages.ImportPlanWizard_Execute);
		cl_Execute.setResizable(true);
		
		final TableColumn cl_Import = new TableColumn(tb_Refactorings, SWT.CENTER);
		cl_Import.setWidth(70);
		cl_Import.setText(Messages.ImportPlanWizard_Import);
		cl_Import.setResizable(false);

		
		final Label theFollowingRefactoringsLabel = new Label(container, SWT.NONE);
		theFollowingRefactoringsLabel.setText(Messages.ImportPlanWizard_PlanRefactorings);
		theFollowingRefactoringsLabel.setBounds(10, 76, 383, 13);

		t_Message = new Text(container, SWT.WRAP | SWT.READ_ONLY | SWT.MULTI);
		t_Message.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN));
		t_Message.setEditable(false);
		t_Message.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		t_Message.setBounds(40, 312, 353, 64);

		refactorings_to_execute = new HashMap<String,String>();
		refactorings_to_import = new HashMap<String,String>();
		
		try {
			existing = DynamicRefactoringLister.getInstance().
				getDynamicRefactoringNameList(
					RefactoringPlugin.getDynamicRefactoringsDir(), true, null);
		}
		catch(Exception exception){
			logger.error(Messages.ImportPlanWizard_ErrorBuilding +
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
		bt_Execute = createButton(parent, IDialogConstants.OK_ID, Messages.ImportPlanWizard_Execute, true);
		bt_Execute.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
			ButtonTextProvider.getCancelText(), false);
		t_Input.setText(RefactoringPlugin.getDefault().getImportRefactoringPlanPreference());
	}

	/**
	 * Obtiene el tama�o inicial de la ventana de di�logo.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(411, 458);
	}

	/**
	 * Prepara la ventana de di�logo para su apertura.
	 * 
	 * @param newShell
	 *            <i>shell</i> que abrir� la ventana.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ImportPlanWizard_ImportRefactoringPlan);
		newShell.setImage(RefactoringImages.getImportPlanIcon());
	}
	
	/**
	 * Puebla la tabla con los nombres de las refactorizaciones que conforman el plan.
	 */
	private void fillInTable(){
		for (String refactoring  : plan){
			// Se crea la nueva entrada de la tabla.
			TableItem item = new TableItem(tb_Refactorings, SWT.BORDER);
			item.setText(0, refactoring + " (" + refactoring +".xml)"); //$NON-NLS-1$ //$NON-NLS-2$
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN));
			
			final TableEditor editor2 = new TableEditor(tb_Refactorings);
			Button ch_Execute = new Button(tb_Refactorings, SWT.CHECK);
			ch_Execute.setSelection(true);
			ch_Execute.setData("Row", tb_Refactorings.indexOf(item)); //$NON-NLS-1$
			ch_Execute.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e){
					widgetSelected(e);
				}
				public void widgetSelected(SelectionEvent e){
					Button execute = (Button)e.getSource();
					boolean selected = execute.getSelection();
					int row = ((Integer)execute.getData("Row")).intValue(); //$NON-NLS-1$
					TableItem item = tb_Refactorings.getItem(row);
					String refactoring = item.getText(0);
					if(selected){
						refactorings_to_execute.put(refactoring,refactorings.get(refactoring));
					}else{
						refactorings_to_execute.remove(refactoring);
					}
					updateMessage();
					updateButton();
				}
			});
			ch_Execute.pack();
			item.setData("Button", ch_Execute); //$NON-NLS-1$
			editor2.minimumWidth = ch_Execute.getSize().x;
			editor2.minimumHeight = ch_Execute.getSize().y;
			editor2.horizontalAlignment = SWT.CENTER;
			editor2.setEditor(ch_Execute, item, 1);
			
			final TableEditor editor = new TableEditor(tb_Refactorings);
			Button ch_Import = new Button(tb_Refactorings, SWT.CHECK);
			ch_Import.setData("Row", tb_Refactorings.indexOf(item)); //$NON-NLS-1$
			ch_Import.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e){
					widgetSelected(e);
				}
				public void widgetSelected(SelectionEvent e){
					Button btimport = (Button)e.getSource();
					boolean selected = btimport.getSelection();
					int row = ((Integer)btimport.getData("Row")).intValue(); //$NON-NLS-1$
					TableItem item = tb_Refactorings.getItem(row);
					String refactoring = item.getText(0);
					if(selected){
						refactorings_to_import.put(refactoring,refactorings.get(refactoring));

						// Si ya hay una refactorizaci�n con ese nombre.
						if (existing.containsKey(refactoring)){
							Object[] messageArgs = {refactoring};
							MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
							formatter.applyPattern(Messages.ImportPlanWizard_Overwritten);
							advise = formatter.format(messageArgs);
						}	
					}else{
						refactorings_to_import.remove(refactoring);
					}
					updateMessage();
					updateButton();
				}
			});
			ch_Import.pack();
			item.setData("Button2", ch_Import); //$NON-NLS-1$
			editor.minimumWidth = ch_Import.getSize().x;
			editor.minimumHeight = ch_Import.getSize().y;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(ch_Import, item, 2);
			
			if (tb_Refactorings.getItemCount() == 1){
				// Se aumenta la altura de las filas.
				tb_Refactorings.addListener(SWT.MeasureItem, new Listener() {
					public void handleEvent(Event event) {
						event.height = editor.minimumHeight;
					}
				});
			}
			
			tb_Refactorings.redraw();
		}
	}
	
	/**
	 * Elimina los elementos de la tabla.
	 */
	private void cleanTable(){
		TableItem[] items = this.tb_Refactorings.getItems();
		for (int i = items.length - 1; i >= 0; i--){
			// Primero se recupera los botones asociados a la fila y se eliminan.
			Object button = items[i].getData(BUTTON_PROPERTY);
			if (button instanceof Button)
				((Button)button).dispose();
			Object button2 = items[i].getData("Button2");
			if (button2 instanceof Button)
				((Button)button2).dispose();
			items[i].dispose();
		}
		tb_Refactorings.removeAll();
	}

	/**
	 * Implementa la funcionalidad de importaci�n, lanzada como respuesta a la
	 * pulsaci�n del bot�n correspondiente.
	 * 
	 * @param buttonId
	 *            identificador del bot�n que ha sido pulsado en el di�logo.
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID){
			
			String imported = ""; //$NON-NLS-1$

				 String[] names = refactorings_to_import.keySet().toArray(new String[refactorings_to_import.keySet().size()]);
				 HashMap<String,String> notExecuted = new HashMap<String,String>();
				try {
				// Ejecutamos la importaci�n de las refactorizaciones se�aladas
				// para ser importadas.
					if(names.length>0){
						ImportJob job = new ImportJob(names);				
						new CustomProgressDialog(getShell()).run(true, false, job);
						
						double limits[] = {0, 1, ChoiceFormat.nextDouble(1)};
						String formats[] = {
							Messages.ImportWizard_0WereImproted, 
							Messages.ImportWizard_1WereImproted,
							Messages.ImportWizard_SeveralWereImproted};
						ChoiceFormat form = new ChoiceFormat(limits, formats);
						
						Object[] messageArgs = {refactorings_to_import.size()};
						MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
						formatter.applyPattern(form.format(refactorings_to_import.size()));
						
						imported=formatter.format(messageArgs)+"."; //$NON-NLS-1$
					}
					
					if(refactorings_to_execute.size()>0){
					// Ejecutamos las refactorizaciones se�aladas para ser
					// ejecutadas
						RefactoringPlanExecutor executeJob = new RefactoringPlanExecutor(refactorings_to_execute,plan,t_Input.getText());
						new CustomProgressDialog(getShell()).run(true, false, executeJob);
						notExecuted = executeJob.getNotExecuted();
						
					}
					if(!notExecuted.isEmpty()){
						
						double limits[] = {0, 1, ChoiceFormat.nextDouble(1)};
						String formats[] = {
							Messages.ImportPlanWizard_0WereExecuted, 
							Messages.ImportPlanWizard_1WereExecuted,
							Messages.ImportPlanWizard_SeveralWereExecuted};
						ChoiceFormat form = new ChoiceFormat(limits, formats);
						
						Object[] messageArgs = {refactorings_to_execute.size()-notExecuted.size()};
						MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
						formatter.applyPattern(form.format(refactorings_to_execute.size()-notExecuted.size()));
						
						String executed=formatter.format(messageArgs)+"."; //$NON-NLS-1$
						
						String problems=Messages.ImportPlanWizard_ProblemsExecuting + ": "; //$NON-NLS-1$
						for(int i=0 ;i<notExecuted.entrySet().size(); i++){
							Map.Entry<String,String> e = (Map.Entry<String,String>)notExecuted.entrySet().toArray()[i]; 		
							if(notExecuted.entrySet().size()>1){
							if(i==(notExecuted.entrySet().size()-2)){
								problems = problems +  e.getKey() + " "+  Messages.ImportPlanWizard_And +" "; //$NON-NLS-1$ //$NON-NLS-2$
							}else if(i==(notExecuted.entrySet().size()-1)){
								problems = problems +  e.getKey();
							}else{
								problems = problems +  e.getKey() + ", "; //$NON-NLS-1$
							}
							}else{
								problems = Messages.ImportPlanWizard_ProblemExecuting + ":" + e.getKey(); //$NON-NLS-2$ //$NON-NLS-1$
							}
					    }
						StringBuffer details = new StringBuffer(Messages.ImportPlanWizard_Problems + ": "); //$NON-NLS-1$
								for(Map.Entry<String,String> e: notExecuted.entrySet()){
									details.append("\n\n\t- "+ Messages.ImportPlanWizard_Refactoring + " " + e.getKey() + " :\n\t" + e.getValue() + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
								}
						
						if((refactorings_to_execute.size() - notExecuted.size())>0){
							details.append("\n\n" + Messages.ImportPlanWizard_Executed+ ":"); //$NON-NLS-1$ //$NON-NLS-2$
							for(Map.Entry<String,String> e: refactorings_to_execute.entrySet()){
								if(!notExecuted.containsKey(e.getKey().substring(0,e.getKey().indexOf("(")-1))){ //$NON-NLS-1$
								    details.append("\n\n\t- " + e.getKey() + " : " + e.getValue() + "." ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								}
							}
						}

						
						if(refactorings_to_import.size()>0){
							details.append("\n\n" + Messages.ImportPlanWizard_Imported + ":"); //$NON-NLS-1$ //$NON-NLS-2$
								for(Map.Entry<String,String> e: refactorings_to_import.entrySet()){
									details.append("\n\n\t- " + e.getKey() + " : " + e.getValue() + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								}
						}
						String message= problems + ".\n" +executed + "\n" + imported; //$NON-NLS-1$ //$NON-NLS-2$

						new InformationDialog(getShell(),Messages.ImportWizard_ImportDone,message,details.toString()).open(); //$NON-NLS-1$ //$NON-NLS-3$
						
				}else{
					double limits[] = {0, 1, ChoiceFormat.nextDouble(1)};
					String formats[] = {
						Messages.ImportPlanWizard_0WereExecuted, 
						Messages.ImportPlanWizard_1WereExecuted,
						Messages.ImportPlanWizard_SeveralWereExecuted};
					ChoiceFormat form = new ChoiceFormat(limits, formats);
					
					Object[] messageArgs = {refactorings_to_execute.size()-notExecuted.size()};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(form.format(refactorings_to_execute.size()-notExecuted.size()));
					
					String executed=formatter.format(messageArgs)+"."; //$NON-NLS-1$
					
					String details =""; //$NON-NLS-1$
						if(refactorings_to_execute.size()>0){
							details = Messages.ImportPlanWizard_Executed +":"; //$NON-NLS-1$
								for(Map.Entry<String,String> e: refactorings_to_execute.entrySet()){
									details = details + "\n\n\t- " + e.getKey() + " : " + e.getValue() + "."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								}
								details = details + "\n"; //$NON-NLS-1$
						}
						
						if(refactorings_to_import.size()>0){
							details =details +  "\n" + Messages.ImportPlanWizard_Imported +":"; //$NON-NLS-1$ //$NON-NLS-2$
								for(Map.Entry<String,String> e: refactorings_to_import.entrySet()){
									details = details + "\n\n\t- " + e.getKey() + " : " + e.getValue() + "."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								}
						}
						
						new InformationDialog(getShell(),Messages.ImportWizard_ImportDone,executed+ //$NON-NLS-1$ //$NON-NLS-2$
								"\n " + imported, details).open(); //$NON-NLS-1$
						
					}
				}
				catch (InterruptedException e) {
				// El usuario cancel� el proceso.
					logger.warn(e.getMessage());
				}
				catch (Exception exception){
					String message = Messages.ImportWizard_NotAllImported +
						":\n\n" + exception.getMessage();  //$NON-NLS-1$
					logger.fatal(message);
					MessageDialog.openError(getShell(), Messages.ImportWizard_Error, message);
				}					

		}
		super.buttonPressed(buttonId);
	}
	
	/**
	 * Actualiza el mensaje de aviso mostrado en la ventana.
	 */
	private void updateMessage() {						
		if (advise == null){
			Object[] messageArgs = {refactorings_to_import.size(), refactorings_to_execute.size()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			if(refactorings_to_import.size()==1){
				if(refactorings_to_execute.size()==1){
					formatter.applyPattern(Messages.ImportPlanWizard_Imported1Executed1);
				}else{
					formatter.applyPattern(Messages.ImportPlanWizard_Imported1Executed);
				}	
			}else{
				if(refactorings_to_execute.size()==1){
					formatter.applyPattern(Messages.ImportPlanWizard_ImportedExecuted1);
				}else{
					formatter.applyPattern(Messages.ImportPlanWizard_ImportedExecuted);
				}
				
			}
			t_Message.setText(formatter.format(messageArgs) + "."); //$NON-NLS-1$
			lb_Icon.setImage(RefactoringImages.getInfoIcon());
		}
		else{
			t_Message.setText(advise);
			lb_Icon.setImage(RefactoringImages. getWarningIcon());
		}
		advise=null;
	}

	/**
	 * Establece el estado del bot�n que permite ejecutar la importaci�n del
	 * plan.
	 */
	private void updateButton(){
		if(refactorings_to_execute.size()==0 && refactorings_to_import.size()==0)
			bt_Execute.setEnabled(false);
		else
			bt_Execute.setEnabled(true);
	}
	
	
	/**
	 * Actualiza la tabla de refactorizaciones encontradas que se pueden importar.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Laura Fuente de la Fuente</A>
	 */
	private class InputListener implements ModifyListener{

		/**
		 * Recibe notificaciones cada vez que se modifica el texto observado.
		 * 
		 * @param e
		 *            el evento de modificaci�n del texto.
		 */
		@Override
		public void modifyText(ModifyEvent e){
			Text field = (Text)e.getSource();
			if(field.getText().endsWith("refactoringPlan")  //$NON-NLS-1$
					&& new File(field.getText().trim()+"/refactorings").exists() //$NON-NLS-1$
					&& new File(field.getText().trim()+"/refactoringPlan.xml").exists()){ //$NON-NLS-1$
			
					
					try {			
						RefactoringSearchJob job = new RefactoringSearchJob(
							field.getText().trim()+ "/refactorings", false);				 //$NON-NLS-1$
						new CustomProgressDialog(getShell()).run(true, true, job);
						plan = RefactoringPlanReader.readAllRefactoringsFromThePlan(
									field.getText()+"/refactoringPlan.xml"); //$NON-NLS-1$
						
						Boolean all=true;
						
						for(String refactoring: plan)
							if(!refactorings.containsKey(refactoring + " (" + refactoring + ".xml)" )) //$NON-NLS-1$ //$NON-NLS-2$
								all=false;
						if(all){
							cleanTable();
							fillInTable();
							advise = Messages.ImportPlanWizard_InitialAdvise;
						}else{
							advise=Messages.ImportPlanWizard_FolderCorrupted;
						}
					}
					catch (Exception exception){
						String message = Messages.ImportWizard_ErrorWhileLooking +
							".\n\n:"  + exception.getMessage(); //$NON-NLS-1$
						logger.fatal(message);
						MessageDialog.openError(getShell(), Messages.ImportWizard_Error, message);
					}
			}else{
			advise = Messages.ImportPlanWizard_NotRefactoringPlanFolder;
		  }
		  updateMessage();
		  updateButton();
		}
	}

	/**
	 * Permite lanzar el trabajo de importaci�n de refactorizaciones y hacer un
	 * seguimiento de su progreso.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
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
			this.names = names.clone();
		}

		/**
		 * Ejecuta el trabajo de importaci�n de refactorizaciones.
		 * 
		 * @param monitor
		 *            el monitor de progreso que deber� usarse para mostrar el
		 *            progreso.
		 * 
		 * @throws InvocationTargetException
		 *             utilizada como envoltura si el m�todo debe propagar una
		 *             excepci�n (<i>checked exception</i>). Las excepciones de
		 *             tipo <i>runtime exception</i> se envuelven
		 *             autom�ticamente en una excepci�n de este tipo por el
		 *             contexto que efect�a la llamada.
		 * @throws InterruptedException
		 *             si la operaci�n detecta una solicitud de cancelaci�n (no
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

					
					// Se obtiene la ruta del fichero con la definici�n.
					String definition = refactorings.get(next);
					String folder = new File(definition).getParent();
				
					try {
						ExportImportUtilities.ImportRefactoring(
								definition, true);
					} catch (FileNotFoundException e) {
						// Elimina la carpeta de la refactorizaci�n ya que
							// si ha llegado
							//a este punto quiere decir que no se ha podido completar la tarea
							//adecuadamente.
							StringTokenizer st_namefolder = new StringTokenizer(folder, "" + File.separatorChar + ""); //$NON-NLS-1$
							String namefolder = ""; //$NON-NLS-1$
							while(st_namefolder.hasMoreTokens()){
								namefolder = st_namefolder.nextElement().toString();
							}
							FileManager.emptyDirectories(RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "" + namefolder); //$NON-NLS-1$
							FileManager.deleteDirectories(RefactoringPlugin.getDynamicRefactoringsDir() + "" + File.separatorChar + "" + namefolder, true); //$NON-NLS-1$
						throw e; //$NON-NLS-1$
	
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
	 * Permite lanzar el trabajo de b�squeda de refactorizaciones y hacer un
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
		 * Si la b�squeda ha de ser recursiva o no.
		 */
		private boolean recursive;

		/**
		 * Constructor.
		 * 
		 * @param folder
		 *            directorio a partir del que se deben buscar las
		 *            refactorizaciones.
		 * @param recursive
		 *            si la b�squeda debe ser recursiva o no.
		 */
		public RefactoringSearchJob(String folder, boolean recursive){
			this.folder = folder;
			this.recursive = recursive;
		}

		/**
		 * Ejecuta el trabajo de b�squeda de refactorizaciones.
		 * 
		 * @param monitor
		 *            el monitor de progreso que deber� usarse para mostrar el
		 *            progreso.
		 * 
		 * @throws InvocationTargetException
		 *             utilizada como envoltura si el m�todo debe propagar una
		 *             excepci�n (<i>checked exception</i>). Las excepciones de
		 *             tipo <i>runtime exception</i> se envuelven
		 *             autom�ticamente en una excepci�n de este tipo por el
		 *             contexto que efect�a la llamada.
		 * @throws InterruptedException
		 *             si la operaci�n detecta una solicitud de cancelaci�n (no
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
				
				String[] names = refactorings.keySet().toArray(new String[refactorings.keySet().size()]);
				monitor.beginTask(Messages.ImportWizard_Validating, names.length);
				for (int i = 0; i < names.length; i++){
					String path = refactorings.get(names[i]);
					
					Object[] messageArgs = {names[i]};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.ImportWizard_ValidatingFile);
					monitor.subTask(formatter.format(messageArgs) + "..."); //$NON-NLS-1$
					
					try {
						// Se intenta obtener la definici�n de la siguiente
						// refactorizaci�n.
						DynamicRefactoringDefinition.getRefactoringDefinition(path);
						
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
						
					}
					catch (Exception exception){
						refactorings.remove(names[i]);
					}
					
					monitor.worked(1);
					
					if (refactorings.size() == 0)
						advise = Messages.ImportWizard_NoneFound;
				}
				
				refactorings_to_execute=new HashMap<String,String>();
				refactorings_to_execute.putAll(refactorings);
				refactorings_to_import = new HashMap<String,String> ();
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