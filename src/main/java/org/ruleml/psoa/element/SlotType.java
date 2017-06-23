package org.ruleml.psoa.element;

/**
 * PSOA RuleML/XML dependent, independent slot type.
 *
 * @author Mohammad Sadnan Al Manir
 */
public enum SlotType {

    INDEPENDENT("independent"), DEPENDENT("dependent");

    String slotType;

    private SlotType(String slotType) {
        this.slotType = slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getSlotType() {
        return slotType;
    }

    public static SlotType getINDEPENDENT() {
        return INDEPENDENT;
    }

    public static SlotType getDEPENDENT() {
        return DEPENDENT;
    }



}
