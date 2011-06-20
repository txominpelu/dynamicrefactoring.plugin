/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard.classificationscombo;

import java.util.Arrays;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;

import com.google.common.collect.Sets;

import dynamicrefactoring.domain.metadata.interfaces.Classification;

/**
 * Comprueba los cambios que se producen cuando alguna de las categorias es
 * marcada. Evita que varias categorias sean seleccionadas cuando la
 * clasificacion no es multicategoria.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 * 
 */
class ClassificationsCheckBoxTreeListener implements
		ICheckStateListener {

	/**
	 * Clasificaciones disponibles.
	 */
	private final Set<Classification> availableClassifications;

	/**
	 * Constructor.
	 * @param availableClassifications clasificaciones disponibles
	 */
	ClassificationsCheckBoxTreeListener(
			Set<Classification> availableClassifications) {
		this.availableClassifications = availableClassifications;
	}

	/**
	 * Evento de cambio de estado.
	 * 
	 * Si el evento corresponde a una clasificacion
	 * {@link dealWithParentCheckStateChange} se ocupa de tratarlo.
	 * 
	 * Si el evento corresponde a una categoria solo permitira marcar una si
	 * su padre no permite refactorizaciones que pertenecen a multiples
	 * categorias (refact. multicategoria).
	 * 
	 * @param event evento de cambio
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		Object elemento = event.getElement();
		CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
		if (PickCategoryTree.isParentElement(elemento)) {
			dealWithParentCheckStateChange(event,
					(Classification) elemento, viewer);
		} else if (PickCategoryTree.isChildElement(elemento)
				&& !PickCategoryTree.getParent(elemento,
						this.availableClassifications).isMultiCategory()
				&& event.getChecked()) {
			viewer.setSubtreeChecked(PickCategoryTree.getParent(elemento,
					this.availableClassifications), false);
			viewer.setChecked(elemento, true);
		}
		// Obtiene el elemento si es una clasificacion o su clasif. padre si
		// es una categoria
		Object classification = (PickCategoryTree.isParentElement(elemento) ? elemento
				: PickCategoryTree.getParent(elemento,
						availableClassifications));
		grayParentIfNeeded((Classification) classification, viewer);
	}

	/**
	 * Gestiona un evento en el que una clasificacion ha sido
	 * marcada/desmarcada.
	 * 
	 * Si la clasificacion es multicategoria aplica a sus categorias el
	 * mismo estado que se le ha aplicado a ella.
	 * 
	 * Si no es multicategoria se deja el arbol como estaba antes del
	 * evento.
	 * 
	 * @param event
	 *            evento
	 * @param classification
	 *            clasification objeto del evento
	 * @param viewer visor
	 */
	private void dealWithParentCheckStateChange(
			CheckStateChangedEvent event, Classification classification,
			CheckboxTreeViewer viewer) {
		if (classification.isMultiCategory()) {
			viewer.setSubtreeChecked(classification, event.getChecked());
		} else if (viewer.getGrayed(classification)){
			viewer.setSubtreeChecked(classification, false);
		}
	}

	/**
	 * Oscurece el padre si alguno de sus hijos esta marcado. Es decir
	 * oscurece la clasificacion si alguna de sus categorias esta marcada.
	 * 
	 * @param classification
	 *            elemento padre
	 * @param viewer visor
	 */

	protected static void grayParentIfNeeded(Classification classification,
			CheckboxTreeViewer viewer) {
		// Si alguno de los hijos de la clasificacion tiene algun hijo marcado
		if (!Sets
				.intersection(
						classification.getCategories(),
						Sets.newHashSet(Arrays.asList(viewer
								.getCheckedElements()))).isEmpty()) {
			// El padre se oscurecera
			viewer.setGrayChecked(classification, true);
		} else {
			viewer.setGrayChecked(classification, false);
		}
	}
}