/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente De La Fuente

This file is part of Foobar

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package dynamicrefactoring.interfaz.wizard;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.google.common.base.Throwables;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringMechanismInstance;
import dynamicrefactoring.domain.RefactoringMechanismType;
import dynamicrefactoring.domain.xml.XMLRefactoringsCatalog;
import dynamicrefactoring.interfaz.wizard.listener.ListDownListener;
import dynamicrefactoring.interfaz.wizard.listener.ListUpListener;
import dynamicrefactoring.interfaz.wizard.search.internal.QueryResult;
import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade;
import dynamicrefactoring.interfaz.wizard.search.internal.SearchingFacade.SearchableType;
import dynamicrefactoring.interfaz.wizard.search.javadoc.EclipseBasedJavadocReader;
import dynamicrefactoring.util.PluginStringUtils;

/**
 * Contenido de las páginas en la que se establecen los diferentes mecanismos de la refactorización.
 * 
 * <p>Implementa de forma genérica la interfaz y el proceso de adición de 
 * componentes concretos del repositorio a la refactorización.</p>
 * 
 * <p>Configurable para mostrar el tipo de elementos que sea necesario:
 * predicados (como precondiciones o como postcondiciones) o acciones.</p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RepositoryElementComposite {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RepositoryElementComposite.class);
	
	/**
	 * Página del asistente a partir de la cual se pueden obtener las entradas
	 * de la refactorización.
	 */

	private RefactoringWizardPage2 inputsPage;
	
	/**
	 * Página del asistente que contiene este contenedor.
	 */
	private IRefactoringWizardElementPage mainPage;
	
	/**
	 * Título de la operación.
	 */
	private String title;
	
	/**
	 * Compuesto asociada al tipo de elementos del repositorio que se manejan.
	 */
	private Composite composite;
	
	/**
	 * Tabla con los parámetros ambiguos del elemento del repositorio.
	 */
	private Table tb_parameters;
	
	/**
	 * Lista de nombres de los elementos del repositorio seleccionados.
	 */
	private List l_Selected;
	
	/**
	 * Lista de nombres de los elementos del repositorio disponibles, aunque ya hayan sido 
	 * seleccionados.
	 */
	private List l_Available;
	
	/**
	 * CheckBox que permite seleccionar al usuario la manera (cualificados o no) en la que se 
	 * muestran los nombres de los mecanismos de la refactorización a seleccionar.
	 */
	private Button cb_qualified;
	
	/**
	 * Botón que permite añadir un elemento del repositorio a la lista de elementos
	 * que forman parte de la refactorización.
	 */
	private Button bt_addElement;
	
	/**
	 * Botón que permite eliminar un elemento seleccionado como parte de la
	 * refactorización y devolverlo a la lista de elementos del repositorio.
	 */
	private Button bt_delElement;
	
	/**
	 * Botón que permite mover hacia arriba en la lista elementos seleccionados 
	 * como parte de la refactorización.
	 */
	private Button bt_moveUp;
	
	/**
	 * Botón que permite mover hacia abajo en la lista elementos seleccionados 
	 * como parte de la refactorización.
	 */
	private Button bt_moveDown;
	
	/**
	 * Tabla con el número de veces que se ha seleccionado un mecanismo. Solo
	 * aparecen las precondiciones seleccionadas.
	 */
	private Hashtable<String, Integer>selectedTimes;
	
	/**
	 * Navegador en el que se muestra información relativa al elemento seleccionado dentro de
	 * la lista de elementos.
	 */
	private Browser navegador;
	
	/**
	 * Caja de texto que permite introducir al usuario el patrón de la búsqueda.
	 */
	private Text tSearch;
	
	/**
	 * Botón que permite realizar la búsqueda.
	 */
	private final Button bSearch;
	
	/**
	 * Indicador de búsqueda.
	 * Verdadero significa que en la lista de elementos del repositorio disponibles
	 * se encuentran los resultantes de la búsqueda por lo que habrá que mantener el
	 * orden obtenido de la búsqueda, ya que atiende a los mayor relevancia.
	 * Falso significa que en la lista se encuentran todo los elementos del repositorio 
	 * disponibles, por lo que aparecerán en orden alfabético.
	 */
	private boolean isSearched;
	
	/**
	 * Botón por defecto de esta página del wizard.
	 */
	private Button bDefault;
	
	/**
	 * Tabla de elementos ya seleccionados.
	 *  
	 * <p>Se utiliza como clave el nombre del elemento concreto del repositorio, 
	 * y como valor, un objeto de tipo <code>RepositoryItem</code> con toda la 
	 * información asociada a la precondición.</p>
	 */
	private Hashtable<String, RepositoryItem> selectedTable;
	
	/**
	 * Tabla asociativa que permite mantener un seguimiento de las listas 
	 * desplegables que han sido asociadas a los elementos de la tabla de
	 * parámetros.
	 */
	private Hashtable<TableItem, CCombo> combosTable;
	
	/**
	 * Elementos del repositorio disponibles con su descripción asociada. 
	 * <p>
	 * Se utiliza como clave el nombre del elemento (sin cualificar) y
	 * como valor la descripción correspondiente a este tipo, obtenida a partir
	 * de la descripción asociada en la documentación del código fuente, javadoc.
	 * </p>
	 */
	private HashMap<String, String> a_Available;
	
	/**
	 * Toolkit.
	 */
	private FormToolkit toolkit;
	
	/**
	 * Contenedor form.
	 */
	private ScrolledForm form;
	
	/**
	 * Etiqueda de descripción para el elemento seleccionado.
	 */
	private Label descriptionFormLabel;
	
	/**
	 * Contenedor expandible que va a contener las refactorizaciones
	 * que contiene el elemento seleccionado en su definición.
	 */
	private ExpandableComposite refExpandableComp;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            elemento padre de la pestaña.
	 * @param inputsPage
	 *            página del asistente a partir de la cual se pueden obtener las
	 *            entradas que se han seleccionado para la refactorización.
	 * @param title
	 *            título asociado a la pestaña.
	 * @param main
	 *            Página donde se va a añadir esta estructura.
	 * @param type
	 *            tipo del elemento
	 */
	public RepositoryElementComposite(Composite parent, String title, 
		IWizardPage inputsPage,IRefactoringWizardElementPage main, final RefactoringMechanismType type){
		
		selectedTable = new Hashtable<String, RepositoryItem>();
		combosTable = new Hashtable<TableItem, CCombo>();
		a_Available = new HashMap<String, String>();
		
		
		if (inputsPage != null && inputsPage instanceof RefactoringWizardPage2){
			this.inputsPage = (RefactoringWizardPage2)inputsPage;
			this.mainPage = main;
		}
		
		// sash_form
		SashForm sash_form = new SashForm(parent, SWT.VERTICAL | SWT.NULL);
		sash_form.setLayout(new FormLayout());
		final FormData sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 5);
		sashFormData.left = new FormAttachment(0, 5);
		sashFormData.right=new FormAttachment(100, -5);
		sashFormData.bottom=new FormAttachment(100, -5);
		sash_form.setLayoutData(sashFormData);
		sash_form.setSashWidth(2);
		sash_form.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

		// sash_form Top: control
		final Composite compTop = new Composite(sash_form, SWT.NONE);
		compTop.setLayout(new FormLayout());
		
		// sash_form Bottom: compBrowser
		final Composite compBrowser = new Composite(sash_form, SWT.NONE);
		compBrowser.setLayout(new FormLayout());
		
		final Composite control = new Composite(compTop, SWT.NONE);
		control.setLayout(new FormLayout());
		
		composite = new Composite(parent, SWT.NONE);		
		this.title = title;
		
		/*Para establecer el tamaño de la ventana*/
		final Label label = new Label(control, SWT.NONE);
		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0,261);
		label.setLayoutData(fd_label);
		
		Label search = new Label(control, SWT.NONE);
		final FormData fd_search = new FormData();
		fd_search.bottom = new FormAttachment(0,58);
		fd_search.top = new FormAttachment(0, 29);
		fd_search.left = new FormAttachment(0, 15);
		search.setLayoutData(fd_search);
		search.setText(Messages.RefactoringWizardPage2_Search);
		
		
		
		tSearch = new Text(control,SWT.BORDER);
		final FormData fd_tsearch = new FormData();
		fd_tsearch.right = new FormAttachment(0, 185);
		fd_tsearch.top = new FormAttachment(0, 28);
		fd_tsearch.left = new FormAttachment(search, 0, SWT.RIGHT);
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

		RefactoringWizardUtils.addContentProposalToTextField(tSearch, new ArrayList<String>(type.getElementAllList().keySet()));
		
		bSearch = new Button(control, SWT.PUSH);
		final FormData fd_bsearch = new FormData();
		fd_bsearch.right = new FormAttachment(0, 225);
		fd_bsearch.top = new FormAttachment(0, 26);
		fd_bsearch.left = new FormAttachment(tSearch, 5, SWT.RIGHT);
		bSearch.setLayoutData(fd_bsearch);
		bSearch.setImage(RefactoringImages.getSearchIcon());
		bSearch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fillSearchTypesList(tSearch.getText());	
			}
		});
		
		final FormData fd_available_list = new FormData();
		fd_available_list.top = new FormAttachment(0, 59);
		fd_available_list.left = new FormAttachment(0, 10);
		fd_available_list.right = new FormAttachment(bSearch, 0, SWT.RIGHT);
		fd_available_list.bottom = new FormAttachment(0,231);//248
		l_Available = new List(control,
			SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		l_Available.setLayoutData(fd_available_list);
		l_Available.addSelectionListener(new RepositorySelectionListener());
		l_Available.setToolTipText(
			Messages.RepositoryElementTab_SelectElements);
				
		cb_qualified = new Button(control, SWT.CHECK);
		cb_qualified.setText(Messages.RepositoryElementTab_Qualified);
		final FormData fd_cbqualified = new FormData();
		fd_cbqualified.top =  new FormAttachment(l_Available,5, SWT.BOTTOM);
		fd_cbqualified.left = new FormAttachment(l_Available,2,SWT.LEFT);
		cb_qualified.setLayoutData(fd_cbqualified);
		cb_qualified.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ArrayList<String> orderedList=new ArrayList<String>();
				for(String element : l_Available.getItems()){
					if(cb_qualified.getSelection())
						orderedList.add(getElementFullyQualifiedName(element));
					else
						orderedList.add(getElementNotQualifiedName(element));
				}
				
				// se vacia la lista l_Available
				l_Available.removeAll();
				
				if(!isSearched){
					Collections.sort(orderedList);
					//se muestra en orden alfabético
				}
				
				for(String element : orderedList)
					l_Available.add(element);
			}
		});
		
		
		final Group elementsGroup = new Group(control, SWT.NONE);
		elementsGroup.setText(title);
		final FormData fd_typwsGroup = new FormData();
		fd_typwsGroup.top = new FormAttachment(0, 7);
		fd_typwsGroup.left = new FormAttachment(0, 5);
		fd_typwsGroup.right = new FormAttachment(bSearch, 5, SWT.RIGHT);
		elementsGroup.setLayoutData(fd_typwsGroup);
		elementsGroup.setLayout(new FormLayout());
		
		final FormData fd_selected_list = new FormData();
		fd_selected_list.top = new FormAttachment(tSearch, 0, SWT.TOP);
		fd_selected_list.bottom = new FormAttachment(0, 115);
		fd_selected_list.right = new FormAttachment(0, 555);
		fd_selected_list.left = new FormAttachment(0, 330);
		l_Selected = new List(control,
			SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		l_Selected.setLayoutData(fd_selected_list);
		l_Selected.addSelectionListener(new ElementSelectionListener());
		
		final FormData fd_bt_addElement = new FormData();
		fd_bt_addElement.right = new FormAttachment(0, 300);
		fd_bt_addElement.top = new FormAttachment(0, 25);
		fd_bt_addElement.left = new FormAttachment(0, 250);
		bt_addElement = new Button(control, SWT.NONE);
		bt_addElement.setLayoutData(fd_bt_addElement);
		bt_addElement.setImage(RefactoringImages.getArrowRightIcon());
		bt_addElement.setEnabled(false);
		
		final FormData fd_bt_delElement = new FormData();
		fd_bt_delElement.bottom = new FormAttachment(0, 118);
		fd_bt_delElement.top = new FormAttachment(0, 95);
		fd_bt_delElement.right = new FormAttachment(bt_addElement, 50, SWT.LEFT);
		fd_bt_delElement.left = new FormAttachment(bt_addElement, 0, SWT.LEFT);
		bt_delElement = new Button(control, SWT.NONE);		
		bt_delElement.setLayoutData(fd_bt_delElement);
		bt_delElement.setImage(RefactoringImages.getArrowLeftIcon());
		
		bt_addElement.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addElements(type);
			}
		});
	
		bt_delElement.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeElements();
			}
		});
	
		final FormData fd_ambiguosParametersGroup = new FormData();
		fd_ambiguosParametersGroup.right = new FormAttachment(l_Selected, 0, SWT.RIGHT);
		fd_ambiguosParametersGroup.bottom = new FormAttachment(100,-5);
		fd_ambiguosParametersGroup.top = new FormAttachment(0, 135);
		fd_ambiguosParametersGroup.left = new FormAttachment(bt_delElement, 0, SWT.LEFT);
		final Group ambiguosParametersGroup = new Group(control, SWT.NONE);
		ambiguosParametersGroup.setText(Messages.RepositoryElementTab_AmbiguousParameters);
		ambiguosParametersGroup.setLayoutData(fd_ambiguosParametersGroup);
		ambiguosParametersGroup.setLayout(new FormLayout());
		fd_typwsGroup.bottom = new FormAttachment(ambiguosParametersGroup,0, SWT.BOTTOM);//252
		
		final FormData fd_tb_parameters = new FormData();
		fd_tb_parameters.right = new FormAttachment(100, -5);
		fd_tb_parameters.top = new FormAttachment(0, 5);
		fd_tb_parameters.bottom = new FormAttachment(100, -5);
		fd_tb_parameters.left = new FormAttachment(100, -294);
		tb_parameters = new Table(ambiguosParametersGroup, SWT.BORDER);
		tb_parameters.setLayoutData(fd_tb_parameters);
		tb_parameters.setLinesVisible(true);
		tb_parameters.setHeaderVisible(true);
		
		
		// Se crean las columnas para la tabla de parámetros.
		TableColumn cl_parameter2 = new TableColumn(tb_parameters, SWT.NONE);
		cl_parameter2.setResizable(false);
		cl_parameter2.setText(Messages.RepositoryElementTab_Type);
		cl_parameter2.setWidth(185);
		
		TableColumn cl_parameter = new TableColumn(tb_parameters, SWT.NONE);
		cl_parameter.setResizable(false);
		cl_parameter.setText(Messages.RepositoryElementTab_Value);
		cl_parameter.setWidth(100);
		
		final FormData fd_bt_moveUp = new FormData();
		fd_bt_moveUp.right = new FormAttachment(0, 620);
		fd_bt_moveUp.left = new FormAttachment(0, 575);
		fd_bt_moveUp.top = new FormAttachment(bt_addElement, -23, SWT.BOTTOM);
		fd_bt_moveUp.bottom = new FormAttachment(bt_addElement, 0, SWT.BOTTOM);
		bt_moveUp = new Button(control, SWT.NONE);
		bt_moveUp.setLayoutData(fd_bt_moveUp);
		bt_moveUp.setImage(RefactoringImages.getArrowUpIcon());
		bt_moveUp.addSelectionListener(new ListUpListener(l_Selected));
		
		final FormData fd_bt_moveDown = new FormData();
		fd_bt_moveDown.right = new FormAttachment(bt_moveUp, 0, SWT.RIGHT);
		fd_bt_moveDown.top = new FormAttachment(bt_delElement, 0, SWT.TOP);
		fd_bt_moveDown.left = new FormAttachment(bt_moveUp, 0, SWT.LEFT);
		bt_moveDown = new Button(control, SWT.NONE);		
		bt_moveDown.setLayoutData(fd_bt_moveDown);
		bt_moveDown.setImage(RefactoringImages.getArrowDownIcon());
		bt_moveDown.addSelectionListener(new ListDownListener(l_Selected));
		
		navegador = new Browser(compBrowser,SWT.BORDER);
		final FormData fd_navegador = new FormData();
		fd_navegador.top = new FormAttachment(0, 10);
		fd_navegador.left = new FormAttachment(0, 10);
		fd_navegador.right = new FormAttachment(100, -10);
		fd_navegador.bottom = new FormAttachment(100, 0);
		navegador.setLayoutData(fd_navegador);
		try{
			navegador.setUrl(FileLocator.toFileURL(getClass().getResource(RefactoringConstants.REFACTORING_JAVADOC + "/overview-summary.html" )).toString());
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
		
		Composite formComp;
		formComp = new Composite(compTop, SWT.BORDER);
		formComp.setLayout(new FormLayout());
		final FormData fd_formComp = new FormData();
		fd_formComp.top = new FormAttachment(control, 10);
		fd_formComp.left = new FormAttachment(0, 10);
		fd_formComp.right = new FormAttachment(100, -10);
		fd_formComp.bottom = new FormAttachment(100, -10);
		formComp.setLayoutData(fd_formComp);
		formComp.setVisible(false);
		
		//form
		toolkit = new FormToolkit(formComp.getDisplay());
		form = toolkit.createScrolledForm(formComp);
		final FormData scrolledFormData = new FormData();
		scrolledFormData.top = new FormAttachment(0, 0);
		scrolledFormData.left = new FormAttachment(0, 0);
		scrolledFormData.right=new FormAttachment(100, 0);
		scrolledFormData.bottom=new FormAttachment(100, 0);
		form.setLayoutData(scrolledFormData);
		form.getBody().setLayout(new TableWrapLayout());
		
		//formLabel
		descriptionFormLabel=toolkit.createLabel(form.getBody(), "", SWT.WRAP);
		
		//refExpandableComp
		refExpandableComp = 
			toolkit.createExpandableComposite(
				form.getBody(), 
				ExpandableComposite.TREE_NODE|
				ExpandableComposite.CLIENT_INDENT);
		refExpandableComp.setText(Messages.RepositoryElementComposite_RefactoringsBelong);
		TableWrapData td = new TableWrapData();
		td.colspan = 1;
		refExpandableComp.setLayoutData(td);
		refExpandableComp.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		Composite refExpandableClient = toolkit.createComposite(refExpandableComp,SWT.WRAP);
		refExpandableClient.setLayout(new RowLayout());
		refExpandableComp.setClient(refExpandableClient);
		
		sash_form.setWeights(new int[] {5 , 1 });
		
		checkForCompletion();
		enableInputButtons(false);
		selectedTimes = new Hashtable<String,Integer>();
	}
	
	/**
	 * Puebla la lista de elementos disponibles con las claves ordenadas 
	 * alfabéticamente de una tabla <i>hash</i>.
	 * 
	 * @param repository tabla <i>hash</i> cuyas claves se deben añadir a la lista
	 * (en principio, se trata de una tabla de elementos concretos del repositorio,
	 * en la que se utiliza como clave el nombre de cada uno de ellos).
	 */
	protected void fillRepositoryList(Map<String, String> repository) {
		
		// Se ordena la lista de candidatos.
    	Vector<String> orderedList = new Vector<String>(repository.keySet()); 
    	Collections.sort(orderedList);
		
		// Se añade a la lista.
    	for (String nextKey : orderedList){
    		if(!nextKey.startsWith("Test")){
    			l_Available.add(nextKey);
    			a_Available.put(nextKey,
    				EclipseBasedJavadocReader.INSTANCE.getTypeJavaDocAsPlainText(getElementFullyQualifiedName(nextKey)));
    		}
    	}
    	isSearched=false;

	}
	
	/**
	 * Rellena la lista de elementos en función del patrón de búsqueda.
	 * 
	 * @param patron Patrón de búsqueda.
	 */
	protected void fillSearchTypesList(String patron){
		boolean search = !(patron.trim().equals("") || patron.trim().equals("*"));

		// se vacia la lista l_Available
		l_Available.removeAll();
		
		if(search){
			try {
				SearchableType typeToSearch=SearchableType.PREDICATE;
				if(title.equals(RefactoringWizardPage4.ACTIONS_TITLE))
					typeToSearch=SearchableType.ACTION;
				Set<QueryResult> queryResultTypes=SearchingFacade.INSTANCE.search(typeToSearch, patron);
				//se muestra por orden de relevancia
				for(QueryResult qResult: queryResultTypes){
					if(cb_qualified.getSelection())
						l_Available.add(qResult.getClassName());
					else
						l_Available.add(getElementNotQualifiedName(qResult.getClassName()));
				}
				isSearched=true;
			} catch (ParseException e) {
				String message = Messages.RepositoryElementComposite_SearchNotSucceded
								 + ".\n" + e.getMessage(); //$NON-NLS-1$
				logger.error(message);
				e.printStackTrace();
			}
		}else{
			ArrayList<String> orderedList=new ArrayList<String>();
			for(String element : a_Available.keySet()){
				if(cb_qualified.getSelection())
					orderedList.add(getElementFullyQualifiedName(element));
				else
					orderedList.add(element);
			}
			Collections.sort(orderedList);
			//se muestra en orden alfabético
			for(String element : orderedList)
				l_Available.add(element);
			isSearched=false;
		}
			
	}

	/**
	 * Obtiene el nombre cualificado del elemento.
	 * 
	 * @param element elemento
	 * @return nombre cualificado del elemento
	 */
	private String getElementFullyQualifiedName(String element) {
		RefactoringMechanismType type = RefactoringMechanismType.PRECONDITION;
		if (title.equals(RefactoringWizardPage3.PRECONDITIONS_TITLE) ||
			title.equals(RefactoringWizardPage5.POSTCONDITIONS_TITLE)){
			type = RefactoringMechanismType.PRECONDITION;
		} else if (title.equals(RefactoringWizardPage4.ACTIONS_TITLE)) {
			type = RefactoringMechanismType.ACTION;
		}
		String qualified_name = PluginStringUtils
				.getMechanismFullyQualifiedName(type, element);
		return qualified_name;
	}

	/**
	 * Obtiene el nombre sin cualificar del elemento a partir de su nombre
	 * cualificado.
	 * 
	 * @param qualifiedName
	 *            nombre cualificado
	 * @return nombre sin cualificar del elemento
	 */
	private String getElementNotQualifiedName(String qualifiedName) {
		final int SEPARATOR='.';
		int pos=qualifiedName.lastIndexOf(SEPARATOR);
		if(pos!=-1){
			return qualifiedName.substring(pos+1);
		}
		return qualifiedName;		
	}
	
	/**
	 * Deselecciona todos los elementos de cualquiera de las listas y vacía
	 * la tabla de parámetros.
	 */
	protected void deselect(){
		l_Selected.deselectAll();
		l_Available.deselectAll();
		
		enableInputButtons(false);
		emptyTable();
	}

	/**
	 * Puebla la lista de elementos que forman ya parte de una refactorización
	 * que se haya cargado para ser editada.
	 * 
	 * @param elements
	 *            elementos que forman ya parte de la refactorización.
	 * @param refactoring
	 *            refactorización cuyos elementos se cargan.
	 * @param typePart
	 *            tipo de elementos que se cargarán (uno de precondicion, accion
	 *            o postcondición)
	 */
	protected void fillSelectedListAsRefactoringMechanism(java.util.List<RefactoringMechanismInstance> elements,
			DynamicRefactoringDefinition refactoring,
			RefactoringMechanismType typePart) {
		// Para cada elemento de la refactorización (predicado o acción).
		for (String next : RefactoringMechanismInstance.getMechanismListClassNames(elements)){
			String element = next;
			if(selectedTimes.containsKey(next)){
				int number = selectedTimes.get(next)+1;
				element = next +" (" + number + ")";
				selectedTimes.put(next, number);
			}else{
				selectedTimes.put(next, 1);
				element = next+" (" + 1 + ")";
			}
			// Se añade su nombre a la lista de componentes.
			l_Selected.add(element);
			
			// Se crea un nuevo elemento para representarlo.
			RepositoryItem elemento = new RepositoryItem(next, typePart);
			
			// Se obtiene su lista de parámetros.
			RefactoringMechanismInstance mechanism = refactoring.getMechanismsWithName(next, typePart).iterator().next();
			// Para cada parámetro.
			for (String nextParam : mechanism.getInputParameters()){
				// Se busca la entrada de la refactorización con ese nombre.
				InputParameter[] previousInputs = inputsPage.getInputTable(); 
				for (int i = 0; i < previousInputs.length; i++)
					if (previousInputs[i].getName().equals(nextParam)){
						elemento.addParameter(previousInputs[i]);
						break;
					}	
			}
			selectedTable.put(element, elemento);
		}
		l_Selected.deselectAll();
	}
	
	/**
	 * Puebla la tabla de parámetros ambiguos con los datos de los parámetros ya
	 * asociados al elementos concreto del repositorio que se indica.
	 * 
	 * @param selected el elemento del repositorio cuyos parámetros se deben
	 * ver reflejados en la tabla.
	 * @param type tipo de los elementos que van a ir en el combo.
	 */
	private void fillInTable(RepositoryItem selected , String type){
			
			TableItem item = new TableItem(tb_parameters, SWT.BORDER);
			item.setText(new String[]{type, "",""}); //$NON-NLS-1$ //$NON-NLS-2$
				
				TableEditor editor = new TableEditor(tb_parameters);
				CCombo combo = new CCombo(tb_parameters, SWT.NONE);
				
				//indica si el parametero ya ha sido estblecido
				int establecido = 0;
				
				//en caso de estar establecido pone el nombre del parámetro como texto del comboBox
				for(InputParameter p: selected.getParameters()){
					try{
						if((Class.forName(type)).isAssignableFrom( Class.forName(p.getType()))){
							combo.setText(p.getName());
							establecido=1;
							break;
						}
					}catch(ClassNotFoundException exception){
					logger.error(
							Messages.MainInputValidator_ErrorLoading 
							+ ":\n\n" + exception.getMessage()); //$NON-NLS-1$
					}
				}
				
				//en caso de no estar establecido pone "..." como texto del comboBox
				if(establecido==0){
					combo.setText("...");
				}
				
				combo.addSelectionListener(new ParameterListener(combo, 
					tb_parameters.getItemCount() - 1));
			    
				
				for(InputParameter nextInput : inputsPage.getInputs()){
					try{
						//añadimos al combo las que coinciden con el tipo introducido como parámetro
						if((Class.forName(type)).isAssignableFrom( Class.forName(nextInput.getType())))
							combo.add(nextInput.getName() + " (" + nextInput.getType() + ")"); //$NON-NLS-1$ //$NON-NLS-2$	
					}catch(ClassNotFoundException exception){
						logger.error(
								Messages.MainInputValidator_ErrorLoading 
								+ ":\n\n" + exception.getMessage()); //$NON-NLS-1$
					}
				}
				
				combosTable.put(item, combo);
				
				
				editor.grabHorizontal = true;
				editor.setEditor(combo, tb_parameters.getItem(
					tb_parameters.getItemCount()-1), 1);
				
		    		
	}
	
	/**
	 * Vacía la tabla de parámetros ambiguos.
	 */
	private void emptyTable() {
		TableItem items[] = tb_parameters.getItems();
		for (int i = 0; i < items.length; i++){
			combosTable.remove(items[i]).dispose();
			items[i].dispose();
		}
		tb_parameters.removeAll();
	}
	
	/**
	 * Activa o desactiva todos los botones que solo deben permanecer operativos
	 * cuando una entrada se encuentre seleccionada.
	 * 
	 * @param enable <code>true</code> si se desean activar los botones;
	 * <code>false</code> si se desean desactivar.
	 */
	public void enableInputButtons(boolean enable){
		this.bt_delElement.setEnabled(enable);
		this.bt_moveUp.setEnabled(enable);
		this.bt_moveDown.setEnabled(enable);
	}

	/**
	 * Añade los elementos concretos disponibles del repositorio seleccionados a
	 * la lista de elementos concretos ya seleccionados.
	 * 
	 * @param type
	 *            tipo del elemento
	 */
	private void addElements(RefactoringMechanismType type){
		String[] selected = l_Available.getSelection();
		//En caso de estar seleccionado el checkbox nos quedamos solamente
		//con la parte final del nombre que es el nombre simple
		//del elemento.
		if(this.cb_qualified.getSelection()){
			for(int i=0; i < selected.length; i++ ){
				String element = "";
				StringTokenizer st_element = new StringTokenizer(selected[i],".");
				while(st_element.hasMoreTokens()){
					element = st_element.nextElement().toString();
				}
				selected[i]=element;
			}
		}
		for(int i=0; i < selected.length; i++){
			String element = selected[i];
			if(selectedTimes.containsKey(selected[i])){
				int number = selectedTimes.get(selected[i])+1;
				element = selected[i] +" (" + number + ")";
				selectedTimes.put(selected[i], number);
			}else{
				selectedTimes.put(selected[i], 1);
				element = selected[i]+" (" + 1 + ")";
			}	
			l_Selected.add(element);
			RepositoryItem newElement = new RepositoryItem(selected[i], type);
			
			selectedTable.put(element, newElement);
		}
		
		l_Selected.deselectAll();
		enableInputButtons(false);
		bt_addElement.setEnabled(false);
		emptyTable();
		
		checkForCompletion();		
	}
	
	/**
	 * Elimina de la lista de elementos concretos ya añadidos a la refactorización
	 * aquéllos que se encuentren seleccionados.
	 */
	private void removeElements(){
		String[] selected = l_Selected.getSelection();
		for(int i=0; i < selected.length; i++){
			l_Selected.remove(selected[i]);
			selectedTable.remove(selected[i]);
		}
		
		l_Selected.deselectAll();
		enableInputButtons(false);
		bt_addElement.setEnabled(false);
		emptyTable();
		
		checkForCompletion();
	}	
	
	/**
	 * Obtiene una tabla asociativa con los elementos del repositorio 
	 * seleccionados en esta pestaña para formar parte de la refactorización,
	 * así como con su lista de parámetros.
	 * 
	 * <p>En la tabla devuelta, la clave la constituye el nombre simple del 
	 * predicado o acción, y el valor es un <code>ArrayList</code> que contiene
	 * las entradas asignadas al elemento del repositorio. El formato de cada
	 * entrada es el de un <i>array</i> de cadenas, donde cada cadena es un
	 * atributo del parámetro de entrada.</p>
	 * 
	 * @return una tabla con los elementos del repositorio seleccionados y su
	 * lista de parámetros.
	 * 
	 */
	public Map<String, RepositoryItem> getParameters() {
		return selectedTable;
	}
	
	/**
	 * Obtiene la lista de elementos del repositorio seleccionados en esta pestaña
	 * como componentes de la refactorización.
	 * 
	 * @return la lista de elementos del repositorio seleccionados en esta pestaña
	 * como componentes de la refactorización.
	 */
	public ArrayList<String> getElements(){
		ArrayList<String> elements = new ArrayList<String>(l_Selected.getItemCount());
		for (int i = 0; i < l_Selected.getItemCount(); i++){
			elements.add(l_Selected.getItem(i));
		}    
		return elements;
	}
	
	/**
	 * Comprueba si todos los campos necesarios se han completado y si se puede 
	 * continuar con el siguiente paso del asistente.
	 * 
	 * <p>Para que se pueda continuar no deberá quedar ningún elemento del 
	 * repositorio con atributos pendientes de completar.</p>
	 */
	private void checkForCompletion() {
		for (String nextName : l_Selected.getItems()){
			// Se recupera el elemento del repositorio asociado.
			RepositoryItem nextElement = selectedTable.get(nextName);
				
			if (! isElementComplete(nextElement)){
				Object[] messageArgs = {nextName};
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(
					Messages.RepositoryElementTab_ParametersMissing);
				
				mainPage.updateStatus(formatter.format(messageArgs) + "."); //$NON-NLS-1$
				return;
			}
		}
		
		mainPage.updateStatus(null);
	}
	
	/**
	 * Comprueba si se han especificado ya todos los argumentos necesarios
	 * para el constructor del elemento del repositorio dado.
	 * 
	 * @param element el elemento del repositorio.
	 * 
	 * @return <code>true</code> si la tabla de parámetros contiene ya tantos
	 * elementos como parámetros necesita el constructor del elemento dado;
	 * <code>false</code> en caso contrario.
	 */
	private boolean isElementComplete(RepositoryItem element){
		
		Class<?>[] parameters = getConstructorParameters(element);
		
		if (parameters != null){
			
			// Si al elemento todavía le faltan parámetros para completar la
			// lista de argumentos formales del constructor.
			if (element.getParameters().size() < parameters.length)
				return false;
			// Si alguno de los parámetros todavía no ha sido configurado.
			for (InputParameter next : element.getParameters())
				if (next.getName() == null || next.getName().length() == 0)
					return false;
			
			return true;
		}
				
		return false;
	}
	
	/**
	 * Obtiene la lista de parámetros del constructor de un elemento del
	 * repositorio.
	 * 
	 * @param element el elemento los parámetros de cuyo constructor se obtienen.
	 * 
	 * @return la lista de parámetros del constructor.
	 */
	private Class<?>[] getConstructorParameters(RepositoryItem element){	
				
		try {
			// Se recupera su clase y los argumentos formales del constructor.
			
			Class<?> itemClass = Class.forName(getElementFullyQualifiedName(element.getName()));
			Class<?>[] parameters = itemClass.getConstructors()[0].getParameterTypes();	
			return parameters;
		}
		catch (ClassNotFoundException exception){
			String message = 
				Messages.RepositoryElementTab_ClassNotFound + 
				".\n" + exception.getMessage();  //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(composite.getParent().getShell(), Messages.RepositoryElementTab_Error, message);
		}catch (SecurityException e){
			e.printStackTrace();
		}catch (Exception e){
			System.out.println("Excepcion " + e);
		}
		
		return null;
	}
	
	/** 
	 * Representa en elemento concreto del repositorio (un predicado o una acción).
	 *
	 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
	 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	protected class RepositoryItem {
		
		/**
		 * El nombre de la clase del elemento del repositorio.
		 */
		private String name;
		
		/**
		 * Parámetros ambiguos del elemento del repositorio.
		 */
		private ArrayList<InputParameter> parameters;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            nombre del elemento del repositorio.
		 * @param type
		 *            tipo del elemento
		 */
		public RepositoryItem(String name, RefactoringMechanismType type) {
			this.name = name;
			parameters = new ArrayList<InputParameter>();
		}		
		
		/**
		 * Obtiene el nombre del elemento del repositorio.
		 * 
		 * @return el nombre del elemento del repositorio
		 */
		@Override
		public String toString() {
			return name;
		}	
		
		/**
		 * Obtiene el nombre del elemento del repositorio.
		 * 
		 * @return el nombre del elemento del repositorio.
		 * 
		 */
		public String getName() {
			return name;
		}		

		
		/**
		 * Añade un parámetro ambiguo al elemento del repositorio.
		 * 
		 * @param parameter el parámetro ambiguo que se debe añadir.
		 */
		public void addParameter(InputParameter parameter) {
			parameters.add(parameter);
		}		
		
		/**
		 * Obtiene los parámetros ambiguos del elemento del repositorio.
		 * 
		 * @return un <code>ArrayList</code> con la lista de parámetros ambiguos.
		 */
		public java.util.List<InputParameter> getParameters() {
			return parameters;
		}
		
		/**
		 * Obtiene los parámetros ambiguos del elemento del repositorio.
		 * 
		 * @return un <code>ArrayList</code> con la lista de parámetros ambiguos.
		 */
		public java.util.List<String> getParametersNames() {
			final java.util.List<String> parametersAsString = new ArrayList<String>();
			for(InputParameter parameter :parameters){
				parametersAsString.add(parameter.getName());
			}
			return parametersAsString;
		}
		
	}

	
	/**
	 * Permite observar el desplegable que permite asignar un valor de entrada
	 * a uno de los parámetros de la tabla.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ParameterListener implements SelectionListener {
		
		/**
		 * Desplegable cuyas modificaciones observa el <i>listener</i>.
		 */
		private CCombo combo;
		
		/**
		 * Fila de la tabla de parámetros en la que se encuentra el combo.
		 */
		private int position;
		
		/**
		 * Constructor.
		 * 
		 * @param combo desplegable cuyas modificaciones se registran.
		 * @param position fila de la tabla de parámetros en la que se encuentra 
		 * el desplegable observado.
		 */		
		public ParameterListener(CCombo combo, int position){
			this.combo = combo;
			this.position = position;
		}
		
		/**
		 * Recibe notificaciones cuando el elemento observado ha sido seleccionado
		 * por defecto.
		 * 
		 * @param e evento de selección disparado en la interfaz.
		 * 
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e){
			widgetSelected(e);
		}
		
		/**
		 * Recibe notificaciones cuando el elemento observado ha sido seleccionado.
		 * 
		 * @param e evento de selección disparado en la interfaz.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e){
			if (l_Selected.getSelection()[0] != null){
				RepositoryItem element = 
					selectedTable.get(l_Selected.getSelection()[0]);
				if (element != null){
					InputParameter parameter = 
						element.getParameters().get(position);
					if (parameter != null){
						// Se recupera la entrada original por su posición en el
						// desplegable.
						InputParameter input = new InputParameter.Builder("").build();
						for(InputParameter inp : inputsPage.getInputTable()){
							if(combo.getItem(combo.getSelectionIndex()).toString().equals(inp.getName()+" ("+inp.getType()+")"))
								input = inp;
						}
						if (input != null){
							int index = element.getParameters().indexOf(parameter);
							element.getParameters().remove(parameter);
							element.getParameters().add(index, input);
							
							checkForCompletion();
						}
					}
				}
			}			
		}
	}

	/**
	 * Actualiza la pestaña del asistente en función del elemento del 
	 * repositorio que se encuentre seleccionado sobre la lista de elementos
	 * añadidos como parte de la refactorización.
	 * 
	 * <p>Actualiza la tabla con los parámetros asignados hasta el momento al
	 * elemento seleccionado, así como los botones que sea necesario activar o
	 * desactivar en función del estado de la selección.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ElementSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificación de que un componente de la lista de elementos
		 * del repositorio que forman parte de la refactorización ha sido 
		 * seleccionado.
		 * 
		 * @param e el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			emptyTable();
			
			
			
			// Si hay elementos seleccionados entre los elegidos
			if (l_Selected.getSelectionCount() == 1 &&
				l_Selected.getItem(l_Selected.getSelectionIndex()) != null){
				enableInputButtons(true);
					
				RepositoryItem element = 
					selectedTable.get(l_Selected.getSelection()[0]);

				if (element != null){
					if(element.getParameters().isEmpty()){
						//inicializamos los parámetros del elemento
						InputParameter input = new InputParameter.Builder("").build();
						
						for(int i=0; i<getConstructorParameters(element).length; i++) {
							element.addParameter(input);
						}
					}
					tb_parameters.setEnabled(true);
					Class<?>[] parameters = getConstructorParameters(element);
					for(Class<?> parameter : parameters){
						String type = "";
						StringTokenizer st_type = new StringTokenizer(parameter.toString() , " ");
						while(st_type.hasMoreTokens())
							type = st_type.nextToken().toString();
						fillInTable(element,type);
						
					}
				}
			}
			// Si no hay ningún elemento seleccionado o hay más de uno.
			else {
				tb_parameters.setEnabled(false);
			}
		}
		
		/**
		 * Recibe notificaciones cuando el elemento observado ha sido seleccionado
		 * por defecto.
		 * 
		 * @param e evento de selección disparado en la interfaz.
		 * 
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}

	/**
	 * Recibe notificaciones cuando uno de los miembros de la lista de 
	 * elementos concretos del repositorio disponibles es seleccionado.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RepositorySelectionListener implements SelectionListener {

		/**
		 * Recibe una notificación de que un miembro de la lista de elementos
		 * concretos del repositorio disponibles ha sido seleccionado.
		 * 
		 * <p>Activa el botón que permite añadir el o los elementos seleccionados
		 * a la lista de componentes de la refactorización.</p>
		 * 
		 * @param e el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {

			String qualifiedName=l_Available.getItem(l_Available.getSelectionIndex()).toString();
			
			if(!cb_qualified.getSelection())
				qualifiedName=getElementFullyQualifiedName(l_Available.getItem(l_Available.getSelectionIndex()).toString());

			String path=RefactoringConstants.REFACTORING_JAVADOC + "/" + qualifiedName.replace('.', '/') + ".html";
			try{
				if(new File(FileLocator.toFileURL(RefactoringPlugin.getDefault().getBundle().getEntry(path)).getFile()).exists())
					navegador.setUrl(FileLocator.toFileURL(RefactoringPlugin.getDefault().getBundle().getEntry(path)) + "#skip-navbar_top");
				else
					navegador.setUrl(FileLocator.toFileURL(getClass().getResource(RefactoringConstants.REFACTORING_JAVADOC + "/moon/notFound.html" )).toString());
			}catch(IOException excp){
				throw Throwables.propagate(excp);
			}

			//form
			form.getParent().setVisible(false);
			//previamente eleminamos las etiquetas que contienen los desplegables
			Control labels[]=null;
			labels=((Composite)refExpandableComp.getClient()).getChildren();
			for(int i=0;i<labels.length;i++)
				labels[i].dispose();
			
			form.setText(qualifiedName);
			
			String notQualifiedName=getElementNotQualifiedName(qualifiedName);
			descriptionFormLabel.setText(a_Available.get(notQualifiedName));
			
			//refactoringsInputType
			ArrayList<DynamicRefactoringDefinition> refactorings=null;
			if(title.equals(RefactoringWizardPage3.PRECONDITIONS_TITLE)){
				refactorings=new ArrayList<DynamicRefactoringDefinition>(

	XMLRefactoringsCatalog.getInstance().getRefactoringsContainsPrecondition(notQualifiedName));
			}else{
				if(title.equals(RefactoringWizardPage4.ACTIONS_TITLE)){
					refactorings=new ArrayList<DynamicRefactoringDefinition>(
							XMLRefactoringsCatalog.getInstance().getRefactoringsContainsAction(notQualifiedName));
				}else{ //title.equals(RefactoringWizardPage5.POSTCONDITIONS_TITLE)
					refactorings=new ArrayList<DynamicRefactoringDefinition>(
							XMLRefactoringsCatalog.getInstance().getRefactoringsContainsPostcondition(notQualifiedName));
				}
			}
			Collections.sort(refactorings);
			Label refLabel=null;
			String refName=null;
			for(int i=0; i<refactorings.size();i++){
				refName=refactorings.get(i).getName();
				if(i<refactorings.size()-1)
					refName+=",";
				refLabel = toolkit.createLabel((Composite)refExpandableComp.getClient(),refName);
				refLabel.setData(refactorings.get(i));
				RefactoringTooltip tooltip = new RefactoringTooltip(refLabel);
				tooltip.setPopupDelay(200);
			}
			refExpandableComp.setExpanded(!refactorings.isEmpty());
			
			form.getParent().setVisible(true);
			form.reflow(true);
			
			
			if (l_Available.getSelectionCount() > 0 &&
				l_Available.getItem(l_Available.getSelectionIndex()) != null)
				bt_addElement.setEnabled(true);
			else 
				bt_addElement.setEnabled(false);
		}
		
		/**
		 * Recibe notificaciones cuando el elemento observado ha sido seleccionado
		 * por defecto.
		 * 
		 * @param e evento de selección disparado en la interfaz.
		 * 
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
}
