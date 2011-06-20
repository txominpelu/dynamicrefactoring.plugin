/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.metadata.imp;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;

/**
 * Conjunto de elementos clasificados por categorías.
 *
 * @param <K> tipo del elemento
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class SimpleClassifiedElements<K> implements ClassifiedElements<K> {

	/**
	 * Conjuntos de elementos clasificados agrupados por categorías.
	 */
	private final Map<Category, Set<K>> classifiedElements;
	/**
	 * Clasificación en la que se basan los elementos clasificados.
	 */
	private final Classification classification;

	/**
	 * Crea una categoria a partir de su nombre y su conjunto de categorias
	 * junto con los hijos que estas tienen.
	 * 
	 * @param classification
	 *            clasificación
	 * @param classifiedElements
	 *            elementos clasificados
	 * 
	 */
	public SimpleClassifiedElements(Classification classification,
			Map<Category, Set<K>> classifiedElements) {
		this.classification = classification;
		this.classifiedElements = ImmutableMap.copyOf(classifiedElements);
	}

	/**
	 * Conjunto de hijos de la categoria.
	 */
	@Override
	public Classification getClassification() {
		return this.classification;
	}

	/**
	 * Conjunto de todos los elementos.
	 * 
	 * @return conjunto de todos los elementos clasificados
	 */
	@Override
	public Map<Category, Set<K>> getClassifiedElements(){
		return classifiedElements;
	}
	
	/**
	 * Devuelve todos los hijos de una categoría.
	 * 
	 * @param category
	 *            categoría
	 * 
	 * @return hijos de una categoría
	 */
	@Override
	public Set<K> getCategoryChildren(Category category) {
		return classifiedElements.get(category);
	}

	/**
	 * Dos elementos clasificados son iguales si ambos son instancias de esta
	 * clase y ambos tienen las mismas categorias y dichas categorias tienen los
	 * mismos elementos.
	 * 
	 * @param o
	 *            a comparar con el actual
	 * @return verdadero si las dos son clasificaciones y tienen las mismas
	 *         categorias y las categorias tienen los mismos hijos
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SimpleClassifiedElements) {
			SimpleClassifiedElements<?> otra = (SimpleClassifiedElements<?>) o;
			return getClassification().equals(otra.getClassification())
					&& classifiedElements.equals(otra.classifiedElements);

		}
		return false;
	}

	/**
	 * De acuerdo a la definicion de equals de este objeto y de acuerdo a los
	 * convenciones de Object.
	 * 
	 * @return hashcode del objeto
	 */
	@Override
	public int hashCode(){
		return Objects.hashCode(getClassification(), classifiedElements.hashCode());
	}

	@Override
	public String toString() {
		return classifiedElements.toString();
	}

}
