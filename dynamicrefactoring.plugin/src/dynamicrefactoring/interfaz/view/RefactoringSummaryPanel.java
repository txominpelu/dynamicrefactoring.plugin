package dynamicrefactoring.interfaz.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.metadata.condition.CategoryCondition;
import dynamicrefactoring.domain.metadata.condition.KeyWordCondition;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.util.RefactoringTreeManager;

/**
 * Proporciona un organizador de pestañas, en el cual se muestra la
 * información relativa a la refactorización.
 * 
 * @author XPMUser
 */
public class RefactoringSummaryPanel {

	/**
	 * Etiqueta título.
	 */
	private Label titleLabel;

	/**
	 * Número mínimo de pestañas, es el número de pestañas fijas a mostrar.
	 */
	private int minNumTabs;

	/**
	 * Organizador de pestañas.
	 */
	private TabFolder refTabFolder;

	/**
	 * Etiqueta descripción de la refactorización.
	 */
	private Label descriptionLabel;

	/**
	 * Cuadro de texto en que se mostrará la descripción de la
	 * refactorización.
	 */
	private Text descriptionText;

	/**
	 * Etiqueta motivación de la refactorización.
	 */
	private Label motivationLabel;

	/**
	 * Cuadro de texto en que se mostrará la motivación de la
	 * refactorización.
	 */
	private Text motivationText;
	
	private FormToolkit toolkit;
	private Section keyWordsSection;
	private Section categoriesSection;

	/**
	 * Propiedad asociada a las filas de la tabla que indica qué botón check
	 * tienen asociado cada una.
	 */
	private final String CHECKBUTTON_PROPERTY = "checkButton"; //$NON-NLS-1$

	/**
	 * Tabla en que se mostrarán las entradas de la refactorización.
	 */
	private Table inputsTable;

	/**
	 * Árbol sobre el que se mostrarán de forma estructurada los diferentes
	 * elementos del repositorio que componen la refactorización
	 * (precondiciones, acciones y postcondiciones).
	 */
	private Tree componentsTree;

	/**
	 * Área en que se muestra la imagen asociada a la refactorización.
	 */
	private Canvas imageCanvas ;
	
	private ArrayList<Link> examplesLink;
	private SourceViewerDialog sourceViewer;

	/**
	 * Definición de la refactorización.
	 */
	private DynamicRefactoringDefinition refactoring;
	
