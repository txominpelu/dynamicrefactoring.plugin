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
 * @author imediava
 * 
 */
public enum SearchingFacade {

	INSTANCE;

	/**
	 * Tipo de elemento sobre el que se pueden hacer busquedas en el plugin.
	 * 
	 * @author imediava
	 * 
	 */
	public enum SearchableType {
		PREDICATE {
			@Override
			Set<String> getClassesToIndex() {
				return PluginStringUtils.getMechanismListFullyQualifiedName(
						RefactoringMechanismType.PRECONDITION, RefactoringMechanismType
								.getPredicatesAllList().keySet());
			}
		
		},
		ACTION {
			@Override
			Set<String> getClassesToIndex() {
				return PluginStringUtils.getMechanismListFullyQualifiedName(
						RefactoringMechanismType.ACTION,
						RefactoringMechanismType.ACTION.getElementAllList()
								.keySet());
			}

		},
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

	private SearchableTypeIndexer indexer;
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
	 * buscados es decir para todos los elementos de {@link SearchableType}
	 * 
	 * @throws IOException
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
