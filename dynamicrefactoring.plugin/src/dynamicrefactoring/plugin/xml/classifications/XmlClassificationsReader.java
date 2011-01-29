package dynamicrefactoring.plugin.xml.classifications;

import java.io.File;
import java.util.Set;

import javax.xml.bind.ValidationException;

import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Lector de clasificaciones guardadas en un xml.
 * 
 * @author imediava
 * 
 */
public interface XmlClassificationsReader {

	/**
	 * Lee el conjunto de refactorizaciones guardadas en un fichero xml.
	 * 
	 * Lanza NullPointerException si la URL es nula.
	 * 
	 * @param path_file
	 *            URL que describe la ruta al fichero
	 * @return conjunto de clasificaciones contenidas en el fichero.
	 * @throws ValidationException si el xml no cumple las especificaciones del esquema
	 */
	public Set<Classification> readClassifications(String path_file) throws ValidationException;

}
