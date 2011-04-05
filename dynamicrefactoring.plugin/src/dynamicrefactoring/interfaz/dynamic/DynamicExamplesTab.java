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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.java2html.Java2HTML;
import com.swtdesigner.SWTResourceManager;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.RefactoringExample;

/**
 * Pesta�a con los ejemplos de una refactorizaci�n.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class DynamicExamplesTab {

	/**
	 * Pesta�a asociada a los ejemplos de la refactorizaci�n.
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
	public DynamicExamplesTab(TabFolder parent,
			DynamicRefactoringDefinition definition) {

		refactoringDefinition = definition;

		final ScrolledComposite scrolledComposite = new ScrolledComposite(
				parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		tab = new TabItem(parent, SWT.NONE);

		tab.setControl(scrolledComposite);

		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		// Pesta�a de Ejemplos.
		final Composite cExamples = new Composite(scrolledComposite, SWT.NONE);
		cExamples.setLocation(0, 0);
		cExamples.setSize(872, 390);
		scrolledComposite.setContent(cExamples);

		// generamos los ficheros java2HTML para ser mostrasdos en la ventana
		generarHTMLS();

		// En caso de existir un solo ejemplo
		if (definition.getExamples().size() == 1) {
			tab.setText(Messages.DynamicExamplesTab_Example);
			final Label nameLabel = new Label(cExamples, SWT.NONE);
			nameLabel.setAlignment(SWT.CENTER);
			nameLabel.setText(Messages.DynamicExamplesTab_ExampleOf + " "
					+ definition.getName());
			nameLabel.setFont(SWTResourceManager
					.getFont("Tahoma", 14, SWT.NONE)); //$NON-NLS-1$
			nameLabel.setBounds(10, 15, 860, 39);

			final Label separator1 = new Label(cExamples, SWT.SEPARATOR
					| SWT.VERTICAL);
			separator1.setBounds(430, 44, 13, 340);

			final Label ejemploAntes = new Label(cExamples, SWT.NONE);
			ejemploAntes.setAlignment(SWT.CENTER);
			ejemploAntes.setText(Messages.DynamicExamplesTab_Before + ": ");
			ejemploAntes.setFont(SWTResourceManager.getFont(
					"Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemploAntes.setBounds(10, 53, 414, 20);

			final Browser ejemplo_antes = new Browser(cExamples, SWT.BORDER);
			ejemplo_antes.setBounds(10, 76, 414, 307);

			final Label ejemploDespues = new Label(cExamples, SWT.NONE);
			ejemploDespues.setAlignment(SWT.CENTER);
			ejemploDespues.setText(Messages.DynamicExamplesTab_After + ": ");
			ejemploDespues.setFont(SWTResourceManager.getFont(
					"Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemploDespues.setBounds(443, 53, 414, 20);

			final Browser ejemplo_despues = new Browser(cExamples, SWT.BORDER);
			ejemplo_despues.setBounds(443, 76, 414, 307);

			try {
				final RefactoringExample firstExample = refactoringDefinition.getExamplesAbsolutePath().get(0);
				ejemplo_antes.setUrl(new File(getExampleHtmlFile(firstExample.getBefore())).toURI()
						.toURL().toString());
				ejemplo_despues.setUrl(new File(getExampleHtmlFile(firstExample.getAfter())).toURI()
						.toURL().toString());
			} catch (MalformedURLException e) {
				throw Throwables.propagate(e);
			}

		} else if (definition.getExamples().size() == 2) {
			tab.setText(Messages.DynamicExamplesTab_Examples);
			final Label nameLabel = new Label(cExamples, SWT.NONE);
			nameLabel.setAlignment(SWT.CENTER);
			nameLabel.setText(Messages.DynamicExamplesTab_ExamplesOf + " "
					+ definition.getName());
			nameLabel.setFont(SWTResourceManager
					.getFont("Tahoma", 14, SWT.NONE)); //$NON-NLS-1$
			nameLabel.setBounds(10, 15, 860, 35);

			final Label separator1 = new Label(cExamples, SWT.SEPARATOR
					| SWT.VERTICAL);
			separator1.setBounds(430, 44, 13, 340);

			final Label ejemplo1 = new Label(cExamples, SWT.NONE);
			ejemplo1.setAlignment(SWT.CENTER);
			ejemplo1.setText(Messages.DynamicExamplesTab_Example + " 1: ");
			ejemplo1.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemplo1.setBounds(10, 47, 414, 20);

			final Label ejemploantes1 = new Label(cExamples, SWT.NONE);
			ejemploantes1.setText(Messages.DynamicExamplesTab_Before + ": ");
			ejemploantes1.setFont(SWTResourceManager.getFont(
					"Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemploantes1.setBounds(10, 67, 414, 15);

			final Browser ejemplo_antes1 = new Browser(cExamples, SWT.BORDER);
			ejemplo_antes1.setBounds(10, 87, 414, 135);

			final Label ejemplodespues1 = new Label(cExamples, SWT.NONE);
			ejemplodespues1.setText(Messages.DynamicExamplesTab_After + ": ");
			ejemplodespues1.setFont(SWTResourceManager.getFont(
					"Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemplodespues1.setBounds(10, 228, 414, 15);

			final Browser ejemplo_despues1 = new Browser(cExamples, SWT.BORDER);
			ejemplo_despues1.setBounds(10, 248, 414, 135);

			final Label ejemplo2 = new Label(cExamples, SWT.NONE);
			ejemplo2.setAlignment(SWT.CENTER);
			ejemplo2.setText(Messages.DynamicExamplesTab_Example + " 2: ");
			ejemplo2.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemplo2.setBounds(443, 47, 414, 20);

			final Label ejemploantes2 = new Label(cExamples, SWT.NONE);
			ejemploantes2.setText(Messages.DynamicExamplesTab_Before + ": ");
			ejemploantes2.setFont(SWTResourceManager.getFont(
					"Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemploantes2.setBounds(443, 67, 414, 15);

			final Browser ejemplo_antes2 = new Browser(cExamples, SWT.BORDER);
			ejemplo_antes2.setBounds(443, 87, 414, 135);

			final Label ejemplodespues2 = new Label(cExamples, SWT.NONE);
			ejemplodespues2.setText(Messages.DynamicExamplesTab_After + ": ");
			ejemplodespues2.setFont(SWTResourceManager.getFont(
					"Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemplodespues2.setBounds(443, 228, 414, 15);

			final Browser ejemplo_despues2 = new Browser(cExamples, SWT.BORDER);
			ejemplo_despues2.setBounds(443, 248, 414, 135);

			try {
				final RefactoringExample firstExample = refactoringDefinition.getExamplesAbsolutePath().get(0);
				final RefactoringExample secondExample = refactoringDefinition.getExamplesAbsolutePath().get(0);
				ejemplo_antes1.setUrl(new File(getExampleHtmlFile(firstExample.getBefore())).toURI()
						.toURL().toString());
				ejemplo_despues1.setUrl(new File(getExampleHtmlFile(firstExample.getAfter())).toURI()
						.toURL().toString());
				ejemplo_antes2.setUrl(new File(getExampleHtmlFile(secondExample.getBefore())).toURI()
						.toURL().toString());
				ejemplo_despues2.setUrl(new File(getExampleHtmlFile(secondExample.getAfter())).toURI()
						.toURL().toString());
			} catch (MalformedURLException e) {
				throw Throwables.propagate(e);
			}
		}

	}

	
	/**
	 * Obtiene la ruta absoluta del fichero de ejemplo en formato html
	 * dada la ruta absoluta del fichero de ejemplo con extension .txt.
	 * 
	 * @param exampleFile ruta absoluta del fichero de ejemplo .txt
	 * @return ruta absoluta del fichero de ejemplo html
	 */
	private String getExampleHtmlFile(final String exampleFile) {
		return exampleFile.replace(".txt", ".java") + ".html";
	}
	
	/**
	 * Copia los ficheros des ejemplo modificando su nombre para darles
	 * otra extension.
	 */
	private void copyExampleFilesWithOtherExtension( String toExtension) {
		for (RefactoringExample ejemplo : refactoringDefinition.getExamplesAbsolutePath()) {
			if(!FilenameUtils.getExtension(ejemplo.getBefore()).equals("java")){
				copyFileWithOtherExtension(ejemplo.getBefore(), toExtension);
			}
			if(!FilenameUtils.getExtension(ejemplo.getAfter()).equals("java")){
				copyFileWithOtherExtension(ejemplo.getAfter(), toExtension);
			}
		}
	}

	/**
	 * Copia el fichero cambiando su extension a la pasada.
	 * 
	 * @param file fichero a renombrar
	 * @param toExtension extension destino
	 */
	private void copyFileWithOtherExtension(String file, String toExtension) {
		Preconditions.checkArgument(new File(file).exists(), String.format("The source file %s doesn't exist.", file));
		try {
			FileUtils.copyFile(new File(file), new File(file.replace("." + FilenameUtils.getExtension(file), toExtension)));
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}

	

	/**
	 * Genera htmls mediante java2html para visualizar los ejemplos de la
	 * refactorizaci�n en un navegador html.
	 */
	private void generarHTMLS() {
		// directorio de la refactorizacion
		String dirRefactoring = new File(refactoringDefinition.getExamplesAbsolutePath().get(0).getAfter()).getParent();

		// Cambiamos de extensi�n los ficheros .txt por .java para que puedan
		// ser interpretados
		// por java2HTML
		copyExampleFilesWithOtherExtension(".java");

		try {
			// redirijo la consola a un fichero .txt para que no salga las
			// trazas de la
			// biblioteca java2html.
			PrintStream out = new PrintStream(new FileOutputStream(
					dirRefactoring + File.separator + "consola.txt"));
			System.setOut(out);

			Java2HTML java2html = new Java2HTML();
			String[] dir = new String[1];
			dir[0] = dirRefactoring;
			java2html.setJavaDirectorySource(dir);
			java2html.setDestination(dirRefactoring);
			java2html.buildJava2HTML();

			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * Elimina todos los ficheros del directorio pasado que tengan
	 * la extension dada.
	 * 
	 * @param dirRefactoring directorio del que se eliminaran los ficheros
	 * @param extension extension de los ficheros
	 */
	private void deleteAllFilesFromDirWithExtension(String dirRefactoring, String extension) {
		for(String file : new File(dirRefactoring).list(FileFilterUtils.suffixFileFilter(extension))){
			FileUtils.deleteQuietly(new File(file));
		}
	}
}