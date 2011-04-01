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

package dynamicrefactoring.listener;

import dynamicrefactoring.domain.RefactoringSummary;

/**
 * Define la interfaz com�n a todos los objetos que deseen actuar como 
 * observadores del proceso de refactorizaci�n.
 * 
 * <p>Define m�todos suficientes para hacer un seguimiento efectivo del proceso
 * desde el momento en que se inicia, hasta que termina, incluyendo m�todos de
 * notificaci�n espec�ficos para aquellos casos en los que la refactorizaci�n
 * falle o sea deshecha, as� como para enviar mensajes detallados a medida que
 * tienen lugar cada uno de los pasos que componen una refactorizaci�n.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface IRefactoringRunListener {
	
	/**
	 * Notifica al <i>listener</i> acerca del comienzo de una refactorizaci�n.
	 * 
	 * @param name nombre de la refactorizaci�n que ha comenzado.
	 */
	public void refactoringStarted(String name);
	
	/**
	 * Notifica al <i>listener</i> acerca de la finalizaci�n de una refactorizaci�n.
	 * 
	 * @param summary resumen de la refactorizaci�n que ha finalizado.
	 */
	public void refactoringFinished(RefactoringSummary summary);

	/**
	 * Notifica al <i>listener</i> acerca de un fallo durante una refactorizaci�n.
	 * 
	 * @param name nombre de la refactorizaci�n que ha fallado.
	 * @param message mensaje asociado al fallo ocurrido.
	 */
	public void refactoringFailed(String name, String message);

	/**
	 * Notifica al <i>listener</i> acerca de la realizaci�n de un paso concreto en la 
	 * ejecuci�n de una refactorizaci�n.
	 * 
	 * @param message el mensaje asociado al paso llevado a cabo.
	 */
	public void refactoringStepTaken(String message);
	
	/**
	 * Notifica al <i>listener</i> acerca del hecho de que se haya recuperado el
	 * estado anterior a una refactorizaci�n.
	 * 
	 * @param name identificador de la refactorizaci�n cuyo estado anterior
	 * se ha recuperado.
	 */
	public void refactoringUndone(String name);
}