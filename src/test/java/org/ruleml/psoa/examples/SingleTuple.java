package org.ruleml.psoa.examples;

import org.ruleml.psoa.absyntax.AbstractSyntax.*;
import org.ruleml.psoa.absyntax.DefaultAbstractSyntax;

import java.util.ArrayList;
import java.util.List;

/**
 * This program creates a single-tuple psoa term and renders in PSOA Presentation Syntax.
 * See http://wiki.ruleml.org/index.php/PSOA_RuleML#Psoa_terms_with_slots_and_tuples for details.
 */

public class SingleTuple {

    public static void main(String[] args) {

        // factory object reference for creating the Abstract Syntax Objects
        DefaultAbstractSyntax factory = new DefaultAbstractSyntax();

        // create the oid term
        Term ind = factory.createConst_Constshort("o1");
        // create op term
        Term op = factory.createConst_Constshort("p");
        // create two tuple terms
        Term tupleTerm1 = factory.createConst_Constshort("a1");
        Term tupleTerm2 = factory.createConst_Constshort("a2");
        // store the tuple terms together in a list
        List<Term> tupleTermsList = new ArrayList<Term>();
        tupleTermsList.add(tupleTerm1);
        tupleTermsList.add(tupleTerm2);
        // wrap the tuple terms in a tuple
        Tuple tuple = factory.createTuple(tupleTermsList);
        // store the tuple as a list item
        List<Tuple> listOfTuples = new ArrayList<Tuple>();
        listOfTuples.add(tuple);
        // create the psoa term without any slot
        Psoa psoaTerm = factory.createPsoa(ind, op, listOfTuples, null);
        // create the atomic formula
        Atom atom = factory.createAtom(psoaTerm);
        // create a clause
        Clause clause = factory.createClause(atom);
        // create a rule consisting of only head but no body
        Rule rule = factory.createRule(null, clause);
        // the rule is an item of a list of sentences
        List<Sentence> sentence = new ArrayList<Sentence>();
        sentence.add(rule);
        // create a group with only one sentence
        Group group = factory.createGroup(sentence);
        // create the PSOA RuleML document
        Document doc = factory.createDocument(null, group);
        // render the single-tuple psoa term in presentation syntax
        System.out.println("PSOA RuleML/XML document rendered in Presentation Syntax: \n" + doc);
    }
}


