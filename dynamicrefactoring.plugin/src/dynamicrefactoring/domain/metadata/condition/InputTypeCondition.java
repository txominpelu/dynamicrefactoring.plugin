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
 * Condición por si un elemento contiene como entrada un tipo.
 * 
 * @param <K> tipo del elemento sobre el que se aplica la condición
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class InputTypeCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición
	 */
	public static final String NAME="inputType";
	
	/**
	 * Tipo de la entrada.
	 */
	private String inputType;
	
	/**
	 * Constructor.
	 * 
	 * @param inputType tipo de entrada
	 */
	public InputTypeCondition(String inputType){
		this.inputType=inputType;
	}
	
	/**
	 * Obtiene el tipo de entrada que define la condición.
	 * 
	 * @return tipo de entrada que define la condición
	 */
	public String getInputType(){
		return inputType;
	}
	
	/**
	 * Comprueba si se aplica la condición.
	 * 
	 * @param arg0
	 *            objeto a comprobar
	 * 
	 * @return devuelve si la condición se cumple para el objeto pasado
	 */
	@Override
	public boolean apply(K arg0) {
		return arg0.containsInputType(inputType);
	}

	/**
	 * Son iguales si ambas contienen el mismo tipo de entrada.
	 * 
	 * @param o
	 *            a comparar
	 * @return verdadero si filtran por el mismo tipo de entrada
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof InputTypeCondition){
			InputTypeCondition<?> otra = (InputTypeCondition<?>) o;
			return otra.getInputType().equals(inputType);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return inputType.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + inputType.toString();
	}
}
