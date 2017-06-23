/**
 * This file is part of PSOARuleML-API.
 * <p/>
 * PSOARuleML-API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * PSOARuleML-API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with PSOARuleML-API.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ruleml.psoa.absyntax;

import java.util.*;
import org.ruleml.psoa.element.*;
/**
 * A straightforward implementation of the interfaces in AbstractSyntax that,
 * however, should be sufficiently good for most uses.
 * @author Mohammad Sadnan Al Manir
 * University of New Brunswick
 */
public class DefaultAbstractSyntax implements AbstractSyntax {

    private static String toStringIfNonNull(AbstractSyntax.Construct obj,
                                            String indent, String prefix, String trail) {
        return (obj == null) ? "" : (prefix + obj.toString(indent) + trail);
    }

    private static String iterableToStringIfNonEmpty(
            Iterable<? extends AbstractSyntax.Construct> iterable,
            String indent, String trail) {

        if (iterable == null)
            return "";

        String result = "";

        Iterator<? extends AbstractSyntax.Construct> it = iterable.iterator();

        AbstractSyntax.Construct obj = null;

        while (it.hasNext() && (obj == null))
            obj = it.next();

        if (obj == null)
            return "";

        result += toStringIfNonNull(obj, indent, "", "");

        while (it.hasNext()) {
            obj = it.next();
            if (obj != null)
                result += toStringIfNonNull(obj, indent, "\n", "");
        }

        return result + trail;

    } // iterableToStringIfNonEmpty(Iterable<? extends AbstractSyntax.Construct

    /**
     * Creates document.
     *
     * @param imports can be null or empty
     * @param group can be null
     * @return document
     */
    public AbstractSyntax.Document createDocument(Iterable<AbstractSyntax.Import> imports, AbstractSyntax.Group group) {
        return new Document(imports, group);
    }

    /**
     * Creates import.
     *
     * @param iri nonnull
     * @param profile can be null or empty
     * @return Document iri to import
     */

    public AbstractSyntax.Import createImport(String iri,
                                              AbstractSyntax.Profile profile) {
        return new Import(iri, profile);
    }

    /**
     * Creates Profile.
     *
     * @param iri nonnull
     * @return Profile
     */

    public AbstractSyntax.Profile createProfile(String iri) {
        return new Profile(iri);
    }

    /**
     * Creates Group.
     *
     * @param sentences collection of sentences
     * @return Group
     */

    public AbstractSyntax.Group createGroup(
            Iterable<AbstractSyntax.Sentence> sentences) {
        return new Group(sentences);
    }

    /**
     * Creates Rule.
     *
     * @param vars can be null or empty
     * @param matrix nonnull
     * @return Rule
     */
    public AbstractSyntax.Rule createRule(Iterable<AbstractSyntax.Var> vars,
                                          AbstractSyntax.Clause matrix) {
        return new Rule(vars, matrix);
    }

    /**
     * Creates Clause.
     *
     * @param implication nonnull
     * @return Clause
     */

    public AbstractSyntax.Clause createClause(AbstractSyntax.Implies implication) {
        return new Clause(implication);
    }

    /**
     * Creates Clause.
     * @param formula nonnull
     * @return Clause
     */

    public AbstractSyntax.Clause createClause(AbstractSyntax.Atomic formula) {
        return new Clause(formula);
    }

    /**
     * Creates Clause.
     *
     * @param head nonnull
     * @return Clause
     */

    public AbstractSyntax.Clause createClause(AbstractSyntax.Head head) {
        return new Clause(head);
    }

    /**
     * Creates Implies.
     * @param heads nonnull
     * @param condition can be null or empty
     * @return Implies
     */
    public AbstractSyntax.Implies createImplies(
            Iterable<AbstractSyntax.Head> heads,
            AbstractSyntax.Formula condition) {
        return new Implies(heads, condition);
    }

    /**
     * Creates a rule head by applying an existential quantifier over the given
     * variables to the formula.
     *
     * @param vars can be null or empty
     * @param matrix nonnull
     * @return conclusion
     */
    public AbstractSyntax.Head createHead(Iterable<AbstractSyntax.Var> vars,
                                          AbstractSyntax.Atomic matrix) {
        return new Head(vars, matrix);
    }

    /**
     * Creates conjunction of formulas.
     *
     * @param formulas nonnull
     * @return conjunction of formulas
     */
    public AbstractSyntax.Formula_And createFormula_And(
            Iterable<AbstractSyntax.Formula> formulas) {
        return new Formula_And(formulas);
    }

    /**
     * Creates disjunction of formulas.
     *
     * @param formulas nonnull
     * @return disjunction of formulas
     */
    public AbstractSyntax.Formula_Or createFormula_Or(
            Iterable<AbstractSyntax.Formula> formulas) {
        return new Formula_Or(formulas);
    }

