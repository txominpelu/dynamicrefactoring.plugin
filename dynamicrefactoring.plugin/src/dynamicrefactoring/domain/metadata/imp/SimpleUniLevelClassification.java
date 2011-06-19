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

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Objects;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Clasificacion de un solo nivel. Solo tiene un nivel de categorias.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class SimpleUniLevelClassification implements Classification {

	private final Set<Category> categories;
	private final String name;
	private final String description;
	/**
	 * Si la clasificacion admite o no elementos en mas de una categoria.
	 */
	private final boolean multicategory;
	/**
	 * Si la clasificacion se puede editar o no
	 */
	private boolean editable;

	/**
	 * Crea una clasificacion de un solo nivel y en la que los elementos solo
	 * pueden pertenecer a una categoria y que se puede editar 
	 * (unicategory y editable).
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param description
	 *            descripcion de la clasificacion
	 * @param categories
	 *            lista de categorias de la clasificacion
	 */
	public SimpleUniLevelClassification(String classificationName,
			String description, Set<Category> categories) {
		this(classificationName, description, categories, false, true);
	}

	/**
	 * Crea una clasificacion de un solo nivel.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @param description
	 *            descripcion de la clasificacion
	 * @param multicategory
	 *            si la clasificacion admite o no elementos en mas de una
	 *            categoria
	 * @param categories
	 *            lista de categorias de la clasificacion
	 * @param editable si la clasificacion se puede editar o es de solo lectura
	 */
	public SimpleUniLevelClassification(String classificationName,
			String description, Set<Category> categories, boolean multicategory, boolean editable) {
		this.categories = categories;
		this.name = classificationName;
		this.description = description;
		this.multicategory = multicategory;
		this.editable = editable;
	}

	@Override
	public Set<Category> getCategories() {
		return new HashSet<Category>(categories);
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Si la clasificacion admite o no elementos en mas de una categoria.
	 * 
	 * @return verdadero si la clasificacion admite o no elementos en mas de una
	 *         categoria.
	 */
	@Override
	public boolean isMultiCategory() {
		return this.multicategory;
	}

	/**
	 * Dos clasificaciones son iguales si tienen las mismas categorias, el mismo
	 * nombre, la misma descripcion y ambas tienen el mismo criterio a la hora
	 * de permitir o no que sus elementos pertenezcan a mas de una categoria.
	 * 
	 * @param o
	 *            objeto a comparar
	 * @return verdadero si sus atributos son iguales
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Classification) {
			Classification otra = (Classification) o;
			return otra.getCategories().equals(getCategories())
					&& getName().equals(otra.getName())
					&& getDescription().equals(otra.getDescription())
					&& isMultiCategory() == otra.isMultiCategory();
		}
		return false;
	}

	/**
	 * Ensures that s1.equals(s2) implies that s1.hashCode()==s2.hashCode() for
	 * any two classification s1 and s2, as required by the general contract of
	 * Object.hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(getCategories(), this.getName(),
				this.getDescription(), this.multicategory);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getName())
				.add("description", getDescription())
				.add("multicategory", isMultiCategory())
				.add("categories", getCategories()).toString();
	}

	@Override
	public int compareTo(Classification o) {
		return name.compareTo(o.getName());
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public boolean containsCategory(Category cat) {
		for(Category c : categories){
			if(c.equals(cat))
				return true;
		}
		return false;
	}

	@Override
	public Classification renameCategory(String oldName, String newName) {
		Set<Category> newCategories = new HashSet<Category>(categories);
		Category oldCategory = new Category(getName(),oldName);
		newCategories.remove(oldCategory);
		Category newCategory = new Category(getName(),newName);
		newCategories.add(newCategory);
		return new SimpleUniLevelClassification(getName(), getDescription(), newCategories);
	}

	@Override
	public Classification addCategory(Category category) {
		Set<Category> newCategories = new HashSet<Category>(categories);
		newCategories.add(category);
		return new SimpleUniLevelClassification(getName(), getDescription(), newCategories);
	}
	
	@Override
	public Classification removeCategory(Category category) {
		Set<Category> newCategories = new HashSet<Category>(categories);
		newCategories.remove(category);
		return new SimpleUniLevelClassification(getName(), getDescription(), newCategories);
	}

	@Override
	public Classification rename(String clasifNewName) {
		Set<Category> newCategories = new HashSet<Category>(categories);
		for (Category c : getCategories()) {
			newCategories.remove(new Category(getName(), c.getName()));
			newCategories.add(new Category(clasifNewName, c.getName()));
		}

		return new SimpleUniLevelClassification(clasifNewName,
				getDescription(), newCategories);
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

}
