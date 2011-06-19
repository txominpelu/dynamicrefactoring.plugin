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
 * Crea una condicion para un elemento que comprueba si este elemento contiene
 * un texto.
 * 
 * @author imediava
 * 
 * @param <K>
 *            Elemento
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class TextCondition<K extends Element> implements Predicate<K> {
	
	/**
	 * Nombre 
	 */
	public static final String NAME="text";
	
	private String text;

	/**
	 * Crea una instancia de la condicion.
	 * 
	 * @param text texto que se buscara en la información relativa al elemento
	 */
	public TextCondition(String text){
		this.text = text.toLowerCase().trim();
	}

	private String getText() {
		return text;
	}
	
	/**
	 * Devuelve si la información relativa al elemento contiene el texto
	 * con el que se definió la condición.
	 * 
	 * @param arg0 elemento en el que se realiza la búsqueda
	 */
	@Override
	public boolean apply(Element arg0) {
		return arg0.containsText(text);
	}

	/**
	 * Son iguales si ambas contienen el mismo texto.
	 * 
	 * @param otra
	 *            a comparar
	 * @return verdadero si filtran por el mismo texto
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof TextCondition){
			TextCondition<?> otra = (TextCondition<?>) o;
			return otra.getText().equals(text);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return text.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + text;
	}

}
