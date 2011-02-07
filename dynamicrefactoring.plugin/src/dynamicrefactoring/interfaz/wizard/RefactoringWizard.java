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

import dynamicrefactoring.util.ScopeLimitedLister;
import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.writer.JDOMXMLRefactoringWriterFactory;
import dynamicrefactoring.writer.JDOMXMLRefactoringWriterImp;
import dynamicrefactoring.writer.XMLRefactoringWriter;
import dynamicrefactoring.writer.XMLRefactoringWriterException;
import dynamicrefactoring.writer.XMLRefactoringWriterFactory;

import dynamicrefactoring.domain.DynamicRefactoringDefinition;
import dynamicrefactoring.domain.Scope;
import dynamicrefactoring.domain.Scope;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.*;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;

import org.eclipse.ui.*;

/**
 * Proporciona un asistente de Eclipse que permite crear una nueva 
 * refactorizaci�n din�mica o modificar una ya existente.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringWizard extends Wizard implements INewWizard {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringWizard.class);
	
	/**
	 * Indica que el asistente debe crear una refactorizaci�n nueval al terminar.
	 */
	protected static final int CREATE = 0;
	
	/**
	 * Indica que el asistente debe modificar una refactorizaci�n existente al
	 * terminar.
	 */
	private static final int EDIT = 1;
	
	/**
	 * Refactorizaci�n configurada a trav�s del asistente y que debe ser creada
	 * finalmente (si se trata de una nueva refactorizaci�n) o modificada (si se
	 * est� editando una ya existente).
	 */
	private DynamicRefactoringDefinition refactoring = null;
	
	/**
	 * Indica el tipo de operaci�n que debe realizar el asistente cuando el
	 * usuario pulse el bot�n "Finalizar".
	 * 
	 * <p>Deber� ser una de {@value #CREATE} o {@value #EDIT}.</p>
	 */
	private int operation;
	
	/**
	 * Nombre original de la refactorizaci�n que se edita.
	 */
	private String originalName;
	
	/**
	 * �mbito original de la clase que se edita.
	 */
	private Scope originalScope;
	
	/**
	 * Primera p�gina del asistente.
	 */
	private RefactoringWizardPage1 pageA;
	
	/**
	 * Segunda p�gina del asistente.
	 */
	private RefactoringWizardPage2 pageB;
	
	/**
	 * Tercera p�gina del asistente.
	 */
	private RefactoringWizardPage3 pageC;
	
	/**
	 * Cuarta p�gina del asistente.
	 */
	private RefactoringWizardPage4 pageD;
	
	/**
	 * Quinta p�gina del asistente.
	 */
	private RefactoringWizardPage5 pageE;
	
	/**
	 * Sexta p�gina del asistente.
	 */
	private RefactoringWizardPage6 pageF;
	
	/**
	 * S�ptima p�gina del asistente.
	 */
	private RefactoringWizardPage7 pageG;
	
	/**
	 * Constructor.
	 * 
	 * @param refactoring refactorizaci�n que se desea editar o <code>null
	 * </code> si se desea crear una nueva.
	 */
	public RefactoringWizard(DynamicRefactoringDefinition refactoring) {
		super();
		setNeedsProgressMonitor(true);
		
		setDefaultPageImageDescriptor(ImageDescriptor.createFromImage(
			ResourceManager.getPluginImage(RefactoringPlugin.getDefault(),
				"icons" + System.getProperty("file.separator") + "siicon.png"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		setWindowTitle(Messages.RefactoringWizard_WizardTitle);
		
		operation = (refactoring == null) ? CREATE : EDIT;
		this.refactoring = refactoring;
		this.originalName = (refactoring != null) ? refactoring.getName() : ""; //$NON-NLS-1$
		//FIXME: Eliminar el null con la enum SCOPE
		this.originalScope = (refactoring != null) ? new ScopeLimitedLister().getRefactoringScope(refactoring) : null;
	}
	
	/**
	 * Constructor.
	 * 
	 * <p>Crea un nuevo asistente para la creaci�n de una refactorizaci�n desde
	 * cero. Necesario para la integraci�n del asistente en Eclipse y su 
	 * activaci�n asistente desde el men� <i>New</i> de Eclipse.</p>
	 */
	public RefactoringWizard(){
		new RefactoringWizard(null);
	}
	
	
	
	/**
	 * A�ade las p�ginas al asistente.
	 */
	@Override
	public void addPages() {
		pageA = new RefactoringWizardPage1(refactoring);
		addPage(pageA);
		pageB = new RefactoringWizardPage2(refactoring);
		addPage(pageB);
		pageC = new RefactoringWizardPage3(refactoring);
		addPage(pageC);
		pageD = new RefactoringWizardPage4(refactoring);
		addPage(pageD);
		pageE = new RefactoringWizardPage5(refactoring);
		addPage(pageE);
		pageF = new RefactoringWizardPage6(refactoring);
		addPage(pageF);
		pageG = new RefactoringWizardPage7(pageA);
		addPage(pageG);
	}

	/**
	 * M�todo llamado cuando se pulsa el bot�n "Finish" en el asistente.
	 * Se crear� la operaci�n que se deba ejecutar, y se ejecutar� utilizando
	 * el propio asistente como contexto de ejecuci�n.
	 * 
	 * @return <code>true</code> para indicar que la solicitud de 
	 * finalizaci�n ha sido aceptada; <code>false</code> para indicar que
	 * ha sido rechazada. 
	 */
	@Override
	public boolean performFinish() {
		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) 
				throws InvocationTargetException {
				configureRefactoring();
				writeRefactoring();
			}
		};
		
		try {
			operation.run(new NullProgressMonitor());
		} catch (InterruptedException e) {
			String message = Messages.RefactoringWizard_CreationInterrupted +
				".\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);
			Logger.getRootLogger().error(message);
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			logger.error(realException.getMessage());
			MessageDialog.openError(
				getShell(), Messages.RefactoringWizard_Error, realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * M�todo de inicializaci�n.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}

	/**
	 * Obtiene el tipo de operaci�n que est� configurando el asistente.
	 * 
	 * @return el tipo de operaci�n que est� configurando el asistente.
	 */
	public int getOperation(){
		return operation;
	}
	
	/**
	 * Obtiene el nombre del tipo de operaci�n que est� configurando el asistente.
	 * 
	 * @return el nombre del tipo de operaci�n que est� configurando el asistente.
	 */
	public String getOperationAsString(){
		return (operation == CREATE) ? Messages.RefactoringWizard_Creation : Messages.RefactoringWizard_Edition; 
	}
	
	/**
	 * Crea y configura la nueva refactorizaci�n personalizada a partir de los
	 * datos introducidos por el usuario en las p�ginas del asistente. 
	 */
	@SuppressWarnings({"unchecked"})
	private void configureRefactoring(){
		if (refactoring == null)
			refactoring = new DynamicRefactoringDefinition();
		
		refactoring.setName(pageA.getNameText().getText().trim());
		refactoring.setDescription(pageA.getDescriptionText().getText().trim());
		refactoring.setImage(pageA.getImageNameText().getText().trim());
		refactoring.setMotivation(pageA.getMotivationText().getText().trim());
		
		refactoring.setInputs(pageB.getInputs());
		
		refactoring.setPreconditions(pageC.getPreconditions());
		refactoring.setActions(pageD.getActions());
		refactoring.setPostconditions(pageE.getPostconditions());
		
		refactoring.setCategories(pageA.getCategories());
		
		HashMap<String, ArrayList<String[]>>[] map = 
			(HashMap<String, ArrayList<String[]>>[])new HashMap[3];
		
		map[RefactoringConstants.PRECONDITION] = 
			pageC.getAmbiguousParameters();
		map[RefactoringConstants.ACTION] = 
			pageD.getAmbiguousParameters();
		map[RefactoringConstants.POSTCONDITION] = 
			pageE.getAmbiguousParameters();
		
		refactoring.setAmbiguousParameters(map);
		
		refactoring.setExamples(pageF.getExamples());
	}
	
	/**
	 * Copia los ficheros asociados a una refactorizaci�n al directorio 
	 * seleccionado y escribe el fichero con la refactorizaci�n creada.
	 * 
	 * @throws IOException si se produce un error de lectura / escritura durante
	 * el proceso de escritura de la refactorizaci�n. 
	 * @throws FileNotFoundException si no se encuentra uno de los ficheros o
	 * directorios implicados en el proceso.
	 * @throws XMLRefactoringWriterException si se produce un error durante la
	 * escritura de la refactorizaci�n en el fichero XML de destino. 
	 */
	private void writeRefactoring() {
		try {
			
			File destination = new File(
				RefactoringPlugin.getDynamicRefactoringsDir() +
				System.getProperty("file.separator") +  //$NON-NLS-1$
				refactoring.getName());

			if (operation == CREATE){

				// Se crea el directorio para la nueva refactorizaci�n.
				if (! destination.mkdir()){
					// Si no se puede crear el directorio (ya existe o error), se impide
					// la escritura de la refactorizaci�n.
					MessageDialog.openError(
						getShell(), Messages.RefactoringWizard_Error,
						Messages.RefactoringWizard_DirectoryNotCreated 
						+ ".\n" + Messages.RefactoringWizard_MakeSureNotExists + ".\n"); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}
				destination.setWritable(true);
				// Se copia el fichero con la DTD.
				FileManager.copyFile(
					new File(RefactoringConstants.DTD_PATH),
					buildFile(destination, RefactoringConstants.DTD_PATH));

				// Se copia la imagen asociada a la refactorizaci�n.
				if (refactoring.getImage() != null &&
						refactoring.getImage().length() > 0){
					File sourceFile = new File(refactoring.getImage());
					FileManager.copyFile(sourceFile, 
						buildFile(destination, refactoring.getImage()));
					refactoring.setImage(
						FileManager.getFileName(refactoring.getImage()));
				}

				// Se copian los ejemplos que se hayan inclu�do.
				ArrayList<String[]> examples = refactoring.getExamples();
				for (String[] example : examples)
					for (int i = 0; i < example.length; i++)
						if (example[i] != null && example[i].length() > 0){
							FileManager.copyFile(new File(example[i]), 
								buildFile(destination, example[i]));
							example[i] = FileManager.getFileName(example[i]);
						}
				
				//actualizamos el fichero refactorings.xml que guarda la informaci�n de las refactorizaciones
				//de la aplicaci�n.
				Scope scope = new ScopeLimitedLister().getRefactoringScope(refactoring);
				new JDOMXMLRefactoringWriterImp(null).addNewRefactoringToXml(scope,refactoring.getName()
						,RefactoringPlugin.getDynamicRefactoringsDir() + "/" + refactoring.getName()
							+ "/" + refactoring.getName() + ".xml");
			}
			
			else if(operation == EDIT){
				
				Scope scope = new ScopeLimitedLister().getRefactoringScope(refactoring);
				// Si se ha renombrado la refactorizaci�n.
				if (!refactoring.getName().equals(originalName)){
					renameResources(destination);
					
					if(scope == originalScope)
						new JDOMXMLRefactoringWriterImp(null).
							renameRefactoringIntoXml(scope,refactoring.getName(),originalName);
				}
				
				//En caso de que el �mbito de la clase haya cambiado hay que editar refactorings.xml
				if(scope != originalScope){
					JDOMXMLRefactoringWriterImp writer = new JDOMXMLRefactoringWriterImp(null);
					writer.deleteRefactoringFromXml(originalScope ,originalName );
					writer.addNewRefactoringToXml(scope,refactoring.getName() ,
						RefactoringPlugin.getDynamicRefactoringsDir() + "/" + refactoring.getName()
							+ "/" + refactoring.getName() + ".xml");
				}
					
				// Se copia el fichero con la DTD si no existe.
				File DTDFile = buildFile(destination, 
					RefactoringConstants.DTD_PATH);
				if (! DTDFile.exists())
					FileManager.copyFile(
						new File(RefactoringConstants.DTD_PATH), DTDFile);
			
				if (refactoring.getImage() != null &&
					refactoring.getImage().length() > 0){
					// Si la ruta al fichero es relativa.
					if (refactoring.getImage().equals(
						FileManager.getFileName(refactoring.getImage()))){
						// La imagen se copi� al crear la refactorizaci�n.
						File sourceFile = buildFile(destination, 
							refactoring.getImage());
						if (! sourceFile.exists()) {
							Object[] messageArgs = {sourceFile.getAbsolutePath()};
							MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
							formatter.applyPattern(Messages.RefactoringWizard_FileNotFound);
							
							throw new FileNotFoundException(
								formatter.format(messageArgs) + "."); //$NON-NLS-1$
						}
					}
					// Si la ruta es absoluta.
					else {
						File sourceFile = new File(refactoring.getImage());
						File newFile = buildFile(destination, 
							FileManager.getFileName(refactoring.getImage()));
						// Si el origen es distinto del destino.
						if (! sourceFile.getAbsolutePath().equals(
							newFile.getAbsolutePath()))
							FileManager.copyFile(sourceFile, newFile);
					}
					refactoring.setImage(FileManager.getFileName(
						refactoring.getImage()));
				}
				
				// Se copian los ejemplos si han sido modificados.
				ArrayList<String[]> examples = refactoring.getExamples();
				for (String[] example : examples){
					for (int i = 0; i < example.length; i++){
						if (example[i] != null && example[i].length() > 0){
							// Se intenta acceder como ruta absoluta.
							File file = new File(example[i]);
							// Si no existe, no se puede copiar.
							if (! file.exists()){
								// El ejemplo se copi� al crear la refactorizaci�n.
								File sourceFile = buildFile(destination, 
									FileManager.getFileName(example[i]));
								// Si en el directorio tampoco est�.
								if (! sourceFile.exists()){
									Object[] messageArgs = {sourceFile.getAbsolutePath()};
									MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
									formatter.applyPattern(Messages.RefactoringWizard_FileNotFound);
									
									throw new FileNotFoundException(
										formatter.format(messageArgs) + "."); //$NON-NLS-1$
								}
							}
							else 
								FileManager.copyFile(file, buildFile(
									destination, FileManager.getFileName(
									example[i])));
						}
					}
				}
			}
						
			// Se escribe la refactorizaci�n en el fichero XML.
			XMLRefactoringWriterFactory factory = 
				new JDOMXMLRefactoringWriterFactory();
			XMLRefactoringWriter writer = new XMLRefactoringWriter(
					factory.makeXMLRefactoringWriterImp(refactoring));
			writer.writeRefactoring(destination);

			String action = (operation == CREATE) ? Messages.RefactoringWizard_CreatedLower : Messages.RefactoringWizard_ModifiedLower;
			
			Object[] messageArgs = {"\"" + refactoring.getName() + "\"", action}; //$NON-NLS-1$ //$NON-NLS-2$
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(Messages.RefactoringWizard_RefactoringSuccessfully);
			
			MessageDialog.openInformation(getShell(), Messages.RefactoringWizard_Completed,
				formatter.format(messageArgs) + "."); //$NON-NLS-1$

		}
		catch(IOException exception){
			logWritingError(exception);
		}
		catch(XMLRefactoringWriterException exception){
			logWritingError(exception);
		}
		catch(XMLRefactoringReaderException exception){
			logWritingError(exception);
		}
	}
	
	/**
	 * Realiza las notificaciones que sean oportunas cuando se produce un error
	 * de escritura de refactorizaci�n.
	 * 
	 * @param source la excepci�n que provoc� el error de escritura.
	 */
	private void logWritingError(Exception source){
		Object[] messageArgs = {"\"" + refactoring.getName() + "\""}; //$NON-NLS-1$ //$NON-NLS-2$
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(Messages.RefactoringWizard_NotSaved);
		
		String message = formatter.format(messageArgs) + ".\n" +  //$NON-NLS-1$
			Messages.RefactoringWizard_ErrorMessage + 
			":\n" + source.getMessage(); //$NON-NLS-1$
		logger.error(message);
		MessageDialog.openError(
			getShell(), Messages.RefactoringWizard_Error, message);
	}

	/**
	 * Construye un nuevo fichero a partir del fichero asociado al directorio en
	 * que deber� crearse y del nombre con que deber� crearse.
	 * 
	 * @param folder directorio en que se deber� crear el fichero.
	 * @param name nombre o ruta de un fichero con el mismo nombre que el que se
	 * quiere crear (�til para la copia de ficheros).
	 * 
	 * @return el fichero creado en la ruta especificada por <code>folder</code>
	 * con el nombre especificado por <code>name</code> (una vez eliminada su parte
	 * correspondiente a la ruta, si es que la tiene).
	 * 
	 * @throws IOException si se produce un error al acceder a la ruta del directorio.
	 */
	private File buildFile(File folder, String name) throws IOException {
		return new File(folder.getCanonicalPath() + 
			System.getProperty("file.separator") + //$NON-NLS-1$
			FileManager.getFileName(name));
	}

	/**
	 * Renombra los recursos asociados a la refactorizaci�n cuyo nombre se debe
	 * corresponder con el de la propia refactorizaci�n.
	 * 
	 * <p>Renombra el directorio que contiene los ficheros de la refactorizaci�n, 
	 * as� como el fichero XML en que se define.</p>
	 * 
	 * @param destination directorio en que deber�an encontrarse los recursos de la
	 * refactorizaci�n una vez ejecutado el renombrado.
	 * 
	 * @throws IOException si se produce un error durante el manejo de los archivos.
	 */
	private void renameResources(File destination) throws IOException {
		// Se busca el directorio con el nombre original.
		File folder = new File(
			RefactoringPlugin.getDynamicRefactoringsDir() +
			System.getProperty("file.separator") + originalName); //$NON-NLS-1$
		// Si se encuentra.
		if (folder.exists() && folder.isDirectory())
			// Se renombra.
			if (folder.renameTo(destination)){
			
				// Se busca el fichero XML de la refactorizaci�n original.			
				File refactoringFile = buildFile(destination, originalName + ".xml"); //$NON-NLS-1$
				// Si se encuentra.
				if (refactoringFile.exists())
					// Se renombra.
					refactoringFile.renameTo(buildFile(destination, 
						refactoring.getName() + ".xml")); //$NON-NLS-1$
			
				String fragment = originalName + System.getProperty("file.separator"); //$NON-NLS-1$
				
				int index = refactoring.getImage().indexOf(fragment); 
				if (index > -1){
					String name = FileManager.getFileName(refactoring.getImage());
					String path = buildFile(destination, name).getAbsolutePath();
					refactoring.setImage(path);
				}
				
				for(String[] example : refactoring.getExamples())
					for (int i = 0; i < example.length; i++){
						index = example[i].indexOf(fragment); 
						if (index > -1){
							String name = FileManager.getFileName(example[i]);
							example[i] = buildFile(destination, name).getAbsolutePath();
						}
					}
		}
	}
}