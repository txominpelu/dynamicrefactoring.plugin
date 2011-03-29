package dynamicrefactoring.wizard.search.internal;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

import org.apache.commons.io.FileUtils;
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

import dynamicrefactoring.wizard.search.javadoc.EclipseBasedJavadocReader;
import dynamicrefactoring.wizard.search.javadoc.JavadocReader;

/**
 * Indexador de clases y sus descripciones en Javadoc.
 * 
 * @author imediava
 *
 */
abstract class AbstractClassIndexer implements Indexer {

	public static final String CLASS_NAME_FIELD = "className";
	public static final String CLASS_DESCRIPTION_FIELD = "contents";

	/**
	 * Genera un documento para una clase.
	 * 
	 * @param className nombre de la clase
	 * @param classDescription descripcion de la clase
	 * @return documento a indexar para la clase
	 */
	protected Document getDocument(String className, String classDescription) {
		Document doc = new Document();
		doc.add(new Field(CLASS_DESCRIPTION_FIELD, new StringReader(classDescription)));
		doc.add(new Field(CLASS_NAME_FIELD, className, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		return doc;
	}


	/**
	 * Genera un indice en el directorio del sistema 
	 * de ficheros cuya ruta viene dada por {@link #getIndexDir()}.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected int index() throws IOException {
		if (!new File(getIndexDir()).exists()) {
			FileUtils.forceMkdir(new File(getIndexDir()));
		}
		return index( FSDirectory.open(new File(getIndexDir())));
	}

	/**
	 * Genera un indice en el directorio pasado. 
	 * 
	 * @param directory directorio sobre el que se generara
	 * el indice
	 * 
	 * @return numero de elementos indizados
	 * @throws CorruptIndexException 
	 * 
	 * @throws IOException 
	 */
	@Override
	public int index(Directory directory) throws IOException {
		final IndexWriter writer = createWriter(directory);
		int numIndexed = 0;
		JavadocReader javadocReader = EclipseBasedJavadocReader.INSTANCE;
		for (String fullyQualifiedName : getClassesToIndex()) {
			String text = javadocReader.getTypeJavaDocAsPlainText(fullyQualifiedName);
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
	 * @param writer escritor del indice
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
	 * @param indexDir directorio en el que se creara el indice
	 * @return escritor del indice
	 * 
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 */
	private IndexWriter createWriter(Directory dir) {
		try {
			return new IndexWriter(dir, new SnowballAnalyzer(Version.LUCENE_30,
					"Spanish"), true, IndexWriter.MaxFieldLength.UNLIMITED);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Devuelve el directorio en el que se guardara el indice.
	 * 
	 * @return ruta del directorio del indice
	 */
	abstract String getIndexDir();

	/**
	 * Devuelve una lista con los nombres completamente cualificados
	 * (moon.core.Model) de todas las clases que se deben indexar.
	 * 
	 * @return conjunto de nombres de las clases a indexar
	 */
	abstract Set<String> getClassesToIndex();

}
