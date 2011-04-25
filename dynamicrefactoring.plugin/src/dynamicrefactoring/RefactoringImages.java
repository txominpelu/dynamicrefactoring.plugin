package dynamicrefactoring;

import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;

/**
 * Clase con las constantes que permiten acceder a las imagenes e iconos
 * contenidos en el plugin.
 * 
 * @author imediava
 * 
 */
public final class RefactoringImages {

	private static final String ICONS_DIR = "/icons/";

	public static final String REF_ICON_PATH = ICONS_DIR + "ref.png"; //$NON-NLS-1$
	
	public static final String PLUGIN_REF_ICON_PATH = ICONS_DIR + "plugin_ref.png"; //$NON-NLS-1$

	public static final String CLASS_ICON_PATH = ICONS_DIR + "class.gif"; //$NON-NLS-1$

	public static final String CAT_ICON_PATH = ICONS_DIR + "cat.gif"; //$NON-NLS-1$

	public static final String CHECK_ICON_PATH = ICONS_DIR + "check.gif"; //$NON-NLS-1$

	public static final String RUN_ICON_PATH = ICONS_DIR + "run.gif"; //$NON-NLS-1$

	public static final String VALIDATE_ICON_PATH = ICONS_DIR + "validate.gif"; //$NON-NLS-1$

	public static final String SEARCH_ICON_PATH = ICONS_DIR + "search.png"; //$NON-NLS-1$

	public static final String FIL_ICON_PATH = ICONS_DIR + "fil.png"; //$NON-NLS-1$

	public static final String EXPORT_PLAN_ICON_PATH = ICONS_DIR + "export_plan.ico"; //$NON-NLS-1$
	
	public static final String EXPORT_ICON_PATH = ICONS_DIR + "export.ico"; //$NON-NLS-1$
	
	public static final String IMPORT_PLAN_ICON_PATH = ICONS_DIR + "import_plan.ico"; //$NON-NLS-1$
	
	public static final String IMPORT_ICON_PATH = ICONS_DIR + "import.ico"; //$NON-NLS-1$
	
	public static final String INFO_ICON_PATH = ICONS_DIR + "info.gif"; //$NON-NLS-1$
	
	public static final String WARNING_ICON_PATH = ICONS_DIR + "warning.gif"; //$NON-NLS-1$
	
	public static final String RECURSIVE_ICON_PATH = ICONS_DIR + "recursive.gif"; //$NON-NLS-1$
	
	public static final String ERROR_ICON_PATH = ICONS_DIR + "error.gif"; //$NON-NLS-1$
	
	public static final String SI_ICON_PATH = ICONS_DIR + "siicon.png"; //$NON-NLS-1$
	
	public static final String CONFIGURE_ICON_PATH = ICONS_DIR + "configure.ico"; //$NON-NLS-1$
	
	public static final String UNDO_ICON_PATH = ICONS_DIR + "undo.ico"; //$NON-NLS-1$
	
	public static final String ARROW_UP_ICON_PATH = ICONS_DIR + "arrow_up.gif"; //$NON-NLS-1$
	
	public static final String ARROW_DOWN_ICON_PATH = ICONS_DIR + "arrow_down.gif"; //$NON-NLS-1$
	
	public static final String ARROW_RIGHT_ICON_PATH = ICONS_DIR + "arrow_right.gif"; //$NON-NLS-1$
	
	public static final String ARROW_LEFT_ICON_PATH = ICONS_DIR + "arrow_left.gif"; //$NON-NLS-1$
	
	public static final String CLEAR_ICON_PATH = ICONS_DIR + "clear.png"; //$NON-NLS-1$
	
	public static final String HELP_ICON_PATH = ICONS_DIR + "help.png"; //$NON-NLS-1$
	
	public static final String REF_VIEW_ICON_PATH = ICONS_DIR + "ref_view.png"; //$NON-NLS-1$
	
	public static final String SOURCE_ICON_PATH = ICONS_DIR + "source.gif"; //$NON-NLS-1$

	private static final String PlUGIN_CLASSIFICATION_ICON_PATH = ICONS_DIR + "plugin-classif.ico"; //$NON-NLS-1$

	/**
	 * Evita que se creen instancias del objeto.
	 */
	private RefactoringImages() {
	}

