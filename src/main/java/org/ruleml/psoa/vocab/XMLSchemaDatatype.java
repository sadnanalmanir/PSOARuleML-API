package org.ruleml.psoa.vocab;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Sadnan Al Manir
 */
public enum XMLSchemaDatatype {

    // only string and integer are supported at this moment
    STRING("string"),
    INTEGER("integer");

    private String uri;
    private String type;

    /**
     * @param type shorthand for the XSD Datatype
     */
    XMLSchemaDatatype(String type) {
        this(Namespaces.XSD_NS, type);
    }


    /**
     * @param xsdNs XSD Namespace
     * @param type  XSD Datatype
     */
    XMLSchemaDatatype(String xsdNs, String type) {
        this.type = type;
        this.uri = xsdNs + type;
    }

    /**
     * @param typeUri the xsd datatype to be checked
     * @return true if it is supprted by PSOA XSD datatype
     */
    public static boolean existDatatype(String typeUri) {
        for (XMLSchemaDatatype xsdDatatype : XMLSchemaDatatype.values()) {
            if (xsdDatatype.getUri().equals(typeUri))
                return true;
        }
        return false;
    }

    /**
     * @param typeUri the xsd datatype to be checked
     * @return xs:datatype
     */
    public static String getShortHandDatatype(String typeUri) {
        return XSDDatatypeMap.shortHandDatatypeMap.get(typeUri);

    }

    /**
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

    // stores datatype uri as key, xs:datatype as value
    private static class XSDDatatypeMap {

        private static final Map<String, String> shortHandDatatypeMap;

        static {
            shortHandDatatypeMap = new HashMap<String, String>();
            for (XMLSchemaDatatype xsdDatatype : XMLSchemaDatatype.values()) {
                shortHandDatatypeMap.put(xsdDatatype.getUri(), "xs:" + xsdDatatype.getType());
            }
        }
    }
}
