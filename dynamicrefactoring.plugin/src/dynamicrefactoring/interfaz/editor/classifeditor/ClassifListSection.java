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

import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.google.common.collect.ImmutableSortedSet;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

/**
 * Seccion en la que aparece la lista de clasificaciones del catalogo. Dentro de
 * esta lista la clasificacion que se seleccione estara en edicion y se
 * permitira modificar sus atributos en el resto de secciones.
 * 
 * Esta seccion tambien permite eliminar, renombrar o agregar clasificaciones.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class ClassifListSection {

	/**
	 * Numero de lineas de alto que tendra la tabla de clasificaciones.
	 */
	private static final int TABLE_CLASSIF_LINES_HEIGHT = 3;
	
	/**
	 * Constante que representa la cadena vacia.
	 */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	/**
	 * Tooltip del botón de agregar clasificación.
	 */
	public static final String ADD_CLASSIFICATION_BUTTON_TOOLTIP = Messages.ClassifListSection_AddClassification;
	
	/**
	 * Catálogo de clasificaciones.
	 */
	private ClassificationsCatalog catalog;
	
	/**
	 * Tabla de clasificaciones.
	 */
	private Table tbClassif;
	
	/**
	 * Botón de borrado de clasificaciones.
	 */
	private Button btDelete;
	
	/**
	 * Botón de renombrado de clasificaciones.
	 */
	private Button btRename;

	/**
	 * Crea una seccion en la que se pueden agregar nuevas clasificaciones,
	 * eliminar clasificaciones existentes o renombrarlas.
	 * 
	 * @param catalog
	 *            catalogo sobre el que se efectuan los cambios en las
	 *            clasificaciones
	 */
	protected ClassifListSection(ClassificationsCatalog catalog) {
		this.catalog = catalog;
	}

	/**
	 * Se encarga de crear la interfaz grafica de la seccion de edicion de
	 * clasificaciones.
	 * 
	 * @param toolkit
	 *            toolkit
	 * @param form
	 *            formulario
	 * @param clasifCatEditor
	 *            editor de clasificaciones que sera notificado cuando sea
	 *            necesario cambiar la clasificacion a editar
	 * @param classifDataSection
	 * 			  sección donde se muestran los datos de la clasificación
	 * 			  seleccionada.
	 */
	protected void createClassificationsSection(FormToolkit toolkit,
			final ScrolledForm form, final CategoriesSection clasifCatEditor,
			final ClassificationDataSection classifDataSection) {
		final Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
						| Section.EXPANDED);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText(Messages.ClassifListSection_Classifications);
		section.setDescription(Messages.ClassifListSection_PickClassificationFromList); //$NON-NLS-1$
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout sectionLayout = new GridLayout(2, false);
		sectionClient.setLayout(sectionLayout);

		tbClassif = toolkit.createTable(sectionClient, SWT.NONE);

		GridData dataTbClassif = new GridData(GridData.FILL_BOTH);
		dataTbClassif.heightHint = tbClassif.getItemHeight()
				* TABLE_CLASSIF_LINES_HEIGHT;
		tbClassif.setLayoutData(dataTbClassif);

		Composite cpButtons = new Composite(sectionClient, SWT.NONE);
		GridData dataCpButtons = new GridData();
		dataCpButtons.verticalAlignment = GridData.BEGINNING;
		cpButtons.setLayoutData(dataCpButtons);

		RowLayout cpButLayout = new RowLayout();
		cpButLayout.type = SWT.VERTICAL;
		cpButtons.setLayout(cpButLayout);
		Button btAdd = toolkit.createButton(cpButtons,
				Messages.ClassifListSection_Add, SWT.NONE);
		btAdd.setToolTipText(ADD_CLASSIFICATION_BUTTON_TOOLTIP);
		btAdd.addSelectionListener(new ButtonAddListener());
		btDelete = toolkit.createButton(cpButtons,
				Messages.ClassifListSection_Delete, SWT.NONE);
		btDelete.setToolTipText(Messages.ClassifListSection_DeleteClassificationToolTip);
		btDelete.addSelectionListener(new ButtonDeleteListener());
		btDelete.setEnabled(false);
		btRename = toolkit.createButton(cpButtons,
				Messages.ClassifListSection_Rename, SWT.NONE);
		btRename.setToolTipText(Messages.ClassifListSection_RenameClassificationToolTip);
		btRename.addSelectionListener(new ButtonRenameListener());
		btRename.setEnabled(false);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		tbClassif.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tbClassif.getSelectionCount() > 0){
					updateButtons(tbClassif.getSelection()[0].getText());
					clasifCatEditor.setClassification(tbClassif.getSelection()[0]
							.getText());
					classifDataSection.setClassification(tbClassif.getSelection()[0]
							.getText());
				}

			}
		});

		updateTable();

	}

	/**
	 * Actualiza la interfaz tras un cambio de clasificacion por parte del
	 * usuario.
	 * 
	 * Rellena la tabla con la lista de clasificaciones del catalogo.
	 * 
	 * Desactiva ciertos botones si la clasificacion seleccionada no es
	 * editable.
	 * 
	 */
	private void updateTable() {
		// borramos los elementos que hay en la tabla
		tbClassif.remove(0, tbClassif.getItemCount() - 1);
		
		// rellenamos con los elementos actuales
		for (Classification classif : ImmutableSortedSet.copyOf(catalog
				.getAllClassifications())) {
			TableItem item = new TableItem(tbClassif, SWT.NONE);
			if(classif.isEditable()){
				item.setImage(RefactoringImages.getClassIcon());
			}else{
				item.setImage(RefactoringImages.getPluginClassificationIcon());
			}
			
			item.setText(classif.getName());
		}

	}

	/**
	 * Renombra una clasificacion existente.
	 * 
	 * @param classifName nombre de la clasificación
	 * @param newClassifName nuevo nombre de la clasificación
	 */
	protected void renameClassification(String classifName,
			String newClassifName) {
		catalog.renameClassification(classifName, newClassifName);
	}

	/**
	 * Agrega la nueva clasificacion al catalogo.
	 * 
	 * @param newClassification
	 *            nueva clasificacion a agregar
	 */
	protected void addClassification(Classification newClassification) {
		catalog.addClassification(newClassification);
	}

	/**
	 * Elimina la clasificacion seleccionada del catalogo.
	 * 
	 * @param classifName
	 *            nombre de la clasificacion a eliminar
	 */
	public void removeClassification(String classifName) {
		catalog.removeClassification(classifName);
	}

	/**
	 * Comportamiento para el borrado de la clasificación.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class ButtonDeleteListener extends SelectionAdapter {

		/**
		 * Comportamiento cuando se produce la acción de selección.
		 * 
		 * @param e evento de selección
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {

			if (tbClassif.getSelectionCount() > 0) {
				MessageDialog dialog = new MessageDialog(
						tbClassif.getShell(),
						Messages.ClassifListSection_DeleteClassification,
						null,
						Messages.ClassifListSection_DeletingClassificationWarning,
						MessageDialog.WARNING, new String[] {
								Messages.ClassifListSection_Proceed,
								Messages.ClassifListSection_Cancel }, 1);
				if (dialog.open() == IStatus.OK) {
					removeClassification(tbClassif.getSelection()[0].getText());
					updateTable();
					selectClassificationAndUpdateGUI(0);
				}
			}
		}
	}

	/**
	 * Comportamiento para la adición de una clasificación.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class ButtonAddListener extends SelectionAdapter {

		/**
		 * Comportamiento cuando se produce la acción de selección.
		 * 
		 * @param e evento de selección
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			InputDialog dialog = new InputDialog(tbClassif.getShell(),
					ADD_CLASSIFICATION_BUTTON_TOOLTIP,
					Messages.ClassifListSection_EnterNewClassificationName,
					EMPTY_STRING, 
					new NotClassificationAlreadyExistsValidator());
			if (dialog.open() == IStatus.OK) {
				addClassification(new SimpleUniLevelClassification(
						dialog.getValue(), EMPTY_STRING,
						new HashSet<Category>(), false)); 
				updateTable();
				selectClassificationAndUpdateGUI(getTableClassificationIndex(dialog
						.getValue()));
			}

		}
	}

	/**
	 * Comportamiento para el renombrado de la clasificación.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class ButtonRenameListener extends SelectionAdapter {

		/**
		 * Comportamiento cuando se produce la acción de selección.
		 * 
		 * @param e evento de selección
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			final String oldName = tbClassif.getSelection()[0].getText();
			InputDialog dialog = new InputDialog(tbClassif.getShell(),
					Messages.ClassifListSection_RenamingClassification
							+ oldName,
					Messages.ClassifListSection_EnterNewClassificationName,
					EMPTY_STRING, 
					new NotClassificationAlreadyExistsValidator());
			if (dialog.open() == IStatus.OK) {
				String newName = dialog.getValue();
				renameClassification(oldName, newName);
				updateTable();
				selectClassificationAndUpdateGUI(getTableClassificationIndex(newName));
			}

		}
	}

	/**
	 * Dada una clasificacion que debe existir en la tabla (sino salta
	 * IllegalArgumentException)
	 * 
	 * devuelve el indice que ocupa dicha clasificacion en la tabla.
	 * 
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @return indice de la clasificacion en la tabla
	 */
	private int getTableClassificationIndex(String classificationName) {
		for (int i = 0; i < tbClassif.getItems().length; i++) {
			if (tbClassif.getItems()[i].getText().equals(classificationName)) {
				return i;
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Selecciona la clasificacion en el indice dado y lanza el evento de
	 * selección para que el resto de secciones del editor de clasificaciones se
	 * actualice.
	 * 
	 * @param index
	 *            indice del elemento a seleccionar
	 */
	private void selectClassificationAndUpdateGUI(int index) {
		// Seleccionamos el primer elemento de la lista
		tbClassif.select(index);
		// Hacemos que se actualice la interfaz con el cambio
		tbClassif.notifyListeners(SWT.Selection, new Event());
		updateButtons(tbClassif.getItem(index).getText());
	}

	/**
	 * Actualiza los botones desactivando los que deban estarlo si la
	 * clasificacion no es editable.
	 * 
	 * @param selectedClassification
	 *            clasificacion actualmente seleccionada
	 */
	private void updateButtons(String selectedClassification) {
		boolean classifIsEditable = catalog.getClassification(
				selectedClassification).isEditable();
		btDelete.setEnabled(classifIsEditable);
		btRename.setEnabled(classifIsEditable);
	}

	/**
	 * Validador que controla que no exista ya una clasificación con el mismo nombre.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class NotClassificationAlreadyExistsValidator implements
			IInputValidator {

		/**
		 * Valida si existe ya una clasificación con ese nombre.
		 * 
		 * @param newText nombre de la clasificación a validar
		 * 
		 * @return si ya existe una clasificación con ese nombre indica el texto de error.
		 */
		@Override
		public String isValid(String newText) {
			if (catalog.containsClassification(newText)) {
				return String
						.format(Messages.ClassifListSection_ClassificationAlreadyExists,
								newText);
			}
			return null;
		}

	};

}
