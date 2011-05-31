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

enum SimpleElementSearcher {

	INSTANCE;

	protected Set<QueryResult> search(SearchableType element, String userQuery)
			throws ParseException {

		try {
			Directory dir = FSDirectory.open(new File(element.getIndexDir()));
			return search(element, userQuery, dir);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}

	}

	protected Set<QueryResult> search(SearchableType element, String userQuery, Directory dir)
			throws ParseException {
		try {
			IndexSearcher is = new IndexSearcher(dir);
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30,new String[] { SearchableTypeIndexer.CLASS_DESCRIPTION_FIELD, SearchableTypeIndexer.CLASS_NAME_FIELD},
					SearchableTypeIndexer.getTermsAnalyzer());
			
			TopDocs hits = is.search(parser.parse(userQuery), 10);
			Set<QueryResult> results = new HashSet<QueryResult>(hits.totalHits);
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = is.doc(scoreDoc.doc);
				results.add(
					new QueryResult(doc.get(SearchableTypeIndexer.FULLY_QUALIFIED_CLASS_NAME_FIELD),
						doc.get(SearchableTypeIndexer.CLASS_DESCRIPTION_FIELD)));
			}
			is.close();
			return results;
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}

	}

}
