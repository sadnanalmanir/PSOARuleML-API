/**
 * This file is part of PSOARuleML-API.
 *
 *  PSOARuleML-API is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PSOARuleML-API is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PSOARuleML-API.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ruleml.psoa.vocab;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mohammad Sadnan Al Manir
 * University of New Brunswick
 *
 */
public enum XMLSchemaDatatype {

    
    STRING("string"),
    INTEGER("integer");
    private String uri;
    private String type;

    
    /**
     * stores datatype uri as key, xs:datatype as value
     */
    private static class XSDDatatypeMap {

        private static final Map<String, String> shortHandDatatypeMap;

        static {
            shortHandDatatypeMap = new HashMap<String, String>();
            for (XMLSchemaDatatype xsdDatatype : XMLSchemaDatatype.values()) {
                shortHandDatatypeMap.put(xsdDatatype.getUri(), "xs:" + xsdDatatype.getType());
            }
        }
    }

    /**
     * @param type shorthand for the XSD Datatype
     */
    private XMLSchemaDatatype(String type) {
        this(Namespaces.XSD_NS, type);
    }

    /**
     *
     * @param xsdNs XSD Namespace
     * @param type XSD Datatype
     */
    private XMLSchemaDatatype(String xsdNs, String type) {
        this.type = type;
        this.uri = xsdNs + type;
    }

    /**
     *
     * @return complete datatype uri
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * @return the shorthand type
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getUri();

    }

    /**
     *
     * @param typeUri the xsd datatype to be checked
     * @return true if it is supprted by PSOA XSD datatype
     */
    public static boolean existDatatype(String typeUri) {
        for (XMLSchemaDatatype xsdDatatype : XMLSchemaDatatype.values()) {
            if (xsdDatatype.getUri().equals(typeUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param typeUri the xsd datatype to be checked
     * @return xs:datatype
     */
    public static String getShortHandDatatype(String typeUri) {
        return XSDDatatypeMap.shortHandDatatypeMap.get(typeUri);

    }
}
