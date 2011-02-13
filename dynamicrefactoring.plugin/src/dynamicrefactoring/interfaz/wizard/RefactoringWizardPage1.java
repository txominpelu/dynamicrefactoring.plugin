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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.ValidationException;

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

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.wizard.classificationscombo.PickCategoryTree;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsReaderFactory;
import dynamicrefactoring.util.io.filter.ImageFilter;

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
	private Text motivationText;

	/**
	 * Ruta a un fichero de imagen con un esquema de la refactorización.
	 */
	private Text imageText;
	
	/**
	 * Descripción de la refactorización.
	 */
	private Text descriptionText;
	
	/**
	 * Nombre de la refactorización.
	 */
	private Text nameText;

	/**
	 * Arbol que permite escoger las categorias
	 * de una refactorizacion.
	 */
	private PickCategoryTree categoryTree;

	/**
	 * Palabras clave que identifican la
	 * refactorización.
	 */
	private Text keywordsText;

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

		nameText = new Text(composite, SWT.BORDER);
		nameText.setToolTipText(Messages.RefactoringWizardPage1_FillInName);
		nameText.setBounds(80, 15, 534, 25);

		final Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText(Messages.RefactoringWizardPage1_Description);
		descriptionLabel.setBounds(10, 54, 57, 13);
		
		descriptionText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		descriptionText.setToolTipText(Messages.RefactoringWizardPage1_GiveDescription);
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
		motivationText.setToolTipText(Messages.RefactoringWizardPage1_GiveMotivation);
		motivationText.setBounds(80, 207, 534, 97);
		

		final Label keywordsLabel = new Label(composite, SWT.NONE);
		//FIXME: Internacionalizar
		keywordsLabel.setText("Keywords");
		keywordsLabel.setBounds(12, 320, 50, 13);
		
		keywordsText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		//FIXME: Internacionalizar
		keywordsText.setToolTipText("Establish a set of KeyWords to identify the refactoring.");
		keywordsText.setBounds(80, 317, 534, 57);

		
		final Label categoriesLabel = new Label(composite, SWT.NONE);
		//FIXME: Internacionalizar el texto
		categoriesLabel.setText("Categories");
		categoriesLabel.setBounds(12, 390, 50, 13);
		
		try {
			Set<Category> categories = (refactoring == null ? new HashSet<Category>() : refactoring.getCategories());
			categoryTree = new PickCategoryTree(composite,ClassificationsReaderFactory
					.getReader(
							ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER)
					.readClassifications(RefactoringConstants.CLASSIFICATION_TYPES_FILE),categories);
			categoryTree.getControl().setBounds(80, 390, 534, 160);
		} catch (ValidationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		

		this.nameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		this.descriptionText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		this.motivationText.addModifyListener(new ModifyListener(){
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
			nameText.setText(refactoring.getName());
		if (refactoring.getImage() != null)
			imageText.setText(refactoring.getImage());
		if (refactoring.getMotivation() != null)
			motivationText.setText(refactoring.getMotivation());
		if (refactoring.getDescription() != null)
			descriptionText.setText(refactoring.getDescription());
		if (refactoring.getKeywords() != null && refactoring.getKeywords().size() > 0 )
			keywordsText.setText(Joiner.on(",").join(refactoring.getKeywords()));
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
		//TODO: Comprobar que las palabras claves tienen sintaxis correcta
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Obtiene las palabras clave definidas
	 * por el usuario.
	 * 
	 * @return conjunto de palabras clave
	 */
	List<String> getKeywords(){
		List<String> keywords = new ArrayList<String>();
		for(String keyword: keywordsText.getText().split(",")){
			keywords.add(keyword);
		}
		return keywords;
	}
	
	/**
	 * Obtiene el campo de texto con la descripción de la refactorización.
	 * 
	 * @return el campo de texto con la descripción de la refactorización.
	 */
	public Text getDescriptionText(){
		return this.descriptionText;
	}

	/**
	 * Obtiene el campo de texto con la motivación asociada a la refactorización.
	 * 
	 * @return el campo de texto con la motivación asociada a la refactorización.
	 */
	public Text getMotivationText(){
		return this.motivationText;
	}

	/**
	 * Obtiene el campo de texto con el nombre de la refactorización.
	 * 
	 * @return el campo de texto con el nombre de la refactorización.
	 */
	public Text getNameText(){
		return this.nameText;
	}

	/**
	 * Obtiene el campo de texto con la ruta de la imagen esquemática de la
	 * refactorización.
	 * 
	 * @return el campo de texto con la ruta de la imagen esquemática de la
	 * refactorización.
	 */
	public Text getImageNameText(){
		return this.imageText;
	}
	
	/**
	 * Obtiene las categorias que se han seleccionado
	 * en el árbol y a las que va a pertenecer la
	 * refactorizacion.
	 * 
	 * @return categorias a las que pertenece la refactorizacion
	 */
	public Set<Category> getCategories(){
		return categoryTree.getRefactoringCategories();
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