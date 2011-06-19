/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.xml.writer.JDOMXMLRefactoringWriterImp;
import dynamicrefactoring.util.io.FileManager;

/**
 * Objeto que permite crear el directorio de la refactorizacion con los ficheros
 * correspondientes
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class AddRefactoringObject {

	private final DynamicRefactoringDefinition refactoring;

	/**
	 * Crea un objeto que permite crear el directorio de la refactorizacion con
	 * los ficheros correspondientes.
	 * 
	 * @see #addRefactoring()
	 * 
	 * @param refactoring2
	 *            refactorizacion de la que se creara el directorio
	 */
	public AddRefactoringObject(DynamicRefactoringDefinition refactoring) {
		this.refactoring = refactoring;
	}

	/**
	 * Agrega la refactorizacion al directorio que la corresponde y crea todos
	 * los ficheros necesarios en dicho directorio.
	 * 
	 * @return devuelve la refactorizacion con las rutas de los ficheros de
	 *         ejemplos y de imágen modificados debido a que ahora pertenecen al
	 *         directorio de la refactorizacion
	 */
	public DynamicRefactoringDefinition addRefactoring() {
		createRefactoringDefinitionDirectory();
		addDtdFile();
		Builder refactBuilder = refactoring.getBuilder();
		if (!refactoring.getImage().isEmpty()) {
			copyFileToRefactoringDirectory(refactoring.getImageAbsolutePath());
			refactBuilder.image(new File(refactoring.getImageAbsolutePath())
					.getName());
		}
		copyExampleFiles();
		DynamicRefactoringDefinition refactToAdd = refactBuilder.examples(
				getExamplesFileNames()).build();
		saveRefactoringToXmlFile(refactToAdd);
		return refactToAdd;

	}

	/**
	 * Crea el directorio de la refactorizacion si no existe ya.
	 */
	private void createRefactoringDefinitionDirectory() {
		try {
			if (!refactoring.getRefactoringDirectoryFile().exists()) {
				FileUtils.forceMkdir(refactoring
						.getRefactoringDirectoryFile());
			}
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Agrega el fichero DTD esquema de la definicion de refactorizaciones en
	 * xml al directorio de la refactorizacion.
	 * 
	 * @param refactToAdd
	 */
	private void addDtdFile() {
		try {
			final File destinationFile = new File(refactoring
					.getRefactoringDirectoryFile()
					.getAbsolutePath()
					+ File.separator + RefactoringConstants.DTD_FILE_NAME);
			if (!destinationFile.exists()) {
				// Se copia el fichero con la DTD.
				FileManager.copyFile(new File(RefactoringConstants.DTD_PATH),
						destinationFile);
			}
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Dada una lista de ficheros de ejemplo los copia al directorio que
	 * corresponde a la refactorizacion y se devuelve la lista de dichos
	 * ejemplos con la nueva ruta que ahora les corresponde en el directorio del
	 * fichero de definicion de la refactorizacion.
	 * 
	 * @param newRefactoringName
	 *            nombre de la refactorizacion
	 * @param sourceExamples
	 *            ficheros origen de ejemplo que se van a copiar
	 * @return rutas de los ficheros de ejemplo tras la copia
	 */
	private List<RefactoringExample> copyExampleFiles() {
		List<RefactoringExample> ejemplos = new ArrayList<RefactoringExample>();
		for (RefactoringExample ejemplo : refactoring.getExamplesAbsolutePath()) {
			copyFileToRefactoringDirectory(ejemplo.getBefore());
			copyFileToRefactoringDirectory(ejemplo.getAfter());
			ejemplos.add(new RefactoringExample(new File(ejemplo.getBefore())
					.getName(), new File(ejemplo.getAfter()).getName()));
		}
		return ejemplos;
	}

	/**
	 * Copia un fichero al directorio de la refactorizacion indicada por el
	 * nombre.
	 * 
	 * @param newRefactoringName
	 *            nombre de la refactorizacon
	 * @param fileSourcePath
	 *            ruta actual del fichero que se copiara al directorio de la
	 *            refactorizacion
	 */
	private void copyFileToRefactoringDirectory(String fileSourcePath) {
		try {
			final File sourceFile = new File(fileSourcePath);
			Preconditions.checkArgument(sourceFile.exists(), String.format("The file %s doesn't exist.", sourceFile.getAbsolutePath()));
			final File targetFile = new File(
					refactoring
							.getRefactoringDirectoryFile()
							+ File.separator + sourceFile.getName());
			if (!targetFile.exists()) {
				FileUtils.copyFile(sourceFile, targetFile);
			}

		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Devuelve los nombres (incluida extension) de los ficheros de ejemplo de
	 * la refactorizacion.
	 * 
	 * @return una lista de arrays de cadenas con los atributos de cada ejemplo.
	 * 
	 */
	private List<RefactoringExample> getExamplesFileNames() {
		List<RefactoringExample> absolutePathExamples = new ArrayList<RefactoringExample>();
		for (RefactoringExample ejemplo : this.refactoring.getExamples()) {
			absolutePathExamples.add(new RefactoringExample(new File(ejemplo
					.getBefore()).getName(), new File(ejemplo.getAfter())
					.getName()));
		}
		return absolutePathExamples;
	}

	/**
	 * Guarda el fichero xml de definicion de la refactorizacion en la ruta que
	 * le corresponde.
	 * 
	 * @param refact
	 *            definicion de la refactorizacion a guardar
	 */
	private void saveRefactoringToXmlFile(DynamicRefactoringDefinition refactoringToSave) {
		try {
			new JDOMXMLRefactoringWriterImp(refactoringToSave)
					.writeRefactoring(refactoringToSave
							.getRefactoringDirectoryFile());
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}


}
