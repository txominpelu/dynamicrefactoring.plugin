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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Proporciona el orden en el que se desea visualizar la lista de
 * refactorizaciones. Este orden atiende al nombre de las mismas.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class RefactoringListSorter extends ViewerSorter{

	 /**
	  * Permite comparar dos elementos del visor.
	  * 
	  * @param viewer visor de elementos.
	  * @param e1 elemento primero a comparar.
	  * @param e2 elemento segundo a comparar.
	  * 
	  * @return indica si son iguales devolviendo 0,
	  * 		si el primer elemento es mayor devuelve mayor que 0 y
	  *         sino menor que 0.
	  * 
	  */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2){
		if (e1 instanceof DynamicRefactoringDefinition && 
			e2 instanceof DynamicRefactoringDefinition) {
			return ((DynamicRefactoringDefinition)e1).getName()
						.compareToIgnoreCase(((DynamicRefactoringDefinition)e2).getName());
		}
		return e1.toString().compareToIgnoreCase(e2.toString());
	}

	
}
