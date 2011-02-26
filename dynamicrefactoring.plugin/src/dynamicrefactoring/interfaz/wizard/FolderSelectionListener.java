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

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Implementa el proceso de elecci�n de un directorio.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class FolderSelectionListener implements SelectionListener {

	/**
	 * Ruta del directorio seleccinado.
	 */
	private Text field;
	
	/**
	 * <i>Shell</i> utilizada para la apertura del di�logo.
	 */
	private Shell shell;
	
	/**
	 * Mensaje explicativo de la funci�n que tendr� el directorio seleccionado.
	 */
	private String message;
	
	/**
	 * Constructor.
	 * 
	 * @param field campo de texto en que se almacenar� la ruta del directorio
	 * seleccionado por el usuario.
	 * @param shell la <i>shell</i> en la que se mostrar� el di�logo.
	 * @param text el mensaje explicativo que llevar� asociado el di�logo.
	 */
	public FolderSelectionListener(Text field, Shell shell, String text){
		this.field = field;
		this.shell = shell;
		this.message = text;
	}
	
	/**
	 * Recibe una notificaci�n de que se ha pulsado el bot�n que permite 
	 * seleccionar un directorio.
	 * 
	 * <p>Abre una ventana de selecci�n de directorio.</p>
	 * 
	 * @param e el evento de selecci�n disparado.
	 * 
	 * @see SelectionListener#widgetSelected(SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
	    DirectoryDialog chooser = new DirectoryDialog(shell, SWT.OPEN);
	    
	    chooser.setText(Messages.FolderSelectionListener_SelectFolder);
	    chooser.setMessage(message);
	    chooser.setFilterPath(Platform.getLocation().toOSString()
	    	+ System.getProperty("file.separator") + ".."); //$NON-NLS-1$ //$NON-NLS-2$

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