	/**
	 * Obtiene la imagen dada una ruta de un fichero de imagen en el plugin.
	 * 
	 * @param path
	 *            ruta del fichero de imagen
	 * @return imagen
	 */
	public static Image getImageForPath(String path) {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				path);
	}

	/**
	 * Obtiene el icono Refactoring.
	 * 
	 * @return imagen del icono
	 */
	public static Image getRefIcon() {
		return getImageForPath(RefactoringImages.REF_ICON_PATH);
	}

	/**
	 * Obtiene el icono Refactoring.
	 * 
	 * @return imagen del icono
	 */
	public static Image getPluginRefIcon() {
		return getImageForPath(RefactoringImages.PLUGIN_REF_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Classification.
	 * 
	 * @return imagen del icono
	 */
	public static Image getClassIcon() {
		return getImageForPath(CLASS_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono de una classification perteneciente al
	 * plugin, es decir, no editable.
	 * 
	 * @return imagen del icono
	 */
	public static Image getPluginClassificationIcon() {
		return getImageForPath(PlUGIN_CLASSIFICATION_ICON_PATH);
	}

	/**
	 * Obtiene el icono categoria.
	 * 
	 * @return imagen del icono
	 */
	public static Image getCatIcon() {
		return getImageForPath(CAT_ICON_PATH);
	}

	/**
	 * Obtiene el icono Check.
	 * 
	 * @return imagen del icono
	 */
	public static Image getCheckIcon() {
		return getImageForPath(CHECK_ICON_PATH);
	}

	/**
	 * Obtiene el icono Run.
	 * 
	 * @return imagen del icono
	 */
	public static Image getRunIcon() {
		return getImageForPath(RUN_ICON_PATH);
	}

	/**
	 * Obtiene el icono Validate.
	 * 
	 * @return imagen del icono
	 */
	public static Image getValidateIcon() {
		return getImageForPath(VALIDATE_ICON_PATH);
	}

	/**
	 * Obtiene el icono Search.
	 * 
	 * @return imagen del icono
	 */
	public static Image getSearchIcon() {
		return getImageForPath(SEARCH_ICON_PATH);
	}

	/**
	 * Obtiene el icono Filtered.
	 * 
	 * @return imagen del icono
	 */
	public static Image getFilIcon() {
		return getImageForPath(FIL_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono  Export Plan.
	 * @return imagen del icono
	 */
	public static Image getExportPlanIcon(){
		return getImageForPath(EXPORT_PLAN_ICON_PATH);
	};
	
	/**
	 * Obtiene el icono Export.
	 * 
	 * @return imagen del icono
	 */
	public static Image getExportIcon(){
		return getImageForPath(EXPORT_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Import Plan.
	 * 
	 * @return imagen del icono
	 */
	public static Image getImportPlanIcon(){
		return getImageForPath(IMPORT_PLAN_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Import.
	 * 
	 * @return imagen del icono
	 */
	public static Image getImportIcon(){
		return getImageForPath(IMPORT_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Info.
	 * 
	 * @return imagen del icono
	 */
	public static Image getInfoIcon(){
		return getImageForPath(INFO_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Warning.
	 * 
	 * @return imagen del icono
	 */
	public static Image getWarningIcon(){
		return getImageForPath(WARNING_ICON_PATH);
	}	
	
	/**
	 * Obtiene el icono Recursive.
	 * 
	 * @return imagen del icono
	 */
	public static Image getRecursiveIcon(){
		return getImageForPath(RECURSIVE_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Error.
	 * 
	 * @return imagen del icono
	 */
	public static Image getErrorIcon(){
		return getImageForPath(ERROR_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono SI.
	 * 
	 * @return imagen del icono
	 */
	public static Image getSIIcon(){
		return getImageForPath(SI_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Configure.
	 * 
	 * @return imagen del icono
	 */
	public static Image getConfigureIcon(){
		return getImageForPath(CONFIGURE_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Undo.
	 * 
	 * @return imagen del icono
	 */
	public static Image getUndoIcon(){
		return getImageForPath(UNDO_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Arrow Up.
	 * 
	 * @return imagen del icono
	 */
	public static Image getArrowUpIcon(){
		return getImageForPath(ARROW_UP_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Arrow Down.
	 * 
	 * @return imagen del icono
	 */
	public static Image getArrowDownIcon(){
		return getImageForPath(ARROW_DOWN_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Arrow Right.
	 * 
	 * @return imagen del icono
	 */
	public static Image getArrowRightIcon(){
		return getImageForPath(ARROW_RIGHT_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Arrow Left.
	 * 
	 * @return imagen del icono
	 */
	public static Image getArrowLeftIcon(){
		return getImageForPath(ARROW_LEFT_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Clear.
	 * 
	 * @return imagen del icono
	 */
	public static Image getClearIcon(){
		return getImageForPath(CLEAR_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Help.
	 * 
	 * @return imagen del icono
	 */
	public static Image getHelpIcon(){
		return getImageForPath(HELP_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Refactoring Catalgo Browser View.
	 * 
	 * @return imagen del icono
	 */
	public static Image getRefViewIcon(){
		return getImageForPath(REF_VIEW_ICON_PATH);
	}
	
	/**
	 * Obtiene el icono Source.
	 * 
	 * @return imagen del icono
	 */
	public static Image getSourceIcon(){
		return getImageForPath(SOURCE_ICON_PATH);
	}
	 
}
