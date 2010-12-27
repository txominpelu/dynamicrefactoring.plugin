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

package dynamicrefactoring.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.util.io.FileManager;

/**
 * Se encarga de interpretar la información almacenada en el xml que guarda el plan de 
 * refactorizaciones actual.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class RefactoringPlanReader {
	
	/**
	 * Permite obtener el conjunto de refactorizaciones dinámicas que actualmente forman el plan
	 * de refactorización. 
	 * 
	 * @param path ruta del fichero que contiene el plan de refactorizaciones.
	 * @return Conjunto de los nombres de las refactorizaciones del plan.
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 */
	public static ArrayList<String> readAllRefactoringsFromThePlan(String path) throws XMLRefactoringReaderException{
		ArrayList<String> refactorings = new ArrayList<String>();
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new File(path).toURI().toString());
			Element root = doc.getRootElement();
			
			for(int i=0;  i < root.getChildren().size(); i++){
				Element refactor = (Element)root.getChildren().get(i);
				Element name = refactor.getChild("name");
				String value = name.getAttribute("value").getValue();
				refactorings.add(value);
			}
		} 
		catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception.getMessage());
		} 
		catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception.getMessage());
		}catch (Exception exception){
			throw new XMLRefactoringReaderException(exception.getMessage());
		}
		return refactorings;
	}
	
	/**
	 * Permite obtener el conjunto de refactorizaciones dinámicas que actualmente forman el plan
	 * de refactorización. 
	 * 
	 * @return Conjunto de los nombres de las refactorizaciones del plan.
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException.
	 */
	public static ArrayList<String> readAllRefactoringsFromThePlan() throws XMLRefactoringReaderException{
		return readAllRefactoringsFromThePlan(RefactoringConstants.REFACTORING_PLAN_FILE);
	}
	
	/**
	 * Obtiene el valor de un determinado parámetro de una determinada refactorización.
	 * 
	 * @param refactoringName nombre de la refactorización.
	 * @param inputName nombre del parámetro.
	 * @param path fichero xml donde esta almacenado el plan
	 * @return valor del parámetro
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException
	 */
	public static String getInputValue(String refactoringName,String inputName, String path)throws XMLRefactoringReaderException {
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new File(FileManager.AbsoluteToRelative
					(new File(path).getAbsolutePath())));
			Element root = doc.getRootElement();
			for(int i=0;  i < root.getChildren().size(); i++){
				Element refactor = (Element)root.getChildren().get(i);
				Element name = refactor.getChild("name");
				String value = name.getAttribute("value").getValue();
				if(value.equals(refactoringName)){
					Element parameters = refactor.getChild("parameters");
					for(int j=0;  j < parameters.getChildren().size(); j++){
						Element parameter = (Element)parameters.getChildren().get(j);
						String p_name = parameter.getAttribute("name").getValue();
						if(p_name.equals(inputName))
							return parameter.getAttribute("value").getValue();
					}
				}
			}
		} 
		catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception.getMessage());
		} 
		catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception.getMessage());
		}catch (Exception exception){
			throw new XMLRefactoringReaderException(exception.getMessage());
		}
		return "";
	}

}
