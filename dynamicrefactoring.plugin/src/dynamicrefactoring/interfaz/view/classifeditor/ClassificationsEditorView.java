package dynamicrefactoring.interfaz.view.classifeditor;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;

import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

public final class ClassificationsEditorView extends EditorPart {
	
	public static final String ID = "dynamicrefactoring.editors.refactoringClasificationsEditorView"; //$NON-NLS-1$

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

		form.setText(Messages.ClassificationsEditorView_FormText1);
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


	@Override
	public void doSave(IProgressMonitor monitor) {
		
	}


	@Override
	public void doSaveAs() {
		
	}


	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
        setInput(input);
	}


	@Override
	public boolean isDirty() {
		return false;
	}


	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
