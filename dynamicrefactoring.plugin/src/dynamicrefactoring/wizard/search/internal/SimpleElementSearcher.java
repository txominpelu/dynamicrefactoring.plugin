package dynamicrefactoring.wizard.search.internal;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.common.base.Throwables;

import dynamicrefactoring.domain.RefactoringMechanism;

public enum SimpleElementSearcher {

	INSTANCE;

	public enum IndexedElement {
		PRECONDITION {
			@Override
			String getIndexDir() {
				return RefactoringMechanism.PRECONDITION.getIndexDir();
			}
		},
		ACTION {
			@Override
			String getIndexDir() {
				return RefactoringMechanism.ACTION.getIndexDir();
			}
		},
		POSTCONDITION {
			@Override
			String getIndexDir() {
				return RefactoringMechanism.POSTCONDITION.getIndexDir();
			}
		},
		INPUT

		{
			@Override
			String getIndexDir() {
				return InputsIndexer.getInstance().getIndexDir();
			}
		};

		abstract String getIndexDir();
	}

	public Set<QueryResult> search(IndexedElement element, String userQuery)
			throws ParseException {

		try {
			Directory dir = FSDirectory.open(new File(element.getIndexDir()));
			return search(userQuery, dir);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}

	}

	protected Set<QueryResult> search(String userQuery, Directory dir)
			throws ParseException {
		try {
			IndexSearcher is = new IndexSearcher(dir);
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30,
					new String[] {
							AbstractClassIndexer.CLASS_DESCRIPTION_FIELD,
							AbstractClassIndexer.CLASS_NAME_FIELD },
					new SnowballAnalyzer(Version.LUCENE_30, "Spanish"));
			Query query = parser.parse(userQuery);
			long start = System.currentTimeMillis();
			TopDocs hits = is.search(query, 10);
			long end = System.currentTimeMillis();
			System.err.println("Found " + hits.totalHits + " document(s) (in "
					+ (end - start) + " milliseconds) that matched query '"
					+ userQuery + "':");
			Set<QueryResult> results = new HashSet<QueryResult>(hits.totalHits);
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = is.doc(scoreDoc.doc);
				results.add(new QueryResult(doc
						.get(AbstractClassIndexer.CLASS_NAME_FIELD), doc
						.get(AbstractClassIndexer.CLASS_DESCRIPTION_FIELD)));
				System.out.println(doc.get("className"));
			}
			is.close();
			return results;
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}

	}

	/**
	 * Obtiene uno de los resultados de una busqueda de elementos en el indice.
	 * 
	 * @author imediava
	 * 
	 */
	public static class QueryResult {

		private String className;
		private String description;

		protected QueryResult(String className, String description) {
			this.className = className;
			this.description = description;
		}

		/**
		 * @return el nombre de la clase encontrada
		 */
		public String getClassName() {
			return className;
		}

		/**
		 * @return la descripcion de la clase encontrada
		 */
		public String getDescription() {
			return className;
		}

		/**
		 * Una QueryResult queda definida por su nombre. No puede haber dos
		 * resultados para la misma busqueda con el mismo nombre de clase por
		 * tanto el nombre de clase es su clave.
		 */
		@Override
		public boolean equals(Object o) {
			if (o instanceof QueryResult) {
				return ((QueryResult) o).getClassName().equals(getClassName());
			}
			return false;
		}
	}

}
