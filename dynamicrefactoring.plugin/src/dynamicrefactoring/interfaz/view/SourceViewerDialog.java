package dynamicrefactoring.interfaz.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ActiveShellExpression;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.interfaz.ButtonTextProvider;

public class SourceViewerDialog extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(SourceViewerDialog.class);

	private Label beforeLabel;
	private Label afterLabel;
	private SourceViewer beforeSourceViewer;
	private SourceViewer afterSourceViewer;
	private String refactoringName;
	private String beforeCode;
	private String afterCode;
	
	
	protected SourceViewerDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	/**
	 * Crea el contenido de la ventana de diálogo.
	 * 
	 * @param parent el elemento padre para los contenidos de la ventana.
	 * 
	 * @return el control del  área de diálogo.
	 */
	@Override
	protected Control createDialogArea(Composite parent){
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		
		FormData formData=null;
		
		//beforeLabel
		beforeLabel=new Label(container, SWT.LEFT);
		beforeLabel.setText(Messages.SourceViewerDialog_BeforeLabel);
		formData=new FormData();
		formData.top=new FormAttachment(0,10);
		formData.left=new FormAttachment(0,5);
		beforeLabel.setLayoutData(formData);
		
		//afterLabel
		afterLabel=new Label(container, SWT.LEFT);
		afterLabel.setText(Messages.SourceViewerDialog_AfterLabel);
		formData=new FormData();
		formData.top=new FormAttachment(0,10);
		formData.left=new FormAttachment(50,5);
		afterLabel.setLayoutData(formData);
		 
		int styleSourceViewer =
				  SWT.MULTI
				| SWT.WRAP
				| SWT.V_SCROLL
				| SWT.H_SCROLL;

		// establecemos Java Syntax Highlighting 
		JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools(); 
		JavaSourceViewerConfiguration javaSourceConfig = 
			new JavaSourceViewerConfiguration(tools.getColorManager(), 
				JavaPlugin.getDefault().getCombinedPreferenceStore(), null, null);
		 
		//beforeSourceViewer
		beforeSourceViewer = 
			new SourceViewer(container, null, null, true, styleSourceViewer); 		
		formData=new FormData();
		formData.top=new FormAttachment(beforeLabel,10);
		formData.left=new FormAttachment(0,5); 
		formData.right=new FormAttachment(50, -5);
		formData.bottom=new FormAttachment(100, -5);
		beforeSourceViewer.getControl().setLayoutData(formData);
		beforeSourceViewer.configure(javaSourceConfig);
		configureStyledTextSourceViewer(beforeSourceViewer);
		
		Document beforeDocument = new Document(beforeCode); 
		tools.setupJavaDocumentPartitioner(beforeDocument); 
		beforeSourceViewer.setDocument(beforeDocument);
		beforeSourceViewer.setEditable(false);
		
		
		//afterSourceViewer
		afterSourceViewer = 
			new SourceViewer(container, null, null, true, styleSourceViewer);   
		formData=new FormData();
		formData.top=new FormAttachment(afterLabel,10);
		formData.left=new FormAttachment(50,5); 
		formData.right=new FormAttachment(100, -5);
		formData.bottom=new FormAttachment(100, -5);
		afterSourceViewer.getControl().setLayoutData(formData);
		afterSourceViewer.configure(javaSourceConfig);
		configureStyledTextSourceViewer(afterSourceViewer);
		
		Document afterDocument = new Document(afterCode);
		tools.setupJavaDocumentPartitioner(afterDocument);
		afterSourceViewer.setDocument(afterDocument);
		afterSourceViewer.setEditable(false);
		
		return container;
	}

	/**
	 * 
	 */
	private void configureStyledTextSourceViewer(SourceViewer sViewer){
		
		StyledText styledText=sViewer.getTextWidget();
		styledText.setIndent(2);
		
		// copy Action
		final TextViewerAction copy = 
			new TextViewerAction(sViewer, ITextOperationTarget.COPY);
		copy.setText("Copy");
		copy.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);
		
		// selectAll Action
		final TextViewerAction selectAll = 
			new TextViewerAction(sViewer, ITextOperationTarget.SELECT_ALL);
		selectAll.setText("Select All");
		selectAll.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_SELECT_ALL);

		// StyledTextFocusListener
		styledText.addFocusListener(
				new StyledTextFocusListener((Composite)sViewer.getControl(),
											 copy,selectAll));
		
		// SourceViewerSelectionChangedListener
		sViewer.addSelectionChangedListener(
				new SourceViewerSelectionChangedListener(copy));
		
		// contextMenu
		MenuManager contextMenu = new MenuManager();
		contextMenu.add(copy);
		contextMenu.add(new Separator());
		contextMenu.add(selectAll);
		styledText.setMenu(contextMenu.createContextMenu(styledText));
		styledText.selectAll();
	}

	/**
	 * 
	 * @param refactoringName nombre de la refactorización
	 * @param beforeFilePath ruta donde se encuentra el codigo original del ejemplo
	 * @param afterFilePath ruta donde se encuentra el codigo refactorizado del ejemplo
	 * @return devuelve si la carga de los ficheros se ha podido realizar correctamente
	 */
	public boolean loadSources(String refactoringName, 
			String beforeFilePath, String afterFilePath) {
		boolean okLoad=true;
		try {
			this.refactoringName=refactoringName;
			beforeCode=readFileAsAString(new File(beforeFilePath));
   		    afterCode=readFileAsAString(new File(afterFilePath));
		} catch (IOException e) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			String message = 
				Messages.SourceViewerDialog_NotAvailable + ".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(window.getShell(),
					Messages.SourceViewerDialog_Error, message);
			okLoad=false;
		}
		return okLoad;
	}

	/**
	 * Crea el contenido de la barra de botones.
	 * 
	 * @param parent el elemento padre de los botones.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, 
				ButtonTextProvider.getOKText(), true);
	}

	/**
	 * Obtiene el tamaño inicial de la ventana de diálogo.
	 * 
	 * @return el tamaño inicial de la ventana de diálogo.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 400);
	}

	/**
	 * Prepara la <i>shell</i> para su apertura.
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.SourceViewerDialog_Title + " - " + refactoringName); //$NON-NLS-1$
		newShell.setImage(RefactoringImages.getSourceIcon());
	}
	
	/**
	   * Le el contenido del fichero y devuelve una cadena con ello. 
	   * @param file fichero a ser leido.
	   * @return cadena con el contenido del fichero.
	   * @throws IOException excepción que lanza en caso de no poder leer fichero.
	   */
	  public static String readFileAsAString(File file) throws IOException {
		  FileInputStream inStream = new FileInputStream(file);
		  
		  long length = file.length();
		  byte inBuf[] = new byte[(int) length];
		  
		  inStream.read(inBuf);
		  inStream.close();
		  
	    return new String(inBuf);
	  }
	
	  private class StyledTextFocusListener implements FocusListener{

		private Composite comp;
		private TextViewerAction copyAction;
		private TextViewerAction selectAllAction;
		private IHandlerActivation copyHandlerActivation;
		private IHandlerActivation selectAllHandlerActivation;
		  
		public StyledTextFocusListener(Composite comp, TextViewerAction copyAction, TextViewerAction selectAllAction){
			this.comp=comp;
			this.copyAction=copyAction;
			this.selectAllAction=selectAllAction;
		}  
		
		@Override
		public void focusGained(FocusEvent e) {
			copyAction.update();
			IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			copyHandlerActivation = service.activateHandler(IWorkbenchCommandConstants.EDIT_COPY, new ActionHandler(copyAction), new ActiveShellExpression(comp.getShell()));
			selectAllHandlerActivation = service.activateHandler(IWorkbenchCommandConstants.EDIT_SELECT_ALL, new ActionHandler(selectAllAction), new ActiveShellExpression(comp.getShell()));
		}

		@Override
		public void focusLost(FocusEvent e) {
			IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);

			if(copyHandlerActivation!=null)
				service.deactivateHandler(copyHandlerActivation);
			if(selectAllHandlerActivation!=null)
				service.deactivateHandler(selectAllHandlerActivation);
		}
		  
	  }
	  
	  private class SourceViewerSelectionChangedListener implements ISelectionChangedListener{

		private TextViewerAction copyAction;
		
		public SourceViewerSelectionChangedListener(TextViewerAction copyAction){
			this.copyAction=copyAction;
		} 
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			copyAction.update();
		}
		  
	  }
}
