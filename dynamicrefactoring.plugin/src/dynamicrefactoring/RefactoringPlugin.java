/*<Dynamic Refactoring Plugin For Eclipse 2.0 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings>

Copyright (C) 2009  Laura Fuente

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

package dynamicrefactoring;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import moon.core.ObjectMoon;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import repository.IRefactoringMessageListener;
import repository.RelayListenerRegistry;
import repository.moon.MOONRefactoring;
import dynamicrefactoring.domain.RefactoringSummary;
import dynamicrefactoring.domain.xml.writer.RefactoringPlanWriter;
import dynamicrefactoring.integration.CodeRegenerator;
import dynamicrefactoring.interfaz.SelectRefactoringWindow;
import dynamicrefactoring.interfaz.view.AvailableRefactoringView;
import dynamicrefactoring.interfaz.view.HistoryView;
import dynamicrefactoring.interfaz.view.ProgressView;
import dynamicrefactoring.interfaz.wizard.search.javadoc.EclipseBasedJavadocReader;
import dynamicrefactoring.listener.IRefactoringRunListener;
import dynamicrefactoring.util.LogManager;
import dynamicrefactoring.util.PropertyManager;
import dynamicrefactoring.util.io.FileManager;
import dynamicrefactoring.util.io.JavaFileManager;
import dynamicrefactoring.util.selection.SelectionInfo;

/**
 * Representa el <i>plugin</i> de refactorizaci�n ante la plataforma Eclipse.
 * 
 * <p>
 * Act�a a modo de mediador entre muchos de los componentes que conforman el
 * <i>plugin</i> y los coordina de manera que todas las operaciones se completen
 * en el orden adecuado.
 * </p>
 * 
 * <p>
 * Implementa las funciones de arranque y finalizaci�n ordenados del
 * <i>plugin</i>, asignando al principio los recursos que sen necesarios, y
 * liber�ndolos al final de forma ordenada. Tambi�n elimina al finalizar los
 * ficheros temporales creados por el <i>plugin</i> durante la ejecuci�n.
 * </p>
 * 
 * <p>
 * Proporciona funciones de acceso a las propiedades del <i>plugin</i> que
 * pueden ser necesarias desde otros puntos del mismo, como el directorio de los
 * ficheros de recuperaci�n o de ficheros temporales, o como el propio nombre
 * identificativo del <i>plugin</i>.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringPlugin extends AbstractUIPlugin 
	implements IRefactoringMessageListener {
	
	
	//The identifiers for the preferences

	/**
	 * Nombre de la carpeta que contiene el catalogo de las refactorizaciones.
	 */
	public static final String DYNAMIC_REFACTORINGS_FOLDER_NAME = "/" + "DynamicRefactorings" + "/";

	/**
	 * Identificador que guarda el directorio de exportaci�n de un plan de
	 * refactorizaciones.
	 */
	public static final String EXPORT_PLAN_DIRECTORY  = "exportPlanDirectory";

	/**
	 * Identificador que guarda el directorio de importaci�n de un plan de
	 * refactorizaciones.
	 */
	public static final String IMPORT_PLAN_DIRECTORY  = "importPlanDirectory";
	
	
	//The default values for the preferences

	/**
	 * Valor por defecto del directorio de exportaci�n de un plan de
	 * refactorizaciones.
	 */
	public static final String DEFAULT_EXPORT_PLAN_DIRECTORY = RefactoringPlugin.getDefault().getBundleRootDir();

	/**
	 * Valor por defecto del directorio de importaci�n de un plan de
	 * refactorizaciones.
	 */
	public static final String DEFAULT_IMPORT_PLAN_DIRECTORY = RefactoringPlugin.getDefault().getBundleRootDir() + "refactoringPlan";
	
	
	
	/**
	 * El nombre del estado inicial del modelo.
	 */
	private static final String INITIAL_REF_NAME = "Initial"; //$NON-NLS-1$

	/**
	 * El nombre del "bundle" asociado al plugin.
	 */
	public static final String BUNDLE_NAME = "dynamicrefactoring.plugin"; //$NON-NLS-1$

	/**
	 * El nombre del punto de extensi�n de los <i>listeners</i>.
	 */
	private static final String LISTENER_EXTPOINT = BUNDLE_NAME + "." + "listeners"; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(RefactoringPlugin.class);

	/**
	 * Instancia �nica del plug-in (patr�n de dise�o Singleton).
	 */
	private static RefactoringPlugin myInstance;

	/**
	 * Nombre del fichero mod que contiene el estado actual del modelo sobre el
	 * que se est�n ejecutando refactorizaciones.
	 */
	private String currentRefactoring;

	/**
	 * Selecci�n actual sobre la interfaz.
	 */
	private SelectionInfo currentSelection;
	
	/**
	 * Proyecto actual sobre el que se trabaja.
	 */
	private IJavaProject currentProject;
			
	/**
	 * Conjunto de <i>listeners</i> a los que hay que informar acerca del 
	 * progreso de las refactorizaciones ejecutadas.
	 */
	private ListenerList listeners;

	/**
	 * Constructor.
	 * 
	 * P�blico, para que sea accesible a Eclipse como plug-in.
	 */
	public RefactoringPlugin(){
		super();
		
		myInstance = this;
		
		currentRefactoring = INITIAL_REF_NAME;
	}

	/**
	 * Inicializa el <i>plugin</i>.
	 * 
	 * @param context el contexto en el que se ejecuta el <i>bundle</i>
	 * del <i>plugin</i>.
	 * 
	 * @throws Exception si se produce un error al arrancar el <i>plugin</i>
	 * 
	 * @see AbstractUIPlugin#start(BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception{
		super.start(context);
		MOONRefactoring.resetModel();
		LogManager.getInstance().loadLogConfig();
		copyDefaultFilesToCommonDir();
		RefactoringPlanWriter.getInstance();
		
		FileManager.copyBundleDirToFileSystem("/DynamicRefactorings", getCommonPluginFilesDir() + File.separator + "temp");
		FileManager.copyResourceToDir("/Classification/classifications.xml", getCommonPluginFilesDir() + File.separator + "temp");
	}

	/**
	 * Copia las refactorizaciones que vienen por defecto incluidas en el plugin
	 * en el directorio de estado de este si no existen todavia en dicho
	 * directorio.
	 * 
	 * @throws IOException
	 */
	private void copyDefaultFilesToCommonDir()
			throws IOException {
		if (!new File(getCommonPluginFilesDir()
				+ "/Classification/user-classifications.xml").exists()) {
			FileManager.copyResourceToDir(
					"/Classification/user-classifications.xml", getCommonPluginFilesDir());
		}
		FileManager.copyResourceToDir("/refactoringsDTD.dtd",
				getCommonPluginFilesDir());
		FileManager.copyResourceToDir("/refactoringPlanDTD.dtd",
				getCommonPluginFilesDir());
		FileManager.copyResourceToDir("/DynamicRefactorings/refactoringDTD.dtd",
				getCommonPluginFilesDir());

		FileManager.copyResourceToDir("/log/dynamicrefactoring.plugin.properties",
				getCommonPluginFilesDir());
		FileManager.copyBundleDirToFileSystem("/bin/", getCommonPluginFilesDir());
	}

	/**
	 * Obtiene la ruta del directorio en el que se guardaran
	 * todos los ficheros que necesite el usuario en la
	 * ejecucion del plugin independientemente del workspace
	 * en el que el usuario este trabajando.
	 * 
	 * @return ruta del directorio comun de ficheros
	 */
	public static String getCommonPluginFilesDir() {
		return Platform.getConfigurationLocation().getURL().getFile() + "dynamicrefactoring.plugin" + "/";
	}

	/**
	 * Directorio en el que se guardan los ficheros
	 * 
	 * @return el directorio en el que se guardaran los ficheros de refactorizacion.
	 */
	public static String getDynamicRefactoringsDir() {
		return getCommonPluginFilesDir()
				+ DYNAMIC_REFACTORINGS_FOLDER_NAME;
	}
	
	/**
	 * Directorio en el que se guardan los ficheros
	 * 
	 * @return el directorio en el que se guardaran los ficheros de refactorizacion.
	 */
	public static String getNonEditableDynamicRefactoringsDir() {
		return getCommonPluginFilesDir() + File.separator + "temp" + DYNAMIC_REFACTORINGS_FOLDER_NAME;
	}
	

	/** 
	 * Initializes a preference store with default preference values 
	 * for this plug-in.
	 * 
	 * @param store the preference store to fill
	 */
	@Override
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(EXPORT_PLAN_DIRECTORY, DEFAULT_EXPORT_PLAN_DIRECTORY);
		store.setDefault(IMPORT_PLAN_DIRECTORY, DEFAULT_IMPORT_PLAN_DIRECTORY);
	}

	/**
	 * Devuelve el valor por defecto del directorio de d�nde selecciona el plan
	 * de refactorizaciones a importar.
	 * 
	 * @return valor por defecto del directorio de importaci�n del plan.
	 */
	public String getDefaultImportRefactoringPlanPreference() {
		return getPreferenceStore().getDefaultString(IMPORT_PLAN_DIRECTORY);
	}

	/**
	 * Devuelve el valor del directorio de d�nde selecciona el plan de
	 * refactorizaciones a importar.
	 * 
	 * @return valor del directorio de importaci�n del plan.
	 * @see #setImportRefactoringPlanPreference
	 */
	public String getImportRefactoringPlanPreference() {
		return getPreferenceStore().getString(IMPORT_PLAN_DIRECTORY);
	}

	/**
	 * Devuelve el valor por defecto del directorio en d�nde se exportara el
	 * plan de refactorizaciones.
	 * 
	 * @return valor por defecto del directorio de exportaci�n del plan.
	 * @see #setImportRefactoringPlanPreference
	 */
	public String getDefaultExportRefactoringPlanPreference() {
		return getPreferenceStore().getDefaultString(EXPORT_PLAN_DIRECTORY);
	}

	/**
	 * Devuelve el valor del directorio en d�nde se exportara el plan de
	 * refactorizaciones.
	 * 
	 * @return valor del directorio de exportaci�n del plan.
	 * @see #setExportRefactoringPlanPreference
	 */
	public String getExportRefactoringPlanPreference() {
		return getPreferenceStore().getString(EXPORT_PLAN_DIRECTORY);
	}

	/**
	 * Establece el valor del directorio en d�nde se exportara el plan de
	 * refactorizaciones.
	 * 
	 * @param directory
	 *            directorio de exportaci�n del plan.
	 * @see #getExportRefactoringPlanPreference
	 */
	public void setExportRefactoringPlanPreference(String directory) {
		getPreferenceStore().setValue(EXPORT_PLAN_DIRECTORY, directory);
	}

	/**
	 * Establece el valor del directorio de d�nde selecciona el plan de
	 * refactorizaciones a importar.
	 * 
	 * @param directory
	 *            directorio de importaci�n del plan.
	 * @see #getImportRefactoringPlanPreference
	 */
	public void setImportRefactoringPlanPreference(String directory) {
		getPreferenceStore().setValue(IMPORT_PLAN_DIRECTORY, directory);
	}

	/**
	 * Elimina el directorio y posibles los ficheros de <i>backup</i> generados
	 * por el <i>plugin</i> durante la ejecuci�n de refactorizaciones, as� como
	 * el fichero MOD utilizado por defecto para guardar el modelo MOON en uso.
	 * 
	 * @param context
	 *            el contexto en el que se ejecuta el <i>bundle</i> del
	 *            <i>plugin</i>.
	 * 
	 * @throws Exception
	 *             si se produce un error al para el <i>plugin</i>.
	 * 
	 * @see AbstractUIPlugin#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception{
		try {
			deleteFiles();
			FileUtils.deleteQuietly(new File(getNonEditableDynamicRefactoringsDir()));
			deleteJavadocReaderProject();			
			myInstance = null;			
		}
		catch (Exception e){
			logger.error(Messages.RefactoringPlugin_NotStopped +
				".\n" + e.getMessage()); //$NON-NLS-1$
		}
		super.stop(context);
	}

	/**
	 * Borra el proyecto creado para obtener la documentacion
	 * de entradas, acciones, precondiciones y postcondiciones
	 * en el wizard de crear refactorizaciones.
	 * 
	 * @throws CoreException
	 */
	private void deleteJavadocReaderProject() throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(EclipseBasedJavadocReader.JAVADOC_READER_PROJECT_NAME);
		project.delete(true, null);
	}

	/**
	 * Elimina los ficheros temporales generados por el <i>plugin</i> hasta ese
	 * momento (ficheros de <i>backup</i> y de modelo).
	 */
	private void deleteFiles() {
		String backupDir = getBackupDir();
		// Si existe el directorio de backup, se asegura de borrarlo.
		if(FileManager.emptyDirectories(backupDir)){
			FileManager.deleteDirectories(backupDir, true);
		}
		
		FileManager.deleteFile(RefactoringConstants.REFACTORING_TYPES_FILE);
		FileManager.deleteFile(RefactoringConstants.REFACTORING_PLAN_FILE);
	}

	/**
	 * Carga bajo demanda la lista de <i>listeners</i> registrados para el punto
	 * de extensi�n declarado por el <i>plugin</i> para el registro de <i>
	 * listeners</i> que hagan un seguimiento del progreso de las
	 * refactorizaciones ejecutadas.
	 * 
	 * @return carga bajo demanda la lista de <i>listeners</i> registrados y
	 *         devuelve la lista obtenida.
	 */
	private ListenerList lazyGetListeners(){
		if (listeners == null)
			listeners = computeListeners();
		else {
			ListenerList newListeners = computeListeners();
			Object[] _newListeners = newListeners.getListeners();
			// Para cada nuevo listener.
			for (int i = 0; i < _newListeners.length; ++i)
				listeners.add(_newListeners[i]);
		}
		return listeners;
	}

	/**
	 * Obtiene la lista de <i>listeners</i> que se han registrado en el punto de
	 * extensi�n declarado por el <i>plugin</i> para el registro de <i>
	 * listeners</i> a los que notificar acerca del progreso de las
	 * refactorizaciones.
	 * 
	 * @return la lista de <i>listener</i> registrados como extensiones.
	 */
	private ListenerList computeListeners(){
		// Se accede al registro de extensiones de Eclipse.
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		// Se solicita el punto de extensi�n declarado para los listener del
		// plugin.
		IExtensionPoint extensionPoint = registry.getExtensionPoint(LISTENER_EXTPOINT); 
		// Se solicitan todas las extensiones registradas en el punto de
		// extensi�n.
		IExtension[] extensions = extensionPoint.getExtensions();
	
		// Se crea la lista de listeners del plugin.
		ListenerList results = new ListenerList();
		for (int i = 0; i < extensions.length; i++){
			// Se accede a la configuraci�n de la siguiente extensi�n.
			IConfigurationElement[] elements = 
				extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++){
				try {
					// Se intenta instanciar un listener a partir de la clase
					// que implementa la extensi�n.
					Object listener = 
						elements[j].createExecutableExtension("class"); //$NON-NLS-1$
					if (listener instanceof IRefactoringRunListener)
						// Se registra el nuevo listener.
						results.add(listener);
				}
				catch (CoreException e){
					logger.error(
						Messages.RefactoringPlugin_ErrorRegistering
						+ ".\n" + e.getMessage()); //$NON-NLS-1$
				}
			}
		}
		return results;
	}

	/**
	 * Registra din�micamente un nuevo <i>listener</i> en la lista de
	 * <i>listeners </i> a los que hay que informar acerca del progreso de las
	 * refactorizaciones.
	 * 
	 * @param listener
	 *            el nuevo <i>listener</i> que se debe registrar.
	 * 
	 * @see #removeRefactoringListener(IRefactoringRunListener)
	 */
	public void addRefactoringListener(IRefactoringRunListener listener){
		if (listener != null)
			lazyGetListeners().add(listener);
	}

	/**
	 * Elimina din�micamente un <i>listener</i> de la lista de <i>listeners </i>
	 * a los que hay que informar acerca del progreso de las refactorizaciones.
	 * 
	 * @param listener
	 *            el <i>listener</i> que se debe eliminar.
	 * 
	 * @see #addRefactoringListener(IRefactoringRunListener)
	 */
	public void removeRefactoringListener(IRefactoringRunListener listener){
		if (listener != null)
			lazyGetListeners().remove(listener);
	}

	/**
	 * Solicita al <i>plugin</i> que notifique a todos los <i>listeners</i>
	 * registrados que una refactorizaci�n acaba de comenzar.
	 * 
	 * <p>
	 * Si una de las extensiones registradas da lugar a alg�n tipo de error, se
	 * puede controlar el problema y continuar con la ejecuci�n normal. En ese
	 * caso, el <i> listener</i> que hubiera provocado el problema ser�a
	 * eliminado de la lista de <i>listeners</i> registrados.
	 * </p>
	 * 
	 * @param name
	 *            el nombre de la refactorizaci�n que acaba de comenzar.
	 */
	public void fireRefactoringStarted(String name){
		// Se solicita la lista de listeners registrados.
		Object[] registeredListeners = lazyGetListeners().getListeners();
		// Para cada listener.
		for (int i = 0; i < registeredListeners.length; ++i){
			final IRefactoringRunListener next = 
				(IRefactoringRunListener) registeredListeners[i];
			// Se envuelve la llamada para evitar que se extiendan fallos 
			// que se pudieran producir en la ejecuci�n de la llamada
			// (el listener puede ser externo).
			try {
				next.refactoringStarted(name);
			}catch(Exception exception) {
				logger.error(
					Messages.RefactoringPlugin_ErrorNotifying
					+ ".\n" + exception.getMessage()); //$NON-NLS-1$
				lazyGetListeners().remove(next);
			}
		}
	}

	/**
	 * Notifica al <i>listener</i> que se ha enviado un mensaje desde uno de los
	 * elementos concretos del repositorio que componen la refactorizaci�n.
	 * 
	 * <p>
	 * Si una de las extensiones registradas da lugar a alg�n tipo de error, se
	 * puede controlar el problema y continuar con la ejecuci�n normal. En ese
	 * caso, el <i>listener</i> que hubier provocado el problema ser�a eliminado
	 * de la lista de <i>listeners</i> registrados.
	 * </p>
	 * 
	 * @param message
	 *            el mensaje enviado.
	 * 
	 * @see IRefactoringMessageListener#messageSent(String)
	 */
	@Override
	public void messageSent(String message){
		// Se solicita la lista de listeners registrados.
		Object[] registeredListeners = lazyGetListeners().getListeners();
		// Para cada listener.
		for (int i = 0; i < registeredListeners.length; ++i){
			final IRefactoringRunListener next = 
				(IRefactoringRunListener) registeredListeners[i];
			// Se envuelve la llamada para evitar que se extiendan fallos 
			// que se pudieran producir en la ejecuci�n de la llamada
			// (el listener puede ser externo).
			try {
				next.refactoringStepTaken(message);
			}catch (Exception exception) {
				logger.error(
					Messages.RefactoringPlugin_ErrorNotifying + 
					".\n" + exception.getMessage()); //$NON-NLS-1$
				lazyGetListeners().remove(next);
			}
		}
	}

	/**
	 * Solicita al <i>plugin</i> que notifique a todos los <i>listeners</i>
	 * registrados que una refactorizaci�n ha fallado.
	 * 
	 * <p>
	 * Si una de las extensiones registradas da lugar a alg�n tipo de error, el
	 * <i> listener</i> que hubiera provocado el problema ser�a eliminado de la
	 * lista de <i>listeners</i> registrados.
	 * </p>
	 * 
	 * @param name
	 *            el nombre de la refactorizaci�n que ha fallado.
	 * @param message
	 *            el mensaje asociado al fallo.
	 */
	public void fireRefactoringFailed(String name, String message){
		// Se solicita la lista de listeners registrados.
		Object[] registeredListeners = lazyGetListeners().getListeners();
		// Para cada listener.
		for (int i = 0; i < registeredListeners.length; ++i){
			final IRefactoringRunListener next = 
				(IRefactoringRunListener) registeredListeners[i];
			// Se envuelve la llamada para evitar que se extiendan fallos 
			// que se pudieran producir en la ejecuci�n de la llamada
			// (el listener puede ser externo).
			try {
				next.refactoringFailed(name, message);	
			}catch (Exception exception) {
				logger.error(
					Messages.RefactoringPlugin_ErrorNotifying
					+ ".\n" + exception.getMessage()); //$NON-NLS-1$
				lazyGetListeners().remove(next);
			}			
		}
	}

	/**
	 * Solicita al <i>plugin</i> que notifique a todos los <i>listeners</i>
	 * registrados que una refactorizaci�n acaba de terminar.
	 * 
	 * <p>
	 * Si una de las extensiones registradas da lugar a alg�n tipo de error, se
	 * puede controlar el problema y continuar con la ejecuci�n normal. En ese
	 * caso, el <i> listener</i> que hubiera provocado el problema ser�a
	 * eliminado de la lista de <i>listeners</i> registrados.
	 * </p>
	 * 
	 * @param summary
	 *            el resumen de la refactorizaci�n que acaba de finalizar.
	 */
	public void fireRefactoringFinished(RefactoringSummary summary){
		
		// Se solicita la lista de listeners registrados.
		Object[] registeredListeners = lazyGetListeners().getListeners();
		// Para cada listener.
		for (int i = 0; i < registeredListeners.length; ++i){
			final IRefactoringRunListener next = 
				(IRefactoringRunListener) registeredListeners[i];
			// Se envuelve la llamada para evitar que se extiendan fallos que 
			// se pudieran producir en la ejecuci�n de la llamada (el listener
			// puede ser externo).
			try {
				next.refactoringFinished(summary);
			}catch(Exception exception) {
				exception.printStackTrace();
				logger.error(
					Messages.RefactoringPlugin_ErrorNotifying
					+ ".\n" + exception.getMessage()); //$NON-NLS-1$
				lazyGetListeners().remove(next);
			}			
		}
	}

	/**
	 * Solicita al <i>plugin</i> que notifique a todos los <i>listeners</i>
	 * registrados que una refactorizaci�n se ha deshecho y se ha recuperado el
	 * estado previo.
	 * 
	 * <p>
	 * Si una de las extensiones registradas da lugar a alg�n tipo de error, se
	 * puede controlar el problema y continuar con la ejecuci�n normal. En ese
	 * caso, el <i> listener</i> que hubiera provocado el problema ser�a
	 * eliminado de la lista de <i>listeners</i> registrados.
	 * </p>
	 * 
	 * @param id
	 *            identificador de la refactorizaci�n que se ha deshecho.
	 */
	public void fireRefactoringUndone(String id){
		// Se solicita la lista de listeners registrados.
		Object[] registeredListeners = lazyGetListeners().getListeners();
		// Para cada listener.
		for (int i = 0; i < registeredListeners.length; ++i){
			final IRefactoringRunListener next = 
				(IRefactoringRunListener) registeredListeners[i];
			// Se envuelve la llamada para evitar que se extiendan fallos que 
			// se pudieran producir en la ejecuci�n de la llamada (el listener
			// puede ser externo).
			try {
				next.refactoringUndone(id);
			}
			catch(Throwable exception) {
				logger.error(Messages.RefactoringPlugin_ErrorNotifying
					+ ".\n" + exception.getMessage()); //$NON-NLS-1$
				lazyGetListeners().remove(next);
			}			
		}
	}

	/**
	 * Obtiene la instancia �nica del plug-in.
	 * 
	 * M�todo definido por el patr�n de dise�o Singleton.
	 * 
	 * @return la instancia �nica del plug-in.
	 */
	public static RefactoringPlugin getDefault(){
		if (myInstance == null)
			myInstance = new RefactoringPlugin();
		return myInstance;
	}

	/**
	 * Inicia el proceso de configuraci�n y ejecuci�n de una refactorizaci�n
	 * sobre un elemento seleccionado.
	 * 
	 * @param selectedElement
	 *            el elemento seleccionado en el modelo MOON. Debe ser uno de
	 *            <code>ClassDef</code>, <code>MethDec</code> o
	 *            <code>AttDec</code>.
	 * @param selection
	 *            el proveedor de informaci�n para la selecci�n actual.
	 * @param showSelector
	 *            indica si se quiere o no mostrar la ventana selectora,
	 */
	public void runRefactoring(ObjectMoon selectedElement, 
		SelectionInfo selection, Boolean showSelector ){
		
		assert (selectedElement instanceof ClassDef ||
				selectedElement instanceof MethDec ||
				selectedElement instanceof AttDec ||
				selectedElement instanceof FormalArgument): Messages.RefactoringPlugin_InvalidElement;
	
		this.currentSelection = selection;
		
		IJavaProject nextProject = currentSelection.getJavaProjectForSelection();  
		if(currentProject != null && nextProject.getElementName() != currentProject.getElementName())
			cleanEnvironment();
		
		this.currentProject = nextProject;
		
		if (selectedElement != null){
			// Se registra el propio plugin como listener de los mensajes
			// enviados por los elementos del repositorio.
			RelayListenerRegistry.getInstance().add(this);
			
			try {
				// Se inicializan las vistas.
				IWorkbenchPage page = 
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()[0];
				page.showView(HistoryView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
				page.showView(AvailableRefactoringView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
				page.showView(ProgressView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
			}
			catch(Exception exception){
				logger.error(
					Messages.RefactoringPlugin_ErrorInitializing + 
					".");  //$NON-NLS-1$
				logger.error(exception.getMessage());
			}
			if(showSelector){
				SelectRefactoringWindow selector = new SelectRefactoringWindow(
					getWorkbench().getActiveWorkbenchWindow().getShell(),
					selectedElement);
				
				selector.setBlockOnOpen(true);
				selector.open();
			}
		}
	}

	/**
	 * Realiza las actualizaciones necesarias sobre los recursos del plugin y
	 * del entorno de Eclipse despu�s de la ejecuci�n de una refactorizaci�n.
	 * 
	 * @param refactoring
	 *            el nombre de la refactorizaci�n ejecutada.
	 */
	public void updateEnvironment(String refactoring){
		storeRefactoring(refactoring);
		
		CodeRegenerator.getInstance().refreshCode();
	}
	
	/**
	 * Elimina todos los recursos temporales asignados a un cierto proyecto antes
	 * de ponerse a trabajar sobre otro diferente.
	 */
	public void cleanEnvironment(){
		currentRefactoring = INITIAL_REF_NAME;		
				
		IWorkbenchPage page = 
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPages()[0];
		IViewReference[] views = page.getViewReferences();
		
		for (int i = 0; i < views.length; i++){
			if (views[i].getId().equals(HistoryView.ID)){
				HistoryView history = (HistoryView)views[i].getView(true);
				history.cleanView();
			}
			else if (views[i].getId().equals(ProgressView.ID)){
				ProgressView progress = (ProgressView)views[i].getView(true);
				progress.cleanTable();
			}
			else if (views[i].getId().equals(AvailableRefactoringView.ID)){
				AvailableRefactoringView available = (AvailableRefactoringView)views[i].getView(true);
				available.cleanView();
			}
		}
		
		deleteFiles();
	}

	/**
	 * Obtiene la ruta ra�z del "bundle" asociado al plugin.
	 * 
	 * La ruta termina en el s�mbolo separador del sistema operativo.
	 * 
	 * @return la ruta ra�z del "bundle" asociado al plugin.
	 */
	public String getBundleRootDir(){
		Bundle pluginBundle = Platform.getBundle(BUNDLE_NAME);
		
		String pluginRoot = "";
		
		if(pluginBundle == null){
			pluginRoot = "." + File.separatorChar + "";
		}else {
			String pluginLocation = pluginBundle.getLocation();
			if(pluginLocation.startsWith("update@")){
				// La localizaci�n tiene el formato update@directorio.
				// Hay que eliminar el primer fragmento de la localizaci�n.
				pluginRoot = pluginLocation.substring(pluginLocation.indexOf('@') + 1);
			}else{
				//pluginRoot = pluginLocation.substring(pluginLocation.indexOf('/') + 1);
				pluginRoot = pluginLocation.substring(pluginLocation.indexOf('/'));
			}
		}
	
		return FilenameUtils.separatorsToSystem(pluginRoot);
	}

	/**
	 * Obtiene el proyecto sobre el que se est� trabajando en un momento dado.
	 * 
	 * @return el proyecto sobre el que se est� trabajando en un momento dado.
	 */
	public IProject getAffectedProject(){
		if(currentSelection==null){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
			IEditorInput input = 
				window.getPages()[0].getActiveEditor().getEditorInput(); 
			currentProject = JavaUI.getEditorInputJavaElement(input).getJavaProject();
			
			return currentProject.getProject(); 
		}
		
		return currentSelection.getProjectForSelection();
	}

	/**
	 * Obtiene el proyecto Java sobre el que se est� trabajando en un momento
	 * dado.
	 * 
	 * @return el proyecto Java sobre el que se est� trabajando en un momento
	 *         dado.
	 */
	public IJavaProject getAffectedJavaProject(){
		return currentProject;
	}

	/**
	 * Obtiene los directorios ra�z a partir de los cuales se pueden encontrar
	 * los ficheros fuente del proyecto afectado.
	 * 
	 * @return los directorios ra�z de los ficheros fuente.
	 */
	public List<String> getAffectedSourceDirs(){
		return JavaFileManager.getSourceDirsForProject(getAffectedJavaProject());
	}
	
	/**
	 * Obtiene la ruta del directorio de backup utilizado por el plugin.
	 * 
	 * @return la ruta del directorio de backup utilizado por el plugin.
	 */
	String getBackupDir(){
		// Directorio temporal asignado para los metadatos del plugin.
		String tmpDir = getCommonPluginFilesDir();
		String backupDirName = 
			PropertyManager.getInstance().getBackupDirectory();
		
		return tmpDir + File.separatorChar + backupDirName; //$NON-NLS-1$
	}

	/**
	 * Almacena una refactorizaci�n en la lista de refactorizaciones ejecutadas.
	 * 
	 * Le asigna un nombre �nico basado en el tipo de refactorizaci�n y la hora
	 * exacta en la que se guarda la refactorizaci�n.
	 * 
	 * @param name
	 *            el nombre de la refactorizaci�n ejecutada.
	 */
	public void storeRefactoring(String name){
		Calendar calendar = new GregorianCalendar();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);

		currentRefactoring = name + "_" + hour + "." + minutes  //$NON-NLS-1$ //$NON-NLS-2$
			+ "." + seconds + ".mod";  //$NON-NLS-1$ //$NON-NLS-2$
	}
}