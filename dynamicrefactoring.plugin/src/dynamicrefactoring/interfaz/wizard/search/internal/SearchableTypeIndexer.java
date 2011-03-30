package dynamicrefactoring.interfaz.wizard.search.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade.SearchableType;
import dynamicrefactoring.interfaz.wizard.search.javadoc.EclipseBasedJavadocReader;
import dynamicrefactoring.interfaz.wizard.search.javadoc.JavadocReader;

/**
 * Indexador de clases y sus descripciones en Javadoc.
 * 
 * @author imediava
 * 
 */
enum SearchableTypeIndexer implements Indexer {
	
	INSTANCE;

	private static final String SEARCH_STOPWORDS_ESPANOL_TXT = "/search/stopwords-espanol.txt";
	public static final String CLASS_NAME_FIELD = "className";
	public static final String CLASS_DESCRIPTION_FIELD = "contents";

	/**
	 * Genera un documento para una clase.
	 * 
	 * @param className
	 *            nombre de la clase
	 * @param classDescription
	 *            descripcion de la clase
	 * @return documento a indexar para la clase
	 */
	protected Document getDocument(String className, String classDescription) {
		Document doc = new Document();
		doc.add(new Field(CLASS_DESCRIPTION_FIELD, new StringReader(
				classDescription)));
		doc.add(new Field(CLASS_NAME_FIELD, className, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		return doc;
	}

	/**
	 * Genera un indice en el directorio del sistema de ficheros cuya ruta viene
	 * dada por {@link #getIndexDir()}.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected int index(SearchableType elementType) throws IOException {
		if (!new File(elementType.getIndexDir()).exists()) {
			FileUtils.forceMkdir(new File(elementType.getIndexDir()));
		}
		return index(elementType, FSDirectory.open(new File(elementType.getIndexDir())));
	}

	/**
	 * Genera un indice en el directorio pasado.
	 * 
	 * @param elementType
	 *            tipo de searchable cuyos elementos se van a indizar
	 * @param directory
	 *            directorio sobre el que se generara el indice
	 * 
	 * @return numero de elementos indizados
	 * @throws CorruptIndexException
	 * 
	 * @throws IOException
	 */
	@Override
	public int index(SearchableType elementType, Directory directory)
			throws IOException {
		final IndexWriter writer = createWriter(directory);
		int numIndexed = 0;
		JavadocReader javadocReader = EclipseBasedJavadocReader.INSTANCE;
		for (String fullyQualifiedName : elementType.getClassesToIndex()) {
			String text = javadocReader
					.getTypeJavaDocAsPlainText(fullyQualifiedName);
			Document doc = getDocument(fullyQualifiedName, text);
			writer.addDocument(doc);
			numIndexed++;
		}
		close(writer);
		return numIndexed;
	}

	/**
	 * Cierra el escritor del indice.
	 * 
	 * @param writer
	 *            escritor del indice
	 */
	private void close(IndexWriter writer) {
		try {
			writer.close();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Crea el escritor del indice.
	 * 
	 * @param indexDir
	 *            directorio en el que se creara el indice
	 * @return escritor del indice
	 * 
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 */
	private IndexWriter createWriter(Directory dir) {
		try {
			return new IndexWriter(dir, new SnowballAnalyzer(Version.LUCENE_30,
					"Spanish", getSpanishStopWords()), true, IndexWriter.MaxFieldLength.UNLIMITED);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
	
	/**
	 * Obtiene la lista de StopWords para el
	 * español del fichero {@link #SEARCH_STOPWORDS_ESPANOL_TXT}.
	 * 
	 * @return lista de Stopwords para el español
	 * @throws IOException si hay algun problema leyendo el fichero
	 */
	private Set<String> getSpanishStopWords() throws IOException{
		InputStream in = RefactoringPlugin.getDefault().getBundle().getEntry( SEARCH_STOPWORDS_ESPANOL_TXT ).openStream();
		Set<String> spanishStopWords = new HashSet<String>();
		 try {
		   for (String line : IOUtils.toString( in ).split("\n")){
			   final String palabra = line.trim();
			   if(! palabra.isEmpty()){
				   spanishStopWords.add(palabra);
			   }
		   }
		} finally {
		   IOUtils.closeQuietly(in);
		 }
		return spanishStopWords;
	}

}
