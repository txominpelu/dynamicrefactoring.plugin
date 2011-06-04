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

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Implementa el proceso de elección de un directorio.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class FolderSelectionListener implements SelectionListener {

	/**
	 * Ruta del directorio seleccinado.
	 */
	private Text field;
	
	/**
	 * <i>Shell</i> utilizada para la apertura del diálogo.
	 */
	private Shell shell;
	
	/**
	 * Mensaje explicativo de la función que tendrá el directorio seleccionado.
	 */
	private String message;
	
	/**
	 * Constructor.
	 * 
	 * @param field campo de texto en que se almacenará la ruta del directorio
	 * seleccionado por el usuario.
	 * @param shell la <i>shell</i> en la que se mostrará el diálogo.
	 * @param text el mensaje explicativo que llevará asociado el diálogo.
	 */
	public FolderSelectionListener(Text field, Shell shell, String text){
		this.field = field;
		this.shell = shell;
		this.message = text;
	}
	
	/**
	 * Recibe una notificación de que se ha pulsado el botón que permite 
	 * seleccionar un directorio.
	 * 
	 * <p>Abre una ventana de selección de directorio.</p>
	 * 
	 * @param e el evento de selección disparado.
	 * 
	 * @see SelectionListener#widgetSelected(SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
	    DirectoryDialog chooser = new DirectoryDialog(shell, SWT.OPEN);
	    
	    chooser.setText(Messages.FolderSelectionListener_SelectFolder);
	    chooser.setMessage(message);
	    chooser.setFilterPath(Platform.getLocation().toOSString()
	    	+ File.separatorChar + ".."); //$NON-NLS-1$ //$NON-NLS-2$

	    String returnVal = chooser.open();
	    if (returnVal != null)
	    	field.setText(returnVal);
	}
	
	/**
	 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
}
