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

package dynamicrefactoring.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.Status;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.util.io.FileManager;

/**
 * Permite cargar las propiedades de la actividad de registro de traza del 
 * <i>plugin</i>.
 * 
 * <p>Utiliza un fichero de configuraci�n por defecto, que deber� 
 * modificarse solo en caso de necesidad y con precauci�n, y guardando en
 * todo momento una copia de la configuraci�n original.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class LogManager {
	
	/**
	 * La instancia �nica de la clase.
	 */
	private static LogManager singletonInstance = null;
	
	/**
	 * Nombre del fichero de configuraci�n del registro de errores.
	 */
	public static final String LOG_PROPERTIES_FILE = 
		"dynamicrefactoring.plugin.properties"; //$NON-NLS-1$
	
	/**
	 * Nombre de la ruta donde se encuentra el fichero de configuración del registro de errores.
	 */
	public static final String LOG_PROPERTIES_FILE_PATH =
		RefactoringPlugin.getCommonPluginFilesDir() +
		PropertyManager.getInstance().getLogFileDirectory() +
		File.separatorChar + 
		LOG_PROPERTIES_FILE;
	
	/**
	 * Nombre del fichero de log de salida.
	 */
	private static final String LOG_FILE = "dynamicrefactoring.log"; //$NON-NLS-1$
	
	/**
	 * Constructor.
	 * 
	 * Privado, siguiendo la estructura del patr�n Singleton.
	 */
	private LogManager(){
		super();
	}
	
	/**
	 * Obtiene la instancia �nica del gestor del registro.
	 * 
	 * M�todo definido por el patr�n de dise�o Singleton.
	 * 
	 * @return la instancia �nica del gestor del registro.
	 */
	public static LogManager getInstance(){
		if (singletonInstance == null)
			singletonInstance = new LogManager();
		return singletonInstance;
	}
	
	/**
	 * Carga la configuraci�n de la actividad de registro.
	 */
	public void loadLogConfig(){
		
		setLogFileProperty();
		try{
			PropertyConfigurator.configure(LOG_PROPERTIES_FILE_PATH);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Intenta situar el destino del fichero de salida del registro en el 
	 * directorio perteneciente al plugin, en lugar de en la ra�z del ejecutable
	 * de eclipse.
	 * 
	 * Modifica el fichero de configuraci�n de la actividad de registro para que
	 * el destino de la salida del <code>appender</code> A2 sea un fichero en la
	 * carpeta de registro del directorio temporal del plugin.
	 * 
	 * Si la carpeta de registro o el fichero de registro no existen, los crea.
	 */
	private void setLogFileProperty(){
		try {
		
			FileInputStream configFile = new FileInputStream(LOG_PROPERTIES_FILE_PATH);
			
			// Se cargan las propiedades de la actividad de registro.
			Properties logProperties = new Properties();
			logProperties.load(configFile);
			
			configFile.close();
			
			// Se construye la ruta actual del directorio temporal del plugin
			// y en ella se sit�a la carpeta de log.
			String logDir = RefactoringPlugin.getCommonPluginFilesDir() + PropertyManager.getInstance().getLogFileDirectory();
			String logFile = logDir + File.separator + LOG_FILE; //$NON-NLS-1$
			
			logProperties.setProperty("log4j.appender.A2.File", logFile); //$NON-NLS-1$
			
			FileOutputStream outputFile = new FileOutputStream(LOG_PROPERTIES_FILE_PATH);
				
			logProperties.store(outputFile, ""); //$NON-NLS-1$
			
			outputFile.close();
			
			FileManager.createDir(logDir);
			FileManager.createFile(logFile);

		} catch (Exception e) {
			dynamicrefactoring.RefactoringPlugin.getDefault().getLog().log(
				new Status(Status.ERROR, RefactoringPlugin.BUNDLE_NAME, 
					Messages.LogManager_ErrorConfiguring +
					".", e)); //$NON-NLS-1$
		}
	}
}