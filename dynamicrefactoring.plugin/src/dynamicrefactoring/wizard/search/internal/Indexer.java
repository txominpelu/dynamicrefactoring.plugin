package dynamicrefactoring.wizard.search.internal;

import java.io.IOException;

import org.apache.lucene.store.Directory;

/**
 * Generador de indices.
 * 
 * @author imediava
 * 
 */
interface Indexer {

	/**
	 * Genera un indice en un directorio pasado.
	 * 
	 * @param dir
	 *            directorio (en memoria, en el sistema de ficheros o en
	 *            cualquier otro formato) en el que se generara el indice
	 * @return numero de elementos indexados
	 * @throws IOException si
	 */
	int index(Directory dir) throws IOException;

}