    /**
     * Creates existentially quantified formula.
     *
     * @param vars nonnull
     * @param matrix nonnull
     * @return existentially quantified formula
     */
    public AbstractSyntax.Formula_Exists createFormula_Exists(
            Iterable<AbstractSyntax.Var> vars, AbstractSyntax.Formula matrix) {
        return new Formula_Exists(vars, matrix);
    }

    /**
     * Creates external atomic formula.
     *
     * @param atom nonnull
     * @return external atomic formula
     */

    public AbstractSyntax.Formula_External createFormula_External(
            AbstractSyntax.Atom atom) {
        return new Formula_External(atom);
    }

    /**
     * Creates an atomic formula.
     *
     * @param term nonnull
     * @return atomic formula
     */

    public AbstractSyntax.Atom createAtom(AbstractSyntax.Psoa term) {
        return new Atom(term);
    }

    /**
     * Creates equality expression.
     *
     * @param lhs nonnull
     * @param rhs nonnull
     * @return equality expression
     */

    public AbstractSyntax.Equal createEqual(AbstractSyntax.Term lhs,
                                            AbstractSyntax.Term rhs) {
        return new Equal(lhs, rhs);
    }

    /**
     * Creates subclass expression.
     *
     * @param sub nonnull
     * @param sup nonnull
     * @return subclass expression
     */

    public AbstractSyntax.Subclass createSubclass(AbstractSyntax.Term sub,
                                                  AbstractSyntax.Term sup) {
        return new Subclass(sub, sup);
    }

    /**
     * Creates a psoa term.
     *
     * @param object can be null or empty
     * @param classTerm nonnull
     * @param tuples can be null or empty
     * @param slots can be null or empty
     * @return psoa term
     */
    public AbstractSyntax.Psoa createPsoa(AbstractSyntax.Term object,
                                          AbstractSyntax.Term classTerm,
                                          Iterable<AbstractSyntax.Tuple> tuples,
                                          Iterable<AbstractSyntax.Slot> slots) {
        return new Psoa(object, classTerm, tuples, slots);
    }

    /**
     * Creates argument (tuple).
     *
     * @param terms nonnull
     * @return argument (tuple)
     */
    //public AbstractSyntax.Tuple createTuple(Iterable<AbstractSyntax.Term> terms, boolean dependency) {
    //    return new Tuple(terms, dependency);
    //}
    public AbstractSyntax.Tuple createTuple(Iterable<AbstractSyntax.Term> terms, TupleType tupleType) {
        return new Tuple(terms, tupleType);
    }
    /**
     * Creates slot as a name value pair
     *
     * @param name nonnull
     * @param value nonnull
     * @return slot as a name value pair
     */

    public AbstractSyntax.Slot createSlot(AbstractSyntax.Term name,
                                          AbstractSyntax.Term value, SlotType slotType) {
        return new Slot(name, value, slotType);
    }

    /**
     * Creates typed literal constants.
     *
     * @param literal nonnull
     * @param symspace nonnull
     * @return typed literal constants
     */

    public AbstractSyntax.Const_Literal createConst_Literal(String literal,
                                                            AbstractSyntax.Symspace symspace) {
        return new Const_Literal(literal, symspace);
    }

    /**
     * Creates untyped constants.
     *
     * @param name nonnull
     * @return untyped constants
     */

    public AbstractSyntax.Const_Constshort createConst_Constshort(String name) {
        return new Const_Constshort(name);
    }

    /**
     * Creates a variable.
     *
     * @param name can be null or "" for anonymous variables.
     * @return variable
     */
    public AbstractSyntax.Var createVar(String name) {
        return new Var(name);
    }

    /**
     * Creates external psoa atom.
     *
     * @param externalexpr nonnull
     * @return external psoa atom
     */

    public AbstractSyntax.External createExternalExpr(
            AbstractSyntax.Psoa externalexpr) {
        return new External(externalexpr);
    }

    /**
     * Creates type of constants.
     *
     * @param value nonnull
     * @return type of constant
     */
    public AbstractSyntax.Symspace createSymspace(String value) {
        return new Symspace(value);
    }

    /**
     * Class Document.
     */

    public static class Document implements AbstractSyntax.Document {

        private Iterable<AbstractSyntax.Import> _imports;
        private AbstractSyntax.Group _group;

        public Document(Iterable<AbstractSyntax.Import> imports,
                        AbstractSyntax.Group group) {
            _imports = imports;
            _group = group;
        }

        /** @return possibly null */
        public AbstractSyntax.Group getGroup() {
            return _group;
        }

