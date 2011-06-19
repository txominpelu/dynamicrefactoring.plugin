/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.metadata.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Clasificacion de un conjunto de elementos por categorias.
 * 
 * Cada categoria de la clasificacion contendra un conjunto de hijos.
 * 
 * @param <K>
 *            Tipo de los elementos que forman la categoria
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public interface ClassifiedElements<K> {

	/**
	 * Obtiene los hijos que corresponden a una categoria en la clasificacion.
	 * 
	 * @param category
	 *            categoria de la que se quieren obtener los hijos
	 * @return hijos de la categoria
	 */
	Set<K> getCategoryChildren(Category category);

	/**
	 * Obtiene la clasificacion que defina la estructura de los elementos
	 * clasificados.
	 * 
	 * @return Clasificacion por la que estos elementos estan organizados
	 */
	Classification getClassification();

	/**
	 * Obtiene los elementos clasificados en un mapa en el que las categorías
	 * son las claves y los valores son el conjunto de elementos que pertenecen
	 * a la categoría.
	 * 
	 * @return elementos clasificados en un mapa en el que las categorías son
	 *         las claves y los valores son el conjunto de elementos que
	 *         pertenecen a la categoría
	 */
	Map<Category, Set<K>> getClassifiedElements();
}
