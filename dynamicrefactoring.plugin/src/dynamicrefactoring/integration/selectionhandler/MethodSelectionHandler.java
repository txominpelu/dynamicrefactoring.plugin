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

package dynamicrefactoring.integration.selectionhandler;

import java.io.IOException;

import moon.core.ObjectMoon;
import moon.core.classdef.ClassDef;
import moon.core.classdef.MethDec;
import repository.moon.concretefunction.MethodRetriever;
import dynamicrefactoring.util.processor.JavaMethodProcessor;

/**
 * Proporciona las funciones necesarias para obtener el método MOON con el que
 * se corresponde un método seleccionado en Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class MethodSelectionHandler implements ISelectionHandler {
	
	/**
	 * La clase MOON en la que se define el método.
	 */
	protected ClassDef methodClass;
	
	/**
	 * La descripción MOON del método seleccionado.
	 */
	protected MethDec methodDescription;
	
	/**
	 * Obtiene la descripción MOON del método representado por una selección del
	 * interfaz gráfico.
	 * 
	 * Método plantilla (patrón de diseño Método Plantilla).
	 * 
	 * @return la descripción MOON del método representado por una selección del 
	 * interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase a
	 * la que pertenece el método en el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 * 
	 * @see ISelectionHandler#getMainObject()
	 */
	public ObjectMoon getMainObject()
		throws ClassNotFoundException, IOException {
		
		if (methodDescription == null){
			// Llamada a la operación primitiva 
			// (patrón de diseño Método Plantilla).
			JavaMethodProcessor methodProcessor = getMethodProcessor(); 
							
			String uniqueName = methodProcessor.getUniqueName();

			methodDescription = 
				(MethDec) new MethodRetriever(getMethodClass(), uniqueName).getValue();
		}
		
		return methodDescription;
	}
	
	/**
	 * Obtiene la clase del modelo MOON en la que se define el método 
	 * representado por una selección del interfaz gráfico.
	 * 
	 * @return la clase del modelo MOON en la que se define el método
	 * representado por una selección del interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase en
	 * el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 */
	public abstract ClassDef getMethodClass() 
		throws ClassNotFoundException, IOException; 

	/**
	 * Obtiene un elemento capaz de procesar la información de un método Java.
	 * 
	 * Operación primitiva (patrón de diseño Método Plantilla).
	 * 
	 * @return un elemento capaz de procesar la información de un método Java.
	 */
	protected abstract JavaMethodProcessor getMethodProcessor();
}
