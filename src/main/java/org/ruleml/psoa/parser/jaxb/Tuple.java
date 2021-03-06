//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.22 at 05:43:50 PM ADT 
//


package org.ruleml.psoa.parser.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;group ref="{http://psoa.ruleml.org/lang/spec#}TUPLEARGS"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ordered" type="{http://www.w3.org/2001/XMLSchema}string" fixed="yes" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "indterm"
})
@XmlRootElement(name = "Tuple")
public class Tuple {

    @XmlElements({
        @XmlElement(name = "Ind", type = Ind.class),
        @XmlElement(name = "Rel", type = Rel.class),
        @XmlElement(name = "Var", type = Var.class),
        @XmlElement(name = "Expr", type = Expr.class),
        @XmlElement(name = "External", type = ExternalTERMType.class)
    })
    protected List<Object> indterm;
    @XmlAttribute(name = "ordered")
    protected String ordered;

    /**
     * Gets the value of the indterm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indterm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getINDTERM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ind }
     * {@link Rel }
     * {@link Var }
     * {@link Expr }
     * {@link ExternalTERMType }
     * 
     * 
     */
    public List<Object> getINDTERM() {
        if (indterm == null) {
            indterm = new ArrayList<Object>();
        }
        return this.indterm;
    }

    /**
     * Gets the value of the ordered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdered() {
        if (ordered == null) {
            return "yes";
        } else {
            return ordered;
        }
    }

    /**
     * Sets the value of the ordered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdered(String value) {
        this.ordered = value;
    }

}
