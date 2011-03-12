package dynamicrefactoring.plugin.xml.classifications.imp;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

abstract class ClassificationsManager implements ClassificationsCatalog {

	private Set<Classification> classifications;

	public ClassificationsManager(String classificationsFilePath) {
		try {
			classifications = ClassificationsReaderFactory
					.getReader(
							ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER)
					.readClassifications(classificationsFilePath);
		} catch (ValidationException e) {
			Throwables.propagate(e);
		}
	}

	/**
	 * Determina si existe una clasificación con el nombre pasado por parámetro.
	 * 
	 * @param name
	 *            nombre de la clasificación
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	@Override
	public boolean containsClassification(String name) {
		for (Classification c : classifications) {
			if (c.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	/**
	 * Obtiene el conjunto de todos las clasificaciones disponibles.
	 * 
	 * @return todas las clasificaciones leidas del fichero xml.
	 */
	@Override
	public Set<Classification> getAllClassifications() {
		return new HashSet<Classification>(classifications);
	}

	@Override
	public Classification getClassification(String name) {
		for (Classification c : classifications) {
			if (c.getName().equalsIgnoreCase(name))
				return c;
		}
		throw new IllegalArgumentException(
				"The catalog doesn't contain a classification by the name: "
						+ name);
	}

	/**
	 * Devuelve otro gestor de clasificaciones en el que se ha renombrado la
	 * categoria <b>oldName</b> a <b>newName</b> en <b>classification</b>.
	 * 
	 * @param classifName
	 *            nombre de la clasificacion
	 * @param oldName
	 *            nombre actual de la categoria
	 * @param newName
	 *            nuevo nombre de la categoria
	 * 
	 */
	@Override
	public void renameCategory(String classifName, String oldName,
			String newName) {
		Preconditions.checkArgument(containsClassification(classifName));
		Preconditions.checkArgument(getClassification(classifName)
				.getCategories().contains(new Category(classifName, oldName)),
				String.format("The classification %s doesn't exist.", oldName));
		Preconditions.checkArgument(!getClassification(classifName)
				.getCategories().contains(new Category(classifName, newName)),
				String.format("The classification %s already exist.", newName));
		Classification oldClassif = getClassification(classifName);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.renameCategory(oldName, newName));
	}

	@Override
	public void addCategory(String classificationName, String categoryNewName) {
		Preconditions.checkArgument(containsClassification(classificationName));
		Preconditions.checkArgument(
				!getClassification(classificationName).getCategories()
						.contains(
								new Category(classificationName,
										categoryNewName)), String
						.format("The classification %s already exist.",
								categoryNewName));
		Classification oldClassif = getClassification(classificationName);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.addCategory(new Category(
				classificationName, categoryNewName)));
	}
	
	@Override
	public void removeCategory(String classification, String categoryName) {
		Preconditions.checkArgument(containsClassification(classification));
		Preconditions.checkArgument(getClassification(classification)
				.getCategories().contains(new Category(classification, categoryName)),
				String.format("The classification %s doesn't exist.", categoryName));
		Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.removeCategory(new Category(
				classification, categoryName)));
	}

}
