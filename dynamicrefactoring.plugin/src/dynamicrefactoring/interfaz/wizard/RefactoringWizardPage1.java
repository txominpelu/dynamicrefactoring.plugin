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

import java.text.MessageFormat;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.util.io.filter.ImageFilter;

import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.core.runtime.Platform;

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

/**
 * Primera página del asistente de creación o edición de refactorizaciones.
 * 
 * <p>Permite configurar los datos básicos de la refactorización, como son:
 * <ul>
 * <li>Su nombre.</li>
 * <li>Su descripción.</li>
 * <li>Su motivación.</li>
 * </ul>
 * </p>
 * 
 * <p>También permite asociar una imagen a la refactorización, que muestre
 * de manera gráfica la intención de la refactorización.</p>
 *
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringWizardPage1 extends WizardPage {

	/**
	 * Refactorización configurada a través del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorización) o modificada (si se
	 * está editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;
	
	/**
	 * Motivación asociada la refactorización.
	 */
	private Text t_Motivation;

	/**
	 * Ruta a un fichero de imagen con un esquema de la refactorización.
	 */
	private Text t_Image;
	
	/**
	 * Descripción de la refactorización.
	 */
	private Text t_Description;
	
	/**
	 * Nombre de la refactorización.
	 */
	private Text t_Name;

	/**
	 * Constructor.
	 * 
	 * @param refactoring refactorización refactorización que se quiere editar,
	 * o <code>null</code> si se está creando una nueva.
	 */
	public RefactoringWizardPage1(DynamicRefactoringDefinition refactoring) {
		super("Wizard page");		 //$NON-NLS-1$
		setDescription(Messages.RefactoringWizardPage1_Description);
		setPageComplete(false);
				
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
				Messages.RefactoringWizardPage1_Step +
				")"); //$NON-NLS-1$
		}
		super.setVisible(visible);
	}

	/**
	 * Crea el contenido de la página del wizard.
	 * 
	 * @param parent el elemento padre de esta página.
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

		final Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText(Messages.RefactoringWizardPage1_Description);
		descriptionLabel.setBounds(10, 54, 57, 13);

		final Label imageLabel = new Label(composite, SWT.NONE);
		imageLabel.setText(Messages.RefactoringWizardPage1_Image);
		imageLabel.setBounds(10, 170, 52, 13);

		final Label motivationLabel = new Label(composite, SWT.NONE);
		motivationLabel.setText(Messages.RefactoringWizardPage1_Motivation);
		motivationLabel.setBounds(12, 210, 50, 13);

		t_Name = new Text(composite, SWT.BORDER);
		t_Name.setToolTipText(Messages.RefactoringWizardPage1_FillInName);
		t_Name.setBounds(80, 15, 534, 25);

		t_Description = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		t_Description.setToolTipText(Messages.RefactoringWizardPage1_GiveDescription);
		t_Description.setBounds(80, 55, 534, 97);

		t_Image = new Text(composite, SWT.BORDER);
		t_Image.setToolTipText(Messages.RefactoringWizardPage1_SelectImage);
		t_Image.setBounds(80, 167, 498, 25);

		t_Motivation = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		t_Motivation.setToolTipText(Messages.RefactoringWizardPage1_GiveMotivation);
		t_Motivation.setBounds(80, 207, 534, 97);

		final Button examineButton = new Button(composite, SWT.NONE);
		examineButton.setText("..."); //$NON-NLS-1$
		examineButton.setBounds(584, 167, 30, 25);
		examineButton.addSelectionListener(new ImageChooserAction());

		this.t_Name.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		this.t_Description.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		this.t_Motivation.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		if (refactoring != null)
			fillInRefactoringData();
	}

	/**
	 * Puebla los campos del formulario del asistente con la información que se
	 * pueda obtener de la refactorización existente que se está editando.
	 */
	private void fillInRefactoringData(){
		if (refactoring.getName() != null)
			t_Name.setText(refactoring.getName());
		if (refactoring.getImage() != null)
			t_Image.setText(refactoring.getImage());
		if (refactoring.getMotivation() != null)
			t_Motivation.setText(refactoring.getMotivation());
		if (refactoring.getDescription() != null)
			t_Description.setText(refactoring.getDescription());
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
	 * Actualiza el estado de la pantalla de diálogo del asistente.
	 * 
	 * @param message mensaje asociado al estado actual de la pantalla.
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Obtiene el campo de texto con la descripción de la refactorización.
	 * 
	 * @return el campo de texto con la descripción de la refactorización.
	 */
	public Text getDescriptionText(){
		return this.t_Description;
	}

	/**
	 * Obtiene el campo de texto con la motivación asociada a la refactorización.
	 * 
	 * @return el campo de texto con la motivación asociada a la refactorización.
	 */
	public Text getMotivationText(){
		return this.t_Motivation;
	}

	/**
	 * Obtiene el campo de texto con el nombre de la refactorización.
	 * 
	 * @return el campo de texto con el nombre de la refactorización.
	 */
	public Text getNameText(){
		return this.t_Name;
	}

	/**
	 * Obtiene el campo de texto con la ruta de la imagen esquemática de la
	 * refactorización.
	 * 
	 * @return el campo de texto con la ruta de la imagen esquemática de la
	 * refactorización.
	 */
	public Text getImageNameText(){
		return this.t_Image;
	}
	
	/**
	 * Implementa el proceso de elección de la imagen de la refactorización 
	 * dinámica.
	 * 
	 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
	 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ImageChooserAction implements SelectionListener {

		/**
		 * Recibe una notificación de que se ha pulsado el botón que permite 
		 * seleccionar una imagen asociada a la refactorización.
		 * 
		 * <p>Abre una ventana de selección de fichero con un filtro que permite
		 * seleccionar solamente imágenes.</p>
		 * 
		 * @param e el evento de selección disparado.
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
		    	+ System.getProperty("file.separator") + ".."); //$NON-NLS-1$ //$NON-NLS-2$
		    
		    String returnVal = chooser.open();
		    if (returnVal != null)
		    	t_Image.setText(returnVal);
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