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

import com.google.common.base.Objects;


/**
 * Categoría de una clasificación.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class Category implements Comparable<Category> {
	
	/**
	 * Categoria por defecto de los elementos que no pertenecen a ninguna
	 * categoría en una clasificación.
	 */
	public static final Category NONE_CATEGORY = new Category("", "None");

	/**
	 * Nombre de la categoría.
	 */
	private final String name;

	/**
	 * Nombre de la clasificación padre de la categoría.
	 */
	private final String parent;

	/**
	 * Crea una categoria a partir de su nombre y su conjunto de hijos.
	 * 
	 * @param parent
	 *            padre de la categoria
	 * @param name nombre de la categoria
	 */
	public Category(String parent, String name) {
		this.name = name;
		this.parent = parent;
	}

	/**
	 * Devuelve el nombre de la categoria.
	 * 
	 * @return nombre de la categoria
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return parent+"@"+name;
	}

	/**
	 * Dos categorias son iguales si tienen el mismo nombre
	 * y el mismo padre.
	 */
	@Override 
	public boolean equals(Object o) {
		if (o instanceof Category) {
			Category otra = (Category) o;
			return otra.getName().equalsIgnoreCase(this.getName()) && 
				   otra.getParent().equalsIgnoreCase(this.getParent());
		}
		return false;
	}

	/**
	 * Obtiene la clasificacion padre
	 * a la que esta categoria pertenece.
	 * @return nombre del padre de la categoria
	 */
	public String getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name.toLowerCase(), parent.toLowerCase());
	}

	/**
	 * Compara dos categorías y las ordena en base a su nombre.
	 * 
	 * @return mayor que cero si la categoría es mayor que la pasada
	 */
	@Override
	public int compareTo(Category o) {
		return toString().toLowerCase().compareTo(o.toString().toLowerCase());
	}

}
