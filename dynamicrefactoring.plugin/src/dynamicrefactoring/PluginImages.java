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
public class PluginImages {

	public static final String REF_PNG_ICON_PATH = "icons" + System.getProperty("file.separator") + "ref.png";

	public static final String CLASS_GIF_ICON_PATH = "icons" + System.getProperty("file.separator") + "class.gif";

	public static Image getRefPngIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				PluginImages.REF_PNG_ICON_PATH);
	}
	
	public static Image getClassGifIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				CLASS_GIF_ICON_PATH);
	}

	
	

}
