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

package dynamicrefactoring.domain;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javamoon.core.JavaModel;
import javamoon.core.JavaMoonFactory;
import moon.core.Name;
import moon.core.NamedObject;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.instruction.CodeFragment;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.integration.CodeRegenerator;
import dynamicrefactoring.integration.ModelGenerator;
import dynamicrefactoring.interfaz.dynamic.DynamicRefactoringRunner;
import dynamicrefactoring.interfaz.dynamic.DynamicRefactoringWindow;
import dynamicrefactoring.interfaz.view.AvailableRefactoringView;
import dynamicrefactoring.reader.RefactoringPlanReader;
import dynamicrefactoring.reader.XMLRefactoringReaderException;
import dynamicrefactoring.util.io.FileManager;

/**
 * Permite ejecutar un conjunto de refactorizaciones que conforman un plan de 
 * refactorizaciones.
 * 
 * @author <A HREF="mailto:lfd0002@alu.ubu.es">Laura Fuente de la Fuente</A>
 */
public class RefactoringPlanExecutor implements IRunnableWithProgress{
	
	/**
	 * Conjunto de refactorizaciones que tienen que ser ejecutadas.
	 */
	private Map<String,String> refactorings;
	
	/**
	 * Modelo actual sobre el que ejecutar el plan.
	 */
	private JavaModel model;
	
	/**
	 * Conjunto de refactorizaciones que conforman el plan.
	 */
	private ArrayList<String> plan;
	
	/**
	 * Elemento de registro de errores y otros eventos de la clase.
	 */
	private static final Logger logger = Logger.getLogger(RefactoringPlanExecutor.class);
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa una clase.
	 */
	private final String CLASSDEF_NAME = "moon.core.classdef.ClassDef"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa un tipo de
	 * una clase. 
	 */
	private final String TYPE_NAME = "moon.core.classdef.Type"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa un m�todo de
	 * una clase. 
	 */
	private final String METHOD_NAME = "moon.core.classdef.MethDec"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa un atributo de
	 * una clase. 
	 */
	private final String ATTDEC_NAME = "moon.core.classdef.AttDec"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa un argumento de
	 * un m�todo. 
	 */
	private final String FORMALARGUMENT_NAME = "moon.core.classdef.FormalArgument"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa el nombre de
	 * un objeto MOON con nombre.
	 */
	private final String NAME_NAME = "moon.core.Name"; //$NON-NLS-1$
	
	/**
	 * Nombre completamente cualificado del tipo MOON que representa un fragmento de c�digo.
	 */
	private final String CODEFRAGMENT_NAME = "moon.core.instruction.CodeFragment"; //$NON-NLS-1$
	
	/**
	 * Refactorizaciones con problemas durante la ejecuci�n del plan de refactorizaciones.
	 */
	private HashMap<String,String> notExecuted=new HashMap<String,String>();
	
	/**
	 * Devuelve el conjunto de refactorizaciones que han sufrido alg�n problema 
	 * durante la ejecuci�n del plan de refactorizaciones.
	 * @return refactorizaciones con problemas y la traza del problema.
	 */
    public HashMap<String,String> getNotExecuted(){
    	return notExecuted;
    }
	
	/**
	 * Constructor.
	 * 
	 * @param refactorings refactorizaciones a ejecutar.
	 * @param plan conjunto de refactorizaciones que conforman el plan.
	 * @param path ruta del directorio en el que se encuentra en plan de refactorizaciones.
	 */
	public RefactoringPlanExecutor(Map<String,String> refactorings, ArrayList<String> plan, String path){
		this.refactorings=refactorings;
		this.plan=plan;
		new AvailableRefactoringView().saveUnsavedChanges();
		generateModel();
		setFilesIntoClasspath(path + "/repository");
	}
	
