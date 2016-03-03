package org.ruleml.psoa.examples;

import org.ruleml.psoa.absyntax.AbstractSyntax.*;
import org.ruleml.psoa.absyntax.DefaultAbstractSyntax;
import org.ruleml.psoa.vocab.XMLSchemaDatatype;

import java.util.ArrayList;
import java.util.List;

/**
 * This program creates a psoa term with slots and tuples and renders in PSOA Presentation Syntax.
 * See http://wiki.ruleml.org/index.php/PSOA_RuleML#Single-tuple_psoa_term for details.
 */

public class MembershipMultiTupleSlot {

    public static void main(String[] args) {
        // factory object reference for creating the Abstract Syntax Objects
        DefaultAbstractSyntax factory = new DefaultAbstractSyntax();

        // create the oid term
        Term f3_Oid = factory.createConst_Constshort("f3");
        // create the op term
        Term f3_Op = factory.createConst_Constshort("family");

        // create the first two terms
        Term f3_multiTupleTerm1 = factory.createConst_Constshort("Mike");
        Term f3_multiTupleTerm2 = factory.createConst_Constshort("Jessie");

        // create the integer typed literal term
        Symspace f3_multiTupleTerm3_Type = factory.createSymspace(XMLSchemaDatatype.INTEGER.toString());
        Term f3_multiTupleTerm3 = factory.createConst_Literal("1951", f3_multiTupleTerm3_Type);

        // keep the first two terms together in a list
        List<Term> f3_multiTupleList1 = new ArrayList<Term>();
        f3_multiTupleList1.add(f3_multiTupleTerm1);
        f3_multiTupleList1.add(f3_multiTupleTerm2);

        // keep the third term in a separate list
        List<Term> f3_multiTupleList2 = new ArrayList<Term>();
        f3_multiTupleList2.add(f3_multiTupleTerm3);

        // wrap the first two terms inside a Tuple and the third term into a separate tuple
        Tuple f3_multiTuple1 = factory.createTuple(f3_multiTupleList1);
        Tuple f3_multiTuple2 = factory.createTuple(f3_multiTupleList2);

        // express tuples as multi-tuple
        List<Tuple> f3_allTuples = new ArrayList<Tuple>();
        f3_allTuples.add(f3_multiTuple1);
        f3_allTuples.add(f3_multiTuple2);

        // create the first slot
        Term name1 = factory.createConst_Constshort("child");
        Term value1 = factory.createConst_Constshort("Fred");
        Slot slot1 = factory.createSlot(name1, value1);

        // create the second slot
        Term name2 = factory.createConst_Constshort("child");
        Term value2 = factory.createConst_Constshort("Jane");
        Slot slot2 = factory.createSlot(name2, value2);
        // store the slots as a list items
        List<Slot> f3_allSlots = new ArrayList<Slot>();
        f3_allSlots.add(slot1);
        f3_allSlots.add(slot2);

        // create the psoa term
        Psoa psoaTerm = factory.createPsoa(f3_Oid, f3_Op, f3_allTuples, f3_allSlots);
        // create an atom from the psoa term
        Atom atom = factory.createAtom(psoaTerm);
        // create a clause containing an atomic formula
        Clause clause = factory.createClause(atom);
        // create the rule
        Rule rule = factory.createRule(null, clause);
        // the rule is an item of a list of sentences
        List<Sentence> sentence = new ArrayList<Sentence>();
        sentence.add(rule);
        // create a group with only one sentence
        Group group = factory.createGroup(sentence);
        // create the document
        Document doc = factory.createDocument(null, group);
        // render the psoa term with slots and tuples in presentation syntax
        System.out.println("PSOA RuleML/XML document rendered in Presentation Syntax: \n" + doc);
    }
}
