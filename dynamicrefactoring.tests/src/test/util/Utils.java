package test.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.metadata.classifications.xml.imp.PluginClassificationsCatalogTest;
import dynamicrefactoring.util.io.FileManager;

/**
 * Clase con utilidades para los tests.
 * 
 * @author imediava
 *
 */
public class Utils {

	/**
	 * Coloca en el directorio de refactorizaciones 
	 * {@link RefactoringPlugin#getDynamicRefactoringsDir()}
	 * las definiciones de refactorizaciones de pruebas.
	 * 
	 * @throws IOException si hay algun fallo con los ficheros
	 */
	public static void setTestRefactoringInRefactoringsDir() throws IOException {
		FileUtils.deleteDirectory(new File(RefactoringPlugin
				.getDynamicRefactoringsDir()));
	
		FileManager.copyBundleDirToFileSystem(PluginClassificationsCatalogTest.TEST_REPO_PATH,
				RefactoringPlugin.getCommonPluginFilesDir()
						+ File.separator + "test" + File.separator);
	
		FileUtils.copyDirectory(new File(RefactoringPlugin.getCommonPluginFilesDir()
				+ File.separator + "test" + File.separator + PluginClassificationsCatalogTest.TEST_REPO_PATH),
				new File(RefactoringPlugin.getCommonPluginFilesDir()));
		FileUtils.deleteDirectory(new File(RefactoringPlugin.getCommonPluginFilesDir()
				+ "test"));
	}

	
	/**
	 * Restaura las refactorizaciones que se instalan por defecto
	 * con el plugin en el directorio de refactorizaciones:
	 * {@link RefactoringPlugin#getDynamicRefactoringsDir()}
	 * 
	 * 
	 * @throws IOException
	 */
	public static void restoreDefaultRefactorings() throws IOException {
		FileUtils.deleteDirectory(new File(RefactoringPlugin
				.getDynamicRefactoringsDir()));
		FileManager.copyBundleDirToFileSystem(
				RefactoringPlugin.DYNAMIC_REFACTORINGS_FOLDER_NAME,
				RefactoringPlugin.getCommonPluginFilesDir());
	}

}
