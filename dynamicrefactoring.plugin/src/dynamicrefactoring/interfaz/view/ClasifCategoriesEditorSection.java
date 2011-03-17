package dynamicrefactoring.interfaz.view;

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

import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

public class ClasifCategoriesEditorSection {

	/**
	 * Nombre de la clasificacion a editar.
	 */
	private String classification;

	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private Catalog catalog;

	private Table tbClassif;

	/**
	 * Validador que comprueba que no se repite una categoria existente.
	 */
	private NotCategoryAlreadyExistsValidator inputValidator;

	/**
	 * Crea un editor de datos de una clasificacion.
	 * 
	 * @param classification
	 *            nombre de la clasificacion a editar
	 * @param catalog
	 *            catalogo de clasificaciones
	 */
	public ClasifCategoriesEditorSection(
			String classification,
			Catalog catalog) {
		this.classification = classification;
		this.catalog = catalog;
		this.inputValidator = new NotCategoryAlreadyExistsValidator();

	}

	protected void createCategoriesSection(FormToolkit toolkit,
			final ScrolledForm form) {
		final Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
						| Section.EXPANDED);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Categories");
		section.setDescription("Categories for the classification.");
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout sectionLayout = new GridLayout(2, false);
		sectionClient.setLayout(sectionLayout);

		tbClassif = toolkit.createTable(sectionClient, SWT.NONE);
		fillCategoriesTableData();
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
	}

	/**
	 * Rellena la lista de categorias de la clasificacion con la lista
	 * actualizada de categorias de la misma.
	 * 
	 * @param tbClassif
	 *            tabla de clasificaciones
	 */
	private final void fillCategoriesTableData() {
		// borramos los elementos que hay en la tabla
		tbClassif.remove(0, tbClassif.getItemCount() - 1);
		// rellenamos con los elementos actuales
		for (Category c : catalog.getClassification(classification)
				.getCategories()) {
			TableItem item = new TableItem(tbClassif, SWT.NONE);
			item.setText(c.getName());
		}
	}

	/**
	 * Cambia el nombre de una categor�a dentro de la clasificacion.
	 * 
	 * @param oldName
	 * @param newName
	 */
	public void renameCategory(String oldName, String newName) {
		catalog.renameCategory(classification, oldName, newName);
	}

	/**
	 * Obtiene la clasificacion que se esta editando en la interfaz.
	 * 
	 * @return clasificacion en edicion
	 */
	public Classification getClassification() {
		return catalog.getClassification(classification);
	}

	/**
	 * Se encarga de agregar una categoria a la clasificacion en edicion.
	 * 
	 * @param categoryNewName
	 *            nombre de la nueva categoria
	 */
	public void addCategory(String categoryNewName) {
		catalog.addCategoryToClassification(classification, categoryNewName);
	}

	/**
	 * Se encarga de eliminar una categoria de la clasificacion en edicion.
	 * 
	 * Si la categoria no existe en la clasificacion saltara una excepcion.
	 * 
	 * @param categoryName
	 *            nombre de la categoria a eliminar
	 */
	public void removeCategory(String categoryName) {
		catalog.removeCategory(classification, categoryName);

	}

	/**
	 * Cambia la clasificacion que el editor esta editando.
	 * 
	 * @param classification
	 *            nueva clasificacion a editar
	 */
	protected void setClassification(String classification) {
		this.classification = classification;
		fillCategoriesTableData();
	}

	private class ButtonDeleteListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			removeCategory(tbClassif.getSelection()[0].getText());
			fillCategoriesTableData();
		}
	}

	private class ButtonAddListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			InputDialog dialog = new InputDialog(tbClassif.getShell(),
					"Add Category",
					"Please enter the name of the new Category", "",
					inputValidator);
			if (dialog.open() == IStatus.OK) {
				String categoryNewName = dialog.getValue();
				addCategory(categoryNewName);
				fillCategoriesTableData();
			}

		}
	}

	private class ButtonRenameListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			final String oldName = tbClassif.getSelection()[0].getText();
			InputDialog dialog = new InputDialog(tbClassif.getShell(),
					"Renaming Category: " + oldName,
					"Please enter the name of the new Category", "",
					inputValidator);
			if (dialog.open() == IStatus.OK) {
				String categoryNewName = dialog.getValue();
				renameCategory(oldName, categoryNewName);
				fillCategoriesTableData();
			}

		}
	}

	private class NotCategoryAlreadyExistsValidator implements IInputValidator {

		@Override
		public String isValid(String newText) {
			if (catalog.getClassification(classification).getCategories()
					.contains(new Category(classification, newText))) {
				return String.format("Category %s@%s already exists.",
						classification, newText);
			}
			return null;
		}

	};

}

