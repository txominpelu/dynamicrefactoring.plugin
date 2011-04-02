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

import org.eclipse.core.runtime.FileLocator;

import com.google.common.base.Throwables;

/**
 * Define las constantes utilizadas para el funcionamiento de las
 * refactorizaciones din�micas.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 * @author <A HREF="mailto:alc0022@alu.ubu.es">�ngel L�pez Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Pe�a Fern�ndez</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RefactoringConstants {
	
	public static final String DTD_FILE_NAME = "refactoringDTD.dtd";

	/**
	 * Nombre del fichero .jar que almacena las clases que componen el modelo moon.
	 */
	public static final String BIBLIOTECA_MOON = "moon-2.4.1.jar";
	
	/**
	 * Nombre del fichero .jar que almacena las clases que componen el modelo javamoon.
	 */
	public static final String BIBLIOTECA_JAVA = "javamoon-2.6.0.jar";

	/**
	 * Nombre del fichero en el que se guardan las distintas refactorizaciones
	 * din�micas clasificadas seg�n el tipo de entrada principal.
	 */
	public static final String REFACTORING_TYPES_FILE = RefactoringPlugin.getCommonPluginFilesDir()
			+ "/refactorings.xml";

	/**
	 * Directorio temporal donde almacenar el classpath temporal que se utiliza
	 * durante la ejecuci�n de un plan de refactorizaciones.
	 */
	public static final String TEMP_DIRECTORY= RefactoringPlugin.getDefault().getBundleRootDir() + "temp";
	
	/**
	 * Fichero DTD que contiene la estructura del REFACTORING_PLAN_FILE.
	 */
	public static final String REFACTORING_PLAN_DTD = RefactoringPlugin.getCommonPluginFilesDir()
			+ "/refactoringPlanDTD.dtd";

	/**
	 * Nombre del fichero en el que se guardan las distintas refactorizaciones
	 * din�micas que han sido ejecutadas dentro de la aplicaci�n.
	 */
	public static final String REFACTORING_PLAN_FILE = RefactoringPlugin.getCommonPluginFilesDir()
			+ "/refactoringPlan.xml";
	
	/**
	 * Nombre completamente cualificado del paquete que contiene los predicados
	 * del repositorio.
	 */
	public static final String PREDICATES_PACKAGE = 
		"repository.moon.concretepredicate."; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del paquete que contiene las acciones
	 * del repositorio.
	 */
	public static final String ACTIONS_PACKAGE = 
		"repository.moon.concreteaction."; //$NON-NLS-1$
	
	/**
	 * Ruta al directorio donde se encuentran las clases del plugin.
	 */
	public static final String REFACTORING_CLASSES_DIR = RefactoringPlugin.getCommonPluginFilesDir()
			+ "/bin/"; //$NON-NLS-1$
	
	/**
	 * Ruta al directorio donde se encuentran los documentos javadoc.
	 */
	public static final String REFACTORING_JAVADOC = "/doc/javadoc"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del paquete que contiene las acciones
	 * del repositorio dependientes de Java.
	 */
	public static final String JAVA_ACTIONS_PACKAGE = "repository.java.concreteaction."; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del paquete que contiene los predicados
	 * del repositorio dependientes de Java.
	 */
	public static final String JAVA_PREDICATES_PACKAGE = "repository.java.concretepredicate."; //$NON-NLS-1$

	/**
	 * La ruta al fichero con la DTD de las refactorizaciones din�micas.
	 */
	public static final String DTD_PATH = 
		RefactoringPlugin.getDynamicRefactoringsDir() + File.separatorChar + DTD_FILE_NAME; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Ruta al directorio donde se encuentran las definiciones de las precondiciones.
	 */
	public static final String PRECONDITION_DIR = getPrePostDir();
	
	/**
	 * Ruta al directorio donde se encuentran las definiciones de las postcondiciones.
	 */
	public static final String POSTCONDITION_DIR = getPrePostDir();
		
	/**
	 * Ruta al directorio donde se encuentran las definiciones de las acciones.
	 */
	public static final String ACTION_DIR = getActionDir();
	
	/**
	 * Ruta al directorio donde se encuentran las definiciones de las acciones
	 * dependientes de Java.
	 */
	public static final String JAVA_ACTION_DIR = getJavaActionDir();
	
	/**
	 * Ruta al directorio donde se encuentran las definiciones de los
	 * predicados dependientes de Java.
	 */
	public static final String JAVA_PREDICATE_DIR = getJavaPredicateDir();
		
	/**
	 * Ruta al fichero del core de MOON.
	 */
	public static final String MOONCORE_DIR = getLibDir();

	/**
	 * Ruta al fichero de la extensi�n Java de MOON.
	 */
	public static final String JAVAEXTENSION_DIR = getJavaLibDir();

	/**
	 * La extensi�n de los ficheros donde se definen refactorizaciones
	 * din�micas.
	 */
	public static final String FILE_EXTENSION = ".xml"; //$NON-NLS-1$
	
	/**
	 * Clase del Modelo de MOON.
	 */
	public static final String MODEL_PATH = "moon.core.Model"; //$NON-NLS-1$
	
	/**
	 * Clase de las cadenas de texto.
	 */
	public static final String STRING_PATH = "java.lang.String"; //$NON-NLS-1$
	
	/**
	 * Clase de las colecciones.
	 */
	public static final String COLLECTION_PATH = "java.util.Collection"; //$NON-NLS-1$
	
	/**
	 * Clase de los iteradores.
	 */
	public static final String ITERATOR_PATH = "java.util.Iterator"; //$NON-NLS-1$

	/**
	 * Ruta al fichero de clasificaciones.
	 */
	public static final String CLASSIFICATION_TYPES_FILE = RefactoringPlugin.getCommonPluginFilesDir() +
			File.separator + "Classification" + File.separator + "classifications.xml";

	/**
	 * Obtiene la ruta de la ra�z de la biblioteca de MOON.
	 * 
	 * @return la ruta de la ra�z de la biblioteca de MOON.
	 */
	public static String getLibDir(){
		try {
			return FileLocator.toFileURL(RefactoringConstants.class.getResource(
				"/lib/" + BIBLIOTECA_MOON)).getPath();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene la ruta de la ra�z de la biblioteca con la extensi�n Java de
	 * MOON.
	 * 
	 * @return la ruta de la ra�z de la biblioteca con la extensi�n Java de
	 *         MOON.
	 */
	public static String getJavaLibDir(){
		try {
			return FileLocator.toFileURL(RefactoringConstants.class.getResource("/lib/" + BIBLIOTECA_JAVA)).getPath();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene la ruta del directorio ra�z bajo el cual se encuentran las clases
	 * de los predicados concretos del repositorio.
	 * 
	 * @return la ruta del directorio ra�z bajo el cual se encuentran las clases
	 *         de los predicados concretos del repositorio.
	 */
	public static String getPrePostDir(){
		return REFACTORING_CLASSES_DIR + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"repository" + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"moon" + File.separatorChar + "concretepredicate"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene la ruta del directorio ra�z bajo el cual se encuentran las clases
	 * de las acciones concretas del repositorio.
	 * 
	 * @return la ruta del directorio ra�z bajo el cual se encuentran las clases
	 *         de las acciones concretas del repositorio.
	 */
	public static String getActionDir(){
		return REFACTORING_CLASSES_DIR + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"repository" + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"moon" + File.separatorChar + "concreteaction"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene la ruta del directorio ra�z bajo el cual se encuentran las clases
	 * de las acciones concretas del repositorio dependientes de Java.
	 * 
	 * @return la ruta del directorio ra�z bajo el cual se encuentran las clases
	 *         de las acciones concretas del repositorio dependientes de Java.
	 */
	public static String getJavaActionDir(){
		return REFACTORING_CLASSES_DIR + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"repository" + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"java" + File.separatorChar + "concreteaction"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Obtiene la ruta del directorio ra�z bajo el cual se encuentran las clases
	 * de los predicados del repositorio dependientes de Java.
	 * 
	 * @return la ruta del directorio ra�z bajo el cual se encuentran las clases
	 *         de los predicados concretos del repositorio dependientes de Java.
	 */
	public static String getJavaPredicateDir(){
		return REFACTORING_CLASSES_DIR + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"repository" + File.separatorChar +  //$NON-NLS-1$ //$NON-NLS-2$
			"java" + File.separatorChar + "concretepredicate"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * Constructor privado que se asegura de que nadie
	 * pueda crear instancias de esta clase.
	 */
	private RefactoringConstants(){}
}