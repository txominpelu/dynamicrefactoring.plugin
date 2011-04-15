package dynamicrefactoring.domain.metadata.classifications.xml.imp;

import dynamicrefactoring.domain.metadata.classifications.xml.XmlClassificationsReader;

/**
 * Permite obtener instancias de un lector
 * de clasificaciones.
 * 
 * @author imediava
 *
 */
final class ClassificationsReaderFactory {
	
	/**
	 * Constructor privado para evitar que se creen
	 * instancias de esta clase.
	 */
	private ClassificationsReaderFactory(){}
	
	/**
	 * Devuelve un lector de clasificaciones.
	 * 
	 * @return lector de clasificaciones
	 */
	public static XmlClassificationsReader getReader(){
		return JAXBClassificationsReader.getInstance();
	}

}
