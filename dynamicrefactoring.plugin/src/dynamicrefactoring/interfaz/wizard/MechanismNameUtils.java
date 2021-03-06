/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Utiles para trabajar con los nombres de mecanismos de una refactorización.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class MechanismNameUtils {
	
	/**
	 * Evita que la clase pueda ser instanciada.
	 */
	private MechanismNameUtils(){}
	
	/**
	 * Dada una lista de nombres con formato:
	 * 
	 * NotExistClassWithName(1)
	 * 
	 * y devuelve el nombre sin el numero:
	 * 
	 * NotExistClassWithName.
	 * 
	 * @param mechanismList lista de nombres de mecanismos a transformar
	 * @return lista de nombres de mecanismos transformada
	 */
	public static List<String> getMechanismNameList(List<String> mechanismList){
		List<String> list = new ArrayList<String>();
		for(String name: mechanismList){
			list.add(getMechanismName(name));
		}
		return list;
	}
	
	/**
	 * Recibe una precondicion, accion o postcondicion con el numero:
	 * 
	 * NotExistClassWithName (1)
	 * 
	 * y devuelve el nombre sin el numero:
	 * 
	 * NotExistClassWithName.
	 * 
	 * @param preconditionWithNumber
	 *            precondicion con formato nombre(numero)
	 * @return devuelve nombre
	 */
	public static String getMechanismName(final String preconditionWithNumber) {
		Preconditions.checkArgument(preconditionWithNumber.matches("\\w* \\([1-9]\\)"));
		return preconditionWithNumber.substring(0,
				preconditionWithNumber.length() - 4);
	}

}
