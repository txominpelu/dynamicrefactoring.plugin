package dynamicrefactoring.interfaz.wizard;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;

public class RefactoringTooltip extends ToolTip {

	private DynamicRefactoringDefinition refactoring;
	
	public RefactoringTooltip(Control control) {
		super(control);
		refactoring=(DynamicRefactoringDefinition)control.getData();
	}

	protected Composite createToolTipContentArea(Event event,
			Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		FormColors colors = toolkit.getColors();
		Color top = colors.getColor(IFormColors.H_GRADIENT_END);
		Color bot = colors.getColor(IFormColors.H_GRADIENT_START);

		// create the base form
		Form form = toolkit.createForm(parent);
		form.setText(refactoring.getName());
		form.setTextBackground(new Color[] { top, bot }, new int[] { 100 }, true);
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 3;
//		form.getBody().setLayout(layout);

		Label description= toolkit.createLabel(form.getBody(), "Description:");
		Label descriptionText= toolkit.createLabel(form.getBody(), refactoring.getDescription(),SWT.WRAP);
		Label motivation= toolkit.createLabel(form.getBody(), "Motivation:");
		Label motivationText= toolkit.createLabel(form.getBody(), refactoring.getMotivation(),SWT.WRAP);
		
		
		
		// create the text for user information
//		FormText text = toolkit.createFormText(form.getBody(), true);
//		GridData td = new GridData();
//		td.horizontalSpan = 2;
//		td.heightHint = 100;
//		td.widthHint = 200;
//		text.setLayoutData(td);
//		
//		text.setText(
//			"<form><p7gt;snippet8</p><p>snippet8</p></form>", 
//			true, 
//			false);

		// create the picture representing the user
//		GridData td = new GridData();
//		td.horizontalSpan = 1;
//		td.heightHint = 100;
//		td.widthHint = 64;
//		FormText formImage = 
//			toolkit.createFormText(form.getBody(), false);
//		formImage.setText(
//			"<form><p><img href=\"image\"/></p></form>", 
//			true, false);
//		formImage.setLayoutData(td);
//
//		formImage.setImage("image", RefactoringImages.getHelpIcon());

		return parent;
	}
} 


