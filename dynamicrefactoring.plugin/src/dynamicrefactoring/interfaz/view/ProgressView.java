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

package dynamicrefactoring.interfaz.view;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.RefactoringSummary;
import dynamicrefactoring.listener.IRefactoringRunListener;

/**
 * Proporciona una vista de Eclipse en la que quedan reflejados todos los 
 * pasos que una refactorización lleva acabo durante su ejecuci�n.
 * 
 * <p>Proporciona información detallada en tiempo real de los pasos concretos que 
 * componen la refactorización que se est� ejecutando.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ProgressView extends ViewPart 
	implements IRefactoringRunListener {
	
	/**
	 * Identificador de la vista.
	 */
	public static final String ID = "dynamicrefactoring.views.progressView"; //$NON-NLS-1$
	
	/**
	 * Tabulador imprimible en la tabla.
	 */
	private static final String TAB = "        "; //$NON-NLS-1$

	/**
	 * Color verde.
	 */
	private Color green;
	
	/**
	 * Color azul.
	 */
	private Color blue;
	
	/**
	 * Color rojo.
	 */
	private Color red;
	
	/**
	 * Color cyan.
	 */
	private Color cyan;

	/**
	 * Tabla en la que se muestran los mensajes de progreso de la refactorización.
	 */
	private Table tb_Messages;
	
	/**
	 * Anchura m�xima de la consola.
	 */
	private static final int TABLE_WIDTH = 90;
	
	/**
	 * Datos de una refactorización que ha fallado y cuyo mensaje de error
	 * est� pendiente de ser mostrado al usuario.
	 */
	private String[] failedRefactoring;
		
	/**
	 * Mensajes enviados por la refactorización durante su ejecuci�n.
	 */
	private ArrayList<String> stepMessages;
	
	@Override
	public void createPartControl(Composite parent) {
		
		green = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
		blue = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		red = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
		cyan = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN);
				
		// Se registra la vista de consola como listener.
		RefactoringPlugin.getDefault().addRefactoringListener(this);
		
		tb_Messages = new Table(parent, SWT.BORDER);
		tb_Messages.setLinesVisible(true);
		tb_Messages.setHeaderVisible(true);

		final TableColumn cl_Time = new TableColumn(tb_Messages, SWT.NONE);
		tb_Messages.setSortColumn(cl_Time);
		cl_Time.setWidth(100);
		cl_Time.setText(Messages.ProgressView_Time);

		final TableColumn cl_Message = new TableColumn(tb_Messages, SWT.NONE);
		cl_Message.setWidth(390);
		cl_Message.setText(Messages.ProgressView_Message);
	}
	
	@Override
	public void setFocus() {}
	
	@Override
	public void dispose(){
		RefactoringPlugin.getDefault().removeRefactoringListener(this);
	}
	
	/**
	 * Notifica al <i>listener</i> acerca del comienzo de una refactorización.
	 * 
	 * @param name nombre de la refactorización que ha comenzado.
	 * 
	 * @see IRefactoringRunListener#refactoringStarted(String)
	 */
	@Override
	public void refactoringStarted(String name){
		cleanTable();
				
		stepMessages = new ArrayList<String>();
		
		Object[] messageArgs = {name};
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.ProgressView_RefactoringStarted);
		
		addMessage(formatString(formatter.format(messageArgs) +
			"."), cyan, true); //$NON-NLS-1$
	}
	
	/**
	 * Notifica al <i>listener</i> acerca de la realización de un paso concreto
	 * en una refactorización.
	 *
	 * @param message mensaje asociado al paso realizado.
	 * 
	 * @see IRefactoringRunListener#refactoringStepTaken(String)
	 */
	@Override
	public void refactoringStepTaken(String message){
		stepMessages.add(message);
	}
	
	/**
	 * Notifica al <i>listener</i> acerca de un fallo en una refactorización.
	 * 
	 * @param name nombre de la refactorización que ha fallado.
	 * @param message mensaje asociado al fallo de la refactorización.
	 * 
	 * @see IRefactoringRunListener#refactoringFailed(String, String)
	 */
	@Override
	public void refactoringFailed(String name, String message){
		failedRefactoring = new String[]{name, message};
	}
	
	/**
	 * Notifica al <i>listener</i> acerca de la finalización de una refactorización.
	 * 	
	 * @param summary resumen informativo de la refactorización que ha finalizado.
	 * 	
	 * @see IRefactoringRunListener#refactoringFinished(RefactoringSummary)
	 */
	@Override
	public void refactoringFinished(RefactoringSummary summary){
		if(stepMessages != null ){
		for (String message : stepMessages)
			addMessage(formatString(message), blue, false);
		}
		// Si la refactorización termin� correctamente.
		if (failedRefactoring == null) {
			Object[] messageArgs = {summary.getName(), "(" + //$NON-NLS-1$
				summary.getStrCompletionTime() + ")"}; //$NON-NLS-1$
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.ProgressView_RefactoringFinished);
			
			addMessage(formatString(formatter.format(messageArgs) +
				"."), green, true); //$NON-NLS-1$
		}

		// Si se produjo algún fallo.
		else {
			Object[] messageArgs = {failedRefactoring[0]};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.ProgressView_RefactoringFailed);
			
			addMessage(formatString(formatter.format(messageArgs) +
				"."), red, true); //$NON-NLS-1$
			addMessage(formatString(
				Messages.ProgressView_Details + failedRefactoring[1]), red, true);
			
			// Se muestra el mensaje de error que deber� haberse almacenado.
			MessageDialog.openError(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				formatter.format(messageArgs),
				failedRefactoring[1]);
			// Se resetea el contenido de #failedRefactoring.
			failedRefactoring = null;
		}
	}
	
	/**
	 * Notifica al <i>listener</i> acerca del hecho de que se haya recuperado el
	 * estado anterior a una refactorización.
	 * 
	 * @param name identificador de la refactorización cuyo estado anterior
	 * se ha recuperado.
	 */
	@Override
	public void refactoringUndone(String name){
		cleanTable();
		
		Object[] messageArgs = {name};
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.ProgressView_RefactoringUndone);
		
		addMessage(formatString(formatter.format(messageArgs) + "."), cyan, true); //$NON-NLS-1$
	}
	
	/**
	 * Elimina los elementos de la tabla.
	 */
	public void cleanTable(){
		TableItem[] items = tb_Messages.getItems();
		for (int i = items.length - 1; i > -1; i--)
			items[i].dispose();
		tb_Messages.removeAll();
		
	}
	
	/**
	 * A�ade un mensaje a la tabla de mensajes de progreso.
	 * 
	 * @param msg mensaje que se debe a�adir.
	 * @param color color con que se a�adir� el mensaje.
	 * @param showdate si se debe mostrar la hora junto al mensaje o no.
	 */
	private void addMessage(String[] msg, Color color, boolean showdate){
		TableItem item = new TableItem(tb_Messages, SWT.BORDER);
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
		String date = (showdate) ? dateFormat.format(new Date()) : ""; //$NON-NLS-1$
		
		item.setText(new String[]{date, msg[0], ""}); //$NON-NLS-1$
		item.setForeground(color);
		
		for (int i = 1; i < msg.length; i++){
			addMessage(new String[]{msg[i]}, color, showdate);
		}
	}
	
	/**
	 * Formatea una cadena de manera adecuada para que se adapte a la representación
	 * en forma de tabla de la vista, separ�ndola en fragmentos en aquellos puntos que
	 * contengan un salto de l�nea, y sustituyendo las tabulaciones por espacios
	 * en blanco.
	 * 
	 * @param original la cadena que se debe formatear.
	 * 
	 * @return un <i>array</i> de cadenas adaptadas a la tabla.
	 */
	private String[] formatString(String original){

		ArrayList<String> strings = new ArrayList<String>();
		
		String[] lines = original.split("\n"); //$NON-NLS-1$
		
		for (int i = 0; i < lines.length; i++){
			String[] sublines = splitString(lines[i]);
			for (int j = 0; j < sublines.length; j++)
				strings.add(sublines[j].replaceAll("\t", TAB)); //$NON-NLS-1$
		}		
		
		return strings.toArray(new String[strings.size()]);
	}
	
	/**
	 * Divide una cadena en subcadenas de un tama�o menor o igual a tres
	 * cuartos del ancho de la tabla de salida.
	 * 
	 * <p>Las cadenas conservan los saltos de l�nea de la cadena original, 
	 * as� como la correcta separación entre palabras. Los nuevos saltos de
	 * l�nea se insertan en los espacios blancos entre palabras.</p>
	 * 
	 * @param original cadena original.
	 * 
	 * @return un <i>array</i> con las subcadenas obtenidas a partir de la
	 * cadena original, cada una de las cuales podr�a imprimirse sobre la 
	 * tabla de salida de la vista con una longitud adecuada.
	 */
	private String[] splitString(String original){
		
		int width = TABLE_WIDTH * 3/4;
		
		ArrayList<String> strings = new ArrayList<String>();
		
		if (original.length() > width){
			// Se busca el primer espacio blanco que supera la 
			// longitud m�xima prevista para cada l�nea.
			int index = 0;
			while(index < original.length()){
				index = original.indexOf(' ', index);
				if (index >= width || index >= original.length() - 1){
					if (index >= original.length() - 1)
						break;
					strings.add(original.substring(0, index));
					original = TAB + original.substring(index + 1);
					index = 0;					
				}
				if (index == -1)
					break;
				index++;
			}

			// Se a�ade el �ltimo fragmento.
			strings.add(original);
			
			return strings.toArray(new String[strings.size()]);
		}
		else
			return new String[]{original};
	}
}