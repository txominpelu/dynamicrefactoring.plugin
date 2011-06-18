/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.editor.classifeditor;

import org.eclipse.osgi.util.NLS;

/**
 * Cadenas preparadas para ser internacionalizadas del editor de
 * Clasificaciones.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public class Messages extends NLS {
	private static final String BUNDLE_NAME = "dynamicrefactoring.interfaz.editor.classifeditor.messages"; //$NON-NLS-1$
	public static String CategoriesSection_AddCategory;
	public static String CategoriesSection_Categories;
	public static String CategoriesSection_CategoriesForTheClassification;
	public static String CategoriesSection_CategoryAlreadyExists;
	public static String CategoriesSection_DeletingCategory;
	public static String CategoriesSection_DeletingCategoryWarning;
	public static String CategoriesSection_EnterCategoryName;
	public static String CategoriesSection_EnterNewCategoryName;
	public static String CategoriesSection_RenamingCategory;
	public static String ClassificationDataSection_CannotBeUniCategory;
	public static String ClassificationDataSection_CannotBeUniCategoryErrorDescription;
	public static String ClassificationDataSection_Description;
	public static String ClassificationDataSection_EnterNewDescription;
	public static String ClassificationDataSection_Modify;
	public static String ClassificationDataSection_ModifyDescription;
	public static String ClassificationDataSection_Multi;
	public static String ClassificationDataSection_Name;
	public static String ClassificationDataSection_SelectedClassification;
	public static String ClassificationDataSection_SelectedClassificationData;
	public static String ClassificationsEditorView_FormText1;
	public static String ClassifListSection_Add;
	public static String ClassifListSection_AddClassification;
	public static String ClassifListSection_Cancel;
	public static String ClassifListSection_ClassificationAlreadyExists;
	public static String ClassifListSection_Classifications;
	public static String ClassifListSection_Delete;
	public static String ClassifListSection_DeleteClassification;
	public static String ClassifListSection_DeleteClassificationToolTip;
	public static String ClassifListSection_DeletingClassificationWarning;
	public static String ClassifListSection_EnterNewClassificationName;
	public static String ClassifListSection_PickClassificationFromList;
	public static String ClassifListSection_Proceed;
	public static String ClassifListSection_Rename;
	public static String ClassifListSection_RenameClassificationToolTip;
	public static String ClassifListSection_RenamingClassification;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
