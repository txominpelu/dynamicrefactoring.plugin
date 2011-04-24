package dynamicrefactoring.domain.metadata.classifications.xml;

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
	 * @param editable si las clasificaciones que se leeran del fichero se crearan como editables o no
	 * @return conjunto de clasificaciones contenidas en el fichero.
	 * @throws ValidationException si el xml no cumple las especificaciones del esquema
	 */
	Set<Classification> readClassifications(String path_file, boolean editable)
			throws ValidationException;

}
