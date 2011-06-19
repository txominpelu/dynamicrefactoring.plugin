/*<Dynamic Refactoring Plugin For Eclipse 3 - Plugin that allows to perform refactorings 
on Java code within Eclipse, as well as to dynamically create and manage new refactorings and classify them.>

Copyright (C) 2011  Míryam Gómez e Íñigo Mediavilla

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.01.31 at 03:54:24 PM CET 
//


package dynamicrefactoring.domain.metadata.classifications.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dynamicrefactoring.plugin.xml.classifications package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 * @author <A HREF="mailto:ims0011@alu.ubu.es">Iñigo Mediavilla Saiz</A>
 * @author <A HREF="mailto:mgs0110@alu.ubu.es">Míryam Gómez San Martín</A>
 */
@XmlRegistry
 public class ObjectFactory {

	/**
	 * Nombre de la etiqueta principal.
	 */
    private final static QName _Classifications_QNAME = new QName("", "classifications");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dynamicrefactoring.plugin.xml.classifications
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CategoriesType }
     * @return categories type
     * 
     */
    public CategoriesType createCategoriesType() {
        return new CategoriesType();
    }

    /**
     * Create an instance of {@link ClassificationsType }
     * @return new classifications type
     * 
     */
    public ClassificationsType createClassificationsType() {
        return new ClassificationsType();
    }

    /**
     * Create an instance of {@link ClassificationType }
     * @return new classification type
     * 
     */
    public ClassificationType createClassificationType() {
        return new ClassificationType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClassificationsType }{@code >}}
     * @param value classificationsType
     * @return classifications
     * 
     */
    @XmlElementDecl(namespace = "", name = "classifications")
    public JAXBElement<ClassificationsType> createClassifications(ClassificationsType value) {
        return new JAXBElement<ClassificationsType>(_Classifications_QNAME, ClassificationsType.class, null, value);
    }

}
