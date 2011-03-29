package dynamicrefactoring.wizard.search.internal;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.util.MOONTypeLister;

/**
 * Indexador de Inputs (entradas).
 * 
 * @author imediava
 *
 */
final class InputsIndexer extends AbstractClassIndexer implements Indexer {
	
	private static final InputsIndexer INSTANCE = new InputsIndexer();
	
	/**
	 * Constructor privado que evita que la clase se pueda
	 * instanciar.
	 */
	private InputsIndexer(){
		
	}
	
	/**
	 * Obtiene una instancia del indexador
	 * de entradas.
	 * 
	 * @return instancia del indexador
	 */
	protected static InputsIndexer getInstance(){
		return INSTANCE;
	}

	@Override
	public String getIndexDir() {
		return RefactoringPlugin.getDefault().getPluginTempDir()
		+ File.separator + "index"
		+ File.separator + "inputs";
	}

	@Override
	Set<String> getClassesToIndex() {
		try {
			return ImmutableSet.copyOf(MOONTypeLister.getInstance().getTypeNameList());
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}


}
