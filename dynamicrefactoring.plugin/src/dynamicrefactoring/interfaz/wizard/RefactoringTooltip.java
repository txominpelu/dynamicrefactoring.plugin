/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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

/**
 * Tooltip que muestra informacion con la descripción de una refactorización.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
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


