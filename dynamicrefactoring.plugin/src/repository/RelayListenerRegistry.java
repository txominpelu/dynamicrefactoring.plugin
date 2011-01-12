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

import org.eclipse.core.runtime.ListenerList;

/**
 * Proporciona las funciones de registro y notificación de <i> listeners</i> que 
 * deban registrar los mensajes enviados desde los elementos del repositorio.
 * 
 * <p>Se limita a reenviar todos los mensajes recibidos a su lista de 
 * <i>listeners</i> registrados.
 * 
 * <p>Desempeña el papel de Sujeto Concreto en el patrón de diseño Observador.</p>
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RelayListenerRegistry implements IListenerRegistry {

	/**
	 * Conjunto de <i>listeners</i> a los que hay que reenviar los mensajes.
	 */
	private ListenerList listeners;
	
	/**
	 * Instancia única de la clase.
	 * 
	 * <p>Patrón de diseño Singleton.</p>
	 */
	private static RelayListenerRegistry myInstance;
	
	/**
	 * Constructor.
	 * 
	 * <p>Privado, según la estructura del patrón de diseño Singleton.</p>
	 */
	private RelayListenerRegistry(){
		listeners = new ListenerList();
	}
	
	/**
	 * Obtiene la instancia única del registro.
	 * 
	 * <p>Método definido en el patrón de diseño Singleton.</p>
	 * 
	 * @return la instancia única del registro.
	 */
	public static RelayListenerRegistry getInstance(){
		if (myInstance == null)
			myInstance = new RelayListenerRegistry();
		return myInstance;
	}
	
	/**
	 * Añade un <i>listener</i> a la lista de <i>listeners</i> registrados.
	 * 
	 * @param listener el <i>listener</i> que se debe añadir.
	 * 
	 * @see IListenerRegistry#add(IRefactoringMessageListener)
	 */
	@Override
	public void add(IRefactoringMessageListener listener){
		if (listener != null)
			listeners.add(listener);
	}
	
	/**
	 * Elimina un <i>listener</i> de la lista de <i>listeners</i> registrados.
	 * 
	 * @param listener el <i>listener</i> que se debe eliminar.
	 * 
	 * @see IListenerRegistry#remove(IRefactoringMessageListener)
	 */
	@Override
	public void remove(IRefactoringMessageListener listener){
		if (listener != null)
			listeners.add(listener);
	}
	
	/**
	 * Notifica a todos los <i>listeners</i> registrados acerca del envío de un
	 * mensaje desde uno de los elementos del repositorio.
	 * 
	 * @param message el mensaje enviado por un elemento concreto del repositorio.
	 * 
	 * @see IListenerRegistry#notify(String)
	 */
	@Override
	public void notify(String message){
		Object[] registeredListeners = listeners.getListeners();
		// Para cada listener.
		for (int i = 0; i < registeredListeners.length; ++i){
			((IRefactoringMessageListener)
				registeredListeners[i]).messageSent(message);
		}
	}
}