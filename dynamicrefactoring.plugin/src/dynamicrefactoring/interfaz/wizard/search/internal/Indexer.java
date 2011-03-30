package dynamicrefactoring.interfaz.wizard.search.internal;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.Directory;

import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade.SearchableType;

/**
 * Generador de indices.
 * 
 * @author imediava
 * 
 */
interface Indexer {


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
	int index(SearchableType elementType, Directory directory)
			throws IOException;

}
