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
import java.util.List;

import moon.core.ObjectMoon;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import dynamicrefactoring.util.processor.JavaFieldProcessor;

/**
 * Proporciona las funciones necesarias para obtener el atributo MOON de una
 * clase con el que se corresponde un atributo seleccionado en Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class FieldSelectionHandler implements ISelectionHandler {
	
	/**
	 * La clase MOON en la que se define el atributo.
	 */
	protected ClassDef fieldClass;
	
	/**
	 * La descripción MOON del atributo seleccionado.
	 */
	protected AttDec fieldDescription;
	
	/**
	 * Obtiene la descripción MOON del atributo representado por una selección 
	 * del interfaz gráfico.
	 * 
	 * Método plantilla (patrón de diseño Método Plantilla).
	 * 
	 * @return la descripción MOON del atributo representado por una selección 
	 * del interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase a
	 * la que pertenece el atributo en el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 * 
	 * @see ISelectionHandler#getMainObject()
	 */
	@Override
	public ObjectMoon getMainObject() 
		throws ClassNotFoundException, IOException {
		
		if (fieldDescription == null){
			// Llamada a la operación primitiva (patrón de diseño Método Plantilla).
			JavaFieldProcessor fieldProcessor = getFieldProcessor();
			
			String uniqueName = fieldProcessor.getUniqueName();
			
			List<AttDec> attributes = getFieldClass().getAttributes();
			for(AttDec nextAttribute : attributes)
				if(nextAttribute.getUniqueName().toString().equals(uniqueName)){
					fieldDescription = nextAttribute;
					break;
				}
		}
		
		return fieldDescription;
	} 
	
	/**
	 * Obtiene la clase del modelo MOON en la que se define el atributo 
	 * representado por una selección del interfaz gráfico.
	 * 
	 * @return la clase del modelo MOON en la que se define el atributo
	 * representado por una selección del interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase en
	 * el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 */
	public abstract ClassDef getFieldClass() 
		throws ClassNotFoundException, IOException;
	
	/**
	 * Obtiene un elemento capaz de procesar la información de un atributo Java.
	 * 
	 * Operación primitiva (patrón de diseño Método Plantilla).
	 * 
	 * @return un elemento capaz de procesar la información de un atributo Java.
	 */
	protected abstract JavaFieldProcessor getFieldProcessor();
}