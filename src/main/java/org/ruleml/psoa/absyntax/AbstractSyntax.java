/**
 * This file is part of PSOARuleML-API.
 * This class contains the interfaces for the Abstract Syntax building blocks.
 * <p/>
 * PSOARuleML-API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p/>
 * PSOARuleML-API is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * PSOARuleML-API. If not, see <http://www.gnu.org/licenses/>.
 */
package org.ruleml.psoa.absyntax;

import java.util.Collection;
import java.util.Set;

/**
 * @author Mohammad Sadnan Al Manir, University of New Brunswick, Saint John
 */
public interface AbstractSyntax {

    /**
     * Creates document.
     *
     * @param imports can be null or empty
     * @param group   can be null
     * @return document
     */
    Document createDocument(Iterable<Import> imports, Group group);

    /**
     * Creates import.
     *
     * @param iri     nonnull
     * @param profile can be null or empty
     * @return Document iri to import
     */
    Import createImport(String iri, Profile profile);

    /**
     * Creates Profile.
     *
     * @param iri nonnull
     * @return Profile
     */
    Profile createProfile(String iri);

    /**
     * Creates Group.
     *
     * @param sentences collection of sentences
     * @return Group
     */
    Group createGroup(Iterable<Sentence> sentences);

    /**
     * Creates Rule.
     *
     * @param vars   can be null or empty
     * @param matrix nonnull
     * @return Rule
     */
    Rule createRule(Iterable<Var> vars, Clause matrix);

    /**
     * Creates Clause.
     *
     * @param implication nonnull
     * @return Clause
     */
    Clause createClause(Implies implication);

    /**
     * Creates Clause.
     *
     * @param formula nonnull
     * @return Clause
     */
    Clause createClause(Atomic formula);

    /**
     * Creates Clause.
     *
     * @param head nonnull
     * @return Clause
     */
    Clause createClause(Head head);

    /**
     * Creates Implies.
     *
     * @param heads     nonnull
     * @param condition can be null or empty
     * @return Implies
     */
    Implies createImplies(Iterable<Head> heads, Formula condition);

    /**
     * Creates a rule head by applying an existential quantifier over the given
     * variables to the formula.
     *
     * @param vars   can be null or empty
     * @param matrix nonnull
     * @return conclusion
     */
    Head createHead(Iterable<Var> vars, Atomic matrix);

    /**
     * Creates conjunction of formulas.
     *
     * @param formulas nonnull
     * @return conjunction of formulas
     */
    Formula_And createFormula_And(Iterable<Formula> formulas);

    /**
     * Creates disjunction of formulas.
     *
     * @param formulas nonnull
     * @return disjunction of formulas
     */
    Formula_Or createFormula_Or(Iterable<Formula> formulas);

    /**
     * Creates existentially quantified formula.
     *
     * @param vars   nonnull
     * @param matrix nonnull
     * @return existentially quantified formula
     */
    Formula_Exists createFormula_Exists(Iterable<Var> vars,
                                        Formula matrix);

    /**
     * Creates external atomic formula.
     *
     * @param atom nonnull
     * @return external atomic formula
     */
    Formula_External createFormula_External(Atom atom);

    /**
     * Creates an atomic formula.
     *
     * @param term nonnull
     * @return atomic formula
     */
    Atom createAtom(Psoa term);

    /**
     * Creates equality expression.
     *
     * @param lhs nonnull
     * @param rhs nonnull
     * @return equality expression
     */
    Equal createEqual(Term lhs, Term rhs);

    /**
     * Creates subclass expression.
     *
     * @param sub nonnull
     * @param sup nonnull
     * @return subclass expression
     */
    Subclass createSubclass(Term sub, Term sup);

    /**
     * Creates a psoa term.
     *
     * @param object    can be null or empty
     * @param classTerm nonnull
     * @param tuples    can be null or empty
     * @param slots     can be null or empty
     * @return psoa term
     */
    Psoa createPsoa(Term object, Term classTerm, Iterable<Tuple> tuples,
                    Iterable<Slot> slots);