        /** @return possibly null or empty */
        public Iterable<AbstractSyntax.Import> getImports() {
            return _imports;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            if (_group == null) return indent + "\nDocument ()";
            return indent
                    + "\nDocument (\n"
                    + indent
                    + "\n"
                    + iterableToStringIfNonEmpty(_imports, indent, "\n"
                    + indent + "\n")
                    + toStringIfNonNull(_group, indent, "", "\n" + indent
                    + "\n") + ")";
        }

    } // class Document

    /**
     * Class Import.
     */

    public static class Import implements AbstractSyntax.Import {

        private String _iri;
        private AbstractSyntax.Profile _profile;

        public Import(String iri, AbstractSyntax.Profile profile) {
            _iri = iri;
            _profile = profile;
        }

        public String getIRI() {
            return _iri;
        }

        public AbstractSyntax.Profile getProfile() {
            return _profile;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {

            String result = indent + "Import(<" + _iri + ">";

            if (_profile != null)
                result += indent + toStringIfNonNull(_profile, indent, "", "") + ")";
            else
                result += toStringIfNonNull(_profile, "", "", "") + ")";

            return result;

        }


    } // class Import

    // createX methods:

    /**
     * Class Profile.
     */

    public static class Profile implements AbstractSyntax.Profile {

        private String _iri;

        public Profile(String iri) {
            _iri = iri;
        }

        public String getIRI() {
            return _iri;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return "<" + _iri + ">";
        }

    }

    /**
     * Class Group.
     */

    public static class Group extends Sentence implements
            AbstractSyntax.Group {

        private LinkedList<AbstractSyntax.Sentence> _groupElement;

        /**
         * @param groupElement
         *            may be null
         */
        public Group(
                Iterable<? extends AbstractSyntax.Sentence> groupElement) {
            _groupElement = new LinkedList<AbstractSyntax.Sentence>();
            if (groupElement != null)
                for (AbstractSyntax.Sentence grElement : groupElement)
                    _groupElement.addLast(grElement);
        }

        public Collection<? extends AbstractSyntax.Sentence> getSentence() {
            return _groupElement;
        }

        /**
         * Should not be called.
         *
         * @throws RuntimeException
         *             always
         */

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result = indent + "Group";
            if (_groupElement.isEmpty())
                return result + " ()";
            result += " (\n";
            for (AbstractSyntax.Sentence ge : _groupElement)
                result += indent + "\n" + ge.toString(indent + "  ") + "\n";
            result += "\n" + indent + ")";
            return result;
        }

    } // class Group

    /**
     * Class Sentence.
     */
    public static abstract class Sentence implements
            AbstractSyntax.Sentence {

        public AbstractSyntax.Rule asRule() {
            assert this instanceof AbstractSyntax.Rule;
            return (AbstractSyntax.Rule) this;
        }

        public AbstractSyntax.Group asGroup() {
            assert this instanceof AbstractSyntax.Group;
            return (AbstractSyntax.Group) this;
        }

        public abstract String toString();

        public abstract String toString(String indent);


    } // class Sentence

    /**
     * Class Rule.
     */

    public static class Rule extends Sentence implements
            AbstractSyntax.Rule {

        private LinkedList<AbstractSyntax.Var> _variables;
        private AbstractSyntax.Clause _clause;

        /**
         * @param variables
         *            may be null
         * @param matrix
         *            must be nonnull
         */
        public Rule(Iterable<AbstractSyntax.Var> variables,
                    AbstractSyntax.Clause matrix) {
            _variables = new LinkedList<AbstractSyntax.Var>();
            if (variables != null)
                for (AbstractSyntax.Var var : variables)
                    _variables.addLast(var);
            _clause = matrix;
        }

        public Collection<AbstractSyntax.Var> getVariables() {
            return _variables;
        }

        public AbstractSyntax.Clause getClause() {
            return _clause;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            Set<AbstractSyntax.Var> vars = new HashSet(_variables);
            if (!vars.isEmpty()) {
                String result = indent + "Forall ";
                for (AbstractSyntax.Var v : vars)
                    result += v.toString("") + " ";
                result += "\n" + indent + "(\n"
                        + getClause().toString(indent + "  ") + "\n" + indent
                        + ")";
                return result;
            } else
                return getClause().toString(indent);
        }

    } // class Rule

    /**
     * Class Clause.
     */

    public static class Clause implements AbstractSyntax.Clause {

        private Object _content;

        public Clause(AbstractSyntax.Implies implies) {
            assert implies != null;
            _content = implies;
        }

        public Clause(AbstractSyntax.Atomic atomic) {
            assert atomic != null;
            _content = atomic;
        }

        public Clause(AbstractSyntax.Head head) {
            assert head != null;
            _content = head;
        }

        public boolean isImplies() {
            return _content instanceof AbstractSyntax.Implies;
        }

