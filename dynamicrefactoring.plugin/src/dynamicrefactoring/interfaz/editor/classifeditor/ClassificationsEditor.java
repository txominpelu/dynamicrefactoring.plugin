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

/**
 * Clase principal del editor de clasificaciones.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class ClassificationsEditor extends EditorPart  {
	
	/**
	 * Identificador del editor de clasificaciones.
	 */
	public static final String ID = "dynamicrefactoring.editors.refactoringClasificationsEditor"; //$NON-NLS-1$

	/**
	 * Toolkit.
	 */
	private FormToolkit toolkit;
	
	/**
	 * Contenedor form.
	 */
	private ScrolledForm form;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 * @param parent contenedor padre.
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

	/**
	 * Devuelve la primera clasificación del conjunto de clasificaciones.
	 * 
	 * @param classifications conjunto de clasificaciones
	 * @return clasificación que se encuentra en primer lugar
	 */
	private Classification firstClassif(final Set<Classification> classifications) {
		return classifications.iterator().next();
	}

	
	/**
	 * Passing the focus request to the form.
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit.
	 */
	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
	
	/**
	 * Guarda el contenido.
	 * @param monitor monitor de progreso
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	/**
	 * Guarda el contenido en otro objeto.
	 */
	@Override
	public void doSaveAs() {
	}

	/**
	 * Inicializa el editor.
	 * 
	 * @param site interfaz del editor
	 * @param input entrada del editor
	 * 
	 * @throws PartInitException si existe algun 
	 *         problema al inicializar
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
        setInput(input);
	}

	/**
	 * Indica si el contenido ha podido cambiar desde el último
	 * salvado.
	 * 
	 * @return verdadero si el contenido ha podido cambiar
	 *         falso en caso contrario.
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/**
	 * Indica si el salvar como esta permitido.
	 * 
	 * @return verdadero si salvar como esta permitido,
	 *         falso en caso contrario.
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}


}
