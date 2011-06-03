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

package dynamicrefactoring.integration.selectionhandler;

import java.io.IOException;

import javamoon.core.JavaMoonFactory;
import moon.core.ObjectMoon;
import moon.core.classdef.ClassDef;
import moon.core.instruction.CodeFragment;

import org.eclipse.jface.text.TextSelection;

import dynamicrefactoring.util.selection.TextSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener el c�digo de fragmento de MOON
 * con el que se corresponde el texto seleccionado en Eclipse.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class TextCodeFragmentSelectionHandler implements ISelectionHandler {
	
	/**
	 * El CodeFragment de MOON que representa el fragmento seleccionado en Eclipse.
	 */
	protected CodeFragment codeFragment;
	
	/**
	 * El proveedor de información concreto para la selecci�n de texto.
	 */
	private TextSelectionInfo infoProvider;
	
	/**
	 * Constructor.
	 * 
	 * @param selectionInfo contenedor de la selecci�n que se desea manejar.
	 * 
	 * @throws Exception si la selecci�n contenida en #selectionInfo no es una
	 * selecci�n de una representación textual.
	 */
	public TextCodeFragmentSelectionHandler (TextSelectionInfo selectionInfo)
		throws Exception{
		
		if (! selectionInfo.isTextSelection())
			throw new Exception(
					Messages.TextCodeFragmentSelectionHandler_InvalidSelection);
		
		infoProvider = selectionInfo;
	}

	/**
	 * @see ClassSelectionHandler#getMainObject()
	 */
	@Override
	public ObjectMoon getMainObject() throws ClassNotFoundException, IOException {
		if (codeFragment == null){
			try{
				int characters=0;
				int beginColumn=0;
				int endColumn=0;
				ClassDef clase=null;
				clase=SelectionClassFinder.getTextSelectionClass(
						clase, infoProvider);
				TextSelection selec= (TextSelection)infoProvider.getSelection();
				String source = infoProvider.getCompilationUnit().getSource();
				for(int i=0; i<selec.getStartLine(); i++){
					characters += source.substring(0,source.indexOf('\n')).length()+1;
					source=source.substring(source.indexOf('\n')+1);
				}
				
				if(source.startsWith("\t")){
					String ultimo=source.substring(1);
					beginColumn=selec.getOffset()-characters+3;
					while(ultimo.startsWith("\t")){
						beginColumn+=3;
						ultimo=ultimo.substring(1);
					}
				}else{
					beginColumn=selec.getOffset()-characters;
				}
				for(int i=0; i<selec.getEndLine()-selec.getStartLine(); i++){
					characters += source.substring(0, source.indexOf('\n')).length()+1;
					source=source.substring(source.indexOf('\n')+1);
				}
				if(source.startsWith("\t")){
					source=source.substring(1);
					endColumn=3+selec.getOffset()+selec.getText().length()-characters;
					while(source.startsWith("\t")){
						endColumn+=3;
						source=source.substring(1);
					}
				}else{
					endColumn=selec.getOffset()+selec.getText().length()-characters;
				}
				codeFragment=JavaMoonFactory.getInstance().createCodeFragment(selec.getStartLine()+1
						,beginColumn+1, selec.getEndLine()+1, endColumn+1, clase
						, selec.getText());

			}catch(Exception e){}
		}
		return codeFragment;
	}
}