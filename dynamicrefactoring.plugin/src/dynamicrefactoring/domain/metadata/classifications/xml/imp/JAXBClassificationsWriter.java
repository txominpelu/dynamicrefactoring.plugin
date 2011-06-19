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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.domain.metadata.classifications.xml.CategoriesType;
import dynamicrefactoring.domain.metadata.classifications.xml.ClassificationType;
import dynamicrefactoring.domain.metadata.classifications.xml.ClassificationsType;
import dynamicrefactoring.domain.metadata.classifications.xml.ObjectFactory;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Escritor de clasificaciones en XML que utiliza JAXB.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class JAXBClassificationsWriter {

	/**
	 * Instancia del escritor.
	 */
	private static JAXBClassificationsWriter instance;

	/**
	 * Constructor privado para evitar que se cree instancias.
	 */
	private JAXBClassificationsWriter() {
	}

	/**
	 * Obtiene la instancia del lector.
	 * 
	 * @return instancia del lector
	 */
	protected static JAXBClassificationsWriter getInstance() {
		if (instance == null) {
			instance = new JAXBClassificationsWriter();
		}
		return instance;
	}

	/**
	 * Guarda en el fichero xml las clasificaciones que se le pasan en el
	 * formato adecuado de fichero de definicion de clasificaciones xml.
	 * 
	 * @param classificationsReceived
	 *            clasificaciones a guardar en xml
	 * @param xmlfile
	 *            ruta del fichero xml
	 * @throws FileNotFoundException
	 *             si el directorio del fichero xml no existe
	 */
	public final void saveClassificationsToXml(
			Set<Classification> classificationsReceived, String xmlfile)
			throws FileNotFoundException {
		Preconditions.checkNotNull(classificationsReceived);
		Preconditions.checkNotNull(xmlfile);
		if (!new File(xmlfile).getParentFile().exists()) {
			throw new FileNotFoundException(String.format(
					"The directory %s doesn't exist.",
					new File(xmlfile).getParent()));
		}

		ClassificationsType classificationsType = new ClassificationsType();
		for (Classification clasif : classificationsReceived) {
			ClassificationType classification = new ClassificationType();
			classification.setName(clasif.getName());
			classification.setDescription(clasif.getDescription());
			classification.setMulticategory(clasif.isMultiCategory());
			CategoriesType categoriesType = new CategoriesType();
			for (Category c : clasif.getCategories()) {
				categoriesType.getCategory().add(c.getName());
			}
			classification.setCategories(categoriesType);
			classificationsType.getClassification().add(classification);
		}

		marshall(xmlfile, classificationsType);
	}

	/**
	 * Ejecuta el proceso de marshall.
	 * 
	 * @param xmlfile
	 *            fichero xml
	 * @param classificationsType
	 *            clasificacion a guardar
	 * @throws FileNotFoundException
	 *             si la ruta al fichero pasado es incorrecta
	 */
	private void marshall(String xmlfile,
			ClassificationsType classificationsType)
			throws FileNotFoundException {
		ValidationEventCollector validationEventHandler = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class
					.getPackage().getName());
			Marshaller m = jc.createMarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf
					.newSchema(JAXBClassificationsReader.XSD_SCHEMA_URL);
			m.setSchema(schema);
			validationEventHandler = new ValidationEventCollector();
			m.setEventHandler(validationEventHandler);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(new ObjectFactory()
					.createClassifications(classificationsType),
					new FileOutputStream(xmlfile));
		} catch (JAXBException e) {
			if (e instanceof UnmarshalException
					&& validationEventHandler.hasEvents()) {
				// Si ha habido problemas de validacion del xml
				throw Throwables.propagate(e);
			}
			throw Throwables.propagate(e);
		} catch (SAXException e) {
			throw Throwables.propagate(e);
		}
	}

}
