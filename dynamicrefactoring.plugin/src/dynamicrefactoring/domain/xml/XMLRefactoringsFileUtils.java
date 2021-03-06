/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.xml;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Throwables;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Metodos utiles para modificar el catalogo de refactorizaciones en XML. 
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class XMLRefactoringsFileUtils {
	
	/**
	 * Elimina el directorio de la refactorizacion.
	 * 
	 * @param refactoring refactorizacion de la que se borrara su directorio
	 */
	public static void deleteRefactoringDir(DynamicRefactoringDefinition refactoring) {
		if (refactoring.getRefactoringDirectoryFile().exists()) {
			try {
				FileUtils
						.deleteDirectory(refactoring.getRefactoringDirectoryFile());
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}
	}
	
}
