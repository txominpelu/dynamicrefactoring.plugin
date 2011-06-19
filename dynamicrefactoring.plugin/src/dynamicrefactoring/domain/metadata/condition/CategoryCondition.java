/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.metadata.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Devuelve si un elemento pertenece a una categoría.
 * 
 * @param <K>
 *            tipo de la condición
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class CategoryCondition<K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="category";
	
	/**
	 * Categoría que se comprobará.
	 */
	private Category category;

	/**
	 * Constructor por categoría.
	 * 
	 * @param category categoría
	 */
	public CategoryCondition(Category category) {
		this.category = category;
	}

	
	/**
	 * Constructor por los elementos que definen una categoría.
	 * 
	 * @param parent nombre de la clasificación padre de la categoría
	 * @param categoryName nombre de la categoría
	 */
	public CategoryCondition(String parent, String categoryName) {
		this.category = new Category(parent.trim(), categoryName.trim());
	}

	/**
	 * Obtiene la categoría que define la condición.
	 * 
	 * @return categoría que define la condición
	 */
	public Category getCategory() {
		return category;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.belongsTo(category);
	}
	
	/**
	 * Son iguales si ambas contienen la misma categoria.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma categoria
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof CategoryCondition){
			CategoryCondition<?> otra = (CategoryCondition<?>) o;
			return otra.getCategory().equals(category);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return category.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + category.toString();
	}

	

}
