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
 * Define la interfaz que deben implementar los <i>listeners</i> que deban
 * registrar los mensajes lanzados por los elementos concretos del repositorio
 * que componen una refactorización.
 * 
 * <p>Desempeña el papel de Observador Abstracto en el patrón de diseño 
 * Observador.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface IRefactoringMessageListener {
	
	/**
	 * Notifica al <i>listener</i> que se ha enviado un mensaje desde uno de los
	 * elementos concretos del repositorio que componen la refactorización.
	 * 
	 * @param message el mensaje enviado.
	 */
	public void messageSent(String message);

}