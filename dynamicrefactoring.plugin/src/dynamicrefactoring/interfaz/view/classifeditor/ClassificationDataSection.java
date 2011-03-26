package dynamicrefactoring.interfaz.view.classifeditor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

public final class ClassificationDataSection {

	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private ClassificationsCatalog catalog;
	/**
	 * Nombre de la clasificacion en edicion.
	 */
	private String classification;
	private Button chkMulti;

	/**
	 * Crea un editor de datos de clasificaciones.
	 * 
	 * @param catalog
	 *            catalogo de clasificaciones
	 */
	public ClassificationDataSection(ClassificationsCatalog catalog,
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

		toolkit.createLabel(sectionClient, "Name:");

		Composite sectionName = toolkit.createComposite(sectionClient);
		sectionName.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout sectionNameLayout = new GridLayout(3, true);
		sectionName.setLayout(sectionNameLayout);
		Text txtName = toolkit.createText(sectionName, "Scope");
		txtName.setEditable(false);
		txtName.setEnabled(false);
		txtName.setLayoutData(new GridData(GridData.FILL_BOTH));

		chkMulti = toolkit.createButton(sectionName, "Multi:", SWT.CHECK);
		chkMulti.setSelection(catalog.getClassification(classification).isMultiCategory());
		chkMulti.addSelectionListener(new ChkMultiCategoryListener());

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}
	
	/**
	 * Asigna la clasificacion a mostrar y editar en la seccion.
	 * 
	 * @param classification clasificacion
	 */
	protected void setClassification(String classification){
		this.classification = classification;
		chkMulti.setSelection(catalog.getClassification(classification).isMultiCategory());
		
	}

	private class ChkMultiCategoryListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!chkMulti.getSelection()
					&& catalog
							.classifHasMultiCategoryRefactorings(classification)) {
				// Create the required Status object
				Status status = new Status(
						IStatus.CANCEL, RefactoringPlugin.BUNDLE_NAME,
						"One or more refactorings belong to more than"
								+ "one of this classification's categories at the same time.");

				// Display the dialog
				ErrorDialog dialog = new ErrorDialog(chkMulti.getShell(),
								"Cannot set the classification as unicategory",
								"Cannot set this refactoring as Multicategory. One or more refactorings belong to more than"
										+ "one of it's categories at the same time.", status,
										IStatus.CANCEL);
				dialog.open();
				chkMulti.setSelection(!chkMulti.getSelection());
			} else {
				catalog.setMultiCategory(classification,
						chkMulti.getSelection());
			}

		}
	}

}
