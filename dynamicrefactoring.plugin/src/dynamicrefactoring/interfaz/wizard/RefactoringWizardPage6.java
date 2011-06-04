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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringExample;

/**
 * Sexta página del asistente de creación o edición de refactorizaciones.
 * 
 * <p>
 * Permite asociar hasta un máximo de dos ejemplos concretos a la
 * refactorización. Cada uno de los ejemplos vendrá definido por un fichero con
 * el estado del sistema antes de la refactorización, y otro con su estado
 * posterior, una vez ejecutada.
 * </p>
 * 
 * <p>
 * Los ficheros aceptados pueden ser ficheros de texto <code>.TXT</code> o
 * directamente ficheros fuente Java.
 * </p>
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringWizardPage6 extends WizardPage {

	/**
	 * Campo de texto con la ruta del fichero que contiene el estado del primer
	 * ejemplo antes de la refactorización.
	 */
	private Text textBefore1;

	/**
	 * Campo de texto con la ruta del fichero que contiene el estado del primer
	 * ejemplo después de la refactorización.
	 */
	private Text textAfter1;

	/**
	 * Campo de texto con la ruta del fichero que contiene el estado del segundo
	 * ejemplo antes de la refactorización.
	 */
	private Text textBefore2;

	/**
	 * Campo de texto con la ruta del fichero que contiene el estado del segundo
	 * ejemplo después de la refactorización.
	 */
	private Text textAfter2;

	/**
	 * Refactorización configurada a través del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorización) o modificada (si se
	 * está editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;

	/**
	 * última ruta en la que se seleccionó un archivo de ejemplo.
	 */
	private String lastSelectionPath = null;

	private RefactoringExampleModel model;

	/**
	 * Constructor.
	 * 
	 * @param refactoring
	 *            refactorización que se está editando o <code>null
	 * </code> si se está creando una nueva.
	 */
	public RefactoringWizardPage6(DynamicRefactoringDefinition refactoring) {
		super("Wizard page"); //$NON-NLS-1$
		setDescription(Messages.RefactoringWizardPage6_DescriptionInputElementsExamples);

		this.refactoring = refactoring;
	}

	/**
	 * Hace visible o invisible la página del asistente.
	 * 
	 * @param visible
	 *            si la página se debe hacer visible o no.
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			Object[] messageArgs = { ((RefactoringWizard) getWizard())
					.getOperationAsString() };
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter
					.applyPattern(Messages.RefactoringWizardPage6_DynamicRefactoring);

			setTitle(formatter.format(messageArgs) + " (" + //$NON-NLS-1$
					Messages.RefactoringWizardPage6_Step + ")"); //$NON-NLS-1$
		}
		super.setVisible(visible);
	}

	/**
	 * Crea el contenido de la página del asistente.
	 * 
	 * @param parent
	 *            el elemento padre de esta página del asistente.
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());

		setControl(container);

		final Composite composite = new Composite(container, SWT.NONE);
		final FormData fd_composite = new FormData();
		fd_composite.left = new FormAttachment(0, 5);
		fd_composite.top = new FormAttachment(0, 5);
		fd_composite.bottom = new FormAttachment(100, -5);
		fd_composite.right = new FormAttachment(100, -5);
		composite.setLayoutData(fd_composite);

		final Label itsPossibleToLabel = new Label(composite, SWT.NONE);
		itsPossibleToLabel.setText(Messages.RefactoringWizardPage6_AddExamples);
		itsPossibleToLabel.setBounds(106, 26, 431, 13);

		final Group example1Group = new Group(composite, SWT.NONE);
		example1Group.setText(Messages.RefactoringWizardPage6_Example1);
		example1Group.setBounds(23, 55, 602, 117);

		final Label beforeRefactoringLabel = new Label(example1Group, SWT.NONE);
		beforeRefactoringLabel
				.setText(Messages.RefactoringWizardPage6_BeforeRefactoring);
		beforeRefactoringLabel.setBounds(10, 29, 116, 13);

		final Label afterRefactoringLabel = new Label(example1Group, SWT.NONE);
		afterRefactoringLabel
				.setText(Messages.RefactoringWizardPage6_AfterRefactoring);
		afterRefactoringLabel.setBounds(10, 75, 116, 13);

		textBefore1 = new Text(example1Group, SWT.BORDER);
		textBefore1.setBounds(132, 26, 420, 25);
		textBefore1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		final Button bt_examineBefore1 = new Button(example1Group, SWT.NONE);
		bt_examineBefore1.setText("..."); //$NON-NLS-1$
		bt_examineBefore1.setBounds(558, 29, 30, 18);
		bt_examineBefore1.addSelectionListener(new ExampleChooserAction(
				textBefore1));

		textAfter1 = new Text(example1Group, SWT.BORDER);
		textAfter1.setBounds(132, 72, 420, 25);
		textAfter1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		final Button btExamineAfter1 = new Button(example1Group, SWT.NONE);
		btExamineAfter1.setText("..."); //$NON-NLS-1$
		btExamineAfter1.setBounds(558, 75, 30, 18);
		btExamineAfter1.addSelectionListener(new ExampleChooserAction(
				textAfter1));

		final Group example1Group_1 = new Group(composite, SWT.NONE);
		example1Group_1.setBounds(23, 195, 602, 117);
		example1Group_1.setText(Messages.RefactoringWizardPage6_Example2);

		final Label beforeRefactoringLabel_1 = new Label(example1Group_1,
				SWT.NONE);
		beforeRefactoringLabel_1.setBounds(10, 29, 118, 13);
		beforeRefactoringLabel_1
				.setText(Messages.RefactoringWizardPage6_BeforeRefactoring);

		final Label afterRefactoringLabel_1 = new Label(example1Group_1,
				SWT.NONE);
		afterRefactoringLabel_1.setBounds(10, 75, 118, 13);
		afterRefactoringLabel_1
				.setText(Messages.RefactoringWizardPage6_AfterRefactoring);

		textBefore2 = new Text(example1Group_1, SWT.BORDER);
		textBefore2.setBounds(134, 26, 418, 25);
		textBefore2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		final Button bt_examineBefore2 = new Button(example1Group_1, SWT.NONE);
		bt_examineBefore2.setBounds(558, 29, 30, 18);
		bt_examineBefore2.setText("..."); //$NON-NLS-1$
		bt_examineBefore2.addSelectionListener(new ExampleChooserAction(
				textBefore2));

		textAfter2 = new Text(example1Group_1, SWT.BORDER);
		textAfter2.setBounds(134, 72, 418, 25);
		textAfter2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		final Button bt_examineAfter2 = new Button(example1Group_1, SWT.NONE);
		bt_examineAfter2.setBounds(558, 75, 30, 18);
		bt_examineAfter2.setText("..."); //$NON-NLS-1$
		bt_examineAfter2.addSelectionListener(new ExampleChooserAction(
				textAfter2));

		model = new RefactoringExampleModel();
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(SWTObservables.observeText(textBefore1, SWT.Modify),
				BeansObservables.observeValue(model, "beforeExample1"), null,
				null);
		dbc.bindValue(SWTObservables.observeText(textBefore2, SWT.Modify),
				BeansObservables.observeValue(model, "beforeExample2"), null,
				null);
		dbc.bindValue(SWTObservables.observeText(textAfter1, SWT.Modify),
				BeansObservables.observeValue(model, "afterExample1"), null,
				null);
		dbc.bindValue(SWTObservables.observeText(textAfter2, SWT.Modify),
				BeansObservables.observeValue(model, "afterExample2"), null,
				null);

		if (refactoring != null) {
			fillInRefactoringData();
			if (refactoring.getExamplesAbsolutePath().size() > 0) {
				model.setBeforeExample1(refactoring.getExamplesAbsolutePath()
						.get(0).getBefore());
				model.setAfterExample1(refactoring.getExamplesAbsolutePath()
						.get(0).getAfter());
			}
			if (refactoring.getExamplesAbsolutePath().size() > 1) {
				model.setBeforeExample2(refactoring.getExamplesAbsolutePath()
						.get(1).getBefore());

				model.setAfterExample2(refactoring.getExamplesAbsolutePath()
						.get(1).getAfter());
			}
		}
	}

	/**
	 * Puebla los campos del formulario del asistente con la información que se
	 * pueda obtener de la refactorización existente que se está editando.
	 */
	private void fillInRefactoringData() {
		if (refactoring.getExamples() != null) {
			List<RefactoringExample> examples = refactoring.getExamples();

			// Se intentan cargar los datos del primer ejemplo.
			if (examples.size() > 0) {
				textBefore1.setText(new File(examples.get(0).getBefore())
						.getName());
				textAfter1.setText(new File(examples.get(0).getAfter())
						.getName());
			}

			// Se intentan cargar los datos del segundo ejemplo.
			if (examples.size() > 1) {
				textBefore2.setText(new File(examples.get(1).getBefore())
						.getName());
				textAfter2.setText(new File(examples.get(1).getAfter())
						.getName());
			}

			dialogChanged();
		}
	}

	/**
	 * Obtiene los ejemplos asociados a la refactorización.
	 * 
	 * @return una lista de arrays de cadenas en que cada array contiene dos
	 *         cadenas: una con la ruta del fichero que contiene el estado del
	 *         ejemplo antes de la refactorización, y otra con la ruta del
	 *         fichero que contiene el estado del ejemplo después de la
	 *         refactorización.
	 */
	public List<RefactoringExample> getExamples() {
		List<RefactoringExample> examples = new ArrayList<RefactoringExample>();
		if (!textBefore1.getText().trim().isEmpty()
				&& !textAfter1.getText().trim().isEmpty()) { //$NON-NLS-1$ //$NON-NLS-2$
			examples.add(new RefactoringExample(model.getBeforeExample1()
					.trim(), model.getAfterExample1().trim()));
		}
		if (!textBefore2.getText().trim().isEmpty()
				&& !textAfter2.getText().trim().isEmpty()) { //$NON-NLS-1$ //$NON-NLS-2$
			examples.add(new RefactoringExample(model.getBeforeExample2()
					.trim(), model.getAfterExample2().trim()));
		}
		return examples;
	}

	/**
	 * Se asegura de que ambos ejemplos están completos o vacíos, es decir, no
	 * se admite la asociación de solo uno de los dos ficheros que componen el
	 * ejemplo.
	 */
	private void dialogChanged() {
		if (isNotValidString(textBefore1) || isNotValidString(textAfter1)
				|| isNotValidString(textBefore2)
				|| isNotValidString(textAfter2)) {
			updateStatus(Messages.RefactoringWizardPage6_MustBeType);
			return;
		}
		if ((textBefore1.getText() != "" && textAfter1.getText() == "") || //$NON-NLS-1$ //$NON-NLS-2$
				(textBefore1.getText() == "" && textAfter1.getText() != "")) { //$NON-NLS-1$ //$NON-NLS-2$
			updateStatus(Messages.RefactoringWizardPage6_FirstIncomplete);
			return;
		}
		if ((textBefore2.getText() != "" && textAfter2.getText() == "") || //$NON-NLS-1$ //$NON-NLS-2$
				(textBefore2.getText() == "" && textAfter2.getText() != "")) { //$NON-NLS-1$ //$NON-NLS-2$
			updateStatus(Messages.RefactoringWizardPage6_SecondIncomplete);
			return;
		}
		updateStatus(null);
	}

	/**
	 * Comprueba si un campo de texto no vacío contiene una cadena terminada en
	 * la extensión <i>.txt</i> o <i>.java</i>.
	 * 
	 * @param field
	 *            campo de texto cuyo contenido se debe verificar.
	 * 
	 * @return <code>true</code> si el campo de texto no está vacío y su
	 *         contenido no termina en la extensión <i>.txt</i> ni <i>.java</i>,
	 *         o está vacío. <code>false</code> si tiene contenido y termina en
	 *         una de dichas extensiones.
	 */
	private boolean isNotValidString(Text field) {
		return (!field.getText().isEmpty()) && //$NON-NLS-1$
				!(field.getText().toLowerCase().endsWith(".txt") || //$NON-NLS-1$
				field.getText().toLowerCase().endsWith(".java")); //$NON-NLS-1$
	}

	/**
	 * Actualiza el estado de la pantalla de diálogo del asistente.
	 * 
	 * @param message
	 *            mensaje asociado al estado actual de la pantalla.
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Implementa el proceso de elección de uno de los ficheros de texto o Java
	 * con el estado del modelo de uno de los ejemplos.
	 * 
	 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
	 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
	 */
	private class ExampleChooserAction implements SelectionListener {

		/**
		 * Plantillas con alguna de las cuales deberán conformar los ficheros
		 * para poder pasar el filtro y poder ser seleccionados.
		 */
		private final String[] extensions = { "*.java", "*.txt" }; //$NON-NLS-1$ //$NON-NLS-2$

		/**
		 * Descripciones breves de las plantillas en {@link #extensions}.
		 */
		private final String[] descriptions = {
				Messages.RefactoringWizardPage6_JavaFile,
				Messages.RefactoringWizardPage6_TextFile };

		/**
		 * Campo de texto en que se guardará la ruta del fichero seleccionado.
		 */
		private Text textField;

		/**
		 * Constructor.
		 * <p>
		 * 
		 * @param textField
		 *            campo de texto en que se guardará la ruta del fichero
		 *            seleccionado.
		 */
		public ExampleChooserAction(Text textField) {
			super();
			this.textField = textField;
		}

		/**
		 * Recibe una notificación de que se ha pulsado el botón que permite
		 * seleccionar un fichero de ejemplo asociado a la refactorización.
		 * 
		 * <p>
		 * Abre una ventana de selección de fichero con un filtro que permite
		 * seleccionar solamente ficheros Java con extensión <i>.java</i> o
		 * ficheros de texto con extensión <i>.txt</i>.
		 * </p>
		 * 
		 * @param e
		 *            el evento de selección disparado.
		 * 
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog chooser = new FileDialog(getShell(), SWT.OPEN);
			chooser.setText(Messages.RefactoringWizardPage6_SelectFile);
			chooser.setFilterExtensions(extensions);
			chooser.setFilterNames(descriptions);

			String openingPath = null;
			if (lastSelectionPath == null)
				openingPath = Platform.getLocation().toOSString()
						+ File.separatorChar + ".."; //$NON-NLS-1$ //$NON-NLS-2$
			else
				openingPath = lastSelectionPath;

			chooser.setFilterPath(openingPath);

			String returnVal = chooser.open();
			if (returnVal != null) {
				textField.setText(returnVal);
				lastSelectionPath = returnVal.substring(0,
						returnVal.lastIndexOf(File.separatorChar)); //$NON-NLS-1$
				dialogChanged();
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

	public static class RefactoringExampleModel {
		private String beforeExample1;
		private String afterExample1;
		private String beforeExample2;
		private String afterExample2;

		/**
		 * @param beforeExample1
		 *            the beforeExample1 to set
		 */
		public void setBeforeExample1(String beforeExample1) {
			this.beforeExample1 = beforeExample1;
		}

		/**
		 * @return the beforeExample1
		 */
		public String getBeforeExample1() {
			return beforeExample1;
		}

		/**
		 * @param afterExample1
		 *            the afterExample1 to set
		 */
		public void setAfterExample1(String afterExample1) {
			this.afterExample1 = afterExample1;
		}

		/**
		 * @return the afterExample1
		 */
		public String getAfterExample1() {
			return afterExample1;
		}

		/**
		 * @param beforeExample2
		 *            the beforeExample2 to set
		 */
		public void setBeforeExample2(String beforeExample2) {
			this.beforeExample2 = beforeExample2;
		}

		/**
		 * @return the beforeExample2
		 */
		public String getBeforeExample2() {
			return beforeExample2;
		}

		/**
		 * @param afterExample2
		 *            the afterExample2 to set
		 */
		public void setAfterExample2(String afterExample2) {
			this.afterExample2 = afterExample2;
		}

		/**
		 * @return the afterExample2
		 */
		public String getAfterExample2() {
			return afterExample2;
		}

		private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
				this);

		public void addPropertyChangeListener(String propertyName,
				PropertyChangeListener listener) {
			changeSupport.addPropertyChangeListener(propertyName, listener);
		}

		public void removePropertyChangeListener(String propertyName,
				PropertyChangeListener listener) {
			changeSupport.removePropertyChangeListener(propertyName, listener);
		}
	}
}