	/**
	 * Ejecuta un conjunto de refactorizaciones.
	 * 
	 * @param monitor monitor actual.
	 */
	public void run(IProgressMonitor monitor) {
		
		monitor.beginTask(Messages.RefactoringPlanExecutor_ExecutingPlan, refactorings.size()+1);
		int number_refactoring=1;
		
		for( String refactoring : plan){
			if(refactorings.containsKey(refactoring + " (" + refactoring +".xml)")){
				try{
					monitor.subTask(number_refactoring + "/" + plan.size() + " " + 
							Messages.RefactoringPlanExecutor_Executing + " " + refactoring  + " ..." ); 
					number_refactoring++;
					HashMap<String, Object> inputs = getInputs(refactoring);
					if(inputs==null){
						notExecuted.put(refactorings.get(refactoring + " (" + refactoring +".xml)") , "no inputs");
					}else{
						DynamicRefactoring d_refactoring = new DynamicRefactoring(
								refactorings.get(refactoring + " (" + refactoring +".xml)"), model,inputs);
						
						DynamicRefactoringRunner runner = new DynamicRefactoringRunner(d_refactoring);
						runner.setInputParameters(DynamicRefactoringWindow.getInputParameters(inputs));
						runner.setFromPlan(true);
						runner.runRefactoring();	
					}
					monitor.worked(1);
				}catch(Exception e){
					e.printStackTrace();
					monitor.worked(1);	
					String trace="";
					trace=e.getMessage();
					for(StackTraceElement s : e.getStackTrace()){
						trace= trace + "\n\t" + s.toString();
					}
					notExecuted.put(refactoring,trace);
					e.printStackTrace();
					Object[] messageArgs = {refactoring};
					MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
					formatter.applyPattern(
						Messages.RefactoringPlanExecutor_RefactoringNotExecuted);
					
					String message = formatter.format(messageArgs) + 
						" .\n\n" + e.getMessage(); //$NON-NLS-1$
					logger.error(message);	
				}
			}
		}
		monitor.subTask(Messages.RefactoringPlanExecutor_Regenerating + " ...");
		CodeRegenerator.getInstance().refresh();
		//Dejamos el classpath como estaba, para ello borramos de temp los ficheros .class
		//que hemos a�adido temporalmente para ejecutar el plan.
		FileManager.emptyDirectories(RefactoringConstants.TEMP_DIRECTORY);
		FileManager.deleteDirectories(RefactoringConstants.TEMP_DIRECTORY,false);
		monitor.worked(1);
		monitor.done();
	}
	
	
	
	
	/**
	 * Obtiene las entradas de una refactorizaci�n.
	 * 
	 * @param refactoring refactorizacion de la que se quiere obtener las entradas.
	 * @return una tabla asociativa con los valores de las entradas. Se utiliza
	 * como clave el nombre de cada entrada y como valor, el objeto MOON encontrado
	 * para la entrada.
	 * @throws XMLRefactoringReaderException XMLRefactoringReaderException
	 * @throws RefactoringException RefactoringException
	 */
	private HashMap<String, Object> getInputs(String refactoring) throws XMLRefactoringReaderException,RefactoringException{
		try{
			HashMap<String, Object> inputs = new HashMap<String, Object>();
			
			DynamicRefactoringDefinition refactoringDefinition = 
				DynamicRefactoringDefinition.getRefactoringDefinition(
						refactorings.get(refactoring + " (" + refactoring +".xml)"));
			
			ArrayList<InputParameter> fromInputs = new ArrayList<InputParameter>();
			
			//Procesamos todas las entradas menos las de tipo from.
			for (InputParameter input : refactoringDefinition.getInputs()){
				if (input.getType().equals("moon.core.Model")){
					// Se obtiene el modelo MOON actual.
					inputs.put(input.getName(), model);
				// Para entradas de tipo from.
				}else if(input.getFrom()!=null && input.getFrom().length() > 0){
					fromInputs.add(input);
				}else {
					String text = RefactoringPlanReader.getInputValue
						(refactoring, input.getName(), 
						  new File(new File( new File(refactorings.get(refactoring + " (" + refactoring +".xml)")).getParent()).getParent()).getParent() + "/refactoringPlan.xml" );
					Object value = computeValue(input, text);
				    inputs.put(input.getName(), value);
				}
			}
			
			int j =0;
			//Calculamos las entradas de tipo from
			while(fromInputs.size()!=0){
				InputParameter input = fromInputs.get(j);
				
				if(inputs.containsKey(input.getFrom())){
					fromInputs.remove(j);
					try{
						Method method = inputs.get(input.getFrom()).getClass().getMethod(input.getMethod(), 
								(Class[]) null);
						Object values = method.invoke(inputs.get(input.getFrom()), (Object[]) null);
						
						if(DynamicRefactoringWindow.isSingleValue(values)){
							inputs.put(input.getName(), values);
							
						}else{
							Object value = selectFromGroup( values,input,refactoring);
							inputs.put(input.getName(), value);
						}
					}catch(Exception e){
						Object[] messageArgs = {input.getMethod(),input.getFrom()};
						MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
						formatter.applyPattern(
							Messages.RefactoringPlanExecutor_InvokeMethod);
						
						String message = formatter.format(messageArgs) + 
							" .\n\n" + e.getMessage(); //$NON-NLS-1$
						logger.error(message);	
					}	
				}
				
				j++;
				if(fromInputs.size()==j)
					j=0;
			}//while
			
			return inputs;
			
		}catch(XMLRefactoringReaderException e){
			logger.error(Messages.RefactoringPlanExecutor_ReadingProblem +  e.getMessage());
			throw new XMLRefactoringReaderException(e.getMessage());
		}catch (RefactoringException e){
			logger.error(Messages.RefactoringPlanExecutor_DefinitionProblem + e.getMessage());
			throw new  RefactoringException(e.getMessage());
		}	
		
	}
	
