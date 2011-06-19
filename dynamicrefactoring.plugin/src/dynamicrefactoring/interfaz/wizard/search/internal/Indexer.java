/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard.search.internal;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.Directory;

import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade.SearchableType;

/**
 * Generador de indices.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 * 
 */
interface Indexer {


	/**
	 * Genera un indice en el directorio pasado.
	 * 
	 * @param elementType
	 *            tipo de searchable cuyos elementos se van a indizar
	 * @param directory
	 *            directorio sobre el que se generara el indice
	 * 
	 * @return numero de elementos indizados
	 * @throws CorruptIndexException
	 * 
	 * @throws IOException
	 */
	int index(SearchableType elementType, Directory directory)
			throws IOException;

}
