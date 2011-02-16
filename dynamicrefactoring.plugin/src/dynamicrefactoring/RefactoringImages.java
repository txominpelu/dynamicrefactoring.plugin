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

	public static final String REF_ICON_PATH = ICONS_DIR + "ref.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String CLASS_ICON_PATH = ICONS_DIR + "class.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String CAT_ICON_PATH = ICONS_DIR + "cat.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String CHECK_ICON_PATH = ICONS_DIR + "check.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String RUN_ICON_PATH = ICONS_DIR + "run.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String VALIDATE_ICON_PATH = ICONS_DIR + "validate.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String SPLIT_ICON_PATH = ICONS_DIR + "split.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String SPLIT_R_ICON_PATH = ICONS_DIR + "split_r.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String SPLIT_L_ICON_PATH = ICONS_DIR + "split_l.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String SEARCH_ICON_PATH = ICONS_DIR + "search.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String FIL_ICON_PATH = ICONS_DIR + "fil.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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
	public final static Image getImageForPath(String path) {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				path);
	}

	/**
	 * Obtiene el icono referencia.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getRefIcon() {
		return getImageForPath(RefactoringImages.REF_ICON_PATH);
	}

	/**
	 * Obtiene el icono class.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getClassIcon() {
		return getImageForPath(CLASS_ICON_PATH);
	}

	/**
	 * Obtiene el icono cat.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getCatIcon() {
		return getImageForPath(CAT_ICON_PATH);
	}

	/**
	 * Obtiene el icono Check.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getCheckIcon() {
		return getImageForPath(CHECK_ICON_PATH);
	}

	/**
	 * Obtiene el icono Run.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getRunIcon() {
		return getImageForPath(RUN_ICON_PATH);
	}

	/**
	 * Obtiene el icono Validate.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getValidateIcon() {
		return getImageForPath(VALIDATE_ICON_PATH);
	}

	/**
	 * Obtiene el icono splitI.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getSplitIcon() {
		return getImageForPath(SPLIT_ICON_PATH);
	}

	/**
	 * Obtiene el icono SplitRI.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getSplitRIcon() {
		return getImageForPath(SPLIT_R_ICON_PATH);
	}

	/**
	 * Obtiene el icono splitLI.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getSplitLIcon() {
		return getImageForPath(SPLIT_L_ICON_PATH);
	}

	/**
	 * Obtiene el icono search.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getSearchIcon() {
		return getImageForPath(SEARCH_ICON_PATH);
	}

	/**
	 * Obtiene el icono fil.
	 * 
	 * @return imagen del icono
	 */
	public final static Image getFilIcon() {
		return getImageForPath(FIL_ICON_PATH);
	}
}
