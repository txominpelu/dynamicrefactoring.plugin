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

import dynamicrefactoring.domain.metadata.interfaces.Element;

/**
 * Condición por si el elemento contiene una
 * postcondición.
 * 
 * @param <K> tipo del elemento
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class PostconditionCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="postcondition";
	
	private String postcondition;
	
	/**
	 * Constructor.
	 * 
	 * @param postcondition nombre de la postcondición
	 */
	public PostconditionCondition(String postcondition){
		this.postcondition=postcondition;
	}
	
	/**
	 * Obtiene la postcondición que define la condición.
	 * @return postcondición que define la condición
	 */
	public String getPostcondition(){
		return postcondition;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsPostcondition(postcondition);
	}
	
	/**
	 * Son iguales si ambas contienen la misma postcondición.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma postcondición
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof PostconditionCondition){
			PostconditionCondition<?> otra = (PostconditionCondition<?>) o;
			return otra.getPostcondition().equals(postcondition);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return postcondition.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + postcondition.toString();
	}
}
