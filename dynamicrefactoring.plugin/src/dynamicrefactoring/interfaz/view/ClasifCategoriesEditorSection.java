package dynamicrefactoring.interfaz.view;

import org.eclipse.swt.SWT;
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

	private FormToolkit toolkit;

	private ScrolledForm form;

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
		this.form = form;
		this.toolkit = toolkit;
		this.classification = classification;
		this.catalog = catalog;

	}

	private void createCategoriesSection() {
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

		final Table tbClassif = toolkit.createTable(sectionClient, SWT.NONE);
		for (int loopIndex = 0; loopIndex < 9; loopIndex++) {
			TableItem item = new TableItem(tbClassif, SWT.NONE);
			item.setText("Item " + loopIndex);
		}
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
		Button btDelete = toolkit.createButton(cpButtons, "Delete..", SWT.NONE);
		Button btRename = toolkit.createButton(cpButtons, "Rename..", SWT.NONE);

		section.setClient(sectionClient);
	}

	/**
	 * Cambia el nombre de una categoría dentro de la clasificacion.
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

}