    /**
     * Creates argument (tuple).
     *
     * @param terms nonnull
     * @return argument (tuple)
     */
    Tuple createTuple(Iterable<Term> terms);

    /**
     * Creates slot as a name value pair
     *
     * @param name  nonnull
     * @param value nonnull
     * @return slot as a name value pair
     */
    Slot createSlot(Term name, Term value);

    /**
     * Creates typed literal constants.
     *
     * @param literal  nonnull
     * @param symspace nonnull
     * @return typed literal constants
     */
    Const_Literal createConst_Literal(String literal, Symspace symspace);

    /**
     * Creates untyped constants.
     *
     * @param name nonnull
     * @return untyped constants
     */
    Const_Constshort createConst_Constshort(String name);

    /**
     * Creates a variable.
     *
     * @param name can be null or "" for anonymous variables.
     * @return variable
     */
    Var createVar(String name);

    /**
     * Creates external psoa atom.
     *
     * @param externalexpr nonnull
     * @return external psoa atom
     */
    External createExternalExpr(Psoa externalexpr);

    /**
     * Creates type of constants.
     *
     * @param value nonnull
     * @return type of constant
     */
    Symspace createSymspace(String value);

    /**
     * Common interface for all abstract syntax constructs.
     */
    interface Construct {

        /**
         * Renders the object in the presentation syntax.
         *
         * @return object in presentation syntax
         */
        String toString();

        /**
         * Renders the objects in the presentation syntax with the specified
         * base indentation.
         *
         * @param indent specified indentation
         * @return objects in the presentation syntax
         */
        String toString(String indent);

    }

    /**
     * PSOA document containing Import directives NOTE: Base and Prefixes will
     * be handled explicitly.
     */
    interface Document extends Construct {

        Iterable<Import> getImports();

        /**
         * @return possibly null
         */
        Group getGroup();
    }

    /**
     * Import directive containing location and optional profile
     */
    interface Import extends Construct {

        /**
         * @return location
         */
        String getIRI();

        /**
         * @return profile
         */
        Profile getProfile();

    }

    /**
     * Optional profile for Import directive
     */
    interface Profile extends Construct {

        /**
         * @return profile if set with the locator
         */
        String getIRI();
    }

    /**
     * Group containing Sentence(s) (also known as Group elements).
     */
    interface Group extends Sentence {

        /**
         * @return nonempty sequence of Groups or nested Groups containing Rules
         */
        Collection<? extends Sentence> getSentence();

    }

    // createX methods:

    /**
     * Common base for Group and Rule.
     */
    interface Sentence extends Construct {

        Rule asRule();

        Group asGroup();

    }

    /**
     * Rule containing Variables and Clause.
     */
    interface Rule extends Sentence {

        /**
         * @return nonempty sequence of Variables
         */
        Collection<Var> getVariables();

        /**
         * @return Clause
         */
        Clause getClause();

    }

    /**
     * Common base for Implies and Atomic
     */
    interface Clause extends Construct {

        boolean isImplies();

        Implies asImplies();

        boolean isAtomic();

        Atomic asAtomic();

        boolean isHead();

        Head asHead();

    }

    /**
     * Implication containing Condition and Conclusion.
     */
    interface Implies extends Construct {

        /**
         * @return nonempty sequence of Heads
         */
        Collection<? extends Head> getHead();

        /**
         * @return possibly null, if there is no condition
         */
        Formula getBody();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Common base for Atom, Equal or Subclass Atomic formula.
     */
    interface Atomic extends Formula {

        Atom asAtom();

        Equal asEqual();

        Subclass asSubclass();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Conclusion formula containing Heads.
     */
    interface Head extends Construct {

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

        /**
         * @return nonempty sequence of Variables
         */
        Collection<Var> getVariables();

        /**
         * @return Atomic formula
         */
        Atomic getAtomic();

    }

