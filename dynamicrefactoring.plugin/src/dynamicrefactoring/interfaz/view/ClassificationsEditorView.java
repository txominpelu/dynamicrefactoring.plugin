package dynamicrefactoring.interfaz.view;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.imp.PluginCatalog;

public class ClassificationsEditorView extends ViewPart {
	public static final String ID = "dynamicrefactoring.views.refactoringClasificationsEditorView";

	private FormToolkit toolkit;
	private ScrolledForm form;

	/**
	 * The constructor.
	 */
	public ClassificationsEditorView() {
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

		// createClassificationsSection();
		// createSelectedClassificationDataSection();
		final Set<Classification> classifications = PluginCatalog.getInstance()
				.getAllClassifications();
		ClasifCategoriesEditorSection clasifCatEditor = new ClasifCategoriesEditorSection(
				classifications.iterator().next().getName(),
				PluginCatalog.getInstance());
		clasifCatEditor.createCategoriesSection(toolkit, form);
		toolkit.paintBordersFor(form.getBody());

	}

	private void createSelectedClassificationDataSection() {
		final Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
						| Section.EXPANDED);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Selected Classification");
		section.setDescription("Selected classification data.");

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout sectionLayout = new GridLayout(2, false);
		sectionClient.setLayout(sectionLayout);

		Label lbName = toolkit.createLabel(sectionClient, "Name:");

		Composite sectionName = toolkit.createComposite(sectionClient);
		sectionName.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout sectionNameLayout = new GridLayout(2, false);
		sectionName.setLayout(sectionNameLayout);
		Text txtName = toolkit.createText(sectionName, "Scope");
		txtName.setEditable(false);
		txtName.setEnabled(false);
		txtName.setLayoutData(new GridData(GridData.FILL_BOTH));
		Button btChangeName = toolkit.createButton(sectionName, "Rename..",
				SWT.NONE);

		Label lbMultiCategory = toolkit.createLabel(sectionClient, "Multi:");
		Button chkMulti = toolkit.createButton(sectionClient, "", SWT.CHECK);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionName);
		toolkit.paintBordersFor(sectionClient);
	}



	private void createClassificationsSection() {
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

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
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
