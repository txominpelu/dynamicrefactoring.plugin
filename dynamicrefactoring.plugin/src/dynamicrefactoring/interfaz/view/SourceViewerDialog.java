package dynamicrefactoring.interfaz.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;

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
		
		beforeLabel=new Label(container, SWT.LEFT);
		beforeLabel.setText(Messages.SourceViewerDialog_BeforeLabel);
		formData=new FormData();
		formData.top=new FormAttachment(0,10);
		formData.left=new FormAttachment(0,5);
		beforeLabel.setLayoutData(formData);
		
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
		
		Document afterDocument = new Document(afterCode);
		tools.setupJavaDocumentPartitioner(afterDocument);
		afterSourceViewer.setDocument(afterDocument);
		afterSourceViewer.setEditable(false);
		
		
		return container;
	}

	/**
	 * 
	 * @param beforeFilePath
	 * @param afterFilePath
	 * @return devuelve si la carga de los ficheros se ha podido realizar correctamente
	 */
	public boolean loadSources(String beforeFilePath, String afterFilePath) {
		boolean okLoad=true;
		try {
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
		newShell.setText(Messages.SourceViewerDialog_Title);
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
	
}
