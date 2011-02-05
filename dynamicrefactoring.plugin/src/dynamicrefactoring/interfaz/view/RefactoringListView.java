package dynamicrefactoring.interfaz.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Predicate;
import com.swtdesigner.ResourceManager;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.imp.ElementCatalog;
import dynamicrefactoring.domain.metadata.imp.SimpleUniLevelClassification;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.domain.metadata.interfaces.Classification;
import dynamicrefactoring.domain.metadata.interfaces.ClassifiedElements;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.plugin.xml.classifications.XmlClassificationsReader;
import dynamicrefactoring.plugin.xml.classifications.imp.ClassificationsReaderFactory;
import dynamicrefactoring.util.DynamicRefactoringLister;
import dynamicrefactoring.util.RefactoringTreeManager;

/**
 * Proporcia una vista de Eclipse la cual muestra la lista de todas las
 * refactorizaciones asi como la información asociada a las mismas.
 * Sobre estas se pueden realizar clasificaciones y filtros.
 */
public class RefactoringListView extends ViewPart {

	/**
	 * Clasificacion cuya unica categoria es NONE_CATEGORY.
	 */
	private static final Classification NONE_CLASSIFICATION;

	static {
		Set<Category> catNone = new HashSet<Category>();
		catNone.add(Category.NONE_CATEGORY);
		NONE_CLASSIFICATION = new SimpleUniLevelClassification(
				Category.NONE_CATEGORY.getName(),
				Messages.RefactoringListView_None_Classification_Description, catNone);
	}

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringListView.class);

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
	// TODO: revisar si no es necesario para eliminarlo
	private HashMap<String, String> refactoringLocations;

	/**
	 * Nombres de las refactorizaciones disponibles.
	 */
	// TODO: revisar si no es necesario para eliminarlo
	private ArrayList<String> refactoringNames;

	/**
	 * Clasificaciones disponibles.
	 */
	private ArrayList<Classification> classifications;

	/**
	 * Catálogo que esta siendo utilizado conforme a la clasificación seleccionada.
	 */
	private ElementCatalog<DynamicRefactoringDefinition> catalog;

	/**
	 * Lista de condiciones que conforman el filtro actual aplicado.
	 */
	//TODO: revisar si no es necesario para eliminarlo
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
	 * Cuadro de texto que permite introducir al usuario el patrón de búsqueda.
	 */
	private Text searchText;

	/**
	 * Botón que permite activar un proceso de búsqueda al usuario.
	 */
	private Button searchButton;

	/**
	 * Árbol sobre el que se mostrarán de forma estructurada las diferentes refactorizaciones 
	 * conforme a la clasificación seleccionada y los filtros aplicados.
	 */
	private Tree refactoringsTree;

	/**
	 * CheckBox que permite seleccionar al usuario si desea que se muestren en el arbol
	 * también las refactorizaciones filtradas.
	 */
	private Button filteredButton;

	/**
	 * Crea los controles SWT para este componente del espacio de trabajo.
	 * 
	 * @param parent
	 *            el control padre.
	 */
	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.RefactoringListView_title);
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
	    SashForm sashForm = new SashForm(scrolledComp, SWT.HORIZONTAL | SWT.SMOOTH |SWT.BORDER);
	    sashForm.setSashWidth(2);
	    sashForm.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		
	    //sashForm Left: classComp
		FormData classFormData=null;
		final Composite classComp = new Composite(sashForm, SWT.NONE);
		classComp.setLayout(new FormLayout());
		
		//sashForm Rigth: refComp
		FormData refFormData=null;
		final Composite refComp = new Composite(sashForm, SWT.NONE);
		refComp.setLayout(new FormLayout());
		
		//TODO: ELIMINAR; ES UNA PRUEBA
		classLabel=new Label(refComp, SWT.LEFT);
		classLabel.setText("Adios");
		refFormData=new FormData();
		refFormData.top=new FormAttachment(0,10);
		refFormData.left=new FormAttachment(0,5);
		classLabel.setLayoutData(refFormData);

		//classLabel
		classLabel=new Label(classComp, SWT.LEFT);
		classLabel.setText(Messages.RefactoringListView_Classification);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(0,10);
		classFormData.left=new FormAttachment(0,5);
		classLabel.setLayoutData(classFormData);

		//classCombo
		classCombo=new Combo(classComp, SWT.READ_ONLY);
		classCombo.setToolTipText(Messages.RefactoringListView_SelectFromClassification);

		//añadimos la clasificacion por defecto
		classCombo.add(NONE_CLASSIFICATION.getName());
		classifications.add(NONE_CLASSIFICATION);

		Collections.sort(classifications);
		for (Classification classification : classifications)
			classCombo.add(classification.getName());

		classFormData=new FormData();
		classFormData.top=new FormAttachment(0,5);
		classFormData.left=new FormAttachment(classLabel,15);
		classFormData.width=100;
		classCombo.setLayoutData(classFormData);
		classCombo.addSelectionListener(new ClassComboSelectionListener());

		//descClassLabel
		descClassLabel=new Label(classComp, SWT.LEFT);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(classLabel,10);
		classFormData.left=new FormAttachment(0,5);
		descClassLabel.setLayoutData(classFormData);

		//searchText
		searchText = new Text(classComp, SWT.BORDER);
		searchText.setMessage(Messages.RefactoringListView_Search);
		classFormData = new FormData();
		classFormData.top = new FormAttachment(0, 6);
		classFormData.left = new FormAttachment(classCombo, 175);
		classFormData.width=150;
		searchText.setLayoutData(classFormData);

		//searchButton 
		searchButton = new Button(classComp, SWT.PUSH);
		searchButton.setImage(ResourceManager.getPluginImage(
				RefactoringPlugin.getDefault(),
				"icons" + System.getProperty("file.separator") + "search.png"));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		classFormData = new FormData();
		classFormData.top = new FormAttachment(0, 3);
		classFormData.left = new FormAttachment(searchText, 5);
		searchButton.setLayoutData(classFormData);
		searchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				search(searchText.getText());
			}

			private void search(String text) {
				String text_aux = null;
				if (text.toLowerCase().contains("category:")) { //$NON-NLS-1$
					text_aux = text.substring(9);
					CategoryCondition<DynamicRefactoringDefinition> catCondition=
						new CategoryCondition<DynamicRefactoringDefinition>(
								"scope", text_aux); //$NON-NLS-1$
					filter.add(catCondition);
					catalog.addConditionToFilter(catCondition);
					showTree(classCombo.getText());
				} else {
					// if(text.toLowerCase().contains("name:")){
					// text_aux=text.substring(5);
					// System.out.println("nombre");
					// System.out.println(text_aux);
					// scopeCatalog.addConditionToFilter(new
					// NameContainsTextCondition<DynamicRefactoringDefinition>(
					// text_aux));
					// }
				}

			}
		});


		//refactoringTree
		refactoringsTree = new Tree(classComp, SWT.BORDER);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(descClassLabel,5);
		classFormData.left=new FormAttachment(0,5);
		classFormData.right=new FormAttachment(100,-5);
		refactoringsTree.setLayoutData(classFormData);

		//filteredButton
		filteredButton = new Button(classComp, SWT.CHECK | SWT.CENTER);
		filteredButton.setText(Messages.RefactoringListView_ShowFiltered);
		filteredButton.setSelection(true);
		classFormData=new FormData();
		classFormData.top=new FormAttachment(refactoringsTree,5);
		classFormData.left=new FormAttachment(0,15);
		filteredButton.setLayoutData(classFormData);
		filteredButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(!filter.isEmpty())
					showTree(catalog.getClassification().getName());	
			}
		});

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
		XmlClassificationsReader classReader = 
			ClassificationsReaderFactory.getReader(
					ClassificationsReaderFactory.ClassificationsReaderTypes.JDOM_READER);
		try {
			classifications = new ArrayList<Classification>(
					classReader.readClassifications(RefactoringConstants.CLASSIFICATION_TYPES_FILE));
		} catch (ValidationException e) {
			e.printStackTrace();
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			String message = Messages.RefactoringListView_ClassificationsNotListed + 
			".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(window.getShell(),
					Messages.RefactoringListView_Error, message);
		}
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

			refactoringNames = new ArrayList<String>();
			refactoringLocations = new HashMap<String, String>();
			refactorings = new HashMap<String, DynamicRefactoringDefinition>();

			for (Map.Entry<String, String> nextRef : allRefactorings.entrySet()) {

				try {
					// Se obtiene la definición de la siguiente refactorización.
					DynamicRefactoringDefinition definition = 
						DynamicRefactoringDefinition.getRefactoringDefinition(
								nextRef.getValue());

					if (definition != null && definition.getName() != null) {
						refactorings.put(definition.getName(), definition);
						refactoringLocations.put(definition.getName(),
								nextRef.getValue());
						refactoringNames.add(definition.getName());
					}
				} catch (RefactoringException e) {
					e.printStackTrace();
					IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
					String message = 
						Messages.RefactoringListView_NotAllListed + 
						".\n" + e.getMessage(); //$NON-NLS-1$
					logger.error(message);
					MessageDialog.openError(window.getShell(),
							Messages.RefactoringListView_Error, message);
				}
			}
		} catch (IOException e) {
			logger.error(Messages.RefactoringListView_AvailableNotListed + 
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
	 * Muestra en forma de árbol la clasificación de las refactorizaciones según
	 * las categorias a las que pertenece, pudiendo aparecer en un grupo de filtrados
	 * si no cumplen con las condiciones establecidas. 
	 * @param classificationName nombre de la clasificación
	 */
	private void showTree(String classificationName) {

		RefactoringTreeManager.cleanTree(refactoringsTree);

		ClassifiedElements<DynamicRefactoringDefinition> classifiedElements = 
			catalog.getClassificationOfElements(filteredButton.getSelection());

		int orderInBranchClass = 0;
		TreeItem classTreeItem = TreeEditor.createBranch(refactoringsTree,
				orderInBranchClass, classificationName,
				"icons" + System.getProperty("file.separator") + "class.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		orderInBranchClass++;

		ArrayList<Category> categories = new ArrayList<Category>(
				classifiedElements.getClassification().getCategories());
		Collections.sort(categories);

		ArrayList<DynamicRefactoringDefinition> dRefactDef = null;
		TreeItem catTreeItem, filTreeItem = null;
		int orderInBranchCat = 0;

		for (Category c : categories){
			dRefactDef=new ArrayList<DynamicRefactoringDefinition>(
					classifiedElements.getCategoryChildren(c));
			Collections.sort(dRefactDef);
			if (!c.equals(Category.FILTERED_CATEGORY)){
				catTreeItem = classTreeItem;
				if ( !(classificationName.equals(NONE_CLASSIFICATION.getName()) && 
						c.equals(Category.NONE_CATEGORY))
						&& dRefactDef.size()>0){
					catTreeItem = TreeEditor.createBranch(classTreeItem,
							orderInBranchCat, c.getName(),
							"icons" + System.getProperty("file.separator")+ "cat.gif"); //$NON-NLS-1$ //$NON-NLS-2$
					orderInBranchCat++;
				}
				createTreeItemFromParent(dRefactDef, catTreeItem, false);
			}else{
				if(dRefactDef.size()>0){
					filTreeItem = TreeEditor.createBranch(refactoringsTree,
							orderInBranchClass, Category.FILTERED_CATEGORY.getName(), 
							"icons" + System.getProperty("file.separator") + "fil.png"); //$NON-NLS-1$
					orderInBranchClass++;
					filTreeItem.setForeground(
							Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
					createTreeItemFromParent(dRefactDef, filTreeItem, true);
				}
			}
		}
		classTreeItem.setExpanded(true);
	}

	/**
	 * Crea una representación en formato de arbol de cada una de las refactorizaciones,
	 * agregandolas al arbol que se pasa. En caso de tratarse de una refactorización
	 * filtrada pone de color gris el texto de todos sus componentes.
	 * @param dRefactDef lista de refactorizaciones
	 * @param catTreeItem 
	 * @param filtered indicador de filtrado. Si es verdadero se trata de
	 * 			refactorizaciones filtradas, falso en caso contrario.
	 */
	private void createTreeItemFromParent(
			ArrayList<DynamicRefactoringDefinition> dRefactDef,
			TreeItem catTreeItem, boolean filtered) {
		int orderInBranchRef = 0;
		for (DynamicRefactoringDefinition ref : dRefactDef) {
			RefactoringTreeManager
			.createRefactoringDefinitionTreeItemFromParentTreeItem(
					orderInBranchRef, ref, catTreeItem);
			orderInBranchRef++;
		}
		if (filtered)
			RefactoringTreeManager.setForegroundTreeItem(catTreeItem, 
					Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
	}

	/**
	 * Actualiza el árbol de refactorizaciones para representarlas conforme
	 * a la clasificación que ha sido seleccionada en el combo.
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
			catalog=(ElementCatalog<DynamicRefactoringDefinition>) 
			catalog.newInstance(classSelected);
			showTree(classSelected.getName());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}
}
