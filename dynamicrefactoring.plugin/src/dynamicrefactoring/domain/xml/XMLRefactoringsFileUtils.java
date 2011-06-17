package dynamicrefactoring.domain.xml;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Throwables;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

/**
 * Metodos utiles para modificar el catalogo de refactorizaciones en XML. 
 *
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
public final class XMLRefactoringsFileUtils {
	
	/**
	 * Elimina el directorio de la refactorizacion.
	 * 
	 * @param refactoring refactorizacion de la que se borrara su directorio
	 */
	public static void deleteRefactoringDir(DynamicRefactoringDefinition refactoring) {
		if (refactoring.getRefactoringDirectoryFile().exists()) {
			try {
				FileUtils
						.deleteDirectory(refactoring.getRefactoringDirectoryFile());
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}
	}
	
}