	/**
	 * Intenta obtener un objeto MOON asociado a una entrada cuyo valor especifica
	 * el usuario a trav�s de un campo de texto.
	 * 
	 * <p>Por defecto, se interpreta que los campos de texto solo pueden contener
	 * nombres (<code>moon.core.Name</code>) , nombres �nicos de clases (<code>
	 * moon.core.classdef.ClassDef</code>) o nombres �nicos de m�todos (<code>
	 * moon.core.classdef.MethDec</code>).</p>
	 * 
	 * @param input entrada cuyo valor asociado se intenta obtener.
	 * @param source contenido del campo de texto mediante el que el usuario 
	 * asigna valor a la entrada.
	 * 
	 * @return un objeto MOON asociado a la entrada, o <code>null</code> si no se
	 * pudo cargar ning�n objeto adecuado.
	 */
	private Object computeValue(InputParameter input, String source){
		
		// Se obtiene el nombre del elemento que habr� que buscar.
		String name = source.trim();
		
		// Si la entrada es de tipo moon.core.Name.
		if (input.getType().equals(NAME_NAME))	
			// Se construye un nombre MOON.
			return model.getMoonFactory().createName(name);
		
		try {
			Class<?> classdef = Class.forName(CLASSDEF_NAME);
			Class<?> declaration = Class.forName(input.getType());
			
			// Si no, se comprueba si es alg�n subtipo de moon.core.classdef.ClassDef.
			if (classdef.isAssignableFrom(declaration)){
				Name className = model.getMoonFactory().createName(name);
				return model.getClassDef(className);				
			}
			
			// Si no, se comprueba si es subitpo de moon.core.classdef.ClassType.
			Class<?> classtype = Class.forName(TYPE_NAME);
			
			if (classtype.isAssignableFrom(declaration)){
				Name typeName = model.getMoonFactory().createName(name);
				return model.getType(typeName);
			}
			
			
			// Si no, se comprueba si es un subtipo de moon.core.classdef.MethDec
			Class<?> methtype = Class.forName(METHOD_NAME);
			if (methtype.isAssignableFrom(declaration))
				return processMethod(source);
			
			
			// Si no, se comprueba si es un subtipo de moon.core.classdef.AttDec
			Class<?> attributetype = Class.forName(ATTDEC_NAME);
			if (attributetype.isAssignableFrom(declaration))
				return processAttribute(source);
			
			// Si no, se comprueba si es un subtipo de moon.core.classdef.FormalArgument
			Class<?> formalargtype = Class.forName(FORMALARGUMENT_NAME);
			if (formalargtype.isAssignableFrom(declaration))
				return processFormalArgument(source);
			
			// Si no, se comprueba si es un subtipo de moon.core.instruction.CodeFragment
			Class<?> codefragment = Class.forName(CODEFRAGMENT_NAME);
			if (codefragment.isAssignableFrom(declaration))
				return processCodeFragment(source);
			
		}
		catch (ClassNotFoundException exception){
			Object[] messageArgs = {input.getName()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.RefactoringPlanExecutor_ObjectNotLoaded);
			
			String message = formatter.format(messageArgs) + 
				" .\n\n" + exception.getMessage(); //$NON-NLS-1$
			logger.error(message);		
		}
		
		return null;
	}
	
	
	/**
	 * Busca un objeto entre un grupo de objetos que se corresponada con la
	 * entrada de la refactorizaci�n.
	 * 
	 * @param group Colecci�n que posiblemente contiene el objeto buscado.
	 * @param input par�metro que se esta procesando.
	 * @param refactoring nombre de la refactorizaci�n.
	 * @return objeto que se corresponde con el par�metro.
	 */
	private Object selectFromGroup( Object group, InputParameter input, String refactoring){
		try{
			Class<?> collection = Class.forName(
				RefactoringConstants.COLLECTION_PATH);
			Class<?> iterator = Class.forName(
				RefactoringConstants.ITERATOR_PATH);
			
			Class<?> container = group.getClass();
			
			
			String name = RefactoringPlanReader.getInputValue(refactoring, input.getName(), 
					new File(new File( new File(refactorings.get(refactoring 
				    + " (" + refactoring +".xml)")).getParent()).getParent()).getParent()
				    + "/refactoringPlan.xml" );
			
			// Si los valores est�n contenidos en una colecci�n.
			if (collection.isAssignableFrom(container))
				for (Object next : ((java.util.Collection<?>)group)){
					if(((NamedObject)next).getUniqueName().toString().equals(name))
						return next;
				}
			
			// Si los valores est�n contenidos en un iterador.
			else if (iterator.isAssignableFrom(container)){
				Iterator<?> valueIterator = (java.util.Iterator<?>)group;
				while(valueIterator.hasNext())
					if(((NamedObject)valueIterator.next()).getUniqueName().toString().equals(name))
						return valueIterator.next();
			}
		}catch(ClassNotFoundException e){
			Object[] messageArgs = {input.getName()};
			MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
			formatter.applyPattern(
				Messages.RefactoringPlanExecutor_ObjectNotLoaded);
			
			String message = formatter.format(messageArgs) + 
				" .\n\n" + e.getMessage(); //$NON-NLS-1$
			logger.error(message);	
		}
		catch(XMLRefactoringReaderException e){}
		
		return null;
	}
	
	
	/**
	 * Obtiene el m�todo del modelo correspondiente al nombre cualificado del mismo que
	 * se le pasa como entrada.
	 * 
	 * @param uniqueName nombre cualificado del m�todo a obtener.
	 * @return m�todo del modelo correspondiente con uniqueName.
	 */
	private MethDec processMethod(String uniqueName){
		String s_methodName="";
		if(uniqueName.contains("%")){
			s_methodName = uniqueName.substring(uniqueName.indexOf('~')+1,uniqueName.indexOf('%'));
		}else{
			s_methodName = uniqueName.substring(uniqueName.indexOf('~')+1);
		}
		String methodClass = uniqueName.substring(0,uniqueName.indexOf('~'));
		Name className = model.getMoonFactory().createName(methodClass);
		Name methodName = model.getMoonFactory().createName(s_methodName);
		ClassDef clase = model.getClassDef(className); 
		for(MethDec meth : clase.getMethDecByName(methodName)){
			if(meth.getUniqueName().toString().equals(uniqueName))
				return meth;
		}
		return null;
	}
	
