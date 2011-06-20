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
 * Condición por si un elemento contiene una palabra clave.
 * 
 * 
 * @param <K>
 *            tipo del elemento
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class KeyWordCondition<K extends Element> implements Predicate<K> {

	/**
	 * Nombre de la condición.
	 */
	public static final String NAME="key";
	
	/**
	 * Palabra clave.
	 */
	private String keyWord;

	/**
	 * Crea una instancia de la condicion.
	 * 
	 * @param keyWord palabra clave que se buscará en el elemento
	 */
	public KeyWordCondition(String keyWord){
		this.keyWord = keyWord.toLowerCase().trim();
	}

	/**
	 * Obtiene la palabra clave que define la condición.
	 * 
	 * @return palabra clave que define la condición
	 */
	private String getKeyWord() {
		return keyWord;
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
		return arg0.belongsTo(keyWord);
	}

	/**
	 * Son iguales si ambas contienen el mismo texto.
	 * 
	 * @param o
	 *            a comparar
	 * @return verdadero si filtran por el mismo texto
	 */
	@Override
	public boolean equals(Object o){
		if (o instanceof KeyWordCondition){
			KeyWordCondition<?> otra = (KeyWordCondition<?>) o;
			return otra.getKeyWord().equals(keyWord);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return keyWord.hashCode();
	}

	@Override
	public String toString() {
		return NAME + ":" + keyWord;
	}

}
