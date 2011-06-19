/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.util.Messages;
import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.util.io.filter.ClassFilter;

/**
 * Tipo de elemento de los que componen los
 * pasos de una refactorizacion.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public enum RefactoringMechanismType {
	

	/**
	 * Precondiciones que se deben cumplir antes de ejecutar una refactorización.
	 */
	PRECONDITION(RefactoringConstants.JAVA_PREDICATES_PACKAGE,
			RefactoringConstants.PREDICATES_PACKAGE), 
			/**
			 * Acción de las que conforman una refactorización.
			 */
	ACTION(RefactoringConstants.JAVA_ACTIONS_PACKAGE,
			RefactoringConstants.ACTIONS_PACKAGE), 
			/**
			 * Requisito que debe cumplir el código tras aplicar la refactorización.
			 */
	POSTCONDITION(RefactoringConstants.JAVA_PREDICATES_PACKAGE,
			RefactoringConstants.PREDICATES_PACKAGE);
	
	/**
	 * Nombre completamente cualificado del paquete que contiene las acciones
	 * del repositorio dependientes de Java.
	 */
	public static final String JAVA_ACTIONS_PACKAGE = 
		"repository.java.concreteaction."; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del paquete que contiene los predicados
	 * del repositorio dependientes de Java.
	 */
	public static final String JAVA_PREDICATES_PACKAGE = 
		"repository.java.concretepredicate."; //$NON-NLS-1$

	private final String javaPackage;
	private final String moonPackage;

	private RefactoringMechanismType(String javaPackage, String moonPackage) {
		this.javaPackage = javaPackage;
		this.moonPackage = moonPackage;
	}

	/**
	 * Obtiene la ruta del directorio raíz bajo el cual se encuentran las clases
	 * de los predicados concretos del repositorio.
	 * 
	 * @return la ruta del directorio raíz bajo el cual se encuentran las clases
	 *         de los predicados concretos del repositorio.
	 */
	public String getElementIndependentDir() {
		return RefactoringConstants.REFACTORING_CLASSES_DIR
				+ getMechanismIndependentPackage().replace(".", File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene la ruta del directorio raíz bajo el cual se encuentran las clases
	 * de los predicados concretos del repositorio.
	 * 
	 * @return la ruta del directorio raíz bajo el cual se encuentran las clases
	 *         de los predicados concretos del repositorio.
	 */
	public String getElementJavaDir() {
		return RefactoringConstants.REFACTORING_CLASSES_DIR
				+ getMechanismJavaPackage().replace(".", File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene el nombre del paquete independiente de una pre/poscondicion o de
	 * una accion.
	 * 
	 * @param type
	 *            si es RefactoringConstant.PRECONDITION /POSTCONDITION/ACTION
	 * @param mechanismName
	 *            mechanism name
	 * @return package name (Ej. "repository.concreteaction")
	 */
	public String getMechanismIndependentPackage() {
		return moonPackage.substring(0, moonPackage.length() - 1);
	}

	/**
	 * Obtiene el nombre del paquete java de una pre/poscondicion o de una
	 * accion.
	 * 
	 * @param type
	 *            si es RefactoringConstant.PRECONDITION /POSTCONDITION/ACTION
	 * @param mechanismName
	 *            mechanism name
	 * @return package name (Ej. "repository.concreteaction")
	 */
	public String getMechanismJavaPackage() {
		return javaPackage.substring(0, javaPackage.length() - 1);
	}

	/**
	 * Obtiene el conjunto de todos los elementos encontrados en los directorios
	 * por defecto del repositorio, de tipo java.
	 * 
	 * @return el conjunto de todos los predicados encontrados en los
	 *         directorios por defecto de predicados del repositorio,
	 *         dependientes o independientes del lenguaje.
	 * 
	 * @throws IOException
	 *             si no se consigue acceder a alguno de los directorios de
	 *             predicados.
	 */
	public Map<String, String> getElementJavaList() {
		return getElementList(getElementJavaDir());
	}

	/**
	 * Obtiene el conjunto de todos los elementos encontrados en los directorios
	 * por defecto del repositorio, independientes del lenguaje.
	 * 
	 * @return el conjunto de todos los predicados encontrados en los
	 *         directorios por defecto de predicados del repositorio,
	 *         dependientes o independientes del lenguaje.
	 * 
	 * @throws IOException
	 *             si no se consigue acceder a alguno de los directorios de
	 *             predicados.
	 */
	public Map<String, String> getElementIndependentList() {
		return getElementList(getElementIndependentDir());
	}

	/**
	 * Lista todos los predicados.
	 * 
	 * @return todos los predicados
	 * @throws IOException
	 */
	public static Map<String, String> getPredicatesAllList() {
		return PRECONDITION.getElementAllList();
	}

	/**
	 * Obtiene si un predicado es dependiente de java.
	 * @param predicateName  nombre del predicado
	 * 
	 * @return todos los predicados
	 * @throws IOException
	 */
	public static boolean isPredicateJavaDependent(String predicateName) {
		return PRECONDITION.isElementJavaDependent(predicateName);
	}

	/**
	 * Obtiene el conjunto de todos los elementos encontrados en los directorios
	 * por defecto de predicados del repositorio, dependientes o independientes
	 * del lenguaje.
	 * 
	 * @return el conjunto de todos los predicados encontrados en los
	 *         directorios por defecto de predicados del repositorio,
	 *         dependientes o independientes del lenguaje.
	 * 
	 * @throws IOException
	 *             si no se consigue acceder a alguno de los directorios de
	 *             predicados.
	 */
	public Map<String, String> getElementAllList() {
		Map<String, String> allElements = getElementIndependentList();
		allElements.putAll(getElementJavaList());
		return allElements;
	}

	/**
	 * Determina si un nombre de elemento corresponde con un elemento del
	 * repositorio dependiente de Java.
	 * 
	 * @param elementName
	 *            nombre de la elemento cuya dependencia del lenguaje se quiere
	 *            determinar.
	 * 
	 * @return <code>true</code> si la elemento es dependiente de Java;
	 *         <code>false
	 * </code> en caso contrario.
	 */
	public boolean isElementJavaDependent(String elementName) {
		Map<String, String> javaPredicates = getElementJavaList();
		return javaPredicates.containsKey(elementName);
	}

	/**
	 * Obtiene un conjunto de pares nombre-fichero de los ficheros con acciones
	 * o predicados disponibles en un directorio.
	 * 
	 * @param sourceDir
	 *            ruta del directorio en que se buscarán los elementos del
	 *            repositorio.
	 * 
	 * @return una tabla en la que se usa como índice el nombre comprensible del
	 *         fichero y como contenido la ruta del fichero; para cada fichero
	 *         encontrado.
	 * 
	 * @throws IOException
	 *             cuando no existe el directorio, o bien no es un directorio.
	 */
	private Map<String, String> getElementList(String sourceDir) {
		File dir = new File(sourceDir);
		Preconditions.checkArgument(dir.exists(), Messages.RepositoryElementLister_RepositoryDirNotExists
				+ ".\n");
		Preconditions.checkArgument(dir.isDirectory(), Messages.RepositoryElementLister_InvalidElementsPath
				+ ".\n");
		HashMap<String, String> h = new HashMap<String, String>();
		listFiles(dir, h);

		return h;
	}

	/**
	 * Genera una tabla con los ficheros de un directorio.
	 * 
	 * @param dir
	 *            el directorio raíz desde donde se comienza el listado.
	 * @param h
	 *            una tabla con el nombre del fichero como clave y la ruta al
	 *            mismo como contenido. Se usa como valor de retorno.
	 */
	private void listFiles(File dir, Map<String, String> h) {
		final ClassFilter fileFilter = new ClassFilter();
		// Si es un directorio se continúa recursivamente.
		if (dir.isDirectory()) {
			for (String hijo : dir.list()) {
				listFiles(new File(dir, hijo), h);
			}
		}
		// Si es un fichero de refactorización se almacena en la tabla.
		else if (fileFilter.accept(dir, dir.getName())) {
			h.put(FileManager.getFilePathWithoutExtension(dir.getName()),
					dir.getName());
		}
	}

}
