package dynamicrefactoring;

import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;

/**
 * Clase con las constantes que permiten acceder a las imagenes e 
 * iconos contenidos en el plugin.
 * 
 * @author imediava
 *
 */
public class RefactoringImages {

	public static final String REF_ICON_PATH = "icons" + System.getProperty("file.separator") + "ref.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String CLASS_ICON_PATH = "icons" + System.getProperty("file.separator") + "class.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String CAT_ICON_PATH = "icons" + System.getProperty("file.separator") + "cat.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String CHECK_ICON_PATH = "icons" + System.getProperty("file.separator") + "check.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String RUN_ICON_PATH = "icons" + System.getProperty("file.separator") + "run.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String VALIDATE_ICON_PATH = "icons" + System.getProperty("file.separator") + "validate.gif"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String SPLIT_ICON_PATH = "icons" + System.getProperty("file.separator") + "split.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String SPLIT_R_ICON_PATH = "icons" + System.getProperty("file.separator") + "split_r.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String SPLIT_L_ICON_PATH = "icons" + System.getProperty("file.separator") + "split_l.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String SEARCH_ICON_PATH = "icons" + System.getProperty("file.separator") + "search.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	public static final String FIL_ICON_PATH = "icons" + System.getProperty("file.separator") + "fil.png"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	
	public static Image getRefIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				RefactoringImages.REF_ICON_PATH);
	}
	
	public static Image getClassIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				CLASS_ICON_PATH);
	}

	public static Image getCatIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				CAT_ICON_PATH);
	}
	
	public static Image getCheckIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				CHECK_ICON_PATH);
	}

	public static Image getRunIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				RUN_ICON_PATH);
	}
	
	public static Image getValidateIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				VALIDATE_ICON_PATH);
	}
	
	public static Image getSplitIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				SPLIT_ICON_PATH);
	}
	
	public static Image getSplitRIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				SPLIT_R_ICON_PATH);
	}
	
	public static Image getSplitLIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				SPLIT_L_ICON_PATH);
	}
	
	public static Image getSearchIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				SEARCH_ICON_PATH);
	}
	
	public static Image getFilIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				FIL_ICON_PATH);
	}
}
