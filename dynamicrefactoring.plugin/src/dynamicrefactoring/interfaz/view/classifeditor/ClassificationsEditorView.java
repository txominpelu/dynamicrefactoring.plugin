package dynamicrefactoring.interfaz.view.classifeditor;

import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

public final class ClassificationsEditorView extends ViewPart {
	public static final String ID = "dynamicrefactoring.views.refactoringClasificationsEditorView";

	private FormToolkit toolkit;
	private ScrolledForm form;

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
		
		final Set<Classification> classifications = PluginCatalog.getInstance()
		.getAllClassifications();
		final CategoriesSection clasifCatEditor = new CategoriesSection(classifications
				.iterator().next().getName(), PluginCatalog.getInstance());
		final ClassifListSection clasifListEditor = new ClassifListSection(PluginCatalog.getInstance());
		clasifListEditor.createClassificationsSection(toolkit, form, clasifCatEditor);

		// createSelectedClassificationDataSection();

		toolkit.paintBordersFor(form.getBody());

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