	private RefactoringCatalogBrowserView rcbView;
	
	
	public RefactoringSummaryPanel(Composite parent, RefactoringCatalogBrowserView rcbView){

		this.rcbView=rcbView;
		examplesLink=new ArrayList<Link>();
		
		FormData refFormData=null;

		//titleLabel
		titleLabel=new Label(parent, SWT.CENTER );
		titleLabel.setFont(new Font(null, "Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
		refFormData=new FormData();
		refFormData.top=new FormAttachment(0,10);
		refFormData.left=new FormAttachment(0,5);
		refFormData.right=new FormAttachment(100,-5);
		titleLabel.setLayoutData(refFormData);

		//refTabFolder
		refTabFolder=new TabFolder(parent, SWT.NONE);
		refFormData = new FormData();
		refFormData.top = new FormAttachment(titleLabel, 9);
		refFormData.left = new FormAttachment(0, 5);
		refFormData.right=new FormAttachment(100, -5);
		refFormData.bottom=new FormAttachment(100, -5);
		refTabFolder.setLayoutData(refFormData);
		refTabFolder.setVisible(false);

		//TabItems
		createOverviewTabItem();
		createInputsTabItem();
		createMechanismTabItem();

		sourceViewer=new SourceViewerDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		minNumTabs=refTabFolder.getItemCount();
	}

	private void createOverviewTabItem(){

		//comp
		final Composite comp = new Composite(refTabFolder, SWT.NONE);
		GridLayout g=new GridLayout();
		g.numColumns=1;
		g.marginHeight=10;
		g.marginWidth=10;
		g.verticalSpacing=10;
		comp.setLayout(g);
		
		//descriptionComp
		final Composite descriptionComp = new Composite(comp, SWT.NONE);
		GridData gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		descriptionComp.setLayoutData(gd);
		descriptionComp.setLayout(new GridLayout());
		
		descriptionLabel = new Label(descriptionComp, SWT.LEFT);
		descriptionLabel.setText(Messages.RefactoringSummaryPanel_Description);
		
		descriptionText = new Text(descriptionComp, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		descriptionText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		descriptionText.setEditable(false);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		descriptionText.setLayoutData(gd);

		//motivationComp
		final Composite motivationComp = new Composite(comp, SWT.NONE);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		motivationComp.setLayoutData(gd);
		motivationComp.setLayout(new GridLayout());
		
		motivationLabel = new Label(motivationComp, SWT.CENTER);
		motivationLabel.setText(Messages.RefactoringSummaryPanel_Motivation);
		
		motivationText = new Text(motivationComp, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		motivationText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		motivationText.setEditable(false);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		motivationText.setLayoutData(gd);

		
		//toolkitComp
		final Composite toolkitComp = new Composite(comp, SWT.NONE);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		toolkitComp.setLayoutData(gd);
		toolkitComp.setLayout(new GridLayout());
		
		toolkit = new FormToolkit(toolkitComp.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(toolkitComp);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		g=new GridLayout();
		g.marginHeight=0;
		g.marginWidth=0;
		form.setLayoutData(gd);
		form.setLayout(g);
		form.getBody().setLayoutData(gd);
		form.getBody().setLayout(g);

		//categoriesSection 
		categoriesSection = toolkit.createSection(form.getBody(), 
				Section.TITLE_BAR|Section.TWISTIE|Section.EXPANDED);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		categoriesSection.setLayoutData(gd);
		categoriesSection.setLayout(new GridLayout());
		categoriesSection.setText(Messages.RefactoringSummaryPanel_Categories);
		categoriesSection.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		toolkit.createCompositeSeparator(categoriesSection);
		Composite catSectionClient = toolkit.createComposite(categoriesSection);
		ColumnLayout cl = new ColumnLayout();
		cl.maxNumColumns = 4;
		catSectionClient.setLayout(cl);
		categoriesSection.setClient(catSectionClient);
				
		//keyWordsSection 
		keyWordsSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR|Section.TWISTIE|Section.EXPANDED);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		keyWordsSection.setLayoutData(gd);
		keyWordsSection.setLayout(new GridLayout());
		keyWordsSection.setText(Messages.RefactoringSummaryPanel_KeyWords);
		keyWordsSection.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		toolkit.createCompositeSeparator(keyWordsSection);
		Composite kwSectionClient = toolkit.createComposite(keyWordsSection);
		kwSectionClient.setLayout(cl);
		keyWordsSection.setClient(kwSectionClient);
		
		TabItem item = new TabItem (refTabFolder, SWT.NONE);
		item.setText(Messages.RefactoringSummaryPanel_Overview);
		item.setControl(comp);
	}
	
	private void createInputsTabItem(){
		inputsTable = new Table(refTabFolder, SWT.BORDER);
		inputsTable.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		inputsTable.setLinesVisible(true);
		inputsTable.setHeaderVisible(true);

		//se crean las columnas de la tabla	
		TableColumn nameCol = new TableColumn(inputsTable, SWT.NONE);
		nameCol.setText(Messages.RefactoringSummaryPanel_Name);
		TableColumn typeCol = new TableColumn(inputsTable, SWT.NONE);
		typeCol.setText(Messages.RefactoringSummaryPanel_Type);
		TableColumn fromCol = new TableColumn(inputsTable, SWT.NONE);
		fromCol.setText(Messages.RefactoringSummaryPanel_From);
		TableColumn rootCol = new TableColumn(inputsTable, SWT.NONE);
		rootCol.setResizable(false);
		rootCol.setText(Messages.RefactoringSummaryPanel_Main);
		rootCol.setToolTipText(Messages.RefactoringSummaryPanel_MainTooltip);

		TabItem item = new TabItem (refTabFolder, SWT.NONE);
		item.setText(Messages.RefactoringSummaryPanel_Inputs);
		item.setControl(inputsTable);
	}

	private void createMechanismTabItem(){
		componentsTree=new Tree(refTabFolder, SWT.BORDER);

		TabItem item = new TabItem (refTabFolder, SWT.NONE);
		item.setText(Messages.RefactoringSummaryPanel_Mechanism);
		item.setControl(componentsTree);
	}

	private void createAndFillImageTabItem(){

		ScrolledComposite scroller = new ScrolledComposite(refTabFolder, SWT.V_SCROLL | SWT.H_SCROLL );

		imageCanvas = new Canvas(scroller, SWT.NONE);
		imageCanvas.setRedraw(true);
		imageCanvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		ImageLoader loader = new ImageLoader();
		final Image image = new Image(imageCanvas.getDisplay(), loader.load(refactoring.getImageAbsolutePath())[0]);

		final int margin=10;
		imageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, margin, margin);
			}
		});

		scroller.setContent(imageCanvas);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		scroller.setMinSize(new Point(image.getBounds().width + (2*margin), 
				image.getBounds().height + (2*margin)));
		scroller.setShowFocusedControl(true);

		TabItem item = new TabItem (refTabFolder, SWT.NONE);
		item.setText(Messages.RefactoringSummaryPanel_Image);
		item.setControl(scroller);
	}

