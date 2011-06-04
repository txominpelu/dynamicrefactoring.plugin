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
import java.util.List;

import moon.core.ObjectMoon;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;

import org.eclipse.jdt.core.ILocalVariable;

import repository.moon.concretefunction.MethodRetriever;
import dynamicrefactoring.util.processor.JavaLocalVariableProcessor;
import dynamicrefactoring.util.processor.JavaMethodProcessor;
import dynamicrefactoring.util.selection.TextSelectionInfo;

/**
 * Proporciona las funciones necesarias para obtener el argumento formal de un
 * método MOON con el que se corresponde un argumento formal seleccionado en 
 * Eclipse.
 * 
 * @author <A HREF="mailto:sfd0009@alu.ubu.es">Sonia Fuente de la Fuente</A>
 * @author <A HREF="mailto:ehp0001@alu.ubu.es">Enrique Herrero Paredes</A>
 */
public class FormalArgumentSelectionHandler implements ISelectionHandler {
	
	/**
	 * El proveedor de información concreto para la selección de texto.
	 */
	private TextSelectionInfo infoProvider;
	
	/**
	 * Procesador de información de la variable local con que se identifica el
	 * argumento formal seleccionado.
	 */
	private JavaLocalVariableProcessor variableProcessor;
	
	/**
	 * La descripción MOON del argumento formal seleccionado.
	 */
	private FormalArgument formalArgument;
	
	/**
	 * La descripción MOON del método al que pertenece el argumento formal.
	 */
	private MethDec method;
	
	/**
	 * La descripción MOON de la clase a la que pertenece el método uno de cuyos
	 * argumentos formales se ha seleccionado.
	 */
	private ClassDef methodClass;
	
	/**
	 * Constructor.
	 * 
	 * @param selectionInfo contenedor de la selección que se desea manejar.
	 * 
	 * @throws Exception si la selección contenida en #selectionInfo no es una
	 * selección de un argumento formal sobre una representación textual.
	 */
	public FormalArgumentSelectionHandler (TextSelectionInfo selectionInfo) 
		throws Exception{
		
		if (! selectionInfo.isFormalArgumentSelection())
			throw new Exception(
				Messages.FormalArgumentSelectionHandler_InvalidSelection
				+ Messages.FormalArgumentSelectionHandler_FormalArgumentExpected);
		if (! selectionInfo.isTextSelection())
			throw new Exception(
				Messages.FormalArgumentSelectionHandler_InvalidSelection
				+ Messages.FormalArgumentSelectionHandler_TextExpected);
		
		infoProvider = selectionInfo;
		
		variableProcessor = 
			new JavaLocalVariableProcessor(
				(ILocalVariable)infoProvider.getSelectedJavaElement());
	}
	
	/**
	 * Obtiene la descripción MOON del argumento formal representado por una 
	 * selección del interfaz gráfico.
	 * 
	 * @return la descripción MOON del argumento formal representado por una 
	 * selección del interfaz gráfico.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase
	 * del argumento formal en el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 * 
	 * @see ISelectionHandler#getMainObject()
	 */
	@Override
	public ObjectMoon getMainObject() 
		throws IOException, ClassNotFoundException {
		
		if (formalArgument == null){
			MethDec parentMethod = getFormalArgumentMethod();
			
			List<FormalArgument> arguments = parentMethod.getFormalArgument();
			
			for(FormalArgument next : arguments)
				if (next.getUniqueName().toString().equals(
					variableProcessor.getUniqueName())){
					formalArgument = next;
					break;
				}
		}
		
		return formalArgument;
	}
	
	/**
	 * Obtiene la descripción MOON del método al que pertenece el argumento formal
	 * seleccionado.
	 * 
	 * @return la descripción MOON del método al que pertenece el argumento formal
	 * seleccionado.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase a
	 * la que pertenece el método en el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 */
	public MethDec getFormalArgumentMethod() 
		throws IOException, ClassNotFoundException {
		
		if (method == null){
			
			JavaMethodProcessor methodProcessor = 
				variableProcessor.getMethodProcessor();
			
			String uniqueName = methodProcessor.getUniqueName();
			
			method = (MethDec) new MethodRetriever(
				getMethodClass(), uniqueName).getValue();
		}			
		return method;
	}
	
	/**
	 * Obtiene la descripción MOON de la clase a la que pertenece el método uno de
	 * cuyos argumentos formales se ha seleccionado.
	 * 
	 * @return la descripción MOON de la clase a la que pertenece el método uno de
	 * cuyos argumentos formales se ha seleccionado.
	 * 
	 * @throws ClassNotFoundException si se no se consigue encontrar la clase en
	 * el modelo MOON cargado.
	 * @throws IOException si se produce algún error al acceder al modelo MOON.
	 */
	public ClassDef getMethodClass() throws ClassNotFoundException, IOException {
		if (methodClass == null)
			methodClass = SelectionClassFinder.getTextSelectionClass(
				methodClass, infoProvider);
		return methodClass;
	}
}