package dynamicrefactoring.util;

import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

import dynamicrefactoring.RefactoringConstants;
import dynamicrefactoring.interfaz.dynamic.RepositoryElementProcessor;

public class StringUtils {

	private static final String PACKAGE_SEPARATOR_CHAR = ".";


	/**
	 * Dado el nombre completo de una clase. Por ejemplo:
	 * 
	 * java.util.List
	 * 
	 * Se queda con el nombre de la clase.
	 * 
	 * List
	 * 
	 * @param fullyQualifiedName
	 *            nombre completo de la clase, es decir nombre de la clase mas
	 *            paquete al que pertenece
	 * @return nombre simple de la clase
	 */
	public static String getClassName(String fullyQualifiedName) {
		return splitGetLast(fullyQualifiedName, PACKAGE_SEPARATOR_CHAR);
	}

	/**
	 * Divide la cadena en partes utilizando como token delim y devuelve la
	 * �ltima de las particiones hechas.
	 * 
	 * @param cadena
	 *            Cadena a dividir
	 * @param delim
	 *            Token para hacer la division
	 * @return devuelve la ultima de las particiones
	 */
	public static String splitGetLast(String cadena, String delim) {
		String name = ""; //$NON-NLS-1$
		StringTokenizer st_name = new StringTokenizer(cadena, delim);
		while (st_name.hasMoreTokens()) {
			name = st_name.nextElement().toString();
		}
		return name;
	}

	/**
	 * Obtiene el nombre totalmente cualificado (incluido paquete al que
	 * pertenece) dado el nombre de una precondicion, accion o postcondicion.
	 * Ejemplo:
	 * 
	 * Recibe :
	 * 
	 * ExistsClassWithName
	 * 
	 * Devuelve:
	 * 
	 * repository.moon.concretepredicate.ExistsClassWithName
	 * 
	 * @param preconditionName
	 *            nombre de la precondicion, accion o postcondicion
	 * @return nombre totalmente cualificado (con paquete)
	 */
	public static String getMechanismFullyQualifiedName(int type,
			final String preconditionName) {
		String preconditionPack;
		switch (type) {
		case RefactoringConstants.PRECONDITION:
			if (RepositoryElementProcessor
					.isPredicateJavaDependent(preconditionName)) {
				preconditionPack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			} else {
				preconditionPack = RefactoringConstants.PREDICATES_PACKAGE;
			}
			return preconditionPack + preconditionName;
		case RefactoringConstants.ACTION:
			if (RepositoryElementProcessor
					.isActionJavaDependent(preconditionName)) {
				preconditionPack = RefactoringConstants.JAVA_ACTIONS_PACKAGE;
			} else {
				preconditionPack = RefactoringConstants.ACTIONS_PACKAGE;
			}
			return preconditionPack + preconditionName;
		case RefactoringConstants.POSTCONDITION:
			if (RepositoryElementProcessor
					.isPredicateJavaDependent(preconditionName)) {
				preconditionPack = RefactoringConstants.JAVA_PREDICATES_PACKAGE;
			} else {
				preconditionPack = RefactoringConstants.PREDICATES_PACKAGE;
			}
			return preconditionPack + preconditionName;
		default:
			throw new RuntimeException(
					"type must be one of: RefactoringConstants. PRECONDITION/POSTCONDITION/ACTION");
		}

	}

	/**
	 * Obtiene un conjunto de nombres simples de pre/postcondiciones o acciones
	 * y devuelve los nombres de ellos totalmente cualificados (incluidos los
	 * paquetes).
	 * 
	 * @see #getMechanismFullyQualifiedName(String)
	 * 
	 * @param simpleNames
	 *            conjunto de nombres simples de pre/postcondiciones o acciones
	 * @return nombres totalmente cualificados (con paquetes) de los mismos
	 */
	public static Set<String> getMechanismListFullyQualifiedName(
			final int type,
			Set<String> simpleNames) {
		return ImmutableSet.copyOf(Collections2.transform(simpleNames,
				new Function<String, String>() {
	
					@Override
					public String apply(String arg0) {
						return getMechanismFullyQualifiedName(type, arg0);
					}
	
				}));
	}

}
