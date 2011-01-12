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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.util.DynamicRefactoringLister;

/**
 * Proporciona el comportamiento abstracto por defecto común a las ventanas de
 * diálogo que deben mostrar una lista seleccionable con el conjunto de
 * refactorizaciones dinámicas disponibles.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class DynamicRefactoringList extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	protected static Logger logger = Logger.getLogger(DynamicRefactoringList.class);

	/**
	 * Tabla de refactorizaciones disponibles.
	 * 
	 * <p>
	 * Se utiliza como clave el nombre de la refactorización y como valor la
	 * propia representación de la definición de la refactorización.
	 * </p>
	 */
	protected HashMap<String, DynamicRefactoringDefinition> refactorings;
	
	/**
	 * Tabla con las rutas de los ficheros asociados a las refactorizaciones.
	 */
	protected HashMap<String, String> refactoringLocations;
	
	/**
	 * Nombres de las refactorizaciones disponibles.
	 */
	protected ArrayList<String> refactoringNames;
	
	/**
	 * Lista de refactorizaciones disponibles.
	 */
	protected List l_Available;

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            <i>shell</i> padre del diálogo.
	 */
	public DynamicRefactoringList(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            <i>shell</i> padre del diálogo.
	 */
	public DynamicRefactoringList(IShellProvider parentShell) {
		super(parentShell);
	}

	/**
	 * Carga la lista de refactorizaciones dinámicas disponibles.
	 */
	protected void loadRefactorings() {
		DynamicRefactoringLister listing = DynamicRefactoringLister.getInstance();
		
		try {
			// Se obtiene la lista de todas las refactorizaciones disponibles.
			HashMap<String, String> allRefactorings = 
				listing.getDynamicRefactoringNameList(
					RefactoringPlugin.getDynamicRefactoringsDir(), true, null);
			
			refactoringNames = new ArrayList<String>();
			refactoringLocations = new HashMap<String, String>();
			refactorings = new HashMap<String, DynamicRefactoringDefinition>();
			
			for (Map.Entry<String, String> nextRef : allRefactorings.entrySet()){
	
				try {
					// Se obtiene la definición de la siguiente refactorización.
					DynamicRefactoringDefinition definition = 
						DynamicRefactoringDefinition.getRefactoringDefinition(
							nextRef.getValue());
				
					if (definition != null && definition.getName() != null){
						refactorings.put(definition.getName(), definition);
						refactoringLocations.put(definition.getName(), 
							nextRef.getValue());
						refactoringNames.add(definition.getName());						
					}
				}
				catch(RefactoringException e){
					String message = 
						Messages.DynamicRefactoringList_NotAllListed + 
						".\n" + e.getMessage(); //$NON-NLS-1$
					logger.error(message);
					MessageDialog.openError(getShell(), Messages.DynamicRefactoringList_Error, message);
				}
			}
		}		
		catch(IOException e){
			logger.error(
				Messages.DynamicRefactoringList_AvailableNotListed + 
				".\n" + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Puebla la tabla de refactorizaciones disponibles con los nombres
	 * ordenados alfabéticamente de las refactorizaciones.
	 */
	protected void fillInRefactoringList(){
		Collections.sort(refactoringNames);
		for (String name : refactoringNames)
			l_Available.add(name);	
					
		if(refactorings.size() == 0){
			l_Available.add(Messages.DynamicRefactoringList_NoneFound);
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

}