package dynamicrefactoring.interfaz.wizard.search.internal;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.BeforeClass;
import org.junit.Test;

import dynamicrefactoring.domain.RefactoringMechanism;
import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade.SearchableType;
import dynamicrefactoring.util.PluginStringUtils;
/**
 * Test del generador de indices de RefactoringMechanism
 * (precondiciones/acciones/postcondiciones).
 * 
 * @author imediava
 *
 */
public final class SearchableTypeIndexerTest {

	private static String actionElementName;
	private static Directory directory;

	/**
	 * Generamos los indices a comprobar en el test.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		actionElementName = PluginStringUtils
				.getMechanismFullyQualifiedName(RefactoringMechanism.ACTION,
						RefactoringMechanism.ACTION.getElementAllList()
								.keySet().iterator().next());
		final SearchableTypeIndexer indexer = SearchableTypeIndexer.INSTANCE;
		directory = new RAMDirectory();
		indexer.index(SearchableType.ACTION, directory);
	}

	/**
	 * Comprueba que un termino que deberia
	 * existir en el indice existe y no esta repetido.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testElementExists() throws Exception {
		TopDocs result = searchTerm(SearchableTypeIndexer.CLASS_NAME_FIELD, actionElementName);
		assertEquals(result.totalHits, 1);
	}
	
	/**
	 * Comprueba que el numero de elementos 
	 * indizados es el esperado.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIndexedElementsCount() throws Exception{
		IndexReader reader = IndexReader.open(directory);
		assertEquals(RefactoringMechanism.ACTION.getElementAllList().size(), reader.numDocs());
	}

	/**
	 * Busca un texto en un campo.
	 * 
	 * @param fieldName nombre del campo a buscar
	 * @param textToSearch texto a buscar
	 * @return resultados de la busqueda
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private TopDocs searchTerm(String fieldName, String textToSearch) throws CorruptIndexException, IOException {
		final IndexSearcher searcher = new IndexSearcher(directory);
		final Term t = new Term(fieldName, textToSearch);
		final Query query = new TermQuery(t);
		final TopDocs result = searcher.search(query, 1);
		searcher.close();
		return result;
	}


}
