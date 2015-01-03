/**
 * 
 */
package org.ruleml.psoa.vocab;

/**
 * @author Mohammad Sadnan Al Manir
 *
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
		for(PsoaDatatype psoaDatatype : PsoaDatatype.values()){			
			if(psoaDatatype.getUri().equals(typeUri))
				return true;
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
