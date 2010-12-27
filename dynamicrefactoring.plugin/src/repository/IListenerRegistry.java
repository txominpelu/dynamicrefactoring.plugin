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

package repository;

/**
 * Define las funciones de registro y notificación de <i> listeners</i> que 
 * deban registrar los mensajes enviados desde los elementos del repositorio.
 * 
 * <p>Los clientes que implementen la interfaz recibirán los mensajes de 
 * proceso de los componentes concretos del repositorio y actuarán en
 * consecuencia registrando o mostrando dichos mensajes, o extendiéndolos
 * a su lista de <i>listeners</i>.
 * 
 * <p>Desempeña el papel de Sujeto Abstracto en el patrón de diseño Observador.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface IListenerRegistry {
	
	/**
	 * Añade un <i>listener</i> a la lista de <i>listeners</i> registrados.
	 * 
	 * @param listener el <i>listener</i> que se debe añadir.
	 */
	public void add(IRefactoringMessageListener listener);
	
	/**
	 * Elimina un <i>listener</i> de la lista de <i>listeners</i> registrados.
	 * 
	 * @param listener el <i>listener</i> que se debe eliminar.
	 */
	public void remove(IRefactoringMessageListener listener);
	
	/**
	 * Notifica a todos los <i>listeners</i> registrados acerca del envío de un
	 * mensaje desde uno de los elementos del repositorio.
	 * 
	 * @param message el mensaje enviado por un elemento concreto del repositorio.
	 */
	public void notify(String message);
}