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

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.interfaz.wizard.classificationscombo.PickCategoryTree;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsReaderFactory;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.xml.bind.ValidationException;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.SWT;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * S�ptima p�gina del asistente de creaci�n o edici�n de refactorizaciones.
 * 
 * <p>Muestra un resumen con la configuraci�n actual de la refactorizaci�n,
 * para que el usuario pueda analizarlo antes de dar su conformidad para la
 * modificaci�n definitiva o la creaci�n de la refactorizaci�n, seg�n el caso.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringWizardPage7 extends WizardPage {

	/**
	 * �rbol sobre el que se mostrar�n de forma estructurada los diferentes elementos
	 * del repositorio que componen la refactorizaci�n (precondiciones, acciones y 
	 * postcondiciones).
	 */
	private Tree tr_Components;
	
	/**
	 * Cuadro de texto en que se mostrar� la motivaci�n de la refactorizaci�n.
	 */
	private Text t_Motivation;
	
	/**
	 * Cuadro de texto en que se mostrar� la descripci�n de la refactorizaci�n.
	 */
	private Text t_Description;
	
	/**
	 * Tabla en que se mostrar�n las entradas de la refactorizaci�n.
	 */
	private Table tb_Inputs;

	/**
	 * Primera p�gina del asistente a trav�s del que se ha compuesto la 
	 * refactorizaci�n.
	 */
	private RefactoringWizardPage1 firstPage;

	/**
	 * Constructor.
	 * 
	 * @param firstPage primer p�gina del asistente a trav�s del que se ha
	 * compuesto la refactorizaci�n.
	 */
	public RefactoringWizardPage7(IWizardPage firstPage){
		super("Wizard page"); //$NON-NLS-1$
		
		if (firstPage != null && firstPage instanceof RefactoringWizardPage1)
			this.firstPage = (RefactoringWizardPage1)firstPage;
		
		setPageComplete(true);
	}

	/**
	 * Crea el contenido de la p�gina del asistente.
	 * 
	 * @param parent el elemento padre de esta p�gina.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		
		setControl(container);

		tb_Inputs = new Table(container, SWT.BORDER);
		tb_Inputs.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		tb_Inputs.setEnabled(false);
		tb_Inputs.setLinesVisible(true);
		tb_Inputs.setHeaderVisible(true);
		tb_Inputs.setBounds(0, 185, 381, 116);
		
		try {
			PickCategoryTree picker = new PickCategoryTree(container,ClassificationsReaderFactory
					.getReader(
							ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER)
					.readClassifications(RefactoringConstants.CLASSIFICATION_TYPES_FILE),firstPage.getCategories(), new Rectangle(0, 330, 381, 116), false);
		} catch (ValidationException e) {
			// FIXME Reemplazar por catalogo de clasificaciones
			e.printStackTrace();
		}
		
		// Se crean las columnas de la tabla de entradas.
		TableColumn cl_Name = new TableColumn(tb_Inputs, SWT.NONE);
		cl_Name.setText(Messages.RefactoringWizardPage7_Name);
		cl_Name.setWidth(102);
		TableColumn cl_Type = new TableColumn(tb_Inputs, SWT.NONE);
		cl_Type.setText(Messages.RefactoringWizardPage7_Type);
		cl_Type.setWidth(150);
		TableColumn cl_From = new TableColumn(tb_Inputs, SWT.NONE);
		cl_From.setText(Messages.RefactoringWizardPage7_From);
		cl_From.setWidth(100);
		TableColumn cl_Root = new TableColumn(tb_Inputs, SWT.NONE);
		cl_Root.setResizable(false);
		cl_Root.setText(Messages.RefactoringWizardPage7_Main);
		cl_Root.setWidth(23);
		cl_Root.setToolTipText(Messages.RefactoringWizardPage7_MainTooltip);

		final Label lb_Description = new Label(container, SWT.CENTER);
		lb_Description.setAlignment(SWT.CENTER);
		lb_Description.setText(Messages.RefactoringWizardPage7_Description);
		lb_Description.setBounds(0, 0, 381, 13);

		t_Description = new Text(container, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		t_Description.setEnabled(false);
		t_Description.setEditable(false);
		t_Description.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Description.setBounds(0, 19, 381, 40);

		final Label lb_Motivation = new Label(container, SWT.CENTER);
		lb_Motivation.setAlignment(SWT.CENTER);
		lb_Motivation.setText(Messages.RefactoringWizardPage7_Motivation);
		lb_Motivation.setBounds(0, 82, 381, 13);

		t_Motivation = new Text(container, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		t_Motivation.setEnabled(false);
		t_Motivation.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Motivation.setEditable(false);
		t_Motivation.setBounds(0, 101, 381, 40);

		tr_Components = new Tree(container, SWT.BORDER);
		tr_Components.setBounds(397, 19, 237, 282);

		final Label lb_Inputs = new Label(container, SWT.CENTER);
		lb_Inputs.setAlignment(SWT.CENTER);
		lb_Inputs.setText(Messages.RefactoringWizardPage7_Inputs);
		lb_Inputs.setBounds(0, 166, 381, 13);

		final Label lb_Mechanism = new Label(container, SWT.CENTER);
		lb_Mechanism.setText(Messages.RefactoringWizardPage7_Mechanism);
		lb_Mechanism.setBounds(397, 0, 237, 13);
		
		setPageComplete(false);
	}

	/**
	 * Puebla los campos de la p�gina del asistente con la informaci�n que se
	 * pueda obtener de la refactorizaci�n existente que se est� editando o
	 * que se est� a punto de crear.
	 */
	private void fillInRefactoringData(){
		setPageComplete(true);
		setDescription(Messages.RefactoringWizardPage7_Summary +
			": " + firstPage.getNameText().getText().trim()); //$NON-NLS-1$
		
		t_Description.setText(firstPage.getDescriptionText().getText().trim());
		t_Motivation.setText(firstPage.getMotivationText().getText().trim());
		
		IWizardPage secondPage = firstPage.getNextPage();
		if (secondPage != null && secondPage instanceof RefactoringWizardPage2){
			ArrayList<String[]> inputs = 
				((RefactoringWizardPage2)secondPage).getInputs();
			for (String[] input : inputs){
				TableItem item = new TableItem(tb_Inputs, SWT.BORDER);
				item.setText(new String[]{input[1], input[0], input[2], "", ""}); //$NON-NLS-1$ //$NON-NLS-2$
				
				TableEditor editor = new TableEditor(tb_Inputs);
				Button checkButton = new Button(tb_Inputs, SWT.CHECK);
				if (input[4] != null && input[4].equals("true")) //$NON-NLS-1$
					checkButton.setSelection(true);
				checkButton.setEnabled(false);
				checkButton.pack();
				editor.minimumWidth = checkButton.getSize().x;
				editor.horizontalAlignment = SWT.CENTER;
				editor.setEditor(checkButton, item, 3);
			}
			
			IWizardPage thirdPage = secondPage.getNextPage();
			IWizardPage fourthPage = thirdPage.getNextPage();
			IWizardPage fifthPage = fourthPage.getNextPage();
			if (thirdPage != null && thirdPage instanceof RefactoringWizardPage3
					&& fourthPage instanceof RefactoringWizardPage4
					&& fifthPage instanceof RefactoringWizardPage5){
				ArrayList<String> preconditions = 
					((RefactoringWizardPage3)thirdPage).getPreconditions();
				ArrayList<String> actions = 
					((RefactoringWizardPage4)fourthPage).getActions();
				ArrayList<String> postconditions = 
					((RefactoringWizardPage5)fifthPage).getPostconditions();
								
				
				TreeItem preconditionsChild = TreeEditor.createBranch(tr_Components, 0,
					Messages.RefactoringWizardPage7_Preconditions, "icons" + System.getProperty("file.separator") + "check.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				TreeItem actionsChild = TreeEditor.createBranch(tr_Components, 1, 
					Messages.RefactoringWizardPage7_Actions, "icons" + System.getProperty("file.separator") + "run.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				TreeItem postconditionsChild = TreeEditor.createBranch(tr_Components, 2,
					Messages.RefactoringWizardPage7_Postconditions, "icons" + System.getProperty("file.separator") + "validate.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				
				TreeEditor.fillInTreeBranch(preconditions, preconditionsChild, 
					"icons" + System.getProperty("file.separator") + "check.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				TreeEditor.fillInTreeBranch(actions, actionsChild, 
					"icons" + System.getProperty("file.separator") + "run.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				TreeEditor.fillInTreeBranch(postconditions, postconditionsChild, 
					"icons" + System.getProperty("file.separator") + "validate.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}
		
	/**
	 * Hace visible o invisible la p�gina del asistente.
	 * 
	 * @param visible si la p�gina se debe hacer visible o no.
	 */
	@Override
	public void setVisible(boolean visible){
		Object[] messageArgs = {((RefactoringWizard)getWizard()).getOperationAsString()};
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.RefactoringWizardPage7_DynamicRefactoring);
		
		setTitle(formatter.format(messageArgs) + " (" + //$NON-NLS-1$
			Messages.RefactoringWizardPage7_Step 
			+ ") -" + Messages.RefactoringWizardPage7_Confirmation); //$NON-NLS-1$
		
		if (visible){
			clean();
			fillInRefactoringData();
		}
		super.setVisible(visible);
	}
	
	/**
	 * Limpia toda la informaci�n de resumen contenida en la p�gina hasta el momento.
	 */
	private void clean(){		
		if (tb_Inputs.getItemCount() > 0){
			TableItem[] items = tb_Inputs.getItems();
			for (int i = items.length - 1; i >= 0; i--)
				items[i].dispose();
		}
		
		if (tr_Components.getItemCount() > 0){
			TreeItem[] items = tr_Components.getItems();
			for (int i = items.length - 1; i >= 0; i--)
				items[i].dispose();
		}
	}
}