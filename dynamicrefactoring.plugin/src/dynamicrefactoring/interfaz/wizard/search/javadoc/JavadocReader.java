/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard.search.javadoc;

/**
 * Permite acceder al javadoc de clases.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public interface JavadocReader {

	/**
	 * Obtiene el html con la descripcion de la clase en formato javadoc.
	 * 
	 * Debe existir informacion sobre la clase en el lector. Esto se comprueba
	 * con {@link #hasType(String)}. Si el lector no tiene informacion para el
	 * tipo pasado este metodo lanza {@link IllegalArgumentException}
	 * 
	 * Si el lector tiene informacion sobre la clase pero no su javadoc este
	 * metodo devuelve la cadena vacia.
	 * 
	 * @param typeFullyQualifiedName
	 *            nombre completamente cualificado de la clase
	 * @return descripcion en el formato html generado por el javadoc de la
	 *         clase
	 */
	String getTypeJavaDocAsHtml(String typeFullyQualifiedName);

	/**
	 * Obtiene el texto plano con la descripcion de la clase en formato javadoc
	 * resultado de eliminar las etiquetas de {@link #getTypeJavaDocAsHtml(String)}.
	 * 
	 * Debe existir informacion sobre la clase en el lector. Esto se comprueba
	 * con {@link #hasType(String)}. Si el lector no tiene informacion para el
	 * tipo pasado este metodo lanza {@link IllegalArgumentException}
	 * 
	 * Si el lector tiene informacion sobre la clase pero no su javadoc este
	 * metodo devuelve la cadena vacia.
	 * 
	 * @param typeFullyQualifiedName
	 *            nombre completamente cualificado de la clase
	 * @return descripcion en el formato html generado por el javadoc de la
	 *         clase
	 */
	String getTypeJavaDocAsPlainText(String typeFullyQualifiedName);
	
	/**
	 * Devuelve si el lector de javadoc tiene informacion sobre la clase que se
	 * le ha pasado.
	 * 
	 * @param typeFullyQualifiedName
	 *            nombre completamente cualificado de la clase
	 * @return si existe informacion sobre dicha clase en el lector
	 */
	boolean hasType(String typeFullyQualifiedName);

	

}
