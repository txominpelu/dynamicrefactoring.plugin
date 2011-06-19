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

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import dynamicrefactoring.RefactoringPlugin;
import dynamicrefactoring.util.io.FileManager;

/**
 * Proporciona acceso a las propiedades del fichero de propiedades.
 */
public class PropertyManager {

	/**
	 * La instancia única de la clase.
	 */
	private static PropertyManager singletonInstance = null;

	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = 
		Logger.getLogger(PropertyManager.class);
	
	/**
	 * El fichero donde se almacenan las propiedades.
	 */
	private final String CONFIGURATION_FILE = "refactoring.properties"; //$NON-NLS-1$

	/**
	 * El nombre de la propiedad que almacena dónde se guardan los ficheros .mod
	 * para recuperar estados intermedios.
	 */
	private final String BACKUP_PROPERTY = "backup"; //$NON-NLS-1$

	/**
	 * El nombre de la propiedad que almacena dónde se guardan temporalmente los
	 * ficheros sobre los que se refactoriza.
	 */
	private final String TEMP_PROPERTY = "temp"; //$NON-NLS-1$

	/**
	 * El nombre de la propiedad que almacena dónde se guardan los ficheros de
	 * registro.
	 */
	protected static final String LOG_PROPERTY = "log"; //$NON-NLS-1$
	
	/**
	 * Las propiedades cargadas del fichero de propiedades.
	 */
	private Properties properties;

	/**
	 * Bloque de inicialización.
	 * 
	 * Recupera las propiedades almacenadas en el fichero de propiedades.
	 */
	private void loadProperties(){
		try {
			
			String pluginId = RefactoringPlugin.BUNDLE_NAME;
			URL fileURL = FileManager.getURLForPluginResource(pluginId, CONFIGURATION_FILE);
			InputStream in = fileURL.openStream();
			
			properties = new Properties();
			properties.load(in);
			
			in.close();

		} catch (Exception e) {
			String message = Messages.PropertyManager_NotLoaded +
				" - " + e.getMessage();  //$NON-NLS-1$
			logger.fatal(message);
			Logger.getRootLogger().fatal(message);			
		}
	}

	/**
	 * Constructor.
	 * 
	 * Privado, siguiendo la estructura del patrón Singleton.
	 */
	private PropertyManager(){
		super();
		
		loadProperties();
	}

	/**
	 * Obtiene la instancia única del gestor de propiedades.
	 * 
	 * Método definido por el patrón de diseño Singleton.
	 * 
	 * @return la instancia única del gestor de propiedades.
	 */
	public static PropertyManager getInstance(){
		if (singletonInstance == null)
			singletonInstance = new PropertyManager();
		return singletonInstance;
	}

	/**
	 * Recupera la propiedad que almacena el nombre del directorio donde se 
	 * guardan los ficheros .mod para recuperar estados anteriores.
	 *
	 * @return La propiedad que almacena el nombre del directorio donde se
	 * guardan los ficheros .mod para recuperar estados anteriores.
	 */
	public String getBackupDirectory(){
		return getCustomProperty(BACKUP_PROPERTY);
	}

	/**
	 * Recupera la propiedad que almacena el nombre del directorio donde se
	 * copian temporalmente los ficheros sobre los que se esté refactorizando.
	 * 
	 * @return La propiedad que almacena el nombre del directorio donde se
	 *         copian temporalmente los ficheros sobre los que se esté
	 *         refactorizando.
	 */
	public String getTempRefactoredFileDirectory(){
		return getCustomProperty(TEMP_PROPERTY);
	}
	
	/**
	 * Recupera la propiedad que almacena el nombre del directorio donde se 
	 * almacenan los ficheros de registro de errores y eventos.
	 *
	 * @return La propiedad que almacena el nombre del directorio donde se 
	 * almacenan los ficheros de registro de errores y eventos.
	 */
	public String getLogFileDirectory(){
		return getCustomProperty(LOG_PROPERTY);
	}

	/**
	 * Obtiene el valor de una propiedad con un nombre determinado.
	 * 
	 * @param name
	 *            el nombre de la propiedad que se busca.
	 * 
	 * @return el valor de la propiedad con el nombre especificado por <code>
	 * name</code>; <code>null</code> si no se encuentra ningún valor para ese
	 *         nombre.
	 */
	public String getCustomProperty(String name){
		return (String) properties.get(name);
	}	
}