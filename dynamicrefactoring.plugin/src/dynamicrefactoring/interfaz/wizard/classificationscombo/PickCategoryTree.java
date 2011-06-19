/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard.classificationscombo;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Árbol para escoger las categorias a las que pertenece una refactorizacion.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class PickCategoryTree {

	/**
	 * Árbol de categorias de clasificaciones.
	 */
	private final CheckboxTreeViewer tv;
	
	/**
	 * Clasificaciones disponibles.
	 */
	private final Set<Classification> availableClassifications;

	/**
	 * Listener de cambios en la selección del árbol de categorías.
	 */
	private ICheckStateListener checkStateListener;
	
	/**
	 * Crea el arbol que permite escoger las categorías a las que pertenece la
	 * refactorizacion.
	 * 
	 * @param parent
	 *            elemento de la GUI en la que se incrustara el arbol
	 * @param availableClassifications
	 *            conjunto de clasificaciones existentes
	 * @param categories
	 *            conjunto de categorías
	 */
	public PickCategoryTree(Composite parent,
			Set<Classification> availableClassifications,
			Set<Category> categories) {
		this(parent, availableClassifications, categories,
				new ClassificationsCheckBoxTreeListener(
						availableClassifications));
	}

	/**
	 * Crea el arbol que permite escoger las categoriías a las que pertenece la
	 * refactorizacion.
	 * 
	 * @param parent
	 *            elemento de la GUI en la que se incrustara el arbol
	 * @param availableClassifications
	 *            conjunto de clasificaciones existentes
	 * @param categories
	 *            conjunto de categorías
	 * @param checkStateListener
	 *            gestionara los eventos del arbol
	 */
	public PickCategoryTree(Composite parent,
			Set<Classification> availableClassifications,
			Set<Category> categories, ICheckStateListener checkStateListener) {
		Preconditions.checkArgument(allCategoriesExistInAClassification(
				availableClassifications, categories));
		tv = new CheckboxTreeViewer(parent, SWT.V_SCROLL | SWT.BORDER
				| SWT.CHECK);
		tv.setContentProvider(new ClassificationsTreeContentProvider());
		tv.setLabelProvider(new PickCategoryTreeLabelProvider());
		tv.addCheckStateListener(checkStateListener);
		tv.setInput(availableClassifications);

		setTreeInitialState(availableClassifications, categories,
				checkStateListener);
		
		this.availableClassifications = availableClassifications;
		this.checkStateListener = checkStateListener;

	}

	/**
	 * Se asegura de que todas las categorias pasadas existen entre las
	 * clasificaciones pasadas.
	 * 
	 * @param availableClassifications2
	 *            clasificaciones
	 * @param categories
	 *            categorias
	 * @return devuelve si todas las categorias se corresponden con alguna
	 *         clasificacion
	 */
	private boolean allCategoriesExistInAClassification(
			Set<Classification> availableClassifications2,
			Set<Category> categories) {
		for(final Category c: categories){
			if(notCategoryExistInAvailableClassifications(availableClassifications2, c)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Devuelve si una categoria pertenece o no a una de las 
	 * clasificaciones disponibles.
	 * 
	 * @param availableClassifications2 clasificaciones disponibles
	 * @param category categoria
	 * @return verdadero si la categoría pertenece,
	 *  	   falseo en caso contrario.
	 */
	private boolean notCategoryExistInAvailableClassifications(
			Set<Classification> availableClassifications2, final Category category) {
		return Collections2.filter(availableClassifications2, new Predicate<Classification>(){

			@Override
			public boolean apply(Classification arg0) {
				return category.getParent().equals(arg0.getName()) && arg0.getCategories().contains(category);
			}
			
		}).isEmpty();
	}

	/**
	 * Cambia las categorias seleccionadas de un
	 * arbol.
	 * 
	 * @param selectedCategories categorias seleccionadas
	 */
	public void setSelectedCategories(
			Set<Category> selectedCategories) {
		setTreeInitialState(availableClassifications, selectedCategories, checkStateListener);
	}

	

	/**
	 * Obtiene el control que permite escoger las categorías de una
	 * clasificación.
	 * 
	 * @return control
	 */
	public Control getControl() {
		return tv.getControl();
	}

	/**
	 * Marca las categorias a las que la refactorizacion pertenece y las
	 * clasificaciones en las que hay categorías marcadas como gris.
	 * 
	 * @param availableClassifications
	 *            clasificaciones disponibles
	 * @param categories
	 *            categorias a las que pertenece inicialmente la refactorizacion
	 * @param checkStateListener
	 *            encargado de lidiar con los eventos de cambio de estado del
	 *            arbol
	 */
	private void setTreeInitialState(
			Set<Classification> availableClassifications,
			Set<Category> categories, ICheckStateListener checkStateListener) {
		tv.setCheckedElements(categories.toArray());
		for (Classification classification : availableClassifications) {
			ClassificationsCheckBoxTreeListener.grayParentIfNeeded(
					classification, tv);
		}
	}

	/**
	 * Obtiene las categorias que han sido marcadas en el arbol y a las que por
	 * tanto la refactorización pertenecerá.
	 * 
	 * @return categorias a las que la refactorizacion pertenece
	 */
	public Set<Category> getRefactoringCategories() {
		Set<Category> categorias = new HashSet<Category>();
		for (Object elemento : tv.getCheckedElements()) {
			if (elemento instanceof Category) {
				categorias.add((Category) elemento);
			}
		}
		return categorias;
	}

	/**
	 * Obtiene las categorias hijas de una clasificacion.
	 * 
	 * @param parentElement
	 *            elemento clasificacion
	 * @return array de categorias de la clasificacion
	 */
	static Object[] createClassificationChildren(Object parentElement) {
		Classification cl = (Classification) parentElement;
		return new TreeSet<Category>(cl.getCategories()).toArray();
	}

	/**
	 * Obtiene si el elemento es un elemento padre del arbol.
	 * 
	 * @param elemento
	 *            elemento a comprobar
	 * @return si el elemento es padre o no
	 */
	static boolean isParentElement(Object elemento) {
		return elemento instanceof Classification;
	}

	/**
	 * Obtiene el padre del elemento dado en el arbol.
	 * 
	 * @param categoryObject
	 *            elemento categoria del que se quiere obtener la clasificacion
	 *            a la que pertenece
	 * @param availableClassifications
	 *            conjunto de clasificaciones existentes
	 * 
	 * @return padre del elemento
	 */
	static Classification getParent(Object categoryObject,
			Set<Classification> availableClassifications) {
		final Category category = (Category) categoryObject;
		return Collections2
		.filter(availableClassifications,
				new Predicate<Classification>() {

			@Override
			public boolean apply(Classification arg0) {
				return arg0.getName().equals(
						category.getParent());
			}

		}).iterator().next();

	}

	/**
	 * Devuelve si el elemento es un elemento hoja del árbol.
	 * 
	 * @param elemento
	 *            elemento a comprobar
	 * @return si el nodo es o no hoja del arbol
	 */
	static boolean isChildElement(Object elemento) {
		return elemento instanceof Category;
	}

	/**
	 * Proveedor de etiquets para los elementos del árbol.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 */
	private static class PickCategoryTreeLabelProvider extends LabelProvider
	implements ILabelProvider {

		/**
		 * Dado el elemento devuelve el texto que se debe mostar en el arbol de
		 * seleccion de categorias para el elemento. Sera el nombre de la
		 * categoría o clasificacion.
		 * 
		 * @param element
		 *            elemento
		 * @return texto a mostrar en el arbol para el elemento
		 */
		@Override
		public String getText(Object element) {
			if (element instanceof Classification) {
				return ((Classification) element).getName();
			}
			if (element instanceof Category) {
				return ((Category) element).getName();
			}
			return element.toString();
		}

		/**
		 * Dado el elemento devuelve la imagen que se debe mostar en el arbol de
		 * seleccion de categorias para el elemento.
		 * 
		 * @param element
		 *            elemento
		 * @return imagen a mostrar en el arbol para el elemento
		 */
		@Override
		public Image getImage(Object element) {
			if (element instanceof Classification) {
				return RefactoringImages.getClassIcon();
			}
			return RefactoringImages.getCatIcon();
		}

	}

}
