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
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.util.RepositoryElementLister;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap; 

import org.apache.log4j.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.FormLayout;

import org.eclipse.swt.widgets.Composite;

/**
 * Tercera página del asistente de creación o edición de refactorizaciones.
 * 
 * <p>Permite componer la refactorización mediante la adición de predicados
 * a la lista de precondiciones o postcondiciones, y de acciones que 
 * implementen las modificaciones llevadas a cabo por la refactorización.</p>
 * 
 * <p>Permite definir el orden en que se comprobarán los predicados y en que
 * se ejecutarán las acciones, así como la lista de entradas de la 
 * refactorización que deberán ser transmitidas a cada uno de los componentes.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class RefactoringWizardPage5 extends WizardPage implements IRefactoringWizardElementPage {

	/**
	 * Título de la operación sobre la que se configuran las postcondiciones de la 
	 * refactorización.
	 */
	protected static final String POSTCONDITIONS_TITLE = Messages.RefactoringWizardPage3_Postconditions;
	
	/**
	 * Refactorización configurada a través del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorización) o modificada (si se
	 * está editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringWizardPage5.class);
	
	
	
	/**
	 * Contenedor para la configuración de las postcondiciones de la refactorización.
	 */
	private RepositoryElementComposite postconditionsTab;
	

	
	/**
	 * Constructor.
	 * 
	 * @param refactoring la refactorización que se está editando, o <code>
	 * null</code> si se está construyendo una nueva.
	 */
	public RefactoringWizardPage5(DynamicRefactoringDefinition refactoring) {
		super("Wizard page"); //$NON-NLS-1$
		setDescription(Messages.RefactoringWizardPage5_DescriptionInputPostconditions);
		
		this.refactoring = refactoring;
	}
	
	/**
	 * Hace visible o invisible la página del asistente.
	 * 
	 * @param visible si la página se debe hacer visible o no.
	 */
	@Override
	public void setVisible(boolean visible){
		if (visible){
			Object[] messageArgs = {((RefactoringWizard)getWizard()).getOperationAsString()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.RefactoringWizardPage3_DynamicRefactoring);
			
			setTitle(formatter.format(messageArgs) + " (" + //$NON-NLS-1$
				Messages.RefactoringWizardPage5_Step + ")"); //$NON-NLS-1$
			
			postconditionsTab.deselect();
		}
		super.setVisible(visible);
	}

	/**
	 * Crea el contenido de la página del asistente.
	 * 
	 * @param parent el elemento padre de esta página del asistente.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());

		setControl(container);
		
		// Las precondiciones necesitan un elemento a través del que acceder a las
		// entradas de la refactorización.
		RefactoringWizardPage2 inputsPage = null;
		if (getPreviousPage().getPreviousPage().getPreviousPage() instanceof RefactoringWizardPage2)
			inputsPage = (RefactoringWizardPage2)getPreviousPage().getPreviousPage().getPreviousPage();
	
		postconditionsTab = new RepositoryElementComposite(
			container, POSTCONDITIONS_TITLE, inputsPage,this);
		
		// Se completan las listas de elementos del repositorio candidatos.
		try {
			fillPostconditionsList();
			if (refactoring != null){
				postconditionsTab.fillSelectedList(refactoring.getPostconditions(),
					refactoring, RefactoringConstants.POSTCONDITION);
			}
		} catch (IOException exception) {
			String message = Messages.RefactoringWizardPage3_ElementsNotLoaded +
				".\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(getShell(), Messages.RefactoringWizardPage3_Error, message);
		}
	}
	
	/**
	 * Obtiene el conjunto de parámetros asignados en cada una de las postcondiciones
	 * del repositorio seleccionadas para formar parte de la refactorización.
	 * 
	 * <p>El formato devuelto se corresponde con una tabla asociativa que sigue la estructura 
	 * definida en {@link RepositoryElementComposite#getParameters()}.</p>
	 * 
	 * @return el conjunto de parámetros asignados a cada elemento concreto del
	 * repositorio seleccionado para formar parte de la refactorización.
	 * 
	 * @see RepositoryElementComposite#getParameters()
	 */
	public HashMap<String, ArrayList<String[]>> getAmbiguousParameters(){
		
		return postconditionsTab.getParameters();
	}
	
	
	/**
	 * Obtiene la lista de nombres de postcondiciones seleccionadas.
	 * 
	 * @return la lista de nombres de postcondiciones seleccionadas.
	 */
	public ArrayList<String> getPostconditions(){
		return postconditionsTab.getElements();
	}
		
	/**
	 * Consulta el directorio de predicados y acciones para obtener los 
	 * elementos posibles de la lista de candidatos.
	 * 
	 * @throws IOException si no se encuentra el directorio.
	 */
	private void fillPostconditionsList() throws IOException {
		RepositoryElementLister l = 
			RepositoryElementLister.getInstance();
		
		postconditionsTab.fillRepositoryList(l.getAllPredicatesList());
	}

	/**
	 * Actualiza el estado de la pantalla de diálogo del asistente.
	 * 
	 * @param message mensaje asociado al estado actual de la pantalla.
	 */
	public void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}