        public AbstractSyntax.Implies asImplies() {
            assert isImplies();
            return (AbstractSyntax.Implies) _content;
        }

        public boolean isAtomic() {
            return _content instanceof AbstractSyntax.Atomic;
        }

        public AbstractSyntax.Atomic asAtomic() {
            assert isAtomic();
            return (AbstractSyntax.Atomic) _content;
        }

        public boolean isHead() {
            return _content instanceof AbstractSyntax.Head;
        }

        public AbstractSyntax.Head asHead() {
            assert isHead();
            return (AbstractSyntax.Head) _content;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            if (isImplies())
                return asImplies().toString(indent);

            else if (isAtomic())
                return asAtomic().toString(indent);

            else if (isHead())
                return asHead().toString(indent);
            else
                throw new Error("Error in Clause");
        }
    }

    /**
     * Class Implies.
     */

    public static class Implies implements AbstractSyntax.Implies {

        private LinkedList<AbstractSyntax.Head> _heads;
        private AbstractSyntax.Formula _body;

        public Implies(Iterable<? extends AbstractSyntax.Head> heads,
                       AbstractSyntax.Formula condition) {
            assert heads != null;
            assert heads.iterator().hasNext();
            _heads = new LinkedList<AbstractSyntax.Head>();
            for (AbstractSyntax.Head h : heads) {
                _heads.addLast(h);
            }
            _body = condition;
        }

        public Collection<? extends AbstractSyntax.Head> getHead() {
            return _heads;
        }

        public AbstractSyntax.Formula getBody() {
            return _body;
        }

        /** The set of all free variables. */
        public Set<AbstractSyntax.Var> variables() {
            TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
            if (_body != null)
                result.addAll(_body.variables());
            for (AbstractSyntax.Head h : _heads) {
                result.addAll(h.variables());
            }
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result = "";

            if (_heads.size() > 1) {
                result += indent + "And\n" + indent + " (\n";
                for (AbstractSyntax.Head h : _heads)
                    result += "\n" + indent + h.toString(indent + "  ");

                result += "\n" + indent + ")";
            } else {
                assert _heads.size() == 1;
                result += _heads.get(0).toString(indent);
            }

            if (_body != null) {
                result += "\n" + indent + "  :-\n" + _body.toString(indent + "    ");
            }

            return result;
        }

    } // class Implies

    /**
     * Class Atomic.
     */

    public static abstract class Atomic extends Formula implements
            AbstractSyntax.Atomic {

        public AbstractSyntax.Atom asAtom() {
            assert this instanceof AbstractSyntax.Atom;
            return (AbstractSyntax.Atom) this; // could be return
            // (AbstractSyntax.Atom)this;
        }

        public AbstractSyntax.Equal asEqual() {
            assert this instanceof AbstractSyntax.Equal;
            return (AbstractSyntax.Equal) this;
        }

        public AbstractSyntax.Subclass asSubclass() {
            assert this instanceof AbstractSyntax.Subclass;
            return (AbstractSyntax.Subclass) this;
        }

        public abstract Set<AbstractSyntax.Var> variables();

        public String toString() {
            return toString("");
        }

        public abstract String toString(String indent);

    }

    /**
     * Class Head.
     */

    public static class Head implements AbstractSyntax.Head {

        private LinkedList<AbstractSyntax.Var> _variables;
        private AbstractSyntax.Atomic _atomic;

        public Head(Iterable<AbstractSyntax.Var> variables,
                    AbstractSyntax.Atomic matrix) {
            _variables = new LinkedList<AbstractSyntax.Var>();
            if (variables != null) {
                for (AbstractSyntax.Var var : variables) {
                    _variables.addLast(var);
                }
            }

            _atomic = matrix;
        }

        public Collection<AbstractSyntax.Var> getVariables() {

            return _variables;
        }

        public AbstractSyntax.Atomic getAtomic() {
            return _atomic;
        }

        /** The set of all free variables in the head. */
        public Set<AbstractSyntax.Var> variables() {
            Set<AbstractSyntax.Var> result = _atomic.variables();
            result.removeAll(_variables);
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result;
            if (_variables.size() > 0) {
                result = indent + "Exists ";
                for (AbstractSyntax.Var v : _variables)
                    result += v.toString("") + " ";

                result += "\n" + indent + "(" + "\n" + indent + "  "
                        + getAtomic().toString("") + "\n"
                        + indent + ")";
                return result;
            }
            result = indent + getAtomic().toString(" ");
            return result;
        }


    } // class Head

    /**
     * Class Formula.
     */
    public static abstract class Formula implements AbstractSyntax.Formula {

        public AbstractSyntax.Formula_And asAndFormula() {
            assert this instanceof AbstractSyntax.Formula_And;
            return (AbstractSyntax.Formula_And) this;
        }

