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

package dynamicrefactoring.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.interfaz.wizard.FolderSelectionListener;

/**
 * Página de preferencias para determinar el directorio de importación de un plan de refactorizaciones y el directorio de 
 * exportación del mismo .
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class DirectoriesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Directorio de importación de un plan de refactorizaciones.
	 */
	private Text t_Import;
	
	/**
	 * Directorio de exportación de un plan de refactorizaciones.
	 */
	private Text t_Export;
	
	
	/**
	 * Crea el contenido de la página de preferencias.
	 * 
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(null);

		
		t_Import = new Text(container, SWT.BORDER);
		t_Import.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Import.setEnabled(false);
		t_Import.setToolTipText(Messages.DirectoriesPreferencePage_ImportPlanDirectory);
		t_Import.setBounds(10, 34, 340, 25);

		final Button bt_Examine = new Button(container, SWT.NONE);
		bt_Examine.setText("..."); //$NON-NLS-1$
		bt_Examine.setBounds(374, 35, 24, 23);
		bt_Examine.addSelectionListener(new FolderSelectionListener(
			t_Import, getShell(), Messages.DirectoriesPreferencePage_SelectDirectory +
			".")); //$NON-NLS-1$

		final Label lb_Input = new Label(container, SWT.NONE);
		lb_Input.setText(Messages.DirectoriesPreferencePage_ImportPlanDirectory);
		lb_Input.setBounds(10, 15, 353, 13);
		

		t_Export = new Text(container, SWT.BORDER);
		t_Export.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Export.setEnabled(false);
		t_Export.setToolTipText(Messages.DirectoriesPreferencePage_ExportPlanDirectory);
		t_Export.setBounds(10, 89, 340, 25);

		final Button bt_Examine2 = new Button(container, SWT.NONE);
		bt_Examine2.setText("..."); //$NON-NLS-1$
		bt_Examine2.setBounds(374, 90, 24, 23);
		bt_Examine2.addSelectionListener(new FolderSelectionListener(
			t_Export, getShell(), Messages.DirectoriesPreferencePage_SelectDirectory +
			".")); 

		final Label lb_Export = new Label(container, SWT.NONE);
		lb_Export.setText(Messages.DirectoriesPreferencePage_ExportPlanDirectory);
		lb_Export.setBounds(10, 70, 353, 13);
		
		return container;
	}

	/**
	 * Inicializa el almacén de preferencias.
	 * 
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		//Initialize the preference store we wish to use
		setPreferenceStore(RefactoringPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * Establece los valores por defecto de los directorioa de importación de un plan 
	 * de refactorizaciones y de exportación del mismo.
	 */
	protected void performDefaults() {
		t_Import.setText(RefactoringPlugin.getDefault().getDefaultImportRefactoringPlanPreference());
		t_Export.setText(RefactoringPlugin.getDefault().getDefaultExportRefactoringPlanPreference());
	}
	/** 
	 * Metodo declarado en <code>IPreferencePage</code>. Guarda en el almacén de preferencias 
	 * el directorio de importación de un plan de refactorizaciones y el directorio de 
	 * exportación del mismo.
	 */
	public boolean performOk() {
		RefactoringPlugin.getDefault().setImportRefactoringPlanPreference(t_Import.getText());
		RefactoringPlugin.getDefault().setExportRefactoringPlanPreference(t_Export.getText());
		return super.performOk();
	}

	

}