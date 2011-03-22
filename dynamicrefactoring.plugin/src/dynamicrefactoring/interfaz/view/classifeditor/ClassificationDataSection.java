package dynamicrefactoring.interfaz.view.classifeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import dynamicrefactoring.domain.metadata.interfaces.Catalog;

public class ClassificationDataSection {

	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private Catalog catalog;
	/**
	 * Nombre de la clasificacion en edicion.
	 */
	private String classification;

	/**
	 * Crea un editor de datos de clasificaciones.
	 * 
	 * @param catalog
	 *            catalogo de clasificaciones
	 */
	public ClassificationDataSection(Catalog catalog,
			String classification) {
		this.catalog = catalog;
		this.classification = classification;

	}

	protected void createSelectedClassificationDataSection(FormToolkit toolkit,
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


}