        public AbstractSyntax.Formula_Or asOrFormula() {
            assert this instanceof AbstractSyntax.Formula_Or;
            return (AbstractSyntax.Formula_Or) this;
        }

        public AbstractSyntax.Formula_Exists asExistsFormula() {
            assert this instanceof AbstractSyntax.Formula_Exists;
            return (AbstractSyntax.Formula_Exists) this;
        }

        public AbstractSyntax.Atomic asAtomic() {
            assert this instanceof AbstractSyntax.Atomic;
            return (AbstractSyntax.Atomic) this;
        }

        public AbstractSyntax.Formula_External asExternalFormula() {
            assert this instanceof AbstractSyntax.Formula_External;
            return (AbstractSyntax.Formula_External) this;
        }

        /** The set of all free variables in the formula. */
        public abstract Set<AbstractSyntax.Var> variables();

        public String toString() {
            return toString("");
        }

        public abstract String toString(String indent);

    } // class Formula

    /**
     * Class Formula_And.
     */

    public static class Formula_And extends Formula implements
            AbstractSyntax.Formula_And {

        private LinkedList<AbstractSyntax.Formula> _formulas;

        /**
         * @param formulas
         *            can be null or an empty sequence
         */
        public Formula_And(Iterable<? extends AbstractSyntax.Formula> formulas) {
            _formulas = new LinkedList<AbstractSyntax.Formula>();
            if (formulas != null)
                for (AbstractSyntax.Formula form : formulas)
                    _formulas.addLast(form);
        }

        public Collection<? extends AbstractSyntax.Formula> getFormulas() {
            return _formulas;
        }

        public Set<AbstractSyntax.Var> variables() {
            TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
            for (AbstractSyntax.Formula f : _formulas)
                result.addAll(f.variables());
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result = indent + "And\n" + indent + "(";

            for (AbstractSyntax.Formula f : _formulas)
                result += "\n" + f.toString(indent + "  ");
            result += "\n" + indent + ")";
            return result;
        }

    } // class Formula_And

    /**
     * Class Formula_Or.
     */

    public static class Formula_Or extends Formula implements
            AbstractSyntax.Formula_Or {

        private LinkedList<AbstractSyntax.Formula> _formulas;

        /**
         * @param formulas
         *            can be null or an empty sequence
         */
        public Formula_Or(Iterable<? extends AbstractSyntax.Formula> formulas) {
            _formulas = new LinkedList<AbstractSyntax.Formula>();
            if (formulas != null)
                for (AbstractSyntax.Formula form : formulas)
                    _formulas.addLast(form);
        }

        public Group asGroup() {
            throw new RuntimeException(
                    "asGroup() should never be called in a Rule.");
        }

        public Collection<? extends AbstractSyntax.Formula> getFormulas() {
            return _formulas;
        }

        public Set<AbstractSyntax.Var> variables() {
            TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
            for (AbstractSyntax.Formula f : _formulas)
                result.addAll(f.variables());
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result = indent + "Or\n" + indent + "(";

            for (AbstractSyntax.Formula f : _formulas)
                result += "\n" + indent + f.toString(indent + "  ");
            result += "\n" + indent + ")";
            return result;
        }

    } // class Formula_Exists

    /**
     * Class Formula_Exists.
     */

    public static class Formula_Exists extends Formula implements
            AbstractSyntax.Formula_Exists {

        private LinkedList<AbstractSyntax.Var> _variables;
        private AbstractSyntax.Formula _formula;

        public Formula_Exists(Iterable<AbstractSyntax.Var> variables,
                              AbstractSyntax.Formula matrix) {
            assert variables != null;
            assert variables.iterator().hasNext();
            _variables = new LinkedList<AbstractSyntax.Var>();
            for (AbstractSyntax.Var var : variables)
                _variables.addLast(var);
            _formula = matrix;
        }

        /** @return non-empty sequence */
        public Collection<AbstractSyntax.Var> getVariables() {
            return _variables;
        }

        public AbstractSyntax.Formula getFormula() {
            return _formula;
        }

        /** The set of all free variables in the formula. */
        public Set<AbstractSyntax.Var> variables() {
            Set<AbstractSyntax.Var> result = _formula.variables();
            result.removeAll(_variables);
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result = indent + "Exists ";
            for (AbstractSyntax.Var v : _variables)
                result += v.toString("") + " ";
            result += "\n" + indent + "(" + "\n" + indent + "  " + _formula.toString("")
                    + indent + "\n" + indent + ")";
            return result;
        }

    } // class Formula_Exists

    /**
     * Class Formula_External.
     */

