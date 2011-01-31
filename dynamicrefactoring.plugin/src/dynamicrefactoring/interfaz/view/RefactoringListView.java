package dynamicrefactoring.interfaz.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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

import com.swtdesigner.ResourceManager;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.domain.Scope;
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
 * refactorizaciones asi como la informaci�n asociada a las mismas. Sobre estas
 * se pueden realizar clasificaciones y filtros.
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
	 * Se utiliza como clave el nombre de la refactorizaci�n y como valor la
	 * propia representaci�n de la definici�n de la refactorizaci�n.
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
	 * Cat�logo que esta siendo utilizado conforme a las clasificaci�n
	 * seleccionada.
	 */
	private ElementCatalog<DynamicRefactoringDefinition> catalog;

	// TODO: Mantener una lista de predicados construidos a partir de los
	// filtros
	// List<Predicate>

	/**
	 * Etiqueta clasificaci�n.
	 */
	private Label classLabel;

	/**
	 * Combo que contiene los tipos de clasificaciones de refactorizaciones
	 * disponibles.
	 */
	private Combo classCombo;

	/**
	 * Etiqueta con la descripci�n de la clasificaci�n
	 */
	private Label descClassLabel;

	/**
	 * Cuadro de texto que permite introducir al usuario el patr�n de b�squeda.
	 */
	private Text searchText;

	/**
	 * Bot�n que permite activar un proceso de b�squeda al usuario.
	 */
	private Button searchButton;

	/**
	 * �rbol sobre el que se mostrar�n de forma estructurada las diferentes
	 * refactorizaciones conforme a la clasificaci�n seleccionada y los filtros
	 * aplicados.
	 */
	private Tree refactoringsTree;

	// TODO: conseguir que los componentes queden bien presentados (layout)
	// TODO: por defecto marcado en el combo None y mostrar asi el arbol??
	/**
	 * Crea los controles SWT para este componente del espacio de trabajo.
	 * 
	 * @param parent
	 *            el control padre.
	 */
	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.RefactoringListView_title);

		loadClassifications();
		loadRefactorings();
		createCatalog();

		classLabel = new Label(parent, SWT.LEFT);
		classLabel.setText(Messages.RefactoringListView_Classification + ": "); //$NON-NLS-1$

		classCombo = new Combo(parent, SWT.READ_ONLY);
		classCombo.add(Category.NONE_CATEGORY.getName());
		classCombo
				.setToolTipText(Messages.RefactoringListView_SelectFromClassification);
		classCombo.addSelectionListener(new ClassComboSelectionListener());

		Collections.sort(classifications);
		for (Classification classification : classifications)
			classCombo.add(classification.getName());

		// a�adimos la clasificacion por defecto
		Set<Category> catNone = new HashSet<Category>();
		catNone.add(Category.NONE_CATEGORY);
		classifications.add(NONE_CLASSIFICATION);

		descClassLabel = new Label(parent, SWT.LEFT);
		descClassLabel.setText(Messages.RefactoringListView_Label_Classification_Description);

		final Composite composite_1 = new Composite(parent, SWT.NONE);
		final FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(0, 245);
		fd_composite_1.top = new FormAttachment(0, 5);
		fd_composite_1.left = new FormAttachment(0, 5);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FormLayout());

		Label search = new Label(composite_1, SWT.NONE);
		final FormData fd_search = new FormData();
		fd_search.bottom = new FormAttachment(0, 58);
		fd_search.top = new FormAttachment(0, 29);
		fd_search.left = new FormAttachment(0, 15);
		search.setLayoutData(fd_search);

		searchText = new Text(composite_1, SWT.BORDER);
		final FormData fd_tsearch = new FormData();
		fd_tsearch.right = new FormAttachment(0, 185);
		fd_tsearch.top = new FormAttachment(0, 28);
		fd_tsearch.left = new FormAttachment(search, 0, SWT.RIGHT);
		searchText.setMessage("Search"); //$NON-NLS-1$
		searchText.setLayoutData(fd_tsearch);

		searchButton = new Button(composite_1, SWT.PUSH);
		final FormData fd_bsearch = new FormData();
		fd_bsearch.right = new FormAttachment(0, 225);
		fd_bsearch.top = new FormAttachment(0, 26);// 28
		fd_bsearch.left = new FormAttachment(searchText, 3, SWT.RIGHT);
		searchButton.setLayoutData(fd_bsearch);
		searchButton.setImage(ResourceManager.getPluginImage(
				RefactoringPlugin.getDefault(),
				"icons" + System.getProperty("file.separator") + "search.png")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		searchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				search(searchText.getText());
			}

			private void search(String text) {
				String text_aux = null;
				if (text.toLowerCase().contains("category:")) { //$NON-NLS-1$
					text_aux = text.substring(9);
					System.out.println("categoria"); //$NON-NLS-1$
					System.out.println(text_aux);
					catalog.addConditionToFilter(new CategoryCondition<DynamicRefactoringDefinition>(
							"scope", text_aux)); //$NON-NLS-1$
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
		refactoringsTree = new Tree(parent, SWT.NULL);
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
		XmlClassificationsReader classReader = ClassificationsReaderFactory
				.getReader(ClassificationsReaderFactory.ClassificationsReaderTypes.JDOM_READER);
		try {
			classifications = new ArrayList<Classification>(
					classReader
							.readClassifications(RefactoringConstants.CLASSIFICATION_TYPES_FILE));
		} catch (ValidationException e) {
			e.printStackTrace();
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			String message = Messages.RefactoringListView_ClassificationsNotListed
					+ ".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(window.getShell(),
					Messages.RefactoringListView_Error, message);
		}
	}

	/**
	 * Carga la lista de refactorizaciones din�micas disponibles.
	 */
	private void loadRefactorings() {
		DynamicRefactoringLister listing = DynamicRefactoringLister
				.getInstance();

		try {
			// Se obtiene la lista de todas las refactorizaciones disponibles.
			HashMap<String, String> allRefactorings = listing
					.getDynamicRefactoringNameList(
							RefactoringPlugin.getDynamicRefactoringsDir(),
							true, null);

			refactoringNames = new ArrayList<String>();
			refactoringLocations = new HashMap<String, String>();
			refactorings = new HashMap<String, DynamicRefactoringDefinition>();

			for (Map.Entry<String, String> nextRef : allRefactorings.entrySet()) {

				try {
					// Se obtiene la definici�n de la siguiente refactorizaci�n.
					DynamicRefactoringDefinition definition = DynamicRefactoringDefinition
							.getRefactoringDefinition(nextRef.getValue());

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
					String message = Messages.RefactoringListView_NotAllListed
							+ ".\n" + e.getMessage(); //$NON-NLS-1$
					logger.error(message);
					MessageDialog.openError(window.getShell(),
							Messages.RefactoringListView_Error, message);
				}
			}
		} catch (IOException e) {
			logger.error(Messages.RefactoringListView_AvailableNotListed
					+ ".\n" + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Crea el cat�logo por defecto para las refactorizaciones disponibles.
	 */
	private void createCatalog(){
		Set<DynamicRefactoringDefinition> drd = new HashSet<DynamicRefactoringDefinition>(refactorings.values());
		catalog=new ElementCatalog<DynamicRefactoringDefinition>(
				drd, NONE_CLASSIFICATION);
	}

	/**
	 * Muestra en forma de �rbol la clasificaci�n de las refactorizaciones seg�n
	 * las categorias a las que pertenece, pudiendo aparecer en un grupo de
	 * filtrados si no cumplen con las condiciones establecidas.
	 * 
	 * @param classificationName
	 *            nombre de la clasificaci�n
	 */
	private void showTree(String classificationName) {

		RefactoringTreeManager.cleanTree(refactoringsTree);

		ClassifiedElements<DynamicRefactoringDefinition> classifiedElements = catalog
				.getClassificationOfElements(true);

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

		for (Category c : categories) {
			dRefactDef = new ArrayList<DynamicRefactoringDefinition>(
					classifiedElements.getCategoryChildren(c));
			Collections.sort(dRefactDef);
			if (!c.equals(Category.FILTERED_CATEGORY)) {
				catTreeItem = classTreeItem;
				if (!(classificationName
						.equalsIgnoreCase(Category.NONE_CATEGORY.getName()) && c
						.equals(Category.NONE_CATEGORY))
						&& dRefactDef.size() > 0) {
					catTreeItem = TreeEditor.createBranch(classTreeItem,
							orderInBranchCat, c.getName(),
							"icons" + System.getProperty("file.separator") //$NON-NLS-1$ //$NON-NLS-2$
									+ "cat.gif"); //$NON-NLS-1$
					orderInBranchCat++;
				}
				createTreeItemFromParent(dRefactDef, catTreeItem, false);
			} else {
				if (dRefactDef.size() > 0) {
					filTreeItem = TreeEditor.createBranch(refactoringsTree,
							orderInBranchClass,
							Category.FILTERED_CATEGORY.getName(), "icons" //$NON-NLS-1$
									+ System.getProperty("file.separator") //$NON-NLS-1$
									+ "fil.png"); //$NON-NLS-1$
					orderInBranchClass++;
					filTreeItem.setForeground(Display.getCurrent()
							.getSystemColor(SWT.COLOR_DARK_GRAY));
					createTreeItemFromParent(dRefactDef, filTreeItem, true);
				}
			}
		}
		classTreeItem.setExpanded(true);
	}

	/**
	 * Crea una representaci�n en formato de arbol de cada una de las
	 * refactorizaciones, agregandolas al arbol que se pasa. En caso de tratarse
	 * de una refactorizaci�n filtrada pone de color gris el texto de todos sus
	 * componentes.
	 * 
	 * @param dRefactDef
	 *            lista de refactorizaciones
	 * @param catTreeItem
	 * @param filtered
	 *            indicador de filtrado. Si es verdadero se trata de
	 *            refactorizaciones filtradas, falso en caso contrario.
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
			RefactoringTreeManager.setForegroundTreeItem(catTreeItem, Display
					.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
	}

	/**
	 * Actualiza el �rbol de refactorizaciones para representarlas conforme a la
	 * clasificaci�n que ha sido seleccionada en el combo.
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

			// TODO:hay que implementar esto cuando lo tengamos:
			// poner la descripcion en descClassLabel
			catalog = (ElementCatalog<DynamicRefactoringDefinition>) catalog
					.newInstance(classSelected);
			showTree(classSelected.getName());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}
}
