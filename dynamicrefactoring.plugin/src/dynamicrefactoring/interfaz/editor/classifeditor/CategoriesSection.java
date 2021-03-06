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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

/**
 * Seccion de la lista de categorías de la clasificación
 * seleccionada.
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class CategoriesSection {

	/**
	 * Constante que representa la cadena vacia.
	 */
	private static final String EMPTY_STRING = "";
	
	/**
	 * Nombre de la clasificacion a editar.
	 */
	private String classification;

	/**
	 * Catalogo de clasificaciones y categorias.
	 */
	private ClassificationsCatalog catalog;

	/**
	 * Tabla con las categorias disponibles.
	 */
	private Table tbCategories;

	/**
	 * Validador que comprueba que no se repite una categoria existente.
	 */
	private NotCategoryAlreadyExistsValidator inputValidator;

	/**
	 * Botón añadir.
	 */
	private Button btAdd;

	/**
	 * Botón eliminar.
	 */
	private Button btDelete;

	/**
	 * Botón renombrar.
	 */
	private Button btRename;

	/**
	 * Crea un editor de datos de una clasificacion.
	 * 
	 * @param classification
	 *            nombre de la clasificacion a editar
	 * @param catalog
	 *            catalogo de clasificaciones
	 */
	public CategoriesSection(String classification,
			ClassificationsCatalog catalog) {
		this.classification = classification;
		this.catalog = catalog;
		this.inputValidator = new NotCategoryAlreadyExistsValidator();

	}

	/**
	 * Crea la sección de categorías.
	 * 
	 * @param toolkit toolkit
	 * @param form contenedor form
	 */
	protected void createCategoriesSection(FormToolkit toolkit,
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
		section.setText(Messages.CategoriesSection_Categories);
		section.setDescription(Messages.CategoriesSection_CategoriesForTheClassification);
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout sectionLayout = new GridLayout(2, false);
		sectionClient.setLayout(sectionLayout);

		tbCategories = toolkit.createTable(sectionClient, SWT.BORDER);

		GridData dataTbCategories = new GridData(GridData.FILL_BOTH);
		dataTbCategories.heightHint = tbCategories.getItemHeight() * 4;
		tbCategories.setLayoutData(dataTbCategories);

		Composite cpButtons = new Composite(sectionClient, SWT.NONE);
		GridData dataCpButtons = new GridData();
		dataCpButtons.verticalAlignment = GridData.BEGINNING;
		cpButtons.setLayoutData(dataCpButtons);

		RowLayout cpButLayout = new RowLayout();
		cpButLayout.type = SWT.VERTICAL;
		cpButtons.setLayout(cpButLayout);

		btAdd = toolkit.createButton(cpButtons, Messages.ClassifListSection_Add, SWT.NONE); 
		btAdd.addSelectionListener(new ButtonAddListener());
		btDelete = toolkit.createButton(cpButtons, Messages.ClassifListSection_Delete, SWT.NONE); 
		btDelete.addSelectionListener(new ButtonDeleteListener());
		btRename = toolkit.createButton(cpButtons, Messages.ClassifListSection_Rename, SWT.NONE); 
		btRename.addSelectionListener(new ButtonRenameListener());

		updateUI();
		section.setClient(sectionClient);
	}

	/**
	 * Actualiza la interfaz del plugin tras un cambio de clasificacion.
	 * 
	 * Rellena la lista de categorias de la clasificacion con la lista
	 * actualizada de categorias de la misma.
	 * 
	 * Deshabilita los botones si la nueva clasificacion no es editable.
	 * 
	 */
	private void updateUI() {
		// borramos los elementos que hay en la tabla
		tbCategories.remove(0, tbCategories.getItemCount() - 1);
		// rellenamos con los elementos actuales
		for (Category c : catalog.getClassification(classification)
				.getCategories()) {
			TableItem item = new TableItem(tbCategories, SWT.NONE);
			item.setImage(RefactoringImages.getCatIcon());
			item.setText(c.getName());
		}
		boolean classifIsEditable = catalog.getClassification(classification)
				.isEditable();
		btAdd.setEnabled(classifIsEditable);
		btRename.setEnabled(classifIsEditable);
		btDelete.setEnabled(classifIsEditable);
	}

	/**
	 * Cambia el nombre de una categoría dentro de la clasificacion.
	 * 
	 * @param oldName nombre viejo
	 * @param newName nombre nuevo
	 */
	public void renameCategory(String oldName, String newName) {
		catalog.renameCategory(classification, oldName, newName);
	}

	/**
	 * Obtiene la clasificacion que se esta editando en la interfaz.
	 * 
	 * @see #setClassification
	 * 
	 * @return clasificacion en edicion
	 */
	public Classification getClassification() {
		return catalog.getClassification(classification);
	}

	/**
	 * Se encarga de agregar una categoria a la clasificacion en edicion.
	 * 
	 * @param categoryNewName
	 *            nombre de la nueva categoria
	 */
	public void addCategory(String categoryNewName) {
		catalog.addCategoryToClassification(classification, categoryNewName);
	}

	/**
	 * Se encarga de eliminar una categoria de la clasificacion en edicion.
	 * 
	 * Si la categoria no existe en la clasificacion saltara una excepcion.
	 * 
	 * @param categoryName
	 *            nombre de la categoria a eliminar
	 */
	public void removeCategory(String categoryName) {
		catalog.removeCategory(classification, categoryName);

	}

	/**
	 * Cambia la clasificacion que el editor esta editando.
	 * 
	 * @see #getClassification
	 * 
	 * @param classification
	 *            nueva clasificacion a editar
	 */
	protected void setClassification(String classification) {
		this.classification = classification;
		updateUI();
	}

	/**
	 * Comportamiento para el borrado de categorías.
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
			if (tbCategories.getSelectionCount() > 0) {
				MessageDialog dialog = new MessageDialog(
						tbCategories.getShell(),
						Messages.CategoriesSection_DeletingCategory,
						null,
						Messages.CategoriesSection_DeletingCategoryWarning,
						MessageDialog.WARNING, new String[] {
								Messages.ClassifListSection_Proceed,
								Messages.ClassifListSection_Cancel }, 1);
				if (dialog.open() == IStatus.OK) {
					removeCategory(tbCategories.getSelection()[0].getText());
					updateUI();
				}
			}
		}
	}

	/**
	 * Comportamiento para la adición de categorías.
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
			InputDialog dialog = new InputDialog(tbCategories.getShell(),
					Messages.CategoriesSection_AddCategory,
					Messages.CategoriesSection_EnterCategoryName, EMPTY_STRING, //$NON-NLS-2$
					inputValidator);
			if (dialog.open() == IStatus.OK) {
				String categoryNewName = dialog.getValue();
				addCategory(categoryNewName);
				updateUI();
			}

		}
	}

	/**
	 * Comportamiento para el renombrado de categorías.
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
			final String oldName = tbCategories.getSelection()[0].getText();
			InputDialog dialog = new InputDialog(tbCategories.getShell(),
					Messages.CategoriesSection_RenamingCategory + oldName,
					Messages.CategoriesSection_EnterNewCategoryName, EMPTY_STRING, //$NON-NLS-2$
					inputValidator);
			if (dialog.open() == IStatus.OK) {
				String categoryNewName = dialog.getValue();
				renameCategory(oldName, categoryNewName);
				updateUI();
			}

		}
	}

	/**
	 * Validador que controla que no exista ya una categoría con el mismo nombre.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 *
	 */
	private class NotCategoryAlreadyExistsValidator implements IInputValidator {

		/**
		 * Valida si existe ya una categoría con ese nombre.
		 * 
		 * @param newText nombre de la categoría a validar
		 * 
		 * @return si ya existe una categoría con ese nombre indica el texto de error.
		 */
		@Override
		public String isValid(String newText) {
			if (catalog.getClassification(classification).getCategories()
					.contains(new Category(classification, newText))) {
				return String.format(Messages.CategoriesSection_CategoryAlreadyExists,
						classification, newText);
			}
			return null;
		}

	};

}
