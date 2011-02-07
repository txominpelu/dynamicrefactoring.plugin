package dynamicrefactoring.interfaz.wizard.classificationscombo;

import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.base.Preconditions;

import dynamicrefactoring.domain.metadata.interfaces.Classification;

class ClassificationsTreeContentProvider implements ITreeContentProvider {

	private Set<Classification> availableClassifications;

	@Override
	public void dispose() {
	}


	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}


	@Override
	public Object[] getElements(Object inputElement) {
		Preconditions.checkArgument(inputElement instanceof Set<?>);

		@SuppressWarnings("unchecked")
		Set<Classification> classifications = (Set<Classification>) inputElement;
		this.availableClassifications = classifications;
		return ((Set<?>) inputElement).toArray();

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (hasChildren(parentElement)) {
			return PickCategoryTree
					.createClassificationChildren(parentElement);
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return PickCategoryTree.getParent(element, availableClassifications);
	}

	@Override
	public boolean hasChildren(Object element) {
		return PickCategoryTree.isParentElement(element);
	}

}



