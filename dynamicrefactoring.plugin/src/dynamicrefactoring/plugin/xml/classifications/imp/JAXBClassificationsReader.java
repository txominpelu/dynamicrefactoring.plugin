package dynamicrefactoring.plugin.xml.classifications.imp;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationException;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.ClassificationType;
import dynamicrefactoring.plugin.xml.classifications.ClassificationsType;
import dynamicrefactoring.plugin.xml.classifications.ObjectFactory;
import dynamicrefactoring.plugin.xml.classifications.XmlClassificationsReader;

class JAXBClassificationsReader implements XmlClassificationsReader {

	static final URL XSD_SCHEMA_URL = JAXBClassificationsReader.class
			.getResource("/Classification/classifications.xsd");
	private static final String DTD_SCHEMA_URL = "/Classification/classificationsDTD.dtd";
	
	private static JAXBClassificationsReader instancia = new JAXBClassificationsReader();

	
	private JAXBClassificationsReader(){}
	
	/**
	 * Lee el conjunto de refactorizaciones guardadas en un fichero xml.
	 * 
	 * Lanza NullPointerException si el stream es nulo.
	 * 
	 * @param path_file
	 * 			url del fichero de clasificaciones
	 * @return conjunto de clasificaciones contenidas en el fichero.
	 * @throws ValidationException si el xml no cumple las especificaciones del esquema
	 */
	@Override
	public Set<Classification> readClassifications(String path_file) throws ValidationException {
		File file=new File(path_file);
		Preconditions.checkNotNull(file);
		Set<Classification> availableClassifications = new HashSet<Classification>();
		ClassificationsType classifications = generateClassificationsXmlType(
				file);
		for (ClassificationType clasif : classifications.getClassification()) {
			Set<Category> categories = new HashSet<Category>();
			for (String categoryName : clasif.getCategories().getCategory()) {
				categories.add(new Category(clasif.getName(),
						categoryName));
			}
			Classification cl = new SimpleUniLevelClassification(
					clasif.getName(), clasif.getDescription(), categories, clasif.isMulticategory());
			availableClassifications.add(cl);
		}
		return availableClassifications;
	}

	/**
	 * Genera un objeto con las clasificaciones obtenidos del fichero xml cuya
	 * url es pasada.
	 * 
	 * @param file
	 *            fichero xml con las clasificaciones disponibles
	 * @param validationEventHandler
	 * 
	 * @return objeto con la informacion de las clasificaciones disponibles
	 * @throws ValidationException si el xml no cumple las especificaciones del esquema
	 */
	private ClassificationsType generateClassificationsXmlType(File file) throws ValidationException{
		ValidationEventCollector validationEventHandler = new ValidationEventCollector();
		try {
			JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
			.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(XSD_SCHEMA_URL);
			unmarshaller.setSchema(schema);
			unmarshaller.setEventHandler(validationEventHandler);
			JAXBElement<?> doc = (JAXBElement<?>) unmarshaller
					.unmarshal(file);
			ClassificationsType classifications = (ClassificationsType) doc
					.getValue();

			return classifications;
		} catch (Exception e) {
			if(e instanceof UnmarshalException && validationEventHandler.hasEvents()){
				// Si ha habido problemas de validacion del xml
				throw new ValidationException(e);
			}
			// si el unmarshall "is unable to perform the XML to Java binding"
			throw Throwables.propagate(e);
		} 
	}
	
	protected static JAXBClassificationsReader getInstance(){
		return instancia;
		
	}

}
