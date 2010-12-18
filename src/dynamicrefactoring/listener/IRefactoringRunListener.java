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
 * Define la interfaz común a todos los objetos que deseen actuar como 
 * observadores del proceso de refactorización.
 * 
 * <p>Define métodos suficientes para hacer un seguimiento efectivo del proceso
 * desde el momento en que se inicia, hasta que termina, incluyendo métodos de
 * notificación específicos para aquellos casos en los que la refactorización
 * falle o sea deshecha, así como para enviar mensajes detallados a medida que
 * tienen lugar cada uno de los pasos que componen una refactorización.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public interface IRefactoringRunListener {
	
	/**
	 * Notifica al <i>listener</i> acerca del comienzo de una refactorización.
	 * 
	 * @param name nombre de la refactorización que ha comenzado.
	 */
	public void refactoringStarted(String name);
	
	/**
	 * Notifica al <i>listener</i> acerca de la finalización de una refactorización.
	 * 
	 * @param summary resumen de la refactorización que ha finalizado.
	 */
	public void refactoringFinished(RefactoringSummary summary);

	/**
	 * Notifica al <i>listener</i> acerca de un fallo durante una refactorización.
	 * 
	 * @param name nombre de la refactorización que ha fallado.
	 * @param message mensaje asociado al fallo ocurrido.
	 */
	public void refactoringFailed(String name, String message);

	/**
	 * Notifica al <i>listener</i> acerca de la realización de un paso concreto en la 
	 * ejecución de una refactorización.
	 * 
	 * @param message el mensaje asociado al paso llevado a cabo.
	 */
	public void refactoringStepTaken(String message);
	
	/**
	 * Notifica al <i>listener</i> acerca del hecho de que se haya recuperado el
	 * estado anterior a una refactorización.
	 * 
	 * @param name identificador de la refactorización cuyo estado anterior
	 * se ha recuperado.
	 */
	public void refactoringUndone(String name);
}