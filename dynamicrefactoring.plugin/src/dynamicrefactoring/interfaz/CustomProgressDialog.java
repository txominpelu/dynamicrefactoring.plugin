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

package dynamicrefactoring.interfaz;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;;

/**
 * Proporciona una especialización del diálogo de progreso de Eclipse.
 * 
 * <p>Permite internacionalizar los textos de los botones y el título del diálogo 
 * de progreso por defecto utilizado en Eclipse, que de otra forma tendrían siempre
 * los textos por defecto en inglés.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class CustomProgressDialog extends ProgressMonitorDialog {

	/**
	 * Constructor.
	 * 
	 * @param shell <i>shell</i> en la que se abrirá el diálogo de progreso.
	 */
	public CustomProgressDialog(Shell shell){
		super(shell);
	}
	
	/**
	 * Crea el botón de cancelación.
	 * 
	 * @param parent el componente padre del botón.
	 */
	@Override
	protected void createCancelButton(Composite parent) {
		super.createCancelButton(parent);
		
		if (this.getButton(IDialogConstants.CANCEL_ID) != null)
			this.getButton(IDialogConstants.CANCEL_ID).setText(
				ButtonTextProvider.getCancelText());
	}
	
	/**
	 * Configura la <i>shell</i> en que se abre el diálogo de progreso.
	 * 
	 * @param shell la <i>shell</i> para el diálogo de progreso.
	 */
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		
		shell.setText(Messages.CustomProgressDialog_ProgressInfo);
	}
}