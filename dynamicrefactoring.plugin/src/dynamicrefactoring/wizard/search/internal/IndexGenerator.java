package dynamicrefactoring.wizard.search.internal;

import java.io.IOException;

import dynamicrefactoring.domain.RefactoringMechanism;

public final class IndexGenerator {
	
	/**
	 * Evita que esta clase sea instanciada.
	 */
	private IndexGenerator(){
		
	}
	
	public static void generateIndexes() throws IOException {
			for (RefactoringMechanism mechanismType : RefactoringMechanism.values()) {
				RefactoringMechanismIndexer.getInstance(mechanismType).index();
			}
			InputsIndexer.getInstance().index();
	}

}