	/**
	 * Obtiene el atributo del modelo correspondiente al nombre cualificado del mismo que
	 * se le pasa como entrada.
	 * 
	 * @param uniqueName nombre cualificado del atributo a obtener.
	 * @return atributo del modelo correspondiente con uniqueName.
	 */
	private AttDec processAttribute(String uniqueName){
		String attributeClass = uniqueName.substring(0,uniqueName.indexOf('#'));
		Name className = model.getMoonFactory().createName(attributeClass);
		ClassDef clase = model.getClassDef(className); 
		for(AttDec attribute : clase.getAttributes()){
			if(attribute.getUniqueName().toString().equals(uniqueName))
				return attribute;
		}
		return null;
	}
	
	/**
	 * Obtiene el par�metro de un m�todo del modelo correspondiente al nombre cualificado del mismo 
	 * que se le pasa como entrada.
	 * 
	 * @param uniqueName nombre cualificado del par�metro a obtener.
	 * @return par�metro del modelo correspondiente con uniqueName.
	 */
	private FormalArgument processFormalArgument(String uniqueName){
		String s_methodName = uniqueName.substring(uniqueName.indexOf('~')+1,uniqueName.indexOf('%'));
		String methodUniqueName = uniqueName.substring(0,uniqueName.indexOf('#'));
		String parameterName = uniqueName.substring(uniqueName.indexOf('#')+1,uniqueName.length());
		String methodClass = uniqueName.substring(0,uniqueName.indexOf('~'));
		Name className = model.getMoonFactory().createName(methodClass);
		Name methodName = model.getMoonFactory().createName(s_methodName);
		ClassDef clase = model.getClassDef(className); 
		for(MethDec meth : clase.getMethDecByName(methodName)){
			if(meth.getUniqueName().toString().equals(methodUniqueName)){
				for(FormalArgument arg : meth.getFormalArgument())
					if(arg.getName().toString().equals(parameterName)){
						return arg;
						
					}
			}	
		}
		return null;
	}
	
