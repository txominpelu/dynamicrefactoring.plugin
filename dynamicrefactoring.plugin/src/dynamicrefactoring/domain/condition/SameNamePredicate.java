/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.condition;

import com.google.common.base.Predicate;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Comprueba si una refactorizacion dada tiene un nombre dado.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class SameNamePredicate implements
		Predicate<DynamicRefactoringDefinition> {

	/**
	 * Nombre del predicado.
	 */
	private String name;

	/**
	 * Crea el predicado.
	 * 
	 * @param name
	 *            nombre
	 */
	public SameNamePredicate(String name) {
		this.name = name;
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
	public boolean apply(DynamicRefactoringDefinition arg0) {
		return arg0.getName().equals(name);
	}

}
