package dynamicrefactoring.plugin.xml.classifications.imp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.interfaces.Catalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.util.DynamicRefactoringLister;

abstract class AbstractCatalog implements Catalog {

	private Set<Classification> classifications;
	private Set<DynamicRefactoringDefinition> refactorings;

	public AbstractCatalog(Set<Classification> classifSet,
			Set<DynamicRefactoringDefinition> refactSet) {
		this.classifications = classifSet;
		this.refactorings = new HashSet<DynamicRefactoringDefinition>(refactSet);
	}

	/**
	 * Determina si existe una clasificación con el nombre pasado por
	 * parámetro.
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
		for (DynamicRefactoringDefinition refact : getRefactoringBelongingTo(
				classifName, oldName)) {
			refactorings.remove(refact);
			DynamicRefactoringDefinition renamedCategory = refact
					.renameCategory(classifName, oldName, newName);
			refactorings.add(renamedCategory);
		}
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
		Preconditions.checkArgument(
				getClassification(classification).getCategories().contains(
						new Category(classification, categoryName)), String
						.format("The classification %s doesn't exist.",
								categoryName));
		Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.removeCategory(new Category(
				classification, categoryName)));

	}

	/**
	 * Obtiene todas las refactorizaciones que pertenecen a la categoria
	 * definida por la clasificacion y el nombre de categoria.
	 * 
	 * @param classification
	 *            clasificacion
	 * @param categoryName
	 *            nombre de categoria
	 * @return coleccion de refactorizaciones que pertenecen a la categoria
	 */
	protected final Collection<DynamicRefactoringDefinition> getRefactoringBelongingTo(
			String classification, String categoryName) {
		return Collections2.filter(refactorings,
				new CategoryCondition<DynamicRefactoringDefinition>(
						classification, categoryName));
	}

	/**
	 * Lee las clasificaciones desde el fichero pasado.
	 * 
	 * @param classifFile
	 *            fichero de clasificaciones
	 * @return conjunto de clasificaciones leidas del fichero
	 */
	protected static final Set<Classification> getClassificationsFromFile(
			String classifFile) {
		try {
			Set<Classification> classifications = ClassificationsReaderFactory
					.getReader(
							ClassificationsReaderFactory.ClassificationsReaderTypes.JAXB_READER)
					.readClassifications(classifFile);
			return classifications;
		} catch (ValidationException e) {
			throw Throwables.propagate(e);
		}
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

	@Override
	public boolean hasRefactoring(final String name) {
		return !Collections2.filter(ImmutableSet.copyOf(refactorings),
				new SameNamePredicate(name)).isEmpty();
	}

	@Override
	public DynamicRefactoringDefinition getRefactoring(String refactName) {
		Collection<DynamicRefactoringDefinition> refactoringsForName = Collections2
				.filter(ImmutableSet.copyOf(refactorings),
						new SameNamePredicate(refactName));
		Preconditions.checkArgument(!refactoringsForName.isEmpty(),
				"There's no refactoring with name:" + refactName);
		Preconditions.checkArgument(!(refactoringsForName.size() > 1),
				"There's more than one refactoring with name:" + refactName
						+ " . Refactoring's names must be unique.");
		return refactoringsForName.iterator().next();
	}

	/**
	 * Comprueba si una refactorizacion dada tiene un nombre dado.
	 * 
	 * @author imediava
	 * 
	 */
	private class SameNamePredicate implements
			Predicate<DynamicRefactoringDefinition> {

		private String name;

		/**
		 * Crea el predicado.
		 * 
		 * @param name
		 *            nombre
		 */
		public SameNamePredicate(String name) {
			this.name = name;
		}

		@Override
		public boolean apply(DynamicRefactoringDefinition arg0) {
			return arg0.getName().equals(name);
		}

	}
}