    /**
     * Common base for Conjunction, Disjunction, Existential, Atomic or External
     * Formula.
     */
    interface Formula extends Construct {

        Formula_And asAndFormula();

        Formula_Or asOrFormula();

        Formula_Exists asExistsFormula();

        Atomic asAtomic();

        Formula_External asExternalFormula();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Conjunction of Formulas as Condition formulas.
     */
    interface Formula_And extends Formula {

        /**
         * @return nonempty sequence of Formulas
         */
        Collection<? extends Formula> getFormulas();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Disjunction of Formulas as Condition formulas.
     */
    interface Formula_Or extends Formula {

        /**
         * @return nonempty sequence of Formulas
         */
        Collection<? extends Formula> getFormulas();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Formula with Existentially quantified formulas as Condition formulas.
     */
    interface Formula_Exists extends Formula {

        /**
         * @return nonempty sequence of variables
         */
        Collection<Var> getVariables();

        /**
         * @return Formula.
         */
        Formula getFormula();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();
    }

    /**
     * External Formula as Condition formula.
     */
    interface Formula_External extends Formula {

        /**
         * @return External Atom
         */
        Atom getContent();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Atom as Psoa term.
     */
    interface Atom extends Atomic {

        /**
         * @return Psoa
         */
        Psoa getPsoa();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Equality Atomic formula.
     */
    interface Equal extends Atomic {

        /**
         * @return left hand Term
         */
        Term getLeft();

        /**
         * @return right hand Term
         */
        Term getRight();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Subclass Atomic formula.
     */
    interface Subclass extends Atomic {

        /**
         * @return subclass Term
         */
        Term getSubclass();

        /**
         * @return superclass Term
         */
        Term getSuperclass();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Psoa atomic term with membership, optionally tuples and slots.
     */
    interface Psoa extends Expr {

        /**
         * @return instance Term
         */
        Term getInstance();

        /**
         * @return class Term
         */
        Term getClassExpr();

        /**
         * @return nonempty sequence of Positional Atoms
         */
        Collection<? extends Tuple> getPositionalAtom();

        /**
         * @return nonempty sequence of Slotted Atoms
         */
        Collection<? extends Slot> getSlottedAtom();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

        String toString(String indent);

    }

    /**
     * Slotted Term with Name, Value pair.
     */
    interface Slot extends Construct {

        /**
         * @return slot Name Term
         */
        Term getName(); // not really a name rather term

        /**
         * @return superclass Term
         */
        Term getValue();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Tuples as positional arguments.
     */
    interface Tuple extends Construct {

        /**
         * @return nonempty sequence of Arguments
         */
        Collection<? extends Term> getArguments();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Common base for Constants, Variables, External Expressions.
     */
    interface Term extends Construct, Comparable<Term> {

        Const asConst();

        Var asVar();

        Psoa asPsoa();

        External asExternal();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Common base for Literal Constants and Short Constants.
     */
    interface Const extends Term {

        Const_Literal asConstLiteral();

        Const_Constshort asConstshort();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Literal constants with type.
     */
    interface Const_Literal extends Const {

        /**
         * @return literal
         */
        String getLiteral(); // cant be null

        /**
         * @return literal type
         */
        Symspace getSymspace();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Short constants without literal type.
     */
    interface Const_Constshort extends Const {

        /**
         * @return short constants
         */
        String getConstshort();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Variable.
     */
    interface Var extends Term {

        /**
         * @return variable
         */
        String getName();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Base for Expression.
     */
    interface Expr extends Term {

        Psoa asPsoa();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();
    }

    /**
     * Externally defined Expression.
     */
    interface External extends Term {

        /**
         * @return Externally defined Psoa Term
         */
        Psoa getExternalExpr();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

    /**
     * Literal type for literal constants.
     */
    interface Symspace extends Construct {

        String getValue();

        /**
         * The set of all free variables.
         */
        Set<Var> variables();

    }

}