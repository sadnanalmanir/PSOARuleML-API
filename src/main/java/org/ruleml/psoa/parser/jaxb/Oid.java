//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.22 at 05:43:50 PM ADT 
//


package org.ruleml.psoa.parser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://psoa.ruleml.org/lang/spec#}INDTERM"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ind",
    "rel",
    "var",
    "expr",
    "external"
})
@XmlRootElement(name = "oid")
public class Oid {

    @XmlElement(name = "Ind")
    protected Ind ind;
    @XmlElement(name = "Rel")
    protected Rel rel;
    @XmlElement(name = "Var")
    protected Var var;
    @XmlElement(name = "Expr")
    protected Expr expr;
    @XmlElement(name = "External")
    protected ExternalTERMType external;

    /**
     * Gets the value of the ind property.
     * 
     * @return
     *     possible object is
     *     {@link Ind }
     *     
     */
    public Ind getInd() {
        return ind;
    }

    /**
     * Sets the value of the ind property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ind }
     *     
     */
    public void setInd(Ind value) {
        this.ind = value;
    }

    /**
     * Gets the value of the rel property.
     * 
     * @return
     *     possible object is
     *     {@link Rel }
     *     
     */
    public Rel getRel() {
        return rel;
    }

    /**
     * Sets the value of the rel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rel }
     *     
     */
    public void setRel(Rel value) {
        this.rel = value;
    }

    /**
     * Gets the value of the var property.
     * 
     * @return
     *     possible object is
     *     {@link Var }
     *     
     */
    public Var getVar() {
        return var;
    }

    /**
     * Sets the value of the var property.
     * 
     * @param value
     *     allowed object is
     *     {@link Var }
     *     
     */
    public void setVar(Var value) {
        this.var = value;
    }

    /**
     * Gets the value of the expr property.
     * 
     * @return
     *     possible object is
     *     {@link Expr }
     *     
     */
    public Expr getExpr() {
        return expr;
    }

    /**
     * Sets the value of the expr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Expr }
     *     
     */
    public void setExpr(Expr value) {
        this.expr = value;
    }

    /**
     * Gets the value of the external property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalTERMType }
     *     
     */
    public ExternalTERMType getExternal() {
        return external;
    }

    /**
     * Sets the value of the external property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalTERMType }
     *     
     */
    public void setExternal(ExternalTERMType value) {
        this.external = value;
    }

}
