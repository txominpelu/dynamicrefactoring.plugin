package dynamicrefactoring.domain.xml;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Throwables;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

public final class XMLRefactoringsFileUtils {
	
	/**
	 * Elimina el directorio de la refactorizacion.
	 * 
	 * @param refactoring refactorizacion de la que se borrara su directorio
	 */
	public static void deleteRefactoringDir(DynamicRefactoringDefinition refactoring) {
		if (refactoring.getDirectoryToSaveRefactoringFile().exists()) {
			try {
				FileUtils
						.deleteDirectory(refactoring.getDirectoryToSaveRefactoringFile());
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}
	}
	
}
