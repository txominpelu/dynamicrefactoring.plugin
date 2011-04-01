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

import java.util.Hashtable;

import org.eclipse.jdt.core.Signature;

/**
 * Proporciona funciones de conversi�n entre las convenciones de nombres de 
 * tipos utilizadas por el metamodelo MOON y por el metamodelo empleado de
 * forma interna por Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class TypeConversor {
	
	/**
	 * Signatura MOON del tipo primitivo booleano <code>boolean</code>.
	 */
	private final String MOON_BOOLEAN = "boolean"; //$NON-NLS-1$

	/**
	 * Signatura MOON del tipo primitivo byte <code>byte</code>.
	 */
	private final String MOON_BYTE = "byte"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo primitivo car�cter <code>char</code>.
	 */
	private final String MOON_CHAR = "char"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo primitivo decimal <code>double</code>.
	 */
	private final String MOON_DOUBLE = "double"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo primitivo decimal <code>float</code>.
	 */
	private final String MOON_FLOAT = "float"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo primitivo entero <code>int</code>.
	 */
	private final String MOON_INT = "int"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo primitivo entero largo <code>long</code>.
	 */
	private final String MOON_LONG = "long"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo primitivo entero corto <code>short</code>.
	 */
	private final String MOON_SHORT = "short"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura booleano <code>Boolean</code>.
	 */
	private final String MOON_BOOLEAN_WRAP = "Boolean"; //$NON-NLS-1$

	/**
	 * Signatura MOON del tipo de envoltura byte <code>Byte</code>.
	 */
	private final String MOON_BYTE_WRAP = "Byte"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura car�cter <code>Character</code>.
	 */
	private final String MOON_CHAR_WRAP = "Character"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura decimal <code>Double</code>.
	 */
	private final String MOON_DOUBLE_WRAP = "Double"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura decimal <code>Float</code>.
	 */
	private final String MOON_FLOAT_WRAP = "Float"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura entero <code>Integer</code>.
	 */
	private final String MOON_INT_WRAP = "Integer"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura entero largo <code>Long</code>.
	 */
	private final String MOON_LONG_WRAP = "Long"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo de envoltura entero corto <code>Short</code>.
	 */
	private final String MOON_SHORT_WRAP = "Short"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo cadena de caracteres <code>String</code>.
	 */
	private final String MOON_STRING = "String"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo objecto <code>Object</code>.
	 */
	private final String MOON_OBJECT = "Object"; //$NON-NLS-1$
	
	/**
	 * Signatura MOON del tipo excepci�n gen�rica <code>Exception</code>.
	 */
	private final String MOON_EXCEPTION = "Exception"; //$NON-NLS-1$
	
	/**
	 * Tabla de conversi�n entre los tipos primitivos utilizados por Eclipse 
	 * para la determinaci�n de las signaturas y los empleados en el modelo MOON.
	 * 
	 * Se utiliza como clave la cadena utilizada en la representaci�n de Eclipse.
	 * El valor asignado a cada clave es la cadena utilizada en el equivalente 
	 * en MOON.
	 */
	private Hashtable<String, String> primitiveConversionTable;
	
	/**
	 * Tabla de conversi�n entre los tipos de envoltura de tipos primitivos 
	 * utilizados por Eclipse para la determinaci�n de las signaturas y los
	 * empleados en el modelo MOON.
	 * 
	 * Se utiliza como clave la cadena utilizada en la representaci�n de Eclipse.
	 * El valor asignado a cada clave es la cadena utilizada en el equivalente
	 * en MOON.
	 */
	private Hashtable<String, String> wrapperConversionTable;
	
	/**
	 * Tabla de conversi�n de tipos no primitivos adicionales.
	 * 
	 * Se utiliza como clave la cadena utilizada en la representaci�n de Eclipse
	 * para los tipos no resueltos. El valor asignado a cada clave es la cadena
	 * utilizada en el equivalente MOON.
	 */
	private Hashtable<String, String> additionalConversions;
	
	/**
	 * Instancia �nica del conversor (patr�n Singleton).
	 */
	private static TypeConversor myInstance;
	
	/**
	 * Constructor.
	 * 
	 * Privado, siguiendo la estructura del patr�n Singleton.
	 */
	private TypeConversor(){
		primitiveConversionTable 	= new Hashtable<String, String>();
		wrapperConversionTable 		= new Hashtable<String, String>();
		additionalConversions 		= new Hashtable<String, String>();
		loadConversionTables();
	}

	/**
	 * Obtiene la instancia �nica del conversor.
	 * 
	 * M�todo definido por el patr�n de dise�o Singleton.
	 * 
	 * @return la instancia �nica del conversor.
	 */
	public static TypeConversor getInstance(){
		if (myInstance == null)
			myInstance = new TypeConversor();
		return myInstance;
	}
	
	/**
	 * Carga las tablas asociativas de conversi�n con los valores utilizados
	 * por la convenci�n de nombres de tipos del metamodelo MOON.
	 */
	private void loadConversionTables(){
		primitiveConversionTable.put(Signature.SIG_BOOLEAN, MOON_BOOLEAN);
		primitiveConversionTable.put(Signature.SIG_BYTE, MOON_BYTE);
		primitiveConversionTable.put(Signature.SIG_CHAR, MOON_CHAR);
		primitiveConversionTable.put(Signature.SIG_DOUBLE, MOON_DOUBLE);
		primitiveConversionTable.put(Signature.SIG_FLOAT, MOON_FLOAT);
		primitiveConversionTable.put(Signature.SIG_INT, MOON_INT);
		primitiveConversionTable.put(Signature.SIG_LONG, MOON_LONG);
		primitiveConversionTable.put(Signature.SIG_SHORT, MOON_SHORT);
		
		wrapperConversionTable.put("Boolean;", MOON_BOOLEAN_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Byte;", MOON_BYTE_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Character;", MOON_CHAR_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Double;", MOON_DOUBLE_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Float;", MOON_FLOAT_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Integer;", MOON_INT_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Long;", MOON_LONG_WRAP); //$NON-NLS-1$
		wrapperConversionTable.put("Short;", MOON_SHORT_WRAP); //$NON-NLS-1$
		
		additionalConversions.put("String;", MOON_STRING); //$NON-NLS-1$
		additionalConversions.put("Object;", MOON_OBJECT); //$NON-NLS-1$
		additionalConversions.put("Exception;", MOON_EXCEPTION); //$NON-NLS-1$
	}
	
	/**
	 * Realiza la conversi�n entre la convenci�n utilizada por Eclipse para la
	 * representaci�n de las signaturas de los tipos y la utilizada por el modelo
	 * MOON.
	 * 
	 * @param type la cadena de caracteres con el tipo representado seg�n la 
	 * convenci�n utilizada por Eclipse.
	 * 
	 * @return la cadena de caracteres con la representaci�n del tipo seg�n la
	 * convenci�n utilizada en MOON.
	 * 
	 * @see org.eclipse.jdt.core.Signature
	 */
	public String convertType(String type){
		if(primitiveConversionTable.containsKey(type))
			return primitiveConversionTable.get(type);
		if(type.startsWith(Character.valueOf(Signature.C_ARRAY).toString()))
			return convertType(type.substring(1)) + "[]"; //$NON-NLS-1$
		
		if(type.contains(Character.valueOf(Signature.C_GENERIC_START).toString())
			&& type.endsWith(
				Character.valueOf(Signature.C_GENERIC_END).toString() + 
				Signature.C_SEMICOLON)){
			
			// MOON no incluye los tipos param�tricos en el nombre �nico.
			// Solo se incluye el nombre del tipo gen�rico, la clase.
			String parametricType = convertType(type.substring(
				0, type.indexOf(Signature.C_GENERIC_START)));
						
			return parametricType;
		}
							
		if(type.startsWith(Character.valueOf(Signature.C_UNRESOLVED).toString()))
			return convertNonPrimitiveType(type.substring(1));
		return null;
	
	}
	
	/**
	 * Realiza la conversi�n entre la convenci�n utilizada por Eclipse para la
	 * representaci�n de los tipos no primitivos en las signaturas y la utilizada
	 * por el modelo MOON.
	 * 
	 * @param npType la cadena de caracteres con el tipo representado seg�n la
	 * convenci�n utilizada por Eclipse, sin el prefijo que indica que se trata 
	 * de un tipo no resuelto ({@link Signature#C_UNRESOLVED}.
	 * 
	 * @return la cadena de caracteres con la representaci�n del tipo seg�n la
	 * convenci�n utilizada en MOON, si se pudo realizar la conversi�n. Si no,
	 * la cadena original.
	 * 
	 * @see org.eclipse.jdt.core.Signature
	 */
	private String convertNonPrimitiveType(String npType){
		if (wrapperConversionTable.containsKey(npType))
			return wrapperConversionTable.get(npType);
		if (additionalConversions.containsKey(npType))
			return additionalConversions.get(npType);
		return npType;
	}
}