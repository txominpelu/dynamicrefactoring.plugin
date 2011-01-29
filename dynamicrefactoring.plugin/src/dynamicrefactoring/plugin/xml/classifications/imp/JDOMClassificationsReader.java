package dynamicrefactoring.plugin.xml.classifications.imp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.plugin.xml.classifications.XmlClassificationsReader;

class JDOMClassificationsReader implements XmlClassificationsReader {

	private static final String XML_TAG_CLASSIFICATION = "classification";
	private static final String XML_TAG_CATEGORY = "category";
	private static final String XML_TAG_CATEGORIES = "categories";
	private static final String XML_ATTRIBUTE_NAME = "name";
	private static final String XML_TAG_CLASSIFICATIONS = "classifications";
	private static XmlClassificationsReader instancia = new JDOMClassificationsReader();

	private JDOMClassificationsReader(){}
	
	/**
	 * Lee el conjunto de refactorizaciones guardadas en un fichero xml.
	 * 
	 * Lanza NullPointerException si el stream es nulo.
	 * 
	 * @param path_file
	 *            ruta del fichero de clasificaciones.
	 * @return conjunto de clasificaciones contenidas en el fichero.
	 * @throws ValidationException
	 *             si el xml no cumple las especificaciones del esquema
	 */
	public Set<Classification> readClassifications(String path_file)
			throws ValidationException {
		File file=new File(path_file);
		Preconditions.checkNotNull(file);
		SAXBuilder builder = new SAXBuilder(true);
		builder.setIgnoringElementContentWhitespace(true);
		try {
			Document doc = builder.build(file.toURI().toString());
			Element root = doc.getRootElement();
			return readInputsRefactoring(root);
		} catch (JDOMException e) {
			throw new ValidationException(e);
		} catch (IOException e) {
			// Error inesperado de programación - propagar
			throw Throwables.propagate(e);
		}

	}

	/**
	 * Lee el elemento root leido del fichero y genera a partir de él el
	 * conjunto de clasificaciones contenidas en el fichero xml.
	 * 
	 * @param root
	 *            elemento raiz del fichero xml
	 * @return conjunto de clasificaciones contenidas en el elemento
	 */
	private Set<Classification> readInputsRefactoring(Element root) {

		// Se obtienen las entradas de la refactorización.
		List<Element> classificationsList = root
				.getChildren(XML_TAG_CLASSIFICATION);
		Set<Classification> clasificationsSet = new HashSet<Classification>();
		for (Element classifElement : classificationsList) {
			String classifName = classifElement
					.getAttributeValue(XML_ATTRIBUTE_NAME);
			Set<Category> categorySet = new HashSet<Category>();
			List<Element> categoriesList = classifElement
					.getChildren(XML_TAG_CATEGORIES);
			for (Element categoriesEl : categoriesList) {
				List<Element> categoryList = categoriesEl.getChildren(XML_TAG_CATEGORY);
				for (Element category : categoryList) {
					categorySet.add(new Category(classifName + "."
							+ category.getText()));
				}
			}
			clasificationsSet.add(new SimpleUniLevelClassification(classifName,
					categorySet));
		}
		return clasificationsSet;

	}

	protected static XmlClassificationsReader getInstance() {
		return instancia ;
	}

}
