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
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Joiner;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.wizard.classificationscombo.PickCategoryTree;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsStore;
import dynamicrefactoring.util.io.filter.ImageFilter;

/**
 * Primera p�gina del asistente de creaci�n o edici�n de refactorizaciones.
 * 
 * <p>
 * Permite configurar los datos b�sicos de la refactorizaci�n, como son:
 * <ul>
 * <li>Su nombre.</li>
 * <li>Su descripci�n.</li>
 * <li>Su motivaci�n.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Tambi�n permite asociar una imagen a la refactorizaci�n, que muestre de
 * manera gr�fica la intenci�n de la refactorizaci�n.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public final class RefactoringWizardPage1 extends WizardPage {

	/**
	 * Refactorización configurada a trav�s del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorizaci�n) o modificada (si se
	 * est� editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;

	/**
	 * Motivaci�n asociada la refactorizaci�n.
	 */
	private Text motivationText;

	/**
	 * Ruta a un fichero de imagen con un esquema de la refactorizaci�n.
	 */
	private Text imageText;

	/**
	 * Descripci�n de la refactorizaci�n.
	 */
	private Text descriptionText;

	/**
	 * Nombre de la refactorizaci�n.
	 */
	private Text nameText;

	/**
	 * Arbol que permite escoger las categorias de una refactorizacion.
	 */
	private PickCategoryTree categoryTree;

	/**
	 * Palabras clave que identifican la refactorizaci�n.
	 */
	private Text keywordsText;

	/**
	 * Constructor.
	 * 
	 * @param refactoring
	 *            refactorizaci�n refactorizaci�n que se quiere editar, o
	 *            <code>null</code> si se est� creando una nueva.
	 */
	public RefactoringWizardPage1(DynamicRefactoringDefinition refactoring) {
		super("Wizard page"); //$NON-NLS-1$
		setDescription(Messages.RefactoringWizardPage1_Description);
		setPageComplete(false);

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
					Messages.RefactoringWizardPage1_Step + ")"); //$NON-NLS-1$
		}
		super.setVisible(visible);
	}

	/**
	 * Crea el contenido de la p�gina del wizard.
	 * 
	 * @param parent
	 *            el elemento padre de esta p�gina.
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());

		setControl(container);

		final Composite composite = new Composite(container, SWT.NONE);
		final FormData fd_composite = new FormData();
		fd_composite.left = new FormAttachment(0, 5);
		fd_composite.top = new FormAttachment(0, 5);
		fd_composite.bottom = new FormAttachment(100, -5);
		fd_composite.right = new FormAttachment(100, -5);
		composite.setLayoutData(fd_composite);

		final Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.RefactoringWizardPage1_Name);
		nameLabel.setBounds(10, 20, 52, 13);

		nameText = new Text(composite, SWT.BORDER);
		nameText.setToolTipText(Messages.RefactoringWizardPage1_FillInName);
		nameText.setBounds(80, 15, 534, 25);

		final Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText(Messages.RefactoringWizardPage1_Description);
		descriptionLabel.setBounds(10, 54, 57, 13);

		descriptionText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		descriptionText
				.setToolTipText(Messages.RefactoringWizardPage1_GiveDescription);
		descriptionText.setBounds(80, 55, 534, 97);

		final Label imageLabel = new Label(composite, SWT.NONE);
		imageLabel.setText(Messages.RefactoringWizardPage1_Image);
		imageLabel.setBounds(10, 170, 52, 13);

		imageText = new Text(composite, SWT.BORDER);
		imageText.setToolTipText(Messages.RefactoringWizardPage1_SelectImage);
		imageText.setBounds(80, 167, 498, 25);

		final Button examineButton = new Button(composite, SWT.NONE);
		examineButton.setText("..."); //$NON-NLS-1$
		examineButton.setBounds(584, 167, 30, 25);
		examineButton.addSelectionListener(new ImageChooserAction());

		final Label motivationLabel = new Label(composite, SWT.NONE);
		motivationLabel.setText(Messages.RefactoringWizardPage1_Motivation);
		motivationLabel.setBounds(12, 210, 50, 13);

		motivationText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		motivationText
				.setToolTipText(Messages.RefactoringWizardPage1_GiveMotivation);
		motivationText.setBounds(80, 207, 534, 97);

		final Label keywordsLabel = new Label(composite, SWT.NONE);
		// FIXME: Internacionalizar
		keywordsLabel.setText("Keywords");
		keywordsLabel.setBounds(12, 320, 50, 13);

		keywordsText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		// FIXME: Internacionalizar
		keywordsText
				.setToolTipText("Establish a set of KeyWords to identify the refactoring.");
		keywordsText.setBounds(80, 317, 534, 57);

		final Label categoriesLabel = new Label(composite, SWT.NONE);
		// FIXME: Internacionalizar el texto
		categoriesLabel.setText("Categories");
		categoriesLabel.setBounds(12, 390, 50, 13);

		Set<Category> categories = (refactoring == null ? new HashSet<Category>()
				: refactoring.getCategories());
		categoryTree = new PickCategoryTree(composite, ClassificationsStore
				.getInstance().getAllClassifications(), categories);
		categoryTree.getControl().setBounds(80, 390, 534, 160);

		this.nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		this.descriptionText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		this.motivationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		if (refactoring != null) {
			fillInRefactoringData();
		}
	}

	/**
	 * Puebla los campos del formulario del asistente con la informaci�n que se
	 * pueda obtener de la refactorizaci�n existente que se est� editando.
	 */
	private void fillInRefactoringData() {
		if (refactoring.getName() != null) {
			nameText.setText(refactoring.getName());
		}
		if (refactoring.getImage() != null) {
			imageText.setText(refactoring.getImage());
		}
		if (refactoring.getMotivation() != null) {
			motivationText.setText(refactoring.getMotivation());
		}
		if (refactoring.getDescription() != null) {
			descriptionText.setText(refactoring.getDescription());
		}
		if (refactoring.getKeywords() != null
				&& refactoring.getKeywords().size() > 0) {
			keywordsText
					.setText(Joiner.on(",").join(refactoring.getKeywords()));
		}
		dialogChanged();
	}

	/**
	 * Se asegura de que todos los campos de texto tengan un valor especificado.
	 */
	private void dialogChanged() {

		if (this.getNameText().getText().length() == 0) {
			updateStatus(Messages.RefactoringWizardPage1_NameNeeded);
			return;
		}
		if (this.getDescriptionText().getText().length() == 0) {
			updateStatus(Messages.RefactoringWizardPage1_DescriptionNeeded);
			return;
		}
		if (this.getMotivationText().getText().length() == 0) {
			updateStatus(Messages.RefactoringWizardPage1_MotivationNeeded);
			return;
		}
		updateStatus(null);
	}

	/**
	 * Actualiza el estado de la pantalla de di�logo del asistente.
	 * 
	 * @param message
	 *            mensaje asociado al estado actual de la pantalla.
	 */
	private void updateStatus(String message) {
		// TODO: Comprobar que las palabras claves tienen sintaxis correcta
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Obtiene las palabras clave definidas por el usuario.
	 * 
	 * @return conjunto de palabras clave
	 */
	final Set<String> getKeywords() {
		Set<String> keywords = new HashSet<String>();
		for (String keyword : keywordsText.getText().split(",")) {
			if(keyword.trim().length()>0)
				keywords.add(keyword.trim().toLowerCase());
		}
		return keywords;
	}

	/**
	 * Obtiene el campo de texto con la descripci�n de la refactorizaci�n.
	 * 
	 * @return el campo de texto con la descripci�n de la refactorizaci�n.
	 */
	public final Text getDescriptionText() {
		return this.descriptionText;
	}

	/**
	 * Obtiene el campo de texto con la motivaci�n asociada a la
	 * refactorizaci�n.
	 * 
	 * @return el campo de texto con la motivaci�n asociada a la
	 *         refactorizaci�n.
	 */
	public final Text getMotivationText() {
		return this.motivationText;
	}

	/**
	 * Obtiene el campo de texto con el nombre de la refactorizaci�n.
	 * 
	 * @return el campo de texto con el nombre de la refactorizaci�n.
	 */
	public final Text getNameText() {
		return this.nameText;
	}

	/**
	 * Obtiene el campo de texto con la ruta de la imagen esquem�tica de la
	 * refactorizaci�n.
	 * 
	 * @return el campo de texto con la ruta de la imagen esquem�tica de la
	 *         refactorizaci�n.
	 */
	public final Text getImageNameText() {
		return this.imageText;
	}

	/**
	 * Obtiene las categorias que se han seleccionado en el �rbol y a las que va
	 * a pertenecer la refactorizacion.
	 * 
	 * @return categorias a las que pertenece la refactorizacion
	 */
	public final Set<Category> getCategories() {
		return categoryTree.getRefactoringCategories();
	}

	/**
	 * Implementa el proceso de elecci�n de la imagen de la refactorizaci�n
	 * din�mica.
	 * 
	 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
	 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ImageChooserAction implements SelectionListener {

		/**
		 * Recibe una notificaci�n de que se ha pulsado el bot�n que permite
		 * seleccionar una imagen asociada a la refactorizaci�n.
		 * 
		 * <p>
		 * Abre una ventana de selecci�n de fichero con un filtro que permite
		 * seleccionar solamente im�genes.
		 * </p>
		 * 
		 * @param e
		 *            el evento de selecci�n disparado.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog chooser = new FileDialog(getShell(), SWT.OPEN);
			chooser.setText(Messages.RefactoringWizardPage1_SelectRefactoringImage);
			chooser.setFilterExtensions(ImageFilter.templates);
			chooser.setFilterNames(ImageFilter.descriptions);
			chooser.setFilterPath(Platform.getLocation().toOSString()
					+ File.separatorChar + ".."); //$NON-NLS-1$ //$NON-NLS-2$

			String returnVal = chooser.open();
			if (returnVal != null)
				imageText.setText(returnVal);
		}

		/**
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}

	@Override
	public void performHelp() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelp();
	}
}