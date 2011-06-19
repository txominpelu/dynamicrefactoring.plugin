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

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import dynamicrefactoring.RefactoringImages;
import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringsCatalog;

/**
 * Proporciona la funcionalidad común a las ventanas que permiten seleccionar una
 * de las refactorizaciones dinámicas disponibles para su edición o eliminación.
 * 
 * <p>Proporciona un listado de refactorizaciones dinámicas disponibles, así como
 * un breve resumen con las características fundamentales de cada una de ellas.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class SelectDynamicRefactoringWindow extends DynamicRefactoringList {
	
	/**
	 * Check que permite filtrar la lista de refactorizaciones para que 
	 * únicamente se muestren las propias del usuario.
	 */
	private Button filterButton;
	
	/**
	 * Filtro para que únicamente las refactorizaciones del
	 * usuario sean visibles en la lista de refactorizaciones.
	 */
	protected RefactoringListFilter filter;
	
	/**
	 * Campo de texto en que se muestra la motivación de la refactorización
	 * seleccionada en un momento dado sobre la lista de refactorizaciones
	 * disponibles.
	 */
	private Text t_Motivation;
	
	/**
	 * Campo de texto en que se muestra la descripción de la refactorización
	 * seleccionada en un momento dado sobre la lista de refactorizaciones
	 * disponibles.
	 */
	private Text t_Description;
	
	/**
	 * <i>Listener</i> que permite mostrar la imagen sobre el área de imagen.
	 */
	private PaintListener listener;
	
	/**
	 * <i>Listeners</i> que manejan la funcionalidad de <i>scroll</i> del área de
	 * imagen cuando la imagen asociada a la refactorización seleccionada supera
	 * el tamaño del área disponible.
	 */
	private Listener[] scrollListeners;
	
	/**
	 * área en que se muestra la imagen asociada a la refactorización.
	 */
	private Canvas cv_Image ;
		
	/**
	 * Crea la ventana de diálogo.
	 * 
	 * @param parentShell la <i>shell</i> padre de esta ventana de diálogo.
	 * @param refactCatalog catalogo de refactorizaciones
	 */
	public SelectDynamicRefactoringWindow(Shell parentShell, RefactoringsCatalog refactCatalog) {
		super(parentShell, refactCatalog);

		scrollListeners = new Listener[2];
	}

	/**
	 * Crea el contenido de la ventana de diálogo.
	 * 
	 * @param parent el componente padre del diálogo.
	 * 
	 * @return el control del área de diálogo.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(694, 410));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		composite.setLayout(gridLayout_1);

		final Group gr_RefList = new Group(composite, SWT.NONE);
		gr_RefList.setLayout(new GridLayout());
		gr_RefList.setText(Messages.SelectDynamicRefactoringWindow_AvailableRefactorings);
		GridData gd=new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gr_RefList.setLayoutData(gd);
		
		Object[] messageArgs = {getOperation()};
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.SelectDynamicRefactoringWindow_SelectRefactoring);
		availableRefListViewer = new TableViewer(gr_RefList, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
		availableRefListViewer.setLabelProvider(new RefactoringListLabelProvider());
		availableRefListViewer.setContentProvider(new ArrayContentProvider());
		availableRefListViewer.setSorter(new RefactoringListSorter());
		availableRefListViewer.getTable().setToolTipText(formatter.format(messageArgs));
		availableRefListViewer.getTable().addSelectionListener(new RefactoringSelectionListener());
		availableRefListViewer.getControl().setLayoutData(gd);
		
		filter=new RefactoringListFilter();
		
		if(getOperation()==Messages.SelectForEditingWindow_EditLower){
			filterButton = new Button(gr_RefList, SWT.CHECK);
			filterButton.setText(Messages.SelectDynamicRefactoringWindow_Filter);
			filterButton.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		        if (((Button) event.widget).getSelection()){
		        	availableRefListViewer.addFilter(filter);
		        	//si al aplicar el filtro no hay ningun elemento seleccionado en la lista
		        	//deshabilitamos el boton de Copy
		        	if(availableRefListViewer.getTable().getSelectionIndex()<0){
		        		getButton(IDialogConstants.CLIENT_ID).setEnabled(false);
		        	}
		        }else{
		        	availableRefListViewer.removeFilter(filter);
		        }
		      }
		    });
		}else{
			availableRefListViewer.addFilter(filter);
		}
		
		final Group refactoringSummaryGroup = new Group(composite, SWT.NONE);
		refactoringSummaryGroup.setText(Messages.SelectDynamicRefactoringWindow_Summary);
		final GridData gd_refactoringSummaryGroup = new GridData(475, 390);
		refactoringSummaryGroup.setLayoutData(gd_refactoringSummaryGroup);
		refactoringSummaryGroup.setLayout(new GridLayout());

		final Label lb_Description = new Label(refactoringSummaryGroup, SWT.CENTER);
		final GridData gd_lb_Description = new GridData(464, SWT.DEFAULT);
		lb_Description.setLayoutData(gd_lb_Description);
		lb_Description.setText(Messages.SelectDynamicRefactoringWindow_Description);

		t_Description = new Text(refactoringSummaryGroup,
			SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		t_Description.setEditable(false);
		final GridData gd_t_Description = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_t_Description.heightHint = 32;
		t_Description.setLayoutData(gd_t_Description);

		final Label lb_Motivation = new Label(refactoringSummaryGroup, SWT.CENTER);
		final GridData gd_lb_Motivation = new GridData(465, SWT.DEFAULT);
		lb_Motivation.setLayoutData(gd_lb_Motivation);
		lb_Motivation.setText(Messages.SelectDynamicRefactoringWindow_Motivation);

		t_Motivation = new Text(refactoringSummaryGroup,
			SWT.BORDER | SWT.MULTI | SWT.WRAP  | SWT.V_SCROLL);
		t_Motivation.setEditable(false);
		final GridData gd_t_Motivation = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_t_Motivation.heightHint = 32;
		t_Motivation.setLayoutData(gd_t_Motivation);

		final Group gr_Image = new Group(refactoringSummaryGroup, SWT.NONE);
		gr_Image.setLayoutData(new GridData(SWT.DEFAULT, 248));
		gr_Image.setText(Messages.SelectDynamicRefactoringWindow_Image);
		gr_Image.setLocation(0, 0);

		cv_Image = new Canvas(gr_Image, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		cv_Image.setRedraw(true);
		cv_Image.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		cv_Image.setBounds(10, 21, 442, 233);
		gr_Image.setSize(462, 254);
		
		fillInRefactoringList();
		
		return container;
	}
	
	/**
	 * Crea el contenido de la barra de botones.
	 * 
	 * @param parent el componente padre de los botones añadidos.
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent){
		createOtherButtons(parent);
		createButton(parent, IDialogConstants.CANCEL_ID,ButtonTextProvider.getCancelText(), false);
	}

	/**
	 * Crea el resto de botones necesarios en el dialogo que permiten 
	 * lanzar las distintas funcionalidades del dialogo.
	 * Entre ellos se encuentra el botón OK.
	 * 
	 * @param parent el componente padre del botón.
	 */
	protected void createOtherButtons(Composite parent){
		createOKButton(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}
	
	/**
	 * Crea el botón que permite lanzar la funcionalidad real para la que
	 * se aplica la ventana (editar o borrar una refactorización).
	 * 
	 * @param parent el componente padre del botón.
	 */
	protected abstract void createOKButton(Composite parent);

	/**
	 * Obtiene el verbo asociado a la acción que permite iniciar la ventana
	 * de diálogo sobre la refactorización seleccionada.
	 * 
	 * @return el verbo asociado a la acción que permite iniciar la ventana
	 * de diálogo sobre la refactorización seleccionada.
	 */
	protected abstract String getOperation();
	
	/**
	 * Devuelve el tamaño inicial de la ventana de diálogo.
	 * 
	 * @return el tamaño inicial de la ventana de diálogo.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(712, 500);
	}
	
	/**
	 * Prepara la ventana para su apertura.
	 * 
	 * @param newShell la <i>shell</i> que se ha de configurar.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.SelectDynamicRefactoringWindow_RefactoringSelection);
		newShell.setImage(RefactoringImages.getRefIcon());
	}

	/**
	 * Completa los datos resumen de una refactorización en el panel derecho.
	 * 
	 * @param refactoring la refactorización dinámica cuyos datos se mostrarán en
	 * el panel resumen derecho.
	 */
	private void fillInData(DynamicRefactoringDefinition refactoring){
		if (refactoring != null){
			if (refactoring.getDescription() != null)
				t_Description.setText(refactoring.getDescription());
			if (refactoring.getMotivation() != null)
				t_Motivation.setText(refactoring.getMotivation());
			if (refactoring.getImage() != null && ! refactoring.getImage().equals("")){ //$NON-NLS-1$
				String path = refactoring.getImageAbsolutePath();
				ImageLoader loader = new ImageLoader();
				
				final Image image = new Image(cv_Image.getDisplay(), loader.load(path)[0]);
				
				// Se restaura el scroll horizontal.
				final Point origin = new Point (0, 0);
				final ScrollBar hBar = cv_Image.getHorizontalBar();
				if (scrollListeners[0] != null)
					hBar.removeListener(SWT.Selection, scrollListeners[0]);
				scrollListeners[0] = new Listener(){
					public void handleEvent(Event e){
						int hSelection = hBar.getSelection();
						int destX = -hSelection - origin.x;
						cv_Image.scroll(destX, 0, 0, 0, image.getBounds().width, 
							image.getBounds().height, false);
						origin.x = -hSelection;						
					}
				};
				hBar.addListener(SWT.Selection, scrollListeners[0]);
				
				// Se restaura el scroll vertical.
				final ScrollBar vBar = cv_Image.getVerticalBar ();
				if (scrollListeners[1] != null)
					vBar.removeListener(SWT.Selection, scrollListeners[1]);
				scrollListeners[1] = new Listener() {
					public void handleEvent(Event e){
						int vSelection = vBar.getSelection();
						int destY = -vSelection - origin.y;
						cv_Image.scroll(0, destY, 0, 0, image.getBounds().width,
							image.getBounds().height, false);
						origin.y = -vSelection;
					}
				};
				vBar.addListener(SWT.Selection, scrollListeners[1]);
						
				// Se elimina el listener anterior.
				if (listener != null)
					cv_Image.removePaintListener(listener);
				// Se añade el nuevo.
				Rectangle rect = image.getBounds();
				Rectangle client = cv_Image.getClientArea();
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
		        cv_Image.redraw();
				
				listener = new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(image, origin.x, origin.y);
					}
				};
								
				cv_Image.addPaintListener(listener);
									
				cv_Image.redraw();

				hBar.setSelection(0);
				hBar.notifyListeners(SWT.Selection, new Event());
				vBar.setSelection(0);
				vBar.notifyListeners(SWT.Selection, new Event());
			}
			// Si no hay imagen asociada.
			else {
				Rectangle bounds = cv_Image.getBounds();
				final Image image = new Image(cv_Image.getDisplay(), new Rectangle(
					0, 0, bounds.width, bounds.height));
				if (listener != null)
					cv_Image.removePaintListener(listener);
				listener = new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(image, 0, 0);
					}
				};								
				cv_Image.addPaintListener(listener);
				cv_Image.redraw();
			}
		}
	}

	
	/**
	 * Actualiza el panel derecho de información para mostrar un resumen con la 
	 * información apropiada acerca de la refactorización seleccionada en la lista 
	 * del refactorizaciones dinámicas disponibles.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class RefactoringSelectionListener implements SelectionListener {

		/**
		 * Constructor.
		 */
		public RefactoringSelectionListener() {
			super();
		}
		
		/**
		 * Recibe una notificación de que un elemento de la lista de 
		 * refactorizaciones dinámicas disponibles ha sido seleccionado.
		 * 
		 * <p>Inicia las acciones que sean necesarias para actualizar la 
		 * información mostrada en el panel derecho acerca de la refactorizacion
		 * seleccionada.</p>
		 * 
		 * @param e el evento de selección disparado en la ventana.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			// Solo se muestra un resumen si se selecciona una única refactorización.
			Table availableList=availableRefListViewer.getTable();
			if(availableList.getSelectionCount() == 1){
				Object obj = availableList.getSelection()[0].getData();
				if(obj instanceof DynamicRefactoringDefinition){
					DynamicRefactoringDefinition refactoring =(DynamicRefactoringDefinition)obj;
					fillInData(refactoring);
					getButton(IDialogConstants.OK_ID).setEnabled(refactoring.isEditable());	
					if(getButton(IDialogConstants.CLIENT_ID)!=null)
						getButton(IDialogConstants.CLIENT_ID).setEnabled(true);
				}
			}else{ // Si no hay ningún elemento seleccionado.
				getButton(IDialogConstants.OK_ID).setEnabled(false);
				if(getButton(IDialogConstants.CLIENT_ID)!=null)
					getButton(IDialogConstants.CLIENT_ID).setEnabled(false);
			}
		}
		
		/**
		 * Comportamiento para la selección por defecto.
		 * 
		 * @see SelectionListener#widgetDefaultSelected(SelectionEvent)
		 * 
		 * @param e evento de selección.
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
}