package dynamicrefactoring.interfaz.view.classifeditor;

import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;
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
		
		final Set<Classification> classifications = PluginClassificationsCatalog.getInstance()
		.getAllClassifications();
		final CategoriesSection clasifCatEditor = new CategoriesSection(firstClassif(classifications).getName(), PluginClassificationsCatalog.getInstance());
		final ClassificationDataSection classifDataSection = new ClassificationDataSection(PluginClassificationsCatalog.getInstance(), firstClassif(classifications).getName());
		final ClassifListSection clasifListEditor = new ClassifListSection(PluginClassificationsCatalog.getInstance());
		clasifListEditor.createClassificationsSection(toolkit, form, clasifCatEditor, classifDataSection);

		
		classifDataSection.createSelectedClassificationDataSection(toolkit, form);
		clasifCatEditor.createCategoriesSection(toolkit, form);
		
		toolkit.paintBordersFor(form.getBody());

	}


	private Classification firstClassif(
			final Set<Classification> classifications) {
		return classifications
				.iterator().next();
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
