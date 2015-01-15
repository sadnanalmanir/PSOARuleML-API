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

/**
 * @author Mohammad Sadnan Al Manir
 * University of New Brunswick
 */
public enum PsoaDatatype {

    LOCAL("local");
    private String uri;
    private String type;

    /**
     * @param type shorthand for the PSOA Datatype
     */
    private PsoaDatatype(String type) {
        this(Namespaces.PSOA_NS, type);
    }

    /**
     *
     * @param psoaNs PSOA Namespcae
     * @param type shorthand for the PSOA Datatype
     */
    private PsoaDatatype(String psoaNs, String type) {
        this.type = type;
        this.uri = psoaNs + type;
    }

    /**
     *
     * @param typeUri the xsd datatype to be checked
     * @return true if it is supprted by PSOA datatype
     */
    public static boolean existDatatype(String typeUri) {
        for (PsoaDatatype psoaDatatype : PsoaDatatype.values()) {
            if (psoaDatatype.getUri().equals(typeUri)) {
                return true;
            }
        }
        return false;
    }

    public String getUri() {
        return this.uri;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return getUri();
    }
}
