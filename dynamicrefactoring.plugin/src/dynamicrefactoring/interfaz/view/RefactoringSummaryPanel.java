package dynamicrefactoring.interfaz.view;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.metadata.interfaces.Category;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.util.RefactoringTreeManager;
import dynamicrefactoring.util.ScopeLimitedLister;

/**
 * Proporciona un organizador de pesta�as, en el cual se muestra
 * la informaci�n relativa a la refactorizaci�n.
 * @author XPMUser
 */
public class RefactoringSummaryPanel {

	/**
	 * Etiqueta t�tulo.
	 */
	private Label titleLabel;
	
	/**
	 * N�mero m�nimo de pesta�as, es el n�mero de pesta�as fijas a mostrar.
	 */
	private int minNumTabs;

	/**
	 * Organizador de pesta�as.
	 */
	private TabFolder refTabFolder;
	
	/**
	 * Etiqueta descripci�n de la refactorizaci�n.
	 */
	private Label descriptionLabel;
	
	/**
	 * Cuadro de texto en que se mostrar� la descripci�n de la refactorizaci�n.
	 */
	private Text descriptionText;
	
	/**
	 * Etiqueta motivaci�n de la refactorizaci�n.
	 */
	private Label motivationLabel;
	
	/**
	 * Cuadro de texto en que se mostrar� la motivaci�n de la refactorizaci�n.
	 */
	private Text motivationText;
	
	/**
	 * Etiqueta categorias de la refactorizaci�n.
	 */
	private Label categoriesLabel;
	
	/**
	 * Cuadro de texto en que se mostrar� las categorias a las que pertenece la refactorizaci�n.
	 */
	private Text categoriesText;
	
	/**
	 * Conjunto de checkButton que se muestran en la pesta�a de entradas.
	 */
	private ArrayList<Button> checkButtonsInputsTab=new ArrayList<Button>();

	/**
	 * Tabla en que se mostrar�n las entradas de la refactorizaci�n.
	 */
	private Table inputsTable;

	/**
	 * �rbol sobre el que se mostrar�n de forma estructurada los diferentes elementos
	 * del repositorio que componen la refactorizaci�n (precondiciones, acciones y 
	 * postcondiciones).
	 */
	private Tree componentsTree;

	/**
	 * �rea en que se muestra la imagen asociada a la refactorizaci�n.
	 */
	private Canvas imageCanvas ;
	
	/**
	 * Definici�n de la refactorizaci�n.
	 */
	private DynamicRefactoringDefinition refactoring;

