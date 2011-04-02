package dynamicrefactoring.interfaz.wizard;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public final class MechanismNameUtils {
	
	/**
	 * Evita que la clase pueda ser instanciada.
	 */
	private MechanismNameUtils(){}
	
	/**
	 * Dada una lista de nombres con formato:
	 * 
	 * NotExistClassWithName(1)
	 * 
	 * y devuelve el nombre sin el numero:
	 * 
	 * NotExistClassWithName
	 * 
	 * @param mechanismList lista de nombres de mecanismos a transformar
	 * @return lista de nombres de mecanismos transformada
	 */
	public static List<String> getMechanismNameList(List<String> mechanismList){
		List<String> list = new ArrayList<String>();
		for(String name: mechanismList){
			list.add(getMechanismName(name));
		}
		return list;
	}
	
	/**
	 * Recibe una precondicion, accion o postcondicion con el numero:
	 * 
	 * NotExistClassWithName (1)
	 * 
	 * y devuelve el nombre sin el numero:
	 * 
	 * NotExistClassWithName
	 * 
	 * @param preconditionWithNumber
	 *            precondicion con formato nombre(numero)
	 * @return devuelve nombre
	 */
	public static String getMechanismName(final String preconditionWithNumber) {
		Preconditions.checkArgument(preconditionWithNumber.matches("\\w* \\([1-9]\\)"));
		return preconditionWithNumber.substring(0,
				preconditionWithNumber.length() - 4);
	}

}
