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

package dynamicrefactoring;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Permite mantener un registro de los renombrados de clase que han tenido 
 * lugar sobre las clases del proyecto manejado.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class RenamingRegistry {

	/**
	 * Instancia única del registro.
	 */
	private static RenamingRegistry myInstance;
	
	/**
	 * Mapeo de refactorizaciones y operaciones de renombrado que desencadenaron.
	 */
	private Hashtable<String, String> bindings;
	
	/**
	 * Tabla asociativa que relaciona una clase con todos los nombres que ha
	 * recibido a lo largo de la ejecución actual de Eclipse.
	 * 
	 * <p>Se utiliza como clave el nombre único original de la clase, y como 
	 * valor, una lista con todos los nombres únicos que ha recibido después
	 * como consecuencia de renombrados.</p>
	 */
	private Hashtable<String, ArrayList<String>> renamings;

	/**
	 * Tabla asociativa que relaciona una clase con el nombre que tenía
	 * inmediatamente antes, antes de una única operación de renombrado.
	 * 
	 * <p>Se utiliza como clave el nombre único de la clase después del 
	 * renombrado, y como valor, el nombre único original.</p>
	 */
	private Hashtable<String, String> reverse;
	
	/**
	 * Último renombrado ejecutado, aún sin refactorización asociada.
	 */
	private String last = new String();
	
	/**
	 * Constructor.
	 */
	private RenamingRegistry(){
		bindings = new Hashtable<String, String>();
		renamings = new Hashtable<String, ArrayList<String>>();
		reverse = new Hashtable<String, String>();
	}
	
	/**
	 * Obtiene la instancia única del registro.
	 * 
	 * @return la instancia única del registro.
	 */
	public static RenamingRegistry getInstance(){
		if (myInstance == null)
			myInstance = new RenamingRegistry();
		return myInstance;
	}
	
	/**
	 * Añade una operación de renombrado pendiente de ser asociada a una 
	 * refactorización. 
	 * 
	 * @param id etiqueta identificativa de la operación de renombrado que
	 * se añade.
	 */
	public void addRenamingOperation(String id){
		last = id;
	}
	
	/**
	 * Asocia una operación de refactorización a la última operación de renombrado
	 * pendiente.
	 * 
	 * <p>Si se recibe una llamada pero no hay ninguna operación de renombrado libre
	 * almacenada en {@link #last}, no se realiza ninguna asociación.</p>
	 * 
	 * @param refactoringId etiqueta identificativa de la operación de 
	 * refactorización que se debe asociar al último renombrado ejecutado.
	 */
	public void bindRenaming(String refactoringId){
		if (last.length() > 0){
			bindings.put(refactoringId, last);
			last = ""; //$NON-NLS-1$
		}
	}
	
	/**
	 * Determina si existe una operación de renombrado pendiente de ser asociada
	 * a una refactorización.
	 * 
	 * @return <code>true</code> si se ha ejecutado una operación de renombrado y
	 * no se le ha asociado todavía ninguna refactorización; <code>false</code> en
	 * caso contrario.
	 */
	public boolean isPending(){
		return last.length() > 0;
	}

	/**
	 * Obtiene la operación de renombrado asociada a una cierta refactorización,
	 * identificada por su etiqueta de operación. 
	 * 
	 * <p>Si no se encuentra directamente una operación de renombrado asociada,
	 * busca entre el resto de renombrados el primero que pudiera haber tenido
	 * lugar después de la refactorización señalada, y lo devuelve.</p> 
	 * 
	 * @param id etiqueta identificativa de la refactorización cuya operación de
	 * renombrado asociada se quiere obtener.
	 * 
	 * @return la etiqueta identificativa de la operación de renombrado asociada a
	 * la refactorización, o la del primer renombrado que tuviera lugar tras ella,
	 * o <code>null</code> si no se encuentra ninguno de ambos.
	 */
	public String getRenaming(String id){
		// Si se encuentra un renombrado directamente asociado.
		String renaming = bindings.get(id);
		if (renaming != null)
			return renaming;
		
		String[] best = null;
		String refactDate = id.substring(id.lastIndexOf("(") + 1); //$NON-NLS-1$
		ArrayList<String> later = new ArrayList<String>();
		// Para cada renombrado almacenado.
		for (String nextRenaming : bindings.keySet()){
			// Se toma su parte correspondiente a la hora.
			String renameDate = nextRenaming.substring(nextRenaming.lastIndexOf("(") + 1); //$NON-NLS-1$
			
			// Si la refactorización es anterior.
			if (refactDate.compareTo(renameDate) < 0){
				// El renombrado deberá eliminarse después de la lista.
				later.add(nextRenaming);
				// Si está más cerca de la refactorización que el mejor hasta ahora.
				if (best == null || best[1].compareTo(renameDate) > 0)
					// Será el punto en que se deshaga el renombrado.
					best = new String[] {bindings.get(nextRenaming), renameDate};
			}
		}
		
		// Se eliminan todos los renombrados posteriores.
		for (String outdated : later)
			bindings.remove(outdated);
		
		return (best != null) ? best[0] : null;
	}
	
	/**
	 * Añade un nuevo renombrado a las tablas asociativas.
	 * 
	 * @param oldName nombre único de la clase previo al renombrado.
	 * @param newName nombre único de la clase tras el renombrado.
	 */
	public void addRenaming(String oldName, String newName){
		addRenaming(oldName, newName,new ArrayList<String>());
	}
	
	/**
	 * Añade un nuevo renombrado a las tablas asociativas.
	 * 
	 * @param oldName nombre único de la clase previo al renombrado.
	 * @param newName nombre único de la clase tras el renombrado.
	 * @param previousPreName valores anteriores que ha tomado la variable local preName.
	 */
	public void addRenaming(String oldName, String newName, ArrayList<String> previousPreName){
		ArrayList<String> renamed = renamings.get(oldName);
		if (renamed == null){
			renamed = new ArrayList<String>();
			renamed.add(newName);
			renamings.put(oldName, renamed);
		}
		else 
			renamed.add(newName);
		
		if (reverse.get(newName) == null)
			reverse.put(newName, oldName);
		String preName = reverse.get(oldName);
		
		
		if (preName != null && !previousPreName.contains(preName)){
			ArrayList<String> previous = new ArrayList<String>();
			previous.addAll(previousPreName);
			previous.add(preName);
			addRenaming(preName, newName,previous);
		}
	}

	/**
	 * Obtiene el nombre que tiene actualmente una clase identificada por su 
	 * nombre único original al que se desea restaurar. 
	 * 
	 * <p>Elimina de las tablas asociativas de renombrados todos los pasos 
	 * intermedios entre el estado actual de la clase y el original.</p>
	 * 
	 * @param oldName nombre único que tenía la clase en el estado original al
	 * que se debería restaurar.
	 * 
	 * @return el nombre único que tiene la clase en el momento de la llamada.
	 */
	public String update(String oldName){
		// Se obtiene el nombre actual de la clase que se llamaba #oldName.
		String currentName = null;

		ArrayList<String> names = renamings.get(oldName);

		if (names != null)
			currentName = names.get(names.size() - 1);
		
		// Se obtienen todos sus nombres anteriores.
		ArrayList<String> oldNames = new ArrayList<String>();
		String previous = reverse.get(currentName);
		while(previous != null && !oldNames.contains(previous)){
			oldNames.add(0, previous);
			previous = reverse.get(previous);
		}

		
		// Para cada nombre anterior de la clase.
		for (String name : oldNames){
			// Se obtiene su lista de sucesores directos e indirectos.
			ArrayList<String> renamed = renamings.get(name);
			for (int i = renamed.size() - 1; i > -1 &&
				! renamed.get(i).equals(oldName); i--)
				// Se eliminan todos los sucesores desde el que es el nombre actual
				// hasta justo el siguiente del nombre viejo que se va a recuperar.
				renamed.remove(i);
			if (renamed.isEmpty())
				renamings.remove(name);
		}
		
		reverse.remove(currentName);
		// Para cada nombre anterior de la clase, empezando por el último.
		for (int i = oldNames.size() - 1; i > -1; i--){
			// Si el nombre no es el que se va a recuperar.
			if (! oldNames.get(i).equals(oldName))
				// Se elimina de la lista inversa.
				reverse.remove(oldNames.get(i));
			else
				// Cuando se llega al que se va a recuperar, se para.
				break;
		}
			
		return currentName;
	}
}