	public RefactoringSummaryPanel(Composite parent){

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
		
		//categoriesComp
		final Composite categoriesComp = new Composite(comp, SWT.NONE);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		categoriesComp.setLayoutData(gd);
		categoriesComp.setLayout(new GridLayout());
		
		categoriesLabel = new Label(categoriesComp, SWT.CENTER);
		categoriesLabel.setText(Messages.RefactoringSummaryPanel_Categories);
		
		categoriesText = new Text(categoriesComp, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		categoriesText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		categoriesText.setEditable(false);
		gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		categoriesText.setLayoutData(gd);
		
		TabItem item = new TabItem (refTabFolder, SWT.NONE);
		item.setText(Messages.RefactoringSummaryPanel_Overview);
		item.setControl(comp);
	}
	
	private void createInputsTabItem(){
		inputsTable = new Table(refTabFolder, SWT.BORDER);
		inputsTable.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		inputsTable.setEnabled(true);
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

		//cargamos la imagen
		String path = RefactoringPlugin.getDynamicRefactoringsDir() + 
		System.getProperty("file.separator") + //$NON-NLS-1$
		refactoring.getName() + System.getProperty("file.separator") + //$NON-NLS-1$
		refactoring.getImage();

		ImageLoader loader = new ImageLoader();
		final Image image = new Image(imageCanvas.getDisplay(), loader.load(path)[0]);

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

	private void clear(){

		//inputsTable
		if(inputsTable.getItemCount()>0){
			TableItem[] items=inputsTable.getItems();
			for(int i=items.length-1; i>=0; i--)
				items[i].dispose();
		}

		for(Button bu:checkButtonsInputsTab)
			bu.dispose();

		//componentsTree
		RefactoringTreeManager.cleanTree(componentsTree);

		//ImageTab
		if(refTabFolder.getItemCount()>minNumTabs)
			refTabFolder.getItem(minNumTabs).dispose();
	}

	private void fillOverview(){
		descriptionText.setText(refactoring.getDescription().trim());
		motivationText.setText(refactoring.getMotivation().trim());
		Scope scope = new ScopeLimitedLister().getRefactoringScope(refactoring);
		String cat="";
		if(scope!=null)
			cat+="Scope."+scope.toString();
		ArrayList<Category> categories = new ArrayList<Category>(refactoring.getCategories());
		for (Category c : categories){
		 cat+="\n" + c.getParent()+"."+c.getName();
		}
		categoriesText.setText(cat.trim());
	}
	
	private void fillInputsTable(){
		ArrayList<String[]> inputs=refactoring.getInputs();
		Button checkButton=null;
		TableEditor editor;
		for(String[] input : inputs){
			TableItem item=new TableItem(inputsTable, SWT.BORDER);
			item.setText(new String[]{input[1], input[0], input[2], "", ""}); //$NON-NLS-1$ //$NON-NLS-2$

			editor = new TableEditor(inputsTable);
			checkButton = new Button(inputsTable, SWT.CHECK);
			if(input[4]!=null && input[4].equals("true")) //$NON-NLS-1$
				checkButton.setSelection(true);
			checkButton.setEnabled(false);
			checkButton.pack();
			checkButtonsInputsTab.add(checkButton);
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

		ArrayList<String> preconditions=refactoring.getPreconditions();
		ArrayList<String> actions=refactoring.getActions();
		ArrayList<String> postconditions=refactoring.getPostconditions();

		TreeItem preconditionsChild = TreeEditor.createBranch(componentsTree, 0,
				Messages.RefactoringSummaryPanel_Preconditions, RefactoringImages.CHECK_ICON_PATH);
		TreeItem actionsChild = TreeEditor.createBranch(componentsTree, 1, 
				Messages.RefactoringSummaryPanel_Action, RefactoringImages.RUN_ICON_PATH);
		TreeItem postconditionsChild = TreeEditor.createBranch(componentsTree, 2,
				Messages.RefactoringSummaryPanel_Postconditions, RefactoringImages.VALIDATE_ICON_PATH);

		TreeEditor.fillInTreeBranch(preconditions, preconditionsChild, 
				RefactoringImages.CHECK_ICON_PATH);
		preconditionsChild.setExpanded(true);

		TreeEditor.fillInTreeBranch(actions, actionsChild, 
				RefactoringImages.RUN_ICON_PATH);
		actionsChild.setExpanded(true);

		TreeEditor.fillInTreeBranch(postconditions, postconditionsChild, 
				RefactoringImages.VALIDATE_ICON_PATH);
		postconditionsChild.setExpanded(true);

		componentsTree.setVisible(true);

	}

	/**
	 * Establece la refactorizaci�n a mostrar.
	 * @param ref definici�n de la refactorizaci�n a mostrar
	 */
	public void setRefactoringDefinition(DynamicRefactoringDefinition ref) {
		refactoring=ref;
	}

	public void showRefactoringSummary(){

		refTabFolder.setVisible(false);
		clear();

		fillOverview();
		fillInputsTable();
		fillComponentsTree();
		if(refactoring.getImage()!=null && 
				!refactoring.getImage().equals("")){ //$NON-NLS-1$
			createAndFillImageTabItem();
		}

		titleLabel.setText(Messages.RefactoringSummaryPanel_Title + refactoring.getName());
		refTabFolder.setVisible(true);
	}

}

