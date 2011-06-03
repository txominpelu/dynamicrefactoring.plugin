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

/**
 * Proporciona las funciones necesarias para obtener la clase MOON con la que
 * se corresponde una clase seleccionada en Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public abstract class ClassSelectionHandler implements ISelectionHandler {
	
	/**
	 * La clase MOON que representa la clase seleccionada.
	 */
	protected ClassDef classDef;
	
	/**
	 * Obtiene la clase del modelo MOON que se corresponde con la clase 
	 * a la que pertenece la selecci�n del interfaz gr�fico.
	 * 
	 * @return la clase del modelo MOON que se corresponde con la clase
	 * a la que pertenece la selecci�n del interfaz gr�fico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase en
	 * el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 * 
	 * @see ISelectionHandler#getMainObject()
	 */
	@Override
	public abstract ObjectMoon getMainObject() 
		throws ClassNotFoundException, IOException;
}