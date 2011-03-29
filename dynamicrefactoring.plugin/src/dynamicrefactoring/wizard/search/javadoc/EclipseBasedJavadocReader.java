package dynamicrefactoring.wizard.search.javadoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.jsoup.Jsoup;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;

public enum EclipseBasedJavadocReader implements JavadocReader {

	INSTANCE;

	private static final String REPOSITORY_DIR = "/home/imediava/Escritorio/workspace-proyecto2/dynamicrefactoring.plugin/";

	private final IJavaProject javaProject;

	private EclipseBasedJavadocReader() {
		try {
			javaProject = createProject();
			setUpProjectClassPath();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Crea el proyecto del que se obtendran las descripciones de los javadoc.
	 * 
	 * @return proyecto a editar
	 * @throws CoreException
	 *             problema
	 */
	private IJavaProject createProject() throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("MyProject");
		project.delete(true, null);
		project.create(null);
		project.open(null);

		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = JavaCore.NATURE_ID;
		description.setNatureIds(newNatures);
		project.setDescription(description, null);

		return JavaCore.create(project);
	}

	/**
	 * Establece el classpath del proyecto por defecto. Incluye al menos las
	 * librerias de la JavaRunTime
	 * 
	 * @throws CoreException
	 */
	private void setUpProjectClassPath() throws CoreException {

		Set<IClasspathEntry> entries = new HashSet<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime
				.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
			entries.add(JavaCore.newLibraryEntry(
					element.getSystemLibraryPath(), null, null));
		}
		javaProject.setRawClasspath(
				entries.toArray(new IClasspathEntry[entries.size()]), null);
		addLibraryToReaderClassPath(new Path(RefactoringConstants.REFACTORING_CLASSES_DIR), "file://" + REPOSITORY_DIR
				+ "dynamicrefactoring.plugin/doc/javadoc/");
		addLibraryToReaderClassPath(new Path(REPOSITORY_DIR
				+ "dynamicrefactoring.plugin/lib/moon-2.4.1.jar"), "file://"
				+ REPOSITORY_DIR
				+ "dynamicrefactoring.plugin/doc/javadoc/moon/");
		addLibraryToReaderClassPath(new Path(REPOSITORY_DIR
				+ "dynamicrefactoring.plugin/lib/" + "javamoon-2.6.0.jar"), "file://"
				+ REPOSITORY_DIR
				+ "dynamicrefactoring.plugin/doc/javadoc/javamoon/");

	}

	/**
	 * Agrega una library al classpath del lector para que de este modo se pueda
	 * leer el javadoc de la misma.
	 * 
	 * @param libraryPath
	 *            path de la libreria
	 * @param javadocPath
	 *            ruta del javadoc de la libreria
	 * @throws JavaModelException
	 *             excepcion al agregar la libreria
	 */
	private void addLibraryToReaderClassPath(Path libraryPath,
			String javadocPath) throws JavaModelException {
		IClasspathEntry[] classPath = javaProject.getRawClasspath();
		List<IClasspathEntry> actualEntries = new ArrayList<IClasspathEntry>(
				Arrays.asList(classPath));
		actualEntries
				.add(getClassPathEntryForLibrary(libraryPath, javadocPath));
		javaProject.setRawClasspath(actualEntries
				.toArray(new IClasspathEntry[actualEntries.size()]), null);
	}

	@Override
	public String getTypeJavaDocAsHtml(String typeFullyQualifiedName) {
		Preconditions.checkArgument(hasType(typeFullyQualifiedName));
		try {
			final String javadoc = javaProject.findType(typeFullyQualifiedName)
					.getAttachedJavadoc(null);
			return javadoc != null ? javadoc : "";
		} catch (JavaModelException e) {
			throw Throwables.propagate(e);
		}
	}
	
	@Override
	public String getTypeJavaDocAsPlainText(String typeFullyQualifiedName) {
		return html2text(getTypeJavaDocAsHtml(typeFullyQualifiedName));
	}
	

	@Override
	public boolean hasType(String typeFullyQualifiedName) {
		try {
			return javaProject.findType(typeFullyQualifiedName) != null;
		} catch (JavaModelException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Convierte el texto en formato html en texto plano.
	 * 
	 * @param htmlString
	 *            cadena con formato html
	 * @return texto html del que se han eliminado las etiquetas
	 */
	private static String html2text(String htmlString) {
		return Jsoup.parse(htmlString).text();
	}
	

	/**
	 * Obtiene una entrada de una libreria que se puede agregar al classpath de
	 * un IJavaProject.
	 * 
	 * @param libraryPath
	 *            ruta de los binarios de la libreria
	 * @param javadocPath
	 *            path en formato url (file:// para ficheros del sistema) del
	 *            javadoc de la libreria
	 * @return entrada de la libreria
	 */
	private IClasspathEntry getClassPathEntryForLibrary(Path libraryPath,
			String javadocPath) {
		IClasspathAttribute attribute = JavaCore.newClasspathAttribute(
				IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME,
				javadocPath);
		IClasspathEntry miLibreriaEntry = JavaCore.newLibraryEntry(libraryPath,
				null,
				null, // no source
				null, new IClasspathAttribute[] { attribute }, false);
		return miLibreriaEntry;
	}

}
