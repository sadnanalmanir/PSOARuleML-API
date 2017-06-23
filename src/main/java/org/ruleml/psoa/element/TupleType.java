package org.ruleml.psoa.element;

/**
 * PSOA RuleML/XML independent(<tup><Tuple>...<Ind.../>...</Tuple></tup>) tuple,
 * dependent (<tupdep><Tuple>...<Ind.../>...</Tuple></tupdep>)tuple, and
 * implicitly dependent (<Ind.../>...<Ind.../>) tuple type.
 *
 * @author Mohammad Sadnan Al Manir
 */
public enum TupleType {

    INDEPENDENT("independent"), DEPENDENT("dependent"), ARGS("args");

    private String tupleType;

    private TupleType(String tupleType) {
        this.tupleType = tupleType;
    }

    public String getTupleType() {
        return tupleType;
    }

    public static TupleType getINDEPENDENT() {
        return INDEPENDENT;
    }

    public static TupleType getDEPENDENT() {
        return DEPENDENT;
    }

    public static TupleType getARGS() {
        return ARGS;
    }



}
