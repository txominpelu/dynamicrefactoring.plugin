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

import java.util.List;

import com.google.common.base.Predicate;

/**
 * 
 * Catálogo de elementos clasificados por categorías
 * y filtrables por criterios.
 * 
 * @param <K> tipo de elemento a clasificar
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public interface ClassifiedFilterableCatalog<K> {

	/**
	 * Agrega un condicion al filtro actual que se esta aplicando al catalogo.
	 * 
	 * @param filter
	 *            filtro a agregar
	 */
	void addConditionToFilter(Predicate<K> filter);


	/**
	 * Elimina una condicion ya existente en el filtro actual aplicado al
	 * catalogo.
	 * 
	 * @param conditionToRemove
	 *            condicion a eliminar
	 */
	void removeConditionFromFilter(Predicate<K> conditionToRemove);

	/**
	 * Obtiene con todos las condiciones que componen el filtro.
	 * 
	 * @return lista con todas las condiciones del filtro
	 */
	List<Predicate<K>> getAllFilterConditions();

	/**
	 * Determina si se encuentra vacio el filtro de condiciones.
	 * 
	 * @return devuelve verdadero si el filtro se encuentra vacio de condiciones, 
	 * falso en caso contrario.
	 */
	boolean isEmptyFilter();
	
	/**
	 * Determina si existen elementos filtrados.
	 * 
	 * @return devuelve verdadero si exiten elementos filtrados, falso en caso contrario.
	 */
	boolean hasFilteredElements();

	/**
	 * Devuelve el conjunto de los elementos que contiene el catalogo
	 * clasificado por categorias, sin los elementos filtrados.
	 * 
	 * @return obtiene los elementos clasificados
	 */
	ClassifiedElements<K> getClassificationOfElements();

	/**
	 * Devuelve el conjunto de los elementos filtrados que contiene el catalogo
	 * clasificado por categorias.
	 * 
	 * @return obtiene los elementos filtrados clasificados
	 */
	ClassifiedElements<K> getClassificationOfFilteredElements();
	
	/**
	 * Construye una catálogo con los mismos filtros y elementos que el actual
	 * pero con una clasificacion distinta.
	 * 
	 * @param classification
	 *            clasificacion a aplicar al nuevo catalogo
	 * @return nuevo catalogo en el que solo cambia que se le aplica una nueva
	 *         clasificacion
	 */
	ClassifiedFilterableCatalog<K> newInstance(Classification classification);

	/**
	 * Devuelve la clasificación que divide en categorias los elementos de este
	 * catalogo.
	 * 
	 * @return clasificacion que divide en categorias el catalogo
	 */
	Classification getClassification();


	/**
	 * Elimina todas las condiciones del filtro.
	 * 
	 * @return nuevo catálogo con los filtros eliminados
	 */
	ClassifiedFilterableCatalog<K> removeAllFilterConditions();


}
