package dynamicrefactoring.interfaz.wizard;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

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

		Form form = toolkit.createForm(parent);
		form.setText(refactoring.getName());
		form.setTextBackground(new Color[] { top, bot }, new int[] { 100 }, true);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		form.getBody().setLayout(layout);

		StringBuffer buf = new StringBuffer();
		buf.append("<form>");
		buf.append("<p>");
		buf.append("<b>");
		buf.append(Messages.RefactoringTooltip_Description);
		buf.append("</b>");
		buf.append(": ");
		buf.append(refactoring.getDescription());
		buf.append("</p>");
		buf.append("<p>");
		buf.append("<b>");
		buf.append(Messages.RefactoringTooltip_Motivation);
		buf.append("</b>");
		buf.append(": ");
		buf.append(refactoring.getMotivation());
		buf.append("</p>");
		buf.append("</form>");

		FormText formText = toolkit.createFormText(form.getBody(), true);
		formText.setWhitespaceNormalized(true);
		GridData td = new GridData();
		td.horizontalSpan = 2;
		td.widthHint = 400;
		formText.setLayoutData(td);
		formText.setText(buf.toString(), true, false);
		
		return parent;
	}
} 


