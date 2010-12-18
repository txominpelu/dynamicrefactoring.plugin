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

import com.swtdesigner.ResourceManager;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringPlugin;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;

import dynamicrefactoring.interfaz.dynamic.InputProcessor;
import dynamicrefactoring.interfaz.wizard.listener.ListDownListener;
import dynamicrefactoring.interfaz.wizard.listener.ListUpListener;

import dynamicrefactoring.util.MOONTypeLister;

import java.io.IOException;
import java.io.File;

import java.lang.reflect.Method;

import java.net.MalformedURLException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyAdapter;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.browser.Browser;

import org.eclipse.swt.custom.SashForm;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ISharedImages;



/**
 * Segunda página del asistente de creación o edición de refactorizaciones.
 * 
 * <p>Permite definir las entradas de la refactorizacion, asociarles un tipo y
 * un nombre, determinar a partir de qué otra entrada y mediante la llamada
 * a qué método se puede obtener su valor, así como establecer cuál de todas
 * constituirá la entrada principal a la refactorización.</p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringWizardPage2 extends WizardPage {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringWizardPage2.class);
	
	/**
	 * Botón que permite añadir una nueva entrada.
	 */
	private Button addButton; 
	
	/**
	 * Botón que permite eliminar entradas previamente añadidas.
	 */
	private Button delButton;
	
	/**
	 * Botón que permite desplazar una entrada de la refactorización hacia abajo.
	 */
	private Button downButton;
	
	/**
	 * Botón que permite desplazar una entrada de la refactorización hacia abajo.
	 */
	private Button upButton;
	
	/**
	 * Refactorización configurada a través del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorización) o modificada (si se
	 * está editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;
	
	/**
	 * Lista desplegable con los nombres de los métodos del objeto seleccionado
	 * en {@link #cFrom} que permiten obtener iteradores o colecciones.
	 */
	private Combo cMethod;

	/**
	 * Lista desplegable con los identificadores de parámetros disponibles ya 
	 * en la lista de elegidos.
	 */
	private Combo cFrom;
	
	/**
	 * Campo de texto en que se muestra el nombre del parámetro de entrada
	 * seleccionado sobre la lista de entradas.
	 */
	private Text tName;
	
	/**
	 * Marca de selección que permite indicar si una entrada es la entrada 
	 * principal de la refactorización o no.
	 */
	private Button ch_Root;
	
	/**
	 * Lista de entradas elegidas para la refactorización.
	 */
	private List lInputs;
	
	/**
	 * Lista de tipos disponibles como tipos de los parámetros de entrada.
	 */
	private List lTypes;
	
	/**
	 * Lista de posibles tipos del modelo.
	 * 
	 * <p>Se utiliza como clave el nombre completamente cualificado del tipo,
	 * y como valor, el número de entradas que tienen el tipo seleccionado, menos 1.
	 * </p>
	 */
	private Hashtable<String, Integer> listModelTypes;
	
	/**
	 * Tabla de parámetros de entrada ya introducidos.
	 *  
	 * <p>Se utiliza como clave el nombre completamente cualificado del tipo
	 * de cada entrada concatenado con un espacio en blanco y el número de entrada
	 * que utiliza ese tipo, y como valor, un objeto de tipo <code>InputParameter
	 * </code> con toda la información asociada a la entrada.</p>
	 */
	private Hashtable<String, InputParameter> inputsTable;
	
	/**
	 * Navegador en el que se muestra información relativa al elemento seleccionado dentro de
	 * la lista de tipos con el fin de ayudar al usuario a la compresión de la interfaz.
	 */
	private Browser navegador;
	
	/**
	 * Caja de texto que permite introducir al usuario el patrón de la búsqueda.
	 */
	private Text tSearch;
	
	/**
	 * Botón que permite activar un proceso de búsqueda al usuario.
	 */
	private Button bSearch;
	
	/**
	 * Botón por defecto de esta página del wizard.
	 */
	private Button bDefault;
	

	/**
	 * Constructor.
	 * 
	 * @param refactoring refactorización que se está editando o <code>null
	 * </code> si se está creando una nueva.
	 */
	public RefactoringWizardPage2(DynamicRefactoringDefinition refactoring) {
		super("Wizard page"); //$NON-NLS-1$
		setPageComplete(false);
		setDescription(Messages.RefactoringWizardPage2_DescriptionInput);
		
		this.refactoring = refactoring;
	}
	
	/**
	 * Hace visible o invisible la página del asistente.
	 * 
	 * @param visible si la página se debe hacer visible o no.
	 */
	@Override
	public void setVisible(boolean visible){
		if (visible) {
			Object[] messageArgs = {((RefactoringWizard)getWizard()).getOperationAsString()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.RefactoringWizardPage2_DynamicRefactoring);
			
			setTitle(formatter.format(messageArgs) + " (" + //$NON-NLS-1$
				Messages.RefactoringWizardPage2_Step + ")"); //$NON-NLS-1$
		}
		super.setVisible(visible);
	}

	/**
	 * Crea el contenido de la página del asistente.
	 * 
	 * @param parent el elemento padre de esta página del asistente.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());

		setControl(container);

		
		SashForm sash_form = new SashForm(container, SWT.VERTICAL | SWT.NULL);
		
		final Composite composite = new Composite(sash_form, SWT.NONE);
		composite.setLayout(new FormLayout());
		final FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.left = new FormAttachment(0, 0);
		composite.setLayoutData(fd_composite);

		final Composite composite_1 = new Composite(composite, SWT.NONE);
		final FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(0, 245);
		fd_composite_1.top = new FormAttachment(0, 5);
		fd_composite_1.left = new FormAttachment(0, 5);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FormLayout());

		Label search = new Label(composite_1, SWT.NONE);
		final FormData fd_search = new FormData();
		fd_search.bottom = new FormAttachment(0,58);
		fd_search.top = new FormAttachment(0, 29);
		fd_search.left = new FormAttachment(0, 15);
		search.setLayoutData(fd_search);
		search.setText(Messages.RefactoringWizardPage2_Search);
		
		tSearch = new Text(composite_1,SWT.BORDER);
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

		
		bSearch = new Button(composite_1, SWT.PUSH);
		final FormData fd_bsearch = new FormData();
		fd_bsearch.right = new FormAttachment(0, 225);
		fd_bsearch.top = new FormAttachment(0, 26);//28
		fd_bsearch.left = new FormAttachment(tSearch, 3, SWT.RIGHT);
		bSearch.setLayoutData(fd_bsearch);
		bSearch.setImage(ResourceManager.getPluginImage(
				RefactoringPlugin.getDefault(),
				"icons" + System.getProperty("file.separator") + "search.png"));
		bSearch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fillSearchTypesList(tSearch.getText());	
			}
		});
		
		lTypes = new List(composite_1, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		final FormData fd_list = new FormData();
		fd_list.bottom = new FormAttachment(0,247);
		fd_list.right = new FormAttachment(0, 225);
		fd_list.top = new FormAttachment(0, 59);//28
		fd_list.left = new FormAttachment(0, 10);
		lTypes.setLayoutData(fd_list);
		lTypes.addSelectionListener(new TypeSelectionListener());
		lTypes.setToolTipText(Messages.RefactoringWizardPage2_SelectTypes);
		
		
		navegador = new Browser(sash_form,SWT.BORDER);
		final FormData fd_navegador = new FormData();
		fd_navegador.bottom = new FormAttachment(100, 0);
		fd_navegador.right = new FormAttachment(100, -30);
		fd_navegador.top = new FormAttachment(100, -50);//-100
		fd_navegador.left = new FormAttachment(0, 10);
		navegador.setLayoutData(fd_navegador);
		try{
			navegador.setUrl(new File(RefactoringConstants.REFACTORING_JAVADOC + "/moon/overview-summary.html" ).toURI().toURL().toString());
		}catch(MalformedURLException e){}

		final Group typesGroup = new Group(composite_1, SWT.NONE);
		typesGroup.setText(Messages.RefactoringWizardPage2_Types);
		final FormData fd_typwsGroup = new FormData();
		fd_typwsGroup.bottom = new FormAttachment(100, -5);
		fd_typwsGroup.top = new FormAttachment(0, 7);
		fd_typwsGroup.left = new FormAttachment(0, 5);
		fd_typwsGroup.right = new FormAttachment(100, -5);
		typesGroup.setLayoutData(fd_typwsGroup);
		typesGroup.setLayout(new FormLayout());

		Composite composite_2;
		composite_2 = new Composite(composite, SWT.NONE);
		fd_composite_1.bottom = new FormAttachment(composite_2, 0, SWT.BOTTOM);
		composite_2.setLayout(new FormLayout());
		final FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(100, -5);
		fd_composite_2.right = new FormAttachment(100, -5);
		fd_composite_2.top = new FormAttachment(0, 1);
		fd_composite_2.left = new FormAttachment(0, 300);
		composite_2.setLayoutData(fd_composite_2);

		lInputs = new List(composite_2, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		final FormData fd_list_1 = new FormData();
		fd_list_1.right = new FormAttachment(100, -83);
		fd_list_1.left = new FormAttachment(0, 15);
		fd_list_1.top = new FormAttachment(0, 30);
		lInputs.setLayoutData(fd_list_1);
		lInputs.addSelectionListener(new InputSelectionListener());

		Group parametersGroup;
		parametersGroup = new Group(composite_2, SWT.NONE);
		parametersGroup.setLayout(new FormLayout());
		parametersGroup.setText(Messages.RefactoringWizardPage2_Parameters);
		final FormData fd_parametersGroup = new FormData();
		fd_parametersGroup.bottom = new FormAttachment(100, -5);
		fd_parametersGroup.top = new FormAttachment(lInputs, 5, SWT.BOTTOM);
		fd_parametersGroup.left = new FormAttachment(0, 10);
		parametersGroup.setLayoutData(fd_parametersGroup);

		Group inputsGroup;
		inputsGroup = new Group(composite_2, SWT.NONE);
		fd_parametersGroup.right = new FormAttachment(inputsGroup, 0, SWT.RIGHT);
		

		Label nameLabel = new Label(parametersGroup, SWT.NONE);
		final FormData fd_nameLabel = new FormData();
		fd_nameLabel.bottom = new FormAttachment(0, 30);
		fd_nameLabel.top = new FormAttachment(0, 10);
		nameLabel.setLayoutData(fd_nameLabel);
		nameLabel.setText(Messages.RefactoringWizardPage2_Name);

		tName = new Text(parametersGroup, SWT.BORDER);
		fd_nameLabel.left = new FormAttachment(tName, 0, SWT.LEFT);
		final FormData fd_tName = new FormData();
		fd_tName.top = new FormAttachment(0, 30);
		tName.setLayoutData(fd_tName);
		tName.addFocusListener(new NameFocusListener());
		tName.setToolTipText(Messages.RefactoringWizardPage2_FillInName);

		Label fromLabel;
		fromLabel = new Label(parametersGroup, SWT.NONE);
		fd_nameLabel.right = new FormAttachment(fromLabel, 0, SWT.RIGHT);
		fd_tName.left = new FormAttachment(fromLabel, 0, SWT.LEFT);
		fd_tName.bottom = new FormAttachment(fromLabel, -5, SWT.TOP);
		final FormData fd_fromLabel = new FormData();
		fd_fromLabel.right = new FormAttachment(0, 145);
		fromLabel.setLayoutData(fd_fromLabel);
		fromLabel.setText(Messages.RefactoringWizardPage2_From);

		cFrom = new Combo(parametersGroup, SWT.NONE);
		fd_fromLabel.top = new FormAttachment(cFrom, -18, SWT.TOP);
		fd_fromLabel.bottom = new FormAttachment(cFrom, -5, SWT.TOP);
		fd_fromLabel.left = new FormAttachment(cFrom, 0, SWT.LEFT);
		final FormData fd_cFrom = new FormData();
		cFrom.setLayoutData(fd_cFrom);
		cFrom.addFocusListener(new FromFocusListener());
		cFrom.setToolTipText(
			Messages.RefactoringWizardPage2_SelectFromInput);

		Label methodLabel = new Label(parametersGroup, SWT.NONE);
		fd_cFrom.top = new FormAttachment(methodLabel, -26, SWT.TOP);
		fd_cFrom.bottom = new FormAttachment(methodLabel, -5, SWT.TOP);
		fd_cFrom.right = new FormAttachment(methodLabel, 207, SWT.LEFT);
		fd_cFrom.left = new FormAttachment(methodLabel, 0, SWT.LEFT);
		final FormData fd_methodLabel = new FormData();
		fd_methodLabel.right = new FormAttachment(0, 150);
		fd_methodLabel.bottom = new FormAttachment(0, 113);
		fd_methodLabel.top = new FormAttachment(0, 100);
		methodLabel.setLayoutData(fd_methodLabel);
		methodLabel.setText(Messages.RefactoringWizardPage2_Method);

		cMethod = new Combo(parametersGroup, SWT.NONE);
		fd_methodLabel.left = new FormAttachment(cMethod, 0, SWT.LEFT);
		final FormData fd_cMethod = new FormData();
		fd_cMethod.bottom = new FormAttachment(0, 136);
		fd_cMethod.top = new FormAttachment(0, 115);
		fd_cMethod.right = new FormAttachment(methodLabel, 207, SWT.LEFT);
		fd_cMethod.left = new FormAttachment(methodLabel, 0, SWT.LEFT);
		cMethod.setLayoutData(fd_cMethod);
		cMethod.addFocusListener(new MethodFocusListener());
		cMethod.setToolTipText(
			Messages.RefactoringWizardPage2_SelectMethod);

		ch_Root = new Button(parametersGroup, SWT.CHECK);
		fd_tName.right = new FormAttachment(ch_Root, -5, SWT.LEFT);
		final FormData fd_ch_Root = new FormData();
		fd_ch_Root.left = new FormAttachment(cFrom, -44, SWT.RIGHT);
		fd_ch_Root.right = new FormAttachment(cFrom, 0, SWT.RIGHT);
		fd_ch_Root.bottom = new FormAttachment(tName, 21, SWT.TOP);
		fd_ch_Root.top = new FormAttachment(tName, 0, SWT.TOP);
		ch_Root.setLayoutData(fd_ch_Root);
		ch_Root.setText(Messages.RefactoringWizardPage2_Main);
		ch_Root.addSelectionListener(new RootSelectionListener());
		ch_Root.setToolTipText(
			Messages.RefactoringWizardPage2_SelectMainBox);
		
		inputsGroup.setText(Messages.RefactoringWizardPage2_Inputs);
		final FormData fd_inputsGroup = new FormData();
		fd_inputsGroup.bottom = new FormAttachment(parametersGroup, 0, SWT.TOP);
		fd_inputsGroup.right = new FormAttachment(100, -75);
		fd_inputsGroup.left = new FormAttachment(0, 10);
		fd_inputsGroup.top = new FormAttachment(0, 10);
		inputsGroup.setLayoutData(fd_inputsGroup);
		inputsGroup.setLayout(new FormLayout());

		upButton = new Button(composite_2, SWT.NONE);
		final FormData fd_button_2 = new FormData();
		fd_button_2.left = new FormAttachment(0, 250);
		fd_button_2.top = new FormAttachment(0, 35);
		fd_button_2.bottom = new FormAttachment(0, 58);
		upButton.setLayoutData(fd_button_2);
		upButton.setImage(ResourceManager.getPluginImage(
			RefactoringPlugin.getDefault(),
			"icons" + System.getProperty("file.separator") + "arrow_up.gif")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		upButton.addSelectionListener(new ListUpListener(lInputs));

		downButton = new Button(composite_2, SWT.NONE);
		fd_list_1.bottom = new FormAttachment(downButton, 0, SWT.BOTTOM);
		fd_button_2.right = new FormAttachment(downButton, 0, SWT.RIGHT);
		final FormData fd_vButton = new FormData();
		fd_vButton.left = new FormAttachment(0, 250);
		fd_vButton.bottom = new FormAttachment(0, 100);
		fd_vButton.top = new FormAttachment(0, 77);
		fd_vButton.right = new FormAttachment(parametersGroup, 50, SWT.RIGHT);
		downButton.setLayoutData(fd_vButton);
		downButton.setImage(ResourceManager.getPluginImage(
			RefactoringPlugin.getDefault(),
			"icons" + System.getProperty("file.separator") + "arrow_down.gif")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		downButton.addSelectionListener(new ListDownListener(lInputs));
		
		delButton = new Button(composite, SWT.NONE);
		final FormData fd_button = new FormData();
		fd_button.right = new FormAttachment(0, 290);
		fd_button.left = new FormAttachment(0, 250);
		fd_button.bottom = new FormAttachment(0, 102);//138
		fd_button.top = new FormAttachment(0, 79);//115
		delButton.setLayoutData(fd_button);
		delButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
			ISharedImages.IMG_TOOL_DELETE));
		
		addButton = new Button(composite, SWT.NONE);
		final FormData fd_button_1 = new FormData();
		fd_button_1.bottom = new FormAttachment(0, 61);
		fd_button_1.top = new FormAttachment(0, 40 );
		fd_button_1.right = new FormAttachment(composite_1, 45, SWT.RIGHT);
		fd_button_1.left = new FormAttachment(composite_1, 5, SWT.RIGHT);
		addButton.setLayoutData(fd_button_1);
		addButton.setImage(ResourceManager.getPluginImage(
			RefactoringPlugin.getDefault(),
			"icons" + System.getProperty("file.separator") + "arrow_right.gif")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addElements();
			}
		});

		delButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeElements();
			}
		});
		
		sash_form.setWeights(new int[] {5 , 2 });

		fillTypesList();
		if (refactoring != null)
			fillInRefactoringData();
		enableFields(false);
		enableInputButtons(false);
		addButton.setEnabled(false);
	}
	
	/**
	 * Obtiene la lista de entradas seleccionadas para la refactorización.
	 * 
	 * <p>El formato devuelve es el de una lista de <i>arrays</i> de cadenas en la
	 * que cada <i>array</i> se corresponde con una entrada y se compone de 5 
	 * elementos ordenados:
	 * <li>
	 * <ol>El nombre completamente cualificado del tipo de la entrada.</ol>
	 * <ol>El nombre identificativo dado a la entrada.</ol>
	 * <ol>El nombre identificativo de la entrada a partir de la cual se obtiene
	 * el valor o posibles valores para la entrada actual (si hay alguna).</ol>
	 * <ol>El nombre simple del métod mediante el cual se obtendrían dichos valores
	 * a partir de la entrada apuntada por el valor del atributo "from".</ol>
	 * <ol>El valor que indica si la entrada es la entrada principal de la 
	 * refactorización (en cuyo caso vale <code>"true"</code>) o no (si tiene
	 * cualquier otro valor, como <code>"false"</code>, una cadena vacía o 
	 * incluso <code>null</code>).</ol>
	 * </p>
	 * 
	 * @return la lista de entradas seleccionadas para la refactorización.
	 */
	public ArrayList<String[]> getInputs(){
		ArrayList<String[]> inputs = new ArrayList<String[]>();
		for (InputParameter nextInput : inputsTable.values())
			inputs.add(new String[]{nextInput.getType(), nextInput.getName(), 
				nextInput.getFrom(), nextInput.getMethod(), nextInput.getRoot()});
		return inputs;
	}
	
	/**
	 * Obtiene la tabla temporal de parámetros.
	 * 
	 * @return la tabla temporal de parámetros.
	 */
	public InputParameter[] getInputTable(){
		return inputsTable.values().toArray(new InputParameter[inputsTable.size()]);
	}
	
	/**
	 * Puebla los campos del formulario del asistente con la información que se
	 * pueda obtener de la refactorización existente que se está editando.
	 */
	private void fillInRefactoringData(){
		if (refactoring.getInputs() != null)
			for (String[] nextInput : refactoring.getInputs()){
				InputParameter input = new InputParameter(nextInput[0], 
					nextInput[1], nextInput[2], nextInput[3], nextInput[4]);

				// Se añade a la tabla de parámetros.
				// Si el tipo no está en la lista de tipos disponibles, se añade.
				if (listModelTypes.get(input.getType()) == null){
					listModelTypes.put(input.getType(), 1);
					lTypes.add(input.getType());
				}
				if (inputsTable == null)
					inputsTable = new Hashtable<String, InputParameter>();
								
				Integer number = listModelTypes.get(input.getType()); 
				inputsTable.put(input.getType() + " (" + number + ")", input); //$NON-NLS-1$
				listModelTypes.put(input.getType(), number + 1);
				
				// Se añade a la lista de parámetros elegidos.
				lInputs.add(input.getType() + " (" + number + ")");								 //$NON-NLS-1$
			}
		lInputs.deselectAll();
		this.enableFields(false);
		
		checkForCompletion();
		
		// Se comprueba que ninguna entrada de las cargadas apunte en el campo
		// "from" a un nombre que no pertenece a ningún parámetro.
		for (InputParameter nextInput : inputsTable.values())
			// Si tiene un campo a partir del cual se obtiene.
			if (nextInput.getFrom() != null && nextInput.getFrom() != ""){ //$NON-NLS-1$
				boolean found = false;
				for (InputParameter subInput : inputsTable.values())
					if (nextInput.getFrom().equals(subInput.getName())){
						found = true;
						break;
					}
					
				if (! found) {
					Object[] messageArgs = {nextInput.getName(), nextInput.getFrom()};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.RefactoringWizardPage2_InputFromInput);
					
					updateStatus(
						Messages.RefactoringWizardPage2_InputsReferToNone +
						": " + formatter.format(messageArgs)); //$NON-NLS-1$
				}
			}
	}

	/**
	 * Añade tantas entradas a la lista de entradas elegidas como tipos se encuentren
	 * seleccionados en la lista de tipos disponibles en el modelo.
	 * 
	 * <p>Para cada entrada añadida, le asocia un número, que será el número de 
	 * entrada con el mismo tipo, al tiempo que actualiza las tablas de referencia
	 * de entradas seleccionadas y de tipos de datos disponibles.</p>
	 */
	private void addElements(){
		String[] selected = lTypes.getSelection();
		for(int i=0; i < selected.length; i++){
			Integer number = listModelTypes.get(selected[i]);
			lInputs.add(selected[i] + " (" + number + ")"); //$NON-NLS-1$
			
			if (inputsTable == null)
				inputsTable = new Hashtable<String, InputParameter>();
			inputsTable.put(selected[i] + " (" + number + ")",  //$NON-NLS-1$
				new InputParameter(selected[i]));
			listModelTypes.put(selected[i], number + 1);
		}
		checkForCompletion();
		lInputs.deselectAll();
		enableFields(false);
		enableInputButtons(false);
	}

	/**
	 * Elimina de la lista de entradas elegidas aquéllas que se encuentren 
	 * seleccionadas y actualiza las tablas de referencia de entradas y tipos.
	 */
	private void removeElements(){
		String[] selected = lInputs.getSelection();
		for(int i=0; i < selected.length; i++){
			if (! selected[i].startsWith(RefactoringConstants.MODEL_PATH + " ")){ //$NON-NLS-1$
				lInputs.remove(selected[i]);
				inputsTable.remove(selected[i]);
			}
			else
				MessageDialog.openWarning(getShell(), Messages.RefactoringWizardPage2_Warning, 
					Messages.RefactoringWizardPage2_ModelRequired +
					"."); //$NON-NLS-1$
		}
		
		checkForCompletion();
		lInputs.deselectAll();
		enableFields(false);
		enableInputButtons(false);
	}

	
	/**
	 * Puebla la lista de tipos disponibles para los parámetros de entrada con 
	 * los tipos disponibles en el modelo MOON.
	 * 
	 * <p>Solo se tienen en cuenta los tipos de representación de los paquetes
	 * <code>moon.core.classdef</code>, <code>moon.core.genericity</code> y
	 * algunos otros.</p>
	 * 
	 * @param patron Expresion regular de búsqueda.
	 */
	private void fillSearchTypesList(String patron){
		String[] itemList;
		
		try{
			MOONTypeLister l = MOONTypeLister.getInstance();

			itemList = l.getTypeNameList();
			// Se ordena la lista de candidatos.
			Arrays.sort(itemList);
			
			//se vacia la lista lTypes
			lTypes.removeAll();
			
			for(int i=0; i < itemList.length; i++){
				String typeName = itemList[i].replaceAll("/" , "."); //$NON-NLS-1$ //$NON-NLS-2$
				
				//En caso de que el tipo coincida con el patrón de búsqueda lo añadimos a la lista.
				if(patron=="" || typeName.matches(patron))
					lTypes.add(typeName);
			}
		}
		catch(IOException ioe){
			logger.error(Messages.RefactoringWizardPage2_ListNotLoaded +
				".\n" + ioe.getMessage()); //$NON-NLS-1$
			MessageDialog.openError(getShell(),
				Messages.RefactoringWizardPage2_Error, Messages.RefactoringWizardPage2_ListNotLoaded +
				".\n" + ioe.getMessage()); //$NON-NLS-1$
		}
	}
	
	
	/**
	 * Puebla la lista de tipos disponibles para los parámetros de entrada con 
	 * los tipos disponibles en el modelo MOON.
	 * 
	 * <p>Solo se tienen en cuenta los tipos de representación de los paquetes
	 * <code>moon.core.classdef</code>, <code>moon.core.genericity</code> y
	 * algunos otros.</p>
	 */
	private void fillTypesList(){
		listModelTypes = new Hashtable<String, Integer>();
		
		String[] itemList;
		
		try{
			MOONTypeLister l = MOONTypeLister.getInstance();

			itemList = l.getTypeNameList();
			// Se ordena la lista de candidatos.
			Arrays.sort(itemList);

			// Se obtiene la lista de candidatos.
			listModelTypes.put(RefactoringConstants.STRING_PATH, 1);
			for(int i=0; i < itemList.length; i++){
				String typeName = itemList[i].replaceAll("/" , "."); //$NON-NLS-1$ //$NON-NLS-2$
				listModelTypes.put(typeName, 1);
				lTypes.add(typeName);
					
				// Si se está creando una nueva refactorización.
				if (((RefactoringWizard)getWizard()).getOperation() == 
					RefactoringWizard.CREATE){
					// Se busca y añade automáticamente el modelo MOON.
					if (itemList[i].replaceAll("/", ".").equals( //$NON-NLS-1$ //$NON-NLS-2$
						RefactoringConstants.MODEL_PATH)){
						lInputs.add(typeName + " (" + 1 + ")"); //$NON-NLS-1$
						if (inputsTable == null)
							inputsTable = new Hashtable<String, InputParameter>();
						inputsTable.put(typeName + " (" + 1 + ")",  //$NON-NLS-1$
							new InputParameter(typeName, Messages.RefactoringWizardPage2_ModelName, "", "", "false"));  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						listModelTypes.put(typeName, 2);
							
						checkForCompletion();
						lInputs.deselectAll();
						enableFields(false);
						enableInputButtons(false);
					}					
				}		
			}
		}
		catch(IOException ioe){
			logger.error(Messages.RefactoringWizardPage2_ListNotLoaded +
				".\n" + ioe.getMessage()); //$NON-NLS-1$
			MessageDialog.openError(getShell(),
				Messages.RefactoringWizardPage2_Error, Messages.RefactoringWizardPage2_ListNotLoaded +
				".\n" + ioe.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Activa o desactiva todos los campos del formulario para la introducción 
	 * de datos de un parámetro seleccionado.
	 * 
	 * @param enable <code>true</code> si se deben habilitar todos los campos;
	 * <code>false</code> si se deben deshabilitar.
	 */
	public void enableFields(boolean enable){
		tName.setEnabled(enable);
		cFrom.setEnabled(enable);
		cMethod.setEnabled(enable);
		ch_Root.setEnabled(enable);
	}
	
	/**
	 * Activa o desactiva todos los botones que solo deben permanecer operativos
	 * cuando una entrada se encuentre seleccionada.
	 * 
	 * @param enable <code>true</code> si se desean activar los botones;
	 * <code>false</code> si se desean desactivar.
	 */
	public void enableInputButtons(boolean enable){
		delButton.setEnabled(enable);
		upButton.setEnabled(enable);
		downButton.setEnabled(enable);
	}

	/**
	 * Completa los campos referentes al parámetro seleccionado en un cierto
	 * instante con los datos de la entrada representada por #parameter.
	 * 
	 * @param parameter la entrada cuyos datos se deben mostrar.
	 */
	private void fillInputData(InputParameter parameter) {
		if(parameter.getName() == null)
			tName.setText(new String("")); //$NON-NLS-1$
		else
			tName.setText(parameter.getName());
		
		if(parameter.getRoot() != null && parameter.getRoot().equals("true")) //$NON-NLS-1$
			ch_Root.setSelection(true);
		else
			ch_Root.setSelection(false);

		fillFromComboBox();		
		cFrom.select(cFrom.indexOf(parameter.getFrom()));
		
		fillMethodComboBox(parameter.getFrom(), parameter.getType());
		cMethod.select(cMethod.indexOf(parameter.getMethod()));
	}
	
	/**
	 * Rellena la lista desplegable de posibles objetos de origen para un valor
	 * con los identificadores de los parámetros ya disponibles. 
	 */
	private void fillFromComboBox() {
		for(InputParameter nextParameter : inputsTable.values())
			// Solo se toman las entradas con nombre.
			if (nextParameter.getName() != null && 
				nextParameter.getName().compareTo("") != 0) //$NON-NLS-1$
				cFrom.add(nextParameter.getName());

		cFrom.add(new String(""), 0); //$NON-NLS-1$
	}
	
	/**
	 * Rellena el desplegable con los nombres de los métodos disponibles para la 
	 * obtención del parámetro de entrada seleccionado a partir de la entrada
	 * especificada en la sección <i>from</i>.
	 * 
	 * @param fromName nombre de la entrada a partir de la cual se obtendría 
	 * el valor de esta entrada mediante la aplicación del método seleccionado.
	 * @param returnType tipo del resultado que deben devolver los métodos en caso de que
	 * devuelvan un valor único en lugar de un conjunto.
	 */
	private void fillMethodComboBox(String fromName, String returnType) {
		
		String[] methods = new String[0];
		
		// Nombre completamente cualificado del tipo de la entrada a partir de
		// la que se debería obtener el valor del parámetro tratado.
		String type = new String();
		
		// Si no se especifica otra entrada como origen, se intentará obtener
		// la nueva entrada directamente desde el modelo MOON.
		if (fromName == null || fromName.length() == 0)
			type = RefactoringConstants.MODEL_PATH;

		else
			for (InputParameter nextInput : inputsTable.values())
				// Se busca la clase asociada a dicho nombre.
				if(nextInput.getName().compareTo(fromName) == 0){
					type = nextInput.getType();
					break;
				}
		
		if(type.length() > 0)
			methods = getMethodsFromType(type, returnType);

		// Se rellena el desplegable con todos los métodos encontrados.
		for (int i = 0; i < methods.length; i++)
			cMethod.add(methods[i]);
	}
	
	/**
	 * Obtiene los nombres de los métodos de una clase que devuelven objetos
	 * de tipo <code>Iterator</code> ,<code>Collection</code> o <code>List</code>,
	 * así como aquéllos que devuelven un único valor del tipo especificado.
	 * 
	 * @param className nombre de la clase cuyos métodos se deben obtener.
	 * @param type tipo del valor único que pueden devolver los métodos.
	 * 
	 * @return un <i>array</i> de cadenas con los nombres de los métodos.
	 */
	private String[] getMethodsFromType(String className, String type) {
		
		ArrayList<String> temp = new ArrayList<String>();
		
		try {
			Class<?> from = Class.forName(className);
			
			int i = 0;
			
			Method[] methods = from.getMethods();
			for(i = 0; i < methods.length; i++)
				if (InputProcessor.isMethodValid(methods[i], type))
					temp.add((from.getMethods()[i]).getName());
			
			temp.add(0, new String("")); //$NON-NLS-1$
		}
		catch(Exception e) {}
		
		String[] array = new String[temp.size()];
		return temp.toArray(array);
	}
	
	/**
	 * Actualiza el estado de la pantalla de diálogo del asistente.
	 * 
	 * @param message mensaje asociado al estado actual de la pantalla.
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	/**
	 * Comprueba si todos los campos necesarios se han completado y si se puede 
	 * continuar con el siguiente paso del asistente.
	 */
	private void checkForCompletion() {
		int rootInputs = 0;
		
		// Se comprueban todos los parámetros de entrada.
		for (InputParameter nextInput : inputsTable.values()){
			// Todos los parámetros deben tener nombre.
			if (! nextInput.getType().equals(
				RefactoringConstants.MODEL_PATH) &&
				nextInput.getName().equals("")){ //$NON-NLS-1$
				
				Object[] messageArgs = {nextInput.getType()};
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(
					Messages.RefactoringWizardPage2_NameNeeded);
				
				updateStatus(formatter.format(messageArgs) + "."); //$NON-NLS-1$
				return;
			}
			
			if (nextInput.getRoot() != null && nextInput.getRoot().equals("true")){ //$NON-NLS-1$
				rootInputs++;
				// El parámetro principal no puede obtenerse a partir de otro.
				if ((nextInput.getFrom() != null && nextInput.getFrom().length() != 0) ||
					(nextInput.getMethod() != null && nextInput.getMethod().length() != 0)){
					updateStatus(Messages.RefactoringWizardPage2_MainCannotBeObtained +
						"."); //$NON-NLS-1$
					return;
				}
				// El parámetro principal no puede ser de cualquier tipo.
				MainInputValidator validator = new MainInputValidator();
				if (! validator.checkMainType(nextInput.getType())){
					
					Object[] messageArgs = {
						MainInputValidator.ATTDEC,
						MainInputValidator.CLASSDEF,
						MainInputValidator.FORMALARG,
						MainInputValidator.FORMALPAR,
						MainInputValidator.METHDEC
					};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(
						Messages.RefactoringWizardPage2_MainMustConform);
					
					updateStatus(formatter.format(messageArgs));
					return;
				}					
			}
			if (nextInput.getFrom() != null && nextInput.getFrom().length() != 0)
				if (nextInput.getMethod() == null || nextInput.getMethod().length() == 0){
					Object[] messageArgs = {"(" + nextInput.getType()  //$NON-NLS-1$
						+ ")"}; //$NON-NLS-1$
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(
						Messages.RefactoringWizardPage2_MethodNeeded);
					
					updateStatus(formatter.format(messageArgs) + "."); //$NON-NLS-1$
					return;
				}
			
			
		}
		
		// Tiene que haber una entrada principal.
		if (rootInputs < 1){
			updateStatus(Messages.RefactoringWizardPage2_MainNeeded +
				"."); //$NON-NLS-1$
			return;
		}
		// No puede haber más de una entrada principal.
		if (rootInputs > 1){
			updateStatus(Messages.RefactoringWizardPage2_OnlyOneMain +
				"."); //$NON-NLS-1$
			return;
		}
		
		updateStatus(null);
	}

	/**
	 * Actualiza el panel inferior para mostrar en cada uno de los campos del
	 * formulario la información apropiada acerca del parámetro seleccionado en
	 * la lista del panel de parámetros.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class InputSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificación de que un elemento de la lista de parámetros
		 * de entrada ha sido seleccionado.
		 * 
		 * <p>Inicia las acciones que sean necesarias para actualizar la 
		 * información mostrada en el panel inferior acerca del parámetro
		 * seleccionado.</p>
		 * 
		 * @param e el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			cFrom.removeAll();
			cMethod.removeAll();
			
			// Si hay elementos seleccionados entre los elegidos.
			if (lInputs.getSelectionCount() > 0 &&
				lInputs.getItem(lInputs.getSelectionIndex()) != null){
				enableInputButtons(true);
				enableFields(false);
				
				if (lInputs.getSelectionCount() == 1){
				
					InputParameter input = 
						inputsTable.get(lInputs.getSelection()[0]);
				
					if (input != null && input instanceof InputParameter){
						enableFields(true);
						fillInputData(input);
					}
					if (input.getType().equals(
						RefactoringConstants.MODEL_PATH)){
						cFrom.setEnabled(false);
						cMethod.setEnabled(false);
					}
				}
			}
			// Si no hay ningún elemento seleccionado.
			else {
				enableFields(false);
				enableInputButtons(false);
			}
		}
		
		/**
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
	/**
	 * Actualiza el valor del atributo de la entrada seleccionada que indica si
	 * se trata de la entrada principal de la refactorización o no.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RootSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificación de que la marca de selección ha sido
		 * seleccionada o deseleccionada.
		 * 
		 * @param e el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			// Si hay elementos seleccionados entre los elegidos
			if (lInputs.getSelectionCount() > 0 &&
				lInputs.getItem(lInputs.getSelectionIndex()) != null){
				InputParameter input = 
					inputsTable.get(lInputs.getSelection()[0]);
				
				if (input != null && input instanceof InputParameter){
					Boolean isTrue = ch_Root.getSelection();
					input.setRoot(isTrue.toString());
				}
				checkForCompletion();
			}
		}
		
		/**
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
	/**
	 * Recibe notificaciones cuando uno de los elementos de la lista de tipos
	 * es seleccionado.
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class TypeSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificación de que un elemento de la lista de tipos
		 * disponibles ha sido seleccionado.
		 * 
		 * <p>Activa el botón que permite añadir el o los tipos seleccionados
		 * a la lista de entradas.</p>
		 * 
		 * @param e el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			String path = lTypes.getItem(lTypes.getSelectionIndex()).toString();
			path = path.replace('.', '/');
			if(path.startsWith("moon")){
				path = RefactoringConstants.REFACTORING_JAVADOC + "/moon/" + path + ".html";
			}else{
				if(path.startsWith("javamoon"))				
					path = RefactoringConstants.REFACTORING_JAVADOC + "/javamoon/" + path + ".html";
			}

			try{
				if(new File(path).exists())
					navegador.setUrl(new File(path).toURI().toURL().toString()+ "#skip-navbar_top");
				else
					navegador.setUrl(new File(RefactoringConstants.REFACTORING_JAVADOC + "/moon/notFound.html" ).toURI().toURL().toString());
			}catch(MalformedURLException excp){
				excp.printStackTrace();
			}
			//tInformation.setText(getJavadocInformation(path));
			if (lTypes.getSelectionCount() > 0 &&
				lTypes.getItem(lTypes.getSelectionIndex()) != null)
				addButton.setEnabled(true);
			else 
				addButton.setEnabled(false);
		}
		
		/**
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}

	/**
	 * Permite observar y controlar los cambios realizados sobre el contenido 
	 * del campo que contiene el nombre del parámetro de entrada seleccionado.
	 * 
	 * <p>Cuando se modifica el contenido de dicho campo, se actualiza el nombre
	 * del parámetro, a menos que se trate de un nombre ya asignado a otra
	 * entrada.</p>
	 * 
	 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class NameFocusListener implements FocusListener {
	
		/**
		 * Recibe una notificación indicando que el texto observado ha recibido
		 * el foco (en este caso, el texto que contiene el nombre del
		 * parámetro seleccionado).
		 *
		 * @param e evento con la información referente a la recepción del foco.
		 * 
		 * @see FocusListener#focusGained(FocusEvent)
		 */
		@Override
		public void focusGained(FocusEvent e){}
		
		/**
		 * Recibe una notificación indicando que el texto observado ha perdido
		 * el foco.
		 * 
		 * @param e evento con la información referente a la pérdida del foco.
		 * 
		 * @see FocusListener#focusLost(FocusEvent)
		 */
		@Override
		public void focusLost(FocusEvent e){

			// Si hay elementos seleccionados en la lista de tipos elegidos.
			String[] selection = lInputs.getSelection();
			if (selection != null && selection.length > 0){
				InputParameter current = inputsTable.get(
					lInputs.getSelection()[0]);

				if (! tName.getText().equals(current.getName())){
					if (checkName(tName.getText(), current))
						current.setName(tName.getText());
					else {
						MessageDialog.openWarning(getShell(),
							Messages.RefactoringWizardPage2_Warning, 
							Messages.RefactoringWizardPage2_NameRepeated
							+ ".\n"); //$NON-NLS-1$
						tName.setText(current.getName());
					}
				}					
			}
			
			checkForCompletion();
		}
		
		/**
		 * Comprueba que un nombre dado no pertenezca a algún otro elemento
		 * distinto del indicado.
		 * 
		 * @param name el nombre cuya unicidad se debe verificar.
		 * @param current el único parámetro de entrada para el que se admite el
		 * nombre como válido.
		 * 
		 * @return <code>false</code> si el nombre ya existe; <code>true
		 * </code> en caso contrario.
		 */
		private boolean checkName(String name, InputParameter current) {
			for (InputParameter nextInput : inputsTable.values())
				// Se busca una entrada con el nombre.
				if(nextInput.getName().equals(name) && nextInput != current)
					return false;
			return true;
		}
	}

	/**
	 * Permite observar y controlar los cambios realizados sobre el contenido 
	 * del campo desplegable que contiene la entrada a partir de la que se 
	 * puede obtener a su vez la entrada actual.
	 * 
	 * <p>Cuando se modifica el contenido de dicho campo, se actualiza el apuntador
	 * del parámetro al objeto a partir del que se puede obtener su valor.</p>
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class FromFocusListener implements FocusListener {
	
		/**
		 * Recibe una notificación indicando que el elemento observado ha recibido
		 * el foco (en este caso, el desplegable que contiene los nombres de las
		 * entradas).
		 *
		 * @param e evento con la información referente a la recepción del foco.
		 * 
		 * @see FocusListener#focusGained(FocusEvent)
		 */
		@Override
		public void focusGained(FocusEvent e){}
		
		/**
		 * Recibe una notificación indicando que el elemento observado ha perdido
		 * el foco.
		 * 
		 * @param e evento con la información referente a la pérdida del foco.
		 * 
		 * @see FocusListener#focusLost(FocusEvent)
		 */
		@Override
		public void focusLost(FocusEvent e){
			cMethod.removeAll();
			
			// Si hay elementos seleccionados en la lista de tipos elegidos.
			String[] selection = lInputs.getSelection();
			if (selection != null && selection.length > 0){
				InputParameter current = inputsTable.get(lInputs.getSelection()[0]);

				if (cFrom.getSelectionIndex() != -1){
					current.setFrom(cFrom.getItem(cFrom.getSelectionIndex()));
					fillMethodComboBox(
						cFrom.getItem(cFrom.getSelectionIndex()),
						current.getType());
					cMethod.select(cMethod.indexOf(current.getMethod()));
				}
				checkForCompletion();
			}
		}
	}
	
	/**
	 * Permite observar y controlar los cambios realizados sobre el contenido 
	 * del campo desplegable que contiene el método mediante el que se 
	 * puede obtener a su vez el valor de la entrada actual.
	 * 
	 * <p>Cuando se modifica el contenido de dicho campo, se actualiza el apuntador
	 * del parámetro al método mediante el que se puede obtener su valor.</p>
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class MethodFocusListener implements FocusListener {
	
		/**
		 * Recibe una notificación indicando que el elemento observado ha recibido
		 * el foco (en este caso, el desplegable que contiene los métodos de la
		 * entrada seleccionada como origen).
		 *
		 * @param e evento con la información referente a la recepción del foco.
		 * 
		 * @see FocusListener#focusGained(FocusEvent)
		 */
		@Override
		public void focusGained(FocusEvent e){}
		
		/**
		 * Recibe una notificación indicando que el elemento observado ha perdido
		 * el foco.
		 * 
		 * @param e evento con la información referente a la pérdida del foco.
		 * 
		 * @see FocusListener#focusLost(FocusEvent)
		 */
		@Override
		public void focusLost(FocusEvent e){
			// Si hay elementos seleccionados entre los elegidos.
			String[] selection = lInputs.getSelection();
			if (selection != null && selection.length > 0){
				InputParameter current = inputsTable.get(lInputs.getSelection()[0]);
			
				if(cMethod.getSelectionIndex() != -1)
					current.setMethod(cMethod.getItem(cMethod.getSelectionIndex()));
				checkForCompletion();
			}
		}
	}
}