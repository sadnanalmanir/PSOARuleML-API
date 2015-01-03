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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://psoa.ruleml.org/lang/spec#}TERM" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ordered" type="{http://www.w3.org/2001/XMLSchema}string" fixed="yes" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "term"
})
@XmlRootElement(name = "args")
public class Args {

    @XmlElements({
        @XmlElement(name = "Ind", type = Ind.class),
        @XmlElement(name = "Rel", type = Rel.class),
        @XmlElement(name = "Var", type = Var.class),
        @XmlElement(name = "Expr", type = Expr.class),
        @XmlElement(name = "External", type = ExternalTERMType.class)
    })
    protected List<Object> term;
    @XmlAttribute(name = "ordered")
    protected String ordered;

    /**
     * Gets the value of the term property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the term property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTERM().add(newItem);
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
    public List<Object> getTERM() {
        if (term == null) {
            term = new ArrayList<Object>();
        }
        return this.term;
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
