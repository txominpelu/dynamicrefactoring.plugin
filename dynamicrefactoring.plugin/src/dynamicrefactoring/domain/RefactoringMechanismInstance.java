package dynamicrefactoring.domain;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Objects;
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
	 * Tipo de mecanismo.
	 */
	private RefactoringMechanismType type;

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
		this.type = type;
	}
	
	/**
	 * Obtiene el nombre de la clase del mecanismo.
	 * 
	 * @return clase que representa el mecanismo
	 */
	public String getClassName(){
		return this.className;
	}
	
	/**
	 * Obtiene el tipo del mecanismo.
	 * 
	 * @return tipo del mecanismo
	 */
	public RefactoringMechanismType getType(){
		return this.type;
	}
	
	@Override
	public String toString(){
		return Objects.toStringHelper(this).add("className", getClassName()).add("type", type).toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof RefactoringMechanismInstance){
			return getClassName().equals(((RefactoringMechanismInstance) o).getClassName()) && type.equals(((RefactoringMechanismInstance) o).type);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(getClassName(), type);
	}
	
	/**
	 * Devuelve los nombres de clase de los mecanismos pasados.
	 * 
	 * @param mechanisms mecanismos a tranformar
	 * @return un <code>List</code> de cadenas con los nombres.
	 * 
	 * @see #setActions
	 */
	public static List<String> getMechanismListClassNames(List<RefactoringMechanismInstance> mechanisms) {
		return Lists.transform(ImmutableList.copyOf(mechanisms), new Function<RefactoringMechanismInstance, String>(){

			@Override
			public String apply(RefactoringMechanismInstance arg0) {
				return arg0.getClassName();
			}
		});
	}

}
