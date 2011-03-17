package dynamicrefactoring.interfaz.view;

import java.util.Set;

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
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.imp.PluginCatalog;

public class ClassificationsEditorView extends ViewPart {
	public static final String ID = "dynamicrefactoring.views.refactoringClasificationsEditorView";

	private FormToolkit toolkit;
	private ScrolledForm form;

	private ClasifCategoriesEditorSection clasifCatEditor;

	/**
	 * The constructor.
	 */
	public ClassificationsEditorView() {
		final Set<Classification> classifications = PluginCatalog.getInstance()
				.getAllClassifications();
		clasifCatEditor = new ClasifCategoriesEditorSection(classifications
				.iterator().next().getName(), PluginCatalog.getInstance());

	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);

		form.setText("Classifications Editor");
		ColumnLayout layout = new ColumnLayout();
		form.getBody().setLayout(layout);
		createClassificationsSection(PluginCatalog.getInstance());

		// createSelectedClassificationDataSection();

		toolkit.paintBordersFor(form.getBody());

	}

	public void createClassificationsSection(Catalog catalog) {
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

		final Table tbClassif = toolkit.createTable(sectionClient, SWT.NONE);
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
		Button btDelete = toolkit.createButton(cpButtons, "Delete..", SWT.NONE);

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
	 * Passing the focus request to the form.
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit
	 */
	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

}
