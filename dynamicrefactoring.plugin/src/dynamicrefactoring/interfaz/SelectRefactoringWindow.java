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

package dynamicrefactoring.interfaz;

import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.interfaz.dynamic.DynamicRefactoringWindowLauncher;

import dynamicrefactoring.util.ScopeLimitedLister;
import dynamicrefactoring.util.io.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.swt.graphics.Point;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import com.swtdesigner.ResourceManager;

import moon.core.ObjectMoon;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.instruction.CodeFragment;

import moon.core.genericity.BoundS;
import moon.core.genericity.FormalPar;
import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;

/**
 * Proporciona una interfaz sencilla sobre la que el usuario puede seleccionar
 * la refactorización que desea ejecutar a continuación sobre el elemento
 * seleccionado.
 * 
 * <p>Muestra un listado poblado de forma dinámica con las refactorizaciones
 * estáticas y dinámicas disponibles y que se puedan ejecutar en el ámbito del
 * objeto seleccionado en la interfaz en ese momento.</p>
 * 
 * <p>El objeto seleccionado en la interfaz constituirá la entrada principal de
 * la refactorización que se seleccione para ser ejecutada.</p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class SelectRefactoringWindow extends Dialog {
	
	/**
	 * Ámbito de aplicación de refactorizaciones con el que trabajará la ventana.
	 */
	private Scope scope;
	
	/**
	 * Objeto seleccionado como entrada principal al proceso de refactorización. 
	 */
	private ObjectMoon mainObject;
	
	/**
	 * Refactorizaciones disponibles para el tipo de entrada principal seleccionada.
	 */
	private Vector<String> available_refactors;
	
	/**
	 * Lista de refactorizaciones dinámicas cargadas.
	 */
	HashMap<String, String> dynamicRefactorings;
	
	/**
	 * Lista sobre la que se visualizan las refactorizaciones disponibles.
	 */
	private List list;
	
	/**
	 * Constructor.
	 * 
	 * @param parentShell shell a la que pertenece la ventana de diálogo.
	 * @param mainObject objeto seleccionado como entrada principal.
	 */
	public SelectRefactoringWindow(Shell parentShell, ObjectMoon mainObject) {
		super(parentShell);

		available_refactors= new Vector<String>();
		this.mainObject = mainObject;
	
		if(mainObject instanceof ClassDef){
			scope = Scope.CLASS;
		}
		else if(mainObject instanceof MethDec){
			scope = Scope.METHOD;
		}
		else if(mainObject instanceof AttDec){
			scope = Scope.ATTRIBUTE;
		}
		else if(mainObject instanceof FormalArgument){
			scope = Scope.FORMAL_ARG;
		}
		else if(mainObject instanceof BoundS && 
				((BoundS)mainObject).getBounds().size() > 0){
			scope = Scope.BOUNDED_PAR;
		}
		else if(mainObject instanceof FormalPar){
			scope = Scope.FORMAL_PAR;
		}else if(mainObject instanceof CodeFragment){
			scope = Scope.CODE_FRAGMENT;
		}
		
		
		dynamicRefactorings = 
			ScopeLimitedLister.getAvailableRefactorings(scope);
		fillInDynamicRefactorings(dynamicRefactorings);
		// Para un parámetro formal acotado valen también las refactorizaciones
		// generales sobre parámetros formales.
		if (scope == Scope.BOUNDED_PAR){
			HashMap <String, String> tempMap =
				ScopeLimitedLister.getAvailableRefactorings(Scope.FORMAL_PAR);
			dynamicRefactorings.putAll(tempMap);				
			fillInDynamicRefactorings(tempMap);
		}
	}

	/**
	 * Puebla la lista de refactorizaciones disponibles con las refactorizaciones
	 * incluidas en una tabla asociativa en el formato devuelto por <code>
	 * ScopeLimitedLister</code>.
	 * 
	 * @param refactorings tabla de refactorizaciones que se deben añadir
	 * a la lista de disponibles.
	 */
	private void fillInDynamicRefactorings(HashMap<String, String> refactorings) {
		if (refactorings != null){
			ArrayList<String> names = new ArrayList<String>();
			for(String nextDynamicRef : refactorings.keySet())
				names.add(nextDynamicRef);
			Collections.sort(names);
			for(String name : names)
				available_refactors.add(name);
		}
	}

	/**
	 * Crea el contenido de la ventana de diálogo.
	 * 
	 * @param parent el elemento compuesto padre de esta ventana de diálogo.
	 * 
	 * @return el control asociado al área de diálogo.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FormLayout());
		final GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd_composite.heightHint = 174;
		gd_composite.widthHint = 343;
		composite.setLayoutData(gd_composite);

		final Label classLabel = new Label(composite, SWT.CENTER);
		final FormData fd_classLabel = new FormData();
		fd_classLabel.top = new FormAttachment(0, 10);
		classLabel.setLayoutData(fd_classLabel);
		classLabel.setText(Messages.SelectRefactoringWindow_SelectRefactoring);

		list = new List(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		fd_classLabel.right = new FormAttachment(list, 0, SWT.RIGHT);
		fd_classLabel.left = new FormAttachment(list, 0, SWT.LEFT);
		list.setToolTipText(Messages.SelectRefactoringWindow_SelectRun);
		fd_classLabel.bottom = new FormAttachment(list, -5, SWT.TOP);
		final FormData fd_list = new FormData();
		fd_list.bottom = new FormAttachment(0, 145);
		fd_list.top = new FormAttachment(0, 35);
		fd_list.right = new FormAttachment(0, 316);
		fd_list.left = new FormAttachment(0, 26);
		list.setLayoutData(fd_list);

		fillList();
		return container;
	}
	
	/**
	 * Devuelve las refactorizaciones dinámicas.
	 * @return refactorizaciones dinámicas.
	 */
	public Vector<String> getAvailableRefactors() {
		return available_refactors;
	}

	/**
	 * Crea el contenido de la barra de botones.
	 * 
	 * @param parent contenedor de la barra de botones.
	 * 
	 * @see Dialog#createButtonsForButtonBar
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.SelectRefactoringWindow_Accept, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.SelectRefactoringWindow_Cancel, false);
	}

	/**
	 * Puebla la lista de refactorizaciones disponibles.
	 */
	public void fillList(){
		for(int i=0; i<available_refactors.size();i++)
			list.add(available_refactors.elementAt(i));
	}

	/**
	 * Obtiene el tamaño inicial de la ventana de diálogo.
	 * 
	 * @return el tamaño inicial de la ventana de diálogo.
	 * 
	 * @see Dialog#getInitialSize
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(357, 243);
	}
	
	/**
	 * Configura la shell dada, preparándola para abrir esta ventana de diálogo
	 * sobre ella.
	 * 
	 * @param newShell la shell que se ha de configurar.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		switch(scope){
		case CLASS:
			newShell.setText(Messages.SelectRefactoringWindow_ClassScope);
			break;
		case METHOD:
			newShell.setText(Messages.SelectRefactoringWindow_MethodScope);
			break;
		case ATTRIBUTE:
			newShell.setText(Messages.SelectRefactoringWindow_FieldScope);
			break;
		case FORMAL_ARG:
			newShell.setText(Messages.SelectRefactoringWindow_FormalArgumentScope);
			break;
		case FORMAL_PAR:
			newShell.setText(Messages.SelectRefactoringWindow_FormalParameterScope);
			break;
		case BOUNDED_PAR:
			newShell.setText(Messages.SelectRefactoringWindow_BoundedParameterScope);
			break;
		case CODE_FRAGMENT:
			newShell.setText(Messages.SelectRefactoringWindow_CodeFragmentScope);
			break;
		}
		newShell.setImage(ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				RefactoringImages.REF_ICON_PATH)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * Notifica que el botón de este diálogo con el identificador especificado
	 * ha sido pulsado.
	 * 
	 * @param buttonId el identificador del botón que ha sido pulsado (véanse
	 * las constantes <code>IDialogConstants.*ID</code>).
	 * 
	 * @see Dialog#buttonPressed
	 * @see IDialogConstants
	 */
	@Override
	protected void buttonPressed(int buttonId) {

		if(buttonId == IDialogConstants.OK_ID) {
						
			String selectedName = list.getItem(list.getSelectionIndex());
			
			this.close();

				String name = FileManager.getFileName(
					dynamicRefactorings.get(selectedName));
				new DynamicRefactoringWindowLauncher(
					mainObject, FileManager.getFilePathWithoutExtension(name));
		}
			
		super.buttonPressed(buttonId);
	}
}