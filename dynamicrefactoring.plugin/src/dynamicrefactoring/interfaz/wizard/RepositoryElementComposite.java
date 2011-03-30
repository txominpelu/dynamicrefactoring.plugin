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
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
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

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.interfaz.dynamic.RepositoryElementProcessor;
import dynamicrefactoring.interfaz.wizard.listener.ListDownListener;
import dynamicrefactoring.interfaz.wizard.listener.ListUpListener;
import dynamicrefactoring.plugin.xml.classifications.imp.PluginCatalog;

/**
 * Contenido de las p�ginas en la que se establecen los diferentes mecanismos de la refactorizaci�n.
 * 
 * <p>Implementa de forma gen�rica la interfaz y el proceso de adici�n de 
 * componentes concretos del repositorio a la refactorizaci�n.</p>
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
	 * P�gina del asistente a partir de la cual se pueden obtener las entradas
	 * de la refactorizaci�n.
	 */

	private RefactoringWizardPage2 inputsPage;
	
	/**
	 * P�gina del asistente que contiene este contenedor.
	 */
	private IRefactoringWizardElementPage mainPage;
	
	/**
	 * T�tulo de la operaci�n.
	 */
	private String title;
	
	/**
	 * Compuesto asociada al tipo de elementos del repositorio que se manejan.
	 */
	private Composite composite;
	
	/**
	 * Tabla con los par�metros ambiguos del elemento del repositorio.
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
	 * muestran los nombres de los mecanismos de la refactorizaci�n a seleccionar.
	 */
	private Button cb_qualified;
	
	/**
	 * Bot�n que permite a�adir un elemento del repositorio a la lista de elementos
	 * que forman parte de la refactorizaci�n.
	 */
	private Button bt_addElement;
	
	/**
	 * Bot�n que permite eliminar un elemento seleccionado como parte de la
	 * refactorizaci�n y devolverlo a la lista de elementos del repositorio.
	 */
	private Button bt_delElement;
	
	/**
	 * Bot�n que permite mover hacia arriba en la lista elementos seleccionados 
	 * como parte de la refactorizaci�n.
	 */
	private Button bt_moveUp;
	
	/**
	 * Bot�n que permite mover hacia abajo en la lista elementos seleccionados 
	 * como parte de la refactorizaci�n.
	 */
	private Button bt_moveDown;
	
	/**
	 * Tabla con el n�mero de veces que se ha seleccionado un mecanismo. Solo
	 * aparecen las precondiciones seleccionadas.
	 */
	private Hashtable<String, Integer>selectedTimes;
	
	/**
	 * Navegador en el que se muestra informaci�n relativa al elemento seleccionado dentro de
	 * la lista de elementos.
	 */
	private Browser navegador;
	
	/**
	 * Caja de texto que permite introducir al usuario el patr�n de la b�squeda.
	 */
	private Text tSearch;
	
	/**
	 * Bot�n que permite realizar la b�squeda.
	 */
	private final Button bSearch;
	
	/**
	 * Bot�n por defecto de esta p�gina del wizard.
	 */
	private Button bDefault;
	
	/**
	 * Tabla de elementos ya seleccionados.
	 *  
	 * <p>Se utiliza como clave el nombre del elemento concreto del repositorio, 
	 * y como valor, un objeto de tipo <code>RepositoryItem</code> con toda la 
	 * informaci�n asociada a la precondici�n.</p>
	 */
	private Hashtable<String, RepositoryItem> selectedTable;
	
	/**
	 * Tabla asociativa que permite mantener un seguimiento de las listas 
	 * desplegables que han sido asociadas a los elementos de la tabla de
	 * par�metros.
	 */
	private Hashtable<TableItem, CCombo> combosTable;
	
	/**
	 * Array de nombres de los elementos del repositorio disponibles , necesitamos esta
	 * a parte de la anterior debido a que a la hora
	 * una b�squeda de un elemento en l_Available no van a aparecer todos los disponibles
	 * y estos tienen que estar recogidos en alg�n sitio.
	 */
	private ArrayList<String> a_Available;
	
	private FormToolkit toolkit;
	private ScrolledForm form;
	private Label descriptionFormLabel;
	private ExpandableComposite refExpandableComp;
	
	/**
	 * Constructor.
	 * 
	 * @param parent elemento padre de la pesta�a.
	 * @param inputsPage p�gina del asistente a partir de la cual se pueden 
	 * obtener las entradas que se han seleccionado para la refactorizaci�n.
	 * @param title t�tulo asociado a la pesta�a.
	 * @param main P�gina donde se va a a�adir esta estructura.
	 */
	public RepositoryElementComposite(Composite parent, String title, 
		IWizardPage inputsPage,IRefactoringWizardElementPage main){
		
		selectedTable = new Hashtable<String, RepositoryItem>();
		combosTable = new Hashtable<TableItem, CCombo>();
		a_Available = new ArrayList<String>();
		
		
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
		
		/*Para establecer el tama�o de la ventana*/
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
		
		bSearch = new Button(control, SWT.PUSH);
		final FormData fd_bsearch = new FormData();
		fd_bsearch.right = new FormAttachment(0, 225);
		fd_bsearch.top = new FormAttachment(0, 26);
		fd_bsearch.left = new FormAttachment(tSearch, 5, SWT.RIGHT);
		bSearch.setLayoutData(fd_bsearch);
		bSearch.setImage(RefactoringImages.getSearchIcon());
		bSearch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fillSearchTypesList(tSearch.getText(), false);	
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
				fillSearchTypesList(tSearch.getText(), true);	
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
				addElements();
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
		
		
		// Se crean las columnas para la tabla de par�metros.
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
		}catch(IOException e){}
		
		//TODO: nuevo
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
	 * alfab�ticamente de una tabla <i>hash</i>.
	 * 
	 * @param repository tabla <i>hash</i> cuyas claves se deben a�adir a la lista
	 * (en principio, se trata de una tabla de elementos concretos del repositorio,
	 * en la que se utiliza como clave el nombre de cada uno de ellos).
	 */
	protected void fillRepositoryList(HashMap<String, String> repository){
		
		// Se ordena la lista de candidatos.
    	Vector<String> orderedList = new Vector<String>(repository.keySet()); 
    	Collections.sort(orderedList);
		
    		
		// Se a�ade a la lista.
    	for (String nextKey : orderedList){
    		if(!nextKey.startsWith("Test")){
    			l_Available.add(nextKey);
    			a_Available.add(nextKey);
    		}
    	}

	}
	
	/**
	 * Rellena la lista de elementos en funci�n del patr�n de b�squeda.
	 * 
	 * @param qualified indica si lo que ha cambiado es el checkbox qualified o no
	 * @param patron Patr�n de b�squeda.
	 */
	protected void fillSearchTypesList(String patron, Boolean qualified){
		l_Available.removeAll();
		for(String element : a_Available){
			if(!(qualified && cb_qualified.getSelection()) ){
				if(patron == "" || element.matches(patron))
					l_Available.add(element);
		    }else{
				String qualified_name = new String();
				if (title.equals(RefactoringWizardPage3.PRECONDITIONS_TITLE) ||
					title.equals(RefactoringWizardPage5.POSTCONDITIONS_TITLE)){
					if (RepositoryElementProcessor.isPredicateJavaDependent(element))
						qualified_name = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
					else
						qualified_name = RefactoringConstants.PREDICATES_PACKAGE;
				}			
				else if (title.equals(RefactoringWizardPage4.ACTIONS_TITLE)){
					if (RepositoryElementProcessor.isActionJavaDependent(element))
						qualified_name = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
					else
						qualified_name = RefactoringConstants.ACTIONS_PACKAGE;
				}
				qualified_name = qualified_name + element;
				if((qualified==true && cb_qualified.getSelection()==true)){
					if(patron == "" || element.matches(patron))
						l_Available.add(qualified_name);
				}if(patron == "" || qualified_name.matches(patron)){
					if(!cb_qualified.getSelection())
						l_Available.add(element);
					else
						l_Available.add(qualified_name);
				}
			}
				
		}
			
	}
	
	/**
	 * Deselecciona todos los elementos de cualquiera de las listas y vac�a
	 * la tabla de par�metros.
	 */
	protected void deselect(){
		l_Selected.deselectAll();
		l_Available.deselectAll();
		
		enableInputButtons(false);
		emptyTable();
	}
	
	/**
	 * Puebla la lista de elementos que forman ya parte de una refactorizaci�n
	 * que se haya cargado para ser editada.
	 * 
	 * @param elements elementos que forman ya parte de la refactorizaci�n.
	 * @param refactoring refactorizaci�n cuyos elementos se cargan.
	 * @param typePart tipo de elementos que se cargar�n (uno de 
	 * {@link RefactoringConstants#PRECONDITION}, 
	 * {@link RefactoringConstants#ACTION} o 
	 * {@link RefactoringConstants#POSTCONDITION}.)
	 */
	protected void fillSelectedList(java.util.List<String> elements,
		DynamicRefactoringDefinition refactoring, int typePart){
		// Para cada elemento de la refactorizaci�n (predicado o acci�n).
		for (String next : elements){
			String element = next;
			if(selectedTimes.containsKey(next)){
				int number = selectedTimes.get(next)+1;
				element = next +" (" + number + ")";
				selectedTimes.put(next, number);
			}else{
				selectedTimes.put(next, 1);
				element = next+" (" + 1 + ")";
			}
			// Se a�ade su nombre a la lista de componentes.
			l_Selected.add(element);
			
			// Se crea un nuevo elemento para representarlo.
			RepositoryItem elemento = new RepositoryItem(next, 
				RepositoryElementProcessor.isActionJavaDependent(next) ||
				RepositoryElementProcessor.isPredicateJavaDependent(next));
			
			// Se obtiene su lista de par�metros.
			java.util.List<String[]> parameters =
				refactoring.getAmbiguousParameters(next, typePart);
			// Para cada par�metro.
			for (String[] nextParam : parameters){
				// Se busca la entrada de la refactorizaci�n con ese nombre.
				InputParameter[] previousInputs = inputsPage.getInputTable(); 
				for (int i = 0; i < previousInputs.length; i++)
					if (previousInputs[i].getName().equals(nextParam[0])){
						elemento.addParameter(previousInputs[i]);
						break;
					}	
			}
			selectedTable.put(element, elemento);
		}
		l_Selected.deselectAll();
	}
	
	/**
	 * Puebla la tabla de par�metros ambiguos con los datos de los par�metros ya
	 * asociados al elementos concreto del repositorio que se indica.
	 * 
	 * @param selected el elemento del repositorio cuyos par�metros se deben
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
				
				//en caso de estar establecido pone el nombre del par�metro como texto del comboBox
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
			    
				
				for(String[] nextInput : inputsPage.getInputs()){
					try{
						//a�adimos al combo las que coinciden con el tipo introducido como par�metro
						if((Class.forName(type)).isAssignableFrom( Class.forName(nextInput[0])))
							combo.add(nextInput[1] + " (" + nextInput[0] + ")"); //$NON-NLS-1$ //$NON-NLS-2$	
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
	 * Vac�a la tabla de par�metros ambiguos.
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
	 * A�ade los elementos concretos disponibles del repositorio seleccionados a la 
	 * lista de elementos concretos ya seleccionados.
	 */
	private void addElements(){
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
			RepositoryItem newElement = new RepositoryItem(selected[i],
				RepositoryElementProcessor.isActionJavaDependent(selected[i]) ||
				RepositoryElementProcessor.isPredicateJavaDependent(selected[i]));
			
			selectedTable.put(element, newElement);
		}
		
		l_Selected.deselectAll();
		enableInputButtons(false);
		bt_addElement.setEnabled(false);
		emptyTable();
		
		checkForCompletion();		
	}
	
	/**
	 * Elimina de la lista de elementos concretos ya a�adidos a la refactorizaci�n
	 * aqu�llos que se encuentren seleccionados.
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
	 * seleccionados en esta pesta�a para formar parte de la refactorizaci�n,
	 * as� como con su lista de par�metros.
	 * 
	 * <p>En la tabla devuelta, la clave la constituye el nombre simple del 
	 * predicado o acci�n, y el valor es un <code>ArrayList</code> que contiene
	 * las entradas asignadas al elemento del repositorio. El formato de cada
	 * entrada es el de un <i>array</i> de cadenas, donde cada cadena es un
	 * atributo del par�metro de entrada.</p>
	 * 
	 * @return una tabla con los elementos del repositorio seleccionados y su
	 * lista de par�metros.
	 * 
	 */
	public HashMap<String, java.util.List<String[]>> getParameters() {
		HashMap<String, java.util.List<String[]>> map = new HashMap<String, java.util.List<String[]>>();

		for (Map.Entry<String,RepositoryItem > nextRef : selectedTable.entrySet()){
			RepositoryItem element = nextRef.getValue();
			ArrayList<String[]> parameters = new ArrayList<String[]>(
				element.getParameters().size());
			
			// En la versi�n actual del DTD y los XML, el �nico atributo que
			// tienen los par�metros es su nombre, dado que el resto de 
			// informaci�n aparece en las entradas de la refactorizaci�n.
			for (InputParameter parameter : element.getParameters())
				parameters.add(new String[]{parameter.getName()});

			map.put(nextRef.getKey(), parameters);
		}

		return map;
	}
	
	/**
	 * Obtiene la lista de elementos del repositorio seleccionados en esta pesta�a
	 * como componentes de la refactorizaci�n.
	 * 
	 * @return la lista de elementos del repositorio seleccionados en esta pesta�a
	 * como componentes de la refactorizaci�n.
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
	 * <p>Para que se pueda continuar no deber� quedar ning�n elemento del 
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
	 * @return <code>true</code> si la tabla de par�metros contiene ya tantos
	 * elementos como par�metros necesita el constructor del elemento dado;
	 * <code>false</code> en caso contrario.
	 */
	private boolean isElementComplete(RepositoryItem element){
		
		Class<?>[] parameters = getConstructorParameters(element);
		
		if (parameters != null){
			
			// Si al elemento todav�a le faltan par�metros para completar la
			// lista de argumentos formales del constructor.
			if (element.getParameters().size() < parameters.length)
				return false;
			// Si alguno de los par�metros todav�a no ha sido configurado.
			for (InputParameter next : element.getParameters())
				if (next.getName() == null || next.getName().length() == 0)
					return false;
			
			return true;
		}
				
		return false;
	}
	
	/**
	 * Obtiene la lista de par�metros del constructor de un elemento del
	 * repositorio.
	 * 
	 * @param element el elemento los par�metros de cuyo constructor se obtienen.
	 * 
	 * @return la lista de par�metros del constructor.
	 */
	private Class<?>[] getConstructorParameters(RepositoryItem element){
		
		String path = new String();
		if (title.equals(RefactoringWizardPage3.PRECONDITIONS_TITLE) ||
			title.equals(RefactoringWizardPage5.POSTCONDITIONS_TITLE)){
			if (element.isJavaDependent())
				path = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			else
				path = RefactoringConstants.PREDICATES_PACKAGE;
		}			
		else if (title.equals(RefactoringWizardPage4.ACTIONS_TITLE)){
			if (element.isJavaDependent())
				path = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
			else
				path = RefactoringConstants.ACTIONS_PACKAGE;
		}			
				
		try {
			// Se recupera su clase y los argumentos formales del constructor.
			
			Class<?> itemClass = Class.forName(path + element.getName());
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
	 * Representa en elemento concreto del repositorio (un predicado o una acci�n).
	 *
	 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
	 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RepositoryItem {
		
		/**
		 * El nombre de la clase del elemento del repositorio.
		 */
		private String name;
		
		/**
		 * Par�metros ambiguos del elemento del repositorio.
		 */
		private ArrayList<InputParameter> parameters;
		
		/**
		 * Indica si el elemento es dependiente de Java o no.
		 */
		private boolean javaDependent;
		
		/**
		 * Constructor.
		 * 
		 * @param name nombre del elemento del repositorio.
		 * @param javaDependent si el elemento es dependiente de Java o no.
		 */
		public RepositoryItem(String name, boolean javaDependent) {
			this.name = name;
			this.javaDependent = javaDependent;
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
		 * Establece el nombre del elemento del repositorio.
		 * 
		 * @param name el nombre del elemento del repositorio.
		 * 
		 * @see #getName
		 */
		public void setName(String name) {
			this.name = name;
		}		
		
		/**
		 * Obtiene el nombre del elemento del repositorio.
		 * 
		 * @return el nombre del elemento del repositorio.
		 * 
		 * @see #setName
		 */
		public String getName() {
			return name;
		}		
		
		/**
		 * Indica si el elemento es dependiente de Java o no.
		 * 
		 * @return <code>true</code> si el elemento es dependiente de Java;
		 * <code>false</code> en caso contrario.
		 */
		public boolean isJavaDependent(){
			return javaDependent;
		}
		
		/**
		 * A�ade un par�metro ambiguo al elemento del repositorio.
		 * 
		 * @see #setParameters
		 * @param parameter el par�metro ambiguo que se debe a�adir.
		 */
		public void addParameter(InputParameter parameter) {
			parameters.add(parameter);
		}		
		
		/**
		 * Obtiene los par�metros ambiguos del elemento del repositorio.
		 * 
		 * @return un <code>ArrayList</code> con la lista de par�metros ambiguos.
		 * @see #setParameters
		 */
		public ArrayList<InputParameter> getParameters() {
			return parameters;
		}
		
		/**
		 * Establece los par�metros ambiguos del elemento del repositorio.
		 * 
		 * @param n_parameters un <code>ArrayList</code> con la lista de par�metros ambiguos.
		 * @see #getParameters
		 */
		public void setParameters(ArrayList<InputParameter> n_parameters) {
			parameters = n_parameters;
		}
	}

	
	/**
	 * Permite observar el desplegable que permite asignar un valor de entrada
	 * a uno de los par�metros de la tabla.
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
		 * Fila de la tabla de par�metros en la que se encuentra el combo.
		 */
		private int position;
		
		/**
		 * Constructor.
		 * 
		 * @param combo desplegable cuyas modificaciones se registran.
		 * @param position fila de la tabla de par�metros en la que se encuentra 
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
		 * @param e evento de selecci�n disparado en la interfaz.
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
		 * @param e evento de selecci�n disparado en la interfaz.
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
						// Se recupera la entrada original por su posici�n en el
						// desplegable.
						InputParameter input = new InputParameter("","","","","");
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
	 * Actualiza la pesta�a del asistente en funci�n del elemento del 
	 * repositorio que se encuentre seleccionado sobre la lista de elementos
	 * a�adidos como parte de la refactorizaci�n.
	 * 
	 * <p>Actualiza la tabla con los par�metros asignados hasta el momento al
	 * elemento seleccionado, as� como los botones que sea necesario activar o
	 * desactivar en funci�n del estado de la selecci�n.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ElementSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificaci�n de que un componente de la lista de elementos
		 * del repositorio que forman parte de la refactorizaci�n ha sido 
		 * seleccionado.
		 * 
		 * @param e el evento de selecci�n disparado en la ventana.
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
						//inicializamos los par�metros del elemento
						InputParameter input = new InputParameter("","","","","");
						
						for(int i=0; i<getConstructorParameters(element).length; i++)
							element.addParameter(input);
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
			// Si no hay ning�n elemento seleccionado o hay m�s de uno.
			else {
				tb_parameters.setEnabled(false);
			}
		}
		
		/**
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
		 * Recibe una notificaci�n de que un miembro de la lista de elementos
		 * concretos del repositorio disponibles ha sido seleccionado.
		 * 
		 * <p>Activa el bot�n que permite a�adir el o los elementos seleccionados
		 * a la lista de componentes de la refactorizaci�n.</p>
		 * 
		 * @param e el evento de selecci�n disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			String path="";
			
			if(cb_qualified.getSelection() == false){
				if (title.equals(RefactoringWizardPage3.PRECONDITIONS_TITLE) ||
							title.equals(RefactoringWizardPage5.POSTCONDITIONS_TITLE)){
					if (RepositoryElementProcessor.isPredicateJavaDependent
							(l_Available.getItem(l_Available.getSelectionIndex()).toString()))
						path = RefactoringConstants.JAVA_PREDICATES_PACKAGE; 
					else 
						path = RefactoringConstants.PREDICATES_PACKAGE;
			
				}			
				else if (title.equals(RefactoringWizardPage4.ACTIONS_TITLE)){
					if (RepositoryElementProcessor.isActionJavaDependent
							(l_Available.getItem(l_Available.getSelectionIndex()).toString()))
						path = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
					else
						path = RefactoringConstants.ACTIONS_PACKAGE;
				}
				path = path.replace('.', '/');
				path = RefactoringConstants.REFACTORING_JAVADOC + "/" + path + l_Available.getItem(l_Available.getSelectionIndex()).toString() + ".html";
			}else
				path= RefactoringConstants.REFACTORING_JAVADOC + "/" 
					+ l_Available.getItem(l_Available.getSelectionIndex()).toString().replace('.', '/') + ".html";
			
			try{
				if(new File(FileLocator.toFileURL(RefactoringPlugin.getDefault().getBundle().getEntry(path)).getFile()).exists())
					navegador.setUrl(FileLocator.toFileURL(RefactoringPlugin.getDefault().getBundle().getEntry(path)) + "#skip-navbar_top");
				else
					navegador.setUrl(FileLocator.toFileURL(getClass().getResource(RefactoringConstants.REFACTORING_JAVADOC + "/moon/notFound.html" )).toString());
			}catch(IOException excp){
				excp.printStackTrace();
			}
			
			//TODO: realizar modificaciones oportunas
			//form
			form.getParent().setVisible(false);
			//previamente eleminamos las etiquetas que contienen los desplegables
			Control labels[]=null;
			labels=((Composite)refExpandableComp.getClient()).getChildren();
			for(int i=0;i<labels.length;i++)
				labels[i].dispose();
			
			String elementSelected=l_Available.getItem(l_Available.getSelectionIndex()).toString();
			form.setText(elementSelected);
			descriptionFormLabel.setText("Esta es la descripcion de " + elementSelected);
			
			//refactoringsInputType
			ArrayList<DynamicRefactoringDefinition> refactorings=null;
			if(title.equals(RefactoringWizardPage3.PRECONDITIONS_TITLE)){
				refactorings=new ArrayList<DynamicRefactoringDefinition>(
						PluginCatalog.getInstance().getRefactoringsContainsPrecondition(elementSelected));
			}else{
				if(title.equals(RefactoringWizardPage4.ACTIONS_TITLE)){
					refactorings=new ArrayList<DynamicRefactoringDefinition>(
							PluginCatalog.getInstance().getRefactoringsContainsAction(elementSelected));
				}else{ //title.equals(RefactoringWizardPage5.POSTCONDITIONS_TITLE)
					refactorings=new ArrayList<DynamicRefactoringDefinition>(
							PluginCatalog.getInstance().getRefactoringsContainsPostcondition(elementSelected));
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
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
}