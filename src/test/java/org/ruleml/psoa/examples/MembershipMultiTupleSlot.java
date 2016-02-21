package org.ruleml.psoa.examples;

import org.ruleml.psoa.absyntax.AbstractSyntax;
import org.ruleml.psoa.absyntax.DefaultAbstractSyntax;
import org.ruleml.psoa.vocab.XMLSchemaDatatype;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadnana on 21/02/16.
 */
public class MembershipMultiTupleSlot {
    public static void main(String[] args) {

        DefaultAbstractSyntax das = new DefaultAbstractSyntax();

// create the oid term
        AbstractSyntax.Term f3_Oid = das.createConst_Constshort("f3");
// create the op term
        AbstractSyntax.Term f3_Op = das.createConst_Constshort("family");

// create the first two terms
        AbstractSyntax.Term f3_multiTupleTerm1 = das.createConst_Constshort("Mike");
        AbstractSyntax.Term f3_multiTupleTerm2 = das.createConst_Constshort("Jessie");

// create the integer typed literal term
        AbstractSyntax.Symspace f3_multiTupleTerm3_Type = das.createSymspace(XMLSchemaDatatype.INTEGER.toString());
        AbstractSyntax.Term f3_multiTupleTerm3 = das.createConst_Literal("1951", f3_multiTupleTerm3_Type);

// keep the first two terms together in a list
        List<AbstractSyntax.Term> f3_multiTupleList1 = new ArrayList<AbstractSyntax.Term>();
        f3_multiTupleList1.add(f3_multiTupleTerm1);
        f3_multiTupleList1.add(f3_multiTupleTerm2);

// keep the third term in a separate list
        List<AbstractSyntax.Term> f3_multiTupleList2 = new ArrayList<AbstractSyntax.Term>();
        f3_multiTupleList2.add(f3_multiTupleTerm3);

// wrap the first two terms inside a Tuple and the third term into a separate tuple
        AbstractSyntax.Tuple f3_multiTuple1 = das.createTuple(f3_multiTupleList1);
        AbstractSyntax.Tuple f3_multiTuple2 = das.createTuple(f3_multiTupleList2);

// express tuples as multi-tuple
        List<AbstractSyntax.Tuple> f3_allTuples = new ArrayList<AbstractSyntax.Tuple>();
        f3_allTuples.add(f3_multiTuple1);
        f3_allTuples.add(f3_multiTuple2);

// create the first slot
        AbstractSyntax.Term name1 = das.createConst_Constshort("child");
        AbstractSyntax.Term value1 = das.createConst_Constshort("Fred");
        AbstractSyntax.Slot slot1 = das.createSlot(name1, value1);

// create the second slot
        AbstractSyntax.Term name2 = das.createConst_Constshort("child");
        AbstractSyntax.Term value2 = das.createConst_Constshort("Jane");
        AbstractSyntax.Slot slot2 = das.createSlot(name2, value2);

        List<AbstractSyntax.Slot> f3_allSlots = new ArrayList<AbstractSyntax.Slot>();
        f3_allSlots.add(slot1);
        f3_allSlots.add(slot2);

        // create the psoa term
        AbstractSyntax.Psoa psoaTerm = das.createPsoa(f3_Oid, f3_Op, f3_allTuples, f3_allSlots);
        // create an atom from the psoa term
        AbstractSyntax.Atom atom = das.createAtom(psoaTerm);
        // create a clause containing an atomic formula
        AbstractSyntax.Clause clause = das.createClause(atom);
        // create the rule
        AbstractSyntax.Rule rule = das.createRule(null, clause);
        // create a sentence containing the rule
        List<AbstractSyntax.Sentence> sentence = new ArrayList<AbstractSyntax.Sentence>();
        sentence.add(rule);
        // create a group with only one sentence
        AbstractSyntax.Group group = das.createGroup(sentence);
        // create the document
        AbstractSyntax.Document doc = das.createDocument(null, group);
        // render the rulebase as presentation syntax
        System.out.println("Translated document : \n" + doc);

    }

}
