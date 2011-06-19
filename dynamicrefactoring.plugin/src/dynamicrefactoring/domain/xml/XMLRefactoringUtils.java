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
import java.text.MessageFormat;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.Messages;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.xml.reader.JDOMXMLRefactoringReaderFactory;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReader;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderFactory;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderImp;

/**
 * Metodos utiles para leer el catalogo de refactorizaciones en XML. 
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class XMLRefactoringUtils {

	/**
	 * Devuelve la definición de una refactorización a partir de un fichero.
	 * 
	 * @param refactoringFilePath
	 *            ruta al fichero que define la refactorización.
	 * 
	 * @return la definición de la refactorización descrita en el fichero.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al cargar la refactorización desde el
	 *             fichero indicado.
	 */
	public static DynamicRefactoringDefinition getRefactoringDefinition(
			String refactoringFilePath) throws RefactoringException {
	
		DynamicRefactoringDefinition definition;
		try {
			XMLRefactoringReaderFactory f = new JDOMXMLRefactoringReaderFactory();
			XMLRefactoringReaderImp implementor = f
					.makeXMLRefactoringReaderImp();
			XMLRefactoringReader temporaryReader = new XMLRefactoringReader(
					implementor);
			definition = temporaryReader
					.getDynamicRefactoringDefinition(new File(
							refactoringFilePath));
		} catch (Exception e) {
			Object[] messageArgs = { refactoringFilePath };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.DynamicRefactoringDefinition_ErrorLoading);
	
			throw new RefactoringException(formatter.format(messageArgs)
					+ ".\n" + //$NON-NLS-1$
					e.getMessage(), e);
		}
		return definition;
	}

}
