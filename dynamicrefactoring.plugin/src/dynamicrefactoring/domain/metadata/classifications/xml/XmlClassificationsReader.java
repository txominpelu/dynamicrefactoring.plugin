/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.metadata.classifications.xml;

import java.util.Set;

import javax.xml.bind.ValidationException;

import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Lector de clasificaciones guardadas en un xml.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public interface XmlClassificationsReader {

	/**
	 * Lee el conjunto de refactorizaciones guardadas en un fichero xml.
	 * 
	 * Lanza NullPointerException si la URL es nula.
	 * 
	 * @param path_file
	 *            URL que describe la ruta al fichero
	 * @param editable si las clasificaciones que se leeran del fichero se crearan como editables o no
	 * @return conjunto de clasificaciones contenidas en el fichero.
	 * @throws ValidationException si el xml no cumple las especificaciones del esquema
	 */
	Set<Classification> readClassifications(String path_file, boolean editable)
			throws ValidationException;

}
