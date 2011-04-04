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

package dynamicrefactoring.interfaz.dynamic;

import java.io.File;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javamoon.core.JavaModel;
import moon.core.NamedObject;
import moon.core.ObjectMoon;
import moon.core.instruction.CodeFragment;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import repository.moon.MOONRefactoring;
import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoring;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.InputParameter;
import dynamicrefactoring.domain.RefactoringExample;
import dynamicrefactoring.domain.RefactoringException;
import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.integration.NamedObjectHandler;
import dynamicrefactoring.interfaz.ComboEditor;

/**
 * Ventana generada din�micamente para la configuraci�n de una refactorizaci�n
 * din�mica antes de su ejecuci�n.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringWindow extends Dialog {

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(DynamicRefactoringWindow.class);

	/**
	 * Campo de texto utilizado para la introducci�n manual de valores.
	 */
	private Text t_Input;

	/**
	 * Campo desplegable utilizado para la selecci�n de valores entre un
	 * conjunto.
	 */
	private Combo combo;

	/**
	 * Campo de texto asociado a la entrada principal de la refactorizaci�n.
	 */
	private Text t_Root;
	
	/**
	 * Tabla asociativa en la que se almacenan los valores ya cargados hasta el
	 * momento para las entradas.
	 */
	protected Hashtable<String, Object> inputValues;

	/**
	 * Tabla asociativa en la que se almacenan los posibles valores asociados a
	 * una lista desplegable, en el mismo orden en que �sta los muestra.
	 * 
	 * <p>
	 * Se utiliza como clave el c�digo <i>hash</i> del desplegable, y como valor
	 * la lista de objetos cuya representaci�n muestra.
	 * </p>
	 */
	private final HashMap<Integer, List<Object>> comboValues;
	
	/**
	 * Tabla asociativa que permite obtener el campo (campo de texto o desplegable)
	 * en que se muestra el valor de una determinada entrada.
	 * 
	 * <p>Se utiliza como clave el nombre de la entrada, y como valor, una 
	 * referencia al campo en que se muestra su valor.</p>
	 */
	private final HashMap<String, Scrollable> inputFields;
	
	/**
	 * Tabla asociativa en la que se almacenan los atributos de cada una de las 
	 * entradas, utilizando como clave de cada entrada su nombre.
	 */
	private final HashMap<String, InputParameter> inputAttributes;

	/**
	 * Tabla asociativa que permite actualizar los valores de las entradas que
	 * dependen de la selecci�n de un determinado desplegable. Se utiliza como
	 * clave un objeto de envoltura que contiene el <code>hashcode</code> del
	 * combo en cuesti�n, y como valor un <i>array</i> con los nombres de las
	 * entradas que dependen de dicho desplegable.
	 */
	private final HashMap<Integer, String[]> comboDependencies;

	/**
	 * Lista de atributos que se han ido cargando a trav�s de una serie de
	 * llamadas recursivas consecutivas.
	 */
	private ArrayList<String> recursiveInputs;

	/**
	 * La definici�n de la refactorizaci�n.
	 */
	protected DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * Entrada principal de la refactorizaci�n.
	 */
	protected ObjectMoon currentObject;

	/**
	 * Modelo MOON-Java sobre el que se refactoriza.
	 */
	protected JavaModel model;

	/**
	 * Variable auxiliar para la situaci�n din�mica de los campos.
	 */
	private int count = 0;

	/**
	 * Desplazamiento que diferencia la situaci�n de los elementos de la
	 * interfaz entre una entrada principal que hereda de NamedObject y otra que
	 * hereda de CodeFragment.
	 */
	private int desplazamiento=0;

	/**
	 * Crea la ventana de di�logo.
	 * 
	 * @param currentObject
	 *            objeto que constituye la entrada principal de la
	 *            refactorizaci�n.
	 * @param refactoringName
	 *            nombre de la refactorizaci�n.
	 * @param refactoringfilePath
	 *            ruta del fichero XML que define la refactorizaci�n.
	 * 
	 * @throws RefactoringException
	 *             si se produce un error al acceder a la definici�n de la
	 *             refactorizaci�n din�mica que se asociar� a la ventana.
	 */
	public DynamicRefactoringWindow(ObjectMoon currentObject, DynamicRefactoringDefinition refactoring) {

		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		
		this.refactoringDefinition = refactoring;
		this.currentObject = currentObject;
		
		inputValues = new Hashtable<String, Object>();
		inputFields = new HashMap<String, Scrollable>();
		comboValues = new HashMap<Integer, List<Object>>();
		comboDependencies = new HashMap<Integer, String[]>();
		inputAttributes = refactoringDefinition.getInputsAsHash();		
		
		model = ModelGenerator.getInstance().getModel();
	}

	/**
	 * Obtiene el campo de texto asociado a una entrada.
	 * 
	 * @param inputName el nombre de la entrada.
	 * 
	 * @return el campo de texto asociado a la entrada, si es que lo tiene.
	 * <code>null</code> si no.
	 */
	public Text getField(String inputName){
		Scrollable field = inputFields.get(inputName);
		if (field != null && field instanceof Text)
			return (Text)field;
		else
			return null;
	}

	/**
	 * Crea el contenido de la ventana de di�logo.
	 * 
	 * @param parent
	 *            el elemento compuesto padre que contendr� el �rea de di�logo.
	 * 
	 * @return el control del �rea de di�logo.
	 * 
	 * @see Dialog#createDialogArea
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		final Composite composite = new Composite(container, SWT.NONE);
		final FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -5);
		fd_composite.right = new FormAttachment(100, -5);
		fd_composite.left = new FormAttachment(0, 5);
		fd_composite.top = new FormAttachment(0, 5);
		composite.setLayoutData(fd_composite);
		final GridLayout gridLayout = new GridLayout();
		composite.setLayout(gridLayout);

		// Se a�aden las pesta�as a la interfaz.
		final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(867, 393));

		final TabItem parametersTabItem = new TabItem(tabFolder, SWT.NONE);
		parametersTabItem.setText(Messages.DynamicRefactoringWindow_Parameters);

		new DynamicRefactoringTab(tabFolder, refactoringDefinition);
		
		if(!refactoringDefinition.getExamples().isEmpty()){
			new DynamicExamplesTab(tabFolder,refactoringDefinition);
		}

		final ScrolledComposite scrolledComposite2 = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		parametersTabItem.setControl(scrolledComposite2);
		scrolledComposite2.setExpandHorizontal(true);
		scrolledComposite2.setExpandVertical(true);

		// Pesta�a de par�metros.
		final Composite cParameters = new Composite(scrolledComposite2, SWT.NONE);
		cParameters.setLocation(0, 0);
		cParameters.setSize(872, 390);
		scrolledComposite2.setContent(cParameters);

		// Se busca la entrada ra�z.
		for(InputParameter nextInput : refactoringDefinition.getInputs()){
			// Para su entrada de tipo "ra�z".
			if (nextInput.isMain()){ //$NON-NLS-1$
				if (! checkMainInput(nextInput)){
					return null;
				}
				final CLabel lb_Root = new CLabel(cParameters, SWT.NONE);
				lb_Root.setAlignment(SWT.CENTER);
				lb_Root.setText(nextInput.getName().replaceAll("_", " ")); //$NON-NLS-1$ //$NON-NLS-2$
				lb_Root.setBounds(342, 38, 183, 19);

				if(currentObject instanceof NamedObject){
					t_Root = new Text(cParameters, SWT.BORDER);
					t_Root.setBounds(35, 65, 790, 25);
					t_Root.setText(((NamedObject)currentObject).getName().toString());
					t_Root.setEditable(false);
				}else{
					desplazamiento=50;
					final StyledText b_Root = new StyledText(cParameters,SWT.BORDER | SWT.V_SCROLL);
					b_Root.setBounds(35, 65, 790, 75);
					b_Root.setText(((CodeFragment)currentObject).getText());
					b_Root.setEditable(false);
				}

				break;
			}
		}
		
		int rootInputs = 0;
		// Para el resto de entradas.		
		for(InputParameter nextInput : refactoringDefinition.getInputs()){

			if (!nextInput.isMain()){
				
				// Para entradas de tipo from.
				if (nextInput.getFrom() != null && nextInput.getFrom().length() > 0){
					
					// Si a�n no se ha obtenido su valor.
					if (! inputValues.containsKey(nextInput.getName())){

						// Se busca la entrada a la que apunta el campo "from".
						InputParameter fromInput = inputAttributes.get(nextInput.getFrom());
					
						// Si se encuentra la entrada apuntada por el "from".
						if (fromInput != null) {

							// Si es la entrada principal o ra�z.
							if(fromInput.isMain())	{					 //$NON-NLS-1$
								if(! loadFromMain(cParameters, nextInput))
									return null;
							}
						
							// Si el "from" apunta al modelo.
							else if (fromInput.getType().equals("moon.core.Model")) { //$NON-NLS-1$
								if (!loadFromModel(cParameters, nextInput))
									return null;
							}
						
							// Para el resto de entradas.
							else {
								recursiveInputs = new ArrayList<String>();
								InputParameter source = inputAttributes.get(nextInput.getFrom());
								// Se inicia su obtenci�n.
								recursiveDependencyResolution(cParameters,
									source, nextInput);							
							}
						}
					
						// Si no se encuentra la entrada a la que apunta el campo "from".
						else {
							Object[] messageArgs = {"\"" + nextInput.getFrom() + "\"", nextInput.getName()}; //$NON-NLS-1$ //$NON-NLS-2$
							MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
							formatter.applyPattern(
								Messages.DynamicRefactoringWindow_ReferencedObjectNotFound);
							
							// La refactorizaci�n contiene un error.
							String message = formatter.format(messageArgs) + ".\n" +  //$NON-NLS-1$
								Messages.DynamicRefactoringWindow_ReviewDefinition + ".\n"; //$NON-NLS-1$
							logger.error(message);
							exitWithError(message);
							return null;
						}
					}
				}
				// Si no es la entrada principal, ni tiene campo "from"
				else {
					// Si no es el propio modelo.
					if(! nextInput.getType().equals(RefactoringConstants.MODEL_PATH)){
						
						final CLabel lb_Input = new CLabel(cParameters, SWT.NONE);
						lb_Input.setBounds(342, desplazamiento + 105 + 67 * count, 183, 19);
						lb_Input.setAlignment(SWT.CENTER);
						lb_Input.setText(nextInput.getName().replaceAll("_", " ")); //$NON-NLS-1$ //$NON-NLS-2$

						// Es una entrada cuyo valor debe introducir el usuario
						// de forma manual (campo de texto).
						t_Input = new Text(cParameters, SWT.BORDER);
						t_Input.setBounds(35, desplazamiento + 132 + 67 * count, 790, 25);
						t_Input.addModifyListener(new TextModifyListener());
						// Se indica que el valor de la entrada se almacena en
						// este campo de texto.
						inputFields.put(nextInput.getName(), t_Input);
						
						count++;
					}
				}
			}
			// Se ha encontrado una entrada marcada como principal.
			else 
				if (++rootInputs > 1){
				// Se han encontrado varias entradas se�aladas como principales.
				// La refactorizaci�n contiene un error.
					Object[] messageArgs = {"\"" + Messages.DynamicRefactoringWindow_Root + "\""}; //$NON-NLS-1$ //$NON-NLS-2$
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(Messages.DynamicRefactoringWindow_SeveralInputsMarked);
					
					String message = formatter.format(messageArgs)
						+ ".\n" + Messages.DynamicRefactoringWindow_ReviewDefinition + ".\n"; //$NON-NLS-1$ //$NON-NLS-2$
					logger.error(message);
					exitWithError(message);
					return null;
				}

		}
		scrolledComposite2.setMinSize(cParameters.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		// Se han terminado de procesar todas las entradas.
		
		return container;
	}

	/**
	 * Carga el valor de una entrada a partir de la entrada principal.
	 * 
	 * @param cParameters
	 *            <code>Composite</code> que contendr� las nuevas etiqueta y
	 *            campo de texto o desplegable la entrada cargada.
	 * @param loadedInput
	 *            entrada cuyo valor se intenta cargar.
	 * 
	 * @return <code>true</code> si se pudo cargar; <code>false</code> en caso
	 *         contrario.
	 */
	private boolean loadFromMain(final Composite cParameters, InputParameter loadedInput) {
		
		try {
			Method method = currentObject.getClass().getMethod(loadedInput.getMethod(), 
				(Class[]) null);
			Object values = method.invoke(currentObject, (Object[]) null);
		
			if (values != null)	{								

				final CLabel lb_Input = new CLabel(cParameters, SWT.NONE);
				lb_Input.setBounds(342, desplazamiento + 105 + 67 * count, 183, 19);
				lb_Input.setAlignment(SWT.CENTER);
				lb_Input.setText(loadedInput.getName().replaceAll("_", " ")); //$NON-NLS-1$ //$NON-NLS-2$
			
				// Si se ha obtenido un �nico valor.
				if (isSingleValue(values)){
					t_Input = new Text(cParameters, SWT.BORDER);
					t_Input.setBounds(35, desplazamiento + 132 +67*count, 790, 25);
					t_Input.setText(NamedObjectHandler.getName(values));
					t_Input.setEditable(false);
					inputValues.put(loadedInput.getName(), values);
					// Se indica que el valor de la entrada lo almacena este campo.
					inputFields.put(loadedInput.getName(), t_Input);
				}
				// Otras entradas aparte del modelo tambi�n pueden
				// devolver conjuntos de valores.
				else {
					combo = new Combo(cParameters, SWT.BORDER);
					combo.setBounds(35, desplazamiento + 132 + 67 * count, 790, 25);
					combo.setVisibleItemCount(10);
					combo.addFocusListener(new ComboEditor());
			
					// Se procesa el contenido del conjunto de valores.
					fillInCombo(values, combo, loadedInput.getName());
					combo.addSelectionListener(new ComboSelectionListener());
					// Se indica que el valor de esta entrada se almacena en esta
					// lista desplegable.
					inputFields.put(loadedInput.getName(), combo);
				}
			}
			// Se incrementa en uno el espacio necesario.
			count++;
			return true;
		}
		catch (Exception e) {
			String message = 
				Messages.DynamicRefactoringWindow_ErrorInvokingAccessMethod +
				".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			exitWithError(message);
			return false;
		}
	}

	/**
	 * Carga la lista de posibles valores de una entrada a partir del modelo
	 * MOON.
	 * 
	 * @param cParameters
	 *            <code>Composite</code> en que se integrar�n la nueva etiqueta
	 *            y el nuevo desplegable con los valores de la entrada.
	 * @param nextInput
	 *            entrada cuyo valor se debe obtener.
	 * 
	 * @return <code>true</code> si se pudo cargar; <code>false</code> en caso
	 *         contrario.
	 */
	private boolean loadFromModel(final Composite cParameters, InputParameter nextInput) {
		
		try {
			Method method = model.getClass().getMethod(
				nextInput.getMethod(), (Class[]) null);
				
			Object values = method.invoke(
				model, (Object[]) null);
			
			if (values != null) {
				final CLabel lb_Input = new CLabel(cParameters, SWT.NONE);
				lb_Input.setBounds(342, desplazamiento +105 + 67 * count, 183, 19);
				lb_Input.setAlignment(SWT.CENTER);
				lb_Input.setText(nextInput.getName().replaceAll("_", " ")); //$NON-NLS-1$ //$NON-NLS-2$
				
				// Los m�todos utilizados del modelo devuelven
				// siempre conjuntos de valores.
				combo = new Combo(cParameters, SWT.BORDER);
				combo.setBounds(35, desplazamiento +132 + 67 * count, 790, 25);
				combo.setVisibleItemCount(10);
				combo.addFocusListener(new ComboEditor());
				
				// Se procesa el contenido del conjunto de valores.
				fillInCombo(values, combo, nextInput.getName());
				combo.addSelectionListener(new ComboSelectionListener());
				// Se indica que el valor de la entrada se almacena en esta lista
				// desplegable.
				inputFields.put(nextInput.getName(), combo);
			}
			// Se incrementa en uno el espacio necesario.
			count++;
			return true;
		}
		catch (Exception e) {
			Object[] messageArgs = {nextInput.getName()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoringWindow_ErrorObtainingValues);
			
			String message = formatter.format(messageArgs) + 
				".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			MessageDialog.openError(getShell(), Messages.DynamicRefactoringWindow_Error, message);
			return false;
		}
	}

	/**
	 * Comprueba si una entrada identificada como <code>root</code> (la entrada
	 * principal de la refactorizaci�n) conforma con el tipo del objeto
	 * seleccionado como entrada principal de la refactorizaci�n en tiempo de
	 * ejecuci�n.
	 * 
	 * <p>
	 * El objeto seleccionado deber� ser del mismo tipo o de un subtipo del tipo
	 * con que se define la entrada ra�z de la refactorizaci�n.
	 * </p>
	 * 
	 * <p>
	 * Si la comprobaci�n falla, se mostrar� un mensaje de error y se forzar� el
	 * cierre de la ventana de di�logo de refactorizaci�n, puesto que es un
	 * error grave que impide la ejecuci�n normal de la refactorizaci�n.
	 * 
	 * @param testedInput
	 *            entrada identificada como <code>root</code> y que conforma con
	 *            el formato utilizado en la definici�n de refactorizaciones.
	 * 
	 * @return <code>true</code> si los tipos se pueden comparar y conforman uno
	 *         con otro; <code>false</code> en caso contrario.
	 * 
	 * @see DynamicRefactoringDefinition#getInputs() para estudiar el formato de
	 *      especificaci�n de las entradas de una refactorizaci�n din�mica.
	 */
	private boolean checkMainInput(InputParameter testedInput) {
		// El tipo de la entrada debe coincidir con el esperado.
		try {
			Class<?> mainClass = Class.forName(testedInput.getType());
			// La clase de la entrada debe ser una superclase del tipo
			// que tiene el argumento principal seleccionado en Eclipse.
			if (! mainClass.isAssignableFrom(currentObject.getClass())){
				Object[] messageArgs = {"(" + currentObject.getClass().getCanonicalName() + ")", //$NON-NLS-1$ //$NON-NLS-2$
					"(" + mainClass.getCanonicalName() + ")"}; //$NON-NLS-1$ //$NON-NLS-2$
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(
					Messages.DynamicRefactoringWindow_ObjectNotComplies);
				
				exitWithError(formatter.format(messageArgs) + "."); //$NON-NLS-1$
				return false;
			}
			return true;
		}
		catch(ClassNotFoundException exception){
			String message = 
				Messages.DynamicRefactoringWindow_ClassNotLoaded + 
				".\n" + exception.getMessage();  //$NON-NLS-1$
			logger.error(message);
			exitWithError(message);
			return false;
		}
	}

	/**
	 * A�ade los botones a la barra de botones de este di�logo.
	 * 
	 * @param parent
	 *            el elemento compuesto de la barra de botones.
	 * 
	 * @see Dialog#createButtonsForButtonBar
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.DynamicRefactoringWindow_Run, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.DynamicRefactoringWindow_Close, false);
	}

	/**
	 * Devuelve el tama�o inicial que se debe utilizar.
	 * 
	 * @return el tama�o inicial de la ventana.
	 * 
	 * @see Dialog#getInitialSize()
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(900, 512);
	}

	/**
	 * Configura la shell proporcionada, prepar�ndola para la apertura de esta
	 * ventana sobre ella.
	 * 
	 * M�todo plantilla (patr�n de dise�o M�todo Plantilla).
	 * 
	 * @param newShell
	 *            la shell que se configura.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.DynamicRefactoringWindow_Refactoring + refactoringDefinition.getName());
		newShell.setImage(RefactoringImages.getConfigureIcon());
	}

	/**
	 * Muestra una ventana de di�logo con un mensaje de error y cierra la
	 * ventana actual.
	 * 
	 * @param errorMsg
	 *            mensaje de error que se debe mostrar.
	 */
	private void exitWithError(String errorMsg){
		MessageDialog.openError(getShell(), Messages.DynamicRefactoringWindow_Error, errorMsg);
		close();
	}

	/**
	 * Analiza el tipo de ejecuci�n de un objeto que deber�a contener un
	 * conjunto de elementos y, si es as�, los obtiene uno a uno y los a�ade
	 * como cadenas de texto al contenido de una lista desplegable.
	 * 
	 * <p>
	 * El contenido de la lista desplegable depender� de la implementaci�n que
	 * los elementos del conjunto proporcionen para el m�todo de
	 * <code>Object</code> <code>toString()</code>, ya que es el que se emplear�
	 * para obtener su representaci�n.
	 * </p>
	 * 
	 * @param values
	 *            objeto que debe contener un conjunto de elementos.
	 * @param combo
	 *            lista desplegable que se puebla con las representaciones
	 *            textuales de los objetos encontrados.
	 * @param inputName
	 *            nombre de la entrada para la que se rellena el desplegable.
	 * 
	 * @throws ClassNotFoundException
	 *             si no se consigue acceder a la clase de algunas de las clases
	 *             de conjuntos de Java.
	 */
	private void fillInCombo(Object values, Combo combo, String inputName) 
		throws ClassNotFoundException {
		
		// Durante la creaci�n de refactorizaciones, a la hora de escoger
		// m�todos
		// que devuelvan conjuntos de valores, se tienen en cuenta dos tipos de
		// conjuntos de datos de Java.

		Class<?> collection = Class.forName(
			RefactoringConstants.COLLECTION_PATH);
		Class<?> iterator = Class.forName(
			RefactoringConstants.ITERATOR_PATH);
		
		Class<?> container = values.getClass();
		
		ArrayList<Object> elements = new ArrayList<Object>();
		
		// Si los valores est�n contenidos en una colecci�n.
		if (collection.isAssignableFrom(container))
			for (Object next : ((java.util.Collection<?>)values))
				elements.add(next);
		
		// Si los valores est�n contenidos en un iterador.
		else if (iterator.isAssignableFrom(container)){
			Iterator<?> valueIterator = (java.util.Iterator<?>)values;
			while(valueIterator.hasNext())
				elements.add(valueIterator.next());
		}
		
		List<Object> orderedElements = NamedObjectHandler.getSortedList(elements);
		inputValues.put(inputName, orderedElements.get(0));
		comboValues.put(Integer.valueOf(combo.hashCode()), orderedElements);
		for (Object next : orderedElements)
			combo.add(NamedObjectHandler.getName(next));
		combo.select(0);
	}

	/**
	 * Analiza el tipo de ejecuci�n de un objeto para determinar si contiene un
	 * �nico elemento o, por el contrario, se trata de alg�n tipo de conjunto.
	 * 
	 * <p>
	 * Si no se trata de un subtipo de <code>java.util.List</code>, ni de
	 * <code>java.util.Iterator</code>, ni de <code>java.util.Collection</code>,
	 * se considera un valor �nico. Estos tres son los tipos de conjuntos que
	 * devuelven los m�todos del modelo que acceden a listas, tablas,
	 * colecciones, etc.
	 * </p>
	 * 
	 * @param object
	 *            objeto cuyo car�cter de elemento �nico se comprueba.
	 * 
	 * @return <code>true</code> si se considera que se trata de un elemento
	 *         �nico; <code>false</code> en caso contrario.
	 * 
	 * @throws ClassNotFoundException
	 *             si no se consigue acceder a la clase de algunas de las clases
	 *             de conjuntos de Java.
	 */
	public static boolean isSingleValue(Object object) throws ClassNotFoundException {
		
		// Durante la creaci�n de refactorizaciones, a la hora de escoger
		// m�todos
		// que devuelvan conjuntos de valores, se tienen en cuenta tres tipos de
		// conjuntos de datos de Java.

		Class<?> collection = Class.forName(
			RefactoringConstants.COLLECTION_PATH);
		Class<?> iterator = Class.forName(
			RefactoringConstants.ITERATOR_PATH);
		
		Class<?> elementType = object.getClass();
		
		if (! collection.isAssignableFrom(elementType) &&
			! iterator.isAssignableFrom(elementType))
			return true;
		
		return false;
	}

	/**
	 * Obtiene un m�todo con un cierto nombre a partir de la definici�n de una
	 * clase cargada a partir de su nombre completamente cualificado.
	 * 
	 * @param from
	 *            nombre completamente cualificado del tipo de la clase cuyo
	 *            m�todo se debe cargar.
	 * @param method
	 *            nombre simple del m�todo que se debe cargar.
	 * 
	 * @return el m�todo obtenido por reflexi�n a partir de la clase de nombre
	 *         dado.
	 * 
	 * @throws ClassNotFoundException
	 *             si no se encuentra la clase.
	 * @throws NoSuchMethodException
	 *             si no se encuentra el m�todo en la clase.
	 */
	private Method loadMethod(String from, String method) 
		throws ClassNotFoundException, NoSuchMethodException {
		
		// Se intenta cargar la clase del tipo de origen.
		Class<?> inputClass = Class.forName(from);
		// Se intenta cargar el m�todo con el nombre dado y sin par�metros.
		return inputClass.getMethod(method, (Class[]) null);
	}

	/**
	 * Convierte los objetos en cadenas con su nombre cualificado.
	 * 
	 * @param inputParameters
	 *            <code>HashMap</code> con el nombre de los par�metros como
	 *            clave y el objeto qeu lo representa como valor.
	 * @return <code>HashMap</code> con el nombre de los par�metros como clave y
	 *         su nombre cualificado como valor.
	 */
	public static HashMap<String,String> getInputParameters(HashMap<String,Object> inputParameters){
		HashMap<String,String> params = new HashMap<String,String>();
		
		// Guardamos la informaci�n de los parametros de entrada para
		// escribirlos en el plan
		//de refactorizaciones.
		for(Map.Entry<String ,Object> param : inputParameters.entrySet()){
			if(!param.getKey().equals("Model")){
				if(param.getValue() instanceof moon.core.NamedObject)
					params.put(param.getKey(), ((NamedObject)param.getValue()).getUniqueName().toString());
				else
					if(param.getValue() instanceof moon.core.instruction.CodeFragment){
						CodeFragment code=(CodeFragment)param.getValue();
						params.put(param.getKey(), code.getLine() + "," + code.getColumn() + "," +
								code.getEndLine() + "," + code.getEndColumn() + "," + code.getClassDef().getUniqueName()
								+ "," + code.getText());
					}else
						params.put(param.getKey(), param.getValue().toString());
			}else{
				// no necesito guardar informaci�n sobre el modelo ya que al
				// exportar el plan
				// el modelo sobre el que se trabajar� ser� diferente.
				params.put(param.getKey(), "");
			}
		}
		return params;
	}

	/**
	 * Resuelve recursivamente el valor de una entrada que depende de otra
	 * entrada distinta de la entrada principal y del modelo.
	 * 
	 * @param cParameters
	 *            elemento sobre el que se situar� el campo de texto o
	 *            desplegable asociado a la nueva entrada una vez resuelta.
	 * @param sourceInput
	 *            <i>array</i> de cadenas con los atributos de la entrada a
	 *            partir de la que se obtiene el valor para la nueva entrada.
	 * @param newInput
	 *            <i>array</i> de cadenas con los atributos de la nueva entrada
	 *            que se debe intentar resolver.
	 * 
	 * @return <code>true</code> si se logr� resolver correctamente la nueva
	 *         entrada a partir de la entrada de origen; <code>false</code> en
	 *         caso contrario.
	 */
	private boolean recursiveDependencyResolution(Composite cParameters,
		InputParameter sourceInput, InputParameter newInput) {
		
		if (recursiveInputs.contains(newInput.getName())){
			Object[] messageArgs = {newInput.getName()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.DynamicRefactoringWindow_CycleFound);
			
			String message = formatter.format(messageArgs) + ".\n" +  //$NON-NLS-1$
				Messages.DynamicRefactoringWindow_ReviewDefinition + ".\n"; //$NON-NLS-1$
			logger.error(message);
			exitWithError(message);
			return false;
		}
		recursiveInputs.add(newInput.getName());

		try {
			if (! inputValues.containsKey(sourceInput.getName())){
				InputParameter recursiveSource = inputAttributes.get(sourceInput.getFrom());
				if (recursiveSource != null){
					if (recursiveSource.getType().equals("moon.core.Model")) //$NON-NLS-1$
						loadFromModel(cParameters, sourceInput);
					else if(recursiveSource.isMain()) //$NON-NLS-1$
						loadFromMain(cParameters, sourceInput);
					else
						recursiveDependencyResolution(cParameters,
							inputAttributes.get(sourceInput.getFrom()), sourceInput);
				}
			}
			
			// Puede que no se haya cargado a�n el valor de origen porque deba
			// ser
			// introducido de forma manual. Solo se continua si se ha cargado.
			if (inputValues.containsKey(sourceInput.getName())){
				
				Method runtimeMeth = loadMethod(sourceInput.getType(), newInput.getMethod());
				Object values = runtimeMeth.invoke(inputValues.get(sourceInput.getName()),
					(Object[]) null);
				
				if (values != null)	{
					
					// Si el valor se ha obtenido a partir de una entrada
					// desplegable, hay que registrar esta dependencia.
					Scrollable sourceField = inputFields.get(sourceInput.getName()); 
					if (sourceField != null && sourceField instanceof Combo){
						String[] dependent = comboDependencies.get(
							Integer.valueOf(sourceField.hashCode()));
						String[] updated;
						if (dependent != null){
							updated = Arrays.copyOf(
								dependent, dependent.length + 1);
							// Se a�ade el nombre de la nueva entrada a la lista
							// de
							// entradas dependientes del desplegable.
							updated[updated.length] = newInput.getName();
						}
						else 
							updated = new String[]{newInput.getName()};
						comboDependencies.put(sourceField.hashCode(), updated);
					}
					
					final CLabel lb_Input = new CLabel(cParameters, SWT.NONE);
					lb_Input.setBounds(342, desplazamiento +105 + 67 * count, 183, 19);
					lb_Input.setAlignment(SWT.CENTER);
					lb_Input.setText(newInput.getName().replaceAll("_", " ")); //$NON-NLS-1$ //$NON-NLS-2$
					
					// Si se ha obtenido un �nico valor.
					if (isSingleValue(values)){
						t_Input = new Text(cParameters, SWT.BORDER);
						t_Input.setBounds(35, desplazamiento +132 + 67 * count, 790, 25);
						t_Input.setText(NamedObjectHandler.getName(values));
						t_Input.setEditable(false);
						inputValues.put(newInput.getName(), values);
						// Se indica que el valor de esta entrada lo tiene este campo.
						inputFields.put(newInput.getName(), t_Input);
					}
					// Otras entradas aparte del modelo tambi�n pueden
					// devolver conjuntos de valores.
					else {
						combo = new Combo(cParameters, SWT.BORDER);
						combo.setBounds(35, desplazamiento +132 + 67 * count, 790, 25);
						combo.setVisibleItemCount(10);
						combo.addFocusListener(new ComboEditor());
				
						// Se procesa el contenido del conjunto de valores.
						fillInCombo(values, combo, newInput.getName());
						combo.addSelectionListener(new ComboSelectionListener());
						// Se indica que el valor de esta entrada se almacena en
						// esta lista desplegable.
						inputFields.put(newInput.getName(), combo);
					}
					
					// Se incrementa en uno el espacio necesario.
					count++;
				}
			}
			// No se admiten dependencias de campos cuyo valor deba ser
			// introducido por el usuario.
			else {
				Object[] messageArgs = {newInput.getName()};
				MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
				formatter.applyPattern(
					Messages.DynamicRefactoringWindow_DependsOnUser);
				
				String message = formatter.format(messageArgs) + ".\n" +  //$NON-NLS-1$
					Messages.DynamicRefactoringWindow_ReviewDefinition + ".\n"; //$NON-NLS-1$
					logger.error(message);
				exitWithError(message);
				return false;
			}
			
			return true;	
		}
		catch (ClassNotFoundException exception){
			String message = 
				Messages.DynamicRefactoringWindow_ErrorLoadingClass + 
				".\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			exitWithError(message);
			return false;
		}
		catch (Exception exception){
			String message = 
				Messages.DynamicRefactoringWindow_ErrorInvokingMethodOnInput 
				+ ".\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			exitWithError(message);
			return false;
		}
	}

	/**
	 * Recalcula el valor asociado a una entrada.
	 * 
	 * <p>
	 * Solo debe llamarse para entradas que dependan de terceras entradas
	 * distintas de la entrada principal de la refactorizaci�n y del modelo,
	 * puesto que ninguna de las dos puede cambiar y se obtendr�a siempre, por
	 * tanto, el mismo valor.
	 * </p>
	 * 
	 * @param input
	 *            entrada cuyo valor se debe recalcular.
	 */
	private void recompute(InputParameter input){
		
		// Se obtiene la entrada de la que depende la actual.
		InputParameter sourceInput = inputAttributes.get(input.getFrom());
		
		// Se obtiene el valor de la entrada de la que depende la actual.
		Object source = inputValues.get(sourceInput.getName());
		
		try {
			// Solo se actualiza el valor si el valor de origen ya est�
			// disponible.
			if (source != null){
				// Se carga el m�todo con el que se obtendr� el valor de la
				// entrada.
				Method method = loadMethod(sourceInput.getType(), input.getMethod());
				// Se calcula el nuevo valor.
				Object values = method.invoke(source, (Object[]) null);
				
				if (values != null){
					
					Scrollable field = inputFields.get(input.getName());
					if (field instanceof Text){
						// Se actualiza el valor de la entrada.
						inputValues.put(input.getName(), values);
						((Text)field).setText(NamedObjectHandler.getName(values));
					}
					else if (field instanceof Combo)
						// Se rellena de nuevo el desplegable.
						// El propio m�todo actualiza las tablas de entradas.
						fillInCombo(values, (Combo)field, input.getName());
				}				
			}
		}
		catch (Exception exception){
			String message = 
				Messages.DynamicRefactoringWindow_ErrorInvokingMethodOnInput + 
				".\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);
			exitWithError(message);
			close();
		}		
	}

	/**
	 * Determina si se han introducido todos los valores necesarios para
	 * configurar la refactorizaci�n.
	 * 
	 * @return <code>true</code> si se ha introducido ya un valor para cada
	 *         entrada necesaria para la refactorizaci�n; <code>false</code> en
	 *         caso contrario.
	 */
	private boolean isComplete(){
		if (inputAttributes.entrySet().size() > inputValues.entrySet().size() + 2)
			return false;
		return true;
	}

	/**
	 * Elimina los htmls generados por java2html para visualizar los ejemplos
	 * en un navegador html.
	 */
	private void deleteHTMLS(){
		//directorio de la refactorizacion
		String dirRefactoring = new File(refactoringDefinition.getExamplesAbsolutePath().get(0).getBefore()).getParent();
		
		for(RefactoringExample ejemplos : refactoringDefinition.getExamples()){
				File html_fich = new File(dirRefactoring + File.separator 
						+ ejemplos.getBefore().replace("txt", "java") + ".html");
				if(html_fich.exists())
					html_fich.delete();
				html_fich = new File(dirRefactoring + File.separator 
						+ ejemplos.getAfter().replace("txt", "java") + ".html");
				if(html_fich.exists())
					html_fich.delete();
		}
		
		if(new File(dirRefactoring + File.separator + "AllClasses.html").exists()){
			new File(dirRefactoring + File.separator + "AllClasses.html").delete();
		}
		
		if(new File(dirRefactoring + File.separator + "default.index.html").exists()){
			new File(dirRefactoring + File.separator + "default.index.html").delete();
		}
		
		if(new File(dirRefactoring + File.separator + "front.html").exists()) {
			new File(dirRefactoring + File.separator + "front.html").delete();
		}
		
		if(new File(dirRefactoring + File.separator + "index.html").exists()) {
			new File(dirRefactoring + File.separator + "index.html").delete();
		}
		
		if(new File(dirRefactoring + File.separator + "packages.html").exists()) {
			new File(dirRefactoring + File.separator + "packages.html").delete();
		}
		
		if(new File(dirRefactoring + File.separator + "stylesheet.css").exists()){
			new File(dirRefactoring + File.separator + "stylesheet.css").delete();
		}
		
		if(new File(dirRefactoring + File.separator + "consola.txt").exists()){
			new File(dirRefactoring + File.separator + "consola.txt").delete();
		}
		
	}

	/**
	 * Cierra la ventana de di�logo.
	 */
	@Override
	public boolean close(){
		//Es necesario hacer limpieza de los ficheros html generados cuando
		//hay ejemplos que mostrar.
		if(!refactoringDefinition.getExamples().isEmpty())
			deleteHTMLS();
		return super.close();
	}

	/**
	 * Notifica que el bot�n de este di�logo con el identificador especificado
	 * ha sido pulsado.
	 * 
	 * @param buttonId
	 *            el identificador del bot�n que ha sido pulsado (v�anse las
	 *            constantes <code>IDialogConstants.*ID</code>).
	 * 
	 * @see Dialog#buttonPressed
	 * @see IDialogConstants
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		
		// Independiente del tipo de bot�n que pusemos en este di�logo, es
		//necesario hacer limpieza de los ficheros html generados cuando
		//hay ejemplos que mostrar.
		if(!refactoringDefinition.getExamples().isEmpty())
			deleteHTMLS();

		if (buttonId == IDialogConstants.OK_ID){
			
			if (isComplete()){
			
				try {
					InputProcessor processor = new InputProcessor(this);
					
					MOONRefactoring.resetModel();
					
					DynamicRefactoring refactoring = new DynamicRefactoring(refactoringDefinition, model, processor.getInputs());
					
					this.close();
					
					DynamicRefactoringRunner runner = new DynamicRefactoringRunner(refactoring);
					runner.setInputParameters(getInputParameters(processor.getInputs()));
					runner.runRefactoring();
					
					return;
				}
				catch(RefactoringException exception){
					String message = 
						Messages.DynamicRefactoringWindow_ErrorBuilding 
						+ ":\n\n" + exception.getMessage(); //$NON-NLS-1$
					logger.error(message);
					exitWithError(message);
					return;
				}
			}
			else
				MessageDialog.openWarning(getShell(), 
					Messages.DynamicRefactoringWindow_Warning, Messages.DynamicRefactoringWindow_FieldsMissing 
					+ "."); //$NON-NLS-1$
		}
		else 
			super.buttonPressed(buttonId);
	}

	/**
	 * Recibe notificaciones cuando cambia la selecci�n en uno de los
	 * desplegables asociado a una de las entradas.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ComboSelectionListener implements SelectionListener {

		/**
		 * Recibe una notificaci�n de que un elemento del desplegable observado
		 * ha sido seleccionado.
		 * 
		 * <p>
		 * Inicia la actualizaci�n de todas las entradas cuyo valor es
		 * dependiente del del desplegable.
		 * </p>
		 * 
		 * @param e
		 *            el evento de selecci�n disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (e.getSource() instanceof Combo){
				Combo observed = (Combo)e.getSource();
				
				// Se obtiene el nombre de la entrada asociada al desplegable.
				String thisInput = ""; //$NON-NLS-1$
				for (Entry<String, Scrollable> entry : inputFields.entrySet()){
					if (entry.getValue() == observed){
						thisInput = entry.getKey();
						break;
					}
				}
				// Se obtiene la lista de valores asociada al desplegable.
				List<Object> values = comboValues.get(Integer.valueOf(observed.hashCode()));
				// Se actualiza el valor de la entrada asociada al desplegable.
				// Se toma el valor de la posici�n indicada por la selecci�n.
				inputValues.put(thisInput, values.get(observed.getSelectionIndex()));
								
				// Se obtienen los nombes de las entradas dependientes del combo.
				String[] dependent = 
					comboDependencies.get(Integer.valueOf(observed.hashCode()));
				if (dependent != null)
					for (String inputName : dependent){
						// Se obtienen los atributos completos de la entrada.
						InputParameter input = inputAttributes.get(inputName);
						recompute(input);
				}
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
	 * Recibe notificaciones cuando cambia la selecci�n en uno de los campos de
	 * texto editables asociado a una de las entradas.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class TextModifyListener implements ModifyListener {

		/**
		 * Recibe una notificaci�n de que un elemento del desplegable observado
		 * ha sido seleccionado.
		 * 
		 * <p>
		 * Inicia la actualizaci�n del valor asociado al campo de texto
		 * editable.
		 * </p>
		 * 
		 * @param e
		 *            el evento de modificaci�n disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void modifyText(ModifyEvent e) {
			if (e.getSource() instanceof Text){
				Text observed = (Text)e.getSource();
				
				// Se obtiene el nombre de la entrada asociada al desplegable.
				String thisInput = ""; //$NON-NLS-1$
				for (Entry<String, Scrollable> entry : inputFields.entrySet()){
					if (entry.getValue() == observed){
						thisInput = entry.getKey();
						break;
					}
				}
				// Se obtiene el valor asociado al campo de texto.
				String value = observed.getText().trim();
				// Se actualiza el valor de la entrada asociada al campo.
				inputValues.put(thisInput, value);
			}			
		}
	}
}