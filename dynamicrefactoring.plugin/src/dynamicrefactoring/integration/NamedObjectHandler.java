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

package dynamicrefactoring.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import moon.core.NamedObject;

/**
 * Proporciona funciones de utilidad para el manejo de objetos con nombre de 
 * MOON (<code>NamedObject</code>).
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class NamedObjectHandler {

	/**
	 * Obtiene una lista ordenada con los nombres únicos o representativos de cada
	 * uno de los elementos de una lista de objetos.
	 * 
	 * <p>El procedimiento seguido es el siguiente:
	 * <li>
	 * <ol>Primero se comprueba si todos los elementos de la lista son objetos de
	 * subtipos de <code>moon.core.NamedObject</code>. Si es así, se ordena la 
	 * lista en base a sus nombres únicos y se devuelve una lista ordenada con dichos
	 * nombres.</ol>
	 * <ol>Si la comprobación anterior falló, se ordena la lista basíndose en las
	 * cadenas de representación obtenidas por el método <code>toString()</code> para
	 * cada uno de los objetos de la misma. Finalmente, se devuelve una lista 
	 * ordenada con dichas representaciones como cadenas de caracteres.</ol>
	 * </li></p>
	 * 
	 * @param list lista original de elementos del modelo.
	 * 
	 * @return una lista ordenada de cadenas de texto con los nombres únicos de los
	 * objetos de la lista original (si lo tienen todos ellos) o con sus 
	 * representaciones textuales (si no).
	 */
	public static List<String> getSortedNameList(List<Object> list){
		
		ArrayList<String> nameList = new ArrayList<String>();
		
		// Si todos son objetos moon.core.NamedObject.
		if (isNamedObjectList(list)){
			// Se ordena la lista utilizando sus nombres únicos.
			sortNamedObjectList(list);			
			for (Object next : list)
				nameList.add(((NamedObject)next).getUniqueName().toString());
		}
		else {
			sortGeneralList(list);
			for (Object next : list)
				nameList.add(next.toString());
		}
		
		return nameList;
	}

	/**
	 * Ordena una lista de objetos en función de su representación obtenida 
	 * mediante el método <code>toString</code>.
	 * 
	 * @param list la lista que se debe ordenar.
	 */
	private static void sortGeneralList(List<Object> list) {
		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object a, Object b) {
				return a.toString().compareTo(b.toString());
			}
		});
	}

	/**
	 * Ordena una lista de objetos basíndose en la suposición de que todos sus
	 * elementos pertenecen a subtipos de <code>moon.core.NamedObject</code>. El
	 * criterio de ordenación es ascendente según el nombre único de los objetos.
	 * 
	 * @param list la lista cuyos elementos se deben ordenar.
	 */
	private static void sortNamedObjectList(List<Object> list) {
		Collections.sort(list, new Comparator<Object>() { 
			@Override
			public int compare(Object a, Object b) { 
				return ((NamedObject)a).getUniqueName().compareTo(
					((NamedObject)b).getUniqueName());					
			}
		});
	}

	/**
	 * Obtiene una lista ordenada con los elementos de una lista de objetos.
	 * 
	 * <p>El procedimiento seguido es el siguiente:
	 * <li>
	 * <ol>Primero se comprueba si todos los elementos de la lista son objetos de
	 * subtipos de <code>moon.core.NamedObject</code>. Si es así, se ordena la 
	 * lista en base a sus nombres únicos.</ol>
	 * <ol>Si la comprobación anterior falló, se ordena la lista basíndose en las
	 * cadenas de representación obtenidas por el método <code>toString()</code> para
	 * cada uno de los objetos de la misma.</ol>
	 * </li></p>
	 * 
	 * @param list lista original de elementos del modelo.
	 * 
	 * @return una lista ordenada con objetos de la lista original .
	 */
	public static List<Object> getSortedList(List<Object> list){
		
		// Si todos son objetos moon.core.NamedObject.
		if (isNamedObjectList(list))
			// Se ordena la lista utilizando sus nombres únicos.
			sortNamedObjectList(list);
		else 
			sortGeneralList(list);
					
		return list;
	}

	/**
	 * Comprueba si una lista de objetos está compuesta exclusivamente por objetos
	 * de algún subtipo de <code>moon.core.NamedObject</code>.
	 * 
	 * @param list lista objeto de la comprobación.
	 * 
	 * @return <code>true</code> si todos los elementos de la lista heredan de
	 * <code>moon.core.NamedObject</code>; <code>false</code> en caso contrario.
	 */
	private static boolean isNamedObjectList(List<Object> list) {
		boolean isNamed = true;
	
		// Se comprueba si todos los elementos de la lista son subtipos de
		// moon.core.NamedObject.
		for (Object next : list)
			if (! NamedObject.class.isAssignableFrom(next.getClass()))
				isNamed = false;
		return isNamed;
	}
	
	/**
	 * Obtiene la representación más adecuada del nombre de un objeto.
	 * 
	 * <p>Si se trata de un objeto de algún subtipo de <code>NamedObject</code>,
	 * devuelve su nombre único como una cadena de caracteres. Si no, simplemente
	 * devuelve el resultado de la llamada a <code>toString()</code> sobre el 
	 * objeto en cuestión.</p>
	 * 
	 * @param object objeto cuyo nombre se debe obtener.
	 * 
	 * @return una cadena de caracteres con el nombre único del objeto o con su
	 * representación textual, si no es un objeto con nombre del modelo.
	 */
	public static String getName(Object object){
		return (NamedObject.class.isAssignableFrom(object.getClass())) ? 
			((NamedObject)object).getUniqueName().toString() : 
				object.toString();
	}
}