package dynamicrefactoring.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.domain.xml.writer.JDOMXMLRefactoringWriterImp;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.io.FileManager;

/**
 * Catalogo de refactorizaciones que lee las refactorizaciones del directorio de
 * refactorizaciones y cuyos cambios se ven reflejados en los ficheros xml de
 * dicho directorio.
 * 
 * @author imediava
 * 
 */
public final class XMLRefactoringsCatalog extends AbstractRefactoringsCatalog
		implements RefactoringsCatalog {

	private static XMLRefactoringsCatalog instance;
	private Set<DynamicRefactoringDefinition> refactorings;

	/**
	 * Construye el catalogo a partir de un conjunto de refactorizaciones
	 * definidas.
	 * 
	 * Uso exclusivo para Tests. En cualquier otro caso debe accederse a
	 * {@link #getInstance()} para utilizar la funcionalidad del catalogo.
	 * 
	 * @param refactorings
	 */
	protected XMLRefactoringsCatalog(
			Set<DynamicRefactoringDefinition> refactorings) {
		super(refactorings);
	}

	/**
	 * Obtiene la instancia del catalogo.
	 * 
	 * @return instancia del catalogo
	 */
	public static RefactoringsCatalog getInstance() {
		if (instance == null) {
			instance = new XMLRefactoringsCatalog(
					getRefactoringsFromDir(RefactoringPlugin
							.getDynamicRefactoringsDir()));

		}
		return instance;
	}

	/**
	 * Actualiza una refactorizacion en el catalogo y su fichero de definicion
	 * xml.
	 */
	@Override
	public void updateRefactoring(String oldRefactoringName,
			DynamicRefactoringDefinition refactoring) {
		super.removeRefactoring(oldRefactoringName);
		addRefactoring(refactoring);
		if (!oldRefactoringName.equals(refactoring.getName())) {
			deleteRefactoringDir(oldRefactoringName);
		}

	}

	@Override
	public void removeRefactoring(String refactoringName) {
		super.removeRefactoring(refactoringName);
		deleteRefactoringDir(refactoringName);
	}

	private void deleteRefactoringDir(String refactoringName) {
		if (getDirectoryToSaveRefactoringFile(refactoringName).exists()) {
			try {
				FileUtils
						.deleteDirectory(getDirectoryToSaveRefactoringFile(refactoringName));
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}
	}

	/**
	 * Agrega una refactorizacion al catalogo y crea su fichero de definicion
	 * xml.
	 */
	@Override
	public void addRefactoring(final DynamicRefactoringDefinition refactoring) {
		createRefactoringDefinitionDirectory(refactoring);
		addDtdFile(refactoring);
		Builder refactBuilder = refactoring.getBuilder();
		if (!refactoring.getImage().isEmpty()) {
			copyFileToRefactoringDirectory(refactoring.getName(),
					refactoring.getAbsolutePathImage());
			refactBuilder.image(getFileName(refactoring.getAbsolutePathImage()));
		}
		final List<RefactoringExample> ejemplos = copyExampleFiles(
				refactoring.getName(), refactoring.getAbsolutePathExamples());
		DynamicRefactoringDefinition refactToAdd = refactBuilder.examples(ejemplos).build();
		super.addRefactoring(refactToAdd);
		saveRefactoringToXmlFile(refactToAdd);

	}

	private String getFileName(String fichero) {
		return new File(fichero).getName();
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
	private List<RefactoringExample> copyExampleFiles(
			String newRefactoringName, List<RefactoringExample> sourceExamples) {
		List<RefactoringExample> ejemplos = new ArrayList<RefactoringExample>();
		for (RefactoringExample ejemplo : sourceExamples) {
			copyFileToRefactoringDirectory(newRefactoringName,
					ejemplo.getBefore());
			copyFileToRefactoringDirectory(newRefactoringName,
					ejemplo.getAfter());
			ejemplos.add(new RefactoringExample(
					getFileName(ejemplo.getBefore()), getFileName(ejemplo
							.getAfter())));
		}
		return ejemplos;
	}

	private void copyFileToRefactoringDirectory(String newRefactoringName,
			String fileSourcePath) {
		try {
			final File sourceImageFile = new File(fileSourcePath);
			Preconditions.checkArgument(sourceImageFile.exists());
			final File targetImageFile = new File(
					getDirectoryToSaveRefactoringFile(newRefactoringName)
							+ File.separator + sourceImageFile.getName());
			if(!targetImageFile.exists()) {
				FileUtils.copyFile(sourceImageFile, targetImageFile);
			}

		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private void createRefactoringDefinitionDirectory(
			final DynamicRefactoringDefinition refactoring) {
		try {
			if (!getDirectoryToSaveRefactoringFile(refactoring.getName())
					.exists()) {
				FileUtils.forceMkdir(getDirectoryToSaveRefactoringFile(refactoring
								.getName()));
			}
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private void addDtdFile(DynamicRefactoringDefinition refactToAdd) {
		try {
			final File destinationFile = new File(
					getDirectoryToSaveRefactoringFile(refactToAdd.getName())
							.getAbsolutePath()
							+ File.separator
							+ RefactoringConstants.DTD_FILE_NAME);
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
	 * Guarda el fichero xml de definicion de la refactorizacion en la ruta que
	 * le corresponde.
	 * 
	 * @param refact
	 *            definicion de la refactorizacion a guardar
	 */
	private void saveRefactoringToXmlFile(DynamicRefactoringDefinition refact) {
		try {
			new JDOMXMLRefactoringWriterImp(refact)
					.writeRefactoring(getDirectoryToSaveRefactoringFile(refact
							.getName()));
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Obtiene ruta donde se guarda el fichero de definicion de la
	 * refactorizacion pasada.
	 * 
	 * @param refactName
	 *            nombre de la refactorizacion
	 * @return ruta donde se guarda la definicion de la refactorizacion
	 */
	public static String getXmlRefactoringDefinitionFilePath(String refactName) {
		return getDirectoryToSaveRefactoringFile(refactName).getPath()
				+ File.separator + refactName
				+ RefactoringConstants.FILE_EXTENSION;

	}

	/**
	 * Obtiene un fichero cuya ruta sera la del directorio donde se guardara el
	 * fichero de definicion de la refactorizacion.
	 * 
	 * @param refact
	 *            nombre de la refactorizacion
	 * @return fichero con la ruta donde se guardara la definicion de la
	 *         refactorizacion
	 */
	protected static File getDirectoryToSaveRefactoringFile(String refactName) {
		return new File(RefactoringPlugin.getDynamicRefactoringsDir()
				+ File.separator + refactName + File.separator);
	}

	/**
	 * Lee las refactorizaciones existentes en el directorio pasado.
	 * 
	 * @param dir
	 *            directorio contenedor de las refactorizaciones
	 * @return conjunto de refactorizaciones leidas del directorio
	 */
	protected static final Set<DynamicRefactoringDefinition> getRefactoringsFromDir(
			String dir) {
		try {
			HashMap<String, String> lista = DynamicRefactoringLister
					.getInstance().getDynamicRefactoringNameList(dir, true,
							null);
			Set<DynamicRefactoringDefinition> refacts = new HashSet<DynamicRefactoringDefinition>();
			JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp();
			for (String refactFile : lista.values()) {
				refacts.add(reader.getDynamicRefactoringDefinition(new File(
						refactFile)));
			}
			return refacts;
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

}
