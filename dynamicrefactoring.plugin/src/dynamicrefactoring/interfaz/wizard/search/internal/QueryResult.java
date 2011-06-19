/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard.search.internal;

/**
 * Obtiene uno de los resultados de una busqueda de elementos en el indice.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class QueryResult {

	private String className;
	private String classDescription;

	protected QueryResult(String className, String classDescription) {
		this.className = className;
		this.classDescription=classDescription;
	}

	/**
	 * @return el nombre de la clase encontrada
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return el descripción de la clase encontrada
	 */
	public String getClassDescription() {
		return classDescription;
	}
	
	/**
	 * Una QueryResult queda definida por su nombre. No puede haber dos
	 * resultados para la misma busqueda con el mismo nombre de clase por tanto
	 * el nombre de clase es su clave.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof QueryResult) {
			return ((QueryResult) o).getClassName().equals(getClassName());
		}
		return false;
	}
}

