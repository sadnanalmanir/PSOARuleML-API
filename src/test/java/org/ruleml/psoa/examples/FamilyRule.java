package org.ruleml.psoa.examples;

import org.ruleml.psoa.absyntax.AbstractSyntax;
import org.ruleml.psoa.absyntax.DefaultAbstractSyntax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadnana on 21/02/16.
 */
public class FamilyRule {
    public static void main(String[] args) {

        DefaultAbstractSyntax das = new DefaultAbstractSyntax();

        /**
         * creating Atomic formula in rule head
         */
        // To hold the quantified variables
        List<AbstractSyntax.Var> universalQuantifiedVars = new ArrayList<AbstractSyntax.Var>();
        List<AbstractSyntax.Var> existentialQuantifiedVars = new ArrayList<AbstractSyntax.Var>();
        //creating ?o#_family( _child->?Ch)
        // create oid term
        AbstractSyntax.Var f1_v1 = das.createVar("o");
        universalQuantifiedVars.add(f1_v1);
        AbstractSyntax.Term f1_Oid = f1_v1;
        // create op term
        AbstractSyntax.Term f1_Op = das.createConst_Constshort("family");
        // create slot1
        AbstractSyntax.Term f1_name = das.createConst_Constshort("child");
        AbstractSyntax.Var f1_v2 = das.createVar("Ch");
        universalQuantifiedVars.add(f1_v2);
        AbstractSyntax.Term f1_value = f1_v2;
        AbstractSyntax.Slot f1_slot = das.createSlot(f1_name, f1_value);
        List<AbstractSyntax.Slot> f1_slots = new ArrayList<AbstractSyntax.Slot>();
        f1_slots.add(f1_slot);
        // create psoa term
        AbstractSyntax.Psoa f1_psoaTerm = das.createPsoa(f1_Oid, f1_Op, null, f1_slots);
        // create an atom
        AbstractSyntax.Atom f1_atom = das.createAtom(f1_psoaTerm);
        AbstractSyntax.Head f1_head = das.createHead(null, f1_atom);
        List<AbstractSyntax.Head> headList = new ArrayList<AbstractSyntax.Head>();
        headList.add(f1_head);

        /**
         * creating rule body
         */
        // create oid term
        AbstractSyntax.Var f2_v1 = f1_v1;
        //universalQuantifiedVars.add(f1_v1);
        AbstractSyntax.Term f2_Oid = f2_v1;
        // create op term
        AbstractSyntax.Term f2_Op = f1_Op;
        // create slot1
        AbstractSyntax.Term f2_name1 = das.createConst_Constshort("husb");
        AbstractSyntax.Var f2_v2 = das.createVar("Hu");
        //universalQuantifiedVars.add(f1_v2);
        AbstractSyntax.Term f2_value1 = f2_v2;
        AbstractSyntax.Slot f2_slot1 = das.createSlot(f2_name1, f2_value1);
        AbstractSyntax.Term f2_name2 = das.createConst_Constshort("wife");
        AbstractSyntax.Var f2_v3 = das.createVar("Wi");
        //universalQuantifiedVars.add(f1_v2);
        AbstractSyntax.Term f2_value2 = f2_v3;
        AbstractSyntax.Slot f2_slot2 = das.createSlot(f2_name2, f2_value2);

        List<AbstractSyntax.Slot> f2_slots = new ArrayList<AbstractSyntax.Slot>();
        f2_slots.add(f2_slot1);
        f2_slots.add(f2_slot2);
        // create psoa term
        AbstractSyntax.Psoa f2_psoaTerm = das.createPsoa(f2_Oid, f2_Op, null, f2_slots);
        // create an atom
        AbstractSyntax.Atom f2_atom = das.createAtom(f2_psoaTerm);

        // create oid
        AbstractSyntax.Var f3_v1 = das.createVar("obj3");
        existentialQuantifiedVars.add(f3_v1);
        AbstractSyntax.Term f3_Oid = f3_v1;
        // create op
        AbstractSyntax.Term f3_Op = das.createConst_Constshort("kid");
        // create 2 tuples
        AbstractSyntax.Var f3_v2 = das.createVar("Hu");
        universalQuantifiedVars.add(f3_v2);
        AbstractSyntax.Term f3_TupleTerm1 = f3_v2;
        AbstractSyntax.Var f3_v3 = das.createVar("Ch");
        universalQuantifiedVars.add(f3_v3);
        AbstractSyntax.Term f3_TupleTerm2 = f3_v3;

        List<AbstractSyntax.Term> f3_TupleList = new ArrayList<AbstractSyntax.Term>();
        f3_TupleList.add(f3_TupleTerm1);
        f3_TupleList.add(f3_TupleTerm2);

        AbstractSyntax.Tuple f3_Tuple = das.createTuple(f3_TupleList);

        List<AbstractSyntax.Tuple> f3_All_Tuples = new ArrayList<AbstractSyntax.Tuple>();
        f3_All_Tuples.add(f3_Tuple);

        // create psoa term
        AbstractSyntax.Psoa f3_psoaTerm = das.createPsoa(f3_Oid, f3_Op, f3_All_Tuples, null);
        // create an atom
        AbstractSyntax.Atom f3_atom = das.createAtom(f3_psoaTerm);

        AbstractSyntax.Formula_Exists formula2 = das.createFormula_Exists(existentialQuantifiedVars, f3_atom);

        List<AbstractSyntax.Formula> formulas = new ArrayList<AbstractSyntax.Formula>();
        formulas.add(f3_atom);
        formulas.add(formula2);

        AbstractSyntax.Formula_And body = das.createFormula_And(formulas);

        AbstractSyntax.Implies implication = das.createImplies(headList, body);
        // create a clause
        AbstractSyntax.Clause clause = das.createClause(implication);
        // create a rule
        AbstractSyntax.Rule rule = das.createRule(universalQuantifiedVars, clause);

        List<AbstractSyntax.Sentence> ges = new ArrayList<AbstractSyntax.Sentence>();
        ges.add(rule);

        AbstractSyntax.Group group = das.createGroup(ges);

        AbstractSyntax.Document doc = das.createDocument(null, group);

        System.out.println("Translated document : \n" + doc);

    }

}
