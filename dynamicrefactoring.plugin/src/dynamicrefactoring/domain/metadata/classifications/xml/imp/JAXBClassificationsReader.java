/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.metadata.classifications.xml.imp;

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

import dynamicrefactoring.domain.metadata.classifications.xml.ClassificationType;
import dynamicrefactoring.domain.metadata.classifications.xml.ClassificationsType;
import dynamicrefactoring.domain.metadata.classifications.xml.ObjectFactory;
import dynamicrefactoring.domain.metadata.classifications.xml.XmlClassificationsReader;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Lector de ficheros de definicion de refactorización basado en JAXB.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 class JAXBClassificationsReader implements XmlClassificationsReader {

	/**
	 * Ruta al fichero de esquema del fichero de clasificaciones XML.
	 */
	static final URL XSD_SCHEMA_URL = JAXBClassificationsReader.class
			.getResource("/Classification/classifications.xsd");

	/**
	 * Instancia del lector de clasificaciones.
	 */
	private static JAXBClassificationsReader instancia = new JAXBClassificationsReader();

	/**
	 * Constructor privado para evitar que se creen instancias nuevas del lector
	 * de clasificaciones.
	 */
	private JAXBClassificationsReader(){}
	
	/**
	 * Lee el conjunto de refactorizaciones guardadas en un fichero xml.
	 * 
	 * Lanza NullPointerException si el stream es nulo.
	 * 
	 * @param path_file
	 * 			url del fichero de clasificaciones
	 * @param editables si las clasificaciones a crear seran editables o no
	 * @return conjunto de clasificaciones contenidas en el fichero.
	 * @throws ValidationException si el xml no cumple las especificaciones del esquema
	 */
	@Override
	public Set<Classification> readClassifications(String path_file, boolean editables) throws ValidationException {
		File file=new File(path_file);
		Preconditions.checkNotNull(file);
		Preconditions.checkArgument(file.exists());
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
					clasif.getName(), clasif.getDescription(), categories, clasif.isMulticategory(), editables);
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
	 *            gestor de eventos de la validación del fichero XML
	 * 
	 * @return objeto con la informacion de las clasificaciones disponibles
	 * @throws ValidationException
	 *             si el xml no cumple las especificaciones del esquema
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
	
	/**
	 * Permite obtener la instancia del lector.
	 * 
	 * @return la instancia del lector
	 */
	protected static JAXBClassificationsReader getInstance(){
		return instancia;
		
	}

}
