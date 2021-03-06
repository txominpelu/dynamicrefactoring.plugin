/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.domain.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.AbstractRefactoringsCatalog;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringsCatalog;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.util.DynamicRefactoringLister;

/**
 * Catalogo de refactorizaciones que lee las refactorizaciones del directorio de
 * refactorizaciones y cuyos cambios se ven reflejados en los ficheros xml de
 * dicho directorio.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public final class XMLRefactoringsCatalog extends AbstractRefactoringsCatalog
		implements RefactoringsCatalog {

	/**
	 * Instancia del catálogo.
	 */
	private static XMLRefactoringsCatalog instance;

	/**
	 * Construye el catalogo a partir de un conjunto de refactorizaciones
	 * definidas.
	 * 
	 * Uso exclusivo para Tests. En cualquier otro caso debe accederse a
	 * {@link #getInstance()} para utilizar la funcionalidad del catalogo.
	 * 
	 * @param refactorings
	 *            conjunto de refactorizaciones
	 */
	protected XMLRefactoringsCatalog(
			Set<DynamicRefactoringDefinition> refactorings) {
		super(refactorings);
	}

	/**
	 * Obtiene la instancia del catalogo.
	 * 
	 * @return instancia del catalogo
	 */
	public static RefactoringsCatalog getInstance() {
		if (instance == null) {

			instance = new XMLRefactoringsCatalog(
					Sets.union(
							getRefactoringsFromDir(
									RefactoringPlugin.getNonEditableDynamicRefactoringsDir(),
									false),
							getRefactoringsFromDir(
									RefactoringPlugin.getDynamicRefactoringsDir(), 
									true)
					)
			);

		}
		return instance;
	}

	/**
	 * Actualiza una refactorizacion en el catalogo y su fichero de definicion
	 * xml.
	 */
	@Override
	public void updateRefactoring(String oldRefactoringName,
			DynamicRefactoringDefinition refactoring) {
		Preconditions.checkArgument(hasRefactoring(oldRefactoringName));
		DynamicRefactoringDefinition refactoringToRemove = getRefactoring(oldRefactoringName);
		super.removeRefactoring(oldRefactoringName);
		addRefactoring(refactoring);
		if (!oldRefactoringName.equals(refactoring.getName())) {
			XMLRefactoringsFileUtils.deleteRefactoringDir(refactoringToRemove);
		}

	}

	@Override
	public void removeRefactoring(String refactoringName) {
		Preconditions.checkArgument(hasRefactoring(refactoringName));
		final DynamicRefactoringDefinition refactoring = getRefactoring(refactoringName);
		super.removeRefactoring(refactoringName);
		XMLRefactoringsFileUtils.deleteRefactoringDir(refactoring);
	}

	/**
	 * Agrega una refactorizacion al catalogo y crea su fichero de definicion
	 * xml.
	 */
	@Override
	public void addRefactoring(final DynamicRefactoringDefinition refactoring) {
		super.addRefactoring(new AddRefactoringObject(refactoring.getBuilder().isEditable(true).build())
				.addRefactoring());
	}

	/**
	 * Lee las refactorizaciones existentes en el directorio pasado.
	 * 
	 * @param editables
	 *            si las refactorizaciones leidas seran editables o no
	 * @param dir
	 *            directorio contenedor de las refactorizaciones
	 * @return conjunto de refactorizaciones leidas del directorio
	 */
	protected static Set<DynamicRefactoringDefinition> getRefactoringsFromDir(
			String dir, boolean editables) {
		try {
			HashMap<String, String> allRefactorings = DynamicRefactoringLister
					.getInstance().getDynamicRefactoringNameList(dir, true, null);
			
			Set<DynamicRefactoringDefinition> refactorings = new HashSet<DynamicRefactoringDefinition>();
			
			JDOMXMLRefactoringReaderImp reader = new JDOMXMLRefactoringReaderImp();
			for (String refactFile : allRefactorings.values()) {
				refactorings.add(reader
						.getDynamicRefactoringDefinition(new File(refactFile))
						.getBuilder().isEditable(editables).build());
			}
			return refactorings;
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
	
}
