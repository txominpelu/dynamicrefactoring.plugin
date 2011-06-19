/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard.search.javadoc;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filtro para que el proyecto que se crea para obtener 
 * la documentación de las clases no se muestre al usuario.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class JavadocReaderViewerFilter extends ViewerFilter {	
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof IJavaProject){
			return !((IJavaProject) element).getElementName().equals(EclipseBasedJavadocReader.JAVADOC_READER_PROJECT_NAME);
		}
		return true;
	}

}