	private void createAndFillExamplesTabItem(){
		
		//comp
		final Composite comp = new Composite(refTabFolder, SWT.NONE);
		GridLayout g=new GridLayout();
		g.numColumns=1;
		g.marginHeight=20;
		g.marginWidth=10;
		g.verticalSpacing=10;
		comp.setLayout(g);
		
		Link exLink=null;
		final List<RefactoringExample> examples = refactoring.getExamplesAbsolutePath();
		int numEx=1;
		
		for(final RefactoringExample ex: examples){
			exLink = new Link(comp, SWT.NONE);
			exLink.setText("<a>"+ //$NON-NLS-1$
					Messages.RefactoringSummaryPanel_ExampleLink+
					" " + numEx + "</a>"); //$NON-NLS-1$
			exLink.setToolTipText(Messages.RefactoringSummaryPanel_ExampleLinkToolTip);
			exLink.addListener (SWT.Selection, new Listener () {
				public void handleEvent(Event event) {
					if(sourceViewer.loadSources(
							refactoring.getName(),
							ex.getBefore(), ex.getAfter())){
						sourceViewer.open();
					}
				}
			});
			examplesLink.add(exLink);
			numEx++;
		}
		
		TabItem item = new TabItem (refTabFolder, SWT.NONE);
		item.setText(Messages.RefactoringSummaryPanel_Examples);
		item.setControl(comp);
	}
	
	private void clear(){

		//examplesLink
		for(Link exLink: examplesLink)
			exLink.dispose();
		
		//hyperLinks
		Control hyperLinks[]=null;
		hyperLinks=((Composite)categoriesSection.getClient()).getChildren();
		for(int i=0;i<hyperLinks.length;i++)
			hyperLinks[i].dispose();
		
		hyperLinks=((Composite)keyWordsSection.getClient()).getChildren();
		for(int i=0;i<hyperLinks.length;i++)
			hyperLinks[i].dispose();
		
		//componentsTree
		RefactoringTreeManager.cleanTree(componentsTree);
		
		//inputsTable
		if(inputsTable.getItemCount()>0){
			TableItem[] items=inputsTable.getItems();
			for(int i=items.length-1; i>=0; i--){
				// recuperamos el botón check asociado a la fila para eliminarlo
				Object checkB=items[i].getData(CHECKBUTTON_PROPERTY);
				if(checkB instanceof Button)
					((Button)checkB).dispose();
				items[i].dispose();
			}
		}
	
		//ImageTab
		//ExamplesTab	
		while(minNumTabs!=refTabFolder.getItemCount()){
			refTabFolder.getItem(minNumTabs).dispose();
		}
	}

	private void fillOverview(){
		descriptionText.setText(refactoring.getDescription().trim());
		motivationText.setText(refactoring.getMotivation().trim());

		categoriesSection.setVisible(false);
		ArrayList<Category> categories = new ArrayList<Category>(refactoring.getCategories());
		Collections.sort(categories);
		Hyperlink catHyperlink=null;
		for(Category c : categories){
			catHyperlink = toolkit.createHyperlink((Composite)categoriesSection.getClient(),
								c.toString(),SWT.WRAP);
			catHyperlink.setData(c);
			catHyperlink.addHyperlinkListener(new HyperlinkAdapter(){
				public void linkActivated(HyperlinkEvent e){
					if(e.getSource() instanceof Hyperlink){
						Hyperlink hlink=(Hyperlink)e.getSource();
						Category c=(Category)hlink.getData();
						rcbView.addConditionToFilter(new CategoryCondition<DynamicRefactoringDefinition>(c));
					}
				}
			});
		}
		categoriesSection.setExpanded(!categories.isEmpty());
		categoriesSection.setVisible(true);
		
		keyWordsSection.setVisible(false);
		ArrayList<String> keyWords = new ArrayList<String>(refactoring.getKeywords());
		Collections.sort(keyWords);
		Hyperlink kwHyperlink=null;
		for(String kw : keyWords){
			kwHyperlink = toolkit.createHyperlink((Composite)keyWordsSection.getClient(),
							kw.toString(),SWT.WRAP);
			kwHyperlink.setText(kw);
			kwHyperlink.addHyperlinkListener(new HyperlinkAdapter(){
				public void linkActivated(HyperlinkEvent e){
					if(e.getSource() instanceof Hyperlink){
						Hyperlink hlink=(Hyperlink)e.getSource();
						rcbView.addConditionToFilter(new KeyWordCondition<DynamicRefactoringDefinition>(hlink.getText()));
					}
				}
			});
		}
		keyWordsSection.setExpanded(!keyWords.isEmpty());
		keyWordsSection.setVisible(true);
		
	}
	
