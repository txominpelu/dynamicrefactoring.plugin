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
 * Condición que indica si un elemento tiene un tipo
 * como el tipo de su entrada raíz.
 * 
 * @param <K> tipo del elemento
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class RootInputTypeCondition <K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="rootInputType";
	
	/**
	 * Tipo de entrada principal que se comprobará.
	 */
	private String rootInputType;
	
	/**
	 * Constructor.
	 * 
	 * @param rootInputType tipo
	 */
	public RootInputTypeCondition(String rootInputType){
		this.rootInputType=rootInputType;
	}
	
	/**
	 * Devuelve el tipo que define la condición.
	 * 
	 * @return tipo que define la condición
	 */
	public String getRootInputType(){
		return rootInputType;
	}
	
	@Override
	public boolean apply(K arg0) {
		return arg0.containsRootInputType(rootInputType);
	}
	
	/**
	 * Son iguales si ambas contienen el mismo tipo de entrada raiz.
	 * 
	 * @param otra a comparar
	 * @return verdadero si filtran por el mismo tipo de entrada raiz
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof RootInputTypeCondition){
			RootInputTypeCondition<?> otra = (RootInputTypeCondition<?>) o;
			return otra.getRootInputType().equals(rootInputType);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return rootInputType.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + rootInputType.toString();
	}
	
}
