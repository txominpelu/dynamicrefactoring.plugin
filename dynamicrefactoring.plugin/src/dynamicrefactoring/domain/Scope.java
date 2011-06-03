package dynamicrefactoring.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import moon.core.ObjectMoon;
import moon.core.classdef.AttDec;
import moon.core.classdef.ClassDef;
import moon.core.classdef.FormalArgument;
import moon.core.classdef.MethDec;
import moon.core.genericity.BoundS;
import moon.core.genericity.FormalPar;
import moon.core.instruction.CodeFragment;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import dynamicrefactoring.interfaz.Messages;

public enum Scope {
	/**
	 * �mbito de clase.
	 */
	CLASS(ClassDef.class, Messages.SelectRefactoringWindow_ClassScope,
			"classdef"),
	/**
	 * �mbito de método.
	 */
	METHOD(MethDec.class, Messages.SelectRefactoringWindow_MethodScope,
			"methdec"),
	/**
	 * �mbito de atributo.
	 */
	ATTRIBUTE(AttDec.class, Messages.SelectRefactoringWindow_FieldScope,
			"attdec"),
	/**
	 * �mbito de argumento formal.
	 */
	FORMAL_ARG(FormalArgument.class,
			Messages.SelectRefactoringWindow_FormalArgumentScope,
			"formalArgument"),
	/**
	 * �mbito de parámetro formal.
	 */
	FORMAL_PAR(FormalPar.class,
			Messages.SelectRefactoringWindow_FormalParameterScope, "formalPar"),
	/**
	 * �mbito de parámetro formal acotado.
	 */
	BOUNDED_PAR(BoundS.class,
			Messages.SelectRefactoringWindow_BoundedParameterScope, ""),
	/**
	 * �mbito de bloque de texto.
	 */
	CODE_FRAGMENT(CodeFragment.class,
			Messages.SelectRefactoringWindow_CodeFragmentScope, "codeFragment");

	private static final Map<String, Scope> stringToEnum = new HashMap<String, Scope>();

	static { // Inicializa el mapa de nombre de ambito a constante de la
				// enumeracion
		for (Scope scope : values()) {
			stringToEnum.put(scope.toString(), scope);
		}
	}

	/**
	 * Dada la representacion toString de un ambito devuelve el ambito que le
	 * corresponde.
	 * 
	 * @param name
	 *            representacion toString de un ambito
	 * @return el ambito que corresponde al string pasado o nulo si el string es
	 *         invalido
	 */
	public static Scope fromString(String name) {
		return stringToEnum.get(name);

	}

	/**
	 * Obtiene una representacion en formato cadena sin "_" entre palabras y con
	 * las palabras en mayusculas del nombre del valor de la enumeracion.
	 * 
	 * @return formato adecuado para impresion del nombre del ambito
	 */
	@Override
	public String toString() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
				super.toString()).replace("_", "");
	}

	/**
	 * Clase de JavaMoon que se corresponde con el ambito.
	 */
	private final Class<? extends ObjectMoon> correspondingClass;

	/**
	 * Descripci�n del ambito.
	 */
	private final String description;

	/**
	 * Etiqueta xml que describe al ambito.
	 */
	private String xmlTag;


	/**
	 * Descripci�n del ambito.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Etiqueta xml que describe al ambito.
	 * 
	 * @return Etiqueta xml que describe al ambito.
	 */
	public String getXmlTag() {
		return this.xmlTag;
	}

	/**
	 * Constructor de un �mbito.
	 * 
	 * @param correspondinClass
	 *            clase de moon que se corresponde con el �mbito
	 */
	Scope(final Class<? extends ObjectMoon> correspondinClass,
			final String description, final String xmlTag) {
		this.correspondingClass = correspondinClass;
		this.description = description;
		this.xmlTag = xmlTag;
	}

	/**
	 * Dada una clase de Moon obtiene el ambito que se corresponde con la misma
	 * 
	 * @param correspondingClass
	 * @return ambito que se corresponde con la clase
	 */
	public static final Scope getScopeForObjectMoonClass(
			final Class<? extends ObjectMoon> correspondingClass) {
		return Collections2
				.filter(Arrays.asList(Scope.values()), new Predicate<Scope>() {

					@Override
					public boolean apply(Scope arg0) {
						return arg0.correspondingClass
								.equals(correspondingClass);
					}

				}).iterator().next();
	}

}