    public static class Formula_External extends Formula implements
            AbstractSyntax.Formula_External {

        private AbstractSyntax.Atom _atom;

        public Formula_External(AbstractSyntax.Atom atom) {
            _atom = atom;
        }

        public AbstractSyntax.Atom getContent() {
            return _atom;
        }

        public Set<AbstractSyntax.Var> variables() {
            return _atom.variables();
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return indent + "External( " + _atom.toString(indent) + ")";
        }

    } // class Formula_External

    /**
     * Class Atom.
     */

    public static class Atom extends Atomic implements AbstractSyntax.Atom {

        private AbstractSyntax.Psoa _term;

        public Atom(AbstractSyntax.Psoa term) {
            _term = term;
        }

        public AbstractSyntax.Psoa getPsoa() {
            return _term;
        }

        public Set<AbstractSyntax.Var> variables() {
            return _term.variables();
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return _term.toString(indent);
        }


    } // class Atom

    /**
     * Class Equal.
     */

    public static class Equal extends Atomic implements AbstractSyntax.Equal {

        private AbstractSyntax.Term _lhs;
        private AbstractSyntax.Term _rhs;

        /**
         * @param lhs
         *            must be nonnull
         * @param rhs
         *            must be nonnull
         */
        public Equal(AbstractSyntax.Term lhs, AbstractSyntax.Term rhs) {
            assert lhs != null;
            assert rhs != null;
            _lhs = lhs;
            _rhs = rhs;
        }

        public AbstractSyntax.Term getLeft() {
            return _lhs;
        }

        public AbstractSyntax.Term getRight() {
            return _rhs;
        }

        public Set<AbstractSyntax.Var> variables() {
            Set<AbstractSyntax.Var> result = _lhs.variables();
            result.addAll(_rhs.variables());
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return indent + _lhs.toString("") + " = " + _rhs.toString("");
        }


    } // class Equal

    /**
     * Class  Subclass.
     */

    public static class Subclass extends Atomic implements
            AbstractSyntax.Subclass {

        private AbstractSyntax.Term _sub;
        private AbstractSyntax.Term _sup;

        /**
         * @param sub
         *            must be nonnull
         * @param sup
         *            must be nonnull
         */
        public Subclass(AbstractSyntax.Term sub, AbstractSyntax.Term sup) {
            assert sub != null;
            assert sup != null;
            _sub = sub;
            _sup = sup;
        }

        public AbstractSyntax.Term getSubclass() {
            return _sub;
        }

        public AbstractSyntax.Term getSuperclass() {
            return _sup;
        }

        public Set<AbstractSyntax.Var> variables() {
            Set<AbstractSyntax.Var> result = _sub.variables();
            result.addAll(_sup.variables());
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            String result;
            result = indent + _sub.toString("") + "##"
                    + _sup.toString("");
            return result;
        }

        @Override
        public org.ruleml.psoa.absyntax.AbstractSyntax.Atom asAtom() {
            // TODO Auto-generated method stub
            return null;
        }

    } // class Subclass

    /**
     * Class Psoa.
     */

    public static class Psoa extends Expr implements AbstractSyntax.Psoa {

        private AbstractSyntax.Term _object;
        private AbstractSyntax.Term _classTerm;
        private LinkedList<AbstractSyntax.Tuple> _tuples;
        private LinkedList<AbstractSyntax.Slot> _slots;

        public Psoa(AbstractSyntax.Term object, AbstractSyntax.Term classTerm,
                    Iterable<AbstractSyntax.Tuple> tuples,
                    Iterable<AbstractSyntax.Slot> slots) {
            //assert object != null;
            assert classTerm != null;


            _object = object;
            _classTerm = classTerm;

            _tuples = new LinkedList<AbstractSyntax.Tuple>();
            if (tuples != null)
                for (AbstractSyntax.Tuple tup : tuples)
                    _tuples.addLast(tup);

            _slots = new LinkedList<AbstractSyntax.Slot>();
            if (slots != null)
                for (AbstractSyntax.Slot slot : slots)
                    _slots.addLast(slot);

        }

        public AbstractSyntax.Term getInstance() {
            return _object;
        }

        public AbstractSyntax.Term getClassExpr() {
            return _classTerm;
        }

        public Collection<? extends AbstractSyntax.Tuple> getPositionalAtom() {
            return _tuples;
        }

        public Collection<? extends AbstractSyntax.Slot> getSlottedAtom() {
            return _slots;
        }

