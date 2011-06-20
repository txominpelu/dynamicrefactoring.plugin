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

import java.util.Set;

/**
 * Catálogo que contiene refactorizaciones
 * y permite consultarlas o modificarlas.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public interface RefactoringsCatalog {

	/**
	 * Obtiene la refactorizacion del catalogo cuyo nombre es el pasado.
	 * 
	 * Saltara {@link IllegalArgumentException} si
	 * {@link #hasRefactoring(String)} devuelve falso para el nombre pasado, es
	 * decir, si no existe una refactorizacion con dicho nombre en el catalogo.
	 * 
	 * @param refactName
	 *            nombre de la refactorización
	 * @return definicion de la refactorizacion
	 */
	DynamicRefactoringDefinition getRefactoring(String refactName);

	/**
	 * Obtiene si existe una refactorizacion con el nombre
	 * pasado en el catalogo.
	 * 
	 * @param name nombre de la refactorizacion a comprobar
	 * @return verdadero si existe una refactorizacion con dicho nombre en el catalogo
	 */
	boolean hasRefactoring(String name);

	/**
	 * Actualiza los datos de la refactorizacion en el catálogo.
	 * 
	 * Basicamente reemplaza la refactorizacion con el mismo nombre que la
	 * actual por la que se pasa.
	 * 
	 * Saltara IlegalArgumentException si la refactorizacion no existe en el
	 * catalogo.
	 * 
	 * @param oldRefactoringName
	 *            nombre de la refactorizacion a modificar
	 * @param refactoring
	 *            refactorizacion a actualizar
	 */
	void updateRefactoring(String oldRefactoringName,
			DynamicRefactoringDefinition refactoring);

	/**
	 * Agrega la siguiente refactorizacion al catalogo.
	 * 
	 * Saltara IlegalArgumentException si una refactorizacion con el mismo
	 * nombre ya existe en el catalogo.
	 * 
	 * @param refactoring
	 *            refactorizacion a agregar
	 */
	void addRefactoring(DynamicRefactoringDefinition refactoring);

	/**
	 * Obtiene el conjunto de todas las refactorizaciones contenidas
	 * en el catalogo.
	 * 
	 * @return conjunto de refactorizaciones contenidas en el catalogo
	 */
	Set<DynamicRefactoringDefinition> getAllRefactorings();

	/**
	 * Elimina la refactorizacion con el nombre dado del catalogo.
	 * 
	 * Saltara IlegalArgumentException si no existe la refactorizacion en el
	 * catalogo.
	 * 
	 * @param refactoringName
	 *            nombre de la refactorizacion a eliminar
	 */
	void removeRefactoring(String refactoringName);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus tipos de entrada el indicado por el parámetro.
	 * 
	 * @param inputType tipo de entrada
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsInputType(String inputType);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene como tipo de entrada raiz el indicado por el parámetro.
	 * 
	 * @param rootInputType tipo de entrada de la entrada raiz
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsRootInputType(String rootInputType);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus precondiciones la precondición indicada por el parámetro.
	 * 
	 * @param precondition precondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsPrecondition(String precondition);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus acciones la acción indicada por el parámetro.
	 * 
	 * @param action acción
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsAction(String action);
	
	/**
	 * Obtiene un subconjunto de refactorizaciones del catálogo que 
	 * tiene entre sus postcondiciones la postcondición indicada por el parámetro.
	 * 
	 * @param postcondition postcondición
	 * @return subconjunto de refactorizaciones del catálogo
	 */
	Set<DynamicRefactoringDefinition> getRefactoringsContainsPostcondition(String postcondition);
	

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
	Set<DynamicRefactoringDefinition> getRefactoringBelongingTo(
			String classification, String categoryName);
	

}
