/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import dynamicrefactoring.domain.metadata.classifications.xml.XmlClassificationsReader;

/**
 * Permite obtener instancias de un lector
 * de clasificaciones.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 final class ClassificationsReaderFactory {
	
	/**
	 * Constructor privado para evitar que se creen
	 * instancias de esta clase.
	 */
	private ClassificationsReaderFactory(){}
	
	/**
	 * Devuelve un lector de clasificaciones.
	 * 
	 * @return lector de clasificaciones
	 */
	public static XmlClassificationsReader getReader(){
		return JAXBClassificationsReader.getInstance();
	}

}
