/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.IUpdate;

/**
 * Implementa una acción para el visor de texto.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class TextViewerAction extends Action implements IUpdate {
	/**
	 * Código correspondiente a la operación de texto.
	 */
	private int operationCode=-1;
	
	/**
	 * Objetivo sobre el que se realizan las operaciones de texto.
	 */
	private ITextOperationTarget operationTarget;

	/**
	 * Define el objetivo sobre el que se realiza la operación de texto,
	 * asi como el código correspondiente a dicha operación.
	 * Además se realiza la actualización del mismo.
	 * 
	 * @param textViewer visor de texto
	 * @param operationCode código de la operación
	 */
	public TextViewerAction(ITextViewer textViewer, int operationCode) {
		this.operationCode = operationCode;
		operationTarget = textViewer.getTextOperationTarget();
		update();
	}
	
	/**
	 * Ejecuta la acción indicada por el código de operación y 
	 * la realiza sobre el objetivo de las operaciones de texto.
	 */
	@Override
	public void run() {
		if (operationCode != -1 && operationTarget != null) {
			operationTarget.doOperation(operationCode);
		}
	}

	/**
	 * Solicita la actualización del propio objeto.
	 */
	@Override
	public void update() {
		boolean wasEnabled = isEnabled();
		boolean isEnabled = (operationTarget != null && operationTarget.canDoOperation(operationCode));
		setEnabled(isEnabled);
		if(wasEnabled != isEnabled) {
			firePropertyChange(ENABLED, wasEnabled ? Boolean.TRUE : Boolean.FALSE, isEnabled ? Boolean.TRUE : Boolean.FALSE);
		}
	}
}
