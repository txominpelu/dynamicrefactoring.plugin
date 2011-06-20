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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import dynamicrefactoring.RefactoringImages;

/**
 * Permite mostrar los detalles de una determinada tarea que se acaba de ejecutar. Por 
 * ejemplo los detalles de las refactorizaciones ejecitadas en un plan de 
 * refactorizaciones.
 * 
 *  @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class InformationDialog extends Dialog {

	/**
	 * Título asociado al diálogo.
	 */
	String title;
	
	/**
	 * Mensaje que muestra el diálogo.
	 */
	String message;
	
	/**
	 * Detalles que muestra en diálogo.
	 */
	String details;
	
	/**
	 * Crea el diálogo.
	 * 
	 * @param parentShell Venta sobre la que se abrirá el diálogo.
	 * @param title titulo.
	 * @param message mensaje del diálogo.
	 * @param details detalles.
	 */
	public InformationDialog(Shell parentShell, String title, String message,String details) {
		super(parentShell);
		this.title= title;
		this.message=message;
		this.details=details;
	}

	/**
	 * Crea los contenidos del diálogo.
	 * 
	 * @param parent Compuesto padre.
	 * @return  un objeto Control.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		final Label label = new Label(container, SWT.WRAP|SWT.NONE);
		label.setBounds(57, 24, 409, 57);
		label.setText(message);

		final StyledText styledText = new StyledText(container, SWT.WRAP| SWT.V_SCROLL | SWT.READ_ONLY | SWT.MIRRORED | SWT.BORDER);
		styledText.setBounds(10, 99, 469, 111);
		styledText.setText(details);

		final Label icon = new Label(container, SWT.NONE);
		icon.setImage(RefactoringImages.getInfoIcon());
		icon.setBounds(10, 10, 41, 57);
		
		final Label details = new Label(container, SWT.NONE);
		details.setText(Messages.InformationDialog_details + ":");
		details.setBounds(10, 84, 57, 13);
		
		return container;
	}

	/**
	 * Crea el contenidod de la barra de botones.
	 * 
	 * @param parent compuesto padre al que se asocia al barra de botones.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Devuelve el tamaño inicial del diálogo.
	 * @return tamaño inicial
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 294);
	}
	
	/**
	 * Configuración inicial de la ventana.
	 * 
	 * @param newShell nueva shell
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}
}
