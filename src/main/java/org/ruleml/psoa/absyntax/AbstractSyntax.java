/**
 * This file is part of PSOARuleML-API.
 *
 * PSOARuleML-API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * PSOARuleML-API is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * PSOARuleML-API. If not, see <http://www.gnu.org/licenses/>.
 */
package org.ruleml.psoa.absyntax;

import java.util.*;

/**
 *
 * @author Mohammad Sadnan Al Manir University of New Brunswick
 */
public interface AbstractSyntax {

    /**
     * Common interface for all abstract syntax constructs.
     */
    public interface Construct {

        /**
         * Renders the object in the presentation syntax.
         *
         * @return an individual construct
         */
        public String toString();

        /**
         * Renders the objects in the presentation syntax with the specified
         * base indentation.
         *
         * @param indent specified indentation
         * @return an individual construct with the specified indentation
         */
        public String toString(String indent);
    }

    /**
     * PSOA document containing Import directives NOTE: Base and Prefixes will
     * be handled explicitly.
     */
    public interface Document extends Construct {

        public Iterable<Import> getImports();

        /**
         * @return Group formula(s)
         */
        public Group getGroup();
    }

    /**
     * Import directive containing location and optional profile.
     */
    public interface Import extends Construct {

        /**
         * @return location
         */
        public String getIRI();

        /**
         * @return profile
         */
        public Profile getProfile();
    }

    /**
     *
     * Optional profile for Import directive.
     *
     */
    public interface Profile extends Construct {

        /**
         * @return profile if set with the locator
         */
        public String getIRI();
    }

    /**
     * Group containing Sentence(s) (also known as Group elements).
     */
    public interface Group extends GroupElement {

        /**
         * @return nonempty sequence of Groups or nested Groups containing Rules
         */
        public Collection<? extends GroupElement> getGroupElement();
    }

    /**
     * Common base for Group and Rule.
     */
    public interface GroupElement extends Construct {

        public Rule asRule();

        public Group asGroup();
    }

    /**
     *
     * Rule containing Variables and Clause.
     *
     */
    public interface Rule extends GroupElement {

        /**
         * @return nonempty sequence of Variables
         */
        public Collection<Var> getVariables();

        /**
         * @return Clause
         */
        public Clause getClause();
    }

    /**
     * Common base for Implies and Atomic.
     */
    public interface Clause extends Construct {

        public boolean isImplies();

        public Implies asImplies();

        public boolean isAtomic();

        public Atomic asAtomic();
    }

    /**
     *
     * Implication containing Condition and Conclusion.
     *
     */
    public interface Implies extends Construct {

        /**
         * @return nonempty sequence of Heads
         */
        public Collection<? extends Head> getHead();

        /**
         * @return possibly null, if there are no condition
         */
        public Formula getBody();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    } // interface Implies

    /**
     * Common base for Atom, Equal or Subclass Atomic formula.
     */
    public interface Atomic extends Formula {

        public Atom asAtom();

        public Equal asEqual();

        public Subclass asSubclass();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Conclusion formula containing Heads.
     *
     */
    public interface Head extends Construct {

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();

        /**
         * @return nonempty sequence of Variables
         */
        public Collection<Var> getVariables();

        /**
         * @return Atomic formula
         */
        public Atomic getAtomic();
    }

    /**
     * Common base for Conjunction, Disjunction, Existential, Atomic or External
     * Formula.
     */
    public interface Formula extends Construct {

        public Formula_And asAndFormula();

        public Formula_Or asOrFormula();

        public Formula_Exists asExistsFormula();

        public Atomic asAtomic();

        public Formula_External asExternalFormula();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Conjunction of Formulas as Condition formulas.
     *
     */
    public interface Formula_And extends Formula {

        /**
         * @return nonempty sequence of Formulas
         */
        public Collection<? extends Formula> getFormulas();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Disjunction of Formulas as Condition formulas.
     *
     */
    public interface Formula_Or extends Formula {

