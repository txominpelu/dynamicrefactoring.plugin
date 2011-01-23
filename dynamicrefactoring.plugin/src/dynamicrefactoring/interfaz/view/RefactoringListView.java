package dynamicrefactoring.interfaz.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.imp.ElementCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.RefactoringTreeManager;

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
	private HashMap<String, DynamicRefactoringDefinition> refactorings;

	/**
	 * Tabla con las rutas de los ficheros asociados a las refactorizaciones.
	 */
	//TODO: revisar si no es necesario para eliminarlo
	private HashMap<String, String> refactoringLocations;

	/**
	 * Nombres de las refactorizaciones disponibles.
	 */
	//TODO: revisar si no es necesario para eliminarlo
	private ArrayList<String> refactoringNames;

	/**
	 * Catálogo Scope.
	 */
	//TODO: Catalogo Scope que deberá ser independiente a la clasificacion = Set<ElementCatalog<DynamicRefactoringDefinition>>
	private ElementCatalog<DynamicRefactoringDefinition> scopeCatalog;

	/**
	 * Combo que contiene los tipos de clasificaciones de refactorizaciones
	 * que se encuentran disponibles para realizar.
	 */
	private Combo classCombo;

	/**
	 * Etiqueta clasificación.
	 */
	private Label classLabel;
	private Label stateClassLabel;
	
	/**
	 * Árbol sobre el que se mostrarán de forma estructurada las diferentes refactorizaciones 
	 * conforme a la clasificación seleccionada y los filtros aplicados.
	 */
	private Tree tr_Refactorings;

	//TODO: conseguir que los componentes queden bien presentados (layout)
	//TODO: por defecto marcado en el combo None y mostrar asi el arbol
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

		classLabel=new Label(parent, SWT.LEFT);
		classLabel.setText(Messages.RefactoringListView_Classification+": ");

		classCombo=new Combo(parent, SWT.READ_ONLY);
		classCombo.add(Category.NONE_CATEGORY.getName(),0);
		classCombo.add(scopeCatalog.getClassificationName(),1);
		classCombo.setToolTipText(
				Messages.RefactoringListView_SelectFromClassification);
		classCombo.addSelectionListener(new ClassComboSelectionListener());
		
		stateClassLabel=new Label(parent, SWT.LEFT);

		tr_Refactorings = new Tree(parent, SWT.NULL);
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
		Set<DynamicRefactoringDefinition> drd = new HashSet<DynamicRefactoringDefinition>(refactorings.values());
		
		for (Scope scope : Scope.values()) {
			if (!scope.equals(Scope.SCOPE_BOUNDED_PAR)) {
				categories.add(new Category("scope." + scope.toString()));
			}
		}
		scopeCatalog = new ElementCatalog<DynamicRefactoringDefinition>(
				drd, categories, "scope");
	}

	//TODO: refactorizar extrayendo la parte común a otro metodo.
	//TODO: conseguir que se quede expandido el arbol en el primer nivel.
	private void showTree(String classificationName){
	
		RefactoringTreeManager.cleanTree(tr_Refactorings);
		
		ClassifiedElements<DynamicRefactoringDefinition> classifiedElements = 
			scopeCatalog.getClassificationOfElements(true);
		
		int orderInBranchClass = 0;
		TreeItem classTreeItem = TreeEditor.createBranch(tr_Refactorings,
				orderInBranchClass, classificationName,
				"icons" + System.getProperty("file.separator") + "class.gif"); 
		orderInBranchClass++;
		
		ArrayList<Category> categories=
			new ArrayList<Category>(classifiedElements.getClassification().getCategories());
		Collections.sort(categories);
		
		ArrayList<DynamicRefactoringDefinition> dRefactDef=null;
		TreeItem catTreeItem=null;
		int orderInBranchCat = 0;
		
		for(Category c : categories){
			dRefactDef=
				new ArrayList<DynamicRefactoringDefinition>(classifiedElements.getCategoryChildren(c));
			Collections.sort(dRefactDef);
			if(c.getName()!=Category.FILTERED_CATEGORY.getName()){
				catTreeItem=classTreeItem;
				if(!classificationName.equals(Category.NONE_CATEGORY.getName())){
					if(dRefactDef.size()>0){
						catTreeItem = TreeEditor.createBranch(classTreeItem,
								orderInBranchCat, c.getName(),
								"icons" + System.getProperty("file.separator") + "cat.gif"); 
						orderInBranchCat++;
					}
				}
				for(DynamicRefactoringDefinition ref: dRefactDef){
					int orderInBranchRef = 0;
					RefactoringTreeManager.
					createRefactoringDefinitionTreeItemFromParentTreeItem(orderInBranchRef, 
							ref, catTreeItem);
					orderInBranchRef++;
				}
			}else{
				if(dRefactDef.size()>0){
					classTreeItem = TreeEditor.createBranch(tr_Refactorings,
							orderInBranchClass, classificationName,
							"icons" + System.getProperty("file.separator") + "fil.gif");
					orderInBranchClass++;
				}
				for(DynamicRefactoringDefinition ref: dRefactDef){
					int orderInBranchRef = 0;
					RefactoringTreeManager.
					createRefactoringDefinitionTreeItemFromParentTreeItem(orderInBranchRef, 
							ref, classTreeItem);
					orderInBranchRef++;
				}
			}
		}
	}

	/**
	 * Actualiza el árbol de refactorización para representarlas conforme
	 * a la clasificación que ha sido seleccionada en el combo.
	 */
	private class ClassComboSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			stateClassLabel.setText("Selected: " + classCombo.getText());
			showTree(classCombo.getText());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}
}
