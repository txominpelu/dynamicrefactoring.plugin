/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.util;

import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.domain.RefactoringMechanismType;

/**
 * Utiles para manejar principalmente nombres de clases del repositorio.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class PluginStringUtils {

	 /**
	  * Constante que almacena el caracter separador de paquetes.
	  */
	private static final String PACKAGE_SEPARATOR_CHAR = ".";

	/**
	 * Dado el nombre completo de una clase. Por ejemplo:
	 * 
	 * java.util.List
	 * 
	 * Se queda con el nombre de la clase.
	 * 
	 * List
	 * 
	 * @param fullyQualifiedName
	 *            nombre completo de la clase, es decir nombre de la clase mas
	 *            paquete al que pertenece
	 * @return nombre simple de la clase
	 */
	public static String getClassName(String fullyQualifiedName) {
		return splitGetLast(fullyQualifiedName, PACKAGE_SEPARATOR_CHAR);
	}

	/**
	 * Devuelve el nombre de paquete a partir del nombre
	 * completo de una clase.
	 * 
	 * @param fullyQualifiedName nombre completo de la clase
	 * @return nombre del paquete
	 */
	public static String getPackage(String fullyQualifiedName){
		String className = splitGetLast(fullyQualifiedName, ".");
		return fullyQualifiedName.substring(0, fullyQualifiedName.length() - className.length() -1 );
	}
	
	/**
	 * Divide la cadena en partes utilizando como token delim y devuelve la
	 * última de las particiones hechas.
	 * 
	 * @param cadena
	 *            Cadena a dividir
	 * @param delim
	 *            Token para hacer la division
	 * @return devuelve la ultima de las particiones
	 */
	public static String splitGetLast(String cadena, String delim) {
		String name = ""; //$NON-NLS-1$
		StringTokenizer st_name = new StringTokenizer(cadena, delim);
		while (st_name.hasMoreTokens()) {
			name = st_name.nextElement().toString();
		}
		return name;
	}

	/**
	 * Obtiene el nombre totalmente cualificado (incluido paquete al que
	 * pertenece) dado el nombre de una precondicion, accion o postcondicion.
	 * Ejemplo:
	 * 
	 * Recibe: ExistsClassWithName
	 * Devuelve: repository.moon.concretepredicate.ExistsClassWithName
	 * 
	 * @param mechanismName
	 *            nombre de la precondicion, accion o postcondicion
	 * @param type
	 * 			  tipo de mecanismo del que se trata
	 * @return nombre totalmente cualificado (con paquete)
	 */
	public static String getMechanismFullyQualifiedName(
			RefactoringMechanismType type,
			final String mechanismName) {
		return getMechanismPackage(type, mechanismName) + "." + mechanismName;
	}

	/**
	 * Obtiene el nombre del paquete de una pre/poscondicion o de una accion.
	 * 
	 * @param type
	 *            si es RefactoringConstant.PRECONDITION /POSTCONDITION/ACTION
	 * @param mechanismName
	 *            mechanism name
	 * @param type
	 * 			  tipo de mecanismo del que se trata
	 * @return package name (Ej. "repository.concreteaction")
	 */
	public static String getMechanismPackage(RefactoringMechanismType type, final String mechanismName) {
		if (type.isElementJavaDependent(mechanismName)) {
			return type.getMechanismJavaPackage();
		}
		return type.getMechanismIndependentPackage();

	}

	/**
	 * Obtiene un conjunto de nombres simples de pre/postcondiciones o acciones
	 * y devuelve los nombres de ellos totalmente cualificados (incluidos los
	 * paquetes).
	 * 
	 * @see #getMechanismFullyQualifiedName(String)
	 * 
	 * @param simpleNames
	 *            conjunto de nombres simples de pre/postcondiciones o acciones
	 * @return nombres totalmente cualificados (con paquetes) de los mismos
	 */
	public static Set<String> getMechanismListFullyQualifiedName(
			final RefactoringMechanismType type, Set<String> simpleNames) {
		return ImmutableSet.copyOf(Collections2.transform(simpleNames,
				new Function<String, String>() {

					@Override
					public String apply(String arg0) {
						return getMechanismFullyQualifiedName(type, arg0);
					}

				}));
	}

}
