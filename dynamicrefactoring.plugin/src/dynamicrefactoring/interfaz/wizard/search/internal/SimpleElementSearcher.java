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
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.common.base.Throwables;

import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade.SearchableType;

/**
 * Elemento encargado de realizar las busquedas.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 enum SimpleElementSearcher {

	 /**
	  * Intancia.
	  */
	INSTANCE;

	/**
	 * Realiza una consulta sobre el indice de un tipo de elemento.
	 * 
	 * @param element tipo de elemento sobre el que se buscara
	 * @param userQuery consulta a realizar
	 * @return resultados de la busqueda
	 * @throws ParseException si la consulta no es valida
	 */
	protected Set<QueryResult> search(SearchableType element, String userQuery)
			throws ParseException {

		try {
			Directory dir = FSDirectory.open(new File(element.getIndexDir()));
			return search(element, userQuery, dir);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}

	}

	/**
	 * Realiza una consulta sobre el indice de un tipo de elemento.
	 * 
	 * @param element tipo de elemento a buscar
	 * @param userQuery consulta a realizar
	 * @param dir directorio del indice
	 * @return resultados de la busqueda
	 * @throws ParseException si la consulta no es valida
	 */
	protected Set<QueryResult> search(SearchableType element, String userQuery,
			Directory dir) throws ParseException {
		try {
			IndexSearcher is = new IndexSearcher(dir);
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30,
					new String[] {
							SearchableTypeIndexer.CLASS_DESCRIPTION_FIELD,
							SearchableTypeIndexer.CLASS_NAME_FIELD,
							SearchableTypeIndexer.PACKAGE_FIELD },
					SearchableTypeIndexer.getTermsAnalyzer());

			TopDocs hits = is.search(parser.parse(userQuery), 30);
			Set<QueryResult> results = new HashSet<QueryResult>(hits.totalHits);
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = is.doc(scoreDoc.doc);
				results.add(new QueryResult(
						doc.get(SearchableTypeIndexer.FULLY_QUALIFIED_CLASS_NAME_FIELD),
						doc.get(SearchableTypeIndexer.CLASS_DESCRIPTION_FIELD)));
			}
			is.close();
			return results;
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}

	}

}
