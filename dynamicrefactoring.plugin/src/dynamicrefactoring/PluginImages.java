package dynamicrefactoring;

import java.io.File;
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
	
	/**
	 * Evita que creemos instancias de la clase.
	 */
	private PluginImages(){}

	public static final String ICONS_DIR = "icons";

	public static final String REF_PNG_ICON_PATH = ICONS_DIR + File.separator + "ref.png";

	public static final String CLASS_GIF_ICON_PATH = ICONS_DIR + File.separator  + "class.gif";


	public static Image getRefPngIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				PluginImages.REF_PNG_ICON_PATH);
	}
	
	public static Image getClassGifIcon() {
		return ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				CLASS_GIF_ICON_PATH);
	}

}
