/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Porveedor de etiquetas para la lista de refactorizaciones.
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class RefactoringListLabelProvider extends LabelProvider implements ITableLabelProvider{

	/**
	 * Dado el elemento refactorización devuelve la imagen asociada 
	 * que se debe mostar en la lista de refactorizaciones para dicho
	 * elemento.
	 * 
	 * @param element elemento refactorización
	 * @param columnIndex indice de la columna
	 * @return imagen a mostrar en la lista de refactorizaciones para el elemento
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof DynamicRefactoringDefinition) {
			if(((DynamicRefactoringDefinition) element).isEditable())
				return RefactoringImages.getRefIcon();
			else
				return RefactoringImages.getPluginRefIcon();
		}
		return null;
	}

	/**
	 * Dado el elemento refactorización devuelve el texto asociado 
	 * que se debe mostar en la lista de refactorizaciones para dicho
	 * elemento. Corresponde con el nombre de la refactorización.
	 * 
	 * @param element elemento refactorización
	 * @param columnIndex indice de la columna
	 * @return texto a mostrar en la lista de refactorizaciones para el elemento
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof DynamicRefactoringDefinition) {
			return ((DynamicRefactoringDefinition) element).getName();
		}
		return element.toString();
	}
}
