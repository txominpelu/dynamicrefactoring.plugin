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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Joiner;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.wizard.classificationscombo.PickCategoryTree;
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

	private static final int MULTILINE_TEXT_WIDTH_HINT = 450;

	private static final int MULTILINE_TEXT_NUM_LINES = 4;

	private static final int WINDOW_HEIGHT = 800;

	private static final int PAGE_VERTICAL_SPACING = 10;

	private static final int WINDOW_WIDTH = 650;

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
	 * Path de la imagen.
	 */
	private String refactoringImage;

	/**
	 * Descripci�n de la refactorización.
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
		
		parent.getShell().setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		centerShell(parent.getShell());

		Composite composite = new Composite(parent, SWT.NULL);
		final GridLayout compositeLayout = new GridLayout(2, false);
		compositeLayout.verticalSpacing = PAGE_VERTICAL_SPACING;
		composite.setLayout(compositeLayout);

		setControl(composite);

		//Name
		createLabel(composite, Messages.RefactoringWizardPage1_Name, SWT.CENTER);
		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(getUniLineTextGridData());
		this.nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		//Description
		createLabel(composite, Messages.RefactoringWizardPage1_Description);
		descriptionText = createMultiLineText(composite, Messages.RefactoringWizardPage1_GiveDescription);
		this.descriptionText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		//Image
		createLabel(composite, Messages.RefactoringWizardPage1_Image, SWT.CENTER);
		createSelectImageComposite(composite);

		//Motivation
		createLabel(composite, Messages.RefactoringWizardPage1_Motivation);
		motivationText = createMultiLineText(composite, Messages.RefactoringWizardPage1_GiveMotivation);
		this.motivationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		//Keywords
		createLabel(composite, Messages.RefactoringWizardPage1_Keywords);
		keywordsText = createMultiLineText(composite, Messages.RefactoringWizardPage1_Give_Keywords);

		//Categories
		createLabel(composite, Messages.RefactoringWizardPage1_Categories);
		categoryTree = createCategoryTree(composite);


		DataBindingContext dbc = new DataBindingContext();
		IObservableValue modelObservable = BeansObservables.observeValue(this,
				"refactoringImage");
		dbc.bindValue(SWTObservables.observeText(imageText, SWT.Modify),
				modelObservable, null, null);

		if (refactoring != null) {
			fillInRefactoringData();
			refactoringImage = refactoring.getImageAbsolutePath();
		}

	}

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Centra la ventana en la pantalla.
	 * 
	 * @param shell ventana a centrar
	 */
	private void centerShell(Shell shell) {
		Point size = shell.computeSize(-1, -1);
		Rectangle screen = shell.getDisplay().getMonitors()[0].getBounds();
		shell.setBounds(
		      (screen.width-size.x)/2,
		      (screen.height-size.y)/2,
		      size.x,
		      size.y
		      );
	}

	/**
	 * Crea el arbol para escoger las categorias de una refactorizacion.
	 * 
	 * @param composite padre del control 
	 * @return arbol para escoger las categorias de una refactorizacion
	 */
	private PickCategoryTree createCategoryTree(Composite composite) {
		Set<Category> categories = (refactoring == null ? new HashSet<Category>()
				: refactoring.getCategories());
		PickCategoryTree pickCategoryTree = new PickCategoryTree(composite,
				PluginClassificationsCatalog.getInstance()
						.getAllClassifications(), categories);

		GridData categoryTreeGridData = new GridData();
		categoryTreeGridData.grabExcessHorizontalSpace = true;
		categoryTreeGridData.horizontalAlignment = GridData.FILL;
		categoryTreeGridData.grabExcessVerticalSpace = true;
		categoryTreeGridData.verticalAlignment = GridData.FILL;

		pickCategoryTree.getControl().setLayoutData(categoryTreeGridData);
		
		pickCategoryTree.getControl().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event){
				dialogChanged();
			}
		});
		
		return pickCategoryTree;
	}

	/**
	 * Crea un label para el wizard con
	 * alineacion vertical BEGINNING.
	 * 
	 * @param composite padre del label
	 * @param labelText texto del label
	 */
	private void createLabel(Composite composite, String labelText) {
		createLabel(composite, labelText, SWT.BEGINNING);
	}
	
	/**
	 * Crea un label para el wizard.
	 * 
	 * @param composite padre del label
	 * @param labelText texto del label
	 * @param verticalAlignment alineacion vertical del label
	 */
	private void createLabel(Composite composite, String labelText, int verticalAlignment) {
		final Label descriptionLabel = new Label(composite, SWT.NONE);
		GridData labelLayoutData = new GridData();
		labelLayoutData.verticalAlignment = verticalAlignment;
		descriptionLabel.setLayoutData(labelLayoutData);
		descriptionLabel.setText(labelText);
	}

	/**
	 * Crea un cuadro de texto multilinea con los parametros pasados.
	 * 
	 * @param composite padre del cuadro de texto
	 * @param toolTipText toolTip que tendra el cuadro de texto
	 * @return devuelve el cuadro de texto creado
	 */
	private Text createMultiLineText(Composite composite, String toolTipText) {
		Text text = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		text.setToolTipText(toolTipText);
		text.setLayoutData(getMultiLineTextGridData());
		return text;
	}

	/**
	 * Crea el composite que dibuja el cuadro de texto 
	 * de la imagen y el boton de escoger la imagen.
	 * 
	 * @param compositeParent padre del composite a crear
	 */
	private void createSelectImageComposite(Composite compositeParent) {
		Composite imageComposite = new Composite(compositeParent, SWT.NONE);
		GridData imageCompositeData = new GridData();
		imageCompositeData.horizontalAlignment = GridData.FILL;
		imageCompositeData.grabExcessHorizontalSpace = true;
		imageComposite.setLayoutData(imageCompositeData);
		imageComposite.setLayout(new GridLayout(2, false));

		imageText = new Text(imageComposite, SWT.BORDER);
		GridData imageTextData = new GridData();
		imageTextData.widthHint = MULTILINE_TEXT_WIDTH_HINT;
		imageTextData.grabExcessHorizontalSpace = true;
		imageTextData.horizontalAlignment = GridData.FILL;
		imageText.setToolTipText(Messages.RefactoringWizardPage1_SelectImage);
		imageText.setLayoutData(imageTextData);

		final Button examineButton = new Button(imageComposite, SWT.NONE);
		examineButton.setText("..."); //$NON-NLS-1$
		examineButton.addSelectionListener(new ImageChooserAction());
	}

	/**
	 * Obtiene el gridData con la configuracion adecuada para los cuadros de
	 * texto multilinea en la pagina.
	 * 
	 * @return gridData con la configuracion para los cuadros de texto multilinea
	 */
	private GridData getMultiLineTextGridData() {
		GridData multiLineTextGridData = new GridData();
		multiLineTextGridData.grabExcessHorizontalSpace = true;
		multiLineTextGridData.widthHint = MULTILINE_TEXT_WIDTH_HINT;
		multiLineTextGridData.horizontalAlignment = GridData.FILL;
		multiLineTextGridData.heightHint = getMultiLineTextFieldHeight(MULTILINE_TEXT_NUM_LINES);
		return multiLineTextGridData;
	}

	/**
	 * Obtiene el gridData con la configuracion adecuada para los cuadros de
	 * texto de una sola linea en la pagina.
	 * 
	 * @return gridData con la configuracion para los cuadros de texto de una
	 *         sola linea
	 */
	private GridData getUniLineTextGridData() {
		GridData uniLineTextGridData = new GridData();
		uniLineTextGridData.widthHint = MULTILINE_TEXT_WIDTH_HINT;
		uniLineTextGridData.grabExcessHorizontalSpace = true;
		uniLineTextGridData.horizontalAlignment = GridData.FILL;
		uniLineTextGridData.verticalAlignment = GridData.BEGINNING;
		return uniLineTextGridData;
	}

	/**
	 * Dado un numero de lineas de un cuadro de texto multilinea devuelve la
	 * altura que debera tener ese cuadro de texto en pixeles.
	 * 
	 * @param numLines
	 *            numero de lineas que tendra el cuadro de texto
	 * @return altura que debera tener el cuadro de texto en pixeles
	 */
	private int getMultiLineTextFieldHeight(int numLines) {
		GC gc = new GC(nameText);
		FontMetrics fm = gc.getFontMetrics();
		int height = fm.getHeight() * numLines;
		gc.dispose();
		return height;
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
			imageText.setText(new File(refactoring.getImage()).getName());
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
					.setText(Joiner.on(",").join(refactoring.getKeywords())); //$NON-NLS-1$
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
		if ((refactoring == null || (!this.getNameText().getText()
				.equalsIgnoreCase(refactoring.getName())))
				&& ((RefactoringWizard) this.getWizard()).refactCatalog
						.hasRefactoring(getNameText().getText().trim())) {
			updateStatus(Messages.RefactoringWizardPage1_NameInUse);
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
		if (!DynamicRefactoringDefinition
				.containsScopeCategory(getCategories())) {
			// TODO: Internacionalizar
			updateStatus("The refactoring must belong to at least one scope."); //$NON-NLS-1$
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
		if(message==null){
			for(Category c: getCategories()){
				if(c.getParent().equals(PluginClassificationsCatalog.SCOPE_CLASSIFICATION))
					((RefactoringWizard)this.getWizard()).scope=c;
			}
		}
		setPageComplete(message == null);
	}

	/**
	 * Obtiene las palabras clave definidas por el usuario.
	 * 
	 * @return conjunto de palabras clave
	 */
	Set<String> getKeywords() {
		Set<String> keywords = new HashSet<String>();
		for (String keyword : keywordsText.getText().split(",")) { //$NON-NLS-1$
			if (keyword.trim().length() > 0) {
				keywords.add(keyword.trim().toLowerCase());
			}
		}
		return keywords;
	}

	/**
	 * Obtiene el campo de texto con la descripci�n de la refactorizaci�n.
	 * 
	 * @return el campo de texto con la descripci�n de la refactorizaci�n.
	 */
	public Text getDescriptionText() {
		return this.descriptionText;
	}

	/**
	 * Obtiene el campo de texto con la motivaci�n asociada a la
	 * refactorizaci�n.
	 * 
	 * @return el campo de texto con la motivaci�n asociada a la
	 *         refactorizaci�n.
	 */
	public Text getMotivationText() {
		return this.motivationText;
	}

	/**
	 * Obtiene el campo de texto con el nombre de la refactorizaci�n.
	 * 
	 * @return el campo de texto con el nombre de la refactorizaci�n.
	 */
	public Text getNameText() {
		return this.nameText;
	}

	/**
	 * Obtiene las categorias que se han seleccionado en el �rbol y a las que va
	 * a pertenecer la refactorizacion.
	 * 
	 * @return categorias a las que pertenece la refactorizacion
	 */
	public Set<Category> getCategories() {
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
			if (returnVal != null) {
				imageText.setText(returnVal);
			}
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

	/**
	 * @param refactoringImage
	 *            the refactoringImage to set
	 */
	public void setRefactoringImage(String refactoringImage) {
		this.refactoringImage = refactoringImage;
	}

	/**
	 * @return the refactoringImage
	 */
	public String getRefactoringImage() {
		return refactoringImage;
	}
}