package dynamicrefactoring.interfaz.view.classifeditor;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

public final class ClassificationDataSection {

	private static final int SECTION_NUM_COLUMNS = 3;
	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private ClassificationsCatalog catalog;
	/**
	 * Nombre de la clasificacion en edicion.
	 */
	private String classification;
	private Button chkMulti;
	private Text txtName;
	private Text txtDescription;

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
		section.setText(Messages.ClassificationDataSection_SelectedClassification);
		section.setDescription(Messages.ClassificationDataSection_SelectedClassificationData);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout sectionLayout = new GridLayout(SECTION_NUM_COLUMNS, false);
		sectionClient.setLayout(sectionLayout);

		toolkit.createLabel(sectionClient,
				Messages.ClassificationDataSection_Name);

		txtName = toolkit.createText(sectionClient, classification);
		txtName.setEditable(false);
		txtName.setEnabled(false);
		txtName.setLayoutData(new GridData(GridData.FILL_BOTH));

		chkMulti = toolkit.createButton(sectionClient,
				Messages.ClassificationDataSection_Multi, SWT.CHECK);
		chkMulti.setSelection(catalog.getClassification(classification)
				.isMultiCategory());
		chkMulti.addSelectionListener(new ChkMultiCategoryListener());

		Label lblDescription = toolkit.createLabel(sectionClient,
				Messages.ClassificationDataSection_Description);
		GridData lblDescriptionLayoutData = new GridData(GridData.FILL_BOTH);
		lblDescriptionLayoutData.horizontalSpan = SECTION_NUM_COLUMNS;
		lblDescription.setLayoutData(lblDescriptionLayoutData);

		Composite cmpDescription = toolkit.createComposite(sectionClient);
		cmpDescription.setLayout(new GridLayout(2, false));
		GridData sectNameLayoutData = new GridData(GridData.FILL_BOTH);
		;
		sectNameLayoutData.horizontalSpan = SECTION_NUM_COLUMNS;
		cmpDescription.setLayoutData(sectNameLayoutData);

		txtDescription = toolkit.createText(cmpDescription, catalog
				.getClassification(classification).getDescription(), SWT.MULTI
				| SWT.V_SCROLL | SWT.WRAP);
		txtDescription.setBackground(txtDescription.getDisplay()
				.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(GridData.FILL_BOTH));

		Button btMofifyDescription = toolkit.createButton(cmpDescription,
				Messages.ClassificationDataSection_Modify, SWT.NONE);
		btMofifyDescription
				.addSelectionListener(new ButtonModifyDescriptionListener());

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}

	/**
	 * Asigna la clasificacion a mostrar y editar en la seccion.
	 * 
	 * @param classification
	 *            clasificacion
	 */
	protected void setClassification(String classification) {
		this.classification = classification;
		chkMulti.setSelection(catalog.getClassification(classification)
				.isMultiCategory());
		chkMulti.setEnabled(!catalog.classifHasMultiCategoryRefactorings(classification));
		txtDescription.setText(catalog.getClassification(classification)
				.getDescription());
		txtName.setText(classification);

	}

	private class ButtonModifyDescriptionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			InputDialog dialog = new InputDialog(chkMulti.getShell(),
					Messages.ClassificationDataSection_ModifyDescription,
					Messages.ClassificationDataSection_EnterNewDescription, "", //$NON-NLS-2$
					null);
			if (dialog.open() == IStatus.OK) {
				String newDescription = dialog.getValue();
				catalog.setDescription(classification, newDescription);
				setClassification(classification);
			}

		}
	}

	private class ChkMultiCategoryListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!chkMulti.getSelection()
					&& catalog
							.classifHasMultiCategoryRefactorings(classification)) {

				// Create the required Status object
				Status status = new Status(IStatus.CANCEL,
						RefactoringPlugin.BUNDLE_NAME,
						getCannotBeUnicategoryErrorMessage());

				// Display the dialog
				ErrorDialog.openError(chkMulti.getShell(),
						Messages.ClassificationDataSection_CannotBeUniCategory,
						null, status, IStatus.CANCEL);
				chkMulti.setSelection(!chkMulti.getSelection());
			} else {
				catalog.setMultiCategory(classification,
						chkMulti.getSelection());
			}

		}

		/**
		 * Devuelve el mensaje de error.
		 * 
		 * @return mensaje de error que se muestra cuando se intenta hacer
		 *         unicategoria una clasificacion que no lo puede ser
		 */
		private String getCannotBeUnicategoryErrorMessage() {
			Object[] messageArgs = {
					getMultiCategoryRefactoringsFormatedNames(), classification };
			MessageFormat formatter = new MessageFormat(Messages.ClassificationDataSection_CannotBeUniCategoryErrorDescription); //$NON-NLS-1$
			String message = formatter.format(messageArgs);
			return message;
		}

		/**
		 * Obtiene una cadean con la lista de los nombres de las
		 * refactorizaciones que son multicategoria separados por comas.
		 * 
		 * @return lista de los nombres de las refactorizaciones que son
		 *         multicategoria separados por comas
		 */
		private String getMultiCategoryRefactoringsFormatedNames() {
			Collection<String> multiCategoryRefactoringsNames = Collections2
					.transform(
							catalog.getClassifMultiCategoryRefactorings(classification),
							new Function<DynamicRefactoringDefinition, String>() {
								@Override
								public String apply(
										DynamicRefactoringDefinition arg0) {
									return arg0.getName();
								}
							});
			Joiner joiner = Joiner.on(", ");
			joiner.skipNulls(); // does nothing!
			String names = joiner.join(multiCategoryRefactoringsNames);
			return names;
		}
	}

}
