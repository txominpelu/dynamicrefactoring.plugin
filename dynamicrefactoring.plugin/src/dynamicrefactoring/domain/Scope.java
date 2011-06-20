/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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

/**
 * Posibles ámbitos sobre los que se puede aplicar una refactorización.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
 public enum Scope {
	/**
	 * Ámbito de clase.
	 */
	CLASS(ClassDef.class, Messages.SelectRefactoringWindow_ClassScope,
			"classdef"),
	/**
	 * Ámbito de método.
	 */
	METHOD(MethDec.class, Messages.SelectRefactoringWindow_MethodScope,
			"methdec"),
	/**
	 * Ámbito de atributo.
	 */
	ATTRIBUTE(AttDec.class, Messages.SelectRefactoringWindow_FieldScope,
			"attdec"),
	/**
	 * Ámbito de argumento formal.
	 */
	FORMAL_ARG(FormalArgument.class,
			Messages.SelectRefactoringWindow_FormalArgumentScope,
			"formalArgument"),
	/**
	 * Ámbito de parámetro formal.
	 */
	FORMAL_PAR(FormalPar.class,
			Messages.SelectRefactoringWindow_FormalParameterScope, "formalPar"),
	/**
	 * Ámbito de parámetro formal acotado.
	 */
	BOUNDED_PAR(BoundS.class,
			Messages.SelectRefactoringWindow_BoundedParameterScope, ""),
	/**
	 * Ámbito de bloque de texto.
	 */
	CODE_FRAGMENT(CodeFragment.class,
			Messages.SelectRefactoringWindow_CodeFragmentScope, "codeFragment");

	/**
	 * Permite convertir de la cadena de la enumeración a la enumeración en sí.
	 */
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
	 * Descripción del ambito.
	 */
	private final String description;

	/**
	 * Etiqueta xml que describe al ambito.
	 */
	private String xmlTag;


	/**
	 * Descripción del ambito.
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
	 * Constructor de un Ámbito.
	 * 
	 * @param correspondinClass
	 *            clase de moon que se corresponde con el Ámbito
	 * @param description
	 *            descripción
	 * @param xmlTag
	 *            etiqueta xml
	 */
	Scope(final Class<? extends ObjectMoon> correspondinClass,
			final String description, final String xmlTag) {
		this.correspondingClass = correspondinClass;
		this.description = description;
		this.xmlTag = xmlTag;
	}

	/**
	 * Dada una clase de Moon obtiene el ambito que se corresponde con la misma.
	 * 
	 * @param correspondingClass
	 *            clase correspondiente
	 * 
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
