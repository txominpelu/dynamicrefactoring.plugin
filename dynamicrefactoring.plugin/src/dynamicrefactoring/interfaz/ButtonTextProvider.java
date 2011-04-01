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

package dynamicrefactoring.interfaz;

/**
 * Proporciona el texto que se debe asignar a los botones por defecto habituales.
 * 
 * <p>Permite centralizar el proceso de asignación de etiquetas a los botones
 * típicos de "Aceptar", "Cancelar" y otros, de manera que la internacionalización
 * de este aspecto quede controlada.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class ButtonTextProvider {

	/**
	 * Obtiene el texto asociado al botón "Aceptar".
	 * 
	 * @return el texto asociado al botón "Aceptar".
	 */
	public static String getOKText(){
		return Messages.ButtonTextProvider_Accept;
	}
	
	/**
	 * Obtiene el texto asociado al botón "Cancelar".
	 * 
	 * @return el texto asociado al botón "Cancelar".
	 */
	public static String getCancelText(){
		return Messages.ButtonTextProvider_Cancel;
	}
	
	/**
	 * Obtiene el texto asociado al botón "Siguiente".
	 * 
	 * @return el texto asociado al botón "Siguiente".
	 */
	public static String getNextText(){
		return Messages.ButtonTextProvider_Next;
	}
	
	/**
	 * Obtiene el texto asociado al botón "Anterior".
	 * 
	 * @return el texto asociado al botón "Anterior".
	 */
	public static String getBackText(){
		return Messages.ButtonTextProvider_Back;
	}
	
	/**
	 * Obtiene el texto asociado al botón "Finalizar".
	 * 
	 * @return el texto asociado al botón "Finalizar".
	 */
	public static String getFinishText(){
		return Messages.ButtonTextProvider_Finish;
	}
}