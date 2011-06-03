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

package dynamicrefactoring.domain.xml.writer;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.domain.xml.reader.XMLRefactoringReaderException;


/**
 * Se encarga escribir la información almacenada en el xml que guarda el plan de 
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public final class RefactoringPlanWriter {
	
	private static final String NAME = "name";

	private static final String VALUE = "value";

	/**
	 * El formato del fichero XML.
	 */
	private Format format;
	
	/**
	 * Instancia única de la clase.
	 * 
	 * Patr�n de dise�o Singleton.
	 */
	private static RefactoringPlanWriter myInstance;
	
	/**
	 * Constructor.
	 * 
	 * Privado, seg�n la estructura del patr�n de dise�o Singleton.
	 */
	private RefactoringPlanWriter(){
		Element root = new Element("RefactoringPlan");
		
		DocType type = new DocType(root.getName(), 
		"refactoringPlanDTD.dtd"); 
		
		Document newdoc = new Document(root,type);
		
		initializeFormat(); 
		
		try{
			
		writeToFile(RefactoringConstants.REFACTORING_PLAN_FILE, newdoc);
		
		}catch(IOException e){
			
		}
	}

	/**
	 * Obtiene la instancia única del generador.
	 * 
	 * Patr�n de dise�o Singleton.
	 * 
	 * @return la instancia única del generador.
	 */
	public static RefactoringPlanWriter getInstance(){
		if (myInstance == null){
			myInstance = new RefactoringPlanWriter();
		}
		return myInstance;
	}
	
	/**
	 * Inicializa las opciones de formato del fichero XML.
	 */
	private void initializeFormat() {		
		format = Format.getPrettyFormat();
		format.setIndent("\t"); //$NON-NLS-1$
		format.setLineSeparator("\n"); //$NON-NLS-1$
		format.setExpandEmptyElements(false);
		format.setEncoding("ISO-8859-1"); //$NON-NLS-1$
	}
	
	/**
	 * Escribe los datos del documento XML en el fichero indicado.
	 * 
	 * @param fname el nombre del fichero.
	 * @param doc el documento XML.
	 * 
	 * @throws IOException si se produce un error de lectura escritura al 
	 * trasladar los datos del documento XML al fichero.
	 */
	private void writeToFile(String fname, Document doc) throws IOException {
		
		FileOutputStream out = new FileOutputStream(fname);
		XMLOutputter op = new XMLOutputter(format);
		
		op.output(doc, out);
		out.flush();
		out.close();
	}
	
	/**
	 * Escribe los datos de una refactorización dentro del fichero que guarda el plan de 
	 * refactorizaciones.
	 * 
	 * @param refactoringName Nombre de la refactorización.
	 * @param fecha fecha en la ques e ejecuta la refactorización.
	 * @param InputsParameters Parámetros de entrada de la refactorización.
	 */
	public void writeRefactoring (String refactoringName,String fecha, Map<String, String> InputsParameters ){
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			// El atributo SYSTEM del DOCTYPE de la definición XML de la
			// refactorización es solo la parte relativa de la ruta del fichero
			// DTD. Se le antepone la ruta del directorio del plugin que 
			// contiene los ficheros de refactorizaciones din�micas.
			Document doc = builder.build(new File(RefactoringConstants
					.REFACTORING_PLAN_FILE).toURI().toString());;
			Element root = doc.getRootElement();
			Element refactoring = new Element("refactoring");
			
			Element name = new Element(NAME);
			name.setAttribute(VALUE, refactoringName);
			refactoring.addContent(name);
			
			Element date = new Element("date");
			date.setAttribute(VALUE, fecha);
			refactoring.addContent(date);
			
			Element parameters = new Element("parameters");
			for(Map.Entry<String ,String> param : InputsParameters.entrySet()){
				Element parameter = new Element("parameter");
				parameter.setAttribute(NAME, param.getKey());
				if(!param.getKey().equals("Model")){
						parameter.setAttribute(VALUE, param.getValue());
						parameters.addContent(parameter);
				}
			}
			refactoring.addContent(parameters);
			root.addContent(refactoring);	
			writeToFile(RefactoringConstants.REFACTORING_PLAN_FILE, doc);
		} 
		catch (JDOMException jdomexception) {
			
		} 
		catch (IOException ioexception) {
			
		}
	}
	
	/**
	 * Elimina una refactorización y las siguientes (ya que al deshacer la primera se deshacen 
	 * el resto) del fichero xml que contiene el plan de refactorizaciones.
	 * 
	 * @param refactoringName refactorización a eliminar
	 * @param fecha fecha de ejecuci�n de la refactorización.
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException
	 */
	public void deleteRefactorings (String refactoringName, String fecha) throws XMLRefactoringReaderException {
		try {
			SAXBuilder builder = new SAXBuilder(true);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new File(RefactoringConstants
					.REFACTORING_PLAN_FILE).toURI().toString());
			Element root = doc.getRootElement();
			boolean encontrado = false;
			ArrayList<Element> remove = new ArrayList<Element>();
			for(int i=0;  i < root.getChildren().size(); i++){
				Element refactor = (Element)root.getChildren().get(i);
				if(encontrado){
					remove.add(refactor);
				}else{
					Element name = refactor.getChild(NAME);
					String value = name.getAttribute(VALUE).getValue();
					if(value.equals(refactoringName)){
						Element date = refactor.getChild("date");
						String dValue = date.getAttribute(VALUE).getValue();
						if(dValue.equals(fecha)){
							encontrado=true;
							remove.add(refactor);
						}	
					}
				}	
			}
			for(Element refactor : remove){
				root.removeContent(refactor);
			}
			writeToFile(RefactoringConstants.REFACTORING_PLAN_FILE, doc);
		} 
		catch (JDOMException jdomexception) {
			throw new XMLRefactoringReaderException(jdomexception);
		} 
		catch (IOException ioexception) {
			throw new XMLRefactoringReaderException(ioexception);
		}
	}
	
	/**
	 * La instancia de esta clase pasa a ser nulo.
	 */
	public static void reset(){
		myInstance=null;
	}

}
