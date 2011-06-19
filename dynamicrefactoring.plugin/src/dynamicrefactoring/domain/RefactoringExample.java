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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Ejemplo de refactorizacion con el codigo
 * fuente de antes y después de que la refactorización
 * se ejecute.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class RefactoringExample {

	private String before;
	private String after;

	/**
	 * Crea unos ficheros de ejemplo de una refactorizacion.
	 * 
	 * @param before ruta absoluta al fichero de ejemplo antes
	 * @param after ruta absoluta al fichero de ejemplo despues
	 */
	public RefactoringExample(String before, String after) {
		this.before = before;
		this.after = after;
		Preconditions.checkNotNull(before);
		Preconditions.checkNotNull(after);
	}
	

	/**
	 * Cadena con el código que tenía el ejemplo
	 * antes de ejecutar la refactorización.
	 * 
	 * @return código antes de ejecutar
	 */
	public String getBefore() {
		return before;
	}

	/**
	 * Cadena con el código que tiene el ejemplo
	 * después de ejecutar la refactorización.
	 * 
	 * @return código después de ejecutar
	 */
	public String getAfter() {
		return after;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RefactoringExample) {
			return getBefore().equals(((RefactoringExample) o).getBefore())
					&& getAfter().equals(((RefactoringExample) o).getAfter());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getBefore(), getAfter());
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("before", getBefore())
				.add("after", getAfter()).toString();
	}


}
