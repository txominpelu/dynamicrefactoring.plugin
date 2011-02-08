package dynamicrefactoring.interfaz.wizard.classificationscombo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import dynamicrefactoring.PluginImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

public final class PickCategoryTree {

	private final CheckboxTreeViewer tv;
	private final Set<Classification> availableClassifications;

	/**
	 * Crea el arbol que permite escoger las categorías a las que pertenece la
	 * refactorizacion.
	 * 
	 * @param parent
	 *            elemento de la GUI en la que se incrustara el arbol
	 * @param availableClassifications
	 *            conjunto de clasificaciones existentes
	 * @param refact
	 *            refactorizacion sobre la que se va a crear el arbol. Si la
	 *            refactorizacion ya pertenece a alguna categoria esas
	 *            apareceran marcadas en el arbol
	 * @return arbol para escoger las categorias a las que pertenece una
	 *         refactorizacion
	 */
	public PickCategoryTree(Composite parent,
			Set<Classification> availableClassifications,
			Set<Category> categories, Rectangle bounds, boolean enabled) {
		tv = new CheckboxTreeViewer(parent, SWT.V_SCROLL | SWT.BORDER
				| SWT.CHECK);
		// tv.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// false, 2, 1));
		tv.getTree().setBounds(bounds);
		tv.setContentProvider(new ClassificationsTreeContentProvider());
		tv.setLabelProvider(new PickCategoryTreeLabelProvider());
		ClassificationsCheckBoxTreeListener checkStateListener = new ClassificationsCheckBoxTreeListener(
				availableClassifications);
		tv.addCheckStateListener(checkStateListener);
		tv.setInput(availableClassifications);
		setTreeInitialState(availableClassifications, categories,
				checkStateListener);
		tv.getTree().setEnabled(enabled);
		this.availableClassifications = availableClassifications;
	}

	/**
	 * Marca las categorias a las que la refactorizacion pertenece y las
	 * clasificaciones en las que hay categor�as marcadas como gris.
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
			Set<Category> categories,
			ClassificationsCheckBoxTreeListener checkStateListener) {
		tv.setCheckedElements(categories.toArray());
		for (Classification classification : availableClassifications) {
			checkStateListener.grayParentIfNeeded(classification, tv);
		}
	}

	/**
	 * Obtiene las categorias que han sido marcadas en el arbol y a las que por
	 * tanto la refactorización pertenecerá.
	 * 
	 * @param checkedElements
	 *            elementos marcados en el arbol
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
		return cl.getCategories().toArray();
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

	

	private static class PickCategoryTreeLabelProvider extends LabelProvider
			implements ILabelProvider {

		/**
		 * Dado el elemento devuelve el texto que se debe mostar en el arbol de
		 * seleccion de categorias para el elemento. Sera el nombre de la
		 * categor�a o clasificacion.
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
				return PluginImages.getClassGifIcon();
			}
			return PluginImages.getRefPngIcon();
		}

	}

}