	/**
	 * Obtiene el CodeFragment correspondiente a la entrada principal de 
	 * una refactorizaci�n.
	 * 
	 * @param data datos sobre el frgamento de c�digo a obtener.
	 * @return par�metro del modelo correspondiente con uniqueName.
	 */
	private CodeFragment processCodeFragment(String data){
		int beginLine = Integer.valueOf(data.substring(0,data.indexOf(',')));
		
		data=data.substring(data.indexOf(',')+1);
		int beginColumn = Integer.valueOf(data.substring(0,data.indexOf(',')));
		data=data.substring(data.indexOf(',')+1);
		int endLine = Integer.valueOf(data.substring(0,data.indexOf(',')));
		data=data.substring(data.indexOf(',')+1);
		int endColumn = Integer.valueOf(data.substring(0,data.indexOf(',')));
		data=data.substring(data.indexOf(',')+1);
		Name className = model.getMoonFactory().createName(data.substring(0,data.indexOf(',')));
		ClassDef clase=model.getClassDef(className);
		data=data.substring(data.indexOf(',')+1);
		return JavaMoonFactory.getInstance().createCodeFragment(beginLine
				,beginColumn, endLine, endColumn,clase,data);
	}
	
	/**
	 * Genera el modelo actual del proyecto que esta abierto.
	 */
	private void generateModel(){
		ModelGenerator.getInstance().generateModel(null, true,true);
		model = ModelGenerator.getInstance().getModel(); 
	}
	
	/**
	 * Introduce el conjunto de ficheros .class con los que trabajan las refactorizaciones dentro
	 * del classpath.
	 * 
	 * <p>Para ello introduce dicha carpeta dentro de una carpeta temporal llamada ./temp
	 * que est� agragada al classpath del plugin.<p> 
	 * 
	 * @param directory directorio en el que se encuentran los ficheros .class a a�adir.
	 */
	private void setFilesIntoClasspath(String directory){
		try{
			FileManager.copyFolder(directory, RefactoringConstants.TEMP_DIRECTORY);
		}catch(IOException e){
			logger.error(e.getMessage());
		}
	}


}