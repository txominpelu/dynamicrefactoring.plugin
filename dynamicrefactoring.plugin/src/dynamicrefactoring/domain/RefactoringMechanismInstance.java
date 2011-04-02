package dynamicrefactoring.domain;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Instancia de un mecanismo de una refactorizacion.
 * 
 * @author imediava
 *
 */
public class RefactoringMechanismInstance {
	
	/**
	 * Nombre de la clase del mecanismo de refactorizacion
	 */
	private String className;

	/**
	 * Crea un mecanismo de una refactorizacion
	 * del tipo pasado.
	 * 
	 * @param className nombre de la clase del mecanismo
	 * @param type tipo del mecanismo
	 * @see RefactoringMechanismType
	 */
	public RefactoringMechanismInstance(String className, RefactoringMechanismType type){
		this.className = className;
	}
	
	/**
	 * Obtiene el nombre de la clase del mecanismo.
	 * 
	 * @return clase que representa el mecanismo de la refactorizacion
	 */
	public String getClassName(){
		return this.className;
	}
	
	/**
	 * Devuelve los nombres de clase de los mecanismos pasados.
	 * 
	 * @param mechanisms mecanismos a tranformar
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setActions
	 */
	public static List<String> getActionsClassNames(List<RefactoringMechanismInstance> mechanisms) {
		return Lists.transform(ImmutableList.copyOf(mechanisms), new Function<RefactoringMechanismInstance, String>(){

			@Override
			public String apply(RefactoringMechanismInstance arg0) {
				return arg0.getClassName();
			}
		});
	}

}
