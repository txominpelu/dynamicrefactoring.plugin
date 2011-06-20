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

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.lucene.queryParser.ParseException;

import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.RefactoringMechanismType;
import dynamicrefactoring.util.MOONTypeLister;
import dynamicrefactoring.util.PluginStringUtils;

/**
 * Fachada que proporciona los metodos de interes para la indexacion y busqueda
 * de elementos.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public enum SearchingFacade {

	/**
	 * Instancia de la fachada de búsqueda.
	 */
	INSTANCE;

	/**
	 * Tipo de elemento sobre el que se pueden hacer busquedas en el plugin.
	 * 
	 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
	 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
	 * 
	 */
	public enum SearchableType {
		/**
		 * Predicado (precondiciones y postcondiciones).
		 */
		PREDICATE {
			@Override
			Set<String> getClassesToIndex() {
				return PluginStringUtils.getMechanismListFullyQualifiedName(
						RefactoringMechanismType.PRECONDITION, RefactoringMechanismType
								.getPredicatesAllList().keySet());
			}
		
		},

		/**
		 * Tipo de elemento de búsqueda acción.
		 */
		ACTION {
			@Override
			Set<String> getClassesToIndex() {
				return PluginStringUtils.getMechanismListFullyQualifiedName(
						RefactoringMechanismType.ACTION,
						RefactoringMechanismType.ACTION.getElementAllList()
								.keySet());
			}

		},

		/**
		 * Tipo de elemento de búsqueda entrada.
		 */
		INPUT {
			@Override
			Set<String> getClassesToIndex() {
				return ImmutableSet.copyOf(MOONTypeLister.getInstance()
						.getTypeNameList());
			}
			
		};

		/**
		 * Obtiene el directorio en el que se guarda el indice del tipo de
		 * elemento.
		 * 
		 * @return ruta del directorio en el que se almacena el indice
		 */
		String getIndexDir() {
			return RefactoringPlugin.getCommonPluginFilesDir()
					+ File.separator + "index" + File.separator
					+ toString().toLowerCase();
		}

		/**
		 * Devuelve una lista con los nombres completamente cualificados
		 * (moon.core.Model) de todas las clases que se deben indexar.
		 * 
		 * @return conjunto de nombres de las clases a indexar
		 */
		abstract Set<String> getClassesToIndex();
	}

	/**
	 * Indexador de clases y sus descripciones en Javadoc.
	 */
	private SearchableTypeIndexer indexer;
	
	/**
	 * Elemento encargado de realizar las busquedas.
	 */
	private SimpleElementSearcher searcher;

	/**
	 * Constructor de la clase.
	 */
	private SearchingFacade() {
		indexer = SearchableTypeIndexer.INSTANCE;
		searcher = SimpleElementSearcher.INSTANCE;
	}

	/**
	 * Genera los indices para todos los tipos de elementos que pueden ser
	 * buscados es decir para todos los elementos de {@link SearchableType}.
	 * 
	 * @throws IOException
	 *             excepción de entrada o salida
	 */
	public void generateAllIndexes() throws IOException {
		for (SearchableType type : SearchableType.values()) {
			indexer.index(type);
		}
	}

	/**
	 * Realiza una busqueda dados unos terminos de consulta, sobre un tipo
	 * especifico de elemento que se pueda buscar.
	 * 
	 * @param typeToSearch
	 *            tipo de elemento sobre el que se buscara
	 * @param query
	 *            terminos de la busqueda a realizar
	 * @return resultados de la busqueda
	 * @throws ParseException
	 *             si el formato de los terminos de la busqueda no es valido
	 */
	public Set<QueryResult> search(SearchableType typeToSearch, String query)
			throws ParseException {
		return searcher.search(typeToSearch, query);
	}

}
