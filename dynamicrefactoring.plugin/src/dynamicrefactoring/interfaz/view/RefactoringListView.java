package dynamicrefactoring.interfaz.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.imp.ElementCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.reader.JDOMXMLRefactoringReaderImp;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.DynamicRefactoringLister;

/**
 * Proporcia una vista de Eclipse la cual muestra la lista de todas las
 * refactorizaciones asi como la información asociada a las mismas.
 * Sobre estas se pueden realizar clasificaciones y filtros.
 */
public class RefactoringListView extends ViewPart {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(RefactoringListView.class);

	/**
	 * Identificador de la vista.
	 */
	public static final String ID = "dynamicrefactoring.views.refactoringListView"; //$NON-NLS-1$

	/**
	 * Tabla de refactorizaciones disponibles.
	 * 
	 * <p>
	 * Se utiliza como clave el nombre de la refactorización y como valor la
	 * propia representación de la definición de la refactorización.
	 * </p>
	 */
	protected HashMap<String, DynamicRefactoringDefinition> refactorings;

	/**
	 * Tabla con las rutas de los ficheros asociados a las refactorizaciones.
	 */
	protected HashMap<String, String> refactoringLocations;

	/**
	 * Nombres de las refactorizaciones disponibles.
	 */
	protected ArrayList<String> refactoringNames;
	
	/**
	 * Catálogo Scope.
	 */
	//TODO: Catalogo Scope que deberá ser independiente a la clasificacion = Set<ElementCatalog<DynamicRefactoringDefinition>>
	private ElementCatalog<DynamicRefactoringDefinition> scopeCatalog;

	/**
	 * Crea los controles SWT para este componente del espacio de trabajo.
	 * 
	 * @param parent
	 *            el control padre.
	 */
	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.RefactoringListView_title);
		
		loadRefactorings();
		createScopeCatalog();
		
	}
	
	/**
	 * Oculta la vista.
	 */
	@Override
	public void dispose() {
	}
	
	/**
	 * Solicita a la vista que tome el foco en el espacio de trabajo.
	 */
	@Override
	public void setFocus() {
	}
	
	/**
	 * Carga la lista de refactorizaciones dinámicas disponibles.
	 */
	private void loadRefactorings() {
		DynamicRefactoringLister listing = DynamicRefactoringLister.getInstance();

		try {
			// Se obtiene la lista de todas las refactorizaciones disponibles.
			HashMap<String, String> allRefactorings = 
				listing.getDynamicRefactoringNameList(
						RefactoringPlugin.getDynamicRefactoringsDir(), true, null);

			refactoringNames = new ArrayList<String>();
			refactoringLocations = new HashMap<String, String>();
			refactorings = new HashMap<String, DynamicRefactoringDefinition>();

			for (Map.Entry<String, String> nextRef : allRefactorings.entrySet()){

				try {
					// Se obtiene la definición de la siguiente refactorización.
					DynamicRefactoringDefinition definition = 
						DynamicRefactoringDefinition.getRefactoringDefinition(
								nextRef.getValue());

					if (definition != null && definition.getName() != null){
						refactorings.put(definition.getName(), definition);
						refactoringLocations.put(definition.getName(), 
								nextRef.getValue());
						refactoringNames.add(definition.getName());						
					}
				}
				catch(RefactoringException e){
					e.printStackTrace();
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					String message = 
						Messages.RefactoringListView_NotAllListed + 
						".\n" + e.getMessage(); //$NON-NLS-1$
					logger.error(message);
					MessageDialog.openError(window.getShell(), Messages.RefactoringListView_Error, message);
				}
			}
		}		
		catch(IOException e){
			logger.error(
					Messages.RefactoringListView_AvailableNotListed + 
					".\n" + e.getMessage()); //$NON-NLS-1$
		}
	}
	
	//TODO: Este metodo deberá ser independiente al tipo de clasificacion
	/**
	 * Obtiene las categorias y refactorizaciones disponibles y 
	 * con ello crea el catálogo para la clasificación de Scope.
	 */
	private void createScopeCatalog(){
		Set<Category> categories = new HashSet<Category>();
		Set<DynamicRefactoringDefinition> drd = (Set<DynamicRefactoringDefinition>) refactorings.values();
		
		for (Scope scope : Scope.values()) {
			if (!scope.equals(Scope.SCOPE_BOUNDED_PAR)) {
				categories.add(new Category("scope." + scope.toString()));
			}
		}
		scopeCatalog = new ElementCatalog<DynamicRefactoringDefinition>(
				drd, categories, "scope");
	}
}