        public Set<AbstractSyntax.Var> variables() {
            HashSet<AbstractSyntax.Var> result = new HashSet<AbstractSyntax.Var>();
            if (_object != null)
                result.addAll(_object.variables());
            result.addAll(_classTerm.variables());

            if (_tuples != null)
                for (AbstractSyntax.Tuple tp : _tuples)
                    result.addAll(tp.variables());

            if (_slots != null)
                for (AbstractSyntax.Slot sl : _slots)
                    result.addAll(sl.variables());
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {

            String result;

            if (_object == null)
                result = indent + _classTerm.toString("") + "(";
            else
                result = indent + _object.toString("") + "#"
                        + _classTerm.toString("") + "(";

            int numOfTuples = _tuples.size();

            /*
            if (_tuples.size() > 1) {
                for (AbstractSyntax.Tuple tuple : _tuples) {
                    result += numOfTuples > 1 ? "[" + tuple.toString("") + "] " : "[" + tuple.toString("") + "]";
                    numOfTuples--;
                }
            }

            if (_tuples.size() == 1) {
                for (AbstractSyntax.Tuple tuple : _tuples) {
                    result += tuple.toString("");
                }
            }
            */

            if (_tuples.size() > 0){
                for (AbstractSyntax.Tuple tuple : _tuples) {
                    result += numOfTuples > 1 ? tuple.toString("") + " " : tuple.toString("");
                    numOfTuples--;
                }
            }

            // add a space between the tuples and slots
            if (_tuples.size() > 0 && _slots.size() > 0)
                result += " ";

            for (AbstractSyntax.Slot slot : _slots) {
                if (!slot.equals(_slots.getLast()))
                    result += slot.toString("") + " ";
                else
                    result += slot.toString("");

            }
            return result + ")";
        }

        @Override
        public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {
            // TODO Auto-generated method stub
            return 0;
        }

    } // class Psoa

    /**
     * Class Slot.
     */

    public static class Slot implements AbstractSyntax.Slot {

        private AbstractSyntax.Term _name;
        private AbstractSyntax.Term _value;
        private SlotType _slotType;

        public Slot(AbstractSyntax.Term name, AbstractSyntax.Term value, SlotType slotType) {
            assert name != null;
            assert value != null;
            _name = name;
            _value = value;
            _slotType = slotType;
        }

        public AbstractSyntax.Term getName() {
            return _name;
        }

        public AbstractSyntax.Term getValue() {
            return _value;
        }

        public Set<AbstractSyntax.Var> variables() {
            Set<AbstractSyntax.Var> result = _name.variables();
            result.addAll(_value.variables());
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {

            String result = "";

            if (_slotType.equals(SlotType.DEPENDENT)){

                result = _name.toString("") + "+>"
                        + _value.toString("");
            }
            else if (_slotType.equals(SlotType.INDEPENDENT)){

                result = _name.toString("") + "->"
                        + _value.toString("");
            }

            return result;
        }

    } // class Slot

    /**
     * Class Tuple.
     */

    public static class Tuple implements AbstractSyntax.Tuple {

        private LinkedList<AbstractSyntax.Term> _terms;
        private TupleType tupleType;

        public Tuple(Iterable<? extends AbstractSyntax.Term> terms, TupleType tupleType) {
            _terms = new LinkedList<AbstractSyntax.Term>();
            if (terms != null)
                for (AbstractSyntax.Term term : terms)
                    _terms.addLast(term);
            this.tupleType = tupleType;
        }

        public Collection<? extends AbstractSyntax.Term> getArguments() {
            return _terms;
        }

        public Set<AbstractSyntax.Var> variables() {
            TreeSet<AbstractSyntax.Var> resultVars = new TreeSet<AbstractSyntax.Var>();
            for (AbstractSyntax.Term tm : _terms)
                resultVars.addAll(tm.variables());
            return resultVars;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {

            //if(_terms.size() > 1){
            String result = "";

            if (tupleType.equals(tupleType.DEPENDENT))
                result += "+" + "[";
            else if (tupleType.equals(tupleType.INDEPENDENT))
                result += "-" + "[";

            // tupleType.ARGS implicitly implemented here, wrapped around Tuple
            for (AbstractSyntax.Term t : _terms) {
                // add space in between elements until the last element is printed
                if (!t.equals(_terms.getLast()))
                    result += t.toString("") + " ";
                else
                    result += t.toString("");
            }

            if (tupleType.equals(tupleType.DEPENDENT))
                result += indent + "]";
            else if (tupleType.equals(tupleType.INDEPENDENT))
                result += indent + "]";

            return result;
        }

    } // class Tuple.

    /**
     * Class Term.
     */

    public static abstract class Term implements AbstractSyntax.Term {

        public AbstractSyntax.Const asConst() {
            assert this instanceof AbstractSyntax.Const;
            return (AbstractSyntax.Const) this;
        }

        public AbstractSyntax.Var asVar() {
            assert this instanceof AbstractSyntax.Var;
            return (AbstractSyntax.Var) this;
        }

        public AbstractSyntax.Psoa asPsoa() {
            assert this instanceof AbstractSyntax.Psoa;
            return (AbstractSyntax.Psoa) this;
        }

