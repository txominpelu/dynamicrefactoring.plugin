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
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.metadata.condition.ActionCondition;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.condition.InputTypeCondition;
import dynamicrefactoring.domain.metadata.condition.PostconditionCondition;
import dynamicrefactoring.domain.metadata.condition.PreconditionCondition;
import dynamicrefactoring.domain.metadata.condition.RootInputTypeCondition;
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

	@Override
	public void addCategoryToRefactoring(String refactName,
			String classificationName, String categoryName) {
		Preconditions.checkArgument(hasRefactoring(refactName));
		Preconditions.checkArgument(containsClassification(classificationName));
		Preconditions.checkArgument(getClassification(classificationName)
				.getCategories().contains(
						new Category(classificationName, categoryName)));
		final DynamicRefactoringDefinition refactoringDef = getRefactoring(refactName);
		refactorings.remove(refactoringDef);
		Set<Category> categories = refactoringDef.getCategories();
		categories.add(new Category(classificationName, categoryName));
		refactorings.add(refactoringDef.getBuilder().categories(categories)
				.build());
	}

	@Override
	public void removeClassification(String classification) {
		Preconditions.checkArgument(containsClassification(classification));
		for (Category c : getClassification(classification).getCategories()) {
			removeCategory(classification, c.getName());
		}
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
	public final boolean containsClassification(String name) {
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
	public final Set<Classification> getAllClassifications() {
		return new HashSet<Classification>(classifications);
	}

	@Override
	public final Classification getClassification(String name) {
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
			DynamicRefactoringDefinition renamedCategory = renameRefactoringCategory(
					refact, classifName, oldName, newName);
			refactorings.add(renamedCategory);
		}
	}

	@Override
	public void addCategoryToClassification(String classificationName, String categoryNewName) {
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
		for (DynamicRefactoringDefinition refact : getRefactoringBelongingTo(
				classification, categoryName)) {
			refactorings.remove(refact);
			DynamicRefactoringDefinition modifiedRefactoring = deleteRefactoringCategory(
					refact, classification, categoryName);
			refactorings.add(modifiedRefactoring);
		}
		Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
		if (!oldClassif
				.removeCategory(new Category(classification, categoryName))
				.getCategories().isEmpty()) {
			classifications.add(oldClassif.removeCategory(new Category(
					classification, categoryName)));
		}

	}

	/**
	 * Obtiene una copia de la refactorizacion en la que se sustituye el nombre
	 * de una de las categorias a la que la refactorizacion original pertenecia.
	 * 
	 * @param refact
	 *            refactorizacion de la que se va a renombrar una categoria
	 * @param classificationName
	 *            clasificacion a la que pertenece la categoria a cambiar
	 * @param oldName
	 *            nombre de la categoria actual
	 * @param newName
	 *            nombre que tomara la nueva categoria
	 * @return nueva refactorizacion con los cambios aplicados
	 */
	private final DynamicRefactoringDefinition renameRefactoringCategory(
			DynamicRefactoringDefinition refact, String classificationName,
			String oldName, String newName) {
		Preconditions
				.checkArgument(
						refact.getCategories().contains(
								new Category(classificationName, oldName)),
						String.format(
								"The category %s you're trying to rename from %s doesn't exist.",
								new Category(classificationName, oldName),
								refact));
		Builder builder = refact.getBuilder();
		Set<Category> categories = refact.getCategories();
		categories.remove(new Category(classificationName, oldName));
		categories.add(new Category(classificationName, newName));
		return builder.categories(categories).build();
	}

	private final DynamicRefactoringDefinition updateRefactoringCategoryParent(
			DynamicRefactoringDefinition refact, Category toUpdate,
			String newParentName) {
		Preconditions.checkArgument(!refact.getCategories().contains(
				new Category(newParentName, toUpdate.getName())));
		Builder builder = refact.getBuilder();
		Set<Category> categories = refact.getCategories();
		categories.remove(toUpdate);
		categories.add(new Category(newParentName, toUpdate.getName()));
		return builder.categories(categories).build();
	}

	@Override
	public void renameClassification(String clasifName, String clasifNewName) {
		Preconditions.checkArgument(containsClassification(clasifName));
		Preconditions.checkArgument(!containsClassification(clasifNewName));
		final Classification oldClassif = getClassification(clasifName);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.rename(clasifNewName));
		for (Category category : oldClassif.getCategories()) {
			for (DynamicRefactoringDefinition refact : getRefactoringBelongingTo(
					clasifName, category.getName())) {
				refactorings.remove(refact);
				refactorings.add(updateRefactoringCategoryParent(refact,
						category, clasifNewName));
			}
		}
	}

	/**
	 * Obtiene una copia de la refactorizacion en la que se elimina una
	 * categoria a la que la refactorizacion original pertenecia.
	 * 
	 * @param refact
	 *            refactorizacion de la que se va a quitar una categoria
	 * @param classificationName
	 *            clasificacion a la que pertenece la categoria a cambiar
	 * @param oldName
	 *            nombre de la categoria a eliminar
	 * @return nueva refactorizacion con los cambios aplicados
	 */
	private final DynamicRefactoringDefinition deleteRefactoringCategory(
			DynamicRefactoringDefinition refact, String classificationName,
			String oldName) {
		Preconditions
				.checkArgument(
						refact.getCategories().contains(
								new Category(classificationName, oldName)),
						String.format(
								"The category %s you're trying to remove from %s doesn't exist.",
								new Category(classificationName, oldName),
								refact));
		Builder builder = refact.getBuilder();
		Set<Category> categories = refact.getCategories();
		categories.remove(new Category(classificationName, oldName));
		return builder.categories(categories).build();
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
		return ImmutableSet.copyOf(Collections2.filter(refactorings,
				new CategoryCondition<DynamicRefactoringDefinition>(
						classification, categoryName)));
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

	@Override
	public void addClassification(Classification classification) {
		classifications.add(classification);
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
	public final boolean hasRefactoring(final String name) {
		return !Collections2.filter(ImmutableSet.copyOf(refactorings),
				new SameNamePredicate(name)).isEmpty();
	}

	@Override
	public final DynamicRefactoringDefinition getRefactoring(String refactName) {
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
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus tipos de entrada el indicado por el parámetro.
	 * 
	 * @param inputType tipo de entrada
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	public Set<DynamicRefactoringDefinition> 
			getRefactoringsContainsInputType(String inputType){
		
		return 
			new HashSet<DynamicRefactoringDefinition>(Collections2
					.filter(ImmutableSet.copyOf(refactorings),
							new InputTypeCondition(inputType)));

	}
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene como tipo de entrada raiz el indicado por el parámetro.
	 * 
	 * @param rootInputType tipo de entrada de la entrada raiz
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	public Set<DynamicRefactoringDefinition> 
			getRefactoringsContainsRootInputType(String rootInputType){

		return 
			new HashSet<DynamicRefactoringDefinition>(Collections2
					.filter(ImmutableSet.copyOf(refactorings),
							new RootInputTypeCondition(rootInputType)));

	}
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus precondiciones la precondición indicada por el parámetro.
	 * 
	 * @param precondition precondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	public Set<DynamicRefactoringDefinition> 
			getRefactoringsContainsPrecondition(String precondition){

		return 
			new HashSet<DynamicRefactoringDefinition>(Collections2
					.filter(ImmutableSet.copyOf(refactorings),
							new PreconditionCondition(precondition)));

	}
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus acciones la acción indicada por el parámetro.
	 * 
	 * @param action acción
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	public Set<DynamicRefactoringDefinition> 
			getRefactoringsContainsAction(String action){

		return 
			new HashSet<DynamicRefactoringDefinition>(Collections2
					.filter(ImmutableSet.copyOf(refactorings),
							new ActionCondition(action)));

	}
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus postcondiciones la postcondición indicada por el parámetro.
	 * 
	 * @param postcondition postcondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	public Set<DynamicRefactoringDefinition> 
			getRefactoringsContainsPostcondition(String postcondition){

		return 
			new HashSet<DynamicRefactoringDefinition>(Collections2
					.filter(ImmutableSet.copyOf(refactorings),
							new PostconditionCondition(postcondition)));

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
