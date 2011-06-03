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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.RefactoringMechanismType;

/**
 * Tercera p�gina del asistente de creación o edición de refactorizaciones.
 * 
 * <p>
 * Permite componer la refactorización mediante la adición de predicados a la
 * lista de precondiciones.
 * </p>
 * 
 * <p>
 * Permite definir el orden en que se comprobar�n los predicados y en que se
 * ejecutar�n las acciones, as� como la lista de entradas de la refactorización
 * que deber�n ser transmitidas a cada uno de los componentes.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public final class RefactoringWizardPage3 extends WizardPage implements
		IRefactoringWizardElementPage {

	/**
	 * T�tulo de la opearación sobre la que se configuran las precondiciones de
	 * la refactorización.
	 */
	protected static final String PRECONDITIONS_TITLE = Messages.RefactoringWizardPage3_Preconditions;

	/**
	 * Refactorización configurada a través del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorización) o modificada (si se
	 * est� editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(RefactoringWizardPage3.class);

	/**
	 * Pesta�a para la configuración de las precondiciones de la
	 * refactorización.
	 */
	private RepositoryElementComposite preconditionsTab;

	/**
	 * Constructor.
	 * 
	 * @param refactoring
	 *            la refactorización que se est� editando, o <code>
	 * null</code> si se est� construyendo una nueva.
	 */
	public RefactoringWizardPage3(DynamicRefactoringDefinition refactoring) {
		super("Wizard page"); //$NON-NLS-1$
		setDescription(Messages.RefactoringWizardPage3_DescriptionInputElements);
		this.refactoring = refactoring;
	}

	/**
	 * Hace visible o invisible la p�gina del asistente.
	 * 
	 * @param visible
	 *            si la p�gina se debe hacer visible o no.
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			Object[] messageArgs = { ((RefactoringWizard) getWizard())
					.getOperationAsString() };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.RefactoringWizardPage3_DynamicRefactoring);

			setTitle(formatter.format(messageArgs) + " (" + //$NON-NLS-1$
					Messages.RefactoringWizardPage3_Step + ")"); //$NON-NLS-1$

			preconditionsTab.deselect();
		}
		super.setVisible(visible);
	}

	/**
	 * Crea el contenido de la p�gina del asistente.
	 * 
	 * @param parent
	 *            el elemento padre de esta p�gina del asistente.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());

		setControl(container);
		// Las precondiciones necesitan un elemento a través del que acceder a
		// las
		// entradas de la refactorización.
		RefactoringWizardPage2 inputsPage = null;
		if (getPreviousPage() instanceof RefactoringWizardPage2)
			inputsPage = (RefactoringWizardPage2) getPreviousPage();

		preconditionsTab = new RepositoryElementComposite(container,
				PRECONDITIONS_TITLE, inputsPage, this,
				RefactoringMechanismType.PRECONDITION);

		// Se completan las listas de elementos del repositorio candidatos.
		try {
			fillPreconditionsList();
			if (refactoring != null) {
				preconditionsTab.fillSelectedListAsRefactoringMechanism(
						refactoring.getPreconditions(), refactoring,
						RefactoringMechanismType.PRECONDITION);
			}
		} catch (IOException exception) {
			String message = Messages.RefactoringWizardPage3_ElementsNotLoaded
					+ ".\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(getShell(),
					Messages.RefactoringWizardPage3_Error, message);
		}
	}

	/**
	 * Obtiene la lista de precondiciones seleccionadas.
	 * 
	 * @return la lista de precondiciones seleccionadas.
	 */
	public List<RefactoringMechanismInstance> getPreconditions() {
		List<RefactoringMechanismInstance> lista = new ArrayList<RefactoringMechanismInstance>();
		for (String className : preconditionsTab.getElements()) {
			lista.add(new RefactoringMechanismInstance(getMechanismName(className),
					preconditionsTab.getParameters().get(className)
							.getParametersNames(),
					RefactoringMechanismType.PRECONDITION));
		}
		return lista;
	}

	/**
	 * Recibe una precondicion, accion o postcondicion con el numero:
	 * 
	 * NotExistClassWithName(1)
	 * 
	 * y devuelve el nombre sin el numero:
	 * 
	 * NotExistClassWithName
	 * 
	 * @param preconditionWithNumber
	 *            precondicion con formato nombre(numero)
	 * @return devuelve nombre
	 */
	protected static String getMechanismName(final String preconditionWithNumber) {
		return preconditionWithNumber.substring(0,
				preconditionWithNumber.length() - 4);
	}
	
	/**
	 * Consulta el directorio de predicados y acciones para obtener los
	 * elementos posibles de la lista de candidatos.
	 * 
	 * @throws IOException
	 *             si no se encuentra el directorio.
	 */
	private void fillPreconditionsList() throws IOException {
		preconditionsTab.fillRepositoryList(RefactoringMechanismType
				.getPredicatesAllList());
	}

	/**
	 * Actualiza el estado de la pantalla de di�logo del asistente.
	 * 
	 * @param message
	 *            mensaje asociado al estado actual de la pantalla.
	 */
	public void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}