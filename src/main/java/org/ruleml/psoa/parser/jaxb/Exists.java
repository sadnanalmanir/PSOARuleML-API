//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.31 at 01:21:57 AM AST 
//


package org.ruleml.psoa.parser.jaxb;

import java.util.ArrayList;
import java.util.List;
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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://psoa.ruleml.org/lang/spec#}declare" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://psoa.ruleml.org/lang/spec#}formula"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "declare",
    "formula"
})
@XmlRootElement(name = "Exists")
public class Exists {

    @XmlElement(required = true)
    protected List<Declare> declare;
    @XmlElement(required = true)
    protected Formula formula;

    /**
     * Gets the value of the declare property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the declare property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeclare().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Declare }
     * 
     * 
     */
    public List<Declare> getDeclare() {
        if (declare == null) {
            declare = new ArrayList<Declare>();
        }
        return this.declare;
    }

    /**
     * Gets the value of the formula property.
     * 
     * @return
     *     possible object is
     *     {@link Formula }
     *     
     */
    public Formula getFormula() {
        return formula;
    }

    /**
     * Sets the value of the formula property.
     * 
     * @param value
     *     allowed object is
     *     {@link Formula }
     *     
     */
    public void setFormula(Formula value) {
        this.formula = value;
    }

}
