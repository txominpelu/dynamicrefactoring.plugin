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

package dynamicrefactoring.util.io.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Implementa un filtro de elecci�n de im�genes que permite seleccionar solamente
 * archivos de tipo imagen.
 * 
 * El criterio seguido es que los archivos tengan una de las extensiones asociadas
 * habitualmente a los ficheros de imagen.
 * 
 * @author <A HREF="mailto:alc0022@alu.ubu.es">Ángel López Campo</A>
 * @author <A HREF="mailto:epf0006@alu.ubu.es">Eduardo Peña Fernández</A>
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */	
public class ImageFilter extends FileFilter {
	
	/**
	 * Tipos de extensiones aceptadas para los archivos de imagen.
	 */
	public static final String[] extensions = {".tiff", ".tif", ".gif", ".jpeg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		".jpg", ".png"}; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Plantillas con alguna de las que deber�n corresponderse los archivos de
	 * imagen para pasar el filtro.
	 */
	public static final String[] templates = {"*.tiff", "*.tif", "*.gif", "*.jpeg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"*.jpg", "*.png"}; //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Descripciones breves de los tipos de archivo aceptados.
	 */
	public static final String[] descriptions = {
		Messages.ImageFilter_TIFF,
		Messages.ImageFilter_TIF,
		Messages.ImageFilter_GIF,
		Messages.ImageFilter_JPEG,
		Messages.ImageFilter_JPG,
		Messages.ImageFilter_PNG
	};

    /**
     * Acepta todos los directorios, y los ficheros <i>.gif</i>, <i>jpg</i>, 
     * <i>tiff</i>, o <i>png</i>. Acepta tambi�n sus versiones en may�sculas y
     * la versi�n de cuatro caracteres de <i>jpg</i>, <i>jpeg</i>.
     * 
     * @param f el fichero que debe pasar por el filtro.
     * 
     * @return <code>true</code> si el fichero es aceptado; <code>false</code> 
     * en caso contrario.
     */
	@Override
    public boolean accept(File f) {
        
    	if (f.exists() && f.isDirectory())
            return true;

        String extension = null;
        String filename = f.getName();
        int i = filename.lastIndexOf('.');

        if (i > 0 &&  i < filename.length() - 1)
            extension = filename.substring(i + 1).toLowerCase();
        
        if (extension != null)
        	for (int j = 0; j < extensions.length; j++)
        		if (extension.toLowerCase().equals(extensions[j]))
                    return true;
        
        return false;
    }

    /**
     * Devuelve la descripci�n del filtro.
     * 
     * @return una cadena con la descripci�n del filtro.
     */
	@Override
    public String getDescription() {        
    	return Messages.ImageFilter_OnlyImages;
    }
}