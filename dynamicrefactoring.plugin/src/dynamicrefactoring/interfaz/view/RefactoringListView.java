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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.swtdesigner.ResourceManager;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.imp.ElementCatalog;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.RefactoringTreeManager;

/**
 * Proporcia una vista de Eclipse la cual muestra la lista de todas las
 * refactorizaciones asi como la información asociada a las mismas. Sobre estas
 * se pueden realizar clasificaciones y filtros.
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
	 * Tabla con los catálogos asociados a clasificaciones disponibles.
	 */
	private HashMap<String,ElementCatalog<DynamicRefactoringDefinition>> catalogs;

	/**
	 * Catálogo que esta siendo utilizado conforme a las clasificación
	 * seleccionada.
	 */

	private ElementCatalog<DynamicRefactoringDefinition> catalogInUse;
	
	//TODO: Mantener una lista de predicados contruidos a partir de los filtros
	//List<Predicate>

	/**
	 * Combo que contiene los tipos de clasificaciones de refactorizaciones
	 * que se encuentran disponibles para realizar.
	 */
	private Combo classCombo;

	/**
	 * Etiqueta clasificación.
	 */
	private Label classLabel;

	//private Label stateClassLabel;
	//private Text findText;
	//private Button findButton;

	private Text tSearch;
	private Button bSearch;
	private Button bDefault;

	/**
	 * Árbol sobre el que se mostrarán de forma estructurada las diferentes
	 * refactorizaciones conforme a la clasificación seleccionada y los filtros
	 * aplicados.
	 */
	private Tree tr_Refactorings;

	//TODO: conseguir que los componentes queden bien presentados (layout)
	//TODO: por defecto marcado en el combo None y mostrar asi el arbol??
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
		createCatalogs();

		classLabel=new Label(parent, SWT.LEFT);
		classLabel.setText(Messages.RefactoringListView_Classification+": ");

		classCombo=new Combo(parent, SWT.READ_ONLY);
		classCombo.add(Category.NONE_CATEGORY.getName(),0);
		classCombo.add("scope",1);
		classCombo.setToolTipText(
				Messages.RefactoringListView_SelectFromClassification);
		classCombo.addSelectionListener(new ClassComboSelectionListener());

		final Composite composite_1 = new Composite(parent, SWT.NONE);
		final FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(0, 245);
		fd_composite_1.top = new FormAttachment(0, 5);
		fd_composite_1.left = new FormAttachment(0, 5);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FormLayout());

		Label search = new Label(composite_1, SWT.NONE);
		final FormData fd_search = new FormData();
		fd_search.bottom = new FormAttachment(0,58);
		fd_search.top = new FormAttachment(0, 29);
		fd_search.left = new FormAttachment(0, 15);
		search.setLayoutData(fd_search);
		//search.setText("Search:");

		tSearch = new Text(composite_1,SWT.BORDER);
		final FormData fd_tsearch = new FormData();
		fd_tsearch.right = new FormAttachment(0, 185);
		fd_tsearch.top = new FormAttachment(0, 28);
		fd_tsearch.left = new FormAttachment(search, 0, SWT.RIGHT);
		tSearch.setMessage("Search");
		tSearch.setLayoutData(fd_tsearch);
		tSearch.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e){
				bDefault=e.display.getShells()[0].getDefaultButton(); 
				e.display.getShells()[0].setDefaultButton(bSearch);
			}
			public void focusLost(FocusEvent e){
				e.display.getShells()[0].setDefaultButton(bDefault);
			}
		});


		bSearch = new Button(composite_1, SWT.PUSH);
		final FormData fd_bsearch = new FormData();
		fd_bsearch.right = new FormAttachment(0, 225);
		fd_bsearch.top = new FormAttachment(0, 26);//28
		fd_bsearch.left = new FormAttachment(tSearch, 3, SWT.RIGHT);
		bSearch.setLayoutData(fd_bsearch);
		bSearch.setImage(ResourceManager.getPluginImage(
				RefactoringPlugin.getDefault(),
				"icons" + System.getProperty("file.separator") + "search.png"));
		bSearch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				search(tSearch.getText());	
			}

			private void search(String text) {
				String text_aux=null;
				if(text.toLowerCase().contains("category:")){
					text_aux=text.substring(9);
					System.out.println("categoria");
					System.out.println(text_aux);
					catalogInUse.addConditionToFilter(new CategoryCondition<DynamicRefactoringDefinition>(
							"scope." + text_aux));
					showTree(classCombo.getText());
				}else{
					//					if(text.toLowerCase().contains("name:")){
					//				     text_aux=text.substring(5);
					//					System.out.println("nombre");
					//					System.out.println(text_aux);
					//					scopeCatalog.addConditionToFilter(new NameContainsTextCondition<DynamicRefactoringDefinition>(
					//							text_aux));
					//					}
				}

			}
		});

		//		findText=new Text(parent,SWT.SINGLE | SWT.BORDER);
		//		findText.setMessage("Find");
		//		findText.setSize(10,20);
		//		findText.setToolTipText("Find");
		//		findButton=new Button(parent, SWT.PUSH);
		//		findButton.setBackgroundImage(ResourceManager.getPluginImage(
		//				RefactoringPlugin.getDefault(),"icons" + System.getProperty("file.separator") + "search.png"));
		//		findText.setBackgroundImage(ResourceManager.getPluginImage(
		//				RefactoringPlugin.getDefault(),				"icons" + System.getProperty("file.separator") + "find.png"));
		//		//stateClassLabel=new Label(parent, SWT.LEFT);

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

	// TODO: Este metodo deberá ser independiente al tipo de clasificacion
	// para ello se debera usar el reader de los xml de las refactorizaciones
	//y categorias disponibles
	/**
	 * Obtiene las clasificaciones, categorias y refactorizaciones disponibles y
	 * con ello crea los catálogos correspondientes para cada una de las
	 * clasificaciones.
	 */
	private void createCatalogs(){
		catalogs=new HashMap<String,ElementCatalog<DynamicRefactoringDefinition>>();
		Set<Category> catScope = new HashSet<Category>();
		Set<DynamicRefactoringDefinition> drd = new HashSet<DynamicRefactoringDefinition>(refactorings.values());

		for (Scope scope : Scope.values()) {
			if (!scope.equals(Scope.SCOPE_BOUNDED_PAR)) {
				catScope.add(new Category("scope." + scope.toString()));
			}
		}
		catalogs.put("scope", new ElementCatalog<DynamicRefactoringDefinition>(
				drd, catScope, "scope"));
		
		Set<Category> catNone = new HashSet<Category>();
		catNone.add(Category.NONE_CATEGORY);
		catalogs.put(Category.NONE_CATEGORY.getName(), new ElementCatalog<DynamicRefactoringDefinition>(
				drd, catNone, Category.NONE_CATEGORY.getName()));
	}


	private void showTree(String classificationName){

		RefactoringTreeManager.cleanTree(tr_Refactorings);

		ClassifiedElements<DynamicRefactoringDefinition> classifiedElements = 
			catalogInUse.getClassificationOfElements(true);

		int orderInBranchClass = 0;
		TreeItem classTreeItem = TreeEditor.createBranch(tr_Refactorings,
				orderInBranchClass, classificationName,
				"icons" + System.getProperty("file.separator") + "class.gif"); 
		orderInBranchClass++;

		ArrayList<Category> categories=
			new ArrayList<Category>(classifiedElements.getClassification().getCategories());
		Collections.sort(categories);

		ArrayList<DynamicRefactoringDefinition> dRefactDef=null;
		TreeItem catTreeItem, filTreeItem=null;
		int orderInBranchCat = 0;

		
			for(Category c : categories){
				dRefactDef=
					new ArrayList<DynamicRefactoringDefinition>(classifiedElements.getCategoryChildren(c));
				Collections.sort(dRefactDef);
				if(!c.equals(Category.FILTERED_CATEGORY)){
					catTreeItem=classTreeItem;
					if(!(classificationName.equalsIgnoreCase(Category.NONE_CATEGORY.getName())&&
							c.equals(Category.NONE_CATEGORY))
							&&
							dRefactDef.size()>0){
						catTreeItem = TreeEditor.createBranch(classTreeItem,
								orderInBranchCat, c.getName(),
								"icons" + System.getProperty("file.separator") + "cat.gif"); 
						orderInBranchCat++;
					}
					createTreeItemFromParent(dRefactDef, catTreeItem);
				}else{
					if(dRefactDef.size()>0){
						filTreeItem = TreeEditor.createBranch(tr_Refactorings,
								orderInBranchClass, Category.FILTERED_CATEGORY.getName(),
								"icons" + System.getProperty("file.separator") + "fil.gif");
						orderInBranchClass++;
					}
					createTreeItemFromParent(dRefactDef, filTreeItem);
				}
			}
		classTreeItem.setExpanded(true);
	}

	private void createTreeItemFromParent(ArrayList<DynamicRefactoringDefinition> dRefactDef,TreeItem catTreeItem) {
		int orderInBranchRef = 0;
		for(DynamicRefactoringDefinition ref: dRefactDef){
			RefactoringTreeManager.
			createRefactoringDefinitionTreeItemFromParentTreeItem(orderInBranchRef, 
					ref, catTreeItem);
			orderInBranchRef++;
		}
	}

	/**
	 * Actualiza el árbol de refactorización para representarlas conforme a la
	 * clasificación que ha sido seleccionada en el combo.
	 */
	private class ClassComboSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			//stateClassLabel.setText("Selected: " + classCombo.getText());
			//TODO:hay que implementar esto cuando lo tengamos:
			//si catalogoInUse no es nulo (es nulo cuando es la primera vez)
			//eliminar todos los filtros del catalogoInUse antes de asignar
			//la variable al nuevo catalogo a usar segun la clasificacion elegida.
			catalogInUse=catalogs.get(classCombo.getText());
			//TODO:hay que implementar esto cuando lo tengamos:
			// añadir toda la lista de predicados si no es nula o vacia
			showTree(classCombo.getText());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}
}
