package dynamicrefactoring.wizard.search.internal;

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import dynamicrefactoring.domain.RefactoringMechanism;
import dynamicrefactoring.util.PluginStringUtils;

/**
 * Indexador de mecanismos (es decir precondiciones / acciones
 * o postcondiciones).
 * 
 * @author imediava
 *
 */
final class RefactoringMechanismIndexer extends AbstractClassIndexer implements Indexer{
	
	private final RefactoringMechanism type;
	
	private static final ImmutableMap<RefactoringMechanism, RefactoringMechanismIndexer> INDEXERS;

	/**
	 * Creamos los indices para las precondiciones, acciones y postcondiciones.
	 */
	static {
		Builder<RefactoringMechanism, RefactoringMechanismIndexer> builder = new Builder<RefactoringMechanism, RefactoringMechanismIndexer>();
		for(RefactoringMechanism type: RefactoringMechanism.values()){
			builder = builder.put(type, new RefactoringMechanismIndexer(type));
		}
		INDEXERS = builder.build();
		
	}
	
	/**
	 * Construye el escritor de indices del mecanismo de refactorizacion
	 * pasado.
	 * 
	 * @param type tipo de mecanismo de refactorizacion
	 */
	private RefactoringMechanismIndexer (RefactoringMechanism type) {
		this.type = type;
	}

	/**
	 * Obtiene la instancia del tipo de mecanismo pasado
	 * 
	 * @param type tipo de mecanismo del que se va obtener el escritor de indices
	 * @return escritor de indices para un mecanismo de una refactorizacion
	 */
	protected static RefactoringMechanismIndexer getInstance(RefactoringMechanism type) {
		return INDEXERS.get(type);
	}

	@Override
	String getIndexDir() {
		return type.getIndexDir();
	}

	@Override
	Set<String> getClassesToIndex() {
		return PluginStringUtils.getMechanismListFullyQualifiedName(type, type.getElementAllList().keySet());
	}


}