	private void fillInputsTable(){
		List<InputParameter> inputs = refactoring.getInputs();
		Button checkButton=null;
		TableEditor editor;
		for(InputParameter input : inputs){
			TableItem item=new TableItem(inputsTable, SWT.BORDER);
			item.setText(new String[]{input.getName(), input.getType(), input.getFrom(), "", ""}); //$NON-NLS-1$ //$NON-NLS-2$

			editor = new TableEditor(inputsTable);
			checkButton = new Button(inputsTable, SWT.CHECK);
			if(input.isMain()) //$NON-NLS-1$
				checkButton.setSelection(true);
			checkButton.setEnabled(false);
			checkButton.pack();
			item.setData(CHECKBUTTON_PROPERTY, checkButton);
			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 3);
		}
		TableColumn cols[]=inputsTable.getColumns();
		for(TableColumn col : cols){
			col.pack();
		}
	}

	private void fillComponentsTree(){
		componentsTree.setVisible(false);

		List<RefactoringMechanismInstance> preconditions = refactoring.getPreconditions();
		List<String> actions = RefactoringMechanismInstance.getMechanismListClassNames(refactoring.getActions());
		List<RefactoringMechanismInstance> postconditions = refactoring.getPostconditions();

		TreeItem preconditionsChild = TreeEditor.createBranch(componentsTree, 0,

				Messages.RefactoringSummaryPanel_Preconditions, RefactoringImages.CHECK_ICON_PATH);
		TreeItem actionsChild = TreeEditor.createBranch(componentsTree, 1, 
				Messages.RefactoringSummaryPanel_Action, RefactoringImages.RUN_ICON_PATH);
		TreeItem postconditionsChild = TreeEditor.createBranch(componentsTree, 2,
				Messages.RefactoringSummaryPanel_Postconditions, RefactoringImages.VALIDATE_ICON_PATH);

		TreeEditor.fillInTreeBranch(RefactoringMechanismInstance.getMechanismListClassNames(preconditions), preconditionsChild, 
				RefactoringImages.CHECK_ICON_PATH);
		preconditionsChild.setExpanded(true);

		TreeEditor.fillInTreeBranch(actions, actionsChild, 
				RefactoringImages.RUN_ICON_PATH);
		actionsChild.setExpanded(true);

		TreeEditor.fillInTreeBranch(RefactoringMechanismInstance.getMechanismListClassNames(postconditions), postconditionsChild, 
				RefactoringImages.VALIDATE_ICON_PATH);
		postconditionsChild.setExpanded(true);

		componentsTree.setVisible(true);

	}

	/**
	 * Establece la refactorización a mostrar.
	 * 
	 * @param ref
	 *            definición de la refactorización a mostrar
	 */
	public void setRefactoringDefinition(DynamicRefactoringDefinition ref) {
		refactoring=ref;
	}

	public void showRefactoringSummary(){

		refTabFolder.setVisible(false);
		clear();

		fillConstantComponents();
		refTabFolder.setSelection(2);
		if(refactoring.getImage()!=null && 
				!refactoring.getImage().equals("")){ //$NON-NLS-1$
			createAndFillImageTabItem();
		}
		if(refactoring.getExamples().size()>0)
			createAndFillExamplesTabItem();

		titleLabel.setText(Messages.RefactoringSummaryPanel_Title + refactoring.getName());
		
		refTabFolder.setSelection(0);
		refTabFolder.setVisible(true);
	}

	private void fillConstantComponents() {
		fillOverview();
		fillInputsTable();
		fillComponentsTree();
	}

	public DynamicRefactoringDefinition getRefactoringSelected(){
		return refactoring;
	}
	
}