        /**
         * @return nonempty sequence of Formulas
         */
        public Collection<? extends Formula> getFormulas();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Formula with Existentially quantified formulas as Condition formulas.
     *
     */
    public interface Formula_Exists extends Formula {

        /**
         * @return nonempty sequence of variables
         */
        public Collection<Var> getVariables();

        /**
         * @return Formula.
         */
        public Formula getFormula();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * External Formula as Condition formula.
     *
     */
    public interface Formula_External extends Formula {

        /**
         * @return External Atom
         */
        public Atom getContent();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Atom as Psoa term.
     *
     */
    public interface Atom extends Atomic {

        /**
         * @return Psoa
         */
        public Psoa getPsoa();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Equality Atomic formula.
     *
     */
    public interface Equal extends Atomic {

        /**
         * @return left hand Term
         */
        public Term getLeft();

        /**
         * @return right hand Term
         */
        public Term getRight();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Subclass Atomic formula.
     *
     */
    public interface Subclass extends Atomic {

        /**
         * @return subclass Term
         */
        public Term getSubclass();

        /**
         * @return superclass Term
         */
        public Term getSuperclass();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Psoa atomic term with membership, optionally tuples and slots.
     *
     */
    public interface Psoa extends Expr {

        /**
         * @return instance Term
         */
        public Term getInstance();

        /**
         * @return class Term
         */
        public Term getClassExpr();

        /**
         * @return nonempty sequence of Positional Atoms
         */
        public Collection<? extends Tuple> getPositionalAtom();

        /**
         * @return nonempty sequence of Slotted Atoms
         */
        public Collection<? extends Slot> getSlottedAtom();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();

        public String toString(String indent);
    }

    /**
     *
     * Slotted Term with Name, Value pair.
     *
     */
    public interface Slot extends Construct {

        /**
         * @return slot Name Term
         */
        public Term getName(); // not really a name rather term

        /**
         * @return superclass Term
         */
        public Term getValue();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Tuples (arg) as positional arguments.
     *
     */
    public interface Tuple extends Construct {

        /**
         * @return nonempty sequence of Arguments
         */
        public Collection<? extends Term> getArguments();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     * Common base for Constants, Variables, External Expressions.
     */
    public interface Term extends Construct, Comparable<Term> {

        public Const asConst();

        public Var asVar();

        public Psoa asPsoa();

        public External asExternal();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     * Common base for Literal Constants and Short Constants.
     */
    public interface Const extends Term {

        public Const_Literal asConstLiteral();

        public Const_Constshort asConstshort();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Literal constants with type.
     *
     */
    public interface Const_Literal extends Const {

        /**
         * @return literal
         */
        public String getLiteral();

        /**
         * @return literal type
         */
        public Symspace getSymspace();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Short constants without literal type.
     *
     */
    public interface Const_Constshort extends Const {

        /**
         * @return short constants
         */
        public String getConstshort();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Variable.
     *
     */    
    public interface Var extends Term {

        /**
         * @return variable
         */
        public String getName();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Base for Expression.
     *
     */
    public interface Expr extends Term {

        public Psoa asPsoa();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Externally defined Expression.
     *
     */
    public interface External extends Term {

        /**
         * @return Externally defined Psoa Term
         */
        public Psoa getExternalExpr();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    /**
     *
     * Literal type for literal constants.
     *
     */
    public interface Symspace extends Construct {

        public String getValue();

        /**
         * The set of all free variables.
         */
        public Set<Var> variables();
    }

    // createX methods:
    /**
     * Creates document.
     * @param imports can be null or empty
     * @param group can be null
     * @return document
     */

    public Document createDocument(Iterable<Import> imports, Group group);

    /**
     * Creates import.
     * 
     * @param iri nonnull
     * @param profile can be null or empty
     * @return import
     */
    public Import createImport(String iri, Profile profile);

    /**
     * Creates import.
     * 
     * @param iri nonnull
     * @return import
     */
    public Profile createProfile(String iri);

