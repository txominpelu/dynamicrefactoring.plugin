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

package repository.moon;

import moon.core.Model;
import refactoring.engine.Refactoring;

/**
 * Representa una refactorizaci�n en el contexto del lenguaje MOON.
 * 
 * @author Ra�l Marticorena
 * 
 * @since MOONRepository-2.1.2
 */
public class MOONRefactoring extends Refactoring {

	/**
	 * Modelo.
	 */
	private static Model MODEL = null;
	
	/**
	 * Constructor.
	 * 
	 * @param name nombre de la refactorizaci�n.
	 * 
	 * @param model modelo sobre el que se ejecuta la refactorizaci�n.
	 */
	public MOONRefactoring(String name, Model model) {
		super(name);
		if (MODEL==null){
			MODEL = model;
		}
		else{
			// Si varias refactorizaciones intentan trabajar sobre modelos diferentes.
			if (MODEL!=model){
				throw new RuntimeException("Operation not available."); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * Obtiene el modelo actual.
	 * 
	 * @return el modelo actual.
	 */
	public static Model getModel(){
		return MODEL;
	}
	
	/**
	 * Restaura el modelo actual a un valor nulo.
	 */
	public static void resetModel(){
		MODEL = null;
	}
}