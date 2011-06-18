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
 * Condicion que indica si un elemento contiene una acción.
 * 
 * @author imediava
 * 
 * @param <K> tipo de elemento de la condicion 
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class ActionCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre.
	 */
	public static final String NAME="action";
	
	private String action;
	
	/**
	 * Constructor.
	 * 
	 * @param action nombre de la acción
	 */
	public ActionCondition(String action){
		this.action=action;
	}
	
	/**
	 * Obtiene la acción que define la condición.
	 * 
	 * @return acción que define la condición
	 */
	public String getAction(){
		return action;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsAction(action);
	}
	
	/**
	 * Son iguales si ambas contienen la misma acción.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por la misma acción
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof ActionCondition){
			ActionCondition<?> otra = (ActionCondition<?>) o;
			return otra.getAction().equals(action);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return action.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + action.toString();
	}

}
