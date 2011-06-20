/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Instancia de un mecanismo de una refactorizacion.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class RefactoringMechanismInstance {

	/**
	 * Nombre de la clase del mecanismo de refactorizacion.
	 */
	private String className;
	/**
	 * Tipo de mecanismo.
	 */
	private RefactoringMechanismType type;

	/**
	 * Parametros de entrada del mecanismo.
	 */
	private List<String> inputParameters;

	/**
	 * Crea un mecanismo de una refactorizacion del tipo pasado.
	 * 
	 * @param className
	 *            nombre de la clase del mecanismo
	 * @param inputParametersNames
	 *            lista de parámetros de entrada
	 * @param type
	 *            tipo del mecanismo
	 * @see RefactoringMechanismType
	 */
	public RefactoringMechanismInstance(String className,
			List<String> inputParametersNames, RefactoringMechanismType type) {
		Preconditions.checkNotNull(inputParametersNames);
		Preconditions.checkNotNull(className);
		this.className = className;
		this.type = type;
		this.inputParameters = inputParametersNames;
	}

	/**
	 * Obtiene el nombre de la clase del mecanismo.
	 * 
	 * @return clase que representa el mecanismo
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * Obtiene el tipo del mecanismo. Si es una precondicion, accion o
	 * poscondicion.
	 * 
	 * @return tipo del mecanismo
	 */
	public RefactoringMechanismType getType() {
		return this.type;
	}

	/**
	 * Obtiene la lista de parametros que contiene el mecanismo.
	 * 
	 * @return tipo del mecanismo
	 */
	public List<String> getInputParameters() {
		return new ArrayList<String>(this.inputParameters);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("className", getClassName())
				.add("type", type).add("parameters", getInputParameters())
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RefactoringMechanismInstance) {
			final RefactoringMechanismInstance toCompare = (RefactoringMechanismInstance) o;
			return Objects.equal(getClassName(), toCompare.getClassName())
					&& Objects.equal(getType(), toCompare.getType())
					&& Objects.equal(getInputParameters(),
							toCompare.getInputParameters());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getClassName(), type);
	}

	/**
	 * Devuelve los nombres de clase de los mecanismos pasados.
	 * 
	 * @param mechanisms
	 *            mecanismos a tranformar
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 */
	public static List<String> getMechanismListClassNames(
			List<RefactoringMechanismInstance> mechanisms) {
		return Lists.transform(ImmutableList.copyOf(mechanisms),
				new Function<RefactoringMechanismInstance, String>() {

					@Override
					public String apply(RefactoringMechanismInstance arg0) {
						return arg0.getClassName();
					}
				});
	}

}
