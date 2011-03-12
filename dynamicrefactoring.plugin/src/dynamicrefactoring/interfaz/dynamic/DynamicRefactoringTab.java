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
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.swtdesigner.SWTResourceManager;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.interfaz.TreeEditor;

/**
 * Pesta�a con el contenido resumen de una refactorizaci�n.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class DynamicRefactoringTab {

	/**
	 * Campo de texto en que se muestra la motivaci�n de la refactorizaci�n.
	 */
	private Text t_Motivation;

	/**
	 * Campo de texto en que se muestra la descripci�n de la refactorizaci�n.
	 */
	private Text t_Description;

	/**
	 * Tabla de entradas de la refactorizaci�n.
	 */
	private Table tb_Inputs;

	/**
	 * Tabla de componentes de la refactorizaci�n.
	 */
	private Tree tr_Components;

	/**
	 * Pesta�a asociada al resumen de la refactorizaci�n.
	 */
	private TabItem tab;

	/**
	 * La definici�n de la refactorizaci�n.
	 */
	protected DynamicRefactoringDefinition refactoringDefinition;

	/**
	 * Crea la pesta�a con el resumen de la refactorizaci�n.
	 * 
	 * @param parent
	 *            el contenedor de pesta�as que contendr� esta pesta�a.
	 * @param definition
	 *            la definici�n de la refactorizaci�n.
	 */
	public DynamicRefactoringTab(TabFolder parent,
		DynamicRefactoringDefinition definition){

		this.refactoringDefinition = definition;
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, 
			SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		tab = new TabItem(parent, SWT.NONE);	
		tab.setText(Messages.DynamicRefactoringTab_Information);
		tab.setControl(scrolledComposite);

		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		// Pesta�a de informaci�n.
		final Composite cInformation = new Composite(scrolledComposite, SWT.NONE);
		cInformation.setLocation(0, 0);
		cInformation.setSize(872, 390);
		scrolledComposite.setContent(cInformation);
		
		final Label nameLabel = new Label(cInformation, SWT.NONE);
		nameLabel.setAlignment(SWT.CENTER);
		nameLabel.setText(refactoringDefinition.getName());
		nameLabel.setFont(SWTResourceManager.getFont("Tahoma", 14, SWT.NONE)); //$NON-NLS-1$
		nameLabel.setBounds(10, 15, 354, 39);
		
		// Se a�ade la imagen.
		final Canvas canvas = new Canvas(cInformation, SWT.NO_REDRAW_RESIZE | SWT.H_SCROLL |SWT.V_SCROLL | SWT.BORDER);
		canvas.setBounds(456, 15, 385, 162);
		canvas.setBackground(new Color(null, 255,255, 255));
		
		if (refactoringDefinition.getImage() != null && 
				refactoringDefinition.getImage().length() != 0){

			String path = RefactoringPlugin.getDynamicRefactoringsDir()
				+ File.separatorChar //$NON-NLS-1$
				+ refactoringDefinition.getName() + File.separatorChar //$NON-NLS-1$
				+ refactoringDefinition.getImage();
			ImageLoader loader = new ImageLoader();

			final Image image = new Image(canvas.getDisplay(), loader.load(path)[0]);
			final Point origin = new Point(0, 0);

			// Los scrolls del canvas para la imagen.
			final ScrollBar hBar = canvas.getHorizontalBar();
			hBar.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					int hSelection = hBar.getSelection();
					int destX = -hSelection - origin.x;
					Rectangle rect = image.getBounds();
					canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
					origin.x = -hSelection;
				}
			});
			final ScrollBar vBar = canvas.getVerticalBar();
			vBar.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					int vSelection = vBar.getSelection();
					int destY = -vSelection - origin.y;
					Rectangle rect = image.getBounds();
					canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
					origin.y = -vSelection;
				}
			});

			Rectangle rect = image.getBounds();
			Rectangle client = canvas.getClientArea();
			hBar.setMaximum(rect.width);
			vBar.setMaximum(rect.height);
			hBar.setThumb(Math.min(rect.width, client.width));
			vBar.setThumb(Math.min(rect.height, client.height));

			int hPage = rect.width - client.width;
			int vPage = rect.height - client.height;
			int hSelection = hBar.getSelection();
			int vSelection = vBar.getSelection();
			if (hSelection >= hPage) {
				if (hPage <= 0)
					hSelection = 0;
				origin.x = -hSelection;
			}
			if (vSelection >= vPage) {
				if (vPage <= 0)
					vSelection = 0;
				origin.y = -vSelection;
			}
			canvas.addListener(SWT.Resize, new Listener() {
				public void handleEvent(Event e) {
					Rectangle rect = image.getBounds();
					Rectangle client = canvas.getClientArea();
					hBar.setMaximum(rect.width);
					vBar.setMaximum(rect.height);
					hBar.setThumb(Math.min(rect.width, client.width));
					vBar.setThumb(Math.min(rect.height, client.height));
					int hPage = rect.width - client.width;
					int vPage = rect.height - client.height;
					int hSelection = hBar.getSelection();
					int vSelection = vBar.getSelection();
					if (hSelection >= hPage) {
						if (hPage <= 0)
							hSelection = 0;
						origin.x = -hSelection;
					}
					if (vSelection >= vPage) {
						if (vPage <= 0)
							vSelection = 0;
						origin.y = -vSelection;
					}
					canvas.redraw();
				}
			});
			canvas.addListener(SWT.Paint, new Listener() {
				public void handleEvent(Event e) {
					GC gc = e.gc;
					gc.drawImage(image, origin.x, origin.y);
					Rectangle rect = image.getBounds();
					Rectangle client = canvas.getClientArea();
					int marginWidth = client.width - rect.width;
					if (marginWidth > 0) {
						gc.fillRectangle(rect.width, 0, marginWidth, client.height);
					}
					int marginHeight = client.height - rect.height;
					if (marginHeight > 0) {
						gc.fillRectangle(0, rect.height, client.width, marginHeight);
					}
				}
			});
			canvas.redraw();
		}
		
		final Label separator1 = new Label(cInformation, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setBounds(10, 183, 843, 13);
		
		scrolledComposite.setMinSize(cInformation.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		final Label lb_Description = new Label(cInformation, SWT.NONE);
		lb_Description.setText(Messages.DynamicRefactoringTab_Description);
		lb_Description.setBounds(10, 60, 354, 13);
		
		t_Description = new Text(cInformation, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		t_Description.setEditable(false);
		t_Description.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Description.setBounds(10, 79, 430, 37);
		
		final Label lb_Motivation = new Label(cInformation, SWT.NONE);
		lb_Motivation.setText(Messages.DynamicRefactoringTab_Motivation);
		lb_Motivation.setBounds(10, 122, 354, 13);

		t_Motivation = new Text(cInformation, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		t_Motivation.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		t_Motivation.setBounds(10, 142, 430, 37);
		
		tr_Components = new Tree(cInformation, SWT.BORDER);
		tr_Components.setBounds(456, 202, 385, 177);

		tb_Inputs = new Table(cInformation, SWT.BORDER);
		tb_Inputs.setEnabled(false);
		tb_Inputs.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		tb_Inputs.setLinesVisible(true);
		tb_Inputs.setHeaderVisible(true);
		tb_Inputs.setBounds(10, 202, 430, 177);

		final TableColumn cl_Name = new TableColumn(tb_Inputs, SWT.NONE);
		cl_Name.setWidth(179);
		cl_Name.setText(Messages.DynamicRefactoringTab_Name);

		final TableColumn cl_Type = new TableColumn(tb_Inputs, SWT.NONE);
		cl_Type.setWidth(212);
		cl_Type.setText(Messages.DynamicRefactoringTab_Type);

		final TableColumn cl_Main = new TableColumn(tb_Inputs, SWT.NONE);
		cl_Main.setWidth(35);
		cl_Main.setText(Messages.DynamicRefactoringTab_Main);
		cl_Main.setResizable(false);
		cl_Main.setToolTipText(Messages.DynamicRefactoringTab_MainInput);
		
		fillInRefactoringData();
	}

	/**
	 * Puebla los campos de la pesta�a con la informaci�n que se pueda obtener
	 * de la refactorizaci�n existente que se est� editando o que se est� a
	 * punto de crear.
	 */
	private void fillInRefactoringData(){
		t_Description.setText(refactoringDefinition.getDescription());
		t_Motivation.setText(refactoringDefinition.getMotivation());
		
		ArrayList<String[]> inputs = refactoringDefinition.getInputs();
		for (String[] input : inputs){
			TableItem item = new TableItem(tb_Inputs, SWT.BORDER);
			item.setText(new String[]{input[1], input[0], "", ""}); //$NON-NLS-1$ //$NON-NLS-2$
				
			TableEditor editor = new TableEditor(tb_Inputs);
			Button checkButton = new Button(tb_Inputs, SWT.CHECK);
			if (input[4] != null && input[4].equals("true")) //$NON-NLS-1$
				checkButton.setSelection(true);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, item, 2);
		}
			
		ArrayList<String> preconditions = refactoringDefinition.getPreconditions();
		ArrayList<String> actions = refactoringDefinition.getActions();
		ArrayList<String> postconditions = refactoringDefinition.getPostconditions();
				
		TreeItem preconditionsChild = TreeEditor.createBranch(tr_Components, 0,
			Messages.DynamicRefactoringTab_Preconditions, RefactoringImages.CHECK_ICON_PATH);
		TreeItem actionsChild = TreeEditor.createBranch(tr_Components, 1, 
			Messages.DynamicRefactoringTab_Actions, RefactoringImages.RUN_ICON_PATH);
		TreeItem postconditionsChild = TreeEditor.createBranch(tr_Components, 2,
			Messages.DynamicRefactoringTab_Postconditions, RefactoringImages.VALIDATE_ICON_PATH); 
				
		TreeEditor.fillInTreeBranch(preconditions, preconditionsChild, 
				RefactoringImages.CHECK_ICON_PATH); 
		TreeEditor.fillInTreeBranch(actions, actionsChild, 
				RefactoringImages.RUN_ICON_PATH); 
		TreeEditor.fillInTreeBranch(postconditions, postconditionsChild,
				RefactoringImages.VALIDATE_ICON_PATH); 
	}
}