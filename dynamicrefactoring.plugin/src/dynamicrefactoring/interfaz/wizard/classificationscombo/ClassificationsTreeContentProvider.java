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

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.base.Preconditions;

import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Proveedor de contenido para el árbol de selección de categorías del asistente
 * de edición de refactorizaciones.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
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
		this.availableClassifications = new TreeSet<Classification>(classifications);
		return (new TreeSet<Classification>(availableClassifications)).toArray();

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



