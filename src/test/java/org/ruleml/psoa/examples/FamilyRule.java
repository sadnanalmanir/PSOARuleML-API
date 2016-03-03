package org.ruleml.psoa.examples;

import org.ruleml.psoa.absyntax.AbstractSyntax.*;
import org.ruleml.psoa.absyntax.DefaultAbstractSyntax;

import java.util.ArrayList;
import java.util.List;

/**
 * This program creates a psoa rule and renders in PSOA Presentation Syntax.
 * The rule head contains an atomic formula and the rule body contains conjunctive
 * formula consisting of an atomic formula and an existentially quantified atomic formula.
 * See http://wiki.ruleml.org/index.php/PSOA_RuleML#Family_.28TBD.29 for details.
 */

public class FamilyRule {

    public static void main(String[] args) {

        // factory object reference for creating the Abstract Syntax Objects
        DefaultAbstractSyntax factory = new DefaultAbstractSyntax();

        /**
         * create an atomic formula in rule head
         */
        // holds the universally quantified quantified variables
        List<Var> varsUniversal = new ArrayList<Var>();
        // holds the universally quantified quantified variables
        List<Var> varsExistential = new ArrayList<Var>();

        /* creating atomic formula ?o#_family(_child->?Ch) in rule head */

        // create oid term
        Var f1_v1 = factory.createVar("o");
        varsUniversal.add(f1_v1);
        Term f1_Oid = f1_v1;

        // create op term
        Term f1_Op = factory.createConst_Constshort("family");

        // slot name
        Term f1_name = factory.createConst_Constshort("child");
        // slot value
        Var f1_v2 = factory.createVar("Ch");
        varsUniversal.add(f1_v2);
        Term f1_value = f1_v2;
        // create slot _child->?Ch
        Slot f1_slot = factory.createSlot(f1_name, f1_value);
        // add the slot as a list item
        List<Slot> f1_slots = new ArrayList<Slot>();
        f1_slots.add(f1_slot);

        // create psoa term
        Psoa f1_psoaTerm = factory.createPsoa(f1_Oid, f1_Op, null, f1_slots);
        // create an atom
        Atom f1_atom = factory.createAtom(f1_psoaTerm);
        // create the head
        Head f1_head = factory.createHead(null, f1_atom);
        // add the head atomic formula as a list item
        List<Head> headList = new ArrayList<Head>();
        headList.add(f1_head);

        /**
         * creating rule body
         */
        // create oid term
        Var f2_v1 = f1_v1;
        Term f2_Oid = f2_v1;
        // create op term
        Term f2_Op = f1_Op;
        // create slot name
        Term f2_name1 = factory.createConst_Constshort("husb");
        // create slot value
        Var f2_v2 = factory.createVar("Hu");
        Term f2_value1 = f2_v2;

        // create the slot _husb->?Hu
        Slot f2_slot1 = factory.createSlot(f2_name1, f2_value1);

        Term f2_name2 = factory.createConst_Constshort("wife");
        Var f2_v3 = factory.createVar("Wi");
        Term f2_value2 = f2_v3;

        // create the slot _wife->?Wi
        Slot f2_slot2 = factory.createSlot(f2_name2, f2_value2);

        // add the slots as list items
        List<Slot> f2_slots = new ArrayList<Slot>();
        f2_slots.add(f2_slot1);
        f2_slots.add(f2_slot2);
        // create a psoa term ?o#_family( _husb->?Hu _wife->?Wi)
        Psoa f2_psoaTerm = factory.createPsoa(f2_Oid, f2_Op, null, f2_slots);
        // create the atom from the psoa term
        Atom f2_atom = factory.createAtom(f2_psoaTerm);

        /* creating the existentially quantified formula */
        // create oid
        Var f3_v1 = factory.createVar("obj3");
        varsExistential.add(f3_v1);
        Term f3_Oid = f3_v1;
        // create op
        Term f3_Op = factory.createConst_Constshort("kid");
        // create 2 tuples
        Var f3_v2 = factory.createVar("Hu");
        varsUniversal.add(f3_v2);
        Term f3_TupleTerm1 = f3_v2;
        Var f3_v3 = factory.createVar("Ch");
        varsUniversal.add(f3_v3);
        Term f3_TupleTerm2 = f3_v3;
        // add two tuple terms to the list of term
        List<Term> f3_TupleList = new ArrayList<Term>();
        f3_TupleList.add(f3_TupleTerm1);
        f3_TupleList.add(f3_TupleTerm2);
        // wrap the tuple terms in a tuple
        Tuple f3_Tuple = factory.createTuple(f3_TupleList);
        // store the tuple as a list item
        List<Tuple> f3_All_Tuples = new ArrayList<Tuple>();
        f3_All_Tuples.add(f3_Tuple);

        // create the psoa term ?obj3#_kid(?Hu ?Ch)
        Psoa f3_psoaTerm = factory.createPsoa(f3_Oid, f3_Op, f3_All_Tuples, null);
        // create an atom from the psoa term
        Atom f3_atom = factory.createAtom(f3_psoaTerm);
        // create the existential formula Exists ?obj3 ( ?obj3#_kid(?Hu ?Ch) )
        Formula_Exists formula2 = factory.createFormula_Exists(varsExistential, f3_atom);
        // add both the atomic formulas and the existentially quantified formula to the list of formulas
        List<Formula> formulas = new ArrayList<Formula>();
        formulas.add(f2_atom);
        formulas.add(formula2);
        // express these two formulas as a conjunctive formula
        Formula_And body = factory.createFormula_And(formulas);
        // create the implication
        Implies implication = factory.createImplies(headList, body);
        // create the clause
        Clause clause = factory.createClause(implication);
        // create the rule
        Rule rule = factory.createRule(varsUniversal, clause);
        // the rule is an item of a list of sentences
        List<Sentence> sentences = new ArrayList<Sentence>();
        sentences.add(rule);
        // create a group with only one sentence
        Group group = factory.createGroup(sentences);
        // create the document
        Document doc = factory.createDocument(null, group);
        // render the rule in presentation syntax
        System.out.println("PSOA RuleML/XML document rendered in Presentation Syntax: \n" + doc);
    }
}
