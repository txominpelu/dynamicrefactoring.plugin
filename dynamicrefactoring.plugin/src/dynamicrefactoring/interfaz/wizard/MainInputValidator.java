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

package dynamicrefactoring.interfaz.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Proporciona la capacidad de comprobar que un determinado tipo, identificado
 * por su nombre completamente cualificado, es apto para ser admitido como 
 * entrada principal de una refactorización.  
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class MainInputValidator {
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(MainInputValidator.class);
	
	/**
	 * Nombre completamente cualificado de la superclase común a todos los elementos
	 * que representen clases en el modelo MOON y sus extensiones.
	 */
	public static final String CLASSDEF = "moon.core.classdef.ClassDef"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado de la superclase común a todos los elementos
	 * que representen métodos en el modelo MOON y sus extensiones.
	 */
	public static final String METHDEC = "moon.core.classdef.MethDec"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado de la superclase común a todos los elementos
	 * que representen atributos en el modelo MOON y sus extensiones.
	 */
	public static final String ATTDEC = "moon.core.classdef.AttDec"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado de la superclase común a todos los elementos
	 * que representen argumentos formales en el modelo MOON y sus extensiones.
	 */
	public static final String FORMALARG = "moon.core.classdef.FormalArgument"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado de la superclase común a todos los elementos
	 * que representen parámetros formales en el modelo MOON y sus extensiones.
	 */
	public static final String FORMALPAR = "moon.core.genericity.FormalPar"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado de la superclase común a todos los elementos
	 * que representen un fragmento de código en el modelo MOON y sus extensiones.
	 */
	public static final String CODEFRAGMENT = "moon.core.instruction.CodeFragment"; //$NON-NLS-1$
	
	/**
	 * Nombres completamente cualificados de los tipos que se admiten como 
	 * entrada principal de una refactorización.
	 */
	private ArrayList<String> validMain;
	
	/**
	 * Nombre de la categoria de la clasificación scope a la que pertenecen
	 * todos los elementos que representen clases en el modelo MOON y sus extensiones.
	 */
	private static final String CLASSDEF_SCOPECATEGORY = "Class"; //$NON-NLS-1$
	
	/**
	 * Nombre de la categoria de la clasificación scope a la que pertenecen 
	 * todos los elementos que representen métodos en el modelo MOON y sus extensiones.
	 */
	private static final String METHDEC_SCOPECATEGORY = "Method"; //$NON-NLS-1$
	
	/**
	 * Nombre de la categoria de la clasificación scope a la que pertenecen 
	 * todos los elementos que representen atributos en el modelo MOON y sus extensiones.
	 */
	private static final String ATTDEC_SCOPECATEGORY = "Attribute"; //$NON-NLS-1$
	
	/**
	 * Nombre de la categoria de la clasificación scope a la que pertenecen 
	 * todos los elementos que representen argumentos formales en el modelo MOON 
	 * y sus extensiones.
	 */
	private static final String FORMALARG_SCOPECATEGORY = "FormalArg"; //$NON-NLS-1$
	
	/**
	 * Nombre de la categoria de la clasificación scope a la que pertenecen 
	 * a todos los elementos que representen parámetros formales en el modelo MOON 
	 * y sus extensiones.
	 */
	private static final String FORMALPAR_SCOPECATEGORY = "FormalPar"; //$NON-NLS-1$
	
	/**
	 * Nombre de la categoria de la clasificación scope a la que pertenecen 
	 * a todos los elementos que representen un fragmento de código en el modelo MOON 
	 * y sus extensiones.
	 */
	private static final String CODEFRAGMENT_SCOPECATEGORY = "CodeFragment"; //$NON-NLS-1$
	
	/**
	 * Tabla en la que se almacena la correspondencia entre los tipos, complemtamente
	 * cualificados, que se admiten y la categoria scope a la que pertenecen.
	 */
	private Map<String, String> scopeCategories;
	
	/**
	 * Constructor.
	 */
	public MainInputValidator(){
		validMain = new ArrayList<String>();
		validMain.add(ATTDEC);
		validMain.add(CLASSDEF);
		validMain.add(FORMALARG);
		validMain.add(FORMALPAR);
		validMain.add(METHDEC);
		validMain.add(CODEFRAGMENT);
		
		scopeCategories=new HashMap<String, String>();
		scopeCategories.put(CLASSDEF, CLASSDEF_SCOPECATEGORY);
		scopeCategories.put(METHDEC, METHDEC_SCOPECATEGORY);
		scopeCategories.put(ATTDEC, ATTDEC_SCOPECATEGORY);
		scopeCategories.put(FORMALPAR, FORMALARG_SCOPECATEGORY);
		scopeCategories.put(FORMALPAR, FORMALPAR_SCOPECATEGORY);
		scopeCategories.put(CODEFRAGMENT, CODEFRAGMENT_SCOPECATEGORY);
	}
	
	/**
	 * Comprueba si el tipo referenciado por su nombre completamente cualificado
	 * conforma con alguno de los tipos válidos como entrada principal de una
	 * refactorización.
	 * 
	 * @param typeName nombre completamente cualificado del tipo.
	 * 
	 * @return <code>true</code> si el tipo es válido; <code>false</code> en caso
	 * contrario.
	 */
	public boolean checkMainType(String typeName) {
		try {
			Class<?> mainClass = Class.forName(typeName);
			for (String validType : validMain){
				Class<?> validClass = Class.forName(validType);
				if (validClass.isAssignableFrom(mainClass))
					return true;
			}
			return true;
		}
		catch (ClassNotFoundException exception){
			logger.error(
				Messages.MainInputValidator_ErrorLoading 
				+ ":\n\n" + exception.getMessage()); //$NON-NLS-1$
			return false;
		}
	}
	
	public String convertScopeCategory(String typeName) {
		try {
			Class<?> mainClass = Class.forName(typeName);
			for (String validType : validMain){
				Class<?> validClass = Class.forName(validType);
				if (validClass.isAssignableFrom(mainClass))
					return scopeCategories.get(validType);
			}
			return null;
		}
		catch (ClassNotFoundException exception){
			logger.error(
				Messages.MainInputValidator_ErrorLoading 
				+ ":\n\n" + exception.getMessage()); //$NON-NLS-1$
			return null;
		}
		
		
	}

}