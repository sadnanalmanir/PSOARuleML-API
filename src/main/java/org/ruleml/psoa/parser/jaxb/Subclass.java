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
 *         &lt;element ref="{http://psoa.ruleml.org/lang/spec#}sub"/&gt;
 *         &lt;element ref="{http://psoa.ruleml.org/lang/spec#}super"/&gt;
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
    "sub",
    "_super"
})
@XmlRootElement(name = "Subclass")
public class Subclass {

    @XmlElement(required = true)
    protected Sub sub;
    @XmlElement(name = "super", required = true)
    protected Super _super;

    /**
     * Gets the value of the sub property.
     * 
     * @return
     *     possible object is
     *     {@link Sub }
     *     
     */
    public Sub getSub() {
        return sub;
    }

    /**
     * Sets the value of the sub property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sub }
     *     
     */
    public void setSub(Sub value) {
        this.sub = value;
    }

    /**
     * Gets the value of the super property.
     * 
     * @return
     *     possible object is
     *     {@link Super }
     *     
     */
    public Super getSuper() {
        return _super;
    }

    /**
     * Sets the value of the super property.
     * 
     * @param value
     *     allowed object is
     *     {@link Super }
     *     
     */
    public void setSuper(Super value) {
        this._super = value;
    }

}
