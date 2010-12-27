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
import java.io.PrintStream;
import java.net.MalformedURLException;

import dynamicrefactoring.RefactoringConstants;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;


import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;

import com.java2html.Java2HTML;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.swtdesigner.SWTResourceManager;

/**
 * Pestaña con los ejemplos de una refactorización.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class DynamicExamplesTab {
	
	
	/**
	 * Pestaña asociada a los ejemplos de la refactorización.
	 */
	private TabItem tab;
	
	/**
	 * La definición de la refactorización.
	 */
	protected DynamicRefactoringDefinition refactoringDefinition;
	
	/**
	 * Crea la pestaña con el resumen de la refactorización.
	 * 
	 * @param parent el contenedor de pestañas que contendrá esta pestaña.
	 * @param definition la definición de la refactorización.
	 */
	public DynamicExamplesTab(TabFolder parent,
			DynamicRefactoringDefinition definition){

		refactoringDefinition= definition;
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, 
			SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		tab = new TabItem(parent, SWT.NONE);	
		
		tab.setControl(scrolledComposite);

		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		// Pestaña de Ejemplos.
		final Composite cExamples = new Composite(scrolledComposite, SWT.NONE);
		cExamples.setLocation(0, 0);
		cExamples.setSize(872, 390);
		scrolledComposite.setContent(cExamples);

		//generamos los ficheros java2HTML para ser mostrasdos en la ventana
		generarHTMLS();
		
		//En caso de existir un solo ejemplo
		if(definition.getExamples().size()==1){
			tab.setText(Messages.DynamicExamplesTab_Example);
			final Label nameLabel = new Label(cExamples, SWT.NONE);
			nameLabel.setAlignment(SWT.CENTER);
			nameLabel.setText(Messages.DynamicExamplesTab_ExampleOf + " " + definition.getName());
			nameLabel.setFont(SWTResourceManager.getFont("Tahoma", 14, SWT.NONE)); //$NON-NLS-1$
			nameLabel.setBounds(10, 15, 860, 39);
			
			final Label separator1 = new Label(cExamples, SWT.SEPARATOR | SWT.VERTICAL);
			separator1.setBounds(430, 44 , 13, 340);
			
			final Label ejemploAntes = new Label(cExamples, SWT.NONE);
			ejemploAntes.setAlignment(SWT.CENTER);
			ejemploAntes.setText(Messages.DynamicExamplesTab_Before + ": ");
			ejemploAntes.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemploAntes.setBounds(10, 53, 414, 20);
			
			final Browser ejemplo_antes = new Browser(cExamples,SWT.BORDER);
			ejemplo_antes.setBounds(10,76,414,307);
			
			final Label ejemploDespues = new Label(cExamples, SWT.NONE);
			ejemploDespues.setAlignment(SWT.CENTER);
			ejemploDespues.setText(Messages.DynamicExamplesTab_After + ": ");
			ejemploDespues.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemploDespues.setBounds(443, 53, 414, 20);
			
			final Browser ejemplo_despues = new Browser(cExamples,SWT.BORDER);
			ejemplo_despues.setBounds(443,76,414,307);
			
			try{
				ejemplo_antes.setUrl(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR 
						+ File.separator + definition.getName() 
						+ File.separator + definition.getExamples().get(0)[0].replace(".txt",".java") 
							+ ".html" ).toURI().toURL().toString());
				ejemplo_despues.setUrl(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR 
						+ File.separator + definition.getName() 
						+ File.separator + definition.getExamples().get(0)[1].replace(".txt",".java") 
							+ ".html" ).toURI().toURL().toString());
			}catch(MalformedURLException e){}
			
		}else if(definition.getExamples().size()==2){
			tab.setText(Messages.DynamicExamplesTab_Examples);
			final Label nameLabel = new Label(cExamples, SWT.NONE);
			nameLabel.setAlignment(SWT.CENTER);
			nameLabel.setText(Messages.DynamicExamplesTab_ExamplesOf + " "+ definition.getName());
			nameLabel.setFont(SWTResourceManager.getFont("Tahoma", 14, SWT.NONE)); //$NON-NLS-1$
			nameLabel.setBounds(10, 15, 860, 35);
			
			final Label separator1 = new Label(cExamples, SWT.SEPARATOR | SWT.VERTICAL);
			separator1.setBounds(430, 44 , 13, 340);
			
			final Label ejemplo1 = new Label(cExamples, SWT.NONE);
			ejemplo1.setAlignment(SWT.CENTER);
			ejemplo1.setText(Messages.DynamicExamplesTab_Example + " 1: ");
			ejemplo1.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemplo1.setBounds(10, 47, 414, 20);
			
			final Label ejemploantes1 = new Label(cExamples, SWT.NONE);
			ejemploantes1.setText(Messages.DynamicExamplesTab_Before + ": ");
			ejemploantes1.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemploantes1.setBounds(10, 67, 414, 15);
			
			final Browser ejemplo_antes1 = new Browser(cExamples,SWT.BORDER);
			ejemplo_antes1.setBounds(10,87,414,135);
			
			final Label ejemplodespues1 = new Label(cExamples, SWT.NONE);
			ejemplodespues1.setText(Messages.DynamicExamplesTab_After + ": ");
			ejemplodespues1.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemplodespues1.setBounds(10, 228, 414, 15);
			
			final Browser ejemplo_despues1 = new Browser(cExamples,SWT.BORDER);
			ejemplo_despues1.setBounds(10,248,414,135);
			
			final Label ejemplo2 = new Label(cExamples, SWT.NONE);
			ejemplo2.setAlignment(SWT.CENTER);
			ejemplo2.setText(Messages.DynamicExamplesTab_Example + " 2: ");
			ejemplo2.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.BOLD)); //$NON-NLS-1$
			ejemplo2.setBounds(443, 47, 414, 20);
			
			final Label ejemploantes2 = new Label(cExamples, SWT.NONE);
			ejemploantes2.setText(Messages.DynamicExamplesTab_Before + ": ");
			ejemploantes2.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemploantes2.setBounds(443, 67, 414, 15);
			
			final Browser ejemplo_antes2 = new Browser(cExamples,SWT.BORDER);
			ejemplo_antes2.setBounds(443,87,414,135);
			
			final Label ejemplodespues2 = new Label(cExamples, SWT.NONE);
			ejemplodespues2.setText(Messages.DynamicExamplesTab_After + ": ");
			ejemplodespues2.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.BOLD)); //$NON-NLS-1$
			ejemplodespues2.setBounds(443, 228, 414, 15);
			
			final Browser ejemplo_despues2 = new Browser(cExamples,SWT.BORDER);
			ejemplo_despues2.setBounds(443,248,414,135);
			
			try{
				ejemplo_antes1.setUrl(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR 
						+ File.separator + definition.getName() 
						+ File.separator + definition.getExamples().get(0)[0].replace(".txt",".java") 
							+ ".html" ).toURI().toURL().toString());
				ejemplo_despues1.setUrl(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR 
						+ File.separator + definition.getName() 
						+ File.separator + definition.getExamples().get(0)[1].replace(".txt",".java") 
							+ ".html" ).toURI().toURL().toString());
				ejemplo_antes2.setUrl(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR 
						+ File.separator + definition.getName() 
						+ File.separator + definition.getExamples().get(1)[0].replace(".txt",".java") 
							+ ".html" ).toURI().toURL().toString());
				ejemplo_despues2.setUrl(new File(RefactoringConstants.DYNAMIC_REFACTORING_DIR 
						+ File.separator + definition.getName() 
						+ File.separator + definition.getExamples().get(1)[1].replace(".txt",".java") 
							+ ".html" ).toURI().toURL().toString());
			}catch(MalformedURLException e){}
		}

	}
	
	/**
	 * Renombra los ficheros de ejemplo de la refactorización acabados en 
	 * <code>.txt</code> a ficheros acabados en <code>.java</code> para que puedan 
	 * ser interpretados por java2html.
	 */
	private void renameTxtToJava(){
		//directorio de la refactorizacion
		String dirRefactoring = RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separator 
			+ refactoringDefinition.getName();
		
		for(String[] ejemplos : refactoringDefinition.getExamples()){
			for(int i=0; i<ejemplos.length; i++){
				if(ejemplos[i].endsWith(".txt"))
					new File(dirRefactoring + File.separator + ejemplos[i]).renameTo
						(new File(dirRefactoring + File.separator + ejemplos[i].replace("txt","java")));
			}
		}
	}
	
	/**
	 * Reestable la extensión de los ejemplos <code>.txt</code> modificados para generar
	 * sus htmls asociados.
	 */
	private void renameJavaToTxt(){
		//directorio de la refactorizacion
		String dirRefactoring = RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separator 
			+ refactoringDefinition.getName();
		
		for(String[] ejemplos : refactoringDefinition.getExamples()){
			for(int i=0; i<ejemplos.length; i++){
				if(ejemplos[i].endsWith(".txt")){
					new File(dirRefactoring + File.separator + ejemplos[i].replace(".txt",".java")).renameTo
						(new File(dirRefactoring + File.separator + ejemplos[i]));
				}
			}
		}
	}
	
	/**
	 * Genera htmls mediante java2html para visualizar los ejemplos de la 
	 * refactorización en un navegador html.
	 */
	private void generarHTMLS(){
		//directorio de la refactorizacion
		String dirRefactoring = RefactoringConstants.DYNAMIC_REFACTORING_DIR + File.separator 
			+ refactoringDefinition.getName();
			
		
		//Cambiamos de extensión los ficheros .txt por .java para que puedan ser interpretados
		//por java2HTML
		renameTxtToJava();
		
		try{
			//redirijo la consola a un fichero .txt para que no salga las trazas de la 
			//biblioteca java2html.
			PrintStream out = new PrintStream(new FileOutputStream(dirRefactoring 
					+ File.separator + "consola.txt"));
			System.setOut(out);
		
		     Java2HTML java2html= new Java2HTML();
		     String[] dir = new String[1];
		     dir[0]=dirRefactoring;
		     java2html.setJavaDirectorySource(dir);
		     java2html.setDestination(dirRefactoring);
		     java2html.buildJava2HTML();
		     
		     out.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		//Volvemos a dejar los ficheros como estaban.
		renameJavaToTxt();		
	}
}