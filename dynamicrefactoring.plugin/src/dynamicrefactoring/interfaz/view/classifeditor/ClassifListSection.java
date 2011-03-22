package dynamicrefactoring.interfaz.view.classifeditor;

import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Seccion en la que aparece la lista de clasificaciones del catalogo. Dentro de
 * esta lista la clasificacion que se seleccione estara en edicion y se
 * permitira modificar sus atributos en el resto de secciones.
 * 
 * Esta seccion tambien permite eliminar, renombrar o agregar clasificaciones.
 * 
 * @author imediava
 * 
 */
public class ClassifListSection {

	private Catalog catalog;
	private Table tbClassif;

	/**
	 * Crea una seccion en la que se pueden agregar nuevas clasificaciones,
	 * eliminar clasificaciones existentes o renombrarlas.
	 * 
	 * @param catalog
	 *            catalogo sobre el que se efectuan los cambios en las
	 *            clasificaciones
	 */
	protected ClassifListSection(Catalog catalog) {
		this.catalog = catalog;
	}

	/**
	 * Se encarga de crear la interfaz grafica de la seccion de edicion de
	 * clasificaciones.
	 * 
	 * @param toolkit
	 *            toolkit
	 * @param form
	 *            formulario
	 * @param clasifCatEditor
	 *            editor de clasificaciones que sera notificado cuando sea
	 *            necesario cambiar la clasificacion a editar
	 */
	protected void createClassificationsSection(FormToolkit toolkit,
			final ScrolledForm form, final CategoriesSection clasifCatEditor) {
		final Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
						| Section.EXPANDED);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Classifications");
		section.setDescription("Pick a classification from the list to edit it"
				+ "or click on \"Add..\" to create a new one.");
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout sectionLayout = new GridLayout(2, false);
		sectionClient.setLayout(sectionLayout);

		tbClassif = toolkit.createTable(sectionClient, SWT.NONE);
		for (Classification clasif : catalog.getAllClassifications()) {
			TableItem item = new TableItem(tbClassif, SWT.NONE);
			item.setText(clasif.getName());
		}
		// Selecciona el primero de la lista
		tbClassif.select(0);

		GridData dataTbClassif = new GridData(GridData.FILL_BOTH);
		dataTbClassif.heightHint = tbClassif.getItemHeight() * 3;
		tbClassif.setLayoutData(dataTbClassif);

		Composite cpButtons = new Composite(sectionClient, SWT.NONE);
		GridData dataCpButtons = new GridData();
		dataCpButtons.verticalAlignment = GridData.BEGINNING;
		cpButtons.setLayoutData(dataCpButtons);

		RowLayout cpButLayout = new RowLayout();
		cpButLayout.type = SWT.VERTICAL;
		cpButtons.setLayout(cpButLayout);
		Button btAdd = toolkit.createButton(cpButtons, "Add..", SWT.NONE);
		btAdd.addSelectionListener(new ButtonAddListener());
		Button btDelete = toolkit.createButton(cpButtons, "Delete..", SWT.NONE);
		btDelete.addSelectionListener(new ButtonDeleteListener());
		Button btRename = toolkit.createButton(cpButtons, "Rename..", SWT.NONE);
		btRename.addSelectionListener(new ButtonRenameListener());

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		clasifCatEditor.createCategoriesSection(toolkit, form);

		tbClassif.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clasifCatEditor.setClassification(tbClassif.getSelection()[0]
						.getText());
			}
		});

	}

	/**
	 * Rellena la lista de clasificaciones del catalogo.
	 * 
	 */
	private final void updateTable() {
		// borramos los elementos que hay en la tabla
		tbClassif.remove(0, tbClassif.getItemCount() - 1);
		// rellenamos con los elementos actuales
		for (Classification classif : catalog.getAllClassifications()) {
			TableItem item = new TableItem(tbClassif, SWT.NONE);
			item.setText(classif.getName());
		}
	}

	/**
	 * Renombra una clasificacion existente.
	 * 
	 * @param classifName
	 * @param newClassifName
	 */
	protected void renameClassification(String classifName,
			String newClassifName) {
		catalog.renameClassification(classifName, newClassifName);
	}

	/**
	 * Agrega la nueva clasificacion al catalogo.
	 * 
	 * @param newClassification
	 *            nueva clasificacion a agregar
	 */
	protected void addClassification(Classification newClassification) {
		catalog.addClassification(newClassification);
	}

	/**
	 * Elimina la clasificacion seleccionada del catalogo.
	 * 
	 * @param classifName
	 *            nombre de la clasificacion a eliminar
	 */
	public void removeClassification(String classifName) {
		catalog.removeClassification(classifName);
	}

	private class ButtonDeleteListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			removeClassification(tbClassif.getSelection()[0].getText());
			updateTable();
		}
	}

	private class ButtonAddListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			InputDialog dialog = new InputDialog(tbClassif.getShell(),
					"Add Classification",
					"Please enter the name of the new Classification", "",
					new NotClassificationAlreadyExistsValidator());
			if (dialog.open() == IStatus.OK) {
				addClassification(new SimpleUniLevelClassification(dialog.getValue(),"", new HashSet<Category>()));
				updateTable();
			}

		}
	}
	
	private class ButtonRenameListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			final String oldName = tbClassif.getSelection()[0].getText();
			InputDialog dialog = new InputDialog(tbClassif.getShell(),
					"Renaming Classification: " + oldName,
					"Please enter the new name of the classification", "",
					new NotClassificationAlreadyExistsValidator());
			if (dialog.open() == IStatus.OK) {
				String categoryNewName = dialog.getValue();
				renameClassification(oldName, categoryNewName);
				updateTable();
			}

		}
	}

	private class NotClassificationAlreadyExistsValidator implements IInputValidator {

		@Override
		public String isValid(String newText) {
			if (catalog.containsClassification(newText)) {
				return String.format("Classification %s already exists.",
						newText);
			}
			return null;
		}

	};

}
