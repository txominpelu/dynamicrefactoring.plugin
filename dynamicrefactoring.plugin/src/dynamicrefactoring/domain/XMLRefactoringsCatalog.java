package dynamicrefactoring.domain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.domain.xml.writer.JDOMXMLRefactoringWriterImp;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.io.FileManager;

/**
 * Catalogo de refactorizaciones que lee las refactorizaciones
 * del directorio de refactorizaciones y cuyos cambios se ven 
 * reflejados en los ficheros xml de dicho directorio.
 * 
 * @author imediava
 *
 */
public final class XMLRefactoringsCatalog extends AbstractRefactoringsCatalog implements RefactoringsCatalog {
	
	private static XMLRefactoringsCatalog instance;
	private Set<DynamicRefactoringDefinition> refactorings;
	
	/**
	 * Construye el catalogo a partir de un conjunto de refactorizaciones definidas.
	 * 
	 * Uso exclusivo para Tests. En cualquier otro caso debe accederse a {@link #getInstance()}
	 * para utilizar la funcionalidad del catalogo.
	 * 
	 * @param refactorings
	 */
	protected XMLRefactoringsCatalog(Set<DynamicRefactoringDefinition> refactorings){
		super(refactorings);
	}
	
	/**
	 * Obtiene la instancia del catalogo.
	 * 
	 * @return instancia del catalogo
	 */
	public static RefactoringsCatalog getInstance() {
		if (instance == null){
			instance = new XMLRefactoringsCatalog(getRefactoringsFromDir(RefactoringPlugin.getDynamicRefactoringsDir()));

		}
		return instance;
	}

	/**
	 * Solo para uso en los cambios manuales de ficheros
	 * que se hacen en los tests.
	 * 
	 * @deprecated
	 */
	protected static void updateInstance(){
		instance = new XMLRefactoringsCatalog(getRefactoringsFromDir(RefactoringPlugin.getDynamicRefactoringsDir()));
	}
	
	/**
	 * Actualiza una refactorizacion en el catalogo y su fichero de definicion xml.
	 */
	@Override
	public void updateRefactoring(DynamicRefactoringDefinition refactoring){
		super.updateRefactoring(refactoring);
		saveRefactoringToXmlFile(refactoring);
	}
	

	/**
	 * Agrega una refactorizacion al catalogo y crea su fichero de definicion xml.
	 */
	@Override
	public void addRefactoring(DynamicRefactoringDefinition refactoring){
		super.addRefactoring(refactoring);
		saveRefactoringToXmlFile(refactoring);
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
			if(!getDirectoryToSaveRefactoringFile(refact.getName()).exists()){
				FileUtils.forceMkdir(getDirectoryToSaveRefactoringFile(refact.getName()));
				// Se copia el fichero con la DTD.
				FileManager.copyFile(new File(RefactoringConstants.DTD_PATH),
						new File(getDirectoryToSaveRefactoringFile(refact.getName()).getAbsolutePath() + File.separator + RefactoringConstants.DTD_FILE_NAME));
			}
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
	private static File getDirectoryToSaveRefactoringFile(String refactName) {
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
