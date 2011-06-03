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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.xml.XMLRefactoringsCatalog;
import dynamicrefactoring.domain.xml.writer.XMLRefactoringWriterException;
import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade;

/**
 * Proporciona un asistente de Eclipse que permite crear una nueva
 * refactorización din�mica o modificar una ya existente.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringWizard extends Wizard implements INewWizard {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(RefactoringWizard.class);

	/**
	 * Indica que el asistente debe crear una refactorización nueval al
	 * terminar.
	 */
	protected static final int CREATE = 0;

	/**
	 * Indica que el asistente debe modificar una refactorización existente al
	 * terminar.
	 */
	private static final int EDIT = 1;

	/**
	 * Refactorización configurada a través del asistente y que debe ser
	 * creada finalmente (si se trata de una nueva refactorización) o
	 * modificada (si se est� editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;

	/**
	 * Indica el tipo de operación que debe realizar el asistente cuando el
	 * usuario pulse el bot�n "Finalizar".
	 * 
	 * <p>
	 * Deber� ser una de {@value #CREATE} o {@value #EDIT}.
	 * </p>
	 */
	private int operation;

	/**
	 * Nombre original de la refactorización que se edita.
	 */
	private String originalName;


	/**
	 * Primera p�gina del asistente.
	 */
	private RefactoringWizardPage1 pageA;

	/**
	 * Segunda p�gina del asistente.
	 */
	private RefactoringWizardPage2 pageB;

	/**
	 * Tercera p�gina del asistente.
	 */
	private RefactoringWizardPage3 pageC;

	/**
	 * Cuarta p�gina del asistente.
	 */
	private RefactoringWizardPage4 pageD;

	/**
	 * Quinta p�gina del asistente.
	 */
	private RefactoringWizardPage5 pageE;

	/**
	 * Sexta p�gina del asistente.
	 */
	private RefactoringWizardPage6 pageF;

	/**
	 * S�ptima p�gina del asistente.
	 */
	private RefactoringWizardPage7 pageG;

	/**
	 * 
	 */
	protected RefactoringsCatalog refactCatalog;
	
	/**
	 * Categoria Scope que ha sido seleccionada en la primera página del asistente.
	 */
	protected Category scope;

	/**
	 * Constructor.
	 * 
	 * @param refactoring
	 *            refactorización que se desea editar o <code>null
	 * </code> si se desea crear una nueva.
	 */
	public RefactoringWizard(DynamicRefactoringDefinition refactoring, RefactoringsCatalog catalog) {
		super();
		setNeedsProgressMonitor(true);

		setDefaultPageImageDescriptor(ImageDescriptor
				.createFromImage(RefactoringImages.getSIIcon()));

		setWindowTitle(Messages.RefactoringWizard_WizardTitle);

		this.refactCatalog = catalog;
		operation = (refactoring == null) ? CREATE : EDIT;
		this.refactoring = refactoring;
		this.originalName = (refactoring != null) ? refactoring.getName() : ""; //$NON-NLS-1$
		//generamos el indice para poder realizar búsquedas en tipos de elementos y acciones y predicados
		try {
			SearchingFacade.INSTANCE.generateAllIndexes();
		} catch (IOException e) {
			String message = Messages.RefactoringWizard_AllIndexesNotGenerated
			+ ".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
		}
	}

	/**
	 * Constructor.
	 * 
	 * <p>
	 * Crea un nuevo asistente para la creación de una refactorización desde
	 * cero. Necesario para la integración del asistente en Eclipse y su
	 * activación asistente desde el men� <i>New</i> de Eclipse.
	 * </p>
	 */
	public RefactoringWizard() {
		new RefactoringWizard(null, XMLRefactoringsCatalog.getInstance());
	}

	/**
	 * A�ade las p�ginas al asistente.
	 */
	@Override
	public void addPages() {
		pageA = new RefactoringWizardPage1(refactoring);
		addPage(pageA);
		pageB = new RefactoringWizardPage2(refactoring);
		addPage(pageB);
		pageC = new RefactoringWizardPage3(refactoring);
		addPage(pageC);
		pageD = new RefactoringWizardPage4(refactoring);
		addPage(pageD);
		pageE = new RefactoringWizardPage5(refactoring);
		addPage(pageE);
		pageF = new RefactoringWizardPage6(refactoring);
		addPage(pageF);
		pageG = new RefactoringWizardPage7(pageA);
		addPage(pageG);
	}

	/**
	 * Método llamado cuando se pulsa el bot�n "Finish" en el asistente. Se
	 * crear� la operación que se deba ejecutar, y se ejecutar� utilizando
	 * el propio asistente como contexto de ejecuci�n.
	 * 
	 * @return <code>true</code> para indicar que la solicitud de finalización
	 *         ha sido aceptada; <code>false</code> para indicar que ha sido
	 *         rechazada.
	 */
	@Override
	public boolean performFinish() {
		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				writeRefactoring(configureRefactoring());
			}
		};

		try {
			operation.run(new NullProgressMonitor());
		} catch (InterruptedException e) {
			String message = Messages.RefactoringWizard_CreationInterrupted
					+ ".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			Logger.getRootLogger().error(message);
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			logger.error(realException.getMessage());
			MessageDialog.openError(getShell(),
					Messages.RefactoringWizard_Error,
					realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Método de inicialización.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	/**
	 * Obtiene el tipo de operación que est� configurando el asistente.
	 * 
	 * @return el tipo de operación que est� configurando el asistente.
	 */
	public final int getOperation() {
		return operation;
	}

	/**
	 * Obtiene el nombre del tipo de operación que est� configurando el
	 * asistente.
	 * 
	 * @return el nombre del tipo de operación que est� configurando el
	 *         asistente.
	 */
	public final String getOperationAsString() {
		return (operation == CREATE) ? Messages.RefactoringWizard_Creation
				: Messages.RefactoringWizard_Edition;
	}

	/**
	 * Crea y configura la nueva refactorización personalizada a partir de los
	 * datos introducidos por el usuario en las p�ginas del asistente.
	 */
	private DynamicRefactoringDefinition.Builder configureRefactoring() {
		DynamicRefactoringDefinition.Builder builder = new DynamicRefactoringDefinition.Builder(
				pageA.getNameText().getText().trim());
		
		
		return builder.description(pageA.getDescriptionText().getText().trim())
				.image(pageA.getRefactoringImage().trim())
				.motivation(pageA.getMotivationText().getText().trim())
				.inputs(pageB.getInputs())
				.preconditions(pageC.getPreconditions())
				.actions(pageD.getActions())
				.postconditions(pageE.getPostconditions())
				.categories(pageA.getCategories())
				.keywords(pageA.getKeywords())
				.examples(pageF.getExamples())
				.isEditable(true);

	}

	/**
	 * Copia los ficheros asociados a una refactorización al directorio
	 * seleccionado y escribe el fichero con la refactorización creada.
	 * 
	 * @param resultingRefactoringDefinition
	 * 
	 * @throws IOException
	 *             si se produce un error de lectura / escritura durante el
	 *             proceso de escritura de la refactorización.
	 * @throws FileNotFoundException
	 *             si no se encuentra uno de los ficheros o directorios
	 *             implicados en el proceso.
	 * @throws XMLRefactoringWriterException
	 *             si se produce un error durante la escritura de la
	 *             refactorización en el fichero XML de destino.
	 */
	private void writeRefactoring(
			DynamicRefactoringDefinition.Builder builder) {
		DynamicRefactoringDefinition resultingRefactoringDefinition = builder.build();

		if (operation == EDIT && refactCatalog.hasRefactoring(originalName)) {
			refactCatalog.updateRefactoring(originalName, resultingRefactoringDefinition);;
		} else {
			// Se escribe la refactorización en el fichero XML.
			refactCatalog.addRefactoring(resultingRefactoringDefinition);
		}

		String action = (operation == CREATE) ? Messages.RefactoringWizard_CreatedLower
				: Messages.RefactoringWizard_ModifiedLower;

		Object[] messageArgs = {
				"\"" + resultingRefactoringDefinition.getName() + "\"", action }; //$NON-NLS-1$ //$NON-NLS-2$
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter
				.applyPattern(Messages.RefactoringWizard_RefactoringSuccessfully);

		MessageDialog.openInformation(getShell(),
				Messages.RefactoringWizard_Completed,
				formatter.format(messageArgs) + "."); //$NON-NLS-1$
	}

}