    /**
     * Creates profile.
     * 
     * @param elements nonnull
     * @return profile
     */
    public Group createGroup(Iterable<GroupElement> elements);

    /**
     * Creates Group.
     * 
     * @param vars can be null or empty
     * @param matrix nonnull
     * @return Group
     */
    public Rule createRule(Iterable<Var> vars, Clause matrix);

    /**
     * Creates rule.
     * 
     * @param implication nonnull
     * @return rule
     */
    public Clause createClause(Implies implication);

    /**
     * Creates clause.
     * 
     * @param formula nonnull
     * @return clause
     */
    public Clause createClause(Atomic formula);

    /**
     * Creates implication.
     * 
     * @param heads nonnull
     * @param condition can be null or empty
     * @return implication
     */
    public Implies createImplies(Iterable<Head> heads, Formula condition);

    /**
     * Creates a rule head by applying an existential quantifier over the given
     * variables to the formula.
     *
     * @param vars can be null or empty
     * @param matrix nonnull
     * @return conclusion
     */
    public Head createHead(Iterable<Var> vars, Atomic matrix);

    /**
     * Creates conjunction of formulas.
     *
     * @param formulas nonnull
     * @return conjunction of formulas
     */
    public Formula_And createFormula_And(Iterable<Formula> formulas);

    /**
     * Creates disjunction of formulas.
     *
     * @param formulas nonnull
     * @return disjunction of formulas
     */
    public Formula_Or createFormula_Or(Iterable<Formula> formulas);

    /**
     * Creates existentially quantified formula.
     * 
     * @param vars nonnull
     * @param matrix nonnull
     * @return existentially quantified formula
     */
    public Formula_Exists createFormula_Exists(Iterable<Var> vars,
            Formula matrix);

    /**
     * Creates external atomic formula.
     * 
     * @param atom nonnull
     * @return external atomic formula
     */
    public Formula_External createFormula_External(Atom atom);

    /**
     * Creates an atomic formula.
     * 
     * @param term nonnull
     * @return atomic formula
     */
    public Atom createAtom(Psoa term);

    /**
     * Creates equality expression.
     * 
     * @param lhs nonnull
     * @param rhs nonnull
     * @return equality expression
     */
    public Equal createEqual(Term lhs, Term rhs);

    /**
     * Creates subclass expression.
     * 
     * @param sub nonnull
     * @param sup nonnull
     * @return subclass expression
     */
    public Subclass createSubclass(Term sub, Term sup);

    /**
     * Creates a psoa term.
     * 
     * @param object nonnull
     * @param classTerm nonnull
     * @param tuples can be null or empty
     * @param slots can be null or empty
     * @return psoa term
     */
    public Psoa createPsoa(Term object, Term classTerm, Iterable<Tuple> tuples,
            Iterable<Slot> slots);

    /**
     * Creates argument (tuple).
     * 
     * @param terms nonnull
     * @return argument (tuple)
     */
    public Tuple createTuple(Iterable<Term> terms);

    /**
     * Creates slot as a name value pair
     * 
     * @param name nonnull
     * @param value nonnull
     * @return slot as a name value pair
     */
    public Slot createSlot(Term name, Term value);

    /**
     * Creates typed literal constants.
     * 
     * @param literal nonnull
     * @param symspace nonnull
     * @return typed literal constants
     */
    public Const_Literal createConst_Literal(String literal, Symspace symspace);

    /**
     * Creates untyped constants.
     * 
     * @param name nonnull
     * @return untyped constants
     */
    public Const_Constshort createConst_Constshort(String name);

    /**
     * Creates a variable.
     * 
     * @param name can be null or "" for anonymous variables.
     * @return variable
     */
    public Var createVar(String name);
    /**
    * Creates external psoa atom.
    * 
    * @param externalexpr nonnull
    * @return external psoa atom
    */
    public External createExternalExpr(Psoa externalexpr);

    /**
     * Creates type of constants.
     * 
     * @param value nonnull
     * @return type of constant
     */
    public Symspace createSymspace(String value);
}