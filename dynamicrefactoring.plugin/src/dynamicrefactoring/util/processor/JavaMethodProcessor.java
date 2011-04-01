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

package dynamicrefactoring.util.processor;

import org.eclipse.jdt.core.IMethod;

import dynamicrefactoring.integration.TypeConversor;

/**
 * Proporciona funciones que permiten manejar un método Java tal y como lo
 * define Eclipse en su representación interna.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class JavaMethodProcessor extends JavaElementProcessor {
	
	/**
	 * El método Java que se debe procesar.
	 */
	private IMethod method;
	
	/**
	 * Constructor.
	 * 
	 * @param method el método Java que se debe procesar.
	 */
	public JavaMethodProcessor(IMethod method){
		super(method);
		this.method = method;
	}
	
	/**
	 * Obtiene la posición relativa del método dentro del conjunto de métodos de
	 * la clase en que se define.
	 * 
	 * La numeración empieza en 1, no en 0. 
	 * 
	 * @return la posición relativa del método dentro del conjunto de métodos de 
	 * la clase en que se define.
	 */	
	public int getPosition(){
		return method.getOccurrenceCount();
	}

	/**
	 * Obtiene el nombre único del método según la convención de nomenclatura
	 * única utilizada en el modelo MOON.
	 * 
	 * @return el nombre único del método según la convención de nomenclatura
	 * única utilizada en el modelo MOON.
	 */
	@Override
	public String getUniqueName(){
		String uniqueName = ""; //$NON-NLS-1$
		
		uniqueName += 
			new JavaClassProcessor(method.getDeclaringType()).getUniqueName();
		uniqueName += "~" + method.getElementName(); //$NON-NLS-1$
		
		String[] parameters = method.getParameterTypes();
		String convertedParameter = ""; //$NON-NLS-1$
		TypeConversor conversor = TypeConversor.getInstance();
		
		for(int i = 0; i < parameters.length; i++){
			if(parameters[i].substring(1).equals(conversor.convertType(parameters[i]))){
				// Lo normal es que el nombre del tipo termine en ';', puesto que es 
				// la convención utilizada por Eclipse.
				if(parameters[i].endsWith(new Character(';').toString()))
					convertedParameter = 
						parameters[i].substring(1, parameters[i].lastIndexOf(';'));
				else
					convertedParameter = parameters[i];
			}
			else 
				convertedParameter = conversor.convertType(parameters[i]);
			
			// Si el parámetro es de un tipo con genericidad.
			if (convertedParameter.contains("<") && convertedParameter.contains(">")){ //$NON-NLS-1$ //$NON-NLS-2$
				String convertedType = 
					convertedParameter.substring(0, 
					convertedParameter.indexOf("<")); //$NON-NLS-1$
				
				// Se elimina del nombre único la parte de genericidad.
				convertedParameter = convertedType;
			}
			
			// Si el nombre del tipo del parámetro es un nombre completamente
			// cualificado, se elimina la parte referente a los paquetes.
			if (convertedParameter.contains(".")) //$NON-NLS-1$
				convertedParameter = convertedParameter.substring(
					convertedParameter.lastIndexOf('.') + 1);

			uniqueName += "%" + convertedParameter;  //$NON-NLS-1$
		}
		
		return uniqueName;
	}
}