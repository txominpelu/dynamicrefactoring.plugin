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
import dynamicrefactoring.action.TextViewerAction;
import dynamicrefactoring.interfaz.ButtonTextProvider;

/**
 * Dialogo que visualiza el ejemplo de código fuente de una refactorización.
 * En él se puede observar un visor de código fuente original y otro del
 * código fuente refactorizado. Además, sobre el código se podrán realizar 
 * operaciones de selección y copiado del mismo.
 * 
 * @author XPMUser
 *
 */
public class SourceViewerDialog extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(SourceViewerDialog.class);

	/**
	 * Nombre de la refactorización.
	 */
	private String refactoringName;
	
	/**
	 * Etiqueta para el código fuente original.
	 */
	private Label beforeLabel;
	
	/**
	 * Etiqueta para el código fuente refactorizado.
	 */
	private Label afterLabel;
	
	/**
	 * Visor de texto para el código fuente original.
	 */
	private SourceViewer beforeSourceViewer;
	
	/**
	 * Visor de texto para el código fuente refactorizado.
	 */
	private SourceViewer afterSourceViewer;
	
	/**
	 * Ruta en la que se encuentra el fichero que contiene
	 * el código fuente original.
	 */
	private String beforeCode;
	
	/**
	 * Ruta en la que se encuentra el fichero que contiene
	 * el código fuente refactorizado.
	 */
	private String afterCode;
	
	/**
	 * Crea el diálogo que visualiza el ejemplo de código fuente 
	 * de una refactorización.
	 * 
	 * @param parentShell la <i>shell</i> padre de esta ventana.
	 */
	public SourceViewerDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	/**
	 * Crea el contenido del diálogo.
	 * 
	 * @param parent el elemento padre para los contenidos de la ventana.
	 * 
	 * @return el control del área de diálogo.
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
	 * Configura el styledText del visor de texto, creando un menú 
	 * contextual con las acciones de selección y copiado del texto.
	 * 
	 * @param sViewer visor de texto
	 */
	private void configureStyledTextSourceViewer(SourceViewer sViewer){
		
		StyledText styledText=sViewer.getTextWidget();
		styledText.setIndent(2);
		
		// copy Action
		final TextViewerAction copy = 
			new TextViewerAction(sViewer, ITextOperationTarget.COPY);
		copy.setText(Messages.SourceViewerDialog_Copy);
		copy.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);
		
		// selectAll Action
		final TextViewerAction selectAll = 
			new TextViewerAction(sViewer, ITextOperationTarget.SELECT_ALL);
		selectAll.setText(Messages.SourceViewerDialog_SelectAll);
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
	 * Realiza la carga de la información relativa al ejemplo de 
	 * código fuente para la refactorización que se esta tratando.
	 * 
	 * @param refactoringName nombre de la refactorización
	 * @param beforeFilePath ruta donde se encuentra el codigo original del ejemplo
	 * @param afterFilePath ruta donde se encuentra el codigo refactorizado del ejemplo
	 * 
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
	 * 
	 * @param file fichero a ser leido.
	 * 
	 * @return cadena con el contenido del fichero.
	 * 
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
	
	  /**
	   * Actualiza el styledText del visor de texto mostrando las 
	   * operaciones disponibles a realizar de acuerdo a si 
	   * este recibe o pierde el foco.
	   * 
	   * @author XPMUser
	   *
	   */
	  private class StyledTextFocusListener implements FocusListener{

		/**
		 * Contenedor del visor de texto.
		 */
		private Composite comp;
		
		/**
		 * Acción de copiado para el visor de texto.
		 */
		private TextViewerAction copyAction;
		
		/**
		 * Acción de selección para el visor de texto.
		 */
		private TextViewerAction selectAllAction;
		
		/**
		 * Representa la activación del controlador de copiado.
		 */
		private IHandlerActivation copyHandlerActivation;
		
		/**
		 * Representa la activación del controlador de selección.
		 */
		private IHandlerActivation selectAllHandlerActivation;
		  
		/**
		 * Establece el contenedor del visor de texto asi como las
		 * acciones de copiado y selección de texto que va a soportar.
		 * 
		 * @param comp contenedor del visor de texto
		 * @param copyAction acción de copiado para el visor de texto
		 * @param selectAllAction acción de selección para el visor de texto
		 */
		public StyledTextFocusListener(Composite comp, 
				TextViewerAction copyAction, TextViewerAction selectAllAction){
			this.comp=comp;
			this.copyAction=copyAction;
			this.selectAllAction=selectAllAction;
		}  
		
		/**
		 * Recibe una notificación indicando que el texto observado ha recibido
		 * el foco. Se establece los controladores de selección y de copiado de
		 * texto.
		 *
		 * @param e evento con la información referente a la recepción del foco.
		 * 
		 * @see FocusListener#focusGained(FocusEvent)
		 */
		@Override
		public void focusGained(FocusEvent e) {
			copyAction.update();
			IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			copyHandlerActivation = service.activateHandler(IWorkbenchCommandConstants.EDIT_COPY, new ActionHandler(copyAction), new ActiveShellExpression(comp.getShell()));
			selectAllHandlerActivation = service.activateHandler(IWorkbenchCommandConstants.EDIT_SELECT_ALL, new ActionHandler(selectAllAction), new ActiveShellExpression(comp.getShell()));
		}

		/**
		 * Recibe una notificación indicando que el texto observado ha perdido
		 * el foco. Se desactivan los controladores de selección y de copiado
		 * de texto.
		 * 
		 * @param e evento con la información referente a la pérdida del foco.
		 * 
		 * @see FocusListener#focusLost(FocusEvent)
		 */
		@Override
		public void focusLost(FocusEvent e) {
			IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);

			if(copyHandlerActivation!=null)
				service.deactivateHandler(copyHandlerActivation);
			if(selectAllHandlerActivation!=null)
				service.deactivateHandler(selectAllHandlerActivation);
		}
		  
	  }

	  
	  /**
	   * Actualiza la acción de copiado de texto cuando se modifica 
	   * la selección del texto del visor de texto.
	   * 
	   * @author XPMUser
	   *
	   */
	  private class SourceViewerSelectionChangedListener implements ISelectionChangedListener{

		/**
		 * Acción de copiado para el visor de texto.
		 */
		private TextViewerAction copyAction;
		
		/**
		 * Establece la acción de copiado que soporta el visor de texto.
		 * 
		 * @param copyAction acción de copiado para el visor de texto
		 */
		public SourceViewerSelectionChangedListener(TextViewerAction copyAction){
			this.copyAction=copyAction;
		} 
		
		/**
		 * Recibe una notificación indicando que la selección del texto observado 
		 * ha cambiado. Se actualiza la acción de copiado.
		 * 
		 * @param event evento con la información referente a la selección de texto.
		 * 
		 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent event)
		 * 
		 */
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			copyAction.update();
		}
		  
	  }
}
