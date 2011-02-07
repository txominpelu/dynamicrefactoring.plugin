package dynamicrefactoring.interfaz.wizard.classificationscombo;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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
			DynamicRefactoringDefinition refact) {
		tv = new CheckboxTreeViewer(parent, SWT.V_SCROLL );
		//tv.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		tv.getTree().setBounds(80, 317, 534, 160);
		tv.setContentProvider(new ClassificationsTreeContentProvider());
		tv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Classification) {
					return ((Classification) element).getName();
				}
				return element.toString();
			}
		});
		tv.addCheckStateListener(new ClassificationsCheckBoxTreeListener(
				availableClassifications));
		tv.setInput(availableClassifications);
		if(refact != null){
			tv.setCheckedElements(refact.getCategories().toArray());
		}
		this.availableClassifications = availableClassifications;
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

	/**
	 * Comprueba los cambios que se producen cuando alguna de las categorias es
	 * marcada. Evita que varias categorias sean seleccionadas cuando la
	 * clasificacion no es multicategoria.
	 * 
	 * @author imediava
	 * 
	 */
	static class ClassificationsCheckBoxTreeListener implements
			ICheckStateListener {

		private final Set<Classification> availableClassifications;

		private ClassificationsCheckBoxTreeListener(
				Set<Classification> availableClassifications) {
			this.availableClassifications = availableClassifications;
		}

		/**
		 * Evento de cambio de estado.
		 * 
		 * Si el evento corresponde a una clasificacion
		 * {@link dealWithParentCheckStateChange} se ocupa de tratarlo.
		 * 
		 * Si el evento corresponde a una categoria solo permitira marcar una si
		 * su padre no permite refactorizaciones que pertenecen a multiples
		 * categorias (refact. multicategoria).
		 */
		@Override
		public void checkStateChanged(CheckStateChangedEvent event) {
			Object elemento = event.getElement();
			CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
			if (PickCategoryTree.isParentElement(elemento)) {
				dealWithParentCheckStateChange(event,
						(Classification) elemento, viewer);
			} else if (PickCategoryTree.isChildElement(elemento)
					&& !PickCategoryTree.getParent(elemento,
							this.availableClassifications).isMultiCategory()
					&& event.getChecked()) {
				viewer.setSubtreeChecked(PickCategoryTree.getParent(elemento,
						this.availableClassifications), false);
				viewer.setChecked(elemento, true);
			}
		}

		/**
		 * Gestiona un evento en el que una clasificacion ha sido
		 * marcada/desmarcada.
		 * 
		 * Si la clasificacion es multicategoria aplica a sus categorias el
		 * mismo estado que se le ha aplicado a ella.
		 * 
		 * Si no es multicategoria se deja el arbol como estaba antes del
		 * evento.
		 * 
		 * @param event
		 *            evento
		 * @param classification
		 *            clasification objeto del evento
		 * @param viewer
		 */
		private void dealWithParentCheckStateChange(
				CheckStateChangedEvent event, Classification classification,
				CheckboxTreeViewer viewer) {
			if (classification.isMultiCategory()) {
				viewer.setSubtreeChecked(classification, event.getChecked());
			} else {
				viewer.setChecked(classification, false);
			}
		}

	}

}
