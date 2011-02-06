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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.interfaz.TreeEditor;
import dynamicrefactoring.interfaz.wizard.RefactoringWizardPage2;
import dynamicrefactoring.util.RefactoringTreeManager;

/**
 * Proporciona un organizador de pestañas, en el cual se muestra
 * la información relativa a la refactorización.
 * @author XPMUser
 */
public class RefactoringSummaryPanel {

	/**
	 * Etiqueta título.
	 */
	private Label titleLabel;

	/**
	 * Organizador de pestañas.
	 */
	private TabFolder refTabFolder;

	/**
	 * Definición de la refactorización.
	 */
	private DynamicRefactoringDefinition refactoring;

	/**
	 * Tabla en que se mostrarán las entradas de la refactorización.
	 */
	private Table inputsTable;

	/**
	 * Árbol sobre el que se mostrarán de forma estructurada los diferentes elementos
	 * del repositorio que componen la refactorización (precondiciones, acciones y 
	 * postcondiciones).
	 */
	private Tree componentsTree;

	/**
	 * Área en que se muestra la imagen asociada a la refactorización.
	 */
	private Canvas imageCanvas ;

	private int minNumTabs;
	
	ArrayList<Button> b=new ArrayList<Button>();

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
		createInputsTabItem();
		createMechanismTabItem();

		minNumTabs=refTabFolder.getItemCount();
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

		for(Button bu:b)
			bu.dispose();

		//componentsTree
		RefactoringTreeManager.cleanTree(componentsTree);

		//ImageTab
		if(refTabFolder.getItemCount()!=minNumTabs)
			refTabFolder.getItem(minNumTabs).dispose();
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
			b.add(checkButton);
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
				Messages.RefactoringSummaryPanel_Preconditions, "icons" + System.getProperty("file.separator") + "check.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		TreeItem actionsChild = TreeEditor.createBranch(componentsTree, 1, 
				Messages.RefactoringSummaryPanel_Action, "icons" + System.getProperty("file.separator") + "run.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		TreeItem postconditionsChild = TreeEditor.createBranch(componentsTree, 2,
				Messages.RefactoringSummaryPanel_Postconditions, "icons" + System.getProperty("file.separator") + "validate.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		TreeEditor.fillInTreeBranch(preconditions, preconditionsChild, 
				"icons" + System.getProperty("file.separator") + "check.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		preconditionsChild.setExpanded(true);

		TreeEditor.fillInTreeBranch(actions, actionsChild, 
				"icons" + System.getProperty("file.separator") + "run.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		actionsChild.setExpanded(true);

		TreeEditor.fillInTreeBranch(postconditions, postconditionsChild, 
				"icons" + System.getProperty("file.separator") + "validate.gif"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		postconditionsChild.setExpanded(true);

		componentsTree.setVisible(true);

	}

	/**
	 * Establece la refactorización a mostrar.
	 * @param ref definición de la refactorización a mostrar
	 */
	public void setRefactoringDefinition(DynamicRefactoringDefinition ref) {
		refactoring=ref;
	}

	public void showRefactoringSummary(){

		refTabFolder.setVisible(false);
		clear();

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