        public AbstractSyntax.External asExternal() {
            assert this instanceof AbstractSyntax.External;
            return (AbstractSyntax.External) this;
        }

        public abstract int compareTo(AbstractSyntax.Term t);

        public abstract Set<AbstractSyntax.Var> variables();

        public String toString() {
            return toString("");
        }

        public abstract String toString(String indent);


    } // class Term

    /**
     * Class Const.
     */

    public static abstract class Const extends Term implements
            AbstractSyntax.Const {

        public AbstractSyntax.Const_Literal asConstLiteral() {
            assert this instanceof AbstractSyntax.Const_Literal;
            return (AbstractSyntax.Const_Literal) this;
        }

        public AbstractSyntax.Const_Constshort asConstshort() {
            assert this instanceof AbstractSyntax.Const_Constshort;
            return (AbstractSyntax.Const_Constshort) this;
        }

        public abstract Set<AbstractSyntax.Var> variables();

    } // class Const

    /**
     * Class Const_Literal.
     */

    public static class Const_Literal extends Const implements
            AbstractSyntax.Const_Literal {

        private String _literal;
        private AbstractSyntax.Symspace _symspace;

        public Const_Literal(String literal, AbstractSyntax.Symspace symspace) {
            assert literal != null; // is this right here without implementing
            // Const interface !!
            _literal = literal;
            _symspace = symspace;
        }

        public String getLiteral() {
            return _literal;
        }

        public AbstractSyntax.Symspace getSymspace() {
            return _symspace;
        }

        public Set<AbstractSyntax.Var> variables() {
            return new TreeSet<AbstractSyntax.Var>(); // not sure about it
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return _symspace.toString().equals("xs:string") ? indent + "\"" + _literal + "\"" : indent + "\"" + _literal + "\"" + "^^"
                    + _symspace.toString(indent);
        }

        @Override
        public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {
            // TODO Auto-generated method stub
            return 0;
        }

    } // class Const_Literal

    /**
     * Class Symspace.
     */

    public static class Symspace implements AbstractSyntax.Symspace {

        private String _value;

        public Symspace(String value) {
            _value = value;
        }

        public String getValue() {
            return _value;
        }

        public Set<AbstractSyntax.Var> variables() {
            return new TreeSet<AbstractSyntax.Var>();
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return indent + _value;
        }

    } // class Symspace.

    /**
     * Class Const_Constshort.
     */

    public static class Const_Constshort extends Const implements
            AbstractSyntax.Const_Constshort {

        private String _name;

        public Const_Constshort(String name) {
            _name = name;
        }

        public String getConstshort() {
            return _name;
        }

        public Set<AbstractSyntax.Var> variables() {
            return new TreeSet<AbstractSyntax.Var>(); // not sure about it
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return indent + "_" + _name;
        }
        // adding the type
        //private String _symspace;

        @Override
        public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {
            // TODO Auto-generated method stub
            return 0;
        }

    } // class Const_Constshort

    /**
     * Class Var.
     */

    public static class Var extends Term implements AbstractSyntax.Var {

        private String _name;

        public Var(String name) {
            assert name != null;
            _name = name;
        }

        public String getName() {
            return _name;
        }

        public int compareTo(AbstractSyntax.Term t) {
            if (t instanceof AbstractSyntax.Const) {
                return 1;
            } else if (t instanceof AbstractSyntax.Var) {
                return getName().compareTo(t.asVar().getName());
            } else
                return -1;
        }

        public Set<AbstractSyntax.Var> variables() {
            TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
            result.add(this);
            return result;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return indent + "?" + _name;
        }

    } // class Var

    // Auxilliary methods:

    /**
     * Class Expr.
     */

    public static abstract class Expr extends Term implements
            org.ruleml.psoa.absyntax.AbstractSyntax.Expr {

        public AbstractSyntax.Psoa asPsoa() {
            assert this instanceof AbstractSyntax.Psoa;
            return (AbstractSyntax.Psoa) this;
        }

        public abstract Set<AbstractSyntax.Var> variables();

    } // class Expr

    /**
     * Class External.
     */

    public static class External extends Term implements
            AbstractSyntax.External {

        private AbstractSyntax.Psoa _externalexpr;

        public External(AbstractSyntax.Psoa externalexpr) {
            _externalexpr = externalexpr;
        }

        public AbstractSyntax.Psoa getExternalExpr() {
            return _externalexpr;
        }

        public Set<AbstractSyntax.Var> variables() {
            return _externalexpr.variables();
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            return indent + "External(" + _externalexpr.toString("") + ")";
        }

        @Override
        public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {
            // TODO Auto-generated method stub
            return 0;
        }


    } // class External


} // class DefaultAbstractSyntax