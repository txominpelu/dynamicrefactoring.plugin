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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassificationsCatalog;
import dynamicrefactoring.domain.xml.XMLRefactoringsCatalog;

/**
 * Almacen con todas las clasificaciones leidas del fichero de configuracion xml
 * y las refactorizaciones. Proporciona metodos para acceder a las
 * clasificaciones y refactorizaciones del plugin y para modificar los y guardar
 * los cambios en sus ficheros xml correspondientes.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class PluginClassificationsCatalog extends AbstractCatalog
		implements ClassificationsCatalog {

	private static PluginClassificationsCatalog instance;

	/**
	 * Nombre de la clasificacion por ambito de una refactorización.
	 */
	public static final String SCOPE_CLASSIFICATION = "Scope";

	/**
	 * Ruta al fichero de clasificaciones definidas por el plugin.
	 */
	protected static final String PLUGIN_CLASSIFICATION_TYPES_FILE = RefactoringPlugin
			.getCommonPluginFilesDir()
			+ File.separator
			+ "temp"
			+ File.separator
			+ "Classification"
			+ File.separator
			+ "classifications.xml";

	/**
	 * Ruta al fichero de clasificaciones definidas por el plugin.
	 */
	protected static final String USER_CLASSIFICATION_TYPES_FILE = RefactoringPlugin
			.getCommonPluginFilesDir()
			+ File.separator
			+ "Classification"
			+ File.separator + "user-classifications.xml";

	/**
	 * Crea el almacen y lee las clasificaciones del fichero de clasificaciones
	 * xml.
	 */
	private PluginClassificationsCatalog() {
		this(readAllClassificationsFromFiles(), XMLRefactoringsCatalog
				.getInstance());
		Collections.sort(new ArrayList<Classification>(super
				.getAllClassifications()));
	}

	/**
	 * Lee las clasificaciones del usuario y del plugin de sus respectivos
	 * ficheros xml y devuelve la union de ambas.
	 * 
	 * @return conjunto que contiene las clasificaciones de usuario y del plugin
	 */
	protected static Set<Classification> readAllClassificationsFromFiles() {
		final Set<Classification> pluginClassifications = AbstractCatalog
				.getClassificationsFromFile(
						PluginClassificationsCatalog.PLUGIN_CLASSIFICATION_TYPES_FILE,
						false);
		final Set<Classification> userClassifications = AbstractCatalog
				.getClassificationsFromFile(
						PluginClassificationsCatalog.USER_CLASSIFICATION_TYPES_FILE,
						true);
		return Sets.union(pluginClassifications, userClassifications);
	}

	/**
	 * Permite crear un catalogo de clasificaciones a partir de las
	 * clasificaciones pasadas y de un catalogo de refactorizaciones.
	 * 
	 * @param classifSet
	 *            conjunto de clasificaciones
	 * @param refactCatalog
	 *            catalogo de refactorizaciones
	 */
	protected PluginClassificationsCatalog(Set<Classification> classifSet,
			RefactoringsCatalog refactCatalog) {
		super(classifSet, refactCatalog);
	}

	@Override
	public void removeClassification(String classification) {
		// No se puede eliminar la clasificacion Scope
		Preconditions.checkArgument(!classification
				.equals(SCOPE_CLASSIFICATION));
		super.removeClassification(classification);
		updateClassificationsFile();
	}

	/**
	 * Tambien se ocupa de actualizar el fichero de clasificaciones a parte de
	 * la funcionalidad de gestion de clasificaciones en memoria.
	 */
	@Override
	public void addCategoryToClassification(String classifName, String name) {
		super.addCategoryToClassification(classifName, name);
		updateClassificationsFile();
	}

	/**
	 * Tambien se ocupa de actualizar el fichero de clasificaciones y de los
	 * ficheros xml de refactorizaciones a parte de la funcionalidad de gestion
	 * en memoria.
	 */
	@Override
	public void removeCategory(String classifName, String name) {
		super.removeCategory(classifName, name);
		updateClassificationsFile();
	}

	/**
	 * Se encarga de renombrar la categoria y actualizar las definiciones en
	 * ficheros xml de las refactorizaciones que pertenecen a la categoria.
	 * 
	 */
	@Override
	public void renameCategory(String classifName, String oldName,
			String newName) {
		super.renameCategory(classifName, oldName, newName);
		updateClassificationsFile();
	}

	/**
	 * Actualiza el contenido del fichero de configuracion de clasificaciones de
	 * acuerdo con el contenido de las clasificaciones de usuario del catalogo
	 * (recordar que las del plugin no son editables).
	 */
	private void updateClassificationsFile() {
		try {
			JAXBClassificationsWriter
					.getInstance()
					.saveClassificationsToXml(
							getUserClassifications(),
							PluginClassificationsCatalog.USER_CLASSIFICATION_TYPES_FILE);
		} catch (FileNotFoundException e) {
			// El fichero debe existir sino seria error de programacion
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Devuelve todas las clasificaciones que son de usuario, es decir, que se
	 * pueden editar.
	 * 
	 * @return devuelve las clasificaciones del catalogo que son de usuario y
	 *         por tanto se pueden editar
	 */
	private Set<Classification> getUserClassifications() {
		return Sets.filter(getAllClassifications(),
				new Predicate<Classification>() {

					@Override
					public boolean apply(Classification arg0) {
						return arg0.isEditable();
					}

				});
	}

	/**
	 * Obtiene la instancia del almacen con las clasificaciones disponibles.
	 * 
	 * @return instancia del almacen
	 */
	public static PluginClassificationsCatalog getInstance() {
		if (instance == null) {
			instance = new PluginClassificationsCatalog();
		}
		return instance;
	}

	/**
	 * Determina si existe una clasificacion que contenga a la categoria
	 * indicada por parámetro.
	 * 
	 * @param cat
	 *            categoria
	 * @return Verdadero en caso de que exista. En caso contrario, falso.
	 */
	public boolean containsCategoryClassification(Category cat) {
		return containsClassification(cat.getParent())
				&& getClassification(cat.getParent()).containsCategory(cat);
	}

	@Override
	public void renameClassification(String clasifName, String clasifNewName) {
		Preconditions.checkArgument(!clasifName.equals(SCOPE_CLASSIFICATION));
		super.renameClassification(clasifName, clasifNewName);
		updateClassificationsFile();
	}

	@Override
	public void addClassification(Classification classification) {
		super.addClassification(classification);
		updateClassificationsFile();
	}

	@Override
	public void setMultiCategory(String classification, boolean isMultiCategory) {
		super.setMultiCategory(classification, isMultiCategory);
		updateClassificationsFile();
	}

	@Override
	public void setDescription(String classification, String description) {
		super.setDescription(classification, description);
		updateClassificationsFile();
	}

}
