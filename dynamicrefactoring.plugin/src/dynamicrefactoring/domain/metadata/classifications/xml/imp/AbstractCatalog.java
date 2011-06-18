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

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.ValidationException;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.DynamicRefactoringDefinition.Builder;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;

/**
 * Catalogo abstracto de clasificaciones en memoria.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 abstract class AbstractCatalog implements ClassificationsCatalog {

	private static final String THE_CLASSIFICATION_DOESNT_EXIST = "The classification %s doesn't exist.";
	private static final String CATEGORY_ALREADY_EXIST = "The category %s already exist.";
	private Set<Classification> classifications;
	private RefactoringsCatalog refactCatalog;

	public AbstractCatalog(Set<Classification> classifSet,
			RefactoringsCatalog refactCatalog) {
		this.classifications = new HashSet<Classification>(classifSet);
		this.refactCatalog = refactCatalog;
	}

	@Override
	public void addCategoryToRefactoring(String refactName,
			String classificationName, String categoryName) {
		Preconditions.checkArgument(refactCatalog.hasRefactoring(refactName));
		Preconditions.checkArgument(containsClassification(classificationName));
		Preconditions.checkArgument(getClassification(classificationName)
				.getCategories().contains(
						new Category(classificationName, categoryName)));
		final DynamicRefactoringDefinition refactoringDef = refactCatalog
				.getRefactoring(refactName);
		Set<Category> categories = refactoringDef.getCategories();
		categories.add(new Category(classificationName, categoryName));
		refactCatalog.updateRefactoring(refactName, refactoringDef.getBuilder()
				.categories(categories).build());
	}

	@Override
	public void removeClassification(String classification) {
		Preconditions.checkArgument(containsClassification(classification));
		Preconditions.checkArgument(getClassification(classification).isEditable());
		for (Category c : getClassification(classification).getCategories()) {
			removeCategory(classification, c.getName());
		}
		Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
	}

	/**
	 * Determina si existe una clasificación con el nombre pasado por parámetro.
	 * 
	 * @param name
	 *            nombre de la clasificación
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	@Override
	public final boolean containsClassification(String name) {
		for (Classification c : classifications) {
			if (c.getName().equalsIgnoreCase(name)){
				return true;
			}
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
				String.format(THE_CLASSIFICATION_DOESNT_EXIST, oldName));
		Preconditions.checkArgument(getClassification(classifName).isEditable());
		Preconditions.checkArgument(!getClassification(classifName)
				.getCategories().contains(new Category(classifName, newName)),
				String.format(CATEGORY_ALREADY_EXIST, newName));
		Classification oldClassif = getClassification(classifName);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.renameCategory(oldName, newName));
		for (DynamicRefactoringDefinition refact : refactCatalog
				.getRefactoringBelongingTo(classifName, oldName)) {
			DynamicRefactoringDefinition renamedCategory = renameRefactoringCategory(
					refact, classifName, oldName, newName);
			refactCatalog.updateRefactoring(refact.getName(), renamedCategory);
		}
	}

	@Override
	public void addCategoryToClassification(String classificationName,
			String categoryNewName) {
		Preconditions.checkArgument(containsClassification(classificationName));
		Preconditions.checkArgument(getClassification(classificationName).isEditable());
		Preconditions.checkArgument(
				!getClassification(classificationName).getCategories()
						.contains(
								new Category(classificationName,
										categoryNewName)), String.format(
						CATEGORY_ALREADY_EXIST, categoryNewName));
		Classification oldClassif = getClassification(classificationName);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.addCategory(new Category(
				classificationName, categoryNewName)));

	}

	@Override
	public void removeCategory(String classification, String categoryName) {
		Preconditions.checkArgument(containsClassification(classification));
		Preconditions.checkArgument(getClassification(classification).isEditable());
		Preconditions.checkArgument(
				getClassification(classification).getCategories().contains(
						new Category(classification, categoryName)), String
						.format(THE_CLASSIFICATION_DOESNT_EXIST,
								categoryName));
		for (DynamicRefactoringDefinition refact : refactCatalog
				.getRefactoringBelongingTo(classification, categoryName)) {
			DynamicRefactoringDefinition modifiedRefactoring = deleteRefactoringCategory(
					refact, classification, categoryName);
			refactCatalog.updateRefactoring(refact.getName(),
					modifiedRefactoring);
		}
		Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.removeCategory(new Category(
				classification, categoryName)));
	}

	@Override
	public void setDescription(String classification, String descripcion) {
		Preconditions.checkArgument(containsClassification(classification));
		Preconditions.checkArgument(getClassification(classification).isEditable());
		Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
		classifications.add(new SimpleUniLevelClassification(oldClassif
				.getName(), descripcion, oldClassif.getCategories(), oldClassif
				.isMultiCategory(), oldClassif.isEditable()));
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
	private DynamicRefactoringDefinition renameRefactoringCategory(
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

	private DynamicRefactoringDefinition updateRefactoringCategoryParent(
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
	public void renameClassification(String classification, String clasifNewName) {
		Preconditions.checkArgument(containsClassification(classification));
		Preconditions.checkArgument(getClassification(classification).isEditable());
		Preconditions.checkArgument(!containsClassification(clasifNewName));
		final Classification oldClassif = getClassification(classification);
		classifications.remove(oldClassif);
		classifications.add(oldClassif.rename(clasifNewName));
		for (Category category : oldClassif.getCategories()) {
			for (DynamicRefactoringDefinition refact : refactCatalog
					.getRefactoringBelongingTo(classification, category.getName())) {
				refactCatalog.updateRefactoring(
						refact.getName(),
						updateRefactoringCategoryParent(refact, category,
								clasifNewName));
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
	private DynamicRefactoringDefinition deleteRefactoringCategory(
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
	 * Lee las clasificaciones desde el fichero pasado.
	 * 
	 * @param classifFile
	 *            fichero de clasificaciones
	 * @param editables si las clasificaciones que se leeran del fichero se crearan como editables o no
	 * 
	 * @return conjunto de clasificaciones leidas del fichero
	 */
	protected static final Set<Classification> getClassificationsFromFile(
			String classifFile, boolean editables) {
		try {
			Set<Classification> classifications = ClassificationsReaderFactory
					.getReader().readClassifications(classifFile, editables);
			return classifications;
		} catch (ValidationException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public void addClassification(Classification classification) {
		Preconditions.checkArgument(!containsClassification(classification
				.getName()));
		classifications.add(classification);
	}

	@Override
	public void setMultiCategory(String classificationName,
			boolean isMultiCategory) {
		Preconditions.checkArgument(containsClassification(classificationName));
		Preconditions.checkArgument(getClassification(classificationName).isEditable());
		Preconditions
				.checkArgument(!(!isMultiCategory && classifHasMultiCategoryRefactorings(classificationName)));
		final Classification oldClassif = getClassification(classificationName);
		classifications.remove(oldClassif);
		classifications.add(new SimpleUniLevelClassification(oldClassif
				.getName(), oldClassif.getDescription(), oldClassif
				.getCategories(), isMultiCategory, oldClassif.isEditable()));

	}

	/**
	 * Devuelve si alguna de todas las refactorizaciones pertenece a varias
	 * categorias en la clasificacion.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @return true si al menos una de las refactorizaciones pertenece a varias
	 *         categorias en la clasificacion
	 */
	@Override
	public boolean classifHasMultiCategoryRefactorings(String classificationName) {
		Preconditions.checkArgument(containsClassification(classificationName));
		Classification classif = getClassification(classificationName);
		for (DynamicRefactoringDefinition refact : refactCatalog
				.getAllRefactorings()) {
			if (refactBelongToMultipleCategories(classif, refact)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve la lista de todas las refactorizaciones que pertenecen a varias
	 * categorias en la clasificacion.
	 * 
	 * @param classificationName
	 *            nombre de la clasificacion
	 * @return lista de clasificaciones que pertenecen a mas de una categoria en
	 *         la clasificacion
	 */
	@Override
	public Set<DynamicRefactoringDefinition> getClassifMultiCategoryRefactorings(
			String classificationName) {
		Preconditions.checkArgument(containsClassification(classificationName));
		Classification classif = getClassification(classificationName);
		Set<DynamicRefactoringDefinition> multicategoryRefactorings = new HashSet<DynamicRefactoringDefinition>();
		for (DynamicRefactoringDefinition refact : refactCatalog
				.getAllRefactorings()) {
			if (refactBelongToMultipleCategories(classif, refact)) {
				multicategoryRefactorings.add(refact);
			}
		}
		return multicategoryRefactorings;
	}

	/**
	 * Devuelve si una refactorizacion pertenece a mas de una categoria en la
	 * clasificacion.
	 * 
	 * @param classification
	 *            clasificacion
	 * @param arg0
	 *            refactorizacion a comprobar
	 * @return true si la refactorizacion pertenece a mas de una categoria de la
	 *         clasificacion
	 */
	public boolean refactBelongToMultipleCategories(
			Classification classification, DynamicRefactoringDefinition arg0) {
		return Sets.intersection(arg0.getCategories(),
				classification.getCategories()).size() > 1;
	}

}
