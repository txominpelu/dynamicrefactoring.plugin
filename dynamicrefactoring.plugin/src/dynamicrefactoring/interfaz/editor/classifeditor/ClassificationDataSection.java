/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.editor.classifeditor;

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

/**
 * Sección de datos de la clasificación seleccionada del editor de
 * clasificaciones.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class ClassificationDataSection {

	/**
	 * Numero de lineas de altura del campo de texto de descripcion.
	 */
	private static final int TEXT_DESCRIPTION_NUMLINE_HEIGHT = 3;
	
	/**
	 * Número de columanas de la sección.
	 */
	private static final int SECTION_NUM_COLUMNS = 3;
	
	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private ClassificationsCatalog catalog;
	
	/**
	 * Clasificacion en edición.
	 */
	private String classification;
	
	/**
	 * Botón donde se indica si la clasificación es uni o multicategoría.
	 */
	private Button chkMulti;
	
	/**
	 * Texto para el nombre de la clasificación.
	 */
	private Text txtName;
	
	/**
	 * Texto para la descripción de la clasificación.
	 */
	private Text txtDescription;
	
	/**
	 * Botón que permite la modificación de la clasificación seleccionada.
	 */
	private Button btMofifyDescription;

	/**
	 * Crea un editor de datos de clasificaciones.
	 * 
	 * @param catalog
	 *            catalogo de clasificaciones
	 * @param classification clasificación en edición
	 */
	public ClassificationDataSection(ClassificationsCatalog catalog,
			String classification) {
		this.catalog = catalog;
		this.classification = classification;

	}

	/**
	 * Crea la sección destinada a la clasificación en edición.
	 * 
	 * @param toolkit toolkit
	 * @param form contenedor form
	 */
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
		txtName.setBackground(txtName.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		txtName.setLayoutData(new GridData(GridData.FILL_BOTH));

		chkMulti = toolkit.createButton(sectionClient,
				Messages.ClassificationDataSection_Multi, SWT.CHECK);
		chkMulti.addSelectionListener(new ChkMultiCategoryListener());

		Label lblDescription = toolkit.createLabel(sectionClient,
				Messages.ClassificationDataSection_Description);
		GridData lblDescriptionLayoutData = new GridData(GridData.FILL_BOTH);
		lblDescriptionLayoutData.horizontalSpan = SECTION_NUM_COLUMNS;
		lblDescription.setLayoutData(lblDescriptionLayoutData);

		Composite cmpDescription = toolkit.createComposite(sectionClient);
		cmpDescription.setLayout(new GridLayout(2, false));
		GridData sectNameLayoutData = new GridData(GridData.FILL_BOTH);

		sectNameLayoutData.horizontalSpan = SECTION_NUM_COLUMNS;
		cmpDescription.setLayoutData(sectNameLayoutData);

		txtDescription = toolkit.createText(cmpDescription, "", SWT.MULTI
				| SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		txtDescription.setBackground(txtDescription.getDisplay()
				.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDescription.setEditable(false);
		txtDescription.setBackground(txtDescription.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData txtDescriptionData = new GridData();
		txtDescriptionData.grabExcessHorizontalSpace = true;
		txtDescriptionData.horizontalAlignment = GridData.FILL;
		txtDescriptionData.heightHint = txtDescription.getLineHeight() * TEXT_DESCRIPTION_NUMLINE_HEIGHT;
		txtDescription.setLayoutData(txtDescriptionData);

		btMofifyDescription = toolkit.createButton(cmpDescription,
				Messages.ClassificationDataSection_Modify, SWT.NONE);
		btMofifyDescription
				.addSelectionListener(new ButtonModifyDescriptionListener());

		setClassification(classification);
		
		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}

	/**
	 * Asigna la clasificacion a mostrar y editar en la seccion.
	 * 
	 * @param classification clasificación
	 */
	protected void setClassification(String classification) {
		this.classification = classification;
		boolean classifIsEditable = catalog.getClassification(classification).isEditable();
		chkMulti.setSelection(catalog.getClassification(classification)
				.isMultiCategory());
		chkMulti.setEnabled(classifIsEditable && !catalog.classifHasMultiCategoryRefactorings(classification));
		btMofifyDescription.setEnabled(classifIsEditable);
		txtDescription.setText(catalog.getClassification(classification)
				.getDescription());
		txtName.setText(classification);

	}

	/**
	 * Comportamiento para la modificación de la descripción de la clasificación.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class ButtonModifyDescriptionListener extends SelectionAdapter {

		/**
		 * Comportamiento cuando se produce la acción de selección.
		 * 
		 * @param e evento de selección
		 */
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

	/**
	 * Comportamiento para la selección de la propiedad multicategoría
	 * de la clasificación.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class ChkMultiCategoryListener extends SelectionAdapter {

		/**
		 * Comportamiento cuando se produce la acción de selección.
		 * 
		 * @param e evento de selección
		 */
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
			return formatter.format(messageArgs);
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
			return joiner.join(multiCategoryRefactoringsNames);
		}
	}

}
