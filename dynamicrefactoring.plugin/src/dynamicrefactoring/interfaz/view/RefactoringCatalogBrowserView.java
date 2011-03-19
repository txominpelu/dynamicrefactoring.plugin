package dynamicrefactoring.interfaz.view;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.action.ShowLeftAndRightPaneViewAction;
import dynamicrefactoring.action.ShowLeftPaneViewAction;
import dynamicrefactoring.action.ShowRightPaneViewAction;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.condition.KeyWordCondition;
import dynamicrefactoring.domain.metadata.condition.TextCondition;
import dynamicrefactoring.domain.metadata.imp.ElementCatalog;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.plugin.xml.classifications.imp.PluginCatalog;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.RefactoringTreeManager;

/**
 * Proporciona una vista de Eclipse la cual muestra la lista de todas las
 * refactorizaciones asi como la información asociada a las mismas. Sobre estas
 * se pueden realizar clasificaciones y filtros.
 */
public class RefactoringCatalogBrowserView extends ViewPart {

	/**
	 * Clasificacion cuya unica categoria es NONE_CATEGORY.
	 */
	private static final Classification NONE_CLASSIFICATION;

	static {
		Set<Category> catNone = new HashSet<Category>();
		catNone.add(Category.NONE_CATEGORY);
		NONE_CLASSIFICATION = new SimpleUniLevelClassification(
				Category.NONE_CATEGORY.getName(),
				Messages.RefactoringCatalogBrowserView_NoneClassDescription, catNone);
	}

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringCatalogBrowserView.class);

	/**
	 * Identificador de la vista.
	 */
	public static final String ID = "dynamicrefactoring.views.refactoringCatalogBrowserView"; //$NON-NLS-1$

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
	private HashMap<String, String> refactoringLocations;
	
	/**
	 * Almacen con todas las clasificaciones.
	 */
	private PluginCatalog classStore;
	
	/**
	 * Clasificaciones disponibles.
	 */
	private ArrayList<Classification> classifications;

	/**
	 * Catálogo que esta siendo utilizado conforme a la clasificación
	 * seleccionada.
	 */
	private ElementCatalog<DynamicRefactoringDefinition> catalog;

	/**
	 * Lista de condiciones que conforman el filtro actual aplicado.
	 */
	private ArrayList<Predicate<DynamicRefactoringDefinition>> filter;

	/**
	 * Etiqueta clasificación.
	 */
	private Label classLabel;

	/**
	 * Combo que contiene los tipos de clasificaciones de refactorizaciones
	 * disponibles.
	 */
	private Combo classCombo;

	/**
	 * Etiqueta con la descripción de la clasificación.
	 */
	private Label descClassLabel;

	/**
	 * Etiqueta de ayuda para el cuadro de texto donde el usuario introduce la
	 * condición.
	 */
	private Label helpLabel;

	/**
	 * ToolTip de ayuda para el cuadro de texto donde el usuario introduce la
	 * condición.
	 */
	private ToolTip searchToolTip;

	/**
	 * Cuadro de texto que permite introducir al usuario el patrón de
	 * búsqueda.
	 */
	private Text searchText;

	/**
	 * Botón que permite activar un proceso de búsqueda al usuario.
	 */
	private Button searchButton;

	
	private static final String FILTERED="Filtered";

	/**
	 * Árbol sobre el que se mostrarán de forma estructurada las diferentes
	 * refactorizaciones conforme a la clasificación seleccionada y los filtros
	 * aplicados.
	 */
	private Tree refactoringsTree;

	/**
	 * CheckBox que permite seleccionar al usuario si desea que se muestren en
	 * el arbol también las refactorizaciones filtradas.
	 */
	private Button filteredButton;

	/**
	 * Propiedad asociada a los botones de seleccion de condiciones que indica
	 * en qué fila de la tabla se encuentran.
	 */
	private final String ROW_PROPERTY = "Row"; //$NON-NLS-1$

	/**
	 * Propiedad asociada a las filas de la tabla que indica qué botón check
	 * tienen asociado cada una.
	 */
	private final String CHECKBUTTON_PROPERTY = "checkButton"; //$NON-NLS-1$

	/**
	 * Propiedad asociada a las filas de la tabla que indica qué botón clear
	 * tienen asociado cada una.
	 */
	private final String CLEARBUTTON_PROPERTY = "clearButton"; //$NON-NLS-1$

	/**
	 * Botón que permite al usuario eliminar todas las condiciones del filtro.
	 */
	private Button clearAllButton;

	/**
	 * Tabla en la que se mostraránn las condiciones del filtro a aplicar a las
	 * refactorizaciones para mostrar en el árbol.
	 */
	private Table conditionsTable;

	/**
	 * Split que separa en dos partes la vista.
	 */
	private SashForm sashForm;

	/**
	 * Organizador de pestañas para mostrar la información relativa a la
	 * refactorización seleccionada en el árbol.
	 */
	private RefactoringSummaryPanel refSummaryPanel;

	/**
	 * Contenedor de la parte izquierda de la vista.
	 */
	private Composite classComp;
	
	/**
	 * Contenedor de la parte derecha de la vista.
	 */
	private Composite refComp;

	/**
	 * Lista de acciones de la barra de herramientas de la vista referentes a la
	 * visualización de los contenedores que se encuentran dividos por el
	 * spliter.
	 */
	private ArrayList<IAction> actionsPane;

	
	/**
	 * Crea los controles SWT para este componente del espacio de trabajo.
	 * 
	 * @param parent
	 *            el control padre.
	 */
	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.RefactoringCatalogBrowserView_Title);
		parent.setLayout(new FormLayout());

		//carga de datos
		loadClassifications();
		loadRefactorings();
		createCatalog();

		//scrolledComp
		final ScrolledComposite scrolledComp = 
			new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		FormData scrolledFormData = new FormData();
		scrolledFormData.top = new FormAttachment(0, 5);
		scrolledFormData.left = new FormAttachment(0, 5);
		scrolledFormData.right=new FormAttachment(100, -5);
		scrolledFormData.bottom=new FormAttachment(100, -5);
		scrolledComp.setLayoutData(scrolledFormData);

		//sashForm
		sashForm = new SashForm(scrolledComp, SWT.HORIZONTAL | SWT.SMOOTH );
		sashForm.setSashWidth(2);
		sashForm.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

		//sashForm Left: classComp
		FormData classFormData=null;
		classComp = new Composite(sashForm, SWT.NONE);
		classComp.setLayout(new FormLayout());

		//sashForm Rigth: refComp
		refComp = new Composite(sashForm, SWT.NONE);
		refComp.setLayout(new FormLayout());

		//refSummaryPanel
		refSummaryPanel=new RefactoringSummaryPanel(refComp,this);

		//classLabel
		classLabel=new Label(classComp, SWT.LEFT);
		classLabel.setText(Messages.RefactoringCatalogBrowserView_Classification);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(0,10);
		classFormData.left=new FormAttachment(0,5);
		classLabel.setLayoutData(classFormData);

		//classCombo
		classCombo=new Combo(classComp, SWT.READ_ONLY);
		classCombo.setToolTipText(Messages.RefactoringCatalogBrowserView_SelectFromClassification);

		fillClassCombo();

		classFormData=new FormData();
		classFormData.top=new FormAttachment(0,5);
		classFormData.left=new FormAttachment(classLabel,15);
		classFormData.width=100;
		classCombo.setLayoutData(classFormData);
		classCombo.addSelectionListener(new ClassComboSelectionListener());

		//descClassLabel
		descClassLabel=new Label(classComp, SWT.LEFT);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(classLabel,15);
		classFormData.left=new FormAttachment(0,5);
		classFormData.right=new FormAttachment(100,-5);
		descClassLabel.setLayoutData(classFormData);
		
		//searchText
		searchText = new Text(classComp, SWT.BORDER);
		searchText.setMessage(Messages.RefactoringCatalogBrowserView_Search);
		classFormData = new FormData();
		classFormData.top = new FormAttachment(0, 6);
		classFormData.left = new FormAttachment(classCombo, 175);
		classFormData.width=150;
		searchText.setLayoutData(classFormData);
		searchText.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				helpLabel.setVisible(true);
			}
			@Override
			public void focusLost(FocusEvent e) {
				helpLabel.setVisible(false);
			}
		});
		searchText.addHelpListener(new HelpListener(){
			@Override
			public void helpRequested(HelpEvent e) {
				Point pt = e.display.map(searchText, null, 0, 10);
				searchToolTip.setLocation(pt);
				searchToolTip.setVisible(true);
			}
		});

		//helpLabel
		helpLabel=new Label(classComp, SWT.LEFT);
		helpLabel.setImage(RefactoringImages.getHelpIcon());
		helpLabel.setVisible(false);
		classFormData=new FormData();
		classFormData.top = new FormAttachment(0, 8);
		classFormData.right=new FormAttachment(searchText,0);
		helpLabel.setLayoutData(classFormData);
		helpLabel.addMouseTrackListener(new MouseTrackAdapter(){
			@Override
			public void mouseEnter(MouseEvent e) {
				Point pt = e.display.map(searchText, null, 0, 10);
				searchToolTip.setLocation(pt);
				searchToolTip.setVisible(true);
			}
			@Override
			public void mouseExit(MouseEvent e) {
				searchToolTip.setVisible(false);
			}		
		});
		
		//searchToolTip
		searchToolTip=new ToolTip(searchText.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION );
		searchToolTip.setText(Messages.RefactoringCatalogBrowserView_TextSearchToolTip);
		searchToolTip.setMessage(Messages.RefactoringCatalogBrowserView_TextSearchMessage);
		searchToolTip.setAutoHide(true);
		
		//searchButton 
		searchButton = new Button(classComp, SWT.PUSH);
		searchButton.setImage(RefactoringImages.getSearchIcon());
		classFormData = new FormData();
		classFormData.top = new FormAttachment(0, 3);
		classFormData.left = new FormAttachment(searchText, 5);
		searchButton.setLayoutData(classFormData);
		searchButton.addSelectionListener(new SearchTextSelectionListener());

		//refactoringTree
		refactoringsTree = new Tree(classComp, SWT.BORDER);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(descClassLabel,5);
		classFormData.left=new FormAttachment(0,5);
		classFormData.right=new FormAttachment(100,-5);
		refactoringsTree.setLayoutData(classFormData);
		refactoringsTree.addMouseListener(new TreeMouseListener());

		//filteredButton
		filteredButton = new Button(classComp, SWT.CHECK | SWT.CENTER);
		filteredButton.setText(Messages.RefactoringCatalogBrowserView_ShowFiltered);
		filteredButton.setSelection(true);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(refactoringsTree,5);
		classFormData.left=new FormAttachment(0,15);
		filteredButton.setLayoutData(classFormData);
		filteredButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(!catalog.isEmptyFilter())
					showTree(catalog.getClassification().getName());	
			}
		});

		//clearAllButton
		clearAllButton = new Button(classComp, SWT.CHECK | SWT.CENTER);
		clearAllButton.setText(Messages.RefactoringCatalogBrowserView_ClearAll);
		clearAllButton.setEnabled(false);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(filteredButton,10);
		classFormData.right=new FormAttachment(100,-5);
		clearAllButton.setLayoutData(classFormData);
		clearAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeAllConditionToFilter();
			}
		});
		
		//conditionsTable
		conditionsTable = new Table(classComp, SWT.BORDER);
		conditionsTable.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		conditionsTable.setLinesVisible(true);
		conditionsTable.setHeaderVisible(true);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(clearAllButton,5);
		classFormData.left=new FormAttachment(0,5);
		classFormData.right=new FormAttachment(100,-5);
		classFormData.bottom=new FormAttachment(100,-5);
		conditionsTable.setLayoutData(classFormData);
		
		//se crean las columnas de la tabla
		TableColumn selectedCol = new TableColumn(conditionsTable, SWT.NONE);
		selectedCol.setText(Messages.RefactoringCatalogBrowserView_SelectedCol);
		selectedCol.setResizable(false);
		selectedCol.setToolTipText(Messages.RefactoringCatalogBrowserView_SelectedColToolTip);
		TableColumn nameCol = new TableColumn(conditionsTable, SWT.NONE);
		nameCol.setText(Messages.RefactoringCatalogBrowserView_NameCol);
		TableColumn clearCol = new TableColumn(conditionsTable, SWT.NONE);
		clearCol.setText(Messages.RefactoringCatalogBrowserView_ClearCol);
		clearCol.setResizable(false);
		clearCol.setToolTipText(Messages.RefactoringCatalogBrowserView_ClearColToolTip);
		packTableColumns();
		
		//mostrar por defecto la clasificacion None
		classCombo.select(0);
		descClassLabel.setText(NONE_CLASSIFICATION.getDescription());
		showTree(classCombo.getText());

		//sashForm
		sashForm.setWeights(new int[] {60,40});

		//scrolledComp
		scrolledComp.setContent(sashForm);
		scrolledComp.setExpandHorizontal(true);
		scrolledComp.setExpandVertical(true);
		scrolledComp.setMinSize(sashForm.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComp.setShowFocusedControl(true);

	}

	private void fillClassCombo() {
		// añadimos la clasificacion por defecto
		classCombo.add(NONE_CLASSIFICATION.getName());

		for (Classification classification : classifications)
			classCombo.add(classification.getName());
		classifications.add(NONE_CLASSIFICATION);
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
	 * Carga las clasificaciones disponibles.
	 */
	private void loadClassifications() {
		classStore = PluginCatalog.getInstance();
		classifications = new ArrayList<Classification> (classStore.getAllClassifications());
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
						RefactoringPlugin.getDynamicRefactoringsDir(),true, null);

			refactoringLocations = new HashMap<String, String>();
			refactorings = new HashMap<String, DynamicRefactoringDefinition>();

			for (Map.Entry<String, String> nextRef : allRefactorings.entrySet()) {

				try {
					// Se obtiene la definición de la siguiente refactorización.
					DynamicRefactoringDefinition definition = 
						DynamicRefactoringDefinition.getRefactoringDefinition(
								nextRef.getValue());

					File f=null;
					if (definition != null && definition.getName() != null) {
						refactorings.put(definition.getName(), definition);
						f=new File(nextRef.getValue());
						refactoringLocations.put(definition.getName(), f.getParent()+ File.separatorChar);
					}
				} catch (RefactoringException e) {
					e.printStackTrace();
					IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
					String message = 
						Messages.RefactoringCatalogBrowserView_NotAllListed + 
						".\n" + e.getMessage(); //$NON-NLS-1$
					logger.error(message);
					MessageDialog.openError(window.getShell(),
							Messages.RefactoringCatalogBrowserView_Error, message);
				}
			}
		} catch (IOException e) {
			logger.error(Messages.RefactoringCatalogBrowserView_AvailableNotListed + 
					".\n" + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Crea el catálogo por defecto para las refactorizaciones disponibles.
	 */
	private void createCatalog(){
		Set<DynamicRefactoringDefinition> drd = 
			new HashSet<DynamicRefactoringDefinition>(refactorings.values());
		filter=new ArrayList<Predicate<DynamicRefactoringDefinition>>();
		catalog=new ElementCatalog<DynamicRefactoringDefinition>(
				drd, NONE_CLASSIFICATION);
	}

	/**
	 * Crea una representación en formato de árbol de cada una de las
	 * refactorizaciones que pertenecen a cada una de las categorias que dispone
	 * la clasificación indicada. En caso de tratarse de refactorizaciones
	 * filtradas pone de color gris el texto de todos los componentes del
	 * árbol.
	 * 
	 * @param orderInTree
	 *            posición de la rama en el árbol refactoringsTree.
	 * @param iconPath
	 *            ruta al icono representativo del árbol que se va a crear
	 * @param className
	 *            nombre de la clasificación.
	 * @param classElements
	 *            lista de refactorizaciones a mostrar.
	 * @param isFilteredClass
	 *            indicador de filtrado. Si es verdadero se trata de
	 *            refactorizaciones filtradas, falso en caso contrario.
	 */
	private void createClassificationTree(int orderInTree, String iconPath, String className, 
			ClassifiedElements<DynamicRefactoringDefinition> classElements, 
			boolean isFilteredClass) {
		
		TreeItem classTreeItem = TreeEditor.createBranch(refactoringsTree,
				orderInTree, className, iconPath);
		
		ArrayList<Category> categories = new ArrayList<Category>(
				classElements.getClassification().getCategories());
		Collections.sort(categories);

		ArrayList<DynamicRefactoringDefinition> dRefactDef = null;
		TreeItem catTreeItem = null;
		int orderInBranchCat = 0;
		int orderInBranchRef = 0;

		for(Category c : categories){
			dRefactDef=new ArrayList<DynamicRefactoringDefinition>(
					classElements.getCategoryChildren(c));
			Collections.sort(dRefactDef);
			catTreeItem = classTreeItem;
			if ( !(className.equals(NONE_CLASSIFICATION.getName()) && 
					c.equals(Category.NONE_CATEGORY))
					&& dRefactDef.size()>0){
				catTreeItem = TreeEditor.createBranch(classTreeItem,
						orderInBranchCat, c.getName(),
						RefactoringImages.CAT_ICON_PATH); 
				orderInBranchCat++;
			}
			orderInBranchRef = 0;
			for (DynamicRefactoringDefinition ref : dRefactDef) {
				RefactoringTreeManager
				.createRefactoringDefinitionTreeItemFromParentTreeItem(
						orderInBranchRef, ref, catTreeItem);
				orderInBranchRef++;
			}
		}
		if(isFilteredClass)
			RefactoringTreeManager.setForegroundTreeItem(classTreeItem, 
					Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
	}

	/**
	 * Muestra en forma de árbol la clasificación de las refactorizaciones
	 * según las categorias a las que pertenece, pudiendo aparecer en un grupo
	 * de filtrados si no cumplen con las condiciones establecidas.
	 * 
	 * @param classificationName
	 *            nombre de la clasificación
	 */
	private void showTree(String classificationName) {

		refactoringsTree.setVisible(false);
		
		RefactoringTreeManager.cleanTree(refactoringsTree);
		
		//classifiedElements
		ClassifiedElements<DynamicRefactoringDefinition> classifiedElements = 
			catalog.getClassificationOfElements();
		createClassificationTree(0, RefactoringImages.CLASS_ICON_PATH, classificationName, classifiedElements, false);
		
		//filteredClassifiedElements
		if(filteredButton.getSelection() && catalog.hasFilteredElements()){
			ClassifiedElements<DynamicRefactoringDefinition> filteredClassifiedElements = 
				catalog.getClassificationOfFilteredElements();
			createClassificationTree(1, RefactoringImages.FIL_ICON_PATH, FILTERED, filteredClassifiedElements, true);	
		}

		refactoringsTree.getItem(0).setExpanded(true);
		
		refactoringsTree.setVisible(true);
	}

	private void removeAllConditionToTable(){

		TableItem item=null;
		Object checkB,clearB=null;
		
		conditionsTable.setVisible(false);
		
		for(int i=conditionsTable.getItemCount()-1; i>=0;i--){
			item=conditionsTable.getItem(i);
			//recuperamos los botones check y clear asociados a la fila
			checkB=item.getData(CHECKBUTTON_PROPERTY);
			clearB=item.getData(CLEARBUTTON_PROPERTY);
			//eliminamos los botones recuperados
			if(checkB instanceof Button)
				((Button)checkB).dispose();
			if(clearB instanceof Button)
				((Button)clearB).dispose();
			item.dispose();
		}
		packTableColumns();
		
		conditionsTable.setVisible(true);
		
		clearAllButton.setSelection(false);
		clearAllButton.setEnabled(false);
		
	}
	
	private void removeAllConditionToFilter(){
		removeAllConditionToTable();
		catalog=(ElementCatalog<DynamicRefactoringDefinition>)catalog.removeAllFilterConditions();
		filter.clear();
		showTree(classCombo.getText());
	}
	
	private void packTableColumns(){
		TableColumn cols[]=conditionsTable.getColumns();
		for(TableColumn col : cols){
			col.pack();
		}
	}

	private void addConditionToTable(Predicate<DynamicRefactoringDefinition> condition){
		
		conditionsTable.setVisible(false);
		
		TableItem item=new TableItem(conditionsTable, SWT.BORDER);
		item.setText(1, condition.toString());
		
		TableEditor editor = null;
		
		//checkButton
		editor = new TableEditor(conditionsTable);
		Button checkButton= new Button(conditionsTable, SWT.CHECK );
		checkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(e.getSource() instanceof Button){
					Button checkB=(Button)e.getSource();
					int row=((Integer)checkB.getData(ROW_PROPERTY)).intValue();
					Color c;
					if(checkB.getSelection()){
						catalog.addConditionToFilter(filter.get(row));
						c=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
					}else{
						catalog.removeConditionFromFilter(filter.get(row));
						c=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
					}
					conditionsTable.getItem(row).setForeground(c);
					showTree(classCombo.getText());
				}
			}
		});
		checkButton.setData(ROW_PROPERTY, conditionsTable.indexOf(item));
		checkButton.setSelection(true);
		checkButton.pack();
		item.setData(CHECKBUTTON_PROPERTY, checkButton);
		editor.minimumWidth = checkButton.getSize().x;
		editor.minimumHeight = checkButton.getSize().y-1;
		editor.horizontalAlignment = SWT.LEFT;
		editor.setEditor(checkButton, item, 0);

		
		//clearButton
		editor = new TableEditor(conditionsTable);
		Button clearButton = new Button(conditionsTable, SWT.NONE | SWT.BORDER_SOLID);
		clearButton.setImage(RefactoringImages.getClearIcon());
		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(e.getSource() instanceof Button){
					Button clearB=(Button)e.getSource();
					int row=((Integer)clearB.getData(ROW_PROPERTY)).intValue();
					
					TableItem itemLast = conditionsTable.getItem(conditionsTable.getItemCount()-1);
					// recuperamos los botones check y clear asociados a la
					// última fila
					Object checkBLast=itemLast.getData(CHECKBUTTON_PROPERTY);
					Object clearBLast=itemLast.getData(CLEARBUTTON_PROPERTY);
					
					conditionsTable.setVisible(false);
					
					//eliminamos los botones recuperados
					if(checkBLast instanceof Button)
						((Button)checkBLast).dispose();
					if(clearBLast instanceof Button)
						((Button)clearBLast).dispose();
					//reestablecemos los nombres de las condiciones en las filas correspondientes
					String nameCondition=null;	
					for(int i=row+1;i<conditionsTable.getItemCount();i++){
						nameCondition=conditionsTable.getItem(i).getText(1);
						conditionsTable.getItem(i-1).setText(1,nameCondition);	
					}
					itemLast.dispose();
					if(conditionsTable.getItemCount()==0)
						clearAllButton.setEnabled(false);
					
					conditionsTable.setVisible(true);
					packTableColumns();
					
					catalog.removeConditionFromFilter(filter.get(row));
					filter.remove(row);
					showTree(classCombo.getText());
				}
			}
		});
		clearButton.setData(ROW_PROPERTY, conditionsTable.indexOf(item));
		clearButton.pack();
		item.setData(CLEARBUTTON_PROPERTY, clearButton);
		editor.grabHorizontal=true;
		editor.minimumHeight=15;
		editor.setEditor(clearButton, item, 2);
		
		packTableColumns();
		
		conditionsTable.setVisible(true);
	}
	
	protected void addConditionToFilter(Predicate<DynamicRefactoringDefinition> condition){
		if(condition!=null){
			if(!filter.contains(condition)){
				filter.add(condition);
				addConditionToTable(condition);
				catalog.addConditionToFilter(condition);
				showTree(classCombo.getText());
				searchText.setText(""); // reseteamos el Text al ser la
										// condición válida
				clearAllButton.setEnabled(true);
			}else{
				Object[] messageArgs = {condition.toString()};
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(Messages.RefactoringCatalogBrowserView_SearchConditionAlreadyExist);		
				
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				MessageDialog.openInformation(window.getShell(), 
							Messages.RefactoringCatalogBrowserView_SearchWarning,
							formatter.format(messageArgs));
			}
		}
	}
	

	public void refreshView() {
		// almacenamos la clasificación seleccionada en el classCombo y
		// la refactorización que se muestra en detalle
		String classSelected=classCombo.getText();
		DynamicRefactoringDefinition refSelected=refSummaryPanel.getRefactoringSelected();
		
		//recarga de clasificaciones, refactorizaciones
		loadClassifications();
		loadRefactorings();

		//recargamos el classCombo
		classCombo.removeAll();
		fillClassCombo();
		
		//comprobamos si entre las nuevas clasificaciones se encuentra la que estaba seleccionada 
		Classification c = null;
		boolean found = false;
		
		Iterator<Classification> iter = classifications.iterator();
		while (iter.hasNext() && found == false) {
			c = iter.next();
			if (c.getName().equals(classSelected))
				found = true;
		}
		
		Set<DynamicRefactoringDefinition> drd = 
			new HashSet<DynamicRefactoringDefinition>(refactorings.values());
		if(found){
			//si se encuentra seleccionamos ese item, 
			// mostramos la descripcion y clasificamos según él
			classCombo.setText(classSelected);
			descClassLabel.setText(c.getDescription());
			catalog=new ElementCatalog<DynamicRefactoringDefinition>(drd,c,filter);
		}else{
			//sino mostrar por defecto la clasificacion None
			classCombo.select(0);
			descClassLabel.setText(NONE_CLASSIFICATION.getDescription());
			catalog=new ElementCatalog<DynamicRefactoringDefinition>(
					drd, NONE_CLASSIFICATION, filter);
		}
		showTree(classCombo.getText());
		
		//si habia alguna refactorizacion mostrada se refresca
		if(refSelected!=null && refactorings.containsKey(refSelected.getName())){
			refSummaryPanel.setRefactoringDefinition(
					refactorings.get(refSelected.getName()),
					refactoringLocations.get(refSelected.getName()));
			refSummaryPanel.showRefactoringSummary();
		}
	}

	/**
	 * Muestra el editor de clasificaciones.
	 */
	public void editClassification() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(ClassificationsEditorView.ID);
		} catch (PartInitException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Registra las acciones referentes a la visualización de los contenedores
	 * separados por el spliter.
	 */
	private void loadActionsPane(){

		//actionsPane
		actionsPane=new ArrayList<IAction>();
		
		ArrayList<String> actionsPaneNames=new ArrayList<String>();
		actionsPaneNames.add(ShowLeftPaneViewAction.ID);
		actionsPaneNames.add(ShowRightPaneViewAction.ID);
		actionsPaneNames.add(ShowLeftAndRightPaneViewAction.ID);
		
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	    IContributionItem[] contributionItems=toolBarManager.getItems();
		ActionContributionItem actionItem=null;
		IAction action=null;
		for(IContributionItem item:contributionItems){
			if(item instanceof ActionContributionItem){
				actionItem=(ActionContributionItem)item;
				action=actionItem.getAction();
				if(actionsPaneNames.contains(action.getId())){
					actionsPane.add(action);
				}
			}
		}

	}

	/**
	 * Habilita las acciones referentes a la visualización de los contenedores
	 * que se encuentran deshabilitadas. Además, en caso de no estar
	 * registradas las acciones previamente las registrará.
	 */
	private void enableActionsPane() {
		
		//registra las acciones si no lo estuviesen
		if(actionsPane==null)
			loadActionsPane();
		
		//habilita acciones deshabilitadas
		for(IAction action:actionsPane){
			if(!action.isEnabled())
				action.setEnabled(true);
		}
		
	}

	/**
	 * Muestra el contendor izquierdo en su totalidad ocultando el tanto el
	 * derecho como el spliter que los separa y habilita las acciones referentes
	 * a la visualización de estos, que se encuentran deshabilitadas.
	 */
	public void showLeftPane() {
		sashForm.setMaximizedControl(classComp);
		enableActionsPane();
	}

	/**
	 * Muestra el contendor derecho en su totalidad ocultando el tanto el
	 * izquierdo como el spliter que los separa y habilita las acciones
	 * referentes a la visualización de estos, que se encuentran
	 * deshabilitadas.
	 */
	public void showRightPane() {
		sashForm.setMaximizedControl(refComp);
		enableActionsPane();
	}

	/**
	 * Muestra los dos contenedores separados por el spliter y habilita las
	 * acciones referentes a la visualización de estos, que se encontraban
	 * deshabilitadas.
	 */
	public void showLeftAndRightPane() {
		sashForm.setMaximizedControl(null);
		enableActionsPane();
	}

	/**
	 * Actualiza el árbol de refactorizaciones para representarlas conforme a
	 * la clasificación que ha sido seleccionada en el combo.
	 */
	private class ClassComboSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			Classification classSelected = null;

			Iterator<Classification> iter = classifications.iterator();
			boolean found = false;
			while (iter.hasNext() && found == false) {
				classSelected = iter.next();
				if (classSelected.getName().equals(classCombo.getText()))
					found = true;
			}

			descClassLabel.setText(classSelected.getDescription());
			catalog=
				(ElementCatalog<DynamicRefactoringDefinition>)catalog.newInstance(classSelected);
			showTree(classSelected.getName());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}

	private class SearchTextSelectionListener implements SelectionListener{

		private final char SEARCH_SEPARATOR=':';
		private final char CAT_SEPARATOR='@';
		private Predicate<DynamicRefactoringDefinition> condition;

		@Override
		public void widgetSelected(SelectionEvent e) {
			if(search(searchText.getText())){
				addConditionToFilter(condition);

			}else{
				IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
				MessageDialog.openError(window.getShell(),
						Messages.RefactoringCatalogBrowserView_Error,
						Messages.RefactoringCatalogBrowserView_SearchError);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

		/**
		 * Comprueba que la clasificación y categoria indicadas por parámetro
		 * se encuentra o no disponible. En caso de estar disponible se le
		 * indica al usuario para que pueda elegir si quiere o no añadir esta
		 * condición de filtrado.
		 * 
		 * @param nameClass
		 *            nombre de la clasificación
		 * @param nameCat
		 *            nombre de la categoria
		 * @return
		 */
		private boolean checkAvailableCategoryToAddCondition(String nameClass, String nameCat){
			boolean addCondition=true;
			
			if(!classStore.containsCategoryClassification(new Category(nameClass,nameCat))){
				String message=Messages.RefactoringCatalogBrowserView_SearchCategoryNotExist;
				if(!classStore.containsClassification(nameClass))
					message=Messages.RefactoringCatalogBrowserView_SearchClassificationNotExist;
				
				Object[] messageArgs = {nameClass,nameCat};
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(message);
				
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				addCondition=
					MessageDialog.openQuestion(window.getShell(), 
							Messages.RefactoringCatalogBrowserView_SearchWarning, 
							formatter.format(messageArgs) + 
							Messages.RefactoringCatalogBrowserView_SearchQuestion);
			}
			return addCondition;
		}
		
		private boolean search(String text){
			int indexSearchSep=text.indexOf(SEARCH_SEPARATOR);
			boolean searchOk=false;
			condition=null;

			if(indexSearchSep>0){
				String word=text.substring(0,indexSearchSep).toLowerCase();
				String value=text.substring(indexSearchSep+1);

				if(word.equals(TextCondition.NAME)){
					condition=new TextCondition<DynamicRefactoringDefinition>(value);
					searchOk=true;
				}else{
					if(word.equals(CategoryCondition.NAME)){
						int indexCatSep=value.indexOf(CAT_SEPARATOR);
						if(indexCatSep>0){
							String valueClass=value.substring(0,indexCatSep);
							String valueCat=value.substring(indexCatSep+1);

							searchOk=true;
							
							if(checkAvailableCategoryToAddCondition(valueClass,valueCat))
								condition=new CategoryCondition<DynamicRefactoringDefinition>(valueClass, valueCat);

						}
					}else{
						if(word.equals(KeyWordCondition.NAME)){
							condition=new KeyWordCondition<DynamicRefactoringDefinition>(value);
							searchOk=true;
						}
					}
				}

			}
			return searchOk;

		}
	}

	/**
	 * Recibe notificaciones cuando uno de los elementos de la lista de
	 * refactorizaciones es seleccionado.
	 */
	private class TreeMouseListener implements MouseListener {

		/**
		 * Recibe una notificación de que un elemento del árbol que forma la
		 * vista ha sido presionado doblemente con el ratón.
		 * 
		 * @param e
		 *            el evento de selección disparado en la ventana.
		 * 
		 * @see MouseListener#mouseDoubleClick(MouseEvent)
		 */
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			TreeItem[] selection = refactoringsTree.getSelection();

			String selectedName = selection[0].getText();
			DynamicRefactoringDefinition refSelected=refactorings.get(selectedName);

			// comprobamos si se trata de una refactorización
			if(refSelected!=null){
				refSummaryPanel.setRefactoringDefinition(refSelected, refactoringLocations.get(selectedName));
				refSummaryPanel.showRefactoringSummary();
			}

		}

		/**
		 * @see MouseListener#mouseDown(MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent e) {
		}

		/**
		 * @see MouseListener#mouseUp(MouseEvent)
		 */
		@Override
		public void mouseUp(MouseEvent e) {
		}
	}

}

