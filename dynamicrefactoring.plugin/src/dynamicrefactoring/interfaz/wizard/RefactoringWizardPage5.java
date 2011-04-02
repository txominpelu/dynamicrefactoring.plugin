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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringMechanism;

/**
 * Tercera pï¿½gina del asistente de creaciï¿½n o ediciï¿½n de
 * refactorizaciones.
 * 
 * <p>
 * Permite componer la refactorizaciï¿½n mediante la adiciï¿½n de predicados a
 * la lista de precondiciones o postcondiciones, y de acciones que implementen
 * las modificaciones llevadas a cabo por la refactorizaciï¿½n.
 * </p>
 * 
 * <p>
 * Permite definir el orden en que se comprobarï¿½n los predicados y en que se
 * ejecutarï¿½n las acciones, asï¿½ como la lista de entradas de la
 * refactorizaciï¿½n que deberï¿½n ser transmitidas a cada uno de los
 * componentes.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class RefactoringWizardPage5 extends WizardPage implements IRefactoringWizardElementPage {

	/**
	 * Tï¿½tulo de la operaciï¿½n sobre la que se configuran las postcondiciones
	 * de la refactorizaciï¿½n.
	 */
	protected static final String POSTCONDITIONS_TITLE = Messages.RefactoringWizardPage3_Postconditions;

	/**
	 * Refactorizaciï¿½n configurada a travï¿½s del asistente y que debe ser
	 * creada finalmente (si se trata de una nueva refactorizaciï¿½n) o
	 * modificada (si se estï¿½ editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringWizardPage5.class);

	/**
	 * Contenedor para la configuraciï¿½n de las postcondiciones de la
	 * refactorizaciï¿½n.
	 */
	private RepositoryElementComposite postconditionsTab;

	/**
	 * Constructor.
	 * 
	 * @param refactoring
	 *            la refactorizaciï¿½n que se estï¿½ editando, o <code>
	 * null</code> si se estï¿½ construyendo una nueva.
	 */
	public RefactoringWizardPage5(DynamicRefactoringDefinition refactoring) {
		super("Wizard page"); //$NON-NLS-1$
		setDescription(Messages.RefactoringWizardPage5_DescriptionInputPostconditions);
		
		this.refactoring = refactoring;
	}

	/**
	 * Hace visible o invisible la pï¿½gina del asistente.
	 * 
	 * @param visible
	 *            si la pï¿½gina se debe hacer visible o no.
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
	 * Crea el contenido de la pï¿½gina del asistente.
	 * 
	 * @param parent
	 *            el elemento padre de esta pï¿½gina del asistente.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());

		setControl(container);
		
		// Las precondiciones necesitan un elemento a travï¿½s del que acceder a
		// las
		// entradas de la refactorizaciï¿½n.
		RefactoringWizardPage2 inputsPage = null;
		if (getPreviousPage().getPreviousPage().getPreviousPage() instanceof RefactoringWizardPage2)
			inputsPage = (RefactoringWizardPage2)getPreviousPage().getPreviousPage().getPreviousPage();
	
		postconditionsTab = new RepositoryElementComposite(
			container, POSTCONDITIONS_TITLE, inputsPage,this);
		
		// Se completan las listas de elementos del repositorio candidatos.
		try {
			fillPostconditionsList();
			if (refactoring != null){
				postconditionsTab.fillSelectedList(
						refactoring.getPostconditions(), refactoring,
						RefactoringMechanism.POSTCONDITION);
			}
		} catch (IOException exception) {
			String message = Messages.RefactoringWizardPage3_ElementsNotLoaded +
				".\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(getShell(), Messages.RefactoringWizardPage3_Error, message);
		}
	}

	/**
	 * Obtiene el conjunto de parï¿½metros asignados en cada una de las
	 * postcondiciones del repositorio seleccionadas para formar parte de la
	 * refactorizaciï¿½n.
	 * 
	 * <p>
	 * El formato devuelto se corresponde con una tabla asociativa que sigue la
	 * estructura definida en {@link RepositoryElementComposite#getParameters()}
	 * .
	 * </p>
	 * 
	 * @return el conjunto de parï¿½metros asignados a cada elemento concreto
	 *         del repositorio seleccionado para formar parte de la
	 *         refactorizaciï¿½n.
	 * 
	 * @see RepositoryElementComposite#getParameters()
	 */
	public HashMap<String, List<String[]>> getAmbiguousParameters() {
		return postconditionsTab.getParameters();
	}
	
	
	/**
	 * Obtiene la lista de nombres de postcondiciones seleccionadas.
	 * Las precondiciones seleccionadas son exclusivamente los nombres
	 * de los tipos de las mismas.
	 * 
	 * Ejemplo:
	 * 
	 * AddAtribute
	 * 
	 * @return la lista de nombres de postcondiciones seleccionadas.
	 */
	public List<String> getPostconditions(){
		return postconditionsTab.getElements();
	}
		
	/**
	 * Consulta el directorio de predicados y acciones para obtener los 
	 * elementos posibles de la lista de candidatos.
	 * 
	 * @throws IOException si no se encuentra el directorio.
	 */
	private void fillPostconditionsList() throws IOException {
		postconditionsTab.fillRepositoryList(RefactoringMechanism
				.getPredicatesAllList());
	}

	/**
	 * Actualiza el estado de la pantalla de diï¿½logo del asistente.
	 * 
	 * @param message
	 *            mensaje asociado al estado actual de la pantalla.
	 */
	public void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}
