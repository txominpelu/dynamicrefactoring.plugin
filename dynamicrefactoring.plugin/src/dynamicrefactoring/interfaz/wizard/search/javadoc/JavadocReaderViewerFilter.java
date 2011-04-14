package dynamicrefactoring.interfaz.wizard.search.javadoc;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filtro para que el proyecto que se crea para obtener 
 * la documentación de las clases no se muestre al usuario.
 * 
 * @author imediava
 *
 */
public class JavadocReaderViewerFilter extends ViewerFilter {
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof IJavaProject){
			return !((IJavaProject) element).getElementName().equals(EclipseBasedJavadocReader.JAVADOC_READER_PROJECT_NAME);
		}
		return true;
	}

}
